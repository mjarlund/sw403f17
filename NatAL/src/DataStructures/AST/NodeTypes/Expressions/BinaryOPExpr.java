package DataStructures.AST.NodeTypes.Expressions;

import Syntax.Tokens.Token;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class BinaryOPExpr extends Expr
{
    public BinaryOPExpr(Expr expr1, Token operation, Expr expr2)
    {
        Operation = operation;
        children.add(expr1);
        children.add(expr2);
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
