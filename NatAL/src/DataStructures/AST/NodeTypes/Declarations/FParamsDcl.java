package DataStructures.AST.NodeTypes.Declarations;

import DataStructures.AST.AST;

import java.util.ArrayList;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class FParamsDcl extends Dcl
{
    public FParamsDcl()
    {
        SetValue("FParamsDcl");
    }
    public ArrayList<FParamDcl> GetFParams()
    {
        ArrayList<FParamDcl> params = new ArrayList<>();
        for(int i=children.size()-1;i>=0;--i){
            params.add((FParamDcl)children.get(i));
        }
        /*for(AST param : children)
        {
            params.add((FParamDcl) param);
        }*/
        return params;
    }
}
