package p4test.SyntaxAnalysis;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by Mikkel on 3/10/2017.
 */
public class Grammar {
    /**
     * Array of grammar productions.
     */
    public Production[] Productions;

    /**
     * Construct a grammar with n productions
     */
    public Grammar(int n) {
        Productions = new Production[n];
    }

    /**
     * Set of terminals.
     */
    public Set<String> Terminals;

    /**
     * Set of non-terminals.
     */
    public Set<String> NonTerminals;

    /**
     * Set of symbols. It's the union of terminals and non-terminals.
     */
    public Set<String> Symbols;

    /**
     The erasable symbols.
     */
    public Map Epsilon;

    /**
     * The initial production.
     */
    public String initialP;

    /**
     *  Construct grammar from file
     */
    public static Grammar fromFile(String path) throws IOException {
        List<String> lines = Files.readAllLines(new File(path).toPath());
        int n = lines.size(); // (== number of productions)
        Grammar cfg = new Grammar(n);

        for(int i = 0; i < n; i++) {
            String[] rule = lines.get(i).split("->", 2);
            String l = rule[0].trim();
            String[] r = rule[1].trim().split("\\s+");
            for(String s : r) {
                if(s.equals("$")) {
                    cfg.initialP = l;
                    break;
                }
            }
            cfg.Productions[i] = new Production(l, r);
        }

        cfg.init();
        return cfg;
    }

    private Set firstChild(String v) {
        Set s = new HashSet();

        for(Production p : Productions) {
            if(p.Left.equals(v)) {
                for(String symbol : p.Right) {
                    s.add(symbol);
                    if(Epsilon.get(symbol).equals(false)) break;
                }
            }
        }

        return s;
    }


    private void exploreFirstChild(String v, Set visited) {
        visited.add(v);
        Set<String> s = firstChild(v);

        for(String symbol : s) {
            if(!visited.contains(symbol)) exploreFirstChild(symbol, visited);
        }
    }

    /**
     * Get the first set for a symbol v
     * @param v
     * @return
     */
    public Set First(String v, Boolean inclEpsilon) {
        Set<String> s = new HashSet(), visited = new HashSet<>();

        exploreFirstChild(v, visited);
        for(String symbol : visited) {
            if(Terminals.contains(symbol)) {
                if(!symbol.equals("EPSILON") || inclEpsilon) s.add(symbol);
            }
        }
        return s;
    }

    private Set sibling(String v) {
        Set s = new HashSet();

        // Check for each production
        for(Production p : Productions) {
            String[] r = p.Right;
            String l = p.Left;
            String e;
            for(int i = 0; i < r.length; i++) {

                // If symbol is present in the production (but not the last element) do
                if(r[i].equals(v)) {

                    if(i < r.length - 1) {
                        e = r[i + 1];
                        // Add the first set for the following symbol
                        s.addAll(First(e, false));

                        if(Epsilon.get(e).equals(true)) s.add(l);
                    }
                    else {
                        s.add(l);
                    }
                }

            }
        }

        return s;
    }

    private void exploreSibling(String v, Set visited) {
        Set<String> s = sibling(v);

        for(String symbol : s) {
            if(!visited.contains(symbol)) {
                visited.add(symbol);
                if(NonTerminals.contains(symbol)) {
                    exploreSibling(symbol, visited);
                }
            }
        }
    }

    public Set Follow(String v) {
        Set<String> s = new HashSet(), visited = new HashSet<>();
        if(v.equals(initialP)) s.add("$");
        visited.add(v);
        exploreSibling(v, visited);
        for(String symbol : visited) {
            if(Terminals.contains(symbol)) s.add(symbol);
        }
        return s;
    }

    public void initializeSymbols() {
        Symbols = new HashSet<>();
        Terminals = new HashSet<>();
        NonTerminals = new HashSet<>();

        for (Production p : Productions) {
            NonTerminals.add(p.Left);
            Symbols.add(p.Left);
        }

        for (Production p : Productions) {
            for(String s : p.Right) {
                if(!NonTerminals.contains(s)) {
                    Terminals.add(s);
                    Symbols.add(s);
                }
            }
        }
    }

    public void initEpsilon() {
        int i;
        for(Production p : Productions) {
            String l = p.Left;

            for(String r : p.Right) {
                if(r.equals("EPSILON")) {
                    Epsilon.put(l, true);
                    break;
                }
            }
        }
        Epsilon.put("EPSILON", true);
    }

    public void init() {
        initializeSymbols();
        Epsilon = new HashMap<String, Boolean>();
        for(String s : Symbols) Epsilon.put(s, false);
        initEpsilon();
    }

    public static void main(String[] args) throws IOException {
        Grammar cfg = fromFile("src/CFG");
        //for(String v : cfg.Symbols) System.out.println(cfg.Epsilon.get(v).toString());
        // for(String s : cfg.NonTerminals) System.out.println(s + " -> " +  cfg.Epsilon.get(s));
        for(String s : cfg.Terminals) System.out.println(s);

        /*
        Set<String> first = cfg.First("IdentifierAppendantOptional", true);

        Set<String> follow = cfg.Follow("IdentifierAppendantOptional");

        System.out.println(cfg.Terminals.contains("$"));

        System.out.println("First set(" + first.size() + ")");
        for(String s : first) System.out.print(s + " ");


        System.out.println("\n\n"+ "Follow set(" + follow.size() + ")");
        for(String s : follow) System.out.print(s + " ");
        */
    }
}
