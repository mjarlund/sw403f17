package CodeGeneration;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Statements.*;
import Utilities.Reporter;

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
// TODO: Mere sigende navne når man skal gette noget fra de forskellige AST-noder,

public class CodeGenerator
{
    public ArrayList<String> instructions = new ArrayList<>();

    public CodeGenerator (AST programTree)
    {
        // ¯\_(ツ)_/¯
        Emit("void setup ()");
        Emit("{");
        Emit("}");
    }

    // region <Code Generation - Declarations>

    public void VisitDcl(Dcl AST)
    {
        Emit("VisitDcl - Not implemented");
    }

    public void VisitFParamDcl (FParamDcl AST)
    {
        Emit("VisitFParamDcl - Not implemented");
    }

    public void VisitFParamsDcl (FParamsDcl AST)
    {
        Emit("VisitFParamsDcl - Not implemented");
    }

    // TODO Still need separation with "," in parameters
    // TODO Need separation with " " in parameters between type and identifier
    public void VisitFuncDcl (FuncDcl AST)
    {
        String returnType = AST.GetVarDcl().GetType().toString();
        String id = AST.GetVarDcl().Identifier;
        String parameters = null;

        for (FParamDcl param : AST.GetFormalParamsDcl().GetFParams())
            parameters += param.Type + param.Identifier;

        if (parameters == null)
            // DO what?

        // Emit code for the function declaration
        Emit(returnType + " " + id + "(" + parameters + ")");

        // Generate code for the block statement
        VisitBlockStmt(AST.GetFuncBlockStmt());
    }

    public void VisitListDcl (ListDcl AST)
    {
        Emit("VisitListDcl - Not implemented");
    }
    public void VisitStructDcl (StructDcl AST)
    {
        Emit("VisitStructDcl - Not implemented");
    }

    public void VisitStructVarDcl (StructVarDcl AST)
    {
        Emit("VisitStructVarDcl - Not implemented");
    }

    public void VisitVarDcl (VarDcl AST)
    {
        Emit("VisitVarDcl - Not implemented");
    }

    // endregion

    // region <Code Generation - Expressions>

    public void VisitBinaryOPExpr(BinaryOPExpr AST)
    {
        Emit(AST.GetLeftExpr() + AST.Operation.toString() + AST.GetRightExpr());
    }

    public void FuncCallExpr (FuncCallExpr AST)
    {
        ArgsExpr arguments = AST.GetFuncArgs();
        String instruction = AST.GetFuncId().ID + "(";

        for (ArgExpr a : arguments.GetArgs())
            instruction += a.GetArg();

        instruction += ");";
        Emit(instruction);
    }

    public void VisitUnaryExpr (UnaryExpr AST)
    {
        Emit("VisitUnaryExpr - Not implemented");
    }

    // endregion

    // region <Code Generation - Statements>

    public void VisitAssignStmt (AssignStmt AST)
    {
        Emit(AST.GetLeft() + " = " + AST.GetRight());
    }

    public void VisitBlockStmt (BlockStmt AST)
    {
        // Every block begins with a '{'
        String instruction = "{";

        List<Stmt> statements = AST.GetStatements();

        for(Stmt s : statements)
        {
            VisitStmt(s);
        }
        // Magic



        instruction += "}";

        Emit(instruction);
        // Every block ends with a '}'
    }

    public void VisitElseStmt (ElseStmt AST)
    {
        Emit("else");
        VisitStmt(AST.GetStatement());
    }

    public void VisitEmptyStmt (EmptyStmt AST)
    {
        Emit("VisitEmptyStmt - Not implemented");
    }

    public void VisitForEachStmt (ForEachStmt AST)
    {
        Emit("VisitForEachStmt - Not implemented");
    }

    public void VisitIfStmt (IfStmt AST)
    {
        Emit("if(" + AST.GetCondition() + ")");
        VisitBlockStmt(AST.GetBlock());
    }

    public void VisitIOStmt (IOStmt AST)
    {
        Emit("VisitIOStmt - Not implemented");
    }

    public void VisitProcCallStmt (ProcCallStmt AST)
    {
        String ID = AST.GetIdentifier().toString();
        ArgsExpr actualParams = AST.GetActualParameters();

        String instruction = ID;

        for(ArgExpr a : actualParams.GetArgs())
        {
            //instruction += a.toString()
        }
    }

    public void VisitReturnStmt (ReturnStmt AST)
    {
        Emit("return " + AST.GetReturnExpr() + ";");
    }

    // TODO this is bad ¯\_(ツ)_/¯ Eventuelt brug method overloading i stedet for
    public void VisitStmt (Stmt AST)
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
    }

    public void VisitUntilStmt (UntilStmt AST)
    {
        Emit("VisitUntilStmt - Not implemented");
    }

    // endregion

    /* Adds an Arduino C instruction to a list */
    public void Emit (String instruction)
    {
        instructions.add(instruction);
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
