package DataStructures.AST.NodeTypes.Declarations;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Statements.BlockStmt;

import java.util.ArrayList;

public class StructDcl extends Dcl {

    public BlockStmt block;

    public StructDcl(VarDcl declaration, BlockStmt contents){
        block = contents;
        block.SetParent(this);

        AddChild(declaration);
        for (AST dcl : contents.children){
        	AddChild(dcl);
        }
        SetValue("StructDcl");
    }

    public VarDcl GetVarDcl(){
        return (VarDcl) children.get(0);
    }

    public BlockStmt GetBlock(){return block;}
}
