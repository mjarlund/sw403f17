package DataStructures.AST.NodeTypes.Expressions;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Types;

public class ArgExpr extends Expr
{
    public ArgExpr(ValExpr value)
    {
        SetValue("ArgExpr");
        AddChild(value);
    }
    public ArgExpr(IdExpr id)
    {
    	AddChild(id);
    	SetValue("ArgExpr");
    }
    public ArgExpr(Expr value)
    {
        SetValue("ArgExpr");
        AddChild(value);
    }
    public Expr GetArg()
    {
        return (Expr) children.get(0);
    }
    public Types GetType()
    {
        AST arg = children.get(0);
        if(arg instanceof ValExpr){
            return ((ValExpr)arg).Type;
        }
        else return null;
    }
}