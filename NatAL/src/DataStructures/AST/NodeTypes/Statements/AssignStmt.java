package DataStructures.AST.NodeTypes.Statements;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.VarDcl;
import DataStructures.AST.NodeTypes.Expressions.Expr;

public class AssignStmt extends Stmt
{
    public AssignStmt(VarDcl var, Expr expr)
    {
        AddChild(var);
        AddChild(expr);
        SetValue("AssignStmt");
    }
    public AssignStmt(Expr identifier, Expr expr)
    {
    	AddChild(identifier);
    	AddChild(expr);
        SetValue("AssignStmt");
    }
    public AST GetLeft()
    {
        return children.get(0);
    }
    public AST GetRight()
    {
        return children.get(1);
    }
}
