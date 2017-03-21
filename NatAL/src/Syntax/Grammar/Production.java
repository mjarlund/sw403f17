package Syntax.Grammar;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class Production {
    /**
     * The left side (a non-terminal).
     */
    public String Left;

    /**
     * The right side (a collection of terminals and / or non-terminals)
     */
    public String[] Right;

    /**
     * Creates a production (left -> right) where left is a non-terminal
     * and right is a collection of terminals and / or non-terminals.
     * @param left
     * @param right
     */
    public Production(String left, String ... right) {
        Left = left;
        Right = right;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder(Left);
        sb.append(" ->");
        for (String r : Right) sb.append(" " + r);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(new Production("Program", "Statement", "Statements"));
    }
}
