package DataStructures.AST.NodeTypes.Statements;

import java.util.List;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class BlockStmt extends Stmt
{
    public BlockStmt(List<Stmt> statements)
    {
        for(Stmt stmt : statements)
        {
            children.add(stmt);
        }
        SetValue("BlockStmt");
    }
    public BlockStmt(){}
}
