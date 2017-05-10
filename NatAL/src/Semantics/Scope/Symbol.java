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
    public ArrayList<FParamDiscriptor> TypeSignature;
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

    public Symbol(String name, ArrayList<FParamDiscriptor> typeSignature){
        Name = name;
        Type = null;
        TypeSignature = typeSignature;
    }
    public void SetTypeSignature(ArrayList<FParamDiscriptor> typeSignature)
    {
        TypeSignature = typeSignature;
    }
    public ArrayList<FParamDiscriptor> GetTypeSignature(){return TypeSignature;}
    public void SetDclType(DclType type)
    {
        dclType = type;
    }
    public Object GetType()
    {
        return Type == null ? CustomType : Type;
    }
}
