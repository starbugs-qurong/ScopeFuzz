package anonymous.ac.grand.main.mutation;

import anonymous.ac.grand.main.Serializable;
import anonymous.ac.grand.main.antlr4.CPP14Parser;

import java.util.ArrayList;
import java.util.List;

public class CppForInsert {
    public static List<CppProgram> cppList = new ArrayList<CppProgram>();
    public static List<CPP14Parser.FunctionDefinitionContext> astFunList = new ArrayList<CPP14Parser.FunctionDefinitionContext>();

    /**
     * anonymousAuthor
     * 2024-12-30左右
     * 用于插入main-main代码片段的插入
     * @param filePath
     */
    public CppForInsert(String filePath){
        Serializable serializable=new Serializable();
        cppList=serializable.objectFromFileForSlice(filePath);
    }

    /**
     * anonymousAuthor
     * 2025-1-9
     * 用于变异插入的函数算子列表
     * @param filePath
     */
    public void getAstFunForInsert(String filePath){
        Serializable serializable=new Serializable();
        astFunList=serializable.objectFromFileForASTFunction(filePath);
    }

    public CppForInsert() {
    }

    public static List<CPP14Parser.FunctionDefinitionContext> getAstFunList() {
        return astFunList;
    }

    public static void setAstFunList(List<CPP14Parser.FunctionDefinitionContext> astFunList) {
        CppForInsert.astFunList = astFunList;
    }

    public List<CppProgram> getCppList() {
        return cppList;
    }

    public void setCppList(List<CppProgram> cppList) {
        this.cppList = cppList;
    }
}
