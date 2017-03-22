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

    public static void Error (String message)
    {
        throw new Error("ERROR: " + message);
    }

    // Throws a specific exception
    public static void Error (Exception exception) throws Exception
    {
        throw exception;
    }

    public static void Warning (String message)
    {
        System.out.println("WARNING: " + "");
    }
}
