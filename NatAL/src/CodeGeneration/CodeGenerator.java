package CodeGeneration;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Modes;
import DataStructures.AST.NodeTypes.Statements.*;
import Exceptions.ClassCastExceptionError;
import Semantics.Scope.SemanticAnalyzer;
import Syntax.Parser.Parser;
import Syntax.Scanner.Scanner;
import Test.InputTester;
import Utilities.IVisitor;
import Utilities.Reporter;
import Utilities.VisitorDriver;
import java.util.ArrayList;
import java.io.*;

public class CodeGenerator implements IVisitor
{
    public ArrayList<String> instructions = new ArrayList<>();
    private VisitorDriver visitValue = new VisitorDriver(this);
    private SemanticAnalyzer SM;
    private String currentIdentifier = "_";
    private int indentation = 0;
    private boolean ListUsed = false;
    private String CreateIndentation(){
        String indent = "";
        for (int i = 0; i < indentation; i++){
            indent += "  ";
        }
        return indent;
    }

    public CodeGenerator (AST programTree, SemanticAnalyzer sm)
    {
        // ¯\_(ツ)_/¯
        SM = sm;
        VisitChildren(programTree);
    }

    public void VisitChildren(AST root) {
        AST currentNode = null;

        try {
            for (AST child : root.children) {
                currentNode = child;
                String switchValue = (child.GetValue() != null) ? child.GetValue() : ((IdExpr) child).ID;
                visitValue.Visit(switchValue, child);
            }
        }
        catch (ClassCastException e){
            Reporter.Error(new ClassCastExceptionError(currentNode.GetValue() + " could not be cast on line: " + currentNode.GetLineNumber()));
        }
    }

    public Object Visit(StructVarDcl dcl) {
        Emit(dcl.GetStructType().ID+ " " + dcl.GetIdentifier().ID);
        return null;
    }

    public Object Visit(RepeatStatement node){
        //TODO: Can only take ID's or simple numbers as iterators - maybe extend this?
        GenerateIdentifier();
        if (node.GetIterationExpression() instanceof IdExpr){
            String i = ((IdExpr)node.GetIterationExpression()).ID;
            Emit("for (int "+ currentIdentifier +" = 0;" + currentIdentifier +"<" + i + "; " + currentIdentifier +"++)");
        } else {
            String i = ((ValExpr)node.GetIterationExpression()).LiteralValue.Value;
            Emit("for (int "+ currentIdentifier +" = 0;"+currentIdentifier+" <" + i + "; " + currentIdentifier +"++)");
        }
        visitValue.Visit(node.GetBlock().GetValue(), node.GetBlock());
        return null;
    }

    public Object Visit(IOStmt stmt) {
        Emit("pinMode("+ stmt.GetPin().ID +",OUTPUT);\n");
        String mode = stmt.GetMode().equals(Modes.ANALOG) ? "analog" : "digital";
        String op = stmt.GetOperation().Value.equals("write") ? "Write" : "Read";
        Emit(mode+op+"("+stmt.GetPin().ID+", ");
        visitValue.Visit(stmt.GetWriteVal().GetValue(), stmt.GetWriteVal());
        Emit(")");
        return null;
    }

    public Object Visit(IOExpr expr) {
        String mode = expr.GetMode().equals(Modes.ANALOG) ? "analog" : "digital";
        String op = expr.GetOperation().Value.equals("write") ? "Write" : "Read";
        Emit(mode+op+"("+expr.GetPin().ID+")");
        return null;
    }

    public Object Visit(IfStmt stmt) {
        Emit("if (");
        visitValue.Visit(stmt.GetCondition().GetValue(), stmt.GetCondition());
        Emit( ")");
        visitValue.Visit(stmt.GetBlock().GetValue(), stmt.GetBlock());
        if (stmt.children.size() > 2)
            visitValue.Visit(stmt.GetElse().GetValue(), stmt.GetElse());

        return null;
    }

    public Object Visit(ElseStmt stmt) {
        Emit(CreateIndentation() + "else");
        VisitChildren(stmt);
        return null;
    }

    public Object Visit(UntilStmt stmt) {
        Emit("while (!(");
        visitValue.Visit(stmt.GetCondition().GetValue(), stmt.GetCondition());
        Emit("))");
        visitValue.Visit(stmt.GetBlock().GetValue(), stmt.GetBlock());
        return null;
    }


    public Object Visit(ForEachStmt stmt) {
        GenerateIdentifier();
        Emit("for (int "+ currentIdentifier +" = 0;"+currentIdentifier+" < sizeof("+stmt.GetCollectionId()+") - 1; " + currentIdentifier +"++)");
        visitValue.Visit(stmt.GetBlock().GetValue(), stmt.GetBlock());
        return null;
    }

    public Object Visit(ReturnStmt stmt) {
        Emit("return ");
        VisitChildren(stmt);
        return null;
    }

    public Object Visit(FuncCallExpr expr) {
        VisitChildren(expr);
        return null;
    }

    public Object Visit(BinaryOPExpr expr) {
        Emit("(");
        visitValue.Visit(expr.GetLeftExpr().GetValue(),expr.GetLeftExpr());
        Emit(expr.Operation.Value);
        visitValue.Visit(expr.GetRightExpr().GetValue(),expr.GetRightExpr());
        Emit(")");
        return null;
    }

    public Object Visit(BoolExpr expr) {
        visitValue.Visit(expr.GetLeftExpr().GetValue(), expr.GetLeftExpr());
        Emit(" " + expr.GetConvertedType() + " ");
        visitValue.Visit(expr.GetRightExpr().GetValue(),expr.GetRightExpr());
        return null;
    }

    public Object Visit(AssignStmt stmt) {
        if (stmt.GetRight().GetValue().equals("IOExpr")){
            Emit("pinMode("+ ((IOExpr)stmt.GetRight()).GetPin().ID +", INPUT);\n");
        }
        visitValue.Visit(stmt.GetLeft().GetValue(),stmt.GetLeft());
        Emit(" = ");
        visitValue.Visit(stmt.GetRight().GetValue(), stmt.GetRight());
        if (indentation == 0) Emit(";\n"); /* Otherwise only added inside blocks */
        return null;
    }

    public Object Visit(UnaryExpr expr) {
        Emit(expr.GetConvertedType());
        VisitChildren(expr);
        return null;
    }

    public Object Visit(ValExpr lit) {
        Emit(lit.LiteralValue.Value);
        return null;
    }

    public Object Visit(VarDcl node) {
        Emit(node.GetConvertedType() + " " + node.Identifier);
        return null;
    }

    public Object Visit(ListDcl node) {
        Emit("list"+"<"+node.GetDeclaration().GetConvertedType()+"> " + node.GetDeclaration().Identifier +";\n");
        for (ArgExpr element: node.GetElements().GetArgs()) {

            Emit(node.GetDeclaration().Identifier+".Add(");
            ValExpr expr = (ValExpr) element.GetArg();
            Emit( expr.LiteralValue.Value + ");\n");
        }
        ListUsed = true;
        return null;
    }

    public Object Visit(IdExpr node) {
        if (node.isIterator){
            Emit(node.CollectionID + "["+currentIdentifier+"]");
        } 
        else if(node.ID.contentEquals("print"))
        {
        	Emit("Serial.println");
        }
        else {
            Emit(node.ID);
        }
        return null;
    }

    public Object Visit(FParamsDcl node) {
        String parameters="(";
        for (FParamDcl param : node.GetFParams())
            parameters += param.GetConvertedType() + " " + param.Identifier + ",";

        if (parameters.endsWith(","))
            parameters = parameters.substring(0, parameters.length() - 1);

        Emit(parameters + ")");
        return null;
    }

    public Object Visit(ArgsExpr node) {
    	Emit("(");
        int size = node.GetArgs().size();
        for (ArgExpr param : node.GetArgs())
        {
            visitValue.Visit(param.GetArg().GetValue(),param.GetArg());
            size--;
            if(size>0) {
                Emit(",");
            }
        }

        Emit(")");
        return null;
    }

    public Object Visit(FuncDcl node) {
        Emit("\n");
        VisitChildren(node);
        return null;
    }

    public Object Visit(StructDcl node) {
        Emit("struct " + node.GetVarDcl().Identifier);
        Emit("\n");

        visitValue.Visit(node.GetBlock().GetValue(), node.GetBlock());

        Emit("\n");
        return null;
    }

    public Object Visit(BlockStmt block) {
        indentation++;
        Emit("{ \n");
        if(block.GetParent() != null && block.GetParent().GetValue().matches("FuncDcl"))
        {
        	if(((FuncDcl)block.GetParent()).GetEndIdentifier().matches("setup")){
        		Emit("Serial.begin(9600); \n");
        	}
        }
        for (AST child : block.children) {
            Emit(CreateIndentation());
            String switchValue = (child.GetValue() != null) ? child.GetValue() : ((IdExpr) child).ID;
            visitValue.Visit(switchValue, child);
            switch (switchValue){
                case "UntilStmt":
                case "ForEachStmt":
                case "IfStmt":
                case "RepeatStmt":
                case "ListDcl":
                    break;
                default: Emit("; \n");
            }
        }
        Emit(CreateIndentation() + "} \n");
        indentation--;
        return null;
    }

    public Object Visit(ListIndexExpr block) {
        Emit(block.GetId().ID + "["+ block.GetIndex() +"]");
        return null;
    }

    public Object Visit(StructCompSelectExpr node) {
        if (node.GetChildComp() != null){
        Emit(node.StructVarId + "." );
        visitValue.Visit(node.GetChildComp().GetValue(), node.GetChildComp());}
        else
            Emit(node.StructVarId + "." + node.ComponentId);
        return null;
    }

    /* Adds an Arduino C instruction to a list */
    public void Emit (String instruction)
    {
        instructions.add(instruction);

        //Reporter.Log("Emitting: " + instruction);
    }

    /* Writes all Arduino C instructions to a file */
    public void ToFile ()
    {
        try{
            PrintWriter writer = new PrintWriter("Arduino-C-Program.txt", "UTF-8");
            if (ListUsed)
            writer.print(InputTester.readFile("src/CodeGeneration/StandardLibrary.txt"));

            for(String s : instructions)
            {
                writer.print(s);
            }
            writer.close();
        } catch (IOException e) {
            // do something
        }
    }

    public void GenerateIdentifier(){
        do {
            currentIdentifier += "i";
        } while (SM.FindSymbol(currentIdentifier) != null);
        SM.AddSymbol(currentIdentifier);
    }
    public static void main(String args[]) throws IOException {
        Scanner sc = new Scanner(InputTester.readFile("src/CodeGeneration/ArduinoTestProgram.txt"));
        Parser parser = new Parser(sc);
        AST programTree = parser.ParseProgram();
        SemanticAnalyzer sm = new SemanticAnalyzer();
        sm.BeginSemanticAnalysis(programTree);
        CodeGenerator c = new CodeGenerator(programTree, sm);
        c.ToFile();
    }
}