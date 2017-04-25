package DataStructures.AST.NodeTypes.Statements;


import DataStructures.AST.NodeTypes.Expressions.Expr;
import DataStructures.AST.NodeTypes.Expressions.IdExpr;
import DataStructures.AST.NodeTypes.Modes;
import Syntax.Tokens.Token;

/**
 * Created by mysjkin on 3/30/17.
 */
public class IOStmt extends Stmt
{
    // read stmt
    public IOStmt(Modes mode, Token op, IdExpr pin)
    {
        this.mode = mode;
        this.operation = op;
        children.add(pin);
        SetValue("IOStmt");
    }
    // write stmt
    public IOStmt(Modes mode, Token op, Expr writeExpr, IdExpr pin)
    {
        this(mode,op,pin);
        AddChild(writeExpr);
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
    public Expr GetWriteVal()
    {
        if((Expr)children.get(1)!=null)return (Expr)children.get(1);
        else return null;
    }
}
