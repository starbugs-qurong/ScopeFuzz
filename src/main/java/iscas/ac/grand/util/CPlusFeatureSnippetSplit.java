package iscas.ac.grand.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.io.FileNotFoundException;

import iscas.ac.grand.main.common.GrammarGetService;
import iscas.ac.grand.main.common.ScopeTree;
import iscas.ac.grand.main.mutation.CppProgram;

public class CPlusFeatureSnippetSplit {
	public static ScopeTree rootScope=new ScopeTree(0,null);
    public static void main(String[] args) {
        // 假设你想要列出的目录是 "C:/exampleFolder"
        String directoryPath1 = "F:\\data-compiler-testing\\wrong-code";
    	String directoryPath2 = "F:\\data-compiler-testing\\rejects-valid";
    	String directoryPath3 = "F:\\data-compiler-testing\\ice-on-valid-code";
    	String directoryPath4 = "F:\\data-compiler-testing\\diagnostic";

        Map<String, Integer> wordCountMap=new HashMap<>();
        List<List<String>> programs=new ArrayList<>();//触发过编译器缺陷的C++文件 按照一行一行来存储
        List<CppProgram> programsList=new ArrayList<>();//触发过编译器缺陷的C++程序 按照程序来存储
        //清洗，得到程序列表

        File directory = new File(directoryPath1);
        
        // 获取目录下所有文件和文件夹
        File[] files = directory.listFiles();
 
        if (files != null) { // 确保文件数组不为空
            for (File file : files) {
                if (file.isFile()) { // 检查是否是文件
                	CppProgram cp=new CppProgram(rootScope);
                	cp.setID(file.getName());
                	List<String> programLines=new ArrayList<>();
                    System.out.println("# "+file.getName()); // 打印文件名
                    String fileContent="";
                    try {
                    	BufferedReader fileReader = new BufferedReader(new FileReader(file.getAbsolutePath()));//触发过编译器缺陷的C++文件
                    	String line="";
                    	while ((line = fileReader.readLine()) != null) {//清洗注释
                            //不保留行尾注释
                            line=line.trim();
                            if(line=="") {
                                continue;//忽略这一个空行
                            }
                            //忽略注释行
                            if(line.startsWith("//")||line.startsWith("/*")){
//                                fileReader=deleteCommentsLine(line,fileReader);
                                String validCOn=GrammarGetService.deleteCommentsLine(line,fileReader);
                                if(validCOn!=null&&validCOn!=""){
                                    line=validCOn;
                                }
                                else{
                                    continue;
                                }
                            }
                            programLines.add(line);
                    	}
                    	
						List<String> filelines=Files.readAllLines(file.toPath());
						fileContent=appendLiat(filelines);//合并文件内容
						
						wordCountMap=WordCount(wordCountMap,file,fileContent);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    programs.add(programLines);//程序列表（按字符串 且按行存储）
                    cp =spiltProgram(cp,programLines);//拆解程序中的结构片段
                    programsList.add(cp);
                }
            }
        }
        //包含关键字的程序片段个数
//        outPutContainSnippet(wordCountMap);
        
        //关键字程序片段个数
//        outPutEqualnippet(wordCountMap);
        
        
        //计算各个类型的片段个数
//        countNum(programsList);
        
    }
    
    

    /**
     * qurong
     * 2024-12-12
     * 输出关键字对应的程序片段个数
     * @param wordCountMap
     */
    private static void outPutEqualSnippet(Map<String, Integer> wordCountMap) {
		// TODO Auto-generated method stub
    	System.out.println("===============equal begin===================");
    	for (Map.Entry<String, Integer> entry :wordCountMap.entrySet() ) {
            if(entry.getKey().equals("include")) {
            	System.out.println(entry.getKey() + " : " + entry.getValue());
            }else if(entry.getKey().equals("template")) {
            	System.out.println(entry.getKey() + " : " + entry.getValue());
            }else if(entry.getKey().equals("struct")) {
            	System.out.println(entry.getKey() + " : " + entry.getValue());
            }else if(entry.getKey().equals("while")) {
            	System.out.println(entry.getKey() + " : " + entry.getValue());
            }else if(entry.getKey().equals("for")) {
            	System.out.println(entry.getKey() + " : " + entry.getValue());
            }else if(entry.getKey().equals("if")) {
            	System.out.println(entry.getKey() + " : " + entry.getValue());
            }else if(entry.getKey().equals("=")) {
            	System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        	
        }
    	System.out.println("===============equal end===================");
	}



    /**
     * qurong
     * 2024-12-12
     * 输出包含关键字的片段个数
     * @param wordCountMap
     */
	private static void outPutContainSnippet(Map<String, Integer> wordCountMap) {
		// TODO Auto-generated method stub
    	System.out.println("===============contain begin===================");
    	for (Map.Entry<String, Integer> entry :wordCountMap.entrySet() ) {
            if(entry.getKey().contains("include")) {
            	System.out.println(entry.getKey() + " : " + entry.getValue());
            }else if(entry.getKey().contains("template")) {
            	System.out.println(entry.getKey() + " : " + entry.getValue());
            }else if(entry.getKey().contains("struct")) {
            	System.out.println(entry.getKey() + " : " + entry.getValue());
            }else if(entry.getKey().contains("while")) {
            	System.out.println(entry.getKey() + " : " + entry.getValue());
            }else if(entry.getKey().contains("for")) {
            	System.out.println(entry.getKey() + " : " + entry.getValue());
            }else if(entry.getKey().contains("if")) {
            	System.out.println(entry.getKey() + " : " + entry.getValue());
            }else if(entry.getKey().contains("=")) {
            	System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        	
        }
    	System.out.println("===============contain end===================");
	}



	/**
     * qurong 
     * 2024-12-12
     * 使用新的数据来更新原有数据，单词频次需要累加
     * @param wordCount
     * @return
     */
    private static Map<String, Integer> updateMap(Map<String, Integer> wordCount) {
		// TODO Auto-generated method stub
    	for (Map.Entry<String, Integer> entry :wordCount.entrySet() ) {
           
            }
    	return null;
	}



	/**
     * qurong
     * 2024-12-12
     * 计算各种程序片段的数量
     * @param programsList
     */
    private static void countNum(List<CppProgram> programsList) {
		// TODO Auto-generated method stub
    	int AssignmentListCount=0;
    	int functionListCount=0;
    	int  templateListCount=0;
    	int  structListCount=0;
    	int  lambdaListCount=0;
    	int  whileListCount=0;
    	int  forListCount=0;
    	int  ifListCount=0;
    	int  includeListCount=0;
        for(CppProgram cp:programsList) {
        	AssignmentListCount+=cp.getAssignmentList().size();
        	functionListCount+=cp.getFunctionList().size();
        	templateListCount+=cp.getTemplateList().size();
        	structListCount+=cp.getStructList().size();
        	lambdaListCount+=cp.getLambdaList().size();
        	whileListCount+=cp.getWhileList().size();
        	forListCount+=cp.getForList().size();
        	ifListCount+=cp.getIfList().size();
        	includeListCount+=cp.getIncludeList().size();
        }
        System.out.println("AssignmentListCount: " +AssignmentListCount);
        System.out.println("functionListCount: " +functionListCount);
    	System.out.println("templateListCount: " +templateListCount);
    	System.out.println("structListCount: " +structListCount);
    	System.out.println("lambdaListCount: " +lambdaListCount);
    	System.out.println("whileListCount: " +whileListCount);
    	System.out.println("forListCount: " +forListCount);
    	System.out.println("ifListCount: " +ifListCount);
    	System.out.println("includeListCount: " +includeListCount);
	}



	/**
     * qurong
     * 2024-12-12
     * 拆解程序中的各种类型的程序片段
     * @param cp
     * @param programLines
     * @return
     */
    private static CppProgram spiltProgram(CppProgram cp, List<String> programLines) {
		// TODO Auto-generated method stub
    	//include
    	
    	
		return null;
	}
    
     
    public static Map<String, Integer> WordCount(Map<String, Integer> wordCountMap,File file,String content) throws FileNotFoundException {
        
//            File file = new File("text.txt"); // 替换为你的文本文件路径
//            Scanner scanner = new Scanner(file);
//            Pattern SPACE_PATTERN = Pattern.compile("\\s+");
//            String content = scanner.useDelimiter(SPACE_PATTERN).next();
//            scanner.close();
     
//            String[] words = content.split("\\s+");
    		String[] words = content.split(",| +|;");
            Map<String, Integer> wordCount = new HashMap<>();
     
            for (String word : words) {
                word = word.toLowerCase(); // 转换为小写
                word.trim();
                if (word.isEmpty()) continue;
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
            wordCount.forEach((word, count) -> wordCountMap.put(word,wordCountMap.getOrDefault(word, 0) +count));
//            wordCount.forEach((word, count) -> System.out.println(word + ": " + count));
            return wordCountMap;
    }

	/**
     * qurong
     * 2024-12-12
     * 清洗程序中的无用内容，如头文件和程序中的注释等等
     * @param fileContent
     * @return
     */
	private static String cleanPrograms(String fileContent) {
		// TODO Auto-generated method stub
		
		return null;
	}

	private static Map<String, Integer> initFeatureNumMap() {
		// TODO Auto-generated method stub
		Map<String, Integer> featureNumMap=new HashMap<>();
		try {
		    FileReader fileReader = new FileReader("F:\\data-compiler-testing\\1.txt");
		    BufferedReader bufferedReader = new BufferedReader(fileReader);
		    String line;
		    while ((line = bufferedReader.readLine()) != null) {
		        // 处理每一行的代码逻辑
		    	featureNumMap.put(line,0);
		    }
		    bufferedReader.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		
//		try {
//		    List<String> lines = Files.readAllLines(Paths.get("H:\\compilertest\\草稿ISSTA2024未投C++ feature complier differential testing\\王锐\\去重后的新特性种类.txt"));
//		    for (String line : lines) {
//		        // 处理每一行的代码逻辑
//		    	featureNumMap.put(line,0);
//		    }
//		} catch (IOException e) {
//		    e.printStackTrace();
//		}
		return featureNumMap;
		
	}

	private static String appendLiat(List<String> filelines) {
		// TODO Auto-generated method stub
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < filelines.size(); i++) {
		  sb.append(filelines.get(i));
		  if (i < filelines.size() - 1) {
		    sb.append(",");
		  }
		}
		 
		String result = sb.toString();
		return result;
	}

	private static Map<String, Integer> updateFeatureMap(Map<String, Integer> featureNumMap, List<String> featureListofFile) {
		// TODO Auto-generated method stub
		if(featureNumMap == null) {
			featureNumMap=new HashMap<>();
		}
		if(featureListofFile == null) {
			return featureNumMap;
		}
		for(String feature:featureListofFile) {
			if(featureNumMap.containsKey(feature)) {
				featureNumMap.replace(feature, featureNumMap.get(feature), featureNumMap.get(feature)+1);
			}
		}
		return featureNumMap;
	}

	private static List<String> collectFeatures(Map<String, Integer> featureNumMap, String fileContent) {
		// TODO Auto-generated method stub
		List<String> result=new ArrayList<>();
		if(featureNumMap == null||fileContent == null) {
			return result;
		}
		Set<String> keys = featureNumMap.keySet();
        // 遍历所有特性（key），并判断文件内容中是否包含该特性
        for (String key : keys) {
        	if(fileContent.contains(key)) {
        		result.add(key);
        	}
        }
		return result;
	}
}