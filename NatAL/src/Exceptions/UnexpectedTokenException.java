package Exceptions;

/**
 * Created by Mathias on 22-03-2017.
 */
public class UnexpectedTokenException extends Error
{
    public UnexpectedTokenException (String message)
    {
        super(message);
    }
}