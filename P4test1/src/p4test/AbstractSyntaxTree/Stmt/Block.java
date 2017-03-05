package p4test.AbstractSyntaxTree.Stmt;

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
            AddNode(stmt);
        }
    }
}
