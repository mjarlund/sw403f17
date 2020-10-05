package Semantics.Scope;

import DataStructures.AST.NodeTypes.Types;

public class FParamDiscriptor
{
    private Types type;
    private String structType;
    private Types listofType;

    public FParamDiscriptor(Types type){
        this.type = type;
    }
    public FParamDiscriptor(Types type, String structType){
        this.type = type;
        this.structType = structType;
    }
    public FParamDiscriptor(Types type, Types listofType){
        this.type = type;
        this.listofType = listofType;
    }

    public Types GetType(){
        return type;
    }
    public String GetStructType(){
        return structType;
    }
    public Types GetListofType(){return listofType;}
}
