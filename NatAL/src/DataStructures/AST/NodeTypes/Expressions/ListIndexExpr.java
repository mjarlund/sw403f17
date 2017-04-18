package DataStructures.AST.NodeTypes.Expressions;

/**
 * Created by Anders Brams on 4/18/2017.
 */
public class ListIndexExpr extends Expr {
    public IdExpr Id;
    public int Index;

    public ListIndexExpr(IdExpr id, int i){
        Id = id;
        Index = i;
    }
}
