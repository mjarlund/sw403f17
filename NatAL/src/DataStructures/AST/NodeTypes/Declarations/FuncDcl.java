package DataStructures.AST.NodeTypes.Declarations;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Expressions.Expr;
import DataStructures.AST.NodeTypes.Statements.BlockStmt;
import DataStructures.AST.NodeTypes.Statements.ReturnStmt;

public class FuncDcl extends Dcl {
    public FuncDcl(VarDcl dcl, FParamsDcl parameters, BlockStmt block)
    {
        block.SetParent(this);
        parameters.SetParent(this);
        dcl.SetParent(this);
        AddChild(dcl);
        AddChild(parameters);
        AddChild(block);
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
    public Expr GetReturnExpr(){
        BlockStmt block = GetFuncBlockStmt();
        if(block!=null){
            for(AST a : block.children){
                if(a instanceof ReturnStmt){
                    ReturnStmt returnStmt = (ReturnStmt) a;
                    return returnStmt.GetReturnExpr();
                }
            }
        }
        return null;
    }
}
