package p4test.AbstractSyntaxTree.Stmt;

import p4test.AbstractSyntaxTree.Visitor;

import java.util.List;

/**
 * Created by mysjkin on 3/5/17.
 */
public class Block extends Statement
{
    public Block(List<Statement> statements)
    {
        for(Statement stmt : statements)
        {
            children.add(stmt);
        }
    }
    public Block()
    {}
    public void visit(Visitor v)
    {
        v.visit(this);
    }
}
