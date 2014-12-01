import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.Vector;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class RfpsData extends PlotData
{
   private double BEARING_COUNTER = 0;
   final private int ALL_BEARINGS=360;
   private Vector<Vector<String>> rfpsStringBearingVector = new Vector<Vector<String>>(ALL_BEARINGS);
   Vector<String> rfpsStringCoordinatesVector; 
   Vector<double[]> PTSarr = new Vector<double[]>();
   Vector<Vector<double[]>> rfpsDoubleCoordinateVector=new Vector<Vector<double[]>>();
   public RfpsData()
   {
      super();
   }
	
   public Vector<Vector<String>> getData()
   {
      return rfpsStringBearingVector;
   }
  
   public void readData(File rfpsFile)
   {
		try
		{
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         DocumentBuilder db = dbf.newDocumentBuilder(); 
			Document dom = db.parse(rfpsFile);
      
      
      NodeList folderNodeList = dom.getElementsByTagName("Folder");
      
      //Loop through each <folder> tag element and look at each subtag
         for (int i = 0; i < folderNodeList.getLength(); i++)
         {
             //Get a <folder> node
             Node folderSubNode = folderNodeList.item(i);
             Element folderElement = (Element)folderSubNode;
             //get the first 'name' element of the <folder> tag 
             Node nameNode = folderElement.getElementsByTagName("name").item(0);
             rfpsStringCoordinatesVector = new Vector<String>();
             if(nameNode.getTextContent().contains("Bearing"))
             {
                //get the child nodes of the <folder> tag
                NodeList folderSubNodeList = folderSubNode.getChildNodes();
                rfpsStringCoordinatesVector = new Vector<String>();
                //For the number of child nodes in that <folder> tag
                for (int x = 0; x < folderSubNodeList.getLength(); x++)
                {    
                    
                   //grab a child node from the <folder> tag
                   Node testNode = folderSubNodeList.item(x);
                   
                   //testing to see if Folder has "Placemark value and seeing if name of that placemark identifies it as a Bearing 
                   if(testNode.getNodeName() == "Placemark")
                   {
                      
                      NodeList placeMarkChildren = testNode.getChildNodes();
                      
                      for (int j = 0; j < placeMarkChildren.getLength(); j++)
                      {
                        
                        Node placemarkSubNode = placeMarkChildren.item(j);
                         
                        //If the subtag of <Placemark> is <LineString>, get the coordinates and put them in a vector
                        if (placemarkSubNode.getNodeName() == "LineString") 
                        {  
                          
                           NodeList childNodesOfLineString = placemarkSubNode.getChildNodes();
                           
                           for (int k = 0; k < childNodesOfLineString.getLength(); k++)
                           {
                              Node lineStringSubNode = childNodesOfLineString.item(k);
                              
   									/*If subnodes of <LineString> is <coordinates>, append the values to the 
   									string vector and then append the string vector to the bearing vector*/
                              if (lineStringSubNode.getNodeName() == "coordinates")
                              {   
                                 rfpsStringCoordinatesVector.add(lineStringSubNode.getTextContent()); 
                                 
                              }    
                           }
                        }  
                     }//End of third for loop   
                  }//End of first if statement   
                }//End second for loop    
              }//end of if statement to ensure folder is of type bearing
               if(rfpsStringBearingVector.size() != 400 && !rfpsStringCoordinatesVector.isEmpty())
               {
                  rfpsStringBearingVector.add(rfpsStringCoordinatesVector);
                  
               }       
          }//end of loop that goes through all folders
      }//End try
      catch(Exception e)
      {
       e.printStackTrace();  
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
   /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~FOR BRENDAN~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
   //what does this do?? there are a lot of parameters for this method but only 4 of them are used... getPoint is used in the coverageNearPoint method
   private double[] getPoint(double lat, double lon,double Dlat, double Dlon,double angle, double distance,int iteration)
   {
      double[] arr= new double[2];
      arr[0]=lat+Dlat;
      arr[1]=lon+Dlon;
      return arr;
   }
   /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

   //calculates distance between two gps coords
   private double distance(double lat1, double lon1, double lat2, double lon2)
   {
      double theta = lon1 - lon2;
      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
      dist = Math.acos(dist);
	   dist = rad2deg(dist);
      dist = dist * 60 * 1.1515;
//calculates distance in meters
         dist = dist * 1609.344;

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
   
   //needs to be more efficient, right now it takes 47 seconds to test 1 point
   //method to compare coverageNearPoint
   public boolean coverageNearPoint(double Lat,double Lon,double distanceMeters)
   {
      //defines variables to test against parameters passed into function
      double rfpsLat,rfpsLon;
      boolean isNear=false;
      double actualDistance;
      //parse strings into two points of latitude and longitude
      for(int largeLoop=0;largeLoop<rfpsStringBearingVector.size();largeLoop++)
      {
         //a master iterator for this largeLoop iteration
         int master=0;
         for(int j=0;j<rfpsStringBearingVector.get(largeLoop).size();j++)
         {
            //initialize variables needed for primary loop's current iteration
            PTSarr = new Vector<double[]>();
            double[][] endPTS = new double [2][2];
            //convert String data to Double values
            endPTS=parseString(rfpsStringBearingVector.get(largeLoop).get(j));
            //calculate the changes in latitude and longetude
            double Dlat= endPTS[0][1]-endPTS[0][0];
            double Dlon= endPTS[1][1]-endPTS[1][0];
            //calculate the lengths of the three sides
            double distH=distance(endPTS[0][0],endPTS[1][0],endPTS[0][1],endPTS[1][1])*1000;
            double distA=distance(endPTS[0][0],endPTS[1][0],endPTS[0][0],endPTS[1][1])*1000;
            double distO=distance(endPTS[0][0],endPTS[1][1],endPTS[0][1],endPTS[1][1])*1000;
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
            
            //go through each partition point in PTSarr and see how far it is from given point Lon,Lat
            for(int i=0;i<PTSarr.size();i++)
            {
               rfpsLat = PTSarr.get(i)[0];
               rfpsLon = PTSarr.get(i)[1];
               actualDistance = distance(Lat,Lon,rfpsLat,rfpsLon); 
               
               if(actualDistance <= distanceMeters)
               {
                  isNear = true;      
               }
            }      
         }
      }
      return isNear;
   }
   
   
}//End class



