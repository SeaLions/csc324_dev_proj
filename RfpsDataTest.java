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
   }/*
	//not yet implemented
	@Test public void wrongFileTypeTest()
	{
		//create an instance where user selects the wrong type of file, and see how readData deals with this
		//Assert(rfpsData.readData(wrongFileType);
		
	}*/
	//Test that checks single bearing coordinates that has three  
   /*
	@Test public void TestABearingsCoordinates()
	{
		rfpsData.readData(bearingTestKml);
		
		Vector<String> bearingTestVector = new Vector<String>();
		
		bearingTestVector.add("-118.377330,37.266580,2.0 -118.377330,37.489291,2.0 ");
		bearingTestVector.add("-118.377330,37.492557,2.0 -118.377330,37.507514,2.0 ");
		bearingTestVector.add("-118.377330,37.541481,2.0 -118.377330,37.543494,2.0 ");
		
	}
	//Test that tests two bearing coordinates should be a vector of size two with vector of strings each vector of strings has three strings representing coordinates in it. 
	@Test public void Test2BearingsCoordinates()
	{
		rfpsData.readData(twoBearingTestKml);
      
		Vector<String> bearingTestVector1 = new Vector<String>();
      
      Vector<String> bearingTestVector2 = new Vector<String>();
      
      Vector<Vector<String>> testRfpsData = new Vector<Vector<String>>();
      
		//add three strings from bearing 0 of testKml file
		bearingTestVector1.add("-118.377330,37.266580,2.0 -118.377330,37.489291,2.0 ");
		bearingTestVector1.add("-118.377330,37.492557,2.0 -118.377330,37.507514,2.0 ");
		bearingTestVector1.add("-118.377330,37.541481,2.0 -118.377330,37.543494,2.0 ");
      
      bearingTestVector2.add("-81.775640,27.343390,2.0 -81.769918,27.635348,2.0 ");
		bearingTestVector2.add("-81.769892,27.636685,2.0 -81.769507,27.656251,2.0 ");
		bearingTestVector2.add("-81.769484,27.657442,2.0 -81.769398,27.661800,2.0 ");
      
      testRfpsData.add(bearingTestVector1);
      testRfpsData.add(bearingTestVector2);  
      
		Assert.assertEquals(testRfpsData, rfpsData.getData());
	}
	//tests first coordinate of all bearings in a file in this case we only test two coordinates
	@Test public void TestFirstCoordinateOfBearing()
	{
		String firstBearingFirstCoordinate = "-118.377330,37.266580,2.0 -118.377330,37.489291,2.0 ";
		String secondBearingFirstCoordinate= "-81.775640,27.343390,2.0 -81.769918,27.635348,2.0 ";
		rfpsData.readData(twoBearingTestKml);
		Assert.assertEquals(firstBearingFirstCoordinate,rfpsData.getData().get(0).get(0));
		Assert.assertEquals(secondBearingFirstCoordinate,rfpsData.getData().get(1).get(0));
	}
	//Testing first coordinate in a kml file equals first coordinate of first bearing
	@Test public void TestFirstCoordinateOfKML()
	{
		String FirstCoordinate= "-118.377330,37.266580,2.0 -118.377330,37.489291,2.0 ";
		
		rfpsData.readData(fullSizeTestKml);
		Assert.assertEquals(FirstCoordinate,rfpsData.getData().get(0).get(0));
	}
   */
   
   //testing coverage near point method for 1 point
   @Test public void TestCoverageNearPointMethod()
	{
      rfpsData.readData(fullSizeTestKml);
      //this point Lat,Lon was taken from printing out one of the points of PTSarr from fullSizeTestKml
 		boolean testAPoint = rfpsData.coverageNearPoint(-118.377330, 37.541481, 100.0);
      Assert.assertEquals(true,testAPoint);
	}
	/*
	//write a test that is a point in between two coverage lines along the same bearing
	@Test public void TestCoverNearPointMethodToFalse()
	{
		rfpsData.readData(twoBearingTestKml);
		//find a point that should NOT have coverage in twoBearingTestKML
		//the point selected is in between two of the coverage lines that are on the same bearing, 
		//longitude is the same but latitude is different by greater than 10 meters 
		boolean testPointFail = rfpsData.coverageNearPoint(-118.37733,37.52111,10);
		Assert.assertEquals(false,testPointFail);
	}
	//test two bearings on same kml file
	@Test public void TestCoverageNearPointMethod4TwoBearings()
	{
		rfpsData.readData(twoBearingTestKml);
		boolean testPoint1 = rfpsData.coverageNearPoint(-118.37733, 37.266580, 10.0);
		boolean testPoint2 = rfpsData.coverageNearPoint(-81.775640, 27.343390, 10.0);
		
		Assert.assertEquals(true,testPoint1);
		Assert.assertEquals(true,testPoint2);
	}
	//test greater range of distance 
	@Test public void TestCoverageAtdistanceOf100Meters()
	{
		rfpsData.readData(twoBearingTestKml);
	}
   */
}

