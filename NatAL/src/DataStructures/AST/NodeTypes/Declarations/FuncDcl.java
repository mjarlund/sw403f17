package DataStructures.AST.NodeTypes.Declarations;

import DataStructures.AST.NodeTypes.Statements.BlockStmt;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class FuncDcl extends Dcl {
    public FuncDcl(VarDcl dcl, FParamsDcl parameters, BlockStmt block)
    {
        children.add(dcl);
        children.add(parameters);
        children.add(block);
    }
}
