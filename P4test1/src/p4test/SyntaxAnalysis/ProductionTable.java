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


    private int NOProductionRulesMax = 7;

    public ArrayList<String> GetProductions(String NonTerminal, Token token)
    {
        RuleType rule = null;
        int index = -1;

        for(RuleType val : RuleType.values()) {
            if(NonTerminal == val.name()) {
                rule = val;
                index = val.ordinal();
                break;
            }
        }

        if(rule == null) return null;

        int subIndex = -1;

        switch(rule.Type) {
            case Program:

            case Statement:
                if()
            case Statements:
                if(!IsStatement(type))
                    subIndex = 1;
                break;
            case DeclarationStatement:
                if(!type.equals(TokenType.KEYWORD))
                    return null;
                break;

        }

        if(subIndex == -1) return null;

        return rules[index][subIndex];
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
        // Rules for Program
        rules[RuleType.Program.ordinal()][0] = new ArrayList<String>() {{
            add(RuleType.Statement.name());
            add(RuleType.Statements.name());
        }};

        // Rules for Program
        rules[RuleType.Statement.ordinal()][0] = new ArrayList<String>() {{
            add(RuleType.DeclarationStatement.name());
            add(RuleType.EOL.name());
        }};

        rules[RuleType.Statement.ordinal()][1] = new ArrayList<String>() {{
            add(RuleType.ExpressionStatement.name());
            add(RuleType.EOL.name());
        }};

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

    private ArrayList<String> rules[][] = new ArrayList[RuleType.values().length][NOProductionRulesMax];

}
