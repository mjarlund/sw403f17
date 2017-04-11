package Semantics.Scope;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Modes;
import DataStructures.AST.NodeTypes.Statements.*;
import DataStructures.AST.NodeTypes.Types;
import Exceptions.*;
import Utilities.Reporter;
import Utilities.TypeConverter;

import java.util.ArrayList;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class SemanticAnalyzer {

    IScope currentScope = new Scope();
    ArrayList<AST> visitedVarDcls = new ArrayList<>();
    public Symbol FindSymbol(String identifier){
        return currentScope.FindSymbol(identifier);
    }

    // Made changes on 27/03
    /* Creates a new scope as a child of the current */
    public void OpenScope(){
        Scope newScope = new Scope();
        currentScope.AddChildScope(newScope);
        currentScope = newScope;
    }

    /* Overrides the current scope with that of its parent,
     * effectively closing the current scope */
    public void CloseScope(){
        currentScope = currentScope.GetParent();
    }

    /* Open a new scope in every block. No FuncDcls allowed past global,
     * so only need to worry about VarDcls. Called recursively for all
     * nodes, opening and closing scopes every time a code-block or a
     * function declaration is entered or exited, respectively. */
    public void AnalyzeSemantics(AST root){
        for (AST child : root.children) {
            try {
                String switchValue;
                switchValue = (child.GetValue() != null) ? child.GetValue() : ((IdExpr) child).ID;
                Visit(switchValue, child);
            } catch (ClassCastException e){
                //System.out.println(e.getCause().toString());
            }
        }
    }
    private Object Visit(String astName, AST child)
    {
        switch (astName){
            case "VarDcl": /* Add it to this scope (works for FuncDcls too) */
                return Visit((VarDcl) child);
            case "IdExpr": /* Just check whether or not it's there */
                return Visit((IdExpr) child);
            case "BlockStmt": /* Open a new scope, continue */
                EnterScope(child);
                break;
            case "FParamsDcl": /* Used as declarations, add them to that scope */
                Visit((FParamsDcl) child);
                break;
            case "ArgsExpr": /* Not much different from IDs */
                Visit((ArgsExpr) child);
                break;
            case "FuncDcl": /* Only in global scope. Open scope so its formal parameters
                                 * are not seen as symbols in the global scope. */
                Visit((FuncDcl) child);
                break;
            case "ValExpr":
                return Visit((ValExpr) child);
            case "AssignStmt":
                Visit((AssignStmt) child);
                break;
            case "BinaryOPExpr":
                return Visit((BinaryOPExpr)child);
            case "UnaryExpr":
                return Visit((UnaryExpr)child);
            case "BoolExpr":
                return Visit((BoolExpr)child);
            case "FuncCallExpr":
                return Visit((FuncCallExpr)child);
            case "ReturnStmt":
                Visit((ReturnStmt)child);
                break;
            case "IfStmt":
                Visit((IfStmt)child);
                break;
            case "UntilStmt":
                Visit((UntilStmt)child);
                break;
            case "StructDcl":
                Visit((StructDcl) child);
                break;
            case "IOStmt":
                Visit((IOStmt)child);
                break;
            case "IOExpr":
                return Visit((IOExpr) child);
            case "StructVarDcl":
                Visit((StructVarDcl) child);
            default:
                AnalyzeSemantics(child);
        }

        return null;
    }

    private Object Visit(StructVarDcl dcl){
        if (currentScope.FindSymbol(dcl.GetStructType().ID) == null){
            Reporter.Error(new UndeclaredSymbolException("Struct type \" " + dcl.GetStructType().ID + " \" not declared. "));
        } else {
            Symbol structId = new Symbol(dcl.GetIdentifier().ID, dcl.GetStructType().ID);
            structId.dclType = DclType.Struct;
            currentScope.AddSymbol(structId);
        }
        
        return null;
    }

    private Object Visit(IOStmt stmt)
    {
        Types type = (Types) Visit(stmt.GetPin());
        if(!type.equals(Types.PIN))
            Reporter.Error(new IncompatibleValueException("Must be a pin type on line " + stmt.GetLineNumber()));
        if(stmt.GetWriteVal() != null)
        {
            Object expr = Visit(stmt.GetWriteVal().GetValue(), stmt.GetWriteVal());
            System.out.println(((ValExpr)stmt.GetWriteVal()).Type);
            if(expr==null)
                Reporter.Error(new ArgumentsException("Missing expression on line " + stmt.GetLineNumber()));
            else
            {
                Types exprType = (Types)expr;
                if(stmt.GetMode().equals(Modes.DIGITAL) && !exprType.equals(Types.DIGITAL))
                    Reporter.Error(new IncompatibleValueException("Incompatible digital mode or expression on line " + stmt.GetLineNumber()));
                if(stmt.GetMode().equals(Modes.ANALOG) && !exprType.equals(Types.INT))
                    Reporter.Error(new IncompatibleValueException("Incompatible analog mode or expression on line " + stmt.GetLineNumber()));
            }
        }
        
        return null;
    }
    
    private Object Visit(IOExpr expr)
    {
        Types type = (Types) Visit(expr.GetPin());
        if(!type.equals(Types.PIN))
            Reporter.Error(new IncompatibleValueException("Must be a pin type on line " + expr.GetLineNumber()));
        return type;
    }
    
    private Object Visit(IfStmt stmt)
    {
        Expr condition = stmt.GetCondition();
        // if condition check must result in a bool expression
        if(!(condition instanceof BoolExpr))
            Reporter.Error(new IncompatibleValueException("Expected boolean expression in " + stmt + " on line " + stmt.GetLineNumber()));
        AnalyzeSemantics(stmt);
        
        return null;
    }
    
    private Object Visit(UntilStmt stmt)
    {
        Expr condition = stmt.GetCondition();
        // until condition check must result in a bool expression
        if(!(condition instanceof BoolExpr))
            Reporter.Error(new IncompatibleValueException("Expected boolean expression in " + stmt + " on line " + stmt.GetLineNumber()));
        AnalyzeSemantics(stmt);
        
        return null;
    }
    private Object Visit(ReturnStmt stmt)
    {
        Expr returnValue = stmt.GetReturnExpr();
        // Get the value after the return stmt
        Object returnedType = Visit(returnValue.GetValue(), returnValue);
        FuncDcl func = (FuncDcl) stmt.GetParent().GetParent();
        // Get function return type
        Types returnType = func.GetVarDcl().Type;
        // The type returned by the return stmt must match the function return type
        if(!returnedType.equals(returnType))
            Reporter.Error(new IncompatibleValueException(returnedType,returnType));
        
        return null;
    }
    
    /* Checks symbol table for function identifier and checks correct usage of arguments */
    private Object Visit(FuncCallExpr expr)
    {
        IdExpr funcId = expr.GetFuncId();
        Symbol identifier = FindSymbol(funcId.ID);
        // Check if function is declared before usage
        if(identifier==null)
            Reporter.Error(new UndeclaredSymbolException(funcId.ID+ " not declared.", expr.GetLineNumber()));
        if(!identifier.dclType.equals( DclType.Function))
            Reporter.Error(new InvalidIdentifierException("Not used as a function call"));
        // Checks that args are used declared before usage
        Visit(expr.GetFuncArgs().GetValue(),expr.GetFuncArgs());
        ArrayList<ArgExpr> args = expr.GetFuncArgs().GetArgs();
        if(identifier.TypeSignature.size()>0 || args.size()>0)
        {
            int iterations = identifier.TypeSignature.size();
            if(iterations > args.size())
                Reporter.Error(new ArgumentsException("Too few Arguments in " + identifier.Name));
            else if(iterations < args.size())
                Reporter.Error(new ArgumentsException("Too many Arguments in " + identifier.Name));
            // Checks that every argument type fits the function type signature
            for (int i = 0; i<iterations;i++)
            {
                ArgExpr argument = args.get(i);

                Types argType = argument.GetArg() instanceof ValExpr ?
                        ((ValExpr)argument.GetArg()).Type :
                        (Types) Visit((IdExpr) argument.GetArg());

                if(!argType.equals(identifier.TypeSignature.get(i)))
                    Reporter.Error(new IncompatibleValueException("Incompatible argument types in " + identifier.Name + " on line " + expr.GetLineNumber()));
            }
        }
        // returns function return type
        return identifier.Type;
    }
    private Object Visit(BinaryOPExpr expr)
    {
        AST left = expr.GetLeftExpr();
        AST right = expr.GetRightExpr();
        Object lType = Visit(left.GetValue(),left);
        Object rType = Visit(right.GetValue(),right);

        // checks that the left hand side is the same as the right hand side
        if(!lType.equals(rType))
            Reporter.Error(new IncompatibleValueException(lType,rType,expr.GetLineNumber()));

        // checks that the type is a valid type for binary expressions
        if(lType.equals(Types.BOOL))
            Reporter.Error(new InvalidTypeException(lType, expr.GetLineNumber()));

        return lType;
    }
    private Object Visit(BoolExpr expr)
    {
        AST left = expr.GetLeftExpr();
        AST right = expr.GetRightExpr();
        Object lType = Visit(left.GetValue(),left);
        Object rType = Visit(right.GetValue(),right);
        // checks that the left hand side type is the same as the right hand side type
        if(!lType.equals(rType))
            Reporter.Error(new IncompatibleValueException(lType,rType,expr.GetLineNumber()));
        return lType;
    }
    private Object Visit(AssignStmt stmt)
    {
        AST left = stmt.GetLeft();
        AST right = stmt.GetRight();
        Object lType = Visit(left.GetValue(),left);
        Object rType = Visit(right.GetValue(),right);
        // left value must be assignable i.e. a variable
        if(!IsAssignable(left))
            throw new Error("LHS not assignable " + left);
        // Pin types are integers
        if(lType.equals(Types.PIN))
            lType = Types.INT;
        if(rType.equals(Types.PIN))
            rType = Types.INT;
        // left hand side is the same type as the right hand side type
        if(!lType.equals(rType))
            Reporter.Error(new IncompatibleValueException(lType,rType,stmt.GetLineNumber()));
        return null;
    }
    
    private boolean IsAssignable(AST lhs)
    {
        // left hand side must be a variable
        if(lhs instanceof IdExpr || lhs instanceof VarDcl)
            return true;
        return false;
    }
    
    private Object Visit(UnaryExpr expr)
    {
        Object res = Visit(expr.GetValExpr().GetValue(),expr.GetValExpr());
        return res;
    }
    
    private Object Visit(ValExpr lit)
    {
        return TypeConverter.TypetoTypes(lit.LiteralValue);
    }

    private Object Visit(VarDcl node){
        for (AST visited : visitedVarDcls){
            if (visited == node) return null;
        }
        String varID  = node.Identifier;
        Types varType = node.GetType();
        Symbol var = new Symbol(varID,varType);
        var.SetDclType(DclType.Variable);
        currentScope.AddSymbol(var);
        visitedVarDcls.add(node);

        return varType;
    }

    public Object Visit(IdExpr node){
        String id = node.ID;
        Symbol identifier = FindSymbol(id);
        if (identifier == null)
            Reporter.Error(new UndeclaredSymbolException(id + " not declared.", node.GetLineNumber()));
        if(identifier.dclType.equals(DclType.Function))
            Reporter.Error(new InvalidIdentifierException("Not a variable " + identifier.Name));
        if(identifier.dclType.equals(DclType.Struct)){
            if(node.children.size() == 0) {
                return identifier.GetType();
            }
            else
            {
                StructSymbol structSymbol = (StructSymbol) currentScope.FindSymbol(identifier.CustomType);
                IdExpr expr = (IdExpr) node.children.get(0);
                identifier = structSymbol.FindSymbol(expr.ID);
                if(identifier == null)
                    Reporter.Error(new UndeclaredSymbolException(id + " not declared.", node.GetLineNumber()));
                return identifier.GetType();
            }
        }
        return identifier.Type;
    }

    public Object Visit(FParamsDcl node){
        ArrayList<AST> params = node.children;
        for (AST param : params){
            String paramID  = ((FParamDcl)param).Identifier;
            Types paramType = ((FParamDcl)param).Type;
            currentScope.AddSymbol(new Symbol(paramID, paramType));
        }
        
        return null;
    }

    public Object Visit(ArgsExpr node){
        ArrayList<ArgExpr> args = node.GetArgs();
        for (ArgExpr arg : args) {
            String argID = arg.GetArg().GetValue();
            /*if (FindSymbol(argID) == null) {
                Reporter.Error(new UndeclaredSymbolException(argID + " not declared, but is used as an argument."));
                //throw new Error(argID + " not declared, but is used as an argument. ");
            }*/
            Visit(argID,arg.GetArg());
        }
        
        return null;
    }

    public Object Visit(FuncDcl node){
        if (currentScope.GetDepth() > 0){
            Reporter.Error(new InvalidScopeException(node.GetVarDcl().Identifier + ": Functions can only be declared in global scope."));
            //throw new Error(node.GetValue() + ": functions can only be declared in global scope. ");
        }
        Visit(node.GetVarDcl());

        // setup function type signature, must be before scope else no recursion
        ArrayList<FParamDcl> parameters = node.GetFormalParamsDcl().GetFParams();
        ArrayList<Types> typeSignature = new ArrayList<>();
        for(FParamDcl param : parameters)
        {
            typeSignature.add(param.Type);
        }
        Symbol funcDcl = FindSymbol(node.GetVarDcl().Identifier);
        funcDcl.SetTypeSignature(typeSignature);
        funcDcl.SetDclType(DclType.Function);

        // Enter scope and visit func declaration children
        EnterScope(node);
        if(!node.GetVarDcl().Identifier.equals(node.GetEndIdentifier()))
            Reporter.Error(new InvalidIdentifierException("Invalid end Identifier in line " + node.GetLineNumber()));
        
        return null;
    }

    public Object Visit(StructDcl node){
        if (currentScope.GetDepth() > 0){
            Reporter.Error(new InvalidScopeException(
                    node.GetVarDcl().Identifier + ": Structs can only be declared in global scope."));
        }
        String varID  = node.GetVarDcl().Identifier;
        Types varType = node.GetVarDcl().GetType();
        StructSymbol symbol = new StructSymbol(varID, varType);
        currentScope.AddSymbol(symbol);
        IScope prevScope = currentScope;
        currentScope = symbol;
        AnalyzeSemantics(node);
        currentScope = prevScope;
        
        return null;
    }

    public void EnterScope(AST node){
        OpenScope();
        AnalyzeSemantics(node);
        CloseScope();
    }
}
