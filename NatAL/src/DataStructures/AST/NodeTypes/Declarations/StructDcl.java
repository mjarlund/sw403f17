package DataStructures.AST.NodeTypes.Declarations;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Statements.BlockStmt;

import java.util.ArrayList;

/**
 * Created by Anders Brams on 3/23/2017.
 */
public class StructDcl extends Dcl {

    public StructDcl(VarDcl declaration, BlockStmt contents){
        children.add(declaration);
        for (AST dcl : contents.children){
            children.add(dcl);
        }
    }

    public VarDcl GetVarDcl(){
        return (VarDcl) children.get(0);
    }

    public ArrayList<VarDcl> GetContents(){
        ArrayList<VarDcl> contents = new ArrayList<>();
        for (int i = 1; i < children.size(); i++){
            contents.add( (VarDcl) children.get(i));
        }
        return contents;
    }
}
