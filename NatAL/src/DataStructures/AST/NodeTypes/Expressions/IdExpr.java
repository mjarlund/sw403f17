package DataStructures.AST.NodeTypes.Expressions;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class IdExpr extends Expr
{
    public IdExpr(String id)
    {
        SetValue("IdExpr");
        ID = id;
    }
    public String ID;

    /* This is a major hack, but I can't solve this in
     * any other way without completely restructuring
     * the AST. Fuck Java. */
    public String CollectionID;
    public boolean isIterator = false;
    public void SetAsIterator(){ isIterator = true; }
}

