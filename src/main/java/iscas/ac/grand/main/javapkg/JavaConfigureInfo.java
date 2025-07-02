package iscas.ac.grand.main.javapkg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaConfigureInfo {
    public static final int starnum=1;//*最大个数
    public static final int addnum=1;//+最大个数
    public static final int charnum=255;//随机生成字符的范围
    public static final int cycleStepLength=2;//终止性参数，循环生成时次数的控制，堆栈溢出报错强相关
    public static final int cycleStepLengthMax=2;//终止性参数上限，防止配置的参数过大，循环生成时次数的控制，堆栈溢出报错强相关
    public static final int terminateMaxFindLen=2;//终止路径寻找的最大路径长度
    public static int needsplitNum=0;//包含需要拆分才能跳出循环的变量的文法个数
    public static int moreThanOneRoot=0;//含有多个可选根节点的文法的数量
    public static boolean ifSplitOr=false;//是否打开分割竖线的开关，false,不打开，true，打开
    public static boolean ifFormatJavaCode=true;//生成的java代码是否按照google-java-format来格式化
    public static boolean isFangzhouMulti=false;//是否为方舟编译器生成批量的测试用例
    public static String identifierToken="identifier";//表示标识符的token
    public static final String fieldDeclaration="typeType identifier  ('=' expression) ';'";//声明全局变量的文法
    public static final String localDeclaration="typeType identifier ('=' expression)";//声明局部变量的文法
    public static final String methodDeclaration="typeTypeOrVoid identifier formalParameters methodBody";//方法声明文法token
    public static final String publicClassDeclaration="CLASS identifier includeMainClassBody";//public类class声明文法token
    public static final String classDeclaration="CLASS identifier classBody";//类class声明文法token
    public static List<String> scopeGrammars=new ArrayList<>();//作用域在文法中的标识符
    public static List<String> keyWords=new ArrayList<String>();//java关键字列表
    public static final String mainContent="mainContent";//main函数内容所对应的文法
    public static final String mainMethodDeclaration="mainMethodDeclaration";//main函数包括函数头和尾对应的文法（用于变异程序识别全局插入位置点）
    public static int loopCountMax=2;//用于防止死循环的计数变量最大值
    public static String loopBreak="break";//用于防止死循环的break的文法
    public static String loopCountType="int";//用于防止死循环的计数变量类型
    public static final String blockDeclaration="block";//块声明文法token

    public static final List<String> statementGrammars=new ArrayList<>();//新的语句文法
    public static void setStatementGrammars() {
        statementGrammars.add("statement");
        statementGrammars.add("blockStatement");
    }
    
    public static final List<String> defineOrModifyGrammars=new ArrayList<>();//定义或修改标识符的文法(用于程序简化)
    public static void setDefineOrModifyGrammars() {
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
    


    public static List<String> divisorGrammars=new ArrayList<>();//循环文法 for while等
    public static void setDivisorGrammars() {
        divisorGrammars.add("longDivisor");
        divisorGrammars.add("shortDivisor");
        divisorGrammars.add("byteDivisor");
        divisorGrammars.add("integerDivisor");
        divisorGrammars.add("floatDivisor");
    }

    public static List<String> loopGrammars=new ArrayList<>();//循环文法 for while等
    public static void setLoopGrammars() {
        loopGrammars.add("forStatement");
        loopGrammars.add("whileStatement");
    }

    public static void setScopeGrammars() {
        scopeGrammars.add("block");
        scopeGrammars.add("mainMethodBlock");
        scopeGrammars.add("forStatement");
        scopeGrammars.add("loopBlock");
    }

    public static List<String> javaBaseType=new ArrayList<>();//java基本类型
    public static void setJavaBaseType() {
        javaBaseType.add("int");
        javaBaseType.add("float");
        javaBaseType.add("double");
        javaBaseType.add("void");
        javaBaseType.add("String");
        javaBaseType.add("byte");
        javaBaseType.add("short");
        javaBaseType.add("long");
        javaBaseType.add("boolean");
    }

    public static Map<String,String> javaBaseTypeLiteral=new HashMap<>();//java基本类型的生成文法
    public static void setJavaBaseTypeLiteral() {
        javaBaseTypeLiteral.put("int","integerLiteral");
        javaBaseTypeLiteral.put("float","floatLiteral");
        javaBaseTypeLiteral.put("double","floatLiteral");
        javaBaseTypeLiteral.put("boolean","BOOL_LITERAL");
        javaBaseTypeLiteral.put("byte","byteLiteral");
        javaBaseTypeLiteral.put("long","longLiteral");
        javaBaseTypeLiteral.put("short","shortLiteral");
        javaBaseTypeLiteral.put("String","STRING_LITERAL");
        javaBaseTypeLiteral.put("void","");
    }
    public static Map<String,String> javaBaseTypeExp=new HashMap<>();//java基本类型的生成文法
    public static void setJavaBaseTypeExp() {
        javaBaseTypeExp.put("int","integerExpression");
        javaBaseTypeExp.put("float","floatExpression");
        javaBaseTypeExp.put("double","floatExpression");
        javaBaseTypeExp.put("boolean","boolExpression");
        javaBaseTypeExp.put("byte","byteExpression");
        javaBaseTypeExp.put("long","longExpression");
        javaBaseTypeExp.put("short","shortExpression");
        javaBaseTypeExp.put("String","stringExpression");
        javaBaseTypeExp.put("void","");
    }


    public static List<String> varnames=new ArrayList<>();//引用变量标识符的的文法
    public static  void setVarnames(){
        varnames.add("integerVarName");//引用int变量标识符的文法
        varnames.add("byteVarName");
        varnames.add("shortVarName");
        varnames.add("longVarName");
        varnames.add("floatVarName");
        varnames.add("boolVarName");
        varnames.add("stringVarName");
    }

    public static Map<String,String> varnamesMap=new HashMap<>();//基本类型文法和对应的关键字映射表
    public static  void setVarnamesMap(){
        varnamesMap.put("integerVarName","int");
        varnamesMap.put("byteVarName","byte");
        varnamesMap.put("shortVarName","short");
        varnamesMap.put("longVarName","long");
        varnamesMap.put("floatVarName","double");
        varnamesMap.put("boolVarName","boolean");
        varnamesMap.put("stringVarName","String");
    }

    public static List<String> getKeyWords(){
        return keyWords;
    }

    public static List<String> getVarnames(){
        return varnames;
    }

    public static  void setKeyWords(){
        keyWords.add("abstract");
        keyWords.add("transient");
        keyWords.add("try");
        keyWords.add("void");
        keyWords.add("volatile");
        keyWords.add("while");
        keyWords.add("throws");
        keyWords.add("throw");
        keyWords.add("this");
        keyWords.add("synchronized");
        keyWords.add("switch");
        keyWords.add("super");
        keyWords.add("strictfp");
        keyWords.add("static");
        keyWords.add("short");
        keyWords.add("return");
        keyWords.add("public");
        keyWords.add("protected");
        keyWords.add("private");
        keyWords.add("package");
        keyWords.add("new");
        keyWords.add("native");
        keyWords.add("long");
        keyWords.add("instanceof");
        keyWords.add("interface");
        keyWords.add("int");
        keyWords.add("import");
        keyWords.add("implements");
        keyWords.add("if");
        keyWords.add("for");
        keyWords.add("float");
        keyWords.add("finally");
        keyWords.add("final");
        keyWords.add("extends");
        keyWords.add("enum");
        keyWords.add("else");
        keyWords.add("double");
        keyWords.add("do");
        keyWords.add("default");
        keyWords.add("continue");
        keyWords.add("class");
        keyWords.add("char");
        keyWords.add("catch");
        keyWords.add("case");
        keyWords.add("byte");
        keyWords.add("break");
        keyWords.add("boolean");
        keyWords.add("assert");
    }

}
