package iscas.ac.grand.main.common;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Constraint {
    private List<String> continuityList = new ArrayList<String>();//连续性约束变量
    private List<String> uniquenessList = new ArrayList<String>();//唯一性约束变量
    private Map<String,Integer> terminationMap = new HashMap<>();//终止性约束变量


    public List<String> getContinuityList() {
        return continuityList;
    }

    public void setContinuityList(List<String> nospace) {
        this.continuityList = nospace;
    }

    public List<String> getUniquenessList() {
        return uniquenessList;
    }

    public void setUniquenessList(List<String> uniquenessList) {
        this.uniquenessList = uniquenessList;
    }

    public Map<String, Integer> getTerminationMap() {
        return terminationMap;
    }

    public void setTerminationMap(Map<String, Integer> terminationMap) {
        this.terminationMap = terminationMap;
    }

    /**
     * 取出生成文法过程中的约束（手动添加），约束文件保存在文法同级目录下，例如JavaParser_constraint.txt
     * @param filePath
     * @return
     */
    public Constraint getConstraint(Grammar grammar, String filePath) throws IOException {
        Constraint result=new Constraint();
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        GrammarGetService grammarGetService =new GrammarGetService();
        Grammar constraintGrammar= grammarGetService.getVarsFormG4(filePath,"&");//暂用文法读取的方式
        if(constraintGrammar!=null){
            Map<String,List<String>> vars=constraintGrammar.getVars();
            if(vars!=null&&vars.size()>0){
                List<String> tokenlist=grammar.getOrderedList();

                //连续性约束
                List<String> continuityList=vars.get("Continuity");
                if(continuityList!=null&&continuityList.size()>0){
                    //约束中的token必须是来自文法中的
                    continuityList.removeIf(str -> !tokenlist.contains(str));
                    result.setContinuityList(continuityList);
                }

                //唯一性约束
                List<String> uniquenessList=vars.get("Uniqueness"); //其它约束在此处拓展
                if(uniquenessList!=null&&uniquenessList.size()>0){
                    uniquenessList.removeIf(str -> !tokenlist.contains(str));
                    result.setUniquenessList(uniquenessList);
                }

                //终止性约束
                List<String> terminationList=vars.get("Termination"); //其它约束在此处拓展
                if(terminationList!=null&&terminationList.size()>0){
                    Map<String,Integer> terminationMap = new HashMap<>();
                    for(String var:terminationList){
                        String[] keyval=var.split(" ");
                        if(keyval!=null&&keyval.length>=2){
                            String key=keyval[0].trim();
                            int val=Integer.valueOf(keyval[1].trim());
                            if(tokenlist.contains(key))
                                terminationMap.put(key,val);
                        }
                    }
                    result.setTerminationMap(terminationMap);
                }
            }
        }
        return result;
    }

    /**
     * 检查一个标识符是否在连续性约束的列表里，如果在，将其所对应的生成内容删除空格
     */
    public String checkContinueConstraint(String tokenGen,String token,int index,List<Grammar> grammars) {
        if(token==null){
            return tokenGen;
        }
        String result=tokenGen;
        //连续性约束（不包含空格）
        if(grammars==null||grammars.size()==0){
            return tokenGen;
        }
        Constraint constraint=grammars.get(index).getConstraint();
        List<String> continuityList=constraint.getContinuityList();
        if(continuityList!=null&&continuityList.contains(token.trim())){
            result=result.replaceAll("\\s*", "");
        }
        return result;
    }


    /**
     * qurong
     * 2025-2-19
     * 检查标识符是否在连续性约束的列表里，如果在，将其所对应的生成内容删除空格
     */
    public List<String> checkContinueConstraints(List<String> tokenGen,String token,int index,List<Grammar> grammars) {
        List<String> resultList=new ArrayList<>();
        if(token==null){
            return tokenGen;
        }
        if(tokenGen==null){
            return resultList;
        }
        if(grammars==null||grammars.size()==0){
            return tokenGen;
        }
        for(String str:tokenGen){
            String result=str;
            //连续性约束（不包含空格）
            Constraint constraint=grammars.get(index).getConstraint();
            List<String> continuityList=constraint.getContinuityList();
            if(continuityList!=null&&continuityList.contains(token.trim())){
                result=result.replaceAll("\\s*", "");
            }
            resultList.add(result);
        }
        return resultList;
    }

    /**
     * 终止性检查 如果满足最大循环次数，那么必须从token的终结子值中随机选出一个作为待生成的文法
     * @param str
     * @param index
     * @param cyclyLength
     * @return
     */
    public String checkTermination(String str, int index, int cyclyLength,List<Grammar> grammars) {
        String result =str;
        String strCopy=result;
        Token token;
        GrammarCheckService gcs=new GrammarCheckService();
        try{
            token=grammars.get(index).getTokens().get(str.trim());
            if(token.getTokenType()=="2"||token.getTokenType()=="3"){//是循环变量
                Constraint constraint=grammars.get(index).getConstraint();
                Map<String,Integer>  terminationMap=constraint.getTerminationMap();
                if(terminationMap!=null&&terminationMap.keySet().contains(strCopy.trim())){
                    int steplength=terminationMap.get(strCopy.trim());
                    if(cyclyLength>steplength){
                        result=result.trim();
                        result=gcs.getTerminateValue(result,grammars.get(index),token).getValue();//取当前token的终结子值
                        if(result.trim()==strCopy.trim()){
                            //没能找到合适的子value
                            //System.out.println(" 1 can't find a terminate child value for : "+result);
                        }else{
                            //找到了合适的子value
                        }
                    }
                }else{
                    if(cyclyLength> ConfigureInfo.getCycleStepLength()){
                        result=result.trim();
                        result=gcs.getTerminateValue(result,grammars.get(index),token).getValue();//取当前token的终结子值
                        if(result.trim()==strCopy.trim()){
                            //没能找到合适的子value
                            //System.out.println(" 2 can't find a terminate child value for : "+result);
                        }else{
                            //找到了合适的子value

                        }
                    }else{
                        //还未到循环上限
                        result=str.trim();
                    }
                }
            }else{//非循环变量
                result=str.trim();
            }
        }catch (Exception e){
            //System.out.println(" not a token : "+str);
        }

        return result;
    }


    /**
     * 在str中检查是否包含唯一约束的token
     * @param str
     * @param token
     * @return
     */
    public String checkUniqueConstraint(String str, String token,int index,List<Grammar> grammars) {
        String result="";
        if(str==null||str==""||token==null||token==""){
            return result;
        }
        result=str.trim();
        if(grammars==null||grammars.size()==0){
            return result;
        }
        Constraint constraint=grammars.get(index).getConstraint();
        String tempStr=token.trim();
        if(tempStr.endsWith("*")||tempStr.endsWith("+")){
            tempStr=tempStr.substring(0,tempStr.length()-1).trim();
            List<String> uniquenessList=constraint.getUniquenessList();
            if(uniquenessList!=null&&uniquenessList.contains(tempStr)){
                result=getUniqueStr(str);
            }
        }
        return result;
    }

    /**
     * qurong
     * 2025-2-19
     * 在str中检查是否包含唯一约束的token
     * @param strs
     * @param token
     * @return
     */
    public List<String> checkUniqueConstraints(List<String> strs, String token,int index,List<Grammar> grammars) {
        List<String> resultList=new ArrayList<>();
        if(strs==null||strs.size()==0){
            return resultList;
        }
        if(grammars==null||grammars.size()==0){
            return resultList;
        }
        for(String str:strs){
            String result="";
            if(str==null||str==""||token==null||token==""){
                resultList.add("");
                continue;
            }
            result=str.trim();
            Constraint constraint=grammars.get(index).getConstraint();
            String tempStr=token.trim();
            if(tempStr.endsWith("*")||tempStr.endsWith("+")){
                tempStr=tempStr.substring(0,tempStr.length()-1).trim();
                List<String> uniquenessList=constraint.getUniquenessList();
                if(uniquenessList!=null&&uniquenessList.contains(tempStr)){
                    result=getUniqueStr(str);
                }
            }
            resultList.add(result);
        }

        return resultList;
    }
    /**
     * 删除字符串中重复的项 例如 public public static static relative relative 删除后public static relative
     * @param str
     * @return
     */
    public String getUniqueStr(String str) {
        String result="";
        if(str==null||str==""){
            return result;
        }
        str=str.trim();
        if(!str.contains(" ")){
            return str;
        }
        String[] items=str.split(" ");
        if(items.length>1){
            Set<String> uniqueItems=new HashSet<>();
            for(String item:items){
                uniqueItems.add(item);
            }
            for(String item:uniqueItems){
                result+=item+" ";
            }
        }else{
            //System.out.println(" unknow space format : "+str);
            return str;
        }
        result=result.trim();
        return result;
    }

}
