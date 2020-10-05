package Syntax.Grammar;

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
}
