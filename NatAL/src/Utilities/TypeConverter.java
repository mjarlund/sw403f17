package Utilities;

import Syntax.Tokens.Token;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class TypeConverter
{
    public static String TypeToTerminal(Token token)
    {
        /* Retrieves the comparable version of the given token
        * Based on the value of the token */
        switch (token.Type)
        {
            case IDENTIFIER:
                return "Identifier";
            case INTEGER_LITERAL:
                return "IntegerLiteral";
            case BOOLEAN_LITERAL:
                return "BooleanLiteral";
            case FLOAT_LITERAL:
                return "FloatingLiteral";
            case STRING_LITERAL:
                return "StringLiteral";
            case EOF:
                return "$";
            case SEPARATOR:
                if (token.Value.equals("\\n"))
                    return "EOL";
            default:
                return null;
        }
    }
}