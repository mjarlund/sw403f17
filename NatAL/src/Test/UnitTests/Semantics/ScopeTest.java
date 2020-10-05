package Test.UnitTests.Semantics;

import DataStructures.AST.AST;
import Exceptions.*;
import Semantics.Scope.SemanticAnalyzer;
import Syntax.Parser.Parser;
import Syntax.Scanner.Scanner;
import Test.InputTester;
import Utilities.Reporter;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class ScopeTest {
	
	
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
				File folder = new File("src/Test/TestPrograms/semantics/");
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
				  Reporter.Log("Error when reading files: " + e.getMessage());
			}
			return data;
		}
		
		@Test
		public void NoExceptionsThrownTest() throws IOException {
			
			Throwable e = null;		
			try 
			{
				Scanner testScanner = new Scanner(InputTester.readFile(testPath));
				Reporter.Log(testPath);
				Parser parser = new Parser(testScanner);
				AST programTree = parser.ParseProgram();
			    SemanticAnalyzer sm = new SemanticAnalyzer();
			    sm.BeginSemanticAnalysis(programTree);
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
				File folder = new File("src/Test/TestPrograms/semantics/");
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
				  Reporter.Log("Error when reading files: " + e.getMessage());
			}
			return data;
		}

		@Test
		public void ExceptionssuccessfullyThrownTest() throws IOException {
			
			boolean thrown = false;		
			try 
			{
				Scanner testScanner = new Scanner(InputTester.readFile(testPath));
				Reporter.Log(testPath);
				Parser parser = new Parser(testScanner);
				AST programTree = parser.ParseProgram();
			    SemanticAnalyzer sm = new SemanticAnalyzer();
			    sm.BeginSemanticAnalysis(programTree);
			} 
			catch (UndeclaredSymbolException ex)
			{					
				thrown = true;
			}			
			catch (DuplicatedSymbolException ex)
			{
				thrown = true;
			}
			catch (IncompatibleValueException ex)
			{
				thrown = true;
			}
			catch (InvalidScopeException ex)
			{
				thrown = true;
			}
			catch (InvalidTypeException ex)
			{
				thrown = true;
			}
			catch (ArgumentsException ex)
			{
				thrown = true;
			}
			catch (UnexpectedTokenException ex)
			{
				thrown = true;
			}
			catch (MissingProductionsException e){
				thrown = true;
			}
			assertEquals(true, thrown);			
		}		
	}
	
	
	
	
}
