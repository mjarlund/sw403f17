package p4test.AST;

import p4test.AST.Nodes.dcls.FuncDCL;

/**
 * Created by mysjkin on 3/3/17.
 */
public abstract class Visitor
{
    public abstract Object VisitFuncDCL(FuncDCL ast);
}
