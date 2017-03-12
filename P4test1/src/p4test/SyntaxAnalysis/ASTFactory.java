package p4test.SyntaxAnalysis;

import com.sun.deploy.security.BlacklistedCerts;
import p4test.AbstractSyntaxTree.AST;
import p4test.AbstractSyntaxTree.Dcl.*;
import p4test.AbstractSyntaxTree.Expr.*;
import p4test.AbstractSyntaxTree.Stmt.*;
import p4test.AbstractSyntaxTree.Types;
import p4test.Token;
import p4test.TokenType;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by mysjkin on 3/7/17.
 */
public class ASTFactory
{
    private enum SemanticActions
    {
        BuildVarDCL, BuildFuncDcl, CombineUp, CombineDown, BuildValExpr, BuildAssign,
        BuildID, BuildBlock, BuildFormalParameters, BuildIfStmt, BuildBoolExpr, ClearIfTerminals,
        BuildBinaryExpr, BuildFuncCall, BuildActualParameters
    }

    private Stack<RuleType> semanticStack;
    private Stack<AST> astStack;
    private Stack<Token> terminals;

    private AST program;


    public ASTFactory(Stack<RuleType> semtanticStack, Stack<Token> terminals)
    {
        this.semanticStack = semtanticStack;
        this.astStack = new Stack<AST>();
        this.terminals = terminals;
        program = new AST();
    }

    public void CreateAbstractTree(SemanticActions action)
    {
        switch (action)
        {
            case BuildVarDCL:
                CreateDclTree();
            case BuildFuncDcl:
                CreateFuncDclTree();
            case BuildBlock:
                CreateBlockTree();
            case BuildFormalParameters:
                CreateFormalParametersTree();
            case BuildAssign:
                CreateAssignTree();
            case BuildID:
                CreateIdentifierTree();
            case BuildValExpr:
                CreateValExprTree();
            case CombineUp:
                CombineAST(action);
            case CombineDown:
                CombineAST(action);
            case ClearIfTerminals:
                // remove 'end' 'if'
                terminals.pop(); terminals.pop();
            case BuildIfStmt:
                CreateIfStmt();
            case BuildBoolExpr:
                CreateBoolExpr();
            case BuildBinaryExpr:
                CreateBinaryExpr();
            case BuildFuncCall:
                CreateFuncCall();
            case BuildActualParameters:
                CreateActualParameters();
        }
    }
    private void CombineAST(SemanticActions action)
    {
        if (astStack.size() > 1)
        {
            if(action.equals(SemanticActions.CombineUp))
            {
                AST parent = astStack.pop();
                AST subtree = astStack.pop();
                parent.AdoptChildren(subtree);
                astStack.push(parent);
            }
            else
            {
                AST subtree = astStack.pop();
                astStack.peek().AdoptChildren(subtree);
            }
        }
        else
        {
            AST subtree = astStack.pop();
            program.AdoptChildren(subtree);
        }
    }
    private void CreateFuncCall()
    {
        FuncCall funcid = new FuncCall(new Identifier(terminals.pop().Value));
    }
    private void CreateActualParameters()
    {
        Token endPara = terminals.pop();
        ArrayList<Argument> parameters = new ArrayList<>();
        while (! endPara.Value.equals("("))
        {
            if (! terminals.peek().Type.SEPARATOR.equals(TokenType.SEPARATOR))
            {
                Token id = terminals.pop();
                endPara = terminals.pop();
                parameters.add(new Argument(id));
            }
        }
        Arguments astParameters = new Arguments();
        for (Argument parameter : parameters)
        {
            astParameters.AdoptChildren(parameter);
        }
        astStack.push(astParameters);
    }
    private void CreateBinaryExpr()
    {
        Expression right = (Expression) astStack.pop();
        Expression left = (Expression)astStack.pop();
        Token op = terminals.pop();
        BinaryOP binaryOP = new BinaryOP(left,op,right);
        astStack.push(binaryOP);
    }
    private void CreateIfStmt()
    {
        Block block = (Block) astStack.pop();
        BoolExpr condition = (BoolExpr) astStack.pop();
        // remove 'if' terminal
        terminals.pop();
        IfStmt ifstmt = new IfStmt(condition,block);
        astStack.push(ifstmt);
    }
    private void CreateElse()
    {
        Block elseBlock = (Block) astStack.pop();
        // pop 'end' 'else' 'if'
        terminals.pop();
        terminals.pop();
        terminals.pop();
        ElseStmt elseStmt = new ElseStmt(elseBlock);
        astStack.push(elseStmt);
    }
    private void CreateBoolExpr()
    {
        Expression right = (Expression)astStack.pop();
        Expression left = (Expression)astStack.pop();
        // remove ')'
        terminals.pop();
        // get operand
        Token op = terminals.pop();
        // remove '('
        terminals.pop();
        BoolExpr expr = new BoolExpr(left, op, right);
        astStack.push(expr);
    }
    // expect terminalStack to start with ')' and end with '(' in between are the parameters
    private void CreateFormalParametersTree()
    {
        Token endPara = terminals.pop();
        ArrayList<FormalParameter> parameters = new ArrayList<>();
        while (! endPara.Value.equals("("))
        {
            if (! terminals.peek().Type.SEPARATOR.equals(TokenType.SEPARATOR))
            {
                Token id = terminals.pop();
                Token type = terminals.pop();
                endPara = terminals.pop();
                parameters.add(new FormalParameter(GetType(type),id.Value));
            }
        }
        FormalParameters astParameters = new FormalParameters();
        for (FormalParameter parameter : parameters)
        {
            astParameters.AdoptChildren(parameter);
        }
        astStack.push(astParameters);
    }
    private void CreateBlockTree()
    {
        Block block = new Block();
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
        Assignment assign = astStack.peek().getClass().equals(Declaration.class) ?
                new Assignment((VarDcl) astStack.pop(), right) :
                new Assignment((Identifier) astStack.pop(), right);
        astStack.push(assign);
    }
    private void CreateIdentifierTree()
    {
        Token id = terminals.pop();
        Identifier identifier = new Identifier(id.Value);
        astStack.push(identifier);
    }
    private void CreateValExprTree()
    {
        Token value = terminals.pop();
        ValExpr val = new ValExpr(value);
        astStack.push(val);
    }
    private void CreateDclTree()
    {
        String id = terminals.pop().Value;
        Types type = GetType(terminals.pop());
        astStack.push(new VarDcl(type, id));
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
        }
        return null;
    }
}
