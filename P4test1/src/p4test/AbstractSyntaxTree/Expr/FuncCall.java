package p4test.AbstractSyntaxTree.Expr;

import p4test.AbstractSyntaxTree.Expr.Arguments;
import p4test.AbstractSyntaxTree.Expr.Expression;

/**
 * Created by mysjkin on 3/5/17.
 */
public class FuncCall extends Expression
{
    public FuncCall(String funcIdentifier, Arguments args)
    {
        Identifier = funcIdentifier;
        AddNode(args);
    }
    public final String Identifier;
}
