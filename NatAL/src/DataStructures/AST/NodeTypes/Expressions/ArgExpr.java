package DataStructures.AST.NodeTypes.Expressions;

import DataStructures.AST.AST;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class ArgExpr extends Expr
{
    public ArgExpr(ValExpr value)
    {
        SetValue("ArgExpr");
        children.add(value);
    }
    public ArgExpr(IdExpr id)
    {
        children.add(id);
    }
    public ValExpr GetArg()
    {
        return (ValExpr) children.get(0);
    }
}