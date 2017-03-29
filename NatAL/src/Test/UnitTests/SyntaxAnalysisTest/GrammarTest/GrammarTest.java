package Test.UnitTests.SyntaxAnalysisTest.GrammarTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import Syntax.Grammar.Grammar;
import Syntax.Grammar.Production;

@RunWith(Enclosed.class)
public class GrammarTest {
	
	public static class InitializeSetTest{
		private Grammar g;
		@Before
		public void setUp() throws Exception {
			
			try{
			g = Grammar.FromFile("src/Test/GrammarTestFile");
	        
			} catch(IOException e){
				fail("Error: " + e);
			}
		}
		@Test
		public void InitializeTerminalsTest(){
				Set<String> Terminals = new HashSet<>();
				Terminals.add("EPSILON");
				Terminals.add("1");
				Terminals.add("$");
				Terminals.add("+");
			assertEquals(Terminals,g.Terminals);
		}
		@Test
		public void InitializeNonTerminalsTest(){
				Set<String> NonTerminals = new HashSet<>();
				NonTerminals.add("Program");
				NonTerminals.add("Statements");
				NonTerminals.add("Statement");
			assertEquals(NonTerminals,g.NonTerminals);
		}
		@Test
		public void InitializeSymbolsTest(){
				Set<String> Symbols = new HashSet<>();
				Symbols.add("Program");
				Symbols.add("Statements");
				Symbols.add("Statement");
				Symbols.add("EPSILON");
				Symbols.add("1");
				Symbols.add("$");
				Symbols.add("+");
			assertEquals(Symbols,g.Symbols);
		}
		@Test
		public void FirstSetTest(){
				Set<String> set = new HashSet<>();
				set.add("1");
				set.add("+");
				assertEquals(set,g.First("Statement", false));
		}
		@Test
		public void FollowSetTest(){
				Set<String> set = new HashSet<>();
				set.add("1");
				set.add("+");
				set.add("$");
				assertEquals(set,g.Follow("Statement"));
		}
	}
}
