package p4test;

        import java.util.Arrays;

/**
 * Created by Mikkel on 3/2/2017.
 */
public class InvalidSyntaxException extends Exception {

    public InvalidSyntaxException(Token ReceivedToken, TokenType ... ExpectedTypes) {
        super("Invalid syntax: expected "+ Arrays.toString(ExpectedTypes) +" got " +ReceivedToken);
    }

    public InvalidSyntaxException(Token ReceivedToken, String ... ExpectedValues) {
        super("Invalid syntax: expected "+ Arrays.toString(ExpectedValues) +" got " +ReceivedToken);
    }

}