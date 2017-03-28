package DataStructures.AST;

import DataStructures.AST.NodeTypes.Declarations.*;
import DataStructures.AST.NodeTypes.Expressions.*;
import DataStructures.AST.NodeTypes.Statements.*;
import DataStructures.AST.NodeTypes.Types;
import DataStructures.DefaultHashMap;
import Syntax.Tokens.Token;
import Syntax.Tokens.TokenType;
import Utilities.TypeConverter;
import jdk.nashorn.internal.ir.Block;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class ASTFactory
{
    public DefaultHashMap<String, Runnable> SemanticAction = new DefaultHashMap<String, Runnable>(null);
    private Stack<AST> astStack;
    private Stack<Token> terminals;
    private int currentLineNumber;

    public AST program;


    public ASTFactory(Stack<Token> terminals)
    {
        this.astStack = new Stack<AST>();
        this.terminals = terminals;
        program = new AST();
    }
    public void initFactory()
    {
        SemanticAction.put("BuildVarDCL", ASTFactory.this::CreateDclTree);
        SemanticAction.put("CombineDown", ASTFactory.this::CombineDown);
        SemanticAction.put("BuildAssign", ASTFactory.this::CreateAssignTree);
        SemanticAction.put("BuildValExpr", ASTFactory.this::CreateValExprTree);
        SemanticAction.put("BuildIdentifier", ASTFactory.this::CreateIdentifierTree);
        SemanticAction.put("BuildBinaryExpr", ASTFactory.this::CreateBinaryExpr);
        SemanticAction.put("BuildBoolExpr", ASTFactory.this::CreateBoolExpr);
        SemanticAction.put("BuildBlock", ASTFactory.this::CreateBlockTree);
        SemanticAction.put("BuildFuncDcl", ASTFactory.this::CreateFuncDclTree);
        SemanticAction.put("BuildFormalParams", ASTFactory.this::CreateFormalParametersTree);
        SemanticAction.put("BuildIfStmt", ASTFactory.this::CreateIfStmt);
        SemanticAction.put("BuildUntilStmt", ASTFactory.this::CreateUntilStmtTree);
        SemanticAction.put("BuildElseStmt", ASTFactory.this::CreateElse);
        SemanticAction.put("BuildActualParams", ASTFactory.this::CreateActualParameters);
        SemanticAction.put("BuildFuncCall", ASTFactory.this::CreateFuncCall);
        SemanticAction.put("BuildReturnStmt", ASTFactory.this::CreateReturnStmt);
        SemanticAction.put("BuildUnaryExpr", ASTFactory.this::CreateUnaryExpr);
        SemanticAction.put("BuildListDeclaration", ASTFactory.this::CreateListDcl);
        SemanticAction.put("BuildStructDeclaration", ASTFactory.this::CreateStructDcl);
    }
    public void CreateAbstractTree(String action, int lineNumber)
    {
        Runnable method = SemanticAction.get(action);
        if (method!=null)
        {
            method.run();
            currentLineNumber = lineNumber;
        }
        else
            throw new Error("Semantic action not found please come again (indian accent)");
    }
    /* Combines the top of the tree stack with the one below it */
    private void CombineDown()
    {
        AST subtree = astStack.pop();
        if (astStack.size() > 1)
        {
            subtree.SetParent(astStack.peek());
            astStack.peek().children.add(subtree);
        }
        else
        {
            subtree.SetParent(program);
            program.children.add(subtree);
        }
    }
    private void CreateUnaryExpr()
    {
        Expr val = (Expr)astStack.pop();
        Token op = terminals.pop();
        UnaryExpr expr = new UnaryExpr(op,val);
        expr.SetLineNumber(currentLineNumber);
        astStack.push(expr);
    }
    private void CreateReturnStmt()
    {
        Expr returnval = (Expr)astStack.pop();
        ReturnStmt stmt = new ReturnStmt(returnval);
        stmt.SetLineNumber(currentLineNumber);
        astStack.push(stmt);
    }
    private void CreateUntilStmtTree()
    {
        BlockStmt block = (BlockStmt) astStack.pop();
        Expr condition = (Expr) astStack.pop();
        // remove 'until' terminal
        terminals.pop();
        UntilStmt untilStmt = new UntilStmt(condition,block);
        untilStmt.SetLineNumber(currentLineNumber);
        astStack.push(untilStmt);
    }
    private void CreateFuncCall()
    {
        ArgsExpr args = (ArgsExpr) astStack.pop();
        IdExpr id = (IdExpr) astStack.pop();
        FuncCallExpr funcCall = new FuncCallExpr(id,args);
        funcCall.SetLineNumber(currentLineNumber);
        astStack.push(funcCall);
    }
    private void CreateActualParameters()
    {
        Token endPara = terminals.pop();
        ArrayList<ArgExpr> parameters = new ArrayList<>();
        while (! endPara.Value.equals("("))
        {
            if (! terminals.peek().Type.equals(TokenType.SEPARATOR))
            {
                Token id = terminals.pop();
                ArgExpr arg;
                if (id.Type.equals(TokenType.IDENTIFIER)){
                    IdExpr expr = new IdExpr(id.Value);
                    expr.SetLineNumber(currentLineNumber);
                    arg = new ArgExpr(expr);
                    arg.SetValue(id.Value);
                } else {
                    ValExpr expr = new ValExpr(TypeConverter.TypetoTypes(id),id);
                    expr.SetLineNumber(currentLineNumber);
                    arg = new ArgExpr(expr);
                }

                parameters.add(arg);
            }
            endPara = terminals.pop();
        }
        ArgsExpr astParameters = new ArgsExpr();
        for (ArgExpr parameter : parameters)
        {
            astParameters.children.add(parameter);
        }
        astStack.push(astParameters);
    }
    private void CreateBinaryExpr()
    {
        Expr right = (Expr) astStack.pop();
        Expr left = (Expr)astStack.pop();
        Token op = terminals.pop();
        Expr expr = new BinaryOPExpr(left, op, right);
        expr.SetLineNumber(currentLineNumber);
        astStack.push(expr);
    }
    private void CreateIfStmt()
    {
        BlockStmt block = (BlockStmt) astStack.pop();
        Expr condition = (Expr) astStack.pop();
        condition.SetLineNumber(currentLineNumber);
        // remove 'if' terminal
        //terminals.pop();
        IfStmt ifstmt = new IfStmt(condition,block);
        ifstmt.SetLineNumber(currentLineNumber);
        astStack.push(ifstmt);
    }
    private void CreateElse()
    {
        BlockStmt elseBlock = (BlockStmt) astStack.pop();
        ElseStmt elseStmt = new ElseStmt(elseBlock);
        elseStmt.SetLineNumber(currentLineNumber);
        astStack.push(elseStmt);
    }
    private void CreateBoolExpr()
    {
        Expr right = (Expr)astStack.pop();
        Expr left = (Expr)astStack.pop();
        if(terminals.peek().equals(")")) // remove ')'
            terminals.pop();
        // get operand
        Token op = terminals.pop();
        if(terminals.peek().equals(")")) // remove '('
            terminals.pop();
        BoolExpr expr = new BoolExpr(left, op, right);
        expr.SetLineNumber(currentLineNumber);
        astStack.push(expr);
    }
    // expect terminalStack to start with ')' and end with '(' in between are the parameters
    private void CreateFormalParametersTree()
    {
        Token endPara = terminals.pop();
        ArrayList<FParamDcl> parameters = new ArrayList<>();
        while (! endPara.Value.equals("("))
        {
            if (!terminals.peek().Type.equals(TokenType.SEPARATOR))
            {
                Token id = terminals.pop();
                Token type = terminals.pop();
                FParamDcl paramDcl = new FParamDcl(GetType(type), id.Value);
                paramDcl.SetLineNumber(currentLineNumber);
                parameters.add(paramDcl);
            }
            endPara = terminals.pop();
        }
        FParamsDcl astParameters = new FParamsDcl();
        for (FParamDcl parameter : parameters)
        {
            /* TODO: We should only use one of these. */
            astParameters.AdoptChildren(parameter);
            astParameters.children.add(parameter);
        }
        astStack.push(astParameters);
    }
    private void CreateBlockTree()
    {
        BlockStmt block = new BlockStmt();
        block.SetValue("BlockStmt");
        astStack.push(block);
    }
    private void CreateFuncDclTree()
    {
        BlockStmt block = (BlockStmt) astStack.pop();
        FParamsDcl parameters = (FParamsDcl) astStack.pop();
        parameters.SetLineNumber(currentLineNumber);
        VarDcl dcl = (VarDcl) astStack.pop();
        dcl.SetLineNumber(currentLineNumber);

        FuncDcl function = new FuncDcl(dcl,parameters,block);
        function.SetLineNumber(currentLineNumber);
        String endId = terminals.pop().Value;
        // remove end keyword
        terminals.pop();
        function.SetEndIdentifier(endId);
        astStack.push(function);
    }
    // Expecting an expr and identifier or dcl on astStack and an is terminal on terminal stack
    private void CreateAssignTree()
    {
        // expr is top of astStack at this point
        Expr right = (Expr) astStack.pop();
        // pop 'is' terminal from terminal stack
        terminals.pop();
        // Either a VarDCL or an identifier
        AssignStmt assign;

        if (astStack.peek() instanceof Dcl){
            assign = new AssignStmt((VarDcl) astStack.pop(), right);
        } else {
            assign = new AssignStmt((Expr) astStack.pop(), right);
        }
        assign.SetLineNumber(currentLineNumber);
        astStack.push(assign);
    }
    private void CreateIdentifierTree()
    {
        Token id = terminals.pop();
        IdExpr identifier = new IdExpr(id.Value);
        identifier.SetLineNumber(currentLineNumber);
        astStack.push(identifier);
    }
    private void CreateValExprTree()
    {
        Token value = terminals.pop();
        ValExpr val = new ValExpr(TypeConverter.TypetoTypes(value),value);
        val.SetLineNumber(currentLineNumber);
        astStack.push(val);
    }
    private void CreateDclTree()
    {
        String id = terminals.pop().Value;
        Types type = GetType(terminals.pop());
        VarDcl dcl = new VarDcl(type, id);
        dcl.SetLineNumber(currentLineNumber);
        astStack.push(dcl);
    }

    private void CreateListDcl(){
        AST listContents;
        ListDcl listDcl;

        /* BuildActualParameters put the contents on the astStack */
        listContents = astStack.pop();

        terminals.pop(); /* is */

        /* BuildVarDcl put the declaration on the astStack */
        AST dcl = astStack.pop();
        listDcl = new ListDcl((VarDcl) dcl, (ArgsExpr) listContents);
        listDcl.SetValue("ListDcl");

        astStack.push(listDcl);
    }

    private void CreateStructDcl(){
        /* astStack contains: VarDcl and BlockStmt */
        BlockStmt block = (BlockStmt) astStack.pop();
        VarDcl dcl = (VarDcl) astStack.pop();

        StructDcl struct = new StructDcl(dcl, block);
        struct.SetValue("StructDcl");
        astStack.push(struct);
    }

    private Types GetType(Token token)
    {
        switch (token.Value)
        {
            case "number":
                return Types.INT;
            case "boolean":
                return Types.BOOL;
            case "fraction":
                return Types.FLOAT;
            case "void":
                return Types.VOID;
            case "character":
                return Types.CHAR;
            case "text":
                return Types.STRING;
        }
        return null;
    }
}