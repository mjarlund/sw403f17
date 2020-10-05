package DataStructures.AST.NodeTypes.Statements;

import DataStructures.AST.NodeTypes.Expressions.Expr;
import DataStructures.AST.NodeTypes.Expressions.IdExpr;
import DataStructures.AST.NodeTypes.Modes;
import Syntax.Tokens.Token;

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
        return (Expr)children.get(1);
    }
}
