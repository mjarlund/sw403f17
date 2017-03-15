package p4test.AbstractSyntaxTree.Stmt;

/**
 * Created by mysjkin on 3/10/17.
 */
public class ElseStmt extends Statement
{
    public ElseStmt(Block block)
    {
        children.add(block);
    }
}
