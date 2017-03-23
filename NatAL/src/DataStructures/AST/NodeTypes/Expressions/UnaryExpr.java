package DataStructures.AST.NodeTypes.Expressions;

import Syntax.Tokens.Token;

/**
 * Created by mysjkin on 3/21/17.
 */
public class UnaryExpr extends Expr
{
    public UnaryExpr(Token op, Expr valExpr)
    {
        children.add(valExpr);
    }
    public Expr GetValExpr()
    {
        return (Expr) children.get(0);
    }
}
