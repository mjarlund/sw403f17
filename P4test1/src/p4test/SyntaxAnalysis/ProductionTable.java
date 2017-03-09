package p4test.SyntaxAnalysis;

import p4test.Token;

import java.util.ArrayList;

public class ProductionTable
{
    // TODO: Should be optimized because there are many operations on strings

    public ArrayList<Symbol> GetProductions(Symbol.NonTerminal nonTerminalSymbol, Token token)
    {
        // https://www.youtube.com/watch?v=igLolIFXkvo
        // Translate token into a terminal symbol
        Symbol.Terminal terminalSymbol = GetTerminalSymbol(token);
        return GetRule(nonTerminalSymbol, terminalSymbol);
    }

    private Symbol.Terminal GetTerminalSymbol(Token token) {

        for (Symbol.Terminal s : Symbol.Terminal.values()) {
            if(s.value == token.Value) {
                return s;
            }
            else if(s.value == token.Type.name()) {
                return s;
            }
        }
        // No match
        return null;
    }

    public void initTable()
    {
        ConstructRule(
                Symbol.NonTerminal.Program,
                new Symbol.Terminal[] {Symbol.Terminal.text, Symbol.Terminal.number, Symbol.Terminal.fraction, Symbol.Terminal.character,
                        Symbol.Terminal.booleanType, Symbol.Terminal.voidType, Symbol.Terminal.struct, Symbol.Terminal.list, Symbol.Terminal.ifTerm,
                        Symbol.Terminal.identifier, Symbol.Terminal.until, Symbol.Terminal.foreach},
                new Symbol[] {Symbol.NonTerminal.Statement, Symbol.NonTerminal.Statements}
        );
        /*
        ConstructRule(Symbol.Program.ordinal(), 0, Symbol.Statement, Symbol.Statements);

        ConstructRule(Symbol.Statements.ordinal(), 0, Symbol.Statement, Symbol.Statements);
        ConstructRule(Symbol.Statements.ordinal(), 1, Symbol.epsilon);

        ConstructRule(Symbol.Statement.ordinal(), 0, Symbol.DeclarationStatement);
        ConstructRule(Symbol.Statement.ordinal(), 1, Symbol.ExpressionStatement);
        ConstructRule(Symbol.Statement.ordinal(), 2, Symbol.SelectionStatement);
        ConstructRule(Symbol.Statement.ordinal(), 3, Symbol.IterationStatement);

        ConstructRule(Symbol.DeclarationStatement.ordinal(), 0, Symbol.Type, Symbol.identifier, Symbol.DeclarationStatementPrime);
        ConstructRule(Symbol.DeclarationStatement.ordinal(), 1, Symbol.ListDeclarationStatement);

        ConstructRule(Symbol.DeclarationStatementPrime.ordinal(), 0, Symbol.FormalParameterList, Symbol.Block, Symbol.end, Symbol.identifier);

        ConstructRule(Symbol.ListDeclarationStatement.ordinal(), 0, Symbol.list, Symbol.of, Symbol.Type, Symbol.identifier, Symbol.is, Symbol.ParameterList);

        ConstructRule(Symbol.FormalParameter.ordinal(), 0, Symbol.Type, Symbol.identifier);

        ConstructRule(Symbol.FormalParameterList.ordinal(), 0, Symbol.parenthesisOpen, Symbol.FormalParameterListBody, Symbol.parenthesisClose);

        ConstructRule(Symbol.FormalParameterListBody.ordinal(), 0, Symbol.FormalParameter, Symbol.FormalParameterListBodyPrime);
        ConstructRule(Symbol.FormalParameterListBody.ordinal(), 1, Symbol.epsilon);

        ConstructRule(Symbol.FormalParameterListBodyPrime.ordinal(), 0, Symbol.comma, Symbol.FormalParameterListBody);
        ConstructRule(Symbol.FormalParameterListBodyPrime.ordinal(), 1, Symbol.epsilon);


        ConstructRule(Symbol.Expression.ordinal(), 0, Symbol.AssignmentExpressionPrime, Symbol.AssignmentExpression);

        ConstructRule(Symbol.AssignmentExpression.ordinal(), 0, Symbol.is, Symbol.AssignmentExpressionPrime, Symbol.AssignmentExpression);
        ConstructRule(Symbol.AssignmentExpression.ordinal(), 1, Symbol.epsilon);


        ConstructRule(Symbol.AssignmentExpressionPrime.ordinal(), 0, Symbol.NotExpressionPrime, Symbol.NotExpression);

        ConstructRule(Symbol.NotExpression.ordinal(), 0, Symbol.not, Symbol.NotExpressionPrime, Symbol.NotExpression);
        ConstructRule(Symbol.NotExpression.ordinal(), 1, Symbol.epsilon);

        ConstructRule(Symbol.NotExpressionPrime.ordinal(), 0, Symbol.OrExpressionPrime, Symbol.OrExpression);

        ConstructRule(Symbol.OrExpression.ordinal(), 0, Symbol.or, Symbol.OrExpressionPrime, Symbol.OrExpression);
        ConstructRule(Symbol.OrExpression.ordinal(), 1, Symbol.epsilon);

        ConstructRule(Symbol.OrExpressionPrime.ordinal(), 0, Symbol.AndExpressionPrime, Symbol.AndExpression);

        ConstructRule(Symbol.AndExpression.ordinal(), 0, Symbol.and, Symbol.AndExpressionPrime, Symbol.AndExpression);
        ConstructRule(Symbol.AndExpression.ordinal(), 1, Symbol.epsilon);

        ConstructRule(Symbol.AndExpressionPrime.ordinal(), 0, Symbol.EqualityExpressionPrime, Symbol.EqualityExpression);

        ConstructRule(Symbol.EqualityExpression.ordinal(), 0, Symbol.equals, Symbol.EqualityExpressionPrime, Symbol.EqualityExpression);
        ConstructRule(Symbol.EqualityExpression.ordinal(), 1, Symbol.epsilon);

        ConstructRule(Symbol.EqualityExpressionPrime.ordinal(), 0, Symbol.RelationalExpressionPrime, Symbol.RelationalExpression);

        ConstructRule(Symbol.RelationalExpression.ordinal(), 0, Symbol.below, Symbol.RelationalExpressionPrime, Symbol.RelationalExpression);
        ConstructRule(Symbol.RelationalExpression.ordinal(), 1, Symbol.above, Symbol.RelationalExpressionPrime, Symbol.RelationalExpression);
        ConstructRule(Symbol.RelationalExpression.ordinal(), 2, Symbol.epsilon);

        ConstructRule(Symbol.RelationalExpressionPrime.ordinal(), 0, Symbol.AdditiveExpressionPrime, Symbol.AdditiveExpression);

        ConstructRule(Symbol.AdditiveExpression.ordinal(), 0, Symbol.plus, Symbol.AdditiveExpressionPrime, Symbol.AdditiveExpression);
        ConstructRule(Symbol.AdditiveExpression.ordinal(), 1, Symbol.minus, Symbol.AdditiveExpressionPrime, Symbol.AdditiveExpression);
        ConstructRule(Symbol.AdditiveExpression.ordinal(), 2, Symbol.epsilon);

        ConstructRule(Symbol.AdditiveExpressionPrime.ordinal(), 0, Symbol.PrimaryExpression, Symbol.MultiplicativeExpression);

        ConstructRule(Symbol.MultiplicativeExpression.ordinal(), 0, Symbol.multiply, Symbol.PrimaryExpression, Symbol.MultiplicativeExpression);
        ConstructRule(Symbol.MultiplicativeExpression.ordinal(), 1, Symbol.divide, Symbol.PrimaryExpression, Symbol.MultiplicativeExpression);
        ConstructRule(Symbol.MultiplicativeExpression.ordinal(), 2, Symbol.epsilon);

        ConstructRule(Symbol.PrimaryExpression.ordinal(), 0, Symbol.identifier, Symbol.IdentifierAppendantOptional);
        ConstructRule(Symbol.PrimaryExpression.ordinal(), 1, Symbol.integerLiteral);
        ConstructRule(Symbol.PrimaryExpression.ordinal(), 2, Symbol.floatingPointLiteral);
        ConstructRule(Symbol.PrimaryExpression.ordinal(), 3, Symbol.stringLiteral);
        ConstructRule(Symbol.PrimaryExpression.ordinal(), 4, Symbol.booleanLiteral);
        ConstructRule(Symbol.PrimaryExpression.ordinal(), 5, Symbol.parenthesisOpen, Symbol.Expression, Symbol.parenthesisClose);

        ConstructRule(Symbol.ExpressionStatement.ordinal(), 0, Symbol.identifier, Symbol.ExpressionStatementPrime);

        ConstructRule(Symbol.ExpressionStatementPrime.ordinal(), 0, Symbol.ExpressionStatementPrimePrime, Symbol.is, Symbol.Expression);
        ConstructRule(Symbol.ExpressionStatementPrime.ordinal(), 1, Symbol.ParameterList);

        ConstructRule(Symbol.ExpressionStatementPrimePrime.ordinal(), 0, Symbol.dotIdentfier);
        ConstructRule(Symbol.ExpressionStatementPrimePrime.ordinal(), 1, Symbol.epsilon);

        ConstructRule(Symbol.Parameter.ordinal(), 0, Symbol.identifier);
        ConstructRule(Symbol.Parameter.ordinal(), 1, Symbol.integerLiteral);
        ConstructRule(Symbol.Parameter.ordinal(), 2, Symbol.floatingPointLiteral);
        ConstructRule(Symbol.Parameter.ordinal(), 3, Symbol.stringLiteral);
        ConstructRule(Symbol.Parameter.ordinal(), 4, Symbol.booleanLiteral);

        ConstructRule(Symbol.IdentifierAppendantOptional.ordinal(), 0, Symbol.ParameterList);
        ConstructRule(Symbol.IdentifierAppendantOptional.ordinal(), 1, Symbol.squareBracketOpen, Symbol.integerLiteral, Symbol.squareBracketClose);
        ConstructRule(Symbol.IdentifierAppendantOptional.ordinal(), 2, Symbol.epsilon);

        ConstructRule(Symbol.ParameterList.ordinal(), 0, Symbol.parenthesisOpen, Symbol.ParameterListBody, Symbol.parenthesisClose);

        ConstructRule(Symbol.ParameterListBody.ordinal(), 0, Symbol.Parameter, Symbol.ParameterListBodyPrime);
        ConstructRule(Symbol.ParameterListBody.ordinal(), 1, Symbol.epsilon);

        ConstructRule(Symbol.ParameterListBodyPrime.ordinal(), 0, Symbol.comma, Symbol.ParameterListBody);
        ConstructRule(Symbol.ParameterListBodyPrime.ordinal(), 1, Symbol.epsilon);

        ConstructRule(Symbol.SelectionStatement.ordinal(), 0, Symbol.ifTerm, Symbol.Condition, Symbol.Block, Symbol.SelectionStatementPrime);

        ConstructRule(Symbol.SelectionStatementPrime.ordinal(), 0, Symbol.elseTerm, Symbol.Block, Symbol.end, Symbol.elseTerm, Symbol.ifTerm);
        ConstructRule(Symbol.SelectionStatementPrime.ordinal(), 1, Symbol.end, Symbol.ifTerm);

        ConstructRule(Symbol.IterationStatement.ordinal(), 0, Symbol.until, Symbol.Condition, Symbol.Block, Symbol.end, Symbol.until);
        ConstructRule(Symbol.IterationStatement.ordinal(), 1, Symbol.foreach, Symbol.parenthesisOpen, Symbol.FormalParameter, Symbol.in, Symbol.Parameter, Symbol.parenthesisClose, Symbol.Block, Symbol.end, Symbol.foreach);

        ConstructRule(Symbol.Condition.ordinal(), 0, Symbol.parenthesisOpen, Symbol.Expression, Symbol.parenthesisClose);

        ConstructRule(Symbol.Block.ordinal(), 0, Symbol.Statements);

        ConstructRule(Symbol.Type.ordinal(), 0, Symbol.text);
        ConstructRule(Symbol.Type.ordinal(), 1, Symbol.number);
        ConstructRule(Symbol.Type.ordinal(), 2, Symbol.fraction);
        ConstructRule(Symbol.Type.ordinal(), 3, Symbol.character);
        ConstructRule(Symbol.Type.ordinal(), 4, Symbol.booleanType);
        ConstructRule(Symbol.Type.ordinal(), 5, Symbol.voidType);
        ConstructRule(Symbol.Type.ordinal(), 6, Symbol.struct);


        ConstructRule(Symbol.StructSpecifier.ordinal(), 0, Symbol.struct, Symbol.identifier, Symbol.StructSpecifierPrime);

        ConstructRule(Symbol.StructSpecifierPrime.ordinal(), 0, Symbol.StructDeclaration, Symbol.end, Symbol.identifier);

        ConstructRule(Symbol.StructSpecifierPrime.ordinal(), 0, Symbol.epsilon);

        ConstructRule(Symbol.StructDeclaration.ordinal(), 0, Symbol.DeclarationStatement, Symbol.StructDeclaration);

        ConstructRule(Symbol.StructDeclaration.ordinal(), 0, Symbol.epsilon);
        */
    }


    // Construct rule for non-terminal (x), applied at the terminals (y) containing the symbols (z)
    private void ConstructRule(Symbol.NonTerminal nonTerminal, Symbol.Terminal[] terminals, Symbol ... symbols) {
        int[] termTable = new int[terminals.length];
        for (int i = 0; i < terminals.length; i++) termTable[i] = terminals[i].ordinal();
        ConstructRule(nonTerminal.ordinal(), termTable, symbols);
    }

    private void ConstructRule(int nonterminal, int[] terminals, Symbol ... symbols) {
        for(Symbol s : symbols) {
            rules[ruleCount].add(s);
        }

        for(int terminal : terminals) {
            ParsingTable[nonterminal][terminal] = ruleCount;
        }

        ruleCount++;
    }

    private ArrayList<Symbol> GetRule(Symbol.NonTerminal nonTerminal, Symbol.Terminal terminal) {
        int ruleIndex = ParsingTable[nonTerminal.ordinal()][terminal.ordinal()];
        return rules[ruleIndex];
    }

    private int ParsingTable[][] = new int[Symbol.NonTerminal.values().length][Symbol.Terminal.values().length];

    // This will keep track of the number of rules constructed.
    private int ruleCount = 0;
    private ArrayList<Symbol> rules[] = new ArrayList[200];
}
