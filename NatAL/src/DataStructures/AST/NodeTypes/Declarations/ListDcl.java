package DataStructures.AST.NodeTypes.Declarations;
import DataStructures.AST.NodeTypes.Expressions.ArgsExpr;

public class ListDcl extends Dcl{

    public ListDcl(VarDcl declaration, ArgsExpr elements){
        children.add(declaration);
        children.add(elements);
        SetValue("ListDcl");
    }


    public VarDcl GetDeclaration()
    {
        return (VarDcl) children.get(0);
    }

    public ArgsExpr GetElements() {
        return (ArgsExpr) children.get(1);
    }
}
