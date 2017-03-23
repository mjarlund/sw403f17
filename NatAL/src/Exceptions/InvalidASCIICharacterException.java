package Exceptions;

/**
 * Created by Mathias on 22-03-2017.
 */
public class InvalidASCIICharacterException extends Error
{
    public InvalidASCIICharacterException(String message)
    {
        super(message);
    }
}
