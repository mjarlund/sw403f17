package p4test;
import p4test.SyntaxAnalysis.Scanner;
import p4test.SyntaxAnalysis.TableDrivenParser;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InputTester {
	

    public static void main(String args[])
    {
    	
    	String currentFile;
    	Scanner scanner;
    	
    	try {
    		
    		File folder = new File(System.getProperty("user.dir") + "/src/TestPrograms/");
    		File[] listOfFiles = folder.listFiles();

    		for (File file : listOfFiles) {
    			System.out.println("----------------" + file.getName() + "----------------");
    		    if (file.isFile()) {
    		        currentFile = "";
    		    	currentFile = readFile(System.getProperty("user.dir") + "/src/TestPrograms/" + file.getName());
    		    	scanner = new Scanner(currentFile);
    		        try 
    		        {
						TableDrivenParser parser = new TableDrivenParser(scanner);
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
    
    static String readFile(String path) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, Charset.forName("ASCII") );
			}
	
}


	