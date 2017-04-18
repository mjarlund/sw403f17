package Utilities;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Statements.*;

import java.util.List;

/**
 * Created by mysjkin on 4/12/17.
 */
public class VisitorDriver
{
    private IVisitor visit;
    public VisitorDriver(IVisitor visit)
    {
        this.visit = visit;
    }
    public Object Visit(String astName, AST child)
    {
        Object returnValue = null;
        switch (astName){
            case "VarDcl": /* Add it to this scope (works for FuncDcls too) */
                returnValue = visit.Visit((VarDcl) child);
                break;
            case "IdExpr": /* Just check whether or not it's there */
                returnValue =  visit.Visit((IdExpr) child);
                break;
            case "BlockStmt": /* Open a new scope, continue */
                visit.Visit((BlockStmt) child);
                break;
            case "FParamsDcl": /* Used as declarations, add them to that scope */
                visit.Visit((FParamsDcl) child);
                break;
            case "ArgsExpr": /* Not much different from IDs */
                visit.Visit((ArgsExpr) child);
                break;
            case "FuncDcl": /* Only in global scope. Open scope so its formal parameters
                                 * are not seen as symbols in the global scope. */
                visit.Visit((FuncDcl) child);
                break;
            case "ListDcl":
                visit.Visit((ListDcl) child);
                break;
            case "ValExpr":
                returnValue = visit.Visit((ValExpr) child);
            case "AssignStmt":
                visit.Visit((AssignStmt) child);
                break;
            case "BinaryOPExpr":
                returnValue = visit.Visit((BinaryOPExpr)child);
                break;
            case "UnaryExpr":
                returnValue = visit.Visit((UnaryExpr)child);
                break;
            case "BoolExpr":
                returnValue = visit.Visit((BoolExpr)child);
                break;
            case "FuncCallExpr":
                returnValue = visit.Visit((FuncCallExpr)child);
                break;
            case "ReturnStmt":
                visit.Visit((ReturnStmt)child);
                break;
            case "IfStmt":
                visit.Visit((IfStmt)child);
                break;
            case "ForEachStmt":
                visit.Visit((ForEachStmt)child);
                break;
            case "UntilStmt":
                visit.Visit((UntilStmt)child);
                break;
            case "StructDcl":
                visit.Visit((StructDcl) child);
                break;
            case "IOStmt":
                visit.Visit((IOStmt)child);
                break;
            case "IOExpr":
                returnValue = visit.Visit((IOExpr) child);
                break;
            case "StructVarDcl":
                visit.Visit((StructVarDcl) child);
                break;
            case "ListIndexExpr":
                returnValue = visit.Visit((ListIndexExpr) child);
            default:
                visit.VisitChildren(child);
        }

        return returnValue;
    }
}
