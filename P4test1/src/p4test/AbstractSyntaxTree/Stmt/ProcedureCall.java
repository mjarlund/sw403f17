package p4test.AbstractSyntaxTree.Stmt;

import p4test.AbstractSyntaxTree.Dcl.Identifier;
import p4test.AbstractSyntaxTree.Expr.Arguments;
import p4test.AbstractSyntaxTree.Visitor;

/**
 * Created by mysjkin on 3/5/17.
 */
public class ProcedureCall extends Statement
{
    public ProcedureCall(Identifier funcIdentifier, Arguments args)
    {
        children.add(funcIdentifier);
        children.add(args);
    }
    public void visit(Visitor v)
    {
        v.visit(this);
    }
}
