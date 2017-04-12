package Utilities;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Statements.*;

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
        switch (astName){
            case "VarDcl": /* Add it to this scope (works for FuncDcls too) */
                return visit.Visit((VarDcl) child);
            case "IdExpr": /* Just check whether or not it's there */
                return visit.Visit((IdExpr) child);
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
            case "ValExpr":
                return visit.Visit((ValExpr) child);
            case "AssignStmt":
                visit.Visit((AssignStmt) child);
                break;
            case "BinaryOPExpr":
                return visit.Visit((BinaryOPExpr)child);
            case "UnaryExpr":
                return visit.Visit((UnaryExpr)child);
            case "BoolExpr":
                return visit.Visit((BoolExpr)child);
            case "FuncCallExpr":
                return visit.Visit((FuncCallExpr)child);
            case "ReturnStmt":
                visit.Visit((ReturnStmt)child);
                break;
            case "IfStmt":
                visit.Visit((IfStmt)child);
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
                return visit.Visit((IOExpr) child);
            case "StructVarDcl":
                visit.Visit((StructVarDcl) child);
            default:
                visit.VisitChildren(child);
        }

        return null;
    }
}
