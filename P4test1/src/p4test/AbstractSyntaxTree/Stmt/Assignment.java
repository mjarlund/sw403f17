package p4test.AbstractSyntaxTree.Stmt;

import p4test.AbstractSyntaxTree.Dcl.Declaration;
import p4test.AbstractSyntaxTree.Dcl.VarDcl;
import p4test.AbstractSyntaxTree.Expr.Expression;

/**
 * Created by mysjkin on 3/5/17.
 */
public class Assignment extends Statement
{
    public Assignment(VarDcl var, Expression expr)
    {
        AddNode(var);
        AddNode(expr);
        Identifier = var.Identifier;
    }
    public Assignment(String identifier, Expression expr)
    {
        Identifier = identifier;
        AddNode(expr);
    }

    public final String Identifier;

}
