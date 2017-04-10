package CodeGeneration;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Statements.*;
import Utilities.Reporter;

import java.util.ArrayList;

/**
 * Created by toffe on 10-04-2017.
 */
public class CodeGenerator implements Visitor
{
    public ArrayList<String> instructions = new ArrayList<>();

    public CodeGenerator (AST programTree)
    {

    }

    public Object VisitDcl(Dcl AST, Object o)
    {

        return null;
    }

    public Object VisitAssignStmt(AssignStmt AST, Object o)
    {
        //Emit(AST.)
        return null;
    }

    public Object VisitBinaryOPExpr(BinaryOPExpr AST, Object o)
    {
        Emit(AST.GetLeftExpr() + AST.Operation.toString() + AST.GetRightExpr());
        return null;
    }


    /* Adds an Arduino C instruction to a list */
    public void Emit (String instruction)
    {
        instructions.add(instruction);
    }

    /* Writes all Arduino C instructions to a file */
    public void ToFile ()
    {
        for(String s : instructions)
        {
            // Magic
        }
    }
}
