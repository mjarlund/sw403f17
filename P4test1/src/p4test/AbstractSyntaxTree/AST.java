package p4test.AbstractSyntaxTree;

import p4test.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mysjkin on 3/4/17.
 */
public class AST
{
    public Token token = null;
    public List<AST> nodes;
    public AST()
    {

    }
    public void AddNode(AST node)
    {
        if(nodes == null)
            nodes = new ArrayList<AST>();
        nodes.add(node);
    }
}
