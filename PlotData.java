import java.io.*;

public abstract class PlotData
{

   public PlotData()
	{
	}
	
   public abstract void readData(File plotDataFile) throws IOException;
	
	//possible generic datastructure function 
	//public abstract <DataStructure> DataStructure getPlotData();
}