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
     * Construct a grammar with n productions.
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
    public Map<String, Boolean> Epsilon;

    /**
     * The initial production.
     */
    public String InitialProduction;

    /**
     * Memoization map of the first sets.
     */
    private Map<String, Set> firstMap;

    /**
     * Memoization map of the follow sets.
     */
    private Map<String, Set> followMap;

    /**
     *  Construct grammar from file
     */
    public static Grammar FromFile(String path) throws IOException {
        List<String> lines = Files.readAllLines(new File(path).toPath());
        int n = lines.size(); // (== number of productions)
        Grammar cfg = new Grammar(n);

        for(int i = 0; i < n; i++) {
            String[] rule = lines.get(i).split("->", 2);
            String l = rule[0].trim();
            String[] r = rule[1].trim().split("\\s+");
            for(String s : r) {
                if(s.equals("$")) {
                    cfg.InitialProduction = l;
                    break;
                }
            }
            cfg.Productions[i] = new Production(l, r);
        }

        cfg.Init();
        return cfg;
    }

    /**
     * Get and return the first children of v.
     * @param v
     * @return
     */
    private Set FirstChild(String v) {
        Set s = new HashSet();

        // For each production p in the productions array
        for(Production p : Productions) {

            // If the non-terminal of p equals the symbol v
            if(p.Left.equals(v)) {

                // For each symbol in the productions right side
                for(String symbol : p.Right) {

                    // Add each symbol to the set until the symbol is not epsilon
                    s.add(symbol);
                    if(Epsilon.get(symbol).equals(false)) break;
                }
            }
        }

        return s;
    }

    /**
     * Explore the first child of the symbol v and add to the visit set.
     * @param v
     * @param visited
     */
    private void ExploreFirstChild(String v, Set visited) {
        visited.add(v);
        Set<String> s = FirstChild(v);

        for(String symbol : s) {

            // Explore the first child of each symbol not in the visit set.
            if(!visited.contains(symbol)) ExploreFirstChild(symbol, visited);
        }
    }

    /**
     * Get the first set for the symbol v.
     * Optional epsilon inclusion.
     * @param v
     * @param inclEpsilon
     * @return
     */
    public Set First(String v, Boolean inclEpsilon) {
        // Retrieve memoized value, if it exists.
        if(firstMap.get(v) != null) return firstMap.get(v);

        // Initialize variables
        Set<String> s = new HashSet(), visited = new HashSet<>();

        // Explore the first child of v and add it's children to the visit set.
        ExploreFirstChild(v, visited);

        for(String symbol : visited) {

            // Foreach symbol in the visit set, if symbol is a terminal, add it to the first set.
            if(Terminals.contains(symbol)) {

                // Check for epsilon inclusion
                if(!symbol.equals("EPSILON") || inclEpsilon) s.add(symbol);

            }
        }

        firstMap.put(v, s);
        return s;
    }

    /**
     * Get and return the siblings of the symbol v
     * @param v
     * @return
     */
    private Set Sibling(String v) {
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

    /**
     * Explore the siblings of v and add to the visit set
     * @param v
     * @param visited
     */
    private void ExploreSibling(String v, Set visited) {
        Set<String> s = Sibling(v);

        for(String symbol : s) {

            // If Sibling is a non-terminal explore its siblings
            if(NonTerminals.contains(symbol)) {
                ExploreSibling(symbol, visited);
            }
        }
    }

    /**
     * Get the follow set for a symbol v.
     * @param v
     * @return
     */
    public Set Follow(String v) {
        // Retrieve memoized value, if it exists.
        if(followMap.get(v) != null) return followMap.get(v);

        // Initialize variables
        Set<String> s = new HashSet(), visited = new HashSet<>();

        // If the symbol is the initial non-terminal add $ to its follow set
        if(v.equals(InitialProduction)) s.add("$");
        visited.add(v);

        // Explore the siblings of v
        ExploreSibling(v, visited);

        for(String symbol : visited) {
            if(Terminals.contains(symbol)) s.add(symbol);
        }

        followMap.put(v, s);
        return s;
    }

    /**
     * Check if grammar is LL(1)
     */
    public Boolean IsLL1() {
        String left = Productions[0].Left;
        Set<String> symbols = new HashSet<>();
        Set<String> tmpSet = new HashSet<>();

        for(Production p : Productions) {
            if(left.equals(p.Left)) {
                if(p.Right[0].equals("EPSILON")) tmpSet.addAll(Follow(p.Left));
                else tmpSet.addAll(First(p.Right[0], true));

                for(String s : tmpSet) {
                    if(symbols.contains(s)) return false;
                    symbols.add(s);
                }

            } else {
                symbols.clear();
                if(p.Right[0].equals("EPSILON")) symbols.addAll(Follow(p.Left));
                else symbols.addAll(First(p.Right[0], true));
            }
            tmpSet.clear();
            left = p.Left;
        }

        return true;
    }

    /**
     * Get all terminals and non-terminals in the grammar
     */
    public void InitializeSymbols() {
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

    /**
     * If symbol in the right set of the production is epsilon,
     * set the non-terminal in the left of the production equal to epsilon.
     */
    public void InitEpsilon() {
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

    public void Init() {
        InitializeSymbols();
        Epsilon = new HashMap<>();
        firstMap = new HashMap<>();
        followMap = new HashMap<>();
        for(String s : Symbols) Epsilon.put(s, false);
        InitEpsilon();
    }

    public static void main(String[] args) throws IOException {
        Grammar cfg = new ParsingTable().Grammar;
        System.out.println(cfg.IsLL1());
    }
}
