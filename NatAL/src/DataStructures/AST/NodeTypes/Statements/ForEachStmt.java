package DataStructures.AST.NodeTypes.Statements;

import DataStructures.AST.NodeTypes.Declarations.FParamDcl;
import DataStructures.AST.NodeTypes.Declarations.VarDcl;
import DataStructures.AST.NodeTypes.Expressions.ArgExpr;
import DataStructures.AST.NodeTypes.Expressions.IdExpr;

/**
 * Created by Anders Brams on 3/23/2017.
 */
public class ForEachStmt extends Stmt{

    public ForEachStmt(VarDcl element, IdExpr collection, BlockStmt code){
        children.add(element);
        children.add(collection);
        children.add(code);
    }
}
