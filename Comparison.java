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
   private final int SAME = 3;
   private final int ONLY_RFPS = 1;
   private final int ONLY_SIGPRO = 2;
   private final int INCONCLUSIVE = 4;
      
   //KML strings
   private String sameString;
   private String diffRFPSString;
   private String diffSignalProString;
   private String inconclusiveString;
   
   private UserInput userInput;
   private RfpsData rfpsData;
   private SignalProData signalProData;
   private ArrayList<CoveragePoint> coverageGrid;
   
   public Comparison(UserInput ui)
   {
      this.userInput = ui;
      
      sameString = diffRFPSString = diffSignalProString = inconclusiveString = "";
      
      this.rfpsData = new RfpsData();
      rfpsData.readData(ui.getRfpsFile());
      this.signalProData = new SignalProData();
      signalProData.readData(ui.getSignalProFile());
      
      this.coverageGrid = new ArrayList<CoveragePoint>();
      
      compare();
   }
   
      //--------------------------------------------------------------------------------------------------------------------------------------- change zone open
 
 	public class CoveragePoint
	{
		public CoveragePoint(Coordinate location, int covType) {
			loc = new Coordinate(location.getLatitude(), location.getLongitude());
			coverageType = covType;
		}
		
		public int getCoverageType() { return coverageType; }
		public Coordinate getCoordinate() { return new Coordinate(loc.getLatitude(), loc.getLongitude()); }
		
		private int coverageType;
		private Coordinate loc;
	}  
	
   private void compare()
   {
		int gridSpacingDistance = 1000;//in meters
		float westBound = signalProData.getWestBound(),
            eastBound = signalProData.getEastBound(),
            northBound = signalProData.getNorthBound(),
            southBound = signalProData.getSouthBound();
		Coordinate gridPoint = new Coordinate(southBound,westBound);
		int row = 0;
//       int count = 0;
		while (northBound - gridPoint.getLatitude() > 0) {
			gridPoint.setLongitude(westBound);
			while (eastBound - gridPoint.getLongitude() > 0) {
            boolean rfpsCoversPoint = rfpsData.isCoverageNear(gridPoint, gridSpacingDistance/2);
            boolean signalProCoversPoint = signalProData.isCoverageNear(gridPoint, gridSpacingDistance/2);
            int coverageType = (signalProCoversPoint ? 2 : 0) & (rfpsCoversPoint ? 1 : 0);
            coverageGrid.add(new CoveragePoint(gridPoint, coverageType));
				gridPoint = CoordinateManager.addDistanceEast(gridPoint.getLatitude(), gridPoint.getLongitude(), gridSpacingDistance);
//             ++count;
//             if (count%100 == 0)
//                System.out.println(count);
			}
			gridPoint = CoordinateManager.addDistanceSouth(gridPoint.getLatitude(), gridPoint.getLongitude(), gridSpacingDistance);
			++row;
		}
   }   
      //--------------------------------------------------------------------------------------------------------------------------------------- change zone closed
   
   public void addKMLPoint(CoveragePoint covPoint){
      switch (covPoint.getCoverageType()) {
         case SAME:
            //coverage matches in signal pro and RFPS
            sameString+= "\t\t\t<Placemark>\n"+
                     //"\t\t\t\t<name>Placemark 2</name>\n"+
                     //"\t\t\t\t<description>Development Team's Headquarters.</description>\n"+
                     "\t\t\t\t<styleUrl>#stylesel_SAME</styleUrl>\n"+
                     "\t\t\t\t<Point>\n"+
                        "\t\t\t\t\t<coordinates>"+covPoint.getCoordinate().getLongitude()+","+covPoint.getCoordinate().getLatitude()+",0 </coordinates>\n"+
                     "\t\t\t\t</Point>\n"+
                  "\t\t\t</Placemark>\n";
            break;
         case ONLY_RFPS:
            //the coverage only in RFPS
   			diffRFPSString+= "\t\t\t<Placemark>\n"+
                     //"\t\t\t\t<name>Placemark 2</name>\n"+
                     //"\t\t\t\t<description>Development Team's Headquarters.</description>\n"+
                     "\t\t\t\t<styleUrl>#stylesel_ONLY_RFPS</styleUrl>\n"+
                     "\t\t\t\t<Point>\n"+
                        "\t\t\t\t\t<coordinates>"+covPoint.getCoordinate().getLongitude()+","+covPoint.getCoordinate().getLatitude()+",0 </coordinates>\n"+
                     "\t\t\t\t</Point>\n"+
                  "\t\t\t</Placemark>\n";				
            break;
         case ONLY_SIGPRO:
            //the coverage only in signal pro
            diffSignalProString+= "\t\t\t<Placemark>\n"+
                      //"\t\t\t\t<name>Placemark 2</name>\n"+
                      //"\t\t\t\t<description>Development Team's Headquarters.</description>\n"+
                      "\t\t\t\t<styleUrl>#stylesel_ONLY_SIGPRO</styleUrl>\n"+
                      "\t\t\t\t<Point>\n"+
                        "\t\t\t\t\t<coordinates>"+covPoint.getCoordinate().getLongitude()+","+covPoint.getCoordinate().getLatitude()+",0 </coordinates>\n"+
                      "\t\t\t\t</Point>\n"+
                   "\t\t\t</Placemark>\n";
            break;
         case INCONCLUSIVE:
            //the coverage inconclusive
            inconclusiveString+= "\t\t\t<Placemark>\n"+
                     //"\t\t\t\t<name>Placemark 2</name>\n"+
                     //"\t\t\t\t<description>Development Team's Headquarters.</description>\n"+
                     "\t\t\t\t<styleUrl>#stylesel_INCON</styleUrl>\n"+
                     "\t\t\t\t<Point>\n"+
                        "\t\t\t\t\t<coordinates>"+covPoint.getCoordinate().getLongitude()+","+covPoint.getCoordinate().getLatitude()+",0 </coordinates>\n"+
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
                     "\t\t\t\t<LabelStyle>\n"+
                        "\t\t\t\t\t<color>FF00FF00</color>\n"+ //green
                        "\t\t\t\t\t<colorMode>normal</colorMode>\n"+
                        "\t\t\t\t\t<scale>1</scale>\n"+
                     "\t\t\t\t</LabelStyle>\n"+
                   "\t\t\t</Style>\n"+
                   "\t\t\t<name>Similar Coverage</name>\n"+
                   "\t\t\t<description>Signal Coverage is the same.</description>\n"+
                   sameString+
                   folderEndTag;

      //Formating points where only RFPS had singal coverage for kml
      String finalDiffRFPSString = folderStartTag+
                   "\t\t\t<Style id=\"stylesel_ONLY_RFPS\">\n"+
                     "\t\t\t\t<LabelStyle>\n"+
                        "\t\t\t\t\t<color>FF3366FF</color>\n"+ //blue
                        "\t\t\t\t\t<colorMode>normal</colorMode>\n"+
                        "\t\t\t\t\t<scale>1</scale>\n"+
                     "\t\t\t\t</LabelStyle>\n"+
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
                     "\t\t\t\t<LabelStyle>\n"+
                        "\t\t\t\t\t<color>FF0000FF</color>\n"+ //red
                        "\t\t\t\t\t<colorMode>normal</colorMode>\n"+
                        "\t\t\t\t\t<scale>1</scale>\n"+
                     "\t\t\t\t</LabelStyle>\n"+
                   "\t\t\t</Style>\n"+
                   "\t\t\t<name>Difference in Coverage</name>\n"+
                   "\t\t\t<description>Signal Coverage only in SignalPro.</description>\n"+
                   diffSignalProString+
                   folderEndTag;

      //Formating points with inconclusive singal coverage for kml
      String finalInconclusiveString = folderStartTag+
                   "\t\t\t<Style id=\"stylesel_INCON\">\n"+
                     "\t\t\t\t<LabelStyle>\n"+
                        "\t\t\t\t\t<color>FFFFFF00</color>\n"+ //yellow
                        "\t\t\t\t\t<colorMode>normal</colorMode>\n"+
                        "\t\t\t\t\t<scale>1</scale>\n"+
                     "\t\t\t\t</LabelStyle>\n"+
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
