package iscas.ac.grand.main;
import iscas.ac.grand.main.common.ScopeTree;
import iscas.ac.grand.main.common.SymbolTable;

public class GenerateResult {
    private String codeSegment="";
    ScopeTree scope;
    SymbolTable table;

    public String getCodeSegment() {
        return codeSegment;
    }

    public void setCodeSegment(String codeSegment) {
        this.codeSegment = codeSegment;
    }

    public ScopeTree getScope() {
        return scope;
    }

    public void setScope(ScopeTree scope) {
        this.scope = scope;
    }

    public SymbolTable getTable() {
        return table;
    }

    public void setTable(SymbolTable table) {
        this.table = table;
    }
}
