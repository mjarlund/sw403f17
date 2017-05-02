package DataStructures.AST.NodeTypes.Expressions;

import Syntax.Tokens.Token;

/**
 * Created by mysjkin on 3/21/17.
 */
public class UnaryExpr extends Expr
{
    Token operator;
    public Token GetOperator () { return operator; }

    public String GetConvertedType ()
    {
        switch (GetOperator().Value)
        {
            case "not":
                return "!";

            default:
                return GetOperator().Value;

        }
    }


    public UnaryExpr(Token op, Expr valExpr)
    {
        operator = op;
        AddChild(valExpr);

        SetValue("UnaryExpr");
    }
    public Expr GetValExpr()
    {
        return (Expr) children.get(0);
    }
}
