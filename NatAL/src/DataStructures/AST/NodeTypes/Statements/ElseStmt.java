package DataStructures.AST.NodeTypes.Statements;

import jdk.nashorn.internal.ir.Block;

public class ElseStmt extends Stmt
{
    public ElseStmt(BlockStmt block)
    {
    	AddChild(block);
        SetValue("ElseStmt");
    }

    public BlockStmt GetStatement () {return (BlockStmt)children.get(0);}
}
