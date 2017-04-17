package DataStructures.AST.NodeTypes.Statements;

import java.util.ArrayList;
import java.util.List;
import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.VarDcl;
import Utilities.Reporter;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class BlockStmt extends Stmt
{
    public BlockStmt ()
    {
        SetValue("BlockStmt");
    }

    /*public BlockStmt (List<Stmt> statements)
    {
        for(Stmt stmt : statements)
        {
            children.add(stmt);
        }
        SetValue("BlockStmt");
    }*/

    public List<AST> GetStatements ()
    {
        List<AST> stmts = new ArrayList<AST>();
        for(AST a : children)
        {
            stmts.add(a);
        }

        return stmts;
    }
}
