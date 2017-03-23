package Exceptions;

/**
 * Created by Mathias on 22-03-2017.
 */
public class InvalidIdentifierException extends Error
{
    public InvalidIdentifierException(String message)
    {
        super(message);
    }
}
