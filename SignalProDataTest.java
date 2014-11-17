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
   File signalProFile;
   SignalProData signalProData;
   InputStream testCoordinates;
   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() throws IOException
   {
		expected = 1;
		actual = 1;
      signalProData = new SignalProData();

      //create signal pro file
      try
      {
         signalProFile = new File("SignalProtestCaseFlorida.kml");
      }catch(Exception E)
      {
      System.out.println("file could not be found ");
      }
   }


  /** A test that always passes. **/
   @Test public void defaultTest() {
      Assert.assertEquals("Default test template that always passes.", expected, actual);
   }
   
   /** Test only the KML File **/
   @Test public void TestAKmlFileFromKmz()
   {
      signalProData.readData(signalProFile);
      signalProData.getKMLInformation(testCoordinates);      
   }
}
