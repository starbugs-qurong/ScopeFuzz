package iscas.ac.grand.main.mutation;

import iscas.ac.grand.main.common.ScopeTree;

import java.util.ArrayList;
import java.util.List;

/**
 * qurong
 * 2024-12-18
 * 在对c++程序进行切片时，对main函数中的片段、外部的片段，内部的可插入位置的行号，外部的可插入位置的行号进行保存
 */
public class MainFunctionForSlice extends FunctionForSlice{
    private List<String> mainIn=new ArrayList<>();//main函数内部内容
    private List<String> mainOut=new ArrayList<>();//main函数外部内容
    private int pMainIn=0;//main函数内部可插入位置
    private int pMainOut=0;//main函数外部可插入位置
    private CppProgram cppProgram=null;//main函数所属的程序

    public MainFunctionForSlice(){
    }

    public MainFunctionForSlice(String functionBlock, ScopeTree rootScope){
        super(functionBlock,rootScope);
    }

    public List<String> getMainIn() {
        return mainIn;
    }

    public void setMainIn(List<String> mainIn) {
        this.mainIn = mainIn;
    }

    public List<String> getMainOut() {
        return mainOut;
    }

    public void setMainOut(List<String> mainOut) {
        this.mainOut = mainOut;
    }

    public int getpMainIn() {
        return pMainIn;
    }

    public int getpMainOut() {
        return pMainOut;
    }



    public void setpMainIn(int pMainIn) {
        this.pMainIn = pMainIn;
    }

    public void setpMainOut(int pMainOut) {
        this.pMainOut = pMainOut;
    }

    public CppProgram getCppProgram() {
        return cppProgram;
    }

    public void setCppProgram(CppProgram cppProgram) {
        this.cppProgram = cppProgram;
    }
}
