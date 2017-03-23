package Visualizing;

import DataStructures.AST.AST;
import Semantics.Scope.ScopeManager;
import Syntax.Scanner.Scanner;
import Syntax.Parser.Parser;
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
        String code = "void func1()\n" +
                        "number a is -2\n" +
                        "number b is a\n" +
                        "if (a equals b)\n " +
                            "b is a+1\n" +
                            "end if\n"+
                        "end func1 \n " +
                      "void func2(number a, number b)\n"+
                        "a is a + b\n"+
                        "end func2\n "+
                      "number func(character x, character y)\n"+
                        /*"func1()\n"+*/
                        "func2(x,y)\n"+
                        "if (x above x)\n"+
                            "func2(y,x)\n"+
                            "end if \n"+
                        " return 2 \n"+
                        "end func \n " ;
        String code1 = "number func1()\n" +
                       "number a is 2\n" +
                        "boolean b is true and false\n" +
                        "return 2\n" +
                        "end func1\n" +
                        "void func2()\n" +
                         "number b is func1()\n end func2\n";
        sc = new Scanner(code);
        parser = new Parser(sc);
        AST programTree = parser.ParseProgram();

        ScopeManager sm = new ScopeManager();
        sm.OpenScope(); //Global
        sm.VisitChildren(programTree);
        System.out.println("No variables out of scope!");
        BuildVisualTree(programTree);
    }

    /* Runs once every frame */
    public void draw(){
        background(255);
        if (visTree != null){
            visTree.Show(this);
        }
    }

    /* Builds the visual tree from an AST */
    public void BuildVisualTree(AST tree){
        visTree = new VisualNode(tree, new PVector(900, 50)); //Root in the top-middle of the screen
        visTree.defaultXOffset = 450;
        ArrayList<VisualNode> visTreeList = new ArrayList<VisualNode>();
        visTree.AssignPositionsToChildren(visTreeList);
    }

    /* Called from Test.Main(), initializes the Processing Unit */
    public void Show(){
        PApplet.main(Visualizer.class.getName());
    }
}
