package anonymous.ac.grand.main.mutation;


import anonymous.ac.grand.main.Serializable;
import anonymous.ac.grand.main.common.ScopeTree;
import anonymous.ac.grand.main.common.SymbolRecord;
import anonymous.ac.grand.main.common.Type;
import anonymous.ac.grand.main.common.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BraceMatcher {
    public static Map<String,List<String>> scopeMap=new HashMap<>();//作用域程序片段map，其中key代表作用域的类型，value代表这个类型下的作用域块代码片段
    public static Map<String,List<ScopeBlockForSlice>> scopeBlockMap=new HashMap<>();//作用域程序片段map，其中key代表作用域的类型，value代表这个类型下的作用域块
    public static List<FunctionForSlice> intFunctions=new ArrayList<>();//返回值为int类型的函数
    public static List<FunctionForSlice> voidFunctions=new ArrayList<>();//返回值为void类型的函数
    public static List<MainFunctionForSlice> mainFunctions=new ArrayList<>();//main函数
    public static ScopeTree rootScope=new ScopeTree(0,null);
    private static int lineCount=0;

    public static void main(String[] args) {
        BraceMatcher bm=new BraceMatcher();
        bm.sliceCpp();
//        bm.sliceFun();

}

    /**
     * anonymousAuthor
     * 2024-12-17
     * 对C++程序进行切片
     */
    private void sliceCpp() {

//        String directoryPath = "F:"+File.separator+"data-compiler-testing"+File.separator+"2024-12-16cpp-gcc-llvm-testsuit"+File.separator+"programs-deleted-failed";
//        String outPutPath = "F:"+File.separator+"data-compiler-testing"+File.separator+"2024-12-16cpp-gcc-llvm-testsuit"+File.separator+"programs-deleted-failed-slice"+File.separator+"0408";
        String directoryPath = "source";
        String outPutPath =  "main2main";
        List<CppProgram> cpList=new ArrayList<>();//程序列表，切片后
        BraceMatcher bm=new BraceMatcher();
        bm.initScopeMap();
        bm.initScopeBlockMap();

        File directory = new File(directoryPath);

        // 获取目录下所有文件和文件夹
        File[] files = directory.listFiles();

        if (files != null) { // 确保文件数组不为空
            for (File file : files) {
                if (file.isFile()) { // 检查是否是文件
                    CppProgram cp=sliceFile(file);
                    cpList.add(cp);
                }
            }
        }
        outPutScopeMap(outPutPath);
        outPutScopeBlockMap(outPutPath);
        outPutFunctions(outPutPath,"intFunctions.txt",intFunctions);
        outPutFunctions(outPutPath,"voidFunctions.txt",voidFunctions);
        outPutFunctionsMain(outPutPath,"mainFunctions.txt",mainFunctions);

        statisticsAllScopes(cpList,outPutPath+""+File.separator+"cppScopeNum.txt");//统计全部的作用域数量
        statisticsAllPrograms(cpList,outPutPath+""+File.separator+"cppProgramNum.txt");//统计全部的程序个数

        statisticsMainAvailableScopes(cpList,outPutPath+""+File.separator+"cppMainScopeNum.txt");//统计全部的作用域数量
        cpList=statisticsMainPrograms(cpList,outPutPath+""+File.separator+"cppMainProgram.txt");//统计main函数不为空的程序数量 并且过滤所有不包含main函数的源程序

        Serializable serializable=new Serializable();
        serializable.objectToFileForSlice(cpList, outPutPath+""+File.separator+"cppProgramList.txt");

        //outputAllScopeRename(cpList,outPutPath+""+File.separator+"scopeRename.txt");//对每个作用域中的标识符进行重命名并输出
    }

    /**
     * anonymousAuthor
     * 2024-12-26
     * 包含main且切分正确的程序个数
     * @param cpList
     * @param filePath
     * @return
     */
    private List<CppProgram> statisticsMainPrograms(List<CppProgram> cpList, String filePath) {
        int num=0;
        List<CppProgram> result=new ArrayList<>();
        if(cpList!=null&&cpList.size()>0){
            for(CppProgram cp:cpList){
                if(cp.getMainIn()!=null&&cp.getMainIn().size()>0){
                    num++;
                    result.add(cp);
                }
            }
        }
        //写入文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("main correct slice programs number : "+num );
                writer.newLine(); // 换行
        } catch (IOException e) {
            // 捕获并处理可能发生的IO异常
            System.err.println("写入文件时出错: " + e.getMessage());
        }
        return result;
    }

    /**
     * anonymousAuthor
     * 2024-12-26
     * 包含main且切分正确的程序个数
     * @param cpList
     * @param filePath
     */
    private void statisticsAllPrograms(List<CppProgram> cpList, String filePath) {
        int num=0;
        if(cpList!=null&&cpList.size()>0){
            for(CppProgram cp:cpList){
                num++;
            }
        }
        //写入文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("programs number : "+num );
            writer.newLine(); // 换行
            writer.write("program average line : "+(lineCount*1.0)/num );
            writer.newLine(); // 换行
        } catch (IOException e) {
            // 捕获并处理可能发生的IO异常
            System.err.println("写入文件时出错: " + e.getMessage());
        }
    }

    /**
     * anonymousAuthor
     * 2024-12-26
     * 统计main函数可用的程序中的作用域数量
     * @param cpList
     * @param filePath
     */
    private void statisticsMainAvailableScopes(List<CppProgram> cpList, String filePath) {
        int num=0;
        if(cpList!=null&&cpList.size()>0){
            for(CppProgram cp:cpList){
                if(cp.getMainIn()!=null&&cp.getMainIn().size()>0){
                    if(cp.getAllScopeBlocks()!=null&&cp.getAllScopeBlocks().size()>0){
                        num+=cp.getAllScopeBlocks().size();
                    }
                }
            }
        }
        //写入文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(" scope sliced in main available program number : "+num);
            writer.newLine(); // 换行
        } catch (IOException e) {
            // 捕获并处理可能发生的IO异常
            System.err.println("写入文件时出错: " + e.getMessage());
        }
    }

    /**
     * anonymousAuthor
     * 2024-12-26
     * 统计作用域数量
     * @param cpList
     * @param filePath
     */
    private void statisticsAllScopes(List<CppProgram> cpList, String filePath) {
        int num=0;
        if(cpList!=null&&cpList.size()>0){
            for(CppProgram cp:cpList){
                if(cp.getAllScopeBlocks()!=null&&cp.getAllScopeBlocks().size()>0){
                    num+=cp.getAllScopeBlocks().size();
                }
            }
        }
        //写入文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("scope sliced number : "+num);
            writer.newLine(); // 换行
            writer.write("scope average line : "+(lineCount*1.0)/num );
            writer.newLine(); // 换行
        } catch (IOException e) {
            // 捕获并处理可能发生的IO异常
            System.err.println("写入文件时出错: " + e.getMessage());
        }
    }

    /**
     * anonymousAuthor
     * 2024-12-26
     * 输出重命名后的作用域
     * @param cpList
     * @param filePath
     */
    private void outputAllScopeRename(List<CppProgram> cpList, String filePath) {
        String output="";
        if(cpList!=null&&cpList.size()>0){
            for(CppProgram cp:cpList){
                if(cp.getAllScopeBlocks()!=null&&cp.getAllScopeBlocks().size()>0){
                    for(ScopeBlockForSlice sbfs:cp.getAllScopeBlocks()){
                        IdentifierRenaming ir=new IdentifierRenaming();
                        List<VariableIdentifierInfo> vi=ir.getVariableIdentifierInfo(sbfs);

                        output+="original scope:\n"+sbfs.getOriginalScopeStr()+"\n";
                        if(sbfs.getRenameScopeStr()!=null&&sbfs.getRenameScopeStr().size()>0){
                            int i=1;
                            for(String rn:sbfs.getRenameScopeStr()){
                                output+="rename result\n"+(i++)+rn+"\n";
                            }
                        }

                        if(vi!=null){
                            for(VariableIdentifierInfo vii:vi){
                                output+=vii.getOriginalVar() + " (as " + vii.getNewName() + ") at line " + vii.getLineNumber()+"\n";
                            }
                        }
                    }
                }
            }
        }
        //写入文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("scope rename: "+output);
            writer.newLine(); // 换行
        } catch (IOException e) {
            // 捕获并处理可能发生的IO异常
            System.err.println("写入文件时出错: " + e.getMessage());
        }
    }


    /**
     * anonymousAuthor
     * 2024-12-18
     * 处理的类型为作用域块
     * @param scopeBlock
     */
    private static void scopeTyping(ScopeBlockForSlice scopeBlock) {
        String block=scopeBlock.getScopeBodyFragment();
        CppProgram cp=scopeBlock.getCppProgram();
        if(block.startsWith("struct")){
            List<ScopeBlockForSlice> blocks=scopeBlockMap.get("struct");
            blocks.add(scopeBlock);
            scopeBlockMap.put("struct",blocks);
            List<ScopeBlockForSlice> structs=cp.getStructList();
            ScopeBlockForSlice sbfs=new ScopeBlockForSlice(block,rootScope);
            structs.add(sbfs);
            cp.setStructList(structs);
        }else if(block.startsWith("template")){
            List<ScopeBlockForSlice> blocks=scopeBlockMap.get("template");
            blocks.add(scopeBlock);
            scopeBlockMap.put("template",blocks);
            List<ScopeBlockForSlice> templates=cp.getTemplateList();
            ScopeBlockForSlice sbfs=new ScopeBlockForSlice(block,rootScope);
            templates.add(sbfs);
            cp.setTemplateList(templates);
        }else if(block.startsWith("while")){
            List<ScopeBlockForSlice> blocks=scopeBlockMap.get("while");
            blocks.add(scopeBlock);
            scopeBlockMap.put("while",blocks);
            List<ScopeBlockForSlice> whiles=cp.getWhileList();
            ScopeBlockForSlice sbfs=new ScopeBlockForSlice(block,rootScope);
            whiles.add(sbfs);
            cp.setWhileList(whiles);
        }else if(block.startsWith("for")){
            List<ScopeBlockForSlice> blocks=scopeBlockMap.get("for");
            blocks.add(scopeBlock);
            scopeBlockMap.put("for",blocks);
            List<ScopeBlockForSlice> fors=cp.getForList();
            ScopeBlockForSlice sbfs=new ScopeBlockForSlice(block,rootScope);
            fors.add(sbfs);
            cp.setForList(fors);
        }else if(block.startsWith("if")){
            List<ScopeBlockForSlice> blocks=scopeBlockMap.get("if");
            blocks.add(scopeBlock);
            scopeBlockMap.put("if",blocks);
            List<ScopeBlockForSlice> ifs=cp.getIfList();
            ScopeBlockForSlice sbfs=new ScopeBlockForSlice(block,rootScope);
            ifs.add(sbfs);
            cp.setIfList(ifs);
        }else if(block.startsWith("void")){//更多function的匹配方法还需要再细化
            List<ScopeBlockForSlice> blocks=scopeBlockMap.get("function");
            blocks.add(scopeBlock);
            scopeBlockMap.put("function",blocks);
            FunctionForSlice fsr=new FunctionForSlice(block,rootScope);
            fsr.setCppProgram(cp);
            voidFunctions.add(fsr);
            List<FunctionForSlice> funs=cp.getFunctionList();
            funs.add(fsr);
            cp.setFunctionList(funs);
        }else if(block.startsWith("int")){//更多赋值语句的匹配方法还需要再细化//其实匹配到的是返回值类型为int的函数
            if(block.startsWith("int main")){
                List<ScopeBlockForSlice> blocks=scopeBlockMap.get("mainFunction");
                blocks.add(scopeBlock);
                scopeBlockMap.put("mainFunction",blocks);
                MainFunctionForSlice mfsr= new MainFunctionForSlice(block,rootScope);
                mfsr.setCppProgram(cp);
                mfsr.setMainOut(cp.getMainOut());
                mfsr.setMainIn(cp.getMainIn());
                mainFunctions.add(mfsr);
                List<MainFunctionForSlice> mainFuns=cp.getMainFunctionList();
                mainFuns.add(mfsr);
                cp.setMainFunctionList(mainFuns);
            }else if(block.contains("main()")){
                System.out.println("main but not in format: "+block);
            }else{//不是main函数 是普通的返回值类型为int的函数
                List<ScopeBlockForSlice> blocks=scopeBlockMap.get("intFunction");
                blocks.add(scopeBlock);
                scopeBlockMap.put("intFunction",blocks);
                FunctionForSlice fsr=new FunctionForSlice(block,rootScope);
                fsr.setCppProgram(cp);
                intFunctions.add(fsr);
                List<FunctionForSlice> funs=cp.getFunctionList();
                funs.add(fsr);
                cp.setFunctionList(funs);
            }
        }else if(block.contains("include")){//需要细化
            List<ScopeBlockForSlice> blocks=scopeBlockMap.get("include");
            blocks.add(scopeBlock);
            scopeBlockMap.put("include",blocks);
            List<String> includes=cp.getIncludeList();
            if(includes==null)includes=new ArrayList<>();
            includes.add(block);
            cp.setIncludeList(includes);
        }
    }

    private void initScopeMap() {
        scopeMap.put("include",new ArrayList<>());
        scopeMap.put("template",new ArrayList<>());
        scopeMap.put("struct",new ArrayList<>());
        scopeMap.put("while",new ArrayList<>());
        scopeMap.put("for",new ArrayList<>());
        scopeMap.put("if",new ArrayList<>());
        scopeMap.put("assignment",new ArrayList<>());//暂时没有，因为是作用域level的
        scopeMap.put("intFunction",new ArrayList<>());
        scopeMap.put("mainFunction",new ArrayList<>());
        scopeMap.put("function",new ArrayList<>());
    }

    private void initScopeBlockMap() {
        scopeBlockMap.put("include",new ArrayList<>());
        scopeBlockMap.put("template",new ArrayList<>());
        scopeBlockMap.put("struct",new ArrayList<>());
        scopeBlockMap.put("while",new ArrayList<>());
        scopeBlockMap.put("for",new ArrayList<>());
        scopeBlockMap.put("if",new ArrayList<>());
        scopeBlockMap.put("assignment",new ArrayList<>());//暂时没有，因为是作用域level的
        scopeBlockMap.put("intFunction",new ArrayList<>());
        scopeBlockMap.put("mainFunction",new ArrayList<>());
        scopeBlockMap.put("function",new ArrayList<>());
    }



    /**
     * anonymousAuthor
     * 2024-12-13
     * 对指定路径对应的程序进行切片
     * @param file
     */
    private static CppProgram sliceFile(File file) {
//        String filePath = "F:"+File.separator+"data-compiler-testing"+File.separator+"slice.cpp"; // 替换为你的文件路径
        String filePath = file.getAbsolutePath();
        String fileName=file.getName();
        List<String> matchedBlocks = new ArrayList<>();
        List<String> matchedBlocksContent = new ArrayList<>();//不含花括号 用于次级作用域切片
        List<String> mainOut = new ArrayList<>();//main函数之外的程序内容
        boolean mainOutFlag=true;
        List<String> mainIn = new ArrayList<>();//main函数内的程序内容
        boolean mainInFlag=false;
        List<ScopeBlockForSlice> matchedScopeBlocks = new ArrayList<>();//除了程序片段本身，也记录作用域的行号等信息

        StringBuilder programStr=new StringBuilder("");//程序对应的内容
        List<String> programLines=new ArrayList<>();//程序对应的行
        CppProgram cp=new CppProgram(rootScope);//每个程序对应一个程序类型的对象
        cp.setCppFileName(fileName);//记录该程序的名字
        cp.setCppFilePath(filePath);//记录该程序的路径



        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder currentBlock = null;
            int openBraceCount = 0;
            String line;
            String preLine=new String("");
            int startLineIndex=0;//花括号起始行
            int endLineIndex=0;//花括号结束后
            int lineIndex=0;//当前读取行
            int firstPosition=0;//花括号{后一行
            int lastPosition=0;//花括号}对应的行
            int mainLineIndex=0;//main函数中第几行
            String scopeType=new String("2");//作用域类型默认为非main函数作用域

            while ((line = reader.readLine()) != null) {
                lineCount++;
                //不保留注释
                line=line.trim();
                if(line=="") {
                    continue;//忽略这一个空行
                }
                //忽略注释行
                if(line.startsWith("//")||line.startsWith("/*")){
                    String validCOn=deleteCommentsLine(line,reader);
                    if(validCOn!=null&&validCOn!=""){
                        line=validCOn;
                    }
                    else{
                        continue;
                    }
                }
                line=deleteComments(line);//删除行尾注释

                //如果要开始切分main函数
                if(line.startsWith("int main")&&mainOutFlag&&currentBlock==null){
                    //增加main-out位置注释
                    currentBlock=new StringBuilder("");
                    currentBlock.append("//insertion-global-available\n");
                    programStr.append("//insertion-global-available\n").append("\n");
                    programLines.add("//insertion-global-available\n");

                    cp.setMainOut(mainOut);//把mainout和include语句拆分开来
                    mainOutFlag=false;
                }
                if(mainOutFlag){mainOut.add(line.trim());}


                boolean startLine=false;
                line=line.trim();
                char[] chars = line.toCharArray();
                lineIndex++;
                String mainFirstLine =new String("");//main函数中第一个语句，可能跟在{同一行，需要拆分出来
                boolean mainFirstLineFlag=false;
                int mainIndex=0;
                String lineWithCommnets=line;//在行首或尾添加注释标记用于插入程序片段的位置
                for (char c : chars) {

                    if (c == '{') {
                        openBraceCount++;
                        if (openBraceCount == 1) {
                            lineWithCommnets=addComments(line,"{","//insertion-scope-first-available\n//insertion-scope-first-statement-available\n");
                            // 开始一个新的块
                            //如果该行以花括号开始，把上一行也添加到当前块
                            if(line.startsWith("{")){
                                currentBlock = new StringBuilder(preLine).append("\n");
                                currentBlock.append(lineWithCommnets).append("\n");
                                startLineIndex=lineIndex-1;//从上一行就开始了
                            }else{
                                currentBlock = new StringBuilder(lineWithCommnets).append("\n");
                                startLineIndex=lineIndex;//从当前行开始
                            }

                            if(!mainOutFlag){//main函数开启
                                lineWithCommnets=addComments(lineWithCommnets,"{","//insertion-main-first-available\n");
                                scopeType="1";
                                mainInFlag=true;
                                mainFirstLine=new String("");
                                mainFirstLineFlag=true;
                                mainIndex=1;
                                mainLineIndex=1;
                            }

                            startLine=true;
                            firstPosition=lineIndex+1;//花括号{后一行
                            matchedBlocksContent=new ArrayList();

                        }
                    } else if (c == '}') {
                        openBraceCount--;
                        if (openBraceCount == 0) {
                            lineWithCommnets=addComments(lineWithCommnets,"}","//insertion-scope-last-available\n//insertion-scope-last-statement-available\n");
                            // 结束当前的块
                            endLineIndex=lineIndex;
                            if(endLineIndex>startLineIndex){//避免花括号起始结束都在一行 会重复添加
                                currentBlock.append(lineWithCommnets).append("\n");
                                lastPosition=endLineIndex;//花括号}对应的行
                            }else{
                                lastPosition=-1;//花括号}
                                firstPosition=-1;//花括号{后一行，-1代表不可插入
                            }
                            matchedBlocks.add(currentBlock.toString());

                            if(mainInFlag){
                                mainInFlag=false;
                                mainIn.add(mainFirstLine);
                                lineWithCommnets=addComments(lineWithCommnets,"}","//insertion-main-last-available\n");
                            }

                            if(mainFirstLineFlag){
                                mainIn.add(mainFirstLine);
                                mainFirstLineFlag=false;
                            }


                            ScopeBlockForSlice sbfs=new ScopeBlockForSlice(currentBlock.toString(),cp.getProgramScope());//更新代码片段对应的作用域及其行号信息
                            sbfs.setScopeStatementLines(matchedBlocksContent);//次级作用域相关
                            sbfs.setScopeStatements(currentBlock.toString().substring(currentBlock.toString().indexOf("{")+1,currentBlock.toString().lastIndexOf("}")));//次级作用域相关
                            sbfs.setStartLineNum(startLineIndex);
                            sbfs.setEndLineNum(endLineIndex);
                            List<Integer> insertPositions=new ArrayList<>();
                            if(firstPosition>0)insertPositions.add(firstPosition);
                            if(lastPosition>0)insertPositions.add(lastPosition);
                            sbfs.setInsertPositions(insertPositions);
                            sbfs.setCppProgram(cp);//把作用域和程序关联起来
                            sbfs.setScopeType(scopeType);//作用域是main还是其它
                            List<ScopeBlockForSlice> children=sliceProgramByStr(sbfs.getScopeStatementLines(),cp);//次级作用域相关 递归调用
                            if(children!=null){
                                matchedScopeBlocks.addAll(children);//次级作用域相关
                                sbfs.setBlockChildren(children);//次级作用域相关
                                for(ScopeBlockForSlice child:children){//将父子作用域关联起来
                                    child.setFatherBlock(sbfs);
                                }
                            }
                            matchedScopeBlocks.add(sbfs);//更新该作用域到匹配列表里

                            List<ScopeBlockForSlice> allScopeBlocks=cp.getAllScopeBlocks();//更新该作用域到程序cp上
                            allScopeBlocks.add(sbfs);
                            cp.setAllScopeBlocks(allScopeBlocks);

                            currentBlock = null;
                            scopeType=new String("2");//作用域类型重置为非main函数作用域
                            matchedBlocksContent=new ArrayList<>();//次级作用域相关

                        }
                    }

                    if(mainFirstLineFlag){//控制main花括号后紧跟着的第一行的分割
                        if (mainIndex!=1){
                            mainFirstLine=mainFirstLine+c;
                        }
                        mainIndex++;
                    }
                }

                // 如果当前在块中，但不是块的开始或结束行，则添加该行到当前块
                if(!startLine){
                    if (currentBlock != null && openBraceCount > 0) {
                        currentBlock.append(line).append("\n");
                        matchedBlocksContent.add(line);
                    }
                }

                if(mainInFlag){//控制main函数中第一行的分割
                    if (mainLineIndex!=1) {
                        mainIn.add(line.trim());
                    }
                    mainLineIndex++;
                }

                preLine=line;

                programStr.append(lineWithCommnets).append("\n");
                programLines.add(lineWithCommnets);

            }



            if(mainIn!=null&&mainIn.size()>0){
                cp.setMainIn(mainIn);
            }
            // 检查是否有未闭合的块
            if (openBraceCount != 0) {
                System.err.println("Error: Unmatched braces found in the input file.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        cp.setProgramStr(programStr);
        cp.setProgramLines(programLines);

        // 输出结果
//        for (String block : matchedBlocks) {
//            System.out.println(block);
//            scopeTyping(block,cp);//为每个程序块区分作用域类型
//        }

        for (ScopeBlockForSlice scopeBlock : matchedScopeBlocks) {
            System.out.println(scopeBlock.getScopeBodyFragment());
            scopeTyping(scopeBlock);//为每个程序块区分作用域类型
        }
        return cp;
    }


    /**
     * anonymousAuthor
     * 2024-12-13
     * 对程序内容进行切片
     * @param fileLines
     * @param cp
     */
    private static List<ScopeBlockForSlice> sliceProgramByStr(List<String> fileLines,CppProgram cp) {

        List<String> matchedBlocks = new ArrayList<>();
        List<String> matchedBlocksContent = new ArrayList<>();//不含花括号 用于次级作用域切片
        List<String> mainOut = new ArrayList<>();//main函数之外的程序内容
        boolean mainOutFlag=true;
        List<String> mainIn = new ArrayList<>();//main函数内的程序内容
        boolean mainInFlag=false;
        List<ScopeBlockForSlice> matchedScopeBlocks = null;//除了程序片段本身，也记录作用域的行号等信息

        StringBuilder programStr=new StringBuilder("");//程序对应的内容
        List<String> programLines=new ArrayList<>();//程序对应的行

        if (fileLines!=null&&fileLines.size()>2) {
            matchedScopeBlocks = new ArrayList<>();//除了程序片段本身，也记录作用域的行号等信息
            StringBuilder currentBlock = null;
            int openBraceCount = 0;
            String line;
            String preLine=new String("");
            int startLineIndex=0;//花括号起始行
            int endLineIndex=0;//花括号结束后
            int lineIndex=0;//当前读取行
            int firstPosition=0;//花括号{后一行
            int lastPosition=0;//花括号}对应的行
            int mainLineIndex=0;//main函数中第几行
            String scopeType=new String("2");//作用域类型默认为非main函数作用域
            int i=0;
            line = fileLines.get(i);
            while (line!=null) {
                i++;
                //不保留注释
                line=line.trim();
                if(line=="") {
                    continue;//忽略这一个空行
                }

                //如果要开始切分main函数
                if(line.startsWith("int main")&&mainOutFlag&&currentBlock==null){
                    //增加main-out位置注释
                    currentBlock=new StringBuilder("");
                    currentBlock.append("//insertion-global-available\n");
                    programStr.append("//insertion-global-available\n").append("\n");
                    programLines.add("//insertion-global-available\n");

                    cp.setMainOut(mainOut);//把mainout和include语句拆分开来
                    mainOutFlag=false;
                }
                if(mainOutFlag){mainOut.add(line.trim());}


                boolean startLine=false;
                line=line.trim();
                char[] chars = line.toCharArray();
                lineIndex++;
                String mainFirstLine =new String("");//main函数中第一个语句，可能跟在{同一行，需要拆分出来
                boolean mainFirstLineFlag=false;
                int mainIndex=0;
                String lineWithCommnets=line;//在行首或尾添加注释标记用于插入程序片段的位置
                for (char c : chars) {

                    if (c == '{') {
                        openBraceCount++;
                        if (openBraceCount == 1) {
                            lineWithCommnets=addComments(line,"{","//insertion-scope-first-available\n//insertion-scope-first-statement-available\n");
                            // 开始一个新的块
                            //如果该行以花括号开始，把上一行也添加到当前块
                            if(line.startsWith("{")){
                                currentBlock = new StringBuilder(preLine).append("\n");
                                currentBlock.append(lineWithCommnets).append("\n");
                                startLineIndex=lineIndex-1;//从上一行就开始了
                            }else{
                                currentBlock = new StringBuilder(lineWithCommnets).append("\n");
                                startLineIndex=lineIndex;//从当前行开始
                            }

                            if(!mainOutFlag){//main函数开启
                                lineWithCommnets=addComments(lineWithCommnets,"{","//insertion-main-first-available\n");
                                scopeType="1";
                                mainInFlag=true;
                                mainFirstLine=new String("");
                                mainFirstLineFlag=true;
                                mainIndex=1;
                                mainLineIndex=1;
                            }

                            startLine=true;
                            firstPosition=lineIndex+1;//花括号{后一行
                            matchedBlocksContent=new ArrayList();

                        }
                    } else if (c == '}') {
                        openBraceCount--;
                        if (openBraceCount == 0) {
                            lineWithCommnets=addComments(lineWithCommnets,"}","//insertion-scope-last-available\n//insertion-scope-last-statement-available\n");
                            // 结束当前的块
                            endLineIndex=lineIndex;
                            if(endLineIndex>startLineIndex){//避免花括号起始结束都在一行 会重复添加
                                currentBlock.append(lineWithCommnets).append("\n");
                                lastPosition=endLineIndex;//花括号}对应的行
                            }else{
                                lastPosition=-1;//花括号}
                                firstPosition=-1;//花括号{后一行，-1代表不可插入
                            }
                            matchedBlocks.add(currentBlock.toString());

                            if(mainInFlag){
                                mainInFlag=false;
                                mainIn.add(mainFirstLine);
                                lineWithCommnets=addComments(lineWithCommnets,"}","//insertion-main-last-available\n");
                            }

                            if(mainFirstLineFlag){
                                mainIn.add(mainFirstLine);
                                mainFirstLineFlag=false;
                            }

                            ScopeBlockForSlice sbfs=new ScopeBlockForSlice(currentBlock.toString(),cp.getProgramScope());//更新代码片段对应的作用域及其行号信息
                            sbfs.setScopeStatementLines(matchedBlocksContent);//次级作用域相关
                            sbfs.setScopeStatements(currentBlock.toString().substring(currentBlock.toString().indexOf("{")+1,currentBlock.toString().lastIndexOf("}")));//次级作用域相关
                            sbfs.setStartLineNum(startLineIndex);
                            sbfs.setEndLineNum(endLineIndex);
                            List<Integer> insertPositions=new ArrayList<>();
                            if(firstPosition>0)insertPositions.add(firstPosition);
                            if(lastPosition>0)insertPositions.add(lastPosition);
                            sbfs.setInsertPositions(insertPositions);
                            sbfs.setCppProgram(cp);//把作用域和程序关联起来
                            sbfs.setScopeType(scopeType);//作用域是main还是其它
                            List<ScopeBlockForSlice> children=sliceProgramByStr(sbfs.getScopeStatementLines(),cp);//次级作用域相关 递归调用
                            if(children!=null){
                                matchedScopeBlocks.addAll(children);//次级作用域相关
                                sbfs.setBlockChildren(children);//次级作用域相关
                                for(ScopeBlockForSlice child:children){//将父子作用域关联起来
                                    child.setFatherBlock(sbfs);
                                }
                            }

                            matchedScopeBlocks.add(sbfs);//更新该作用域到匹配列表里

                            List<ScopeBlockForSlice> allScopeBlocks=cp.getAllScopeBlocks();//更新该作用域到程序cp上
                            allScopeBlocks.add(sbfs);
                            cp.setAllScopeBlocks(allScopeBlocks);

                            currentBlock = null;
                            scopeType=new String("2");//作用域类型重置为非main函数作用域
                            matchedBlocksContent=new ArrayList<>();//次级作用域相关
                        }
                    }

                    if(mainFirstLineFlag){//控制main花括号后紧跟着的第一行的分割
                        if (mainIndex!=1){
                            mainFirstLine=mainFirstLine+c;
                        }
                        mainIndex++;
                    }
                }

                // 如果当前在块中，但不是块的开始或结束行，则添加该行到当前块
                if(!startLine){
                    if (currentBlock != null && openBraceCount > 0) {
                        currentBlock.append(line).append("\n");
                        matchedBlocksContent.add(line);
                    }
                }

                if(mainInFlag){//控制main函数中第一行的分割
                    if (mainLineIndex!=1) {
                        mainIn.add(line.trim());
                    }
                    mainLineIndex++;
                }

                preLine=line;

                programStr.append(lineWithCommnets).append("\n");
                programLines.add(lineWithCommnets);
                if(i>=fileLines.size()){
                    break;
                }else{
                    line = fileLines.get(i);
                }
            }



            if(mainIn!=null&&mainIn.size()>0){
                cp.setMainIn(mainIn);
            }
            // 检查是否有未闭合的块
            if (openBraceCount != 0) {
                System.err.println("Error: Unmatched braces found in the input file.");
            }

        }

        cp.setProgramStr(programStr);
        cp.setProgramLines(programLines);

        // 输出结果
//        for (String block : matchedBlocks) {
//            System.out.println(block);
//            scopeTyping(block,cp);//为每个程序块区分作用域类型
//        }
        if(matchedScopeBlocks!=null) {
            for (ScopeBlockForSlice scopeBlock : matchedScopeBlocks) {
                System.out.println(scopeBlock.getScopeBodyFragment());
                scopeTyping(scopeBlock);//为每个程序块区分作用域类型
            }
        }
        return matchedScopeBlocks;
    }

    /**
     * anonymousAuthor
     * 2024-12-26
     * 删除行末注释//
     * @param str
     * @return
     */
    public static String deleteComments(String str){
        String result=str;
        if(str.contains("//")&&!str.contains("'//'")){
            int index=str.lastIndexOf("//");
            result=str.substring(0,str.lastIndexOf("//")>1?str.lastIndexOf("//"):str.length());
            result=result.trim();
        }
        else if(str.contains("/*")){
            int index=str.lastIndexOf("/*");
            result=str.substring(0,str.lastIndexOf("/*")>1?str.lastIndexOf("/*"):str.length());
            result=result.trim();
        }
        return result;
    }


    /**
     * 删除从当前行开始的空行，单行注释，以及多行注释，返回文件读取的位置
     * 这里完全是copy了读取grammar时的注释处理方法（GrammarGetService里的deleteCommentsLine）
     * anonymousAuthor
     * 2024-12-26
     * @param line
     * @param fileReader
     * @return
     * @throws IOException
     */
    public static String deleteCommentsLine(String line, BufferedReader fileReader) throws IOException{
        //确定是否为单注释行
        if(line.equals("")||line.startsWith("//")) {
            return null;//忽略这一行注释
        }
        //确定是否为多行注释
        if(line.startsWith("/*")){
            if(line.endsWith("*/")){
                //多行注释在一行中结束了
                return null;
            }
            if(line.contains("*/")){
                String[] con=line.split("\\*/");
                if(con.length==2){
                    return con[1].trim();
                }else{
                    //一行中有两个*/注释结束符号
                    //System.out.println(line+": 2 ‘*/’ in this line ");
                }
            }
            String comments=line;
            //向后继续读入，直到多行注释结束
            line = fileReader.readLine();
            while (line!= null){
                line=line.trim();
                if(!line.endsWith("*/")&&!line.contains("*/")){
                    comments+="/n"+line;
                }else{
                    if(line.contains("*/")&&!line.endsWith("*/")){
                        //System.out.println(line+": the content after this comment can't be processed ");
                        String[] con=line.split("\\*/");
                        if(con.length==2){
                            //System.out.println(line+": there are some contents after ‘*/’ in this line ");
                            return con[1].trim();
                        }else{
                            //一行中有两个*/注释结束符号
                            //System.out.println(line+": 2 ‘*/’ in this line ");
                        }
                        return "";
                    }
                    return "";
                }
                line = fileReader.readLine();
            }
            if(line==null){
                //读取结束没有匹配到多行注释的结束符号*/
                //System.out.println(comments+": no */ to match");
                return null;
            }
            return "";
        }
        return "";
    }

    /**
     * 2024-12-23
     * anonymousAuthor
     * 在一行程序中花括号{后，以及}前，加入str字符串
     * @param line
     * @param startOrEnd
     * @param str
     * @return
     */
    private static String addComments(String line, String startOrEnd, String str) {
        if (line == null || str == null ) {
            return line;
        }
        int firstLeftBraceIndex=0;
        int lastRightBraceIndex=0;

        // Find the index of the first '{' and the last '}'
        if(startOrEnd.equals("{")||startOrEnd=="{"){
            firstLeftBraceIndex = line.indexOf('{');
        }
        if(startOrEnd.equals("}")||startOrEnd=="}") {
            lastRightBraceIndex = line.lastIndexOf('}');
        }

        // If either '{' or '}' is not found, throw an exception or handle it accordingly
        if (firstLeftBraceIndex == -1 || lastRightBraceIndex == -1) {
            return line;
        }

        // Create the new string by inserting str1 after the first '{' and str2 before the last '}'
        StringBuilder sb = new StringBuilder(line);
        if(startOrEnd.equals("{")||startOrEnd=="{") {
            sb.insert(firstLeftBraceIndex + 1, str);
        }
        if(startOrEnd.equals("}")||startOrEnd=="}") {
            sb.insert(lastRightBraceIndex, str);
        }

        return sb.toString();
    }

    private void outPutScopeBlockMap(String outPutPath) {
        // 遍历Map，并将每个键值对写入到单独的txt文件中
        for (Map.Entry<String, List<ScopeBlockForSlice>> entry : scopeBlockMap.entrySet()) {
            String key = entry.getKey();
            List<ScopeBlockForSlice> value = entry.getValue();

            // 创建文件名，这里简单使用键名作为文件名
            String fileName = outPutPath+""+File.separator+""+key + "scopeBlock.txt";
//            String fileName ="F:"+File.separator+"data-compiler-testing"+File.separator+"2024-12-16cpp-gcc-llvm-testsuit"+File.separator+"gcc-testsuit"+File.separator+"template.txt";
            File file = new File(fileName);

            // 使用try-with-resources语句来自动关闭BufferedWriter
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                // 写入键
                writer.write("Key: " + key);
                writer.newLine();

                // 写入值（List<String>），遍历List并写入每个元素
                for (ScopeBlockForSlice item : value) {
                    writer.write("程序内容："+item.getCppProgram().getProgramStr());
                    writer.newLine();
                    writer.write("起始行号："+item.getStartLineNum());
                    writer.newLine();
                    writer.write("结束行号："+item.getEndLineNum());
                    writer.newLine();
                    int j=0;
                    for(int i:item.getInsertPositions()){
                        writer.write("可插入行号"+(j++)+":"+i);
                        writer.newLine();
                    }
                    writer.newLine();
                }
                // 可选：添加一个空行或分隔符来区分不同的键值对（如果文件被连续写入）
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Error writing to file " + fileName);
                e.printStackTrace();
            }
        }
    }

    private static void outPutScopeMap(String outPutPath) {
        // 遍历Map，并将每个键值对写入到单独的txt文件中
        for (Map.Entry<String, List<String>> entry : scopeMap.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();

            // 创建文件名，这里简单使用键名作为文件名
            String fileName = outPutPath+""+File.separator+""+key + ".txt";
//            String fileName ="F:"+File.separator+"data-compiler-testing"+File.separator+"2024-12-16cpp-gcc-llvm-testsuit"+File.separator+"gcc-testsuit"+File.separator+"template.txt";
            File file = new File(fileName);

            // 使用try-with-resources语句来自动关闭BufferedWriter
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                // 写入键
                writer.write("Key: " + key);
                writer.newLine();

                // 写入值（List<String>），遍历List并写入每个元素
                for (String item : value) {
                    writer.write( item);
                    writer.newLine();
                }
                // 可选：添加一个空行或分隔符来区分不同的键值对（如果文件被连续写入）
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Error writing to file " + fileName);
                e.printStackTrace();
            }
        }
    }

    /**
     * anonymousAuthor
     * 2024-12-17
     * 输出所有函数
     * @param outPutPath
     */
    private static void outPutFunctions(String outPutPath,String fileName,List<FunctionForSlice> functions) {
        // 指定输出文件的路径
        String filePath = outPutPath+""+File.separator+""+fileName;

        // 使用try-with-resources语句来自动关闭BufferedWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 遍历学生列表
            for (FunctionForSlice fsr : functions) {
                if(fsr.getName()!=null&&fsr.getID()!=null&&fsr.getReturnType()!=null){
                    writer.write("ID: " + fsr.getID() );
                    writer.newLine(); // 换行
                    writer.write("Name: " + fsr.getName());
                    writer.newLine(); // 换行
                    writer.write("returnType: "+fsr.getReturnType().getTypeName());
                    writer.newLine(); // 换行
                }
                if(fsr.getParameterList()!=null){
                    for (Type t : fsr.getParameterList()) {
                        writer.write("parameterType: " + t.getTypeName() );
                        writer.newLine(); // 换行
                    }
                }
                if(fsr.getParameterIdentifiers()!=null){
                    for (SymbolRecord sr : fsr.getParameterIdentifiers()) {
                        writer.write("parameterName: " + sr.getName() );
                        writer.newLine(); // 换行
                    }
                }
                writer.write(fsr.getFunctionBody());
                writer.newLine(); // 换行
            }
            //System.out.println("学生信息已成功写入文件: " + filePath);
        } catch (IOException e) {
            // 捕获并处理可能发生的IO异常
            System.err.println("写入文件时出错: " + e.getMessage());
        }
    }

    /**
     * anonymousAuthor
     * 2024-12-17
     * 输出所有函数
     * @param outPutPath
     */
    private static void outPutFunctionsMain(String outPutPath,String fileName,List<MainFunctionForSlice> functions) {
        // 指定输出文件的路径
        String filePath = outPutPath+""+File.separator+""+fileName;

        // 使用try-with-resources语句来自动关闭BufferedWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            // 遍历
            for (MainFunctionForSlice fsr : functions) {
                writer.write("program: " );
                writer.newLine(); // 换行
                writer.write(fsr.getCppProgram().getProgramStr() );
                writer.newLine(); // 换行

                if(fsr.getName()!=null&&fsr.getID()!=null&&fsr.getReturnType()!=null){
                    writer.write("ID: " + fsr.getID() );
                    writer.newLine(); // 换行
                    writer.write("Name: " + fsr.getName());
                    writer.newLine(); // 换行
                    writer.write("returnType: "+fsr.getReturnType().getTypeName());
                    writer.newLine(); // 换行

                }

                if(fsr.getMainIn()!=null){
                    writer.write("main-in: ");
                    writer.newLine(); // 换行
                    for (String s : fsr.getMainIn()) {
                        writer.write(s);
                        writer.newLine(); // 换行
                    }
                }

                if(fsr.getMainOut()!=null){
                    writer.write("main-out: ");
                    writer.newLine(); // 换行
                    for (String s : fsr.getMainOut()) {
                        writer.write(s);
                        writer.newLine(); // 换行
                    }
                }

                if(fsr.getParameterList()!=null){
                    for (Type t : fsr.getParameterList()) {
                        writer.write("parameterType: " + t.getTypeName() );
                        writer.newLine(); // 换行
                    }
                }
                if(fsr.getParameterIdentifiers()!=null){
                    for (SymbolRecord sr : fsr.getParameterIdentifiers()) {
                        writer.write("parameterName: " + sr.getName() );
                        writer.newLine(); // 换行
                    }
                }
                writer.write(fsr.getFunctionBody());
                writer.newLine(); // 换行
            }
            //System.out.println("已成功写入文件: " + filePath);
        } catch (IOException e) {
            // 捕获并处理可能发生的IO异常
            System.err.println("写入文件时出错: " + e.getMessage());
        }
    }

    private static void scopeTyping(String block, CppProgram cp) {
        if(block.startsWith("struct")){
            List<String> blocks=scopeMap.get("struct");
            blocks.add(block);
            scopeMap.put("struct",blocks);
            List<ScopeBlockForSlice> structs=cp.getStructList();
            ScopeBlockForSlice sbfs=new ScopeBlockForSlice(block,rootScope);
            structs.add(sbfs);
            cp.setStructList(structs);
        }else if(block.startsWith("template")){
            List<String> blocks=scopeMap.get("template");
            blocks.add(block);
            scopeMap.put("template",blocks);
            List<ScopeBlockForSlice> templates=cp.getTemplateList();
            ScopeBlockForSlice sbfs=new ScopeBlockForSlice(block,rootScope);
            templates.add(sbfs);
            cp.setTemplateList(templates);
        }else if(block.startsWith("while")){
            List<String> blocks=scopeMap.get("while");
            blocks.add(block);
            scopeMap.put("while",blocks);
            List<ScopeBlockForSlice> whiles=cp.getWhileList();
            ScopeBlockForSlice sbfs=new ScopeBlockForSlice(block,rootScope);
            whiles.add(sbfs);
            cp.setWhileList(whiles);
        }else if(block.startsWith("for")){
            List<String> blocks=scopeMap.get("for");
            blocks.add(block);
            scopeMap.put("for",blocks);
            List<ScopeBlockForSlice> fors=cp.getForList();
            ScopeBlockForSlice sbfs=new ScopeBlockForSlice(block,rootScope);
            fors.add(sbfs);
            cp.setForList(fors);
        }else if(block.startsWith("if")){
            List<String> blocks=scopeMap.get("if");
            blocks.add(block);
            scopeMap.put("if",blocks);
            List<ScopeBlockForSlice> ifs=cp.getIfList();
            ScopeBlockForSlice sbfs=new ScopeBlockForSlice(block,rootScope);
            ifs.add(sbfs);
            cp.setIfList(ifs);
        }else if(block.startsWith("void")){//更多function的匹配方法还需要再细化
            List<String> blocks=scopeMap.get("function");
            blocks.add(block);
            scopeMap.put("function",blocks);
            FunctionForSlice fsr=new FunctionForSlice(block,rootScope);
            fsr.setCppProgram(cp);
            voidFunctions.add(fsr);
            List<FunctionForSlice> funs=cp.getFunctionList();
            funs.add(fsr);
            cp.setFunctionList(funs);
        }else if(block.startsWith("int")){//更多赋值语句的匹配方法还需要再细化//其实匹配到的是返回值类型为int的函数
            if(block.startsWith("int main")){
                List<String> blocks=scopeMap.get("mainFunction");
                blocks.add(block);
                scopeMap.put("mainFunction",blocks);
                MainFunctionForSlice mfsr= new MainFunctionForSlice(block,rootScope);
                mfsr.setCppProgram(cp);
                mainFunctions.add(mfsr);
                List<MainFunctionForSlice> mainFuns=cp.getMainFunctionList();
                mainFuns.add(mfsr);
                cp.setMainFunctionList(mainFuns);
            }else if(block.contains("main()")){
                System.out.println("main but not in format: "+block);
            }else{//不是main函数 是普通的返回值类型为int的函数
                List<String> blocks=scopeMap.get("intFunction");
                blocks.add(block);
                scopeMap.put("intFunction",blocks);
                FunctionForSlice fsr=new FunctionForSlice(block,rootScope);
                fsr.setCppProgram(cp);
                intFunctions.add(fsr);
                List<FunctionForSlice> funs=cp.getFunctionList();
                funs.add(fsr);
                cp.setFunctionList(funs);
            }
        }else if(block.contains("include")){//需要细化
            List<String> blocks=scopeMap.get("include");
            blocks.add(block);
            scopeMap.put("include",blocks);
            List<String> includes=cp.getIncludeList();
            includes.add(block);
            cp.setIncludeList(includes);
        }
    }

}