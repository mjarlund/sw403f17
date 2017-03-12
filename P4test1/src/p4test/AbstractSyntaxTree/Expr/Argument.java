package p4test.AbstractSyntaxTree.Expr;

import p4test.AbstractSyntaxTree.AST;
import p4test.AbstractSyntaxTree.Visitor;
import p4test.Token;

/**
 * Created by mysjkin on 3/5/17.
 */
public class Argument extends AST
{
    public Argument(Token value)
    {

    }
    public void visit(Visitor v)
    {
        v.visit(this);
    }
}