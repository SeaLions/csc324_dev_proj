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


   /** Tests the example squareNum function. **/
   @Test public void squareNumFourGivesSixteen() {
      Comparison comp = new Comparison();
      Assert.assertEquals("Testing squareNum function with 4", 16, comp.squareNum(4));
   }
   
}
