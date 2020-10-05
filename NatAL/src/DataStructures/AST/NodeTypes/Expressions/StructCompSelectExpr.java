package DataStructures.AST.NodeTypes.Expressions;

import DataStructures.AST.AST;

public class StructCompSelectExpr extends Expr{
    public String StructVarId;
    public String ComponentId;
    public StructCompSelectExpr(String svi, String ci){
        StructVarId = svi;
        ComponentId = ci;
    }
    public StructCompSelectExpr GetChildComp(){
        if(children.size()==0) return null;
        AST subcomp = children.get(0);
        return (StructCompSelectExpr)subcomp;
    }
}
