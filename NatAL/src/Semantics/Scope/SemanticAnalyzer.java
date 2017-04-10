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

    Scope currentScope = new Scope();
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
        currentScope = currentScope.Parent;
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
                VisitValue(switchValue, child);
            } catch (ClassCastException e){
                //System.out.println(e.getCause().toString());
            }
        }
    }
    private Object VisitValue(String astName, AST child)
    {
        switch (astName){
            case "VarDcl": /* Add it to this scope (works for FuncDcls too) */
                return VisitVarDcl((VarDcl) child);
            case "IdExpr": /* Just check whether or not it's there */
                return VisitId((IdExpr) child);
            case "BlockStmt": /* Open a new scope, continue */
                EnterScope(child);
                break;
            case "FParamsDcl": /* Used as declarations, add them to that scope */
                VisitFParams((FParamsDcl) child);
                break;
            case "ArgsExpr": /* Not much different from IDs */
                VisitAParams((ArgsExpr) child);
                break;
            case "FuncDcl": /* Only in global scope. Open scope so its formal parameters
                                 * are not seen as symbols in the global scope. */
                VisitFuncDcl((FuncDcl) child);
                break;
            case "ValExpr":
                return VisitLiteral((ValExpr) child);
            case "AssignStmt":
                VisitAssignment((AssignStmt) child);
                break;
            case "BinaryOPExpr":
                return VisitBinaryExpr((BinaryOPExpr)child);
            case "UnaryExpr":
                return VisitUnaryExpr((UnaryExpr)child);
            case "BoolExpr":
                return VisitBoolExpr((BoolExpr)child);
            case "FuncCallExpr":
                return VisitFuncCall((FuncCallExpr)child);
            case "ReturnStmt":
                VisitReturnStmt((ReturnStmt)child);
                break;
            case "IfStmt":
                VisitIfStmt((IfStmt)child);
                break;
            case "UntilStmt":
                VisitUntilStmt((UntilStmt)child);
                break;
            case "StructDcl":
                VisitStructDcl((StructDcl) child);
                break;
            case "IOStmt":
                VisitIOStmt((IOStmt)child);
                break;
            case "IOExpr":
                return VisitIOExpr((IOExpr) child);
            case "StructVarDcl":
                VisitStructVarDcl((StructVarDcl) child);
            default:
                AnalyzeSemantics(child);
                break;
        }
        return null;
    }

    private void VisitStructVarDcl(StructVarDcl dcl){
        if (currentScope.FindSymbol(dcl.Type.ID) == null){
            Reporter.Error(new UndeclaredSymbolException("Struct type \" " + dcl.Type.ID + " \" not declared. "));
        } else {
            currentScope.AddSymbol(new Symbol(dcl.ID.ID));
        }
    }

    private void VisitIOStmt(IOStmt stmt)
    {
        Types type = (Types)VisitId(stmt.GetPin());
        if(!type.equals(Types.PIN))
            Reporter.Error(new IncompatibleValueException("Must be a pin type on line " + stmt.GetLineNumber()));
        if(stmt.GetWriteVal() != null)
        {
            Object expr = VisitValue(stmt.GetWriteVal().GetValue(), stmt.GetWriteVal());
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
    }
    private Object VisitIOExpr(IOExpr expr)
    {
        Types type = (Types)VisitId(expr.GetPin());
        if(!type.equals(Types.PIN))
            Reporter.Error(new IncompatibleValueException("Must be a pin type on line " + expr.GetLineNumber()));
        return type;
    }
    private void VisitIfStmt(IfStmt stmt)
    {
        Expr condition = stmt.GetCondition();
        // if condition check must result in a bool expression
        if(!(condition instanceof BoolExpr))
            Reporter.Error(new IncompatibleValueException("Expected boolean expression in " + stmt + " on line " + stmt.GetLineNumber()));
        AnalyzeSemantics(stmt);
    }
    private void VisitUntilStmt(UntilStmt stmt)
    {
        Expr condition = stmt.GetCondition();
        // until condition check must result in a bool expression
        if(!(condition instanceof BoolExpr))
            Reporter.Error(new IncompatibleValueException("Expected boolean expression in " + stmt + " on line " + stmt.GetLineNumber()));
        AnalyzeSemantics(stmt);
    }
    private void VisitReturnStmt(ReturnStmt stmt)
    {
        Expr returnValue = stmt.GetReturnExpr();
        // Get the value after the return stmt
        Object returnedType = VisitValue(returnValue.GetValue(), returnValue);
        FuncDcl func = (FuncDcl) stmt.GetParent().GetParent();
        // Get function return type
        Types returnType = func.GetVarDcl().Type;
        // The type returned by the return stmt must match the function return type
        if(!returnedType.equals(returnType))
            Reporter.Error(new IncompatibleValueException(returnedType,returnType));
    }
    /* Checks symbol table for function identifier and checks correct usage of arguments */
    private Object VisitFuncCall(FuncCallExpr expr)
    {
        IdExpr funcId = expr.GetFuncId();
        Symbol identifier = FindSymbol(funcId.ID);
        // Check if function is declared before usage
        if(identifier==null)
            Reporter.Error(new UndeclaredSymbolException(funcId.ID+ " not declared.", expr.GetLineNumber()));
        if(!identifier.dclType.equals( DclType.Function))
            Reporter.Error(new InvalidIdentifierException("Not used as a function call"));
        // Checks that args are used declared before usage
        VisitValue(expr.GetFuncArgs().GetValue(),expr.GetFuncArgs());
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
                        (Types)VisitId((IdExpr) argument.GetArg());

                if(!argType.equals(identifier.TypeSignature.get(i)))
                    Reporter.Error(new IncompatibleValueException("Incompatible argument types in " + identifier.Name + " on line " + expr.GetLineNumber()));
            }
        }
        // returns function return type
        return identifier.Type;
    }
    private Object VisitBinaryExpr(BinaryOPExpr expr)
    {
        AST left = expr.GetLeftExpr();
        AST right = expr.GetRightExpr();
        Object lType = VisitValue(left.GetValue(),left);
        Object rType = VisitValue(right.GetValue(),right);

        // checks that the left hand side is the same as the right hand side
        if(!lType.equals(rType))
            Reporter.Error(new IncompatibleValueException(lType,rType,expr.GetLineNumber()));

        // checks that the type is a valid type for binary expressions
        if(lType.equals(Types.BOOL))
            Reporter.Error(new InvalidTypeException(lType, expr.GetLineNumber()));

        return lType;
    }
    private Object VisitBoolExpr(BoolExpr expr)
    {
        AST left = expr.GetLeftExpr();
        AST right = expr.GetRightExpr();
        Object lType = VisitValue(left.GetValue(),left);
        Object rType = VisitValue(right.GetValue(),right);
        // checks that the left hand side type is the same as the right hand side type
        if(!lType.equals(rType))
            Reporter.Error(new IncompatibleValueException(lType,rType,expr.GetLineNumber()));
        return lType;
    }
    private Object VisitAssignment(AssignStmt stmt)
    {
        AST left = stmt.GetLeft();
        AST right = stmt.GetRight();
        Object lType = VisitValue(left.GetValue(),left);
        Object rType = VisitValue(right.GetValue(),right);
        // Pin types are integers
        if(lType.equals(Types.PIN))
            lType = Types.INT;
        if(rType.equals(Types.PIN))
            rType = Types.INT;
        // left hand side is the same type as the right hand side type
        if(!lType.equals(rType))
            Reporter.Error(new IncompatibleValueException(lType,rType,stmt.GetLineNumber()));
        // left value must be assignable i.e. a variable
        if(!IsAssignable(left))
            throw new Error("LHS not assignable " + left);
        return null;
    }
    private boolean IsAssignable(AST lhs)
    {
        // left hand side must be a variable
        if(lhs instanceof IdExpr || lhs instanceof VarDcl)
            return true;
        return false;
    }
    private Object VisitUnaryExpr(UnaryExpr expr)
    {
        Object res = VisitValue(expr.GetValExpr().GetValue(),expr.GetValExpr());
        return res;
    }
    private Object VisitLiteral(ValExpr lit)
    {
        return TypeConverter.TypetoTypes(lit.LiteralValue);
    }

    public Object VisitVarDcl(VarDcl node){
        for (AST visited : visitedVarDcls){
            if (visited == node) return null;
        }
        String varID  = node.Identifier;
        Types varType = node.GetType();
        Symbol var = new Symbol(varID,varType);
        var.SetDclType(DclType.Variable);
        currentScope.AddSymbol(var);
        Reporter.Log(varID + " added to scope at depth = " + currentScope.Depth);
        visitedVarDcls.add(node);

        return varType;
    }

    public Object VisitId(IdExpr node){
        String id = node.ID;
        Symbol identifier = FindSymbol(id);
        if (identifier == null)
            Reporter.Error(new UndeclaredSymbolException(id + " not declared.", node.GetLineNumber()));
        if(identifier.dclType.equals(DclType.Function))
            Reporter.Error(new InvalidIdentifierException("Not a variable " + identifier.Name));
        return identifier.Type;
    }

    public void VisitFParams(FParamsDcl node){
        ArrayList<AST> params = node.children;
        for (AST param : params){
            String paramID  = ((FParamDcl)param).Identifier;
            Types paramType = ((FParamDcl)param).Type;
            currentScope.AddSymbol(new Symbol(paramID, paramType));
            Reporter.Log(paramID + " added to scope at depth = " + currentScope.Depth);
        }
    }

    public void VisitAParams(ArgsExpr node){
        ArrayList<ArgExpr> args = node.GetArgs();
        for (ArgExpr arg : args) {
            String argID = arg.GetArg().GetValue();
            /*if (FindSymbol(argID) == null) {
                Reporter.Error(new UndeclaredSymbolException(argID + " not declared, but is used as an argument."));
                //throw new Error(argID + " not declared, but is used as an argument. ");
            }*/
            VisitValue(argID,arg.GetArg());
        }
    }

    public void VisitFuncDcl(FuncDcl node){
        if (currentScope.Depth > 0){
            Reporter.Error(new InvalidScopeException(node.GetVarDcl().Identifier + ": Functions can only be declared in global scope."));
            //throw new Error(node.GetValue() + ": functions can only be declared in global scope. ");
        }
        VisitVarDcl(node.GetVarDcl());

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
    }

    public void VisitStructDcl(StructDcl node){
        if (currentScope.Depth > 0){
            Reporter.Error(new InvalidScopeException(
                    node.GetVarDcl().Identifier + ": Structs can only be declared in global scope."));
        }
        VisitVarDcl(node.GetVarDcl());
        EnterScope(node);
    }

    public void EnterScope(AST node){
        OpenScope();
        AnalyzeSemantics(node);
        CloseScope();
    }
}
