package Utilities;

import DataStructures.AST.AST;

/**
 * Created by mysjkin on 5/3/17.
 */
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
