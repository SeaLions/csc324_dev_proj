import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import junit.framework.*;
import java.util.Vector;
/*********************************
*    Preliminary JUnit Setup     *
**********************************
* Steps to setup JUnit in jGrasp:
*  1. Click "Tools" -> "JUnit" -> Configure
*  2. Set JUnit Home to "U:\csc324_dev_proj\JUnit_resources"
*  3. Click "OK"
*
* May need to add to Workspace CLASSPATH
*  1. Under Settings -> PATH/CLASSPATH -> Workspace
*  2. Switch to the CLASSPATHS tab
*  3. Click 'NEW' and select the junit-4.10 file
*  4. Click 'NEW' again and select the hamcrest-core-1.3
*  5. Then click 'APPLY' at the bottom and 'OK'
*/


public class RfpsDataTest 
{
	int expected;
	int actual;
   RfpsData rfpsData;
   File fullSizeTestKml,bearingTestKml,twoBearingTestKml,wrongFileType;
	
   
   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp()throws IOException 
   {
		expected = 1;
		actual = 1;
      rfpsData = new RfpsData();
		
      try
      {
         bearingTestKml = new File("bearingTestKml.kml");
			twoBearingTestKml = new File("twoBearingTestKml.kml");
	      wrongFileType = new File("fakeFile.doc");
			fullSizeTestKml = new File("fullSizeTestKml.kml");
      }
      catch(Exception E)
      {
         System.out.println("one of the kml files was not found");
      }   
   }
    
   //testing coverage near point method for 1 point
   @Test public void TestCoverageNearPointMethod()
	{
      rfpsData.readData(fullSizeTestKml);
      float lat1 = 37.541481f;
      float lon1 = -118.37733f;
      Coordinate testPoint = new Coordinate(lat1,lon1);
      //this point Lat,Lon was taken from printing out one of the points of PTSarr from fullSizeTestKml
 		boolean testAPoint = rfpsData.coverageNearPoint(testPoint, 100.0);
      Assert.assertEquals(true,testAPoint);
	}

}

	