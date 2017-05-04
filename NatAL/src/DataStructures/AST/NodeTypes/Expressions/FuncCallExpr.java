package DataStructures.AST.NodeTypes.Expressions;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class FuncCallExpr extends Expr
{
    public FuncCallExpr(IdExpr funcIdentifier, ArgsExpr args)
    {
    	AddChild(funcIdentifier);
    	AddChild(args);
        SetValue("FuncCallExpr");
    }
    public FuncCallExpr(StructCompSelectExpr funcIdentifier, ArgsExpr args)
    {
        /* OBS all children.add should be replaced with AddChild */
        AddChild(funcIdentifier);
        AddChild(args);
        //children.add(funcIdentifier);
        //children.add(args);
        SetValue("FuncCallExpr");
    }

    public Expr GetFuncId()
    {
        return (Expr) children.get(0);
    }
    public ArgsExpr GetFuncArgs()
    {
        return (ArgsExpr) children.get(1);
    }
}
