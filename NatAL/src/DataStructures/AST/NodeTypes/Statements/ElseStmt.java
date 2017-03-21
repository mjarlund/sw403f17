package DataStructures.AST.NodeTypes.Statements;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class ElseStmt extends Stmt
{
    public ElseStmt(BlockStmt block)
    {
        children.add(block);
    }
}
