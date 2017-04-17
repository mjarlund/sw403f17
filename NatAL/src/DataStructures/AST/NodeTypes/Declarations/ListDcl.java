package DataStructures.AST.NodeTypes.Declarations;
import DataStructures.AST.NodeTypes.Expressions.ArgsExpr;

/**
 * Created by Anders Brams on 3/23/2017.
 */
public class ListDcl extends Dcl{

    public ListDcl(VarDcl declaration, ArgsExpr elements){
        children.add(declaration);
        children.add(elements);
    }


    public VarDcl GetDeclaration()
    {
        return (VarDcl) children.get(0);
    }
}
