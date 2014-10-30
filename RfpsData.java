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
		try
		{
			String rfpsFileText = "";
			Scanner scanRfpsFile = new Scanner(new BufferedReader(new FileReader(rfpsFile)));
			while (scanRfpsFile.hasNextLine())
			{
				rfpsFileText += scanRfpsFile.nextLine();
			}
			System.out.println(rfpsFileText);
		}
		catch(Exception E)
		{
			System.out.println("Could not scan RFPS file");
		}	
   }
	
	/*public  <DataStructure> DataStructure getPlotData()
	{
		return DataStructure;
	}*/
}