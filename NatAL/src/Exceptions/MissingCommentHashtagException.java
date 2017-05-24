package Exceptions;

/**
 * Created by Mathias on 22-03-2017.
 */
public class MissingCommentHashtagException extends Error
{
    public MissingCommentHashtagException (int lineNumber)
    {
        super("Missing # in a comment at line: " + lineNumber);
    }


}
