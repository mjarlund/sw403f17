package CodeGeneration;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Statements.*;
import Syntax.Parser.Parser;
import Syntax.Scanner.Scanner;
import Test.InputTester;
import Utilities.IVisitor;
import Utilities.Reporter;
import Utilities.VisitorDriver;
import java.util.ArrayList;
import java.io.*;

// TODO: Nogle steder bruger vi ArrayList andre steder List. Det skal både rettes her og inde i AST-noderene
// TODO: Mere sigende navne / konsistente navne når man skal gette noget fra de forskellige AST-noder. Plus ikke alle AST-node har getter-metoder

public class CodeGenerator implements IVisitor
{
    public ArrayList<String> instructions = new ArrayList<>();
    private VisitorDriver visitValue = new VisitorDriver(this);
    public CodeGenerator (AST programTree)
    {
        // ¯\_(ツ)_/¯
        VisitChildren(programTree);
    }

    public void VisitChildren(AST root) {
        try {
            for (AST child : root.children) {
                String switchValue = (child.GetValue() != null) ? child.GetValue() : ((IdExpr) child).ID;
                visitValue.Visit(switchValue, child);
            }
        }
        catch (ClassCastException e){
            System.out.println(e);
        }
    }

    public Object Visit(StructVarDcl dcl) {
        //Emit("StructVarDcl - Not implemented");

        Emit(dcl.GetStructType().ID);
        Emit(" ");
        Emit(dcl.GetIdentifier().ID + " = { 0, 0, 0 }");
        return null;
    }

    public Object Visit(IOStmt stmt) {
        Emit(stmt.GetMode().name()+ stmt.GetOperation().Value + "("+stmt.GetPin().ID+", ");
        visitValue.Visit(stmt.GetWriteVal().GetValue(), stmt.GetWriteVal());
        Emit(")");
        return null;
    }

    public Object Visit(IOExpr expr) {
        Emit(expr.GetMode()+expr.GetOperation().Value+"("+expr.GetPin().ID+")");
        return null;
    }

    public Object Visit(IfStmt stmt) {
        Emit("if(");
        visitValue.Visit(stmt.GetCondition().GetValue(), stmt.GetCondition());
        Emit( ")");
        visitValue.Visit(stmt.GetBlock().GetValue(), stmt.GetBlock());
        visitValue.Visit(stmt.GetElse().GetValue(), stmt.GetElse());

        return null;
    }

    public Object Visit(ElseStmt stmt) {
        Emit("else");
        VisitChildren(stmt);
        return null;
    }

    public Object Visit(UntilStmt stmt) {
        Emit("while(!(");
        visitValue.Visit(stmt.GetCondition().GetValue(), stmt.GetCondition());
        Emit("))");
        visitValue.Visit(stmt.GetBlock().GetValue(), stmt.GetBlock());
        return null;
    }


    public Object Visit(ForEachStmt stmt) {
        Emit("for (int i = 0; < sizeof("+stmt.GetCollectionId()+") - 1; i++)\n");
        visitValue.Visit(stmt.GetBlock().GetValue(), stmt.GetBlock());
        return null;
    }

    public Object Visit(ReturnStmt stmt) {
        Emit("return " );
        VisitChildren(stmt);
        return null;
    }

    public Object Visit(FuncCallExpr expr) {
        VisitChildren(expr);
        return null;
    }

    public Object Visit(BinaryOPExpr expr) {
        visitValue.Visit(expr.GetLeftExpr().GetValue(),expr.GetLeftExpr());
        Emit(expr.Operation.Value);
        visitValue.Visit(expr.GetRightExpr().GetValue(),expr.GetRightExpr());
        return null;
    }

    public Object Visit(BoolExpr expr) {
        visitValue.Visit(expr.GetLeftExpr().GetValue(), expr.GetLeftExpr());
        Emit(" " + expr.Operator.Value+ " ");
        visitValue.Visit(expr.GetRightExpr().GetValue(),expr.GetRightExpr());
        return null;
    }

    public Object Visit(AssignStmt stmt) {
        visitValue.Visit(stmt.GetLeft().GetValue(),stmt.GetLeft());
        Emit(" = ");
        visitValue.Visit(stmt.GetRight().GetValue(), stmt.GetRight());
        return null;
    }

    public Object Visit(UnaryExpr expr) {
        Emit(expr.GetOperator().Value);
        VisitChildren(expr);
        return null;
    }

    public Object Visit(ValExpr lit) {
        Emit(lit.LiteralValue.Value);
        return null;
    }

    public Object Visit(VarDcl node) {
        Emit(node.GetType().name() + " " + node.Identifier);
        return null;
    }

    public Object Visit(ListDcl node) {
        String elements = "";
        visitValue.Visit(node.GetDeclaration().GetValue(), node.GetDeclaration());
        Emit("[] = {");
        for (ArgExpr element: node.GetElements().GetArgs())
            elements += element.GetArg().LiteralValue.Value + ",";

        if (elements.endsWith(","))
            elements = elements.substring(0, elements.length() - 1);

        Emit(elements + "}");
        return null;
    }

    public Object Visit(IdExpr node) {
        Emit(node.ID);
        return null;
    }

    public Object Visit(FParamsDcl node) {
        String parameters="(";
        for (FParamDcl param : node.GetFParams())
            parameters += param.Type + " " + param.Identifier + ",";

        if (parameters.endsWith(","))
            parameters = parameters.substring(0, parameters.length() - 1);

        Emit(parameters + ")");
        return null;
    }

    public Object Visit(ArgsExpr node) {
        String parameters="(";
        for (ArgExpr param : node.GetArgs())
            parameters += param.GetValue() + ",";
        if (parameters.endsWith(","))
            parameters = parameters.substring(0, parameters.length() - 1);

        Emit(parameters + ")");
        return null;
    }

    public Object Visit(FuncDcl node) {
        VisitChildren(node);
        return null;
    }

    public Object Visit(StructDcl node) {
        Emit("struct " + node.GetVarDcl().Identifier);
        Emit("{\n");

        // Emits all content of the struct
        // NOTE: variables do not need to be initialized in Arduino C
        for (VarDcl dcl : node.GetContents())
            Emit(dcl.GetType() + " " + dcl.Identifier + ";\n");

        Emit("}\n");
        return null;
    }

    public Object Visit(BlockStmt block) {
        Emit("{ \n");
        for (AST child : block.children) {
            String switchValue = (child.GetValue() != null) ? child.GetValue() : ((IdExpr) child).ID;
            visitValue.Visit(switchValue, child);
            switch (switchValue){
                case "UntilStmt":
                case "ForEachStmt":
                case "IfStmt":
                    break;
                default: Emit(";\n");
            }

        }
        Emit("} \n");
        return null;
    }

    public Object Visit(ListIndexExpr block) {
        Emit(block.GetId().ID + "["+ block.Index +"]");
        return null;
    }

    public Object Visit(StructCompSelectExpr node) {
        Emit(node.StructVarId + "." + node.ComponentId);
        return null;
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
            PrintWriter writer = new PrintWriter("Arduino-C-Program.txt", "UTF-8");
            for(String s : instructions)
            {
                writer.print(s);
            }
            writer.close();
        } catch (IOException e) {
            // do something
        }
    }

    public static void main(String args[]) throws IOException {

        Scanner sc = new Scanner(InputTester.readFile("src/CodeGeneration/FinalProgram.txt"));
        Parser parser = new Parser(sc);
        AST programTree = parser.ParseProgram();
        CodeGenerator c = new CodeGenerator(programTree);
        c.ToFile();

    }
}