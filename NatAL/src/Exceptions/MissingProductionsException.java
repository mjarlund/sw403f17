package Exceptions;

public class MissingProductionsException extends Error
{
    public MissingProductionsException (String value, Syntax.Tokens.Token token)
    {
        super("No productions available for " + value + " at line: " + token.LineNumber + " " + token.Value);
    }
}
