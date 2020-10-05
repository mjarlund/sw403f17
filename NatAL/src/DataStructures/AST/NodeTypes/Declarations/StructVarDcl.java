package DataStructures.AST.NodeTypes.Declarations;

import DataStructures.AST.NodeTypes.Expressions.IdExpr;

public class StructVarDcl extends Dcl{

    public IdExpr Type;
    IdExpr ID;
    public StructVarDcl(IdExpr structType, IdExpr id){
    	AddChild(structType);
    	AddChild(id);
        Type = structType;
        ID = id;
        SetValue("StructVarDcl");
    }
    public IdExpr GetStructType()
    {
        return (IdExpr) children.get(0);
    }
    public IdExpr GetIdentifier()
    {
        return (IdExpr) children.get(1);
    }
}
