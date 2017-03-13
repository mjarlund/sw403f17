package p4test.SyntaxAnalysis;


import p4test.AbstractSyntaxTree.AST;

/**
 * Created by mysjkin on 3/6/17.
 */
public class Test
{
    public static void main(String args[])
    {
        String code = "number b is a";
        Scanner sc = new Scanner(code);
        /*while(!sc.IsEOF())
            System.out.println(sc.nextToken());*/
        TableDrivenParser parser = new TableDrivenParser(sc);
        AST programTree = parser.ParseProgram();
        System.out.println("............");
        System.out.println(programTree);
    }
}
