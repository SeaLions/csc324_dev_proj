import org.junit.Test;
import org.junit.Ignore;
import org.junit.Assert;
import org.junit.Before;


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

public class ComparisonTest {

	String expected = "pass";
	String actual = "pass";


   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() {
   }


   /** A test that always passes. **/
   @Test public void defaultTest() {
      Assert.assertEquals("Default test for template use", expected, actual);
   }
}