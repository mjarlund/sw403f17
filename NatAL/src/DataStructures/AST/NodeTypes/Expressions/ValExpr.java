package DataStructures.AST.NodeTypes.Expressions;

import Syntax.Tokens.Token;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class ValExpr extends Expr
{
    public ValExpr(Token literalValue)

    {
        LiteralValue = literalValue;
    }
    public final Token LiteralValue;
}
