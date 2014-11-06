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
   
   public Comparison(UserInput ui)
   {
      this.userInput = ui;
      
      this.rfpsData = new RfpsData();
      rfpsData.readData(ui.getRfpsFile());
      
      this.signalProData = new SignalProData();
      signalProData.readData(ui.getSignalProFile());
   }
   
   public boolean createKmlOutputFile()
   {
      
      //string that will hold the initial .kml file
      String kmlString = "";
      
      // Template for generating base .kml example file:
      kmlString+= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; //begin tag
      kmlString+= "<kml xmlns=\"http://earth.google.com/kml/2.0\">"; //begin kml tag
      kmlString+= "<Document><name>KML Example file</name><description>Simple markers</description><Placemark><name>Marker 1</name><description>Test pin! WOOHOO!</description><Point><coordinates>-117.250092,32.717501,0 </coordinates></Point></Placemark></Document>"; 
      kmlString+= "</kml>";//end kml tag
		
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
