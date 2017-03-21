package Syntax.Tokens;

/**
 * Created by Anders Brams on 3/21/2017.
 */
public class Token
{
    public final TokenType Type;
    public final String Value;

    public Token(String val, TokenType type)
    {
        this.Value = val;
        this.Type = type;
    }
    public String toString() {return "<"+Type+", \'"+Value+"\'>";}
}
