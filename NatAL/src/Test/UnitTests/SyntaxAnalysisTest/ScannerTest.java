package Test.UnitTests.SyntaxAnalysisTest;

import Exceptions.*;
import Syntax.Tokens.Token;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;

import Syntax.Parser.Parser;
import Syntax.Scanner.Scanner;
import Syntax.Tokens.TokenType;
import Test.InputTester;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(Enclosed.class)
public class ScannerTest {

	@RunWith(Parameterized.class)
	public static class SingleInputTests
	{
		private TokenType expected;
		private String inputString;

		public SingleInputTests(String input, TokenType expectedToken)
		{
			this.inputString = input;
			this.expected = expectedToken;
		}

		@Parameters(name = "{index}: Scan({0})={1}")
		public static Collection<Object[]> generateData()
		{
			return Arrays.asList(new Object[][]{
					//Operators
					{"or", TokenType.OPERATOR},
					{"and", TokenType.OPERATOR},
					{"equals", TokenType.OPERATOR},
					{"not", TokenType.OPERATOR},
					{"below", TokenType.OPERATOR},
					{"is", TokenType.OPERATOR},
					{"above", TokenType.OPERATOR},
					{"-", TokenType.OPERATOR},
					{"+", TokenType.OPERATOR},
					{"/", TokenType.OPERATOR},
					{"*", TokenType.OPERATOR},

					//Keywords
					{"until", TokenType.KEYWORD},
					{"if", TokenType.KEYWORD},
					{"string", TokenType.KEYWORD},
					{"return", TokenType.KEYWORD},
					{"structure", TokenType.KEYWORD},
					{"number", TokenType.KEYWORD},
					{"character", TokenType.KEYWORD},
					{"end", TokenType.KEYWORD},
					{"else", TokenType.KEYWORD},
					{"boolean", TokenType.KEYWORD},
					{"void", TokenType.KEYWORD},
					{"fraction", TokenType.KEYWORD},
					{"text", TokenType.KEYWORD},

					//Booleans
					{"true", TokenType.BOOLEAN_LITERAL},
					{"false", TokenType.BOOLEAN_LITERAL},

					//

					//Identifiers
					{"jajajaj", TokenType.IDENTIFIER},
					{"j", TokenType.IDENTIFIER},
					{"z84", TokenType.IDENTIFIER},

					//Integer Literals
					{"1", TokenType.INTEGER_LITERAL},
					{"0", TokenType.INTEGER_LITERAL},
					{"1000000000000000", TokenType.INTEGER_LITERAL},
					
					//Float Literals
					{"1.0292", TokenType.FLOAT_LITERAL},
					//{".0292", TokenType.FLOAT_LITERAL},
					//{"1.", TokenType.FLOAT_LITERAL},
					//{"9961.9haha292", TokenType.FLOAT_LITERAL},
					{"9991.12292", TokenType.FLOAT_LITERAL},

					//String Literals
					{"\"hejmor63\"", TokenType.STRING_LITERAL},
					{"\"hejmor..,.63\"", TokenType.STRING_LITERAL},
					{"\"hejm o383r63\"", TokenType.STRING_LITERAL},
					{"\"hejm \n ???` ?#*^+or63\"", TokenType.STRING_LITERAL},//mangler newline token 
					{"\"hej#�&/()=/())=?mor63\"", TokenType.STRING_LITERAL},
					{"\"hej "+ System.lineSeparator() +"mor63\"", TokenType.STRING_LITERAL},
					
					//Char Literals
					{"'A'",TokenType.CHAR_LITERAL},
					{"'Z'",TokenType.CHAR_LITERAL},
					{"'a'",TokenType.CHAR_LITERAL},
					{"'z'",TokenType.CHAR_LITERAL},
					{"'w'",TokenType.CHAR_LITERAL},
					{"'U'",TokenType.CHAR_LITERAL},
					
					
					//Separators
					{"\n", TokenType.SEPARATOR},
					

			});
		}

		@Test
		public void SingleOutputTest() throws Exception
		{
			Scanner testScanner = new Scanner(inputString);
			assertEquals(expected, testScanner.NextToken().Type);
			assertEquals(TokenType.EOF, testScanner.NextToken().Type);
		}


	}


	public static class MultipleInputTests
	{

		@Test
		public void MinusIntegerTest()
		{
			Scanner multipleInputScanner = new Scanner("-999999");
			assertEquals(TokenType.OPERATOR, multipleInputScanner.NextToken().Type);
			assertEquals(TokenType.INTEGER_LITERAL, multipleInputScanner.NextToken().Type);
		}
		
		@Test
		public void separatorTest()
		{
			Scanner scanner = new Scanner("(hello, world)");
			assertEquals(TokenType.SEPARATOR, scanner.NextToken().Type);
			assertEquals(TokenType.IDENTIFIER, scanner.NextToken().Type);
			assertEquals(TokenType.SEPARATOR, scanner.NextToken().Type);
			assertEquals(TokenType.IDENTIFIER, scanner.NextToken().Type);
			assertEquals(TokenType.SEPARATOR, scanner.NextToken().Type);
		}
		
		@Test
		public void AccessorTest()
		{
			Scanner scanner = new Scanner("Accessor.Test");
			assertEquals(TokenType.IDENTIFIER, scanner.NextToken().Type);
			assertEquals(TokenType.ACCESSOR, scanner.NextToken().Type);
			assertEquals(TokenType.IDENTIFIER, scanner.NextToken().Type);
		}

		
		@Test
		public void EOFTest()
		{
			Scanner multipleInputScanner = new Scanner("main void");
			assertEquals(TokenType.IDENTIFIER, multipleInputScanner.NextToken().Type);
			assertEquals(TokenType.KEYWORD, multipleInputScanner.NextToken().Type);
			assertEquals(TokenType.EOF, multipleInputScanner.NextToken().Type);
		}

		@Test
		public void IdentifierDoesNotThrowExceptionTest()
		{
			try
			{
				Scanner testScanner = new Scanner("iaris3383ialak");
				while(true)
				{
					Token currentToken = testScanner.NextToken();
					if(currentToken.Type == TokenType.EOF)
						break;
				}
			}
			catch(Throwable e)
			{
				fail("ups something went wrong exception was thrown");
			}
		}		

		@Test
		public void ScannerRecognizesNewLineTest()
		{
			Scanner testScanner = new Scanner("void main" + System.lineSeparator() + "another line");
			assertEquals(TokenType.KEYWORD, testScanner.NextToken().Type);
			assertEquals(TokenType.IDENTIFIER, testScanner.NextToken().Type);
			assertEquals(TokenType.SEPARATOR, testScanner.NextToken().Type);
			assertEquals(TokenType.IDENTIFIER, testScanner.NextToken().Type);
			assertEquals(TokenType.IDENTIFIER, testScanner.NextToken().Type);
		}
	}

	public static class ErrorHandlingTests
	{

		@Rule
		public final ExpectedException exception = ExpectedException.none();

		@Test
		public void EmptyStringThrowsErrorTest()
		{
			exception.expect(StringIndexOutOfBoundsException.class);
			Scanner testScanner = new Scanner("");
		}
		
		
		@Test
		public void EmptyDocumentThrowsErrorTest() throws IOException
		{
			boolean thrown = false;
				
			try 
			{
				Scanner testScanner = new Scanner(InputTester.readFile("src/Test/TestPrograms/Testfail.txt"));
			} 
			catch (StringIndexOutOfBoundsException e){
				thrown = true;
			}
			assertEquals(true, thrown);
		}
		

		@Test
		public void IntegerThenLettersErrorTest()
		{
			try
			{
				Scanner testScanner = new Scanner("999aabfj");
				while(true)
				{
					testScanner.NextToken();
				}
			}
			catch(InvalidIdentifierException e)
			{
				// Der er sikkert en smartere måde at gøre dette på, men det virker - Math
				assertEquals(true, true);
			}
		}
		
		@Test
		public void NotCorrectIdentifierThowsErrorTest(){
			try
			{
				Scanner testScanner = new Scanner("abc?#(");
				while(true)
				{
					testScanner.NextToken();
				}
			}
			catch(InvalidCharacterSequenceException e)
			{
				// Der er sikkert en smartere måde at gøre dette på, men det virker - Mathias
				assertEquals(true, true);
			}
		}
	}
}
