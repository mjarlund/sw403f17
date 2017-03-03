package p4test.AST;

import p4test.AST.Nodes.datatypes.Int;
import p4test.AST.Nodes.expr.BinaryOp;
import p4test.AST.Nodes.expr.Expr;
import p4test.Scanner;
import p4test.Token;
import p4test.TokenType;

/**
 * Created by mysjkin on 3/3/17.
 */
public class test
{
    public static void main(String args[])
    {
        try
        {
            String code = "number a is 2 + 2\n";
            Scanner sc = new Scanner(code);
            /*while(!sc.IsEOF())
            {
                System.out.println(sc.nextToken());
            }*/
            Token plus = new Token("+", TokenType.OPERATOR);
            Token num1 = new Token("2", TokenType.INTEGER_LITERAL);
            Token num2 = new Token("2", TokenType.INTEGER_LITERAL);
            BinaryOp root = new BinaryOp(new Int(num1), plus, new Int(num2));
            System.out.println(root.treeToString());
        }
        catch (Exception e)
        {}
    }
}


