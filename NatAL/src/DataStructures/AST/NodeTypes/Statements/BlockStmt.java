package DataStructures.AST.NodeTypes.Statements;

import java.util.List;
import DataStructures.AST.AST;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class BlockStmt extends Stmt
{
    public BlockStmt ()
    {

    }

    /*public BlockStmt (List<Stmt> statements)
    {
        for(Stmt stmt : statements)
        {
            children.add(stmt);
        }
        SetValue("BlockStmt");
    }*/

    public List<Stmt> GetStatements ()
    {
        List<Stmt> stmts = null;
        for(AST a : children)
        {
            stmts.add((Stmt)a);
        }

        return stmts;
    }
}
