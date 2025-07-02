package iscas.ac.grand.main.common;

public class GrammarInfo {
    private String grammarName="";//文法的名称
    private boolean isGrammar=false;//当前文件是否为一个完整的文法文件
    private boolean isParser=false;//当前文法是否为语法文件
    private boolean isLexer=false;//当前文法是否为词法文件
    private String tokenVocab="";//文法需要引用的的词法文件 如果有的话

    public GrammarInfo(String grammarName, boolean isGrammar, boolean isParser, boolean isLexer, String tokenVocab) {
        this.grammarName = grammarName;
        this.isGrammar = isGrammar;
        this.isParser = isParser;
        this.isLexer = isLexer;
        this.tokenVocab = tokenVocab;
    }

    public String getGrammarName() {
        return grammarName;
    }

    public void setGrammarName(String grammarName) {
        this.grammarName = grammarName;
    }

    public boolean isGrammar() {
        return isGrammar;
    }

    public void setGrammar(boolean grammar) {
        isGrammar = grammar;
    }

    public boolean isParser() {
        return isParser;
    }

    public void setParser(boolean parser) {
        isParser = parser;
    }

    public boolean isLexer() {
        return isLexer;
    }

    public void setLexer(boolean lexer) {
        isLexer = lexer;
    }

    public String getTokenVocab() {
        return tokenVocab;
    }

    public void setTokenVocab(String tokenVocab) {
        this.tokenVocab = tokenVocab;
    }
}
