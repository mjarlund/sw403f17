package Test.UnitTests.SyntaxAnalysisTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import Syntax.Grammar.Grammar;
import Syntax.Grammar.Production;

@RunWith(Enclosed.class)
public class GrammarTest {
	
	@RunWith(Parameterized.class)
	public static class LL1Check{
		
		private boolean expected;
		private String path;
		private boolean result;
		
		public LL1Check(String input, boolean expectedBool)
		{
			this.path = input;
			this.expected = expectedBool;
		}

		@Parameters(name = "{index}: Scan({0})={1}")
		public static Collection<Object[]> generateData()
		{
			return Arrays.asList(new Object[][]{
				{"src/Test/GrammaTestFile", true},
				{"src/Syntax/Grammar/CFG/CFG", true},
			});
		}
		@Test
		public void IsLL1() {
			try{
				Grammar g = Grammar.FromFile(path);
				String left = g.Productions[0].Left;
				Set<String> symbols = new HashSet<>();
				Set<String> tmpSet = new HashSet<>();

				for(Production p : g.Productions) {
					if(left.equals(p.Left)) {
						if(p.Right[0].equals("EPSILON")) tmpSet.addAll(g.Follow(p.Left));
						else tmpSet.addAll(g.First(p.Right[0], true));

						for(String s : tmpSet) {
							if(symbols.contains(s)) result = false;
							symbols.add(s);
						}
					} else {
						symbols.clear();
						if(p.Right[0].equals("EPSILON")) symbols.addAll(g.Follow(p.Left));
						else symbols.addAll(g.First(p.Right[0], true));
					}
					tmpSet.clear();
					left = p.Left;
				}
				result = true;
			} catch (IOException e) {     
			}
			assertEquals(expected, result);
		}
	}
	public static class InitializeSetTest{
		@Test
		public void InitializeTerminalsTest(){
			try {
				Set<String> Terminals = new HashSet<>();
				Grammar g;
				Terminals.add("EPSILON");
				Terminals.add("1");
				Terminals.add("$");
				Terminals.add("+");
				Terminals.add("IntegerLiteral");
				g = Grammar.FromFile("src/Test/GrammaTestFile");
			assertEquals(Terminals,g.Terminals);
			} catch(IOException e) {
			}
		}
		@Test
		public void InitializeNonTerminalsTest(){
			try {
				Set<String> NonTerminals = new HashSet<>();
				Grammar g;
				NonTerminals.add("Program");
				NonTerminals.add("Statements");
				NonTerminals.add("Statement");
				g = Grammar.FromFile("src/Test/GrammaTestFile");
			assertEquals(NonTerminals,g.NonTerminals);
			} catch(IOException e) {
			}
		}
		@Test
		public void InitializeSymbolsTest(){
			try {
				Set<String> Symbols = new HashSet<>();
				Grammar g;
				Symbols.add("Program");
				Symbols.add("Statements");
				Symbols.add("Statement");
				Symbols.add("EPSILON");
				Symbols.add("1");
				Symbols.add("$");
				Symbols.add("+");
				Symbols.add("IntegerLiteral");
				g = Grammar.FromFile("src/Test/GrammaTestFile");
			assertEquals(Symbols,g.Symbols);
			} catch(IOException e) {
			}
		}
		@Test
		public void FirstSetTest(){
			try {
				Set<String> set = new HashSet<>();
				Grammar g;
				set.add("1");
				set.add("+");
				set.add("IntegerLiteral");
				g = Grammar.FromFile("src/Test/GrammaTestFile");
			assertEquals(set,g.First("Statement", false));
			} catch(IOException e) {
			}
		}
	}
}
