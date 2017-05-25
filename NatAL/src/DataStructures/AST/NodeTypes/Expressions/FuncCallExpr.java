package DataStructures.AST.NodeTypes.Expressions;

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
        AddChild(funcIdentifier);
        AddChild(args);
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
