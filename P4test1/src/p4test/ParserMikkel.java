package p4test;


public class ParserMikkel {
    // LookAhead properties
    private final int lookAheadConsts = 3;
    private Token[] lookahed;
    private int currentIndex = 0;

    // Other properties
    private Scanner input;

    private void InitiateLookAheadBuffer() {
        lookahed = new Token[lookAheadConsts];
        for(int i = 0; i < lookAheadConsts; i++)
        {
            lookahed[i] = input.nextToken();
        }
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
        // Get initial tokens
        InitiateLookAheadBuffer();

        Dcls();
        // Stmts();
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
        Dcls -> Dcl Dcls
                | Func Stmts Dcls
     */
    private void Dcls() {
        // Both Dcl and Func predicts a type, so we check if lookahead is a type.
        if(Match(GetToken(currentIndex), MatchType.TYPE)) {

            // Both Dcl and Func predicts an Id, so we check if lookahead is an Id.
            if(GetToken(currentIndex + 1).Type != TokenType.IDENTIFIER) throw new Error("Invalid syntax");

            // Func predicts an '(' to be the next token, so we check if lookahead is a '('.
            if(GetToken(currentIndex + 2).Type == TokenType.SEPERATOR) {
                Func();

                // Stmts();
                // Dcls();
            }
            else {
                Dcl();
                Dcls();
            }
        }
    }

    /*
        Func -> Signature Block end id
     */
    private void Func() {
        String funcId = GetToken(currentIndex+1).Value;
        Signature();
        // Block();
        // MatchExpect(TokenType.KEYWORD, "end");
        // MatchExpect(TokenType.IDENTIFIER, funcId);
    }

    /*
        Signature -> Dcl ( Params )
    */
    private void Signature() {
        Dcl();
        MatchExpect(TokenType.SEPERATOR, "(");
        Params();
        MatchExpect(TokenType.SEPERATOR, ")");
    }

    /*
        Params -> Dcl*
     */
    private void Params(){

        while(true) {
            if(Match(GetToken(currentIndex), MatchType.TYPE) &&
                    GetToken(currentIndex + 1).Type == TokenType.IDENTIFIER) {

                Dcl();

                if(Match(GetToken(currentIndex + 1), MatchType.TYPE)) {
                    MatchExpect(TokenType.SEPERATOR, ",");
                }

            } else break;
        }

    }

    /*
        Dcl -> type id
     */
    private void Dcl() {
        MatchExpect(TokenType.KEYWORD);
        MatchExpect(TokenType.IDENTIFIER);
    }

    /*
    Stmts -> Stmt Stmts
            | Func Stmts
            | LAMBDA
     */
    private void Stmts() {
        // Both Stmt and Func predicts a type, so we check if lookahead is a type.
        if(Match(GetToken(currentIndex), MatchType.TYPE)) {

            // Both Stmt and Func predicts an Id, so we check if lookahead is an Id.
            if(GetToken(currentIndex + 1).Type != TokenType.IDENTIFIER) throw new Error("Invalid syntax");

            // Func predicts an '(' to be the next token, so we check if lookahead is a '('.
            if(GetToken(currentIndex + 2).Type == TokenType.SEPERATOR) {
                Func();
            }
            else {
                Stmt();
            }

            // Recursively call self
            // Stmts();
        }


        // Otherwise, do nothing (Epsilon rule)
    }

    private void Stmt() {
    }


    private void Block(){

    }



    private void Val() {

    }

    private boolean Match(Token token, MatchType type) {
        switch (type) {
            case TYPE:
                if(token.Type == TokenType.KEYWORD) {

                    switch(token.Value) {
                        case "number":
                        case "character":
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

    private void MatchExpect(TokenType type, String value){
        Token token = GetToken(currentIndex);
        if (token.Type == type && token.Value.equals(value)) {
            System.out.println("Matched"+" "+token);
            ConsumeToken();
        }
        else
            throw new Error("Invalid syntax expected "+value+" got " +token.Value);
    }
    private void MatchExpect(TokenType type){
        Token token = GetToken(currentIndex);
        if (token.Type == type) {
            System.out.println("Matched" + " "+token);
            ConsumeToken();
        }
        else
            throw new Error("Invalid syntax");
    }
    private enum MatchType {
        TYPE,
    }

    private Token GetToken(int index)
    {
        Token tmp = lookahed[index % lookAheadConsts];
        return tmp;
    }

    private void ConsumeToken()
    {
        Token next = input.nextToken();
        if(next == null) next = new Token("EOF", TokenType.EOF);
        lookahed[currentIndex % lookAheadConsts] = next;
        currentIndex++;
    }

}
