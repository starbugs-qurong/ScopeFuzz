package iscas.ac.grand.mutate.cplus14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CPlusConfigureInfo {
    public static final int starnum=1;//*最大个数
    public static final int addnum=1;//+最大个数
    public static final int charnum=255;//随机生成字符的范围
    public static final int cycleStepLength=2;//终止性参数，循环生成时次数的控制，堆栈溢出报错强相关
    public static final int cycleStepLengthMax=2;//终止性参数上限，防止配置的参数过大，循环生成时次数的控制，堆栈溢出报错强相关
    public static final int terminateMaxFindLen=2;//终止路径寻找的最大路径长度
    public static int needsplitNum=0;//包含需要拆分才能跳出循环的变量的文法个数
    public static int moreThanOneRoot=0;//含有多个可选根节点的文法的数量
    public static boolean ifSplitOr=false;//是否打开分割竖线的开关，false,不打开，true，打开
    public static String identifierToken="identifier";//表示标识符的token
    public static final String fieldDeclaration="primitiveType identifier  ('=' expression) ';'";//声明全局变量的文法
    public static final String localDeclaration="primitiveType identifier ('=' expression)";//声明局部变量的文法
    public static final String fieldArrayDeclaration="INT identifier '[' arrayLength ']' ('=' '{' arrayExpression '}') ';'";//声明全局数组变量的文法
    public static final String localArrayDeclaration="INT identifier '[' arrayLength ']' ('=' '{' arrayExpression '}')";//声明局部数组变量的文法
    public static final String fieldPointerDeclaration="INT '*' identifier ('=' pointerExpression ) ';'";//声明全局指针变量的文法
    public static final String localPointerDeclaration="INT '*' identifier ('=' pointerExpression )";//声明局部指针变量的文法
    public static final String lamdaExpression="renameLamda lamdaCaptureList lamdaParameterList lamdaSpecifiersException lamdaType lamdaFunctionBody callParameter";//声明lamda表达式的文法
    public static final String classDeclaration="CLASS identifier classBody";//类class声明文法token
    public static final String structDeclaration="STRUCT identifier structBody";//结构体struct声明文法token
    public static final String templateFunDeclaration="templateMethodDeclaration";//模板函数声明文法token
    
    
    public static final String arrayElement="arrayElement";//取数组元素的文法
    public static final String pointerValue="pointerValue";//取指针中的值的文法
    public static final String arrayVarName="arrayVarName";//取数组名称的文法
    public static final String pointerVarName="pointerVarName";//取指针名称的文法
    public static final String methodDeclaration="typeTypeOrVoid identifier formalParameters methodBody";//方法声明文法token
    public static final String methodDeclarationTotal="methodDeclaration";//方法声明文法token
    public static final String blockDeclaration="block";//块声明文法token
    
    
    
    public static List<String> varnames=new ArrayList<>();//引用变量标识符的的文法
    public static List<String> scopeGrammars=new ArrayList<>();//作用域在文法中的标识符
    public static List<String> keyWords=new ArrayList<String>();//c关键字列表
    public static final String mainContent="mainContent";//main函数内容所对应的文法
    public static final String mainMethodDeclaration="mainMethodDeclaration";//main函数包括函数头和尾对应的文法（用于变异程序识别全局插入位置点）
    public static String loopCountType="int";//用于防止死循环的计数变量类型
    public static int loopCountMax=2;//用于防止死循环的计数变量最大值
    public static String loopBreak="break";//用于防止死循环的break的文法
    public static int arrayLength=10;//数组的最大长度
    public static String insertPositionVar="GlobalVariable";//插入全局变量的位置（和指针的定义相关）
    public static String insertPositionPointer="GlobalPointerVariable";//插入全局指针变量的位置（和指针的定义相关）
    private static boolean mutationAvailable=false;//用于变异程序 是否需要变异 true或false 默认为false
    private static String selectedMutationStrategy="no mutation";//用于变异程序 变异的具体策略 "no mutation", "main mutation", "scope mutation"

    public static List<String> mutationStrategies=new ArrayList<>();
    public static void setMutationStrategies() {
        mutationStrategies.add("main mutation");//main函数级别的变异
        mutationStrategies.add("scope mutation");//作用域级别的变异
    }
    
    public static final List<String> defineOrModifyGrammars=new ArrayList<>();//定义或修改标识符的文法(用于程序简化)
    public static void setDefineOrModifyGrammars() {
    	defineOrModifyGrammars.add("updateExpression");//更新标识符取值
    	defineOrModifyGrammars.add("fieldDeclaration");//全局变量定义
    	defineOrModifyGrammars.add("arrayDeclaration");//数组定义
    	defineOrModifyGrammars.add("pointerDeclaration");//指针定义
    	defineOrModifyGrammars.add("localVariableDeclaration");//局部变量定义
    }


    public static final List<String> statementGrammars=new ArrayList<>();//新的语句文法
	public static final String Typename = "int";//template中的typename在实例化对象时的类型
    public static void setStatementGrammars() {
        statementGrammars.add("statement");
        statementGrammars.add("blockStatement");
        statementGrammars.add("memberDeclaration");
        statementGrammars.add("fieldDeclaration");
        statementGrammars.add("localVariableDeclaration");
        statementGrammars.add("elseStatement");
    }

    public static List<String> divisorGrammars=new ArrayList<>();//除法文法等
    public static void setDivisorGrammars() {
        divisorGrammars.add("longDivisor");
        divisorGrammars.add("shortDivisor");
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
        scopeGrammars.add("structBody");
        scopeGrammars.add("classBody");
    }
    

    public static List<String> scopeBodyGrammars=new ArrayList<>();//整个作用域（包括花括号外的if,for,while,方法头等等）
    public static void setscopeBodyGrammars() {
    	scopeBodyGrammars.add("whileStatement");
    	scopeBodyGrammars.add("forStatement");
    	scopeBodyGrammars.add("ifStatement");
    	scopeBodyGrammars.add("elseStatement");
    	scopeBodyGrammars.add("methodDeclaration");
    	scopeBodyGrammars.add("lamdaExpression");
    	scopeBodyGrammars.add("mainMethodDeclaration");
        
    }
    

    public static List<String> cPlusBaseType=new ArrayList<>();//c基本类型
    public static void setCBaseType() {
    	cPlusBaseType.add("int");
    	cPlusBaseType.add("float");
    	cPlusBaseType.add("double");
    	cPlusBaseType.add("void");
    	cPlusBaseType.add("short");
    	cPlusBaseType.add("long");
    	cPlusBaseType.add("bool");
    	cPlusBaseType.add("int*");
    	cPlusBaseType.add("int[]");
    }

    public static Map<String,String> cPlusBaseTypeLiteral=new HashMap<>();//c基本类型的生成文法
    public static void setCBaseTypeLiteral() {
    	cPlusBaseTypeLiteral.put("int","integerLiteral");
    	cPlusBaseTypeLiteral.put("float","floatLiteral");
    	cPlusBaseTypeLiteral.put("double","floatLiteral");
    	cPlusBaseTypeLiteral.put("bool","BOOL_LITERAL");
    	cPlusBaseTypeLiteral.put("long","longLiteral");
    	cPlusBaseTypeLiteral.put("short","shortLiteral");
    	cPlusBaseTypeLiteral.put("int*","pointerLiteral");
    	cPlusBaseTypeLiteral.put("int[]","arrayLiteral");
    	cPlusBaseTypeLiteral.put("void","");
    }
    public static Map<String,String> cPlusBaseTypeExp=new HashMap<>();//c基本类型的生成文法
    public static void setCBaseTypeExp() {
    	cPlusBaseTypeExp.put("int","integerExpression");
    	cPlusBaseTypeExp.put("float","floatExpression");
    	cPlusBaseTypeExp.put("double","floatExpression");
    	cPlusBaseTypeExp.put("bool","boolExpression");
    	cPlusBaseTypeExp.put("long","longExpression");
    	cPlusBaseTypeExp.put("short","shortExpression");
    	cPlusBaseTypeExp.put("int*","pointerExpression");
    	cPlusBaseTypeExp.put("int[]","arrayExpression");
    	cPlusBaseTypeExp.put("void","");
    }

    public static Map<String,String> varnamesMap=new HashMap<>();//基本类型文法和对应的关键字映射表
    public static  void setVarnamesMap(){
        varnamesMap.put("integerVarName","int");
        varnamesMap.put("shortVarName","short");
        varnamesMap.put("longVarName","long");
        varnamesMap.put("floatVarName","float");
        varnamesMap.put("pointerVarName","int*");
        varnamesMap.put("arrayVarName","int[]");
        varnamesMap.put("boolVarName","int");//c语言没有bool,这里调整为int
    }
    
    public static Map<String,String> varnameCategoryMap=new HashMap<>();//变量类型映射表
    public static  void setVarnameCategoryMap(){
    	varnameCategoryMap.put("integerVarName","var");
    	varnameCategoryMap.put("shortVarName","var");
    	varnameCategoryMap.put("longVarName","var");
    	varnameCategoryMap.put("floatVarName","var");
    	varnameCategoryMap.put("pointerVarName","pointer");
    	varnameCategoryMap.put("arrayVarName","array");
    	varnameCategoryMap.put("boolVarName","var");//c语言没有bool,这里调整为int
    }

    public static  void setVarnames(){
        varnames.add("integerVarName");//引用int变量标识符的文法
        varnames.add("shortVarName");
        varnames.add("longVarName");
        varnames.add("floatVarName");
        varnames.add("boolVarName");
        varnames.add("pointerVarName");
        varnames.add("arrayVarName");
    }

    public static List<String> getKeyWords(){
        return keyWords;
    }

    public static List<String> getVarnames(){
        return varnames;
    }

    public static  void setKeyWords(){
        keyWords.add("void");
        keyWords.add("while");
        keyWords.add("this");
        keyWords.add("switch");
        keyWords.add("short");
        keyWords.add("return");
        keyWords.add("new");
        keyWords.add("native");
        keyWords.add("long");
        keyWords.add("int");
        keyWords.add("if");
        keyWords.add("for");
        keyWords.add("float");
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
        keyWords.add("bool");
        keyWords.add("assert");
    }

}
