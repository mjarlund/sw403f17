using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Lexer
{
    public class Program
    {
        static void Main(string[] args)
        {
            var input = "abc234 = -11.23230 + 22 -33 =  22 equals 3 or 2 false \"sdgf23423`?KLk23j 8 +35j825j,28+ +2d\"";

            // Define token definitions
            var tokenDefinitions = new List<TokenDefinition>
            {
                new TokenDefinition(new Regex("-?[0-9]+\\.[0-9]+"), "float", false),
                new TokenDefinition(new Regex("-?[0-9]+"), "number", false),
                new TokenDefinition(new Regex("\".+\""), "string", false),
                new TokenDefinition(new Regex("\'.+\'"), "char", false),
                new TokenDefinition(new Regex("(true|false)"), "boolean", false),
                new TokenDefinition(new Regex("[*|/|+|-]"), "arithmetic operator", false),
                new TokenDefinition(new Regex("(equals|over|under)"), "relational operator", false),
                new TokenDefinition(new Regex("(or|and|not)"), "logical operator", false),
                new TokenDefinition(new Regex("(=)"), "assignment operator", false),
                new TokenDefinition(new Regex("(until|if|else|number|fraction|character|string|boolean|return|void|structure)"), "keyword", false),
                new TokenDefinition(new Regex("([a-z]|[A-Z])+([a-z]|[A-Z]|[0-9])*"), "identifier", false),
                new TokenDefinition(new Regex("[ \t\f\r\n]+"), "whitespace", true)
            };

            // Tokenize the input string
            var result = Tokenize(input, tokenDefinitions);
            foreach(var token in result)
            {
                Console.WriteLine(token);
            }
            Console.ReadKey();
        }
        
        public static IEnumerable<Token> Tokenize(string source, IEnumerable<TokenDefinition> tokenDefinitions)
        {
            int currentIndex = 0;

            while (currentIndex < source.Length)
            {
                TokenDefinition matchedDefinition = null;
                int matchLength = 0;

                foreach (var rule in tokenDefinitions)
                {
                    var match = rule.Regex.Match(source, currentIndex);

                    if (match.Success && (match.Index - currentIndex) == 0)
                    {
                        matchedDefinition = rule;
                        matchLength = match.Length;
                        break;
                    }
                }

                if (matchedDefinition == null)
                {
                    throw new Exception(string.Format("Unrecognized symbol '{0}' at index {1}.", source[currentIndex], currentIndex));
                }
                else
                {
                    var value = source.Substring(currentIndex, matchLength);

                    if (!matchedDefinition.IsIgnored)
                        yield return new Token(matchedDefinition.Type, value);
                    
                    currentIndex += matchLength;
                }
            }

            yield return new Token("end", null);
        }
    }


    public class TokenDefinition
    {
        public TokenDefinition(Regex regex, string type, bool isIgnored)
        {
            this.IsIgnored = isIgnored;
            this.Regex = regex;
            this.Type = type;
        }

        public bool IsIgnored { get; set; }
        public Regex Regex { get; set; }
        public string Type { get; set; }
    }

    public class Token
    {
        public Token(string type, string value)
        {
            this.Type = type;
            this.Value = value;
        }
        
        public string Type { get; set; }
        public string Value { get; set; }

        public override string ToString()
        {
            return "(" + Type + " " + "\"" + Value + "\"" + ")";
        }
    }
}
