package iscas.ac.grand.main.mutation;

import iscas.ac.grand.main.common.ScopeTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * qurong
 * 2024-12-18
 * 用于保存每个作用域对应的程序片段的类
 */
public class ScopeBlockForSlice extends ScopeTree {
    private int startLineNum=0;//作用域开始行
    private int endLineNum=0;//作用域结束行
    private String scopeType="";//作用域类型 1main 2other
    private ScopeBlockForSlice fatherBlock;//父作用域块

    private List<ScopeBlockForSlice> blockChildren=new ArrayList<>();//子作用域（可以多个） 专用于切片外部程序 区别于Children


    private List<Integer> insertPositions=new ArrayList<>();//此作用域内可以插入其它作用域代码的位置（行号），可以是多个，先把作用域花括号{之后和}所在的行作为可插入行

    private CppProgram cppProgram=null;//作用域块所属的程序

    private String originalScopeStr="";//原本的作用域程序片段，和scopeBodyFragment含义是一样的
    private List<String> renameScopeStr=new ArrayList<>();//重命名后的作用域程序片段，除了重命名的标识符之外，程序的骨架和scopeBodyFragment是一样的

    private List<String> scopeStatementLines=new ArrayList<>();//作用域花括号内的语句块，不含花括号 用于程序变异 且是按行存储。

    private String fragmentWithSpace="";//程序片段 包含必要的空格

    private String renameStrByAST="";//根据AST树上的节点按序对标识符进行重命名

    private int[][] identifierRelation;//标识符关系数组，下标是标识符的序号

    private Map<String,String> renameMap=new HashMap<>();//标识符重命名关系map,<newName,origName>

    private List<int[]> RelationList=new ArrayList<>();//标识符关系列表 每个数组中包含三个元素 序号1 序号2 以及关系1或0

    public String getOriginalScopeStr() {
        return originalScopeStr;
    }

    public void setOriginalScopeStr(String originalScopeStr) {
        this.originalScopeStr = originalScopeStr;
    }

    public List<String> getRenameScopeStr() {
        return renameScopeStr;
    }

    public void setRenameScopeStr(List<String> renameScopeStr) {
        this.renameScopeStr = renameScopeStr;
    }

    public ScopeBlockForSlice(int scopeID, ScopeBlockForSlice fatherScope, String scopeBodyFragment){
        super(scopeID,fatherScope,scopeBodyFragment);
    }

    public ScopeBlockForSlice(String scopeBodyFragment){
        this.setScopeBodyFragment(scopeBodyFragment);
    }

    public ScopeBlockForSlice(String scopeBlock, ScopeTree fatherScope) {
        FunctionForSlice ffs= new FunctionForSlice();
        int id=ffs.generateUniqueID();
        this.setScopeID(id);
        this.setFather(fatherScope);
        this.setScopeBodyFragment(scopeBlock);
    }
    public ScopeBlockForSlice(){

    }

    public int getStartLineNum() {
        return startLineNum;
    }

    public void setStartLineNum(int startLineNum) {
        this.startLineNum = startLineNum;
    }

    public int getEndLineNum() {
        return endLineNum;
    }

    public void setEndLineNum(int endLineNum) {
        this.endLineNum = endLineNum;
    }

    public List<Integer> getInsertPositions() {
        return insertPositions;
    }

    public void setInsertPositions(List<Integer> insertPositions) {
        this.insertPositions = insertPositions;
    }

    public CppProgram getCppProgram() {
        return cppProgram;
    }

    public void setCppProgram(CppProgram cppProgram) {
        this.cppProgram = cppProgram;
    }

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public List<String> getScopeStatementLines() {
        return scopeStatementLines;
    }

    public void setScopeStatementLines(List<String> scopeStatementLines) {
        this.scopeStatementLines = scopeStatementLines;
    }

    public List<ScopeBlockForSlice> getBlockChildren() {
        return blockChildren;
    }

    public void setBlockChildren(List<ScopeBlockForSlice> blockChildren) {
        this.blockChildren = blockChildren;
    }

    public ScopeBlockForSlice getFatherBlock() {
        return fatherBlock;
    }

    public void setFatherBlock(ScopeBlockForSlice fatherBlock) {
        this.fatherBlock = fatherBlock;
    }

    public String getRenameStrByAST() {
        return renameStrByAST;
    }

    public void setRenameStrByAST(String renameStrByAST) {
        this.renameStrByAST = renameStrByAST;
    }

    public int[][] getIdentifierRelation() {
        return identifierRelation;
    }

    public void setIdentifierRelation(int[][] identifierRelation) {
        this.identifierRelation = identifierRelation;
    }

    public Map<String, String> getRenameMap() {
        return renameMap;
    }

    public void setRenameMap(Map<String, String> renameMap) {
        this.renameMap = renameMap;
    }

    public String getFragmentWithSpace() {
        return fragmentWithSpace;
    }

    public void setFragmentWithSpace(String fragmentWithSpace) {
        this.fragmentWithSpace = fragmentWithSpace;
    }

    public List<int[]> getRelationList() {
        return RelationList;
    }

    public void setRelationList(List<int[]> relationList) {
        RelationList = relationList;
    }
}
