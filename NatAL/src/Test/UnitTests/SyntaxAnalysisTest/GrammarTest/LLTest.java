package Test.UnitTests.SyntaxAnalysisTest.GrammarTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import Syntax.Grammar.Grammar;
import Syntax.Grammar.Production;
@RunWith(Enclosed.class)
public class LLTest {

	@RunWith(Parameterized.class)
	public static class LLCheck {
		
		private boolean expected;
		private String path;
		
		public LLCheck(String input, boolean expectedBool)
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
        public void IsLLTest() {
		    boolean result = IsLL();
            assertEquals(expected, result);
        }

		public boolean IsLL() {
            try {
                Grammar g = Grammar.FromFile(path);
                List<Production> productions = new LinkedList<>(Arrays.asList(g.Productions));
                Set<String> tmpSet = new HashSet<>();


                while (!productions.isEmpty()) {
                    tmpSet.clear();
                    Boolean tmpResult = true;

                    String left = productions.get(0).Left;
                    String right = productions.get(0).Right[0];
                    productions.remove(0);


                    if (g.Epsilon.get(left)) tmpSet.addAll(g.Follow(left));
                    for (String firstVal : g.First(right, false)) {
                        if (tmpSet.contains(firstVal)) {
                            tmpResult = false;
                            break;
                        }
                        tmpSet.add(firstVal);
                    }

                    List<Production> garbage = new LinkedList<>();

                    for(Production innerProduction : productions) {
                        if (innerProduction.Left.equals(left)) {
                            if (tmpResult.equals(false)) {
                                return false;
                            }

                            String innerRight = innerProduction.Right[0];
                            garbage.add(innerProduction);

                            for (String firstVal : g.First(innerRight, false)) {
                                if (tmpSet.contains(firstVal)) {
                                    return false;
                                }
                                tmpSet.add(firstVal);
                            }
                        }
                    }

                    productions.removeAll(garbage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

	}

}
