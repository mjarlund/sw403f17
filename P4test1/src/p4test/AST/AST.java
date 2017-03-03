package p4test.AST;

import p4test.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mysjkin on 3/3/17.
 */
public abstract class AST
{
    public final Token token;
    private ArrayList<AST> children;
    public AST(Token token)
    {
        this.token = token;
    }

    public void AddNode(AST node)
    {
        // avoid stackoverflow
        if(children == null)
            children = new ArrayList<AST>();
        children.add(node);
    }
    public String toString()
    {
        if(token != null)
            return token.toString();
        else
            return "null";
    }
    public String treeToString()
    {
        StringBuilder sb = new StringBuilder();
        if(children == null || children.size()==0) return toString();
        if(token != null)
            sb.append(this.toString() + " ");
        for(AST c : children)
        {
            sb.append(c.toString() + " ");
        }
        return sb.toString();
    }
}
