import java.io.*;
import java.util.*;


public class SignalProData extends PlotData
{
   public SignalProData()
   {
      super();
   }
   public void readData(File signalProFile) throws IOException
   {
	//create Scanner for file and read in all the text as one large string
	try
		{
			String signalProFileText = "";
			Scanner scanSignalProFile = new Scanner(new BufferedReader(new FileReader(signalProFile)));
			//reading ten lines of file into a string to see some data for presentation
			for (int i =0;i<10;i++)
			{
				signalProFileText += scanSignalProFile.nextLine();
				signalProFileText += "\n";
			}
			System.out.println("SignalProFile in plain text "+signalProFileText);
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