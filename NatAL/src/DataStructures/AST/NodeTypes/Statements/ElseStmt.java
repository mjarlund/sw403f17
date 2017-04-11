package DataStructures.AST.NodeTypes.Statements;

import jdk.nashorn.internal.ir.Block;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class ElseStmt extends Stmt
{
    public ElseStmt(BlockStmt block)
    {
        children.add(block);
        SetValue("ElseStmt");
    }

    public BlockStmt GetStatement () {return (BlockStmt)children.get(0);}
}
