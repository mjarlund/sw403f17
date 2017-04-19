package Utilities;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Statements.*;

/**
 * Created by mysjkin on 4/12/17.
 */
public interface IVisitor
{
    Object Visit(StructVarDcl dcl);

    Object Visit(IOStmt stmt);

    Object Visit(IOExpr expr);

    Object Visit(IfStmt stmt);

    Object Visit(ElseStmt stmt);

    Object Visit(UntilStmt stmt);

    Object Visit(ForEachStmt stmt);

    Object Visit(ReturnStmt stmt);

    Object Visit(FuncCallExpr expr);

    Object Visit(BinaryOPExpr expr);

    Object Visit(BoolExpr expr);

    Object Visit(AssignStmt stmt);

    Object Visit(UnaryExpr expr);

    Object Visit(ValExpr lit);

    Object Visit(VarDcl node);

    Object Visit(ListDcl node);

    Object Visit(IdExpr node);

    Object Visit(FParamsDcl node);

    Object Visit(ArgsExpr node);

    Object Visit(FuncDcl node);

    Object Visit(StructDcl node);

    Object Visit(BlockStmt block);

    Object Visit(ListIndexExpr block);

    Object Visit(StructCompSelectExpr node);

}
