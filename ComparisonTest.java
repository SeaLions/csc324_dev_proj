import org.junit.Assert;
import static org.junit.Assert.*;
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

public class ComparisonTest {


   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() {
   }


   /** A test that always fails. **/
   @Test public void defaultTest() {
      Assert.assertEquals("Default test added by jGRASP. Delete "
            + "this test once you have added your own.", 0, 1);
   }
}
