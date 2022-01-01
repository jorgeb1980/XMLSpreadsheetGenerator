import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.generator.TestGenerator;
import tests.misc.MiscTests;
import tests.styles.StyleTests;

@RunWith(Suite.class)
@SuiteClasses({ 
	StyleTests.class,
	MiscTests.class,
	TestGenerator.class})
public class TestXmlSpreadsheet {

}
