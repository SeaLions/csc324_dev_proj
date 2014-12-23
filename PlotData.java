import java.io.*;

public abstract class PlotData
{
   public PlotData()
	{
	}
	
   public abstract void readData(File plotDataFile) throws IOException;
}