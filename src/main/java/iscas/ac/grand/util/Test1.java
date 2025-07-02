package iscas.ac.grand.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Test1{
	
//	public static String root="H:"+File.separator+"compilertest"+File.separator+"龚可-王锐差异分析";
//	public static String root="H:"+File.separator+"compilertest"+File.separator+"龚可-王锐差异分析0304lambda";
	//public static String root="H:"+File.separator+"compilertest"+File.separator+"龚可-王锐差异分析0229lambda"+File.separator+"0304-nearest"+File.separator+"build";
//	public static String root="H:"+File.separator+"compilertest"+File.separator+"龚可-王锐差异分析0229lambda"+File.separator+"0307-random";
	public static String root="H:"+File.separator+"compilertest"+File.separator+"龚可-王锐差异分析0229lambda"+File.separator+"0312PISS-46";
	public static String srcDir="H:"+File.separator+"demo"+File.separator+"cplus"+File.separator+"0312"+File.separator+"PISS"+File.separator+"programs";
	public static void main(String args[]) throws IOException{
		deleteUnralatedFiles();
//		copyRalatedFiles();
	}
	public 	static void copyRalatedFiles() {
		String path=root+""+File.separator+"compiler";
		String difflog_path="difflog";
		Map<String,List<String>> programCompilers=new HashMap<>();
		Map<String,Set<String>> programCompilersSet=new HashMap<>();
		Set<String> uniquePrograms=new HashSet<>();
		
		
		File folder = new File(path); //遍历不同编译器结果
		if (folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File file : files) {
	                if(file.exists() && file.isDirectory()) {//进到不同的编译器目录下
	                	String compilerName=file.getName();//编译器名称getName()
	                	File folder2 = new File(file.getAbsolutePath()+""+File.separator+""+difflog_path); // 指定目标文件夹的路径
	                    if (folder2.exists() && folder2.isDirectory()) {//进到difflog目录下
	                    	Set<String> uniqueProgramsOfOneCompiler=new HashSet<>();
	                        File[] files2 = folder2.listFiles();
	                        if (files2 != null) {
	                          for (File file2 : files2) {//每一个difflog.txt
	                        	  String diffFileName=file2.getName();
	                        	  diffFileName=diffFileName.replace("inconsistencies_with_", "");
	                        	  diffFileName=diffFileName.replace(".log", "");
	                            try (BufferedReader reader = new BufferedReader(new FileReader(file2))) {
	                              String line="";
	                              while ((line = reader.readLine()) != null) {
	                                // 处理每一行内容
	                            	  uniquePrograms.add(line);
	                            	  uniqueProgramsOfOneCompiler.add(line);
	                            	  String pair="("+compilerName+","+diffFileName+")";
	                            	  String reversePair="("+diffFileName+","+compilerName+")";
	                            	  List<String> compilers=programCompilers.get(line);
	                            	  if(compilers==null) {
	                            		  compilers=new ArrayList<>();
	                            		  //compilers.add(compilerName);
	                            	  }else if(!compilers.contains(pair)&&!compilers.contains(reversePair)) {
	                            		  //compilers.add(diffFileName);
	                            		  compilers.add(pair);
	                            	  }
	                            	  programCompilers.put(line, compilers);
	                            	  
	                            	  Set<String> compilersSet=programCompilersSet.get(line);
	                            	  if(compilersSet==null) {
	                            		  compilersSet=new HashSet<>();
	                            		 // compilersSet.add(compilerName);
	                            		  compilersSet.add("("+compilerName+","+diffFileName+")");
	                            	  }else if(!compilersSet.contains(pair)&&!compilersSet.contains(reversePair)) {
	                            		  //compilersSet.add(diffFileName);
	                            		  compilersSet.add(pair);
	                            	  }
	                            	  programCompilersSet.put(line, compilersSet);
	                              }
	                            } catch (IOException e) {
	                              e.printStackTrace();
	                            }
	                          }
	                        }
	                    }
	                }
	              }
			}
		}
		
        
		//program_compilersSet
		//uniquePrograms
		copyFile(uniquePrograms,srcDir,root+"\\program",".cpp");
		
		outputDiffDetail(programCompilersSet,uniquePrograms,root+"\\diff1.log",root+"\\diff2.log",root+"\\diff3.log");
	    
	}
	/**
	 * qurong
	 * 复制这些文件uniquePrograms到
	 * @param uniquePrograms
	 * @param srcDir2
	 * @param path
	 * @param postfix
	 */
	private static void copyFile(Set<String> uniquePrograms, String srcDir, String path, String postfix) {
		// TODO Auto-generated method stub
		if(uniquePrograms==null||uniquePrograms.size()==0) {
			return;
		}

        File folder = new File(path);
        if (folder.exists()) {
            File[] files = folder.listFiles();
            
            for (File file : files) {
                if (file.isFile()) {
                    // 在这里添加判断条件，只复制特定的文件
//                	String fileName=file.getName();
//                	fileName=fileName.replace(postfix, "");
//                    if(!uniquePrograms.contains(fileName)) {
//                    	boolean deleteSuccessful = file.delete();
//                    	if(deleteSuccessful) {
//                    		//System.out.println("成功删除文件：" + file.getName());
//                    	}else {
//                    		//System.out.println("删除文件失败：" + file.getName());
//                    	}
//                    }
                } else if (file.isDirectory()) {
                    // 若子项为文件夹，则进行相应操作
                	//System.out.println("文件夹不删除：" + file.getName());
                }
            }
        } else {
            //System.out.println("目标文件夹不存在！");
        }
	}
	public 	static void deleteUnralatedFiles() {
		String path=root+"\\compiler";
		String difflog_path="difflog";
		String outPath="out";
		String outPutPath="output";
		Map<String,List<String>> programCompilers=new HashMap<>();
		Map<String,Set<String>> programCompilersSet=new HashMap<>();
		Set<String> uniquePrograms=new HashSet<>();
		
		
		File folder = new File(path); //遍历不同编译器结果
		if (folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File file : files) {
	                if(file.exists() && file.isDirectory()) {//进到不同的编译器目录下
	                	String compilerName=file.getName();//编译器名称getName()
	                	File folder2 = new File(file.getAbsolutePath()+"\\"+difflog_path); // 指定目标文件夹的路径
	                    if (folder2.exists() && folder2.isDirectory()) {//进到difflog目录下
	                    	Set<String> uniqueProgramsOfOneCompiler=new HashSet<>();
	                        File[] files2 = folder2.listFiles();
	                        if (files2 != null) {
	                          for (File file2 : files2) {//每一个difflog.txt
	                        	  String diffFileName=file2.getName();
	                        	  diffFileName=diffFileName.replace("inconsistencies_with_", "");
	                        	  diffFileName=diffFileName.replace(".log", "");
	                            try (BufferedReader reader = new BufferedReader(new FileReader(file2))) {
	                              String line="";
	                              while ((line = reader.readLine()) != null) {
	                                // 处理每一行内容
	                            	  uniquePrograms.add(line);
	                            	  uniqueProgramsOfOneCompiler.add(line);
	                            	  String pair="("+compilerName+","+diffFileName+")";
	                            	  String reversePair="("+diffFileName+","+compilerName+")";
	                            	  List<String> compilers=programCompilers.get(line);
	                            	  if(compilers==null) {
	                            		  compilers=new ArrayList<>();
	                            		  //compilers.add(compilerName);
	                            	  }else if(!compilers.contains(pair)&&!compilers.contains(reversePair)) {
	                            		  //compilers.add(diffFileName);
	                            		  compilers.add(pair);
	                            	  }
	                            	  programCompilers.put(line, compilers);
	                            	  
	                            	  Set<String> compilersSet=programCompilersSet.get(line);
	                            	  if(compilersSet==null) {
	                            		  compilersSet=new HashSet<>();
	                            		 // compilersSet.add(compilerName);
	                            		  compilersSet.add("("+compilerName+","+diffFileName+")");
	                            	  }else if(!compilersSet.contains(pair)&&!compilersSet.contains(reversePair)) {
	                            		  //compilersSet.add(diffFileName);
	                            		  compilersSet.add(pair);
	                            	  }
	                            	  programCompilersSet.put(line, compilersSet);
	                              }
	                            } catch (IOException e) {
	                              e.printStackTrace();
	                            }
	                          }
	                        }
	                        
	                        //删out和output中的多余文件，保留uniqueProgramsOfOneCompiler中的程序
	                        deleteFile(uniqueProgramsOfOneCompiler,file.getAbsolutePath()+"\\"+outPath,"");
	                        deleteFile(uniqueProgramsOfOneCompiler,file.getAbsolutePath()+"\\"+outPutPath,".txt");
	                    }
	                }
	              }
			}
		}
		
        
		//program_compilersSet
		//uniquePrograms
		deleteFile(uniquePrograms,root+"\\cppprogram",".cpp");
		deleteFile(uniquePrograms,root+"\\cprogram",".c");
//		outputDiffDetail(programCompilersSet,uniquePrograms,root+"\\diff1.log",root+"\\diff2.log",root+"\\diff3.log",outPath,outPutPath);
		outputDiffDetail(programCompilersSet,uniquePrograms,root+"\\diff1.log",root+"\\diff2.log",root+"\\diff3.log");
	    //分成两个部分
		//splitFiles(path,programCompilersSet,outPath,outPutPath);
	}

	/**
	 * 对文件进行分割，分成两个部分
	 * @param path
	 * @param outPutPath 
	 * @param outPath 
	 * @param programCompilersSet 
	 */
	private static void splitFiles(String path, Set<String> programCompilersSet, String outPath, String outPutPath) {
		// TODO Auto-generated method stub
		File folder3 = new File(path); //遍历不同编译器结果
		if (folder3.exists() && folder3.isDirectory()) {
			File[] files = folder3.listFiles();
			if (files != null) {
				for (File file : files) {
	                if(file.exists() && file.isDirectory()) {//进到不同的编译器目录下
	                	String compilerName=file.getName();//编译器名称getName()
	                	 deleteFile(programCompilersSet,file.getAbsolutePath()+"\\"+outPath,"");
	                     deleteFile(programCompilersSet,file.getAbsolutePath()+"\\"+outPutPath,".txt");
	                     //diff
	                     File folder4 = new File(file.getAbsolutePath()+"\\diff"); //遍历差异文件
	                     if (folder4.exists() && folder4.isDirectory()) {
	             			File[] diffWithDics = folder4.listFiles();
	             			if (diffWithDics != null) {
	             				for (File diffDic : diffWithDics) {
	             	                if(diffDic.exists() && file.isDirectory()) {//进到不同的diffwith目录下
	             	                	deleteFile(programCompilersSet,diffDic.getAbsolutePath(),".diff");
	             	                	deleteFile(programCompilersSet,diffDic.getAbsolutePath()+"\\cppprogram",".cpp");
	             	                	deleteFile(programCompilersSet,diffDic.getAbsolutePath()+"\\cprogram",".c");
	             	                }
	             				}
	             			}
	                     }
	                     
	                     
	                }
				}
			}
		}
	}

	private static void splitPrograms(String string, int i, int j, Map<String, Set<String>> programCompilersSet) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 输出 程序和其输出存在差异的编译器名称
	 * @param program_compilersSet
	 * @param uniquePrograms
	 * @param j 
	 * @param i 
	 */
//	private static void outputDiffDetail(Map<String, Set<String>> programCompilersSet, Set<String> uniquePrograms,String outputPath1,String outputPath2,String outputPath3,String out,String output) {
	private static void outputDiffDetail(Map<String, Set<String>> programCompilersSet, Set<String> uniquePrograms,String outputPath1,String outputPath2,String outputPath3) {
		// TODO Auto-generated method stub
		if(programCompilersSet==null||programCompilersSet.size()==0) {
			return;
		}
		if(uniquePrograms==null||uniquePrograms.size()==0) {
			return;
		}
		if(programCompilersSet.size()!=uniquePrograms.size()) {
			//System.out.println("个数不匹配：\n" + programCompilersSet.size()+"\n"+uniquePrograms.size());
		}
		String outputContent1="";
		String outputContent2="";
		String temp1="";
		String temp2="";
		List<String> items1=new ArrayList<>();
		List<String> items2=new ArrayList<>();
		
		Set<String> uniquePrograms1=new HashSet<>();
		Set<String> uniquePrograms2=new HashSet<>();
		Iterator<String> iterator = programCompilersSet.keySet().iterator();
		int index=0;
	    while (iterator.hasNext()) {
	    	String key = iterator.next();
	        Set<String> value = programCompilersSet.get(key);
	    	if(index>=0&&index<50) {
	    		uniquePrograms1.add(key);
	    		temp1="\n"+key+":\n";
		        if(value!=null&&value.size()>0) {
		        	for(String compilerName:value) {
		        		temp1+=compilerName+"\t";
		        	}
		        	temp1+="\n";
		        }
		        items1.add(temp1);
	    	}else if(index>=50&&index<programCompilersSet.size()){
	    		uniquePrograms2.add(key);
	    		temp2="\n"+key+":\n";
		        if(value!=null&&value.size()>0) {
		        	for(String compilerName:value) {
		        		temp2+=compilerName+"\t";
		        	}
		        	temp2+="\n";
		        }
		        items2.add(temp2);
	    	}else {
	    		//System.out.println("size长度问题" );
	    	}
	    	index++;
	    }
	    //排序
	    Collections.sort(items1);
	    Collections.sort(items2);
	    StringBuilder sb = new StringBuilder(); // 创建一个StringBuilder对象
        for (String str : items1) {
            sb.append(str); // 将每个字符串添加到StringBuilder中
        }
        outputContent1 = sb.toString(); // 转换为最终结果的字符串形式
        
        StringBuilder sb2 = new StringBuilder(); // 创建一个StringBuilder对象
        for (String str : items2) {
            sb2.append(str); // 将每个字符串添加到StringBuilder中
        }
        outputContent2 = sb2.toString(); // 转换为最终结果的字符串形式
        
	    try (FileWriter writer = new FileWriter(outputPath1)) {
            writer.write(outputContent1);
        } catch (IOException e) {
            //System.out.println("写入文件1时发生错误：" + e.getMessage());
        }
	    try (FileWriter writer = new FileWriter(outputPath2)) {
            writer.write(outputContent2);
        } catch (IOException e) {
            //System.out.println("写入文件2时发生错误：" + e.getMessage());
        }
	    
	    try (FileWriter writer = new FileWriter(outputPath3)) {
            writer.write(outputContent1+outputContent2);
        } catch (IOException e) {
            //System.out.println("写入文件2时发生错误：" + e.getMessage());
        }
	    
	    deleteFile(uniquePrograms1,root+"\\1-50\\cppprogram",".cpp");
		deleteFile(uniquePrograms1,root+"\\1-50\\cprogram",".c");
		
		deleteFile(uniquePrograms2,root+"\\51-107\\cppprogram",".cpp");
		deleteFile(uniquePrograms2,root+"\\51-107\\cprogram",".c");
		
		//splitFiles(root+"\\1-50\\compiler",uniquePrograms1,out,output);
		//splitFiles(root+"\\51-107\\compiler",uniquePrograms2,out,output);
	}


	/**
	 * 删除某个文件夹下，不在programIDs集合中的文件
	 * @param programIDs
	 * @param path
	 * @param postfix
	 */
	private static void deleteFile(Set<String> programIDs, String path, String postfix) {
		// TODO Auto-generated method stub
		if(programIDs==null||programIDs.size()==0) {
			return;
		}

        File folder = new File(path);
        if (folder.exists()) {
            File[] files = folder.listFiles();
            
            for (File file : files) {
                if (file.isFile()) {
                    // 在这里添加判断条件，只删除特定的文件
                	String fileName=file.getName();
                	fileName=fileName.replace(postfix, "");
                    if(!programIDs.contains(fileName)) {
                    	boolean deleteSuccessful = file.delete();
                    	if(deleteSuccessful) {
                    		//System.out.println("成功删除文件：" + file.getName());
                    	}else {
                    		//System.out.println("删除文件失败：" + file.getName());
                    	}
                    }
                } else if (file.isDirectory()) {
                    // 若子项为文件夹，则进行相应操作
                	//System.out.println("文件夹不删除：" + file.getName());
                }
            }
        } else {
            //System.out.println("目标文件夹不存在！");
        }
	}
}