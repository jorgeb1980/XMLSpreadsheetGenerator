package tests.misc;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;



@RunWith(Suite.class)
@SuiteClasses({ 
	TestBooleanFormat.class,
	TestNumberFormat.class,
	TestDateFormat.class,
	TestXmlHelper.class,
	TestPropertiesReader.class})
public class MiscTests {

}
