package p4test.SyntaxAnalysis;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParameterList;
import jdk.nashorn.internal.ir.ExpressionStatement;

import java.util.concurrent.locks.Condition;

// Terminals start with lowercase and non-terminals uppercase
public enum RuleType
{
    // Terminator rules
    EPSILON,
    EOF,
    EOL,

    // Start rule
    Program,

    // Statement rules
    Statement,
    Statements,
    DeclarationStatement,
    ExpressionStatement,
    SelectionStatement,
    ItterationStatement,

    identifier,
    end,
    is,
    of,
    below,
    above,

    // SelectionStatement specific terminals
    ifLitteral,
    elseLitteral,

    // Litteral values
    integerLiteral,
    floatingPointLiteral,
    stringLiteral,
    booleanLitteral,

    // Types
    voidLitteral,
    text,
    number,
    fraction,
    character,
    booleanType,
    struct,

    // Misc
    bracketOpen,
    bracketClose,
    squareBracketOpen,
    squareBracketClose,

    until,
    foreach,
    in,

    dot,


    add,
    sub,
    div,
    mul,

    Type,
    DeclarationStatementPrime,
    ListDeclarationStatement,
    FormalParameterList,
    Block,
    FormalParameterListBody,

    FormalParameterListBodyPrime,

    Expression,


    AssignmentExpression,
    AssignmentExpressionPrime,
    NotExpression,
    NotExpressionPrime,

    OrExpression,
    OrExpressionPrime,

    AndExpression,
    AndExpressionPrime,

    EqualityExpression,
    EqualityExpressionPrime,

    RelationalExpression,
    RelationalExpressionPrime,

    AdditiveExpressionPrime,
    AdditiveExpression,


    MultiplicativeExpression,


    PrimaryExpression,



    IdentifierAppendantOptional,


    ExpressionStatementPrime,
    ExpressionStatementPrimePrime,

    Parameter,
    ParameterList,
    ParameterListBody,
    ParameterListBodyPrime,

    Condition,
    SelectionStatementPrime,
    StructSpecifier,

    StructSpecifierPrime,
    StructDeclaration,

}
