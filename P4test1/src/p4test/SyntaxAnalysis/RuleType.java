package p4test.SyntaxAnalysis;

// Terminals start with lowercase and non-terminals uppercase
public enum RuleType
{
    // Terminator rules
    EPSILON,
    EOF,

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
    if,
    else,

    // Litteral values
    integerLiteral,
    floatingPointLiteral,
    stringLiteral,
    booleanLitteral,

    // Types
    void,
    text,
    number,
    fraction,
    character,
    boolean,
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
    ParameterList,
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
    ParameterList,

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
