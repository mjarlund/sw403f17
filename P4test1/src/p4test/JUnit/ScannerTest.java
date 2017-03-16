package p4test.JUnit;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import p4test.SyntaxAnalysis.Scanner;
import p4test.TokenType;


public class ScannerTest {
	Scanner scanner = new Scanner("void main() \n end main");
	
	@Test
	public void NextTokenTest() {
    assertEquals("Result Keyword", TokenType.KEYWORD, scanner.nextToken().Type);
    assertEquals("Result Identifier", TokenType.IDENTIFIER, scanner.nextToken().Type);
	}
}
