package p4test.SyntaxAnalysis;

import p4test.AbstractSyntaxTree.AST;
import p4test.AbstractSyntaxTree.Dcl.FuncDcl;
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
        BuildVarDCL, Combine, BuildFuncDcl
    }

    private SemanticActions actions;
    private Stack<RuleType> semanticStack;
    private Stack<AST> astStack;
    private Queue<Token> terminals;


    public ASTFactory(Stack<RuleType> semtanticStack, Queue<Token> terminals)
    {
        this.semanticStack = semtanticStack;
        this.astStack = new Stack<AST>();
        this.terminals = terminals;
    }

    public void CreateAbstractTree()
    {
        switch (actions)
        {
            case BuildVarDCL:
                CreateDclTree();
            case BuildFuncDcl:
                CreateDclTree();
            case Combine:
                CombineTrees();

        }
    }
    private void CombineTrees()
    {
        AST tree = astStack.pop();
        astStack.peek().MakeSiblings(tree);
    }
    private void CreateFuncDclTree()
    {

    }
    private void CreateDclTree()
    {
        Token type = terminals.remove();
        Token id = terminals.remove();
        Token lookAhead = terminals.isEmpty() ? null : terminals.peek();
        Types primitiv = GetType(type);
        AST dcl = null;
        if(lookAhead == null)
        {
            dcl = new VarDcl(primitiv, id.Value);
        }
        else
        {
            switch (lookAhead.Type)
            {
                case KEYWORD:
                    dcl = new VarDcl(primitiv, id.Value);
                case SEPARATOR:
                    dcl = new FuncDcl(primitiv, id.Value);
                default:
                    dcl = new VarDcl(primitiv, id.Value);
            }
        }
        astStack.push(dcl);
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
