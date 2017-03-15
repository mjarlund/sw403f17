package p4test.AbstractSyntaxTree.Stmt;

import p4test.AbstractSyntaxTree.Dcl.Declaration;
import p4test.AbstractSyntaxTree.Dcl.Identifier;
import p4test.AbstractSyntaxTree.Dcl.VarDcl;
import p4test.AbstractSyntaxTree.Expr.Expression;
import p4test.AbstractSyntaxTree.Visitor;

/**
 * Created by mysjkin on 3/5/17.
 */
public class Assignment extends Statement
{
    public Assignment(VarDcl var, Expression expr)
    {
        children.add(var);
        children.add(expr);
    }
    public Assignment(Expression identifier, Expression expr)
    {
        children.add(identifier);
        children.add(expr);
    }
    public void visit(Visitor v)
    {
        v.visit(this);
    }

}
