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
   private UserInput userInput;
   private RfpsData rfpsData;
   private SignalProData signalProData;
	private ArrayList<ArrayList<CoveragePoint> > comparisonGrid;
   
   public Comparison(UserInput ui)
   {
      this.userInput = ui;
      
      
      this.rfpsData = new RfpsData();
      rfpsData.readData(ui.getRfpsFile());
      this.signalProData = new SignalProData();
      signalProData.readData(ui.getSignalProFile());
      
      compare();
   }
   
      //--------------------------------------------------------------------------------------------------------------------------------------- change zone open
 
 	public class CoveragePoint
	{
		public CoveragePoint(Coordinate location, boolean coverageHere) {
			loc = new Coordinate(location.getLatitude(), location.getLongitude());
			coverageIsHere = coverageHere;
		}
		
		public boolean isCoverageHere() { return coverageIsHere; }
		public Coordinate getLocation() { return new Coordinate(loc.getLatitude(), loc.getLongitude()); }
		
		private boolean coverageIsHere;
		private Coordinate loc;
	}  
	
   private void compare()
   {
		int gridSpacingDistance = 100;//in meters
		float westBound = signalProData.getWestBound(),
            eastBound = signalProData.getEastBound(),
            northBound = signalProData.getNorthBound(),
            southBound = signalProData.getSouthBound();
		comparisonGrid = new ArrayList< ArrayList< CoveragePoint> >();
		Coordinate gridPoint = new Coordinate(westBound, southBound);
		int row = 0;
		while (northBound - gridPoint.getLatitude() > 0) {
			comparisonGrid.add(new ArrayList<CoveragePoint>());
			gridPoint.setLongitude(westBound);
			while (eastBound - gridPoint.getLongitude() > 0) {
				comparisonGrid.get(row).add(new CoveragePoint(gridPoint, rfpsData.isCoverageNear(gridPoint, gridSpacingDistance/2) && signalProData.isCoverageNear(gridPoint, gridSpacingDistance/2)));
				gridPoint = CoordinateManager.addDistanceEast(gridPoint.getLatitude(), gridPoint.getLongitude(), gridSpacingDistance);
			}
			gridPoint = CoordinateManager.addDistanceSouth(gridPoint.getLatitude(), gridPoint.getLongitude(), gridSpacingDistance);
			++row;
		}
   }   
      //--------------------------------------------------------------------------------------------------------------------------------------- change zone closed
   
   public void addKMLPoint(Coordinate newCoord){
      switch (newCoord.getCoverage()) {
         case SAME:
            //coverage matches in signal pro and RFPS
            sameString+= "\t\t\t<Placemark>\n"+
                     //"\t\t\t\t<name>Placemark 2</name>\n"+
                     //"\t\t\t\t<description>Development Team's Headquarters.</description>\n"+
                     "\t\t\t\t<styleUrl>#stylesel_SAME</styleUrl>\n"+
                     "\t\t\t\t<Point>\n"+
                        "\t\t\t\t\t<coordinates>"+newCoord.getLongitude()+","+newCoord.getLatitude()+",0 </coordinates>\n"+
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
                        "\t\t\t\t\t<coordinates>"+newCoord.getLongitude()+","+newCoord.getLatitude()+",0 </coordinates>\n"+
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
                        "\t\t\t\t\t<coordinates>"+newCoord.getLongitude()+","+newCoord.getLatitude()+",0 </coordinates>\n"+
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
