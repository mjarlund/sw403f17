package p4test.SymbolTable;
import p4test.AbstractSyntaxTree.AST;
import p4test.AbstractSyntaxTree.Dcl.FormalParameter;
import p4test.AbstractSyntaxTree.Dcl.FormalParameters;
import p4test.AbstractSyntaxTree.Dcl.Identifier;
import p4test.AbstractSyntaxTree.Dcl.VarDcl;
import p4test.AbstractSyntaxTree.Expr.Argument;
import p4test.AbstractSyntaxTree.Expr.Arguments;
import p4test.AbstractSyntaxTree.Types;

import java.util.ArrayList;

/**
 * Created by Anders Brams on 3/20/2017.
 */
public class ScopeManager {

    Scope currentScope;

    public Symbol FindSymbol(String identifier){
        return currentScope.FindSymbol(identifier);
    }

    /* Creates a new scope as a child of the current */
    public void OpenScope(){
        if (currentScope == null){ //Create a new global scope
            currentScope = new Scope();
        } else { //Global scope already exists, continue
            Scope newScope = new Scope();
            currentScope.AddChildScope(newScope);
            currentScope = newScope;
        }
    }

    /* memes */
    public void CloseScope(){
        currentScope = currentScope.Parent;
    }

    /* Open a new scope in every block. No FuncDcls allowed past global,
     * so only need to worry about VarDcls. Called recursively for all
     * nodes, opening and closing scopes every time a Block is entered
     * or exited, respectively. */
    public void Scopify(AST root){
        for (AST child : root.children){

            String switchValue;
            switchValue = (child.GetValue() != null)? child.GetValue() : ((Identifier)child).ID;

            switch (switchValue){
                case "VarDcl": /* Add it to this scope (works for FuncDcls too) */
                    String varID  = ((VarDcl)child).Identifier;
                    Types varType = ((VarDcl)child).Type;
                    currentScope.AddSymbol(new Symbol(varID, varType));
                    break;
                case "Id": /* Just check whether or not it's there */
                    String id = ((Identifier)child).ID;
                    if (FindSymbol(id) == null)
                        throw new Error(id + " not declared.");
                    break;
                case "Block": /* Open a new scope, continue */
                    OpenScope();
                    Scopify(child);
                    CloseScope();
                    break;
                case "FParams": /* Used as declarations, add them to that scope */
                    ArrayList<AST> params = ((FormalParameters)child).children;
                    for (AST param : params){
                        String paramID  = ((FormalParameter)param).Identifier;
                        Types paramType = ((FormalParameter)param).Type;
                        currentScope.AddSymbol(new Symbol(paramID, paramType));
                    }
                    break;
                case "AParams": /* Not much different from IDs */
                    ArrayList<AST> args = ((Arguments)child).children;
                    for (AST arg : args) {
                        String argID = arg.GetValue();
                        if (FindSymbol(argID) == null) {
                            throw new Error(argID + " not declared, but is used as an argument. ");
                        }
                    }
                default:
                    Scopify(child);
                    break;
            }
        }
    }
}
