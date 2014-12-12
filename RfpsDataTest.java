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

   /****************************************************************/
   /*             NEED TO REFACTOR FOR NEW CLASS LOGIC             */
   /****************************************************************/
   
/*
	//not yet implemented
	@Test public void wrongFileTypeTest()
	{
		//create an instance where user selects the wrong type of file, and see how readData deals with this
		//Assert(rfpsData.readData(wrongFileType);
		
	}
	//Test that checks single bearing coordinates that has three  
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
		String FirstCoordinate= "-118.377330,39.068387,0.000 -118.678860,39.052595,0.000 -118.974841,39.005509,0.000 -119.259852,38.927996,0.000 -119.528713,38.821477,0.000 -119.776600,38.687897,0.000 -119.999142,38.529683,0.000 -120.192505,38.349691,0.000 -120.353455,38.151147,0.000 -120.479409,37.937579,0.000 -120.568461,37.712751,0.000 -120.619398,37.480590,0.000 -120.631696,37.245113,0.000 -120.605503,37.010358,0.000 -120.541616,36.780311,0.000 -120.441438,36.558847,0.000 -120.306940,36.349664,0.000 -120.140611,36.156228,0.000 -119.945403,35.981720,0.000 -119.724677,35.828992,0.000 -119.482149,35.700526,0.000 -119.221824,35.598399,0.000 -118.947947,35.524258,0.000 -118.664937,35.479292,0.000 -118.377330,35.464223,0.000 -118.089723,35.479292,0.000 -117.806713,35.524258,0.000 -117.532836,35.598399,0.000 -117.272511,35.700526,0.000 -117.029983,35.828992,0.000 -116.809257,35.981720,0.000 -116.614049,36.156228,0.000 -116.447720,36.349664,0.000 -116.313222,36.558847,0.000 -116.213044,36.780311,0.000 -116.149157,37.010358,0.000 -116.122964,37.245113,0.000 -116.135262,37.480590,0.000 -116.186199,37.712751,0.000 -116.275251,37.937579,0.000 -116.401205,38.151147,0.000 -116.562155,38.349691,0.000 -116.755518,38.529683,0.000 -116.978060,38.687897,0.000 -117.225947,38.821477,0.000 -117.494808,38.927996,0.000 -117.779819,39.005509,0.000 -118.075800,39.052595,0.000 -118.377330,39.068387,0.000 ";
		
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

}

	