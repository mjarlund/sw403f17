package DataStructures.AST.NodeTypes.Statements;

import DataStructures.AST.NodeTypes.Expressions.ArgExpr;
import DataStructures.AST.NodeTypes.Expressions.Expr;
import com.sun.org.apache.xpath.internal.Arg;

/**
 * Created by Anders Brams on 5/2/2017.
 */
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
