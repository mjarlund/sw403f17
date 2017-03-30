package Utilities;

import DataStructures.AST.NodeTypes.Types;
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
                return "FloatingPointLiteral";
            case STRING_LITERAL:
                return "StringLiteral";
            case CHAR_LITERAL:
                return "CharacterLiteral";
            case DIGITAL_LITERAL:
                return "DigitalLiteral";
            case EOF:
                return "$";
            case SEPARATOR:
                if (token.Value.equals("\\n"))
                    return "EOL";
            default:
                return null;
        }
    }
    // Ast types
    public static Types TypetoTypes(Token token)
    {
        switch (token.Type)
        {
            case INTEGER_LITERAL:
                return Types.INT;
            case BOOLEAN_LITERAL:
                return Types.BOOL;
            case FLOAT_LITERAL:
                return Types.FLOAT;
            case STRING_LITERAL:
                return Types.STRING;
            case CHAR_LITERAL:
            	return Types.CHAR;
            case KEYWORD:
                if(token.Value.equals("void"))
                    return Types.VOID;
            default:
                return null;
        }
    }
}