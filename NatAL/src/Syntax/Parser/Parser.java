package Syntax.Parser;

import DataStructures.AST.AST;
import DataStructures.AST.ASTFactory;
import Exceptions.MissingProductionsException;
import Exceptions.UnexpectedTokenException;
import Syntax.Scanner.Scanner;
import Syntax.Tokens.Token;
import Syntax.Grammar.Production;

import Utilities.Reporter;
import Utilities.TypeConverter;
import java.util.Stack;

public class Parser
{
    private ParsingTable table;
    private Scanner scanner;
    private Stack<String> parseStack; /* RHS symbols for productions and terminals */
    private Token currentToken;

    public Parser (Scanner input)
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
        ASTFactory.InitFactory();

        ApplyProduction("Program");

        while (parseStack.size() > 0)
        {
            String parseTop = parseStack.pop();
            /* If the next RHS symbol is a terminal
             * try to match the symbol with the current token */
            if (table.IsTerminal(parseTop))
            {
                // Hvis dette slettes, så virker skidtet ikke. Wat 2 do
                if (parseTop.equals("EPSILON"))
                {
                }
                // Terminal might be a semantic actions
                else if (ASTFactory.SemanticAction.get(parseTop) != null)
                {
                    ASTFactory.CreateAbstractTree(parseTop, scanner.GetLineNumber()-1);
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
            {
                Reporter.Error(new MissingProductionsException(value, currentToken));
            }

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
            if(value.equals("EOL"))
                scanner.IncreaseLineNumber(1);
            Consume();
        }
        else
            throw new UnexpectedTokenException(token, val);
    }

    /* Sets current token to the next token found by the scanner */
    private void Consume ()
    {
        currentToken = scanner.NextToken();
    }
}
