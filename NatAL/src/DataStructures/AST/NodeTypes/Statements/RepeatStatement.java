package DataStructures.AST.NodeTypes.Statements;

import DataStructures.AST.NodeTypes.Expressions.Expr;

public class RepeatStatement extends Stmt {

    public RepeatStatement(Expr iterations, BlockStmt code){
        children.add(iterations);
        children.add(code);
        SetValue("RepeatStmt");
    }

    public Expr GetIterationExpression(){
        return (Expr) children.get(0);
    }

    public BlockStmt GetBlock(){
        return (BlockStmt) children.get(1);
    }
}
