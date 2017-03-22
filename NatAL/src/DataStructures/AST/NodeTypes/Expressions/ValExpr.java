package DataStructures.AST.NodeTypes.Expressions;

import DataStructures.AST.NodeTypes.Types;
import Syntax.Tokens.Token;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class ValExpr extends Expr
{
    public ValExpr(Types type, Token literalValue)

    {
        LiteralValue = literalValue;
        Type = type;
    }
    public ValExpr(Token literalValue)

    {
        LiteralValue = literalValue;
    }
    public final Token LiteralValue;
    public Types Type;
}
