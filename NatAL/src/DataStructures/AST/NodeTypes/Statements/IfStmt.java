package DataStructures.AST.NodeTypes.Statements;

import DataStructures.AST.NodeTypes.Expressions.Expr;

public class IfStmt extends Stmt
{
    public IfStmt(Expr expr, BlockStmt statements)
    {
        children.add(expr);
        children.add(statements);
        SetValue("IfStmt");
    }
    public Expr GetCondition()
    {
        return (Expr)children.get(0);
    }
    public BlockStmt GetBlock()
    {
        return (BlockStmt) children.get(1);
    }
    public ElseStmt GetElse(){ return (ElseStmt) children.get(2); }
}