package Semantics.Scope;

import DataStructures.AST.NodeTypes.Types;
import Utilities.Reporter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mysjkin on 4/21/17.
 */
public class ListSymbol extends Symbol implements IScope {

    private static ArrayList<Symbol> symbols = new ArrayList<>();

    public ListSymbol(Types listtype, String name) {
        super(name);
        Symbol add = new Symbol("add");
        add.SetDclType(DclType.Function);
        ArrayList<Types> addSignatur = new ArrayList<>(Arrays.asList(listtype));
        add.SetTypeSignature(addSignatur);
        Symbol remove = new Symbol("remove");
        add.SetDclType(DclType.Function);
        ArrayList<Types> removeSignatur = new ArrayList<>(Arrays.asList(Types.INT));
        add.SetTypeSignature(removeSignatur);

        symbols.add(add);
        symbols.add(remove);
    }

    @Override
    public void AddSymbol(Symbol entry) throws Error {
        Reporter.Error(new Error("Add symbol not defined for list symbol"));
    }

    @Override
    public Symbol FindSymbol(String identifier) {
        for(Symbol s : symbols){
            if(s.equals(identifier))
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
