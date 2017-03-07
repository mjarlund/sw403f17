package p4test.AbstractSyntaxTree.Stmt;

import p4test.AbstractSyntaxTree.Expr.BoolExpr;
import p4test.AbstractSyntaxTree.Visitor;

/**
 * Created by mysjkin on 3/5/17.
 */
public class IfStmt extends Statement
{
    public IfStmt(BoolExpr expr, Block statements)
    {
        AddNode(expr);
        AddNode(statements);
    }
    public void visit(Visitor v)
    {
        v.visit(this);
    }
}
