package DataStructures.AST.NodeTypes.Expressions;

import DataStructures.AST.NodeTypes.Types;
import Syntax.Tokens.Token;

public class ValExpr extends Expr
{
    public ValExpr(Types type, Token literalValue)
    {
        Type = type;
        LiteralValue = literalValue;
        SetValue("ValExpr");
    }

    public final Token LiteralValue;
    public Types Type;
}
