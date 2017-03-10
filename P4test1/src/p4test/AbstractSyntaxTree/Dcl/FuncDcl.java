package p4test.AbstractSyntaxTree.Dcl;


import p4test.AbstractSyntaxTree.Dcl.FuncDcl;
import p4test.AbstractSyntaxTree.Stmt.Block;
import p4test.AbstractSyntaxTree.Stmt.Statement;
import p4test.AbstractSyntaxTree.Types;
import p4test.AbstractSyntaxTree.Visitor;

/**
 * Created by mysjkin on 3/5/17.
 */
public class FuncDcl extends Declaration {
    public FuncDcl(Types returnType, String ientifer, FormalParameters parameters, Block block) {
        ReturnType = returnType;
        Identifier = ientifer;
    }
    public void visit(Visitor v)
    {
        v.visit(this);
    }
    public final Types ReturnType;
    public final String Identifier;
}
