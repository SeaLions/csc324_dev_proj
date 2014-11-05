import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*********************************
*    Preliminary JUnit Setup     *
**********************************
* Steps to setup JUnit in jGrasp:
*  1. Click "Tools" -> "JUnit" -> Configure
*  2. Set JUnit Home to "U:\csc324_dev_proj\JUnit_resources"
*  3. Click "OK"
*/

public class UserInputTest {

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
