package p4test;

/**
 * Created by Mysjkin on 28-02-2017.
 */
public class ParserIaro
{
    private Token lookahed;
    private Scanner input;

    public ParserIaro(Scanner input)
    {
        this.input = input;
        lookahed = input.nextToken();
    }
    public void Run()
    {
        Dcls();
        Stmts();
        if(input.currentChar == input.EOF)
        {
            System.out.println("Parse Completed");
            return;
        }
        else
            throw new Error("did not parse everything fix dis u nub!");
    }
    private void Dcls()
    {
        boolean quit = false;
        while(!quit)
        {
            switch (lookahed.Value)
            {
                case "number":
                case "fraction":
                case "string":
                case "character":
                    Dcl();
                default:
                    quit = true;
            }
        }
    }
    private void Dcl()
    {
        match(TokenType.KEYWORD);
        match(TokenType.IDENTIFIER);
        match(TokenType.SEPERATOR, "\\n");
    }
    private void Stmts()
    {

    }
    private void Stmt()
    {

    }
    private void match(TokenType type, String c1)
    {
        try
        {
            if (lookahed.Type == type && lookahed.Value == c1) {
                System.out.println("matched " + c1);
                consume();
            } else
                throw new Error("double match expected " + c1 + " got " + lookahed.Value);
        }
        catch (NullPointerException e)
        {
            throw new Error("Missing termination value");
        }
    }
    private void match(TokenType type)
    {
        if(lookahed.Type == type)
        {
            System.out.println("matched "+type);
            consume();
        }
        else
            throw new Error("single match expected "+type.toString()+" got "+lookahed.Type.toString());
    }
    private void consume()
    {
        lookahed = input.nextToken();
    }
}
