package p4test.AST.Nodes.expr;

import p4test.Token;

/**
 * Created by mysjkin on 3/3/17.
 */
public class BinaryOp extends Expr
{

    public BinaryOp(Expr operandL,Token operation, Expr operandR)
    {
        super(operation);
        AddNode(operandL);
        AddNode(operandR);
    }
}
