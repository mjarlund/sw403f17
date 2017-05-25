package DataStructures.AST.NodeTypes.Expressions;

import java.util.ArrayList;

public class ArgsExpr extends Expr
{
    public ArgsExpr()
    {
        SetValue("ArgsExpr");
    }
    public ArrayList<ArgExpr> GetArgs()
    {
        ArrayList<ArgExpr> args = new ArrayList<>();
        for(int i=children.size()-1;i>=0;--i){
            args.add((ArgExpr)children.get(i));
        }
        return args;
    }
}