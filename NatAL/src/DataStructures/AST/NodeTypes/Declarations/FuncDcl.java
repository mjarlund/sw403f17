package DataStructures.AST.NodeTypes.Declarations;

import CodeGeneration.CodeGenerator;
import DataStructures.AST.NodeTypes.Statements.BlockStmt;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class FuncDcl extends Dcl {
    public FuncDcl(VarDcl dcl, FParamsDcl parameters, BlockStmt block)
    {
        block.SetParent(this);
        parameters.SetParent(this);
        dcl.SetParent(this);
        children.add(dcl);
        children.add(parameters);
        children.add(block);
        SetValue("FuncDcl");

        //CodeGenerator c = new CodeGenerator();
        //c.Declaration(this);
    }
    private String endIdentifier;
    public void SetEndIdentifier(String endId)
    {
        endIdentifier = endId;
    }
    public String GetEndIdentifier()
    {
        return endIdentifier;
    }
    public VarDcl GetVarDcl()
    {
        return (VarDcl) children.get(0);
    }
    public FParamsDcl GetFormalParamsDcl()
    {
        return (FParamsDcl) children.get(1);
    }
    public BlockStmt GetFuncBlockStmt()
    {
        return (BlockStmt) children.get(2);
    }
}
