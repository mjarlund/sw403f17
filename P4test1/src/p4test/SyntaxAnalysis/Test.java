package p4test.SyntaxAnalysis;


import p4test.AbstractSyntaxTree.AST;
import p4test.AbstractSyntaxTree.Visualizing.Visualizer;

/**
 * Created by mysjkin on 3/6/17.
 */
public class Test
{
    public static void main(String args[])
    {
        Visualizer v = new Visualizer();
        v.Show();
    }

    public static void printtree(AST tree)
    {
        System.out.println(tree);
        System.out.println(tree.children);
        for (AST c : tree.children)
            printtree(c);
    }
}
