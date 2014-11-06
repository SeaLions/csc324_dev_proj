import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
         
      }
      catch (Exception e) {
      }
   }
   
   public void readData(File signalProFile)
   {
      try {
         ZipFile kmz = new ZipFile(signalProFile);
         Enumeration<? extends ZipEntry> entries = kmz.entries();
         while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            System.out.println(entry.getName());
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
