package p4test.AbstractSyntaxTree.Expr;

import p4test.AbstractSyntaxTree.Visitor;
import p4test.Token;

/**
 * Created by mysjkin on 3/5/17.
 */
public class BinaryOP extends Expression
{
    public BinaryOP(Expression expr1, Token operation, Expression expr2)
    {
        Operation = operation;
        children.add(expr1);
        children.add(expr2);
    }
    public void visit(Visitor v)
    {
        v.visit(this);
    }

    public final Token Operation;
}
