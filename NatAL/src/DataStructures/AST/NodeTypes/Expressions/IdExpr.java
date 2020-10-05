package DataStructures.AST.NodeTypes.Expressions;

public class IdExpr extends Expr
{
    public IdExpr(String id)
    {
        SetValue("IdExpr");
        ID = id;
    }
    public String ID;
    public String CollectionID;
    public boolean isIterator = false;
    public void SetAsIterator(){ isIterator = true; }
}

