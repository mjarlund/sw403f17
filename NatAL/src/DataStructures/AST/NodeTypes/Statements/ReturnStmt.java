package DataStructures.AST.NodeTypes.Statements;

import DataStructures.AST.NodeTypes.Expressions.Expr;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class ReturnStmt extends Stmt
{
    public ReturnStmt(Expr returnVal)
    {
        children.add(returnVal);
    }
    public Expr GetReturnExpr()
    {
        return (Expr) children.get(0);
    }
}