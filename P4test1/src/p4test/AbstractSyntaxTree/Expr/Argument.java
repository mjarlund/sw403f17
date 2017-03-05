package p4test.AbstractSyntaxTree.Expr;

import p4test.AbstractSyntaxTree.AST;

/**
 * Created by mysjkin on 3/5/17.
 */
public class Argument extends AST
{
    public Argument(String value)
    {
        Value = value;
    }
    public final String Value;
}