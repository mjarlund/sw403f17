package DataStructures.AST.NodeTypes.Declarations;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Statements.BlockStmt;

import java.util.ArrayList;

public class StructDcl extends Dcl {

    public BlockStmt block;

    public StructDcl(VarDcl declaration, BlockStmt contents){
        block = contents;
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

    public ArrayList<VarDcl> GetContents(){
        ArrayList<VarDcl> contents = new ArrayList<>();
        for (int i = 1; i < children.size(); i++){
            contents.add( (VarDcl) children.get(i));
        }
        return contents;
    }
}
