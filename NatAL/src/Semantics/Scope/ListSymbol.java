package Semantics.Scope;

import DataStructures.AST.NodeTypes.Types;
import Utilities.Reporter;

import java.util.ArrayList;
import java.util.Arrays;

public class ListSymbol extends Symbol implements IScope {

    private ArrayList<Symbol> symbols = new ArrayList<>();

    public ListSymbol(Types listtype, String name) {
        super(name,listtype);
        Symbol add = new Symbol("add");
        add.SetDclType(DclType.Function);
        ArrayList<FParamDiscriptor> addSignatur = new ArrayList<>(Arrays.asList(new FParamDiscriptor(listtype)));
        add.SetTypeSignature(addSignatur);
        Symbol remove = new Symbol("remove");
        add.SetDclType(DclType.Function);
        ArrayList<FParamDiscriptor> removeSignatur = new ArrayList<>(Arrays.asList(new FParamDiscriptor(Types.INT)));
        remove.SetTypeSignature(removeSignatur);

        symbols.add(add);
        symbols.add(remove);
    }

    @Override
    public void AddSymbol(Symbol entry) throws Error {
        Reporter.Error(new Error("Add symbol not defined for list symbol"));
    }

    @Override
    public Symbol FindSymbol(String identifier)
    {
        for(Symbol s : symbols)
        {
            if(s.Name.equals(identifier))
                return s;
        }
        return null;
    }

    @Override
    public void AddChildScope(Scope child) {

    }

    @Override
    public IScope GetParent() {
        return null;
    }

    @Override
    public int GetDepth() {
        return 0;
    }
}
