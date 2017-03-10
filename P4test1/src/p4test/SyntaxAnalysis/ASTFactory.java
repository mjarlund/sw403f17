package p4test.SyntaxAnalysis;

import p4test.AbstractSyntaxTree.AST;
import p4test.AbstractSyntaxTree.Dcl.*;
import p4test.AbstractSyntaxTree.Expr.Expression;
import p4test.AbstractSyntaxTree.Expr.ValExpr;
import p4test.AbstractSyntaxTree.Stmt.Assignment;
import p4test.AbstractSyntaxTree.Stmt.Block;
import p4test.AbstractSyntaxTree.Stmt.Statement;
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
        BuildVarDCL, CombineAST, BuildFuncDcl, Combine, BuildValExpr, BuildAssign, BuildID, BuildBlock,
        BuildFormalParameters, BuildIfStmt, BuildBoolExpr
    }

    private SemanticActions actions;
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

    public void CreateAbstractTree()
    {
        switch (actions)
        {
            case BuildVarDCL:
                CreateDclTree();
            case CombineAST:
                CombineAST();
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
            case Combine:
                CombineAST();
        }
    }
    private void CombineAST()
    {
        AST subtree = astStack.pop();
        program.AdoptChildren(subtree);
    }
    private void CreateFormalParametersTree()
    {
        Token endPara = terminals.pop();
        ArrayList<FormalParameter> parameters = new ArrayList<>();
        while(!endPara.Value.equals("("))
        {
            if(!terminals.peek().Type.SEPARATOR.equals(TokenType.SEPARATOR))
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
        if(!astStack.peek().getClass().equals(Statement.class))
        {
            return;
        }
        else
        {
            while(astStack.peek().getClass().equals(Statement.class))
            {
                block.AdoptChildren(astStack.pop());
            }
        }
        astStack.push(block);
    }
    private void CreateFuncDclTree()
    {
        Block block = (Block) astStack.pop();
        FormalParameters parameters = (FormalParameters) astStack.pop();
        Token id = terminals.pop();
        Types returntype = GetType(terminals.pop());
        FuncDcl function = new FuncDcl(returntype, id.Value, parameters, block);
        astStack.push(function);
    }
    private void CreateAssignTree()
    {
        Declaration left = (Declaration) astStack.pop();
        Expression right = (Expression) astStack.pop();
        Assignment assign = new Assignment(left, right);
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
        }
        return null;
    }
}
