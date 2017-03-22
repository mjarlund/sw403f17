package Syntax.Tokens;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class Token
{
    public final TokenType Type;
    public final String Value;
    public final int LineNumber;

    public Token(String val, TokenType type) { this(val, type, 0);}

    public Token(String val, TokenType type, int lineNumber)
    {
        this.Value = val;
        this.Type = type;
        this.LineNumber = lineNumber;
    }
    public String toString() {return "<"+Type+", \'"+Value+"\'>";}
}
