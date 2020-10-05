package DataStructures.AST.NodeTypes.Expressions;

public class ListIndexExpr extends Expr {
    public IdExpr Id;
    public int Index;

    public ListIndexExpr(IdExpr id, int i){
        Id = id;
        Index = i;
    }
    public IdExpr GetId(){return Id;}
    public int GetIndex(){return Index;}
}
