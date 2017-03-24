package DataStructures.AST.NodeTypes.Declarations;

import DataStructures.AST.NodeTypes.Expressions.IdExpr;

import DataStructures.AST.NodeTypes.Types;
/**
 * Created by Anders Brams on 3/23/2017.
 */
public class StructVarDcl extends Dcl{

    public StructVarDcl(IdExpr structType, String ID){
        VarDcl structVarDcl = new VarDcl(Types.STRUCT, ID);
        children.add(structVarDcl);
    }
}
