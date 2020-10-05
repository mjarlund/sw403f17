package Exceptions;

public class UnexpectedTokenException extends Error
{
    public UnexpectedTokenException (Syntax.Tokens.Token token, String expected)
    {
        super("Got " + token.Value + " expected " + expected + ". Line: " + token.LineNumber);
    }
}
