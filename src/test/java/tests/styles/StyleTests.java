package tests.styles;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestAlignment.class, 
	TestBorders.class, 
	TestFont.class, 
	TestInterior.class,
	TestNumberFormat.class,
	TestProtection.class})
public class StyleTests {

}
