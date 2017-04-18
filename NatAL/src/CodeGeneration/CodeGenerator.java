package CodeGeneration;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Statements.*;
import Syntax.Parser.Parser;
import Syntax.Scanner.Scanner;
import Utilities.IVisitor;
import Utilities.Reporter;
import Utilities.VisitorDriver;

import java.util.ArrayList;
import java.util.List;

import java.io.*;

/**
 * Created by toffe on 10-04-2017.
 */

// TODO: Nogle steder bruger vi ArrayList andre steder List. Det skal både rettes her og inde i AST-noderene
// TODO: Flyt nogle Get-funktioner op i superklassen, så som GetStatement in de forskellige Stmt-klasser.
// TODO: Mere sigende navne / konsistente navne når man skal gette noget fra de forskellige AST-noder. Plus ikke alle AST-node har getter-metoder

public class CodeGenerator implements IVisitor
{
    public ArrayList<String> instructions = new ArrayList<>();
    private VisitorDriver visitValue = new VisitorDriver(this);
    public CodeGenerator (AST programTree)
    {
        // ¯\_(ツ)_/¯
        Emit("void setup ()");
        Emit("{");
        VisitChildren(programTree);
        Emit("}");

    }


    public void Declaration (Dcl AST)
    {
        Emit("VisitDcl - Not implemented");
    }

    public void Declaration (FParamDcl AST)
    {
        Emit("VisitFParamDcl - Not implemented");
    }

    public void Declaration (ListDcl AST)
    {
        Emit("VisitListDcl - Not implemented");
        // Lav array, lav funktion der ændrer størrelsen, sætter ind, fjerne, mm.
    }

    public void Statement (ElseStmt AST)
    {
        Emit("else");
        //Statement(AST.GetStatement());
    }








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
        String code = "structure book\n" +
                "number c\n" +
                "end book\n" +
                "void func2(number a, number b)\n"+
                "book z \n" +
                "z.c is 2\n" +
                "end func2\n " +
                "void func3(boolean a, boolean b)\n" +
                "boolean c is a and b\n" +
                "end func3\n";

            Scanner sc = new Scanner(code);
            Parser parser = new Parser(sc);
            AST programTree = parser.ParseProgram();
            CodeGenerator c = new CodeGenerator(programTree);
            c.ToFile();



    }

    public void VisitChildren(AST root) {
        for (AST child : root.children) {
            try {
                String switchValue = (child.GetValue() != null) ? child.GetValue() : ((IdExpr) child).ID;
                visitValue.Visit(switchValue, child);
            } catch (ClassCastException e){
                //System.out.println(e.getCause().toString());
            }
        }
    }

    public Object Visit(StructVarDcl dcl) {
        Emit(dcl.GetStructType() + "." + dcl.GetIdentifier());
        return null;
    }

    public Object Visit(IOStmt stmt) {
        Emit("VisitIOStmt - Not implemented");
        return null;
    }

    public Object Visit(IOExpr expr) {
        return null;
    }

    public Object Visit(IfStmt stmt) {

        Emit("if(" + stmt.GetCondition() + ")");
        VisitChildren(stmt.GetBlock());
        return null;
    }

    public Object Visit(UntilStmt stmt) {
        Emit("while(!("+stmt.GetCondition()+"))");
        VisitChildren(stmt.GetBlock());
        return null;
    }

    @Override
    public Object Visit(ForEachStmt stmt) {
        Emit("VisitForEachStmt - Not implemented");
        return null;
    }

    public Object Visit(ReturnStmt stmt) {
        Emit("return " + stmt.GetReturnExpr() + ";");
        return null;
    }

    public Object Visit(FuncCallExpr expr) {
        // TODO Mangler stadig "," seperator mellem parameter
            IdExpr identifier = expr.GetFuncId();
            ArgsExpr arguments = expr.GetFuncArgs();

            String instruction = identifier + "(";

            for (ArgExpr a : arguments.GetArgs())
                instruction += a.GetArg();

            instruction += ");";
            Emit(instruction);
        return null;
    }

    public Object Visit(BinaryOPExpr expr) {
        Emit(expr.GetLeftExpr() + expr.Operation.toString() + expr.GetRightExpr());
        return null;
    }

    public Object Visit(BoolExpr expr) {
        return null;
    }

    public Object Visit(AssignStmt stmt) {
        Emit("Test: " + stmt.GetLeft().GetValue() + " = " + stmt.GetRight().GetValue());
        return null;
    }

    public Object Visit(UnaryExpr expr) {
        // TODO Outputer vist ikke det rigtige
        // TODO Hvordan ved vi om operatoren kommer før eller efter identifier
        // TODO undersøg om C skelner mellem i++ og ++i. Det mener jeg den gør i nogle tilfælde
        Emit(expr.GetOperator() + expr.GetValExpr().GetValue());
        return null;
    }

    public Object Visit(ValExpr lit) {
        return null;
    }

    public Object Visit(VarDcl node) {
        Emit("VisitVarDcl - Not implemented");

        //   Emit(node.GetType().toString() + " " + node.Identifier + "=" + node.SOMETHING);
        return null;
    }

    @Override
    public Object Visit(ListDcl node) {
        return null;
    }

    public Object Visit(IdExpr node) {
        return null;
    }

    public Object Visit(FParamsDcl node) {
        Emit("VisitFParamsDcl - Not implemented");
        return null;
    }

    public Object Visit(ArgsExpr node) {
        return null;
    }

    public Object Visit(FuncDcl node) {
        String returnType = node.GetVarDcl().GetType().toString();
        String ID = node.GetVarDcl().Identifier;         // TODO lave disse om til getter funktion
        String parameters = "";

        // Concatenates every parameter in a string separated by a space and a comma
        for (FParamDcl param : node.GetFormalParamsDcl().GetFParams())
            parameters += param.Type + " " + param.Identifier + ",";

        // Removes the last comma that is created in the for loop
        if (parameters.endsWith(","))
            parameters = parameters.substring(0, parameters.length() - 1);

        // Emit code for the function declaration
        Emit(returnType + " " + ID + "(" + parameters + ")");

        // Generate code for the block statement
        VisitChildren(node.GetFuncBlockStmt());
        return null;
    }

    public Object Visit(StructDcl node) {
        Emit("struct " + node.GetVarDcl().Identifier);
        Emit("{");

        // Emits all content of the struct
        // NOTE: variables do not need to be initialized in Arduino C
        for (VarDcl dcl : node.GetContents())
            Emit(dcl.GetType() + " " + dcl.Identifier + ";");

        Emit("}");
        return null;
    }

    public Object Visit(BlockStmt block) {
        // TODO Giv AssignStmt mere signende funktioner end "GetLeft" og "GetRight"
        // Every block begins with a '{'
        Emit("{");
        String instruction = "";

        List<AST> statements = block.GetStatements();

        for (AST node : statements)
        {
            VisitChildren(node);
            //VisitNode(node);
        }

        Emit(instruction);
        Emit("}"); // Every block ends with a '}'
        return null;
    }

    public Object Visit(ListIndexExpr expr){
        return null;
    }
}



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