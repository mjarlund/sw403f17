package DataStructures.AST.NodeTypes.Declarations;

import DataStructures.AST.NodeTypes.Types;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class FParamDcl extends Dcl
{
    public FParamDcl(Types type, String identifier)
    {
        Type = type;
        Identifier = identifier;
        SetValue("FParamDcl");
    }
    public FParamDcl(Types type, String structType, String identifier)
    {
        Type = type;
        Identifier = identifier;
        this.structType = structType;
        SetValue("FParamDcl");
    }

    public final Types Type;
    public final String Identifier;
    private String structType;
    public String GetStructType(){return structType;}

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
            case STRING: return "string";
            case STRUCT: return "struct";
            default: return "NON_CONVERTABLE_TYPE";
        }
    }
}