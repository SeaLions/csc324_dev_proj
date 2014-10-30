import java.io.PrintWriter;
import java.io.File;
import java.sql.*;
import java.util.*;
import java.lang.*;
import java.text.*;

public class Comparison
{
   
   public Comparison() {
   }
   
   public boolean createKmlOutputFile(File userOutputDirectory){
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
      
      try {
		   
         File kmlOutputFile = new File(userOutputDirectory.getPath() + "/" + outputFileName);
         kmlOutputFile.getParentFile().mkdirs();
      
         PrintWriter writer = new PrintWriter(kmlOutputFile);
         writer.println(kmlString);
         writer.close();
      }
      catch (Exception e) {
         return false;
      }
      
      return true;
   }
}

