package DataStructures.AST;

import java.util.ArrayList;

public class AST
{
    private String value;
    public String GetValue(){
        return value;
    }
    public void SetValue(String val){
        value = val;
    }

    private int lineNumber;
    public void SetLineNumber(int lineNumber)
    {
        this.lineNumber = lineNumber;
    }
    public int GetLineNumber()
    {
        return lineNumber;
    }


    public ArrayList<AST> children = new ArrayList<AST>();

    public void AddChild(AST child)
    {
        child.SetParent(this);
        this.children.add(child);
    }

    private AST parent;
    public AST GetParent(){
        return parent;
    }
    public void SetParent(AST par){
        parent = par;
    }

    public AST()
    {
        lineNumber = Syntax.Scanner.Scanner.GetLineNumber();
    }
}
