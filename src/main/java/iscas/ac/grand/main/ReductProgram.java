package iscas.ac.grand.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import iscas.ac.grand.main.common.ConfigureInfo;
import iscas.ac.grand.main.common.FunctionSymbolRecord;
import iscas.ac.grand.main.common.ScopeTree;
import iscas.ac.grand.main.common.SymbolRecord;
import iscas.ac.grand.main.common.SymbolTable;
/**
 * qurong
 * 2023.8.18
 * 程序精简，在提交缺陷报告之前
 * @author 86188
 *
 */
public class ReductProgram {
	public void reductProgram() {
		
	}
	
	/**
	 * qurong
	 * 2023.8.18
	 * 根据程序，程序的符号表，差异内容的行号进行程序的精简
	 * @param programs
	 * @param st
	 * @param order
	 * @return
	 * @throws IOException 
	 */
	public String ReductProgramBySymbolTable(String programs, SymbolTable st,int order) throws IOException {
//		//System.out.println(st.getLineOrderStatement().toString());
//		//System.out.println(programs);
		String tempProgram=programs;
		int linecountBeofre=getLineCount(programs);
		
		String result="";
		Map<Integer,SymbolRecord> map=st.getPrintPositionOfSymbolRecord();//标识符的打印行号，对于数组，存在多对一的情况
		if(map==null||map.size()<=0) {
			return programs;
		}
		SymbolRecord problemIdentifier=map.get(order);
		if(problemIdentifier==null) {
			return programs;
		}
		
		//打印每个标识符的全部相关标识符
		String resltedIdentifiers=getPrintRelatedIdentifiers(st);
		
		
		//策略1
		programs=deleteUnrelatedScopes(problemIdentifier,programs,st);//删除无关作用域以及其后代作用域
		
		programs=deleteUnrelatedPrints(problemIdentifier,programs,st,order);//删除main函数中无关的打印语句
		
		programs=deleteUnrelatedFuntionCall(problemIdentifier,programs,st,order);//删除main函数中无关的函数调用语句
		
		int linecountAfter=getLineCount(programs);
		double reduceRate=(linecountBeofre-linecountAfter)/(linecountBeofre*1.0);
//		//System.out.println(programs);
//		//System.out.println(reduceRate);
		
		//策略二
		programs=deleteUrelatedLines(problemIdentifier,programs,st);//删除程序中，和相关标识符、函数都无关的语句所对应的行，保留作用域头尾
		
//		List<SymbolRecord> relatedSymbolRecord=new ArrayList<>();
//		List<FunctionSymbolRecord> relatedFunSymbolRecord=new ArrayList<>();
//		List<ScopeTree> relatedScope=new ArrayList<>();
//		getRelatedSymbolRecord(problemIdentifier,relatedSymbolRecord,relatedFunSymbolRecord,relatedScope,st);//取问题标识符相关的标识符、函数、以及作用域
//		result=deleteUnrelatedScopes(programs,relatedScope);//删除无关作用域以及其后代作用域
//		result=deleteRightScopes(result,relatedScope);//删除相关但出现在问题标识符修改右侧的作用域以及其后代作用域
		//result=moveToParent(result,relatedScope);//如果中间的多层作用域都无关，移动相关作用域到外层
		linecountAfter=getLineCount(programs);
		double reduceRate2=(linecountBeofre-linecountAfter)/(linecountBeofre*1.0);
		
		//打印每个标识符的全部相关标识符(处理之后)
		String resltedIdentifiersAfter=getRelatedIdentifiersByIdentifier(st,problemIdentifier);
		
		String resltedIdentifiers2=getPrintRelatedIdentifiers(st);//检查st是否发生变化
		
//		//System.out.println(reduceRate+"\t"+reduceRate2);
		if(linecountAfter>15) {
//			//System.out.println(tempProgram);
//			//System.out.println(programs);
//			//System.out.println(resltedIdentifiers);
//			//System.out.println(resltedIdentifiersAfter);
//			//System.out.println(resltedIdentifiers2);
			
			File file = new File("H:"+File.separator+"demo"+File.separator+"log.txt");
			FileWriter fr = new FileWriter(file, true);
			fr.write("/*"+tempProgram+"*/\r\n");
			fr.write(programs+"\r\n");
			fr.write(resltedIdentifiers+"\r\n");
			fr.write(resltedIdentifiersAfter+"\r\n");
			fr.write(resltedIdentifiers2+"\r\n");
			fr.close();
		}
//		//System.out.println(programs);
		return programs;
	}

	/**
	 * qurong
	 * 2023.9.19
	 * 打印每个标识符的直接关联标识符
	 * @param st
	 */
	private String getPrintRelatedIdentifiers(SymbolTable st) {
		// TODO Auto-generated method stub
		String result="";
		result+="/*相关标识符=====================================================";
		List<SymbolRecord> list=st.getSymbolRecords();
		for(SymbolRecord sr:list) {
			List<SymbolRecord> relatedList=sr.getAllRelatedIdentifiers();
			result+="\r\n";
			result+=sr.getName()+" related: "+"\r\n";
			for(SymbolRecord id:relatedList) {result+=id.getName()+" ";};
		}
		result+="*/";
		return result;
	}
	
	/**
	 * qurong
	 * 2023.9.19
	 * 打印每个标识符的直接关联标识符
	 * @param st
	 */
	private String getRelatedIdentifiersByIdentifier(SymbolTable st,SymbolRecord sr) {
		// TODO Auto-generated method stub
		String result="";
		result+="/*相关标识符====================================================="+sr.getName();
		List<SymbolRecord> relatedList=sr.getAllRelatedIdentifiers();
		result+="\r\n";
		result+=sr.getName()+" related: "+"\r\n";
		for(SymbolRecord id:relatedList) {result+=id.getName()+" ";};
		result+="*/";
		return result;
	}

	/**
	 * qurong
	 * 2023.8.29
	 * 删除各个相关作用域内的不相关行，自底向上
	 * @param problemIdentifier
	 * @param programs
	 * @param st
	 * @return
	 */
	private String deleteUrelatedLines(SymbolRecord problemIdentifier, String programs, SymbolTable st) {
		// TODO Auto-generated method stub
		ScopeTree scopeTree=new ScopeTree();
		String[] lines=programs.split("\r\n");
		if(lines==null||lines.length<=1) {
			return programs;
		}
		List<String> allRelatedIdentifiersNames=problemIdentifier.getAllRelatedIdentifiersNames();//当前标识符的取值和哪些标识符有关，直接或间接，标识符名称列表
	    List<String> allRelatedFunsNames=problemIdentifier.getAllRelatedFunsNames();//当前标识符的取值和哪些函数有关，直接或间接，函数名称列表
	    boolean forFlag=false;
	    String forHead="";
		for(String line:lines) {
			boolean flag=false;
			if(line.contains("break")||line.contains("while")||line.contains("print")||line.contains("count")||line.contains("return")) {
				continue;
			}
			
			if(!forFlag&&(line.startsWith("for (")||line.startsWith("for("))) {
				forFlag=true;
			}
			if(forFlag&&(line.endsWith(") {")||line.endsWith("){"))) {
				forFlag=false;
				continue;
			}
			if(forFlag) {
				continue;
			}
			
			if(line.endsWith(";")) {
				if(line.contains("=")) {//是赋值语句 只关心左值
					String left=line.split("=")[0];
					for(String id:allRelatedIdentifiersNames) {
						if(left.contains(id)) {
							flag=true;
							break;
						}
					}
					if(!flag) {
						for(String fun:allRelatedFunsNames) {
							if(left.contains(fun)) {
								flag=true;
								break;
							}
						}
					}
				}else {//不是赋值语句 看整个语句
					for(String id:allRelatedIdentifiersNames) {
						if(line.contains(id)) {
							flag=true;
							break;
						}
					}
					if(!flag) {
						for(String fun:allRelatedFunsNames) {
							if(line.contains(fun)) {
								flag=true;
								break;
							}
						}
					}
				}
				
				if(!flag) {
					//删除该行
					programs=programs.replace(line+"\r\n","" );
				}
			}
			
		}
		return programs;
	}


	/**
	 * 计算程序的行数
	 * @param programs
	 * @return
	 */
	private int getLineCount(String programs) {
		// TODO Auto-generated method stub
		String[] lines=programs.split("\r\n");
		return lines.length;
	}

	/**
	 * qurong
	 * 2023.8.24
	 * 删除不相关函数的调用语句（负责函数被删了，调用语句还在，程序会报错）
	 * @param problemIdentifier
	 * @param programs
	 * @param st
	 * @param order
	 * @return
	 */
	private String deleteUnrelatedFuntionCall(SymbolRecord problemIdentifier, String programs, SymbolTable st,
			int order) {
		// TODO Auto-generated method stub
		ScopeTree mainScope=st.getMainScope();
		
		String mainContent=mainScope.getScopeBodyFragment();
		String mainContentFormat=formatCodeLines(mainContent);
		if(mainContentFormat==null||mainContentFormat=="")return programs; 
		
		List<FunctionSymbolRecord> allRelatedFuns=problemIdentifier.getAllRelatedFuns();
		
		
		String[] lines=mainContentFormat.split("\r\n");
		for(String line:lines) {
			//如果line包含函数标识符，但是该函数不是和问题行相关的函数，删掉这一行
			//if(fsr.getName())
			boolean flag1=false;
			for(FunctionSymbolRecord fsr:st.getFunctionSymbolRecords()) {
				if(line.contains(fsr.getName())){
					flag1=true;
					break;
				}
			}
			boolean flag2=false;
			if(flag1) {
				for(FunctionSymbolRecord fsr:allRelatedFuns) {
					if(line.contains(fsr.getName())){
						flag2=true;
						break;
					}
				}
			}else {
				continue;
			}
			if(!flag2) {
				programs=programs.replace(line+"\r\n", "");
				mainContent=mainContent.replace(line, "");
			}
		}
		
		mainScope.setScopeBodyFragment(mainContent);
		st.setMainScope(mainScope);
		
		return programs;
	}

	/**
	 * qurong
	 * 2023.8.24
	 * 删除main函数中无关的打印语句
	 * @param problemIdentifier
	 * @param programs
	 * @param st
	 * @param order 
	 * @return
	 */
	private String deleteUnrelatedPrints(SymbolRecord problemIdentifier, String programs, SymbolTable st, int order) {
		// TODO Auto-generated method stub
		Map<Integer,String> map=st.getPrintString();
		if(map==null||map.size()<=0) return programs;
		String problemStatement=map.get(order);
		String problemStatementFormat=formatCodeLines(problemStatement);
		if(problemStatement==null||problemStatement=="") return programs;
		
		ScopeTree mainScope=st.getMainScope();
		if(mainScope==null)return programs; 
		
		String mainContent=mainScope.getScopeBodyFragment();
		String mainContentFormat=formatCodeLines(mainContent);
		if(mainContentFormat==null||mainContentFormat=="")return programs; 
		
		String printFragment=st.getPrintFragment();
		String printFragmentFormat=formatCodeLines(printFragment);
		if(printFragment==null||printFragment=="")return programs; 
		
		programs=programs.replace(printFragmentFormat, problemStatementFormat);//将原有的打印片段替换成问题标识符的打印片段
				
		mainContent=mainContent.replace(printFragment, problemStatement);//更新main函数对应的程序内容
		
		mainScope.setScopeBodyFragment(mainContent);
		
		st.setMainScope(mainScope);
		
		st.setPrintFragment(problemStatement);//更新打印片段对应的内容
		
		return programs;
	}

	/**
	 * qurong
	 * 2023.8.18
	 * 策略1，删除无关作用域以及其后代作用域
	 * @param problemIdentifier
	 * @param programs
	 * @param st
	 * @return
	 */
	private String deleteUnrelatedScopes(SymbolRecord problemIdentifier, String programs, SymbolTable st) {
		// TODO Auto-generated method stub
		List<ScopeTree> relatedScope=new ArrayList<>();
		//List<Integer> relatedScopeIds=getAllRelatedScopes(problemIdentifier);//取出所有相关的
		ScopeTree scopeTree=new ScopeTree();
		try {
			//relatedScope=scopeTree.getRelatedScopesObj(problemIdentifier.getRelatedScopes(),st);
			relatedScope=problemIdentifier.getAllRelatedScopes(st);
			if(problemIdentifier.getRelatedScopes().size()==0) {
				//和其它作用域都无关
//				//System.out.println("2");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//"null related scope info "
		}
		
//		//System.out.println("相关标识符=====================================================");
//		for(String id:problemIdentifier.getAllRelatedIdentifiersNames()) {//System.out.println(id);};
//		
//		
//		//System.out.println("相关函数=====================================================");
//		for(String funid:problemIdentifier.getAllRelatedFunsNames()) {//System.out.println(funid);};
//			
		List<ScopeTree> leafNodes=scopeTree.getLeafNodes(st);//叶子节点
		programs=deleteProgramFromLeaf(leafNodes,relatedScope,programs,st);
		
		return programs;
	}

	/**
	 * qurong
	 * 2023.8.23
	 * 自底向上删除不相关作用域对应的程序片段
	 * @param leafNodes
	 * @param relatedScope 
	 * @param programs 
	 * @param st 
	 * @return
	 */
	private String deleteProgramFromLeaf(List<ScopeTree> leafNodes, List<ScopeTree> relatedScope, String programs, SymbolTable st) {
		// TODO Auto-generated method stub
		if(leafNodes==null||leafNodes.size()<=0) {//没有叶子节点，不做删除
			//System.out.println("no leaf");
			return programs;
		}
		ScopeTree root=st.getRootScope();//根节点
		
		List<ScopeTree> relatedScopesAndParent=getRelatedScopesAndParents(relatedScope,root);//取出所有相关的节点

		confirmRootAndMain(relatedScopesAndParent,st);//确保根节点和main方法的节点在相关节点列表中
		
		
		//相关作用域
//		//System.out.println("相关作用域=====================================================");
//		printScopeIdAndContent(relatedScopesAndParent);//打印相关的作用域信息
		//不相关作用域
//		//System.out.println("不相关作用域=====================================================");
		List<ScopeTree> unRelatedScopes=getUnrelatedScopes(relatedScopesAndParent,root);
//		printScopeIdAndContent(unRelatedScopes);//打印相关的作用域信息
		
//		programs=deleteProgramByScopeNodes(relatedScopesAndParent,programs,root);//删除相关节点对应的程序片段
		
		programs=deleteProgramByUnrelatedScopes(unRelatedScopes,programs);//删除相关节点对应的程序片段
		
		return programs;
	}

	/**
	 * qurong
	 * 2023.8.31
	 * 删除程序中的无关作用域对应的程序片段
	 * @param unRelatedScopes
	 * @param programs
	 * @return
	 */
	private String deleteProgramByUnrelatedScopes(List<ScopeTree> unRelatedScopes, String programs) {
		// TODO Auto-generated method stub
		for(ScopeTree scopeNode:unRelatedScopes) {
			programs=deleteProgramByScopeNode(scopeNode,programs);
		}
		return programs;
	}

	/**
	 * qurong
	 * 2023.8.31
	 * 取出所有不相关的作用域
	 * @param relatedScopesAndParent
	 * @param root
	 * @return
	 */
	private List<ScopeTree> getUnrelatedScopes(List<ScopeTree> relatedScopesAndParent, ScopeTree root) {
		// TODO Auto-generated method stub
		List<ScopeTree> result=new ArrayList<>();
		Queue<ScopeTree> nodesQueue=new LinkedList<ScopeTree>();
		nodesQueue.add(root);
		while(!nodesQueue.isEmpty()) {
			ScopeTree firstNode=nodesQueue.poll();
			if(!relatedScopesAndParent.contains(firstNode)) {//不相关
				result.add(firstNode);
			}
			List<ScopeTree> children=firstNode.getChildren();
			if(children!=null&&children.size()>0) {//存在性判定
				for(ScopeTree child: children) {
					if(!nodesQueue.contains(child)) {
						nodesQueue.add(child);
					}
				}
			}
			
		}
		return result;
	}

	/**
	 * qurong
	 * 2023.8.23
	 * 确保相关节点列表中包含root节点以及main函数节点
	 * @param relatedScopesAndParent
	 * @param st 
	 */
	private void confirmRootAndMain(List<ScopeTree> relatedScopesAndParent, SymbolTable st) {
		// TODO Auto-generated method stub
		if(relatedScopesAndParent==null) {
			relatedScopesAndParent=new ArrayList<ScopeTree>();
		}
		
		if(!relatedScopesAndParent.contains(st.getMainScope())) {
			relatedScopesAndParent.add(st.getMainScope());
			
		}
		if(!relatedScopesAndParent.contains(st.getRootScope())) {
			relatedScopesAndParent.add(st.getRootScope());
		}
	}

	private void printScopeIdAndContent(List<ScopeTree> relatedScopesAndParent) {
		// TODO Auto-generated method stub
		for(ScopeTree st:relatedScopesAndParent) {
			//System.out.println("scope id: "+st.getScopeID());
			//System.out.println("scope body fragment: ");
			//System.out.println(st.getScopeBodyFragment());
			//System.out.println("scope fragment: ");
			//System.out.println(st.getScopeFragment());
			//System.out.println("scope head: ");
			//System.out.println(st.getScopeHead());
			//System.out.println("scope tail: ");
			//System.out.println(st.getScopeTail());
		}
		
	}

	/**
	 * qurong
	 * 2023.8.23
	 * 删除程序中某个作用域对应的程序片段
	 * @param relatedScopesAndParent
	 * @param programs
	 * @param root 
	 * @return
	 */
	private String deleteProgramByScopeNodes(List<ScopeTree> relatedScopesAndParent, String programs, ScopeTree root) {
		// TODO Auto-generated method stub
		Queue<ScopeTree> nodesQueue=new LinkedList<ScopeTree>();
		nodesQueue.add(root);
		while(!nodesQueue.isEmpty()) {
			ScopeTree firstNode=nodesQueue.poll();
			if(!relatedScopesAndParent.contains(firstNode)) {//不相关，可以删掉
				programs=deleteProgramByScopeNode(firstNode,programs);
			}else {//相关，节点出队，节点的子节点入队
				List<ScopeTree> children=firstNode.getChildren();
				if(children!=null&&children.size()>0) {//存在性判定
					for(ScopeTree child: children) {
						if(!nodesQueue.contains(child)) {
							nodesQueue.add(child);
						}
					}
				}
			}
		}
		return programs;
	}

	/**
	 * qurong
	 * 2023.8.23
	 * 取所有相关节点及其祖先节点
	 * @param relatedScope
	 * @param root 
	 * @param leafNodes 
	 * @return
	 */
	private List<ScopeTree> getRelatedScopesAndParents(List<ScopeTree> relatedScope, ScopeTree root) {
		// TODO Auto-generated method stub
		List<ScopeTree> result=new ArrayList<>();
		
		for(ScopeTree scope: relatedScope) {
			ScopeTree temp=scope;
			while(temp!=null&&temp!=root) {
				if(!result.contains(temp)) {
					result.add(temp);
				}
				temp=temp.getFather();
			}
		}
		
		return result;
	}

	/**
	 * qurong
	 * 2023.8.23
	 * 删除程序中某个作用域对应的程序片段
	 * @param firstNode
	 * @param programs
	 * @return
	 */
	private String deleteProgramByScopeNode(ScopeTree scopeNode, String programs) {
		// TODO Auto-generated method stub
		
		if(scopeNode==null||programs=="") {
			return programs;
		}
		
		String fragment=scopeNode.getScopeBodyFragment();
//		if(fragment.contains("while")||fragment.contains("for")) {
//			//System.out.println(fragment);
//		}
		
		if(fragment==null||fragment=="") {
			return programs;
		}
		String formatFragment="";
		if(ConfigureInfo.ifCCode){//按照程序中的换行规则对scope对应的程序片段也进行格式化
			formatFragment=formatCodeLines(fragment);
        }
		
		if(programs.contains(formatFragment)) {
			programs=programs.replace(formatFragment, "");
		}else {
//			//System.out.println("not match!!");
		}
		
		return programs;
	}

	/**
	 * qurong
	 * 2023.8.24
	 * 格式化程序内容（换行）
	 * @param fragment
	 * @return
	 */
	private String formatCodeLines(String fragment) {
		// TODO Auto-generated method stub
		String result="";
		result=fragment.replaceAll("}","}\r\n");
		result=result.replaceAll(";",";\r\n");
		result=result.replaceAll("\\{","\\{\r\n");
		result=result.replaceAll("= \\{\r\n","= \\{");
		result=result.replaceAll("}\r\n;","};");
		result=result.replaceAll("}\r\n ;","};");
		return result;
	}

	/**
	 * qurong
	 * 2023.8.18
	 * 策略2，删除相关但出现在问题标识符修改右侧的作用域以及其后代作用域
	 * @param result
	 * @param relatedScope
	 * @return
	 */
	private String deleteRightScopes(String result, List<ScopeTree> relatedScope) {
		// TODO Auto-generated method stub
		
		return null;
	}

	/**
	 * qurong
	 * 2023.8.18
	 * 策略1，删除无关作用域以及其后代作用域
	 * @param programs
	 * @param relatedScope
	 * @return
	 */
	private String deleteUnrelatedScopes(String programs, List<ScopeTree> relatedScope) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * qurong
	 * 2023.8.18
	 * 根据问题标识符取出相关的标识符集合，相关的函数集合，相关作用域的集合
	 * @param problemIdentifier
	 * @param relatedSymbolRecord
	 * @param relatedScope
	 */
	private void getRelatedSymbolRecord(SymbolRecord problemIdentifier, List<SymbolRecord> relatedSymbolRecord,
			List<FunctionSymbolRecord> relatedFunSymbolRecord,List<ScopeTree> relatedScope,SymbolTable st) {
		// TODO Auto-generated method stub
		if(problemIdentifier==null) {
			return;
		}
		ScopeTree scopeTree=new ScopeTree();
		Queue<SymbolRecord> queueSymbolRecord=new LinkedList<SymbolRecord>();
		Queue<FunctionSymbolRecord> queueFunSymbolRecord=new LinkedList<FunctionSymbolRecord>();
		queueSymbolRecord.add(problemIdentifier);
		
		while((queueSymbolRecord!=null&&!queueSymbolRecord.isEmpty())) {
			SymbolRecord element=queueSymbolRecord.poll();
			if(element==null) {
				break;
			}
			relatedSymbolRecord.add(element);
			
			Map<Integer,Integer> scopeUsedTimeMap=element.getScopeUsedTimeMap();//当前标识符在各个可用作用域内的使用次数，<scopeId,次数>
		    
		    Map<Integer,List<SymbolRecord>> scopeModifiedIdentifiers=element.getScopeModifiedIdentifiers();//当前标识符的取值在各个可用作用域内的和哪些标识符相关，<scopeId,相关的标识符列表>
		    if(scopeModifiedIdentifiers!=null&&scopeModifiedIdentifiers.size()>0) {
		    	for(Map.Entry<Integer,List<SymbolRecord>> entry : scopeModifiedIdentifiers.entrySet()){
		    		Integer scopeId = entry.getKey();
		    		List<SymbolRecord> srs = entry.getValue();
		    		ScopeTree scopeNode=scopeTree.getScopeNodeById(st,scopeId);
		    		if(!relatedScope.contains(scopeNode)) {
		    			relatedScope.add(scopeNode);
		    		}
		    		
		    		if(srs!=null) {
		    			for(SymbolRecord sr:srs) {
		    				if(!relatedSymbolRecord.contains(sr)) {
		    					relatedSymbolRecord.add(sr);
		    				}
		    				if(!queueSymbolRecord.contains(sr)) {
		    					queueSymbolRecord.add(sr);
		    				}
		    			}
		    		}
		    		
	    		}
		    }
		    
		    Map<Integer,List<FunctionSymbolRecord>> scopeModifiedFuns=element.getScopeModifiedFuns();//当前标识符的取值在各个可用作用域内的和哪些函数标识符相关，<scopeId,相关的函数标识符列表>
		    if(scopeModifiedFuns!=null&&scopeModifiedFuns.size()>0) {
		    	for(Map.Entry<Integer,List<FunctionSymbolRecord>> entry : scopeModifiedFuns.entrySet()){
		    		Integer scopeId = entry.getKey();
		    		List<FunctionSymbolRecord> fsrs = entry.getValue();
		    		ScopeTree scopeNode=scopeTree.getScopeNodeById(st,scopeId);
		    		if(!relatedScope.contains(scopeNode)) {
		    			relatedScope.add(scopeNode);
		    		}
		    		
		    		if(fsrs!=null) {
		    			for(FunctionSymbolRecord fsr:fsrs) {
		    				if(!relatedFunSymbolRecord.contains(fsr)) {
		    					relatedFunSymbolRecord.add(fsr);
		    				}
		    				
		    				scopeNode=scopeTree.getScopeNodeById(st,fsr.getScope().getScopeID());
				    		if(!relatedScope.contains(scopeNode)) {
				    			relatedScope.add(scopeNode);
				    		}
		    				
		    				List<SymbolRecord> returnIdentifiers=fsr.getReturnIdentifiers();//当前函数返回值中包含的标识符
		    				for(SymbolRecord sr:returnIdentifiers) {
		    					
		    					scopeNode=scopeTree.getScopeNodeById(st,sr.getScope().getScopeID());
		    		    		if(!relatedScope.contains(scopeNode)) {
		    		    			relatedScope.add(scopeNode);
		    		    		}
		    					
			    				if(!relatedSymbolRecord.contains(sr)) {
			    					relatedSymbolRecord.add(sr);
			    				}
			    				
			    				if(!queueSymbolRecord.contains(sr)) {
			    					queueSymbolRecord.add(sr);
			    				}
			    			}
		    				
		    				if(!queueFunSymbolRecord.contains(fsr)) {
		    					queueFunSymbolRecord.add(fsr);
		    				}
		    			}
		    		}
	    		}
		    }
		}
		return;
	}



}
