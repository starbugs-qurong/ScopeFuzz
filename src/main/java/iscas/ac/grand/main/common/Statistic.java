package iscas.ac.grand.main.common;

import iscas.ac.grand.main.common.Grammar;
import iscas.ac.grand.main.common.Token;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Statistic {
    /**
     * 取出各个文法中终结变量和非终结变量的占比情况数据
     * @param grammar
     * @return
     */
    public String getVarTerminateStatistics(Grammar grammar) {
        String result="";
        if(grammar==null){
            return result;
        }
        List<String> tokenList=grammar.getOrderedList();
        int tokenNum=tokenList.size();
        //变量类型 非递归有子token1 递归2 跨层递归3 无子token4
        List<String> terminateTokenList=getListByTokenType(grammar,"4");//无子token的，终止变量
        String filename=grammar.getPath();
        filename=filename.substring(filename.lastIndexOf(File.separator)+1);
        result+=filename+"\t";
        result+=tokenNum+"\t";
        //计算百分比 并保留两位小数
        result+=terminateTokenList.size()+"("+getRate(terminateTokenList.size(),tokenNum)+")"+"\t";//非终结变量个数

        result+=(tokenNum-terminateTokenList.size())+"("+getRate(tokenNum-terminateTokenList.size(),tokenNum)+")"+"\n";//非终结变量个数
        return result;
    }

    /**
     * 计算比例，保留两位小数，按百分比返回
     * @param num
     * @param sum
     * @return
     */
    public String getRate(int num, int sum) {
        if(sum<=0||num<=0){
            return "0%";
        }
        double rate = (float)num/(float)(sum*1.0)* 100;
        String result=String.format("%.2f", rate)+"%";
        return result;
    }

    /**
     * 取出各个文法中变量总数以及取值总数
     * @param grammar
     * @return
     */
    public String getValueNumStatistics(Grammar grammar) {
        String result="";
        if(grammar==null){
            return result;
        }
        List<String> tokenList=grammar.getOrderedList();
        int tokenNum=tokenList.size();
        String filename=grammar.getPath();
        filename=filename.substring(filename.lastIndexOf(File.separator)+1);
        result+=filename+"\t";
        result+=tokenNum+"\t";
        int valueNum=0;
        for(String token:tokenList){
            if(grammar.getTokenByKey(token)!=null&&grammar.getTokenByKey(token).getValueList()!=null){
                valueNum+=grammar.getTokenByKey(token).getValueList().size();
            }
        }
        result+=valueNum+"\n";
        return result;
    }


    /**
     * 取出各个文法中循环文法变量的占比情况数据
     * @param grammar
     * @return
     */
    public String getcycleStatistics(Grammar grammar) {
        String result="";
        if(grammar==null){
            return result;
        }
        List<String> tokenList=grammar.getOrderedList();
        int tokenNum=tokenList.size();
        //变量类型 非递归有子token1 递归2 跨层递归3 无子token4
        List<String> acyclicTokenList=getListByTokenType(grammar,"1");//非循环的
        List<String> cyclicTokenList=getListByTokenType(grammar,"2");//单个变量循环的
        List<String> cyclicMultiTokenList=getListByTokenType(grammar,"3");//多个变量循环的
//        System.out.println("=============================多变量循环==========================");
//        for(String s:cyclicMultiTokenList) {
//        	System.out.println(s);
//        }
        
        
        List<String> terminateTokenList=getListByTokenType(grammar,"4");//无子token的，终止变量
        int[] cycleLens=getCycleLens(grammar);//统计跨层循环中各个变量的循环长度，cycleLens[i]表示长度为i的变量的个数
        int[] interval={2,5,10};
        String cycleLensStr=arrOutFormatBySplit(cycleLens,interval,"\t",tokenNum);//把循环长度用\t拼接起来

        String filename=grammar.getPath();
        filename=filename.substring(filename.lastIndexOf(File.separator)+1);
        result+=filename+"\t";
        result+=tokenNum+"\t";

        result+=terminateTokenList.size()+"("+getRate(terminateTokenList.size(),tokenNum)+")"+"\t";//终结变量个数

        result+=(tokenNum-terminateTokenList.size())+"("+getRate(tokenNum-terminateTokenList.size(),tokenNum)+")"+"\t";//非终结变量个数

        result+=acyclicTokenList.size()+"("+getRate(acyclicTokenList.size(),tokenNum)+")"+"\t";//非循环变量

        result+=cyclicTokenList.size()+cyclicMultiTokenList.size()+"("+getRate(cyclicTokenList.size()+cyclicMultiTokenList.size(),tokenNum)+")"+"\t";//循环变量

        result+=cyclicTokenList.size()+"("+getRate(cyclicTokenList.size(),tokenNum)+")"+"\t";//单变量循环 长度为1

        result+=cycleLensStr+"\t";//长度为2-5为一组，长度大于5为一组共两组

        result+=cyclicMultiTokenList.size()+"("+getRate(cyclicMultiTokenList.size(),tokenNum)+")"+"\n";//多变量循环个数
        return result;
    }
    /**
     * 取出各个文法中循环文法变量的占比情况数据
     * @param grammar
     * @return
     */
    public String getLoopStatistics(Grammar grammar) {
        String result="";
        if(grammar==null){
            return result;
        }
        List<String> tokenList=grammar.getOrderedList();
        int tokenNum=tokenList.size();
        //变量类型 非递归有子token1 递归2 跨层递归3 无子token4
        List<String> acyclicTokenList=getListByTokenType(grammar,"1");//非循环的
        List<String> cyclicTokenList=getListByTokenType(grammar,"2");//单个变量循环的
        List<String> cyclicMultiTokenList=getListByTokenType(grammar,"3");//多个变量循环的
        List<String> terminateTokenList=getListByTokenType(grammar,"4");//无子token的，终止变量
        int[] cycleLens=getCycleLens(grammar);//统计跨层循环中各个变量的循环长度，cycleLens[i]表示长度为i的变量的个数
        String cycleLensStr=arrOutFormat(cycleLens,"\t");//把循环长度用\t拼接起来

        result+=grammar.getPath()+"\t";
        result+=tokenNum+"\t";
        result+=acyclicTokenList.size()+terminateTokenList.size()+"\t";
        result+=acyclicTokenList.size()+"\t";
        result+=terminateTokenList.size()+"\t";
        result+=cyclicTokenList.size()+cyclicMultiTokenList.size()+"\t";
        result+=cyclicTokenList.size()+"\t";
        result+=cyclicMultiTokenList.size()+"\t";
        result+=cycleLensStr+"\n";
        return result;
    }


    /**
     * 使用interval拼接int 数组中的值
     * @param arr
     * @return
     */
    public String arrOutFormat(int[] arr,String interv) {
        String result="";
        if(arr==null||arr.length==0){
            return result;
        }
        for(int i=2;i<arr.length;i++ ){
            result+=arr[i]+interv;
        }
        return result;
    }

    /**
     * 使用interval拼接int 数组中的值,需要按区间累计，{2,5,10}表示从2开始，2<=i<5为第一组 5<=i<10为第一组  10=<i为第三组
     * @param arr
     * @return
     */
    public String arrOutFormatBySplit(int[] arr,int[] splits,String interv,int total) {
        String result="";
        if(arr==null||arr.length==0||splits==null||splits.length==0){
            return result;
        }
        if(splits.length==1){//相当于统计循环长度大于等于某一个值的变量的个数
            for(int i=splits[0];i<arr.length;i++ ){
                result+=arr[i]+"("+getRate(arr[i],total)+")"+interv;//非循环变量
            }
        }else{
            int j=0;
            int start=splits[j++];
            int end=splits[j++];
            while(j<splits.length){
                int sum=0;
                for(int i=start;i<arr.length&&i<end;i++ ){
                    sum+=arr[i];
                }
                result+=sum+"("+getRate(sum,total)+")"+interv;//非循环变量
                start=end;
                end=splits[j++];
            }

            //5-10
            int sum=0;
            for(int i=start;i<arr.length&&i<end;i++ ){
                sum+=arr[i];
            }
            result+=sum+"("+getRate(sum,total)+")"+interv;//非循环变量


            //10-末尾
            sum=0;
            for(int i=end;i<arr.length;i++ ){
                sum+=arr[i];
            }
            result+=sum+"("+getRate(sum,total)+")"+interv;//非循环变量
        }

        return result;
    }
    /**
     * 取出grammar中各个变量的循环步长
     * @param grammar
     * @return
     */
    public int[] getCycleLens(Grammar grammar) {
        if(grammar==null){
            return new int[0];
        }
        int max=getMaxCyclen(grammar);
        int[] result=new int[max+1];
        List<String> tempList=grammar.getOrderedList();
        for(String tokenName:tempList ){
            Token token=grammar.getTokenByKey(tokenName);
            if(token.getTokenType().equals("3")){
//                if(token.getCycleStep()==4){
                    //System.out.println(grammar.getPath()+": token "+tokenName+" lenght: "+token.getCycleStep()+" type: "+token.getTokenType());
//                }
                if(token.getCycleStep()>1){
                    result[token.getCycleStep()]=result[token.getCycleStep()]+1;
                }else{
                    //System.out.println(grammar.getPath()+": token "+tokenName+" lenght: "+token.getCycleStep()+" type: "+token.getTokenType());
                }
            }
        }
        return result;
    }

    /**
     * 取出跨层循环中的最大循环长度，用于初始化数组的长度
     * @param grammar
     * @return
     */
    public int getMaxCyclen(Grammar grammar) {
        int result=0;
        if(grammar==null){
            return result;
        }
        List<String> tempList=grammar.getOrderedList();
        for(String tokenName:tempList ) {
            Token token = grammar.getTokenByKey(tokenName);
            if (token.getTokenType().equals("3")) {
                if (token.getCycleStep() > 1) {
                    if(token.getCycleStep()>result){
                        result=token.getCycleStep();
                    }
                }
            }
        }
        return result;
    }

    /**
     *取出各种类型的token的数量
     * @param grammar
     * @param type
     * @return
     */
    public List<String> getListByTokenType(Grammar grammar, String type) {
        if(grammar==null){
            return new ArrayList<>();
        }
        List<String> result=new ArrayList<>();
        List<String> tempList=grammar.getOrderedList();
        for(String tokenName:tempList ){
            Token token=grammar.getTokenByKey(tokenName);
            if(token.getTokenType().equals(type)){
                result.add(tokenName);
            }
        }
        return result;
    }

}
