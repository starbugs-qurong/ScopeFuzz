package iscas.ac.grand.main.common;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class ScopeTree  implements Serializable{
    private int scopeID;//作用域ID
    private ScopeTree father;//父作用域
    private ScopeTree root;//根节点
    private List<ScopeTree> children=new ArrayList<>();//子作用域（可以多个）
    private String position="";//作用域位置编号
    private String scopeHead="";//作用域头
    private String scopeTail="";//作用域头
    private String scopeFragment="";//作用域对应的程序片段
    private String scopeBodyFragment="";//作用域对应的程序片段(含头尾)
    private int depth=0;
    private Type returnType;//当前作用域所在的返回值的类型,void也是一种类型
    private FunctionSymbolRecord fun;//当前作用域对应的函数
    private LamdaSymbolRecord lam;//当前作用域对应的Lamda表达式
    private Type locateType;//当前作用域所在的类
	private String scopeStatements="";//作用域花括号内的语句块，不含花括号 用于程序变异
	private String scopeStatementsWithBlock="";//作用域花括号内的语句块，含花括号 用于程序变异
	private String scopeStatementsWithComments="";//作用域花括号内的语句块，不含花括号,含插入点注释，在开始和结束位置 用于程序变异
	private String scopeStatementsWithCommentsAndBlock="";//作用域花括号内的语句块，含花括号,含插入点注释，在开始和结束位置 用于程序变异



    public ScopeTree(int scopeID, ScopeTree father) {
        this.scopeID = scopeID;
        this.father = father;
    }

	public ScopeTree(int scopeID, ScopeTree father,String scopeBodyFragment) {
		this.scopeID = scopeID;
		this.father = father;
		this.scopeBodyFragment=scopeBodyFragment;
	}

    public ScopeTree() {
    }

    public ScopeTree(int scopeID, ScopeTree father, String position, Type type, Type locateType) {
        this.scopeID = scopeID;
        this.father = father;
        this.position = position;
        this.returnType = type;
        this.locateType = locateType;
    }

    public int getScopeID() {
        return scopeID;
    }

    public void setScopeID(int scopeID) {
        this.scopeID = scopeID;
    }

    public ScopeTree getFather() {
        return father;
    }

    public void setFather(ScopeTree father) {
        this.father = father;
    }

    public List<ScopeTree> getChildren() {
        return children;
    }

    public void setChildren(List<ScopeTree> children) {
        this.children = children;
    }

    public ScopeTree getRoot() {
        return root;
    }

    public void setRoot(ScopeTree root) {
        this.root = root;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Type getType() {
        return returnType;
    }

    public void setType(Type type) {
        this.returnType = type;
    }

    public Type getLocateType() {
        return locateType;
    }

    public void setLocateType(Type locateType) {
        this.locateType = locateType;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

	public String getScopeHead() {
		return scopeHead;
	}

	public void setScopeHead(String scopeHead) {
		this.scopeHead = scopeHead;
	}

	public String getScopeTail() {
		return scopeTail;
	}

	public void setScopeTail(String scopeTail) {
		this.scopeTail = scopeTail;
	}

	public String getScopeFragment() {
		return scopeFragment;
	}

	public void setScopeFragment(String scopeFragment) {
		this.scopeFragment = scopeFragment;
	}

	public String getScopeBodyFragment() {
		return scopeBodyFragment;
	}

	public void setScopeBodyFragment(String scopeBodyFragment) {
		this.scopeBodyFragment = scopeBodyFragment;
	}
	/**
	 * qurong
	 * 2023.8.23
	 * 根据作用域id列表取出对应的作用域对象列表
	 * @param scopeNodeIds
	 * @param st
	 * @return
	 */
	public List<ScopeTree> getRelatedScopesObj(List<Integer> scopeNodeIds,SymbolTable st) {
		// TODO Auto-generated method stub
		List<ScopeTree> result=new ArrayList<ScopeTree>();
		for(int id:scopeNodeIds) {
			result.add(getScopeNodeById(st,id));
		}
		return result;
	}

	/**
	 * qurong
	 * 2023.8.18
	 * 遍历作用域树，根据作用域的ID取出作用域节点
	 * @param st
	 * @param scopeId
	 * @return
	 */
	public ScopeTree getScopeNodeById(SymbolTable st, Integer scopeId) {
		// TODO Auto-generated method stub
		if(st==null) {
			return null;
		}
		Map<Integer,ScopeTree> map=st.getScopeIdNodeMap();
		if(map==null||map.size()<=0) {
			setScopeIdNodeMap(st);
			map=st.getScopeIdNodeMap();
		}
		return map.get(scopeId);
	}
	
	public void setScopeIdNodeMap(SymbolTable st) {
		ScopeTree root=st.getRootScope();
		Map<Integer, ScopeTree> scopeIdNodeMap =new HashMap<>();
		if(root==null||root.getChildren()==null) {
			return;
		}else if(root.getChildren()==null||root.getChildren().size()<=0) {
			scopeIdNodeMap.put(root.getScopeID(), root);
		}
		//树的遍历
		Queue<ScopeTree> nodeList=new LinkedList<ScopeTree>();
		nodeList.add(root);
		while(!nodeList.isEmpty()) {
			ScopeTree first=nodeList.poll();
			scopeIdNodeMap.put(first.getScopeID(), first);
			List<ScopeTree> children=first.getChildren();
			if(children!=null&&children.size()>0) {
				nodeList.addAll(children);
			}
		}
		st.setScopeIdNodeMap(scopeIdNodeMap);
		return;
	}

	public List<ScopeTree> getLeafNodes(SymbolTable st) {
		// TODO Auto-generated method stub
		
		List<ScopeTree> result=st.getLeafNodes();
		if(result!=null&&result.size()>0) {
			return result;
		}
		result=new ArrayList<>();
		
		ScopeTree root=st.getRootScope();
		if(root==null) {
			return null;
		}else if(root.getChildren()==null||root.getChildren().size()<=0) {
			result.add(root);
			return result;
		}
		//树的遍历
		Queue<ScopeTree> nodeList=new LinkedList<ScopeTree>();
		nodeList.add(root);
		while(!nodeList.isEmpty()) {
			ScopeTree first=nodeList.poll();
			List<ScopeTree> children=first.getChildren();
			if(children!=null&&children.size()>0) {
				nodeList.addAll(children);
			}else if(children==null||children.size()<=0) {
				result.add(first);
			}
		}
		st.setLeafNodes(result);
		return result;
	}

	public FunctionSymbolRecord getFun() {
		return fun;
	}

	public void setFun(FunctionSymbolRecord fun) {
		this.fun = fun;
	}

	public LamdaSymbolRecord getLam() {
		return lam;
	}

	public void setLam(LamdaSymbolRecord lam) {
		this.lam = lam;
	}

	public String getScopeStatements() {
		return scopeStatements;
	}

	public void setScopeStatements(String scopeStatements) {
		this.scopeStatements = scopeStatements;
	}

	public String getScopeStatementsWithBlock() {
		return scopeStatementsWithBlock;
	}

	public void setScopeStatementsWithBlock(String scopeStatementsWithBlock) {
		this.scopeStatementsWithBlock = scopeStatementsWithBlock;
	}

	public String getScopeStatementsWithComments() {
		return scopeStatementsWithComments;
	}

	public void setScopeStatementsWithComments(String scopeStatementsWithComments) {
		this.scopeStatementsWithComments = scopeStatementsWithComments;
	}

	public String getScopeStatementsWithCommentsAndBlock() {
		return scopeStatementsWithCommentsAndBlock;
	}

	public void setScopeStatementsWithCommentsAndBlock(String scopeStatementsWithCommentsAndBlock) {
		this.scopeStatementsWithCommentsAndBlock = scopeStatementsWithCommentsAndBlock;
	}
}
