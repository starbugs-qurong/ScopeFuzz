package iscas.ac.grand.util;

import com.jcraft.jsch.JSchException;
import iscas.ac.grand.util.JschUtil;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RemoteAutoTest {
	public static JschUtil jschUtil =JschUtil.getConfig();
//	public RemoteAutoTest(){
//		try {
//	        jschUtil.connect();
//	    } catch (JSchException e) {
//	        e.printStackTrace();
////	        LOGGER.info("jschUtil.connect failure");
//	    }
//	}
	 /**
     * qurong
     * 2024.4.11
     * 远程编译测试
     * @param programName 测试程序名称 如program0
     * @param programPath 测试程序本地路径 如C://programs//program0.cpp
	 * @throws Exception 
     */
public boolean remoteCompileTest(String programName,String programPath,String suffix) throws Exception {
		// TODO Auto-generated method stub
	try {
        jschUtil.connect();
    } catch (JSchException e) {
        e.printStackTrace();
//        LOGGER.info("jschUtil.connect failure");
        return false;
    }
	
	try {
    	String com1="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName;
        jschUtil.execCmd(com1);
    } catch (Exception e) {
        e.printStackTrace();
//        LOGGER.info("jschUtil.execCmd mkdir failure");
        return false;
    }
	
	try {
        String localPath1 = programPath;
        String remotePath1 = "/home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+programName+suffix;
        jschUtil.uploadFile(localPath1, remotePath1);
    }catch (Exception e){
        e.printStackTrace();
        //LOGGER.info("jschUtil.uploadFile failure");
        return false;
    }
	
	
    try {
    	String com1="cd /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName;
        jschUtil.execCmd(com1);
    } catch (Exception e) {
        e.printStackTrace();
//        LOGGER.info("jschUtil.execCmd mkdir failure");
        return false;
    }
    
    try {//创建编译除数文件夹
    	String com1="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++9" ;
		String com2="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++9/out"; 
		String com3="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++9/output"; 
		String com4="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++10"; 
		String com5="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++10/out"; 
		String com6="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++10/output"; 
		String com7="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++14"; 
		String com8="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++14/out"; 
		String com9="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++14/output"; 
		String com10="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++15"; 
		String com11="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++15/out"; 
		String com12="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++15/output"; 
		String com13="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++9-O2"; 
		String com14="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++9-O2/out"; 
		String com15="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++9-O2/output"; 
		String com16="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++10-O2"; 
		String com17="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++10-O2/out"; 
		String com18="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++10-O2/output"; 
		String com19="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++14-O2"; 
		String com20="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++14-O2/out"; 
		String com21="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++14-O2/output"; 
		String com22="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++15-O2"; 
		String com23="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++15-O2/out"; 
		String com24="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++15-O2/output";
        jschUtil.execCmd(com1);
        jschUtil.execCmd(com2);
        jschUtil.execCmd(com3);
        jschUtil.execCmd(com4);
        jschUtil.execCmd(com5);
        jschUtil.execCmd(com6);
        jschUtil.execCmd(com7);
        jschUtil.execCmd(com8);
        jschUtil.execCmd(com9);
        jschUtil.execCmd(com10);
        jschUtil.execCmd(com11);
        jschUtil.execCmd(com12);
        jschUtil.execCmd(com13);
        jschUtil.execCmd(com14);
        jschUtil.execCmd(com15);
        jschUtil.execCmd(com16);
        jschUtil.execCmd(com17);
        jschUtil.execCmd(com18);
        jschUtil.execCmd(com19);
        jschUtil.execCmd(com20);
        jschUtil.execCmd(com21);
        jschUtil.execCmd(com22);
        jschUtil.execCmd(com23);
        jschUtil.execCmd(com24);
    } catch (Exception e) {
        e.printStackTrace();
//        LOGGER.info("jschUtil.execCmd mkdir failure");
        return false;
    }
    
    
    try {//编译测试脚本执行
    	String com1=
    	"g++-9  /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+programName+suffix+" -o  /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+"g++9/out/"+programName +";\r\n" + 
    	"	./home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++9/out/"+programName+" >>/home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++9/output/"+programName+".txt\r\n" + 
    	"g++-10  /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+programName+suffix+" -o  /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+"g++10/out/"+programName +";\r\n" + 
    	"	./home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++10/out/"+programName+" >>/home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++10/output/"+programName+".txt\r\n";  
        jschUtil.execCmd(com1);
        
        String com2=
    	"clang++-14  /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+programName+suffix+" -o  /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+"clang++14/out/"+programName +";\r\n" + 
    	"	./home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++14/out/"+programName+" >>/home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++14/output/"+programName+".txt\r\n" + 
    	"clang++-15  /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+programName+suffix+" -o  /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+"clang++15/out/"+programName +";\r\n" + 
    	"	./home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++15/out/"+programName+" >>/home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++15/output/"+programName+".txt\r\n";  
        jschUtil.execCmd(com2);
        
        String com3=
    	"g++-9  /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+programName+suffix+" -o2  /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+"g++9-O2/out/"+programName +";\r\n" + 
    	"	./home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++9-O2/out/"+programName+" >>/home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++9/output/"+programName+".txt\r\n" + 
    	"g++-10  /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+programName+suffix+" -o2  /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+"g++10-O2/out/"+programName +";\r\n" + 
    	"	./home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++10-O2/out/"+programName+" >>/home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/g++10-O2/output/"+programName+".txt\r\n";  
        jschUtil.execCmd(com3);
        
        String com4=
    	"clang++-14  /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+programName+suffix+" -o2  /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+"clang++14-O2/out/"+programName +";\r\n" + 
    	"	./home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++14-O2/out/"+programName+" >>/home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++14-O2/output/"+programName+".txt\r\n" + 
    	"clang++-15  /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+programName+suffix+" -o2  /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/"+"clang++15-O2/out/"+programName +";\r\n" + 
    	"	./home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++15-O2/out/"+programName+" >>/home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+programName+"/clang++15-O2/output/"+programName+".txt\r\n";  
        jschUtil.execCmd(com4);
        
        
    } catch (Exception e) {
        e.printStackTrace();
//        LOGGER.info("jschUtil.execCmd mkdir failure");
        return false;
    }
    
    
	try {
        jschUtil.disconnect();
    } catch (JSchException e) {
        e.printStackTrace();
//        LOGGER.info("jschUtil.connect failure");
        return false;
    }
	
    return true;
	}

	public boolean disRemoteAutoTest() throws Exception{
		try {
	        jschUtil.disconnect();
	        return true;
	    } catch (JSchException e) {
	        e.printStackTrace();
//	        LOGGER.info("jschUtil.connect failure");
	        return false;
	    }
	}

	/**
	 * 批量的编译测试c++程序
	 * @param childDir 
	 * @param string
	 * @throws Exception 
	 */
	public boolean remoteCompileTestMulti(String path, String childDir,int index) throws Exception {
		// TODO Auto-generated method stub
		boolean result=true;
		try {
	        jschUtil.connect();
	    } catch (JSchException e) {
	        e.printStackTrace();
//	        LOGGER.info("jschUtil.connect failure");
	        return false;
	    }
		
		
		
		try {
	    	String com1="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+getDate();
	        jschUtil.execCmd(com1);
	    } catch (Exception e) {
	        e.printStackTrace();
//	        LOGGER.info("jschUtil.execCmd mkdir failure");
	        return false;
	    }
		
		try {
	    	String com2="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+getDate()+"/"+index;
	        jschUtil.execCmd(com2);
	    } catch (Exception e) {
	        e.printStackTrace();
//	        LOGGER.info("jschUtil.execCmd mkdir failure");
	        return false;
	    }
		
		try {
	    	String com3="mkdir /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+getDate()+"/"+childDir;
	        jschUtil.execCmd(com3);
	    } catch (Exception e) {
	        e.printStackTrace();
//	        LOGGER.info("jschUtil.execCmd mkdir failure");
	        return false;
	    }
		
		
		
		try {//递归上传文件夹中的文件
	        String localPath1 = path;
	        String remotePath1 = "/home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+getDate()+"/"+childDir;
	        jschUtil.uploadDir(localPath1, remotePath1);
	    }catch (Exception e){
	        e.printStackTrace();
	        //LOGGER.info("jschUtil.uploadFile failure");
	        return false;
	    }
		
		try {//复制编译测试脚本到工作目录下
	    	String com1="cp /home/qurong/codes-generated-by-grammar/cplus/automatic_test/*.sh /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+getDate()+"/"+index;
	        jschUtil.execCmd(com1);
	    } catch (Exception e) {
	        e.printStackTrace();
//	        LOGGER.info("jschUtil.execCmd mkdir failure");
	        return false;
	    }
		
		try {//复制测试结果绘制脚本到工作目录下
	    	String com1="cp /home/qurong/codes-generated-by-grammar/cplus/automatic_test/*.py /home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+getDate()+"/"+index;
	        jschUtil.execCmd(com1);
	    } catch (Exception e) {
	        e.printStackTrace();
//	        LOGGER.info("jschUtil.execCmd mkdir failure");
	        return false;
	    }
//		
		
		String operateDir="/home/qurong/codes-generated-by-grammar/cplus/automatic_test/"+getDate()+"/"+index;//
		
	//执行测试脚本
		/**
		 * ./mov_cpp.sh
			./mkdir_out.sh
			./g++9-10.sh
			./clang++14-15.sh
			./compare_outputs.sh
			./delete_empty_diff.sh
			./copy_diff_cppprogram.sh
			./copy_diff_program.sh
			./copy_out_program.sh
			 python3 generate_individual_plots.py
		 */
		executeSh(operateDir,"mov_cpp.sh",index);
		executeSh(operateDir,"mkdir_out.sh", index);
		executeSh(operateDir,"g++9-10.sh", index);
		executeSh(operateDir,"clang++14-15.sh", index);
		executeSh(operateDir,"compare_outputs.sh", index);
		executeSh(operateDir,"compare_outputs.sh", index);
		executeSh(operateDir,"delete_empty_diff.sh", index);
		executeSh(operateDir,"copy_diff_cppprogram.sh", index);
		executeSh(operateDir,"copy_diff_program.sh", index);
		executeSh(operateDir,"copy_out_program.sh", index);

		
		
		try {
	    	String com1="cd "+path+"; python3 "+operateDir+"/generate_individual_plots.py";
	        jschUtil.execCmd(com1);
	    } catch (Exception e) {
	        e.printStackTrace();
	        //System.out.println("conpiler test fail in"+index+" generate_individual_plots.py");
//	        LOGGER.info("jschUtil.execCmd mkdir failure");
	        return false;
	    }
		
		
		
		
		try {
	        jschUtil.disconnect();
	    } catch (JSchException e) {
	        e.printStackTrace();
//	        LOGGER.info("jschUtil.connect failure");
	        return false;
	    }
		return result;
	}
	
	

	/**
	 * 执行某个路径下的脚本
	 * @param path
	 * @param shName
	 */
    private void executeSh(String path,String shName,int index) {
    	try {
    		//String commandToExecute = "cd " + directory + "; " + command;
	    	String com1="cd "+path+"; ./"+shName;
	        jschUtil.execCmd(com1);
	    } catch (Exception e) {
	        e.printStackTrace();
	        //System.out.println("conpiler test fail in "+index+": "+shName);
//	        LOGGER.info("jschUtil.execCmd mkdir failure");
	    }
		
	}

	public static String getDate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(currentDate);
        return formattedDate;
    }

}
