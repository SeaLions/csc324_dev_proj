import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


public class SignalProData extends PlotData
{
	
	ArrayList<ArrayList<Boolean>> coverage;
	double northCoord, southCoord, eastCoord, westCoord;
	
	
   public SignalProData()
   {
      super();
   }
   
   private String getKmlInformation(InputStream input)
   {
      try {
         
			Document kmlTree = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
         NodeList nl;
         
         nl = kmlTree.getElementsByTagName("north");
         northCoord = Double.parseDouble(nl.item(0).getTextContent());
         
         nl = kmlTree.getElementsByTagName("south");
         southCoord = Double.parseDouble(nl.item(0).getTextContent());
         
         nl = kmlTree.getElementsByTagName("east");
         eastCoord = Double.parseDouble(nl.item(0).getTextContent());
         
         nl = kmlTree.getElementsByTagName("west");
         westCoord = Double.parseDouble(nl.item(0).getTextContent());
         
   		nl = kmlTree.getElementsByTagName("href");
			String imageFileName = "";
			
			for(int i = 0; i < nl.getLength(); i++) {
				if (nl.item(i).getTextContent().contains("S.png"))
					imageFileName = nl.item(i).getTextContent();
			}      
			
   		return imageFileName;
			
      }
      catch (Exception e) {
         System.out.println(e);
			return "Image not found";
      }
   }
	
	private void processImage(InputStream input)
	{
		/*int file_chunk_size = 1024 * 4; //4KBs, written like this to easily change it to 8
		byte[] buffer = new byte[file_chunk_size];
		int bytesRead = 0;
		while ( (bytesRead = read(buffer)) > 0 ) {
			fileOutputStream.write(buffer, 0, bytesRead);
		}*/

	}
   
   public void readData(File signalProFile)
   {
      try {
         ZipFile kmz = new ZipFile(signalProFile);
         Enumeration<? extends ZipEntry> entries = kmz.entries();
   		String imageFileName = "";      
			while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            imageFileName = getKmlInformation(kmz.getInputStream(entry));
				if (! imageFileName.equals(""))
					break;
         }
			
			entries = kmz.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.getName().equals(imageFileName)) {
					processImage(kmz.getInputStream(entry));					
				}
			}
      }
      catch (IOException e) {
			System.out.println(e);
      }
	}
}
