package Syntax.Tokens;

public class Token
{
    public final TokenType Type;
    public final String Value;
    public final int LineNumber;

    public Token(String val, TokenType type) { this(val, type, 0);}

    public Token(String val, TokenType type, int lineNumber)
    {
        if (val.equals("low")) Value = val.toUpperCase();
        else if (val.equals("high")) Value = val.toUpperCase();
        else Value = val;

        Type = type;
        LineNumber = lineNumber;
    }
    public String toString() {return "<"+Type+", \'"+Value+"\'>";}
}
