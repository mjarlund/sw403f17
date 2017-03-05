package p4test.AbstractSyntaxTree.Stmt;

import p4test.AbstractSyntaxTree.Expr.Arguments;

/**
 * Created by mysjkin on 3/5/17.
 */
public class ProcedureCall extends Statement
{
    public ProcedureCall(String funcIdentifier, Arguments args)
    {
        FunctionIdentifier = funcIdentifier;
        AddNode(args);
    }
    public final String FunctionIdentifier;
}
