package p4test;

/**
 * Created by Mysjkin on 24-02-2017.
 */
public class test
{
    public static void main(String args[])
    {
        // Test string
        /*String input = "void FirstMission (structure person, number speed)\n" +
                "    until (heading equals 90 90.0)\n" +
                "        turn (left)\n" +
                "        move (forward)\n" +
                "        end until\n" +
                "        string a equals \"wasd\"\n" +
                "        character b equals \'a\'\n" +
                "        boolean s equals false\n" +
                "    end FirstMission"; */
        String input = "number anders";

        ParserMikkel bla = new ParserMikkel(new Scanner(input));
        if(bla.Parse()) {
            System.out.println("Success");
        }

        /*
        // Run scanner on test string
        Scanner scanner = new Scanner(input);
        try
        {
            long i = input.chars().count();
            //while(scanner.index < scanner.inputLen)
                //System.out.println(scanner.nextToken());
            ParserIaro parserIaro = new ParserIaro(scanner);
            parserIaro.Run();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        */
    }
}
