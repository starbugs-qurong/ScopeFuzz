package anonymous.ac.grand.mutate.cplus14;

import anonymous.ac.grand.main.c.CConfigureInfo;
import anonymous.ac.grand.main.common.*;
import anonymous.ac.grand.main.common.*;
import anonymous.ac.grand.main.javapkg.JavaConfigureInfo;
import anonymous.ac.grand.main.mutation.Mutation;
import anonymous.ac.grand.main.python.PythonConfigureInfo;

import java.util.*;

public class GrammarGenerateForCPlus {
    public static GrammarGenerateService grammarGenerateService=new GrammarGenerateService();
    List<Grammar> grammars=new ArrayList<>();

    public GrammarGenerateForCPlus(List<Grammar> grammars) {
        this.grammars = grammars;
        grammarGenerateService=new GrammarGenerateService(grammars);
    }

    public GrammarGenerateForCPlus() {
    }

    /**
     * 除数不能为零
     * @param str
     * @return
     */
    public String zeroExclideC(String str) {
        str="(("+str+"==0)?"+"1:("+str+"))";
        return str;
    }

    /**
     * anonymousAuthor
     * 2022-12-7
     * @param scopeNode
     * @return
     */
    public String getTabProfixByDepth(ScopeTree scopeNode) {
        String result="";
        if(scopeNode==null){
            return result;
        }
        int depth=scopeNode.getDepth();
        int i=0;
        while(i<depth){
            i++;
            result+="\t";
        }
        return result;
    }

    /**
     * anonymousAuthor
     * 2022-11-14
     * 生成c中的特殊部分如声明变量、方法、类型等
     * @param str
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    public String generateSpecialPartForCPlus(String str, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode, int lineOrder) {
        String result="";
        //方法返回值相关
        if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.methodDeclaration))){
            result=generateMethodDeclaration(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder,false);
            return result;
        }

        //main方法相关
        if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.mainContent))){
            result=generateMain(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            //更新main函数对应的作用域到符号表
            st.setMainScope(scopeNode.getFather());
            return result;
        }
        
      //模板函数相关
        if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.templateFunDeclaration))){
            result=generateTemplateFun(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            
            return result;
        }
        
      //类声明相关
        if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.classDeclaration)||str.trim().equalsIgnoreCase(CPlusConfigureInfo.structDeclaration))){
            result=generateClassDeclaration(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }

        //引用标识符相关变量
        //是否是C声明token,如果是，要更新直接包含的Identifier到符号表
        if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.fieldDeclaration)||str.trim().equalsIgnoreCase(CPlusConfigureInfo.localDeclaration))){//不区分大小写，如果是声明变量的文法串
            //声明文法特殊处理，记录类型，标识符，作用域等信息
            result=generateDeclaration(str,st,scopeNode,index,ifNeedOutCycle,tokenAppearTimes,lineOrder);
            return result;
        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.fieldArrayDeclaration)||str.trim().equalsIgnoreCase(CPlusConfigureInfo.localArrayDeclaration))){//不区分大小写，如果是声明数组变量的文法串
            //声明文法特殊处理，记录类型，标识符，作用域等信息
            result=generateArrayDeclaration(str,st,scopeNode,index,ifNeedOutCycle,tokenAppearTimes,lineOrder);
            return result;
        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.fieldPointerDeclaration)||str.trim().equalsIgnoreCase(CPlusConfigureInfo.localPointerDeclaration))){//不区分大小写，如果是声明指针变量的文法串
            //声明文法特殊处理，记录类型，标识符，作用域等信息
            result=generatePointerDeclaration(str,st,scopeNode,index,ifNeedOutCycle,tokenAppearTimes,lineOrder);
            return result;
        }
        //使用数组变量
        else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.arrayElement))) {
        	String temResult=generateIdentifierByType(str,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,"array","value",lineOrder);
            result += " " + temResult;
        }//使用指针变量
        else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.pointerValue))) {
        	String temResult=generateIdentifierByType(str,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,"pointer","value",lineOrder);
            result += " " + temResult;
        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&str.trim().equalsIgnoreCase(CPlusConfigureInfo.arrayVarName)){//引用数组变量标识符
            String temResult=generateIdentifierByType(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,"array","self",lineOrder);
            result =temResult;

        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.pointerVarName))){//引用指针变量标识符
            String temResult=generateIdentifierByType(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,"pointer","self",lineOrder);
            result =temResult;

        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(CPlusConfigureInfo.getVarnames().contains(str.trim()))){
            result=generateIdentifierByType(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,"var","-",lineOrder);
            return result;
        }//Lamda表达式
        else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.lamdaExpression))){//lamada表达式
            result=generateLamdaExpression(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }
        return result;
    }

    /**
     * anonymousAuthor
     * 2025-2-18
     * 生成c中的特殊部分如声明变量、方法、类型等
     * @param str
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    public List<String> generatePrgramsOfSpecialPartForCPlus(String str, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        List<String> resultList=new ArrayList<>();
        String result="";
        //方法返回值相关
        if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.methodDeclaration))){
            resultList=generateMethodDeclarations(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder,false);
            return resultList;
        }

        //main方法相关
        else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.mainContent))){
            result=generateMain(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            //更新main函数对应的作用域到符号表
            st.setMainScope(scopeNode.getFather());
        }

        //模板函数相关
        else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.templateFunDeclaration))){
            resultList=generateTemplateFuns(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return resultList;
        }

        //类声明相关
        else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.classDeclaration)||str.trim().equalsIgnoreCase(CPlusConfigureInfo.structDeclaration))){
            result=generateClassDeclaration(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        }

        //引用标识符相关变量
        //是否是C声明token,如果是，要更新直接包含的Identifier到符号表
        else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.fieldDeclaration)||str.trim().equalsIgnoreCase(CPlusConfigureInfo.localDeclaration))){//不区分大小写，如果是声明变量的文法串
            //声明文法特殊处理，记录类型，标识符，作用域等信息
            result=generateDeclaration(str,st,scopeNode,index,ifNeedOutCycle,tokenAppearTimes,lineOrder);
        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.fieldArrayDeclaration)||str.trim().equalsIgnoreCase(CPlusConfigureInfo.localArrayDeclaration))){//不区分大小写，如果是声明数组变量的文法串
            //声明文法特殊处理，记录类型，标识符，作用域等信息
            result=generateArrayDeclaration(str,st,scopeNode,index,ifNeedOutCycle,tokenAppearTimes,lineOrder);
        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.fieldPointerDeclaration)||str.trim().equalsIgnoreCase(CPlusConfigureInfo.localPointerDeclaration))){//不区分大小写，如果是声明指针变量的文法串
            //声明文法特殊处理，记录类型，标识符，作用域等信息
            result=generatePointerDeclaration(str,st,scopeNode,index,ifNeedOutCycle,tokenAppearTimes,lineOrder);
        }
        //使用数组变量
        else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.arrayElement))) {
            String temResult=generateIdentifierByType(str,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,"array","value",lineOrder);
            result += " " + temResult;
        }//使用指针变量
        else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.pointerValue))) {
            String temResult=generateIdentifierByType(str,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,"pointer","value",lineOrder);
            result += " " + temResult;
        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&str.trim().equalsIgnoreCase(CPlusConfigureInfo.arrayVarName)){//引用数组变量标识符
            String temResult=generateIdentifierByType(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,"array","self",lineOrder);
            result =temResult;

        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.pointerVarName))){//引用指针变量标识符
            String temResult=generateIdentifierByType(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,"pointer","self",lineOrder);
            result =temResult;

        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(CPlusConfigureInfo.getVarnames().contains(str.trim()))){
            result=generateIdentifierByType(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,"var","-",lineOrder);
        }//Lamda表达式
        else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.trim().equalsIgnoreCase(CPlusConfigureInfo.lamdaExpression))){//lamada表达式
            resultList=generateLamdaExpressions(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return resultList;
        }
        resultList.add(result);
        return resultList;
    }

    /**
     * 生成模板函数，对这类函数进行标记
     * @param str
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @param lineOrder
     * @return
     */
    private String generateTemplateFun(String str, int index, Map<String, Integer> tokenAppearTimes,
			boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode, int lineOrder) {
		// TODO Auto-generated method stub
    	
    	String result=generateMethodDeclaration(CPlusConfigureInfo.methodDeclaration, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder,true);
		return result;
	}

    /**
     * anonymousAuthor
     * 2025-2-19
     * 生成模板函数，对这类函数进行标记
     * @param str
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @param lineOrder
     * @return
     */
    private List<String> generateTemplateFuns(String str, int index, Map<String, Integer> tokenAppearTimes,
                                       boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode, int lineOrder) {
        // TODO Auto-generated method stub

        List<String> result=generateMethodDeclarations(CPlusConfigureInfo.methodDeclaration, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder,true);
        return result;
    }

	/**
     * 2022-10-12生成一个标识符，确保有定义
     * @param strCopy
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    public String generateIdentifierByTypeC(String strCopy, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,String identifierCategory,String object,int lineOrder) {
        String result="";
    	String typeType="";
        strCopy=strCopy.trim();
        String identifier="";
        Map<String, Integer> tempTokenAppearTimes =tokenAppearTimes;
        //这里做类型的区分
        String tokenType=strCopy.trim();
        if(ConfigureInfo.ifCCode){
            typeType= CConfigureInfo.varnamesMap.get(tokenType);
        }else if(ConfigureInfo.ifPythonCode){
            typeType= PythonConfigureInfo.varnamesMap.get(tokenType);
        }else if(ConfigureInfo.ifCPlusCode){
            typeType= CPlusConfigureInfo.varnamesMap.get(tokenType);
        }else if(ConfigureInfo.ifJavaCode){
            typeType=JavaConfigureInfo.varnamesMap.get(tokenType);
        }
        if(typeType==null||typeType==""){
            typeType="int";
        }

        	 
        String identifierRelationStrategy=ConfigureInfo.identifierRelation;
        SymbolRecord sr=new SymbolRecord();
        switch(identifierRelationStrategy) {//"new", "nearest", "farthest ","random","PASI","PISS"
	        case "new": {
	        	result=getNewIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//新创建一个标识符，创建语句的位置插入到全局变量中
	        	break;
	        }
	        case "nearest": {
	        	result= getNearestIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//随机使用距离当前作用域最近的一个作用域内的可用标识符
	        	break;
	        }
	        case "farthest": {
	        	result=getFarthestIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//随机使用距离当前作用域最远的一个作用域内的可用标识符
	        	break;
	        }
	        case "random": {
	        	result= getRandomIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//随机使用一个已定义的标识符
	        	break;
	        }
	        case "PASI": {
	        	sr= getEvenPASIIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object);//根据PASI策略，选择一个已定义的标识符
	        	if(sr==null) {
	        		identifierRelationStrategy="new";
	        		result=getNewIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//新创建一个标识符，创建语句的位置插入到全局变量中
		        	break;
	        	}else {
	        		result=getResultBySymbolRecord(sr,identifierCategory,object);
	        	}
	        	break;
	        }
	        case "PISS": {
	        	sr= getEvenPISSIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object);//根据PASI策略，选择一个已定义的标识符
	        	if(sr==null) {
	        		identifierRelationStrategy="new";
	        		result=getNewIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//新创建一个标识符，创建语句的位置插入到全局变量中
		        	break;
	        	}else {
	        		result=getResultBySymbolRecord(sr,identifierCategory,object);
	        	}
	        	break;
	        }
	        default:{
	        	result= getRandomIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//随机使用一个已定义的标识符
	        	break;
	        }
	    }
        if(identifierRelationStrategy.equals("PASI")||identifierRelationStrategy.equals("PISS")) {
        	grammarGenerateService.updateSymTableBySymbolRecord(st,scopeNode,sr.getName());//标识符sr.getName()在scopeNode中的使用次数加1，更新符号表
        }else {
        	grammarGenerateService.updateSymTableBySymbolRecord(st,scopeNode,result);//标识符result在scopeNode中的使用次数加1，更新符号表
        }
        
        
        return result;
//        }
    }

    /**
     * anonymousAuthor
     * 2025-2-19生成多个标识符，确保有定义
     * @param strCopy
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    public List<String> generateIdentifiersByTypeC(String strCopy, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,String identifierCategory,String object,int lineOrder) {
        List<String> resultList=new ArrayList<>();
        String result="";
        String typeType="";
        strCopy=strCopy.trim();
        String identifier="";
        Map<String, Integer> tempTokenAppearTimes =tokenAppearTimes;
        //这里做类型的区分
        String tokenType=strCopy.trim();
        if(ConfigureInfo.ifCCode){
            typeType= CConfigureInfo.varnamesMap.get(tokenType);
        }else if(ConfigureInfo.ifPythonCode){
            typeType= PythonConfigureInfo.varnamesMap.get(tokenType);
        }else if(ConfigureInfo.ifCPlusCode){
            typeType= CPlusConfigureInfo.varnamesMap.get(tokenType);
        }else if(ConfigureInfo.ifJavaCode){
            typeType=JavaConfigureInfo.varnamesMap.get(tokenType);
        }
        if(typeType==null||typeType==""){
            typeType="int";
        }


        String identifierRelationStrategy=ConfigureInfo.identifierRelation;
        SymbolRecord sr=new SymbolRecord();
        switch(identifierRelationStrategy) {//"new", "nearest", "farthest ","random","PASI","PISS"
            case "new": {
                result=getNewIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//新创建一个标识符，创建语句的位置插入到全局变量中
                break;
            }
            case "nearest": {
                result= getNearestIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//随机使用距离当前作用域最近的一个作用域内的可用标识符
                break;
            }
            case "farthest": {
                result=getFarthestIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//随机使用距离当前作用域最远的一个作用域内的可用标识符
                break;
            }
            case "random": {
                result= getRandomIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//随机使用一个已定义的标识符
                break;
            }
            case "PASI": {
                sr= getEvenPASIIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object);//根据PASI策略，选择一个已定义的标识符
                if(sr==null) {
                    identifierRelationStrategy="new";
                    result=getNewIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//新创建一个标识符，创建语句的位置插入到全局变量中
                    break;
                }else {
                    result=getResultBySymbolRecord(sr,identifierCategory,object);
                }
                break;
            }
            case "PISS": {
                sr= getEvenPISSIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object);//根据PASI策略，选择一个已定义的标识符
                if(sr==null) {
                    identifierRelationStrategy="new";
                    result=getNewIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//新创建一个标识符，创建语句的位置插入到全局变量中
                    break;
                }else {
                    result=getResultBySymbolRecord(sr,identifierCategory,object);
                }
                break;
            }
            default:{
                result= getRandomIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//随机使用一个已定义的标识符
                break;
            }
        }
        if(identifierRelationStrategy.equals("PASI")||identifierRelationStrategy.equals("PISS")) {
            grammarGenerateService.updateSymTableBySymbolRecord(st,scopeNode,sr.getName());//标识符sr.getName()在scopeNode中的使用次数加1，更新符号表
        }else {
            grammarGenerateService.updateSymTableBySymbolRecord(st,scopeNode,result);//标识符result在scopeNode中的使用次数加1，更新符号表
        }

        resultList.add(result);
        return resultList;
//        }
    }


    /**
     * 2022-11-17
     * 在合适的位置生成一个typeType类型的变量声明，方便当前作用域内使用
     * @param typeType
     * @param st
     * @param scopeNode
     * @return
     */
    public SymbolRecord generateDeclarationByTypeC(String typeType, SymbolTable st, ScopeTree scopeNode,int index,Map<String,Integer> tempTokenAppearTimes,boolean ifNeedOutCycle,int lineOrder) {
        SymbolRecord sr=generateDeclarationByTypeInCurScopeC(typeType, st, scopeNode, index,tempTokenAppearTimes, ifNeedOutCycle, CPlusConfigureInfo.cPlusBaseTypeLiteral,lineOrder);
//        sr.setScope(scopeNode.getLocateType().getScopeNode());//用当前作用域所在类的作用域
        sr.setScope(scopeNode.getRoot());//用根作用域 生成大量全局变量开关2
        return sr;
    }

    /**
     * 2022-11-17
     * 在当前作用域内生成一个typeType类型的变量声明。
     * @param typeType
     * @param st
     * @param scopeNode
     * @return
     */
    public SymbolRecord generateDeclarationByTypeInCurScopeC(String typeType, SymbolTable st, ScopeTree scopeNode, int index, Map<String, Integer> tempTokenAppearTimes, boolean ifNeedOutCycle, Map<String, String> typeExpression,int lineOrder) {
        String declaration=typeType;
        String identifier="";
        String identifierValue="";
        Constraint constraint=new Constraint();
        identifier=grammarGenerateService.getIdentifier(index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        identifierValue=grammarGenerateService.generateProgramForMatchedValue(typeExpression.get(typeType), index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        identifierValue = constraint.checkUniqueConstraint(identifierValue, typeExpression.get(typeType), index, grammars);//唯一性约束（*不可重复相同值）
        identifierValue = constraint.checkContinueConstraint(identifierValue, typeExpression.get(typeType), index, grammars);//连续性约束，，如果在约束列表中，temResult中不能有空格
        Type type=st.getTypeMap().get(typeType);
//        if(type==null)//System.out.println("what is this"+typeType);
        declaration = identifier+ " = "+identifierValue +" NEWLINE ";
        return new SymbolRecord(identifier,type,identifier,"var",scopeNode,identifierValue);
    }

    /**
     * 2024.2.2
     * 生成一个lamda表达式
     */
    private String generateLamdaExpression(String str, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
    	String result="";
        scopeNode=grammarGenerateService.updateScope(st,scopeNode);//方法定义也开启一个作用域，这是为了把方法的参数也能包含进作用域
        String[] items=str.split(" ");//认为括号后面要么跟+？* 要么跟空格  renameLamda lamdaCaptureList lamdaParameterList lamdaSpecifiersException lamdaType lamdaFunctionBody callParameter
        String temResult="";
        List<SymbolRecord> returnIdentifiers=new ArrayList<>();
        Type type=new Type();
        String identifier="";//lamda表达式的别名
        String cpatureIdentifier="";//捕获列表中的特殊标识符
        List<Type> parameterList=new ArrayList<>();
        List<SymbolRecord> parameterIdentifiers=new ArrayList<>();
        Constraint constraint=new Constraint();
        LamdaSymbolRecord lamdaSymbolRecord = null;
        for(String item:items) {
            temResult = grammarGenerateService.generateProgramForMatchedValue(item, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            temResult = constraint.checkUniqueConstraint(temResult, item, index, grammars);//唯一性约束（*不可重复相同值）
            temResult = constraint.checkContinueConstraint(temResult, item, index, grammars);//连续性约束，，如果在约束列表中，temResult中不能有空格
            if (item.equals("lamdaType")) {
            	while(temResult.contains("void")||temResult.contains("[]")) {
            		temResult = grammarGenerateService.generateProgramForMatchedValue(item, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
                }
            	temResult=temResult.replace("*", "").trim();//返回值类型为指针类型时，有时会返回形参或者返回局部变量的地址，这会导致出现未定义行为
            	String typeName=temResult.replace("->", "").trim();
            	
                type =st.getTypeMap().get(typeName);
                if(type==null){
                    //System.out.println("lamda type is null" + temResult);
                }
                scopeNode.setType(type);//当前作用域内返回return语句需要返回的表达式的类型
            } else if (item.equals("renameLamda")) {
            	if(!temResult.trim().equals("")) {
            		String[] renameList = temResult.split(" ");//用空格拆分别名
            		if(renameList!=null&&renameList.length==3) {
            			identifier=renameList[1];
            		}
            	}
            }else if (item.equals("lamdaSpecifiersException")) {
            	//mutable，暂时全部使用此选项
            }else if (item.equals("callParameter")) {
                List<String> addedDeclaration=new ArrayList<>();//补充的声明列表,main生成结束后将其添加在main函数最开始的位置
            	// 'follow' 在lamda表达式定义结束之后，分号之前，直接在括号里传参调用这个匿名函数
            	if(temResult.trim().equals("follow")||temResult.contains("follow")) {
            		String params=generateParas(parameterIdentifiers,st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle,lineOrder, addedDeclaration,lamdaSymbolRecord);
            		temResult=params+";";
            	}
            	// 'next'
            	else if(temResult.trim().equals("next")||temResult.contains("next")) {
            		temResult=";";
//            		if(identifier!=null&&identifier!="") {
//            			if(type!=null&&type.getTypeName()!="void") {
//            				temResult+=getNewIdentifier(type.getTypeName(),st, scopeNode,index,tokenAppearTimes, ifNeedOutCycle,"var","-",lineOrder);
//            			}else {
            				temResult+=generateObjectCallLamda(index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,addedDeclaration,lineOrder,lamdaSymbolRecord);
//                			temResult+=";";
//            			}
//            		}
            	}
            	// 'no'
            	else if(temResult.trim().equals("no")||temResult.contains("no")) {
            		temResult=";";
            	}else {
            		temResult=";";
            	}
            	temResult+=generatePrintLambda(identifier,type);//在lambda表达式定义之后加入打印表达式取值的语句
            	result=insertAddDeclarationToMain(addedDeclaration,result);//在result最开始的位置加入为声明标识符的声明语句
            	
            }else if (item.equals("lamdaCaptureList")) {
            	cpatureIdentifier=generateIdentifierByType("int", index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,"var","-",lineOrder);//这里暂时只对普通变量进行个例选取，如=，&a，a不是指针或者数组
                String captureType= StringTools.deleteBrackets("[","]",temResult);
                if(captureType.trim().equals("")){
                    //捕获列表为空,在lamda表达式内部不能使用所用的外部变量,暂未打开此选项
                }else {
                    if(captureType.equals("=,&")) {
                    	captureType+=cpatureIdentifier;
                    }if(captureType.equals("= ,&")) {
                    	captureType=captureType.replace(" ", cpatureIdentifier);
                    }else if(captureType.equals("=")||captureType.equals("&")) {
                    	//所有变量都可用
                    }else if(captureType.startsWith("=")||captureType.startsWith("&")) {
                    	//某一个变量外，其它全部限制使用,暂未打开此选项
                    }
                }
                temResult="["+captureType+"]";
            }else if(item.equals("lamdaParameterList")){
                //可以用逗号拆分开来，把各个参数类型提取出来，保存在parameterList当中
                if(temResult.trim().equals("")){
                    //参数列表为空
                	temResult="()";
                }else{
                    String parameters= StringTools.deleteBrackets("(",")",temResult);
                    String[] parameterArr = parameters.split(",");//用逗号拆分参数列表
                    for(int j=0;j<parameterArr.length;j++){
                        String param=parameterArr[j].trim();
                        String[] paraInfo=param.split(" ");//用空格来分割类型和标识符
                        if(paraInfo.length<2){//一个参数 但是类型和标识符有问题
//                            //System.out.println("paraInfo.length less than 2: "+paraInfo.length);
                        }else if(paraInfo.length==2){//一个普通参数 类型和标识符匹配
                            String type1name=paraInfo[0].trim();
                            String ident1=paraInfo[1].trim();
                            Type type1=st.getTypeMap().get(type1name);
                            parameterList.add(type1);
                            SymbolRecord sr1=new SymbolRecord(ident1,type1,ident1,"var",scopeNode,"");
                            parameterIdentifiers.add(sr1);
                            grammarGenerateService.updateSymTableByVar(st,sr1);//参数列表中的形参也是函数体的作用域内可以使用的变量，更新到符号表
                        }else if(paraInfo.length==3){//一个指针参数 类型和标识符匹配
                            String type1name=paraInfo[0].trim();
                            String type1star=paraInfo[1].trim();
                            String ident1=paraInfo[2].trim();
                            if(type1star.equals("*")) {
	                            Type type1=st.getTypeMap().get(type1name+type1star);
	                            parameterList.add(type1);
	                            SymbolRecord sr1=new SymbolRecord(ident1,type1,ident1,"pointer",scopeNode,"");
	                            parameterIdentifiers.add(sr1);
	                            //grammarGenerateService.updateSymTableByVar(st,sr1);//参数列表中的形参也是函数体的作用域内可以使用的变量，更新到符号表
                            }
                        }else if(paraInfo.length==4){//一个数组参数 类型和标识符匹配
                            String type1name=paraInfo[0].trim();
                            String ident1=paraInfo[1].trim();
                            String leftBracket=paraInfo[2].trim();
                            String rightBracket=paraInfo[3].trim();
                            if(leftBracket.equals("[")&&rightBracket.equals("]")) {
	                            Type type1=st.getTypeMap().get(type1name+leftBracket+rightBracket);
	                            parameterList.add(type1);
	                            SymbolRecord sr1=new SymbolRecord(ident1,type1,ident1,"array",scopeNode,"");
	                            sr1.setArrayLength(CPlusConfigureInfo.arrayLength);
	                            parameterIdentifiers.add(sr1);
	                            //grammarGenerateService.updateSymTableByVar(st,sr1);//参数列表中的形参也是函数体的作用域内可以使用的变量，更新到符号表
                            }
                        }else{//一个参数 但是类型和标识符有问题
//                            //System.out.println("paraInfo.length more than 2: "+paraInfo.length);
                        }
                    }
                }

            }else if(item.equals("lamdaFunctionBody")){
                //暂时没想到是否需要特殊处理
                //return 方法返回值的添加
                Type returnType=scopeNode.getType();
                temResult=addReturn(temResult,returnType,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,lineOrder);
                int returnExp=temResult.lastIndexOf("return");
                if(returnExp>=0) {
                	returnIdentifiers=st.getIdentifiersFromStr(temResult.substring(temResult.lastIndexOf("return")),st);
                }
                
                String outPutStr=generateOutPut(st, scopeNode);
                temResult=insertStr2ScopeEnd(temResult,outPutStr);
                //在生成此内容后，lamda表达式的各个部分已经全部生成
                lamdaSymbolRecord=new LamdaSymbolRecord(identifier,type,identifier,"lamda",scopeNode,null,parameterList,parameterIdentifiers);
                lamdaSymbolRecord.setReturnIdentifiers(returnIdentifiers);
                grammarGenerateService.updateSymTableByLamda(st,lamdaSymbolRecord);//使用新声明的Lamda表达式更新符号表模块
                scopeNode=scopeNode.getFather();//把作用域回溯到方法的声明之外
            }
            result += " " + temResult;
        }
    	return result;
    }

    /**
     * anonymousAuthor
     * 2025-2-19
     * 生成一个lamda表达式
     */
    private List<String> generateLamdaExpressions(String str, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        List<String> resultList=new ArrayList<>();
        String result="";
        scopeNode=grammarGenerateService.updateScope(st,scopeNode);//方法定义也开启一个作用域，这是为了把方法的参数也能包含进作用域
        String[] items=str.split(" ");//认为括号后面要么跟+？* 要么跟空格  renameLamda lamdaCaptureList lamdaParameterList lamdaSpecifiersException lamdaType lamdaFunctionBody callParameter
        String temResult="";
        List<SymbolRecord> returnIdentifiers=new ArrayList<>();
        Type type=new Type();
        String identifier="";//lamda表达式的别名
        String cpatureIdentifier="";//捕获列表中的特殊标识符
        List<Type> parameterList=new ArrayList<>();
        List<SymbolRecord> parameterIdentifiers=new ArrayList<>();
        Constraint constraint=new Constraint();
        LamdaSymbolRecord lamdaSymbolRecord = null;
        for(String item:items) {
            temResult = grammarGenerateService.generateProgramForMatchedValue(item, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            temResult = constraint.checkUniqueConstraint(temResult, item, index, grammars);//唯一性约束（*不可重复相同值）
            temResult = constraint.checkContinueConstraint(temResult, item, index, grammars);//连续性约束，，如果在约束列表中，temResult中不能有空格
            if (item.equals("lamdaType")) {
                while(temResult.contains("void")||temResult.contains("[]")) {
                    temResult = grammarGenerateService.generateProgramForMatchedValue(item, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
                }
                temResult=temResult.replace("*", "").trim();//返回值类型为指针类型时，有时会返回形参或者返回局部变量的地址，这会导致出现未定义行为
                String typeName=temResult.replace("->", "").trim();

                type =st.getTypeMap().get(typeName);
                if(type==null){
                    //System.out.println("lamda type is null" + temResult);
                }
                scopeNode.setType(type);//当前作用域内返回return语句需要返回的表达式的类型
            } else if (item.equals("renameLamda")) {
                if(!temResult.trim().equals("")) {
                    String[] renameList = temResult.split(" ");//用空格拆分别名
                    if(renameList!=null&&renameList.length==3) {
                        identifier=renameList[1];
                    }
                }
            }else if (item.equals("lamdaSpecifiersException")) {
                //mutable，暂时全部使用此选项
            }else if (item.equals("callParameter")) {
                List<String> addedDeclaration=new ArrayList<>();//补充的声明列表,main生成结束后将其添加在main函数最开始的位置
                // 'follow' 在lamda表达式定义结束之后，分号之前，直接在括号里传参调用这个匿名函数
                if(temResult.trim().equals("follow")||temResult.contains("follow")) {
                    String params=generateParas(parameterIdentifiers,st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle,lineOrder, addedDeclaration,lamdaSymbolRecord);
                    temResult=params+";";
                }
                // 'next'
                else if(temResult.trim().equals("next")||temResult.contains("next")) {
                    temResult=";";
//            		if(identifier!=null&&identifier!="") {
//            			if(type!=null&&type.getTypeName()!="void") {
//            				temResult+=getNewIdentifier(type.getTypeName(),st, scopeNode,index,tokenAppearTimes, ifNeedOutCycle,"var","-",lineOrder);
//            			}else {
                    temResult+=generateObjectCallLamda(index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,addedDeclaration,lineOrder,lamdaSymbolRecord);
//                			temResult+=";";
//            			}
//            		}
                }
                // 'no'
                else if(temResult.trim().equals("no")||temResult.contains("no")) {
                    temResult=";";
                }else {
                    temResult=";";
                }
                temResult+=generatePrintLambda(identifier,type);//在lambda表达式定义之后加入打印表达式取值的语句
                result=insertAddDeclarationToMain(addedDeclaration,result);//在result最开始的位置加入为声明标识符的声明语句

            }else if (item.equals("lamdaCaptureList")) {
                cpatureIdentifier=generateIdentifierByType("int", index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,"var","-",lineOrder);//这里暂时只对普通变量进行个例选取，如=，&a，a不是指针或者数组
                String captureType= StringTools.deleteBrackets("[","]",temResult);
                if(captureType.trim().equals("")){
                    //捕获列表为空,在lamda表达式内部不能使用所用的外部变量,暂未打开此选项
                }else {
                    if(captureType.equals("=,&")) {
                        captureType+=cpatureIdentifier;
                    }if(captureType.equals("= ,&")) {
                        captureType=captureType.replace(" ", cpatureIdentifier);
                    }else if(captureType.equals("=")||captureType.equals("&")) {
                        //所有变量都可用
                    }else if(captureType.startsWith("=")||captureType.startsWith("&")) {
                        //某一个变量外，其它全部限制使用,暂未打开此选项
                    }
                }
                temResult="["+captureType+"]";
            }else if(item.equals("lamdaParameterList")){
                //可以用逗号拆分开来，把各个参数类型提取出来，保存在parameterList当中
                if(temResult.trim().equals("")){
                    //参数列表为空
                    temResult="()";
                }else{
                    String parameters= StringTools.deleteBrackets("(",")",temResult);
                    String[] parameterArr = parameters.split(",");//用逗号拆分参数列表
                    for(int j=0;j<parameterArr.length;j++){
                        String param=parameterArr[j].trim();
                        String[] paraInfo=param.split(" ");//用空格来分割类型和标识符
                        if(paraInfo.length<2){//一个参数 但是类型和标识符有问题
//                            //System.out.println("paraInfo.length less than 2: "+paraInfo.length);
                        }else if(paraInfo.length==2){//一个普通参数 类型和标识符匹配
                            String type1name=paraInfo[0].trim();
                            String ident1=paraInfo[1].trim();
                            Type type1=st.getTypeMap().get(type1name);
                            parameterList.add(type1);
                            SymbolRecord sr1=new SymbolRecord(ident1,type1,ident1,"var",scopeNode,"");
                            parameterIdentifiers.add(sr1);
                            grammarGenerateService.updateSymTableByVar(st,sr1);//参数列表中的形参也是函数体的作用域内可以使用的变量，更新到符号表
                        }else if(paraInfo.length==3){//一个指针参数 类型和标识符匹配
                            String type1name=paraInfo[0].trim();
                            String type1star=paraInfo[1].trim();
                            String ident1=paraInfo[2].trim();
                            if(type1star.equals("*")) {
                                Type type1=st.getTypeMap().get(type1name+type1star);
                                parameterList.add(type1);
                                SymbolRecord sr1=new SymbolRecord(ident1,type1,ident1,"pointer",scopeNode,"");
                                parameterIdentifiers.add(sr1);
                                //grammarGenerateService.updateSymTableByVar(st,sr1);//参数列表中的形参也是函数体的作用域内可以使用的变量，更新到符号表
                            }
                        }else if(paraInfo.length==4){//一个数组参数 类型和标识符匹配
                            String type1name=paraInfo[0].trim();
                            String ident1=paraInfo[1].trim();
                            String leftBracket=paraInfo[2].trim();
                            String rightBracket=paraInfo[3].trim();
                            if(leftBracket.equals("[")&&rightBracket.equals("]")) {
                                Type type1=st.getTypeMap().get(type1name+leftBracket+rightBracket);
                                parameterList.add(type1);
                                SymbolRecord sr1=new SymbolRecord(ident1,type1,ident1,"array",scopeNode,"");
                                sr1.setArrayLength(CPlusConfigureInfo.arrayLength);
                                parameterIdentifiers.add(sr1);
                                //grammarGenerateService.updateSymTableByVar(st,sr1);//参数列表中的形参也是函数体的作用域内可以使用的变量，更新到符号表
                            }
                        }else{//一个参数 但是类型和标识符有问题
//                            //System.out.println("paraInfo.length more than 2: "+paraInfo.length);
                        }
                    }
                }

            }else if(item.equals("lamdaFunctionBody")){
                //暂时没想到是否需要特殊处理
                //return 方法返回值的添加
                Type returnType=scopeNode.getType();
                temResult=addReturn(temResult,returnType,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,lineOrder);
                int returnExp=temResult.lastIndexOf("return");
                if(returnExp>=0) {
                    returnIdentifiers=st.getIdentifiersFromStr(temResult.substring(temResult.lastIndexOf("return")),st);
                }

                String outPutStr=generateOutPut(st, scopeNode);
                temResult=insertStr2ScopeEnd(temResult,outPutStr);
                //在生成此内容后，lamda表达式的各个部分已经全部生成
                lamdaSymbolRecord=new LamdaSymbolRecord(identifier,type,identifier,"lamda",scopeNode,null,parameterList,parameterIdentifiers);
                lamdaSymbolRecord.setReturnIdentifiers(returnIdentifiers);
                grammarGenerateService.updateSymTableByLamda(st,lamdaSymbolRecord);//使用新声明的Lamda表达式更新符号表模块
                scopeNode=scopeNode.getFather();//把作用域回溯到方法的声明之外
            }
            result += " " + temResult;
        }
        resultList.add(result);
        return resultList;
    }
    /**
     * anonymousAuthor
     * 2024.3.9
     * 输出lambda表达式调用后的取值
     * @param identifier
     * @param type
     * @return
     */
    private String generatePrintLambda(String identifier, Type type) {
		// TODO Auto-generated method stub
    	String result="";
    	if(type==null) {
    		return"";
    	}
    	if(type.getTypeName().contains("*")) {
    		result="cout << *"+identifier+" << endl;";
    	}else if((type.getTypeName().contains("["))){
//    		//System.out.println("lambda return type is []");
    	}else {
    		result="cout << "+identifier+" << endl;";
    	}
		return result;
	}

	/**
     * 2024.2.2
     * 根据参数列表生成调用的实参
     * @param parameterIdentifiers
     * @param lineOrder,List<String> addedDeclaration,LamdaSymbolRecord lam 
     * @param ifNeedOutCycle 
     * @param tokenAppearTimes 
     * @param index 
     * @param scopeNode 
     * @param st 
     * @return
     */
    private String generateParas(List<SymbolRecord> parameterIdentifiers, SymbolTable st, ScopeTree scopeNode, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, int lineOrder,List<String> addedDeclaration,LamdaSymbolRecord lam) {
		// TODO Auto-generated method stub
    	String result="";
        //参数列表部分
        List<Type> parameterList=lam.getParameterList();//函数的参数列表，key是标识符，value是标识符的类型
        if(parameterList==null||parameterList.size()==0){//方法没有参数
        	result+="()";
        }else{
        	result+="(";
            int count=0;
            for (Type type1 : parameterList) {
                String param= "";
                String paraStatement="";
                //在当前作用域内生成某个类型的标识符作为函数的实参
                SymbolRecord symbolRecord;
                if(type1!=null) {
                    switch (type1.getTypeName()) {
                        case "int[]": {
                            symbolRecord = generateArrayDeclarationByTypeInCur("int[]", st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                            paraStatement = "int" + " " + symbolRecord.getName() + "[" + CPlusConfigureInfo.arrayLength + "] = " + symbolRecord.getValue() + ";";
                            break;
                        }
                        case "int*": {
                            symbolRecord = generatePointerDeclarationByTypeInCur("int*", st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                            paraStatement = "int" + " *" + symbolRecord.getName() + " = &" + symbolRecord.getValue() + ";";
                            break;
                        }
                        default: {//非数组和指针变量
                            symbolRecord = generateDeclarationByTypeInCur(type1.getTypeName(), st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                            paraStatement = type1.getTypeName() + " " + symbolRecord.getName() + " = " + symbolRecord.getValue() + ";";
                        }
                    }
                }else{
                    System.out.println("function return type is missed 6");
                    return "";
                }
                addedDeclaration.add(paraStatement);//刷新符号表
                grammarGenerateService.updateSymTableByVar(st, symbolRecord);
                grammarGenerateService.updateIdentifierRelations("", paraStatement, scopeNode,st,lineOrder);
                param=symbolRecord.getName();
                if(count==0) {//第一个参数
                	result += param;
                    count++;
                }else{//第二个及之后的参数
                	result +=","+ param;
                    count++;
                }
            }
            result+=")";
        }
        return result;
	}

	/**
     * 2022.10.24
     * anonymousAuthor
     * @param str
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    private String generateMethodDeclaration(String str, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder,boolean isTemplate) {
        if(ConfigureInfo.selectedMutationStrategy==ConfigureInfo.mutationStrategyAstFun2Fun) {
            Random random = new Random();
            int num = random.nextInt(100);
            if (num > 50) {//一半概率用变异的函数，一半概率基于文法生成函数
                String result = "";
                List<String> fragments=new ArrayList<>();
                scopeNode = grammarGenerateService.updateScope(st, scopeNode);//方法定义也开启一个作用域，这是为了把方法的参数也能包含进作用域
                Mutation mut = new Mutation(ConfigureInfo.selectedMutationStrategy);
                fragments= mut.mutate(st, scopeNode, isTemplate,index,  tokenAppearTimes,  ifNeedOutCycle,this);//返回插入的函数文本 并且更新函数的符号表
                result=fragments.get(0);
                return result;
            }
        }

        String result="";
        scopeNode=grammarGenerateService.updateScope(st,scopeNode);//方法定义也开启一个作用域，这是为了把方法的参数也能包含进作用域
        String[] items=str.split(" ");//认为括号后面要么跟+？* 要么跟空格  typeTypeOrVoid identifier formalParameters methodBody
        String temResult="";
        List<SymbolRecord> returnIdentifiers=new ArrayList<>();
        Type type=new Type();
        String identifier="";
        List<Type> parameterList=new ArrayList<>();
        List<SymbolRecord> parameterIdentifiers=new ArrayList<>();
        Type locateType=new Type();//函数所属的类
        Constraint constraint=new Constraint();
        for(String item:items) {
            temResult = grammarGenerateService.generateProgramForMatchedValue(item, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            temResult = constraint.checkUniqueConstraint(temResult, item, index, grammars);//唯一性约束（*不可重复相同值）
            temResult = constraint.checkContinueConstraint(temResult, item, index, grammars);//连续性约束，，如果在约束列表中，temResult中不能有空格
            if (item.equals("typeTypeOrVoid")) {
            	if(temResult.equals("int[]")) {
                	temResult="int";
                }else if(temResult.equals("int*")) {
                	temResult="int";//返回值类型为指针类型时，有时会返回形参或者返回局部变量的地址，这会导致出现未定义行为
                }
                type =st.getTypeMap().get(temResult);
                if(type==null){
//                    //System.out.println("what is this" + temResult);
                }
                scopeNode.setType(type);//当前作用域内返回return语句需要返回的表达式的类型
            } else if (item.equals("identifier")) {
                identifier=grammarGenerateService.getIdentifier(index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
                
                List<String> identifierNameList=st.getIdentifierNameList();//更新标识符总表，以免在生成函数体的过程中生成一些和函数同名的变量
                identifierNameList.add(identifier);
                st.setIdentifierNameList(identifierNameList);
                
                temResult=identifier;
                if(type==null||type.getTypeName()==null) {
//                	//System.out.println("function returntype is null");
                }
                if(type.getTypeName().equals("int[]")) {
                	temResult+="[]";
                }
            }else if(item.equals("formalParameters")){
                //可以用逗号拆分开来，把各个参数类型提取出来，保存在parameterList当中
                if(temResult.trim().equals("")){
                    //参数列表为空
                }else{
                    String parameters= StringTools.deleteBrackets("(",")",temResult);
                    String[] parameterArr = parameters.split(",");//用逗号拆分参数列表
                    for(int j=0;j<parameterArr.length;j++){
                        String param=parameterArr[j].trim();
                        String[] paraInfo=param.split(" ");//用空格来分割类型和标识符
                        if(paraInfo.length<2){//一个参数 但是类型和标识符有问题
//                            //System.out.println("paraInfo.length less than 2: "+paraInfo.length);
                        }else if(paraInfo.length==2){//一个普通参数 类型和标识符匹配
                            String type1name=paraInfo[0].trim();
                            String ident1=paraInfo[1].trim();
                            Type type1=st.getTypeMap().get(type1name);
                            parameterList.add(type1);
                            SymbolRecord sr1=new SymbolRecord(ident1,type1,ident1,"var",scopeNode,"");
                            parameterIdentifiers.add(sr1);
                            grammarGenerateService.updateSymTableByVar(st,sr1);//参数列表中的形参也是函数体的作用域内可以使用的变量，更新到符号表
                        }else if(paraInfo.length==3){//一个指针参数 类型和标识符匹配
                            String type1name=paraInfo[0].trim();
                            String type1star=paraInfo[1].trim();
                            String ident1=paraInfo[2].trim();
                            if(type1star.equals("*")) {
	                            Type type1=st.getTypeMap().get(type1name+type1star);
	                            parameterList.add(type1);
	                            SymbolRecord sr1=new SymbolRecord(ident1,type1,ident1,"pointer",scopeNode,"");
	                            parameterIdentifiers.add(sr1);
	                            grammarGenerateService.updateSymTableByVar(st,sr1);//参数列表中的形参也是函数体的作用域内可以使用的变量，更新到符号表
                            }
                        }else if(paraInfo.length==4){//一个数组参数 类型和标识符匹配
                            String type1name=paraInfo[0].trim();
                            String ident1=paraInfo[1].trim();
                            String leftBracket=paraInfo[2].trim();
                            String rightBracket=paraInfo[3].trim();
                            if(leftBracket.equals("[")&&rightBracket.equals("]")) {
	                            Type type1=st.getTypeMap().get(type1name+leftBracket+rightBracket);
	                            parameterList.add(type1);
	                            SymbolRecord sr1=new SymbolRecord(ident1,type1,ident1,"array",scopeNode,"");
	                            sr1.setArrayLength(CPlusConfigureInfo.arrayLength);
	                            parameterIdentifiers.add(sr1);
	                            grammarGenerateService.updateSymTableByVar(st,sr1);//参数列表中的形参也是函数体的作用域内可以使用的变量，更新到符号表
                            }
                        }else{//一个参数 但是类型和标识符有问题
//                            //System.out.println("paraInfo.length more than 2: "+paraInfo.length);
                        }
                    }
                }

            }else if(item.equals("methodBody")){
                //暂时没想到是否需要特殊处理
                //return 方法返回值的添加
                Type returnType=scopeNode.getType();
                temResult=addReturn(temResult,returnType,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,lineOrder);
                
                int returnExp=temResult.lastIndexOf("return");
                if(returnExp>=0) {
                	returnIdentifiers=st.getIdentifiersFromStr(temResult.substring(temResult.lastIndexOf("return")),st);
                }
                String outPutStr=generateOutPut(st, scopeNode);
                temResult=insertStr2ScopeEnd(temResult,outPutStr);
            }
            result += " " + temResult;
        }
        locateType=scopeNode.getLocateType();
        FunctionSymbolRecord funSymbolRecord=new FunctionSymbolRecord(identifier,type,identifier,"function",scopeNode,locateType,parameterList,parameterIdentifiers);
        
        if(isTemplate) {
        	funSymbolRecord.setTemplateFun(isTemplate);
        }
        funSymbolRecord.setReturnIdentifiers(returnIdentifiers);

        //更新函数所在类的函数列表
        if(locateType!=null) {//如果类型不是空，方法是属于类的方法
	        List<FunctionSymbolRecord> functions=locateType.getFunctions();//类中的函数列表
	        functions.add(funSymbolRecord);
	        locateType.setFunctions(functions);
	        scopeNode.setLocateType(locateType);
        }
        
        else if(locateType==null) {//如果类型是空，方法是全局的方法，不是属于类的方法
	        ////System.out.println(funSymbolRecord.getName()); 
        }

        //更新函数列表
//        List<FunctionSymbolRecord> functions=st.getFunctionSymbolRecords();//函数列表
//        functions.add(funSymbolRecord);
//        st.setFunctionSymbolRecords(functions);

        grammarGenerateService.updateSymTableByFun(st,funSymbolRecord);//使用新声明的函数更新符号表模块
        scopeNode=scopeNode.getFather();//把作用域回溯到方法的声明之外


        return result;
    }

    /**
     * 2025.2.19
     * anonymousAuthor
     * @param str
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    private List<String> generateMethodDeclarations(String str, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder,boolean isTemplate) {

        if(ConfigureInfo.selectedMutationStrategy==ConfigureInfo.mutationStrategyAstFun2Fun) {
            Random random = new Random();
            int num = random.nextInt(100);
            if (num > 50) {//一半概率用变异的函数，一半概率基于文法生成函数
                List<String> result =new ArrayList<>();
                scopeNode = grammarGenerateService.updateScope(st, scopeNode);//方法定义也开启一个作用域，这是为了把方法的参数也能包含进作用域
                Mutation mut = new Mutation(ConfigureInfo.selectedMutationStrategy);
                result= mut.mutate(st, scopeNode, isTemplate,index,  tokenAppearTimes,  ifNeedOutCycle,this);//返回插入的函数文本 并且更新函数的符号表
                return result;
            }
        }
        List<String> resultList=new ArrayList<>();
        String result="";
        scopeNode=grammarGenerateService.updateScope(st,scopeNode);//方法定义也开启一个作用域，这是为了把方法的参数也能包含进作用域
        String[] items=str.split(" ");//认为括号后面要么跟+？* 要么跟空格  typeTypeOrVoid identifier formalParameters methodBody
        String temResult="";
        List<SymbolRecord> returnIdentifiers=new ArrayList<>();
        Type type=new Type();
        String identifier="";
        List<Type> parameterList=new ArrayList<>();
        List<SymbolRecord> parameterIdentifiers=new ArrayList<>();
        Type locateType=new Type();//函数所属的类
        Constraint constraint=new Constraint();
        for(String item:items) {
            temResult = grammarGenerateService.generateProgramForMatchedValue(item, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            temResult = constraint.checkUniqueConstraint(temResult, item, index, grammars);//唯一性约束（*不可重复相同值）
            temResult = constraint.checkContinueConstraint(temResult, item, index, grammars);//连续性约束，，如果在约束列表中，temResult中不能有空格
            if (item.equals("typeTypeOrVoid")) {
                if(temResult.equals("int[]")) {
                    temResult="int";
                }else if(temResult.equals("int*")) {
                    temResult="int";//返回值类型为指针类型时，有时会返回形参或者返回局部变量的地址，这会导致出现未定义行为
                }
                type =st.getTypeMap().get(temResult);
                if(type==null){
//                    //System.out.println("what is this" + temResult);
                }
                scopeNode.setType(type);//当前作用域内返回return语句需要返回的表达式的类型
            } else if (item.equals("identifier")) {
                identifier=grammarGenerateService.getIdentifier(index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);

                List<String> identifierNameList=st.getIdentifierNameList();//更新标识符总表，以免在生成函数体的过程中生成一些和函数同名的变量
                identifierNameList.add(identifier);
                st.setIdentifierNameList(identifierNameList);

                temResult=identifier;
                if(type==null||type.getTypeName()==null) {
//                	//System.out.println("function returntype is null");
                }
                if(type.getTypeName().equals("int[]")) {
                    temResult+="[]";
                }
            }else if(item.equals("formalParameters")){
                //可以用逗号拆分开来，把各个参数类型提取出来，保存在parameterList当中
                if(temResult.trim().equals("")){
                    //参数列表为空
                }else{
                    String parameters= StringTools.deleteBrackets("(",")",temResult);
                    String[] parameterArr = parameters.split(",");//用逗号拆分参数列表
                    for(int j=0;j<parameterArr.length;j++){
                        String param=parameterArr[j].trim();
                        String[] paraInfo=param.split(" ");//用空格来分割类型和标识符
                        if(paraInfo.length<2){//一个参数 但是类型和标识符有问题
//                            //System.out.println("paraInfo.length less than 2: "+paraInfo.length);
                        }else if(paraInfo.length==2){//一个普通参数 类型和标识符匹配
                            String type1name=paraInfo[0].trim();
                            String ident1=paraInfo[1].trim();
                            Type type1=st.getTypeMap().get(type1name);
                            parameterList.add(type1);
                            SymbolRecord sr1=new SymbolRecord(ident1,type1,ident1,"var",scopeNode,"");
                            parameterIdentifiers.add(sr1);
                            grammarGenerateService.updateSymTableByVar(st,sr1);//参数列表中的形参也是函数体的作用域内可以使用的变量，更新到符号表
                        }else if(paraInfo.length==3){//一个指针参数 类型和标识符匹配
                            String type1name=paraInfo[0].trim();
                            String type1star=paraInfo[1].trim();
                            String ident1=paraInfo[2].trim();
                            if(type1star.equals("*")) {
                                Type type1=st.getTypeMap().get(type1name+type1star);
                                parameterList.add(type1);
                                SymbolRecord sr1=new SymbolRecord(ident1,type1,ident1,"pointer",scopeNode,"");
                                parameterIdentifiers.add(sr1);
                                grammarGenerateService.updateSymTableByVar(st,sr1);//参数列表中的形参也是函数体的作用域内可以使用的变量，更新到符号表
                            }
                        }else if(paraInfo.length==4){//一个数组参数 类型和标识符匹配
                            String type1name=paraInfo[0].trim();
                            String ident1=paraInfo[1].trim();
                            String leftBracket=paraInfo[2].trim();
                            String rightBracket=paraInfo[3].trim();
                            if(leftBracket.equals("[")&&rightBracket.equals("]")) {
                                Type type1=st.getTypeMap().get(type1name+leftBracket+rightBracket);
                                parameterList.add(type1);
                                SymbolRecord sr1=new SymbolRecord(ident1,type1,ident1,"array",scopeNode,"");
                                sr1.setArrayLength(CPlusConfigureInfo.arrayLength);
                                parameterIdentifiers.add(sr1);
                                grammarGenerateService.updateSymTableByVar(st,sr1);//参数列表中的形参也是函数体的作用域内可以使用的变量，更新到符号表
                            }
                        }else{//一个参数 但是类型和标识符有问题
//                            //System.out.println("paraInfo.length more than 2: "+paraInfo.length);
                        }
                    }
                }

            }else if(item.equals("methodBody")){
                //暂时没想到是否需要特殊处理
                //return 方法返回值的添加
                Type returnType=scopeNode.getType();
                temResult=addReturn(temResult,returnType,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,lineOrder);

                int returnExp=temResult.lastIndexOf("return");
                if(returnExp>=0) {
                    returnIdentifiers=st.getIdentifiersFromStr(temResult.substring(temResult.lastIndexOf("return")),st);
                }
                String outPutStr=generateOutPut(st, scopeNode);
                temResult=insertStr2ScopeEnd(temResult,outPutStr);
            }
            result += " " + temResult;
        }
        locateType=scopeNode.getLocateType();
        FunctionSymbolRecord funSymbolRecord=new FunctionSymbolRecord(identifier,type,identifier,"function",scopeNode,locateType,parameterList,parameterIdentifiers);

        if(isTemplate) {
            funSymbolRecord.setTemplateFun(isTemplate);
        }
        funSymbolRecord.setReturnIdentifiers(returnIdentifiers);

        //更新函数所在类的函数列表
        if(locateType!=null) {//如果类型不是空，方法是属于类的方法
            List<FunctionSymbolRecord> functions=locateType.getFunctions();//类中的函数列表
            functions.add(funSymbolRecord);
            locateType.setFunctions(functions);
            scopeNode.setLocateType(locateType);
        }

        else if(locateType==null) {//如果类型是空，方法是全局的方法，不是属于类的方法
            ////System.out.println(funSymbolRecord.getName());
        }

        //更新函数列表
//        List<FunctionSymbolRecord> functions=st.getFunctionSymbolRecords();//函数列表
//        functions.add(funSymbolRecord);
//        st.setFunctionSymbolRecords(functions);

        grammarGenerateService.updateSymTableByFun(st,funSymbolRecord);//使用新声明的函数更新符号表模块
        scopeNode=scopeNode.getFather();//把作用域回溯到方法的声明之外

        resultList.add(result);
        return resultList;
    }

    private String addOutPut(String temResult, Type returnType, int index, Map<String, Integer> tokenAppearTimes,
			boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode, int lineOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * anonymousAuthor
     * 2022-11-14
     * 生成C中的特殊项如声明变量、方法、类型等
     * @param item
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    public String generateSpecialItemForCPlus(String item, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        String result="";
        Constraint constraint=new Constraint();
        if(ConfigureInfo.ifCPlusCode&&item!=""&&item.trim().equalsIgnoreCase(CPlusConfigureInfo.arrayVarName)){//引用数组变量标识符
            String temResult=generateIdentifierByType(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,"array","self",lineOrder);
            result =temResult;

        }else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.pointerVarName))){//引用指针变量标识符
            String temResult=generateIdentifierByType(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,"pointer","self",lineOrder);
            result =temResult;

        }
        
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(CPlusConfigureInfo.getVarnames().contains(item))){//引用变量标识符
            String temResult=generateIdentifierByType(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,"var","-",lineOrder);
            result =temResult;

        }
      //类声明相关
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.classDeclaration)||item.trim().equalsIgnoreCase(CPlusConfigureInfo.structDeclaration))){
            result=generateClassDeclaration(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }
        
      //模板函数相关
        if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.templateFunDeclaration))){
            result=generateTemplateFun(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }
        
        //main方法相关
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.mainContent))){
            result=generateMain(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            st.setMainScope(scopeNode.getFather());
            return result;
        }//变量相关
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.fieldDeclaration)||item.trim().equalsIgnoreCase(CPlusConfigureInfo.localDeclaration))){//声明文法特殊处理，记录类型，标识符，作用域等信息
            String temResult=generateDeclaration(item,st,scopeNode,index,ifNeedOutCycle,tokenAppearTimes,lineOrder);
            result += " " + temResult;
        }//数组变量相关
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.fieldArrayDeclaration)||item.trim().equalsIgnoreCase(CPlusConfigureInfo.localArrayDeclaration))){//声明数组文法特殊处理，记录类型，标识符，数组长度，作用域等信息
            String temResult=generateArrayDeclaration(item,st,scopeNode,index,ifNeedOutCycle,tokenAppearTimes,lineOrder);
            result += " " + temResult;
        }//指针变量相关
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.fieldPointerDeclaration)||item.trim().equalsIgnoreCase(CPlusConfigureInfo.localPointerDeclaration))){//声明数组文法特殊处理，记录类型，标识符，作用域等信息
            String temResult=generatePointerDeclaration(item,st,scopeNode,index,ifNeedOutCycle,tokenAppearTimes,lineOrder);
            result += " " + temResult;
        }//函数相关
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.methodDeclaration))){//声明方法
            String temResult=generateMethodDeclaration(item,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,lineOrder,false);
            result += " " + temResult;
        }//使用数组变量
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.arrayElement))) {
        	String temResult=generateIdentifierByType(item,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,"array","value",lineOrder);
            result += " " + temResult;
        }//使用指针变量
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.pointerValue))) {
        	String temResult=generateIdentifierByType(item,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,"pointer","value",lineOrder);
            result += " " + temResult;
        }//除法相关
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(CPlusConfigureInfo.divisorGrammars.contains(item))){
            String temResult=grammarGenerateService.generateProgramForMatchedValue(item, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            temResult = constraint.checkUniqueConstraint(temResult, item, index, grammars);//唯一性约束（*不可重复相同值）
            temResult = constraint.checkContinueConstraint(temResult, item, index, grammars);//连续性约束，如果在约束列表中，temResult中不能有空格
            temResult=zeroExclideC(temResult);
            result += " " + temResult;
        }//Lamda表达式
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.lamdaExpression))){//lamada表达式
        	String temResult=generateLamdaExpression(item,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,lineOrder);
            result += " " + temResult;
        }
        return result;
    }

    /**
     * anonymousAuthor
     * 2025-2-19
     * 生成C中的特殊项如声明变量、方法、类型等
     * @param item
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    public List<String> generateProgramsOfSpecialItemForCPlus(String item, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        List<String> resultList=new ArrayList<>();
        String result="";
        StringTools stringTools=new StringTools();
        Constraint constraint=new Constraint();
        if(ConfigureInfo.ifCPlusCode&&item!=""&&item.trim().equalsIgnoreCase(CPlusConfigureInfo.arrayVarName)){//引用数组变量标识符
            String temResult=generateIdentifierByType(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,"array","self",lineOrder);
            result =temResult;

        }else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.pointerVarName))){//引用指针变量标识符
            String temResult=generateIdentifierByType(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,"pointer","self",lineOrder);
            result =temResult;

        }

        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(CPlusConfigureInfo.getVarnames().contains(item))){//引用变量标识符
            String temResult=generateIdentifierByType(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,"var","-",lineOrder);
            result =temResult;

        }
        //类声明相关
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.classDeclaration)||item.trim().equalsIgnoreCase(CPlusConfigureInfo.structDeclaration))){
            result=generateClassDeclaration(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        }

        //模板函数相关
        if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.templateFunDeclaration))){
            List<String> temResultList=generateTemplateFuns(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            resultList=stringTools.combineStrList(resultList,temResultList);
            return resultList;
        }

        //main方法相关
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.mainContent))){
            result=generateMain(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            st.setMainScope(scopeNode.getFather());
        }//变量相关
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.fieldDeclaration)||item.trim().equalsIgnoreCase(CPlusConfigureInfo.localDeclaration))){//声明文法特殊处理，记录类型，标识符，作用域等信息
            String temResult=generateDeclaration(item,st,scopeNode,index,ifNeedOutCycle,tokenAppearTimes,lineOrder);
            result += " " + temResult;
        }//数组变量相关
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.fieldArrayDeclaration)||item.trim().equalsIgnoreCase(CPlusConfigureInfo.localArrayDeclaration))){//声明数组文法特殊处理，记录类型，标识符，数组长度，作用域等信息
            String temResult=generateArrayDeclaration(item,st,scopeNode,index,ifNeedOutCycle,tokenAppearTimes,lineOrder);
            result += " " + temResult;
        }//指针变量相关
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.fieldPointerDeclaration)||item.trim().equalsIgnoreCase(CPlusConfigureInfo.localPointerDeclaration))){//声明数组文法特殊处理，记录类型，标识符，作用域等信息
            String temResult=generatePointerDeclaration(item,st,scopeNode,index,ifNeedOutCycle,tokenAppearTimes,lineOrder);
            result += " " + temResult;
        }//函数相关
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.methodDeclaration))){//声明方法
            List<String> temResultList=generateMethodDeclarations(item,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,lineOrder,false);
            resultList=stringTools.combineStrList(resultList,temResultList);
            return resultList;
        }//使用数组变量
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.arrayElement))) {
            String temResult=generateIdentifierByType(item,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,"array","value",lineOrder);
            result += " " + temResult;
        }//使用指针变量
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.pointerValue))) {
            String temResult=generateIdentifierByType(item,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,"pointer","value",lineOrder);
            result += " " + temResult;
        }//除法相关
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(CPlusConfigureInfo.divisorGrammars.contains(item))){
            String temResult=grammarGenerateService.generateProgramForMatchedValue(item, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            temResult = constraint.checkUniqueConstraint(temResult, item, index, grammars);//唯一性约束（*不可重复相同值）
            temResult = constraint.checkContinueConstraint(temResult, item, index, grammars);//连续性约束，如果在约束列表中，temResult中不能有空格
            temResult=zeroExclideC(temResult);
            result += " " + temResult;
        }//Lamda表达式
        else if(ConfigureInfo.ifCPlusCode&&item!=""&&(item.trim().equalsIgnoreCase(CPlusConfigureInfo.lamdaExpression))){//lamada表达式
            List<String> temResultList=generateLamdaExpressions(item,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,lineOrder);
            resultList=stringTools.combineStrList(resultList,temResultList);
            return resultList;
        }
        resultList.add(result);
        return resultList;
    }
    
    /**
     * anonymousAuthor
     * 2024.4.9
     * @param str
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param sn
     * @return
     */
    public String generateClassDeclaration(String str, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree sn,int lineOrder) {
        String result="";
        //新的类开启一个作用域
        int scopeId=st.getScopeNum()+1;
        ScopeTree scopeNode=new ScopeTree(scopeId,null,"1",null,null);//作用域根节点
        scopeNode.setRoot(sn.getRoot());
        scopeNode.setFather(sn.getRoot());//生成全局变量多少的开关，不加这一行，每个类中会生成较多全局变量，只用一次，加这一行，每个新生成的全局变量会在备选列表中，因此生成的全局变量很少，每个变量参与的运算很多
        st.setScopeNum(scopeId);
        List<ScopeTree> children=sn.getRoot().getChildren();
        children.add(scopeNode);
        sn.getRoot().setChildren(children);

        String[] items=str.split(" ");//CLASS identifier classBody
        String temResult="";
        Type type=new Type();
        String identifier="";
        Constraint constraint=new Constraint();
        if(st!=null&&st.getProgramName()!=null&&!st.getProgramName().equals("")){
            type.setFileName(st.getProgramName());
        }else{
            return str;
        }
        //作用域即所在类更新
        int typeId=st.getTypeMap().size();
        type.setTypeId(typeId);
        Type outerType=scopeNode.getLocateType();
        if(outerType!=null){
            List<Type> innerTypes=outerType.getInnerTypes();
            innerTypes.add(type);
            outerType.setInnerTypes(innerTypes);
            type.setOuterType(outerType);
        }
        //生成类型标识符
        identifier=grammarGenerateService.getIdentifier(index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        type.setTypeName(identifier);

        type.setScopeNode(scopeNode);//生成全局变量多少的开关3
        scopeNode.setLocateType(type);
        type.setScopeNode(scopeNode);//生成全局变量多少的开关3


        //class体生成

        for(String item:items) {
            temResult = grammarGenerateService.generateProgramForMatchedValue(item, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            temResult = constraint.checkUniqueConstraint(temResult, item, index, grammars);//唯一性约束（*不可重复相同值）
            temResult = constraint.checkContinueConstraint(temResult, item, index, grammars);//连续性约束，，如果在约束列表中，temResult中不能有空格
            if (item.equals("identifier")) {
                temResult = identifier;
            }
            else if(item.equals("STRUCT")) {//类的类型是结构体还是class
            	type.setTypeCategory("struct");
                scopeNode.setLocateType(type);
                type.setScopeNode(scopeNode);

            }else if(item.equals("CLASS")) {
            	type.setTypeCategory("class");
                scopeNode.setLocateType(type);
                type.setScopeNode(scopeNode);
            }
            result += " " + temResult;
        }

        //作用域所在类回溯
        scopeNode.setLocateType(outerType);
        grammarGenerateService.updateSymTableByClass(st,type);//使用新声明的函数更新符号表模块
        //添加当前类中所有的未声明标识符到类的最顶端
        result=insertAddDeclarationToType(st,result,type);//把补充声明语句插入到生成的程序中
        return result;
    }

    /**
     * anonymousAuthor
     * 2022.10.30
     * @param str
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    public String generateMain(String str, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        String result="";
        List<String> addedDeclaration=new ArrayList<>();//补充的声明列表,main生成结束后将其添加在main函数最开始的位置

        //查找类型列表
        Map<String,Type> cusTomTypeMap=st.getCustomizedTypeMap();

        //为每个类创建一个对象
        result+=generateObjectByType(cusTomTypeMap,index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);


        //调用的每一个方法，从符号表中读取，在当前作用域中
        result+=generateObjectCallMethod(index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,addedDeclaration,lineOrder);

        //输出main中声明的变量以及对象的成员变量的值
        String printFragment=generateOutPut(st, scopeNode);
        printFragment+=generateOutPutVarClass(cusTomTypeMap,st, scopeNode);
        st.setPrintFragment(printFragment);//记录这些打印语句对应的程序片段，简化程序使用
        result+=printFragment;

        if(ConfigureInfo.mutationAvailable) {
            result += "//insertion-main-last-available\n";//main函数结束前的标记 用于程序变异插入其它语句
            Mutation mut=new Mutation(ConfigureInfo.selectedMutationStrategy);
            result=mut.mutate(st)+result;//在生成结果之前插入
            result=result+mut.mutate(st);//在生成结果之后插入
        }

        result+="return 0;";

        result=insertAddDeclarationToMain(addedDeclaration,result);//在result最开始的位置加入为声明标识符的声明语句

        if(ConfigureInfo.mutationAvailable) {
            result = "//insertion-main-first-available\n" + result;//main函数开始后的标记 用于程序变异插入其它语句
        }
        return result;
    }

    /**
     * 生成变量声明
     * @param str
     * @param st
     * @param scopeNode
     * @param index
     * @param ifNeedOutCycle
     * @return
     */
    public String generateDeclaration(String str, SymbolTable st, ScopeTree scopeNode, int index, boolean ifNeedOutCycle, Map<String, Integer> tempTokenAppearTimes,int lineOrder) {
        String item="primitiveType";
        String typeType="";
        String result="";
        String temResult = grammarGenerateService.generateProgramForMatchedValue(item, index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        //确定声明的变量的类型
        typeType = temResult.trim();
        //确定声明的变量的标识
        SymbolRecord symbolRecord;
        if(str.equals(CPlusConfigureInfo.fieldDeclaration)){//和全局变量声明文法相关 只能使用常量为其赋值
            symbolRecord=generateDeclarationByTypeInCurScope(typeType,st,scopeNode,index,tempTokenAppearTimes,ifNeedOutCycle, CPlusConfigureInfo.cPlusBaseTypeLiteral,lineOrder);
            result =typeType+" "+ symbolRecord.getName()+ " = "+symbolRecord.getValue()+";";
        }else{//和局部变量声明文法相关
            symbolRecord=generateDeclarationByTypeInCurScope(typeType,st,scopeNode,index,tempTokenAppearTimes,ifNeedOutCycle, CPlusConfigureInfo.cPlusBaseTypeExp,lineOrder);
            result =typeType+" "+ symbolRecord.getName()+ " = "+symbolRecord.getValue();
        }
        grammarGenerateService.updateSymTableByVar(st,symbolRecord);//更新符号表模块-定义标识符
        return result;
    }

    
    /**
     * anonymousAuthor
     * 2023.7.4
     * 生成数组变量声明
     * @param str
     * @param st
     * @param scopeNode
     * @param index
     * @param ifNeedOutCycle
     * @return
     */
    public String generateArrayDeclaration(String str, SymbolTable st, ScopeTree scopeNode, int index, boolean ifNeedOutCycle, Map<String, Integer> tempTokenAppearTimes,int lineOrder) {
        String typeType="int";
        String result="";
        //确定声明的变量的标识
        SymbolRecord symbolRecord;
        if(str.equals(CPlusConfigureInfo.fieldArrayDeclaration)){//和全局变量声明文法相关 只能使用常量为其赋值
            symbolRecord=generateArrayDeclarationByTypeInCurScope(typeType,st,scopeNode,index,tempTokenAppearTimes,ifNeedOutCycle, CPlusConfigureInfo.cPlusBaseTypeLiteral,lineOrder);
            result =typeType+" "+ symbolRecord.getName()+ "["+symbolRecord.getArrayLength()+"] = "+symbolRecord.getValue()+";";
        }else{//和局部数组变量声明文法相关
            symbolRecord=generateArrayDeclarationByTypeInCurScope(typeType,st,scopeNode,index,tempTokenAppearTimes,ifNeedOutCycle, CPlusConfigureInfo.cPlusBaseTypeExp,lineOrder);
            result =typeType+" "+ symbolRecord.getName()+ "["+symbolRecord.getArrayLength()+"] = "+symbolRecord.getValue();
        }
        grammarGenerateService.updateSymTableByVar(st,symbolRecord);//更新符号表模块-定义标识符
        return result;
    }

    /**
     * anonymousAuthor
     * 2023.7.4
     * 生成指针变量声明
     * @param str
     * @param st
     * @param scopeNode
     * @param index
     * @param ifNeedOutCycle
     * @return
     */
    public String generatePointerDeclaration(String str, SymbolTable st, ScopeTree scopeNode, int index, boolean ifNeedOutCycle, Map<String, Integer> tempTokenAppearTimes,int lineOrder) {
    	String typeType="int";
        String result="";
      
        //确定声明的变量的标识
        SymbolRecord symbolRecord;
        if(str.equals(CPlusConfigureInfo.fieldPointerDeclaration)){//和全局变量声明文法相关 只能使用常量为其赋值
            symbolRecord=generatePointerDeclarationByTypeInCurScope(typeType,st,scopeNode,index,tempTokenAppearTimes,ifNeedOutCycle, CPlusConfigureInfo.cPlusBaseTypeLiteral,lineOrder);
            result =typeType+" *"+ symbolRecord.getName()+ " = &"+symbolRecord.getValue()+";";
        }else{//和局部指针变量声明文法相关
            symbolRecord=generatePointerDeclarationByTypeInCurScope(typeType,st,scopeNode,index,tempTokenAppearTimes,ifNeedOutCycle, CPlusConfigureInfo.cPlusBaseTypeExp,lineOrder);
            result =typeType+" *"+ symbolRecord.getName()+ " = &"+symbolRecord.getValue();
        }
        grammarGenerateService.updateSymTableByVar(st,symbolRecord);//更新符号表模块-定义标识符
        return result;
    }

    /**
     * anonymousAuthor
     * 2022.10.25
     * 加入return语句
     * @param str
     * @param type
     * @return
     */
    public String addReturn(String str, Type type, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        String result="";
        String addStr="";
        if(type==null||type.getTypeName()==null){
            return str;
        }
        if(type.getTypeName().equals("void")){//方法的返回值是void类型
            //可以添加一个空的return语句到方法体结束之前
            addStr="return;";
        }else{
            //添加一条return expression; 到方法体的末尾
        	String returnContent=grammarGenerateService.constructProgramByValue(type.getGrammarExpression(),index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,lineOrder)+";";
            addStr="return "+returnContent;
            
        }
        if(str.lastIndexOf("}")==str.length()-1){//最后一位是}
            str=str.substring(0,str.length()-2);
            str+=" "+addStr;
            str+=" }";
        }
        return str;
    }

    /**
     * 2022-10-12生成一个标识符，确保有定义
     * @param strCopy
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @param identifierCategory var array pointer fun
     * @return
     */
    public String generateIdentifierByType(String strCopy, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,String identifierCategory,String object,int lineOrder) {
        String result="";
    	String typeType="";
        Map<String, Integer> tempTokenAppearTimes = tokenAppearTimes;
        switch(identifierCategory) {
			case "var": {
				strCopy=strCopy.trim();
		        //这里做类型的区分
		        String tokenType=strCopy.trim();
		        if(ConfigureInfo.ifCCode){
		            typeType=CConfigureInfo.varnamesMap.get(tokenType);
		        }else if(ConfigureInfo.ifPythonCode){
		            typeType= PythonConfigureInfo.varnamesMap.get(tokenType);
		        }else if(ConfigureInfo.ifCPlusCode){
		            typeType= CPlusConfigureInfo.varnamesMap.get(tokenType);
		        }else if(ConfigureInfo.ifJavaCode){
		            typeType= JavaConfigureInfo.varnamesMap.get(tokenType);
		        }
		        if(typeType==null||typeType==""){
		            typeType="int";
//		            //System.out.println("unknown type "+"strCopy");
		        }
		        break;
			}
			case "array": typeType="int[]";break;
			case "pointer": typeType="int*";break;
			default:typeType="int";break;
        }
        
        String identifierRelationStrategy=ConfigureInfo.identifierRelation;
        SymbolRecord sr=new SymbolRecord();
        switch(identifierRelationStrategy) {//"new", "nearest", "farthest ","random","PASI","PISS"
	        case "new": {
	        	result=getNewIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//新创建一个标识符，创建语句的位置插入到全局变量中
	        	break;
	        }
	        case "nearest": {
	        	result= getNearestIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//随机使用距离当前作用域最近的一个作用域内的可用标识符
	        	break;
	        }
	        case "farthest": {
	        	result=getFarthestIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//随机使用距离当前作用域最远的一个作用域内的可用标识符
	        	break;
	        }
	        case "random": {
	        	result= getRandomIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//随机使用一个已定义的标识符
	        	break;
	        }
	        case "PASI": {
	        	sr= getEvenPASIIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object);//根据PASI策略，选择一个已定义的标识符
	        	if(sr==null) {
	        		identifierRelationStrategy="new";
	        		result=getNewIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//新创建一个标识符，创建语句的位置插入到全局变量中
		        	break;
	        	}else {
	        		result=getResultBySymbolRecord(sr,identifierCategory,object);
	        	}
	        	break;
	        }
	        case "PISS": {
	        	sr= getEvenPISSIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object);//根据PASI策略，选择一个已定义的标识符
	        	if(sr==null) {
	        		identifierRelationStrategy="new";
	        		result=getNewIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//新创建一个标识符，创建语句的位置插入到全局变量中
		        	break;
	        	}else {
	        		result=getResultBySymbolRecord(sr,identifierCategory,object);
	        	}
	        	break;
	        }
	        default:{
	        	result= getRandomIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory,object,lineOrder);//随机使用一个已定义的标识符
	        	break;
	        }
	    }
        if(identifierRelationStrategy.equals("PASI")||identifierRelationStrategy.equals("PISS")) {
        	grammarGenerateService.updateSymTableBySymbolRecord(st,scopeNode,sr.getName());//标识符sr.getName()在scopeNode中的使用次数加1，更新符号表
        }else {
        	grammarGenerateService.updateSymTableBySymbolRecord(st,scopeNode,result);//标识符result在scopeNode中的使用次数加1，更新符号表
        }
        
        
        return result;
    }
    
    /**
	 * 2023-7-20
	 * anonymousAuthor
     * 选择使得标识符在可用的作用域上使用最均匀的标识符
     * @param typeType
     * @param st
     * @param scopeNode
     * @param index
     * @param tempTokenAppearTimes
     * @param ifNeedOutCycle
     * @return
     */
    private SymbolRecord getEvenPASIIdentifier(String typeType, SymbolTable st, ScopeTree scopeNode, int index,
			Map<String, Integer> tempTokenAppearTimes, boolean ifNeedOutCycle,String identifierCategory,String object) {
    	String result="";
    	List<SymbolRecord>  typeSymbolRecords=st.getSymbolRecords();//符号总表
        List<SymbolRecord>  validsymbolScopeIdentifier=getScopeIdentifier(typeSymbolRecords,scopeNode,typeType,identifierCategory);//取出可以使用的标识符名称
        if(validsymbolScopeIdentifier.size()>0){
        	double pasiCor=Double.MAX_VALUE;
        	double tempPasiCor=0;
        	SymbolRecord selectedSymbolRecord=new SymbolRecord();
        	for(SymbolRecord sr:validsymbolScopeIdentifier) {
        		tempPasiCor=getPasiCor(sr,scopeNode,typeSymbolRecords,st);
        		if(tempPasiCor<pasiCor) {
        			pasiCor=tempPasiCor;
        			selectedSymbolRecord=sr;
        		}
        	}
        	return selectedSymbolRecord;
        }else{
            return null;
        }
	}
    
    
    /**
	 * 2023-7-20
	 * anonymousAuthor
     * 选择使得标识符在可用的作用域上使用最均匀的标识符
     * @param typeType
     * @param st
     * @param scopeNode
     * @param index
     * @param tempTokenAppearTimes
     * @param ifNeedOutCycle
     * @return
     */
    private SymbolRecord getEvenPISSIdentifier(String typeType, SymbolTable st, ScopeTree scopeNode, int index,
			Map<String, Integer> tempTokenAppearTimes, boolean ifNeedOutCycle,String identifierCategory,String object) {
    	String result="";
    	List<SymbolRecord>  typeSymbolRecords=st.getSymbolRecords();//符号总表
        List<SymbolRecord>  validsymbolScopeIdentifier=getScopeIdentifier(typeSymbolRecords,scopeNode,typeType,identifierCategory);//取出可以使用的标识符名称
        if(validsymbolScopeIdentifier.size()>0){
        	double pissCor=Double.MIN_VALUE;
        	double tempPasiCor=0;
        	SymbolRecord selectedSymbolRecord=new SymbolRecord();
        	for(SymbolRecord sr:validsymbolScopeIdentifier) {
        		tempPasiCor=getPissCor(sr,scopeNode,typeSymbolRecords,st);
        		if(tempPasiCor>pissCor) {
        			pissCor=tempPasiCor;
        			selectedSymbolRecord=sr;
        		}
        	}
        	return selectedSymbolRecord;
        }else{
            return null;
        }
	}

    /**
     * 计算在当前作用域scopeNode中使用符号记录sr中的标识符后，程序的PASI值
     * @param correntSr
     * @param sn
     * @param symbolRecords 在这个表示符集合上做优化
     * @return
     */
    private double getPasiCor(SymbolRecord correntSr, ScopeTree sn,List<SymbolRecord> symbolRecords,SymbolTable st) {
		// TODO Auto-generated method stub
    	if(correntSr==null||sn==null||symbolRecords==null) {
    		return Double.MAX_VALUE;
    	}
    	correntSr.setScopeUsedTimeMapByData(sn,1);
    	grammarGenerateService.updateSymTableBySymbolRecord(st,correntSr);
    	
    	double result=0;
    	int n=0;
    	SymbolRecord sr;
    	int varUsed;
    	int varScopeNum;
    	int varMinTime;
    	Map<Integer,Integer> scopeUsedTimeMap;
    	for(int i=0;i<symbolRecords.size();i++) {
    		sr=symbolRecords.get(i);
    		varUsed=0;
    		varScopeNum=0;
    		varMinTime=Integer.MAX_VALUE;
    		List<ScopeTree> validScopeofSymbolRecord=new ArrayList<>();
    		validScopeofSymbolRecord=getAllChildren(sr.getScope());
    		
    		varScopeNum=validScopeofSymbolRecord.size();
    		for(ScopeTree scopeNode:validScopeofSymbolRecord) {
    			scopeUsedTimeMap=sr.getScopeUsedTimeMap();
    			if(scopeUsedTimeMap!=null) {
    				Integer varUsedSr=scopeUsedTimeMap.get(scopeNode.getScopeID());
    				if(varUsedSr!=null) {
    					
    					varUsed+=varUsedSr;
        				if(varUsedSr<varMinTime) {
            				varMinTime=varUsedSr;
            			}
    				}
    			}
    			
    		}
    		result+=varUsed/(varScopeNum*1.0)-varMinTime;
    	}
		return result;
	}

    /**
     * 计算在当前作用域scopeNode中使用符号记录sr中的标识符后，程序的PISS值
     * @param correntSr
     * @param sn
     * @param symbolRecords 在这个表示符集合上做优化
     * @return
     */
    private double getPissCor(SymbolRecord correntSr, ScopeTree sn,List<SymbolRecord> symbolRecords,SymbolTable st) {
		// TODO Auto-generated method stub
    	if(correntSr==null||sn==null||symbolRecords==null) {
    		return Double.MAX_VALUE;
    	}
    	correntSr.setScopeUsedTimeMapByData(sn,1);
    	grammarGenerateService.updateSymTableBySymbolRecord(st,correntSr);
    	
    	double result=0;
    	int n=0;
    	SymbolRecord sr;
    	int varUsed;
    	int varScopeNum;
    	int varMinTime;
    	Map<Integer,Integer> scopeUsedTimeMap;
    	for(int i=0;i<symbolRecords.size();i++) {
    		sr=symbolRecords.get(i);
    		varUsed=0;
    		varScopeNum=0;
    		varMinTime=Integer.MAX_VALUE;
    		List<ScopeTree> validScopeofSymbolRecord=new ArrayList<>();
    		validScopeofSymbolRecord=getAllChildren(sr.getScope());
    		
    		varScopeNum=validScopeofSymbolRecord.size();
    		for(ScopeTree scopeNode:validScopeofSymbolRecord) {
    			scopeUsedTimeMap=sr.getScopeUsedTimeMap();
    			if(scopeUsedTimeMap!=null) {
    				Integer varUsedSr=scopeUsedTimeMap.get(scopeNode.getScopeID());
    				if(varUsedSr!=null) {
    					result+=getPiss(varUsedSr);
    				}
    			}
    			
    		}
    	}
		return result;
	}

    /**
     * 返回1+1/2+1/4+1/8+...+1/2(n-1)
     * @param varUsedSr
     * @return
     */
    private double getPiss(Integer varUsedSr) {
		// TODO Auto-generated method stub
    	double result=0;
    	while(varUsedSr>0) {
    		result+=(Math.pow(2,-(varUsedSr)));
    		varUsedSr--;
    	}
		return result;
	}

	/**
     * 获取作用域的所有子作用域（包括作用域本身）
     * @return
     */
	private List<ScopeTree> getAllChildren(ScopeTree scope) {
		// TODO Auto-generated method stub
		List<ScopeTree> result=new ArrayList<>();
		if(scope==null||scope.getChildren()==null) {
			result.add(scope);
			return result;
		}else {
			List<ScopeTree> children=scope.getChildren();
			for(ScopeTree st:children) {
				result.addAll(getAllChildren(st));
			}
		}
		result.add(scope);
		return result;
	}

	/**
	 * 2023-6-15
	 * anonymousAuthor
     * 随机使用距离当前作用域最远的一个作用域内的可用标识符
     * @param typeType
     * @param st
     * @param scopeNode
     * @param index
     * @param tempTokenAppearTimes
     * @param ifNeedOutCycle
     * @return
     */
    private String getFarthestIdentifier(String typeType, SymbolTable st, ScopeTree scopeNode, int index,
			Map<String, Integer> tempTokenAppearTimes, boolean ifNeedOutCycle,String identifierCategory,String object,int lineOrder) {
    	String result="";
    	List<SymbolRecord>  typeSymbolRecords=st.getSymbolRecords();//符号总表
        List<SymbolRecord>  validsymbolScopeIdentifier=getFarestScopeIdentifier(typeSymbolRecords,scopeNode,typeType,identifierCategory);//取出距离当前作用域最远的作用域内，可以使用的标识符名称
        if(validsymbolScopeIdentifier.size()>0){
            int indexI=(int)(Math.random()*(validsymbolScopeIdentifier.size()-1));
            SymbolRecord sr=validsymbolScopeIdentifier.get(indexI);
            result=getResultBySymbolRecord(sr,identifierCategory,object);
            return result; 
        }else{
            return getNewIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle, identifierCategory, object,lineOrder);
        }
	}

    /**
	 * 2023-6-15
	 * anonymousAuthor
     * 随机使用距离当前作用域最近的一个作用域内的可用标识符
     * @param typeType
     * @param st
     * @param scopeNode
     * @param index
     * @param tempTokenAppearTimes
     * @param ifNeedOutCycle
     * @return
     */
	private String getNearestIdentifier(String typeType, SymbolTable st, ScopeTree scopeNode, int index,
			Map<String, Integer> tempTokenAppearTimes, boolean ifNeedOutCycle,String identifierCategory,String object,int lineOrder) {
		// TODO Auto-generated method stub
		String result="";
		List<SymbolRecord>  typeSymbolRecords=st.getSymbolRecords();//符号总表
        List<SymbolRecord>  validsymbolScopeIdentifier=getNearestScopeIdentifier(typeSymbolRecords,scopeNode,typeType,identifierCategory);//取出距离当前作用域最近的作用域内，可以使用的标识符名称
        if(validsymbolScopeIdentifier.size()>0){
            int indexI=(int)(Math.random()*(validsymbolScopeIdentifier.size()-1));
            SymbolRecord sr=validsymbolScopeIdentifier.get(indexI);
            result=getResultBySymbolRecord(sr,identifierCategory,object);
           return result; 
        }else{
            return getNewIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle, identifierCategory, object,lineOrder);
        }
	}

	
	/**
	 * 2023-6-15
	 * anonymousAuthor
     * 随机使用一个当前已定义的标识符
     * @param typeType
     * @param st
     * @param scopeNode
     * @param index
     * @param tempTokenAppearTimes
     * @param ifNeedOutCycle
     * @return
     */
	private String getRandomIdentifier(String typeType, SymbolTable st, ScopeTree scopeNode, int index,
			Map<String, Integer> tempTokenAppearTimes, boolean ifNeedOutCycle,String identifierCategory,String object,int lineOrder) {
		// TODO Auto-generated method stub
		String result="";
		List<SymbolRecord>  typeSymbolRecords=st.getSymbolRecords();//符号总表
        List<SymbolRecord>  validsymbolScopeIdentifier=getScopeIdentifier(typeSymbolRecords,scopeNode,typeType,identifierCategory);//取出在当前scope可以使用的标识符名称
        if(validsymbolScopeIdentifier.size()>0){
            int indexI=(int)(Math.random()*(validsymbolScopeIdentifier.size()-1));
            SymbolRecord sr=validsymbolScopeIdentifier.get(indexI);
            result=getResultBySymbolRecord(sr,identifierCategory,object);
            return result; 
        }else{
            return getNewIdentifier(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,identifierCategory, object,lineOrder);
        }
	}

	/**
	 * 2023-6-15
	 * anonymousAuthor
	 * 新创建一个标识符，创建语句的位置插入到全局变量中
	 * @param typeType
	 * @param st
	 * @param scopeNode
	 * @param index
	 * @param tempTokenAppearTimes
	 * @param ifNeedOutCycle
	 * @param identifierCategory
	 * @return
	 */
	public String getNewIdentifier(String typeType, SymbolTable st, ScopeTree scopeNode, int index,
				Map<String, Integer> tempTokenAppearTimes, boolean ifNeedOutCycle,String identifierCategory,String object,int lineOrder) {
			// TODO Auto-generated method stub
		String result="";
		int addCount=0;
		Map<String,List<String>> addedDeclarationMap=st.getAddedDeclaration();
        List<String> addedDeclaration=addedDeclarationMap.get(st.getProgramName());//补充的声明列表
        if(addedDeclaration==null){
            addedDeclaration=new ArrayList<>();
        }
        
		SymbolRecord symbolRecord;
		String defineStatement="";
		switch(identifierCategory) {
			case "var": {
				symbolRecord=generateDeclarationByType(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,lineOrder);//添加新变量
				defineStatement=typeType+" "+ symbolRecord.getName()+ " = "+symbolRecord.getValue()+";";
				result=symbolRecord.getName();
				
				
				break;
			}
			case "array": {
				symbolRecord=generateArrayDeclarationByType(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,lineOrder);//添加新数组变量
				defineStatement="int"+" "+ symbolRecord.getName()+ "["+symbolRecord.getArrayLength()+"] = "+symbolRecord.getValue()+";";
				int elementIndex=(int)(Math.random()*symbolRecord.getArrayLength());
				if(object.equals("value")) {
					result=symbolRecord.getName()+"["+elementIndex+"]";
				}else if(object.equals("self")) {
					result=symbolRecord.getName();
				}
				
				break;
			}
			case "pointer": {
				symbolRecord=generatePointerDeclarationByType(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,lineOrder);//添加新数组变量
				//刷新符号表
				
				addedDeclarationMap=st.getAddedDeclaration();
		        addedDeclaration=addedDeclarationMap.get(st.getProgramName());//补充的声明列表
		        if(addedDeclaration==null){
		            addedDeclaration=new ArrayList<>();
		        }
		        
		        //向补充声明列表中添加指针指向的地址
		        defineStatement="int"+" *"+ symbolRecord.getName()+ " = &"+symbolRecord.getValue()+";";
				if(object.equals("value")) {
					result="*"+symbolRecord.getName();
				}else if(object.equals("self")) {
					result=symbolRecord.getName();
				}
				
				break;
			}
			
			default:{
				symbolRecord=generateDeclarationByType(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,lineOrder);//添加新变量
				defineStatement=typeType+" "+ symbolRecord.getName()+ " = "+symbolRecord.getValue()+";";
				result=symbolRecord.getName();
				break;
			}
		}
		
		addedDeclaration.add(defineStatement);
		addCount=addedDeclaration.size();
		addedDeclarationMap.put(st.getProgramName(),addedDeclaration);
        st.setAddedDeclaration(addedDeclarationMap);
        grammarGenerateService.updateSymTableByVar(st,symbolRecord);
        grammarGenerateService.updateIdentifierRelations("", defineStatement, scopeNode,st,(0-addCount));
        
        return result;
	}

	/**
	 * anonymousAuthor
	 * 2023.7.4
	 * 根据标识符的类型返回一个标识符，或者一个该类型的值
	 * @param sr
	 * @param identifierCategory
	 * @return
	 */
	private String getResultBySymbolRecord(SymbolRecord sr,String identifierCategory,String object) {
		// TODO Auto-generated method stub
		String result="";
		switch(identifierCategory) {
			case "var": {
				result=sr.getName();
				break;
			}
			case "array": {
				int elementIndex=(int)(Math.random()*sr.getArrayLength());
				if(object.equals("value")) {
					result=sr.getName()+"["+elementIndex+"]";
				}else if(object.equals("self")) {
					result=sr.getName();
				}
				
				break;
			}
			case "pointer": {
				if(object.equals("value")) {
					result="*"+sr.getName();
				}else if(object.equals("self")) {
					result=sr.getName();
				}
				break;
			}
			
			default:{
				result=sr.getName();
				break;
			}
		}
		return result;
	}



	/**
     * 2022-10-12
     * 筛选出作用域内可用的，同类型的标识符名称
     * @param symbolRecords
     * @param scopeNode
     * @param typeType
     * @return
     */
    public List<SymbolRecord> getScopeIdentifier(List<SymbolRecord> symbolRecords, ScopeTree scopeNode, String typeType,String identifierCategory) {
        List<SymbolRecord> result=new ArrayList<>();
        List<Integer> scopeIDs=grammarGenerateService.getValidScopeIDs(scopeNode);//获取当前作用域以及父级作用域的ID
        if(scopeIDs.size()==0||symbolRecords==null||symbolRecords.size()==0){
            return result;
        }
        for(SymbolRecord sr : symbolRecords){
            if(sr.getType()==null){
                continue;
            }
            if(scopeIDs.contains(sr.getScope().getScopeID())&&typeType.equals(sr.getType().getTypeName())&&identifierCategory.equals(sr.getCategory())){
                result.add(sr);
            }
        }
        return result;
    }
    
    /**
     * 2023-6-15
     * 筛选出距离当前作用域最近的作用域内可用的，同类型的标识符名称
     */
    public List<SymbolRecord> getNearestScopeIdentifier(List<SymbolRecord> symbolRecords, ScopeTree scopeNode, String typeType,String identifierCategory) {
        List<SymbolRecord> result=new ArrayList<>();
        List<Integer> scopeIDs=grammarGenerateService.getValidScopeIDs(scopeNode);//获取当前作用域以及父级作用域的ID
        if(scopeIDs.size()==0||symbolRecords==null||symbolRecords.size()==0){
            return result;
        }
        for(int i=0;i< scopeIDs.size();i++) {
        	for(SymbolRecord sr : symbolRecords){
                if(sr.getType()==null){
                    continue;
                }
                if(scopeIDs.get(i).equals(sr.getScope().getScopeID())&&typeType.equals(sr.getType().getTypeName())&&identifierCategory.equals(sr.getCategory())){
                    result.add(sr);
                }
            }
        	if(result.size()>0) {
        		break;
        	}
        }
        return result;
    }
    
    /**
     * 2023-6-15
     * 筛选出距离当前作用域最远的作用域内可用的，同类型的标识符名称
     */
    public List<SymbolRecord> getFarestScopeIdentifier(List<SymbolRecord> symbolRecords, ScopeTree scopeNode, String typeType,String identifierCategory) {
        List<SymbolRecord> result=new ArrayList<>();
        List<Integer> scopeIDs=grammarGenerateService.getValidScopeIDs(scopeNode);//获取当前作用域以及父级作用域的ID
        if(scopeIDs.size()==0||symbolRecords==null||symbolRecords.size()==0){
            return result;
        }
        for(int i=scopeIDs.size()-1;i>= 0;i--) {
        	for(SymbolRecord sr : symbolRecords){
                if(sr.getType()==null){
                    continue;
                }
                if(scopeIDs.get(i).equals(sr.getScope().getScopeID())&&typeType.equals(sr.getType().getTypeName())&&identifierCategory.equals(sr.getCategory())){
                    result.add(sr);
                }
            }
        	if(result.size()>0) {
        		break;
        	}
        }
        return result;
    }

    /**
     * 2022-10-27
     * 在生成类中，一些标识符未声明，记录在临时列表里，在类生成结束后，一次性的添加到类中
     * @param st
     * @param program
     * @return
     */
    public String insertAddDeclarationToType(SymbolTable st, String program, Type type) {
        List<String> addedDeclaration=st.getAddedDeclaration().get(type.getTypeName());
        if(addedDeclaration==null||addedDeclaration.size()==0){
            return program;
        }
        String result;
        String declarationString="";
        for(String ad:addedDeclaration){
            declarationString+=ad;
        }
        String head=program.substring(0,program.indexOf("{")+1);
        String tail=program.substring(program.indexOf("{")+1);

        result=head+declarationString+tail;
        return result;

    }

    /**
     * 2022-10-12
     * 在合适的位置生成一个typeType类型的变量声明，方便当前作用域内使用
     * @param typeType
     * @param st
     * @param scopeNode
     * @return
     */
    private SymbolRecord generateDeclarationByType(String typeType, SymbolTable st, ScopeTree scopeNode,int index,Map<String,Integer> tempTokenAppearTimes,boolean ifNeedOutCycle,int lineOrder) {
        SymbolRecord sr=generateDeclarationByTypeInCurScope(typeType, st, scopeNode, index,tempTokenAppearTimes, ifNeedOutCycle, CPlusConfigureInfo.cPlusBaseTypeLiteral,lineOrder);
//        sr.setScope(scopeNode.getLocateType().getScopeNode());//用当前作用域所在类的作用域
        sr.setScope(scopeNode.getRoot());//用根作用域 生成大量全局变量开关2
        return sr;
    }


    /**
     * 2023-7-4
     * 在合适的位置生成一个typeType类型的数组变量声明，方便当前作用域内使用
     * @param typeType
     * @param st
     * @param scopeNode
     * @return
     */
    private SymbolRecord generateArrayDeclarationByType(String typeType, SymbolTable st, ScopeTree scopeNode,int index,Map<String,Integer> tempTokenAppearTimes,boolean ifNeedOutCycle,int lineOrder) {
        SymbolRecord sr=generateArrayDeclarationByTypeInCurScope(typeType, st, scopeNode, index,tempTokenAppearTimes, ifNeedOutCycle, CPlusConfigureInfo.cPlusBaseTypeLiteral,lineOrder);
//        sr.setScope(scopeNode.getLocateType().getScopeNode());//用当前作用域所在类的作用域
        sr.setScope(scopeNode.getRoot());//用根作用域 生成大量全局变量开关2
        return sr;
    }
    /**
     * 2023-7-4
     * 在合适的位置生成一个typeType类型的指针变量声明，方便当前作用域内使用
     * @param typeType
     * @param st
     * @param scopeNode
     * @return
     */
    private SymbolRecord generatePointerDeclarationByType(String typeType, SymbolTable st, ScopeTree scopeNode,int index,Map<String,Integer> tempTokenAppearTimes,boolean ifNeedOutCycle,int lineOrder) {
        SymbolRecord sr=generatePointerDeclarationByTypeInCurScope(typeType, st, scopeNode, index,tempTokenAppearTimes, ifNeedOutCycle, CPlusConfigureInfo.cPlusBaseTypeLiteral,lineOrder);
//        sr.setScope(scopeNode.getLocateType().getScopeNode());//用当前作用域所在类的作用域
        sr.setScope(scopeNode.getRoot());//用根作用域 生成大量全局变量开关2
        return sr;
    }
    
    /**
     * anonymousAuthor
     * 2022.10.31
     * 为每个类创建一个对象
     * @param cusTomTypeMap
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    private String generateObjectByType(Map<String,Type> cusTomTypeMap, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        String result="";
        String createObjectStr="";
        String objectIdentifier="";
        String objectValue="";
        for (Map.Entry<String,Type> entry : cusTomTypeMap.entrySet()) {
            createObjectStr="";
            String typename=entry.getKey();
            Type type=entry.getValue();
            objectIdentifier=grammarGenerateService.getIdentifier(index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            objectValue="new "+typename+"();";
            //createObjectStr+=typename+" "+objectIdentifier+"= "+objectValue;
            createObjectStr+=typename+"<"+CPlusConfigureInfo.Typename+">"+" "+objectIdentifier+";";//
            //更新符号表
            SymbolRecord sr=new SymbolRecord(objectIdentifier,type,objectIdentifier,"object",scopeNode,objectValue);
            grammarGenerateService.updateSymTableByObj(st,sr);
            result+=createObjectStr;
        }
        return result;
    }

    /**
     * anonymousAuthor
     * 2022.10.31
     *  调用每个类中的每一个方法，从符号表中读取，在当前作用域中，且category是object，或者直接读取
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    private String generateObjectCallMethod( int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,List<String> addedDeclaration,int lineOrder) {
        String result="";
        
        List<SymbolRecord>  objSymbolRecords=st.getObjSymbolRecords();//对象名
        for(SymbolRecord obj:objSymbolRecords){
            Type type=obj.getType();//对象的类型
            List<FunctionSymbolRecord> functions=type.getFunctions();//类中的函数列表
            result+=generateCallsClassForFuns(functions,obj,st,scopeNode, index, tokenAppearTimes, ifNeedOutCycle,lineOrder,addedDeclaration);
            
        }
        
        List<FunctionSymbolRecord> functions1=st.getFunctionSymbolRecords();//函数列表
        
        result+=generateCallsForBlobalFuns(functions1,st,scopeNode, index, tokenAppearTimes, ifNeedOutCycle,lineOrder,addedDeclaration);
        
        
        
        return result;
    }
    
    /**
     * 为全局方法生调用
     * @param functions
     * @param st
     * @param scopeNode
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param lineOrder
     * @param addedDeclaration 
     * @return
     */
    private String generateCallsForBlobalFuns(List<FunctionSymbolRecord> functions, SymbolTable st, ScopeTree scopeNode,
			int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, int lineOrder, List<String> addedDeclaration) {
    	String result="";
    	String functionCall="";
        for(FunctionSymbolRecord fun:functions){
        	if(fun.getLocateType()!=null) {
        		continue;
        	}
        	
            functionCall="";//初始化方法调用生成结内容
            //方法返回值部分
            Type returnType=fun.getReturnType();//方法的返回值类型
            if(returnType==null||returnType.getTypeName()==null||returnType.getTypeName().equals("void")){//方法没有返回值 或者是插入的函数算子，没有把返回值类型更新到函数的符号表上

            }else{//方法有返回值
                SymbolRecord symbolRecord;
                if(returnType!=null) {
                    switch (returnType.getTypeName()) {
                        case "int[]": {
                            symbolRecord = generateArrayDeclarationByTypeInCur("int[]", st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                            functionCall += "int " + symbolRecord.getName() + "[" + CPlusConfigureInfo.arrayLength + "]";
                            break;
                        }
                        case "int*": {
                            symbolRecord = generatePointerDeclarationByTypeInCur("int*", st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                            functionCall += "int *" + symbolRecord.getName();
                            break;
                        }
                        default: {//非数组和指针变量
                            functionCall += returnType.getTypeName();
                            functionCall += " ";
                            symbolRecord = generateDeclarationByTypeInCur(returnType.getTypeName(), st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);
                            functionCall += symbolRecord.getName();
                        }
                    }
                }
                else{
                    System.out.println("function return type is missed 7");
                    return "";
                }
                grammarGenerateService.updateSymTableByVar(st, symbolRecord);
                functionCall+="=";
            }
            //方法名
            functionCall+=fun.getName();
            if(fun.isTemplateFun()) {
            	functionCall+="<"+CPlusConfigureInfo.Typename+">";
        	}

            //参数列表部分
            List<Type> parameterList=fun.getParameterList();//函数的参数列表，key是标识符，value是标识符的类型
            if(parameterList==null||parameterList.size()==0){//方法没有参数
                functionCall+="()";
            }else{
                functionCall+="(";
                int count=0;
                for (Type type1 : parameterList) {
                    String param= "";
                    String paraStatement="";
                    //在当前作用域内生成某个类型的标识符作为函数的实参
                    SymbolRecord symbolRecord;
                    if(type1!=null) {
                        switch (type1.getTypeName()) {
                            case "int[]": {
                                symbolRecord = generateArrayDeclarationByTypeInCur("int[]", st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                                paraStatement = "int" + " " + symbolRecord.getName() + "[" + CPlusConfigureInfo.arrayLength + "] = " + symbolRecord.getValue() + ";";
                                break;
                            }
                            case "int*": {
                                symbolRecord = generatePointerDeclarationByTypeInCur("int*", st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                                paraStatement = "int" + " *" + symbolRecord.getName() + " = &" + symbolRecord.getValue() + ";";
                                break;
                            }
                            default: {//非数组和指针变量
                                symbolRecord = generateDeclarationByTypeInCur(type1.getTypeName(), st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                                paraStatement = type1.getTypeName() + " " + symbolRecord.getName() + " = " + symbolRecord.getValue() + ";";
                            }
                        }
                    }else{
                        System.out.println("function return type is missed");
                        return "";
                    }
                    addedDeclaration.add(paraStatement);//刷新符号表
                    grammarGenerateService.updateSymTableByVar(st, symbolRecord);
                    grammarGenerateService.updateIdentifierRelations("", paraStatement, scopeNode,st,lineOrder);
                    param=symbolRecord.getName();
                    if(count==0) {//第一个参数
                        functionCall += param;
                        count++;
                    }else{//第二个及之后的参数
                        functionCall +=","+ param;
                        count++;
                    }
                }
                functionCall+=")";
            }
            //语句结束符号;
            functionCall+=";";
            result+=functionCall;
            
            grammarGenerateService.updateIdentifierRelations("", functionCall, scopeNode,st,lineOrder);
        }
		return result;
	}

	private String generateCallsClassForFuns(List<FunctionSymbolRecord> functions, SymbolRecord obj, SymbolTable st,
			ScopeTree scopeNode, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle,
			int lineOrder, List<String> addedDeclaration) {
		String result="";
    	String functionCall="";
        for(FunctionSymbolRecord fun:functions){
            functionCall="";//初始化方法调用生成结内容
            //方法返回值部分
            Type returnType=fun.getReturnType();//方法的返回值类型
            if(returnType==null||returnType.getTypeName().equals("void")){//方法没有返回值

            }else{//方法有返回值
                SymbolRecord symbolRecord;
                if(returnType!=null) {
                    switch (returnType.getTypeName()) {
                        case "int[]": {
                            symbolRecord = generateArrayDeclarationByTypeInCur("int[]", st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                            functionCall += "int " + symbolRecord.getName() + "[" + CPlusConfigureInfo.arrayLength + "]";
                            break;
                        }
                        case "int*": {
                            symbolRecord = generatePointerDeclarationByTypeInCur("int*", st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                            functionCall += "int *" + symbolRecord.getName();
                            break;
                        }
                        default: {//非数组和指针变量
                            functionCall += returnType.getTypeName();
                            functionCall += " ";
                            symbolRecord = generateDeclarationByTypeInCur(returnType.getTypeName(), st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);
                            functionCall += symbolRecord.getName();
                        }
                    }
                }else{
                    System.out.println("function return type is missed 3");
                    return "";
                }
                grammarGenerateService.updateSymTableByVar(st, symbolRecord);
                functionCall+="=";
            }
            //方法名
            //functionCall+=fun.getName();
          //对象及方法名
            functionCall+=obj.getName();
            functionCall+=".";
            functionCall+=fun.getName();

            //参数列表部分
            List<Type> parameterList=fun.getParameterList();//函数的参数列表，key是标识符，value是标识符的类型
            if(parameterList==null||parameterList.size()==0){//方法没有参数
                functionCall+="()";
            }else{
                functionCall+="(";
                int count=0;
                for (Type type1 : parameterList) {
                    String param= "";
                    String paraStatement="";
                    //在当前作用域内生成某个类型的标识符作为函数的实参
                    SymbolRecord symbolRecord;
                    if(type1!=null) {
                        switch (type1.getTypeName()) {
                            case "int[]": {
                                symbolRecord = generateArrayDeclarationByTypeInCur("int[]", st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                                paraStatement = "int" + " " + symbolRecord.getName() + "[" + CPlusConfigureInfo.arrayLength + "] = " + symbolRecord.getValue() + ";";
                                break;
                            }
                            case "int*": {
                                symbolRecord = generatePointerDeclarationByTypeInCur("int*", st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                                paraStatement = "int" + " *" + symbolRecord.getName() + " = &" + symbolRecord.getValue() + ";";
                                break;
                            }
                            default: {//非数组和指针变量
                                symbolRecord = generateDeclarationByTypeInCur(type1.getTypeName(), st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                                paraStatement = type1.getTypeName() + " " + symbolRecord.getName() + " = " + symbolRecord.getValue() + ";";
                            }
                        }
                    }else{
                        System.out.println("function return type is missed 4");
                        return "";
                    }
                    addedDeclaration.add(paraStatement);//刷新符号表
                    grammarGenerateService.updateSymTableByVar(st, symbolRecord);
                    grammarGenerateService.updateIdentifierRelations("", paraStatement, scopeNode,st,lineOrder);
                    param=symbolRecord.getName();
                    if(count==0) {//第一个参数
                        functionCall += param;
                        count++;
                    }else{//第二个及之后的参数
                        functionCall +=","+ param;
                        count++;
                    }
                }
                functionCall+=")";
            }
            //语句结束符号;
            functionCall+=";";
            result+=functionCall;
            
            grammarGenerateService.updateIdentifierRelations("", functionCall, scopeNode,st,lineOrder);
        }
		return result;
	}

	/**
     * anonymousAuthor
     * 2024.2.2
     *  调用当前定义的lamda表达式中的匿名函数，从符号表中读取，在当前作用域中，且category是object，或者直接读取
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    private String generateObjectCallLamda( int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,List<String> addedDeclaration,int lineOrder,LamdaSymbolRecord lam) {
        String result="";
        String lamdaCall="";
        lamdaCall="";//初始化方法调用生成结内容
        //方法返回值部分
        Type returnType=lam.getReturnType();//方法的返回值类型
        if(returnType==null||returnType.getTypeName().equals("void")){//方法没有返回值

        }else{//方法有返回值
            SymbolRecord symbolRecord;
            if(returnType!=null) {
                switch (returnType.getTypeName()) {
                    case "int[]": {
                        symbolRecord = generateArrayDeclarationByTypeInCur("int[]", st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                        lamdaCall += "int " + symbolRecord.getName() + "[" + CPlusConfigureInfo.arrayLength + "]";
                        break;
                    }
                    case "int*": {
                        symbolRecord = generatePointerDeclarationByTypeInCur("int*", st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                        lamdaCall += "int *" + symbolRecord.getName();
                        break;
                    }
                    default: {//非数组和指针变量
                        lamdaCall += returnType.getTypeName();
                        lamdaCall += " ";
                        symbolRecord = generateDeclarationByTypeInCur(returnType.getTypeName(), st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);
                        lamdaCall += symbolRecord.getName();
                    }
                }
            }else{
                System.out.println("function return type is missed 5");
                return "";
            }
            grammarGenerateService.updateSymTableByVar(st, symbolRecord);
            lamdaCall+="=";
        }
        //方法名
        lamdaCall+=lam.getName();

        //参数列表部分
        List<Type> parameterList=lam.getParameterList();//函数的参数列表，key是标识符，value是标识符的类型
        if(parameterList==null||parameterList.size()==0){//方法没有参数
            lamdaCall+="()";
        }else{
            lamdaCall+="(";
            int count=0;
            for (Type type1 : parameterList) {
                String param= "";
                String paraStatement="";
                //在当前作用域内生成某个类型的标识符作为函数的实参
                SymbolRecord symbolRecord;
                if(type1!=null) {
                    switch (type1.getTypeName()) {
                        case "int[]": {
                            symbolRecord = generateArrayDeclarationByTypeInCur("int[]", st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                            paraStatement = "int" + " " + symbolRecord.getName() + "[" + CPlusConfigureInfo.arrayLength + "] = " + symbolRecord.getValue() + ";";
                            break;
                        }
                        case "int*": {
                            symbolRecord = generatePointerDeclarationByTypeInCur("int*", st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                            paraStatement = "int" + " *" + symbolRecord.getName() + " = &" + symbolRecord.getValue() + ";";
                            break;
                        }
                        default: {//非数组和指针变量
                            symbolRecord = generateDeclarationByTypeInCur(type1.getTypeName(), st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle, lineOrder);//
                            paraStatement = type1.getTypeName() + " " + symbolRecord.getName() + " = " + symbolRecord.getValue() + ";";
                        }
                    }
                }else{
                    System.out.println("function return type is missed 1");
                    return "";
                }
                addedDeclaration.add(paraStatement);//刷新符号表
                grammarGenerateService.updateSymTableByVar(st, symbolRecord);
                grammarGenerateService.updateIdentifierRelations("", paraStatement, scopeNode,st,lineOrder);
                param=symbolRecord.getName();
                if(count==0) {//第一个参数
                    lamdaCall += param;
                    count++;
                }else{//第二个及之后的参数
                    lamdaCall +=","+ param;
                    count++;
                }
            }
            lamdaCall+=")";
        }
        //语句结束符号;
        lamdaCall+=";";
        result+=lamdaCall;
        
        grammarGenerateService.updateIdentifierRelations("", lamdaCall, scopeNode,st,lineOrder);
        
        return result;
    }
    

    /**
     * 2022-10-12
     * 在当前作用域内生成一个typeType类型的变量声明。
     * @param typeType
     * @param st
     * @param scopeNode
     * @return
     */
    public SymbolRecord generateDeclarationByTypeInCurScope(String typeType, SymbolTable st, ScopeTree scopeNode, int index, Map<String, Integer> tempTokenAppearTimes, boolean ifNeedOutCycle, Map<String, String> typeExpression,int lineOrder) {
        String declaration = typeType;
        String identifier = "";
        String identifierValue = "";
        Constraint constraint = new Constraint();
        identifier = grammarGenerateService.getIdentifier(index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode, lineOrder);
        identifierValue = grammarGenerateService.generateProgramForMatchedValue(typeExpression.get(typeType), index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode, lineOrder);
        identifierValue = constraint.checkUniqueConstraint(identifierValue, typeExpression.get(typeType), index, grammars);//唯一性约束（*不可重复相同值）
        identifierValue = constraint.checkContinueConstraint(identifierValue, typeExpression.get(typeType), index, grammars);//连续性约束，如果在约束列表中，temResult中不能有空格
        Type type = st.getTypeMap().get(typeType);
        if (type == null) {
            Map<String, Type> typeMap =st.getTypeMap();
            Type tempType = new Type(typeMap.size()+1, typeType);//类型
            typeMap.put(typeType,tempType);
            st.setTypeMap(typeMap);
        }
        if (type == null) {
            System.out.println("what is this type: " + typeType);
        }
        declaration += " " +identifier+ " = "+identifierValue;
        return new SymbolRecord(identifier,type,identifier,"var",scopeNode,identifierValue);
    }

    
    /**
     * 2023-7-4
     * 在当前作用域内生成一个typeType类型的数组变量声明。
     * @param typeType
     * @param st
     * @param scopeNode
     * @return
     */
    public SymbolRecord generateArrayDeclarationByTypeInCurScope(String typeType, SymbolTable st, ScopeTree scopeNode, int index, Map<String, Integer> tempTokenAppearTimes, boolean ifNeedOutCycle, Map<String, String> typeExpression,int lineOrder) {
        String declaration=typeType;
        Type type=st.getTypeMap().get(typeType);
        String identifier="";
        String identifierValue="{";
        int arrayLength=(int)(Math.random()*CPlusConfigureInfo.arrayLength+1);
        Constraint constraint=new Constraint();
//        Constraint constraint=new Constraint();
        identifier=grammarGenerateService.getIdentifier(index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        identifierValue+=grammarGenerateService.generateProgramForMatchedValue(typeExpression.get("int"), index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        for(int i=1;i<arrayLength;i++) {
        	identifierValue+=","+grammarGenerateService.generateProgramForMatchedValue(typeExpression.get("int"), index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
//            identifierValue = constraint.checkUniqueConstraint(identifierValue, typeExpression.get(typeType), index, grammars);//唯一性约束（*不可重复相同值）
            identifierValue = constraint.checkContinueConstraint(identifierValue, typeExpression.get("int"), index, grammars);//连续性约束，如果在约束列表中，temResult中不能有空格
        }
        identifierValue+="}";
        
//        if(type==null)//System.out.println("what is this"+typeType);
        declaration += " " +identifier+ "["+arrayLength+"] = "+identifierValue;
        return new SymbolRecord(identifier,type,identifier,"array",scopeNode,identifierValue,arrayLength);
    }
    
    /**
     * 2023-7-4
     * 在当前作用域内生成一个typeType类型的变量声明。
     * @param typeType
     * @param st
     * @param scopeNode
     * @return
     */
    public SymbolRecord generatePointerDeclarationByTypeInCurScope(String typeType, SymbolTable st, ScopeTree scopeNode, int index, Map<String, Integer> tempTokenAppearTimes, boolean ifNeedOutCycle, Map<String, String> typeExpression,int lineOrder) {
        String declaration=typeType;
        String identifier="";
        String identifierValue="";
        Constraint constraint=new Constraint();
        identifier=grammarGenerateService.getIdentifier(index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        String varIdentifier="";
        varIdentifier=generateIdentifierByTypeC("int", index,tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode.getRoot(),"var","-",lineOrder);
//        }
        //        identifierValue=grammarGenerateService.generateProgramForMatchedValue(typeExpression.get(typeType), index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode);
//        identifierValue = constraint.checkUniqueConstraint(identifierValue, typeExpression.get(typeType), index, grammars);//唯一性约束（*不可重复相同值）
//        identifierValue = constraint.checkContinueConstraint(identifierValue, typeExpression.get(typeType), index, grammars);//连续性约束，如果在约束列表中，temResult中不能有空格
        Type type=st.getTypeMap().get(typeType);
//        if(type==null)//System.out.println("what is this"+typeType);
        declaration +=  identifier+ " = &"+varIdentifier;
        return new SymbolRecord(identifier,type,identifier,"pointer",scopeNode,varIdentifier);
    }


    /**
     * 2022-10-12
     * 在当前作用于内生成一个typeType类型的变量声明，方便当前作用域内使用
     * @param typeType
     * @param st
     * @param scopeNode
     * @return
     */
    private SymbolRecord generateDeclarationByTypeInCur(String typeType, SymbolTable st, ScopeTree scopeNode,int index,Map<String,Integer> tempTokenAppearTimes,boolean ifNeedOutCycle,int lineOrder) {
        SymbolRecord sr=generateDeclarationByTypeInCurScope(typeType, st, scopeNode, index,tempTokenAppearTimes, ifNeedOutCycle, CPlusConfigureInfo.cPlusBaseTypeLiteral,lineOrder);
        sr.setScope(scopeNode);//用当前作用域所在类的作用域
        return sr;
    }
    
    /**
     * 2023-7-6
     * 在当前作用于内生成一个指针类型的变量声明，方便当前作用域内使用
     * @param typeType
     * @param st
     * @param scopeNode
     * @return
     */
    private SymbolRecord generatePointerDeclarationByTypeInCur(String typeType, SymbolTable st, ScopeTree scopeNode,int index,Map<String,Integer> tempTokenAppearTimes,boolean ifNeedOutCycle,int lineOrder) {
        SymbolRecord sr=generatePointerDeclarationByTypeInCurScope(typeType, st, scopeNode, index,tempTokenAppearTimes, ifNeedOutCycle, CPlusConfigureInfo.cPlusBaseTypeLiteral,lineOrder);
        sr.setScope(scopeNode);//用当前作用域所在类的作用域
        return sr;
    }
    
    /**
     * 2023-7-6
     * 在当前作用于内生成一个array类型的变量声明，方便当前作用域内使用
     * @param typeType
     * @param st
     * @param scopeNode
     * @return
     */
    private SymbolRecord generateArrayDeclarationByTypeInCur(String typeType, SymbolTable st, ScopeTree scopeNode,int index,Map<String,Integer> tempTokenAppearTimes,boolean ifNeedOutCycle,int lineOrder) {
        SymbolRecord sr=generateArrayDeclarationByTypeInCurScope(typeType, st, scopeNode, index,tempTokenAppearTimes, ifNeedOutCycle, CPlusConfigureInfo.cPlusBaseTypeLiteral,lineOrder);
        sr.setScope(scopeNode);//用当前作用域所在类的作用域
        return sr;
    }

    /**
     * anonymousAuthor
     * 2022.10.31
     *输出main中声明的变量以及对象的成员变量的值
     * @param st
     * @param scopeNode
     * @return
     */
    public String generateOutPut( SymbolTable st, ScopeTree scopeNode) {
        String result="";
        //main中的基本变量的值输出到控制台
        Map<String,Type> typeMap=st.getTypeMap();//基本类型映射表
        Set<String> baseTypes=typeMap.keySet();//基本类型名称列表
        List<SymbolRecord> baseSymbolRecords=grammarGenerateService.getSymbolRecordByType(st,scopeNode,baseTypes);//main当中可用的基本类型的标识符
        for(SymbolRecord baseIden:baseSymbolRecords){
        	String categpory=baseIden.getCategory();
        	String typeName=baseIden.getType().getTypeName();
        	if(categpory!="pointer") {
        		result+=getOutPutVar(categpory,typeName,baseIden,st);
        	}
        }

        List<SymbolRecord> globalSymbols =grammarGenerateService.getSymbolRecordByScope(st,scopeNode.getRoot());//main当中可用的全局标识符
        for(SymbolRecord globalSymbol:globalSymbols){
            String typeName=globalSymbol.getType().getTypeName();
            String categpory=globalSymbol.getCategory();
            if(categpory!="pointer") {
                result+=getOutPutVar(categpory,typeName,globalSymbol,st);
            }
            
        }
        
        return result;
    }
    
    /**
     * anonymousAuthor
     * 2024.4.9
     *输出对象的成员变量的值
     * @param cusTomTypeMap 
     * @param st
     * @param scopeNode
     * @return
     */
    public String generateOutPutVarClass( Map<String, Type> cusTomTypeMap, SymbolTable st, ScopeTree scopeNode) {
        String result="";
      //main中自定义的变量输出其成员变量的值到控制台
        Set<String> cusTypes=cusTomTypeMap.keySet();//自定义类型集合
        List<SymbolRecord> cusSymbolRecords=grammarGenerateService.getSymbolRecordByType(st,scopeNode,cusTypes);//main当中可用的自定义类型的标识符
        for(SymbolRecord cusIden:cusSymbolRecords){
            Type type=cusIden.getType();//获取自定义的类型
            String iden=cusIden.getName();//获取自定义类型的变量标识符
            List<SymbolRecord> vars=type.getVars();//获取自定义类型中的成员变量列表
            for(SymbolRecord var:vars){
            	String categpory=var.getCategory();
            	String typeName=var.getType().getTypeName();
            	if(categpory!="pointer") {
            		result+=getOutPutVarClass(categpory,typeName,var,st,iden);
            	}
            }
        }
        return result;
    }


    /**
     * anonymousAuthor
     * 2023-7-11
     *  根据变量的类型组织打印语句
     * @param categpory 变量的类别（变量、数组、指针）
     * @param typeName 变量的类型（整型，浮点，布尔）
     * @param baseIden 标识符记录
     * @param st
     * @return
     */
    private String getOutPutVar(String categpory, String typeName,SymbolRecord baseIden,SymbolTable st) {
		// TODO Auto-generated method stub
    	String result="";
    	int printIndex=st.getPrintIndex()+1;//输出内容的行号（用于程序简化）
    	Map<Integer, SymbolRecord> printPositionOfSymbolRecord=st.getPrintPositionOfSymbolRecord();//输出行对应的标识符map
    	Map<Integer, String> printStrings=st.getPrintString();//输出行对应的打印语句map
    	String printString="";
    	switch(categpory){
	    	case "var":{
	            switch(typeName){
	                case "float":
	                case "double":printString="printf(\"%.3f\\n\", "+baseIden.getName()+");"; break;
	                case "short":
	                case "int":
	                case "bool":
	                case "long":
	                default :printString="cout << "+baseIden.getName()+"<< endl;";break;  
	            }
	            printPositionOfSymbolRecord.put(printIndex, baseIden);
	            printStrings.put(printIndex, printString);
	            break;
	    	}
	    	case "pointer":{
	    		switch(typeName){
		            case "float*":
		            case "double*":
		            case "short*":
		            case "int*":
		            case "bool*":
		            case "long*":
		            default :printString="cout <<  *"+baseIden.getName()+"<< endl;";break;
	    		}
	    		printPositionOfSymbolRecord.put(printIndex, baseIden);
	    		printStrings.put(printIndex, printString);
	    		break;
	    		
	    	}
	    	case "array":{
	    		switch(typeName){
		            case "float[]":
		            case "double[]":printString="for(int i=0;i<"+baseIden.getArrayLength()+";i++){printf(\"%.3f\\n\", "+baseIden.getName()+"[i]);}";break;
		            case "short[]":
		            case "int[]":
		            case "bool[]":
		            case "long[]":
		            default :printString="for(int i=0;i<"+baseIden.getArrayLength()+";i++){cout << "+baseIden.getName()+"[i]<< endl;}";break;
	    		}
	    		for(int i=0;i<baseIden.getArrayLength();i++) {
	    			printPositionOfSymbolRecord.put(printIndex, baseIden);
	    			printStrings.put(printIndex, printString);
	    			printIndex++;
	    		}
	    		break;
	    		
	    	}
		}
    	result+=printString;
    	st.setPrintIndex(printIndex);
    	st.setPrintPositionOfSymbolRecord(printPositionOfSymbolRecord);
    	st.setPrintString(printStrings);
	return result;
	}
    
    /**
     * anonymousAuthor
     * 2024-4-9
     *  根据变量的类型以及所属的类，组织打印语句
     * @param categpory 变量的类别（变量、数组、指针）
     * @param typeName 变量的类型（整型，浮点，布尔）
     * @param baseIden 标识符记录
     * @param st
     * @return
     */
    private String getOutPutVarClass(String categpory, String typeName,SymbolRecord baseIden,SymbolTable st,String objName) {
		// TODO Auto-generated method stub
    	String result="";
    	int printIndex=st.getPrintIndex()+1;//输出内容的行号（用于程序简化）
    	Map<Integer, SymbolRecord> printPositionOfSymbolRecord=st.getPrintPositionOfSymbolRecord();//输出行对应的标识符map
    	Map<Integer, String> printStrings=st.getPrintString();//输出行对应的打印语句map
    	String printString="";
    	switch(categpory){
	    	case "var":{
	            switch(typeName){
	                case "float":
	                case "double":printString="printf(\"%.3f\\n\", "+objName+"."+baseIden.getName()+");"; break;
	                case "short":
	                case "int":
	                case "bool":
	                case "long":
	                default :printString="cout << "+objName+"."+baseIden.getName()+"<< endl;";break;  
	            }
	            printPositionOfSymbolRecord.put(printIndex, baseIden);
	            printStrings.put(printIndex, printString);
	            break;
	    	}
	    	case "pointer":{
	    		switch(typeName){
		            case "float*":
		            case "double*":
		            case "short*":
		            case "int*":
		            case "bool*":
		            case "long*":
		            default :printString="cout <<  *"+objName+"."+baseIden.getName()+"<< endl;";break;
	    		}
	    		printPositionOfSymbolRecord.put(printIndex, baseIden);
	    		printStrings.put(printIndex, printString);
	    		break;
	    		
	    	}
	    	case "array":{
	    		switch(typeName){
		            case "float[]":
		            case "double[]":printString="for(int i=0;i<"+objName+"."+baseIden.getArrayLength()+";i++){printf(\"%.3f\\n\", "+objName+"."+baseIden.getName()+"[i]);}";break;
		            case "short[]":
		            case "int[]":
		            case "bool[]":
		            case "long[]":
		            default :printString="for(int i=0;i<"+baseIden.getArrayLength()+";i++){cout << "+objName+"."+baseIden.getName()+"[i]<< endl;}";break;
	    		}
	    		for(int i=0;i<baseIden.getArrayLength();i++) {
	    			printPositionOfSymbolRecord.put(printIndex, baseIden);
	    			printStrings.put(printIndex, printString);
	    			printIndex++;
	    		}
	    		break;
	    		
	    	}
		}
    	result+=printString;
    	st.setPrintIndex(printIndex);
    	st.setPrintPositionOfSymbolRecord(printPositionOfSymbolRecord);
    	st.setPrintString(printStrings);
	return result;
	}

	/**
     * 2022-10-30
     * 在生成程序中，一些标识符未声明，记录在临时列表里，在程序生成结束后，一次性的添加到程序的最开始
     * @param addedDeclaration
     * @param program
     * @return
     */
    private String insertAddDeclarationToMain(List<String> addedDeclaration, String program) {
        if(addedDeclaration==null||addedDeclaration.size()==0){
            return program;
        }
        String result;
        String declarationString="";
        for(String ad:addedDeclaration){
            declarationString+=ad;
        }
        result=declarationString+program;
        return result;
    }

    /**
     * 向循环中插入break语句，防止死循环
     * anonymousAuthor
     * 2022.11.4
     * @param loopSegment
     * @param identifierCount
     * @return
     */
    public String insertBreakToLoop(String loopSegment, String identifierCount) {
        String result;
        String breakString="";
        breakString+=identifierCount+"="+identifierCount+"+1;";
        breakString+="if("+identifierCount+">"+ ConfigureInfo.getLoopCountMax()+"){";
        breakString+= CPlusConfigureInfo.loopBreak+";}";
        String head=loopSegment.substring(0,loopSegment.indexOf("{")+1);
        String tail=loopSegment.substring(loopSegment.indexOf("{")+1);
        result=head+breakString+tail;
        return result;
    }

    /**
     * anonymousAuthor
     * 2024.3.11
     * 在作用域对应的程序内容的结尾插入一段输出语句
     * @param str
     * @param outPutStr
     * @return
     */
	public String insertStr2ScopeEnd(String str, String outPutStr) {
		// TODO Auto-generated method stub
		String result="";
		if(str.endsWith("}")) {
			String head="";
			String tail="";
			String[] lines=str.split(";");
			int index=0;
			for(int i=lines.length-1;i>=0;i--) {
				if(lines[i].trim().equals("")) {
					continue;
				}else {
					if(lines[i].trim().equals("}")) {
						index++;
					}
					else if(lines[i].trim().startsWith("return")) {
						index++;
						if(index==2) {
							head=str.substring(0,str.lastIndexOf("return"));
					        tail=str.substring(str.lastIndexOf("return"));
					        break;
						}
					}
				}
			}
			if(index!=2) {
				head=str.substring(0,str.lastIndexOf("}"));
		        tail=str.substring(str.lastIndexOf("}"));
			}
			result=head+outPutStr+tail;
		}else {//该作用域不是以}结尾的
			return str;
		}
		return result;
	}
}
