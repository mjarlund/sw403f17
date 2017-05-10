package Utilities;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Expressions.BoolExpr;
import Exceptions.IncompatibleValueException;

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
    public static void Error (ReportTypes type, AST node){
        String message = "On line: " + node.GetLineNumber() + "\n";
        switch (type){
            case NonBoolArgsInBoolExprError:
                message += "Arguments on both sides of the \"" + ((BoolExpr)node).Operator.Value + "\" operator must be boolean types.";
                throw new IncompatibleValueException(message);
            case NonNumericArgsInBoolExprError:
                message += "Arguments on both sides of the \"" + ((BoolExpr)node).Operator.Value + "\" operator must be numeric values.";
                throw new IncompatibleValueException(message);
            case IncompatibleTypesInEqualityExprError:
                message += "Different types can never be equal one another. ";
                throw new IncompatibleValueException(message);
        }
    }

    public static void Warning (String message)
    {
        System.out.println("WARNING: " + message);
    }
}
