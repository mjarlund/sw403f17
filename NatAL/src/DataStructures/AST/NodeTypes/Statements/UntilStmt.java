package DataStructures.AST.NodeTypes.Statements;

import DataStructures.AST.NodeTypes.Expressions.Expr;

public class UntilStmt extends Stmt
{
    public UntilStmt(Expr condition, BlockStmt statements)
    {
    	AddChild(condition);
    	AddChild(statements);
        SetValue("UntilStmt");
    }
    public Expr GetCondition()
    {
        return (Expr) children.get(0);
    }
    public BlockStmt GetBlock()
    {
        return (BlockStmt) children.get(1);
    }
}
