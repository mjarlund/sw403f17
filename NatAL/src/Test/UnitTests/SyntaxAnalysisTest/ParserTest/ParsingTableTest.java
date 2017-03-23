package Test.UnitTests.SyntaxAnalysisTest.ParserTest;

import static org.junit.Assert.*;

import java.io.EOFException;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import Syntax.Parser.ParsingTable;
import Syntax.Scanner.Scanner;
import Syntax.Tokens.Token;
import Syntax.Tokens.TokenType;

@RunWith(Enclosed.class)
public class ParsingTableTest {

		@RunWith(Parameterized.class)
		public static class TerminalInputTests
		{
			private boolean expected;
			private String inputString;

			public TerminalInputTests(String input, boolean expectedBool)
			{
				this.inputString = input;
				this.expected = expectedBool;
			}

			@Parameters(name = "{index}: Scan({0})={1}")
			public static Collection<Object[]> generateData()
			{
				return Arrays.asList(new Object[][]{
						//Operators
						{"Statement", false},
						{"Statements", false},
						{"1", true},
						{"EPSILON", true},
				});
			}
		
		@Test	
		public void IsTerminal() {
			ParsingTable p = new ParsingTable("src/Test/GrammaTestFile");
			assertEquals(expected, p.IsTerminal(inputString));
		} 
	}
	@RunWith(Parameterized.class)
	public static class EpsilonInputTests
	{
		private boolean expected;
		private String inputString;

		public EpsilonInputTests(String input, boolean expectedBool)
		{
			this.inputString = input;
			this.expected = expectedBool;
		}

		@Parameters(name = "{index}: Scan({0})={1}")
		public static Collection<Object[]> generateData()
		{
			return Arrays.asList(new Object[][]{
					//Operators
					{"Statement", true},
					{"Statements", true},
					{"1", false},
					{"EPSILON", true},
			});
		}
		@Test
		public void IsEpsilon() {
			ParsingTable p = new ParsingTable("src/Test/GrammaTestFile");
			assertEquals(expected, p.IsEpsilon(inputString));
		}
	}

	@RunWith(Parameterized.class)
	public static class PredictionStringTests
	{
		private String token;
		private String nonterminal;
		private String expected;

		public PredictionStringTests(String inputOne, String inputToken, String expectedProduction)
		{
			this.nonterminal = inputOne;
			this.token = inputToken;
			this.expected = expectedProduction;
		}

		@Parameters(name = "{index}: Scan({0})={1}")
		public static Collection<Object[]> generateData()
		{
			return Arrays.asList(new Object[][]{
					//Operators
					{"Statement", "EPSILON","Statement -> EPSILON"},
					{"Statement", "1","Statement -> 1"},
					{"Statements","1", "Statements -> Statement Statements"},
			});
		}

		@Test
		public void testGetPredictionStringToken() {
			ParsingTable p = new ParsingTable("src/Test/GrammaTestFile");
			assertEquals(expected, p.GetPrediction(nonterminal, token).toString());
		}
	}
	
	@RunWith(Parameterized.class)
	public static class PredictionStringTokenTests
	{
		private Token token;
		private String nonterminal;
		private String expected;

		public PredictionStringTokenTests(String inputOne, Token inputToken, String expectedProduction)
		{
			this.nonterminal = inputOne;
			this.token = inputToken;
			this.expected = expectedProduction;
		}

		@Parameters(name = "{index}: Scan({0})={1}")
		public static Collection<Object[]> generateData()
		{
			return Arrays.asList(new Object[][]{
					
					{"Statement", new Token("1",TokenType.KEYWORD),"Statement -> 1"},
					{"Statement", new Token("+",TokenType.OPERATOR),"Statement -> + Statement"},
					{"Statement", new Token("IntegerLiteral",TokenType.INTEGER_LITERAL),"Statement -> IntegerLiteral"},

			});
		}
		@Test
		public void testGetPredictionStringToken() {
			ParsingTable p = new ParsingTable("src/Test/GrammaTestFile");
			assertEquals(expected, p.GetPrediction(nonterminal, token).toString());
		}
	}
	
	public static class ErrorHandlingTests
	{
		@Test
		public void GetPredictionErrorTest()
		{
				ParsingTable testParsingTable = new ParsingTable("src/Test/GrammaTestFile");		
				assertEquals(testParsingTable.GetPrediction("Statements", "2"), null);
		}
	}
}
