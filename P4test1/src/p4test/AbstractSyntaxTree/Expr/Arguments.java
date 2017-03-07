package p4test.AbstractSyntaxTree.Expr;

import p4test.AbstractSyntaxTree.AST;
import p4test.AbstractSyntaxTree.Visitor;

import java.util.List;

/**
 * Created by mysjkin on 3/5/17.
 */
public class Arguments extends AST
{
    public Arguments()
    {
    }
    public void visit(Visitor v)
    {
        v.visit(this);
    }
}