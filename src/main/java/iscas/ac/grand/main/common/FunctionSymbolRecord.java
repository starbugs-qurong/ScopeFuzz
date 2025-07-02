package iscas.ac.grand.main.common;

import iscas.ac.grand.main.antlr4.CPP14Parser;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * qurong
 * 2022.10.25
 * 生成的程序中的方法（函数）符号表
 */
public class FunctionSymbolRecord implements Serializable {
    public String name="";//函数名称标识符
    public Type returnType;//函数返回值类型,void,int,String等
    public String ID="";//标识符唯一ID，为了区别可能同名，但作用域不同的标识符
    public String category="";//标识符种类，变量var，常量，函数名function，类名等等
    public ScopeTree scope;//标识符作用域，某个方法内，类内，块内
    public Type locateType;//函数所在类,比如自定义类等
    public List<Type> parameterList=new ArrayList<>();//函数的参数列表，标识符映射标识符的类型
    public String modifier="public";//
    public boolean isTemplateFun=false;//函数是否为模板函数
    public CPP14Parser.FunctionDefinitionContext funContext=null;//用于基于AST的变异插入函数 插入的函数的上下文

    
    //和程序简化相关
    private List<SymbolRecord> returnIdentifiers=new ArrayList<>();//当前函数返回值中包含的标识符
    
    //和程序简化相关
    public List<SymbolRecord> parameterIdentifiers=new ArrayList<>();//当前函数参数中的标识符
    
    //和程序简化相关
    private List<SymbolRecord> actrualParameterIdentifiers=new ArrayList<>();//当前函数实参中的标识符
    
    //和程序简化相关
    private List<SymbolRecord> actrualReturnIdentifiers=new ArrayList<>();//当前函数返回值赋值的标识符（多次调用会对应多个）

    public FunctionSymbolRecord(){

    }

    public FunctionSymbolRecord(String name, Type returnType, String ID, String category, ScopeTree scope, Type locateType, List<Type> parameterList, List<SymbolRecord> parameterIdentifiers) {
        this.name = name;
        this.returnType = returnType;
        this.ID = ID;
        this.category = category;
        this.scope = scope;
        this.locateType = locateType;
        this.parameterList = parameterList;
        this.parameterIdentifiers = parameterIdentifiers;
    }




//    public FunctionSymbolRecord(String functionBlock, String typeName) {
//        Type returnType=new Type(1,typeName);
//        this.ID=generateUniqueId();
//        this.name=getNameByBlock(functionBlock,typeName);
//        this.parameterList = getParameterListByBlock(functionBlock,typeName);
//    }
//
//    private List<Type> getParameterListByBlock(String functionBlock, String typeName) {
//    }
//
//    private String getNameByBlock(String functionBlock, String typeName) {
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ScopeTree getScope() {
        return scope;
    }

    public void setScope(ScopeTree scope) {
        this.scope = scope;
    }

    public Type getLocateType() {
        return locateType;
    }

    public void setLocateType(Type locateType) {
        this.locateType = locateType;
    }

    public List<Type> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<Type> parameterList) {
        this.parameterList = parameterList;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

	public List<SymbolRecord> getReturnIdentifiers() {
		return returnIdentifiers;
	}

	public void setReturnIdentifiers(List<SymbolRecord> returnIdentifiers) {
		this.returnIdentifiers = returnIdentifiers;
	}

	public List<SymbolRecord> getParameterIdentifiers() {
		return parameterIdentifiers;
	}

	public void setParameterIdentifiers(List<SymbolRecord> parameterIdentifiers) {
		this.parameterIdentifiers = parameterIdentifiers;
	}

	public List<SymbolRecord> getActrualParameterIdentifiers() {
		return actrualParameterIdentifiers;
	}

	public void setActrualParameterIdentifiers(List<SymbolRecord> actrualParameterIdentifiers) {
		this.actrualParameterIdentifiers = actrualParameterIdentifiers;
	}

	public List<SymbolRecord> getActrualReturnIdentifiers() {
		return actrualReturnIdentifiers;
	}

	public void setActrualReturnIdentifiers(List<SymbolRecord> actrualReturnIdentifiers) {
		this.actrualReturnIdentifiers = actrualReturnIdentifiers;
	}

	public boolean isTemplateFun() {
		return isTemplateFun;
	}

	public void setTemplateFun(boolean isTemplateFun) {
		this.isTemplateFun = isTemplateFun;
	}

    public CPP14Parser.FunctionDefinitionContext getFunContext() {
        return funContext;
    }

    public void setFunContext(CPP14Parser.FunctionDefinitionContext funContext) {
        this.funContext = funContext;
    }
}
