package p4test.SyntaxAnalysis;

import p4test.DefaultHashMap;
import p4test.Token;
import p4test.TokenType;

import java.util.ArrayList;

/**
 * Created by mysjkin on 3/6/17.
 */
public class ProductionTable
{
    // TODO: Should be optimized because there are many operations on strings

    private Scanner input;

    public ArrayList<String> GetProductions(String NonTerminal, TokenType type)
    {
        int index = -1;
        switch (NonTerminal)
        {
            case "Program":
                index = program;
                break;
            case "Statement":
                index = Statement;
                break;
            case "Statements":
                index = Statements;
                break;
            case "DclStatement":
                index = DclStatement;
                break;
            case "DclStatementPrime":
                index = DclStatementPrime;
                break;
        }
        switch (index)
        {
            case 0:
                return rules[program][0];
            case 1:
                return rules[Statement][0];
            case 2:
                if(IsStatement(type))
                    return rules[Statements][0];
                else
                    return rules[Statements][1];
            case 3:
                if(type.equals(TokenType.KEYWORD))
                    return rules[DclStatement][0];
                else
                    return null;
            case 4:
                return rules[DclStatementPrime][0];
        }
        return null;
    }
    private boolean IsStatement(TokenType type)
    {
        if(type.equals(TokenType.KEYWORD))
            return true;
        else
            return false;
    }
    public void initTable()
    {
        ArrayList<String> programRules = new ArrayList<String>();
        programRules.add("Statement"); programRules.add("Statements");
        rules[program][0] = programRules;

        ArrayList<String> StatementRules = new ArrayList<String>();
        StatementRules.add("DclStatement"); StatementRules.add("EOL");
        rules[Statement][0] = StatementRules;

        ArrayList<String> StatementsRules = new ArrayList<String>();
        StatementsRules.add("Statement"); StatementsRules.add("Statements");

        ArrayList<String> StatementsRules2 = new ArrayList<String>();
        StatementsRules2.add("EPSILON");
        rules[Statements][0] = StatementsRules;
        rules[Statements][1] = StatementsRules2;

        ArrayList<String> DclStatementRules = new ArrayList<String>();
        DclStatementRules.add("Type"); DclStatementRules.add("Identifier");
        DclStatementRules.add("DclStatementPrime");
        rules[DclStatement][0] = DclStatementRules;

        ArrayList<String> DclStatementPrimeRules = new ArrayList<String>();
        DclStatementPrimeRules.add("EPSILON");
        rules[DclStatementPrime][0] = DclStatementPrimeRules;
    }

    public DefaultHashMap<String, String> Keywords;

    private int program = 0;
    private int Statement = 1;
    private int Statements = 2;
    private int DclStatement = 3;
    private int DclStatementPrime = 4;

    private ArrayList<String> rules[][] = new ArrayList[5][3];

}
