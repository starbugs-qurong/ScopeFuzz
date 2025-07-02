package iscas.ac.grand.main.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Type  implements Serializable{
    private int typeId;//类型ID，void的id为0
    private String fileName="";//类所在java文件名称
    private String typeName="";//类型名称 例如int String boolean 等基本类型，或者Student Product等等自定义类型
    private Type parentType;//父类  如果没有写null
    private Type outerType;//外部类  如果没有写null
    private List<Type> innerTypes;//内部类
    private List<Type> childrenTypes;//子类
    private String directLiteral;//文法中该类型的直接取值生成方式
    private String grammarExpression;//文法中该类型的表达式
    private ScopeTree scopeNode;//生成该类时所在的作用域
    private List<FunctionSymbolRecord> functions=new ArrayList<>();//类中的函数列表
    private List<SymbolRecord> vars=new ArrayList<>();//类中的变量列表
    private String modifier="";//类型的修饰符
    private String typeCategory="";//类型的类别，例如class还是struct



    public Type(int  typeId, String typeName,String directLiteral,String grammarExpression) {
        this.typeId=typeId;
        this.typeName=typeName;
        this.directLiteral=directLiteral;
        this.grammarExpression=grammarExpression;
    }

    public Type() {

    }
    public Type(String typeName) {
        this.typeName=typeName;
    }

    public Type(int  typeId, String typeName) {
        this.typeId=typeId;
        this.typeName=typeName;
    }


    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Type getParentType() {
        return parentType;
    }

    public void setParentType(Type parentType) {
        this.parentType = parentType;
    }

    public List<Type> getChildrenTypes() {
        return childrenTypes;
    }

    public void setChildrenTypes(List<Type> childrenTypes) {
        this.childrenTypes = childrenTypes;
    }

    public String getGrammarExpression() {
        return grammarExpression;
    }

    public void setGrammarExpression(String grammarExpression) {
        this.grammarExpression = grammarExpression;
    }

    public String getDirectLiteral() {
        return directLiteral;
    }

    public Type getOuterType() {
        return outerType;
    }

    public void setOuterType(Type outerType) {
        this.outerType = outerType;
    }

    public List<Type> getInnerTypes() {
        return innerTypes;
    }

    public void setInnerTypes(List<Type> innerTypes) {
        this.innerTypes = innerTypes;
    }

    public void setDirectLiteral(String directLiteral) {
        this.directLiteral = directLiteral;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ScopeTree getScopeNode() {
        return scopeNode;
    }

    public void setScopeNode(ScopeTree scopeNode) {
        this.scopeNode = scopeNode;
    }

    public List<FunctionSymbolRecord> getFunctions() {
        return functions;
    }

    public void setFunctions(List<FunctionSymbolRecord> functions) {
        this.functions = functions;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public List<SymbolRecord> getVars() {
        return vars;
    }

    public void setVars(List<SymbolRecord> vars) {
        this.vars = vars;
    }

	public String getTypeCategory() {
		return typeCategory;
	}

	public void setTypeCategory(String typeCategory) {
		this.typeCategory = typeCategory;
	}
    
}
