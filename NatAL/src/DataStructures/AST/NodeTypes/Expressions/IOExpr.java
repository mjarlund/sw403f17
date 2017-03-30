package DataStructures.AST.NodeTypes.Expressions;

import DataStructures.AST.NodeTypes.Modes;
import Syntax.Tokens.Token;

/**
 * Created by mysjkin on 3/30/17.
 */
public class IOExpr extends Expr
{
    public IOExpr(Modes mode, Token op, IdExpr pin)
    {
        this.mode = mode;
        this.operation = op;
        children.add(pin);
        SetValue("IOExpr");
    }
    private Modes mode;
    private Token operation;
    private Token pin;
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
