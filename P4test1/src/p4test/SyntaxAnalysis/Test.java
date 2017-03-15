package p4test.SyntaxAnalysis;


import p4test.AbstractSyntaxTree.AST;

/**
 * Created by mysjkin on 3/6/17.
 */
public class Test
{
    public static void main(String args[])
    {
        String code = "void func1() if(a or b) number a is 2 else number b is 3 end else if end func1";
        Scanner sc = new Scanner(code);
        /*while(!sc.IsEOF())
            System.out.println(sc.nextToken());*/
        TableDrivenParser parser = new TableDrivenParser(sc);
        AST programTree = parser.ParseProgram();
        System.out.println("............");
        printtree(programTree);
    }

    private static void printtree(AST tree)
    {
        System.out.println(tree);
        System.out.println(tree.children);
        for (AST c : tree.children)
            printtree(c);
    }
}
