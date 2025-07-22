package anonymous.ac.grand.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.swing.SwingConstants;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.border.EtchedBorder;
import javax.swing.JTextArea;

import java.awt.Toolkit;

import javax.swing.JScrollPane;
import javax.swing.DefaultComboBoxModel;

//import com.jgoodies.looks.FontPolicies;
//import com.jgoodies.looks.FontPolicy;
//import com.jgoodies.looks.FontSet;
//import com.jgoodies.looks.FontSets;
//import com.jgoodies.looks.HeaderStyle;
//import com.jgoodies.looks.Options;
//import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
//import com.jgoodies.looks.plastic.PlasticLookAndFeel;
//import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
//import com.jgoodies.looks.plastic.theme.*;
//import com.jgoodies.looks.windows.WindowsLookAndFeel;

import anonymous.ac.grand.main.GenerateProgramsByGrammar;
import anonymous.ac.grand.main.ReductProgram;
import anonymous.ac.grand.main.common.ConfigureInfo;
import anonymous.ac.grand.main.common.Constraint;
import anonymous.ac.grand.util.GlobalVariable;
import anonymous.ac.grand.util.ScriptOperation;

import java.awt.Dimension;


public class MainPage {
	private JFrame frmLandx;
	private JScrollPane showRunningMessagePane;
	
	private JTextField txtChoosenGrammar;
	private JTextArea taShowRunningMessage;
	private JTextField txtContinuity;
	private JTextField txtUniqueness;
	private JTextField txtTermination;
	private JTextField txtGenerateNumber;
	private JTextField txtGenerateResultPath;
	
	
	private JTextField txtLoopMaxmium;
	private JTextField txtAddNumber;
	private JTextField txtStarNumber;
	private JTextField txtCycleNumber;
	private JTextField txtPostfix;
	
	private JButton btnChooseParser;
	private JButton btnChooseLexer;
	private JButton btnDecompile;
	private JButton btnInstrument;
	private JButton btnInstall;
	private JButton btnExplore;
	private JButton btnExploreStop;
	private JButton btnGenerateReport;
	private JButton btnGenerateCrashReport;
	private JButton btnGenerateTestcase;
	private JButton btnRunTestcase;
	private JButton btnShowDecompileResult;
	private JButton btnViewExplorationReport;
	private JButton btnViewGeneratedTestcase;
	private JButton btnInstrumentationConfigure;
	private JButton btnExplorationConfigure;
	private JButton btnProfile;
	private JButton btnViewscreenshot;
	
	private JComboBox<?> cbbTestMode;
	private JComboBox<?> cbbIdentifierRelation;
	private JComboBox<?> cbbMutation;
	
	
	private Dimension myLabelDimension;
	
	BtnExploreThread exploreThread;
	ShowExploreMessageThread showPictureThread ;
	Thread t1,t2;
	
	class BtnDecompileThread implements Runnable{
		@Override
		public void run() {
			ScriptOperation.runDecompilePython(getApkName(),taShowRunningMessage,showRunningMessagePane,"operate/1_decompile.py","Decompile Finished!",null,
				new JButton[] { btnInstrument,btnShowDecompileResult,btnInstrumentationConfigure},1,new JTextField[] {txtContinuity,txtUniqueness,txtTermination});
		}
	}
	
	class BtnInstrumentThread implements Runnable{
		@Override
		public void run() {
			ScriptOperation.runPython(getApkName(), taShowRunningMessage,showRunningMessagePane, "operate/2_instrument.py", "Instrument Finished!", null, new JButton[] {btnInstall}, 2);
		}
	}
	class BtnProfileThread implements Runnable{
		@Override
		public void run() {
			ScriptOperation.runPython(getApkName(), taShowRunningMessage,showRunningMessagePane, "operate/4_profile.py", "Generate Profile Finished!", null, new JButton[] {}, 0);
		}
	}
	
	class BtnInstallThread implements Runnable{
		@Override
		public void run() {
			ScriptOperation.runPython(getApkName(),taShowRunningMessage,showRunningMessagePane,"operate/3_install.py","Installation Finished!",null,new JButton[] {btnProfile,btnExplore,btnExplorationConfigure}, 3);
		}
	}
	
	class BtnGenerateReportThread implements Runnable{
		@Override
		public void run() {
			ScriptOperation.runPython(getApkName(), taShowRunningMessage, showRunningMessagePane, "operate/7_cal_coverage.py","Report Generation Finished", null, new JButton[] {btnViewExplorationReport}, -1);
		}
	}
	class BtnGenerateCrashReportThread implements Runnable{
		@Override
		public void run() {
			ScriptOperation.runPython(getApkName(), taShowRunningMessage, showRunningMessagePane, "operate/7_bug_report.py","Report Generation Finished", null, new JButton[] {btnViewExplorationReport}, -1);
		}
	}
	
	class BtnRunTestcaseThread implements Runnable{
		@Override
		public void run() {
			ScriptOperation.runPython(getApkName(), taShowRunningMessage, showRunningMessagePane, "operate/9_run_test_case.py","Run Generation Finished", null, new JButton[] {}, 0);
		}
	}
	
	class BtnExploreThread implements Runnable{
		@Override
		public void run() {
			btnViewscreenshot.setEnabled(true);
			btnGenerateReport.setEnabled(true);
			btnGenerateCrashReport.setEnabled(true);
			btnGenerateTestcase.setEnabled(true);
			ScriptOperation.runExplorePython(getApkName(),btnExplore,taShowRunningMessage,"operate/5_exploration.py","Exploration Finished!",null,new JButton[] {});
		}
	}
	
	class ShowExploreMessageThread implements Runnable{
		@Override
		public void run() {
			//System.out.println(GlobalVariable.isExploring);
			while(GlobalVariable.isExploring) {
				int height = (int)(showRunningMessagePane.getBounds().getHeight());
				int width = (int)(showRunningMessagePane.getBounds().getWidth());
				String[] messageArray = ScriptOperation.runShowExploreMessage("currentATG.dot",getApkName(),"operate\\10_getInfos.py",showRunningMessagePane, 
						taShowRunningMessage,width, height);
				if( messageArray != null ) {
					txtLoopMaxmium.setText(messageArray[0]);
					txtAddNumber.setText(messageArray[1]);
					txtStarNumber.setText(messageArray[2]);
					txtCycleNumber.setText(messageArray[3]);
				}
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//System.out.println(GlobalVariable.isExploring);
		}
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainPage window = new MainPage();
					window.frmLandx.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainPage() {
		try {
			initialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initializeTaShowRunningMessage() {
		taShowRunningMessage = new JTextArea();
		taShowRunningMessage.setEditable(false);
		taShowRunningMessage.setBackground(Color.WHITE);
		taShowRunningMessage.setOpaque(true);
		showRunningMessagePane.setViewportView(taShowRunningMessage);
	}
	
	private String getApkName() {
//		//System.out.println(txtChoosenGrammar.getText().split("\\.")[0]);
		if(txtChoosenGrammar.getText()!=null)
			return txtChoosenGrammar.getText().split("\\.")[0];
		return "";
	}
	
	private void setFileChooseCenter(JFileChooser theChooser) {
		 Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		 theChooser.setLocation((int)(screen.getWidth()/2-theChooser.getWidth()/2), (int)(screen.getHeight()/2-theChooser.getHeight()/2));
	}
	
	private void changeChoosenApk() { 
//		txtDecompileStatus.setText("No");
//		txtInstallStatus.setText("No");
		//txtExplorationStatus.setText("No");
		txtContinuity.setText("");
		txtUniqueness.setText("");
		txtTermination.setText("");
		txtLoopMaxmium.setText("");
		txtAddNumber.setText("");
		txtStarNumber.setText("");
		txtCycleNumber.setText("");
		
		btnInstrument.setEnabled(false);
		btnInstall.setEnabled(false);
		btnExplore.setEnabled(false);
		btnGenerateReport.setEnabled(false);
		btnGenerateTestcase.setEnabled(false);
		btnShowDecompileResult.setEnabled(false);
//		btnViewExplorationReport.setEnabled(false);
		btnViewGeneratedTestcase.setEnabled(false);
		btnInstrumentationConfigure.setEnabled(true);
		btnExplorationConfigure.setEnabled(false);
		
		taShowRunningMessage = new JTextArea();
		showRunningMessagePane.setViewportView(taShowRunningMessage);
	}
	
	private void openExploreConfiguration() { 
		JDialog configureDialog = new ExploreConfiguration(getApkName());
//		JDialog configureDialog = new ExploreConfiguration("aa");
		configureDialog.setLocationRelativeTo(this.frmLandx);
		configureDialog.setVisible(true);
	}
	
	private void openTestGenerateConfiguration() { 
		JDialog configureDialog = new TestGeneConfiguration(getApkName());
		configureDialog.setLocationRelativeTo(this.frmLandx);
		configureDialog.setVisible(true);
	}
	
	private void openInstrumentConfiguration() { 
		JDialog configureDialog = new InstrumentConfiguration();
		configureDialog.setLocationRelativeTo(this.frmLandx);
		configureDialog.setVisible(true);
	}
	private void openFileChoose() {
		UIManager.put("FileChooser.cancelButtonText", "Cancel"); //�޸�ȡ��
		UIManager.put("FileChooser.saveButtonText", "Save"); //�޸ı���
		UIManager.put("FileChooser.openButtonText", "Choose");//�޸Ĵ�
		JFileChooser chooser = new JFileChooser(new File(GlobalVariable.basePath+"\\Apk"));
//		FileNameExtensionFilter filter = new FileNameExtensionFilter(".apk","apk");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//
//		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Choose Grammar Path");
		chooser.setLocale(Locale.ENGLISH);
		int returnVal = chooser.showOpenDialog(this.frmLandx); 
		if (returnVal == JFileChooser.APPROVE_OPTION) {  			
			String filePath = chooser.getSelectedFile().getAbsolutePath();
//			btnDecompile.setEnabled(true);	
			txtChoosenGrammar.setText(filePath);
//			if( filePath.endsWith(".apk")) {
//				String[] theArray = filePath.split("\\\\");
//				if( !txtChoosenGrammar.getText().equals("None") && !theArray[theArray.length-1].equals(txtChoosenGrammar.getText()) ) {
//					changeChoosenApk();
//				}
//				else{
//					btnDecompile.setEnabled(true);							
//				}
//				txtChoosenGrammar.setText(theArray[theArray.length-1]);
//			}
//			else{
//				JOptionPane.showMessageDialog(null,"Please choose a valid Grammar Path within .g4 file!", "Error", JOptionPane.ERROR_MESSAGE);
//			}
		}  
	}
	
	/**
	 * Ԥ������
	 */
	private void initializeGrammarParsing() {
		
		JPanel panelPreprocess = new JPanel();
		panelPreprocess.setBounds(10, 13, 700, 170);
		TitledBorder border =  BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Grammar Parsing",TitledBorder.CENTER,TitledBorder.CENTER); 
		panelPreprocess.setBorder(border);
		panelPreprocess.setLayout(new FlowLayout(FlowLayout.LEFT));
		frmLandx.getContentPane().add(panelPreprocess);
		
		//parser file
		JPanel parser_message_panel = new JPanel();
		panelPreprocess.add(parser_message_panel);
		parser_message_panel.setBorder(null);
		btnChooseParser = new JButton(" Choose  ");
		btnChooseParser.addActionListener(new ActionListener() {        
			@Override
			public void actionPerformed(ActionEvent e) {    
				if( ScriptOperation.isPythonScriptRunning() ) {
					JOptionPane.showMessageDialog(null,"Please wait for the current process to be completed!", "Warning", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				openFileChoose();
			}  
		});
		//apk_message_panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Choosen Grammar Path: ");
//		lblNewLabel.setPreferredSize(myLabelDimension)
		parser_message_panel.add(lblNewLabel);
		parser_message_panel.add(btnChooseParser);
		txtChoosenGrammar = new JTextField();
		parser_message_panel.add(txtChoosenGrammar);
		txtChoosenGrammar.setToolTipText("");
		txtChoosenGrammar.setHorizontalAlignment(SwingConstants.LEFT);
		txtChoosenGrammar.setEditable(true);
		/*------------------------------------------------------------------------------------grammar path-----------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------
*/
//		txtChoosenGrammar.setText("D:\\demo\\C");
//		txtChoosenGrammar.setText("H:\\demo\\cplus-grammar");//c++ c和java扩展而来的文法 2024.4
		txtChoosenGrammar.setText("H:\\demo\\lambda-template");//lambda
		
//		txtChoosenGrammar.setText("H:\\demo\\cpp-grammar");//c++ 2024.12 grammar-v4中的cpp文法
		
//		txtChoosenGrammar.setText("G:\\cangjie\\grammar");
		txtChoosenGrammar.setColumns(20);
		
		
		//Language:
		JPanel panelTestMode = new JPanel();
//		panelTestMode.setBorder(new MatteBorder(1, 1, 1, 1, (Color) SystemColor.scrollbar));
		panelTestMode.setBorder(null);
		panelPreprocess.add(panelTestMode);
//		panelTestMode.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelTestMode.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel lblTestMode = new JLabel("Language:");
		lblTestMode.setPreferredSize(myLabelDimension);
		panelTestMode.add(lblTestMode);
		
		cbbTestMode = new JComboBox();
		cbbTestMode.setModel(new DefaultComboBoxModel(new String[] {"C++","C", "Java", "Python","Other"}));
		panelTestMode.add(cbbTestMode);
		
		//postfix
		JPanel panelPostfix = new JPanel();
		panelPreprocess.add(panelPostfix);
		panelPostfix.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel lblPostfix = new JLabel("file extension: ");
		lblPostfix.setPreferredSize(myLabelDimension);
		panelPostfix.add(lblPostfix);
		
		txtPostfix = new JTextField(".cpp");
		txtPostfix.setEditable(true);
		panelPostfix.add(txtPostfix);
		txtPostfix.setColumns(10);
		
		
		//parse grammar
		JPanel start_Parse_panel = new JPanel();
		panelPreprocess.add(start_Parse_panel);
		start_Parse_panel.setBorder(null);
		JLabel startParseLabel = new JLabel("Parse Grammar:                           ");
		start_Parse_panel.add(startParseLabel);
		btnExplore = new JButton("  Parse  ");
		btnExplore.setEnabled(true);
		panelPreprocess.add(btnExplore);

		btnExplore.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//F:\compiler random test\python
				String grammarPath=txtChoosenGrammar.getText();
				ConfigureInfo.directory=grammarPath;
				//language
				String language=cbbTestMode.getSelectedItem().toString();
				setLanguage(language);
				//postfix
				String postfix=txtPostfix.getText();
				ConfigureInfo.postfix=postfix;
//				exploreThread = new BtnExploreThread();
//				showPictureThread = new ShowExploreMessageThread();
//				t1 =  new Thread(exploreThread);
//				t2 =  new Thread(showPictureThread);
//				if( ScriptOperation.isPythonScriptRunning() ) {
//					JOptionPane.showMessageDialog(null,"Please wait for the current process to be completed!", "Warning", JOptionPane.INFORMATION_MESSAGE);
//					GlobalVariable.isExploring=false;
//					return;
//				}
//				GlobalVariable.isExploring=true;
//				initializeTaShowRunningMessage();
//				FileOperation.folderDelete(GlobalVariable.basePath+"\\files\\"+getApkName()+"\\Logs");
//				FileOperation.folderDelete(GlobalVariable.basePath+"\\files\\"+getApkName()+"\\Screenshots");
//				FileOperation.folderDelete(GlobalVariable.basePath+"\\files\\"+getApkName()+"\\Infos");
//				
//				t1.start();
//				t2.start();
			}
		});
		
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

	/**
	 * APK��Ϣ
	 */
	private void initializeTestProgramGeneration() {
		JPanel panelGnerateTestProgram = new JPanel();
		TitledBorder border =  BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Test Program Generation",TitledBorder.CENTER,TitledBorder.CENTER); 
		panelGnerateTestProgram.setBorder(border);
		panelGnerateTestProgram.setBounds(550, 250, 500, 400);
		panelGnerateTestProgram.setLayout(new FlowLayout(FlowLayout.LEFT));
		frmLandx.getContentPane().add(panelGnerateTestProgram);
		//panelGnerateTestProgram.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 12));
		
		
		//标识符策略
		JPanel panelIdentifierRelation = new JPanel();
		panelIdentifierRelation.setBorder(null);
		panelGnerateTestProgram.add(panelIdentifierRelation);
		panelIdentifierRelation.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel lblIdentifierRelation= new JLabel("Identifier Strategy:");
		lblIdentifierRelation.setPreferredSize(myLabelDimension);
		panelIdentifierRelation.add(lblIdentifierRelation);
		
		cbbIdentifierRelation = new JComboBox();
		cbbIdentifierRelation.setModel(new DefaultComboBoxModel(new String[] {"new", "nearest", "farthest","random","PASI","PISS"}));
		panelIdentifierRelation.add(cbbIdentifierRelation);

		//变异策略
		JPanel panelMutation = new JPanel();
		panelMutation.setBorder(null);
		panelGnerateTestProgram.add(panelMutation);
		panelMutation.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel lblMutation= new JLabel("Mutation Strategy:");
		lblMutation.setPreferredSize(myLabelDimension);
		panelMutation.add(lblMutation);

		cbbMutation = new JComboBox();
		cbbMutation.setModel(new DefaultComboBoxModel(new String[] {"no-mutation", "comments-only","main-mutation","scope-mutation","AST-Fun-mutation"}));

		// 添加下拉框的选中状态改变监听器
		cbbMutation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				boolean mutationAvailable=false;//用于变异程序 是否需要变异 true或false 默认为false
//				String selectedMutationStrategy="no mutation";
				String selectedItem = (String) cbbMutation.getSelectedItem();
				if (selectedItem.equals("no mutation")) {
					ConfigureInfo.mutationAvailable = false;
				} else {
					ConfigureInfo.mutationAvailable = true;
				}
				// 设置变异策略
				ConfigureInfo.selectedMutationStrategy = selectedItem.trim();
			}
		});

		panelMutation.add(cbbMutation);
		
		
		//Continuity
		JPanel panelPackageName = new JPanel();
		panelGnerateTestProgram.add(panelPackageName);
		panelPackageName.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel lblPackageName = new JLabel("Continuity: ");
		lblPackageName.setPreferredSize(myLabelDimension);

		panelPackageName.add(lblPackageName);
		
		txtContinuity = new JTextField();
		txtContinuity.setEditable(true);
		txtContinuity.setText("identifier,Digits,Letter,IDENTIFIER,classOrInterfaceType,qualifiedName,STRING_LITERAL,string,double,OR,boolExpression,boolParExpression,updateExpression");
		panelPackageName.add(txtContinuity);
		txtContinuity.setColumns(15);
		
		//Uniqueness
		JPanel panelMainActivity = new JPanel();
		panelGnerateTestProgram.add(panelMainActivity);
		panelMainActivity.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel lblMainActivity = new JLabel("Uniqueness: ");
		lblMainActivity.setPreferredSize(myLabelDimension);
		panelMainActivity.add(lblMainActivity);
		
		txtUniqueness = new JTextField();
		txtUniqueness.setEditable(true);
		txtUniqueness.setText("classOrInterfaceModifier,requiresModifier,modifier,interfaceMethodModifier,classBodyDeclaration,variableModifier,finallyBlock");
		txtUniqueness.setColumns(15);
		panelMainActivity.add(txtUniqueness);
		
		//Termination
		JPanel panelAcitivityNumber = new JPanel();
		panelGnerateTestProgram.add(panelAcitivityNumber);
		panelAcitivityNumber.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel lblActivityNumber = new JLabel("Generation Termination: ");
//		lblActivityNumber.setPreferredSize(myLabelDimension);
		panelAcitivityNumber.add(lblActivityNumber);
		
		txtTermination = new JTextField();
		
		txtTermination.setEditable(true);
//		txtTermination.setText("expression 5,parExpression 5,statement 10,stringExpression 2,floatExpression 2,integerExpression 2");//生成长程序
		txtTermination.setText("expression 2,parExpression 2,statement 2,stringExpression 2,floatExpression 2,integerExpression 2");//生成短程序
		txtTermination.setColumns(15);
		panelAcitivityNumber.add(txtTermination);
		
		
		//Generate Number
		JPanel panelGenerateNumber = new JPanel();
		panelGnerateTestProgram.add(panelGenerateNumber);
		panelGenerateNumber.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel GenerateNumber = new JLabel("Number of Test Programs: ");
//		GenerateNumber.setPreferredSize(myLabelDiension);

		panelGenerateNumber.add(GenerateNumber);
		
		txtGenerateNumber = new JTextField();
		txtGenerateNumber.setEditable(true);
		txtGenerateNumber.setText("10");
		panelGenerateNumber.add(txtGenerateNumber);
		txtGenerateNumber.setColumns(15);
		
		//Generate Result Save Path
		JPanel panelSavePath = new JPanel();
		panelGnerateTestProgram.add(panelSavePath);
		panelSavePath.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel lblMainSavePath = new JLabel("Generate Result Save Path: ");
//		lblMainSavePath.setPreferredSize(myLabelDimension);
		panelSavePath.add(lblMainSavePath);
		
		txtGenerateResultPath = new JTextField();
		txtGenerateResultPath.setEditable(true);
		txtGenerateResultPath.setColumns(15);
		/*------------------------------------------------------------------------------------out path-----------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------
*/

//		txtGenerateResultPath.setText("H:\\demo\\cplus\\0417\\nearest");//cpp
//		txtGenerateResultPath.setText("G:\\cangjie\\0422\\nearest");//cangjie
		txtGenerateResultPath.setText("H:\\demo\\lambda-programs\\2025\\0109\\1");//lambda
//		txtGenerateResultPath.setText("H:\\demo\\cpp-programs\\1211");//cpp in grammar-v4
//		txtGenerateResultPath.setText("D:\\demo\\result");
		panelSavePath.add(txtGenerateResultPath);
		
		
		//Generate Test Program
		JPanel start_Generate_panel = new JPanel();
		panelGnerateTestProgram.add(start_Generate_panel);
		start_Generate_panel.setBorder(null);
		btnExplore = new JButton("  Generate Test Programs  ");
		btnExplore.setEnabled(true);
		start_Generate_panel.add(btnExplore);

		btnExplore.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//获取各种配置信息
				//identifier,Digits
				String continuity=txtContinuity.getText();
				String[] continuityArr=continuity.split(",");
				List<String> continuityConstraint=Arrays.asList(continuityArr);
				if(ConfigureInfo.constraint==null) {
					ConfigureInfo.constraint=new Constraint();
				}
				ConfigureInfo.constraint.setContinuityList(continuityConstraint);
				//finallyBlock,modifier
				String uniqueness=txtUniqueness.getText();
				String[] uniquenessArr=uniqueness.split(",");
				List<String> uniquenessConstraint=Arrays.asList(uniquenessArr);
				ConfigureInfo.constraint.setUniquenessList(uniquenessConstraint);
				//expression 5,parExpression 5
				String termination=txtTermination.getText();
				String[] terminationArr=termination.split(",");
				List<String> terminationConstraint=Arrays.asList(terminationArr);
				Map<String,Integer> terminationMap=listToMap(terminationConstraint);
				ConfigureInfo.constraint.setTerminationMap(terminationMap);
				
				//Identifier relation strategy
				String identifierRelation=cbbIdentifierRelation.getSelectedItem().toString();
				ConfigureInfo.identifierRelation=identifierRelation;
				//10
				String generateNum=txtGenerateNumber.getText();
				ConfigureInfo.generateNum=Integer.parseInt(generateNum);
				//F:\compiler random test\python\test_grand_gui
				String generateResultPath=txtGenerateResultPath.getText();
				ConfigureInfo.outPutdirectory=generateResultPath;

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
		});
		
		
		
		//Program Reduction
		JPanel program_Reduction = new JPanel();
		panelGnerateTestProgram.add(program_Reduction);
		program_Reduction.setBorder(null);
		btnExplore = new JButton("  Program Reduction  ");
		btnExplore.setEnabled(true);
		program_Reduction.add(btnExplore);

		btnExplore.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ReductProgram reductProgram=new ReductProgram();
				reductProgram.reductProgram();
			}
		});
		
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

	/**
	 * ģ����Ϣ
	 */
	private void initializeTestProgramOptimization() {
		JPanel panelOptimizeTestProgram = new JPanel();
		panelOptimizeTestProgram.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Test Program Optimization", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panelOptimizeTestProgram.setBounds(00, 250, 500, 400);
		panelOptimizeTestProgram.setLayout(new FlowLayout(FlowLayout.LEFT));

		frmLandx.getContentPane().add(panelOptimizeTestProgram);
		
		JPanel panelMainActivity = new JPanel();
		panelOptimizeTestProgram.add(panelMainActivity);
		panelMainActivity.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel lblNodeNumber = new JLabel("Execution of Loop: ");
//		lblNodeNumber.setPreferredSize(myLabelDimension);
		panelMainActivity.add(lblNodeNumber);
		
		txtLoopMaxmium = new JTextField();
		txtLoopMaxmium.setEditable(true);
		panelMainActivity.add(txtLoopMaxmium);
		txtLoopMaxmium.setColumns(10);
		txtLoopMaxmium.setText("1");//2
//		txtLoopMaxmium.setText("5");
		
		//+ Number:
		JPanel panelMainActivity2 = new JPanel();
		panelOptimizeTestProgram.add(panelMainActivity2);
		panelMainActivity2.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel lblAddNumber = new JLabel("Number of +:");
		lblAddNumber.setPreferredSize(myLabelDimension);
		panelMainActivity2.add(lblAddNumber);
		
		txtAddNumber = new JTextField();
		txtAddNumber.setEditable(true);
		panelMainActivity2.add(txtAddNumber);
		txtAddNumber.setColumns(10);
		txtAddNumber.setText("1");//3
//		txtAddNumber.setText("10");
		
		//* Number:
		JPanel panelMainActivity3 = new JPanel();
		panelOptimizeTestProgram.add(panelMainActivity3);
		panelMainActivity3.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		
		JLabel lblStarNumber = new JLabel("Number of *:");
		lblStarNumber.setPreferredSize(myLabelDimension);
		panelMainActivity3.add(lblStarNumber);
		
		txtStarNumber = new JTextField();
		txtStarNumber.setEditable(true);
		panelMainActivity3.add(txtStarNumber);
		txtStarNumber.setColumns(10);
		txtStarNumber.setText("1");//3
//		txtStarNumber.setText("10");
		
		//Grammar Cycle Maxmium:
		JPanel panelMainActivity4 = new JPanel();
		panelOptimizeTestProgram.add(panelMainActivity4);
		panelMainActivity4.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		
		JLabel lblCycleNumber = new JLabel("Termination for All Rule Loops:");
//		lblCycleNumber.setPreferredSize(myLabelDimension);
		panelMainActivity4.add(lblCycleNumber);
		
		txtCycleNumber = new JTextField();
		txtCycleNumber.setEditable(true);
		panelMainActivity4.add(txtCycleNumber);
		txtCycleNumber.setColumns(10);
		txtCycleNumber.setText("2");//5
//		txtCycleNumber.setText("10");
		
		
		//Generate Test Program
		JPanel apply_Optimization_panel = new JPanel();
		panelOptimizeTestProgram.add(apply_Optimization_panel);
		apply_Optimization_panel.setBorder(null);
		btnExplore = new JButton("  Apply  ");
		btnExplore.setEnabled(true);
		apply_Optimization_panel.add(btnExplore);

		btnExplore.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//2
				String loopMaxmium=txtLoopMaxmium.getText();
				ConfigureInfo.loopCountMax=Integer.parseInt(loopMaxmium);
				//3
				String addNumber=txtAddNumber.getText();
				ConfigureInfo.addnum=Integer.parseInt(addNumber);
				//3
				String starNumber=txtStarNumber.getText();
				ConfigureInfo.starnum=Integer.parseInt(starNumber);
				//5
				String cycleNumber=txtCycleNumber.getText();
				ConfigureInfo.cycleStepLength=Integer.parseInt(cycleNumber);
				
//				exploreThread = new BtnExploreThread();
//				showPictureThread = new ShowExploreMessageThread();
//				t1 =  new Thread(exploreThread);
//				t2 =  new Thread(showPictureThread);
//				if( ScriptOperation.isPythonScriptRunning() ) {
//					JOptionPane.showMessageDialog(null,"Please wait for the current process to be completed!", "Warning", JOptionPane.INFORMATION_MESSAGE);
//					GlobalVariable.isExploring=false;
//					return;
//				}
//				GlobalVariable.isExploring=true;
//				initializeTaShowRunningMessage();
//				FileOperation.folderDelete(GlobalVariable.basePath+"\\files\\"+getApkName()+"\\Logs");
//				FileOperation.folderDelete(GlobalVariable.basePath+"\\files\\"+getApkName()+"\\Screenshots");
//				FileOperation.folderDelete(GlobalVariable.basePath+"\\files\\"+getApkName()+"\\Infos");
//				
//				t1.start();
//				t2.start();
			}
		});
		
	}

	private void initialize() throws IOException {
		GlobalVariable.basePath = System.getProperty("user.dir");
//		GlobalVariable.basePath = "F:\\LAND";
//		//System.out.println(GlobalVariable.basePath);
		
		
//		FontSet fontSet = FontSets.createDefaultFontSet(
//			    new Font("Dialog", Font.PLAIN, 18),    // control font
//			    new Font("Dialog", Font.BOLD, 18),    // menu font
//			    new Font("Dialog", Font.BOLD, 18)     // title font
//			    );
//		FontPolicy fixedPolicy = FontPolicies.createFixedPolicy(fontSet);
//		PlasticLookAndFeel.setFontPolicy(fixedPolicy);
//
//		//���ý�����۷��
//		try {
//			Plastic3DLookAndFeel.setCurrentTheme(new ExperienceRoyale());
//			UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
//		} catch (Exception exc) {
//			exc.printStackTrace();
//		}
		
		myLabelDimension = 	new Dimension(150,20);

		frmLandx = new JFrame();
		Properties props = System.getProperties(); 
		props.put("user.language", "en"); 
		System.setProperties(props); 
		frmLandx.setTitle("GeProG");
		frmLandx.setBounds(100, 100, 1100, 700);
		frmLandx.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmLandx.getContentPane().setLayout(null);
		frmLandx.setLocationRelativeTo(null);
		frmLandx.setResizable(true);
		
		frmLandx.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				if(t1!=null || t2!=null){
					if(t1!=null && t1.isAlive())
						t1.stop();
					if(t1!=null && t2.isAlive())
						t2.stop();
					ScriptOperation.killRunningPythonScript();
					ScriptOperation.killRunningAdb();
				}
				System.exit(0);
			}
		});
		this.initializeGrammarParsing();
//		this.initilizeConfiguration();
		this.initializeTestProgramGeneration();
		this.initializeTestProgramOptimization();
		
//		showRunningMessagePane = new JScrollPane();
//		showRunningMessagePane.setBounds(466, 200, 754, 420);
//		//showRunningMessagePane.setBackground(Color.WHITE);
//		showRunningMessagePane.setOpaque(false);
//		frmLandx.getContentPane().add(showRunningMessagePane);
//		
//		this.initializeTaShowRunningMessage();
		
		
		
		
//		//System.out.println("the width of taShowRunningMessage is: "+taShowRunningMessage.getScrollableTracksViewportWidth());
		
//		ImageOperation.setBackgroundImage(new ImageIcon("F:\\LAND\\current.png"), showRunningMessagePane, taShowRunningMessage,625,502);		
//		taShowRunningMessage = new JTextArea();
//		taShowRunningMessage.setBackground(Color.WHITE);
//		taShowRunningMessage.setForeground(Color.BLACK);
//		taShowRunningMessage.setEditable(false);
//		taShowRunningMessage.setOpaque(true);
//		showRunningMessagePane.setViewportView(taShowRunningMessage);
		
		
	}
	
}
