package Test.UnitTests.SyntaxAnalysisTest.ParserTest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import Exceptions.InvalidCharacterException;
import Exceptions.InvalidIdentifierException;
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
				File folder = new File("src/Test/TestPrograms/parserAndScanner/");
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
	
	
	@RunWith(Parameterized.class)
	public static class multipleInputParserFailTest
	{
		private String inputString;

		public multipleInputParserFailTest(String input)
		{
			this.inputString = input;
		}

		@Parameters(name = "{index}: Scan({0})={1}")
		public static Collection<Object[]> generateData()
		{
			return Arrays.asList(new Object[][]{
				{"text shouldBeNumber is analog read from motor1"},
				{"number shouldBeText is Digital read from motor2"},
				{"digital write 2 to motor1"},
				{"analog write high to motor2"},
				{"character shouldBeText is digital read from motor1"},
			});
		}
		@Test
		public void MultiInputFailTest() throws Exception
		{				
			boolean thrown = false;
			String input = "void main()" + System.lineSeparator() + 
					"pin motor1 is 1" +
					"pin motor2 is 2" +
					inputString + 
					System.lineSeparator() + "end main";
			try 
			{
				Scanner testScanner = new Scanner(input);
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
			catch (StringIndexOutOfBoundsException e){
				thrown = true;
			}
			catch (InvalidIdentifierException e)
			{
				thrown = true;
			}
			assertEquals(true, thrown);		
		}	
	}
	
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
				File folder = new File("src/Test/TestPrograms/parserAndScanner");
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
			try 
			{
				Scanner testScanner = new Scanner(InputTester.readFile(testPath));
				System.out.println(testPath);
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
			catch (StringIndexOutOfBoundsException e){
				thrown = true;
			}
			catch (InvalidIdentifierException e)
			{
				thrown = true;
			}
			assertEquals(true, thrown);
		}		
	}
	
	
	
	
}
