package DataStructures.AST.NodeTypes.Statements;

import DataStructures.AST.NodeTypes.Expressions.Expr;

public class ReturnStmt extends Stmt
{
    public ReturnStmt(Expr returnVal)
    {
    	AddChild(returnVal);
        SetValue("ReturnStmt");
    }
    public Expr GetReturnExpr()
    {
        return (Expr) children.get(0);
    }
}