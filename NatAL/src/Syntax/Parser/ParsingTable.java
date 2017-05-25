package Syntax.Parser;

import Syntax.Grammar.Production;
import Syntax.Tokens.Token;
import Syntax.Tokens.TokenType;
import Syntax.Grammar.Grammar;
import Utilities.Reporter;
import Utilities.TypeConverter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class ParsingTable {
    public Grammar Grammar;
    public Production ProductionRules[];

    public ParsingTable() {
        try {
            this.Grammar = Grammar.FromFile("src/Syntax/Grammar/CFG/CFG");
        } catch (IOException e) {
            e.printStackTrace();
        }

        InitProductionRules();
    }
    public ParsingTable(String path){
        try {
            this.Grammar = Grammar.FromFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InitProductionRules();
    }

    public boolean IsTerminal(String s) {
        return (Grammar.Terminals.contains(s));
    }

    public boolean IsEpsilon(String s)
    {
        return Grammar.Epsilon.get(s);
    }

    private String GetTerminalSymbol(Token token) {

        for (String s : Grammar.Terminals) {
            if(token.Type.name().equals(s)) return s;
        }

        return token.Value;
    }

    public Production GetPrediction(String nonTerminal, Token token)
    {
        String terminal = TypeConverter.TypeToTerminal(token);
        terminal = terminal != null ? terminal : GetTerminalSymbol(token);
        return GetPrediction(nonTerminal, terminal);
    }

    public Production GetPrediction(String nonterminal, String terminal) {
        int i = 0;

        for(Production p : ProductionRules) {
            if(p.Left.equals(nonterminal)) {
                for(String s : p.Right) {
                    if(s.equals(terminal)) return Grammar.Productions[i]; 
                }
            }
            i++;
        }

        return null;
    }

    public void InitProductionRules() {
        int nbProductions = Grammar.Productions.length;
        ProductionRules = new Production[nbProductions];
        for(int i = 0; i < nbProductions; i++) {
            String alpha = Grammar.Productions[i].Right[0];
            String left = Grammar.Productions[i].Left;

            Set<String> t = Grammar.First(alpha, true);
            Set<String> predictSet = new HashSet();

            predictSet.addAll(t);

            if(Grammar.Epsilon.get(alpha).equals(true)) {
                t = Grammar.Follow(left);
                predictSet.addAll(t);
            }

            ProductionRules[i] = new Production(Grammar.Productions[i].Left, predictSet.toArray(new String[0]));
        }
    }
}