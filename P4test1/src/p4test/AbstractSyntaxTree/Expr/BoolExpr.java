package p4test.AbstractSyntaxTree.Expr;

import p4test.AbstractSyntaxTree.Visitor;
import p4test.Token;

/**
 * Created by mysjkin on 3/5/17.
 */
public class BoolExpr extends Expression
{
    public BoolExpr(Expression expr1, Token operator, Expression expr2)
    {
        Operator = operator;
    }

    public void visit(Visitor v)
    {
        v.visit(this);
    }
    public final Token Operator;
}
