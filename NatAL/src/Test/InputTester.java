package Test;
import Syntax.Scanner.Scanner;
import Syntax.Parser.Parser;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Nikolaj on 3/16/2017.
 */
public class InputTester
{
    public static String readFile(String path) throws IOException
    {
	    byte[] encoded = Files.readAllBytes(Paths.get(path));
	    return new String(encoded, Charset.forName("ASCII") );
    }
}



	