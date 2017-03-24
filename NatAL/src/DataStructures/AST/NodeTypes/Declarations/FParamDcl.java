package DataStructures.AST.NodeTypes.Declarations;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Types;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class FParamDcl extends AST
{
    public FParamDcl(Types type, String identifier)
    {
        Type = type;
        Identifier = identifier;
        SetValue("FParamDcl");
    }

    public final Types Type;
    public final String Identifier;
}