package p4test.AST.Nodes.expr;

import p4test.Token;

/**
 * Created by mysjkin on 3/4/17.
 */
public class NotUnary extends Expr
{

    public NotUnary(Token notOP, Expr expr)
    {
        super(notOP);
    }
}
