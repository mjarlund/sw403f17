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
public class InputTester {

	public static void main(String args[])
	{
		InputTester.TestFiles();
	}

	public static void TestFiles()
	{
    	String currentFile;
    	Scanner scanner;

    	try {
    		File folder = new File(System.getProperty("user.dir") + "/src/Test/TestPrograms/");
    		File[] listOfFiles = folder.listFiles();

    		for (File file : listOfFiles) {
    			System.out.println("----------------" + file.getName() + "----------------");
    		    if (file.isFile()) {
    		        currentFile = "";
    		    	currentFile = readFile(System.getProperty("user.dir") + "/src/Test/TestPrograms/" + file.getName());
    		    	scanner = new Scanner(currentFile);
    		        try 
    		        {
						Parser parser = new Parser(scanner);
						parser.ParseProgram();
    		        }
    		        catch (Exception e)//catch exception if any
    		        {
    		            System.out.println("Mistakes were made: " + e);
    		        }
    		    }
    		}
		} catch (Exception e){//Catch exception if any
			  System.out.println("Error: " + e.getMessage());
		}
    }
    public static String readFile(String path) throws IOException
    {
	    byte[] encoded = Files.readAllBytes(Paths.get(path));
	    return new String(encoded, Charset.forName("ASCII") );
    }
}



	