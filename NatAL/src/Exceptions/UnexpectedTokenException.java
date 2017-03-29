package Exceptions;

/**
 * Created by Mathias on 22-03-2017.
 */
public class UnexpectedTokenException extends Error
{
    public UnexpectedTokenException (Syntax.Tokens.Token token, String expected)
    {
        super("Got " + token.Value + " expected " + expected + ". Line: " + token.LineNumber);
    }


}
