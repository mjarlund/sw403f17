package Semantics.Scope;

import DataStructures.AST.NodeTypes.Types;

/**
 * Created by Anders Brams on 3/21/2017.
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
