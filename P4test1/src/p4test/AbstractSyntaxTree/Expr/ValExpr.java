package p4test.AbstractSyntaxTree.Expr;

import p4test.AbstractSyntaxTree.Types;
import p4test.Token;

/**
 * Created by mysjkin on 3/5/17.
 */
public class ValExpr extends Expression
{
    public ValExpr(Token literalValue)
    {
        LiteralValue = literalValue;
    }
    public final Token LiteralValue;
}
