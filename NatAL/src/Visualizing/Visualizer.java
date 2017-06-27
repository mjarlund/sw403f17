package Visualizing;

import DataStructures.AST.AST;
import Semantics.Scope.SemanticAnalyzer;
import Syntax.Parser.Parser;
import Syntax.Scanner.Scanner;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

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
