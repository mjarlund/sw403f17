package DataStructures.AST.NodeTypes.Expressions;

import DataStructures.AST.NodeTypes.Types;
import Syntax.Tokens.Token;

public class BinaryOPExpr extends Expr
{
    public BinaryOPExpr(Expr expr1, Token operation, Expr expr2)
    {
        Operation = operation;
        AddChild(expr1);
        AddChild(expr2);
        SetValue("BinaryOPExpr");
    }
    public final Token Operation;
    private Types evaluationType;
    public Types GetEvaluationType()
    {
        return evaluationType;
    }
    public void SetEvaluationType(Types type)
    {
        evaluationType = type;
    }
    public Expr GetLeftExpr()
    {
        return (Expr) children.get(0);
    }
    public Expr GetRightExpr()
    {
        return (Expr) children.get(1);
    }
}
