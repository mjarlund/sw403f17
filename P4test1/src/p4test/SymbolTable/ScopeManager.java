package p4test.SymbolTable;
import p4test.AbstractSyntaxTree.AST;
import p4test.AbstractSyntaxTree.Dcl.*;
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
     * nodes, opening and closing scopes every time a code-block or a
     * function declaration is entered or exited, respectively. */
    public void Scopify(AST root){
        for (AST child : root.children){

            String switchValue;
            switchValue = (child.GetValue() != null)? child.GetValue() : ((Identifier)child).ID;

            switch (switchValue){
                case "VarDcl": /* Add it to this scope (works for FuncDcls too) */
                    VisitVarDcl((VarDcl) child);
                    break;
                case "Id": /* Just check whether or not it's there */
                    VisitId((Identifier) child);
                    break;
                case "Block": /* Open a new scope, continue */
                    EnterScope(child);
                    break;
                case "FParams": /* Used as declarations, add them to that scope */
                    VisitFParams((FormalParameters) child);
                    break;
                case "AParams": /* Not much different from IDs */
                    VisitAParams((Arguments) child);
                    break;
                case "FuncDcl": /* Only in global scope. Open scope so its formal parameters
                                 * are not seen as symbols in the global scope. */
                    VisitFuncDcl((FuncDcl) child);
                    EnterScope(child);
                    break;
                default:
                    Scopify(child);
                    break;
            }
        }
    }
    
    public void VisitVarDcl(VarDcl node){
        String varID  = node.Identifier;
        Types varType = node.Type;
        currentScope.AddSymbol(new Symbol(varID, varType));
    }

    public void VisitId(Identifier node){
        String id = node.ID;
        if (FindSymbol(id) == null)
            throw new Error(id + " not declared.");
    }

    public void VisitFParams(FormalParameters node){
        ArrayList<AST> params = node.children;
        for (AST param : params){
            String paramID  = ((FormalParameter)param).Identifier;
            Types paramType = ((FormalParameter)param).Type;
            currentScope.AddSymbol(new Symbol(paramID, paramType));
        }
    }

    public void VisitAParams(Arguments node){
        ArrayList<AST> args = node.children;
        for (AST arg : args) {
            String argID = arg.GetValue();
            if (FindSymbol(argID) == null) {
                throw new Error(argID + " not declared, but is used as an argument. ");
            }
        }
    }

    public void VisitFuncDcl(FuncDcl node){
        if (currentScope.Depth != 0){
            throw new Error(
                    node.GetValue() + ": functions can only be declared in global scope. ");
        }
    }

    public void EnterScope(AST node){
        OpenScope();
        Scopify(node);
        CloseScope();
    }
}
