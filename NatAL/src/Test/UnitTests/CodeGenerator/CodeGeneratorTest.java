package Test.UnitTests.CodeGenerator;

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

import CodeGeneration.CodeGenerator;
import DataStructures.AST.AST;
import Exceptions.UndeclaredSymbolException;
import Semantics.Scope.SemanticAnalyzer;
import Syntax.Parser.Parser;
import Syntax.Scanner.Scanner;
import Test.InputTester;

@RunWith(Enclosed.class)
public class CodeGeneratorTest {

	@RunWith(Parameterized.class)
	public static class CodeCanBeGeneratedTests
	{
	
		private String testPath;


		public CodeCanBeGeneratedTests(String path)
		{
			this.testPath = path;
		}
		
		
		@Parameters(name = "{index}: Generated Code for: ({0})")
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
				  System.out.println("Error when reading files: " + e.getMessage());
			}
			return data;
			
		}
		
		@Test
		public void CanGenerateCodeTest() throws IOException {
			
			Throwable e = null;		
			try 
			{
				Scanner testScanner = new Scanner(InputTester.readFile(testPath));
				Parser parser = new Parser(testScanner);
				AST programTree = parser.ParseProgram();
			    SemanticAnalyzer sm = new SemanticAnalyzer();
			    sm.VisitChildren(programTree);
			    CodeGenerator c = new CodeGenerator(programTree, sm);
			} 
			catch (Throwable ex)
			{					
				e = ex;	
				ex.printStackTrace();
			}			
			assertEquals(null, e);
		}		
	} 
}
