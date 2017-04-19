package DataStructures.AST.NodeTypes.Expressions;

import DataStructures.AST.NodeTypes.Declarations.StructVarDcl;

/**
 * Created by Anders Brams on 4/19/2017.
 */
public class StructCompSelectExpr extends Expr{
    public String StructVarId;
    public String ComponentId;
    public StructCompSelectExpr(String svi, String ci){
        StructVarId = svi;
        ComponentId = ci;
    }
}
