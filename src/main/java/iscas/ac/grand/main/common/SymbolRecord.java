package iscas.ac.grand.main.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 *符号表中的一条记录
 * qurong
 * 2022.9.27
 */
public class SymbolRecord implements Serializable {
    private String name="";//标识符名称
    private Type type;//标识符类型,int String等
    private String ID="";//标识符唯一ID，为了区别可能同名，但作用域不同的标识符
    private String category="";//标识符种类，变量var，常量，函数名function，数组名array，指针pointer，类名等等
    private ScopeTree scope;//标识符作用域，某个方法内，类内，块内
    private String value="";//标识符取值，初始化时创建符号记录，后续赋值时更新符号记录
    private String modifier="";//标识符的修饰符
    private int arrayLength=1;//数组标识符的长度
    
//    private SymbolScopeRelation ssr=new SymbolScopeRelation();
    
    //和标识符选择策略相关
    private Map<Integer,Integer> scopeUsedTimeMap=new HashMap<>();//当前标识符在各个可用作用域内的使用次数，<scopeId,次数>
    
    //和程序简化相关
    private Map<Integer,List<SymbolRecord>> scopeModifiedIdentifiers=new HashMap<>();//当前标识符的取值在各个可用作用域内的和哪些标识符相关，<scopeId,相关的标识符列表>
    
    //和程序简化相关
    private Map<Integer,List<FunctionSymbolRecord>> scopeModifiedFuns=new HashMap<>();//当前标识符的取值在各个可用作用域内的和哪些函数标识符相关，<scopeId,相关的函数标识符列表>
    
    //和程序简化相关
    private Map<Integer,Map<Integer,String>> scopeContent=new HashMap<>();//当前标识符的取值在各个可用作用域内的和哪些程序片段（含作用域头尾）相关，<scopeId,程序片段>
    
    //和程序简化相关
    private List<Integer> relatedScopes=new ArrayList<>();//当前标识符的取值在哪些作用域内定义或改变过
    
    //和程序简化相关
    private List<ScopeTree> allRelatedScopes=new ArrayList<>();//当前标识符的取值在哪些作用域内定义或改变过，直接或间接
    
    //和程序简化相关
    private List<SymbolRecord> allRelatedIdentifiers=new ArrayList<>();//当前标识符的取值和哪些标识符有关，直接或间接
    
    //个程序简化相关
    private List<String> allRelatedIdentifiersNames=new ArrayList<>();//当前标识符的取值和哪些标识符有关，直接或间接，标识符名称列表
    
    //和程序简化相关
    private List<FunctionSymbolRecord> allRelatedFuns=new ArrayList<>();//当前标识符的取值和哪些函数有关，直接或间接
    
    //个程序简化相关
    private List<String> allRelatedFunsNames=new ArrayList<>();//当前标识符的取值和哪些函数有关，直接或间接，函数名称列表
    

    public SymbolRecord(String name, Type type, String ID, String category, ScopeTree scope, String value) {
        this.name = name;
        this.type = type;
        this.ID = ID;
        this.category = category;
        this.scope = scope;
        this.value = value;
    }

    public SymbolRecord(String name, Type type, String ID, String category, ScopeTree scope, String value, int arrayLength) {
    	this.name = name;
        this.type = type;
        this.ID = ID;
        this.category = category;
        this.scope = scope;
        this.value = value;
        this.arrayLength = arrayLength;
	}

	public SymbolRecord() {
		// TODO Auto-generated constructor stub
	}

	public ScopeTree getScope() {
        return scope;
    }

    public void setScope(ScopeTree scope) {
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

	public int getArrayLength() {
		return arrayLength;
	}

	public void setArrayLength(int arrayLength) {
		this.arrayLength = arrayLength;
	}
	
	public Map<Integer, Integer> getScopeUsedTimeMap() {
		return scopeUsedTimeMap;
	}

	public void setScopeUsedTimeMap(Map<Integer, Integer> scopeUsedTimeMap) {
		this.scopeUsedTimeMap = scopeUsedTimeMap;
	}
    
	public void setScopeUsedTimeMapByData(ScopeTree st, int time) {
		Integer correntSrUsedTimeInSn=scopeUsedTimeMap.get(st.getScopeID());
		if(correntSrUsedTimeInSn==null||correntSrUsedTimeInSn<=0) {
			scopeUsedTimeMap.put(st.getScopeID(), time);
		}else {
			scopeUsedTimeMap.put(st.getScopeID(), correntSrUsedTimeInSn+time);
		}
	}

	public Map<Integer, List<SymbolRecord>> getScopeModifiedIdentifiers() {
		return scopeModifiedIdentifiers;
	}

	public void setScopeModifiedIdentifiers(Map<Integer, List<SymbolRecord>> scopeModifiedIdentifiers) {
		this.scopeModifiedIdentifiers = scopeModifiedIdentifiers;
	}

	public Map<Integer, List<FunctionSymbolRecord>> getScopeModifiedFuns() {
		return scopeModifiedFuns;
	}

	public void setScopeModifiedFuns(Map<Integer, List<FunctionSymbolRecord>> scopeModifiedFuns) {
		this.scopeModifiedFuns = scopeModifiedFuns;
	}

	public Map<Integer, Map<Integer, String>> getScopeContent() {
		return scopeContent;
	}

	public void setScopeContent(Map<Integer, Map<Integer, String>> scopeContent) {
		this.scopeContent = scopeContent;
	}

	public List<Integer> getRelatedScopes() {
		return relatedScopes;
	}

	public void setRelatedScopes(List<Integer> relatedScopes) {
		this.relatedScopes = relatedScopes;
	}

	public List<ScopeTree> getAllRelatedScopes(SymbolTable st) throws IOException {
		ScopeTree scopeTree=new ScopeTree();
		Queue<SymbolRecord> queueSymbolRecord=new LinkedList<SymbolRecord>();
		Queue<SymbolRecord> queueSymbolRecordtemp=new LinkedList<SymbolRecord>();
		queueSymbolRecord.add(this);
		List<FunctionSymbolRecord> funs=new ArrayList<>();//相关函数，return中的标识符已经添加到相关标识符集合
		queueSymbolRecord=initQueue(queueSymbolRecord,st);//递归地取出所有相关标识符，加入到待分析的标识符队列当中
		
		
		while((queueSymbolRecord!=null&&!queueSymbolRecord.isEmpty())) {
			SymbolRecord element=queueSymbolRecord.poll();
			if(element==null) {
				break;
			}
//			//System.out.println(1+element.getName());
			if(!containsSR(allRelatedIdentifiers,element)) {
				allRelatedIdentifiers.add(element);
			}
			if(!allRelatedIdentifiersNames.contains(element.getName())) {
				allRelatedIdentifiersNames.add(element.getName());
			}
			queueSymbolRecordtemp.add(element);
			
		    Map<Integer,List<SymbolRecord>> scopeModifiedIdentifiers=element.getScopeModifiedIdentifiers();//当前标识符的取值在各个可用作用域内的和哪些标识符相关，<scopeId,相关的标识符列表>
		    if(scopeModifiedIdentifiers!=null&&scopeModifiedIdentifiers.size()>0) {
		    	for(Map.Entry<Integer,List<SymbolRecord>> entry : scopeModifiedIdentifiers.entrySet()){
		    		Integer scopeId = entry.getKey();
		    		List<SymbolRecord> srs = entry.getValue();
		    		ScopeTree scopeNode=scopeTree.getScopeNodeById(st,scopeId);
		    		if(!containsST(allRelatedScopes,scopeNode)) {
		    			allRelatedScopes.add(scopeNode);
		    			updateScopeIfFun(funs,scopeNode,st, queueSymbolRecord, queueSymbolRecordtemp);//如果这个作用域是一个函数，那么更新函数的返回值中包含的标识符到相关标识符集合当中
		    		}
//		    		//System.out.println(2+element.getName());
		    		if(srs!=null) {
		    			int k=0;
		    			updateByVariable(srs, queueSymbolRecord, queueSymbolRecordtemp);
		    		}
		    		
	    		}
		    }
		    
		    
		    
		    Map<Integer,List<FunctionSymbolRecord>> scopeModifiedFuns=element.getScopeModifiedFuns();//当前标识符的取值在各个可用作用域内的和哪些函数标识符相关，<scopeId,相关的函数标识符列表>
		    if(scopeModifiedFuns!=null&&scopeModifiedFuns.size()>0) {
		    	for(Map.Entry<Integer,List<FunctionSymbolRecord>> entry : scopeModifiedFuns.entrySet()){
		    		Integer scopeId = entry.getKey();
		    		List<FunctionSymbolRecord> fsrs = entry.getValue();
		    		ScopeTree scopeNode=scopeTree.getScopeNodeById(st,scopeId);
		    		if(!allRelatedScopes.contains(scopeNode)) {
		    			allRelatedScopes.add(scopeNode);
		    			updateScopeIfFun(funs,scopeNode,st, queueSymbolRecord, queueSymbolRecordtemp);//如果这个作用域是一个函数，那么更新函数的返回值中包含的标识符到相关标识符集合当中
			    		
		    		}
		    		
		    		if(fsrs!=null) {
		    			updateByFunction(fsrs, queueSymbolRecord, queueSymbolRecordtemp,funs, st);
		    		}
	    		}
		    }
		    
		    
		    //取出相关作用域的头和尾中的相关标识符和函数，加入到相关标识符和函数列表里
		    
		    List<Integer> relatedScopes=element.getRelatedScopes();
		    List<ScopeTree> nodeAncestors=new ArrayList<>();
		    for(Integer scopeId:relatedScopes) {
		    	ScopeTree scopeNode=st.getScopeIdNodeMap().get(scopeId);
		    	while(scopeNode!=null) {
		    		updateScopeIfFun(funs,scopeNode,st, queueSymbolRecord, queueSymbolRecordtemp);//如果这个作用域是一个函数，那么更新函数的返回值中包含的标识符到相关标识符集合当中
		    		if(nodeAncestors.contains(scopeNode)) {
		    			scopeNode=scopeNode.getFather(); 
		    			continue;
		    		}else {
		    			nodeAncestors.add(scopeNode);
		    		}
		    		String head=scopeNode.getScopeHead();
			    	String tail=scopeNode.getScopeTail();
			    	List<SymbolRecord> relatedIdentifiersH=st.getIdentifiersFromStr(head,st);//提取出标识符名称
		    		List<SymbolRecord> relatedIdentifiersT=st.getIdentifiersFromStr(tail,st);//提取出标识符名称
		    		
		    		if(relatedIdentifiersH==null) {
		    			relatedIdentifiersH=relatedIdentifiersT;
		    		}else {
		    			if(relatedIdentifiersT!=null) {
		    				relatedIdentifiersH.addAll(relatedIdentifiersT);
		    			}
		    		}
		    		if(relatedIdentifiersT==null) {
		    			scopeNode=scopeNode.getFather(); 
		    			continue;
		    		}
		    		if(relatedIdentifiersH!=null&relatedIdentifiersH.size()>0) {
		    			updateByVariable(relatedIdentifiersH, queueSymbolRecord, queueSymbolRecordtemp);
		    		}
		    		scopeNode=scopeNode.getFather(); 
		    	}
		    	
		    }
		    
		}
		
		
		String printStr="";
		printStr+="/*相关标识符=====================================================遍历结束后";
		printStr+="\r\n";
		for(SymbolRecord id:allRelatedIdentifiers) {printStr+=id.getName()+" ";};
		printStr+="*/";
		File file = new File("H:\\demo\\log.txt");
		FileWriter fr = new FileWriter(file, true);
		fr.write(printStr+"\r\n");
		fr.close();
//		//System.out.println(printStr);
		
		
		queueSymbolRecord=completeQueue(queueSymbolRecord,st);//递归地取出所有相关标识符，加入到待分析的标识符队列当中，防止遗漏
		
		//如果相关的作用域是函数，那么把函数的返回值中包含的标识符，调用函数的语句中的标识符
		return allRelatedScopes;
	}
	
	/**
	 * qurong
	 * 2023.9.21
	 * 初始化相关标识符列表，递归
	 * @param queueSymbolRecord
	 * @param st
	 * @return
	 * @throws IOException 
	 */
	private Queue<SymbolRecord> initQueue(Queue<SymbolRecord> queueSymbolRecord,SymbolTable st) throws IOException {
		// TODO Auto-generated method stub
		Set<SymbolRecord> result=new HashSet<>();
		Set<SymbolRecord> temp=new HashSet<>();
		while((queueSymbolRecord!=null&&!queueSymbolRecord.isEmpty())) {
			SymbolRecord element=queueSymbolRecord.poll();
			if(element==null) {
				break;
			}
			result.add(element);
			temp.add(element);
			List<SymbolRecord> srs=element.getAllRelatedIdentifiers();
//			srs=matchSymbolRecords(srs,st);//相关标识符列表和总表对齐
			if(srs!=null) {
				for(SymbolRecord sr: srs) {
					if(!temp.contains(sr)) {
						queueSymbolRecord.add(sr);
					}
				}
				result.addAll(srs);
    		}
		}
		
		
		result=ensureRelatedIdentifiers(result,st);
		
		String printStr2="";
		printStr2+="/*相关标识符=====================================================初始化result";
		printStr2+="\r\n";
		for(SymbolRecord id:result) {printStr2+=id.getName()+" ";};
		printStr2+="*/";
		File file = new File("H:\\demo\\log.txt");
		FileWriter fr = new FileWriter(file, true);
		fr.write(printStr2+"\r\n");
		
		

		
//		allRelatedIdentifiers.addAll(result);
		for(SymbolRecord sr:result) {
			queueSymbolRecord.add(sr);
			allRelatedIdentifiersNames.add(sr.getName());
			allRelatedIdentifiers.add(sr);
		}
		
		
		String printStr="";
		printStr+="/*相关标识符=====================================================初始化allRelatedIdentifiers";
		printStr+="\r\n";
		for(SymbolRecord id:allRelatedIdentifiers) {printStr+=id.getName()+" ";};
		printStr+="*/";
		fr.write(printStr+"\r\n");
		fr.close();
//		//System.out.println(printStr);
		
		return queueSymbolRecord;
	}
	
	
	/**
	 * qurong
	 * 2023.9.23
	 * 初始化相关标识符列表，递归
	 * @param queueSymbolRecord
	 * @param st
	 * @return
	 * @throws IOException 
	 */
	private Queue<SymbolRecord> completeQueue(Queue<SymbolRecord> queueSymbolRecord,SymbolTable st) throws IOException {
		// TODO Auto-generated method stub
		Set<SymbolRecord> result=new HashSet<>();
		Set<SymbolRecord> temp=new HashSet<>();
		while((queueSymbolRecord!=null&&!queueSymbolRecord.isEmpty())) {
			SymbolRecord element=queueSymbolRecord.poll();
			if(element==null) {
				break;
			}
			result.add(element);
			temp.add(element);
			List<SymbolRecord> srs=element.getAllRelatedIdentifiers();
//			srs=matchSymbolRecords(srs,st);//相关标识符列表和总表对齐
			if(srs!=null) {
				for(SymbolRecord sr: srs) {
					if(!temp.contains(sr)) {
						queueSymbolRecord.add(sr);
					}
				}
				result.addAll(srs);
    		}
		}
		
		
		result=completeRelatedIdentifiers(st);
		
		String printStr2="";
		printStr2+="/*相关标识符=====================================================初始化result";
		printStr2+="\r\n";
		for(SymbolRecord id:result) {printStr2+=id.getName()+" ";};
		printStr2+="*/";
		File file = new File("H:\\demo\\log.txt");
		FileWriter fr = new FileWriter(file, true);
		fr.write(printStr2+"\r\n");
		
		

		
//		allRelatedIdentifiers.addAll(result);
		for(SymbolRecord sr:result) {
			queueSymbolRecord.add(sr);
			allRelatedIdentifiersNames.add(sr.getName());
			allRelatedIdentifiers.add(sr);
		}
		
		
		String printStr="";
		printStr+="/*相关标识符=====================================================初始化allRelatedIdentifiers";
		printStr+="\r\n";
		for(SymbolRecord id:allRelatedIdentifiers) {printStr+=id.getName()+" ";};
		printStr+="*/";
		fr.write(printStr+"\r\n");
		fr.close();
//		//System.out.println(printStr);
		
		return queueSymbolRecord;
	}

	/**
	 * qurong
	 * 2023.9.22
	 * 确保set中每一个标识符的相关标识符，都已经包含在set集合当中了
	 * @param result
	 * @param st
	 * @return
	 */
	private Set<SymbolRecord> ensureRelatedIdentifiers(Set<SymbolRecord> set, SymbolTable st) {
		// TODO Auto-generated method stub
		List<SymbolRecord> symbolRecord=st.getSymbolRecords();
		Set<SymbolRecord> result=new HashSet<>();
		result.addAll(set);
		for(SymbolRecord sr:symbolRecord) {
			if(result.contains(sr)||sameName(result,sr)) {
				result.addAll(sr.getAllRelatedIdentifiers());
			}
		}
		
		return result;
	}
	
	/**
	 * qurong
	 * 2023.9.23
	 * 确保set中每一个标识符的相关标识符，都已经包含在set集合当中了
	 * @param result
	 * @param st
	 * @return
	 */
	private Set<SymbolRecord> completeRelatedIdentifiers(SymbolTable st) {
		// TODO Auto-generated method stub
		List<SymbolRecord> symbolRecord=st.getSymbolRecords();
		Set<SymbolRecord> result=new HashSet<>();
		result.addAll(allRelatedIdentifiers);
		for(SymbolRecord sr:symbolRecord) {
			if(result.contains(sr)||sameName(result,sr)) {
				result.addAll(sr.getAllRelatedIdentifiers());
			}
		}
		
		return result;
	}

	/**
	 * qurong
	 * 2023.9.21
	 * 相关标识符列表和总表对齐
	 * @param srs
	 * @param st
	 * @return
	 */
	private List<SymbolRecord> matchSymbolRecords(List<SymbolRecord> srs, SymbolTable st) {
		// TODO Auto-generated method stub
		List<SymbolRecord> symbolRecord=st.getSymbolRecords();
		List<SymbolRecord> result=new ArrayList<>();
		for(SymbolRecord sr:symbolRecord) {
			for(SymbolRecord sr1:srs) {
				if(sr.getID().equals(sr1.getID())||sr.getName().equals(sr1.getName())) {
					result.add(sr);
					break;
				}
			}
		}
//		if(result.size()<srs.size()) {
//			//System.out.println("unmatched identifier: ");
//			//System.out.println("all identifiers: ");
//			for(SymbolRecord sr:symbolRecord) {
//				//System.out.println(sr.getName());
//			}
//			//System.out.println("related identifiers: ");
//			for(SymbolRecord sr1:srs) {
//				//System.out.println(sr1.getName());
//			}
//			
//		}
		return result;
	}

	/**
	 * 根据变量标识符记录，更新相关标识符列表
	 * @param srs
	 * @param queueSymbolRecord
	 * @param queueSymbolRecordtemp
	 */
	private void updateByVariable(List<SymbolRecord> srs,Queue<SymbolRecord> queueSymbolRecord, Queue<SymbolRecord> queueSymbolRecordtemp) {
		// TODO Auto-generated method stub
		for(SymbolRecord sr:srs) {
//			//System.out.println(++k+" related identifier of "+element.getName()+": "+sr.getName());
			if(!containsSR(allRelatedIdentifiers,sr)) {
				allRelatedIdentifiers.add(sr);
			}
			if(!allRelatedIdentifiersNames.contains(sr.getName())) {
				allRelatedIdentifiersNames.add(sr.getName());
			}
			if(!containsSRQueue(queueSymbolRecord,sr)&&!containsSRQueue(queueSymbolRecordtemp,sr)) {
				queueSymbolRecord.add(sr);
			}
		}
	}

	/**
	 * 根据函数标识符记录，更新相关函数，相关作用域
	 * @param fsrs
	 */
	private void updateByFunction(List<FunctionSymbolRecord> fsrs,Queue<SymbolRecord> queueSymbolRecord, Queue<SymbolRecord> queueSymbolRecordtemp,List<FunctionSymbolRecord> funs,SymbolTable st) {
		// TODO Auto-generated method stub
		for(FunctionSymbolRecord fsr:fsrs) {
			if(!containsFSR(allRelatedFuns,fsr)) {
				allRelatedFuns.add(fsr);
				allRelatedFunsNames.add(fsr.getName());
			}
			
			ScopeTree funScopeNode=fsr.getScope();
    		if(!allRelatedScopes.contains(funScopeNode)) {
    			allRelatedScopes.add(funScopeNode);
    			updateScopeIfFun(funs,funScopeNode,st, queueSymbolRecord, queueSymbolRecordtemp);//如果这个作用域是一个函数，那么更新函数的返回值中包含的标识符到相关标识符集合当中
    		}
    		
    		List<SymbolRecord> returnIdentifiers=fsr.getReturnIdentifiers();
    		returnIdentifiers.addAll(fsr.getParameterIdentifiers());
    		//调用函数的行中涉及到的标识符（实参）
    		returnIdentifiers.addAll(fsr.getActrualParameterIdentifiers());
    		returnIdentifiers.addAll(fsr.getActrualReturnIdentifiers());
//    		returnIdentifiers.addAll(fsr.getReturnIdentifiers());
    		updateByVariable(returnIdentifiers, queueSymbolRecord, queueSymbolRecordtemp);

		}
	}

	/**
	 * qurong
	 * 2023.9.20
	 * 如果这个作用域是一个函数，那么更新函数的返回值中包含的标识符到相关标识符集合当中
	 * @param funs
	 * @param scopeNode
	 * @param st
	 */
	private void updateScopeIfFun(List<FunctionSymbolRecord> funs, ScopeTree scopeNode,SymbolTable st,Queue<SymbolRecord> queueSymbolRecord, Queue<SymbolRecord> queueSymbolRecordtemp) {
		// TODO Auto-generated method stub
		FunctionSymbolRecord fsr=scopeNode.getFun();
		if(fsr!=null) { 
			if(funs.contains(fsr)) {
				return;
			}
			List<SymbolRecord> returnIdentifiers=fsr.getReturnIdentifiers();
			updateByVariable(returnIdentifiers, queueSymbolRecord, queueSymbolRecordtemp);
			funs.add(fsr);
		}
	}

	/**
	 * qurong
	 * 2023.9.20
	 * 更新相关作用域（函数）的返回值中包含的标识符到相关标识符列表里
	 * @throws IOException 
	 */
	private void updateReturnIdentifiers(SymbolTable st) throws IOException {
		// TODO Auto-generated method stub
		List<ScopeTree> relatedScopes=new ArrayList<>();
		List<ScopeTree> temp=new ArrayList<>();
		for(ScopeTree scopeNode:allRelatedScopes) {
			FunctionSymbolRecord fsr=scopeNode.getFun();
			if(fsr!=null) { 
				List<SymbolRecord> returnIdentifiers=fsr.getReturnIdentifiers();
				for(SymbolRecord sr:returnIdentifiers) {
					temp=sr.getAllRelatedScopes(st);
				}
			}
		}
	}

	private boolean containsSR(List<SymbolRecord> list,SymbolRecord sr) {
		if(list==null) {
			return false;
		}
		for(SymbolRecord temp:list) {
			if(temp.getID()==sr.getID()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean sameName(Set<SymbolRecord> set,SymbolRecord sr) {
		if(set==null) {
			return false;
		}
		for(SymbolRecord temp:set) {
			if(temp.getID()==sr.getID()||temp.getName()==sr.getName()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean containsST(List<ScopeTree> list,ScopeTree st) {
		if(list==null) {
			return false;
		}
		for(ScopeTree temp:list) {
			if(temp.getScopeID()==st.getScopeID()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean containsFSR(List<FunctionSymbolRecord> list,FunctionSymbolRecord fsr) {
		if(list==null) {
			return false;
		}
		for(FunctionSymbolRecord temp:list) {
			if(temp.getID()==fsr.getID()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean containsSRQueue(Queue<SymbolRecord> list,SymbolRecord sr) {
		if(list==null) {
			return false;
		}
		for(SymbolRecord temp:list) {
			if(temp.getID()==sr.getID()) {
				return true;
			}
		}
		return false;
	}

	public List<ScopeTree> getAllRelatedScopes() {
		return allRelatedScopes;
	}

	public void setAllRelatedScopes(List<ScopeTree> allRelatedScopes) {
		this.allRelatedScopes = allRelatedScopes;
	}

	public List<SymbolRecord> getAllRelatedIdentifiers() {
		return allRelatedIdentifiers;
	}

	public void setAllRelatedIdentifiers(List<SymbolRecord> allRelatedIdentifiers) {
		this.allRelatedIdentifiers = allRelatedIdentifiers;
	}

	public List<FunctionSymbolRecord> getAllRelatedFuns() {
		return allRelatedFuns;
	}

	public void setAllRelatedFuns(List<FunctionSymbolRecord> allRelatedFuns) {
		this.allRelatedFuns = allRelatedFuns;
	}

	public List<String> getAllRelatedIdentifiersNames() {
		return allRelatedIdentifiersNames;
	}

	public void setAllRelatedIdentifiersNames(List<String> allRelatedIdentifiersNames) {
		this.allRelatedIdentifiersNames = allRelatedIdentifiersNames;
	}

	public List<String> getAllRelatedFunsNames() {
		return allRelatedFunsNames;
	}

	public void setAllRelatedFunsNames(List<String> allRelatedFunsNames) {
		this.allRelatedFunsNames = allRelatedFunsNames;
	}

	
}
