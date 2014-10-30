import java.io.*;
import java.util.*;

public class RfpsData extends PlotData
{
   public RfpsData()
   {
      super();
   }
	
   public void readData(File rfpsFile) throws IOException
   {
		//create Scanner for file and read in all the text as one large string
		try
		{
			String rfpsFileText = "";
			Scanner scanRfpsFile = new Scanner(new BufferedReader(new FileReader(rfpsFile)));
			//reading ten lines of file into a string to see some data for presentation
			for (int i =0;i<10;i++)
			{
				rfpsFileText += scanRfpsFile.nextLine();
				rfpsFileText += "\n";
			}
			System.out.println("rfpsFile in plain text "+rfpsFileText);
		}
		catch(Exception E)
		{
			System.out.println("Could not scan RFPS file");
		}	
   }
	//possible generic datastructure function 
	/*public  <DataStructure> DataStructure getPlotData()
	{
		return DataStructure;
	}*/
}