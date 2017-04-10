package CodeGeneration;
import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Statements.*;

/**
 * Created by toffe on 10-04-2017.
 */
public interface Visitor
{
    /* Declarations */
    Object VisitDcl(Dcl AST, Object o);

    /* Expressions */
    Object VisitBinaryOPExpr (BinaryOPExpr AST, Object o);

    /* Statements */

    Object VisitAssignStmt (AssignStmt AST, Object o);
}
