package DataStructures.AST.NodeTypes.Expressions;

import CodeGeneration.CodeGenerator;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class FuncCallExpr extends Expr
{
    public FuncCallExpr(IdExpr funcIdentifier, ArgsExpr args)
    {
        children.add(funcIdentifier);
        children.add(args);
        SetValue("FuncCallExpr");
    }
    public IdExpr GetFuncId()
    {
        return (IdExpr) children.get(0);
    }
    public ArgsExpr GetFuncArgs()
    {
        return (ArgsExpr) children.get(1);
    }
}
