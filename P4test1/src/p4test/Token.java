package p4test;



/**
 * Created by Mysjkin on 23-02-2017.
 */
public class Token
{
    private TokentypeP4 type;
    private String val;

    public TokentypeP4 GetType(){return type;}
    public String GetVal() {return val;}

    public Token(String val, TokentypeP4 type)
    {
        this.val = val;
        this.type = type;
    }
    public String toString() {return "<"+val+","+type+">";}
}
