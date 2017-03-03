package p4test;

/**
 * Created by Mysjkin on 24-02-2017.
 */
public class Scanner
{
    /* Uses string builder maybe more effective without */


    public static final char EOF = (char)-1;
    public static final int EOF_TYPE = 1;

    private static DefaultHashMap<String, TokenType> words =
            new DefaultHashMap<String, TokenType>(TokenType.IDENTIFIER);

    protected String input;
    protected int index = 0;
    protected char currentChar;
    protected int inputLen;
    private int previousIndex;

    public Scanner(String input)
    {
        this.input = input;
        inputLen = input.length();
        currentChar = input.charAt(index);

        // Operators
        words.put("or", TokenType.OPERATOR); words.put("and", TokenType.OPERATOR);
        words.put("equals", TokenType.OPERATOR); words.put("is", TokenType.OPERATOR);
        words.put("not", TokenType.OPERATOR); words.put("above", TokenType.OPERATOR);
        words.put("under", TokenType.OPERATOR);

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
    public boolean IsEOF()
    {
        boolean ans;
        return ans = currentChar == EOF ? true : false;
    }
    protected void PushBack()
    {
        index = previousIndex;
        currentChar = input.charAt(index);
    }
    public void Consume(){Advance(); WhiteSpace();}
    public void Advance()
    {
        previousIndex = index;
        index++;
        if(index >= inputLen)
            currentChar = EOF;
        else
            currentChar = input.charAt(index);
    }

    // Useful for parser, maybe?!?
    public void Match(char x)
    {
        if(x == currentChar)
            Consume();
        else
            throw new Error("expecting "+x+"; found "+currentChar);
    }

    public Token nextToken()
    {
        while(currentChar != EOF)
        {
            switch (currentChar)
            {
                case '\n':case '(':case ')':case ',':
                    if(currentChar=='\n') {
                        Advance();
                        return new Token("\\n", TokenType.SEPARATOR);
                    }
                    else
                    {
                        Advance();
                        return new Token(Character.toString(input.charAt(previousIndex)), TokenType.SEPARATOR);
                    }
                case '+':case '-':case '/':case '*':
                    Advance();
                    return new Token(Character.toString(input.charAt(previousIndex)), TokenType.OPERATOR);
                case '\'':
                    return ScanChar();
                case '\"':
                    return ScanString();
                case ' ':case '\t':case '\r': WhiteSpace(); continue;
                default:
                    if(IsLetter())
                        return ScanLetters();
                    else if(Character.isDigit(currentChar))
                        return ScanDigit();
                    else
                        throw new Error("invalid char: "+currentChar);
            }
        }
        return null;
    }

    private boolean IsWS()
    {
        boolean ans = (currentChar == ' ' || currentChar == '\t' ||
                currentChar == '\r' || currentChar == ' ');
        return ans;
    }

    private boolean IsLetter()
    {
        boolean ans = (currentChar >= 'a' && currentChar <= 'z') ||
                (currentChar >= 'A' && currentChar <= 'Z');
        return  ans;
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
            Advance();
            if(currentChar == '.' && type == TokenType.INTEGER_LITERAL) {
                type = TokenType.FLOAT_LITERAL;
                sb.append(currentChar);
                Advance();
            }
        } while(Character.isDigit(currentChar));
        //PushBack();
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
        //PushBack(); // pushback to previous char, because moved to fare in the do while

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

    public void WhiteSpace()
    {
        while(currentChar == '\t' || currentChar == ' '||
                currentChar == '\r')
        {
            Advance();
        }
    }
}
