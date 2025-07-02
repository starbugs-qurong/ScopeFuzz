package iscas.ac.grand.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CPlusFeatureCountByFiles {
    public static void main(String[] args) {
        // 假设你想要列出的目录是 "C:/exampleFolder"
        String directoryPath = "F:"+File.separator+"data-compiler-testing"+File.separator+"Diagnostic";
        Map<String, Integer> featureNumMap=new HashMap<>();
        featureNumMap=initFeatureNumMap();
        int newFeatureFile=0;
        int baseFeatureFile=0;

        File directory = new File(directoryPath);

        // 获取目录下所有文件和文件夹
        File[] files = directory.listFiles();

        if (files != null) { // 确保文件数组不为空
            for (File file : files) {
                if (file.isFile()) { // 检查是否是文件
                	
                    System.out.println("# "+file.getName()); // 打印文件名
                    String fileContent="";
                    try {
						List<String> filelines=Files.readAllLines(file.toPath());
						fileContent=appendLiat(filelines);//合并文件内容
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    List<String> featureListofFile=collectFeatures(featureNumMap,fileContent);//打开文件，搜索包含的新特性
                    
                    for(String s:featureListofFile) {
                    	System.out.print(s+" "); // 打印该文件中的新特性
                    }
                    System.out.println(); // 打印换行
                    if(featureListofFile!=null&&featureListofFile.size()>=1) {
                    	newFeatureFile=newFeatureFile+1;
                    }else {
                    	baseFeatureFile=baseFeatureFile+1;
                    }
                    
                    featureNumMap=updateFeatureMap(featureNumMap,featureListofFile);//更新新特性频次统计表
                    
                }
            }
        }
        
        System.out.print("新特性文件数： "+newFeatureFile); // 打印新特性文件数量
        System.out.print("不包含新特性文件数： "+baseFeatureFile); // 打印不包含新特性文件数量
        Set<String> keys = featureNumMap.keySet();
        // 遍历所有特性，并输出特性名称和对应出现频次（一个文件中出现多次只算一次）
        for (String key : keys) {
            System.out.println(key + "\t" + featureNumMap.get(key));
        }
        
    }

	private static Map<String, Integer> initFeatureNumMap() {
		// TODO Auto-generated method stub
		Map<String, Integer> featureNumMap=new HashMap<>();
		try {
//		    FileReader fileReader = new FileReader("F:"+File.separator+"data-compiler-testing"+File.separator+"1.txt");
		    FileReader fileReader = new FileReader("F:"+File.separator+"data-compiler-testing"+File.separator+"2.txt");
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
//		    List<String> lines = Files.readAllLines(Paths.get("H:"+File.separator+"compilertest"+File.separator+"草稿ISSTA2024未投C++ feature complier differential testing"+File.separator+"王锐"+File.separator+"去重后的新特性种类.txt"));
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