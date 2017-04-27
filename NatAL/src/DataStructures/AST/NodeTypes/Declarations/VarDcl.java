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
    public String GetConvertedType(){
        switch (Type){
            case PIN: return "int";
            case VOID: return "void";
            case BOOL: return "bool";
            case ANALOG: return "analog";
            case CHAR: return "char";
            case DIGITAL: return "digital";
            case FLOAT: return "float";
            case INT: return "int";
            case STRING: return "String";
            case STRUCT: return "struct";
            default: throw new Error("Whoops. Non-convertible type encountered");
        }
    }
}
