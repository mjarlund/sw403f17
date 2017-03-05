package p4test.AbstractSyntaxTree;

import p4test.AbstractSyntaxTree.Dcl.FormalParameter;
import p4test.AbstractSyntaxTree.Dcl.FormalParameters;
import p4test.AbstractSyntaxTree.Dcl.FuncDcl;
import p4test.AbstractSyntaxTree.Dcl.VarDcl;
import p4test.AbstractSyntaxTree.Expr.*;
import p4test.AbstractSyntaxTree.Stmt.*;

/**
 * Created by mysjkin on 3/4/17.
 */

/* Uses double dispatch to call the correct visit method
*  Meaning that each node should implement a standard visit method */
public interface Visitor
{
    void visit(FuncDcl node);
    void visit(VarDcl node);
    void visit(BinaryOP node);
    void visit(FormalParameters node);
    void visit(FormalParameter node);
    void visit(Argument node);
    void visit(Arguments node);
    void visit(BoolExpr node);
    void visit(FuncCall node);
    void visit(ValExpr node);
    void visit(Assignment node);
    void visit(Block node);
    void visit(IfStmt node);
    void visit(ProcedureCall node);
    void visit(UntilStmt node);
}
