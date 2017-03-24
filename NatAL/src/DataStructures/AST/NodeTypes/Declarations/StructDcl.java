package DataStructures.AST.NodeTypes.Declarations;

import java.util.ArrayList;

/**
 * Created by Anders Brams on 3/23/2017.
 */
public class StructDcl extends Dcl {

    public StructDcl(VarDcl declaration, ArrayList<Dcl> elements){
        children.add(declaration);
        for (Dcl el : elements){
            children.add(el);
        }
    }
}
