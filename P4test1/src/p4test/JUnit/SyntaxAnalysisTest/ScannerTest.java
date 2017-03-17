package p4test.JUnit.SyntaxAnalysisTest;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collection;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;

import p4test.SyntaxAnalysis.Scanner;
import p4test.TokenType;

@RunWith(Parameterized.class)
public class ScannerTest {
	
	private TokenType expected;
	private String input;
	private String result;
	
	Scanner scanner = new Scanner("void main() \n end main");
	
	 public void ParameterizedTest(String input, TokenType expected)
	 {
	 this.expected = expected;
	 this.input = input;
	 }
		 
	@Parameters
	public static Collection<Object[]> generateData()
	{
      return Arrays.asList(new Object[][] 
    	{
    	  {"or", TokenType.OPERATOR},
    	  {"and", TokenType.OPERATOR} 
    	});
	}
	 
	@Test
	public void NextTokenTest() {
    assertEquals("Result Keyword", TokenType.KEYWORD, scanner.nextToken().Type);
    assertEquals("Result Identifier", TokenType.IDENTIFIER, scanner.nextToken().Type);
	}
	
	@Test
	public void OutputTest(){
	Scanner testScanner = new Scanner(input);
	
	}
	
}
