package p4test.JUnit;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import p4test.Scanner;
import p4test.Token;
import p4test.TokenType;


public class ScannerTest {
	Scanner scanner = new Scanner("void main() \n end main");
	@Test
	public void test() {
    assertEquals("Result", new Token("void", TokenType.KEYWORD).Type, scanner.nextToken().Type);
	}

}
