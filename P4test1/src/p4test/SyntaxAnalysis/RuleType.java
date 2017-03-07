package p4test;

// Terminals start with lowercase and non-terminals uppercase
public enum RuleType
{
    EPSILON,
    EOF,
    Program,
    Statement,
    Statements,
    DeclarationStatement,
    ExpressionStatement,
    SelectionStatement,
    ItterationStatement,
    Type,
    identifier,
    DeclarationStatementPrime,
    ListDeclarationStatement,
    FormalParameterList,
    Block,
    end,
    is,
    of,
    ParameterList,
    FormalParameterListBody,
    bracketOpen,
    bracketClose,
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

    below,
    above,

    AdditiveExpressionPrime,
    AdditiveExpression,

    +,
    -,

    MultiplicativeExpression,

    *,
    /,

    PrimaryExpression,



    IdentifierAppendantOptional,

    integerLiteral,
    floatingPointLiteral,
    stringLiteral,
    falseLiteral,
    trueLiteral,

    ExpressionStatementPrime,
    ExpressionStatementPrimePrime,
    ParameterList,
    dot,
    Parameter,
    ParameterList,
    squareBracketOpen,
    squareBracketClose,
    ParameterListBody,
    ParameterListBodyPrime,
    ifLitteral,
    Condition,
    SelectionStatementPrime,
    elseLitteral,
    until,
    foreach,
    in,
    text,
    number,
    fraction,
    character,
    booleanLitteral,
    voidLitteral,
    StructSpecifier,
    struct,
    StructSpecifierPrime,
    StructDeclaration,




}
