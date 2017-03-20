package p4test.SymbolTable;

import p4test.AbstractSyntaxTree.Types;

/**
 * Created by Anders Brams on 3/20/2017.
 */
public class SymbolTableEntry {
    public String Name;
    public Types Type;
    public SymbolTableEntry NextOuterDeclaration;
}
