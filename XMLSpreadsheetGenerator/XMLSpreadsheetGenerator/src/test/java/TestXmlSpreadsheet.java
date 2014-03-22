import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.misc.MiscTests;
import tests.generator.TestGenerator;
import tests.styles.StyleTests;
import tests.templates.TestTemplates;



@RunWith(Suite.class)
@SuiteClasses({ 
	StyleTests.class,
	MiscTests.class,
	TestTemplates.class,
	TestGenerator.class})
public class TestXmlSpreadsheet {

}
