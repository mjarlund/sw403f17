package Utilities;

import DataStructures.AST.AST;

public class Report
{
    public AST Node;
    public String Message;
    public ReportTypes Type;

    public Report (AST node, ReportTypes type){
        Node = node;
        Type = type;
    }
}
