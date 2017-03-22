package Test.UnitTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import Test.UnitTests.SyntaxAnalysisTest.ScannerTest;
import Test.UnitTests.SyntaxAnalysisTest.ParserTest.ParserTest;

@RunWith(Suite.class)
@SuiteClasses({ParserTest.class, ScannerTest.class})
public class RunAllUnitTestsSuite {

}
