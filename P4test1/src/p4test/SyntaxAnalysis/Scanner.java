package p4test.SyntaxAnalysis;
import p4test.DefaultHashMap;
import p4test.Token;
import p4test.TokenType;

public class Scanner
{
    /* Uses string builder maybe more effective without */
    public static final char EOF = (char)-1;

    public static DefaultHashMap<String, TokenType> words =
            new DefaultHashMap<String, TokenType>(TokenType.IDENTIFIER);

    protected String input;
    protected int index = 0;
    protected char currentChar;
    protected int inputLen;

    public Scanner(String input)
    {
        this.input = input;
        inputLen = input.length();
        currentChar = input.charAt(index);

        // Operators
        words.put("or", TokenType.OPERATOR); words.put("and", TokenType.OPERATOR);
        words.put("equals", TokenType.OPERATOR); words.put("is", TokenType.OPERATOR);
        words.put("not", TokenType.OPERATOR); words.put("above", TokenType.OPERATOR);
        words.put("below", TokenType.OPERATOR);

        // Keywords
        words.put("until", TokenType.KEYWORD); words.put("end", TokenType.KEYWORD);
        words.put("if", TokenType.KEYWORD); words.put("else", TokenType.KEYWORD);
        words.put("string", TokenType.KEYWORD); words.put("boolean", TokenType.KEYWORD);
        words.put("return", TokenType.KEYWORD); words.put("void", TokenType.KEYWORD);
        words.put("structure", TokenType.KEYWORD); words.put("fraction", TokenType.KEYWORD);
        words.put("number", TokenType.KEYWORD);  words.put("text", TokenType.KEYWORD);
        words.put("character", TokenType.KEYWORD);

        // Other
        words.put("false", TokenType.BOOLEAN_LITERAL); words.put("true", TokenType.BOOLEAN_LITERAL);
    }

    public void Advance()
    {
    	index++;
        if(index >= inputLen)
            currentChar = EOF;
        else
        {
            currentChar = input.charAt(index);
        }
    }

    public Token NextToken()
    {
        while(currentChar != EOF)
        {
            switch (currentChar)
            {
                case '\n':
                    Advance();
                    return new Token("\\n", TokenType.SEPARATOR);
                case '(':case ')':case ',':
                	Token sp = new Token(Character.toString(currentChar), TokenType.SEPARATOR);
                    Advance();
                    return sp;
                case '+':case '-':case '/':case '*':
                	Token op = new Token(Character.toString(currentChar), TokenType.OPERATOR);
                    Advance();
                    return op;
                case '\'':
                    return ScanChar();
                case '\"':
                    return ScanString();
                default:
                    if(IsLetter())
                        return ScanLetters();
                    else if(Character.isDigit(currentChar))
                        return ScanDigit();
                    else if(IsWS())
                        Advance();
                    else
                        throw new Error("invalid char: "+currentChar);
            }
        }
        return new Token("EOF", TokenType.EOF);
    }

    private boolean IsWS()
    {
        return (currentChar == ' ' || currentChar == '\t' || currentChar == '\r');
    }

    private boolean IsLetter()
    {
        return (currentChar >= 'a' && currentChar <= 'z') ||
                (currentChar >= 'A' && currentChar <= 'Z');
    }
    private Token ScanChar()
    {
        String val = "";
        do
        {
            val += currentChar;
            Advance();
        } while(currentChar != '\'');
        val+= currentChar;
        Advance();

        return new Token(val, TokenType.CHAR_LITERAL);
    }
    private Token ScanString()
    {
        StringBuilder sb = new StringBuilder();
        do
        {
            sb.append(currentChar);
            Advance();
        } while(currentChar != '\"');
        sb.append(currentChar);
        Advance();
        return new Token(sb.toString(), TokenType.STRING_LITERAL);
    }

    private Token ScanDigit()
    {
        StringBuilder sb = new StringBuilder();
        TokenType type = TokenType.INTEGER_LITERAL;
        do
        {

            sb.append(currentChar);
            
        	if(IsLetter())
        	{
        		throw new Error(sb.toString() + " is not a valid number");
        	}
        	
            Advance();
            if(currentChar == '.' && type == TokenType.INTEGER_LITERAL) {
                type = TokenType.FLOAT_LITERAL;
                sb.append(currentChar);
                Advance();
            }
        } while(Character.isDigit(currentChar) || IsLetter() == true);
        return new Token(sb.toString(), type);
    }

    private Token ScanLetters()
    {
        StringBuilder sb = new StringBuilder();
        do
        {
            sb.append(currentChar);
            Letter();
        } while((IsLetter() || Character.isDigit(currentChar)) && !IsWS());

        String val = sb.toString();
        TokenType type = words.get(val);
        if(type != TokenType.IDENTIFIER)
            return new Token(val, type);

        return new Token(val, TokenType.IDENTIFIER);
    }
    private void Letter()
    {
        if(IsLetter() || Character.isDigit(currentChar))
            Advance();
        else
            throw new Error("expecting letter but found: "+currentChar);
    }
}
