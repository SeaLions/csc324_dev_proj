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
      
      compare();
   }
   
      //--------------------------------------------------------------------------------------------------------------------------------------- change zone open
   
   private void compare()
   {
      rfps=rfpsData.getData();
      for(int largeLoop=0;largeLoop<360;largeLoop++)
      {
         //a master iterator for this largeLoop iteration
         int master=0;
         for(int j=0;j<rfps.get(largeLoop).size();j++)
         {
            //initialize variables needed for primary loop's current iteration
            Vector<double[]> PTSarr = new Vector<double[]>();
            double[][] endPTS = new double [2][2];
            //convert String data to Double values
            endPTS=parseString(rfps.get(largeLoop).get(j));
            //calculate the changes in latitude and longetude
            double Dlat= endPTS[0][1]-endPTS[0][0];
            double Dlon= endPTS[1][1]-endPTS[1][0];
            //calculate the lengths of the three sides
            double distH=distance(endPTS[0][0],endPTS[1][0],endPTS[0][1],endPTS[1][1],'K')*1000;
            double distA=distance(endPTS[0][0],endPTS[1][0],endPTS[0][0],endPTS[1][1],'K')*1000;
            double distO=distance(endPTS[0][0],endPTS[1][1],endPTS[0][1],endPTS[1][1],'K')*1000;
            //calculate the number of partitions
            int partitions=(int)(distH/100);
            //calculate the angle for the point used as an origin
            double angle = Fangle(distO,distA);
            //initialize first point
            double[] firstPoint = new double[2];
            firstPoint[0]=endPTS[0][0];
            firstPoint[1]=endPTS[1][0];
            PTSarr.add(firstPoint);
            
            //iterator
            int it;
            //per iteration lat and lon change
            double dLatPit=(100/distH)*Dlat;
            double dLonPit=(100/distH)*Dlon;
            //current segment's loop
            for(int i=partitions;i>0;i--)
            {
               //set iterator value
               it=partitions-i;
               master++;
               //getPoint(double currentBaseLatitude, double currentBaseLongitude,double Dlat, double Dlon,double angle, double distanceChangeFromOrigin)
               PTSarr.add(getPoint(PTSarr.get(it)[0], PTSarr.get(it)[1], dLatPit, dLonPit, angle, distH, it));
            }
            for(int i=0;i<partitions;i++)
            {
               boolean correlation=signalProData.isCoverageNear(PTSarr.get(i)[1],PTSarr.get(i)[0]);
               afterCompare[largeLoop][master][0]=PTSarr.get(i)[0];
               afterCompare[largeLoop][master][1]=PTSarr.get(i)[1];
               if(correlation)
                  afterCompare[largeLoop][master][2]=1;
               else
                  afterCompare[largeLoop][master][2]=0;
            }
         }
      }
      
      //signalProData.isCoverageNear(double longitude, double latitude);
      
      
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
