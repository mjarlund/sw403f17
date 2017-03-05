package p4test.AbstractSyntaxTree.Expr;

import p4test.AST.Nodes.expr.Expr;
import p4test.Token;

/**
 * Created by mysjkin on 3/5/17.
 */
public class BoolExpr extends Expression
{
    public BoolExpr(Expression expr1, Token operator, Expression expr2)
    {
        AddNode(expr1);
        Operator = operator;
        AddNode(expr2);
    }
    public final Token Operator;
}
