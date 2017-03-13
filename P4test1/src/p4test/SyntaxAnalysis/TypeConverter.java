package p4test.SyntaxAnalysis;

import p4test.Token;
import p4test.TokenType;

/**
 * Created by mysjkin on 3/13/17.
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
            default:
                return null;
        }
    }
}
