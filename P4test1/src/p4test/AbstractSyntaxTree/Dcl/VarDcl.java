package p4test.AbstractSyntaxTree.Dcl;


import p4test.AbstractSyntaxTree.Types;
import p4test.AbstractSyntaxTree.Visitor;

/**
 * Created by mysjkin on 3/5/17.
 */
public class VarDcl extends Declaration
{
    public VarDcl(Types type, String identifier)
    {
        Type = type;
        Identifier = identifier;
    }
    public void visit(Visitor v)
    {
        v.visit(this);
    }
    public final Types Type;
    public final String Identifier;
}
