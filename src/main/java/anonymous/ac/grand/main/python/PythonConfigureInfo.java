package anonymous.ac.grand.main.python;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PythonConfigureInfo {
    public static final int starnum=1;//*最大个数
    public static final int addnum=1;//+最大个数
    public static final int charnum=255;//随机生成字符的范围
    public static final int cycleStepLength=2;//终止性参数，循环生成时次数的控制，堆栈溢出报错强相关
    public static final int cycleStepLengthMax=2;//终止性参数上限，防止配置的参数过大，循环生成时次数的控制，堆栈溢出报错强相关
    public static final int terminateMaxFindLen=2;//终止路径寻找的最大路径长度
    public static int needsplitNum=0;//包含需要拆分才能跳出循环的变量的文法个数
    public static int moreThanOneRoot=0;//含有多个可选根节点的文法的数量
    public static boolean ifSplitOr=false;//是否打开分割竖线的开关，false,不打开，true，打开
    public static boolean ifFormatJPythonCode=true;//是否打开python格式化开关
    public static boolean isFangzhouMulti=false;//是否为方舟编译器生成批量的测试用例
    public static String identifierToken="NAME";//表示标识符的token
    public static String tab="TABNEWSTATEMENT";//表示tab符号
    public static String newline="NEWLINE";//表示newline符号
    public static final String varDeclaration="identifier ('=' expression) semicolon? NEWLINE";//声明变量的文法
    public static final String methodDeclaration="DEF identifier formalParameters methodType? ':' NEWLINE methodBody";//方法声明文法token
    public static final String classDeclaration="CLASS identifier '():' NEWLINE classBody";//类class声明文法token
    public static final String mainClassDeclaration="CLASS identifier ':' NEWLINE mainMethodDeclaration";//主方法类class声明文法token

    public static List<String> varnames=new ArrayList<>();//引用变量标识符的的文法
    public static List<String> scopeGrammars=new ArrayList<>();//作用域在文法中的标识符
    public static List<String> keyWords=new ArrayList<String>();//java关键字列表
    public static final String mainContent="mainContent";//main函数内容所对应的文法
    public static final String mainMethodDeclaration="mainMethodBlock";//main函数包括函数头和尾对应的文法（用于变异程序识别全局插入位置点）
    public static String type="typeType";//类型文法
    public static int loopCountMax=2;//用于防止死循环的计数变量最大值
    public static String loopBreak="break";//用于防止死循环的break的文法
    public static String loopCountType="int";//用于防止死循环的计数变量类型
    public static final String blockDeclaration="block";//块声明文法token

    
    public static final List<String> defineOrModifyGrammars=new ArrayList<>();//定义或修改标识符的文法(用于程序简化)
    public static void setDefineOrModifyGrammars() {
    }
    
    public static final List<String> statementGrammars=new ArrayList<>();//新的语句文法
    public static void setStatementGrammars() {
        statementGrammars.add("statement");
        statementGrammars.add("elseStatement");
        statementGrammars.add("methodDeclaration");
        statementGrammars.add("blockStatement");
        statementGrammars.add("localVariableDeclaration");
    }

    public static final List<String> mthdStatementGrammars=new ArrayList<>();//方法语句文法（作用域深度不变）
    public static void setMthdStatementGrammars() {
        mthdStatementGrammars.add("methodDeclaration");
        mthdStatementGrammars.add("classDeclaration");
    }
    
    public static List<String> scopeBodyGrammars=new ArrayList<>();//整个作用域（包括花括号外的if,for,while,方法头等等）
    public static void setscopeBodyGrammars() {
    	scopeBodyGrammars.add("whileStatement");
    	scopeBodyGrammars.add("forStatement");
    	scopeBodyGrammars.add("ifStatement");
    	scopeBodyGrammars.add("elseStatement");
    	scopeBodyGrammars.add("methodDeclaration");
    	scopeBodyGrammars.add("mainMethodDeclaration");
        
    }
    

    public static List<String> divisorGrammars=new ArrayList<>();//除数文法等
    public static void setDivisorGrammars() {
        divisorGrammars.add("byteDivisor");
        divisorGrammars.add("integerDivisor");
        divisorGrammars.add("floatDivisor");
    }

    public static List<String> loopGrammars=new ArrayList<>();//循环文法 for while等
    public static void setLoopGrammars() {
        loopGrammars.add("forStatement");
        loopGrammars.add("whileStatement");
    }

    public static void setScopeGrammars() {//作用域文法
        scopeGrammars.add("block");
        scopeGrammars.add("mainMethodBlock");
        scopeGrammars.add("loopBlock");
//        scopeGrammars.add("elseStatement");
        scopeGrammars.add("classBody");
    }

    public static List<String> pythonBaseType=new ArrayList<>();//python基本类型
    public static void setPythonBaseType() {
        pythonBaseType.add("int");
        pythonBaseType.add("void");
        pythonBaseType.add("bytes");
        pythonBaseType.add("bool");
    }
    public static Map<String,String> varnamesMap=new HashMap<>();//基本类型文法和对应的关键字映射表
    public static  void setVarnamesMap(){
        varnamesMap.put("integerVarName","int");
        varnamesMap.put("byteVarName","bytes");
        varnamesMap.put("floatVarName","float");
        varnamesMap.put("boolVarName","bool");
        varnamesMap.put("stringVarName","String");
    }
    public static Map<String,String> pythonBaseTypeLiteral=new HashMap<>();//python基本类型的生成文法
    public static void setPythonBaseTypeLiteral() {
        pythonBaseTypeLiteral.put("int","integerLiteral");
        pythonBaseTypeLiteral.put("float","floatLiteral");
        pythonBaseTypeLiteral.put("double","floatLiteral");
        pythonBaseTypeLiteral.put("bool","BOOL_LITERAL");
        pythonBaseTypeLiteral.put("bytes","byteLiteral");
        pythonBaseTypeLiteral.put("String","STRING_LITERAL");
        pythonBaseTypeLiteral.put("void","");
    }
    public static Map<String,String> pythonBaseTypeExp=new HashMap<>();//python基本类型的生成文法
    public static void setPythonBaseTypeExp() {
        pythonBaseTypeExp.put("int","integerExpression");
        pythonBaseTypeExp.put("float","floatExpression");
        pythonBaseTypeExp.put("double","floatExpression");
        pythonBaseTypeExp.put("bool","boolExpression");
        pythonBaseTypeExp.put("bytes","byteExpression");
        pythonBaseTypeExp.put("String","stringExpression");
        pythonBaseTypeExp.put("void","");
    }



    public static  void setVarnames(){
        varnames.add("integerVarName");//引用int变量标识符的文法
        varnames.add("byteVarName");
        varnames.add("floatVarName");
        varnames.add("boolVarName");
        varnames.add("stringVarName");
    }

    public static List<String> getKeyWords(){
        return keyWords;
    }

    public static List<String> getVarnames(){
        return varnames;
    }

    public static  void setKeyWords(){
        keyWords.add("False");
        keyWords.add("class");
        keyWords.add("finally");
        keyWords.add("is");
        keyWords.add("return");
        keyWords.add("None");
        keyWords.add("continue");
        keyWords.add("for");
        keyWords.add("lambda");
        keyWords.add("try");
        keyWords.add("True");
        keyWords.add("def");
        keyWords.add("from");
        keyWords.add("nonlocal");
        keyWords.add("while");
        keyWords.add("and");
        keyWords.add("del");
        keyWords.add("global");
        keyWords.add("not");
        keyWords.add("with");
        keyWords.add("as");
        keyWords.add("elif");
        keyWords.add("if");
        keyWords.add("or");
        keyWords.add("yield");
        keyWords.add("assert");
        keyWords.add("else");
        keyWords.add("import");
        keyWords.add("pass");
        keyWords.add("break");
        keyWords.add("except");
        keyWords.add("in");
        keyWords.add("raise");
    }

}
