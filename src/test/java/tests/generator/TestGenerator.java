package tests.generator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestGeneratorStyles.class,
	TestGeneratorTransitions.class,
	TestStylesInheritance.class,
	TestGeneratorMisc.class})
public class TestGenerator {

}
