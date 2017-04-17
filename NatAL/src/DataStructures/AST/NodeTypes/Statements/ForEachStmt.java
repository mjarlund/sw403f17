package DataStructures.AST.NodeTypes.Statements;

import DataStructures.AST.NodeTypes.Declarations.FParamDcl;
import DataStructures.AST.NodeTypes.Declarations.VarDcl;
import DataStructures.AST.NodeTypes.Expressions.ArgExpr;
import DataStructures.AST.NodeTypes.Expressions.IdExpr;
import DataStructures.AST.NodeTypes.Types;

/**
 * Created by Anders Brams on 3/23/2017.
 */
public class ForEachStmt extends Stmt{

    public ForEachStmt(VarDcl element, IdExpr collection, BlockStmt code){
        children.add(element);
        children.add(collection);
        children.add(code);
        SetValue("ForEachStmt");
    }

    public Types GetElementType() {
        VarDcl tmp = (VarDcl) children.get(0);
        return tmp.GetType();
    }

    public String GetCollectionId() {
        IdExpr tmp = (IdExpr) children.get(1);
        return tmp.ID;
    }

    public String GetElementId() {
        VarDcl tmp = (VarDcl) children.get(0);
        return tmp.Identifier;
    }
}
