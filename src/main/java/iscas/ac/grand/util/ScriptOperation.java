package iscas.ac.grand.util;

import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


public class ScriptOperation {
	public static boolean judgeWhetherCellphoneConnectingComputer() {
		try {
			Process p = Runtime.getRuntime().exec("adb devices");
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;  
            StringBuilder sb = new StringBuilder();
            int lineNumber=0;
            while ((line = br.readLine()) != null) {  
                lineNumber++;
            }  
			int returnVal=p.waitFor();
			p.waitFor();
			if( lineNumber <= 2 )
				return false;
			else
				return true;
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	
	public static String[] runShowExploreMessage(String atgName,String apkName,String scriptName,JScrollPane showRunningMessagePane,JTextArea taShowRunningMessage,int width, int height) {
		String basePath=GlobalVariable.basePath+""+File.separator+"";
		try {
			//System.out.println("runShowExploreMessage!!");
			//System.out.println(GlobalVariable.basePath+""+File.separator+"files"+File.separator+""+apkName+""+File.separator+"Infos"+File.separator+""+atgName);
			String firstPath = GlobalVariable.basePath+""+File.separator+"files"+File.separator+""+apkName+""+File.separator+"Infos"+File.separator+""+atgName;
			String secondPath = GlobalVariable.basePath+""+File.separator+"files"+File.separator+""+apkName+"\\Infos\\"+GlobalVariable.backupDotName;
			Process pullProcess = Runtime.getRuntime().exec("python "+scriptName+" "+apkName,null,new File(GlobalVariable.basePath));
			pullProcess.waitFor();
			if( !new File(firstPath).exists()){//���ԭʼͼ�ļ������ں���ֱ�ӷ���
				////System.out.println("firstPath not exist");
				return null;
			}
			if( !new File(secondPath).exists()) {//�������ͼ�ļ������ڣ�����ԭʼͼ�ļ���Ϊ����ͼ�ļ���������ͼ�ļ�����ͼƬ��ʾ��ǰ̨
//				Process fileCopyProcess = Runtime.getRuntime().exec("cp "+atgName+" "+GlobalVariable.backupDotName,null,
//					new File(GlobalVariable.basePath+"\\files\\"+apkName+"\\Infos"));
				Process fileCopyProcess = Runtime.getRuntime().exec("cmd /c  copy "+firstPath+" "+secondPath);
				fileCopyProcess.waitFor();
				showPicture(atgName, apkName, showRunningMessagePane, taShowRunningMessage, width, height, basePath);
				return FileOperation.readModelFile(GlobalVariable.basePath+"\\files\\"+apkName+"\\Infos\\model.txt");
			}
			if( FileOperation.isFileSame(firstPath,secondPath) )//ԭʼͼ�ļ��ͱ���ͼ�ļ���ͬ��˵��ͼ�ļ�δ���£�����Ҫ������
				return null;
			else {
//				Process fileCopyProcess = Runtime.getRuntime().exec("cp "+atgName+" "+GlobalVariable.backupDotName,null,
//					new File(GlobalVariable.basePath+"\\files\\"+apkName+"\\Infos"));
				Process fileCopyProcess = Runtime.getRuntime().exec("cmd /c  copy "+firstPath+" "+secondPath);
				fileCopyProcess.waitFor();
				showPicture(atgName, apkName, showRunningMessagePane, taShowRunningMessage, width, height, basePath);
				return FileOperation.readModelFile(GlobalVariable.basePath+"\\files\\"+apkName+"\\Infos\\model.txt");
			}
		}
		catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	

	private static ImageIcon showPicture(String atgName, String apkName, JScrollPane showRunningMessagePane,
			JTextArea taShowRunningMessage, int width, int height, String basePath)
			throws IOException, InterruptedException {
		String[] cmdArgs = null;
		String currentPng = "current.png";
		String newOutput="new-output.png";
		cmdArgs = new String[]{"dot.exe", "-Tpng",GlobalVariable.basePath+"\\files\\"+apkName+"\\Infos\\"+atgName,
			"-o", GlobalVariable.basePath+"\\files\\"+apkName+"\\Infos\\"+currentPng};
		//System.out.println(cmdArgs);
		Process p = Runtime.getRuntime().exec(cmdArgs);
		int value = p.waitFor();
		String imagePath = basePath+"files\\"+apkName+"\\Infos\\"+currentPng;
		String newImagePath = basePath+"files\\"+apkName+"\\Infos\\"+newOutput;
		if( ImageOperation.isImageTooLarge(width, height, imagePath)) {
			ImageOperation.resizeImage(imagePath, newImagePath , width, height,"png");
		}
		else {
			Process fileCopyProcess = Runtime.getRuntime().exec("cmd /c  copy "+imagePath+" "+newImagePath);
			fileCopyProcess.waitFor();
		}
        
		ImageIcon theImageIcon = new ImageIcon(newImagePath);
		theImageIcon.getImage().flush();//��仰���ͼƬ���棬�ܹؼ���һ��Ҫ�ӣ�����ǰ̨�޷���ʾ���µ�ͼƬ
		if( theImageIcon != null ) {
			ImageOperation.setBackgroundImage(theImageIcon, showRunningMessagePane, taShowRunningMessage,width,height);
		}
		return theImageIcon;
	}
	
	/**
	 * 
	 * @param apkName Ҫ������apk����
	 * @param taShowRunningMessage ��ʾ���й�������������Ϣ��JTextField�ؼ�
	 * @param pythonScriptName Ҫ���еĽű�����
	 * @param informationMessage ���гɹ���Ի������ʾ��Ϣ
	 * @param theField �ű����гɹ���Ҫ�޸ĵĶԻ���
	 * @param buttonArray �ű����гɹ�����õİ�ť
	 * @param stage TODO
	 */
	public static void runPython(String apkName,JTextArea taShowRunningMessage,JScrollPane showRunningMessagePane,
			String pythonScriptName,String informationMessage,JTextField theField,JButton[] buttonArray, int stage) {
//		if( isPythonScriptRunning() ) {
//			JOptionPane.showMessageDialog(null,"Please wait for the current process to be completed!", "Warning", JOptionPane.INFORMATION_MESSAGE);
//			return;
//		}
		BufferedReader br = null;
		try {
			
			
			//System.out.println("pythonScriptName: " + pythonScriptName);
			Process p = Runtime.getRuntime().exec("python "+pythonScriptName+" "+apkName,null,new File(GlobalVariable.basePath));
            if(stage!=-1){
            	taShowRunningMessage = new JTextArea();
    			taShowRunningMessage.setEditable(false);
	            showRunningMessagePane.setViewportView(taShowRunningMessage);
	            String line = null;  
	            br = new BufferedReader(new InputStreamReader(p.getInputStream()));  
	            while ((line = br.readLine()) != null){
	                taShowRunningMessage.append(line+"\n");
	                taShowRunningMessage.paintImmediately(taShowRunningMessage.getBounds());  
	            }
            }

			if( p.waitFor() == 0) {
				JOptionPane.showMessageDialog(null,informationMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
				if(theField!=null)
				theField.setText("Yes");
				for(int i=0;i < buttonArray.length; i++)
					buttonArray[i].setEnabled(true);
			}
		}catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void runDecompilePython(String apkName,JTextArea taShowRunningMessage,JScrollPane showRunningMessagePane,
			String pythonScriptName,String informationMessage,JTextField theField,JButton[] buttonArray, int stage,JTextField[] fieldArray) {
		ScriptOperation.runPython(apkName, taShowRunningMessage, showRunningMessagePane, pythonScriptName, informationMessage, theField, buttonArray, stage);
		String[] messageArray = FileOperation.readApkInformation(GlobalVariable.basePath+"\\files\\"+apkName+"\\Infos\\GetName.txt");
		for(int i=0;i < 3; i++) {
			fieldArray[i].setText(messageArray[i]);
		}
	}
	
	
	public static void runExplorePython(String apkName,JButton btnExplore,JTextArea taShowRunningMessage,String pythonScriptName,String informationMessage,JTextField theField,JButton[] buttonArray) {
		BufferedReader br = null;  
		try {
			taShowRunningMessage.setText("");
			//������Ҫ��ʱ����������ʹ������Ļ�����������ᵼ�³�������
			ProcessBuilder pb = new ProcessBuilder("python", pythonScriptName, apkName);
			pb.directory(new File(GlobalVariable.basePath));
			pb.redirectErrorStream(true);//�ϲ��������ʹ�����
			Process process = pb.start();
			br = new BufferedReader(new InputStreamReader(process.getInputStream()));  
		    String output = null;
		    while ((output=br.readLine()) != null) {//���������
		    	//System.out.println(output);
		    }
		    br.close();
		    
		    int returnVal = process.waitFor();
			if( returnVal == 0) {
				JOptionPane.showMessageDialog(null,informationMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
				if(theField!=null)
					theField.setText("Yes");
				for(int i=0;i < buttonArray.length; i++)
					buttonArray[i].setEnabled(true);
			}
			else {
				JOptionPane.showMessageDialog(null,informationMessage, "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			GlobalVariable.isExploring=false;
			JOptionPane.showMessageDialog(null,e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
			
		}
		GlobalVariable.isExploring=false;
	}
	
	/**
	 * ��ǰ�Ƿ����������е�python�ű�
	 * @return
	 */
	public static boolean isPythonScriptRunning() {
        String cmd = "tasklist /nh /FI \"IMAGENAME eq python.exe\"";  
        try {  
            Runtime runtime = Runtime.getRuntime();  
            Process process = runtime.exec(cmd);  
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));  
            String line = null;  
            while((line=br.readLine()) != null){  
                if(line.indexOf("python.exe") != -1){  
                    return true;
                }  
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }  
	}
	
	private static boolean killRunningProgram(String programName) {
		String pid=null;
		String cmd="tasklist /nh /FI \"IMAGENAME eq "+programName+"\"";
		try {  
            Runtime runtime = Runtime.getRuntime();  
            Process process = runtime.exec(cmd);  
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));  
            String line = null;  
            while((line=br.readLine()) != null){  
                if(line.indexOf(programName) != -1){  
                    String[] lineArray = line.split(" ");
                    for(int i=1;i < lineArray.length; i++)
                    	if( !lineArray[i].equals("")) {
            	    		pid = lineArray[i].trim();
            	    		break;
            	    	}
                    Process p2 = Runtime.getRuntime().exec("taskkill /F /PID "+pid);
                    p2.waitFor();
                    break;
                }  
            }
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }  
	}
	private static boolean sendCommand(String programName) {
		try {
			Process p2 = Runtime.getRuntime().exec(programName);
			p2.waitFor();
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		
		
	}
	
	public static boolean killRunningAdb() {
//		return ScriptOperation.killRunningProgram("adb.exe");
		
		return sendCommand("adb kill-server") && sendCommand("adb start-server");
	}
	
	/**
	 * �رյ�ǰ���е�python�ű�
	 * @return
	 */
	public static boolean killRunningPythonScript() {
		return ScriptOperation.killRunningProgram("python.exe");
	}

}
