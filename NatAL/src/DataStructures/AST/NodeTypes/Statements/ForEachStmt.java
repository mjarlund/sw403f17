package DataStructures.AST.NodeTypes.Statements;

import DataStructures.AST.NodeTypes.Declarations.FParamDcl;
import DataStructures.AST.NodeTypes.Expressions.ArgExpr;

/**
 * Created by Anders Brams on 3/23/2017.
 */
public class ForEachStmt extends Stmt{

    public ForEachStmt(FParamDcl element, ArgExpr collection, BlockStmt code){
        children.add(element);
        children.add(collection);
        children.add(code);
    }
}
