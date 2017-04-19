package Utilities;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Statements.*;

import java.util.List;

public class VisitorDriver
{
    private IVisitor visit;
    public VisitorDriver(IVisitor visit)
    {
        this.visit = visit;
    }
    public Object Visit(String astName, AST child)
    {
        System.out.println(astName);
        Object returnValue = null;
        switch (astName){
            case "VarDcl": /* Add it to this scope (works for FuncDcls too) */
                returnValue = visit.Visit((VarDcl) child);
                break;
            case "IdExpr": /* Just check whether or not it's there */
                returnValue =  visit.Visit((IdExpr) child);
                break;
            case "BlockStmt": /* Open a new scope, continue */
                returnValue = visit.Visit((BlockStmt) child);
                break;
            case "FParamsDcl": /* Used as declarations, add them to that scope */
                returnValue = visit.Visit((FParamsDcl) child);
                break;
            case "ArgsExpr": /* Not much different from IDs */
                returnValue = visit.Visit((ArgsExpr) child);
                break;
            case "FuncDcl": /* Only in global scope. Open scope so its formal parameters
                                 * are not seen as symbols in the global scope. */
                returnValue = visit.Visit((FuncDcl) child);
                break;
            case "ListDcl":
                returnValue = visit.Visit((ListDcl) child);
                break;
            case "ValExpr":
                returnValue = visit.Visit((ValExpr) child);
                break;
            case "AssignStmt":
                returnValue = visit.Visit((AssignStmt) child);
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
                returnValue = visit.Visit((ReturnStmt)child);
                break;
            case "IfStmt":
                returnValue = visit.Visit((IfStmt)child);
                break;
            case "ForEachStmt":
                returnValue = visit.Visit((ForEachStmt)child);
                break;
            case "UntilStmt":
                returnValue = visit.Visit((UntilStmt)child);
                break;
            case "StructDcl":
                returnValue = visit.Visit((StructDcl) child);
                break;
            case "IOStmt":
                returnValue = visit.Visit((IOStmt)child);
                break;
            case "IOExpr":
                returnValue = visit.Visit((IOExpr) child);
                break;
            case "StructVarDcl":
                returnValue = visit.Visit((StructVarDcl) child);
                break;
            case "ListIndexExpr":
                returnValue = visit.Visit((ListIndexExpr) child);
                break;
            case "StructCompSelectExpr":
                returnValue = visit.Visit((StructCompSelectExpr) child);
                break;
        }

        return returnValue;
    }
}
