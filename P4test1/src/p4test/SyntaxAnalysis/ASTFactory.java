package p4test.SyntaxAnalysis;

import p4test.AbstractSyntaxTree.AST;
import p4test.AbstractSyntaxTree.Dcl.VarDcl;
import p4test.AbstractSyntaxTree.Types;
import p4test.Token;

import java.util.Queue;
import java.util.Stack;

/**
 * Created by mysjkin on 3/7/17.
 */
public class ASTFactory
{
    private enum SemanticActions
    {
        BuildDCL
    }

    private SemanticActions actions;
    private Stack<RuleType> semanticStack;
    private Queue<Token> terminals;


    public ASTFactory(Stack<RuleType> semtanticStack, Queue<Token> terminals)
    {
        this.semanticStack = semtanticStack;
        this.terminals = terminals;
    }

    public void CreateAbstractTree()
    {
        switch (actions)
        {
            case BuildDCL:
                CreateDclTree();
        }
    }
    public void CreateDclTree()
    {
        Token type = terminals.remove();
        Types primitiv = GetType(type);
        Token id = terminals.remove();

        VarDcl dcl = new VarDcl(primitiv, id.Value);
    }

    private Types GetType(Token token)
    {
        switch (token.Value)
        {
            case "number":
                return Types.INT;
        }
        return null;
    }
}
