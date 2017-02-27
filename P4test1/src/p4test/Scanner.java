package p4test;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.xml.ws.soap.AddressingFeature;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mysjkin on 24-02-2017.
 */
public class Scanner
{
    /* Uses stringbuilder maybe more effective without */


    public static final char EOF = (char)-1;
    public static final int EOF_TYPE = 1;

    private static DefaultHashMap<String, TokentypeP4> words =
            new DefaultHashMap<String,TokentypeP4>(TokentypeP4.IDENTIFIER);

    protected String input;
    protected int index = 0;
    protected char currentChar;
    protected int inputLen;
    private int previosIndex;

    public Scanner(String input)
    {
        this.input = input;
        inputLen = input.length();
        currentChar = input.charAt(index);
        // Operatos
        words.put("or",TokentypeP4.OPERATOR); words.put("and",TokentypeP4.OPERATOR);
        words.put("equals",TokentypeP4.OPERATOR); words.put("is",TokentypeP4.OPERATOR);
        words.put("not",TokentypeP4.OPERATOR); words.put("over",TokentypeP4.OPERATOR);
        words.put("under",TokentypeP4.OPERATOR);

        // Keywords
        words.put("until", TokentypeP4.KEYWORD); words.put("end", TokentypeP4.KEYWORD);
        words.put("if", TokentypeP4.KEYWORD); words.put("else", TokentypeP4.KEYWORD);
        words.put("string", TokentypeP4.KEYWORD); words.put("boolean", TokentypeP4.KEYWORD);
        words.put("return", TokentypeP4.KEYWORD); words.put("void", TokentypeP4.KEYWORD);
        words.put("structure", TokentypeP4.KEYWORD); words.put("fraction", TokentypeP4.KEYWORD);
        words.put("number", TokentypeP4.KEYWORD); words.put("boolean", TokentypeP4.KEYWORD);
        words.put("string", TokentypeP4.KEYWORD); words.put("character", TokentypeP4.KEYWORD);

        // Other
        words.put("false", TokentypeP4.BOOLEAN_LITERAL); words.put("true", TokentypeP4.BOOLEAN_LITERAL);
    }
    protected void PushBack()
    {
        index = previosIndex;
        currentChar = input.charAt(index);
    }
    public void Consume(){Advance(); WhiteSpace();}
    public void Advance()
    {
        previosIndex = index;
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
                    if(currentChar=='\n')
                        return new Token("\\n", TokentypeP4.SEPERATOR);
                    else
                        return new Token(Character.toString(currentChar), TokentypeP4.SEPERATOR);
                case '+':case '-':case '/':case '*':
                    return new Token(Character.toString(currentChar), TokentypeP4.OPERATOR);
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
        throw new Error("No termination");
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

        return new Token(val,TokentypeP4.CHAR_LITERAL);
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
        return new Token(sb.toString(), TokentypeP4.STRING_LITERAL);
    }

    private Token ScanDigit()
    {
        StringBuilder sb = new StringBuilder();
        TokentypeP4 type = TokentypeP4.INTEGER_LITERAL;
        do
        {
            sb.append(currentChar);
            Advance();
            if(currentChar == '.' && type == TokentypeP4.INTEGER_LITERAL) {
                type = TokentypeP4.FLOAT_LITERAL;
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
        TokentypeP4 type = words.get(val);
        if(type != TokentypeP4.IDENTIFIER)
            return new Token(val, type);

        return new Token(val, TokentypeP4.IDENTIFIER);
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
