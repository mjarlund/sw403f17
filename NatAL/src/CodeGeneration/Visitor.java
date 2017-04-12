package CodeGeneration;
import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Statements.*;
import javafx.beans.binding.ObjectExpression;

import java.util.Objects;

public interface Visitor
{
    /* Declarations */
    Object VisitDcl(Dcl AST, Object o);
    Object VisitFParamDcl(FParamDcl AST, Object o);
    Object VisitFParamsDcl(FParamsDcl AST, Object o);
    Object VisitFuncDcl(FuncDcl AST, Object o);
    Object VisitListDcl(ListDcl AST, Object o);
    Object VisitStructDcl(StructDcl AST, Object o);
    Object VisitStructVarDcl(StructVarDcl AST, Object o);
    Object VisitVarDcl(VarDcl AST, Object o);

    /* Expressions */
    Object VisitArgExpr(ArgExpr AST, Object o);
    Object VisitArgsExpr(ArgsExpr AST, Object o);
    Object VisitBinaryOPExpr(BinaryOPExpr AST, Object o);
    Object VisitBoolExpr(BoolExpr ASt, Object o);
    Object VisitExpr(Expr AST, Object o);
    Object VisitFuncCallExpr(FuncCallExpr AST, Object o);
    Object VisitIdExpr(IdExpr AST, Object o);
    Object VisitIOExpr(IOExpr AST, Object o);
    Object VisitUnaryExpr(UnaryExpr AST, Object o);
    Object VisitValExpr(ValExpr AST, Object o);

    /* Statements */
    Object VisitAssignStmt(AssignStmt AST, Object o);
    Object VisitBlockStmt(BlockStmt AST, Object o);
    Object VisitElseSmt(ElseStmt AST, Object o);
    Object VisitEmptyStmt(EmptyStmt AST, Object o);
    Object VisitForEachStmt(ForEachStmt AST, Object o);
    Object VisitIfStmt(IfStmt AST, Object o);
    Object VisitIOStmt(IOExpr AST, Object o);
    //Object VisitProcCallStmt(ProcCallStmt AST, Object o);
    Object VisitReturnStmt(ReturnStmt AST, Object o);
    Object VisitStmt(Stmt AST, Object o);
    Object VisitUntilStmt(UntilStmt AST, Object o);
}
