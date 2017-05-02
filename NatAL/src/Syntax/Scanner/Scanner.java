package Syntax.Scanner;
import DataStructures.DefaultHashMap;
import Exceptions.*;
import Syntax.Tokens.Token;
import Syntax.Tokens.TokenType;
import Utilities.Reporter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class Scanner
{
    /* Uses string builder maybe more effective without */
    public static final char EOF = (char)-1;

    public static DefaultHashMap<String, TokenType> words =
            new DefaultHashMap<String, TokenType>(TokenType.IDENTIFIER);

    protected String input;
    protected int index = 0;
    protected char currentChar;
    protected int inputLength;
    private static int lineNumber;
    public static int GetLineNumber()
    {
        return lineNumber;
    }
    public static void IncreaseLineNumber(int increaseBy) {lineNumber+=increaseBy;}

    public Scanner (String input)
    {
        this.input = input;
        inputLength = input.length();
        currentChar = input.charAt(index);
        this.lineNumber = 1;
        // Operators
        words.put("or", TokenType.OPERATOR); words.put("and", TokenType.OPERATOR);
        words.put("equals", TokenType.OPERATOR); words.put("is", TokenType.OPERATOR);
        words.put("not", TokenType.OPERATOR); words.put("above", TokenType.OPERATOR);
        words.put("below", TokenType.OPERATOR); words.put("read", TokenType.OPERATOR);
        words.put("write",TokenType.OPERATOR);

        // Keywords
        words.put("until", TokenType.KEYWORD); words.put("end", TokenType.KEYWORD);
        words.put("if", TokenType.KEYWORD); words.put("else", TokenType.KEYWORD);
        words.put("string", TokenType.KEYWORD); words.put("boolean", TokenType.KEYWORD);
        words.put("return", TokenType.KEYWORD); words.put("void", TokenType.KEYWORD);
        words.put("structure", TokenType.KEYWORD); words.put("fraction", TokenType.KEYWORD);
        words.put("number", TokenType.KEYWORD);  words.put("text", TokenType.KEYWORD);
        words.put("character", TokenType.KEYWORD); words.put("list", TokenType.KEYWORD);
        words.put("of", TokenType.KEYWORD); words.put("digital", TokenType.KEYWORD);
        words.put("analog", TokenType.KEYWORD); words.put("pin", TokenType.KEYWORD);
        words.put("input",TokenType.KEYWORD); words.put("output",TokenType.KEYWORD);
        words.put("from",TokenType.KEYWORD); words.put("to",TokenType.KEYWORD);
        words.put("foreach", TokenType.KEYWORD); words.put("in", TokenType.KEYWORD);
        words.put("repeat", TokenType.KEYWORD);

        // Other
        words.put("false", TokenType.BOOLEAN_LITERAL); words.put("true", TokenType.BOOLEAN_LITERAL);
        words.put("high", TokenType.DIGITAL_LITERAL); words.put("low", TokenType.DIGITAL_LITERAL);
        
        while (IsWS() || currentChar == '\n')
    	{
    	    if(currentChar == '\n')
    	        IncreaseLineNumber(1);
    		Advance();
    	}
    }

    public void Advance ()
    {
        index++;
        if (index >= inputLength)
        {
            currentChar = EOF;
        }
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
                    return MakeToken("\\n", TokenType.SEPARATOR);
                case '(':case ')':case ',':
                    Token sp = MakeToken(Character.toString(currentChar), TokenType.SEPARATOR);
                    Advance();
                return sp;
                case '+':case '-':case '/':case '*':
                    Token op = MakeToken(Character.toString(currentChar), TokenType.OPERATOR);
                    Advance();
                return op;
                case '\'':
                    return ScanChar();
                case '\"':
                    return ScanString();
                case '.' :case '[':case ']':
                	Token ac = MakeToken(Character.toString(currentChar), TokenType.ACCESSOR);
                	Advance();
                	return ac;
                default:
                    if(IsLetter())
                        return ScanLetters();
                    else if(Character.isDigit(currentChar))
                        return ScanDigit();
                    else if(IsWS())
                        Advance();
                    else
                        Reporter.Error(new InvalidCharacterSequenceException("Invalid character encountered: " + currentChar));
            }
        }

        return MakeToken("EOF", TokenType.EOF);
    }

    public boolean IsWS()
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
        val += currentChar;
        Advance();
        if(AsciiCheck(currentChar))
        {
        	val += currentChar;
            Advance();
            if(currentChar == '\'')
            {
            	val+= currentChar;
            	Advance();
            }
            else
            	Reporter.Error(new InvalidCharacterSequenceException("Invalid character encountered: " + currentChar + "Expected '"));
        }
        else
        	 Reporter.Error(new InvalidCharacterException("Invalid character encountered: " + currentChar + " is not a valid Ascii Character"));
        
        return MakeToken(val, TokenType.CHAR_LITERAL);     
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
        if(AsciiCheck(sb.toString()) == false)
        	 Reporter.Error(new InvalidCharacterException("Invalid character encountered in string: " + sb + "contains a non-Ascii Character"));
        return MakeToken(sb.toString(), TokenType.STRING_LITERAL);
    }

    private Token ScanDigit()
    {
        StringBuilder sb = new StringBuilder();
        TokenType type = TokenType.INTEGER_LITERAL;
        do
        {
            sb.append(currentChar);

            if (currentChar == 'f'){
                Advance();
                return MakeToken(sb.toString(), TokenType.FLOAT_LITERAL);
            }
            if(IsLetter())
                Reporter.Error(new InvalidIdentifierException(sb + " is not a valid number"));

            Advance();
            if(currentChar == '.' && type == TokenType.INTEGER_LITERAL) {
                type = TokenType.FLOAT_LITERAL;
                sb.append(currentChar);
                Advance();
            }
        } while(Character.isDigit(currentChar) || IsLetter());
        return MakeToken(sb.toString(), type);
    }

    private Token ScanLetters()
    {
        StringBuilder sb = new StringBuilder();
        do
        {
            sb.append(currentChar);
            Advance();
        } while((IsLetter() || Character.isDigit(currentChar)) && !IsWS());

        String val = sb.toString();
        TokenType type = words.get(val);

        if(type != TokenType.IDENTIFIER)
            return MakeToken(val, type);

        return MakeToken(val, TokenType.IDENTIFIER);
    }

    private Token MakeToken (String value, TokenType type)
    {
        return new Token(value, type, lineNumber);
    }
    
    private boolean AsciiCheck(String v){
    CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();
    
    return asciiEncoder.canEncode(v); 
    }
    
    private boolean AsciiCheck(char v)
    {
    CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();
        
    return asciiEncoder.canEncode(v); 
    }
}
