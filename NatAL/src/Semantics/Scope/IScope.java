package Semantics.Scope;

public interface IScope {
    void AddSymbol(Symbol entry) throws Error;
    Symbol FindSymbol(String identifier);
    void AddChildScope(Scope child);
    IScope GetParent();
    int GetDepth();
}
