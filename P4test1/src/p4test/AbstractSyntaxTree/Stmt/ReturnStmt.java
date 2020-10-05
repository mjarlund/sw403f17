package p4test.AbstractSyntaxTree.Stmt;

import p4test.AbstractSyntaxTree.Expr.Expression;

/**
 * Created by mysjkin on 3/17/17.
 */
public class ReturnStmt extends Statement
{
    public ReturnStmt(Expression returnVal)
    {
        children.add(returnVal);
    }
}
