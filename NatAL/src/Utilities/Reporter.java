package Utilities;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Expressions.BinaryOPExpr;
import DataStructures.AST.NodeTypes.Expressions.BoolExpr;
import DataStructures.AST.NodeTypes.Expressions.FuncCallExpr;
import DataStructures.AST.NodeTypes.Expressions.IdExpr;
import DataStructures.AST.NodeTypes.Statements.ForEachStmt;
import Exceptions.*;

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
    
    public static void Error (ReportTypes type, String EssentialPart){
    	
    	String message;
    	
    	switch (type){
    		case MissingEssentialMethodError:
    			message = "Missing: " + EssentialPart + "() method" ;
    			throw new UndeclaredSymbolException(message);
            case EssentialMethodNotVoidError:
            	message = EssentialPart + " Has to be of type void and can not have a return value"; 
            	throw new InvalidTypeException(message);
            case EssentialMethodHasParamsError:
            	message = "The " + EssentialPart + " can not have any parameters";
                throw new ArgumentsException(message);
            case MisuseOfLoopOrSetupError:
            	message = EssentialPart + " is an essential method and has to be declared as such";
                throw new InvalidTypeException(message);

    	}
    }
    
    public static void Error (ReportTypes type, AST node){

        String message = "On line " + node.GetLineNumber() + ":\n";

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
            case NonBoolTypeInBooleanNegationError:
                message += "Only boolean types can be negated by the \"not\" operator. ";
                throw new IncompatibleValueException(message);
            case NonNumericTypeInNumericNegationError:
                message += "Only numeric values can be negated by a \"-\" operator. ";
                throw new IncompatibleValueException(message);
            case NonStringTypeInStringConcatError:
                message += "You can only concatenate text values onto a text value. ";
                throw new IncompatibleValueException(message);
            case NonNumericTypesInBinaryOPExprError:
                message += "Arguments on both sides of the \"" + ((BinaryOPExpr)node).Operation.Value + "\" operator must be numeric values";
                throw new IncompatibleValueException(message);
            case NonPinTypeInIOStatementError:
                message += "Read and Write commands are only possible with \"pin\" types. ";
                throw new IncompatibleValueException(message);
            case NotAssignableError:
                message += "Left hand side of assignment is not an assignable variable. ";
                throw new InvalidIdentifierException(message);
            case NonBooleanConditionError:
                message += "A condition must be a boolean expression. ";
                throw new InvalidTypeException(message);
            case MissingExpressionInIOStmtError:
                message += "You must specify what to write to a pin when using Write statements. ";
                throw new ArgumentsException(message);
            case NonDigitalValueInDigitalIOStmtError:
                message += "You can only use HIGH or LOW when writing digitally to a pin. ";
                throw new ArgumentsException(message);
            case NonIntValueInAnalogIOStmtError:
                message += "You can only use \"number\" type values when writing analogically to a pin. ";
                throw new ArgumentsException(message);
            case IdentifierNotDeclaredError:
                String id = "An identifier";
                if (node instanceof FuncCallExpr){
                    id = ((FuncCallExpr) node).GetFuncId().GetValue();
                } else if (node instanceof IdExpr)
                    id = ((IdExpr) node).ID;
                message += id + " has not been declared in a scope that is reachable from here. ";
                throw new UndeclaredSymbolException(message);
            case FuncCallAsFuncDclError:
                id = ((FuncCallExpr)node).GetFuncId().GetValue();
                message += id + " is not used as a function call. ";
                throw new InvalidScopeException(message);
            case TooFewArgumentsError:
                id = ((FuncCallExpr)node).GetFuncId().GetValue();
                message += "Too few arguments when calling " + id;
                throw new ArgumentsException(message);
            case TooManyArgumentsError:
                id = ((FuncCallExpr)node).GetFuncId().GetValue();
                message += "Too many arguments when calling " + id;
                throw new ArgumentsException(message);
            case IncompatibleTypeArgumentError:
                id = ((FuncCallExpr)node).GetFuncId().GetValue();
                message += "Wrong types in arguments when calling " + id;
                throw new ArgumentsException(message);
            case NonCollectionSubjectInForeachError:
                id = ((ForEachStmt)node).GetCollectionId();
                message += id + " is not a collection of elements and cannot be iterated over. ";
                throw new ArgumentsException(message);
            case IncompatibleElementTypeInForeachError:
                id = ((ForEachStmt)node).GetElementId();
                message += id + " is not the same type as the elements in " + ((ForEachStmt)node).GetCollectionId();
                throw new ArgumentsException(message);
            case NonIntegerIteratorInRepeatError:
                message += "The iterator of a repeat statement must be of type \"number\"";
                throw new ArgumentsException(message);
            case FuncIdUsedAsVarIdError:
                id = ((IdExpr)node).ID;
                message += id + " is used as a variable but is a function. ";
                throw new InvalidTypeException(message);
        }
    }

    public static void Warning (String message)
    {
        System.out.println("WARNING: " + message);
    }
}
