import java.io.PrintWriter;
import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;

public class Comparison
{
   UserInput userInput;
   RfpsData rfpsData;
   SignalProData signalProData;
   
   public Comparison(UserInput ui)
   {
      this.userInput = ui;
      
      this.rfpsData = new RfpsData();
      rfpsData.readData(ui.getRfpsFile());
      
      this.signalProData = new SignalProData();
      signalProData.readData(ui.getSignalProFile());
   }
   
   public void createKmlOutputFile(){
      //string that will hold the initial .kml file
      String kmlString = "";
      
      // Template for generating base .kml example file:
      kmlString+= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; //begin tag
      kmlString+= "<kml xmlns=\"http://www.opengis.net/kml/2.2\">"; //begin kml tag
      kmlString+= "</kml>"; //end kml tag
		
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
      }
      catch (Exception e) {
      }
   
   }
}