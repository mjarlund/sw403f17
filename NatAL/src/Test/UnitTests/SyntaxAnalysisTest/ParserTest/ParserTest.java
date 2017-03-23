package Test.UnitTests.SyntaxAnalysisTest.ParserTest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import Exceptions.MissingProductionsException;
import Exceptions.UnexpectedTokenException;
import Syntax.Parser.Parser;
import Syntax.Scanner.Scanner;
import Syntax.Tokens.TokenType;
import Test.InputTester;

@RunWith(Enclosed.class)
public class ParserTest {
	
	
	@RunWith(Parameterized.class)
	public static class AllParsableFilesTests
	{
	
		private String testPath;


		public AllParsableFilesTests(String path)
		{
			this.testPath = path;
		}
		
		
		@Parameters(name = "{index}: parsed({0})")
		public static Collection<Object[]> generateData()
		{
			Collection<Object[]> data = new ArrayList<Object[]>();
			
			try{
				File folder = new File("src/Test/TestPrograms/");
				File[] listOfFiles = folder.listFiles();
    		
    			for (File file : listOfFiles) {
    				if (file.isFile() && !file.getName().contains("fail")) 
    				{
    					Object[] testFile = new Object[] { file.getPath() };
    					data.add(testFile);
    				}
    			}
			}
			catch (Exception e){//Catch exception if any
				  System.out.println("Error when reading files: " + e.getMessage());
			}
			return data;
		}
		
		@Test
		public void NoExceptionsThrownTest() throws IOException {
			
			Throwable e = null;		
			Scanner testScanner = new Scanner(InputTester.readFile(testPath));
			try 
			{
				Parser parser = new Parser(testScanner);
				parser.ParseProgram();
			} 
			catch (Throwable ex)
			{					
				e = ex;	
				ex.printStackTrace();
			}			
			assertEquals(null, e);
		}		
	}
	
	
	/*
	public static class multipleInputParserTest
	{
		@Test
		public void voidMainTest()
		{	
			try{
			Scanner multipleInputScanner = new Scanner("void main()" + System.lineSeparator() + "Interger a is 4"
					+ "hej mor etc" );
			Parser parser = new Parser(multipleInputScanner);
			parser.ParseProgram();
			}
			catch (MissingProductionsException ex)
			{					
				assertEquals(true, true);
			}			
		}	
	}
	*/
	@RunWith(Parameterized.class)
	public static class AllFailableTests{
		
		private String testPath;

		public AllFailableTests(String path)
		{
			this.testPath = path;
		}		
		
		@Parameters(name = "{index}: successfullyFailedToPass({0})")
		public static Collection<Object[]> generateData()
		{
			Collection<Object[]> data = new ArrayList<Object[]>();
			
			try{
				File folder = new File("src/Test/TestPrograms/");
				File[] listOfFiles = folder.listFiles();
    		
    			for (File file : listOfFiles) {
    				if (file.isFile() && file.getName().contains("fail")) 
    				{
    					Object[] testFile = new Object[] { file.getPath() };
    					data.add(testFile);
    				}
    			}
			}
			catch (Exception e){//Catch exception if any
				  System.out.println("Error when reading files: " + e.getMessage());
			}
			return data;
		}

		@Test
		public void ExceptionssuccessfullyThrownTest() throws IOException {
			
			boolean thrown = false;		
			Scanner testScanner = new Scanner(InputTester.readFile(testPath));
			try 
			{
				Parser parser = new Parser(testScanner);
				parser.ParseProgram();
			} 
			catch (UnexpectedTokenException ex)
			{					
				thrown = true;
			}			
			catch (MissingProductionsException ex)
			{
				thrown = true;
			}
			assertEquals(true, thrown);
		}		
	}
	
	
	
	
}
