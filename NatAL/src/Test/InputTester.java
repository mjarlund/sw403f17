package Test;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InputTester
{
    public static String readFile(String path) throws IOException
    {
	    byte[] encoded = Files.readAllBytes(Paths.get(path));
	    return new String(encoded, Charset.forName("ASCII") );
    }
}



	