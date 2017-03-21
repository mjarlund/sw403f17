package p4test.JUnit.SyntaxAnalysisTest;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collection;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;

import p4test.SyntaxAnalysis.Scanner;
import p4test.TokenType;

@RunWith(Parameterized.class)
public class ScannerTest {
	
	 private TokenType expected;
	 private String inputString;

	 public ScannerTest(String input, TokenType expectedToken)
	 {
	 this.inputString = input;
	 this.expected = expectedToken;
	 }
		 
	@Parameters
	public static Collection<Object[]> generateData()
	{
		return Arrays.asList(new Object[][]{
			new Object[]{"or", TokenType.OPERATOR}, 
			new Object[]{"and", TokenType.OPERATOR},
			new Object[]{"jajajaj", TokenType.IDENTIFIER}
			});
	}
	
	@Test
	public void OutputTest(){
	Scanner testScanner = new Scanner(inputString);
	assertEquals(expected, testScanner.NextToken().Type);
	}
	
}
