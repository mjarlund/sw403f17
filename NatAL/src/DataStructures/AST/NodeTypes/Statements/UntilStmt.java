package DataStructures.AST.NodeTypes.Statements;

import DataStructures.AST.NodeTypes.Expressions.BoolExpr;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class UntilStmt extends Stmt
{
    public UntilStmt(BoolExpr condition, BlockStmt statements)
    {
        children.add(condition);
        children.add(statements);
    }
}
