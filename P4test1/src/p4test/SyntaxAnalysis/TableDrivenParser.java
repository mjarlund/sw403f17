package p4test.SyntaxAnalysis;

import p4test.AbstractSyntaxTree.AST;
import p4test.Token;

import java.util.*;

/**
 * Created by mysjkin on 3/6/17.
 */
public class TableDrivenParser
{
    private ParsingTable table;
    private Scanner scanner;
    private Stack<String> parseStack; /* RHS symbols for productions and terminals */
    private Token currentToken;

    public TableDrivenParser (Scanner input)
    {
        this.scanner = input;
        this.table = new ParsingTable();

        currentToken = input.NextToken();
    }

    /* Parses the input program and returns the AST for the program */
    public AST ParseProgram ()
    {
        parseStack = new Stack<String>();
        Stack<Token> terminalStack = new Stack<Token>();
        ASTFactory ASTFactory = new ASTFactory(terminalStack);
        ASTFactory.initFactory();

        ApplyProduction("Program");

        while (parseStack.size() > 0)
        {
            //System.out.println(parseStack);
            String parseTop = parseStack.pop();

            /* If the next RHS symbol is a terminal
             * try to match the symbol with the current token */
            if (table.IsTerminal(parseTop))
            {
                // Hvis dette slettes, så virker skidtet ikke. Wat 2 do
                if (parseTop.equals("EPSILON"))
                {
                    // Do nothing??
                }
                // Terminal might be a semantic actions
                else if (ASTFactory.SemanticAction.get(parseTop) != null)
                {
                    ASTFactory.CreateAbstractTree(parseTop);
                }
                else
                {
                    // Terminal stack is used when processing semantic actions
                    terminalStack.push(currentToken);
                    Match(parseTop, currentToken);
                }
            }
            else
            {
                ApplyProduction(parseTop);
            }
        }

        return ASTFactory.program;
    }

    // Applies the production
    private void ApplyProduction (String value)
    {
        Production production = table.GetPrediction(value, currentToken);

        // If there are no productions, thrown an error.
        if (production == null)
        {
            if (table.IsEpsilon(value))
                parseStack.push("EPSILON");
            else
                throw new Error("No productions available for " + value);
        }
        else
        {
            String[] RHSSymbols = production.Right;

            /* Pushes the input list of productions onto the
            * parseStack in reverse order, so that the first
            * production rule in the list is the first one popped */
            int iterations = RHSSymbols.length;
            for (int i = iterations - 1; i >= 0; i--) {
                parseStack.push(RHSSymbols[i]);
            }
        }
    }

    /* If the given val and the string version of token
     * are identical, retrieve the next token in the scanner */
    private void Match (String val, Token token)
    {
        String value = TypeConverter.TypeToTerminal(token);
        value = value != null ? value : token.Value;

        if (val.equals(value))
        {
            System.out.println("Matched " + value);
            Consume();
        }
        else
            throw new Error("Got " + value + " expected " + val);
    }

    /* Sets current token to the next token found by the scanner */
    private void Consume ()
    {
        currentToken = scanner.NextToken();
    }
}