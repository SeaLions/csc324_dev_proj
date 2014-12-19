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
	
	private ArrayList<ArrayList<Boolean>> coverage;//first dimension is latitude, second is longitude
	private float northCoord, southCoord, eastCoord, westCoord;
	
	
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
         northCoord = Float.parseFloat(nl.item(0).getTextContent());
         
         nl = kmlTree.getElementsByTagName("south");
         southCoord = Float.parseFloat(nl.item(0).getTextContent());
         
         nl = kmlTree.getElementsByTagName("east");
         eastCoord = Float.parseFloat(nl.item(0).getTextContent());
         
         nl = kmlTree.getElementsByTagName("west");
         westCoord = Float.parseFloat(nl.item(0).getTextContent());
         
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
// 		float pixelUnitLat = (northCoord - southCoord)/coverage.get(0).size();
// 		float pixelUnitLon = (eastCoord - westCoord)/coverage.get(0).size();
// 		for (int i = 0; i < coverage.size(); i++)
// 		{
// 			for (int j = 0; j < coverage.get(i).size(); j++)
// 			{
//             float curLon = westCoord + j/(float)coverage.get(0).size()*(eastCoord-westCoord) + pixelUnitLon/2;
//             float curLat = southCoord + i/(float)coverage.size()*(northCoord-southCoord) + pixelUnitLat/2;
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
			e.printStackTrace();
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
	
	public boolean isCoverageNear(Coordinate loc, int radiusInMeters) {		
		float pixelUnitLat = (northCoord - southCoord)/coverage.get(0).size();
		float meterUnitLat = (float)(Math.PI*pixelUnitLat*6371000/360);
		int pixelRadiusLat = (int)Math.floor(radiusInMeters/meterUnitLat) + 1;
		
		float pixelUnitLon = (eastCoord - westCoord)/coverage.get(0).size();
		float meterUnitLon = (float)(Math.PI*pixelUnitLon*6371000/360);
		int pixelRadiusLon = (int)Math.floor(radiusInMeters/meterUnitLon) + 1;
		
		int centerPixelLon = nearestPixelToLongitude(loc.getLongitude());
		int centerPixelLat = nearestPixelToLatitude(loc.getLatitude());
		
      /*
      left/right/lower/upper are named for spatial orientation.
      "coverage" 2-d array is west-to-east increasing row indices,
                              north-to-south increasing column indices.
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
            
            float curLon = centerLonOfPixel(j);
            float curLat = centerLatOfPixel(i);
				if (CoordinateManager.distance(loc, new Coordinate(curLat,curLon)) <= radiusInMeters && coverage.get(i).get(j))
					return true;
            
			}
		}
      
		return false;
	}
	
	private int nearestPixelToLongitude(float longitude) {
      return (int) ( (longitude - westCoord)/(eastCoord - westCoord)*(float)coverage.get(0).size() ) ;
	}
	
	private int nearestPixelToLatitude(float latitude) {
		return (int) ( (latitude - southCoord)/(northCoord - southCoord)*(float)coverage.size() ) ;
	}
   
   private float centerLatOfPixel(int pixelRow) {
      return southCoord + Math.signum(pixelRow)*pixelRow/(float)coverage.size()*(northCoord-southCoord);
   }
   
   private float centerLonOfPixel(int pixelCol) {
      return westCoord + Math.signum(pixelCol)*pixelCol/(float)coverage.get(0).size()*(eastCoord-westCoord);
   }
   
   public float getNorthBound() { return northCoord; }
   
   public float getSouthBound() { return southCoord; }
   
   public float getEastBound() { return eastCoord; }
   
   public float getWestBound() { return westCoord; }
}
