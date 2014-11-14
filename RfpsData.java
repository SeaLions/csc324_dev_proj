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
   Vector<String> rfpsStringCoordinatesVector; 
   
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
         
         //Get a node list of each tag named <Folder>     
         NodeList folderNodeList = dom.getElementsByTagName("Folder");
       
         //Loop through each <folder> tag element and look at each subtag
         for (int i = 0; i < folderNodeList.getLength(); i++)
         {
             
             //Get a <folder> node
             Node folderSubNode = folderNodeList.item(i);
             
             //get the child nodes of the <folder> tag
             NodeList folderSubNodeList = folderSubNode.getChildNodes();
             rfpsStringCoordinatesVector = new Vector<String>();
             //For the number of child nodes in that <folder> tag
             for (int x = 0; x < folderSubNodeList.getLength(); x++)
             {    
                 
                //grab a child node from the <folder> tag
                Node testNode = folderSubNodeList.item(x);
                
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
            if(rfpsStringBearingVector.size() != 400 && !rfpsStringCoordinatesVector.isEmpty())
            {
               rfpsStringBearingVector.add(rfpsStringCoordinatesVector);
            }       
         }//End of first for loop
         
		}//End try
      
		catch(Exception E)
		{
			System.out.println("Could not find RFPS file");
		}		
		
   }
}//End class









/*Check to see if tag name is <name> and it has "Bearing" as a text value
                if (testNode.getNodeName() == "name" && testNode.getTextContent().equals("Bearing - " + BEARING_COUNTER + ".0 T"))
                {
                  BEARING_COUNTER++; 
                  
                  Get the next node after <name> which is <Placemark>
                  Node nextNode = testNode.getNextSibling();
                  nextNode = nextNode.getNextSibling();
                  
                  //Look through <Placemark>
                  NodeList childNodesOfNextNode = nextNode.getChildNodes();
                  
                  //Loop through each <Placemark> tag element and look at each of its subtags
                  if(rfpsStringBearingVector.size() != 360)
                  {
                     System.out.println("Im here");
                     rfpsStringBearingVector.add(rfpsStringCoordinatesVector);
                  }
                //}//End of first if statement */
