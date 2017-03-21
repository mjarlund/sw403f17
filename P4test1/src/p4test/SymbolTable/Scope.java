package p4test.SymbolTable;
import java.util.ArrayList;

/**
 * Created by Anders Brams on 3/20/2017.
 * A "Symbol table" implemented in a tree structure.
 */
public class Scope {

    public int Depth;
    private ArrayList<Symbol> symbols;
    public Scope Parent;

    public Scope(){
        symbols = new ArrayList<>();
    }

    /* If that identifier already exists in this scope,
     * throw an error */
    public void AddSymbol(Symbol entry) throws Error{
        for (Symbol n : symbols){
            if (n.Name.equals(entry.Name)){
                throw new Error("Name already defined in this scope");
            }
        }
        symbols.add(entry);
    }

    /* Recursively traverses the tree in reverse in
     * order to find the symbol. */
    public Symbol FindSymbol(String identifier){
        Symbol toReturn = null;

        for (Symbol s : symbols){
            if (s.Name.equals(identifier)){
                toReturn = s;
                break;
            }
        }
        if (toReturn == null && Parent != null) {
            toReturn = Parent.FindSymbol(identifier);
        }
        return toReturn;
    }

    public void AddChildScope(Scope child){
        child.Depth = Depth+1;
        child.Parent = this;
    }
}
