package p4test.SyntaxAnalysis;

import p4test.AbstractSyntaxTree.AST;
import p4test.AbstractSyntaxTree.Dcl.VarDcl;
import p4test.AbstractSyntaxTree.Types;
import p4test.Token;

import java.util.Queue;

/**
 * Created by mysjkin on 3/7/17.
 */
public class ASTFactory
{
    public ASTFactory()
    {
    }
    public AST GetAbstractTree(Queue<Token> terminals)
    {
        switch (terminals.peek().Value)
        {
            case "number":
                terminals.remove();
                return GetDclAST(Types.INT,terminals);

        }
        return null;
    }
    private AST GetDclAST(Types type, Queue<Token> terminals)
    {
        switch (terminals.peek().Type)
        {
            case IDENTIFIER:
                String id = terminals.peek().Value;
                terminals.remove();
                if(terminals.size() == 0)
                    return new VarDcl(type,id);
        }
        return null;
    }
}
