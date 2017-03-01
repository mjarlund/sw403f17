package p4test;

import jdk.internal.org.objectweb.asm.tree.analysis.Value;

import java.util.ArrayList;

/**
 * Created by Mysjkin on 28-02-2017.
 */
public class ParserIaro
{
    // lookahed value
    private int k = 2;
    private Token[] lookahed;
    private Scanner input;
    private int currentIndex = 0;

    public ParserIaro(Scanner input)
    {
        this.input = input;
        lookahed = new Token[k];
        for(int i = 0; i < k; i++)
        {
            lookahed[i] = input.nextToken();
        }
    }
    public void Run()
    {
        Dcls();
        //Stmts();
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
            switch (getIndex(currentIndex).Value)
            {
                case "number":
                case "fraction":
                case "string":
                case "character":
                    Dcl();
                    break;
                default:
                    quit = true;
            }
        }
    }
    private void Dcl()
    {
        match(TokenType.KEYWORD);
        match(TokenType.IDENTIFIER);
        match(TokenType.SEPERATOR);
    }
    private void Stmts()
    {
        while(input.currentChar != input.EOF)
        {
            switch(getIndex(currentIndex).Value)
            {

            }
        }
    }
    private void Stmt()
    {

    }
    private Token getIndex(int index)
    {
        return lookahed[index % k];
    }
    private void match(TokenType type, String c1)
    {
        try
        {
            if (getIndex(currentIndex).Type == type && getIndex(currentIndex).Value == c1) {
                System.out.println("matched " + c1);
                consume();
            } else
                throw new Error("double match expected " + c1 + " got " + getIndex(currentIndex).Value);
        }
        catch (NullPointerException e)
        {
            throw new Error("Missing termination value");
        }
    }
    private void match(TokenType type)
    {
        if(getIndex(currentIndex).Type == type)
        {
            System.out.println("matched "+type);
            consume();
        }
        else
            throw new Error("single match expected "+type.toString()+" got "+getIndex(currentIndex).Type.toString());
    }
    private void consume()
    {
        lookahed[currentIndex % k] = input.nextToken();
        currentIndex++;
    }
}
