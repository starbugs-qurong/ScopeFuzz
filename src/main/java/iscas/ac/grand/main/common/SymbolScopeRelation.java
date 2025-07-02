package iscas.ac.grand.main.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolScopeRelation {
    //和标识符选择策略相关
    private Map<Integer,Integer> scopeUsedTimeMap=new HashMap<>();//当前标识符在各个可用作用域内的使用次数，<scopeId,次数>
    
    //和程序简化相关
    private Map<Integer,List<SymbolRecord>> scopeModifiedIdentifiers=new HashMap<>();//当前标识符的取值在各个可用作用域内的和哪些标识符相关，<scopeId,相关的标识符列表>
    
    //和程序简化相关
    private Map<Integer,List<FunctionSymbolRecord>> scopeModifiedFuns=new HashMap<>();//当前标识符的取值在各个可用作用域内的和哪些函数标识符相关，<scopeId,相关的函数标识符列表>
    
    //和程序简化相关
    private Map<Integer,String> scopeContent=new HashMap<>();//当前标识符的取值在各个可用作用域内的和哪些程序片段（含作用域头尾）相关，<scopeId,程序片段>
    
    //和程序简化相关
    private List<Integer> scopes=new ArrayList<>();//当前标识符的取值在哪些作用域内定义或改变过

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

	public Map<Integer, String> getScopeContent() {
		return scopeContent;
	}

	public void setScopeContent(Map<Integer, String> scopeContent) {
		this.scopeContent = scopeContent;
	}
    
    

}
