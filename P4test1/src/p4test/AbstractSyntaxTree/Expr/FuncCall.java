package p4test.AbstractSyntaxTree.Expr;

import p4test.AbstractSyntaxTree.Expr.Arguments;
import p4test.AbstractSyntaxTree.Expr.Expression;
import p4test.AbstractSyntaxTree.Visitor;

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
    public void visit(Visitor v)
    {
        v.visit(this);
    }
    public final String Identifier;
}
