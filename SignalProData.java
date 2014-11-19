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
	
	ArrayList<ArrayList<Boolean>> coverage;//first dimension is latitude, second is longitude
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
   
//    public void printForDemo() {
// 		double pixelUnitLat = (northCoord - southCoord)/coverage.get(0).size();
// 		double pixelUnitLon = (eastCoord - westCoord)/coverage.get(0).size();
// 		for (int i = 0; i < coverage.size(); i++)
// 		{
// 			for (int j = 0; j < coverage.get(i).size(); j++)
// 			{
//             double curLon = westCoord + j/(double)coverage.get(0).size()*(eastCoord-westCoord) + pixelUnitLon/2;
//             double curLat = southCoord + i/(double)coverage.size()*(northCoord-southCoord) + pixelUnitLat/2;
// 				System.out.print(isCoverageNear(curLon, curLat, 200) == true ? 'X' : ' ');
// 			}
// 			System.out.println();
// 		} 
//    }
	
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
				}
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
	
	public boolean isCoverageNear(double longitude, double latitude, double radiusInMeters) {
		double pixelUnitLat = (northCoord - southCoord)/coverage.get(0).size();
		double meterUnitLat = Math.PI*pixelUnitLat*6371000/360;
		int pixelRadiusLat = (int)Math.floor(radiusInMeters/meterUnitLat) + 1;
		
		double pixelUnitLon = (eastCoord - westCoord)/coverage.get(0).size();
		double meterUnitLon = Math.PI*pixelUnitLon*6371000/360;
		int pixelRadiusLon = (int)Math.floor(radiusInMeters/meterUnitLon) + 1;
		
		int centerPixelLon = nearestPixelToLongitude(longitude);
		int centerPixelLat = nearestPixelToLatitude(latitude);
		
      /*
      left/right/lower/upper are named for spatial orientation.
      "coverage" 2-d array is left-to-right increasing row indices,
                              upper-to-lower increasing column indices.
      */
      
		int leftPixelBound = centerPixelLon - pixelRadiusLon;
		int rightPixelBound = centerPixelLon + pixelRadiusLon;
		
		int upperPixelBound = centerPixelLat - pixelRadiusLat;
		int lowerPixelBound = centerPixelLat + pixelRadiusLat;
		
		if (lowerPixelBound < 0
			 || upperPixelBound >= coverage.size()
			 || rightPixelBound < 0
			 || leftPixelBound >= coverage.get(0).size())
			return false;
		for (int i = upperPixelBound; i <= lowerPixelBound; ++i) {
			if (i < 0 || i >= coverage.size()) continue;
			for (int j = leftPixelBound; j <= rightPixelBound; ++j) {
            if (j < 0 || j >= coverage.get(0).size()) continue;
            double curLon = westCoord + j/(double)coverage.get(0).size()*(eastCoord-westCoord) + pixelUnitLon/2;
            double curLat = southCoord + i/(double)coverage.size()*(northCoord-southCoord) + pixelUnitLat/2;
				if (distance(longitude, latitude, curLon, curLat) <= radiusInMeters && coverage.get(i).get(j))
					return true;
			}
		}
      
		return false;
	}
   
   private double distance(double lon1, double lat1, double lon2, double lat2) {
      double dLat = Math.toRadians(lat2-lat1);  
      double dLon = Math.toRadians(lon2-lon1);  
      double a = Math.sin(dLat/2) * Math.sin(dLat/2) +  
         Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *  
         Math.sin(dLon/2) * Math.sin(dLon/2);  
      double c = 2 * Math.asin(Math.sqrt(a));  
      return 6371000 * c;  
   }
	
	private int nearestPixelToLongitude(double longitude) {
		double pixelUnit = (eastCoord - westCoord)/coverage.get(0).size();
		double pixelLongitude = westCoord;
		for (int pixel = 0; pixel < coverage.get(0).size(); ++pixel) {
			if (pixelLongitude + pixelUnit > longitude)
				return pixel;
         pixelLongitude = westCoord + pixel/(double)coverage.get(0).size()*(eastCoord-westCoord);
		}
		return -1;
	}
	
	private int nearestPixelToLatitude(double latitude) {
		double pixelUnit = (northCoord - southCoord)/coverage.size();
		double pixelLatitude = southCoord;
		for (int pixel = 0; pixel < coverage.size(); ++pixel) {
			if (pixelLatitude + pixelUnit > latitude)
				return pixel;
         pixelLatitude = southCoord + pixel/(double)coverage.size()*(northCoord-southCoord);
		}
		return -1;
	}
}
