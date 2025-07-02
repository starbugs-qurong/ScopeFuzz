package iscas.ac.grand.gui;

import iscas.ac.grand.main.GenerateProgramsByGrammar;
import iscas.ac.grand.main.common.ConfigureInfo;
import iscas.ac.grand.main.common.Constraint;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.*;


public class MainClassForGeneration {
	private static String grammarPath="grammar";

	private static String language="C++";
	private static String postfix=".cpp";
	private static String loopMaxmum = "1";
	private static String addNumber = "1";
	private static String starNumber = "1";
	private static String cycleNumber = "2";
	private static String continuity = "identifier,Digits,Letter,IDENTIFIER,classOrInterfaceType,qualifiedName,STRING_LITERAL,string,double,OR,boolExpression,boolParExpression,updateExpression";
	private static String uniqueness = "classOrInterfaceModifier,requiresModifier,modifier,interfaceMethodModifier,classBodyDeclaration,variableModifier,finallyBlock";
	private static String termination = "expression 2,parExpression 2,statement 2,stringExpression 2,floatExpression 2,integerExpression 2";
	private static String generateNum = "10";
	private static String identifierRelation = "random";
//	private static String mutationStrategy = "AST-Fun-mutation";
	private static String mutationStrategy = "main-mutation";
	private static String generateResultPath = "generate-output";
//	private static String sourcePath = "source";
	private static String sourcePath = "small-set-for-test";
	private static String astPath = "astObj";
	private static String help = "0";
	private static String index= LocalDate.now().toString();
	private static String identifierEnumeration="true";
//private static String index= "2025-1-23";
	public static void main(String[] args){
		// 检查是否有--help参数
		boolean showHelp = false;
		for (String arg : args) {
			if ("--help".equals(arg)) {
				showHelp = true;
				break;
			}
		}

		// 如果需要显示帮助信息，则打印出来
		if (showHelp) {
			printHelpInfo();
			return; // 退出程序，因为用户只想要帮助信息
		}


		for (int i = 0; i < args.length; i += 2) {
			String key = args[i];
			String value = (i + 1 < args.length) ? args[i + 1] : ""; // 如果没有提供值，则默认为空字符串

			switch (key) {
				case "--help":
					help = value;
					break;
				case "--grammarPath":
					grammarPath = value;
					break;
				case "--language":
					language = value;
					break;
				// ... 为其他参数添加case语句 ...
				case "--postfix":
					// 假设有一个变量 postfix
					postfix = value;
					break;
				case "--loopMaximum":
					loopMaxmum = value;
					break;
				case "--addNumber":
					addNumber = value;
					break;
				case "--starNumber":
					starNumber = value;
					break;
				case "--cycleNumber":
					cycleNumber = value;
					break;
				case "--continuity":
					continuity = value;
					break;
				case "--uniqueness":
					uniqueness = value;
					break;
				case "--termination":
					termination = value;
					break;
				case "--generateNum":
					generateNum = value;
					break;
				case "--identifierRelation":
					identifierRelation = value;
					break;
				case "--mutationStrategy":
					mutationStrategy = value;
					break;
				case "--generateResultPath":
					generateResultPath = value;
					break;
				case "--sourcePath":
					sourcePath = value;
					break;
				case "--astPath":
					astPath = value;
					break;
				case "--index":
					index = value;
					break;
				case "--identifierEnumeration":
					identifierEnumeration = value;
					break;

				default:
					System.err.println("Unknown argument: " + key);
			}
		}


		//parser
		ConfigureInfo.directory=grammarPath;
		//language
		MainClassForGeneration mcg=new MainClassForGeneration();
		mcg.setLanguage(language);
		//postfix
		ConfigureInfo.postfix=postfix;

		//optimization
		//代码中循环执行次数
		ConfigureInfo.loopCountMax=Integer.parseInt(loopMaxmum);
		//+号最大值
		ConfigureInfo.addnum=Integer.parseInt(addNumber);
		//*号最大值
		ConfigureInfo.starnum=Integer.parseInt(starNumber);
		//循环生成次数最大值
		ConfigureInfo.cycleStepLength=Integer.parseInt(cycleNumber);


		//generation
		//identifier,Digits
		String[] continuityArr=continuity.split(",");
		List<String> continuityConstraint=Arrays.asList(continuityArr);
		if(ConfigureInfo.constraint==null) {
			ConfigureInfo.constraint=new Constraint();
		}
		ConfigureInfo.constraint.setContinuityList(continuityConstraint);
		//finallyBlock,modifier
		String[] uniquenessArr=uniqueness.split(",");
		List<String> uniquenessConstraint=Arrays.asList(uniquenessArr);
		ConfigureInfo.constraint.setUniquenessList(uniquenessConstraint);
		//expression 5,parExpression 5
		String[] terminationArr=termination.split(",");
		List<String> terminationConstraint=Arrays.asList(terminationArr);
		Map<String,Integer> terminationMap=mcg.listToMap(terminationConstraint);
		ConfigureInfo.constraint.setTerminationMap(terminationMap);

		//Identifier relation strategy
		ConfigureInfo.identifierRelation=identifierRelation;

		//mutation
		if (mutationStrategy.equals("no mutation")) {
			ConfigureInfo.mutationAvailable = false;
		} else {
			ConfigureInfo.mutationAvailable = true;
		}
		// 设置变异策略
		ConfigureInfo.selectedMutationStrategy = mutationStrategy.trim();

		//设置变异源程序路径
		ConfigureInfo.sourcePath=sourcePath;
		//设置源程序解析后的ast序列化存储路径
		ConfigureInfo.astPath=astPath;

		//生成数量10
		ConfigureInfo.generateNum=Integer.parseInt(generateNum);
		//输出路径
		ConfigureInfo.outPutdirectory=generateResultPath;


		//输出路径以日期区分不同批次
		ConfigureInfo.index=index;

		//标识符枚举选项开启
		if (identifierEnumeration.equals("true")) {
			ConfigureInfo.identifierEnumeration = true;
		} else {
			ConfigureInfo.identifierEnumeration = false;
		}


		GenerateProgramsByGrammar generateProgramsByGrammar=new GenerateProgramsByGrammar();
		try {
			////System.out.println("======================================================");
			generateProgramsByGrammar.generateTestProgram();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//System.out.println("automatic error");
			e1.printStackTrace();
		}


	}

	/**
	 * qurong
	 * 2025-1-10
	 * 打印帮助信息
	 */
	private static void printHelpInfo() {
		System.out.println("Usage: java ScopeFuzz [options]");
		System.out.println("Options:");
		System.out.println("--grammarPath <path>         Path of the grammar file");
		System.out.println("--language <language>        Programming language to generate code for, the value can be one of the list:C++,C, Java, Python,Other, the default value is C++");
		System.out.println("--postfix <postfix>          Postfix to add to generated files, the value can be one of the list:.cpp,.c, .java, .py,Other, the default value is .cpp");
		System.out.println("--loopMaximum <number>       Maximum number of loops to generate, the default number is 1");
		System.out.println("--addNumber <number>         Number of '+' to generate, the default number is 1");
		System.out.println("--starNumber <number>        Number of '*' to generate, the default number is 1");
		System.out.println("--cycleNumber <number>       Maximum number of grammar cycles in generate for token, the default number is 2");
		System.out.println("--continuity <List>    Whether to generate continuous code for the token, the default value is 1");
		System.out.println("--uniqueness <List>    Whether to ensure uniqueness of generated token");
		System.out.println("--termination <List>       Maximum number of grammar cycles in generate for all the tokens");
		System.out.println("--generateNum <number>       Number of programs to generate, the default number is 100");
		System.out.println("--identifierRelation <type>  Type of identifier relation to use,the value can be one of the list: new, nearest, farthest,random,PASI,PISS, the default value is random");
		System.out.println("--mutationStrategy <strategy>  Mutation strategy to apply,the value can be one of the list: no-mutation, comments-only,main-mutation,scope-mutation,AST-Fun-mutation, the default value is AST-Fun-mutation");
		System.out.println("--generateResultPath <path>  Path to save generated results, the default value is generate-output");
		System.out.println("--sourcePath <path>  Path of source programs for mutation, the default value is source");
		System.out.println("--astPath <path>  Path to save ast of parsed source programs, the default value is astObj");
		System.out.println("--help                       Display this help message");

	}

	/**
	 * list to map
	 * @param terminationConstraint
	 * @return
	 */
	protected Map<String, Integer> listToMap(List<String> terminationConstraint) {
		// TODO Auto-generated method stub
		Map<String, Integer> result=new HashMap<String, Integer>();
		if(terminationConstraint==null) {
			return null;
		}
		int i=0;
		while(i<terminationConstraint.size()) {
			String[] data=terminationConstraint.get(i).split(" ");
			if(data.length!=2) {
				return null;
			}
			result.put(data[0], Integer.parseInt(data[1]));
			i++;
		}
		return result;
	}


	protected void setLanguage(String language) {
		// TODO Auto-generated method stub
		if(language=="C") {
			ConfigureInfo.ifCCode=true;
			ConfigureInfo.ifJavaCode=false;
			ConfigureInfo.ifPythonCode=false;
			ConfigureInfo.ifCPlusCode=false;
		}else if(language=="C++") {
			ConfigureInfo.ifCCode=false;
			ConfigureInfo.ifJavaCode=false;
			ConfigureInfo.ifPythonCode=false;
			ConfigureInfo.ifCPlusCode=true;
		}else if(language=="Java") {
			ConfigureInfo.ifCCode=false;
			ConfigureInfo.ifJavaCode=true;
			ConfigureInfo.ifPythonCode=false;
			ConfigureInfo.ifCPlusCode=false;
		}else if(language=="Python") {
			ConfigureInfo.ifCCode=false;
			ConfigureInfo.ifJavaCode=false;
			ConfigureInfo.ifPythonCode=true;
			ConfigureInfo.isFangzhouMulti=false;
			ConfigureInfo.ifCPlusCode=false;
		}else if(language=="Other"){
			ConfigureInfo.ifCCode=false;
			ConfigureInfo.ifJavaCode=false;
			ConfigureInfo.ifPythonCode=false;
			ConfigureInfo.isFangzhouMulti=false;
			ConfigureInfo.ifCPlusCode=false;
		}else {
			ConfigureInfo.ifCCode=false;
			ConfigureInfo.ifJavaCode=false;
			ConfigureInfo.ifPythonCode=false;
			ConfigureInfo.isFangzhouMulti=false;
			ConfigureInfo.ifCPlusCode=false;
		}

	}
}
