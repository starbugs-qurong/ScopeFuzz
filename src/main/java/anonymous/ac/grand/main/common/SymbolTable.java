package anonymous.ac.grand.main.common;

import anonymous.ac.grand.main.c.CConfigureInfo;
import anonymous.ac.grand.main.javapkg.JavaConfigureInfo;
import anonymous.ac.grand.main.python.PythonConfigureInfo;
import anonymous.ac.grand.mutate.cplus14.CPlusConfigureInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成的程序的符号表
 * anonymousAuthor
 * 2022.9.25
 */
public class SymbolTable implements Serializable {
    private String parogramId="";//生成程序的ID，文法名+索引号，例如Java5，表示为java文法生成的第5个程序
    private  String programName="";//生成程序对应的java文件名称，这个名称和public类名对应
    private List<SymbolRecord>  objSymbolRecords=new ArrayList<>();//对象名
    private List<SymbolRecord>  constantSymbolRecords=new ArrayList<>();//常量名
    private List<SymbolRecord>  varSymbolRecords=new ArrayList<>();//变量名
    private List<FunctionSymbolRecord>  functionSymbolRecords=new ArrayList<>();//方法的符号表
    private List<LamdaSymbolRecord>  lamdaSymbolRecords=new ArrayList<>();//Lamda表达式的符号表
    private List<String> identifierNameList=new ArrayList<>();//所有的标识符的名称
    private List<SymbolRecord>  symbolRecords=new ArrayList<>();//符号总表
    private Map<String, Type> typeMap=new HashMap<>();//类型映射表，用类名来对应每一个类
    private Map<String, Type> customizedTypeMap=new HashMap<>();//自定义类型映射表，用类名来对应每一个类

    private ScopeTree rootScope;//一个符号表对应一个作用域表，作用域的根节点记录在符号表中
    private int scopeNum=0;//作用域的数量
    private Map<String,List<String>> addedDeclaration=new HashMap<>();//补充的声明列表,键值对，类名——未声明变量列表
    private Map<String,List<String>> addedScopeDeclaration=new HashMap<>();//补充的作用域声明列表,键值对，类名——作用域中未声明的变量函数等的声明 用于变异
    private Map<String,List<String>> addedIncludeDeclaration=new HashMap<>();//补充的头文件,键值对，类名——未声明的头文件等 用于变异

    private List<String> mainAddedDeclaration=new ArrayList();//main函数中的补充的声明列表,键值对，类名——未声明变量列表
    private ScopeTree mainScope;//main方法对应的作用域，简化程序使用
    
    private Map<Integer,SymbolRecord> printPositionOfSymbolRecord=new HashMap<>();//简化程序使用，标识符的打印次序，对于数组，存在多对一的情况
    private Map<Integer,String> printString=new HashMap<>();//简化程序使用，标识符打印的次序以及打印语句，对于数组，存在多对一的情况
    private String printFragment="";//简化程序使用，main中所有的打印语句
    
    private Map<Integer,ScopeTree> scopeIdNodeMap=new HashMap<>();//简化程序使用，作用域ID和作用域节点的对应关系map
    
    private Map<Integer,String> lineOrderStatement=new HashMap<>();//简化程序使用，行号及语句
    
    private List<ScopeTree> leafNodes=new ArrayList<>();//作用域树的叶子节点
    
    private int printIndex=0;//标识符的打印总行数

    public String getMainClassIdentifier() {
        return MainClassIdentifier;
    }

    public String MainClassIdentifier="";//用在python当中，主方法所在的类进行标记

    public String getParogramId() {
        return parogramId;
    }

    public void setParogramId(String parogramId) {
        this.parogramId = parogramId;
    }

    public List<SymbolRecord> getObjSymbolRecords() {
        return objSymbolRecords;
    }

    public void setObjSymbolRecords(List<SymbolRecord> objSymbolRecords) {
        this.objSymbolRecords = objSymbolRecords;
    }

    public List<SymbolRecord> getConstantSymbolRecords() {
        return constantSymbolRecords;
    }

    public void setConstantSymbolRecords(List<SymbolRecord> constantSymbolRecords) {
        this.constantSymbolRecords = constantSymbolRecords;
    }

    public List<SymbolRecord> getVarSymbolRecords() {
        return varSymbolRecords;
    }

    public void setVarSymbolRecords(List<SymbolRecord> varSymbolRecords) {
        this.varSymbolRecords = varSymbolRecords;
    }

    public List<FunctionSymbolRecord> getFunctionSymbolRecords() {
        return functionSymbolRecords;
    }

    public void setFunctionSymbolRecords(List<FunctionSymbolRecord> functionSymbolRecords) {
        this.functionSymbolRecords = functionSymbolRecords;
    }

    public List<LamdaSymbolRecord> getLamdaSymbolRecords() {
		return lamdaSymbolRecords;
	}

	public void setLamdaSymbolRecords(List<LamdaSymbolRecord> lamdaSymbolRecords) {
		this.lamdaSymbolRecords = lamdaSymbolRecords;
	}

	public List<String> getIdentifierNameList() {
        return identifierNameList;
    }

    public void setIdentifierNameList(List<String> identifierNameList) {
        this.identifierNameList = identifierNameList;
    }

    public List<SymbolRecord> getSymbolRecords() {
        return symbolRecords;
    }

    public void setSymbolRecords(List<SymbolRecord> symbolRecords) {
        this.symbolRecords = symbolRecords;
    }

    public ScopeTree getRootScope() {
        return rootScope;
    }

    public void setRootScope(ScopeTree rootScope) {
        this.rootScope = rootScope;
    }

    public int getScopeNum() {
        return scopeNum;
    }

    public void setScopeNum(int scopeNum) {
        this.scopeNum = scopeNum;
    }

    public Map<String,List<String>> getAddedDeclaration() {
        return addedDeclaration;
    }

    public void setAddedDeclaration(Map<String,List<String>> addedDeclaration) {
        this.addedDeclaration = addedDeclaration;
    }

    public Map<String, Type> getTypeMap() {
        return typeMap;
    }

    public void setTypeMap(Map<String, Type> typeMap) {
        this.typeMap = typeMap;
    }

    public Map<String, Type> getCustomizedTypeMap() {
        return customizedTypeMap;
    }

    public void setCustomizedTypeMap(Map<String, Type> customizedTypeMap) {
        this.customizedTypeMap = customizedTypeMap;
    }

    public void initailTypeMap(List<String> baseTypes) {
        if(baseTypes==null||baseTypes.size()==0){
            return;
        }
        if(ConfigureInfo.ifJavaCode) {
            for (int i = 0; i < baseTypes.size(); i++) {
                Type tempType = new Type(i, baseTypes.get(i), JavaConfigureInfo.javaBaseTypeLiteral.get(baseTypes.get(i)), JavaConfigureInfo.javaBaseTypeExp.get(baseTypes.get(i)));//类型
                this.typeMap.put(baseTypes.get(i), tempType);
            }
        }else if(ConfigureInfo.ifCCode) {
            for (int i = 0; i < baseTypes.size(); i++) {
                Type tempType = new Type(i, baseTypes.get(i), CConfigureInfo.cBaseTypeLiteral.get(baseTypes.get(i)), CConfigureInfo.cBaseTypeExp.get(baseTypes.get(i)));//类型
                this.typeMap.put(baseTypes.get(i), tempType);
            }
            
        }else if(ConfigureInfo.ifPythonCode) {
            for (int i = 0; i < baseTypes.size(); i++) {
                Type tempType = new Type(i, baseTypes.get(i), PythonConfigureInfo.pythonBaseTypeLiteral.get(baseTypes.get(i)), PythonConfigureInfo.pythonBaseTypeExp.get(baseTypes.get(i)));//类型
                this.typeMap.put(baseTypes.get(i), tempType);
            }
        }else if(ConfigureInfo.ifCPlusCode) {
            for (int i = 0; i < baseTypes.size(); i++) {
                Type tempType = new Type(i, baseTypes.get(i), CPlusConfigureInfo.cPlusBaseTypeLiteral.get(baseTypes.get(i)), CPlusConfigureInfo.cPlusBaseTypeExp.get(baseTypes.get(i)));//类型
                this.typeMap.put(baseTypes.get(i), tempType);
            }
            
        }
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public void setMainClassIdentifier(String identifier) {
    }

	public List<String> getMainAddedDeclaration() {
		return mainAddedDeclaration;
	}

	public void setMainAddedDeclaration(List<String> mainAddedDeclaration) {
		this.mainAddedDeclaration = mainAddedDeclaration;
	}

	public Map<Integer, SymbolRecord> getPrintPositionOfSymbolRecord() {
		return printPositionOfSymbolRecord;
	}

	public void setPrintPositionOfSymbolRecord(Map<Integer, SymbolRecord> printPositionOfSymbolRecord) {
		this.printPositionOfSymbolRecord = printPositionOfSymbolRecord;
	}

	public int getPrintIndex() {
		return printIndex;
	}

	public void setPrintIndex(int printIndex) {
		this.printIndex = printIndex;
	}

	public Map<Integer, ScopeTree> getScopeIdNodeMap() {
		return scopeIdNodeMap;
	}
    
	public void setScopeIdNodeMap(Map<Integer, ScopeTree> scopeIdNodeMap) {
		this.scopeIdNodeMap = scopeIdNodeMap;
	}

	public Map<Integer, String> getLineOrderStatement() {
		return lineOrderStatement;
	}

	public void setLineOrderStatement(Map<Integer, String> lineOrderStatement) {
		this.lineOrderStatement = lineOrderStatement;
	}

	public ScopeTree getMainScope() {
		return mainScope;
	}

	public void setMainScope(ScopeTree mainScope) {
		this.mainScope = mainScope;
	}

	public Map<Integer, String> getPrintString() {
		return printString;
	}

	public void setPrintString(Map<Integer, String> printString) {
		this.printString = printString;
	}

	public String getPrintFragment() {
		return printFragment;
	}

	public void setPrintFragment(String printFragment) {
		this.printFragment = printFragment;
	}

	public List<ScopeTree> getLeafNodes() {
		return leafNodes;
	}

	public void setLeafNodes(List<ScopeTree> leafNodes) {
		this.leafNodes = leafNodes;
	}
	
	/**
     * 2023.8.17
     * 从一个程序片段中取出所有的标识符名称
     * @param str
     * @param st
     * @return
     */
	public List<SymbolRecord> getIdentifiersFromStr(String str,SymbolTable st) {
//		List<String> names=new ArrayList<>();   
		List<SymbolRecord> srs=st.getSymbolRecords();
		if(srs==null||srs.size()==0) {
			return null;
		}
		List<SymbolRecord> reuslt=new ArrayList<>();
		for(SymbolRecord sr:srs) {
			if(str.contains(sr.getID())) {
				reuslt.add(sr);
			}
//			if(names.contains(sr.getName())) {
//				//System.out.println(names.size()+": "+str);
//			}else {
//				names.add(sr.getName());
//			}
		}
		
		return reuslt;
	}
	
    /**
     * 2023.8.17
     * 从一个程序片段中取出所有的函数标识符名称
     * @param str
     * @return
     */
	public List<FunctionSymbolRecord> getFunsFromStr(String str) {
		   
		List<FunctionSymbolRecord> srs=this.getFunctionSymbolRecords();
		if(srs==null||srs.size()==0) {
			return null;
		}
		List<FunctionSymbolRecord> reuslt=new ArrayList<>();
		for(FunctionSymbolRecord sr:srs) {
			if(str.contains(sr.getID())) {
				reuslt.add(sr);
			}
		}
		return reuslt;
	}
    
	
	/**
     * 2023.9.22
     * 根据标识符的ID取出对应的标识符号
     * @param str
     * @return
     */
	public SymbolRecord getIdentifiersByID(String str) {
//		List<String> names=new ArrayList<>();   
		List<SymbolRecord> srs=this.getSymbolRecords();
		if(srs==null||srs.size()==0) {
			return null;
		}
		SymbolRecord reuslt=new SymbolRecord();
		for(SymbolRecord sr:srs) {
			if(str.contains(sr.getID())) {
				return sr;
			}
		}
		
		return reuslt;
	}

    /**
     * anonymousAuthor
     * 2024-12-25
     * @return
     */
    public Map<String, List<String>> getAddedScopeDeclaration() {
        return addedScopeDeclaration;
    }

    public void setAddedScopeDeclaration(Map<String, List<String>> addedScopeDeclaration) {
        this.addedScopeDeclaration = addedScopeDeclaration;
    }

    public Map<String, List<String>> getAddedIncludeDeclaration() {
        return addedIncludeDeclaration;
    }

    public void setAddedIncludeDeclaration(Map<String, List<String>> addedIncludeDeclaration) {
        this.addedIncludeDeclaration = addedIncludeDeclaration;
    }
}
