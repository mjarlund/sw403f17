package DataStructures.AST.NodeTypes.Declarations;

import DataStructures.AST.NodeTypes.Expressions.IdExpr;
/**
 * Created by Anders Brams on 3/23/2017.
 */
public class StructVarDcl extends Dcl{

    public IdExpr Type;
    IdExpr ID;
    public StructVarDcl(IdExpr structType, IdExpr id){
        children.add(structType);
        children.add(id);
        Type = structType;
        ID = id;
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
