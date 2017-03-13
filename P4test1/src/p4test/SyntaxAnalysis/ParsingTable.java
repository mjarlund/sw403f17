package p4test.SyntaxAnalysis;

import p4test.Token;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mikkel on 3/11/2017.
 */
public class ParsingTable {
    public Grammar Grammar;
    public Production ProductionRules[];

    public ParsingTable() {
        try {
            this.Grammar = Grammar.fromFile("src/CFG");
        } catch (IOException e) {
            e.printStackTrace();
        }

        init();
    }

    private String GetTerminalSymbol(Token token) {

        for (String s : Grammar.Terminals) {
            if(token.Type.name().equals(s)) return s;
        }

        return token.Value;
    }

    public Production GetPrediction(String nonTerminal, Token token) {
        String terminal = GetTerminalSymbol(token);
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

    public void initProductionRules() {
        int nbProductions = Grammar.Productions.length;
        ProductionRules = new Production[nbProductions];
        for(int i = 0; i < nbProductions; i++) {
            String alpha = Grammar.Productions[i].Right[0];
            String left = Grammar.Productions[i].Left;

            Set<String> t = Grammar.First(alpha, false);
            Set<String> predictSet = new HashSet();

            predictSet.addAll(t);

            if(Grammar.Epsilon.get(alpha).equals(true)) {
                t = Grammar.Follow(left);
                predictSet.addAll(t);
            }

            ProductionRules[i] = new Production(Grammar.Productions[i].Left, predictSet.toArray(new String[0]));
        }
    }

    public void init() {
        initProductionRules();
    }

    public static void main(String[] args) throws IOException {
        ParsingTable p = new ParsingTable();

        System.out.println(p.GetPrediction("Statement", "until"));


        /*
        for(String nt : p.Grammar.NonTerminals) {
            System.out.println("<<" + nt + ">>");

            System.out.print("First -> ");
            Set<String> first = p.Grammar.First(nt, true);
            for(String i : first) System.out.print(i + " ");

            System.out.print("\nFollow -> ");
            Set<String> follow = p.Grammar.Follow(nt);
            for(String i : follow) System.out.print(i + " ");

            System.out.println("\n\n");
        }

        for(Production production : p.ProductionRules) {
            System.out.println(production.toString());
        }
        */
    }
}
