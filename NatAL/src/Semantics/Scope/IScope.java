package Semantics.Scope;

/**
 * Created by mysjkin on 4/10/17.
 */
public interface IScope {
    void AddSymbol(Symbol entry) throws Error;
    Symbol FindSymbol(String identifier);
    void AddChildScope(Scope child);
    IScope GetParent();
    int GetDepth();
}
