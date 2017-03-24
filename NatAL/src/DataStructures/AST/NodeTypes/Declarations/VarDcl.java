package DataStructures.AST.NodeTypes.Declarations;

import DataStructures.AST.NodeTypes.Types;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class VarDcl extends Dcl
{
    public VarDcl(Types type, String identifier)
    {
        Type = type;
        Identifier = identifier;
        SetValue("VarDcl");
    }

    public final Types Type;
    public final String Identifier;
}
