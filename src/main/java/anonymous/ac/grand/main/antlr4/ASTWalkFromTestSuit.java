package anonymous.ac.grand.main.antlr4;

import anonymous.ac.grand.main.Serializable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * anonymousAuthor
 * 2025-1-5
 * 解析源程序返回解析的AST树
 */
public class ASTWalkFromTestSuit {
    public static void main(String[] args) throws IOException {
        ASTWalkFromTestSuit awts = new ASTWalkFromTestSuit();
        awts.parserCpp();
    }
    public void parserCpp() throws IOException {
//        String directoryPath = "F:\\data-compiler-testing\\2024-12-16cpp-gcc-llvm-testsuit\\programs-deleted-failed";
        String directoryPath = "F:\\data-compiler-testing\\2024-12-16cpp-gcc-llvm-testsuit\\problem-cases";
        String outPutPath = "F:\\data-compiler-testing\\2024-12-16cpp-gcc-llvm-testsuit\\programs-deleted-failed-slice\\0109\\ast";
        List<CPP14Parser.FunctionDefinitionContext> cpList = new ArrayList<>();//函数列表
        ASTWalkFromTestSuit awts = new ASTWalkFromTestSuit();
        Serializable serializable=new Serializable();



        File directory = new File(directoryPath);

        // 获取目录下所有文件和文件夹
        File[] files = directory.listFiles();

        if (files != null) { // 确保文件数组不为空
            for (File file : files) {
                if (file.isFile()) { // 检查是否是文件
                    CPPParserASTListener cpd = new CPPParserASTListener();
                    String filePath = file.getAbsolutePath();
                    if(cpd.getFunctionByFile(filePath)==null){
                        System.out.println("faLse in parse file: "+file.getName());
                    }
                }
            }
        }


        serializable.objectToFileForASTFunction(CPPParserASTListener.getFunList(), outPutPath+"\\AStFunList.txt");

        cpList=serializable.objectFromFileForASTFunction( outPutPath+"\\AStFunList.txt");

        serializable.objectToFileForASTStatement(CPPParserASTListener.getStatementList(), outPutPath+"\\AStStatementList.txt");

        serializable.objectToFileForASTUnqualifiedId(CPPParserASTListener.getUnqualifiedIdList(), outPutPath+"\\AStUnqualifiedIdList.txt");

        serializable.objectToFileForASTQualifiedId(CPPParserASTListener.getQualifiedIdList(), outPutPath+"\\AStQualifiedIdList.txt");

        outPutFunctions(outPutPath,"Functions.txt");
        outPutStatements(outPutPath,"Statements.txt");
        outPutUnqualifiedIds(outPutPath,"UnqualifiedIds.txt");
        outPutQualifiedIds(outPutPath,"QualifiedIds.txt");

    }

    public void parserCpp(String sourcePath,String astPath) throws IOException {
//        String directoryPath = "F:\\data-compiler-testing\\2024-12-16cpp-gcc-llvm-testsuit\\programs-deleted-failed";
        String directoryPath =sourcePath;
        String outPutPath =astPath;
        List<CPP14Parser.FunctionDefinitionContext> cpList = new ArrayList<>();//函数列表
        ASTWalkFromTestSuit awts = new ASTWalkFromTestSuit();
        Serializable serializable=new Serializable();



        File directory = new File(directoryPath);

        // 获取目录下所有文件和文件夹
        File[] files = directory.listFiles();

        if (files != null) { // 确保文件数组不为空
            for (File file : files) {
                if (file.isFile()) { // 检查是否是文件
                    CPPParserASTListener cpd = new CPPParserASTListener();
                    String filePath = file.getAbsolutePath();
                    cpd.filePath=filePath;
                    if(cpd.getFunctionByFile(filePath)==null){
                        System.out.println("faLse in parse file: "+file.getName());
                    }
                }
            }
        }


        serializable.objectToFileForASTFunction(CPPParserASTListener.getFunList(), outPutPath+File.separator+"AStFunList.txt");


        serializable.objectToFileForASTStatement(CPPParserASTListener.getStatementList(), outPutPath+File.separator+"AStStatementList.txt");

        serializable.objectToFileForASTUnqualifiedId(CPPParserASTListener.getUnqualifiedIdList(), outPutPath+File.separator+"AStUnqualifiedIdList.txt");

        serializable.objectToFileForASTQualifiedId(CPPParserASTListener.getQualifiedIdList(), outPutPath+File.separator+"AStQualifiedIdList.txt");

        outPutFunctions(outPutPath,"Functions.txt");
        outPutStatements(outPutPath,"Statements.txt");
        outPutUnqualifiedIds(outPutPath,"UnqualifiedIds.txt");
        outPutQualifiedIds(outPutPath,"QualifiedIds.txt");

    }
    /**
     * anonymousAuthor
     * 2025-1-8
     * 输出修饰标识符列表
     * @param outPutPath
     * @param fileName
     */
    private void outPutQualifiedIds(String outPutPath, String fileName) {
        // 指定输出文件的路径
        String filePath = outPutPath+File.separator+fileName;
        // 使用try-with-resources语句来自动关闭BufferedWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 遍历列表
            for (CPP14Parser.QualifiedIdContext ctx : CPPParserASTListener.getQualifiedIdList()) {
//                writer.write("QualifiedId found: " + ctx.getText());
                writer.write( ctx.getText());
                writer.newLine(); // 换行
            }
        } catch (IOException e) {
            // 捕获并处理可能发生的IO异常
            System.err.println("写入文件时出错: " + e.getMessage());
        }
    }
    /**
     * anonymousAuthor
     * 2025-1-8
     * 输出未修饰标识符列表
     * @param outPutPath
     * @param fileName
     */
    private void outPutUnqualifiedIds(String outPutPath, String fileName) {
        // 指定输出文件的路径
        String filePath = outPutPath+File.separator+fileName;
        // 使用try-with-resources语句来自动关闭BufferedWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 遍历列表
            for (CPP14Parser.UnqualifiedIdContext ctx : CPPParserASTListener.getUnqualifiedIdList()) {
//                writer.write("UnqualifiedId found: " + ctx.getText());
                writer.write(ctx.getText());
                writer.newLine(); // 换行
            }
        } catch (IOException e) {
            // 捕获并处理可能发生的IO异常
            System.err.println("写入文件时出错: " + e.getMessage());
        }
    }
    /**
     * anonymousAuthor
     * 2025-1-8
     * 输出语句列表
     * @param outPutPath
     * @param fileName
     */
    private void outPutStatements(String outPutPath, String fileName) {
        // 指定输出文件的路径
        String filePath = outPutPath+File.separator+fileName;
        // 使用try-with-resources语句来自动关闭BufferedWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 遍历列表
            for (CPP14Parser.StatementContext ctx : CPPParserASTListener.getStatementList()) {
                writer.write( ctx.getText());
//                writer.write("Statement found: " + ctx.getText());
                writer.newLine(); // 换行
            }
        } catch (IOException e) {
            // 捕获并处理可能发生的IO异常
            System.err.println("写入文件时出错: " + e.getMessage());
        }
    }
    /**
     * anonymousAuthor
     * 2025-1-8
     * 输出函数列表
     * @param outPutPath
     * @param fileName
     */
    private void outPutFunctions(String outPutPath, String fileName) {
        // 指定输出文件的路径
        String filePath = outPutPath+File.separator+fileName;
        // 使用try-with-resources语句来自动关闭BufferedWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 遍历列表
            for (CPP14Parser.FunctionDefinitionContext ctx : CPPParserASTListener.getFunList()) {
//                writer.write("Function definition found: " + ctx.getText());
                writer.write(ctx.getText());
                writer.newLine(); // 换行
            }
        } catch (IOException e) {
            // 捕获并处理可能发生的IO异常
            System.err.println("写入文件时出错: " + e.getMessage());
        }
    }
}
