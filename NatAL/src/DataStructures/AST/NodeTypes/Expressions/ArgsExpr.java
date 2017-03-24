package DataStructures.AST.NodeTypes.Expressions;

import DataStructures.AST.AST;

import java.util.ArrayList;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class ArgsExpr extends Expr
{
    public ArgsExpr()
    {
        SetValue("ArgsExpr");
    }
    public ArrayList<ArgExpr> GetArgs()
    {
        ArrayList<ArgExpr> args = new ArrayList<>();
        for(AST arg : children)
        {
            args.add((ArgExpr) arg);
        }
        return args;
    }
}