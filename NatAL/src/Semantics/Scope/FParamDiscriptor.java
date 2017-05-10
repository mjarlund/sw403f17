package Semantics.Scope;

import DataStructures.AST.NodeTypes.Types;

/**
 * Created by mysjkin on 5/10/17.
 */
public class FParamDiscriptor
{
    private Types type;
    private String structType;

    public FParamDiscriptor(Types type){
        this.type = type;
    }
    public FParamDiscriptor(Types type, String structType){
        this.type = type;
        this.structType = structType;
    }

    public void SetType(Types type){
        this.type = type;
    }
    public Types GetType(){
        return type;
    }
    public void SetStructType(String type){
        structType = type;
    }
    public String GetStructType(){
        return structType;
    }
}
