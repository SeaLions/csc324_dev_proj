import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.*;


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


public class SignalProDataTest {
	int expected;
	int actual;
	SignalProData signalProData;
	File signalProFile;

   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() {
		expected = 1;
		actual = 1;
      signalProData = new SignalProData();

      //create signal pro file
      try
      {
         signalProFile = new File("TestCaseFlorida.kmz");
      }catch(Exception E)
      {
      System.out.println("file could not be found ");
      }

   }


  /** A test that always passes. **/
   @Test public void defaultTest() {
      Assert.assertEquals("Default test template that always passes.", expected, actual);
   }
   
   /** Test the isCoverageNear method **/
   @Test public void TestIsCoverageNearMethod()
   {
		double lon = 1;
		double lat = 0;
		double distanceInMeters = 10;
      signalProData.readData(signalProFile);
      boolean test = signalProData.isCoverageNear(lon,lat,distanceInMeters);      
 		Assert.assertEquals(true,test);  
	}

}
