package DataStructures.AST.NodeTypes.Expressions;

import Syntax.Tokens.Token;

public class BoolExpr extends Expr
{
    public BoolExpr(Expr expr1, Token operator, Expr expr2)
    {
        Operator = operator;
        AddChild(expr1);
        AddChild(expr2);
        SetValue("BoolExpr");
    }
    public final Token Operator;
    public Expr GetLeftExpr()
    {
        return (Expr) children.get(0);
    }
    public Expr GetRightExpr()
    {
        return (Expr) children.get(1);
    }

    public String GetConvertedType ()
    {
        switch (Operator.Value)
        {
            case "or": return "||";
            case "equals": return "==";
            case "not": return "!";
            case "below": return "<";
            case "and": return "&&";
            case "is": return "=";
            case "above": return ">";
            case "above or equals": return ">=";
            case "below or equals": return "<=";
            case "not equals": return "!=";
            default:
                throw new Error("Whoops - ¯\\_(ツ)_/¯");
        }
    }
}
