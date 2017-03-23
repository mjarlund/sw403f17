package DataStructures.AST.NodeTypes.Statements;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.VarDcl;
import DataStructures.AST.NodeTypes.Expressions.Expr;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class AssignStmt extends Stmt
{
    public AssignStmt(VarDcl var, Expr expr)
    {
        children.add(var);
        children.add(expr);
    }
    public AssignStmt(Expr identifier, Expr expr)
    {
        children.add(identifier);
        children.add(expr);
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
