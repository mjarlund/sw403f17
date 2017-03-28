package Test.UnitTests;

import Test.UnitTests.SyntaxAnalysisTest.ParserTest.ParsingTableTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import Test.UnitTests.SyntaxAnalysisTest.GrammarTest.GrammarTest;
<<<<<<< HEAD
import Test.UnitTests.SyntaxAnalysisTest.GrammarTest.LLTest;
=======
import Test.UnitTests.SyntaxAnalysisTest.GrammarTest.LLOneTest;
import Test.UnitTests.Semantics.ScopeTest;
>>>>>>> 8978b5bdad5b0c877db48478c233b09fa8b4ee72
import Test.UnitTests.SyntaxAnalysisTest.ScannerTest;
import Test.UnitTests.SyntaxAnalysisTest.ParserTest.ParserTest;

@RunWith(Suite.class)
<<<<<<< HEAD
@SuiteClasses({ParserTest.class, ScannerTest.class, ParsingTableTest.class, GrammarTest.class, LLTest.class})
=======
@SuiteClasses({ParserTest.class, ScannerTest.class, ParsingTableTest.class, GrammarTest.class, LLOneTest.class, ScopeTest.class})
>>>>>>> 8978b5bdad5b0c877db48478c233b09fa8b4ee72
public class RunAllUnitTestsSuite {

}
