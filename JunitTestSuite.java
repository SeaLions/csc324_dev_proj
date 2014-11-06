import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//JUnit Suite Test
@RunWith(Suite.class)
@Suite.SuiteClasses({ 
   ComparisonTest.class,
	UserInputTest.class,
	RfpsDataTest.class,
	SignalProDataTest.class
})
public class JunitTestSuite {

	//don't need to fill with anything, only
	//need to add the test class to the SuiteClasses
	//array above
	
}