package DataStructures.AST.NodeTypes.Statements;


import DataStructures.AST.NodeTypes.Expressions.IdExpr;
import DataStructures.AST.NodeTypes.Modes;
import Syntax.Tokens.Token;

/**
 * Created by mysjkin on 3/30/17.
 */
public class IOStmt extends Stmt
{
    public IOStmt(Modes mode, Token op, IdExpr pin)
    {
        this.mode = mode;
        this.operation = op;
        children.add(pin);
        SetValue("IOStmt");
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
