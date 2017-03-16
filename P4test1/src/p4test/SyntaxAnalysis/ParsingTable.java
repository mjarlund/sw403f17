package p4test.SyntaxAnalysis;

import p4test.Token;
import p4test.TokenType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
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
            this.Grammar = Grammar.fromFile(Paths.get(getClass().getResource("/"+"CFG").toURI()).toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        init();
    }

    public boolean IsTerminal(String s) {
        if(Grammar.Terminals.contains(s)) return true;
        return false;
    }
    // TODO: THIS IS BADNESS SOMEONE PLZ SEND HELP
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

    public void initProductionRules() {
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

    public void init() {
        initProductionRules();
    }

    public static void main(String[] args) throws IOException {
        ParsingTable p = new ParsingTable();
        System.out.println(p.GetPrediction("DeclarationStatementPrime", "EPSILON"));
        //for(Production product : p.ProductionRules) System.out.println(product);
        System.out.println(p.GetPrediction("Expression", "IntegerLiteral"));
        System.out.println(p.GetPrediction("Expression", new Token("2", TokenType.INTEGER_LITERAL)));


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
