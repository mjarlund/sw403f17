package Visualizing;

import CodeGeneration.CodeGenerator;
import DataStructures.AST.AST;
import Semantics.Scope.SemanticAnalyzer;
import Syntax.Parser.Parser;
import Syntax.Scanner.Scanner;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Created by Anders Brams on 3/15/2017.
 */
public class Visualizer extends PApplet {

    Scanner sc;
    VisualNode visTree;
    Parser parser;

    public void settings(){
        size(1920,1080); //Because for some reason this has to be here
    }

    public void setup(){
        /* Visualizer settings */
        textSize(12);
        /* Scanner and parser */
        String code =   "void main()\n" +
                        "list of boolean a is (true, false, true, false)\n" +
                        "number sum\n" +
                        "foreach(number num in a)\n" +
                        "sum is sum + num\n" +
                        "end foreach\n" +
                        "end main\n";
        String code1 = "text func1()\n" +
                       "pin a is 2\n" +
                        "a is digital read from a\n" +
                         "analog write 2 to a\n" +
                        "boolean b is true and false\n" +
                        "return \"a\"\n" +
                        "end func1\n" +
                        "void func2()\n" +
                         "text b is func1()\n end func2\n";
        sc = new Scanner(code);
        parser = new Parser(sc);
        AST programTree = parser.ParseProgram();

        SemanticAnalyzer sm = new SemanticAnalyzer();
        sm.VisitChildren(programTree);
        System.out.println("No variables out of scope!");

        CodeGenerator codeGen = new CodeGenerator(programTree);

        BuildVisualTree(programTree);
        background(255);
        if (visTree != null){
            visTree.Show(this);
        }
    }

    /* Runs once every frame */
    public void draw(){

    }

    /* Builds the visual tree from an AST */
    public void BuildVisualTree(AST tree){
        visTree = new VisualNode(tree, new PVector(900, 50)); //Root in the top-middle of the screen
        visTree.defaultXOffset = 600;
        ArrayList<VisualNode> visTreeList = new ArrayList<VisualNode>();
        visTree.AssignPositionsToChildren(visTreeList);
    }

    /* Called from Test.Main(), initializes the Processing Unit */
    public void Show(){
        PApplet.main(Visualizer.class.getName());
    }
}
