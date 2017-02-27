package p4test;

/**
 * Created by Mysjkin on 24-02-2017.
 */
public class test
{
    public static void main(String args[])
    {
        String code = "void FirstMission (structure person, number speed)\n" +
                "    until (heading equals 90 90.0)\n" +
                "        turn (left)\n" +
                "        move (forward)\n" +
                "        end until\n" +
                "        string a equals \"wasd\"\n" +
                "        character b equals \'a\'\n" +
                "        boolean s equals false\n" +
                "    end FirstMission";
        String codeIzi = "or";
        Scanner scanner = new Scanner(code);
        try
        {
            long i = code.chars().count();
            while(scanner.index < scanner.inputLen)
            {
                System.out.println(scanner.nextToken());
                scanner.Consume();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
