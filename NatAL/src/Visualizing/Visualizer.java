package Visualizing;

import DataStructures.AST.AST;
import Semantics.Scope.SemanticAnalyzer;
import Syntax.Parser.Parser;
import Syntax.Scanner.Scanner;
import Test.InputTester;
import processing.core.PApplet;
import processing.core.PVector;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Anders Brams on 3/15/2017.
 */
public class Visualizer extends PApplet {

    Scanner sc;
    VisualNode visTree;
    Parser parser;

    public void settings(){
        size(1280,720); //Because for some reason this has to be here
    }

    public void setup(){
        /* Visualizer settings */
        textSize(12);
        /* Scanner and parser */
        String code =   "structure testingStruct\n" +
        				"number a\n" +
        				"number b\n" +
        				"number c\n" +
        				"end test\n\n" +

        				"structure outsideStruct\n" +
        				"number d\n" +
        				"testingStruct test1\n" +
        				"end outside\n\n" +

        				"void main()\n" +
        				"outsideStruct hej\n" +
        				"hej.d is 5\n" +
        				"hej.test1.b is 8\n" +
        				"hej.test1.c is hej.test1.a + hej.test1.b\n" +
        				"end main" ;

        String code1 = "text func1()\n" +
                       "pin a is 2\n" +
                        "a is digital read from a\n" +
                         "analog write 2 to a\n" +
                        "boolean b is true and false\n" +
                        "return \"a\"\n" +
                        "end func1\n" +
                        "void func2()\n" +
                         "text b is func1()\n end func2\n";
        try {
			sc = new Scanner(InputTester.readFile("src/Test/TestPrograms/semantics/4LayeredStructsTest"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        parser = new Parser(sc);
        AST programTree = parser.ParseProgram();

        SemanticAnalyzer sm = new SemanticAnalyzer();
        //sm.VisitChildren(programTree);

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
        visTree = new VisualNode(tree, new PVector(100, 50)); //Root in the top-middle of the screen
        visTree.defaultXOffset = 600;
        ArrayList<VisualNode> visTreeList = new ArrayList<VisualNode>();
        visTree.AssignPositionsToChildren(visTreeList);
    }

    /* Called from Test.Main(), initializes the Processing Unit */
    public void Show(){
        PApplet.main(Visualizer.class.getName());
    }
}
