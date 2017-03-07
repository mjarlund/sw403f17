package p4test.AbstractSyntaxTree.Dcl;

import p4test.AbstractSyntaxTree.AST;
import p4test.AbstractSyntaxTree.Visitor;

import java.text.Normalizer;
import java.util.List;

/**
 * Created by mysjkin on 3/5/17.
 */
public class FormalParameters extends AST
{
    public FormalParameters()
    {

    }
    public void visit(Visitor v)
    {
        v.visit(this);
    }
}
