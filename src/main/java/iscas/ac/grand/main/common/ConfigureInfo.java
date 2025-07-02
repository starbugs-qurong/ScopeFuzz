package iscas.ac.grand.main.common;
import iscas.ac.grand.main.c.CConfigureInfo;
import iscas.ac.grand.main.python.PythonConfigureInfo;
import iscas.ac.grand.mutate.cplus14.CPlusConfigureInfo;
import iscas.ac.grand.main.javapkg.JavaConfigureInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigureInfo {
    public static int needsplitNum=0;//包含需要拆分才能跳出循环的变量的文法个数
    public static int moreThanOneRoot=0;//含有多个可选根节点的文法的数量
    public static boolean ifCCode=false;//生成C代码
    public static boolean ifPythonCode=false;//生成python代码
    public static boolean ifJavaCode=false;//生成Java代码
    public static boolean ifCPlusCode=false;//生成C++代码
    public static int starnum=1;//*最大个数
    public static int addnum=1;//+最大个数
    public static int cycleStepLength=3;//终止性参数，循环生成时次数的控制，堆栈溢出报错强相关
    public static final int cycleStepLengthMax=3;//终止性参数上限，防止配置的参数过大，循环生成时次数的控制，堆栈溢出报错强相关
    public static final int terminateMaxFindLen=3;//终止路径寻找的最大路径长度
    public static String compilerUnit="translationUnit";
//    public static String logPath="H:"+File.separator+"demo"+File.separator+"log.txt";//windows
    public static String logPath="log.txt";//windows & linux

//        String directory="F:"+File.separator+"compiler random test"+File.separator+"zhangliwei"+File.separator+"C1";
//        public static String directory="F:"+File.separator+"compiler random test"+File.separator+"java"+File.separator+"java-fangzhoutest";//Java文法路径
    public static String directory="F:"+File.separator+"compiler random test"+File.separator+"python";//python文法路径
//    public static String directory="F:"+File.separator+"compiler random test"+File.separator+"C";//C文法路径
//    public static String directory="F:"+File.separator+"compiler random test"+File.separator+"grammars-v4-master"+File.separator+"python"+File.separator+"python-code-generate"+File.separator+"copythongrammar";//python文法路径
//    public static String directory="E:"+File.separator+"qurong"+File.separator+"compilerTest";//python文法路径
    
    public static int generateNum=5;
    public static String outPutdirectory="F:"+ File.separator+"compiler random test"+File.separator+"python"+File.separator+"outPut";//输出路径
    public static Constraint constraint=null;
    public static String postfix=".txt";
    public static String identifierRelation="random";
    public static String index="1";

    public static String sourcePath="source";//用于变异 源程序的（相对）路径
    public static String astPath="astObj";//用于变异 源程序解析后AST存储序列化的（相对）路径
    public static boolean mutationAvailable=false;//用于变异程序 是否需要变异 true或false 默认为false
    public static String selectedMutationStrategy="no-mutation";////用于变异程序 变异的具体策略 "no mutation", "main mutation", "scope mutation"
    public static final String mutationStrategyMain2Main="main-mutation";
    public static final String mutationStrategyScope2Scope="scope-mutation";
    public static final String mutationStrategyAstFun2Fun="AST-Fun-mutation";
    public static List<String> mutationStrategies=new ArrayList<>();//变异相关
    public static boolean identifierEnumeration=false;//变异相关 是否开启标识符枚举
    public static int minValidIdntifer=5;//变异相关，作用域内最少的可用标识符数量
    public static boolean ifLog=false;//是否输出生成日志


    public static void setMutationStrategies() {
        mutationStrategies.add(mutationStrategyMain2Main);//main函数级别的变异
        mutationStrategies.add(mutationStrategyScope2Scope);//作用域级别的变异
        mutationStrategies.add(mutationStrategyAstFun2Fun);//AST树函数级别的变异
    }


    public static boolean isFangzhouMulti=false;//是否为方舟编译器生成批量的测试用例
    public static String loopCountType="int";//用于防止死循环的计数变量类型
    public static int loopCountMax=2;//用于防止死循环的计数变量最大值

    /**
     * qurong
     * 2022-11-14
     * 设置程序语言的基本信息
     */
    public static void setConfiguration() {
        setMutationStrategies();
        if(ifJavaCode){
            JavaConfigureInfo.setKeyWords();//设置关键字表
            JavaConfigureInfo.setVarnames();//设置变量标识符引用文法token表
            JavaConfigureInfo.setJavaBaseType();//设置java基本类型
            JavaConfigureInfo.setJavaBaseTypeLiteral();//设置java基本类型的直接生成文法
            JavaConfigureInfo.setJavaBaseTypeExp();//设置java基本类型的表达式文法
            JavaConfigureInfo.setScopeGrammars();//设置java中作用域相关文法
            JavaConfigureInfo.setLoopGrammars();//设置java中循环相关文法
            JavaConfigureInfo.setDivisorGrammars();//设置java中除数相关文法
            JavaConfigureInfo.setVarnamesMap();//设置java中基本类型变量名称文法和基本类型关键字映射表
            JavaConfigureInfo.setStatementGrammars();//设置java中新的语句文法
            JavaConfigureInfo.setDefineOrModifyGrammars();//设置java中定义或修改标识符取值的文法
            JavaConfigureInfo.setscopeBodyGrammars();//设置java中作用域体相关的文法
        }
        else if(ifPythonCode){
            PythonConfigureInfo.setKeyWords();//设置关键字表
            PythonConfigureInfo.setVarnames();//设置变量标识符引用文法token表
            PythonConfigureInfo.setPythonBaseType();//设置python基本类型
            PythonConfigureInfo.setPythonBaseTypeLiteral();//设置python基本类型的直接生成文法
            PythonConfigureInfo.setPythonBaseTypeExp();//设置python基本类型的表达式文法
            PythonConfigureInfo.setScopeGrammars();//设置python中作用域相关文法
            PythonConfigureInfo.setLoopGrammars();//设置python中循环相关文法
            PythonConfigureInfo.setDivisorGrammars();//设置python中除数相关文法
            PythonConfigureInfo.setVarnamesMap();//设置python中基本类型变量名称文法和基本类型关键字映射表
            PythonConfigureInfo.setStatementGrammars();//设置python中新的语句文法
            PythonConfigureInfo.setMthdStatementGrammars();//设置python方法语句文法
            PythonConfigureInfo.setDefineOrModifyGrammars();//设置python中定义或修改标识符取值的文法
            PythonConfigureInfo.setscopeBodyGrammars();//设置python中作用域体相关的文法

        }else if(ifCCode){
            CConfigureInfo.setKeyWords();//设置关键字表
            CConfigureInfo.setVarnames();//设置变量标识符引用文法token表
            CConfigureInfo.setCBaseType();//设置c基本类型
            CConfigureInfo.setCBaseTypeLiteral();//设置c基本类型的直接生成文法
            CConfigureInfo.setCBaseTypeExp();//设置c基本类型的表达式文法
            CConfigureInfo.setScopeGrammars();//设置c中开启新的作用域相关文法
            CConfigureInfo.setLoopGrammars();//设置c中循环相关文法
            CConfigureInfo.setDivisorGrammars();//设置c中除数相关文法
            CConfigureInfo.setVarnamesMap();//设置c中基本类型变量名称文法和基本类型关键字映射表
            CConfigureInfo.setStatementGrammars();//设置c中新的语句文法
            CConfigureInfo.setVarnameCategoryMap();//设置变量的类别，变量、数组、指针
            CConfigureInfo.setDefineOrModifyGrammars();//设置c中定义或修改标识符取值的文法
            CConfigureInfo.setscopeBodyGrammars();//设置c中作用域体相关的文法
            
        }else if(ifCPlusCode){
            CPlusConfigureInfo.setKeyWords();//设置关键字表
            CPlusConfigureInfo.setVarnames();//设置变量标识符引用文法token表
            CPlusConfigureInfo.setCBaseType();//设置c基本类型
            CPlusConfigureInfo.setCBaseTypeLiteral();//设置c基本类型的直接生成文法
            CPlusConfigureInfo.setCBaseTypeExp();//设置c基本类型的表达式文法
            CPlusConfigureInfo.setScopeGrammars();//设置c中开启新的作用域相关文法
            CPlusConfigureInfo.setLoopGrammars();//设置c中循环相关文法
            CPlusConfigureInfo.setDivisorGrammars();//设置c中除数相关文法
            CPlusConfigureInfo.setVarnamesMap();//设置c中基本类型变量名称文法和基本类型关键字映射表
            CPlusConfigureInfo.setStatementGrammars();//设置c中新的语句文法
            CPlusConfigureInfo.setVarnameCategoryMap();//设置变量的类别，变量、数组、指针
            CPlusConfigureInfo.setDefineOrModifyGrammars();//设置c中定义或修改标识符取值的文法
            CPlusConfigureInfo.setscopeBodyGrammars();//设置c中作用域体相关的文法
            CPlusConfigureInfo.setMutationStrategies();//设置变异策略集合
            
        }
    }

    /**
     * 2022-11-14
     * qurong
     * 取程序语言的基本类型列表
     * @return
     */
    public static List<String> getBaseTypes() {
        if(ifJavaCode){
            return JavaConfigureInfo.javaBaseType;//基本类型
        }
        else if(ifPythonCode){
            return PythonConfigureInfo.pythonBaseType;//基本类型
        }else if(ifCCode){
            return CConfigureInfo.cBaseType;//基本类型
        }else if(ifCPlusCode){
            return CPlusConfigureInfo.cPlusBaseType;//基本类型
        }
        else{
            return null;
        }
    }

    /**
     * qurong
     * 2022-11-16
     * @return
     */
    public static int getCycleStepLengthMax() {
        if(ConfigureInfo.ifJavaCode){
            return JavaConfigureInfo.cycleStepLengthMax;
        }else if(ConfigureInfo.ifPythonCode){
            return PythonConfigureInfo.cycleStepLengthMax;
        }else if(ConfigureInfo.ifCCode){
            return CConfigureInfo.cycleStepLengthMax;
        }else if(ConfigureInfo.ifCPlusCode){
            return CPlusConfigureInfo.cycleStepLengthMax;
        }else{
            return ConfigureInfo.cycleStepLengthMax;
        }
    }

    /**
     * qurong
     * 2022-11-16
     * @return
     */
    public static int getCycleStepLength() {
    	if(ConfigureInfo.cycleStepLength>0) {
    		return ConfigureInfo.cycleStepLength;
    	}
        if(ConfigureInfo.ifJavaCode){
            return JavaConfigureInfo.cycleStepLength;
        }else if(ConfigureInfo.ifPythonCode){
            return PythonConfigureInfo.cycleStepLength;
        }else if(ConfigureInfo.ifCCode){
            return CConfigureInfo.cycleStepLength;
        }else if(ConfigureInfo.ifCPlusCode){
            return CPlusConfigureInfo.cycleStepLength;
        }else{
            return ConfigureInfo.cycleStepLength;
        }
    }

    /**
     * qurong
     * 2022-11-16
     * *的最大值
     * @return
     */
    public static int getStarnum() {
    	if(ConfigureInfo.starnum>0) {
    		return ConfigureInfo.starnum;
    	}
        if(ConfigureInfo.ifJavaCode){
            return JavaConfigureInfo.starnum;
        }else if(ConfigureInfo.ifPythonCode){
            return PythonConfigureInfo.starnum;
        }else if(ConfigureInfo.ifCCode){
            return CConfigureInfo.starnum;
        }else if(ConfigureInfo.ifCPlusCode){
            return CPlusConfigureInfo.starnum;
        }else{
            return ConfigureInfo.starnum;
        }
    }

    /**
     * qurong
     * 2022-11-16
     * +的最大值
     * @return
     */
    public static int getAddnum() {
    	if(ConfigureInfo.addnum>0) {
    		return ConfigureInfo.addnum;
    	}
        if(ConfigureInfo.ifJavaCode){
            return JavaConfigureInfo.addnum;
        }else if(ConfigureInfo.ifPythonCode){
            return PythonConfigureInfo.addnum;
        }else if(ConfigureInfo.ifCCode){
            return CConfigureInfo.addnum;
        }else if(ConfigureInfo.ifCPlusCode){
            return CPlusConfigureInfo.addnum;
        }else{
            return ConfigureInfo.addnum;
        }
    }
    
    /**
     * qurong
     * 2024-4-9
     * 循环执行的最大次数
     * @return
     */
    public static int getLoopCountMax() {
    	if(ConfigureInfo.loopCountMax>0) {
    		return ConfigureInfo.loopCountMax;
    	}
        if(ConfigureInfo.ifJavaCode){
            return JavaConfigureInfo.loopCountMax;
        }else if(ConfigureInfo.ifPythonCode){
            return PythonConfigureInfo.loopCountMax;
        }else if(ConfigureInfo.ifCCode){
            return CConfigureInfo.loopCountMax;
        }else if(ConfigureInfo.ifCPlusCode){
            return CPlusConfigureInfo.loopCountMax;
        }else{
            return ConfigureInfo.loopCountMax;
        }
    }

    /**
     * qurong
     * 2022-11-16
     * +的最大值
     * @return
     */
    public static int getTerminateMaxFindLen() {
        if(ConfigureInfo.ifJavaCode){
            return JavaConfigureInfo.terminateMaxFindLen;
        }else if(ConfigureInfo.ifPythonCode){
            return PythonConfigureInfo.terminateMaxFindLen;
        }else if(ConfigureInfo.ifCCode){
            return CConfigureInfo.terminateMaxFindLen;
        }else if(ConfigureInfo.ifCPlusCode){
            return CPlusConfigureInfo.terminateMaxFindLen;
        }else{
            return ConfigureInfo.terminateMaxFindLen;
        }
    }

    /**
     * qurong
     * 2022-11-16
     * @return
     */
    public static int getMoreThanOneRoot() {
        if(ConfigureInfo.ifJavaCode){
            return JavaConfigureInfo.moreThanOneRoot;
        }else if(ConfigureInfo.ifPythonCode){
            return PythonConfigureInfo.moreThanOneRoot;
        }else if(ConfigureInfo.ifCCode){
            return CConfigureInfo.moreThanOneRoot;
        }else if(ConfigureInfo.ifCPlusCode){
            return CPlusConfigureInfo.moreThanOneRoot;
        }else{
            return ConfigureInfo.moreThanOneRoot;
        }
    }

    public static void setMoreThanOneRoot(int num) {
        if(ConfigureInfo.ifJavaCode){
            JavaConfigureInfo.moreThanOneRoot=num;
        }else if(ConfigureInfo.ifPythonCode){
            PythonConfigureInfo.moreThanOneRoot=num;
        }else if(ConfigureInfo.ifCCode){
            CConfigureInfo.moreThanOneRoot=num;
        }else if(ConfigureInfo.ifCPlusCode){
            CPlusConfigureInfo.moreThanOneRoot=num;
        }else{
            ConfigureInfo.moreThanOneRoot=num;
        }
    }


    /**
     * qurong
     * 2022-11-16
     * @return
     */
    public static int getNeedsplitNum() {
        if(ConfigureInfo.ifJavaCode){
            return JavaConfigureInfo.needsplitNum;
        }else if(ConfigureInfo.ifPythonCode){
            return PythonConfigureInfo.needsplitNum;
        }else if(ConfigureInfo.ifCCode){
            return CConfigureInfo.needsplitNum;
        }else if(ConfigureInfo.ifCPlusCode){
            return CPlusConfigureInfo.needsplitNum;
        }else{
            return ConfigureInfo.needsplitNum;
        }
    }

    public static void setNeedsplitNum(int num) {
        if(ConfigureInfo.ifJavaCode){
            JavaConfigureInfo.needsplitNum=num;
        }else if(ConfigureInfo.ifPythonCode){
            PythonConfigureInfo.needsplitNum=num;
        }else if(ConfigureInfo.ifCCode){
            CConfigureInfo.needsplitNum=num;
        }else if(ConfigureInfo.ifCPlusCode){
            CPlusConfigureInfo.needsplitNum=num;
        }else{
            ConfigureInfo.needsplitNum=num;
        }
    }
}
