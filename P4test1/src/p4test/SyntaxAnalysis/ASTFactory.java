package p4test.SyntaxAnalysis;

import p4test.AbstractSyntaxTree.AST;
import p4test.AbstractSyntaxTree.Dcl.*;
import p4test.AbstractSyntaxTree.Expr.*;
import p4test.AbstractSyntaxTree.Stmt.*;
import p4test.AbstractSyntaxTree.Types;
import p4test.DefaultHashMap;
import p4test.Token;
import p4test.TokenType;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by mysjkin on 3/7/17.
 */
public class ASTFactory
{
    public DefaultHashMap<String, Runnable> SemanticAction = new DefaultHashMap<String, Runnable>(null);
    private Stack<AST> astStack;
    private Stack<Token> terminals;

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
    }
    public void CreateAbstractTree(String action)
    {
        Runnable method = SemanticAction.get(action);
        if (method!=null)method.run();
        else
            throw new Error("Semantic action not found please come again (indian accent)");
    }
    /* Combines the top of the tree stack with the one below it */
    private void CombineDown()
    {
        if (astStack.size() > 1)
        {
            AST subtree = astStack.pop();
            astStack.peek().children.add(subtree);
        }
        else
        {
            AST subtree = astStack.pop();
            program.children.add(subtree);
        }
    }
    private void CreateUntilStmtTree()
    {
        Block block = (Block) astStack.pop();
        BoolExpr condition = (BoolExpr) astStack.pop();
        // remove 'until' terminal
        terminals.pop();
        UntilStmt untilStmt = new UntilStmt(condition,block);
        untilStmt.SetValue("Until");
        astStack.push(untilStmt);
    }
    private void CreateFuncCall()
    {
        Arguments args = (Arguments) astStack.pop();
        Identifier id = (Identifier) astStack.pop();
        FuncCall funcCall = new FuncCall(id,args);
        funcCall.SetValue("FuncCall");
        astStack.push(funcCall);
    }
    private void CreateActualParameters()
    {
        Token endPara = terminals.pop();
        ArrayList<Argument> parameters = new ArrayList<>();
        while (! endPara.Value.equals("("))
        {
            if (! terminals.peek().Type.equals(TokenType.SEPARATOR))
            {
                Token id = terminals.pop();
                Argument arg;
                if (id.Type.equals(TokenType.IDENTIFIER)){
                    arg = new Argument(new Identifier(id.Value));
                    arg.SetValue(id.Value);
                } else
                    arg = new Argument(new ValExpr(id));

                parameters.add(arg);
            }
            endPara = terminals.pop();
        }
        Arguments astParameters = new Arguments();
        for (Argument parameter : parameters)
        {
            System.out.println("asdassdas " + parameter);
            astParameters.children.add(parameter);
        }
        astParameters.SetValue("AParams");
        astStack.push(astParameters);
    }
    private void CreateBinaryExpr()
    {
        Expression right = (Expression) astStack.pop();
        Expression left = (Expression)astStack.pop();
        Token op = terminals.pop();
        BinaryOP binaryOP = new BinaryOP(left,op,right);
        binaryOP.SetValue("BinaryOP");
        astStack.push(binaryOP);
    }
    private void CreateIfStmt()
    {
        Block block = (Block) astStack.pop();
        BoolExpr condition = (BoolExpr) astStack.pop();
        // remove 'if' terminal
        //terminals.pop();
        IfStmt ifstmt = new IfStmt(condition,block);
        ifstmt.SetValue("If");
        astStack.push(ifstmt);
    }
    private void CreateElse()
    {
        Block elseBlock = (Block) astStack.pop();
        ElseStmt elseStmt = new ElseStmt(elseBlock);
        elseStmt.SetValue("Else");
        astStack.push(elseStmt);
    }
    private void CreateBoolExpr()
    {
        Expression right = (Expression)astStack.pop();
        Expression left = (Expression)astStack.pop();
        if(terminals.peek().equals(")")) // remove ')'
            terminals.pop();
        // get operand
        Token op = terminals.pop();
        if(terminals.peek().equals(")")) // remove '('
            terminals.pop();
        BoolExpr expr = new BoolExpr(left, op, right);
        expr.SetValue("BoolExpr");
        astStack.push(expr);
    }
    // expect terminalStack to start with ')' and end with '(' in between are the parameters
    private void CreateFormalParametersTree()
    {
        Token endPara = terminals.pop();
        ArrayList<FormalParameter> parameters = new ArrayList<>();
        while (! endPara.Value.equals("("))
        {
            if (!terminals.peek().Type.SEPARATOR.equals(TokenType.SEPARATOR))
            {
                Token id = terminals.pop();
                Token type = terminals.pop();
                parameters.add(new FormalParameter(GetType(type),id.Value));
            }
            endPara = terminals.pop();
        }
        FormalParameters astParameters = new FormalParameters();
        for (FormalParameter parameter : parameters)
        {
            astParameters.AdoptChildren(parameter);
        }
        astParameters.SetValue("FParams");
        astStack.push(astParameters);
    }
    private void CreateBlockTree()
    {
        Block block = new Block();
        block.SetValue("Block");
        astStack.push(block);
    }
    private void CreateFuncDclTree()
    {
        Block block = (Block) astStack.pop();
        FormalParameters parameters = (FormalParameters) astStack.pop();
        VarDcl dcl = (VarDcl) astStack.pop();

        // remove 'end' 'id' from terminal stack
        terminals.pop();
        terminals.pop();

        FuncDcl function = new FuncDcl(dcl,parameters,block);
        function.SetValue("FuncDcl");
        astStack.push(function);
    }
    // Expecting an expr and identifier or dcl on astStack and an is terminal on terminal stack
    private void CreateAssignTree()
    {
        // expr is top of astStack at this point
        Expression right = (Expression) astStack.pop();
        // pop 'is' terminal from terminal stack
        terminals.pop();
        // Either a VarDCL or an identifier
        Assignment assign;

        if (astStack.peek() instanceof Declaration){
            assign = new Assignment((VarDcl) astStack.pop(), right);
            assign.SetValue("Assign");
        } else {
            assign = new Assignment((Expression) astStack.pop(), right);
            assign.SetValue("Assign");
        }

        astStack.push(assign);
    }
    private void CreateIdentifierTree()
    {
        Token id = terminals.pop();
        Identifier identifier = new Identifier(id.Value);
        identifier.SetValue("Id");
        astStack.push(identifier);
    }
    private void CreateValExprTree()
    {
        Token value = terminals.pop();
        ValExpr val = new ValExpr(value);
        val.SetValue("ValExpr");
        astStack.push(val);
    }
    private void CreateDclTree()
    {
        String id = terminals.pop().Value;
        Types type = GetType(terminals.pop());
        VarDcl dcl = new VarDcl(type, id);
        dcl.SetValue("VarDcl");
        astStack.push(dcl);
    }

    private Types GetType(Token token)
    {
        switch (token.Value)
        {
            case "number":
                return Types.INT;
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
