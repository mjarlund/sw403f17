package Semantics.Scope;

import DataStructures.AST.NodeTypes.Types;

import java.util.ArrayList;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class Symbol {
    public String Name;
    public Types Type;
    public String CustomType;
    public ArrayList<Types> TypeSignature;
    public DclType dclType = DclType.Variable;

    public Symbol(String name){
        Name = name;
    }
    public Symbol(String name, String type){
        Name = name;
        CustomType = type;
    }
    public Symbol(String name, Types type){
        Name = name;
        Type = type;
    }

    public Symbol(String name, ArrayList<Types> typeSignature){
        Name = name;
        Type = null;
        TypeSignature = typeSignature;
    }
    public void SetTypeSignature(ArrayList<Types> typeSignature)
    {
        TypeSignature = typeSignature;
    }
    public ArrayList<Types> GetTypeSignature(){return TypeSignature;}
    public void SetDclType(DclType type)
    {
        dclType = type;
    }
    public Object GetType()
    {
        return Type == null ? CustomType : Type;
    }
}
