package p4test.AbstractSyntaxTree.Visualizing;

import p4test.AbstractSyntaxTree.AST;
import p4test.SyntaxAnalysis.Scanner;
import p4test.SyntaxAnalysis.TableDrivenParser;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Created by Anders Brams on 3/15/2017.
 */
public class Visualizer extends PApplet {

    VisualNode visTree;
    Scanner sc;
    TableDrivenParser parser;

    public void settings(){
        size(1000,800);
    }

    public void setup(){
        String code = "void func1() number b is a end func1";
        sc = new p4test.SyntaxAnalysis.Scanner(code);
        /*while(!sc.IsEOF())
            System.out.println(sc.nextToken());*/
        TableDrivenParser parser = new TableDrivenParser(sc);
        AST programTree = parser.ParseProgram();
        InitTree(programTree);
    }

    public void draw(){
        background(51);
        if (visTree != null){
            visTree.Show(this);
        }
    }

    public void InitTree(AST tree){
        visTree = new VisualNode(tree, new PVector(500, 50));
        visTree.AssignPositionsToChildren();
        System.out.println(visTree == null);
    }

    public void Show(){
        PApplet.main(Visualizer.class.getName());
    }
}
