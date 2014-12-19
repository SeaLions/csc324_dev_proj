import java.io.PrintWriter;
import java.io.File;
import java.sql.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.text.Format;
import java.text.SimpleDateFormat;
  
public class Comparison
{
   //compare constants
   private final int SAME = 1;
   private final int ONLY_RFPS = 2;
   private final int ONLY_SIGPRO = 3;
   private final int INCONCLUSIVE = 4;
   
   //KML strings
   private String sameString;
   private String diffRFPSString;
   private String diffSignalProString;
   private String inconclusiveString;
   
   
   private UserInput userInput;
   private RfpsData rfpsData;
   private SignalProData signalProData;
   private double[][][] afterCompare=new double[360][1000][3];
   Vector<Vector<String>> rfps= new Vector<Vector<String>>();
   double[][] sigpro = new double[100][100];
   
   public Comparison(UserInput ui)
   {
      this.userInput = ui;
      
      this.rfpsData = new RfpsData();
      rfpsData.readData(ui.getRfpsFile());
      this.signalProData = new SignalProData();
      signalProData.readData(ui.getSignalProFile());
      
      //initialize the kml strings
      sameString = "";
      diffRFPSString = "";
      diffSignalProString = "";
      inconclusiveString = "";
      
      compare();
      
      createKmlOutputFile();
   }
   
      //--------------------------------------------------------------------------------------------------------------------------------------- change zone open
   
   private void compare()
   {
      int gridSpacingDistance = 500;//in meters
		float westBound = signalProData.getWestBound(),
            eastBound = signalProData.getEastBound(),
            northBound = signalProData.getNorthBound(),
            southBound = signalProData.getSouthBound();
		Coordinate gridPoint = new Coordinate(southBound,westBound);
		int row = 0;
		while (northBound - gridPoint.getLatitude() > 0) {
			gridPoint.setLongitude(westBound);
			while (eastBound - gridPoint.getLongitude() > 0) {
            boolean rfpsCoversPoint = rfpsData.isCoverageNear(gridPoint, gridSpacingDistance/2);
            boolean signalProCoversPoint = signalProData.isCoverageNear(gridPoint, gridSpacingDistance/2);
            
            int coverageType;
            if (signalProCoversPoint ^ rfpsCoversPoint) {
               if (signalProCoversPoint)
                  coverageType = ONLY_SIGPRO;
               else
                  coverageType = ONLY_RFPS;
            }
            else coverageType = SAME;
            
            gridPoint.setCoverage(coverageType);
            addKMLPoint(gridPoint);
            
				gridPoint = CoordinateManager.addDistanceEast(gridPoint.getLatitude(), gridPoint.getLongitude(), gridSpacingDistance);
			}
			gridPoint = CoordinateManager.addDistanceNorth(gridPoint.getLatitude(), gridPoint.getLongitude(), gridSpacingDistance);
			++row;
		}
   }
   //calculate new x
   private double metersXcalc(double angle, double distance)
   {
      angle=90-angle;
      return distance*Math.sin(angle);
   }
   //calculate new y
   private double metersYcalc(double angle, double distance)
   {
      return 100*Math.sin(angle);
   }
   //find angle
   public double Fangle(double Opposite,double Adjacent)
   {
      return Math.asin(Opposite/Adjacent);
   }
   //parse the current string into 4 doubles and returns the array
   private double[][] parseString(String current)
   {
      double Tlat1=Double.parseDouble( current.substring(0 , current.indexOf(',') ) );
      current=current.substring( current.indexOf(',')+1,current.length() );
      double Tlon1=Double.parseDouble( current.substring(0 , current.indexOf(',') ) );
      current=current.substring( current.indexOf(',')+1,current.length() );
      current=current.substring( current.indexOf(' ')+1,current.length() );
      double Tlat2=Double.parseDouble( current.substring(0 , current.indexOf(',') ) );
      current=current.substring( current.indexOf(',')+1,current.length() );
      double Tlon2=Double.parseDouble( current.substring(0 , current.indexOf(',') ) );
      double[][] endPTS = new double [2][2];
      endPTS[0][0]=Tlat1;
      endPTS[0][1]=Tlat2;
      endPTS[1][0]=Tlon1;
      endPTS[1][1]=Tlon2;
      return endPTS;
   }
   private double[] getPoint(double lat, double lon,double Dlat, double Dlon,double angle, double distance,int iteration)
   {
      double[] arr= new double[2];
      arr[0]=lat+Dlat;
      arr[1]=lon+Dlon;
      return arr;
   }
   //calculates distance between two gps coords
   private double distance(double lat1, double lon1, double lat2, double lon2, char unit)
   {
      double theta = lon1 - lon2;
      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
      dist = Math.acos(dist);
	   dist = rad2deg(dist);
      dist = dist * 60 * 1.1515;
      if (unit == 'K')
      {
         dist = dist * 1.609344;
      }
      else if (unit == 'N')
      {
         dist = dist * 0.8684;
      }
      return (dist);
   }
   //converts degrees to radians
   private double deg2rad(double deg)
   {
      return (deg * Math.PI / 180.0);
   }
   //converts radians to degrees
   private double rad2deg(double rad)
   {
      return (rad * 180 / Math.PI);
   }
   
      //--------------------------------------------------------------------------------------------------------------------------------------- change zone closed


   public void addKMLPoint(Coordinate newCoord){
      switch (newCoord.getCoverage()) {
         case SAME:
            //coverage matches in signal pro and RFPS
            sameString+= "\t\t\t<Placemark>\n"+
                     //"\t\t\t\t<name>"+newCoord.getLatitude()+","+newCoord.getLongitude()+"</name>\n"+
                     "\t\t\t\t<description>"+newCoord.getLatitude()+","+newCoord.getLongitude()+"</description>\n"+
                     "\t\t\t\t<styleUrl>#stylesel_SAME</styleUrl>\n"+
                     "\t\t\t\t<Point>\n"+
                        "\t\t\t\t\t<coordinates>"+newCoord.getLongitude()+","+newCoord.getLatitude()+",0 </coordinates>\n"+
                     "\t\t\t\t</Point>\n"+
                  "\t\t\t</Placemark>\n";
            break;
         case ONLY_RFPS:
            //the coverage only in RFPS
   			diffRFPSString+= "\t\t\t<Placemark>\n"+
                     //"\t\t\t\t<name>"+newCoord.getLatitude()+","+newCoord.getLongitude()+"</name>\n"+
                     "\t\t\t\t<description>"+newCoord.getLatitude()+","+newCoord.getLongitude()+"</description>\n"+
                     "\t\t\t\t<styleUrl>#stylesel_ONLY_RFPS</styleUrl>\n"+
                     "\t\t\t\t<Point>\n"+
                        "\t\t\t\t\t<coordinates>"+newCoord.getLongitude()+","+newCoord.getLatitude()+",0 </coordinates>\n"+
                     "\t\t\t\t</Point>\n"+
                  "\t\t\t</Placemark>\n";				
            break;
         case ONLY_SIGPRO:
            //the coverage only in signal pro
            diffSignalProString+= "\t\t\t<Placemark>\n"+
                      //"\t\t\t\t<name>"+newCoord.getLatitude()+","+newCoord.getLongitude()+"</name>\n"+
                      "\t\t\t\t<description>"+newCoord.getLatitude()+","+newCoord.getLongitude()+"</description>\n"+
                      "\t\t\t\t<styleUrl>#stylesel_ONLY_SIGPRO</styleUrl>\n"+
                      "\t\t\t\t<Point>\n"+
                        "\t\t\t\t\t<coordinates>"+newCoord.getLongitude()+","+newCoord.getLatitude()+",0 </coordinates>\n"+
                      "\t\t\t\t</Point>\n"+
                   "\t\t\t</Placemark>\n";
            break;
         case INCONCLUSIVE:
            //the coverage inconclusive
            inconclusiveString+= "\t\t\t<Placemark>\n"+
                     //"\t\t\t\t<name>"+newCoord.getLatitude()+","+newCoord.getLongitude()+"</name>\n"+
                     "\t\t\t\t<description>"+newCoord.getLatitude()+","+newCoord.getLongitude()+"</description>\n"+
                     "\t\t\t\t<styleUrl>#stylesel_INCON</styleUrl>\n"+
                     "\t\t\t\t<Point>\n"+
                        "\t\t\t\t\t<coordinates>"+newCoord.getLongitude()+","+newCoord.getLatitude()+",0 </coordinates>\n"+
                     "\t\t\t\t</Point>\n"+
                   "\t\t\t</Placemark>\n";
            break;
         default:
            //do nothing
            break;
       }//end switch
   }//end addKMLPoint()
   
  
   public boolean createKmlOutputFile()
   {
      
      //string that will hold the initial .kml file
      String kmlString = "";
      
      String kmlBeginTag = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"; //begin tag
		String kmlTypeTag = "<kml xmlns=\"http://earth.google.com/kml/2.0\">\n"; //begin kml tag
		String kmlEndTag = "</kml>"; //end kml tag
      
      String docStartTag = "\t<Document>\n";
      String docEndTag = "\t</Document>\n";
      String folderStartTag = "\t\t<Folder>\n";
      String folderEndTag = "\t\t</Folder>\n";
      
      //Formating points with similar singal coverage for kml
      String finalSameString = folderStartTag+
                   "\t\t\t<Style id=\"stylesel_SAME\">\n"+
                     "\t\t\t\t<IconStyle>\n"+
                        "\t\t\t\t\t<color>FF00FF00</color>\n"+ //green
                        "\t\t\t\t\t<colorMode>normal</colorMode>\n"+
                        "\t\t\t\t\t<scale>0.25</scale>\n"+ // scale of 1 is normalized to 32x32 pixels
								"\t\t\t\t\t<Icon>\n"+
									//<href>http://maps.google.com/mapfiles/kml/pal3/icon21.png</href>
					            "\t\t\t\t\t\t<href>green_point_small.jpg</href>\n"+
								"\t\t\t\t\t</Icon>\n"+
                     "\t\t\t\t</IconStyle>\n"+
                   "\t\t\t</Style>\n"+
                   "\t\t\t<name>Similar Coverage</name>\n"+
                   "\t\t\t<description>Signal Coverage is the same.</description>\n"+
                   sameString+
                   folderEndTag;

      //Formating points where only RFPS had singal coverage for kml
      String finalDiffRFPSString = folderStartTag+
                   "\t\t\t<Style id=\"stylesel_ONLY_RFPS\">\n"+
                     "\t\t\t\t<IconStyle>\n"+
                        "\t\t\t\t\t<color>FF3366FF</color>\n"+ //blue
                        "\t\t\t\t\t<colorMode>normal</colorMode>\n"+
                        "\t\t\t\t\t<scale>0.25</scale>\n"+ // scale of 1 is normalized to 32x32 pixels
								"\t\t\t\t\t<Icon>\n"+
									//<href>http://maps.google.com/mapfiles/kml/pal3/icon21.png</href>
					            "\t\t\t\t\t\t<href>blue_point_small.jpg</href>\n"+
								"\t\t\t\t\t</Icon>\n"+
                     "\t\t\t\t</IconStyle>\n"+
                   "\t\t\t</Style>\n"+
                   "\t\t\t<name>Similar Coverage</name>\n"+
                   "\t\t\t<description>Signal Coverage is the same.</description>\n"+
                   "\t\t\t<name>Difference in Coverage</name>\n"+
                   "\t\t\t<description>Signal Coverage only in RFPS.</description>\n"+
                   diffRFPSString+
                   folderEndTag;

      //Formating points where only SignalPro had singal coverage for kml
      String finalDiffSignalProString = folderStartTag+
                   "\t\t\t<Style id=\"stylesel_ONLY_SIGPRO\">\n"+
                     "\t\t\t\t<IconStyle>\n"+
                        "\t\t\t\t\t<color>FF0000FF</color>\n"+ //red
                        "\t\t\t\t\t<colorMode>normal</colorMode>\n"+
                        "\t\t\t\t\t<scale>0.25</scale>\n"+ // scale of 1 is normalized to 32x32 pixels
								"\t\t\t\t\t<Icon>\n"+
									//<href>http://maps.google.com/mapfiles/kml/pal3/icon21.png</href>
					            "\t\t\t\t\t\t<href>orange_point_small.jpg</href>\n"+
								"\t\t\t\t\t</Icon>\n"+
                     "\t\t\t\t</IconStyle>\n"+
                   "\t\t\t</Style>\n"+
                   "\t\t\t<name>Difference in Coverage</name>\n"+
                   "\t\t\t<description>Signal Coverage only in SignalPro.</description>\n"+
                   diffSignalProString+
                   folderEndTag;

      //Formating points with inconclusive singal coverage for kml
      String finalInconclusiveString = folderStartTag+
                   "\t\t\t<Style id=\"stylesel_INCON\">\n"+
                     "\t\t\t\t<IconStyle>\n"+
                        "\t\t\t\t\t<color>FFFFFF00</color>\n"+ //yellow
                        "\t\t\t\t\t<colorMode>normal</colorMode>\n"+
                        "\t\t\t\t\t<scale>0.25</scale>\n"+ // scale of 1 is normalized to 32x32 pixels
								"\t\t\t\t\t<Icon>\n"+
									//<href>http://maps.google.com/mapfiles/kml/pal3/icon21.png</href>
					            "\t\t\t\t\t\t<href>red_point_small.jpg</href>\n"+
								"\t\t\t\t\t</Icon>\n"+
                     "\t\t\t\t</IconStyle>\n"+
                   "\t\t\t</Style>\n"+
                   "\t\t\t<name>Inconclusive Coverage</name>\n"+
                   "\t\t\t<description>Signal Coverage is inconclusive.</description>\n"+
                   inconclusiveString+
                   folderEndTag;
						  						  
      
      // Generating complete output .kml file
      kmlString+= kmlBeginTag;
      kmlString+= kmlTypeTag;
      kmlString+= docStartTag+"\t\t<name>KML Output</name>\n";
      kmlString+=  finalSameString;
      kmlString+=  finalDiffRFPSString;
      kmlString+=  finalDiffSignalProString;
      kmlString+=  finalInconclusiveString;
      kmlString+= docEndTag;
      kmlString+= kmlEndTag;

		
		//date for file name---
      java.util.Date date= new java.util.Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String dateString = formatter.format(date);
      String outputFileName = dateString + "_outfile.kml";

      File kmlOutputFile = new File(userInput.getOutputDirectory().getPath() + "/" + outputFileName);
      kmlOutputFile.getParentFile().mkdirs();
      
      try {
      
         PrintWriter writer = new PrintWriter(kmlOutputFile);
         writer.println(kmlString);
         writer.close();
         return true;
      }
      catch (Exception e) {
         return false;
      } 
   }
   
}
