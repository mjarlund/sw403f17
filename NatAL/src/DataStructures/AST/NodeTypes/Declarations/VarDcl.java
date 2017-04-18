package DataStructures.AST.NodeTypes.Declarations;

import DataStructures.AST.NodeTypes.Types;

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
    public Types GetType()
    {
        //if(Type.equals(Types.PIN)) return Types.INT;
        //else
        return Type;
    }
}
