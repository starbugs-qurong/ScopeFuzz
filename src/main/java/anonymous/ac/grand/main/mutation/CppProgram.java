package anonymous.ac.grand.main.mutation;
/**
 * anonymousAuthor
 * 2024-12-13
 * 对C++程序进行切片 每个程序保存为一个CppProgram
 */

import anonymous.ac.grand.main.antlr4.CPP14Parser;
import anonymous.ac.grand.main.common.ScopeTree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CppProgram implements Serializable {
	private String ID="";
	private String programStr="";//程序的全部内容
	private String cppFileName="";//程序名称 如果发现问题 可以找到是来源于哪些程序的作用域片段合并时引起的
	private String cppFilePath="";//源程序路径
	private List<String> programLines=new ArrayList<>();//程序的全部内容 按行存储
	private List<MainFunctionForSlice> mainFunctionList=new ArrayList<>();//程序中的主函数 如果超出一个 记为无效程序
	private List<String> AssignmentList=new ArrayList<>();//赋值语句
	private List<FunctionForSlice> functionList=new ArrayList<>();//函数
//	private List<ScopeBlockForSlice> functionScopeList=new ArrayList<>();//函数对应的作用域列表
	private List<ScopeBlockForSlice> templateList=new ArrayList<>();//模版
	private List<ScopeBlockForSlice> structList=new ArrayList<>();//结构体
	private List<ScopeBlockForSlice> lambdaList=new ArrayList<>();//lambda表达式
	private List<ScopeBlockForSlice> whileList=new ArrayList<>();//while作用域
	private List<ScopeBlockForSlice> forList=new ArrayList<>();//for作用域
	private List<ScopeBlockForSlice> ifList=new ArrayList<>();//if作用域
	private List<String> includeList=null;//include
	private ScopeBlockForSlice programScope=new ScopeBlockForSlice();//该程序对应的作用域
	private List<ScopeBlockForSlice> allScopeBlocks=new ArrayList<>();
	private List<String> mainOut=null;//main函数外部的程序内容
	private List<String> mainIn=new ArrayList<>();//main函数内部的程序内容

	private List<CPP14Parser.FunctionDefinitionContext> functionContextList=new ArrayList<>();//用于基于AST的变异，记录每个源程序中的函数上下文
	private List<CPP14Parser.ClassSpecifierContext> classContextList=new ArrayList<>();//用于基于AST的变异，记录每个源程序中的类上下文
	private List<CPP14Parser.TypeSpecifierContext> typeContextList=new ArrayList<>();//用于基于AST的变异，记录每个源程序中的type上下文
	private List<CPP14Parser.TrailingTypeSpecifierContext> trailTypeContextList=new ArrayList<>();//用于基于AST的变异，记录每个源程序中的type上下文
	private List<CPP14Parser.EnumSpecifierContext> enumContextList=new ArrayList<>();//用于基于AST的变异，记录每个源程序中的enum上下文


	public CppProgram(ScopeTree rootScope) {
		FunctionForSlice ffs= new FunctionForSlice();
		int id=ffs.generateUniqueID();
		this.programScope.setScopeID(id);
		this.programScope.setFather(rootScope);
	}

	public String getCppFilePath() {
		return cppFilePath;
	}

	public void setCppFilePath(String cppFilePath) {
		this.cppFilePath = cppFilePath;
	}

	public String getCppFileName() {
		return cppFileName;
	}

	public void setCppFileName(String cppFileName) {
		this.cppFileName = cppFileName;
	}

	public List<ScopeBlockForSlice> getAllScopeBlocks() {
		return allScopeBlocks;
	}

	public void setAllScopeBlocks(List<ScopeBlockForSlice> allScopeBlocks) {
		this.allScopeBlocks = allScopeBlocks;
	}

	public ScopeBlockForSlice getProgramScope() {
		return programScope;
	}

	public void setProgramScope(ScopeBlockForSlice programScope) {
		this.programScope = programScope;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public List<String> getAssignmentList() {
		return AssignmentList;
	}

	public void setAssignmentList(List<String> assignmentList) {
		AssignmentList = assignmentList;
	}

	public List<ScopeBlockForSlice> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(List<ScopeBlockForSlice> templateList) {
		this.templateList = templateList;
	}

	public List<ScopeBlockForSlice> getStructList() {
		return structList;
	}

	public void setStructList(List<ScopeBlockForSlice> structList) {
		this.structList = structList;
	}

	public List<ScopeBlockForSlice> getLambdaList() {
		return lambdaList;
	}

	public void setLambdaList(List<ScopeBlockForSlice> lambdaList) {
		this.lambdaList = lambdaList;
	}

	public List<ScopeBlockForSlice> getWhileList() {
		return whileList;
	}

	public void setWhileList(List<ScopeBlockForSlice> whileList) {
		this.whileList = whileList;
	}

	public List<ScopeBlockForSlice> getForList() {
		return forList;
	}

	public void setForList(List<ScopeBlockForSlice> forList) {
		this.forList = forList;
	}

	public List<ScopeBlockForSlice> getIfList() {
		return ifList;
	}

	public void setIfList(List<ScopeBlockForSlice> ifList) {
		this.ifList = ifList;
	}

	public List<String> getIncludeList() {
		return includeList;
	}

	public void setIncludeList(List<String> includeList) {
		this.includeList = includeList;
	}

	public String getProgramStr() {
		return programStr;
	}

	public void setProgramStr(StringBuilder programStr) {
		this.programStr = programStr.toString();
	}

	public void setProgramStr(String programStr) {
		this.programStr = programStr;
	}

	public List<MainFunctionForSlice> getMainFunctionList() {
		return mainFunctionList;
	}

	public void setMainFunctionList(List<MainFunctionForSlice> mainFunctionList) {
		this.mainFunctionList = mainFunctionList;
	}

	public List<String> getProgramLines() {
		return programLines;
	}

	public void setProgramLines(List<String> programLines) {
		this.programLines = programLines;
	}

	public List<FunctionForSlice> getFunctionList() {
		return functionList;
	}

	public void setFunctionList(List<FunctionForSlice> functionList) {
		this.functionList = functionList;
	}

	public List<String> getMainOut() {
		return mainOut;
	}

	public List<CPP14Parser.FunctionDefinitionContext> getFunctionContextList() {
		return functionContextList;
	}

	public void setFunctionContextList(List<CPP14Parser.FunctionDefinitionContext> functionContextList) {
		this.functionContextList = functionContextList;
	}

	public List<CPP14Parser.ClassSpecifierContext> getClassContextList() {
		return classContextList;
	}

	public void setClassContextList(List<CPP14Parser.ClassSpecifierContext> classContextList) {
		this.classContextList = classContextList;
	}

	public List<CPP14Parser.TypeSpecifierContext> getTypeContextList() {
		return typeContextList;
	}

	public void setTypeContextList(List<CPP14Parser.TypeSpecifierContext> typeContextList) {
		this.typeContextList = typeContextList;
	}

	public List<CPP14Parser.TrailingTypeSpecifierContext> getTrailTypeContextList() {
		return trailTypeContextList;
	}

	public void setTrailTypeContextList(List<CPP14Parser.TrailingTypeSpecifierContext> trailTypeContextList) {
		this.trailTypeContextList = trailTypeContextList;
	}

	public List<CPP14Parser.EnumSpecifierContext> getEnumContextList() {
		return enumContextList;
	}

	public void setEnumContextList(List<CPP14Parser.EnumSpecifierContext> enumContextList) {
		this.enumContextList = enumContextList;
	}

	/**
	 * anonymousAuthor
	 * 2024-12-25
	 * 把main函数之外的程序分为两个部分 一部分包含头文件宏定义等 一部分是其它声明
	 * @param mainOut
	 */
	public void setMainOut(List<String> mainOut) {
		if(mainOut==null)return;
		List<String> includes=new ArrayList<>();
		List<String> mainOutList=new ArrayList<>();
		int i=0;
		for(i=0;i<mainOut.size();i++){
			String str=mainOut.get(i);
			str=str.trim();
			if(str.startsWith("#")){
				includes.add(str);
			}else{
				break;
			}
		}
		for(int j=i;j<mainOut.size();j++){
			String str=mainOut.get(j);
			str=str.trim();
			if(str.startsWith("#")){
				System.out.println("why  # start in this line: "+str);
			}
			mainOutList.add(str);
		}
		if(includes!=null&&includes.size()>0) {
			this.setIncludeList(includes);
		}
		if(mainOutList!=null&&mainOutList.size()>0){
			this.mainOut = mainOutList;
		}
	}

	public List<String> getMainIn() {
		return mainIn;
	}

	public void setMainIn(List<String> mainIn) {
		this.mainIn = mainIn;
	}
}
