import static org.junit.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ Comparison.class, MySecondClassTest.class })
public class TestSuite
{


    @Test
    public void testIfTwoNumbersAreEqual()
    {
        // MyClass is tested
        Comparison compObj = new Comparison();
   
        // Tests
        assertEquals("a is 1, and b is 2 means they're not equal", 0, compObj.isEqual(1,2));
        assertEquals("a is 2, and b is 2 means they're equal", 0, compObj.isEqual(2,2));
        assertEquals("a is 2, and b is 3 means they're not equal", 0, compObj.isEqual(2,3));
    } 


   
   /******************* JUNIT Test Case **********************
   @Test
   public void multiplicationOfZeroIntegersShouldReturnZero() {
   
      // MyClass is tested
      MyClass tester = new MyClass();
   
      // Tests
      assertEquals("10 x 0 must be 0", 0, tester.multiply(10, 0));
      assertEquals("0 x 10 must be 0", 0, tester.multiply(0, 10));
      assertEquals("0 x 0 must be 0", 0, tester.multiply(0, 0));
    } 
    
    */
 
 
 
 
 
}