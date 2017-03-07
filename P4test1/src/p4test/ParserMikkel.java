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

    // Prog -> Dcls Stmt $
    public boolean Parse() {
        // Get initial tokens
        InitiateLookAheadBuffer();

        try {
            Dcls();
            Stmts();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                | Func Stmt Dcls
     */
    private void Dcls() throws Exception {
        try {
            // Both Dcl and Func takes type and id as the first two tokens, so in order to pick the right route,
            // we need to check if the third occurring token is a separator as needed by func.
            if(GetToken(currentIndex + 2).Type != TokenType.SEPARATOR) {
                Dcl();
                Dcls();
            }
            else {
                Func();
                Stmts();
                Dcls();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /*
        Func -> Signature Block end id
     */
    private void Func() throws Exception {
        try {
            String id = GetToken(currentIndex+1).Value;
            Signature();
            Block();
            MatchExpect(TokenType.KEYWORD, "end");
            MatchExpect(TokenType.IDENTIFIER, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /*
        Signature -> Dcl ( Params )
    */
    private void Signature() throws Exception {
        try {
            Dcl();
            MatchExpect(TokenType.SEPARATOR, "(");
            Params();
            MatchExpect(TokenType.SEPARATOR, ")");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /*
        Params -> Dcl (,Dcl)*
                | LAMBDA
     */
    private void Params() throws Exception {

        while(MatchCheck(currentIndex, TokenType.KEYWORD, "number", "character", "string", "boolean", "void", "float")) {
            try {
                Dcl();

                if(MatchCheck(currentIndex + 1, TokenType.KEYWORD, "number", "character", "string", "boolean", "void", "float")
                        && !MatchCheck(currentIndex, TokenType.SEPARATOR, ")")) {
                    MatchExpect(TokenType.SEPARATOR, ",");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

    }

    /*
        Dcl -> Type id
     */
    private void Dcl() {
        try {
            Type();
            MatchExpect(TokenType.IDENTIFIER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        Stmt -> Stmt Stmt
    	        | LAMBDA
     */
    private void Stmts() throws Exception {
        // Stmt predicts a type, so we check if lookahead is compliant.
        if(MatchCheck(currentIndex, TokenType.KEYWORD, "number", "character", "string", "boolean", "void", "float")) {
            try {
                Stmt();
                Stmts();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    /*
        Stmt -> Dcl is Val Expr
   	            | LAMBDA
     */
    private void Stmt() throws Exception {
        // Dcl predicts a type, so we check if lookahead is compliant.
        if(MatchCheck(currentIndex, TokenType.KEYWORD, "number", "character", "string", "boolean", "void", "float")) {

            try {
                Dcl();
                MatchExpect(TokenType.OPERATOR, "is");
                Val();
                Expr();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }

        }
    }

    /*
        Block -> ( Stmt | CtrlStrc | FuncCall )*
     */
    private void Block() throws Exception {
        while(GetToken(currentIndex).Value != "end" || GetToken(currentIndex).Value != "EOF") {

            try {
                // TokenType can either be keyword (for Stmt or CtrlStrc) or identifier (for FuncCall).
                if(GetToken(currentIndex).Type == TokenType.IDENTIFIER) {
                    FuncCall();
                }
                else if(GetToken(currentIndex).Type == TokenType.KEYWORD) {

                    // CtrlStrc expects an if or until
                    if(MatchCheck(currentIndex, TokenType.KEYWORD, "if", "until")) {
                        CtrlStrc();
                    }
                    else {
                        Stmts();
                    }
                }
                else {
                    throw new InvalidSyntaxException(GetToken(currentIndex), TokenType.IDENTIFIER, TokenType.KEYWORD);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    /*
        FuncCall -> id ‘(‘ Params ‘)’
     */
    private void FuncCall() throws Exception {
        try {
            MatchExpect(TokenType.IDENTIFIER);
            MatchExpect(TokenType.SEPARATOR, "(");
            Params();
            MatchExpect(TokenType.SEPARATOR, ")");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    /*
        CtrlStrc -> ( IfStmt | UntilStmt )
     */
    private void CtrlStrc() throws Exception {
        try {
            // We expect either if or until, so we'll check for one of them
            if(GetToken(currentIndex).Value == "if") {
                IfStmt();
            }
            else {
                UntilStmt();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /*
        IfStmt -> if ‘(‘ BoolExpr ‘)’ Block end if
     */
    private void IfStmt() throws Exception {
        try {
            MatchExpect(TokenType.KEYWORD, "if");
            MatchExpect(TokenType.SEPARATOR, "(");
            BoolExpr();
            MatchExpect(TokenType.SEPARATOR, ")");
            Block();
            MatchExpect(TokenType.KEYWORD, "end");
            MatchExpect(TokenType.KEYWORD, "if");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /*
        UntilStmt -> until ‘(‘ BoolExpr ‘)’ Block end until
     */
    private void UntilStmt() throws Exception {
        try {
            MatchExpect(TokenType.KEYWORD, "until");
            MatchExpect(TokenType.SEPARATOR, "(");
            BoolExpr();
            MatchExpect(TokenType.SEPARATOR, ")");
            Block();
            MatchExpect(TokenType.KEYWORD, "end");
            MatchExpect(TokenType.KEYWORD, "until");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /*
        BoolExpr -> true
                | false
                | BoolExpr or BoolExpr
                | BoolExpr equals BoolExpr
                | BoolExpr and BoolExpr
                | not ‘(‘ BoolExpr ‘)’
     */
    private void BoolExpr() throws Exception {
        try {
            if(GetToken(currentIndex).Type == TokenType.BOOLEAN_LITERAL) {
                switch (GetToken(currentIndex).Value) {
                    case "false":
                        MatchExpect(TokenType.BOOLEAN_LITERAL, "false");
                        break;
                    case "true":
                        MatchExpect(TokenType.BOOLEAN_LITERAL, "true");
                        break;
                }
            }

            if(GetToken(currentIndex).Type == TokenType.OPERATOR) {
                switch (GetToken(currentIndex).Value) {
                    case "or":
                        MatchExpect(TokenType.OPERATOR, "or");
                        break;
                    case "equals":
                        MatchExpect(TokenType.OPERATOR, "equals");
                        break;
                    case "and":
                        MatchExpect(TokenType.OPERATOR, "and");
                        break;
                    case "not":
                        MatchExpect(TokenType.OPERATOR, "not");
                        MatchExpect(TokenType.SEPARATOR, "(");
                        BoolExpr();
                        MatchExpect(TokenType.SEPARATOR, ")");
                        break;
                }
            }

            if(GetToken(currentIndex + 1).Type == TokenType.OPERATOR ||
                    GetToken(currentIndex + 1).Type == TokenType.SEPARATOR
                            && GetToken(currentIndex + 1).Value == ")") {
                BoolExpr();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }



    /*
        Val ->  id
            | id ‘(‘Args’)’
     */
    private void Val() throws Exception {
        // In either case, we expect an identifier
        try {
            MatchExpect(TokenType.IDENTIFIER);
            // For the 2nd case to run, we expect a separator
            if(GetToken(currentIndex).Type == TokenType.SEPARATOR) {
                MatchExpect(TokenType.SEPARATOR, "(");
                MatchExpect(TokenType.SEPARATOR, ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }

    /*
        Args -> id
                |  id (, id)*
                | LAMBDA
     */
    private void Args() throws Exception {
        while(GetToken(currentIndex).Type == TokenType.IDENTIFIER) {
            try {
                MatchExpect(TokenType.IDENTIFIER);

                if(GetToken(currentIndex + 1).Type == TokenType.IDENTIFIER)
                    MatchExpect(TokenType.SEPARATOR, ",");
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }

        }
    }

    /*
        Expr -> plus Val Expr
                | minus Val Expr
                | div Val Expr
                | mul Val Expr
                | LAMBDA
     */
    private void Expr() throws Exception {
        if(GetToken(currentIndex).Type == TokenType.OPERATOR) {
            try {
                MatchExpect(TokenType.OPERATOR, "plus", "minus", "div", "mul");
                Val();
                Expr();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }

        }

    }

    private void Type() throws Exception {
        try {
            MatchExpect(TokenType.KEYWORD, "number", "character", "string", "boolean", "void", "float");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    private Boolean MatchCheck(int tokenIndex, TokenType type, String ... values) {
        Token token = GetToken(tokenIndex);
        for(String value : values) {
            if (token.Type == type && token.Value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    private Boolean MatchCheck(int tokenIndex, TokenType ... types) {
        Token token = GetToken(tokenIndex);
        for(TokenType type : types) {
            if (token.Type == type) {
                return true;
            }
        }
        return false;
    }

    private void MatchExpect(TokenType type, String ... values) throws Exception {
        Token token = GetToken(currentIndex);
        Boolean matched = false;
        for(String value : values) {
            if (token.Type == type && token.Value.equals(value)) {
                System.out.println("Matched"+" "+token);
                ConsumeToken();
                matched = true;
                break;
            }
        }

        if(!matched)
            throw new InvalidSyntaxException(token, values);
    }

    private void MatchExpect(TokenType type, String value) throws Exception {
        Token token = GetToken(currentIndex);
        if (token.Type == type && token.Value.equals(value)) {
            System.out.println("Matched"+" "+token);
            ConsumeToken();
        }
        else
            throw new InvalidSyntaxException(token, value);
    }

    private void MatchExpect(TokenType type) throws Exception {
        Token token = GetToken(currentIndex);
        if (token.Type == type) {
            System.out.println("Matched" + " "+token);
            ConsumeToken();
        }
        else
            throw new InvalidSyntaxException(token, type);
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
