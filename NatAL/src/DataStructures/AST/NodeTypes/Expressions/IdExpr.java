package DataStructures.AST.NodeTypes.Expressions;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class IdExpr extends Expr
{
    public IdExpr(String id)
    {
        SetValue("IdExpr");
        ID = id;
    }
    public String ID;
}

