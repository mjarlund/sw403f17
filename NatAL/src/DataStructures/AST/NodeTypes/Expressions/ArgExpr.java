package DataStructures.AST.NodeTypes.Expressions;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class ArgExpr extends Expr
{
    public ArgExpr(ValExpr value)
    {
        children.add(value);
    }
    public ArgExpr(IdExpr id)
    {
        children.add(id);
    }
}