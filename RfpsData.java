import java.io.*;
import java.util.*;
import org.jsoup.*;
import javax.swing.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import java.util.Vector;
public class RfpsData extends PlotData
{
   Elements rfpsCoordinates;  
   public RfpsData()
   {
      super();
   }
	
   public void readData(File rfpsFile)
   {
		//create Scanner for file and read in all the text as one large string
		try
		{
			/*String rfpsFileText = "";
			Scanner scanRfpsFile = new Scanner(new BufferedReader(new FileReader(rfpsFile)));
			//reading ten lines of file into a string to see some data for presentation
			for (int i =0;i<10;i++)
			{
				rfpsFileText += scanRfpsFile.nextLine();
				rfpsFileText += "\n";
			}

			System.out.println(rfpsFileText);*/
         Document parsedRfpsFile = Jsoup.parse(rfpsFile, null);
         rfpsCoordinates = parsedRfpsFile.select("coordinates");
         
         System.out.println(rfpsCoordinates); 
			System.out.println("rfpsFile in plain text "+rfpsFileText);
		}
		catch(Exception E)
		{
			System.out.println("Could not scan RFPS file");
		}	
   }
   
   //method to get coordinates
   public Elements getRfpsCoordinates()
   {
      return rfpsCoordinates;
   }
   
	//possible generic datastructure function 
	/*public  <DataStructure> DataStructure getPlotData()
	{
		return DataStructure;
	}*/
   public static void main(String[] args)throws IOException
   {
      RfpsData rfpsData = new RfpsData();
   	File testRfpsFile = new File("C:/Users/jmorar567/Desktop/RFPS-SignalPro Test Cases/Bishop/RFPS/MountainousInBishop.kml");
      rfpsData.readData(testRfpsFile);
      
      try
      {
         File file = new File("U:/csc324_dev_proj/myfile.txt");
         FileWriter fileWriter = new FileWriter(file, true);
         BufferedWriter bufferFileWriter  = new BufferedWriter(fileWriter);
         Elements testCoordinates = rfpsData.getRfpsCoordinates();
         System.out.println(testCoordinates);
         for( Element testCoordinate : testCoordinates)
         {
            fileWriter.append(testCoordinate.toString());
         }
      }
      catch (IOException e) 
      {
        System.out.println("Did not append");
      }
      //System.out.println(testRfpsFile);
   }
}

