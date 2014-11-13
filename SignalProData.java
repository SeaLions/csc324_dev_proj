import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.awt.image.*;
import javax.imageio.*;
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
		try {
			BufferedImage pngImage = ImageIO.read(input);
			int height = pngImage.getHeight();
			int width = pngImage.getWidth();
			
			coverage = new ArrayList<ArrayList<Boolean>>();
						
			for (int i = 0; i < height; i++)
			{
				coverage.add(new ArrayList<Boolean>());
				for (int j = 0; j < width; j++)
				{
					coverage.get(i).add(false);
					int pixel = pngImage.getRGB(j,i);
					if ((pixel>>24) != 0x00)
						coverage.get(i).set(j, true);
					//System.out.print(coverage.get(i).get(j) == true ? 'X' : ' ');
				}
				//System.out.println();
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
					break;					
				}
			}
      }
      catch (IOException e) {
			System.out.println(e);
      }
	}
	
	public boolean isCoverageNear(double longitude, double latitude) {
		int longPixel = nearestPixelToLongitude(longitude);
		int latPixel = nearestPixelToLatitude(latitude);
		if (longPixel == -1 || latPixel == -1) return false;
		return coverage.get(longPixel).get(latPixel);
	}
	
	private int nearestPixelToLongitude(double longitude) {
		double pixelUnit = (northCoord - southCoord)/coverage.get(0).size();
		double pixelLongitude = northCoord + pixelUnit/2;
		for (int pixel = 0; pixel < coverage.size(); ++pixel) {
			if (pixelLongitude + pixelUnit > longitude)
				return pixel;
		}
		return -1;
	}
	
	private int nearestPixelToLatitude(double latitude) {
		double pixelUnit = (eastCoord - westCoord)/coverage.size();
		double pixelLatitude = westCoord + pixelUnit/2;
		for (int pixel = 0; pixel < coverage.size(); ++pixel) {
			if (pixelLatitude + pixelUnit > latitude)
				return pixel;
		}
		return -1;
	}
}
