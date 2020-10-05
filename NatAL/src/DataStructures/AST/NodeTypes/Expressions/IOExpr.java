package DataStructures.AST.NodeTypes.Expressions;

import DataStructures.AST.NodeTypes.Modes;
import Syntax.Tokens.Token;

public class IOExpr extends Expr
{
    public IOExpr(Modes mode, Token op,IdExpr pin)
    {
        this.mode = mode;
        this.operation = op;
        AddChild(pin);
        SetValue("IOExpr");
    }
    private Modes mode;
    private Token operation;
    public Modes GetMode()
    {
        return mode;
    }
    public Token GetOperation()
    {
        return operation;
    }
    public IdExpr GetPin()
    {
        return (IdExpr) children.get(0);
    }
}
