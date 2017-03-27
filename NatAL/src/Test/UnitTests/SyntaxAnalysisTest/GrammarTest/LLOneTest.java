package Test.UnitTests.SyntaxAnalysisTest.GrammarTest;

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
public class LLOneTest {

	@RunWith(Parameterized.class)
	public static class LLOneCheck{
		
		private boolean expected;
		private String path;
		private boolean result;
		
		public LLOneCheck(String input, boolean expectedBool)
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
		public void IsLLOne() {
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

}
