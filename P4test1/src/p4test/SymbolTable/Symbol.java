package p4test.SymbolTable;

import p4test.AbstractSyntaxTree.Types;

/**
 * Created by Anders Brams on 3/20/2017.
 */
public class Symbol {
    public String Name;
    public Types Type;

    public Symbol(String name, Types type){
        Name = name;
        Type = type;
    }

    public Symbol(String name){
        Name = name;
        Type = null;
    }
}
