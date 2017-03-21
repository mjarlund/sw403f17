package DataStructures.AST.NodeTypes.Expressions;

import Syntax.Tokens.Token;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class BoolExpr extends Expr
{
    public BoolExpr(Expr expr1, Token operator, Expr expr2)
    {
        Operator = operator;
        children.add(expr1);
        children.add(expr2);
    }
    public final Token Operator;
}
