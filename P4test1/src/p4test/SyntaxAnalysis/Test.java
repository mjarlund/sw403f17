package p4test.SyntaxAnalysis;


import p4test.AbstractSyntaxTree.AST;

/**
 * Created by mysjkin on 3/6/17.
 */
public class Test
{
    public static void main(String args[])
    {
        String code = "void func1() a is func(1,2,3) b is 2 number a is 2 end func1";
        Scanner sc = new Scanner(code);
        /*while(!sc.IsEOF())
            System.out.println(sc.nextToken());*/
        TableDrivenParser parser = new TableDrivenParser(sc);
        AST programTree = parser.ParseProgram();
        System.out.println("............");
        printtree(programTree);
    }

    public static void printtree(AST tree)
    {
        System.out.println(tree);
        System.out.println(tree.children);
        for (AST c : tree.children)
            printtree(c);
    }
}
