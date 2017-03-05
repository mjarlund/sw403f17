package p4test.AST.Nodes.expr;

import p4test.Token;

import java.util.List;

/**
 * Created by mysjkin on 3/4/17.
 */
public class FuncCommand extends Expr
{
    public final String funcId;
    public final List<Expr> funcArgs;
    public FuncCommand(Token token, List<Expr> args)
    {
        super(token);
        funcId = token.Value;
        funcArgs = args;
    }
}
