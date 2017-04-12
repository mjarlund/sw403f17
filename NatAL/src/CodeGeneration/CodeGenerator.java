package CodeGeneration;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Statements.*;
import Syntax.Parser.Parser;
import Syntax.Scanner.Scanner;
import Utilities.Reporter;

import java.util.ArrayList;
import java.util.List;

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

        // TODO Traverse the AST somehow this is just hardcoded
        /*Declaration((StructDcl) programTree.children.get(0));
        Declaration((FuncDcl) programTree.children.get(1));
        Declaration((FuncDcl) programTree.children.get(2));*/
    }

    private void VisitNode (AST node)
    {
        if (node instanceof AssignStmt)
            Statement((AssignStmt) node);
        else if (node instanceof  BlockStmt)
            Statement((BlockStmt) node);
        else if (node instanceof ElseStmt)
            Statement((ElseStmt) node);
        else if (node instanceof EmptyStmt)
            Statement((EmptyStmt) node);
        else if (node instanceof  ForEachStmt)
            Statement((ForEachStmt) node);
        else if (node instanceof  IfStmt)
            Statement((IfStmt) node);
        else if (node instanceof IOStmt)
            Statement((IOStmt) node);
        else if (node instanceof ReturnStmt)
            Statement((ReturnStmt) node);
        else if (node instanceof UntilStmt)
            Statement((UntilStmt) node);
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

    public void Declaration (FuncDcl AST)
    {
        String returnType = AST.GetVarDcl().GetType().toString();
        String ID = AST.GetVarDcl().Identifier;         // TODO lave disse om til getter funktion
        String parameters = "";

        // Concatenates every parameter in a string separated by a space and a comma
        for (FParamDcl param : AST.GetFormalParamsDcl().GetFParams())
            parameters += param.Type + " " + param.Identifier + ",";

        // Removes the last comma that is created in the for loop
        if (parameters.endsWith(","))
            parameters = parameters.substring(0, parameters.length() - 1);

        // Emit code for the function declaration
        Emit(returnType + " " + ID + "(" + parameters + ")");

        // Generate code for the block statement
        Statement(AST.GetFuncBlockStmt());
    }

    public void Declaration (ListDcl AST)
    {
        Emit("VisitListDcl - Not implemented");

        // Lav array, lav funktion der ændrer størrelsen, sætter ind, fjerne, mm.
    }

    public void Declaration (StructDcl AST)
    {
        Emit("struct " + AST.GetVarDcl().Identifier);
        Emit("{");

        // Emits all content of the struct
        // NOTE: variables do not need to be initialized in Arduino C
        for (VarDcl dcl : AST.GetContents())
            Emit(dcl.GetType() + " " + dcl.Identifier + ";");

        Emit("}");
    }

    public void Declaration (StructVarDcl AST)
    {
        Emit(AST.GetStructType() + "." + AST.GetIdentifier());
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

    // TODO Outputer vist ikke det rigtige
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
        Emit("Test: " + AST.GetLeft().GetValue() + " = " + AST.GetRight().GetValue());
    }

    public void Statement (BlockStmt AST)
    {
        // Every block begins with a '{'
        Emit("{");
        String instruction = "";

        List<AST> statements = AST.GetStatements();

        for (AST node : statements)
        {
            VisitNode(node);
        }

        Emit(instruction);
        Emit("}"); // Every block ends with a '}'
    }

    public void Statement (ElseStmt AST)
    {
        Emit("else");
        Statement(AST.GetStatement());
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
    }

    public void Statement (IOStmt AST)
    {
        Emit("VisitIOStmt - Not implemented");
    }

    public void Statement (ReturnStmt AST)
    {
        Emit("return " + AST.GetReturnExpr() + ";");
    }

    public void Statement (UntilStmt AST)
    {
        Emit("while(!("+AST.GetCondition()+"))");
        Statement(AST.GetBlock());
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
            for(String s : instructions)
            {
                writer.println(s);
            }
            writer.close();
        } catch (IOException e) {
            // do something
        }
    }

    public static void main(String args[])
    {
        String code = "text func1()\n" +
                "pin a is 2\n" +
                "a is digital read from a\n" +
                "analog write 2 to a\n" +
                "boolean b is true and false\n" +
                "return \"a\"\n" +
                "end func1\n" +
                "void func2()\n" +
                "text b is func1()\n end func2\n";
        Scanner sc = new Scanner(code);
        Parser parser = new Parser(sc);
        AST programTree = parser.ParseProgram();

        new CodeGenerator(programTree);

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

    /*
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

     */