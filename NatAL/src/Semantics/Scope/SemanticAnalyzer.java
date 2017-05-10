package Semantics.Scope;

import DataStructures.AST.AST;
import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Modes;
import DataStructures.AST.NodeTypes.Statements.*;
import DataStructures.AST.NodeTypes.Types;
import Exceptions.*;
import Syntax.Tokens.Token;
import Utilities.*;

import java.util.ArrayList;

public class SemanticAnalyzer implements IVisitor{

    IScope currentScope = new Scope();
    IScope GlobalScope = currentScope;
    ArrayList<AST> visitedVarDcls = new ArrayList<>();
    public Symbol FindSymbol(String identifier){
        return currentScope.FindSymbol(identifier);
    }

    public SemanticAnalyzer(){

    }

    
    
    private VisitorDriver visitValue = new VisitorDriver(this);
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
    public void CheckForSetupAndLoopFunc()
    {
    	String[] funcs = new String[]{"setup", "loop"};
    	
    	for (String Func : funcs) {
    		Symbol sym = GlobalScope.FindSymbol(Func);
        	if (sym == null)
        	{
        		System.out.println("hej null");
        	}
        	if (sym.dclType != DclType.Function)
        	{
        		System.out.println("hej dclType");
        	}
        	if (sym.Type != Types.VOID)
        	{
        		Reporter.Error(ReportTypes.EssentialMethodNotVoidError, sym.Name);
        	}
        	if (sym.TypeSignature.size() != 0)
        	{
        		System.out.println("hej typesignature");
        	}
    	} 		
    }
    public void BeginSemanticAnalysis(AST root){
    	VisitChildren(root);
    	CheckForSetupAndLoopFunc(); 	
    }
    
    /* Open a new scope in every block. No FuncDcls allowed past global,
     * so only need to worry about VarDcls. Called recursively for all
     * nodes, opening and closing scopes every time a code-block or a
     * function declaration is entered or exited, respectively. */
    private void VisitChildren(AST root){
        for (AST child : root.children) {
            try {
                String switchValue;
                switchValue = (child.GetValue() != null) ? child.GetValue() : ((IdExpr) child).ID;
                visitValue.Visit(switchValue, child);
            } catch (ClassCastException e){
                //System.out.println(e.getCause().toString());
            }
        }
    }
    public Object Visit(BlockStmt block){
        EnterScope(block);
        return null;
    }

    public Object Visit(ListDcl dcl) {
        String dclId = dcl.GetDeclaration().Identifier;
        Types type = dcl.GetDeclaration().Type;
        ArrayList<ArgExpr> elements = dcl.GetElements().GetArgs();

        for(ArgExpr arg : elements) {
            if(!arg.GetType().equals(type))
                Reporter.Error(new InvalidTypeException("\"" + arg.GetArg() + "\" is not a " + type + " on line " + dcl.GetLineNumber()));
        }
        Symbol listId = new ListSymbol(type,dclId);
        listId.dclType = DclType.List;
        currentScope.AddSymbol(listId);
        return null;
    }

    public Object Visit(StructVarDcl dcl){
        if (GlobalScope.FindSymbol(dcl.GetStructType().ID) == null){
            Reporter.Error(new UndeclaredSymbolException("Struct type \" " + dcl.GetStructType().ID + " \" not declared on line: " + dcl.GetLineNumber()));
        } else {
            Symbol structId = new Symbol(dcl.GetIdentifier().ID, dcl.GetStructType().ID);
            structId.dclType = DclType.Struct;
            currentScope.AddSymbol(structId);
        }
        
        return null;
    }

    public Object Visit(IOStmt stmt)
    {
        Types type = (Types) Visit(stmt.GetPin());

        if(!type.equals(Types.PIN))
            Reporter.Error(ReportTypes.NonPinTypeInIOStatementError, stmt);

        if(stmt.GetWriteVal() != null)
        {
            Object expr = visitValue.Visit(stmt.GetWriteVal().GetValue(), stmt.GetWriteVal());
            if(expr == null)
                Reporter.Error(ReportTypes.MissingExpressionInIOStmtError, stmt);
            else
            {
                Types exprType = (Types)expr;

                if(stmt.GetMode().equals(Modes.DIGITAL) && !exprType.equals(Types.DIGITAL))
                    Reporter.Error(ReportTypes.NonDigitalValueInDigitalIOStmtError, stmt);

                if(stmt.GetMode().equals(Modes.ANALOG) && !exprType.equals(Types.INT))
                    Reporter.Error(ReportTypes.NonIntValueInAnalogIOStmtError, stmt);
            }
        }
        
        return null;
    }

    public Object Visit(StructCompSelectExpr expr){
        Symbol symbol = currentScope.FindSymbol(expr.StructVarId);
    	if(symbol == null) {
            Reporter.Error(new UndeclaredSymbolException("struct: \"" + expr.StructVarId + "\" on line " + expr.GetLineNumber() + " " + expr.ComponentId));
    	}
    	if(symbol.dclType.equals(DclType.Struct))
    	{
    	    StructSymbol struct = (StructSymbol) GlobalScope.FindSymbol((String)symbol.GetType());
    	    if(struct==null)
                Reporter.Error(new UndeclaredSymbolException("struct: \"" + symbol.Name + "\" on line " + expr.GetLineNumber() + " trying " + symbol.GetType()));
            Symbol comp = struct.FindSymbol(expr.ComponentId);
            if(comp==null)
                Reporter.Error(new UndeclaredSymbolException("struct: \"" + expr.ComponentId + "\" on line " + expr.GetLineNumber() + " trying " + expr.ComponentId));
            if(comp.dclType.equals(DclType.Struct))
            {
                IScope old = currentScope;
                currentScope = struct;
                Object type = visitValue.Visit(expr.GetChildComp().GetValue(), expr.GetChildComp());
                currentScope = old;
                return type;
            }
            return comp.Type;
    	}
    	else if(symbol.dclType.equals(DclType.List))
    	{
    	    ListSymbol list = (ListSymbol) symbol;
    	    Symbol op = list.FindSymbol(expr.ComponentId);
    	    if(op==null)
    	        Reporter.Error(new UndeclaredSymbolException("list operation " + expr.ComponentId + " not defined on line " + expr.GetLineNumber()));
    	    ArrayList<ArgExpr> args = ((FuncCallExpr)expr.GetParent()).GetFuncArgs().GetArgs();
            ArrayList<FParamDiscriptor> funcSignature = op.GetTypeSignature();
            if(args.size() > funcSignature.size())
                Reporter.Error(new ArgumentsException("too many arguments in " + expr.ComponentId + " on line " + expr.GetLineNumber()));
            if(args.size() < funcSignature.size())
                Reporter.Error(new ArgumentsException("too few arguments in " + expr.ComponentId + " on line " + expr.GetLineNumber()));
            for(int i=0;i<args.size();++i)
            {
                if(!args.get(i).GetType().equals(funcSignature.get(i).GetType()))
                    Reporter.Error(new ArgumentsException("Argument not matching type signature in " + expr.ComponentId + " on line " + expr.GetLineNumber()));
            }
    	    return null;
        }
        else
        {
    	    return null;
        }

    }
    
    public Object Visit(IOExpr expr)
    {
        Types type = (Types) Visit(expr.GetPin());

        if(!type.equals(Types.PIN))
            Reporter.Error(ReportTypes.NonPinTypeInIOStatementError, expr);

        return type;
    }
    
    public Object Visit(IfStmt stmt)
    {
        Expr condition = stmt.GetCondition();

        if(!(condition instanceof BoolExpr) && !(condition instanceof UnaryExpr))
            Reporter.Error(ReportTypes.NonBooleanConditionError, stmt);

        VisitChildren(stmt);
        
        return null;
    }
    public Object Visit(ElseStmt stmt) {
        VisitChildren(stmt);
        return null;
    }

    public Object Visit(UntilStmt stmt)
    {
        Expr condition = stmt.GetCondition();

        if(!(condition instanceof BoolExpr))
            Reporter.Error(ReportTypes.NonBooleanConditionError, stmt);

        VisitChildren(stmt);
        
        return null;
    }

    public Object Visit(ForEachStmt stmt) {
        VisitChildren(stmt);
        Symbol smb = currentScope.FindSymbol(stmt.GetCollectionId());
        if(!smb.dclType.equals(DclType.List))
            Reporter.Error(ReportTypes.NonCollectionSubjectInForeachError, stmt);

        if(!stmt.GetElementType().equals(smb.Type))
            Reporter.Error(ReportTypes.IncompatibleElementTypeInForeachError, stmt);

        return null;
    }

    public Object Visit(ReturnStmt stmt)
    {
        Expr returnValue = stmt.GetReturnExpr();
        // Get the value after the return stmt
        Object returnedType = visitValue.Visit(returnValue.GetValue(), returnValue);
        FuncDcl func = (FuncDcl) stmt.GetParent().GetParent();
        // Get function return type
        Types returnType = func.GetVarDcl().Type;
        // The type returned by the return stmt must match the function return type
        if(!returnedType.equals(returnType))
            Reporter.Error(new IncompatibleValueException(returnedType,returnType));
        
        return null;
    }
    
    /* Checks symbol table for function identifier and checks correct usage of arguments */
    public Object Visit(FuncCallExpr expr)
    {
        Expr Id = expr.GetFuncId();
        if(Id instanceof StructCompSelectExpr)
            return visitValue.Visit(Id.GetValue(),Id);
        else {
            IdExpr funcId = (IdExpr)Id;
            Symbol identifier = FindSymbol(funcId.ID);
            // Check if function is declared before usage
            if (identifier == null)
                Reporter.Error(ReportTypes.IdentifierNotDeclaredError, expr);

            if (!identifier.dclType.equals(DclType.Function))
                Reporter.Error(ReportTypes.FuncCallAsFuncDclError, expr);

            // Checks that args are used declared before usage
            visitValue.Visit(expr.GetFuncArgs().GetValue(), expr.GetFuncArgs());
            ArrayList<ArgExpr> args = expr.GetFuncArgs().GetArgs();

            if (identifier.TypeSignature.size() > 0 || args.size() > 0) {

                /* Verify number of arguments match the function signature */
                int numFormalParameters = identifier.TypeSignature.size();
                if (numFormalParameters > args.size())
                    Reporter.Error(ReportTypes.TooFewArgumentsError, expr);
                else if (numFormalParameters < args.size())
                    Reporter.Error(ReportTypes.TooManyArgumentsError, expr);

                /* Verify the type of all arguments match the function signature */
                for (int i = 0; i < numFormalParameters; i++) {
                    ArgExpr argument = args.get(i);

                    Object argType = visitValue.Visit(argument.GetArg().GetValue(),argument.GetArg());
                    Types signatureType = identifier.TypeSignature.get(i).GetType();
                    if(signatureType.equals(Types.STRUCT)){
                        Symbol structType = currentScope.FindSymbol(argType.toString());
                        if(!structType.GetType().equals(identifier.TypeSignature.get(i).GetStructType())){
                            Reporter.Error(ReportTypes.IncompatibleTypeArgumentError, expr);
                        }
                    }
                    else if(signatureType.equals(Types.LIST)){
                        Symbol list = currentScope.FindSymbol(argType.toString());
                        if(!list.GetType().equals(identifier.TypeSignature.get(i).GetListofType())){
                            Reporter.Error(ReportTypes.IncompatibleTypeArgumentError, expr);
                        }
                    }
                    else if (!argType.equals(signatureType)) {
                        Reporter.Error(ReportTypes.IncompatibleTypeArgumentError, expr);
                    }
                }
            }
            return identifier.Type;
        }
    }
    public Object Visit(BinaryOPExpr expr)
    {
        Token operator = expr.Operation;

        AST left = expr.GetLeftExpr();
        AST right = expr.GetRightExpr();

        Object lType = visitValue.Visit(left.GetValue(),left);
        Object rType = visitValue.Visit(right.GetValue(),right);

        Object returnValue = null;

        switch (operator.Value){
            case "+":
                if ((lType.equals(Types.STRING) || rType.equals(Types.STRING))) {
                    if (lType.equals(rType)){
                        returnValue = lType;
                    } else {
                        Reporter.Error(ReportTypes.NonStringTypeInStringConcatError, expr);
                    } break;
                }
            case "-":
            case "/":
            case "*":
                if ((lType.equals(Types.INT) || lType.equals(Types.FLOAT)) &&
                    (rType.equals(Types.INT) || rType.equals(Types.FLOAT))){
                    returnValue = lType;
                } else {
                    Reporter.Error(ReportTypes.NonNumericTypesInBinaryOPExprError, expr);
                }
        }
        return returnValue;
    }
    public Object Visit(BoolExpr expr)
    {
        Token operator = expr.Operator;

        AST left = expr.GetLeftExpr();
        AST right = expr.GetRightExpr();

        Object lType = visitValue.Visit(left.GetValue(),left);
        Object rType = visitValue.Visit(right.GetValue(),right);

        Object returnValue = null;

        switch (operator.Value){
            case "or" :
            case "and":
                if (lType.equals(Types.BOOL) && lType.equals(rType)){
                    returnValue = lType;
                } else {
                    Reporter.Error(ReportTypes.NonBoolArgsInBoolExprError, expr);
                } break;
            case "below":
            case "above":
            case "below or equals":
            case "above or equals":
                if ((lType.equals(Types.INT) || lType.equals(Types.FLOAT)) &&
                    (rType.equals(Types.INT) || rType.equals(Types.FLOAT)) ){
                    returnValue = lType;
                } else {
                    Reporter.Error(ReportTypes.NonNumericArgsInBoolExprError, expr);
                } break;
            case "equals":
            case "not equals":
                if (lType.equals(rType)){
                    returnValue = Types.BOOL;
                } else {
                    Reporter.Error(ReportTypes.IncompatibleTypesInEqualityExprError, expr);
                } break;
        }
        return returnValue;
    }

    public Object Visit(RepeatStatement stmt){
        Expr iterations = stmt.GetIterationExpression();
        Object type = visitValue.Visit(iterations.GetValue(), iterations );
        if (!type.equals(Types.INT)){
            Reporter.Error(ReportTypes.NonIntegerIteratorInRepeatError, stmt);
        }
        return null;
    }
    public Object Visit(AssignStmt stmt)
    {
        AST left = stmt.GetLeft();
        AST right = stmt.GetRight();
        Object lType = visitValue.Visit(left.GetValue(),left);
        Object rType = visitValue.Visit(right.GetValue(),right);
        /* LHS must be a variable */
        if(!IsAssignable(left))
            Reporter.Error(ReportTypes.NotAssignableError, stmt);
        /* During assignments, pin types should be read as ints */
        if(lType.equals(Types.PIN))
            lType = Types.INT;
        if(rType.equals(Types.PIN))
            rType = Types.INT;
        /* Types should be identical except for implicit conversion between
         * floats and ints */
        if(!lType.equals(rType) && !(lType.equals(Types.FLOAT) && rType.equals(Types.INT)))
            Reporter.Error(new IncompatibleValueException(lType,rType,stmt.GetLineNumber()));
        return null;
    }

    public Object Visit(ListIndexExpr expr){
        return currentScope.FindSymbol(expr.Id.ID).Type;
    }


    public boolean IsAssignable(AST lhs)
    {
        // left hand side must be a variable
        return (lhs instanceof IdExpr ||
                lhs instanceof VarDcl ||
                lhs instanceof ListIndexExpr ||
                lhs instanceof StructCompSelectExpr);
    }
    
    public Object Visit(UnaryExpr expr)
    {
        Token operator = expr.GetOperator();
        Object argType = visitValue.Visit(expr.GetValExpr().GetValue(), expr.GetValExpr());
        Object returnValue = null;

        switch (operator.Value){
            case "-":
                if (argType.equals(Types.INT) || argType.equals(Types.FLOAT)){
                    returnValue = argType;
                } else {
                    Reporter.Error(ReportTypes.NonNumericTypeInNumericNegationError, expr);
                } break;
            case "not":
                if (argType.equals(Types.BOOL)){
                    returnValue = argType;
                } else {
                    Reporter.Error(ReportTypes.NonBoolTypeInBooleanNegationError, expr);
                } break;
        }
        return returnValue;
    }
    
    public Object Visit(ValExpr lit)
    {
        return TypeConverter.TypetoTypes(lit.LiteralValue);
    }

    public Object Visit(VarDcl node){
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
            Reporter.Error(ReportTypes.IdentifierNotDeclaredError, node);

        if(identifier.dclType.equals(DclType.Function))
            Reporter.Error(ReportTypes.FuncIdUsedAsVarIdError, node);
        if(identifier.dclType.equals(DclType.Struct) || identifier.dclType.equals(DclType.List))
            return identifier.Name;

        else
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
            visitValue.Visit(argID,arg.GetArg());
        }
        
        return null;
    }

    public Object Visit(FuncDcl node){
        if (currentScope.GetDepth() > 0){
            Reporter.Error(new InvalidScopeException(node.GetVarDcl().Identifier + ": Functions can only be declared in global scope."));
        }
        Visit(node.GetVarDcl());

        // setup function type signature, must be before scope else no recursion
        ArrayList<FParamDcl> parameters = node.GetFormalParamsDcl().GetFParams();
        ArrayList<FParamDiscriptor> typeSignature = new ArrayList<>();
        for(FParamDcl param : parameters) {
            if(param.Type.equals(Types.STRUCT)){
                typeSignature.add(new FParamDiscriptor(param.Type,param.GetStructType()));
            }
            else if(param.Type.equals(Types.LIST)){
                typeSignature.add(new FParamDiscriptor(param.Type,param.GetListType()));
            }
            else
                typeSignature.add(new FParamDiscriptor(param.Type));
        }
        Symbol funcDcl = FindSymbol(node.GetVarDcl().Identifier);
        funcDcl.SetTypeSignature(typeSignature);
        funcDcl.SetDclType(DclType.Function);

        if(!funcDcl.GetType().equals(Types.VOID)){
            Expr returnStmt = node.GetReturnExpr();
            if(returnStmt==null)
                Reporter.Error(new IncompatibleValueException("no return statement for function " + funcDcl.Name + " on line " + node.GetLineNumber()));
        }
        // Enter scope and visit func declaration children
        EnterScope(node);
        if(!node.GetVarDcl().Identifier.equals(node.GetEndIdentifier()))
            Reporter.Error(new InvalidIdentifierException("Invalid end Identifier in line " + node.GetLineNumber()));
        
        return null;
    }

    public Object Visit(StructDcl node){
        if (currentScope.GetDepth() > 0){

            Reporter.Error(new InvalidScopeException(
                    node.GetVarDcl().Identifier + ": Structs can only be declared in global scope." + " on line " + node.GetLineNumber()));
        }
        String varID  = node.GetVarDcl().Identifier;
        Types varType = node.GetVarDcl().GetType();
        StructSymbol symbol = new StructSymbol(varID, varType);
        currentScope.AddSymbol(symbol);
        IScope prevScope = currentScope;
        currentScope = symbol;
        VisitChildren(node);
        currentScope = prevScope;
        
        return null;
    }

    public void EnterScope(AST node){
        OpenScope();
        VisitChildren(node);
        CloseScope();
    }

    public void AddSymbol(String id){
        currentScope.AddSymbol(new Symbol(id));
    }
}
