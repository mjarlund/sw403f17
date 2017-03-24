package DataStructures.AST.NodeTypes.Statements;

import DataStructures.AST.NodeTypes.Expressions.Expr;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class UntilStmt extends Stmt
{
    public UntilStmt(Expr condition, BlockStmt statements)
    {
        children.add(condition);
        children.add(statements);
    }
    public Expr GetCondition()
    {
        return (Expr) children.get(0);
    }
    public BlockStmt GetBlock()
    {
        return (BlockStmt) children.get(1);
    }
}
