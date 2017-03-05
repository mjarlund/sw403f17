package p4test.AbstractSyntaxTree.Stmt;

import p4test.AbstractSyntaxTree.Expr.BoolExpr;

/**
 * Created by mysjkin on 3/5/17.
 */
public class UntilStmt extends Statement
{
    public UntilStmt(BoolExpr condition, Block statements)
    {
        AddNode(condition);
        AddNode(statements);
    }
}
