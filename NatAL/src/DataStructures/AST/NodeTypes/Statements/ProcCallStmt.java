package DataStructures.AST.NodeTypes.Statements;

import DataStructures.AST.NodeTypes.Expressions.ArgsExpr;
import DataStructures.AST.NodeTypes.Expressions.IdExpr;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class ProcCallStmt extends Stmt
{
    public ProcCallStmt(IdExpr funcIdentifier, ArgsExpr args)
    {
        children.add(funcIdentifier);
        children.add(args);
    }
}
