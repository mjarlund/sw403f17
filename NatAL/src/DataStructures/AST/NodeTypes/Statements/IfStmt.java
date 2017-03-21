package DataStructures.AST.NodeTypes.Statements;

import DataStructures.AST.NodeTypes.Expressions.BoolExpr;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class IfStmt extends Stmt
{
    public IfStmt(BoolExpr expr, BlockStmt statements)
    {
        children.add(expr);
        children.add(statements);
    }
}