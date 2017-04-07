package DataStructures.AST.NodeTypes.Declarations;

import DataStructures.AST.NodeTypes.Expressions.IdExpr;

import DataStructures.AST.NodeTypes.Types;
/**
 * Created by Anders Brams on 3/23/2017.
 */
public class StructVarDcl extends Dcl{

    public IdExpr Type;
    public IdExpr ID;
    public StructVarDcl(IdExpr structType, IdExpr id){
        Type = structType;
        ID = id;
    }
}
