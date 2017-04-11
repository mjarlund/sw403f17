package CodeGeneration;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Statements.*;
import Utilities.Reporter;

import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;

/**
 * Created by toffe on 10-04-2017.
 */

// TODO: Nogle steder bruger vi ArrayList andre steder List. Det skal både rettes her og inde i AST-noderene
// TODO: Flyt nogle Get-funktioner op i superklassen, så som GetStatement in de forskellige Stmt-klasser.
// TODO: Mere sigende navne / konsistente navne når man skal gette noget fra de forskellige AST-noder. Plus ikke alle AST-node har getter-metoder

public class CodeGenerator
{
    public ArrayList<String> instructions = new ArrayList<>();

    public CodeGenerator (AST programTree)
    {
        // ¯\_(ツ)_/¯
        Emit("void setup ()");
        Emit("{");
        Emit("}");
        //Statement(programTree);
        /*Declaration((StructDcl) programTree.children.get(0));
        Declaration((FuncDcl) programTree.children.get(1));
        Declaration((FuncDcl) programTree.children.get(2));*/
    }

    // region <Code Generation - Declarations>

    public void Declaration (Dcl AST)
    {
        Emit("VisitDcl - Not implemented");
    }

    public void Declaration (FParamDcl AST)
    {
        Emit("VisitFParamDcl - Not implemented");
    }

    public void Declaration (FParamsDcl AST)
    {
        Emit("VisitFParamsDcl - Not implemented");
    }

    // TODO Still need separation with "," in parameters
    // TODO Need separation with " " in parameters between type and identifier
    public void Declaration (FuncDcl AST)
    {
        String returnType = AST.GetVarDcl().GetType().toString();
        String ID = AST.GetVarDcl().Identifier;
        String parameters = "";

        for (FParamDcl param : AST.GetFormalParamsDcl().GetFParams())
        {
            parameters += param.Type + " " + param.Identifier + ",";
        }

        // Emit code for the function declaration
        Emit(returnType + " " + ID + "(" + parameters + ")");
        Emit("{");
        // Generate code for the block statement
        Statement(AST.GetFuncBlockStmt());

        Emit("}");
    }

    public void Declaration (ListDcl AST)
    {
        Emit("VisitListDcl - Not implemented");
    }
    public void Declaration (StructDcl AST)
    {
        Emit("VisitStructDcl - Not implemented");
    }

    public void Declaration (StructVarDcl AST)
    {
        Emit("VisitStructVarDcl - Not implemented");
    }

    public void Declaration (VarDcl AST)
    {
        Emit("VisitVarDcl - Not implemented");

     //   Emit(AST.GetType().toString() + " " + AST.Identifier + "=" + AST.SOMETHING);
    }

    // endregion

    // region <Code Generation - Expressions>

    public void Expression (BinaryOPExpr AST)
    {
        Emit(AST.GetLeftExpr() + AST.Operation.toString() + AST.GetRightExpr());
    }

    // TODO Mangler stadig "," seperator mellem parameter
    public void Expression (FuncCallExpr AST)
    {
        IdExpr identifier = AST.GetFuncId();
        ArgsExpr arguments = AST.GetFuncArgs();

        String instruction = identifier + "(";

        for (ArgExpr a : arguments.GetArgs())
            instruction += a.GetArg();

        instruction += ");";
        Emit(instruction);
    }

    // TODO Hvordan ved vi om operatoren kommer før eller efter identifier
    // TODO undersøg om C skelner mellem i++ og ++i. Det mener jeg den gør i nogle tilfælde
    public void Expression (UnaryExpr AST)
    {
        Emit(AST.GetOperator() + AST.GetValExpr().GetValue());
    }

    // endregion

    // region <Code Generation - Statements>

    // TODO Giv AssignStmt mere signende funktioner end "GetLeft" og "GetRight"
    public void Statement (AssignStmt AST)
    {
        Emit(AST.GetLeft() + " = " + AST.GetRight());
    }

    public void Statement (BlockStmt AST)
    {
        // Every block begins with a '{'
        String instruction = "";

       // List<AST> statements = AST.GetStatements();

        /*for(AST s : statements)
        {
            VisitStmt(s);
        }*/
        // Magic

        instruction += "IMAGINARY STMT HERE";

       // instruction += "}";

        Emit(instruction);
        // Every block ends with a '}'
    }

    public void Statement (ElseStmt AST)
    {
        Emit("else");
        Statement(AST.GetStatement());
        Emit("end else");
    }

    public void Statement (EmptyStmt AST)
    {
        Emit("VisitEmptyStmt - Not implemented");
    }

    public void Statement (ForEachStmt AST)
    {
        Emit("VisitForEachStmt - Not implemented");
    }

    public void Statement (IfStmt AST)
    {
        Emit("if(" + AST.GetCondition() + ")");
        Statement(AST.GetBlock());
        Emit("end if");
    }

    public void Statement (IOStmt AST)
    {
        Emit("VisitIOStmt - Not implemented");
    }

    public void Statement (ProcCallStmt AST)
    {
        String ID = AST.GetIdentifier().toString();
        ArgsExpr actualParams = AST.GetActualParameters();

        String instruction = ID;

        for(ArgExpr a : actualParams.GetArgs())
        {
            //instruction += a.toString()
        }
    }

    public void Statement (ReturnStmt AST)
    {
        Emit("return " + AST.GetReturnExpr() + ";");
    }

    public void Statement (UntilStmt AST)
    {
        Emit("VisitUntilStmt - Not implemented");
    }

    // endregion

    /* Adds an Arduino C instruction to a list */
    public void Emit (String instruction)
    {
        instructions.add(instruction);

        Reporter.Log("Emitting: " + instruction);
    }

    /* Writes all Arduino C instructions to a file */
    public void ToFile ()
    {
        try{
            PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
            writer.println("The first line");
            writer.println("The second line");
            writer.close();
        } catch (IOException e) {
            // do something
        }

        /*for(String s : instructions)
        {
            // Magic
        }*/
    }
}






// TODO this is bad ¯\_(ツ)_/¯ Eventuelt brug method overloading i stedet for
    /*public void VisitStmt (AST AST)
    {
        if (AST instanceof AssignStmt)
        {
            VisitAssignStmt((AssignStmt) AST);
        }
        else if (AST instanceof BlockStmt)  // Er det her farligt? Stackoverflow?
        {
            VisitBlockStmt((BlockStmt) AST);
        }
        else if (AST instanceof ElseStmt)
        {
            VisitElseStmt((ElseStmt) AST);
        }
        else if (AST instanceof EmptyStmt)
        {
            VisitEmptyStmt((EmptyStmt) AST);
        }
        else if (AST instanceof ForEachStmt)
        {
            VisitForEachStmt((ForEachStmt) AST);
        }
        else if (AST instanceof IfStmt)
        {
            VisitIfStmt((IfStmt) AST);
        }
        else if (AST instanceof IOStmt)
        {
            VisitIOStmt((IOStmt) AST);
        }
        else if (AST instanceof ProcCallStmt)
        {
            VisitProcCallStmt((ProcCallStmt) AST);
        }
        else if (AST instanceof ReturnStmt)
        {
            VisitReturnStmt((ReturnStmt) AST);
        }
        else if (AST instanceof UntilStmt)
        {
            VisitUntilStmt((UntilStmt) AST);
        }
        else
        {
            Reporter.Log("RIP");
        }
    }*/