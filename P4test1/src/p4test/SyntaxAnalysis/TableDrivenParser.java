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
        semanticActions.add("Some semantic action");

        CurrentToken = input.nextToken();
        table = new ProductionTable();
        table.initTable();

    }

    /* Parses the input program and returns the AST for the program */
    public AST ParseProgram()
    {
        parseStack = new Stack<String>(); /* RHS symbols for productions, and semantic actions. */
        semanticStack = new Stack<String>(); /* Semantic actions */
        Apply(table.GetProductions("Program", null)); /* Push RHS symbols for the productions of "Program". */
        boolean accepted = false;
        AST programTree = new AST();

        while (!accepted)
        {
            /* If the next RHS symbol is a terminal, or if the current token is an identifier,
             * try to match the symbol with the current token.
             * If the parseStack is empty and the current token is EOF, end the parsing. */
            if (terminals.contains(parseStack.peek()) || CurrentToken.Type.equals(TokenType.IDENTIFIER))
            {
                Match(parseStack.peek(), CurrentToken);
                if (parseStack.size() == 0 && CurrentToken.Type.equals(TokenType.EOF))
                {
                    accepted = true;
                }
                parseStack.pop();
            }
            else
            {
                /* If the next right hand side symbol is the empty string, pop it, and
                 * check for empty parseStack and EOF current token. End the parsing if
                 * both of these are true. */
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
                    /* Acquire the RHS symbols for the production of the next RHS symbol
                     * in the parseStack. If there are no productions, throw an error.
                     * Otherwise, push the productions' RHS symbols to the parseStack. */
                    ArrayList<String> RHSSymbols = table.GetProductions(parseStack.peek(), CurrentToken.Type);
                    if (RHSSymbols == null)
                        throw new Error("lel");
                    else
                    {
                        parseStack.pop();
                        Apply(RHSSymbols);
                    }
                }
            }

            /* If the next symbol in the parseStack is a semanticAction,
             * push this to the semanticStack. */
            if (semanticActions.contains(parseStack.peek()))
            {
                semanticStack.push(parseStack.peek());
            }
        }
        System.out.println(semanticStack);
        return programTree;
    }

    /* Pushes the input list of productions onto the
     * parseStack in reverse order, so that the first
      * production rule in the list is the first one popped */
    private void Apply(ArrayList<String> RHSSymbols)
    {
        int iterations = RHSSymbols.size();
        for(int i = iterations-1; i>=0; i--)
        {
            parseStack.push(RHSSymbols.get(i));
        }
    }

    /* If the two input tokens match by type, retrieve
     * the next token in the scanner */
    private void Match(TokenType type, Token token)
    {
        if(type.equals(token.Type))
            Consume();
        else
            throw new Error("lel");
    }

    /* If the given if the val and the string version of token
     * are identical, retrieve the next token in the scanner */
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

    /* Retrieves the string version of the given token
     * Based on the value of the token */
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

    /* Sets current token to the next token found by the scanner */
    private void Consume()
    {
        CurrentToken = input.nextToken();
    }
}