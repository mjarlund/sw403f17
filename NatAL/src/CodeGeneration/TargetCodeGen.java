package CodeGeneration;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Statements.*;
import Semantics.Scope.SemanticAnalyzer;
import Utilities.IVisitor;
import Utilities.VisitorDriver;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by mysjkin on 4/28/17.
 */
public class TargetCodeGen implements IVisitor {

    /*
     * How to do?
     * ¯\_(ツ)_/¯ - everyone
     */
    private AST program;
    private SemanticAnalyzer symbols;
    private ArrayList<String> instructions;
    private ArrayList<String> globalInstructions;
    private VisitorDriver visitValue;
    public TargetCodeGen(AST program, SemanticAnalyzer symbols){
        visitValue = new VisitorDriver(this);
        this.program = program;
        this.symbols = symbols;
        instructions = new ArrayList<>();
        globalInstructions = new ArrayList<>();
        globalInstructions.add(".include \"m328Pdef.inc\"");
    }
    public void ToFile(String name){
        try{
            PrintWriter writer = new PrintWriter(name, "UTF-8");
            for(String s : globalInstructions)
            {
                writer.print(s);
            }
            for(String s : instructions)
            {
                writer.print(s);
            }
            writer.close();
        } catch (IOException e) {
            // do something
        }
    }
    private void VisitChildren(){
        for(AST a : program.children){
            visitValue.Visit(a.GetValue(),a);
        }
    }

    @Override
    public Object Visit(IOStmt stmt)
    {
        /* digital write high to a */
        // find value of a
        // make digital pin a output pin by making fx 1 in 0b00100000 is output pin
        // the value 0b00100000 is assigned to a register
        // that register is assigned to the DDR appropriate for the port relevant for the pin
        // make new binary number 0b00100000 fx if P 5 should be high and rest low or 0b00000000 if low is written to all pins
        return null;
    }

    @Override
    public Object Visit(IOExpr expr) {
        return null;
    }

    @Override
    public Object Visit(FuncDcl node) {
        return null;
    }

    @Override
    public Object Visit(BlockStmt block) {
        return null;
    }
    @Override
    public Object Visit(AssignStmt stmt) {
        return null;
    }

    @Override
    public Object Visit(ValExpr lit) {
        return null;
    }

    @Override
    public Object Visit(VarDcl node) {
        return null;
    }

    @Override
    public Object Visit(IdExpr node) {
        return null;
    }

    @Override
    public Object Visit(FParamsDcl node) {
        return null;
    }



    /* <<<<< NOT YET DEFINED FOR TARGET CODE GENERATION >>>>> */

    @Override
    public Object Visit(StructVarDcl dcl) {
        return null;
    }

    @Override
    public Object Visit(IfStmt stmt) {
        return null;
    }

    @Override
    public Object Visit(ElseStmt stmt) {
        return null;
    }

    @Override
    public Object Visit(UntilStmt stmt) {
        return null;
    }

    @Override
    public Object Visit(ForEachStmt stmt) {
        return null;
    }

    @Override
    public Object Visit(ReturnStmt stmt) {
        return null;
    }

    @Override
    public Object Visit(FuncCallExpr expr) {
        return null;
    }

    @Override
    public Object Visit(BinaryOPExpr expr) {
        return null;
    }

    @Override
    public Object Visit(BoolExpr expr) {
        return null;
    }


    @Override
    public Object Visit(StructDcl node) {
        return null;
    }


    @Override
    public Object Visit(ListIndexExpr block) {
        return null;
    }

    @Override
    public Object Visit(StructCompSelectExpr node) {
        return null;
    }

    @Override
    public Object Visit(UnaryExpr expr) {
        return null;
    }

    @Override
    public Object Visit(ListDcl node) {
        return null;
    }

    @Override
    public Object Visit(ArgsExpr node) {
        return null;
    }
}
