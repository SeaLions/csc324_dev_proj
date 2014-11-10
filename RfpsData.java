import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.Vector;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class RfpsData extends PlotData
{
   private int BEARING_COUNTER = 0;
   private final int ALL_BEARINGS = 360;
   private Vector<Vector<String>> rfpsStringBearingVector = new Vector<Vector<String>>(ALL_BEARINGS);
   private Vector<String> rfpsStringCoordinatesVector = new Vector<String>();
   
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
         NodeList nodeList = dom.getElementsByTagName("Folder");
       
         //Loop through each <folder> tag element and look at each subtag
         for (int i = 0; i < nodeList.getLength(); i++)
         {
             Node folderSubNode = nodeList.item(i);
             NodeList test = folderSubNode.getChildNodes();

             for (int x = 0; x < test.getLength(); x++)
             {
                Node testNode = test.item(x);
                
                //Check to see if tag name is <name> and it has "Bearing" as a text value
                if (testNode.getNodeName() == "name" && testNode.getTextContent().equals("Bearing - " + BEARING_COUNTER + ".0 T"))
                {
                  BEARING_COUNTER++; 
                  
                  //Get the next node after <name> which is <Placemark>
                  Node nextNode = testNode.getNextSibling();
                  nextNode = nextNode.getNextSibling();
                  
                  //Look through <Placemark>
                  NodeList childNodesOfNextNode = nextNode.getChildNodes();
                  
                  //Loop through each <Placemark> tag element and look at each of its subtags
                  for (int j = 0; j < childNodesOfNextNode.getLength(); j++)
                  {
                    
                     Node placemarkSubNode = childNodesOfNextNode.item(j);
                      
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
                           
                           if(rfpsStringBearingVector.size() != 360)
                           {
                              rfpsStringBearingVector.add(rfpsStringCoordinatesVector);
                           }
                        }
                        
                     }
       
                  }//End of third for loop
                }//End of first if statement 
          
            }//End of second for loop         
         }//End of first for loop
         //System.out.println(rfpsStringBearingVector);
		}//End try
      
		catch(Exception E)
		{
			System.out.println("Could not find RFPS file");
		}		
		
   }
}//End class
