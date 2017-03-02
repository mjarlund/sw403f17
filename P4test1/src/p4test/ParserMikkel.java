package p4test;

/*

Dcl -> type id
Stmt -> Dcl is Val Expr
        | LAMBDA
Expr -> minus Val Expr
        | div Val Expr
        | mul Val Expr
        | LAMBDA
Val ->  id
Func -> Signature Block end id
Signature -> Dcl ( Params )
Params -> Dcl*
Block -> ( Stmts | CtrlStrc | FuncCall )*
CtrlStrc -> ( IfStmt | UntilStmt )
IfStmt -> if ( BoolExpr ) Block end if
BoolExpr -> true
            | false
            | BoolExpr or BoolExpr
            | BoolExpr equals BoolExpr
            | BoolExpr and BoolExpr
            | not (BoolExpr)
UntilStmt -> until ( BoolExpr ) Block end until
FuncCall -> id ( Params )
 */
public class ParserMikkel {
    private Scanner input;
    private Token lookAhead;
    private Token GetLookAhead() {
        Token tmp = input.nextToken();
        System.out.println(tmp);
        return tmp;
    }

    public ParserMikkel(Scanner input) {
        this.input = input;
    }

    public boolean Parse(Scanner input) {
        this.input = input;
        return Parse();
    }

    // Prog -> Stmts $
    public boolean Parse() {
        // Get initial token
        lookAhead = GetLookAhead();
        Stmts();
        if(input.currentChar == input.EOF) {
            // Parsing succeeded
            return true;
        }
        else {
            // Parsing failed
            return false;
        }
    }

    /*
    Stmts ->  Stmt Stmts
            | Func Stmts
            | LAMBDA
     */
    private void Stmts() {
        // Both Stmt and Func predicts a type, so we check if lookahead is a type.
        if(Match(lookAhead, MatchType.TYPE)) {
            // Advance the lookahead
            lookAhead = GetLookAhead();
            // Both Stmt and Func predicts an Id, so we check if lookahead is an Id.
            if(lookAhead.Type != TokenType.IDENTIFIER) throw new Error("Invalid syntax");

            // Func predicts an '(' to be the next token, so we check if lookahead is a '('.
            lookAhead = GetLookAhead();


            if(lookAhead.Type == TokenType.SEPERATOR) {
                Func();
            }
            else {
                Stmt();
            }

            // Recursively call self
            Stmts();
        }


        // Otherwise, do nothing (Epsilon rule)
    }

    private void Stmt() {
    }

    private void Func() {

    }

    private void Dcl() {
    }


    private void Val() {

    }

    private boolean Match(Token token, MatchType type) {
        switch (type) {
            case TYPE:
                if(token.Type == TokenType.KEYWORD) {

                    switch(token.Value) {
                        case "number":
                        case "string":
                        case "boolean":
                        case "void":
                        case "float":
                            return true;
                    }

                }
            default:
                return false;
        }

    }

    private enum MatchType {
        TYPE,
    }

}
