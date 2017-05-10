package Visualizing;

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
        size(1280,720); //Because for some reason this has to be here
    }

    public void setup(){
        /* Visualizer settings */
        textSize(12);
        /* Scanner and parser */
        String code =   "structure layer1\n" +
                "number a \n" +
                "number b\n" +
                "number c\n" +
                "end layer1\n" +
                "\n" +
                "structure layer2\n" +
                "number d \n" +
                "layer1 instanceOfLayer1\n" +
                "end layer2\n" +
                "\n" +
                "structure layer3\n" +
                "number e\n" +
                "layer2 instanceOfLayer2\n" +
                "end layer3\n" +
                "\n" +
                "structure layer4\n" +
                "number f\n" +
                "layer3 instanceOfLayer3\n" +
                "end layer4\n" +
                "\n" +
                "void main()\n" +
                "layer4 instanceOfLayer4\n" +
                "instanceOfLayer4.instanceOfLayer3.instanceOfLayer2.instanceOfLayer1.a is 5\n" +
                "end main\n" ;

        String code1 = "text func1()\n" +
                       "pin a is 2\n" +
                        "a is digital read from a\n" +
                        "fraction whatIsThis is 2f/0f\n" +
                        "number asd is 5\n" +
                        "repeat (asd) \n" +
                        "  fraction n is 2\n" +
                        " end repeat\n" +
                        "return \"LEL\" \n" +
                        "end func1\n";
        String code2 = "structure testStruct\n" +
                "number a\n" +
                "end testStruct\n" +
                "\n" +
                "void test(number b, list of number a)\n" +
                "b is 5\n" +
                "end test\n" +
                "\n" +
                "void main()\n" +
                "list of number l is (1,2,3)\n" +
                "test(5, l)\n" +
                "end main\n";
        sc = new Scanner(code2);
        parser = new Parser(sc);
        AST programTree = parser.ParseProgram();

        SemanticAnalyzer sm = new SemanticAnalyzer();
        sm.VisitChildren(programTree);

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
        visTree = new VisualNode(tree, new PVector(400, 50)); //Root in the top-middle of the screen
        visTree.defaultXOffset = 200;
        ArrayList<VisualNode> visTreeList = new ArrayList<VisualNode>();
        visTree.AssignPositionsToChildren(visTreeList);
    }

    /* Called from Test.Main(), initializes the Processing Unit */
    public void Show(){
        PApplet.main(Visualizer.class.getName());
    }
}
