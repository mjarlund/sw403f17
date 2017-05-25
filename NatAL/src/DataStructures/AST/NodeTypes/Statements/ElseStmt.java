package DataStructures.AST.NodeTypes.Statements;

public class ElseStmt extends Stmt
{
    public ElseStmt(BlockStmt block)
    {
    	AddChild(block);
        SetValue("ElseStmt");
    }
}
