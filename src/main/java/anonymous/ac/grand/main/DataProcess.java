package anonymous.ac.grand.main;

import anonymous.ac.grand.main.common.PrintData;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class DataProcess {

    public List<String> res = new ArrayList<>();

    public List<String> getRes() {
        return res;
    }
    public static void main(String[] args) throws IOException {
        DataProcess dp = new DataProcess();
//        dp.outputIssueContent("F:"+File.separator+"produce_program相关"+File.separator+"C"+File.separator+"data"+File.separator+"三个编译器结果不一致的测试用例");
//        dp.deleteIrrelevantFile("F:"+File.separator+"produce_program相关"+File.separator+"C"+File.separator+"data"+File.separator+"提交issue对应的程序");
//        dp.renameFile("F:\\produce_program相关\\C\\data\\提交issue对应的程序");

        //C
//        dp.diff("msvc","expected","H:\\程序生成及编译器测试\\测试结果\\黄渐刚\\输出不同的那些程序以及结果\\windows\\");
//        dp.diff("clang","expected","H:\\程序生成及编译器测试\\测试结果\\黄渐刚\\输出不同的那些程序以及结果\\windows\\");
//        dp.diff("gcc","expected","H:\\程序生成及编译器测试\\测试结果\\黄渐刚\\输出不同的那些程序以及结果\\windows\\");
//        dp.diff("intel","expected","H:\\程序生成及编译器测试\\测试结果\\黄渐刚\\输出不同的那些程序以及结果\\windows\\");


        //python
//        dp.samePy("coden","H:\\compilertest\\test_result\\python\\2023.3.31\\diff\\");
//        dp.samePy("codenExpected","H:\\compilertest\\test_result\\python\\2023.3.24\\diff\\");
//        dp.diffPy("python3win","python3linux","H:\\compilertest\\test_result\\python\\2023.3.31\\diff\\");
//        dp.diffPy("python3win","pypyout","H:\\compilertest\\test_result\\python\\2023.3.31\\diff\\");
//        dp.diffPy("pypyout","python3linux","H:\\compilertest\\test_result\\python\\2023.3.31\\diff\\");
//        dp.diffPy("pyWin10Expected","expected","H:\\程序生成及编译器测试\\测试结果\\python\\2023.3.24\\diff\\");
        
        String[] compilerNames= {"clang-14-O2","clang-14-withoutO2","clang-15-O2","clang-15-withoutO2","gcc-9-O2","gcc-9-withoutO2","gcc-11-O2","gcc-11-withoutO2","icc-O2","icc-withoutO2","icx-O2","icx-withoutO2"};
        dp.diffWithOtherCCompilers("H:\\compilertest\\test_result\\C\\黄渐刚-4-23\\test\\small_5000_0414\\build\\compiler",
        		compilerNames,
        		"H:\\compilertest\\test_result\\C\\黄渐刚-4-23\\差异分类\\",
        		"H:\\compilertest\\test_result\\C\\黄渐刚-4-23\\test\\small_5000_0414\\build\\cprogram");
    }

    private void renameFile(String root) {
        String index="";
        File file=new File(root);
        File[] children = file.listFiles();
        String path=file.getPath();
        for (File childFile: children) {
            if (childFile.isDirectory()) {
                index = getIndexFromFilename(childFile.getName());
                childFile.renameTo(new File(path+"\\Cprogram"+index));
            }
        }
    }

    public void deleteIrrelevantFile(String root) throws IOException {
        String index="";
        File file=new File(root);
        File[] children = file.listFiles();
        List<File> deleteList=new ArrayList<>();
        for (File childFile: children) {
            if (childFile.isDirectory()){
                File[] gradeChildren = childFile.listFiles();
                index=getIndexFromFilename(childFile.getName());
                for (File gradeChildFile: gradeChildren) {
                    if (gradeChildFile.isFile()) {
                        String name = gradeChildFile.getName();
                        if (name.equals("expected.txt")) {

                        }else if (name.equals("expected1.txt")) {

                        }else if (name.equals("output.log")) {

                        }else if (name.equals("cprogram"+index+".c")) {

                        }else {
                            deleteList.add(gradeChildFile);
                        }
                    }
                }
            }
        }
        int len=deleteList.size();
        for(int i=0;i<len;i++){
            File f=deleteList.get(i);
            f.delete();
        }
    }

    public void outputIssueContent(String root) throws IOException {
        String index;
        String outputContent="";
        File file=new File(root);
        File[] children = file.listFiles();
        for (File childFile: children) {
            if (childFile.isDirectory()){
                File[] gradeChildren = childFile.listFiles();
                outputContent+="\r\n";
                outputContent+=childFile.getName();
                index=getIndexFromFilename(childFile.getName());
                outputContent+="\r\n";
                outputContent+="C test case output different result compared with GCC and LLVM";
                outputContent+="\r\n";
                outputContent+="The C program in this test case is a little long, so I put it in the following link. It is generated by a tool randomly developed by our test team.";
                outputContent+="\r\n";
                outputContent+="\r\n";
                outputContent+="https://gitee.com/star-bugs/open-ark-compiler-test-cases/tree/master/C/Cprogram"+index;
                outputContent+="\r\n";
                for (File gradeChildFile: gradeChildren) {
                    if (gradeChildFile.isFile()) {
                        String name = gradeChildFile.getName();
                        if (name.equals("expected.txt")) {
                            BufferedReader fileReader = new BufferedReader(new FileReader(gradeChildFile));//语法文件
                            String line="";
                            while ((line = fileReader.readLine()) != null) {
                                //不保留行尾注释
                                line = line.trim();
                                if (line == "") {
                                    continue;//忽略这一个空行
                                }else{
                                    outputContent+=line;
                                }
                            }
                            outputContent+="\tGCC\r\n";
                        }else if (name.equals("expected1.txt")) {
                            BufferedReader fileReader = new BufferedReader(new FileReader(gradeChildFile));//语法文件
                            String line="";
                            while ((line = fileReader.readLine()) != null) {
                                //不保留行尾注释
                                line = line.trim();
                                if (line == "") {
                                    continue;//忽略这一个空行
                                }else{
                                    outputContent+=line;
                                }
                            }
                            outputContent+="\tLLVM\r\n";
                        }else if (name.equals("output.log")) {
                            BufferedReader fileReader = new BufferedReader(new FileReader(gradeChildFile));//语法文件
                            String line="";
                            while ((line = fileReader.readLine()) != null) {
                                //不保留行尾注释
                                line = line.trim();
                                if (line == "") {
                                    continue;//忽略这一个空行
                                }else{
                                    outputContent+=line;
                                }
                            }
                            outputContent+="\tfz\r\n";
                        }
                    }
                }
            }
        }
//        //System.out.println(outputContent);
    }

    private static String getIndexFromFilename(String name) {
        String result="";
        String[] names=name.split("program");
        if(names!=null&&names.length>=2){
            String post=names[1];
            String[] strs=post.split("\\.");
            result=strs[0];
        }
        return result;
    }

    /**
     *
     * @param file1 对比编译器输出 msvc  clang intel gcc
     * @param file2 预期编译器输出 expected
     * @param outPath 差异文件输出路径 H:\程序生成及编译器测试\测试结果\黄渐刚\输出不同的那些程序以及结果\windows
     * @throws IOException
     */

    public void diff(String file1,String file2,String outPath) throws IOException {
        String pre = "H:\\程序生成及编译器测试\\测试结果\\黄渐刚\\c\\c\\SANITY000";
        String pre2 = "-cprogram";
        String post1 = "_out.log";
        String post2 = ".txt";
        int i = 0;
        while (i < 1000) {
            File f1 = new File(pre + i + pre2 + i + "\\" + file1 + post1);

            File f2 = new File(pre + i + pre2 + i + "\\" + file2 + post2);

            List<String> outs = Files.readAllLines(f1.toPath());

            List<String> expecteds = Files.readAllLines(f2.toPath());

            StringBuilder diffContent = new StringBuilder(new String(""));

            for (int j = 0; j < outs.size(); j++) {
                if (!outs.get(j).equals(expecteds.get(j))) {
                    diffContent.append(j).append("\t").append(outs.get(j)).append("\t").append(expecteds.get(j)).append("\r\n");
                }
            }

            if (diffContent.length() > 0) {
                PrintData pd = new PrintData();

                pd.writeProgram(outPath + file1 + "\\cprogram" + i + ".diff", diffContent.toString());
            }

            i++;
        }
    }
    
    
/**
 * 
 * @param prefix 编译器输出的路径前缀
 * @param otherCompiersNames 编译器文件名称数组
 * @param differentRecord 用于记录差异数据的路径
 * @param programPath源程序文件的路径
 * @throws IOException
 */
   public void diffWithOtherCCompilers(String prefix,String otherCompiersNames[],String differentRecord,String programPath) throws IOException {
       int i = 0;
       int count1=0;//>=2种输出结果的程序数量
       int count11=0;//=1种输出结果的程序数量
       
       int count2=0;//=2种输出结果的程序数量
       int count3=0;//=3种输出结果的程序数量
       int count4=0;//=4种输出结果的程序数量
       int count5=0;//>=5种输出结果的程序数量
       int count1_11=0;//1个编译器和其它11个编译器的输出不同的程序数量
       int count2_10=0;//2个编译器和其它10个编译器的输出不同的程序数量
       int count3_9=0;//3个编译器和其它9个编译器的输出不同的程序数量
       int count4_8=0;//4个编译器和其它8个编译器的输出不同的程序数量
       int count5_7=0;//5个编译器和其它7个编译器的输出不同的程序数量
       int count6_6=0;//6个编译器和其它6个编译器的输出不同的程序数量
       StringBuilder diffContent = new StringBuilder(new String(""));
       StringBuilder diffContent1_11 = new StringBuilder(new String(""));
       StringBuilder diffContent2_10 = new StringBuilder(new String(""));
       StringBuilder diffContent3_9 = new StringBuilder(new String(""));
       StringBuilder diffContent4_8 = new StringBuilder(new String(""));
       StringBuilder diffContent5_7 = new StringBuilder(new String(""));
       StringBuilder diffContent6_6 = new StringBuilder(new String(""));
       StringBuilder diffContent2 = new StringBuilder(new String(""));
       StringBuilder diffContent3 = new StringBuilder(new String(""));
       StringBuilder diffContent4 = new StringBuilder(new String(""));
       StringBuilder diffContent5 = new StringBuilder(new String(""));
       
       while (i < 5000) {
    	   
    	   List<List<String>> outputs=new ArrayList<List<String>>();//读入所有编译器在同一个程序i上的输出文件内容(精确到每一行)
    	   List<String> outputsStr=new ArrayList<String>();//同一个程序在不同编译器下编译后的运行的输出output list
    	   for(int j=0;j<otherCompiersNames.length;j++) {
    		   String compilerName=otherCompiersNames[j];
    		   File f = new File(prefix+"\\"+compilerName+"\\output\\cprogram"+i+".out");  
    		   List<String> output = Files.readAllLines(f.toPath());
    		   outputs.add(output);
    		   outputsStr.add(output.toString());//同一个程序在不同编译器下编译后的运行的输出output list
//    		   //System.out.println("Program "+i+" compilerName "+j+": "+output.toString());
    	   }
    	   
    	   //确认去重后的输出个数，当>>3种时，不好判断哪个编译器有问题，重点关注=2的情况，如果只有一种说明一致
           int differentCount=0;//去重后的输出内容计数
           List<String> differentOutputs=new ArrayList<String>();//去重后的输出内容列表
           int differentOutputsCount[]=new int[otherCompiersNames.length];//每个不同输出内容出现的次数计数（重复次数）
           for(int j=0;j<differentOutputsCount.length;j++) {
        	   differentOutputsCount[0]=0;
           }
           for(int j=0;j<outputsStr.size();j++) {
        	   int k=0;
        	   if(j==0) {//第一个输出直接加入去重列表
        		   differentOutputs.add(outputsStr.get(j));
        		   differentOutputsCount[j]=1;
        		   continue;
        	   }
        	   for(;k<differentOutputs.size();k++) {
            	   if(differentOutputs.get(k).equals(outputsStr.get(j))) {//和前面已统计的第k个输出重复，该重复计数加1
            		   differentOutputsCount[k]=differentOutputsCount[k]+1;
            		   break;
            	   }
               }
        	   if(k>=differentOutputs.size()) {//该输出内容第一次出现，添加到去重列表中
        		   differentOutputs.add(outputsStr.get(j));
        		   differentOutputsCount[k]=1;
        	   }
           }
           differentCount=differentOutputs.size();
           if(differentCount==1) {
        	   count11++;
        	   diffContent.append(getDiffContent(i,otherCompiersNames,outputsStr,programPath, differentRecord,"all"));//输出output有差异的程序，以及output到路径differentRecord
            }
           
           if(differentCount>=2) {
        	   count1++;
        	   }
           
           if(differentCount==2) {
        	   count2++;
        	   if((differentOutputsCount[0]==1&&differentOutputsCount[1]==11)||(differentOutputsCount[0]==11&&differentOutputsCount[1]==1)) {
        		   count1_11++;
        		   diffContent1_11.append(getDiffContent(i,otherCompiersNames,outputsStr,programPath, differentRecord,"1_11"));
        	   }
        	   
        	   if((differentOutputsCount[0]==2&&differentOutputsCount[1]==10)||(differentOutputsCount[0]==10&&differentOutputsCount[1]==2)) {
        		   count2_10++;
        		   diffContent2_10.append(getDiffContent(i,otherCompiersNames,outputsStr,programPath, differentRecord,"2_10"));
        	   }
        	   if((differentOutputsCount[0]==3&&differentOutputsCount[1]==9)||(differentOutputsCount[0]==9&&differentOutputsCount[1]==3)) {
        		   count3_9++;
        		   diffContent3_9.append(getDiffContent(i,otherCompiersNames,outputsStr,programPath, differentRecord,"3_9"));
        	   }
        	   if((differentOutputsCount[0]==4&&differentOutputsCount[1]==8)||(differentOutputsCount[0]==8&&differentOutputsCount[1]==4)) {
        		   count4_8++;
        		   diffContent4_8.append(getDiffContent(i,otherCompiersNames,outputsStr,programPath, differentRecord,"4_8"));
        	   }
        	   if((differentOutputsCount[0]==5&&differentOutputsCount[1]==7)||(differentOutputsCount[0]==7&&differentOutputsCount[1]==5)) {
        		   count5_7++;
        		   diffContent5_7.append(getDiffContent(i,otherCompiersNames,outputsStr,programPath, differentRecord,"5_7"));
        	   }
        	   if((differentOutputsCount[0]==6&&differentOutputsCount[1]==6)||(differentOutputsCount[0]==6&&differentOutputsCount[1]==6)) {
        		   count6_6++;
        		   diffContent6_6.append(getDiffContent(i,otherCompiersNames,outputsStr,programPath, differentRecord,"6_6"));
        	   }
        	   diffContent2.append(getDiffContent(i,otherCompiersNames,outputsStr,programPath, differentRecord,"2"));
           }
           
           if(differentCount==3) {
        	   count3++;
        	   diffContent3.append(getDiffContent(i,otherCompiersNames,outputsStr,programPath, differentRecord,"3"));
           }
           
           if(differentCount==4) {
        	   count4++;
        	   diffContent4.append(getDiffContent(i,otherCompiersNames,outputsStr,programPath, differentRecord,"4"));
           }
           
           if(differentCount>=5) {
        	   count5++;
        	   diffContent5.append(getDiffContent(i,otherCompiersNames,outputsStr,programPath, differentRecord,"5"));
//        	   for(int j=0;j<otherCompiersNames.length;j++) {
//        		   //System.out.println("Program "+i+" output "+j+": "+outputsStr.get(j));
//        	   }
            }
           if(differentCount==12) {
        	   for(int j=0;j<otherCompiersNames.length;j++) {
        		   //System.out.println("Program "+i+" output "+j+": "+outputsStr.get(j));
        	   }
           }
           i++;
       }
       //System.out.println("different at least 2 :  "+count1);
       //System.out.println("different 1 count:  "+count11);
       //System.out.println("different 2 count:  "+count2);
       //System.out.println("different 3 count:  "+count3);
       //System.out.println("different 4 count:  "+count4);
       //System.out.println("different at least 5 count:  "+count5);
       //System.out.println("one compiler different from other eleven compilers:  "+count1_11);
       //System.out.println("two compiler different from other ten compilers:  "+count2_10);
       //System.out.println("three compiler different from other nine compilers:  "+count3_9);
       //System.out.println("4 compiler different from other 8 compilers:  "+count4_8);
       //System.out.println("5 compiler different from other 7 compilers:  "+count5_7);
       //System.out.println("6 compiler different from other 6 compilers:  "+count6_6);
       
       PrintData pd = new PrintData();
       pd.writeProgram(differentRecord + "all\\outputs.txt", diffContent.toString());
       pd.writeProgram(differentRecord + "1_11\\outputs.txt", diffContent1_11.toString());
       pd.writeProgram(differentRecord + "2_10\\outputs.txt", diffContent2_10.toString());
       pd.writeProgram(differentRecord + "3_9\\outputs.txt", diffContent3_9.toString());
       pd.writeProgram(differentRecord + "4_8\\outputs.txt", diffContent4_8.toString());
       pd.writeProgram(differentRecord + "5_7\\outputs.txt", diffContent5_7.toString());
       pd.writeProgram(differentRecord + "6_6\\outputs.txt", diffContent6_6.toString());
       pd.writeProgram(differentRecord + "2\\outputs.txt", diffContent2.toString());
       pd.writeProgram(differentRecord + "3\\outputs.txt", diffContent3.toString());
       pd.writeProgram(differentRecord + "4\\outputs.txt", diffContent4.toString());
       pd.writeProgram(differentRecord + "5\\outputs.txt", diffContent5.toString());
   }
   
   /**
    * 输出output有差异的程序，以及output到路径differentRecord
    * @param i 第i个程序
    * @param otherCompiersNames 编译器名称列表
    * @param outputsStr 不同编译器的输出
    * @return
    */
   private StringBuilder getDiffContent(int i,String[] otherCompiersNames, List<String> outputsStr,String programPath, String differentRecord,String prefix) {
	// TODO Auto-generated method stub
	   StringBuilder diffContent = new StringBuilder(new String("\nProgram"+i+":\n"));
	   for(int j=0;j<otherCompiersNames.length;j++) {
		   diffContent.append(getSpace(otherCompiersNames[j])+": "+outputsStr.get(j)+"\n");
	   }
       //赋值源文件到差异文件路径下programPath——>all\programs 
       try {
		copy("cprogram"+i+".c", programPath, differentRecord + prefix+"\\programs");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return diffContent;
}
private String getSpace(String str) {
	// TODO Auto-generated method stub
	if(str.contains("with")) {
		while(str.length()<22) {
			   str=str+" ";
		   }
	}else {
		while(str.length()<27) {
			   str=str+" ";
		   }
	}
	return str;
}
private static void copy(String filename, String oldpath, String newpath) throws IOException {
       File oldpaths = new File(oldpath + "/" + filename);
       File newpaths = new File(newpath + "/" + filename);
       if (!newpaths.exists()) {
           Files.copy(oldpaths.toPath(), newpaths.toPath());
       } else {
           newpaths.delete();
           Files.copy(oldpaths.toPath(), newpaths.toPath());
       }
   }



        /**
         *
         * @param file1 对比编译器输出 pypy python2 python3 python3_linux
         * @param file2 预期编译器输出 expected
         * @param outPath 差异文件输出路径 H:\程序生成及编译器测试\测试结果\python\2023.3.24\diff
         * @throws IOException
         */

        public void diffPy(String file1,String file2,String outPath) throws IOException {
            String pre="H:\\compilertest\\test_result\\python\\2023.3.31\\test_10000_0331\\Python_program";
            String pre2="";
            String post1=".txt";
            String post2=".txt";
            int i=0;
            while(i<5000){
                File f1=new File(pre+i+"\\"+file1+post1);

                File f2=new File(pre+i+"\\"+file2+post2);


                FileInputStream fis1 = new FileInputStream(pre+i+"\\"+file1+post1);
                // 防止路径乱码   如果utf-8 乱码  改GBK     eclipse里创建的txt  用UTF-8，在电脑上自己创建的txt  用GBK//Unicode
                InputStreamReader isr1 = new InputStreamReader(fis1, "GBK");

                BufferedReader fileReader1 = new BufferedReader(isr1);//文件1
                List<String> file1content=new ArrayList<>();
                String line="";
                while ((line = fileReader1.readLine()) != null) {
                    //不保留行尾注释
                    line = line.trim();
                    if (line == ""||line.equalsIgnoreCase("") ) {
                        continue;//忽略这一个空行
                    }else{
                        file1content.add(line);
                    }
                }


                FileInputStream fis2 = new FileInputStream(pre+i+"\\"+file2+post2);
                // 防止路径乱码   如果utf-8 乱码  改GBK     eclipse里创建的txt  用UTF-8，在电脑上自己创建的txt  用GBK//Unicode
                InputStreamReader isr2 = new InputStreamReader(fis2, "GBK");
                BufferedReader fileReader2 = new BufferedReader(isr2);//文件1

                List<String> file1content2=new ArrayList<>();
                String line2="";
                while ((line2 = fileReader2.readLine()) != null) {
                    //不保留行尾注释
                    line2 = line2.trim();
                    if (line2 == ""||line2.equalsIgnoreCase("") ){
                        continue;//忽略这一个空行
                    }else{
                        file1content2.add(line2);
                    }
                }

                StringBuilder diffContent= new StringBuilder(new String(""));

                int j=1;
                for(;j<file1content.size()&&j<file1content2.size();j++){
                    String str1=file1content.get(j).replaceAll(" ","");
                    String str2=file1content2.get(j).replaceAll(" ","");
                    if(!(file1content.get(j).trim().equalsIgnoreCase(file1content2.get(j).trim()))){
                        diffContent.append(j).append("\t").append(file1content.get(j).trim()).append("\t").append(file1content2.get(j).trim()).append("\r\n");
//                        //System.out.println(str1.length()+"\r\n"+str2.length());
                    }
                }

                if(j<file1content.size()){
                    for(;j<file1content.size();j++){
                        diffContent.append(j).append("\t").append(file1content.get(j)).append("\r\n");
                    }
                }

                if(j<file1content2.size()){
                    for(;j<file1content2.size();j++){
                        diffContent.append(j).append("\t").append(file1content2.get(j)).append("\r\n");
                    }
                }

                if(diffContent.length()>0){
                    String outputPath=outPath+file1+"_"+file2+"\\";
                    File dir=new File(outputPath);
                    PrintData pd=new PrintData();
                    File outpitfile = new File(outputPath+"Python_program"+i+".diff");
                    pd.writeProgram(outpitfile.getPath(),diffContent.toString());

                }

                i++;
            }

    }
    /**
     *
     * @param file1 对比编译器输出 pypy python2 python3 python3_linux
     * @param outPath 差异文件输出路径 H:\程序生成及编译器测试\测试结果\python\2023.3.24\diff
     * @throws IOException
     */

    public void samePy(String file1,String outPath) throws IOException {
//        String pre = "H:\\compilertest\\test_result\\python\\2023.3.31\\test_10000_0331\\Python_program";
        String pre = "H:\\compilertest\\test_result\\python\\2023.3.24\\test_1000_20230324\\Python_program";
        String post1 = ".txt";
        int i = 0;
        while (i < 5000) {
            File f1 = new File(pre + i + "\\" + file1 + post1);


            FileInputStream fis1 = new FileInputStream(pre + i + "\\" + file1 + post1);
            // 防止路径乱码   如果utf-8 乱码  改GBK     eclipse里创建的txt  用UTF-8，在电脑上自己创建的txt  用GBK//Unicode
            InputStreamReader isr1 = new InputStreamReader(fis1, "GBK");

            BufferedReader fileReader1 = new BufferedReader(isr1);//文件1
            List<String> file1content = new ArrayList<>();
            String line = "";
            while ((line = fileReader1.readLine()) != null) {
                //不保留行尾注释
                line = line.trim();
                if (line == "" || line.equalsIgnoreCase("")) {
                    continue;//忽略这一个空行
                } else {
                    file1content.add(line);
                }
            }


            StringBuilder fileContent = new StringBuilder(new String(""));


            for (int j = 0; j < file1content.size() ; j++) {
                fileContent.append(file1content.get(j));
            }

            if (fileContent.length() > 0) {
                String outputPath = outPath + file1  + "\\";
                File dir = new File(outputPath);
                PrintData pd = new PrintData();
                File outpitfile = new File(outputPath + "Python_program" + i + ".diff");
                pd.writeProgram(outpitfile.getPath(), fileContent.toString());

            }

            i++;
        }
    }

}
