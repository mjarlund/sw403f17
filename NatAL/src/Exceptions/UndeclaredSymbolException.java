package Exceptions;

/**
 * Created by Mathias on 22-03-2017.
 */
public class UndeclaredSymbolException extends Error
{
    public UndeclaredSymbolException(String message)
    {
        super(message);
    }
    public UndeclaredSymbolException(String message, int linenumber)
    {
        super(message + " on line " + linenumber);
    }
}
