package p4test.AbstractSyntaxTree.Visualizing;

import p4test.AbstractSyntaxTree.AST;
import p4test.SyntaxAnalysis.Scanner;
import p4test.SyntaxAnalysis.TableDrivenParser;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Created by Anders Brams on 3/15/2017.
 */
public class Visualizer extends PApplet {

    Scanner sc;
    VisualNode visTree;
    TableDrivenParser parser;

    public void settings(){
        size(1000,800); //Because for some reason this has to be here
    }

    public void setup(){
        /* Visualizer settings */
        textSize(18);

        /* Scanner and parser */
        String code = "void func1() number b is a if (a equals b) number b is a+1 end if end func1";
        sc = new p4test.SyntaxAnalysis.Scanner(code);
        parser = new TableDrivenParser(sc);
        AST programTree = parser.ParseProgram();
        BuildVisualTree(programTree);
    }

    /* Runs once every frame */
    public void draw(){
        background(255);
        if (visTree != null){
            visTree.Show(this);
        }
        AdjustPositions();
    }

    /* Builds the visual tree from an AST */
    public void BuildVisualTree(AST tree){
        visTree = new VisualNode(tree, new PVector(500, 50)); //Root in the top-middle of the screen
        visTree.AssignPositionsToChildren();
    }

    void AdjustPositions(){
        
    }

    /* Called from Test.Main(), initializes the Processing Unit */
    public void Show(){
        PApplet.main(Visualizer.class.getName());
    }
}
