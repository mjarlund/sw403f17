package Utilities;

/**
 * Created by Mathias on 22-03-2017.
*/

public final class Reporter
{
    public static void Log (String message)
    {
        System.out.println("LOG: " + message);
    }

    // Throws a specific exception
    public static void Error (Error error)
    {
        throw error;
    }    

    public static void Warning (String message)
    {
        System.out.println("WARNING: " + message);
    }
}
