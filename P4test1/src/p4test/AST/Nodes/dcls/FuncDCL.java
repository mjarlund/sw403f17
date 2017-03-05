package p4test.AST.Nodes.dcls;

import p4test.AST.Nodes.dcls.Dcl;
import p4test.AST.Nodes.stmts.Stmt;
import p4test.AST.Visitor;
import p4test.Token;

/**
 * Created by mysjkin on 3/3/17.
 */
public class FuncDCL extends Dcl
{
    public FuncDCL(String returnType , Token identifier, Stmt body)
    {
        super(identifier);
    }
    public Object AcceptVisitor(Visitor v)
    {
        return v.VisitFuncDCL(this);
    }
}
