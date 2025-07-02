package iscas.ac.grand.main.common;

import iscas.ac.grand.main.ReductProgram;
import iscas.ac.grand.main.Serializable;
import iscas.ac.grand.main.antlr4.ASTWalkFromTestSuit;
import iscas.ac.grand.main.antlr4.CPP14Parser;
import iscas.ac.grand.main.antlr4.CPPParserASTListener;
import iscas.ac.grand.main.antlr4.CustomRuleContextDecorator;
import iscas.ac.grand.main.c.CConfigureInfo;
import iscas.ac.grand.main.c.GrammarGenerateForC;
import com.github.curiousoddman.rgxgen.RgxGen;

//import com.google.googlejavaformat.java.Formatter;
//import com.google.googlejavaformat.java.FormatterException;
import iscas.ac.grand.main.javapkg.GrammarGenerateForJava;
import iscas.ac.grand.main.javapkg.JavaConfigureInfo;
import iscas.ac.grand.main.mutation.CppForInsert;
import iscas.ac.grand.main.python.PythonConfigureInfo;
import iscas.ac.grand.main.python.PythonGrammarGenerateService;
import iscas.ac.grand.mutate.cplus14.CPlusConfigureInfo;
import iscas.ac.grand.mutate.cplus14.GrammarGenerateForCPlus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class GrammarGenerateService {
    List<Grammar> grammars=new ArrayList<>();
//    RemoteAutoTest remoteAutoTest=new RemoteAutoTest();

    public GrammarGenerateService(List<Grammar> grammars) {
        this.grammars = grammars;
    }

    public GrammarGenerateService() {
    }

    public List<String> generateProgramsByGrammmar(int index, int num) throws Exception {
        List<String> result=new ArrayList<>();
        List<String> rootKeys=new ArrayList<>();
        ConfigureInfo.setConfiguration();//程序语言配置项 如关键字列表 基本类型列表等等
        //变异程序用 2024-12-25
        if(ConfigureInfo.mutationAvailable&&ConfigureInfo.selectedMutationStrategy.equals(ConfigureInfo.mutationStrategyMain2Main)){
//            CppForInsert cfi=new CppForInsert("F:"+File.separator+"data-compiler-testing"+File.separator+"2024-12-16cpp-gcc-llvm-testsuit"+File.separator+"gcc-testsuit-slice"+File.separator+"1225"+File.separator+"cppProgramList.txt");//静态的程序列表
//            CppForInsert cfi=new CppForInsert("F:"+File.separator+"data-compiler-testing"+File.separator+"2024-12-16cpp-gcc-llvm-testsuit"+File.separator+"programs-deleted-failed-slice"+File.separator+"0120"+File.separator+"cppProgramList.txt");//静态的程序列表
            CppForInsert cfi=new CppForInsert("main2main"+File.separator+"cppProgramList.txt");//静态的程序列表 用于main-main策略，基于匹配切片而非AST
             }
        if(ConfigureInfo.mutationAvailable&&ConfigureInfo.selectedMutationStrategy.equals(ConfigureInfo.mutationStrategyAstFun2Fun)){
//            CppForInsert cfi=new CppForInsert();反序列化方式
//            cfi.getAstFunForInsert("F:"+File.separator+"data-compiler-testing"+File.separator+"2024-12-16cpp-gcc-llvm-testsuit"+File.separator+"programs-deleted-failed-slice"+File.separator+"0109"+File.separator+"ast"+File.separator+"AStFunList.txt");//ast函数列表
            ASTWalkFromTestSuit awts = new ASTWalkFromTestSuit();
//            awts.parserCpp();//图形界面操作 在windows中
            awts.parserCpp(ConfigureInfo.sourcePath,ConfigureInfo.astPath);
            //statisticsAboutFunState();统计源程序中的函数及语句里的赋值语句涵盖情况
        }

        rootKeys = getRootKeys(index);//获取文法树的根节点，由此来自顶向下（或）的构造
        if(rootKeys!=null&&rootKeys.size()>1)
        {
        	rootKeys=new ArrayList<>();
        	rootKeys.add(ConfigureInfo.compilerUnit);
        }
        if(rootKeys==null||rootKeys.size()==0){
            //System.out.println(" hard to generate program for a grammar with no root nodes ");
        }
//        int i=1;
        int number=1;
        String grammarName=getGrammarName(index);//文法名称，例如JavaParser
        //System.out.println(grammars.get(index).getPath()+" ++++++++ ");
        updateKeyAllTokens(rootKeys,index);
        PrintData pd=new PrintData(grammars);
        int j=1;
        int i=0;
        while(number<num){
            //每个程序维护一张自己的符号表
        	//System.out.println("program "+number);
//        	int i=number%10000;//远程执行相关
            i=number;

            String programName = grammarName + "_program" + i;//测试的名称
            String programPath = grammars.get(index).getPath().replace(".g4", File.separator);//保存测试的目录
            String programPathAuto = grammars.get(index).getPath().replace(".g4", File.separator);//保存测试的目录
            
            SymbolTable st=new SymbolTable();
            st.setParogramId(grammarName + "_symboltable" + i);
            ScopeTree scopeNode=new ScopeTree(1,null,"0",null,null);//作用域根节点
            scopeNode.setRoot(scopeNode);
            st.setRootScope(scopeNode);
            st.setScopeNum(1);
            st.setProgramName(programName);
            List<String> baseTypes= ConfigureInfo.getBaseTypes();//取出基本类型列表
            st.initailTypeMap(baseTypes);
            String program="";
            List<String> programs=new ArrayList<>();
            String programAuto="";
            try{
//            	setGlobalVar(st,scopeNode,index);//为程序生成第一个全局变量

                if(ConfigureInfo.mutationAvailable&&ConfigureInfo.selectedMutationStrategy.equals(ConfigureInfo.mutationStrategyAstFun2Fun)){
                    programs=generateProgramsByKey(rootKeys,index,st,scopeNode,0);
                    if(programs!=null&&programs.size()>0) {
                        List<String> tempPro=new ArrayList<>();
                        for (String pro:programs) {
                            tempPro.add(postProcessAfterGeneration(pro,st));
                        }
                        programs=tempPro;
                    }
                }else{
                    program=generateProgramByKey(rootKeys,index,st,scopeNode,0);
                    program=postProcessAfterGeneration(program,st);//换行等操作格式化程序内容
                }

//                if(common.ConfigureInfo.ifJavaCode&&JavaConfigureInfo.ifFormatJavaCode){
//                    com.google.googlejavaformat.java.Formatter formatter = new Formatter();
//                    try {
//                        program=formatter.formatSource(program);
//                    } catch (FormatterException e) {
//                        //System.out.println("googlejavaformat error");
//                        //System.out.println(program);
//                        e.printStackTrace();
//                        continue;
//                    }
//                }else

                if(ConfigureInfo.ifCPlusCode){//将部分变量类型替换为auto
                	//programAuto=autoProgram(program);
                	programAuto=program;
                }
                if(ConfigureInfo.ifCCode){
                	//programAuto=autoProgram(program);
                	programAuto=program;
                }
            }catch(Exception e){
                //System.out.println("fail generate of :"+i+" in grammar: "+grammars.get(index).getPath());
                e.printStackTrace();
                i--;
            }

            if(ConfigureInfo.mutationAvailable&&ConfigureInfo.selectedMutationStrategy.equals(ConfigureInfo.mutationStrategyAstFun2Fun)){
                List<String> tempResult=outPutPrgraoms(programs,st,programPath,index,pd,programName,i);
                result.addAll(tempResult);
                number+=tempResult.size();
            }else{
                List<String> tempResult=outPutPrgraom(program,st,programPath,index,pd,programName,i);
                result.addAll(tempResult);
                number+=tempResult.size();
            }
//            if(i==0&&ConfigureInfo.ifCPlusCode) {先不远程执行了
//            	//远程编译测试（批量）
//            	remoteAutoTest.remoteCompileTestMulti(ConfigureInfo.outPutdirectory+File.separator+j+""+File.separator+"programs",j+"/programs",j);
//            	j++;
//            }
        }
        
//        remoteAutoTest.disRemoteAutoTest();
        return result;
        
    }

    /**
     * qurong
     * 2025-2-19
     * 输出多个程序，可能也要处理一些字符串内容 用在枚举方法中
     * @param programs
     * @param st
     * @param programPath
     * @param index
     * @param pd
     * @param programName
     * @param i
     */
    private List<String> outPutPrgraoms(List<String> programs, SymbolTable st, String programPath, int index, PrintData pd, String programName, int i) throws IOException {
        List<String> result=new ArrayList<>();
        if(programs!=null){
            for(String str:programs){
                result.addAll(outPutPrgraom(str, st, programPath, index, pd, programName, i++));
            }
        }
        return result;
    }

    /**
     * qurong
     * 2025-2-19
     * 输出一个程序，用在一般的生成结果上
     * @param program
     * @param st
     * @param programPath
     * @param index
     * @param pd
     * @param i
     */
    private List<String> outPutPrgraom(String program, SymbolTable st, String programPath, int index, PrintData pd, String programName, int i) throws IOException {

        //确保赋值语句中标识符之间的关系都被记录下来了
        List<String> result=new ArrayList<>();
        ensureIdentiferRelation(program,st);

        //程序的精简
//            ReductProgram reductProgram=new ReductProgram();
//            reductProgram.ReductProgramBySymbolTable(program,st,1);

        result.add(program);
        program=program.trim();


        if(program!="") {
            int len=program.length();
            if(len<2){//不要生成空程序
                return new ArrayList<>();
            }
            if (ConfigureInfo.isFangzhouMulti) {//如果是为方舟编译器生成批量的测试程序  SANITY000206-cprogram206
                if(ConfigureInfo.ifJavaCode) programPath = grammars.get(index).getPath().replace(".g4", ""+File.separator+"samll_5000"+File.separator+"" +"APP000"+i+"-"+ programName + File.separator);//保存测试的目录
                if(ConfigureInfo.ifCCode) programPath = grammars.get(index).getPath().replace(".g4", ""+File.separator+"small_5000"+File.separator+"" +"SANITY000"+i+"-cprogram"+ i + File.separator);//保存测试的目录
                File directory = new File(programPath);
                if(directory.mkdirs()){
                    if(ConfigureInfo.ifJavaCode) pd.writeProgram(programPath + programName + ConfigureInfo.postfix, program);//java文件
                    if(ConfigureInfo.ifCCode) {
                        programName="cprogram" + i;
                        pd.writeProgram(programPath + programName + ConfigureInfo.postfix, program);//java文件
                    }
                    pd.writeProgram(programPath + "test.cfg", "compile(" + programName + ")\r\n" + "run(" + programName + ")\r\n");//test.cfg文件
                }
            } else{
                //输出程序到programs路径
//                	programPath=ConfigureInfo.outPutdirectory+File.separator+j+""+File.separator+"programs"+File.separator+"program"+i+File.separator;
                System.out.println("program" + i);
//                    programPath=ConfigureInfo.outPutdirectory+File.separator+j+File.separator+"programs"+File.separator;
                programPath=ConfigureInfo.outPutdirectory+File.separator+ConfigureInfo.index+File.separator+"build"+File.separator+"cprogram"+File.separator;
                File directory = new File(programPath);
                if(!directory.exists()){boolean dircreated= directory.mkdirs();}
                //if(directory.mkdirs()) {
                programName="program" + i;
                String programFilePath=programPath+programName+ ConfigureInfo.postfix;

                pd.writeProgram(programFilePath, program);//输出程序文件

                //远程编译测试（单个程序）
                //remoteAutoTest.remoteCompileTest(programName,programFilePath,ConfigureInfo.postfix);
                //}

                //printAutoPrograms(programPathAuto,pd,i,programAuto);//输出auto程序到autoprograms路径

                //printReductionData(programPath,directory,i,program,pd,st);//输出程序的符号表数据、精简程序到data路径

            }
        }
        return result;
    }

    /**
     * qurong
     * 2025-2-19
     * 在程序生成结束后的操作，比如换行等等
     * @param program
     * @param st
     */
    private String postProcessAfterGeneration(String program, SymbolTable st) {
        if(ConfigureInfo.ifCCode){
            program=insertAddDeclarationToHead(st,program);
            program=insertAddInclude(program);
        }
        if(ConfigureInfo.ifCPlusCode){
            program=insertAddDeclarationToHeadCPlus(st,program);
            program=insertAddIncludeCPlus(program,st);

        }
        if(ConfigureInfo.ifPythonCode&&PythonConfigureInfo.ifFormatJPythonCode){
            program=program.replaceAll(PythonConfigureInfo.newline+" ",PythonConfigureInfo.newline);//移除换行和tab之间的多余空格
            program=program.replaceAll(" "+PythonConfigureInfo.tab,PythonConfigureInfo.tab);//移除换行和tab之间的多余空格
            program=program.replaceAll(PythonConfigureInfo.newline,"\r\n");
            program=program.replaceAll(PythonConfigureInfo.tab,"\t");

        }else{
            program=program.replaceAll("}","}\r\n");
            program=program.replaceAll(";",";\r\n");
            program=program.replaceAll("\\{","\\{\r\n");
            program=program.replaceAll("= \\{\r\n","= \\{");
            program=program.replaceAll("}\r\n;","};");
            program=program.replaceAll("}\r\n ;","};");
        }
        return program;
    }

    /**
     * qurong
     * 2025-2-6
     * 统计源程序中的函数和语句中的赋值语句（等号）的包含情况
     */
    private void statisticsAboutFunState() {
        List<CPP14Parser.FunctionDefinitionContext> funList = CPPParserASTListener.getFunList();//在生成之前解析程序，不需要序列化等操作
        int length=funList.size();
        int functionContainEqualCount=0;
        int statementContainEqualCount=0;
        int statementContainEqualCountByDeractorFun=0;
        int statementContainEqualCountByDeractorFunState=0;
        for(CPP14Parser.FunctionDefinitionContext fun:funList){
            if(fun.getText().contains("=")){
                functionContainEqualCount++;
                List<CPP14Parser.StatementContext> statements=fun.getStatementList();
                if(statements==null&&statements.size()==0){
                    //该函数中不包含语句，没有枚举的必要
                    continue;
                }
                List<CPP14Parser.StatementContext> funAssignStatement=new ArrayList<>();//函数中包含的赋值语句
                for(CPP14Parser.StatementContext statement:statements){
                    if(fun.getText().contains(statement.getText())&&statement.getText().contains("=")){
                        //funAssignStatement.add(statement);
                        statementContainEqualCount++;
                        //System.out.println("fun.getText() :"+fun.getText());
                        break;
                    }
                }

                for(CPP14Parser.StatementContext statement:statements){
                    CustomRuleContextDecorator decorator = new CustomRuleContextDecorator(fun);
                    String customText = decorator.getTextWithSpaces();
                    if(customText.contains(statement.getText())&&statement.getText().contains("=")){
                        //funAssignStatement.add(statement);
                        statementContainEqualCountByDeractorFun++;
                        //System.out.println("decorator fun.getText() :"+fun.getText());
                        break;
                    }
                }

                for(CPP14Parser.StatementContext statement:statements){
                    CustomRuleContextDecorator decorator1 = new CustomRuleContextDecorator(fun);
                    String customFunText = decorator1.getTextWithSpaces();

                    CustomRuleContextDecorator decorator2 = new CustomRuleContextDecorator(statement);
                    String customStateText = decorator2.getTextWithSpaces();
                    if(customFunText.contains(customStateText)&&customStateText.contains("=")){
                        //funAssignStatement.add(statement);
                        statementContainEqualCountByDeractorFunState++;
                        //System.out.println("decorator fun.getText() :"+fun.getText());
                        break;
                    }
                }
            }
        }
        System.out.println("function :"+length);
        System.out.println("function contains =:"+functionContainEqualCount);
        System.out.println("function contains = and statement contains =:"+statementContainEqualCount);
        System.out.println("decorator function contains = and statement contains =:"+statementContainEqualCountByDeractorFun);
        System.out.println("decorator function contains = and decorator statement contains =:"+statementContainEqualCountByDeractorFunState);
    }

    /**
     * qurong
     * 2024.4.25
     * 判断字符串中是否包含unicode
     */
    public static boolean containsUnicode(String str) {
        // 正则表达式匹配形如 \\uXXXX 的字符串
        return str.matches(".*\\\\u[0-9a-fA-F]{4}.*");
    }
   
/**
 * qurong
 * 2024.3.9
 * 输出程序的符号表数据、精简程序到data路径
 * @param programPath
 * @param directory
 * @param i
 * @param program
 * @param pd
 * @param st
 */
private void printReductionData(String programPath, File directory, int i, String program, PrintData pd,
			SymbolTable st) {
		// TODO Auto-generated method stub
	
    programPath=ConfigureInfo.outPutdirectory+File.separator+"data"+File.separator+"program"+i+File.separator;
    directory = new File(programPath);
    if(directory.mkdirs()) {
    	pd.writeProgram(programPath+"program" + i + ConfigureInfo.postfix, program);//输出程序文件
    	try {
			reductProgramByOrder(directory,programPath,i,pd,program,st);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//符号表序列化
        Serializable serializable=new Serializable();
        serializable.objectToFile(st, programPath+"program" + i+".txt");
    }
	}

/**
 * qurong
 * 2024.3.9
 * 输出auto程序到autoprograms路径
 * @param programPathAuto
 * @param pd
 * @param i
 * @param programAuto
 */
    private void printAutoPrograms(String programPathAuto, PrintData pd, int i, String programAuto) {
		// TODO Auto-generated method stub
    	
    	programPathAuto=ConfigureInfo.outPutdirectory+File.separator+"autoprograms"+File.separator+"program"+i+File.separator;
        File directoryAuto = new File(programPathAuto);
        if(directoryAuto.mkdirs()) {
        	pd.writeProgram(programPathAuto+"program" + i + ".cpp", programAuto);//输出程序文件
        }
		
	}

	/**
     * 程序精简 将程序按照每个出错的标识符进行精简，假设每个程序只有一个变量的值在打印时有差异
     * @param directory 
     * @param i 
     * @param pd 
     * @param program
     * @param st
     * @throws IOException 
     */
    private void reductProgramByOrder(File directory, String programPath, int i, PrintData pd, String program, SymbolTable st) throws IOException {
		// TODO Auto-generated method stub
    	 //程序的精简
    	Map<Integer,SymbolRecord> printPositionOfSymbolRecord=st.getPrintPositionOfSymbolRecord();
    	SymbolRecord temp;
    	SymbolRecord before=null;
    	for(int j=1;j<printPositionOfSymbolRecord.size()+1;j++) {
    		temp=printPositionOfSymbolRecord.get(j);
    		if(temp==null||temp==before) {
    			continue;
    		}
    		before=temp;
    		
    		ReductProgram reductProgram=new ReductProgram();
            String program2=reductProgram.ReductProgramBySymbolTable(program,st,j);
            
            //if(directory.mkdirs()) {
                pd.writeProgram(programPath+"program" + i+"_after_"+j + ConfigureInfo.postfix, program2);//输出精简后的程序到文件
            //}
    	}
	}

	/**
     * qurong
     * 2023.9.19
     * 确保赋值语句左右的标识符关系被记录下来了
     * @param programs
     * @param st
     * @throws IOException 
     */
    private void ensureIdentiferRelation(String programs, SymbolTable st) throws IOException {
    		// TODO Auto-generated method stub
		ScopeTree scopeTree=new ScopeTree();
		String[] lines=programs.split("\r\n");
		if(lines==null||lines.length<=1) {
			return ;
		}
	    boolean forFlag=false;
	    String forHead="";
	    String printStr="";
	    //printStr+="/*========================程序中不以分号结尾的语句";
		for(String line:lines) {
			boolean flag=false;
			if(line.contains("break")||line.contains("while")||line.contains("print")||line.contains("count")||line.contains("return")||line.contains("include")) {
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
			line=line.trim();
			if(line.endsWith(";")) {
				if(line.contains("=")) {//是赋值语句
					updateIdentifierRelations("",line,st.getRootScope(),st,-1);//更新当前程序中在当前作用域内各个标识符之间的关系
				}else {
//					printStr+="\r\n奇怪的语句";
//					printStr+=line;
//					printStr+="\r\n";
				}
			}else if(!line.endsWith("{")&&!line.endsWith("}")){
				printStr+="\r\n";
				printStr+=line;
				printStr+="\r\n";
			}
			
		}
		//printStr+="*/";
//		File file = new File("H:"+File.separator+"demo"+File.separator+"log.txt");//只在windows本地适用
		File file = new File(ConfigureInfo.logPath);//
		FileWriter fr = new FileWriter(file, true);
		//System.out.println(printStr);
		fr.write(printStr+"\r\n");
		fr.close();
		return ;
	}

	private void setGlobalVar(SymbolTable st, ScopeTree scopeNode,int index,int lineOrder) {
		int addCount=0;
    	Map<String,List<String>> addedDeclarationMap1=st.getAddedDeclaration();
        List<String> addedDeclaration1=addedDeclarationMap1.get(st.getProgramName());//补充的声明列表
        if(addedDeclaration1==null){
            addedDeclaration1=new ArrayList<>();
        }
        String identifier=getIdentifier(index, null, false, st, scopeNode,lineOrder);
        String identifierValue="0";
        Type type=st.getTypeMap().get("int");
        SymbolRecord symbolRecord1 =new SymbolRecord(identifier,type,identifier,"var",scopeNode,identifierValue);
        
        String defineStatement="int"+" "+ identifier+ " = "+identifierValue+";";
    	addedDeclaration1.add(defineStatement);
    	addCount=addedDeclaration1.size();
        addedDeclarationMap1.put(st.getProgramName(),addedDeclaration1);
        st.setAddedDeclaration(addedDeclarationMap1);
        updateSymTableByVar(st,symbolRecord1);
        updateIdentifierRelations("", defineStatement, scopeNode,st,(0-addCount));
        
	}

	/**
     * 构造满足文法的字符串 important method recursion
     * @param str
     * @param index grammar的序号
     * @param tokenAppearTimes 当前生成的路径上，各个token参与的次数
     * @param ifNeedOutCycle 是否需要尽快跳出循环
     * @return
     */
    public String constructProgramByValue(String str,int index,Map<String,Integer> tokenAppearTimes,boolean ifNeedOutCycle,SymbolTable st, ScopeTree scopeNode,int lineOrder) {

        String strCopy=str;//调试专用临时变量
        String result="";
        String identifierCount="";
        String loopIdentifier="";
        //System.out.println(str);

        //作用域相关
        if(ifScopeGrammar(strCopy)){
            scopeNode=updateScope(st,scopeNode);
        }
        
        //如果是新语句，更新行号
        if(ifStatementGrammar(str)){
        	lineOrder++;
        }

        //循环相关
        if(ifLoopGrammar(strCopy)){
            identifierCount=getIdentifier(index, tokenAppearTimes, ifNeedOutCycle, st,  scopeNode,lineOrder)+"count";
            loopIdentifier=getLoopCountIdentifier(identifierCount,scopeNode);//防止同一个作用域内多个并列的循环，声明count重名
            updateSymbolIdentifer(st,identifierCount);
//            common.SymbolRecord symbolRecord = new common.SymbolRecord(identifierCount,st.getTypeMap().get(common.ConfigureInfo.loopCountType),identifierCount,"var", scopeNode,"0");
//            updateSymTableByVar(st, symbolRecord); //防止计数变量的值被修改，不将其更新到符号表
        }

        //如果是特殊文法，返回生成结果
        String spResult=generateIfSpecialGrammar(strCopy, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        if(spResult!=""){
        	spResult=updateProgramAfterGenerateRule(spResult,strCopy,index,loopIdentifier,identifierCount,scopeNode,st,lineOrder);
            return spResult;
        }

        //System.out.println(" input: "+str);
        //终止性约束
//        str=constraint.checkTermination(str,index,cyclyLength);
        if(str==null||str==""){
            return "";
        }
        str=checkFormat(str,index);
        if(str==null||str==""){
            return "";
        }
        if(str.contains("Expression")){
            //System.out.println("  ");
        }
        if(str.contains("Varname")){
            //System.out.println("  ");
        }
        String[] items=str.split(" ");//认为括号后面要么跟+？* 要么跟空格
        String tempItem="";
        boolean tempFlag=false;
        for(String item:items){
            if(tempItem==""){
                tempItem=item;
                tempFlag=true;
            }else{
                tempFlag=false;
            }
            if(StringTools.judgeBrackets(tempItem)){
                if(!tempFlag){
                    tempItem+=" "+item;
                }
            }else{
                tempItem=tempItem.trim();
//                if(result.equals("") || result.trim().equals("")||(ConfigureInfo.ifPythonCode&&result.endsWith(PythonConfigureInfo.newline))){
                if(result.equals("") || result.trim().equals("")){
                    result +=generateProgramForItem(tempItem, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);//为文法中的每一个项生成相应的code
                }else {
                    result += " " + generateProgramForItem(tempItem, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);//为文法中的每一个项生成相应的code
                }
                if (!tempFlag) {
                    tempItem = item;
                } else {
                    tempItem = "";
                }
            }
        }
        if(!tempItem.equals("")){//最后一个结果也要为其生成语言
            if(StringTools.judgeBrackets(tempItem)){
                //System.out.println(" can't match brackets: "+tempItem);
                if(result.equals("") || result.trim().equals("")) {
                    result += tempItem;
                }else{
                    result += " " + tempItem;
                }
            }else{
                tempItem=tempItem.trim();
                if(result.equals("") || result.trim().equals("")){
                    result =generateProgramForItem(tempItem, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
                }else {
                    result +=" " +generateProgramForItem(tempItem, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
                }
            }
        }
        
        result=updateProgramAfterGenerateRule(result,strCopy,index,loopIdentifier,identifierCount,scopeNode,st,lineOrder);//更新规则生成之后程序中的信息
        if(containsUnicode(result)) {//如果生成的程序中包含unicode
        	String stringWithChars = result.replace("\\u", "\\");
            stringWithChars = stringWithChars.replace("\\", "\\u");
            result = decodeUnicode(stringWithChars);
        }
        return result;

    }

    /**
     * qurong
     * 2025-2-13
     * 构造满足文法的字符串 important method recursion 这个返回的字符串是列表，用在由于枚举出现的多结果上。
     * @param str
     * @param index grammar的序号
     * @param tokenAppearTimes 当前生成的路径上，各个token参与的次数
     * @param ifNeedOutCycle 是否需要尽快跳出循环
     * @return
     */
    public List<String> constructProgramsByValue(String str,int index,Map<String,Integer> tokenAppearTimes,boolean ifNeedOutCycle,SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        List<String> result=new ArrayList<>();
        String strCopy=str;//调试专用临时变量
        //String result="";
        String identifierCount="";
        String loopIdentifier="";
        //System.out.println(str);
        StringTools stringTools=new StringTools();


        //作用域相关
        if(ifScopeGrammar(strCopy)){
            scopeNode=updateScope(st,scopeNode);
        }

        //如果是新语句，更新行号
        if(ifStatementGrammar(str)){
            lineOrder++;
        }

        //循环相关
        if(ifLoopGrammar(strCopy)){
            identifierCount=getIdentifier(index, tokenAppearTimes, ifNeedOutCycle, st,  scopeNode,lineOrder)+"count";
            loopIdentifier=getLoopCountIdentifier(identifierCount,scopeNode);//防止同一个作用域内多个并列的循环，声明count重名
            updateSymbolIdentifer(st,identifierCount);
//            common.SymbolRecord symbolRecord = new common.SymbolRecord(identifierCount,st.getTypeMap().get(common.ConfigureInfo.loopCountType),identifierCount,"var", scopeNode,"0");
//            updateSymTableByVar(st, symbolRecord); //防止计数变量的值被修改，不将其更新到符号表
        }

        //如果是特殊文法，返回生成结果
        List<String> spResult=generateProgramsIfSpecialGrammar(strCopy, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        if(spResult!=null&&spResult.size()>0){
            List<String> tempResult=new ArrayList<>();
            for(String res:spResult){
                String tempRes=updateProgramAfterGenerateRule(res,strCopy,index,loopIdentifier,identifierCount,scopeNode,st,lineOrder);//更新规则生成之后程序中的信息
                if(containsUnicode(tempRes)) {//如果生成的程序中包含unicode
                    String stringWithChars = tempRes.replace("\\u", "\\");
                    stringWithChars = stringWithChars.replace("\\", "\\u");
                    tempRes=decodeUnicode(stringWithChars);
                }
                tempResult.add(tempRes);
            }
            spResult=tempResult;
            return spResult;
        }

        //System.out.println(" input: "+str);
        //终止性约束
//        str=constraint.checkTermination(str,index,cyclyLength);
        if(str==null||str==""){
            return result;
        }
        str=checkFormat(str,index);
        if(str==null||str==""){
            return result;
        }
        if(str.contains("Expression")){
            //System.out.println("  ");
        }
        if(str.contains("Varname")){
            //System.out.println("  ");
        }
        String[] items=str.split(" ");//认为括号后面要么跟+？* 要么跟空格
        String tempItem="";

        boolean tempFlag=false;
        for(String item:items){
            if(tempItem==""){
                tempItem=item;
                tempFlag=true;
            }else{
                tempFlag=false;
            }
            if(StringTools.judgeBrackets(tempItem)){
                if(!tempFlag){
                    tempItem+=" "+item;
                }
            }else{
                tempItem=tempItem.trim();
//                if(result.equals("") || result.trim().equals("")||(ConfigureInfo.ifPythonCode&&result.endsWith(PythonConfigureInfo.newline))){
                if(result==null||result.size()==0){
                    result =generateProgramsForItem(tempItem, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);//为文法中的每一个项生成多个相应的code
                }else{
                    List<String> geneResults=generateProgramsForItem(tempItem, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);//为文法中的每一个项生成相应的code
                    List<String> tempResults=new ArrayList<>();
                    for(String resPre:result){
                        if(geneResults==null||geneResults.size()==0)
                        {
                            tempResults.add(resPre);
                        }else {
                            for (String resPost : geneResults) {
                                tempResults.add(resPre + " " + resPost);
                            }
                        }
                    }
                    result=tempResults;
                }
                if (!tempFlag) {
                    tempItem = item;
                } else {
                    tempItem = "";
                }
            }
        }
        if(!tempItem.equals("")){//最后一个结果也要为其生成语言
            if(StringTools.judgeBrackets(tempItem)){
                //System.out.println(" can't match brackets: "+tempItem);
                if(result==null||result.size()==0){
                    result=new ArrayList<>();
                    result.add(tempItem);
                }else{
                    List<String> tempResults=new ArrayList<>();
                    for(String resPre:result){
                        tempResults.add(resPre+" ");
                    }
                    result=tempResults;
                }
            }else{
                tempItem=tempItem.trim();
                if(result==null||result.size()==0){
                    result =generateProgramsForItem(tempItem, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);//为文法中的每一个项生成多个相应的code
                }else{
                    List<String> geneResults=generateProgramsForItem(tempItem, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);//为文法中的每一个项生成相应的code
                    List<String> tempResults=new ArrayList<>();
                    for(String resPre:result){
                        if(geneResults==null||geneResults.size()==0)
                        {
                            tempResults.add(resPre);
                        }else {
                            for (String resPost : geneResults) {
                                tempResults.add(resPre + " " + resPost);
                            }
                        }
                    }
                    result=tempResults;
                }
            }
        }
        List<String> tempResult=new ArrayList<>();
        if(result==null||result.size()==0){
            return new ArrayList<>();
        }else{
            for(String res:result){
                String tempRes=updateProgramAfterGenerateRule(res,strCopy,index,loopIdentifier,identifierCount,scopeNode,st,lineOrder);//更新规则生成之后程序中的信息
                if(containsUnicode(tempRes)) {//如果生成的程序中包含unicode
                    String stringWithChars = tempRes.replace("\\u", "\\");
                    stringWithChars = stringWithChars.replace("\\", "\\u");
                    tempRes=decodeUnicode(stringWithChars);
                }
                tempResult.add(tempRes);
            }
        }

        result=tempResult;
        return result;

    }
    
    /**
     * qurong
     * 2024.4.25
     * 将字符串中所有的unicode转换为对应的字符
     * @param unicode
     * @return
     */
 
    private static String decodeUnicode(String unicode) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < unicode.length();) {
            if (unicode.charAt(i) == '\\' && unicode.charAt(i + 1) == 'u') {
                String unicodeStr = unicode.substring(i + 2, i + 6);
                sb.append((char) Integer.parseInt(unicodeStr, 16));
                i += 6;
            } else {
                sb.append(unicode.charAt(i++));
            }
        }
        return sb.toString();
    }

    /**
     * 2023.9.4
     * qurong
     * 在生成一条规则之后更新程序中的结构等信息
     * @param result
     * @param strCopy
     * @param index
     * @param loopIdentifier
     * @param identifierCount
     * @param scopeNode
     * @param st
     * @param lineOrder
     * @return
     */
    private String updateProgramAfterGenerateRule(String result, String strCopy, int index, String loopIdentifier, String identifierCount, ScopeTree scopeNode, SymbolTable st, int lineOrder) {
		// TODO Auto-generated method stub

        Constraint constraint=new Constraint();
    	//连续性约束（不包含空格）
        result=constraint.checkContinueConstraint(result,strCopy,index,grammars);

        //循环相关,循环生成结束后，在循环内添加跳出循环的判断
        if(ifLoopGrammar(strCopy)){
            result=loopIdentifier+result;
            result=insertBreakToLoop(result,identifierCount,scopeNode);
        }
        
      //函数相关
//        if(ifMethodGrammar(strCopy)){
//        	result=addOutput(st,scopeNode,result);
//        }
        
      //块相关
//        if(ifBlockGrammar(strCopy)){
//        	result=addOutput(st,scopeNode,result);
//        }
        
        //作用域相关
        if(ifScopeGrammar(strCopy)){
        	//result=addOutput(st,scopeNode,result);
        	scopeNode.setScopeFragment(result);
        	scopeNode=updateScopeToFather(result,scopeNode);
        }
        
        //作用域头部和尾部的记录
        if(ifScopeBodyGrammar(strCopy)){
        	//返回上一级作用域之前添加作用域内变量的打印语句
        	//result=addOutput(st,scopeNode,result);
            if(ConfigureInfo.mutationAvailable) {
                result = addComment(result, scopeNode);//程序变异作用域内插入位置点标记
            }
        	scopeNode=updateScopeBodyHeadTail(result,scopeNode);
        	if(ConfigureInfo.ifCCode) {
        		if(strCopy.equals(CConfigureInfo.methodDeclarationTotal)||strCopy.equals(CConfigureInfo.methodDeclaration)) {//把作用域和函数关联起来
            		if(st.getFunctionSymbolRecords()!=null&&st.getFunctionSymbolRecords().size()>0) {
                        scopeNode.setFun(st.getFunctionSymbolRecords().get(st.getFunctionSymbolRecords().size() - 1));
                    }
            	}
        	}else if(ConfigureInfo.ifCPlusCode) {
        		if(strCopy.equals(CPlusConfigureInfo.methodDeclarationTotal)||strCopy.equals(CPlusConfigureInfo.methodDeclaration)) {//把作用域和函数关联起来
                    if(st.getFunctionSymbolRecords()!=null&&st.getFunctionSymbolRecords().size()>0) {
                        scopeNode.setFun(st.getFunctionSymbolRecords().get(st.getFunctionSymbolRecords().size() - 1));
                    }
            	}else if(strCopy.equals(CPlusConfigureInfo.lamdaExpression)) {//把作用域和Lamda表达式关联起来
                    if(st.getLamdaSymbolRecords()!=null&&st.getLamdaSymbolRecords().size()>0) {
                        scopeNode.setLam(st.getLamdaSymbolRecords().get(st.getLamdaSymbolRecords().size() - 1));
                    }
            	}
        	}
        }

        //程序变异main函数外的全局插入位置点标记
        if(ifMainBodyGrammar(strCopy)){
            if(ConfigureInfo.mutationAvailable) {
                result = "//insertion-global-available\n" + addComment(result, scopeNode);//程序变异作用域内插入位置点标记
            }
        }
        
        //标识符定义或修改相关
        if(ifDefineOrModifyGrammar(strCopy)){
            //记录生成此文法的过程中，关联到的所有标识符
        	if(strCopy.equals("updateExpression")) {
        		////System.out.println();
        	}
        	updateIdentifierRelations(strCopy,result,scopeNode,st,lineOrder);//更新当前程序中在当前作用域内各个标识符之间的关系
        }

        //除数相关,除数生成后，判断是否为零，是的话，除数用1代替，不是的话返回除数的值
        if(ifDivisorGrammar(strCopy))result=zeroExclide(result);

        //语句相关
        if(ifStatementGrammar(strCopy)){
        	//更新语句及其行号
        	Map<Integer,String> lineOrderStatement=st.getLineOrderStatement();
        	String state=lineOrderStatement.get(lineOrder);
        	if(lineOrderStatement!=null&&state!=null&&state!="") {
        		//System.out.println("lineOrder: "+lineOrder);
        		//System.out.println("state: "+state);
        	}else if(lineOrderStatement!=null&&(state==null||state=="")) {
        		lineOrderStatement.put(lineOrder, result);
        		st.setLineOrderStatement(lineOrderStatement);
        	}
        	
        	//如果是开启新的语句，在python中需要考虑严格缩进
            result=getTabProfixByDepth(strCopy,scopeNode,result);
        }
        
        if(containsUnicode(result)) {//如果生成的程序中包含unicode
        	String stringWithChars = result.replace("\\u", "\\");
            stringWithChars = stringWithChars.replace("\\", "\\u");
            result = decodeUnicode(stringWithChars);
        }

		return result;
	}

    /**
     * qurong
     * 2024.3.9
     *在程序中添加 打印作用域中直接声明的所有标识符的语句
     * @param st
     * @param scopeNode
     * @return
     */
	private String addOutput(SymbolTable st, ScopeTree scopeNode,String str) {
		// TODO Auto-generated method stub
		String outPutStr="";
		String result="";
		PythonGrammarGenerateService pggs=new PythonGrammarGenerateService(grammars);
        GrammarGenerateForJava ggf=new GrammarGenerateForJava(grammars);
        GrammarGenerateForC ggc=new GrammarGenerateForC(grammars);
        GrammarGenerateForCPlus ggcplus=new GrammarGenerateForCPlus(grammars);
        if(ConfigureInfo.ifJavaCode){
        	Map<String,Type> cusTomTypeMap=st.getCustomizedTypeMap();
        	//outPutStr=ggf.generateOutPut(cusTomTypeMap, st, scopeNode);
        	
        }else if(ConfigureInfo.ifCCode){
        	//outPutStr=ggc.generateOutPut(st, scopeNode);
        	
        }else if(ConfigureInfo.ifPythonCode){
        	//outPutStr=pggs.generateOutPut(st, scopeNode);
        	
        }else if(ConfigureInfo.ifCPlusCode){
        	outPutStr=ggcplus.generateOutPut(st, scopeNode);
        	result=ggcplus.insertStr2ScopeEnd(str,outPutStr);
	    }
        
        return result;
	}

	/**
     * qurong
     * 2023.8.22
     * 更新作用域对应的程序片段（含头尾）
     * @param result
     * @param scopeNode
     * @return
     */
    private ScopeTree updateScopeBodyHeadTail(String result, ScopeTree scopeNode) {
		// TODO Auto-generated method stub
    	if(!result.contains("{")||!result.contains("}")) {
    		//System.out.println("no {}: "+result);
    		return scopeNode;
    	}
    	
    	if(scopeNode!=null){
    		List<ScopeTree> children=scopeNode.getChildren();
    		if(children!=null&&children.size()>0) {
    			ScopeTree lastNode=children.get(children.size()-1);//取出最后一个children
    			String head=result.substring(0,result.indexOf("{")+1);
        		String tail=result.substring(result.lastIndexOf("}"));
        		lastNode.setScopeHead(head);
        		lastNode.setScopeTail(tail);
        		lastNode.setScopeBodyFragment(result);
        		children.remove(children.size()-1);
        		children.add(lastNode);
        		scopeNode.setChildren(children);
    		}
    	}
    	return scopeNode;
	}

	/**
     * qurong
     * 2023.8.22
     * 作用域生成结束，将当前作用域更新至外层作用域
     * @param result
     * @param scopeNode
     */
    private ScopeTree updateScopeToFather(String result, ScopeTree scopeNode) {
    	//作用域的回溯
    	if(scopeNode!=null&&scopeNode.getFather()!=null){
            scopeNode=scopeNode.getFather();//当前作用域的上一级作用域
        }
        return scopeNode;
	}

	/**
     * qurong
     * 2023.3.23
     * 循环计数count声明语句
     * @return
     */
    private String getLoopCountIdentifier(String identifierCount,ScopeTree scopeNode) {
        String result="";
        if(ConfigureInfo.ifJavaCode){
            result= JavaConfigureInfo.loopCountType+" "+identifierCount+"=0;";
            return result;
        }else if(ConfigureInfo.ifCCode){
            result= CConfigureInfo.loopCountType+" "+identifierCount+"=0;";
            return result;
        }else if(ConfigureInfo.ifPythonCode){
            result=identifierCount+"=0"+PythonConfigureInfo.newline;//防止同一个作用域内多个并列的循环，声明count重名
            PythonGrammarGenerateService ps=new PythonGrammarGenerateService();
            String prefix=ps.getTabProfixByDepth(scopeNode.getDepth()-1);//缩进
            result=result+prefix;//防止同一个作用域内多个并列的循环，声明count重名
            return result;
        }else if(ConfigureInfo.ifCPlusCode){
            result= CPlusConfigureInfo.loopCountType+" "+identifierCount+"=0;";
            return result;
        }
        result+= ConfigureInfo.loopCountType+" "+identifierCount+"=0;";
        return result;
    }

    /**
     * qurong
     * 2022-11-15
     * 为文法中的item生成文法
     * @param item
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    private String generateProgramForItem(String item, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        String result="";
        Constraint constraint=new Constraint();
        if(isSpecialItem(item)){//文法中的特殊item
            result=generateSpecialPartForItem(item, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        }else{//文法中的普通item
            Map<String, Integer> tempTokenAppearTimes = copyMap(tokenAppearTimes);
            result = generateProgramForMatchedValue(item, index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            result = constraint.checkUniqueConstraint(result, item, index, grammars);//唯一性约束（*不可重复相同值）
            result = constraint.checkContinueConstraint(result, item, index, grammars);//连续性约束，，如果在约束列表中，temResult中不能有空格
        }
        return result;
    }

    /**
     * qurong
     * 2025-2-18
     * 为文法中的item生成文法
     * @param item
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    private List<String> generateProgramsForItem(String item, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        List<String> result=new ArrayList<>();
        Constraint constraint=new Constraint();
        if(isSpecialItem(item)){//文法中的特殊item
            result=generateProgramsIfSpecialPartForItem(item, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        }else{//文法中的普通item
            Map<String, Integer> tempTokenAppearTimes = copyMap(tokenAppearTimes);
            result = generateProgramsForMatchedValue(item, index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            result = constraint.checkUniqueConstraints(result, item, index, grammars);//唯一性约束（*不可重复相同值）
            result = constraint.checkContinueConstraints(result, item, index, grammars);//连续性约束，，如果在约束列表中，temResult中不能有空格
        }
        return result;
    }

    /**
     * qurong
     * 2022-11-14
     * 为文法中特殊的项生成代码片段
     * @param item
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    private String generateSpecialPartForItem(String item, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        PythonGrammarGenerateService pggs=new PythonGrammarGenerateService(grammars);
        GrammarGenerateForJava ggf=new GrammarGenerateForJava(grammars);
        GrammarGenerateForC ggc=new GrammarGenerateForC(grammars);
        GrammarGenerateForCPlus ggcplus=new GrammarGenerateForCPlus(grammars);
        String result="";
        if(ifJavaSpecialItem(item)){
            result=ggf.generateSpecialItemForJava(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }else if(ifCSpecialItem(item)){
            result=ggc.generateSpecialItemForC(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }else if(pggs.ifPythonSpecialItem(item)){
            result=pggs.generateSpecialItemForPython(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }else if(ifCPlusSpecialItem(item)){
	        result=ggcplus.generateSpecialItemForCPlus(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
	        return result;
	    }
        return result;
    }

    /**
     * qurong
     * 2025-2-18
     * 为文法中特殊的项生成代码片段
     * @param item
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    private List<String> generateProgramsIfSpecialPartForItem(String item, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        PythonGrammarGenerateService pggs=new PythonGrammarGenerateService(grammars);
        GrammarGenerateForJava ggf=new GrammarGenerateForJava(grammars);
        GrammarGenerateForC ggc=new GrammarGenerateForC(grammars);
        GrammarGenerateForCPlus ggcplus=new GrammarGenerateForCPlus(grammars);
        List<String> result=new ArrayList<>();
//        if(ifJavaSpecialItem(item)){
//            result=ggf.generateSpecialItemForJava(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
//            return result;
//        }else if(ifCSpecialItem(item)){
//            result=ggc.generateSpecialItemForC(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
//            return result;
//        }else if(pggs.ifPythonSpecialItem(item)){
//            result=pggs.generateSpecialItemForPython(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
//            return result;
//        }else
        if(ifCPlusSpecialItem(item)) {
            result = ggcplus.generateProgramsOfSpecialItemForCPlus(item, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode, lineOrder);
            return result;
        }
        return result;
    }


    /**
     * qurong
     * 2022-11-14
     * 判断文法中的单个item是否为特殊项
     * @param item
     * @return
     */
    private boolean isSpecialItem(String item) {
        PythonGrammarGenerateService pggs=new PythonGrammarGenerateService(grammars);
        if(ifJavaSpecialItem(item)){
            return true;
        }else if(ifCSpecialItem(item)){
            return true;
        }else if(pggs.ifPythonSpecialItem(item)){
            return true;
        }else if(ifCPlusSpecialItem(item)){
            return true;
        }
        return false;
    }


    /**
     * qurong
     * 2022-11-14
     * 为特殊文法部分生成程序片段并返回
     * @param str
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    private String generateIfSpecialGrammar(String str, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        String result="";
        PythonGrammarGenerateService pggs=new PythonGrammarGenerateService(grammars);
        GrammarGenerateForJava ggf=new GrammarGenerateForJava(grammars);
        GrammarGenerateForC ggc=new GrammarGenerateForC(grammars);
        GrammarGenerateForCPlus ggcplus=new GrammarGenerateForCPlus(grammars);
        if(ifJavaSpecialGrammar(str)){
            result=ggf.generateSpecialPartForJava(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }else if(ifCSpecialGrammar(str)){
            result=ggc.generateSpecialPartForC(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }else if(pggs.ifPythonSpecialGrammar(str)){
            result=pggs.generateSpecialPartForPython(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }else if(ifCPlusSpecialGrammar(str)){
	        result=ggcplus.generateSpecialPartForCPlus(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
	        return result;
	    }
        return result;
    }

    /**
     * qurong
     * 2025-2-18
     * 为特殊文法部分生成程序片段并返回
     * @param str
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    private List<String> generateProgramsIfSpecialGrammar(String str, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        List<String> result=new ArrayList<>();
        PythonGrammarGenerateService pggs=new PythonGrammarGenerateService(grammars);
        GrammarGenerateForJava ggf=new GrammarGenerateForJava(grammars);
        GrammarGenerateForC ggc=new GrammarGenerateForC(grammars);
        GrammarGenerateForCPlus ggcplus=new GrammarGenerateForCPlus(grammars);
        if(ifCPlusSpecialGrammar(str)){
            result=ggcplus.generatePrgramsOfSpecialPartForCPlus(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }
        return result;
    }

    /**
     * qurong
     * 2022-12-7
     * add tab especially for python
     * @param scopeNode
     * @return
     */
    private String getTabProfixByDepth(String stmt,ScopeTree scopeNode, String str) {
        String result="";
        if(ConfigureInfo.ifJavaCode){
            GrammarGenerateForJava ggf=new GrammarGenerateForJava();
            return ggf.getTabProfixByDepth(scopeNode)+str;
        }else if(ConfigureInfo.ifCCode){
            GrammarGenerateForC ggf=new GrammarGenerateForC();
            return ggf.getTabProfixByDepth(scopeNode)+str;
        }else if(ConfigureInfo.ifCPlusCode){
            GrammarGenerateForCPlus ggf=new GrammarGenerateForCPlus();
            return ggf.getTabProfixByDepth(scopeNode)+str;
        }else if(ConfigureInfo.ifPythonCode){
            if(!str.startsWith(PythonConfigureInfo.tab)){
                str=str.trim();//删去开头的多余空格
                PythonGrammarGenerateService pgg=new PythonGrammarGenerateService();
                String tabStr="";
                if(isMethod(stmt)){//方法作用域深度不变，其它需要-1
                    tabStr=pgg.getTabProfixByDepth(scopeNode.getDepth());
                }else{
                    tabStr=pgg.getTabProfixByDepth(scopeNode.getDepth()-1);
                }

                str=tabStr+str;
            }
            return str;
        }
        return result;
    }

    /**
     * qurong
     * 是否为方法生成语句
     * 2023.3.22
     * @param stmt
     * @return
     */
    private boolean isMethod(String stmt) {
        if(ConfigureInfo.ifJavaCode&&stmt!=""&&(JavaConfigureInfo.statementGrammars.contains(stmt))){
            return true;
        }else if(ConfigureInfo.ifCCode&&stmt!=""&&(CConfigureInfo.statementGrammars.contains(stmt))){
            return true;
        }else if(ConfigureInfo.ifCPlusCode&&stmt!=""&&(CPlusConfigureInfo.statementGrammars.contains(stmt))){
            return true;
        }else if(ConfigureInfo.ifPythonCode&&stmt!=""&&(PythonConfigureInfo.mthdStatementGrammars.contains(stmt))){
            return true;
        }
        return false;
    }


    /**
     * 是否为java中的特殊文法部分，例如声明方法、类型、变量的文法
     * @param str
     * @return
     */
    private boolean ifJavaSpecialGrammar(String str) {
        if(ConfigureInfo.ifJavaCode&&str!="") {
            switch(str.trim()){
                case JavaConfigureInfo.methodDeclaration:;
                case JavaConfigureInfo.classDeclaration:;
                case JavaConfigureInfo.publicClassDeclaration:;
                case JavaConfigureInfo.mainContent:;
                case JavaConfigureInfo.fieldDeclaration:;
                case JavaConfigureInfo.localDeclaration:return true;
                default:
            }
            if (JavaConfigureInfo.getVarnames().contains(str.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为C中的特殊文法部分，例如声明方法、类型、变量的文法
     * @param str
     * @return
     */
    private boolean ifCSpecialGrammar(String str) {
        if(str==null){
            //System.out.println("ah str is null! ");
            return false;
        }
        if(ConfigureInfo.ifCCode&&str!="") {
            switch(str.trim()){
                case CConfigureInfo.methodDeclaration:;
                case CConfigureInfo.mainContent:;
                case CConfigureInfo.fieldDeclaration:;
                case CConfigureInfo.localDeclaration:;
                case CConfigureInfo.fieldArrayDeclaration:;
                case CConfigureInfo.localArrayDeclaration:;
                case CConfigureInfo.fieldPointerDeclaration:;
                case CConfigureInfo.arrayElement:;
                case CConfigureInfo.pointerValue:;
                case CConfigureInfo.localPointerDeclaration:return true;
                default:
            }
            if (CConfigureInfo.getVarnames().contains(str.trim())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 是否为C中的特殊文法部分，例如声明方法、类型、变量的文法
     * @param str
     * @return
     */
    private boolean ifCPlusSpecialGrammar(String str) {
        if(str==null){
            //System.out.println("ah c++ str is null!");
            return false;
        }
        if(ConfigureInfo.ifCPlusCode&&str!="") {
            switch(str.trim()){
                case CPlusConfigureInfo.methodDeclaration:;
                case CPlusConfigureInfo.lamdaExpression:;
                case CPlusConfigureInfo.mainContent:;
                case CPlusConfigureInfo.classDeclaration:;
                case CPlusConfigureInfo.structDeclaration:;
                case CPlusConfigureInfo.templateFunDeclaration:;
                
                case CPlusConfigureInfo.fieldDeclaration:;
                case CPlusConfigureInfo.localDeclaration:;
                case CPlusConfigureInfo.fieldArrayDeclaration:;
                case CPlusConfigureInfo.localArrayDeclaration:;
                case CPlusConfigureInfo.fieldPointerDeclaration:;
                case CPlusConfigureInfo.arrayElement:;
                case CPlusConfigureInfo.pointerValue:;
                case CPlusConfigureInfo.localPointerDeclaration:return true;
                default:
            }
            if (CPlusConfigureInfo.getVarnames().contains(str.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为java中的特殊文法部分，例如声明方法、类型、变量的文法
     * @param str
     * @return
     */
    private boolean ifJavaSpecialItem(String str) {
        if(ConfigureInfo.ifJavaCode&&str!="") {
            switch(str.trim()){
                case JavaConfigureInfo.methodDeclaration:;
                case JavaConfigureInfo.classDeclaration:;
                case JavaConfigureInfo.publicClassDeclaration:;
                case JavaConfigureInfo.mainContent:;
                case JavaConfigureInfo.fieldDeclaration:;
                case JavaConfigureInfo.localDeclaration:return true;
                default:
            }
            if (JavaConfigureInfo.getVarnames().contains(str.trim())||JavaConfigureInfo.divisorGrammars.contains(str.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为C中的特殊文法部分，例如声明方法、类型、变量的文法
     * @param str
     * @return
     */
    private boolean ifCSpecialItem(String str) {
        if(ConfigureInfo.ifCCode&&str!="") {
            switch(str.trim()){
                case CConfigureInfo.methodDeclaration:;
                case CConfigureInfo.mainContent:;
                case CConfigureInfo.fieldDeclaration:;
                case CConfigureInfo.localDeclaration:;
                case CConfigureInfo.fieldArrayDeclaration:;
                case CConfigureInfo.localArrayDeclaration:;
                case CConfigureInfo.fieldPointerDeclaration:;
                case CConfigureInfo.arrayElement:;
                case CConfigureInfo.pointerValue:;
                case CConfigureInfo.localPointerDeclaration:return true;
                default:
            }
            if (CConfigureInfo.getVarnames().contains(str.trim())||CConfigureInfo.divisorGrammars.contains(str.trim())) {
                return true;
            }
        }
        return false;
    }

    
    /**
     * 是否为C++中的特殊文法部分，例如声明方法、类型、变量的文法
     * @param str
     * @return
     */
    private boolean ifCPlusSpecialItem(String str) {
        if(ConfigureInfo.ifCPlusCode&&str!="") {
            switch(str.trim()){
                case CPlusConfigureInfo.methodDeclaration:;
                case CPlusConfigureInfo.lamdaExpression:;
                case CPlusConfigureInfo.mainContent:;
                case CPlusConfigureInfo.classDeclaration:;
                case CPlusConfigureInfo.structDeclaration:;
                case CPlusConfigureInfo.templateFunDeclaration:;
                
                case CPlusConfigureInfo.fieldDeclaration:;
                case CPlusConfigureInfo.localDeclaration:;
                case CPlusConfigureInfo.fieldArrayDeclaration:;
                case CPlusConfigureInfo.localArrayDeclaration:;
                case CPlusConfigureInfo.fieldPointerDeclaration:;
                case CPlusConfigureInfo.arrayElement:;
                case CPlusConfigureInfo.pointerValue:;
                case CPlusConfigureInfo.localPointerDeclaration:return true;
                default:
            }
            if (CPlusConfigureInfo.getVarnames().contains(str.trim())||CPlusConfigureInfo.divisorGrammars.contains(str.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * qurong
     * 2022-11-14
     * 是否为循环相关文法
     * @param str
     * @return
     */
    private boolean ifLoopGrammar(String str) {
        if(ConfigureInfo.ifJavaCode&&str!=""&&(JavaConfigureInfo.loopGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifCCode&&str!=""&&(CConfigureInfo.loopGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(CPlusConfigureInfo.loopGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifPythonCode&&str!=""&&(PythonConfigureInfo.loopGrammars.contains(str))){
            return true;
        }
        return false;
    }
    /**
     * qurong
     * 2022-11-15
     * 是否为除数相关文法
     * @param str
     * @return
     */
    private boolean ifDivisorGrammar(String str) {
        if(ConfigureInfo.ifJavaCode&&str!=""&&(JavaConfigureInfo.divisorGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifCCode&&str!=""&&(CConfigureInfo.divisorGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(CPlusConfigureInfo.divisorGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifPythonCode&&str!=""&&(PythonConfigureInfo.divisorGrammars.contains(str))){
            return true;
        }
        return false;
    }
    /**
     * qurong
     * 2022-12-7
     * 是否为新语句相关文法
     * @param str
     * @return
     */
    private boolean ifStatementGrammar(String str) {
        if(ConfigureInfo.ifJavaCode&&str!=""&&(JavaConfigureInfo.statementGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifCCode&&str!=""&&(CConfigureInfo.statementGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(CPlusConfigureInfo.statementGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifPythonCode&&str!=""&&(PythonConfigureInfo.statementGrammars.contains(str))){
            return true;
        }
        return false;
    }
    
    /**
     * qurong
     * 2023-8-16
     * 是否为标识符定义或标识符赋值语句
     * @param str
     * @return
     */
    private boolean ifDefineOrModifyGrammar(String str) {
        if(ConfigureInfo.ifJavaCode&&str!=""&&(JavaConfigureInfo.defineOrModifyGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifCCode&&str!=""&&(CConfigureInfo.defineOrModifyGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(CPlusConfigureInfo.defineOrModifyGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifPythonCode&&str!=""&&(PythonConfigureInfo.defineOrModifyGrammars.contains(str))){
            return true;
        }
        return false;
    }

    /**
     * qurong
     * 2023.8.22
     * 判断是否为一个完整的作用域文法（包含作用域头尾）
     */
    private boolean ifScopeBodyGrammar(String str) {
        if(ConfigureInfo.ifJavaCode&&str!=""&&(JavaConfigureInfo.scopeBodyGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifCCode&&str!=""&&(CConfigureInfo.scopeBodyGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(CPlusConfigureInfo.scopeBodyGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifPythonCode&&str!=""&&(PythonConfigureInfo.scopeBodyGrammars.contains(str))){
            return true;
        }
        return false;
    }

    /**
     * qurong
     * 2024.12.24
     * 判断是否为一个完整的main函数文法（包含作用域头尾）
     */
    private boolean ifMainBodyGrammar(String str) {
        if(ConfigureInfo.ifJavaCode&&str!=""&&(JavaConfigureInfo.mainMethodDeclaration.equals(str))){
            return true;
        }else if(ConfigureInfo.ifCCode&&str!=""&&(CConfigureInfo.mainMethodDeclaration.equals(str))){
            return true;
        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(CPlusConfigureInfo.mainMethodDeclaration.equals(str))){
            return true;
        }else if(ConfigureInfo.ifPythonCode&&str!=""&&(PythonConfigureInfo.mainMethodDeclaration.equals(str))){
            return true;
        }
        return false;
    }

    
    /**
     * qurong
     * 2024.3.11
     * 判断是否为一个函数文法
     */
    private boolean ifMethodGrammar(String str) {
        if(ConfigureInfo.ifJavaCode&&str!=""&&(str.equals(JavaConfigureInfo.methodDeclaration))){
            return true;
        }else if(ConfigureInfo.ifCCode&&str!=""&&(str.equals(CConfigureInfo.methodDeclaration))){
            return true;
        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.equals(CPlusConfigureInfo.methodDeclarationTotal)||str.equals(CPlusConfigureInfo.methodDeclaration))){
            return true;
        }else if(ConfigureInfo.ifPythonCode&&str!=""&&(str.equals(PythonConfigureInfo.methodDeclaration))){
            return true;
        }
        return false;
    }
    
    /**
     * qurong
     * 2024.3.11
     * 判断是否为一个块文法
     */
    private boolean ifBlockGrammar(String str) {
        if(ConfigureInfo.ifJavaCode&&str!=""&&(str.equals(JavaConfigureInfo.blockDeclaration))){
            return true;
        }else if(ConfigureInfo.ifCCode&&str!=""&&(str.equals(CConfigureInfo.blockDeclaration))){
            return true;
        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(str.equals(CPlusConfigureInfo.blockDeclaration))){
            return true;
        }else if(ConfigureInfo.ifPythonCode&&str!=""&&(str.equals(PythonConfigureInfo.blockDeclaration))){
            return true;
        }
        return false;
    }
    
    /**
     * qurong
     * 2022-11-14
     * 判断是否为作用域起始文法
     * @param str
     * @return
     */
    private boolean ifScopeGrammar(String str) {
        if(ConfigureInfo.ifJavaCode&&str!=""&&(JavaConfigureInfo.scopeGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifCCode&&str!=""&&(CConfigureInfo.scopeGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(CPlusConfigureInfo.scopeGrammars.contains(str))){
            return true;
        }else if(ConfigureInfo.ifPythonCode&&str!=""&&(PythonConfigureInfo.scopeGrammars.contains(str))){
            return true;
        }
        return false;
    }


    /**
     * qurong
     * 2022-11-15
     * 判断是否为变量声明文法
     * @param str
     * @return
     */
    private boolean ifVarnameGrammar(String str) {
        if(ConfigureInfo.ifJavaCode&&str!=""&&(JavaConfigureInfo.getVarnames().contains(str))){
            return true;
        }else if(ConfigureInfo.ifCCode&&str!=""&&(CConfigureInfo.getVarnames().contains(str))){
            return true;
        }else if(ConfigureInfo.ifCPlusCode&&str!=""&&(CPlusConfigureInfo.getVarnames().contains(str))){
            return true;
        }else if(ConfigureInfo.ifPythonCode&&str!=""&&(PythonConfigureInfo.getVarnames().contains(str))){
            return true;
        }
        return false;
    }

    /**
     * 除数不能为零
     * @param str
     * @return
     */
    public String zeroExclide(String str) {
        if(ConfigureInfo.ifJavaCode){
            GrammarGenerateForJava ggf=new GrammarGenerateForJava();
            return ggf.zeroExclideJava(str);
        }else if(ConfigureInfo.ifCCode){
            GrammarGenerateForC ggf=new GrammarGenerateForC();
            return ggf.zeroExclideC(str);
        }else if(ConfigureInfo.ifCPlusCode){
            GrammarGenerateForCPlus ggf=new GrammarGenerateForCPlus();
            return ggf.zeroExclideC(str);
        }else if(ConfigureInfo.ifPythonCode){
            PythonGrammarGenerateService pgg=new PythonGrammarGenerateService();
            return pgg.zeroExclidePython(str);
        }else{
            str="(("+str+"==0)?"+"1:("+str+"))";
        }
        return str;
    }

    /**
     * 生成varname
     * @return
     */
    public String generateVarname(String strCopy, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        if(ConfigureInfo.ifJavaCode){
            GrammarGenerateForJava ggf=new GrammarGenerateForJava();
            return ggf.generateIdentifierByType( strCopy,  index, tokenAppearTimes,  ifNeedOutCycle,  st,  scopeNode,lineOrder);
        }else if(ConfigureInfo.ifCCode){
            GrammarGenerateForC ggf=new GrammarGenerateForC();
            return ggf.generateIdentifierByTypeC( strCopy,  index, tokenAppearTimes,  ifNeedOutCycle,  st,  scopeNode, CConfigureInfo.varnameCategoryMap.get(strCopy),"self",lineOrder);
        }else if(ConfigureInfo.ifCPlusCode){
            GrammarGenerateForCPlus ggf=new GrammarGenerateForCPlus();
            return ggf.generateIdentifierByTypeC( strCopy,  index, tokenAppearTimes,  ifNeedOutCycle,  st,  scopeNode, CPlusConfigureInfo.varnameCategoryMap.get(strCopy),"self",lineOrder);
        }else if(ConfigureInfo.ifPythonCode){
            PythonGrammarGenerateService pgg=new PythonGrammarGenerateService();
            return pgg.generateIdentifierByTypePython(strCopy,  index, tokenAppearTimes,  ifNeedOutCycle,  st,  scopeNode,lineOrder);
        }else{
            return "";
        }
    }

    /**
     * qurong
     * 2025-2-19
     * 生成varname
     * @return
     */
    public List<String> generateVarnames(String strCopy, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        List<String> result=new ArrayList<>();
        if(ConfigureInfo.ifJavaCode){
            GrammarGenerateForJava ggf=new GrammarGenerateForJava();
            System.out.println("Can I Enumerate for JAVA?");
            result.add(ggf.generateIdentifierByType( strCopy,  index, tokenAppearTimes,  ifNeedOutCycle,  st,  scopeNode,lineOrder));
        }else if(ConfigureInfo.ifCCode){
            GrammarGenerateForC ggf=new GrammarGenerateForC();
            System.out.println("Can I Enumerate for C?");
            result.add(ggf.generateIdentifierByTypeC( strCopy,  index, tokenAppearTimes,  ifNeedOutCycle,  st,  scopeNode, CConfigureInfo.varnameCategoryMap.get(strCopy),"self",lineOrder));
        }else if(ConfigureInfo.ifCPlusCode){
            GrammarGenerateForCPlus ggf=new GrammarGenerateForCPlus();
            result=ggf.generateIdentifiersByTypeC( strCopy,  index, tokenAppearTimes,  ifNeedOutCycle,  st,  scopeNode, CPlusConfigureInfo.varnameCategoryMap.get(strCopy),"self",lineOrder);
        }else if(ConfigureInfo.ifPythonCode){
            PythonGrammarGenerateService pgg=new PythonGrammarGenerateService();
            System.out.println("Can I Enumerate for Python?");
            result.add(pgg.generateIdentifierByTypePython(strCopy,  index, tokenAppearTimes,  ifNeedOutCycle,  st,  scopeNode,lineOrder));
        }else{
            return new ArrayList<>();
        }
        return result;
    }

    /**
     * 向循环中插入break语句，防止死循环
     * qurong
     * 2022.11.4
     * @param loopSegment
     * @param identifierCount
     * @return
     */
    private String insertBreakToLoop(String loopSegment, String identifierCount,ScopeTree scopeNode) {
        if(ConfigureInfo.ifJavaCode){
            GrammarGenerateForJava ggf=new GrammarGenerateForJava();
            return ggf.insertBreakToLoop(loopSegment, identifierCount);
        }else if(ConfigureInfo.ifCCode){
            GrammarGenerateForC ggf=new GrammarGenerateForC();
            return ggf.insertBreakToLoop(loopSegment, identifierCount);
        }else if(ConfigureInfo.ifCPlusCode){
            GrammarGenerateForCPlus ggf=new GrammarGenerateForCPlus();
            return ggf.insertBreakToLoop(loopSegment, identifierCount);
        }else if(ConfigureInfo.ifPythonCode){
            PythonGrammarGenerateService pgg=new PythonGrammarGenerateService();
            return pgg.insertBreakToLoop(loopSegment, identifierCount, scopeNode);
        }else{
            return "";
        }
    }

    /**
     * 从文法的路径中取出文法名称，例如JavaParser
     * @param index
     * @return
     */
    private String getGrammarName(int index) {
        String result="";
        String grammarName=grammars.get(index).getGrammarName();
        if(grammarName==null||grammarName.equals("")){
            String path=grammars.get(index).getPath();
            grammarName=path.substring(path.lastIndexOf(File.separator)+1,path.lastIndexOf("."));
            grammars.get(index).setGrammarName(grammarName);
            result=grammarName;
        }else{
            result=grammarName;
        }
        return result;
    }

    /**
     * 更新文法中key的所有子token
     * @param rootKeys
     */
    public void updateKeyAllTokens(List<String> rootKeys, int index) {
        if(rootKeys==null||rootKeys.size()==0||index<0||index>grammars.size()-1){
            return;
        }
        for(String key:rootKeys){
            grammars.get(index).updateAllTokensByKey(key);
        }
    }
    /**
     * 获取文法树的根节点
     * @return
     */
    public List<String> getRootKeys(int index) {
        List<String> roots=new ArrayList<>();
        Map<String,List<String>> vars=grammars.get(index).getVars();//文法变量键值map
        List<String> orderedList=grammars.get(index).getOrderedList();//所有的键key列表
        //如果不在任何值中出现的键，认为是一个root
        Set<String> keysInValsSet=new HashSet<>();//所有的key对应的value中出现的key
        for(Map.Entry<String,List<String>> entry : vars.entrySet()){
            List<String> valueList=entry.getValue();//单个key对应的value值
            Set<String> keyInValSet=getKeyFromValues(valueList,orderedList);//单个key对应的多个value中出现的key
            keysInValsSet.addAll(keyInValSet);
        }
        for(String key:orderedList){
            if(!keysInValsSet.contains(key)){
                roots.add(key);
            }
        }
        if(roots!=null&&roots.size()>1){
            ConfigureInfo.setMoreThanOneRoot(ConfigureInfo.getMoreThanOneRoot()+1);
        }
        return roots;
    }

    /**
     * 从一个字符串列表中找出所有包含的key，并加入到返回值set中
     * @param valueList
     * @return
     */
    public Set<String> getKeyFromValues(List<String> valueList,List<String> orderedList) {
        Set<String> result=new HashSet<>();
        if(valueList==null||valueList.size()==0){
            return result;
        }
        for(String val:valueList){
            Set<String> keys=getKeyFromValue(val,orderedList);//单个key对应的单个value中出现的key
            result.addAll(keys);
        }
        return result;
    }

    /**
     * 从一个字符串中找出所有包含的key，并加入到返回值set中
     * @param val
     * @param orderedList
     * @return
     */
    public Set<String> getKeyFromValue(String val, List<String> orderedList) {
        Set<String> result=new HashSet<>();
        if(val==null||val==""){
            return result;
        }
        StringTools st=new StringTools();
        Set<String> words=st.getWordsFromStr(val);
        for(String word:words){
            if(orderedList.contains(word)){
                result.add(word);
            }
        }
        return result;
    }


    /**
     *
     * @param rootKeys
     * @return
     */
    public String generateProgramByKey(List<String> rootKeys, int index,SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        String result="";
        //选择一个根节点key
        String selectKey=getKey(rootKeys,index);
        result=generateProgram(selectKey,index, st, scopeNode,lineOrder);
        return result;
    }

    /**
     *qurong
     * 2025-2-18
     * @param rootKeys
     * @return
     */
    public List<String> generateProgramsByKey(List<String> rootKeys, int index,SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        List<String> result=new ArrayList<>();
        //选择一个根节点key
        String selectKey=getKey(rootKeys,index);
        result=generatePrograms(selectKey,index, st, scopeNode,lineOrder);
        return result;
    }

    /**
     * 从多个key中选出一个作为本次生成程序的起点
     * @param rootKeys
     * @return
     */
    public String getKey(List<String> rootKeys,int index) {
        String result="";
        if(rootKeys==null||rootKeys.size()==0){
            //System.out.println(" no root to generate program ");
            return result;
        }else if(rootKeys.size()==1){
            return rootKeys.get(0);
        }
        //随机选择一个根节点key
//        result=getStrByRandom(rootKeys);

        //根据根节点的信息量来选择一个根节点key
        result=getKeyByIC(rootKeys,index);
        //System.out.println(result);
        return result;
    }

    /**
     * 根据节点的IC信息量值，选出信息量最大的节点返回
     * @param list
     * @return
     */
    public String getKeyByIC(List<String> list, int index) {
        String result="";
        if(list==null||list.size()==0){
            //System.out.println(" no root to generate program ");
            return result;
        }
        Map<String,Double> ICs=getICs(list,index);
        Double minIC=-1000.00;
        for(Map.Entry<String,Double> entry:ICs.entrySet()){
            if(entry.getValue()>minIC){
                minIC=entry.getValue();
                result=entry.getKey();
            }
        }
        return result;
    }

    /**
     * 选出list中IC值最大的一个key返回
     * @param list
     * @return
     */
    public Map<String, Double> getICs(List<String> list, int index) {
        Map<String,Double> result=new HashMap();
        if(list==null||list.size()==0){
            //System.out.println(" no root to generate program ");
            return result;
        }
        for(String node:list){
            Double ic=getIC(node,index);
            result.put(node,ic);
        }
        return result;
    }

    /**
     * 返回一个节点在一个文法中的IC值
     * @param node
     * @return
     */
    public Double getIC(String node, int index) {
        Double result=-1000.00;
        if(node==null||node==""||index<0||index>=grammars.size()){
            return result;
        }
//        Set<String> tokens=grammar.getAllTokensByKey(node);
        Set<String> tokens=grammars.get(index).getKeyAllTokensByKey(node);
        if(tokens==null){
            tokens=grammars.get(index).updateAllTokensByKey(node);
        }
        if(tokens!=null&&tokens.size()>0){
            result=Double.valueOf(tokens.size());
//            result=-Math.log(tokens.size());
        }
        return result;
    }

    /**
     * 采用随机的方式选出list中的一个String值返回
     * @param list
     * @return
     */
    public  String getStrByRandom(List<String> list) {
        String result="";
        if(list==null||list.size()==0){
            //System.out.println(" no root to generate program ");
            return result;
        }
        int length=list.size();
        if(length==1){
            result=list.get(0);
        }else{
            int random=(int)(Math.random()*length);
            result=list.get(random);
        }
        return result;
    }

    /**
     * 随机生成一个符合文法的程序
     * @param node
     * @return
     */
    public String generateProgramRandomByNode(String node, int index,SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        String result="";
        List<String> valueList=grammars.get(index).getValueByKey(node);//key的所有取值方式，竖线分割
        String selectedValue=getStrByRandom(valueList);//从取值方式中随机选一种
        Map<String,Integer> tokenAppearTimes=new HashMap<>();
        result=constructProgramByValue(selectedValue,index,tokenAppearTimes,false,st, scopeNode,lineOrder);
        return result;
    }

    /**
     * qurong
     * 2025-2-18
     * 随机生成多个符合文法的程序
     * @param node
     * @return
     */
    public List<String> generateProgramsRandomByNode(String node, int index,SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        List<String> result=new ArrayList<>();
        List<String> valueList=grammars.get(index).getValueByKey(node);//key的所有取值方式，竖线分割
        String selectedValue=getStrByRandom(valueList);//从取值方式中随机选一种
        Map<String,Integer> tokenAppearTimes=new HashMap<>();
        result=constructProgramsByValue(selectedValue,index,tokenAppearTimes,false,st, scopeNode,lineOrder);
        return result;
    }

    /**
     * 2022.10.27
     * qurong
     * @param st
     * @param type
     */
    public void updateSymTableByClass(SymbolTable st, Type type) {
        if(st==null||type==null){
            return;
        }
        List<String> identifierNameList=st.getIdentifierNameList();//所有的标识符的名称
        identifierNameList.add(type.getTypeName());
        st.setIdentifierNameList(identifierNameList);
//        Map<String,common.Type> typeMap=st.getTypeMap();//类型映射表，用类名来对应每一个类
//        typeMap.put(type.getTypeName(),type);
//        st.setTypeMap(typeMap);
        Map<String,Type> cusTypeMap=st.getCustomizedTypeMap();//自定义类型映射表，用类名来对应每一个类
        cusTypeMap.put(type.getTypeName(),type);
        st.setCustomizedTypeMap(cusTypeMap);
    }

    /**
     * 更新当前的作用域节点
     * @param st
     * @param scopeNode
     */
    public ScopeTree updateScope(SymbolTable st, ScopeTree scopeNode) {
        int scopeId=st.getScopeNum()+1;
        ScopeTree scopeChildNode=new ScopeTree(scopeId,scopeNode);//作用域节点
        if(scopeNode.getChildren()!=null&&scopeNode.getChildren().size()>0){//当前作用域已有子作用域，因此新的作用域的ID可以按照1-3-1这种形式来确定，表示第一个作用域的第三个子作用域下的第一个子作用域
            int childrenOrder=scopeNode.getChildren().size()+1;
            String parentPosition=scopeNode.getPosition();
            scopeChildNode.setPosition(parentPosition+"-"+childrenOrder);
        }else{
            String parentPosition=scopeNode.getPosition();
            scopeChildNode.setPosition(parentPosition+"-1");
        }
        //作用域深度
        int parentDepth=scopeNode.getDepth();
        scopeChildNode.setDepth(parentDepth+1);

        scopeChildNode.setRoot(scopeNode.getRoot());
        scopeChildNode.setLocateType(scopeNode.getLocateType());
        scopeChildNode.setType(scopeNode.getType());
        List<ScopeTree> children=scopeNode.getChildren();
        children.add(scopeChildNode);
        scopeNode.setChildren(children);
        st.setScopeNum(scopeId);
        scopeNode=scopeChildNode;
        return scopeNode;
    }

    /**
     * 2022-10-12
     * 筛选出当前作用域中某些类型的标识符名称
     * @param st
     * @param scopeNode
     * @param types
     * @return
     */
    public List<SymbolRecord> getSymbolRecordByType(SymbolTable st, ScopeTree scopeNode, Set<String> types) {
        List<SymbolRecord> result=new ArrayList<>();
        List<SymbolRecord> symbolRecords=st.getSymbolRecords();
        if(symbolRecords!=null){
            for(SymbolRecord sr : symbolRecords){
                try {
                    if (types.contains(sr.getType().getTypeName()) && sr.getScope().getScopeID() == scopeNode.getScopeID()) {
                        result.add(sr);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    if(sr==null) {
                        System.out.println("some symbol null");
                        continue;
                    }else if(sr.getType()==null){
                        System.out.println("some symbol "+sr.getName()+" type null");
                        continue;
                    }else if(sr.getType().getTypeName()==null){
                        System.out.println("some symbol type name null");
                        continue;
                    }else if(sr.getScope()==null){
                        System.out.println("some symbol scope null");
                        continue;
                    }else if(scopeNode==null){
                        System.out.println("some symbol scope null scopeNode");
                        continue;
                    }else if(types==null){
                        System.out.println("some null types");
                        continue;
                    }else {
                        System.out.println("some null ");
                        continue;
                    }

                }
            }
        }
        return result;
    }

    /**
     * 2022-10-12
     * 筛选出当前作用域中某些类型的标识符名称
     * @param st
     * @param scopeNode
     * @param types
     * @return
     */
    public List<SymbolRecord> getSymbolRecordByScopeAndType(SymbolTable st, ScopeTree scopeNode, Set<String> types) {
        List<SymbolRecord> result=new ArrayList<>();
        List<SymbolRecord> symbolRecords=st.getSymbolRecords();
        if(symbolRecords!=null){
            for(SymbolRecord sr : symbolRecords){
                Type t=sr.getType();
                if(t!=null){
                    String tname=t.getTypeName();
                    ScopeTree snode=sr.getScope();
                    int sID=snode.getScopeID();
                    if(types.contains(tname)&&sID==scopeNode.getScopeID()){
                        result.add(sr);
                    }
                }
            }
        }
        return result;
    }


    /**
     * 2023.3.17
     * 筛选出当前作用域中标识符名称
     * @param st
     * @param scopeNode
     * @return
     */
    public List<SymbolRecord> getSymbolRecordByScope(SymbolTable st, ScopeTree scopeNode) {
        List<SymbolRecord> result=new ArrayList<>();
        List<SymbolRecord> symbolRecords=st.getSymbolRecords();
        if(symbolRecords!=null){
            for(SymbolRecord sr : symbolRecords){
                if(sr.getScope().getScopeID()==scopeNode.getScopeID()){
                    result.add(sr);
                }
            }
        }
        return result;
    }

    /**
     * 2022-10-12
     * 获取当前作用域以及父级作用域的ID
     * @param scopeNode
     * @return
     */
    public List<Integer> getValidScopeIDs(ScopeTree scopeNode) {
        List<Integer> result=new ArrayList<>();

        if(scopeNode==null){
            return result;
        }
        ScopeTree tempScope=scopeNode;
        while(tempScope!=null){
            result.add(tempScope.getScopeID());
            tempScope=tempScope.getFather();
        }
        return result;
    }
    
    /**
     * 2023-7-14
     * 获取父级作用域的ID
     * @param scopeNode
     * @return
     */
    public List<Integer> getValidFatherScopeIDs(ScopeTree scopeNode) {
        List<Integer> result=new ArrayList<>();

        if(scopeNode==null){
            return result;
        }
        ScopeTree tempScope=scopeNode.getFather();
        while(tempScope!=null){
            result.add(tempScope.getScopeID());
            tempScope=tempScope.getFather();
        }
        return result;
    }

    /**
     * qurong
     * 2022.10.30
     * @param index
     * @param tempTokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    public String getIdentifier(int index, Map<String, Integer> tempTokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        String identifier="";
        Constraint constraint=new Constraint();
        String tempItem="identifier";
        if(ConfigureInfo.ifCCode) {
        	tempItem= CConfigureInfo.identifierToken;
        }else if(ConfigureInfo.ifCPlusCode) {
        	tempItem= CPlusConfigureInfo.identifierToken;
        }else if(ConfigureInfo.ifJavaCode) {
        	tempItem= JavaConfigureInfo.identifierToken;
        }else if(ConfigureInfo.ifPythonCode) {
        	tempItem= PythonConfigureInfo.identifierToken;
        }
        
        String temResult = generateProgramForMatchedValue(tempItem, index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        temResult = constraint.checkUniqueConstraint(temResult, tempItem, index, grammars);//唯一性约束（*不可重复相同值）
        temResult = constraint.checkContinueConstraint(temResult, tempItem, index, grammars);//连续性约束，，如果在约束列表中，temResult中不能有空格
        identifier = temResult;
        List<String> identifierNameList = st.getIdentifierNameList();//所有的标识符的名称
        while (!checkIdentifierValid(identifier, identifierNameList)) {//如果是标识符的话，尽量和已有标识符不同名，也和关键字不同名
            identifier = generateProgramForMatchedValue(tempItem, index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            identifier = constraint.checkUniqueConstraint(identifier, tempItem, index, grammars);//唯一性约束（*不可重复相同值）
            identifier = constraint.checkContinueConstraint(identifier, tempItem, index, grammars);//连续性约束，，如果在约束列表中，temResult中不能有空格
        }
        return identifier;
    }

    /**
     * 2022-10-24
     * 更新符号表，新增一条函数记录
     * @param st
     * @param sr
     */
    public void updateSymTableByFun(SymbolTable st, FunctionSymbolRecord sr) {
        if(st==null||sr==null){
            return;
        }
        List<FunctionSymbolRecord>  functionSymbolRecord=st.getFunctionSymbolRecords();//变量名
        List<String> identifierNameList=st.getIdentifierNameList();//所有的标识符的名称
        identifierNameList.add(sr.getName());
        st.setIdentifierNameList(identifierNameList);
        functionSymbolRecord.add(sr);
        st.setFunctionSymbolRecords(functionSymbolRecord);
    }
    
    /**
     * 2024-2-2
     * 更新符号表，新增一条Lamda表达式记录
     * @param st
     * @param sr
     */
    public void updateSymTableByLamda(SymbolTable st, LamdaSymbolRecord sr) {
        if(st==null||sr==null){
            return;
        }
        List<LamdaSymbolRecord>  lamdaSymbolRecord=st.getLamdaSymbolRecords();//变量名
        List<String> identifierNameList=st.getIdentifierNameList();//所有的标识符的名称
        identifierNameList.add(sr.getName());
        st.setIdentifierNameList(identifierNameList);
        lamdaSymbolRecord.add(sr);
        st.setLamdaSymbolRecords(lamdaSymbolRecord);
    }
    

    /**
     * 2022-10-30
     * qurong
     * 更新符号表，新增一条变量记录
     * @param st
     * @param indentifier
     */
    private void updateSymbolIdentifer(SymbolTable st, String  indentifier) {
        if(st==null||indentifier==""){
            return;
        }
        List<String> identifierNameList=st.getIdentifierNameList();//所有的标识符的名称
        identifierNameList.add(indentifier);
        st.setIdentifierNameList(identifierNameList);
    }
    /**
     * 2022-10-11
     * 更新符号表，新增一条变量记录
     * @param st
     * @param sr
     */
    public void updateSymTableByVar(SymbolTable st, SymbolRecord sr) {
        if(st==null||sr==null){
            return;
        }
        List<SymbolRecord>  varSymbolRecords=st.getVarSymbolRecords();//变量名
        List<String> identifierNameList=st.getIdentifierNameList();//所有的标识符的名称
        List<SymbolRecord>  symbolRecords=st.getSymbolRecords();//符号总表

        varSymbolRecords.add(sr);
        identifierNameList.add(sr.getName());
        symbolRecords.add(sr);

        st.setVarSymbolRecords(varSymbolRecords);
        st.setIdentifierNameList(identifierNameList);
        st.setSymbolRecords(symbolRecords);
    }
    
    /**
     * 2023-7-20
     * 更新符号表，更新一条变量记录，主要更新作用域使用次数Map
     * @param st
     * @param symbolRecord
     */
    public void updateSymTableBySymbolRecord(SymbolTable st, SymbolRecord symbolRecord) {
        if(st==null||symbolRecord==null){
            return;
        }
        List<SymbolRecord>  varSymbolRecords=st.getVarSymbolRecords();//变量名
        List<SymbolRecord>  newVarSymbolRecords=new ArrayList<>();//更新后的变量名
        for(SymbolRecord sr:varSymbolRecords ) {
        	if(sr.getID().equals(symbolRecord.getID())) {
        		newVarSymbolRecords.add(symbolRecord);
        	}else {
        		newVarSymbolRecords.add(sr);
        	}
        }
        st.setVarSymbolRecords(newVarSymbolRecords);

        
        List<SymbolRecord>  symbolRecords=st.getSymbolRecords();//符号总表
        List<SymbolRecord>  newSymbolRecords=new ArrayList<>();//更新后的符号总表
        for(SymbolRecord sr:symbolRecords ) {
        	if(sr.getID().equals(symbolRecord.getID())||sr.getName().equals(symbolRecord.getName())) {
        		newSymbolRecords.add(symbolRecord);
        	}else {
        		newSymbolRecords.add(sr);
        	}
        }
        st.setSymbolRecords(newSymbolRecords);
    }
    
    
    
    /**
     * 2023-10-2
     * 更新符号表，更新一条变量记录，主要更新作用域使用次数Map
     * @param st
     * @param symbolRecord
     */
    public void updateSymTableBySymbolRecord1(SymbolTable st, SymbolRecord symbolRecord, SymbolTable newSt) {
        if(st==null||symbolRecord==null){
            return;
        }
        List<SymbolRecord>  varSymbolRecords=st.getVarSymbolRecords();//变量名
        List<SymbolRecord>  newVarSymbolRecords=new ArrayList<>();//更新后的变量名
        for(SymbolRecord sr:varSymbolRecords ) {
        	if(sr.getID().equals(symbolRecord.getID())) {
        		newVarSymbolRecords.add(symbolRecord);
        	}else {
        		newVarSymbolRecords.add(sr);
        	}
        }
        st.setVarSymbolRecords(newVarSymbolRecords);
        newSt.setVarSymbolRecords(newVarSymbolRecords);

        
        List<SymbolRecord>  symbolRecords=st.getSymbolRecords();//符号总表
        List<SymbolRecord>  newSymbolRecords=new ArrayList<>();//更新后的符号总表
        for(SymbolRecord sr:symbolRecords ) {
        	if(sr.getID().equals(symbolRecord.getID())||sr.getName().equals(symbolRecord.getName())) {
        		newSymbolRecords.add(symbolRecord);
        	}else {
        		newSymbolRecords.add(sr);
        	}
        }
        st.setSymbolRecords(newVarSymbolRecords);
        newSt.setSymbolRecords(newSymbolRecords);
    }
    
    /**
     * 2023-9-22
     * 更新符号表，更新一条变量记录，主要更新作用域使用次数Map
     * @param st
     * @param symbolRecord
     */
    public void updateSymTableBySymbolRecord1(SymbolTable st, SymbolRecord symbolRecord) {
        if(st==null||symbolRecord==null){
            return;
        }
        List<SymbolRecord>  varSymbolRecords=st.getVarSymbolRecords();//变量名
        List<SymbolRecord>  newVarSymbolRecords=new ArrayList<>();//更新后的变量名
        for(SymbolRecord sr:varSymbolRecords ) {
        	if(sr.getID().equals(symbolRecord.getID())) {
        		newVarSymbolRecords.add(symbolRecord);
        	}else {
        		newVarSymbolRecords.add(sr);
        	}
        }
        st.setVarSymbolRecords(newVarSymbolRecords);

        
        List<SymbolRecord>  symbolRecords=st.getSymbolRecords();//符号总表
        List<SymbolRecord>  newSymbolRecords=new ArrayList<>();//更新后的符号总表
        for(SymbolRecord sr:symbolRecords ) {
        	if(sr.getID().equals(symbolRecord.getID())||sr.getName().equals(symbolRecord.getName())) {
        		newSymbolRecords.add(symbolRecord);
        	}else {
        		newSymbolRecords.add(sr);
        	}
        }
        st.setSymbolRecords(newSymbolRecords);
    }
    
    
    
    /**
     * 2023-7-20
     * 更新符号表，更新一条变量记录，主要更新作用域使用次数Map
     * @param st
     * @param scopeNode
     */
    public void updateSymTableBySymbolRecord(SymbolTable st, ScopeTree scopeNode, String identifier) {
        if(st==null||scopeNode==null||identifier==""){
            return;
        }
        List<SymbolRecord>  varSymbolRecords=st.getVarSymbolRecords();//变量
        List<SymbolRecord>  newVarSymbolRecords=new ArrayList<>();//更新后的变量
        for(SymbolRecord sr:varSymbolRecords ) {
        	if(sr.getID().equals(identifier)) {
        		SymbolRecord tempSr=new SymbolRecord(sr.getName(), sr.getType(),sr.getID(), sr.getCategory(),  sr.getScope(), sr.getValue(), sr.getArrayLength());
        		Map<Integer,Integer> scopeUsedTimeMap=sr.getScopeUsedTimeMap();
        		tempSr.setScopeUsedTimeMap(scopeUsedTimeMap);
        		tempSr.setScopeUsedTimeMapByData(scopeNode,1);
        		newVarSymbolRecords.add(tempSr);
        	}else {
        		newVarSymbolRecords.add(sr);
        	}
        }
        st.setVarSymbolRecords(newVarSymbolRecords);

        
        List<SymbolRecord>  symbolRecords=st.getSymbolRecords();//符号总表
        List<SymbolRecord>  newSymbolRecords=new ArrayList<>();//更新后的符号总表
        for(SymbolRecord sr:symbolRecords ) {
        	if(sr.getID().equals(identifier)) {
        		SymbolRecord tempSr=new SymbolRecord(sr.getName(), sr.getType(),sr.getID(), sr.getCategory(),  sr.getScope(), sr.getValue(), sr.getArrayLength());
        		Map<Integer,Integer> scopeUsedTimeMap=sr.getScopeUsedTimeMap();
        		tempSr.setScopeUsedTimeMap(scopeUsedTimeMap);
        		tempSr.setScopeUsedTimeMapByData(scopeNode,1);
        		newSymbolRecords.add(tempSr);
        	}else {
        		newSymbolRecords.add(sr);
        	}
        }
        st.setSymbolRecords(newSymbolRecords);
    }

    /**
     * 2022-10-30
     * qurong
     * 更新符号表，新增一条创建的对象记录
     * @param st
     * @param sr
     */
    public void updateSymTableByObj(SymbolTable st, SymbolRecord sr) {
        if(st==null||sr==null){
            return;
        }
        List<SymbolRecord>  varSymbolRecords=st.getVarSymbolRecords();//变量名
        List<SymbolRecord>  objSymbolRecords=st.getObjSymbolRecords();//对象名
        List<String> identifierNameList=st.getIdentifierNameList();//所有的标识符的名称
        List<SymbolRecord>  symbolRecords=st.getSymbolRecords();//符号总表
        varSymbolRecords.add(sr);
        objSymbolRecords.add(sr);
        identifierNameList.add(sr.getName());
        symbolRecords.add(sr);
        st.setVarSymbolRecords(varSymbolRecords);
        st.setObjSymbolRecords(objSymbolRecords);
        st.setIdentifierNameList(identifierNameList);
        st.setSymbolRecords(symbolRecords);
    }

    /**
     * 2022-10-11
     * 检查标识符命名合法性
     * @param identifier
     * @param identifierNameList
     * @return
     */
    private boolean checkIdentifierValid(String identifier, List<String> identifierNameList) {
        boolean result=false;
        if(identifierNameList.contains(identifier)){
            return result;
        }
        if(JavaConfigureInfo.getKeyWords().contains(identifier)){
            return result;
        }
        else if(CConfigureInfo.getKeyWords().contains(identifier)){
            return result;
        }
        else if(CPlusConfigureInfo.getKeyWords().contains(identifier)){
            return result;
        }else if(PythonConfigureInfo.getKeyWords().contains(identifier)){
            return result;
        }
        
        if(ConfigureInfo.ifCCode) {
        	for(String keyword:CConfigureInfo.getKeyWords()) {
        		if(identifier.contains(keyword)) {
        			return result;
        		}
        	}
        }else if(ConfigureInfo.ifCPlusCode) {
        	for(String keyword:CPlusConfigureInfo.getKeyWords()) {
        		if(identifier.contains(keyword)) {
        			return result;
        		}
        	}
        }
        return true;
    }


    /**
     * 实现map的copy;
     * @param map
     * @return
     */
    public Map<String, Integer> copyMap(Map<String, Integer> map) {
        Map<String, Integer> result=new HashMap<>();
        if(map==null||map.size()==0){
            return result;
        }
        for(Map.Entry<String,Integer> entry:map.entrySet()){
            result.put(entry.getKey(),entry.getValue());
        }
        return result;
    }




    /**
     * 针对括号已匹配的文法生成程序
     * @param item
     * @param index
     * @return
     */

    public String generateProgramForMatchedValue(String item, int index,Map<String,Integer> tokenAppearTimes,boolean ifNeedOutCycle,SymbolTable st1,ScopeTree scopeNode,int lineOrder) {
        if(item==null||item.trim()==""){
            return "";
        }

        //声明标识符相关
        if(ifVarnameGrammar(item)){
            return generateVarname(item, index,tokenAppearTimes, ifNeedOutCycle, st1, scopeNode,lineOrder);
        }
        //作用域相关
        if(ifScopeGrammar(item)){
            scopeNode=updateScope(st1,scopeNode);
        }
        
        String result="";
        item=item.trim();
        if(item==""){
            //System.out.println(" item is null : "+item);
            return " ";
        }
        StringTools st=new StringTools();
        int type=st.judgeTokenOrRegex(item);//判断tempItem是正则表达式 还是token表达式 还是''表达式
        if(type==1){//token类型
            if(item.endsWith("*")||item.endsWith("?")||item.endsWith("+")){
                result+=constructProgramByTokenMulti(item,index, tokenAppearTimes, ifNeedOutCycle,st1, scopeNode,lineOrder);
            }else {
                String afterBracketStr = backBracket(item);//) 非')',)结尾返回"",)abc返回abc，没有括号返回null
                result+=constructProgramByTokenSingle(item, afterBracketStr, index,  tokenAppearTimes, ifNeedOutCycle,st1, scopeNode,lineOrder);
                if (afterBracketStr != null && afterBracketStr != "") {
                    item = new String();
                    item = afterBracketStr;//这个区别于tempItem="";
                    //System.out.println(" some characters after ) "+item);
                }else{
                }
            }
        }else {//正则表达式或者字符串类型
            result+=constructProgram(item);
        }
        
        //标识符定义或修改相关相关
        if(ifDefineOrModifyGrammar(item)){
            //记录生成此文法的过程中，关联到的所有标识符
        	updateIdentifierRelations(item,result,scopeNode,st1,lineOrder);//更新当前程序中在当前作用域内各个标识符之间的关系
        }
        
        return result;
    }

    /**
     * qurong
     * 2025-2-19
     * 针对括号已匹配的文法生成程序
     * @param item
     * @param index
     * @return
     */

    public List<String> generateProgramsForMatchedValue(String item, int index,Map<String,Integer> tokenAppearTimes,boolean ifNeedOutCycle,SymbolTable st1,ScopeTree scopeNode,int lineOrder) {
        if(item==null||item.trim()==""){
            return new ArrayList<>();
        }

        //声明标识符相关
        if(ifVarnameGrammar(item)){
            return generateVarnames(item, index,tokenAppearTimes, ifNeedOutCycle, st1, scopeNode,lineOrder);
        }
        //作用域相关
        if(ifScopeGrammar(item)){
            scopeNode=updateScope(st1,scopeNode);
        }

        List<String> result=new ArrayList<>();
        StringTools stringTools=new StringTools();
        item=item.trim();
        if(item==""){
            //System.out.println(" item is null : "+item);
            result.add(" ");
            return result;
        }
        StringTools st=new StringTools();
        int type=st.judgeTokenOrRegex(item);//判断tempItem是正则表达式 还是token表达式 还是''表达式
        if(type==1){//token类型
            if(item.endsWith("*")||item.endsWith("?")||item.endsWith("+")){
                result=stringTools.combineStrList(result,constructProgramsByTokenMulti(item,index, tokenAppearTimes, ifNeedOutCycle,st1, scopeNode,lineOrder));
            }else {
                String afterBracketStr = backBracket(item);//) 非')',)结尾返回"",)abc返回abc，没有括号返回null
                result=stringTools.combineStrList(result,constructProgramsByTokenSingle(item, afterBracketStr, index,  tokenAppearTimes, ifNeedOutCycle,st1, scopeNode,lineOrder));
                if (afterBracketStr != null && afterBracketStr != "") {
                    item = new String();
                    item = afterBracketStr;//这个区别于tempItem="";
                    //System.out.println(" some characters after ) "+item);
                }else{
                }
            }
        }else {//正则表达式或者字符串类型
            result=stringTools.combineStrList(result,constructPrograms(item));
        }

        //标识符定义或修改相关相关
        if(ifDefineOrModifyGrammar(item)){
            //记录生成此文法的过程中，关联到的所有标识符
            if(result!=null&&result.size()>0){
                for(String str:result){
                    updateIdentifierRelations(item,str,scopeNode,st1,lineOrder);//更新当前程序中在当前作用域内各个标识符之间的关系
                }
            }
        }

        return result;
    }



    /**
     * qurong
     * 2025-2-19 将List<String>中的字符串，和post字符串，两两拼接形成新的字符型列表
     * @param pre
     * @param post
     * @return
     */
    private List<String> combineListAndStr(List<String> pre, String post) {
        List<String> result=new ArrayList<>();
        if(pre==null||pre.size()==0){
            result.add(post);
        }else{
            for(String resPre:pre){
                if(post==null)
                {
                    result.add(resPre);
                }else {
                    result.add(resPre + " " +post);
                }
            }
        }
        return result;
    }
    /**
     * 如果是定义或修改标识符取值的文法，更新标识符之间的关系
     * 2023.8.17
     * @param grammar
     * @param statement
     * @param scopeNode
     * @param st
     */

    public void updateIdentifierRelations(String grammar, String statement, ScopeTree scopeNode,SymbolTable st,int lineOrder) {
		   
    	if(statement=="") {
    		return;
    	}
    	if(!statement.contains("=")) {//例如函数调用fun(a,b),需要建立a,b和函数fun之间的关系，以及a,b和形参对应的标识符之间的关系
    		List<SymbolRecord> relatedIdentifiers=st.getIdentifiersFromStr(statement,st);//从函数调用中提取出标识符名称
    		List<FunctionSymbolRecord> relatedFuns=st.getFunsFromStr(statement);//从函数调用中提取出函数标识符名称
    		updateParameterRelations(relatedIdentifiers,relatedFuns,scopeNode,st, statement, lineOrder);
    		return;
    	}
    	String[] parts=statement.split("=");
    	if(parts.length<=1) {
    		return;
    	}
    	if(parts.length>=2) {//a=(b<=c?b:c) 可能会出现多个等号
    		
    		String left=statement.substring(0, statement.indexOf("="));//等式左边
    		String right=statement.substring(statement.indexOf("=")+1);//等式右边
    		
    		
    		
    		SymbolRecord identifier=getIdentifierFromStr(left,st);//从等式左侧提取出标识符名称
    		List<SymbolRecord> relatedIdentifiers=st.getIdentifiersFromStr(right,st);//从等式右侧提取出标识符名称
    		List<FunctionSymbolRecord> relatedFuns=st.getFunsFromStr(right);//从等式右侧提取出函数标识符名称
    		
    		
    		if(identifier==null&&containsLetter(left)) {
    			//System.out.println("identifier==null&&containsLetter(left)");
    		}
    		
    		if(relatedIdentifiers==null&&relatedFuns==null&&containsLetter(right)) {
    			//System.out.println("relatedIdentifiers==null&&relatedFuns==null&&containsLetter(right)");
    		}
    		
    		
    		if(identifier!=null&&relatedIdentifiers!=null&&relatedIdentifiers.size()>0) {
    			//System.out.println("identifier!=null&&relatedIdentifiers!=null&&relatedIdentifiers.size()>0");
//    			SymbolTable newSt=new SymbolTable();
//    			updateIdentifierScopeRelation(identifier,scopeNode, st, statement, lineOrder,newSt);//更新标识符和作用域之间的关系
//    			updateIdentifiersRelation(identifier,scopeNode, st, statement, lineOrder,relatedIdentifiers,newSt);//更新标识符和标识符之间关系
    		}
    		
    		if(identifier==null||((relatedIdentifiers==null||relatedIdentifiers.size()==0)&&(relatedFuns==null||relatedFuns.size()==0))) {//不是一个赋值语句
    			if(identifier!=null) {
//    				updateIdentifierScopeRelation(identifier,scopeNode, st, statement, lineOrder);
    				updateIdentifier(identifier,scopeNode,st, statement, lineOrder,relatedIdentifiers,relatedFuns);
    				return;
    			}
    			//System.out.println("what's this? "+statement);
    		}else {
    			updateIdentifier(identifier,scopeNode,st, statement, lineOrder,relatedIdentifiers,relatedFuns);
    		}
    		if(identifier!=null&&relatedFuns!=null&&relatedFuns.size()>0) {
    			updateParameterAndResultRelations(identifier,relatedIdentifiers,relatedFuns,scopeNode,st, statement, lineOrder);
    		}
    		
    	}
    	return;
		
	}
    
    /**
     * qurong
     * 2023.10.02
     * 判断是否包含字母
     * @param str
     * @return
     */
    public static boolean containsLetter(String str) {
        String pattern = ".*[a-zA-Z].*";
        return Pattern.matches(pattern, str);
    }

    /**
     * qurong
     * 2023.9.21
     * 函数调用且有返回值，把函数的实参加入到实参列表，并且将函数的实参和形参关联起来，将返回值和实参、形参、return中的标识符都关联起来
     * @param identifier
     * @param relatedIdentifiers
     * @param relatedFuns
     * @param scopeNode
     * @param st
     * @param statement
     * @param lineOrder
     */
    private void updateParameterAndResultRelations(SymbolRecord identifier, List<SymbolRecord> relatedIdentifiers,
			List<FunctionSymbolRecord> relatedFuns, ScopeTree scopeNode, SymbolTable st, String statement,
			int lineOrder) {
		// TODO Auto-generated method stub
    	//把赋值对象加入到实际赋值标识符列表中
		List<SymbolRecord> actrualReturnIdentifiers=relatedFuns.get(0).getActrualReturnIdentifiers();
		actrualReturnIdentifiers.add(identifier);
		relatedFuns.get(0).setActrualReturnIdentifiers(actrualReturnIdentifiers);
		
		//如果标识符是数组或者指针，把函数的参数关联起来
		List<SymbolRecord> returnIdentifierList=new ArrayList<>();
		returnIdentifierList.add(identifier);
		for(SymbolRecord id:relatedIdentifiers) {
//			if(id.getCategory().equals("array")||id.getCategory().equals("pointer")) {
//				updateIdentifier(id,scopeNode,st, statement, lineOrder,returnIdentifierList,relatedFuns);
				updateIdentifiersRelation(id,scopeNode, st, statement, lineOrder,returnIdentifierList);//更新标识符和标识符之间关系
//			}
		}
		
    	updateParameterRelations(relatedIdentifiers,relatedFuns,scopeNode,st, statement, lineOrder);
	}
    
    /**
     * qurong
     * 2023.9.21
     * 仅函数调用，返回值为void，把函数的实参加入到实参列表，并且将函数的实参和形参关联起来
     * @param relatedIdentifiers
     * @param relatedFuns
     * @param scopeNode
     * @param st
     * @param statement
     * @param lineOrder
     */
	private void updateParameterRelations(List<SymbolRecord> relatedIdentifiers,
			List<FunctionSymbolRecord> relatedFuns,ScopeTree scopeNode,SymbolTable st,String statement,int lineOrder) {
		// TODO Auto-generated method stub
    	if(relatedFuns!=null&&relatedFuns.size()>0) {//包含函数调用
			if(relatedIdentifiers!=null&&relatedIdentifiers.size()>0) {//函数有参数
				if(relatedFuns.size()!=1) {
					//System.out.println("more than one function call in one line");
					return;
				}
				//把实参加入到实参列表里
				List<SymbolRecord> actrualParameterIdentifiers=relatedFuns.get(0).getActrualParameterIdentifiers();
				actrualParameterIdentifiers.addAll(relatedIdentifiers);
				relatedFuns.get(0).setActrualParameterIdentifiers(actrualParameterIdentifiers);
				
				//把函数和实参关联起来
				for(SymbolRecord identifier:relatedIdentifiers) {
					updateIdentifierScopeRelation(identifier,scopeNode, st, statement, lineOrder);//更新标识符和作用域之间的关系
					updateIdentifierScopeRelationByFun(identifier,relatedFuns, st, statement, lineOrder);//调用的函数对应的作用域也更新到相关作用域当中
					updateFunsRelation(identifier,scopeNode, st, statement, lineOrder,relatedFuns);//如果标识符的修改中使用了函数，更新标识符和函数之间的关系
				}
				//把函数和形参关联起来（不必要）
				
				//把实参和形参关联起来（对于数组和指针变量）
				for(FunctionSymbolRecord fun:relatedFuns) {
					List<SymbolRecord> parameterIdentifiers=fun.getParameterIdentifiers();
					if(parameterIdentifiers.size()==relatedIdentifiers.size()) {
						for(int i=1;i<parameterIdentifiers.size();i++) {
							List<SymbolRecord> temp=new ArrayList<>();
							temp.add(relatedIdentifiers.get(i));
							updateIdentifiersRelation(parameterIdentifiers.get(i),scopeNode, st, statement, lineOrder,temp);//更新标识符和标识符之间关系
						}
					}
				}
				
				//如果标识符是数组或者指针，把函数的参数关联起来
				for(SymbolRecord id:relatedIdentifiers) {
//					if(id.getCategory().equals("array")||id.getCategory().equals("pointer")) {
						updateIdentifier(id,scopeNode,st, statement, lineOrder,relatedIdentifiers,relatedFuns);
//					}
				}
			}
		}
	}

	/**
     * 更新标识符和其相关标识符以及相关函数之间的关系
     * @param identifier
     * @param scopeNode
     * @param st
     * @param statement
     * @param lineOrder
     * @param relatedIdentifiers
     * @param relatedFuns
     */
    private void updateIdentifier(SymbolRecord identifier, ScopeTree scopeNode, SymbolTable st, String statement,
			int lineOrder, List<SymbolRecord> relatedIdentifiers, List<FunctionSymbolRecord> relatedFuns) {
		// TODO Auto-generated method stub
    	updateIdentifierScopeRelation(identifier,scopeNode, st, statement, lineOrder);//更新标识符和作用域之间的关系
		updateIdentifierScopeRelationByFun(identifier,relatedFuns, st, statement, lineOrder);//调用的函数对应的作用域也更新到相关作用域当中
		updateIdentifiersRelation(identifier,scopeNode, st, statement, lineOrder,relatedIdentifiers);//更新标识符和标识符之间关系
		updateFunsRelation(identifier,scopeNode, st, statement, lineOrder,relatedFuns);//如果标识符的修改中使用了函数，更新标识符和函数之间的关系
	}

	/**
     * qurong
     * 2023.8.24
     * 调用的函数对应的作用域也更新到相关作用域当中
     * @param identifier
     * @param relatedFuns
     * @param st
     * @param statement
     * @param lineOrder
     */
    private void updateIdentifierScopeRelationByFun(SymbolRecord identifier, List<FunctionSymbolRecord> relatedFuns, SymbolTable st,
			String statement, int lineOrder) {
    	if(relatedFuns==null||relatedFuns.size()<=0) {
    		return;
    	}
    	
    	List<Integer> relatedScopes=identifier.getRelatedScopes();//相关作用域的更新
    	if(relatedScopes==null) {
    		relatedScopes=new ArrayList<>();
    	}
    	
    	for(FunctionSymbolRecord fsr:relatedFuns) {
    	    if(fsr.getScope()!=null) {
                if (!relatedScopes.contains(fsr.getScope().getScopeID())) {
                    relatedScopes.add(fsr.getScope().getScopeID());
                }
            }
    	}
    	identifier.setRelatedScopes(relatedScopes);
    	
	}

	/**
     * qurong
     * 2023.8.22
     * 如果标识符的修改中使用了函数，更新标识符和函数之间的关系
     * @param identifier
     * @param scopeNode
     * @param st
     * @param statement
     * @param lineOrder
     * @param relatedFuns
     */
    private void updateFunsRelation(SymbolRecord identifier, ScopeTree scopeNode, SymbolTable st, String statement,
			int lineOrder, List<FunctionSymbolRecord> relatedFuns) {
    	if(relatedFuns==null||relatedFuns.size()<=0) {
    		return;
    	}
		   
		Map<Integer,List<FunctionSymbolRecord>> scopeModifiedFuns=identifier.getScopeModifiedFuns();//取出当前标识符的关联函数标识符map
		if(scopeModifiedFuns==null) {
			scopeModifiedFuns=new HashMap<>();
			scopeModifiedFuns.put(scopeNode.getScopeID(), relatedFuns);
		}else {
			List<FunctionSymbolRecord> frs= scopeModifiedFuns.get(scopeNode.getScopeID());
			if(frs==null||frs.size()==0) {
				scopeModifiedFuns.put(scopeNode.getScopeID(), relatedFuns);
			}else {
				for(FunctionSymbolRecord refsr:relatedFuns) {
					if(!frs.contains(refsr)) {//去重
						frs.add(refsr);
					}
				}
				scopeModifiedFuns.put(scopeNode.getScopeID(), frs);
			}
		}
		identifier.setScopeModifiedFuns(scopeModifiedFuns);

    	
    	for(FunctionSymbolRecord fsr:relatedFuns) {	//把函数的返回值对应的标识符关联到赋值语句左侧的标识符
    		List<SymbolRecord> returnIdentifiers=fsr.getReturnIdentifiers();//当前函数返回值中包含的标识符
    		returnIdentifiers.addAll(fsr.getParameterIdentifiers());
    		updateIdentifiersRelation(identifier,scopeNode, st, statement, lineOrder,returnIdentifiers);
    	}
		
		updateSymTableBySymbolRecord(st,identifier);//更新一条标识符记录
	}

	/**
     * qurong
     * 2023.8.22
     *更新标识符和标识符之间关系
     * @param identifier
     * @param scopeNode
     * @param st
     * @param statement
     * @param lineOrder
     */
    private void updateIdentifiersRelation(SymbolRecord identifier, ScopeTree scopeNode, SymbolTable st,
			String statement, int lineOrder,List<SymbolRecord> relatedIdentifiers) {
		 
    	if(relatedIdentifiers!=null&&relatedIdentifiers.size()>=2) {
//    		System.out.print("");
    	}
    	
		//建立等式左侧的identifier和右侧relatedIdentifiers各个标识符之间的使用关联关系
		if((relatedIdentifiers!=null&&relatedIdentifiers.size()!=0)) {
			Map<Integer,List<SymbolRecord>> scopeModifiedIdentifiers=identifier.getScopeModifiedIdentifiers();//取出当前标识符的关联标识符map
			if(scopeModifiedIdentifiers==null) {
				scopeModifiedIdentifiers=new HashMap<>();
				scopeModifiedIdentifiers.put(scopeNode.getScopeID(), relatedIdentifiers);
			}else {
				List<SymbolRecord> srs= scopeModifiedIdentifiers.get(scopeNode.getScopeID());
				if(srs==null||srs.size()==0) {
					scopeModifiedIdentifiers.put(scopeNode.getScopeID(), relatedIdentifiers);
				}else {
					for(SymbolRecord resr:relatedIdentifiers) {
						if(!containsSR(srs,resr)) {//去重
//						if(!srs.contains(resr)) {//去重
							srs.add(resr);
						}
					}
//					srs.addAll(relatedIdentifiers);
					scopeModifiedIdentifiers.put(scopeNode.getScopeID(), srs);
				}
			}
			identifier.setScopeModifiedIdentifiers(scopeModifiedIdentifiers);
			
			List<SymbolRecord> allRelatedIdentifiers=identifier.getAllRelatedIdentifiers();//校验
			Set<SymbolRecord> uniqueIdentifiers=new HashSet<>();//校验
			uniqueIdentifiers.addAll(allRelatedIdentifiers);
			uniqueIdentifiers.addAll(relatedIdentifiers);
			
		    deleteSameID(uniqueIdentifiers);
//			for(SymbolRecord resr:relatedIdentifiers) {
//				if(!containsSR(srs,resr)) {//去重
//				if(!allRelatedIdentifiers.contains(resr)) {//去重
//					allRelatedIdentifiers.add(resr);
//				}
//			}
//			allRelatedIdentifiers.addAll(relatedIdentifiers);
			List<SymbolRecord> allRelatedIdentifiersAfter=new ArrayList<>();//校验
			allRelatedIdentifiersAfter.addAll(uniqueIdentifiers);
			identifier.setAllRelatedIdentifiers(allRelatedIdentifiersAfter);
			updateSymTableBySymbolRecord1(st,identifier);//更新一条标识符记录
		}
		
		
	}
    
    
    /**
     * qurong
     * 2023.10.2
     * 删除重复的标识符记录（根据ID
     * @param uniqueIdentifiers
     */
    private void deleteSameID(Set<SymbolRecord> uniqueIdentifiers) {
		// TODO Auto-generated method stub
    	List<String> names=new ArrayList<>();
    	Set<SymbolRecord> result=new HashSet<>();
		if(uniqueIdentifiers!=null) {
			for(SymbolRecord sr:uniqueIdentifiers) {
				if(names.contains(sr.getID())) {
					continue;
				}else {
					names.add(sr.getID());
					result.add(sr);
				}
			}
		}
		uniqueIdentifiers=result;
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

	/**
     * qurong
     * 2023.8.22
     * 更新标识符和作用域之间的关系
     * @param identifier
     */
    private void updateIdentifierScopeRelation(SymbolRecord identifier,ScopeTree scopeNode,SymbolTable st,String statement,int lineOrder) {
		   
    	List<Integer> relatedScopes=identifier.getRelatedScopes();//相关作用域的更新
    	if(relatedScopes==null) {
    		relatedScopes=new ArrayList<>();
    	}
    	if(!relatedScopes.contains(scopeNode.getScopeID())) {
    		relatedScopes.add(scopeNode.getScopeID());
    	}
    	identifier.setRelatedScopes(relatedScopes);
    	
    	Map<Integer,Map <Integer,String>> scopeContent=identifier.getScopeContent();//作用域对应程序片段的更新 作用域ID，行号，语句内容
    	if(scopeContent==null) {
    		scopeContent=new HashMap<>();
    	}
    	if(scopeContent.size()<=0) {
    		Map <Integer,String> lineContent=new HashMap<>();
    		lineContent.put(lineOrder, statement);
    		scopeContent.put(scopeNode.getScopeID(),lineContent);
    	}else {
    		Map <Integer,String> lineContent=scopeContent.get(scopeNode.getScopeID());
    		if(lineContent==null) {
    			lineContent=new HashMap<>();
    		}
    		lineContent.put(lineOrder, statement);
    		scopeContent.put(scopeNode.getScopeID(),lineContent);
    	}
    	identifier.setScopeContent(scopeContent);
    	
    	updateSymTableBySymbolRecord(st,identifier);//更新一条标识符记录
	}
    
	/**
     * qurong
     * 2023.10.2
     * 更新标识符和作用域之间的关系
     * @param identifier
     */
    private void updateIdentifierScopeRelation(SymbolRecord identifier,ScopeTree scopeNode,SymbolTable st,String statement,int lineOrder,SymbolTable newSt) {
		   
    	List<Integer> relatedScopes=identifier.getRelatedScopes();//相关作用域的更新
    	if(relatedScopes==null) {
    		relatedScopes=new ArrayList<>();
    	}
    	if(!relatedScopes.contains(scopeNode.getScopeID())) {
    		relatedScopes.add(scopeNode.getScopeID());
    	}
    	identifier.setRelatedScopes(relatedScopes);
    	
    	Map<Integer,Map <Integer,String>> scopeContent=identifier.getScopeContent();//作用域对应程序片段的更新 作用域ID，行号，语句内容
    	if(scopeContent==null) {
    		scopeContent=new HashMap<>();
    	}
    	if(scopeContent.size()<=0) {
    		Map <Integer,String> lineContent=new HashMap<>();
    		lineContent.put(lineOrder, statement);
    		scopeContent.put(scopeNode.getScopeID(),lineContent);
    	}else {
    		Map <Integer,String> lineContent=scopeContent.get(scopeNode.getScopeID());
    		if(lineContent==null) {
    			lineContent=new HashMap<>();
    		}
    		lineContent.put(lineOrder, statement);
    		scopeContent.put(scopeNode.getScopeID(),lineContent);
    	}
    	identifier.setScopeContent(scopeContent);
    	
    	updateSymTableBySymbolRecord1(st,identifier,newSt);//更新一条标识符记录
	}
    
	/**
     * qurong
     * 2023.10.2
     *更新标识符和标识符之间关系
     * @param identifier
     * @param scopeNode
     * @param st
     * @param statement
     * @param lineOrder
     */
    private void updateIdentifiersRelation(SymbolRecord identifier, ScopeTree scopeNode, SymbolTable st,
			String statement, int lineOrder,List<SymbolRecord> relatedIdentifiers, SymbolTable newSt) {
		 
    	if(relatedIdentifiers!=null&&relatedIdentifiers.size()>=2) {
    		System.out.print("relatedIdentifiers!=null&&relatedIdentifiers.size()>=2)");
    	}
    	
		//建立等式左侧的identifier和右侧relatedIdentifiers各个标识符之间的使用关联关系
		if((relatedIdentifiers!=null&&relatedIdentifiers.size()!=0)) {
			Map<Integer,List<SymbolRecord>> scopeModifiedIdentifiers=identifier.getScopeModifiedIdentifiers();//取出当前标识符的关联标识符map
			if(scopeModifiedIdentifiers==null) {
				scopeModifiedIdentifiers=new HashMap<>();
				scopeModifiedIdentifiers.put(scopeNode.getScopeID(), relatedIdentifiers);
			}else {
				List<SymbolRecord> srs= scopeModifiedIdentifiers.get(scopeNode.getScopeID());
				if(srs==null||srs.size()==0) {
					scopeModifiedIdentifiers.put(scopeNode.getScopeID(), relatedIdentifiers);
				}else {
					for(SymbolRecord resr:relatedIdentifiers) {
//						if(!containsSR(srs,resr)) {//去重
						if(!srs.contains(resr)) {//去重
							srs.add(resr);
						}
					}
//					srs.addAll(relatedIdentifiers);
					scopeModifiedIdentifiers.put(scopeNode.getScopeID(), srs);
				}
			}
			identifier.setScopeModifiedIdentifiers(scopeModifiedIdentifiers);
			
			List<SymbolRecord> allRelatedIdentifiers=identifier.getAllRelatedIdentifiers();//校验
			Set<SymbolRecord> uniqueIdentifiers=new HashSet<>();//校验
			uniqueIdentifiers.addAll(allRelatedIdentifiers);
			uniqueIdentifiers.addAll(relatedIdentifiers);
//			for(SymbolRecord resr:relatedIdentifiers) {
//				if(!containsSR(srs,resr)) {//去重
//				if(!allRelatedIdentifiers.contains(resr)) {//去重
//					allRelatedIdentifiers.add(resr);
//				}
//			}
//			allRelatedIdentifiers.addAll(relatedIdentifiers);
			List<SymbolRecord> allRelatedIdentifiersAfter=new ArrayList<>();//校验
			allRelatedIdentifiersAfter.addAll(uniqueIdentifiers);
			identifier.setAllRelatedIdentifiers(allRelatedIdentifiersAfter);
			updateSymTableBySymbolRecord1(st,identifier,newSt);//更新一条标识符记录
		}
		
		
	}

	/**
     * 2023.8.17
     * 从一个程序片段中取出仅有的一个标识符名称
     * @param str
     * @param st
     * @return
     */
	private SymbolRecord getIdentifierFromStr(String str, SymbolTable st) {
		List<SymbolRecord> names=new ArrayList<>();
		List<SymbolRecord> srs=st.getSymbolRecords();
		if(srs==null||srs.size()==0) {
			return null;
		}
		for(SymbolRecord sr:srs) {
			if(str.contains(sr.getID())) {
				names.add(sr);
			}
		}
		if(names.size()==1) {
			return names.get(0);
		}else if (names.size()>=2) {
			////System.out.println(names.size()+" more than 2: "+str);
		}
		return null;
	}

	/**
     *为末尾是*？+的正则表达式文法生成多个program再拼接起来
     * @param tempItem
     * @return
     */
    public String constructProgramByRegexMulti(String tempItem) {
        String result="";
        Block block = new Block("");

//        if(tempItem.contains("\\u")) {//unicode相关
//        	return block.getUnicodeStrByRegex(tempItem);
//        }
        
        try {
            RgxGen rgxGen = new RgxGen(tempItem);
            String s = rgxGen.generate();
            result = s;
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 自己写的正则表达式生成方法 过于简单 在实际生成过程中遇到不少解析失败的情况，改用RgxGen试试
         */
//        if(tempItem.endsWith("*")){
//            result+=" "+block.getMoreThanZeroByRegex(tempItem,starnum,true);
//        }else if(tempItem.endsWith("?")){
//            result+=" "+block.getZeroOrOneByRegex(tempItem);
//        }else if(tempItem.endsWith("+")){
//            result+=" "+block.getMoreThanOneByRegex(tempItem,addnum,true);
//        }else if(tempItem.startsWith("[")&&tempItem.endsWith("]")){
//            result+=" "+block.getOneFromRegex(tempItem);
//        }
//        else if(tempItem.startsWith("~[")&&tempItem.endsWith("]")){
//            result+=" "+block.getRevertByRegex(tempItem,addnum);
//        }
        return result;
    }

    /**
     * 检查待生成文法串的格式，比如或的话随机取一个值，多余的括号删除
     * @param str
     * @return
     */
    public String checkFormat(String str,int index) {
        str= StringTools.deleteBrackets("(",")",str);//先删除value前后多余的括号()
        str= StringTools.deleteBrackets("{","}",str);//先删除value前后多余的括号{}
        str=str.trim();
        Block block=new Block("");
        str=block.getOneFromOr(str);
        str=str.trim();
        if(str.contains("(")||str.contains(")")||str.contains("=")){
            str=blockMatch(str,index);//profix=(sdhjh 拆分开profix = (sdhjh
        }
        str=str.trim();
        str=deleteMoreSpace(str);//删除字符串中多余的空格，比如某处有三个空格，只保留一个
        return str;
    }

    /**
     * 删除字符串中多余的空格字符
     * @param str
     * @return
     */
    public String deleteMoreSpace(String str) {
        String result="";
        if(str==null||str==""){
            return result;
        }
        str=str.trim();
        if(str.length()<=0){
            return result;
        }
        char prechar=str.charAt(0);
        result+=prechar;
        for(int i=1;i<str.length();i++){
            char curchar=str.charAt(i);
            if(curchar==(' ')){
                if(prechar!=' '){
                    result+=curchar;
                }else{

                }
            }else{
                result+=curchar;
            }
            prechar=curchar;
        }
        return result;
    }


    /**
     * 为末尾不是*？+的含有token的文法生成program
     * @param str
     * @param afterBracketStr
     * @return
     */
    public String constructProgramByTokenSingle(String str,String afterBracketStr,int index,Map<String,Integer> tokenAppearTimes,boolean ifNeedOutCycle,SymbolTable st,ScopeTree scopeNode,int lineOrder) {
        //System.out.println("Single input: "+str);

        //如果是特殊文法，返回生成结果
        String spResult=generateIfSpecialGrammar(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        
        //如果是新语句，更新行号
        if(ifStatementGrammar(str)){
        	lineOrder++;
        }
        
        if(spResult!=""){
        	spResult=updateProgramAfterGenerateRule(spResult,str,index,"","",scopeNode,st,lineOrder);
            return spResult;
        }
        String result="";
        //如果有括号，去除一层括号，将其中的内容扫描,把括号之后的字符归入下一个待匹配的内容里面
        if(afterBracketStr!=null){
            String validStr="";
            if(afterBracketStr!=""){//)之后还有其它内容，加入到下一次匹配的内容当中;以)结尾，尝试删除首尾的括号，对子项进行处理
                validStr=str.substring(0,str.length()-afterBracketStr.length());
            }else {
                validStr = StringTools.deleteBrackets("(", ")", str);
            }
            validStr=validStr.trim();
            if(validStr!=null&&validStr.length()>0) {
                if(validStr.equals("identifier")){
                    //System.out.println("generate identifier in constructProgramByTokenSingle:");
                }
                result +=constructProgramByValue(validStr, index, tokenAppearTimes, ifNeedOutCycle,st, scopeNode,lineOrder);
            }else{
                //按理说不会运行到这一行
                //System.out.println(" space in a () : "+str);
                return "";
            }
        }else{
            //没有括号
            if(str.contains(" ")){
                //按理说不会运行到这一行
                //System.out.println(" space in this token : "+str);
                return "";
            }
            if(grammars.get(index).getOrderedList().contains(str)){
//                str=checkTermination(str,index,cyclyLength);
                str=str.trim();
                Token token=grammars.get(index).getTokens().get(str);//终止约束相关
                List<Value> valueList=token.getValueList();//取出token的所有取值方式
                Value selectedValue=null;
                Map<String,Value> selectedStrategy=null;
                Grammar g=new Grammar();
                if(ifNeedOutCycle){//是某个循环中的关键变量，且循环次数已达上界，按照终止取值跳出循环
                    selectedValue=token.getOutCycleValue();
                }else{
                    if(token.getTokenType()=="2"||token.getTokenType()=="3"){//是单变量循环或者多变量循环
                        if(tokenAppearTimes.keySet().contains(str)){//如果不是第一次出现
                            tokenAppearTimes.put(str,tokenAppearTimes.get(str)+1);
                        }else{
                            tokenAppearTimes.put(str,1);
                        }
                        if(isOverCycleLimit(tokenAppearTimes,str,index)){//当前token(str)在生成路径中出现的次数已经大于上限，为防止堆栈溢出，必须为其选择一条能跳出循环到达终止子值的取值方式
                        	selectedValue=token.getOutCycleValue();
                            ifNeedOutCycle=true;
                        }else{
                            selectedValue=g.getValueByRandom(valueList);//从取值方式中随机选一种

                        }
                    }else{//非循环变量
                        selectedValue=g.getValueByRandom(valueList);//从取值方式中随机选一种
                    }
                }
                if(selectedValue==null){
                    selectedValue=g.getValueByRandom(valueList);//从取值方式中随机选一种
                }
                if(selectedValue==null){
                    result+="";
                    //System.out.println(" this token: "+str+" none available  common.Value  in this grammar:"+grammars.get(index).getPath());
                }else{
                    //System.out.println("Single select value: "+selectedValue.getValue());
                    if(selectedValue.getValue().equals("identifier")){
                        //System.out.println("generate value here: ");
                    }
                    result+=constructProgramByValue(selectedValue.getValue(),index,tokenAppearTimes,ifNeedOutCycle,st, scopeNode,lineOrder);
                }
            }else{
                //按理说不会运行到这一行
                //System.out.println(" this token: "+str+" can't be matched in the token list ");
//                return "";
                return str;
            }
        }
        
        //函数相关
//      if(ifMethodGrammar(str)){
//      	result=addOutput(st,scopeNode,result);
//      }
      
    //块相关
//      if(ifBlockGrammar(str)){
//      	result=addOutput(st,scopeNode,result);
//      }

        //作用域的回溯
        if(ifScopeGrammar(str)){
//            result="//insertion-scope-first-available\n//insertion-scope-first-statement-available\n"
//                    +result
//                    +"//insertion-scope-last-available\n//insertion-scope-last-statement-available\n";//变异C++程序插入位置标记
        	scopeNode.setScopeFragment(result);
        	scopeNode=updateScopeToFather(result,scopeNode);
        }
        
      //作用域头部和尾部的记录
        if(ifScopeBodyGrammar(str)){
        	//result=addOutput(st,scopeNode,result);
            if(ConfigureInfo.mutationAvailable) {
                result = addComment(result, scopeNode);//程序变异作用域内插入位置点标记
            }
        	scopeNode=updateScopeBodyHeadTail(result,scopeNode);
        }

        //程序变异main函数外的全局插入位置点标记
        if(ifMainBodyGrammar(str)){
            if(ConfigureInfo.mutationAvailable) {
                result = "//insertion-global-available\n" + addComment(result, scopeNode);//程序变异作用域内插入位置点标记
            }
        }

        //标识符定义或修改相关
        if(ifDefineOrModifyGrammar(str)){
            //记录生成此文法的过程中，关联到的所有标识符
        	updateIdentifierRelations(str,result,scopeNode,st,lineOrder);//更新当前程序中在当前作用域内各个标识符之间的关系
        }

        //除数相关,除数生成后，判断是否为零，是的话，除数用1代替，不是的话返回除数的值
        if(ifDivisorGrammar(str))result=zeroExclide(result);

        //语句相关
        if(ifStatementGrammar(str)){
        	//更新语句及其行号
        	Map<Integer,String> lineOrderStatement=st.getLineOrderStatement();
        	String state=lineOrderStatement.get(lineOrder);
        	if(lineOrderStatement!=null&&state!=null&&state!="") {
        		////System.out.println(lineOrder);
        		////System.out.println(state);
        	}else if(lineOrderStatement!=null&&(state==null||state=="")) {
        		lineOrderStatement.put(lineOrder, result);
        		st.setLineOrderStatement(lineOrderStatement);
        	}
        	
        	//如果是开启新的语句，在python中需要考虑严格缩进
            result=getTabProfixByDepth(str,scopeNode,result);
        }

        //System.out.println("Single input: "+str+" Single output: "+result);
        return result;
    }


    /**
     * qurong
     * 2025-2-19
     * 为末尾不是*？+的含有token的文法生成program
     * @param str
     * @param afterBracketStr
     * @return
     */
    public List<String> constructProgramsByTokenSingle(String str,String afterBracketStr,int index,Map<String,Integer> tokenAppearTimes,boolean ifNeedOutCycle,SymbolTable st,ScopeTree scopeNode,int lineOrder) {
        //System.out.println("Single input: "+str);
        List<String> resultList=new ArrayList<>();
        StringTools stringTools=new StringTools();
        //如果是特殊文法，返回生成结果
        List<String> spResult=generateProgramsIfSpecialGrammar(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);

        //如果是新语句，更新行号
        if(ifStatementGrammar(str)){
            lineOrder++;
        }


        if(spResult!=null&&spResult.size()>0){
            List<String> tempResult=new ArrayList<>();
            for(String res:spResult){
                String tempRes=updateProgramAfterGenerateRule(res,str,index,"","",scopeNode,st,lineOrder);//更新规则生成之后程序中的信息
                tempResult.add(tempRes);
            }
            spResult=tempResult;
            return spResult;
        }


        List<String> result=new ArrayList<>();
        //如果有括号，去除一层括号，将其中的内容扫描,把括号之后的字符归入下一个待匹配的内容里面
        if(afterBracketStr!=null){
            String validStr="";
            if(afterBracketStr!=""){//)之后还有其它内容，加入到下一次匹配的内容当中;以)结尾，尝试删除首尾的括号，对子项进行处理
                validStr=str.substring(0,str.length()-afterBracketStr.length());
            }else {
                validStr = StringTools.deleteBrackets("(", ")", str);
            }
            validStr=validStr.trim();
            if(validStr!=null&&validStr.length()>0) {
                if(validStr.equals("identifier")){
                    //System.out.println("generate identifier in constructProgramByTokenSingle:");
                }
                result=stringTools.combineStrList(result,constructProgramsByValue(validStr, index, tokenAppearTimes, ifNeedOutCycle,st, scopeNode,lineOrder));
            }else{
                //按理说不会运行到这一行
                //System.out.println(" space in a () : "+str);
                return new ArrayList<>();
            }
        }else{
            //没有括号
            if(str.contains(" ")){
                //按理说不会运行到这一行
                //System.out.println(" space in this token : "+str);
                return new ArrayList<>();
            }
            if(grammars.get(index).getOrderedList().contains(str)){
//                str=checkTermination(str,index,cyclyLength);
                str=str.trim();
                Token token=grammars.get(index).getTokens().get(str);//终止约束相关
                List<Value> valueList=token.getValueList();//取出token的所有取值方式
                Value selectedValue=null;
                Map<String,Value> selectedStrategy=null;
                Grammar g=new Grammar();
                if(ifNeedOutCycle){//是某个循环中的关键变量，且循环次数已达上界，按照终止取值跳出循环
                    selectedValue=token.getOutCycleValue();
                }else{
                    if(token.getTokenType()=="2"||token.getTokenType()=="3"){//是单变量循环或者多变量循环
                        if(tokenAppearTimes.keySet().contains(str)){//如果不是第一次出现
                            tokenAppearTimes.put(str,tokenAppearTimes.get(str)+1);
                        }else{
                            tokenAppearTimes.put(str,1);
                        }
                        if(isOverCycleLimit(tokenAppearTimes,str,index)){//当前token(str)在生成路径中出现的次数已经大于上限，为防止堆栈溢出，必须为其选择一条能跳出循环到达终止子值的取值方式
                            selectedValue=token.getOutCycleValue();
                            ifNeedOutCycle=true;
                        }else{
                            selectedValue=g.getValueByRandom(valueList);//从取值方式中随机选一种

                        }
                    }else{//非循环变量
                        selectedValue=g.getValueByRandom(valueList);//从取值方式中随机选一种
                    }
                }
                if(selectedValue==null){
                    selectedValue=g.getValueByRandom(valueList);//从取值方式中随机选一种
                }
                if(selectedValue==null){
                    result=stringTools.combineStrList(result,new ArrayList<>());
                    //System.out.println(" this token: "+str+" none available  common.Value  in this grammar:"+grammars.get(index).getPath());
                }else{
                    //System.out.println("Single select value: "+selectedValue.getValue());
                    if(selectedValue.getValue().equals("identifier")){
                        //System.out.println("generate value here: ");
                    }
                    result=stringTools.combineStrList(result,constructProgramsByValue(selectedValue.getValue(),index,tokenAppearTimes,ifNeedOutCycle,st, scopeNode,lineOrder));
                }
            }else{
                //按理说不会运行到这一行
                //System.out.println(" this token: "+str+" can't be matched in the token list ");
//                return "";
                resultList.add(str);
                return resultList;
            }
        }
        if(result!=null&&result.size()>0) {
            for (String res : result) {
                //作用域的回溯
                if(ifScopeGrammar(str)){
                    scopeNode.setScopeFragment(res);
                    scopeNode=updateScopeToFather(res,scopeNode);
                }

                //作用域头部和尾部的记录
                if(ifScopeBodyGrammar(str)){
                    //result=addOutput(st,scopeNode,result);
                    if(ConfigureInfo.mutationAvailable) {
                        res = addComment(res, scopeNode);//程序变异作用域内插入位置点标记
                    }
                    scopeNode=updateScopeBodyHeadTail(res,scopeNode);
                }

                //程序变异main函数外的全局插入位置点标记
                if(ifMainBodyGrammar(str)){
                    if(ConfigureInfo.mutationAvailable) {
                        res = "//insertion-global-available\n" + addComment(res, scopeNode);//程序变异作用域内插入位置点标记
                    }
                }

                //标识符定义或修改相关
                if(ifDefineOrModifyGrammar(str)){
                    //记录生成此文法的过程中，关联到的所有标识符
                    updateIdentifierRelations(str,res,scopeNode,st,lineOrder);//更新当前程序中在当前作用域内各个标识符之间的关系
                }

                //除数相关,除数生成后，判断是否为零，是的话，除数用1代替，不是的话返回除数的值
                if(ifDivisorGrammar(str))res=zeroExclide(res);

                //语句相关
                if(ifStatementGrammar(str)){
                    //更新语句及其行号
                    Map<Integer,String> lineOrderStatement=st.getLineOrderStatement();
                    String state=lineOrderStatement.get(lineOrder);
                    if(lineOrderStatement!=null&&state!=null&&state!="") {
                        ////System.out.println(lineOrder);
                        ////System.out.println(state);
                    }else if(lineOrderStatement!=null&&(state==null||state=="")) {
                        lineOrderStatement.put(lineOrder, res);
                        st.setLineOrderStatement(lineOrderStatement);
                    }

                    //如果是开启新的语句，在python中需要考虑严格缩进
                    res=getTabProfixByDepth(str,scopeNode,res);
                }
                resultList.add(res);
            }
        }

        //System.out.println("Single input: "+str+" Single output: "+result);
        return resultList;
    }
    /**
     * 2024-12-24
     * qurong
     * 在作用域开始和结束插入位置点标记，用于变异程序
     * @param result
     * @param scopeNode
     * @return
     */
    private String addComment(String result, ScopeTree scopeNode) {
        if(!result.contains("{")||!result.contains("}")) {
            //System.out.println("no {}: "+result);
            return result;
        }

        if(scopeNode!=null){
            List<ScopeTree> children=scopeNode.getChildren();
            if(children!=null&&children.size()>0) {
                ScopeTree lastNode=children.get(children.size()-1);//取出最后一个children
                String head=result.substring(0,result.indexOf("{")+1);
                String tail=result.substring(result.lastIndexOf("}"));
                String middle=result.substring(result.indexOf("{")+1,result.lastIndexOf("}"));
                lastNode.setScopeStatements(middle);
                lastNode.setScopeStatementsWithBlock("{"+middle+"}");
                middle="//insertion-scope-first-available\n//insertion-scope-first-statement-available\n"
                    +middle
                    +"//insertion-scope-last-available\n//insertion-scope-last-statement-available\n";//变异C++程序插入位置标记
                result=head+middle+tail;
                lastNode.setScopeStatementsWithComments(middle);
                lastNode.setScopeStatementsWithCommentsAndBlock("{"+middle+"}");
                children.remove(children.size()-1);
                children.add(lastNode);
                scopeNode.setChildren(children);
            }
        }
        return result;
    }

    /**
     * block匹配 block之间如果没有空格 需要添加
     * @param str
     * @return
     */
    public String blockMatch(String str,int index) {
        String result="";
        //System.out.println(str);
        if(str==null||str==""){
            return "";
        }

        Stack stack = new Stack();
        String terminator="";
        for(int i = 0; i < str.length(); i++){
            Character temp = str.charAt(i);//先转换成字符
            if(temp=='\\'){//如果是转义字符，向后再读一位
                i++;
                continue;
            }
            //是否为左括号或者'
            if(temp=='['||temp=='\''){
                if(stack.isEmpty()){
                    if(temp=='\''&&terminator!=""){
                        result+=terminator+" ";
                        terminator="";//清空累积的文法
                    }
                    terminator+=temp;
                    stack.push(temp);
                }else{
                    terminator+=temp;
                    if(stack.peek().equals('\'')){
                        if(temp=='\''){
                            //栈顶是'，可以匹配
//                            squarBracketFlag=true;
                            terminator+=" ";
                            stack.pop();
                        }else{
                            //栈顶是单引号，但是没读到单引号与他匹配
                        }
                    }else if(stack.peek().equals('[')){
                        //栈顶是[，但是没读到]与他匹配
                        //System.out.println(" an unmatched '[' in a grammar: "+str);
                    }
                }
            }else if(temp==']'){//是否右括号
                if(stack.isEmpty()){
                    //读到了]但栈为空，这个字符认为是文法中的特殊字符
                    terminator+=" "+temp+" ";
                    //System.out.println(" an unmatched ']' in a grammar: "+str+"  "+grammars.get(index).getPath());
                }else {
                    terminator+=temp;
                    if(stack.peek().equals('\'')){
                        //栈顶是单引号，但是没读到单引号与他匹配
                    }else if(stack.peek().equals('[')){
                        //栈顶是[，可以匹配
//                        squarBracketFlag=true;
                        stack.pop();
                    }
                }
            }else{
                if(stack.isEmpty()){
                    if(temp=='?'||temp=='*'||temp=='+'){
                        terminator=terminator.trim();
                        if(terminator==""){
                            result=result.trim();
                        }
                        terminator+=temp+" ";
                        result+=terminator;
                        terminator="";//清空累积的文法
                    }else if(temp==')'||temp=='}'){
                        terminator+=" "+temp+" ";
                        result+=terminator;
                        terminator="";//清空累积的文法
                    }else if(temp=='='||temp=='>' ||temp=='<'){
                        terminator+=" "+temp+" ";
                        result+=terminator;
                        terminator="";//清空累积的token
                    }else if(temp=='~'||temp=='-'||temp=='('||temp=='{'){
                        terminator+=" "+temp;
                        result+=terminator;
                        terminator="";//清空累积的token
                    }else if(temp==';'){
                        terminator+=" "+temp+" ";
                        result+=terminator;
                        terminator="";//清空累积的token
                    }else if(temp==' '||temp=='\t'||temp=='\r'||temp=='\n'){//
                        terminator+=temp;
                        result+=terminator;
                        terminator="";//清空累积的token
                    }else{
                        terminator+=temp;
                    }
                }else{
                    terminator+=temp;//[]或者''中的内容
                }
            }
        }
        if(terminator!=""){
            result+=terminator;
        }
        return result;
    }

    /**
     * 构造满足文法的字符串
     * @param str
     * @return
     */
    public String constructProgram(String str) {
        //声明标识符相关
        if(ConfigureInfo.ifJavaCode&&str!=""&& JavaConfigureInfo.getVarnames().contains(str)){
            //System.out.println("unexpected string "+str);
        }
        //作用域相关
        if(ConfigureInfo.ifJavaCode&&str!=""&&(JavaConfigureInfo.scopeGrammars.contains(str))){
            //System.out.println("unexpected string "+str);
        }
        if(str==null||str.equals("")){
            return "";
        }
        String result="";
        StringTools st=new StringTools();
        int type=st.judgeTokenOrRegex(str);//判断tempItem是正则表达式 还是token表达式 还是''表达式
        if(type==2){//regex类型
            if(str.endsWith("*")||str.endsWith("?")||str.endsWith("+")||(str.endsWith("]")&&(str.startsWith("[")||str.startsWith("~[")))){
                if(str.equals("[]")){
                    result+=str;
                }else{
                    result+=constructProgramByRegexMulti(str);
                }
            }
            else{
                //System.out.println(" this regular expression is hard to understand : "+str);
            }
        }else if(type==3){
            if(str.startsWith("'")&&str.endsWith("'")&&str.length()>=3){
                str=str.substring(1,str.length()-1);
                result+=str;
            }else{
                //System.out.println("hard to process this:"+str);
            }
        }else{
            if(str.equals("{")||str.equals("}")||str.equals(";")){
                result=str+"\r\n";
            }else if(str.equals("=")||str.equals("<")||str.equals(">")||str.equals("'")||str.equals(":")){
                result=str;
            }else{
                //System.out.println("what's is? :"+str);
                result=str;
            }

        }
        return result;
    }


    /**
     * qurong
     * 2025-2-19
     * 构造满足文法的字符串
     * @param str
     * @return
     */
    public List<String> constructPrograms(String str) {
        List<String> resultList=new ArrayList<>();
        //声明标识符相关
        if(ConfigureInfo.ifJavaCode&&str!=""&& JavaConfigureInfo.getVarnames().contains(str)){
            //System.out.println("unexpected string "+str);
        }
        //作用域相关
        if(ConfigureInfo.ifJavaCode&&str!=""&&(JavaConfigureInfo.scopeGrammars.contains(str))){
            //System.out.println("unexpected string "+str);
        }
        if(str==null||str.equals("")){
            return new ArrayList<>();
        }
        String result="";
        StringTools st=new StringTools();
        int type=st.judgeTokenOrRegex(str);//判断tempItem是正则表达式 还是token表达式 还是''表达式
        if(type==2){//regex类型
            if(str.endsWith("*")||str.endsWith("?")||str.endsWith("+")||(str.endsWith("]")&&(str.startsWith("[")||str.startsWith("~[")))){
                if(str.equals("[]")){
                    result+=str;
                }else{
                    result+=constructProgramByRegexMulti(str);
                }
            }
            else{
                //System.out.println(" this regular expression is hard to understand : "+str);
            }
        }else if(type==3){
            if(str.startsWith("'")&&str.endsWith("'")&&str.length()>=3){
                str=str.substring(1,str.length()-1);
                result+=str;
            }else{
                //System.out.println("hard to process this:"+str);
            }
        }else{
            if(str.equals("{")||str.equals("}")||str.equals(";")){
                result=str+"\r\n";
            }else if(str.equals("=")||str.equals("<")||str.equals(">")||str.equals("'")||str.equals(":")){
                result=str;
            }else{
                //System.out.println("what's is? :"+str);
                result=str;
            }

        }
        resultList.add(result);
        return resultList;
    }
    /**
     * 判断是否超过了终止性约束的最大循环次数
     * @param tokenAppearTimes
     * @param str
     * @param index
     * @return
     */
    public boolean isOverCycleLimit(Map<String, Integer> tokenAppearTimes, String str, int index) {
        boolean result=true;
        if(str==null||str==""){
            return false;
        }
        str=str.trim();
        if(tokenAppearTimes.get(str)==null){
            return false;
        }
        int time=tokenAppearTimes.get(str);
        Constraint constraint=grammars.get(index).getConstraint();
        Map<String,Integer>  terminationMap=constraint.getTerminationMap();
        if(terminationMap!=null&&terminationMap.keySet().contains(str)) {//在终止性约束中配置了的上限
            int timeLimit = terminationMap.get(str);
            if(time>timeLimit){
                result= true;
            }else if(time> ConfigureInfo.getCycleStepLengthMax()){//超过程序设置的最大值
                result=true;
            }else{
                result=false;
            }
        }else{
            if (time > ConfigureInfo.getCycleStepLength()) {//未在终止性约束中配置上限，按照程序中设置的上限处理
                result= true;
            }else{
                result=false;
            }
        }
        return result;
    }

    /**
     * 为末尾是*？+的含有token的文法生成多个program再拼接起来
     * @param str
     * @return
     */
    public String constructProgramByTokenMulti(String str,int index,Map<String,Integer> tokenAppearTimes,boolean ifNeedOutCycle,SymbolTable st,ScopeTree scopeNode,int lineOrder) {
        //声明标识符相关
        if(ifVarnameGrammar(str)){
            return generateVarname(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        }
        //作用域相关
        if(ifScopeGrammar(str)){
            scopeNode=updateScope(st,scopeNode);
        }
        
        String result="";
        Block block = new Block("");
        List<String> strs=new ArrayList<String>();
        if(str.endsWith("*")){
            int starnum=ConfigureInfo.getStarnum();
            strs=block.getMoreThanZero(str, starnum);
        }else if(str.endsWith("?")){
            strs=block.getZeroOrOne(str);
        }else if(str.endsWith("+")) {
            int addnum=ConfigureInfo.getAddnum();
            strs=block.getMoreThanOne(str, addnum);
        }else if(str.startsWith("(")&&str.endsWith(")")) {
            str= StringTools.deleteBrackets("(",")",str);
            str=str.trim();
            strs.add(str);
        }
        if(strs==null||strs.size()==0){
            return "";
        }

        for(String s:strs){
            s=s.trim();
            if(s.equals("identifier")){
                //System.out.println("");
            }
            if(result.equals("") || result.trim().equals("")){
                result =constructProgramByValue(s,index, tokenAppearTimes, ifNeedOutCycle,st, scopeNode,lineOrder);
            }else {
                result +=" "+constructProgramByValue(s,index, tokenAppearTimes, ifNeedOutCycle,st, scopeNode,lineOrder);
            }
        }
        
        return result;
    }
    /**
     * qurong
     * 2025-2-19
     * 为末尾是*？+的含有token的文法生成多个program再拼接起来
     * @param str
     * @return
     */
    public List<String> constructProgramsByTokenMulti(String str,int index,Map<String,Integer> tokenAppearTimes,boolean ifNeedOutCycle,SymbolTable st,ScopeTree scopeNode,int lineOrder) {
        //声明标识符相关
        if(ifVarnameGrammar(str)){
            return generateVarnames(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        }
        //作用域相关
        if(ifScopeGrammar(str)){
            scopeNode=updateScope(st,scopeNode);
        }

        List<String> result=new ArrayList<>();
        Block block = new Block("");
        StringTools stringTools=new StringTools();
        List<String> strs=new ArrayList<String>();
        if(str.endsWith("*")){
            int starnum=ConfigureInfo.getStarnum();
            strs=block.getMoreThanZero(str, starnum);
        }else if(str.endsWith("?")){
            strs=block.getZeroOrOne(str);
        }else if(str.endsWith("+")) {
            int addnum=ConfigureInfo.getAddnum();
            strs=block.getMoreThanOne(str, addnum);
        }else if(str.startsWith("(")&&str.endsWith(")")) {
            str= StringTools.deleteBrackets("(",")",str);
            str=str.trim();
            strs.add(str);
        }
        if(strs==null||strs.size()==0){
            return new ArrayList<>();
        }

        for(String s:strs){
            s=s.trim();
            if(s.equals("identifier")){
                //System.out.println("");
            }
            result =stringTools.combineStrList(result,constructProgramsByValue(s,index, tokenAppearTimes, ifNeedOutCycle,st, scopeNode,lineOrder));
        }

        return result;
    }

    /**
     * 返回一个字符串中括号后面的内容
     * @param str
     * @return
     */
    public String backBracket(String str) {
        String result="";
        if(str==null||str==""||!str.contains(")")){
            return null;
        }
        str=str.trim();
        if(str.endsWith(")")){
            return "";
        }
        result=str.substring(str.lastIndexOf(")"));
        return result;
    }

    /**
     * 为文法grammar生成一个满足文法的程序
     */
    public void generateProgram(int index,SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        List<String> rootKeys=getRootKeys(index);//获取文法树的根节点，由此来自顶向下（或）的构造
        String program=generateProgramByKey(rootKeys,index,st, scopeNode,lineOrder);
    }

    /**
     * qurong
     * 2025-2-18
     * 以root为根节点，为文法grammar生成一个满足文法的程序
     */
    public String generateProgram(String root,int index,SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        String result="";
        result=generateProgramRandomByNode( root, index,st, scopeNode,lineOrder);//随机生成

        return result;
    }



    /**
     * qurong
     * 2025-2-18
     * 以root为根节点，为文法grammar生成一个满足文法的程序
     */
    public List<String> generatePrograms(String root,int index,SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        List<String> result=new ArrayList<>();
        result=generateProgramsRandomByNode( root, index,st, scopeNode,lineOrder);//随机生成

        return result;
    }
    /**
     * 2022-10-12
     * 在生成程序中，一些标识符未声明，记录在临时列表里，在程序生成结束后，一次性的添加到程序
     * @param st
     * @param program
     * @return
     */
    private String insertAddDeclaration(SymbolTable st, String program) {
        List<String> addedDeclaration=st.getAddedDeclaration().get(st.getProgramName());
        if(addedDeclaration==null||addedDeclaration.size()==0){
            return program;
        }
        String result;
        String declarationString="";
        for(String ad:addedDeclaration){
            declarationString+=ad;
        }
        String head=program.substring(0,program.indexOf("{")+1);
        String tail=program.substring(program.indexOf("{")+1);

        result=head+declarationString+tail;
        return result;

    }


    /**
     * 2022-10-12
     * 在生成程序中，一些标识符未声明，记录在临时列表里，在程序生成结束后，一次性的添加到程序最开始（例如C程序）
     * @param st
     * @param program
     * @return
     */
    private String insertAddDeclarationToHead(SymbolTable st, String program) {
        List<String> addedDeclaration=st.getAddedDeclaration().get(st.getProgramName());
        if(addedDeclaration==null||addedDeclaration.size()==0){
        	if(ConfigureInfo.ifCCode) {
	        	program=program.replaceAll(CConfigureInfo.insertPositionVar, " ");
	        	program=program.replaceAll(CConfigureInfo.insertPositionPointer, " ");
        	}
            return program;
        }
        String result="";
        String declarationStringVar="";
        String declarationStringPointer="";
        for(String ad:addedDeclaration){
            if(ad.startsWith("int *")||ad.startsWith("int*")) {
            	declarationStringPointer+=ad;
            }else {
            	declarationStringVar+=ad;
            }
            updateIdentifierRelations("", ad, st.getRootScope(),st,-1);
        }
        
        if(ConfigureInfo.ifCCode) {
    		String head=program.substring(0,program.indexOf(CConfigureInfo.insertPositionVar));
            String middle=program.substring(program.indexOf(CConfigureInfo.insertPositionVar),program.indexOf(CConfigureInfo.insertPositionPointer));
            String tail=program.substring(program.indexOf(CConfigureInfo.insertPositionPointer));
            result=head+declarationStringVar+middle+declarationStringPointer+tail;
            result= result.replaceAll(CConfigureInfo.insertPositionVar, " ");
            result=result.replaceAll(CConfigureInfo.insertPositionPointer, " ");
    	}
        
        
//        result=declarationString+program;
        return result;

    }

    /**
     * 2022-12-20
     * 在生成程序中的顶部，加入include头文件
     * @param program
     * @return
     */
    private String insertAddInclude(String program) {
        String result;
        String includeString="";
//        includeString+="#include <iostream> \r\n";//c++
        includeString+="#include <stdio.h> \r\n";
        includeString+="//insertion-include-available\n";//程序变异相关
//        includeString+="using namespace std; \r\n";//c++
        result=includeString+program;
        return result;

    }

    /**
     * 2024-1-25
     * 在生成c++程序中，一些标识符未声明，记录在临时列表里，在程序生成结束后，一次性的添加到程序最开始
     * @param st
     * @param program
     * @return
     */
    private String insertAddDeclarationToHeadCPlus(SymbolTable st, String program) {
        List<String> addedDeclaration=st.getAddedDeclaration().get(st.getProgramName());
        if(addedDeclaration==null||addedDeclaration.size()==0){
        	if(ConfigureInfo.ifCPlusCode) {
        		program=program.replaceAll(CPlusConfigureInfo.insertPositionVar, " ");
//            	program=program.replaceAll(CPlusConfigureInfo.insertPositionPointer, " ");
        	}
            return program;
        }
        String result="";
        String declarationStringVar="";
//        String declarationStringPointer="";
        for(String ad:addedDeclaration){
//            if(ad.startsWith("int *")||ad.startsWith("int*")) {
//            	declarationStringPointer+=ad;
//            }else 
            {
            	declarationStringVar+=ad;
            }
            updateIdentifierRelations("", ad, st.getRootScope(),st,-1);
        }

        if(ConfigureInfo.mutationAvailable) {//程序变异相关 把全局的变量函数声明加在全局变量声明的后面 原有函数等等的前面
            declarationStringVar += "//insertion-global-available\n";//插入头文件等的位置
            List<String> addedScopeDeclaration=st.getAddedScopeDeclaration().get(st.getProgramName());
            if(addedScopeDeclaration!=null&&addedScopeDeclaration.size()>0){
                for(String ad:addedScopeDeclaration){
                    declarationStringVar+=ad+"\r\n";
                }
            }
        }
        
        if(ConfigureInfo.ifCPlusCode) {
        	String head=program.substring(0,program.indexOf(CPlusConfigureInfo.insertPositionVar));
//            String middle=program.substring(program.indexOf(CPlusConfigureInfo.insertPositionVar),program.indexOf(CConfigureInfo.insertPositionPointer));
            String tail=program.substring(program.indexOf(CPlusConfigureInfo.insertPositionVar));
//            result=head+declarationStringVar+middle+declarationStringPointer+tail;
            result=head+declarationStringVar+tail;
            result= result.replaceAll(CPlusConfigureInfo.insertPositionVar, " ");
//            result=result.replaceAll(CPlusConfigureInfo.insertPositionPointer, " ");
    	}
//        result=declarationString+program;
        return result;

    }

    /**
     * 2024-1-25
     * 在生成c++程序中的顶部，加入include头文件
     * @param program
     * @return
     */
    private String insertAddIncludeCPlus(String program,SymbolTable st) {
        String result;
        String includeString="";
        includeString+="#include <iostream> \r\n";//c++
        if(ConfigureInfo.mutationAvailable) {//程序变异相关
            includeString += "//insertion-include-available\n";//插入头文件等的位置
            List<String> addedIncludeDeclaration=st.getAddedIncludeDeclaration().get(st.getProgramName());
            if(addedIncludeDeclaration!=null&&addedIncludeDeclaration.size()>0){
                for(String ad:addedIncludeDeclaration){
                    includeString+=ad+"\r\n";
                }
            }
        }
//        includeString+="#include <stdio.h> \r\n";
        includeString+="using namespace std; \r\n";//c++

        result=includeString+program;
        return result;

    }
    
    /**
     * 2024-1-25
     * 在生成c++程序中,随机替换部分类型为auto
     * @param program
     * @return
     */
    private String autoProgram(String program) {
        String result="";
        String includeString="";
        
        String result1=program;
        String lines[]=result1.split("\r\n");
        for(String line:lines) {
        	if(!line.contains("*")&&!line.contains("[")&&!line.contains("]")&&!line.contains("main")&&!line.contains("(")&&!line.contains(")")) {
        		line=line.replace("int ", "auto ");
        		line=line.replace("long ", "auto ");
        		line=line.replace("short ", "auto ");
        		line=line.replace("double ", "auto ");
        	}
        	result+=line+"\r\n";
        }
        return result;

    }

}
