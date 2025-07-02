package iscas.ac.grand.main.common;

import iscas.ac.grand.main.common.Grammar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrintData {

    List<Grammar> grammars=new ArrayList<>();

    public PrintData(List<Grammar> grammars) {
        this.grammars = grammars;
    }

    public PrintData() {
    }

    /**
     * 输出map中的数据
     */
    public boolean printMap(Grammar grammar){
        Map<String, List<String>> vars=grammar.getVars();
        List<String> orderedList=grammar.getOrderedList();
        boolean result=false;
        if(vars!=null){
            for(int i=0;i<orderedList.size();i++){
                List<String> values=vars.get(orderedList.get(i));
                if(values!=null){
                    //System.out.println(orderedList.get(i)+": in printMap");
                    for(int j=0;j<values.size();j++){
                        //System.out.println("printMap:     " + values.get(j));
                    }
                }
            }
        }
        return result;
    }

    /**
     * 写入txt
     *
     * @param path 需要写入txt的路径
     * @param grammar 需要写入的grammar
     * @since 2021/1/8 19:37
     */
    public void writeMap(String path, Grammar grammar) {
        BufferedWriter bw = null;
        FileWriter fr = null;
        Map<String,List<String>> map=grammar.getVars();
        List<String> orderedList=grammar.getOrderedList();
        try {
            File file=new File(path);
            if(!file.exists())
            {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //将写入转化为流的形式
            fr = new FileWriter(path);
            bw = new BufferedWriter(fr);
            //一次写一行
            if(map!=null){
                for(int i=0;i<orderedList.size();i++){
                    List<String> values=map.get(orderedList.get(i));
                    if(values!=null){
                        bw.write(orderedList.get(i)+":");
                        bw.newLine();  //换行用
                        //System.out.println(orderedList.get(i)+": in writeMap");
                        for(int j=0;j<values.size();j++){
                            bw.write("    " + values.get(j));
                            bw.newLine();  //换行用
                            //System.out.println("writeMap:     " + values.get(j));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 输出变量的取值数量信息
     * @param directory
     */
    public void writeSplitValueInfo(String directory) {
        String valueStatistics="filename(文件名)\ttokenNum\tvalueNum\n";
        Statistic statistic=new Statistic();
        for(Grammar grammar:grammars){
            valueStatistics+=statistic.getValueNumStatistics(grammar);
        }
        writeProgram(directory+""+File.separator+"valueStatistics.txt",valueStatistics);
    }

    /**
     * 输出变量的占比信息 文件名（去掉目录） 总变量数 终结变量数 非终结变量数
     * @param directory
     */
    public void writeCycleInfo(String directory) {
        String loopStatistics="filename(文件名)\ttokenNum(文法变量总数)\t终结变量个数\t非终结变量个数\t非终结非循环变量个数\t非终结循环变量个数\t单变量循环的变量个数\t多变量循环的变量个数>=2,<5\t多变量循环长度为>=5,<10\t多变量循环长度为>=10\t多变量循环个数\n";
        Statistic statistic=new Statistic();
        for(Grammar grammar:grammars){
            loopStatistics+=statistic.getcycleStatistics(grammar);
        }
        writeProgram(directory+""+File.separator+"cycleStatistics.txt",loopStatistics);
    }

    /**
     * 输出非终结变量的占比信息 文件名（去掉目录） 总变量数 终结变量数 非终结变量数
     * @param directory
     */
    public void writeVarInfo(String directory) {
        String varTerminateStatistics="filename(文件名)\ttokenNum(文法变量总数)\tterminateNum终结变量个数\tnonterminateNum非终结变量个数\n";
        Statistic statistic=new Statistic();
        for(Grammar grammar:grammars){
            varTerminateStatistics+=statistic.getVarTerminateStatistics(grammar);
        }
        writeProgram(directory+""+File.separator+"terminateStatistics.txt",varTerminateStatistics);
    }

    /**
     * 输出字符串到一指定文件里
     * @param path
     * @param str
     */
    public void writeProgram(String path, String str) {
        BufferedWriter bw = null;
        FileWriter fr = null;
        try {
            File file=new File(path);
            if(!file.exists())
            {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //将写入转化为流的形式
            fr = new FileWriter(path);
            bw = new BufferedWriter(fr);
            //一次写一行
            bw.write(str);
            bw.newLine();  //换行用
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void printGrammarName(String directory) {
        File root=new File(directory);
        File[] sons = root.listFiles();
        //如果sons已经没有子文件了(即root本身就是一个文件时) 则return 结束递归
        if (sons == null) return;
        int index=0;
        //System.out.println("printGrammarName");
        for (File son: sons) {
            //如果son是一个文件，且它的文件名的后缀是txt结尾,则保存该文件
            if (son.isDirectory()) {
                index++;
                System.out.print(son.getName()+"\t");
                if(index%10==0){
                    //System.out.println("");
                }
            }
        }
    }
}
