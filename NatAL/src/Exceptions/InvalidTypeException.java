package Exceptions;

import DataStructures.AST.NodeTypes.Types;

/**
 * Created by Mikkel on 10-04-2017.
 */
public class InvalidTypeException extends Error {
    public InvalidTypeException(String message) {
        super(message);
    }

    public InvalidTypeException(Object ExprType, int lineNumber) {
        super("Invalid type " + ExprType + " on line " + lineNumber);
    }

    public InvalidTypeException(Object ExprType) {
        super("Incompatible types " + ExprType);
    }
}
