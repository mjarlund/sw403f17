package p4test.JUnit.SyntaxAnalysisTest;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collection;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;

import p4test.SyntaxAnalysis.Scanner;
import p4test.TokenType;

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
				new Object[]{"or", TokenType.OPERATOR}, 
				new Object[]{"and", TokenType.OPERATOR},
				new Object[]{"equals", TokenType.OPERATOR},
				new Object[]{"not", TokenType.OPERATOR},
				new Object[]{"below", TokenType.OPERATOR},
				new Object[]{"is", TokenType.OPERATOR},
				new Object[]{"above", TokenType.OPERATOR},
				new Object[]{"-", TokenType.OPERATOR},
				new Object[]{"+", TokenType.OPERATOR},
				new Object[]{"/", TokenType.OPERATOR},
				new Object[]{"*", TokenType.OPERATOR},
				
				//Keywords
				new Object[]{"until", TokenType.KEYWORD},
				new Object[]{"if", TokenType.KEYWORD},
				new Object[]{"string", TokenType.KEYWORD},
				new Object[]{"return", TokenType.KEYWORD},
				new Object[]{"structure", TokenType.KEYWORD},
				new Object[]{"number", TokenType.KEYWORD},
				new Object[]{"character", TokenType.KEYWORD},
				new Object[]{"end", TokenType.KEYWORD},
				new Object[]{"else", TokenType.KEYWORD},
				new Object[]{"boolean", TokenType.KEYWORD},
				new Object[]{"void", TokenType.KEYWORD},
				new Object[]{"fraction", TokenType.KEYWORD},
				new Object[]{"text", TokenType.KEYWORD},
				
				//Booleans
				new Object[]{"true", TokenType.BOOLEAN_LITERAL},
				new Object[]{"false", TokenType.BOOLEAN_LITERAL},
				
				//
				
				//Identifiers
				new Object[]{"jajajaj", TokenType.IDENTIFIER},
				new Object[]{"j", TokenType.IDENTIFIER},
				
				//Integer Literals
				new Object[]{"1", TokenType.INTEGER_LITERAL},
				new Object[]{"0", TokenType.INTEGER_LITERAL},
				new Object[]{"1000000000000000", TokenType.INTEGER_LITERAL},
				//new Object[]{"999abn", TokenType.INTEGER_LITERAL},
			
			});
		}
		
		@Test
		public void SingleOutputTest()
		{
			Scanner testScanner = new Scanner(inputString);
			assertEquals(expected, testScanner.NextToken().Type);
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
        public void EOFTest()
        {
        	Scanner multipleInputScanner = new Scanner("main void");
        	assertEquals(TokenType.IDENTIFIER, multipleInputScanner.NextToken().Type);
        	assertEquals(TokenType.KEYWORD, multipleInputScanner.NextToken().Type);        	
        	assertEquals(TokenType.EOF, multipleInputScanner.NextToken().Type);
        }
        
        @Test
        public void IntegerThenLettersTest()
        {
        	Scanner testScanner = new Scanner("999aabfj");
        	assertEquals(TokenType.INTEGER_LITERAL, testScanner.NextToken().Type);
        	assertEquals(TokenType.IDENTIFIER, testScanner.NextToken().Type);        	
        	assertEquals(TokenType.EOF, testScanner.NextToken().Type);
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
    }
}
