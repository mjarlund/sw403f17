package p4test.AbstractSyntaxTree.Expr;

import p4test.Token;

/**
 * Created by mysjkin on 3/5/17.
 */
public class BinaryOP extends Expression
{
    public BinaryOP(Expression expr1, Token operation, Expression expr2)
    {
        AddNode(expr1);
        Operation = operation;
        AddNode(expr2);
    }
    public final Token Operation;
}
