package DataStructures.AST.NodeTypes.Expressions;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class FuncCallExpr extends Expr
{
    public FuncCallExpr(IdExpr funcIdentifier, ArgsExpr args)
    {
        children.add(funcIdentifier);
        children.add(args);
    }
}
