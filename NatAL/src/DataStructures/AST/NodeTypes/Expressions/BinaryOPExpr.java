package DataStructures.AST.NodeTypes.Expressions;

import Syntax.Tokens.Token;

public class BinaryOPExpr extends Expr
{
    public BinaryOPExpr(Expr expr1, Token operation, Expr expr2)
    {
        Operation = operation;
        AddChild(expr1);
        AddChild(expr2);
        SetValue("BinaryOPExpr");
    }
    public final Token Operation;
    public Expr GetLeftExpr()
    {
        return (Expr) children.get(0);
    }
    public Expr GetRightExpr()
    {
        return (Expr) children.get(1);
    }
}
