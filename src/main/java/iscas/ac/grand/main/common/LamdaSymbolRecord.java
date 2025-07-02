package iscas.ac.grand.main.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * qurong
 * 2024.2.2
 * 生成的程序中的lamda表达式符号表
 */
public class LamdaSymbolRecord implements Serializable {
    private String name="";//lamda表达式，即匿名函数别名
    private Type returnType;//函数返回值类型,void,int,String等
    private String ID="";//标识符唯一ID，可能是没有的，同name
    private String category="";//标识符种类，变量var，常量，函数名function，类名，匿名函数的别名等等
    private ScopeTree scope;//标识符作用域，某个方法内，类内，块内
    private Type locateType;//表达式所在类,比如自定义类等
    private List<Type> parameterList=new ArrayList<>();//lamda表达式中，匿名函数的参数列表，标识符映射标识符的类型
    private String modifier="public";//
    
    //和程序简化相关
    private List<SymbolRecord> returnIdentifiers=new ArrayList<>();//当前函数返回值中包含的标识符
    private List<SymbolRecord> parameterIdentifiers=new ArrayList<>();//当前函数参数中的标识符
    private List<SymbolRecord> actrualParameterIdentifiers=new ArrayList<>();//当前函数实参中的标识符
    private List<SymbolRecord> actrualReturnIdentifiers=new ArrayList<>();//当前函数返回值赋值的标识符（多次调用会对应多个）
    

    public LamdaSymbolRecord(String name, Type returnType, String ID, String category, ScopeTree scope, Type locateType, List<Type> parameterList, List<SymbolRecord> parameterIdentifiers) {
        this.name = name;
        this.returnType = returnType;
        this.ID = ID;
        this.category = category;
        this.scope = scope;
        this.locateType = locateType;
        this.parameterList = parameterList;
        this.parameterIdentifiers = parameterIdentifiers;
    }

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
    
    
}
