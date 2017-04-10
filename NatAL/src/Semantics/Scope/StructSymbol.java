package Semantics.Scope;

import DataStructures.AST.NodeTypes.Types;
import Exceptions.DuplicatedSymbolException;
import Utilities.Reporter;

import java.util.ArrayList;

/**
 * Created by mysjkin on 4/10/17.
 */
public class StructSymbol extends Symbol implements IScope
{
    public int Depth = 1;
    private ArrayList<Symbol> symbols;
    public Scope Parent;

    public StructSymbol(String name, Types type) {
        super(name, type);
        dclType = DclType.Struct;
        symbols = new ArrayList<>();
    }

    @Override
    public void AddSymbol(Symbol entry) throws Error {
        if(symbols.contains(entry))
            Reporter.Error(new DuplicatedSymbolException("Name already defined in current scope " + entry.Name));
        else
            symbols.add(entry);
    }

    @Override
    public Symbol FindSymbol(String identifier) {
        for(Symbol s : symbols)
            if(s.Name.equals(identifier))
                return s;
        return null;
    }

    @Override
    public void AddChildScope(Scope child) {
        /* DO NOTHING */
    }

    @Override
    public IScope GetParent() {
        return Parent;
    }

    @Override
    public int GetDepth() {
        return Depth;
    }
}
