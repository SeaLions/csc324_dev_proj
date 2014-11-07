import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class SignalProData extends PlotData
{
   public SignalProData()
   {
      super();
   }
   
   private void processKml(InputStream input)
   {
      try {
         Document kmlTree = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
         NodeList nl = kmlTree.getElementsByTagName("north");
                  
         if (nl != null && nl.getLength() > 0) {
      	   Node n = nl.item(0).getChildNodes().item(0);
            //NodeList coordinates = el.getChildNodes();
                        
            /*Node northNode = coordinates.item(0);
            double northCoordinate = Double.parseDouble(northNode.getTextContent()); 
            
            Node southNode = coordinates.item(1);
            double southCoordinate = Double.parseDouble(southNode.getTextContent());
            
            Node eastNode = coordinates.item(2);
            double eastCoordinate = Double.parseDouble(eastNode.getTextContent());
            
            Node westNode = coordinates.item(3);
            double westCoordinate = Double.parseDouble(westNode.getTextContent());*/
            
            //System.out.println(northCoordinate);
            //System.out.println(southCoordinate);
            //System.out.println(eastCoordinate);
            //System.out.println(westCoordinate);
   		}
         
      }
      catch (Exception e) {
         System.out.println(e);
      }
   }
   
   public void readData(File signalProFile)
   {
      try {
         ZipFile kmz = new ZipFile(signalProFile);
         Enumeration<? extends ZipEntry> entries = kmz.entries();
         while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (true) {
               
               processKml(kmz.getInputStream(entry));
               
               break;
            }
         }
      }
      catch (IOException e) {
      }
      
      
	}
}
