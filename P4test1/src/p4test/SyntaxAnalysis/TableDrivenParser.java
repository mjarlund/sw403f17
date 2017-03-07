package p4test.SyntaxAnalysis;

import p4test.AbstractSyntaxTree.AST;
import p4test.AbstractSyntaxTree.Dcl.VarDcl;
import p4test.AbstractSyntaxTree.Types;
import p4test.DefaultHashMap;
import p4test.Token;
import p4test.TokenType;

import java.util.*;

/**
 * Created by mysjkin on 3/6/17.
 */
public class TableDrivenParser
{
    // Can be optimized to use integers instead of strings
    private Stack<String> ParseStack;

    private ProductionTable table;
    private Scanner input;
    private Stack<String> parseStack;
    private Stack<String> semanticStack;
    private Token CurrentToken;
    private ASTFactory AstFactory;

    private List<String> terminals;
    private List<String> semanticActions;

    public TableDrivenParser(Scanner input)
    {
        this.input = input;
        AstFactory = new ASTFactory();
        terminals = new ArrayList<>();
        // terminal values in CFG
        terminals.add("Type"); terminals.add("Identifier");
        terminals.add("EOL");

        semanticActions = new ArrayList<>();
        semanticActions.add("DclStatement");

        CurrentToken = input.nextToken();
        table = new ProductionTable();
        table.initTable();

    }

    public AST ParseProgram()
    {
        parseStack = new Stack<String>();
        semanticStack = new Stack<String>();
        Apply(table.GetProductions("Program", null));
        boolean accepted = false;
        AST programTree = new AST();

        while (!accepted)
        {
            if (semanticActions.contains(parseStack.peek()))
            {
                semanticStack.push(parseStack.peek());
            }
            if (terminals.contains(parseStack.peek()) || CurrentToken.Type.equals(TokenType.IDENTIFIER))
            {
                semanticStack.add(parseStack.peek());

                Match(parseStack.peek(), CurrentToken);
                if (parseStack.size() == 0 && CurrentToken.Type.equals(TokenType.EOF))
                {
                    accepted = true;
                }
                parseStack.pop();
            }
            else
            {
                if(parseStack.peek() != null && parseStack.peek().equals("EPSILON"))
                {
                    parseStack.pop();
                    if(parseStack.size() == 0 && !CurrentToken.Type.equals(TokenType.EOF))
                        throw new Error("lel");
                    else if (parseStack.size() == 0)
                    {
                        accepted = true;
                        break;
                    }
                }
                else
                {
                    ArrayList<String> productions = table.GetProductions(parseStack.peek(), CurrentToken.Type);
                    if (productions == null)
                        throw new Error("lel");
                    else
                    {
                        parseStack.pop();
                        Apply(productions);
                    }
                }
            }
        }
        System.out.println(semanticStack);
        return programTree;
    }

    private void Apply(ArrayList<String> productionRules)
    {
        int iterations = productionRules.size();
        for(int i = iterations-1; i>=0; i--)
        {
            parseStack.push(productionRules.get(i));
        }
    }
    private void Match(TokenType type, Token token)
    {
        if(type.equals(token.Type))
            Consume();
        else
            throw new Error("lel");
    }
    private void Match(String val, Token token)
    {
        String value = GetMatchVal(token);
        if(val.equals(value))
        {
            System.out.println("matched " + value);
            Consume();
        }
        else
            throw new Error("Got " + value + " expected " + val);
    }
    private String GetMatchVal(Token token)
    {
        switch (token.Value)
        {
            case "number":
                return "Type";
            case "\\n":
                return "EOL";
        }
        switch (token.Type)
        {
            case IDENTIFIER:
                return "Identifier";
        }
        return "sentinel";
    }
    private void Consume()
    {
        CurrentToken = input.nextToken();
    }
}