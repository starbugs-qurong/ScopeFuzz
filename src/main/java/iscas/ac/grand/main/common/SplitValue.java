package iscas.ac.grand.main.common;
import iscas.ac.grand.main.common.Block;
import iscas.ac.grand.main.common.StringTools;

import java.util.*;

public class SplitValue {

    /**
     * 把每个Value的字符串中的子字符串拆分开来，用竖线|分割，构成多个可行的取值Value，注意括号的匹配
     * @param valueList
     * @return
     */
    public List<String> splitValueByOr(List<String> valueList) {
        List<String> result=new ArrayList<>();
        if(valueList==null||valueList.size()==0){
            return null;
        }
        OpenStarAdd osa=new OpenStarAdd();
        //去星号*，展开*
        int valueNum=valueList.size();
        int valueNumNew=valueNum;
        do{
            valueNum=valueNumNew;
            valueList=osa.deleteStars(valueList);
            valueNumNew=valueList.size();
        }while(valueNum<valueNumNew);

        //去加号+，展开+
        valueNum=valueList.size();
        valueNumNew=valueNum;
        do{
            valueNum=valueNumNew;
            valueList=osa.deleteAdds(valueList);
            valueNumNew=valueList.size();
        }while(valueNum<valueNumNew);


        Set<String> valueSet=new HashSet<>();
        Block block=new Block("");
        for(String s:valueList){
            List<String> ss=block.getItemsFromOr(s);
            if(ss!=null&&ss.size()>0){
                for(String sss:ss){
                    valueSet.add(sss);
                }
            }else{
                valueSet.add(s);
            }

        }

        Set<String> copyValueSet=valueSet;

        Set<String> tempResult;

        Set<String> needSplitValueSet=getContainsOR(copyValueSet);//确定需要裂变的value
        SetOperation so=new SetOperation();
        result.addAll(so.different(valueSet,needSplitValueSet));//不需要裂变的直接假如结果集

        while(needSplitValueSet!=null&&needSplitValueSet.size()>0){
            tempResult=new HashSet();
            for(String str:needSplitValueSet){
                Set<String> tempSet=new HashSet<>();
                Set<String> tempOrValueSet=new HashSet<>();
                List<String> threePart = new ArrayList<>();
                threePart=splitValueByBracketAndOr(str);//拆分一个value为三份，XX   ( XX | XX | XX)    XX
                if(threePart!=null&&threePart.size()==3){//对含有竖线和括号的内容进行裂变，组成多个value
                    String targetStr=threePart.get(1);
                    targetStr= StringTools.deleteParBrackets("(",")",targetStr);//删除前后的多余括号
//                    targetStr=common.StringTools.deleteBrackets("(",")",targetStr);//删除前后的多余括号
                    tempOrValueSet=getOrValueSet(targetStr);//把 XX | XX | XX拆分成多份，按照竖线拆分
                    tempSet=constrcutValues(threePart,tempOrValueSet);//裂变value
                    if(tempSet!=null&&tempSet.size()==1&&tempSet.iterator().next().equals(str)){
                        result.add(str);
                        continue;
                    }else{
                        if(tempSet!=null&&tempSet.size()>0) {
                            tempResult.addAll(tempSet);
                        }
                    }

                }
            }
            if(tempResult==null||tempResult.size()==0){// 例如 '[' (']' ('[' ']')* arrayInitializer | expression ']' ('[' expression ']')* ('[' ']')*)
                result.addAll(needSplitValueSet);
                //System.out.println("these values cant be split:"+needSplitValueSet);
                break;
            }
            needSplitValueSet=getContainsOR(tempResult);//确定需要裂变的value
            result.addAll(so.different(tempResult,needSplitValueSet));
        }
        return result;
    }



    /**
     * 判断待拆分集合是否有变化，如果无变化但包含竖线 但是无法再拆分 就结束拆分任务
     * @param set1
     * @param set2
     * @return
     */
    public boolean isSameSet(Set<String> set1, Set<String> set2) {
        boolean result=false;
        if(set1==null&&set2==null)return true;
        if(set1==null&&set2!=null)return false;
        if(set1!=null&&set2==null)return false;
        if(set1.size()!=set2.size())return false;
        for(String str:set1){
            if(!set2.contains(str)){
                return false;
            }
        }
        return true;
    }

    /**
     *把threeParts中的第二项裂变为childs中的多项，组成新的多个value
     * @param threeParts
     * @param childs
     * @return
     */
    public Set<String> constrcutValues(List<String> threeParts, Set<String> childs) {
        Set<String> result=new HashSet<String>();
        if(threeParts==null||threeParts.size()<3||childs==null||childs.size()==0){
            return null;
        }
        for(String middle:childs){
            String tempStr="";
            tempStr+=threeParts.get(0);
            tempStr+=""+middle+"";
            tempStr+=threeParts.get(2);
            result.add(tempStr);
        }
        return result;
    }

    /**
     * //把( XX | XX | XX) 拆分成多份，按照竖线拆分
     * @param str
     * @return
     */
    public Set<String> getOrValueSet(String str) {
        Set<String> result=new HashSet<>();
        Block block=new Block("");
        List<String> tempList=block.getItemsFromOr(str);
        if(tempList==null||tempList.size()==0){
            return null;
        }
        for(String s:tempList){
            result.add(s);
        }
        return result;
    }

    /**
     * 拆分一个value为三份，XX   ( XX | XX | XX)    XX, 注意括号的嵌套
     * @param str
     * @return
     */
    public List<String> splitValueByBracketAndOr(String str) {
        List<String> result=new ArrayList<>();
        if(str==null||str.length()==0){
            return null;
        }
        List<Integer> leftBrackets=new ArrayList<>();//记录左括号的位置
        List<Integer> rightBrackets=new ArrayList<>();//记录右括号的位置
        Stack bracketsStack=new Stack();//存放(
        int targetStackIndex=0;
        int targetIndex=0;
        int startIndex=0;
        int endIndex=0;

        //定义左右括号的对应关系
        Map<Character,Character> bracket  = new HashMap<>();
        bracket.put(']','[');
        bracket.put('\'','\'');//单引号也要匹配，尤其是单引号中的括号不应该参与匹配
        Stack stack = new Stack();
        for(int i = 0; i < str.length(); i++){
            Character temp = str.charAt(i);//先转换成字符
            //是否为左括号
            if(temp=='['||temp=='\''){
                if(stack.isEmpty()){
                    stack.push(temp);
                }else{
                    if(stack.peek().equals('\'')){
                        if(temp=='\''){
                            stack.pop();
                            continue;
                        }else{
                            //栈顶是单引号，但是没读到单引号与他匹配
                        }
                    }else if(stack.peek().equals('[')){
                        //栈顶是[，但是没读到]与他匹配
                    }
                }
            }else if(temp==']'){
                if(stack.isEmpty()){
                    //读到了]但栈为空，这个字符认为是文法中的特殊字符
//                    strpre+=temp;
                }else if(!stack.isEmpty()){
                    if(stack.peek().equals('\'')){
                        //栈顶是单引号，但是没读到单引号与他匹配
                    }else if(stack.peek().equals('[')){
                        //栈顶是[，可以匹配
                        stack.pop();
                    }
                }
            }
            if(stack.isEmpty()){//此时读入的才是非‘’ []中的字符
                if(temp=='('){
                    bracketsStack.push(temp);
                    leftBrackets.add(i);
                }else if(temp==')'){
                    if(!bracketsStack.isEmpty()){
                        int size=bracketsStack.size();
                        if(size==targetStackIndex){
                            if(leftBrackets.size()>=targetIndex){
                                startIndex=leftBrackets.get(targetIndex-1);
                            }else{
                                //System.out.println("unbelievable!"+str+" targetIndex :"+targetIndex+" leftBrackets.size():"+ leftBrackets.size());
                            }
                            endIndex=i;
                            break;
                        }else if(size>targetStackIndex){
                            rightBrackets.add(i);
                            bracketsStack.pop();
                            if(bracketsStack.isEmpty()){//如果出栈后栈为空了，需要清除两个记录位置的列表
                                leftBrackets=new ArrayList<>();
                                rightBrackets=new ArrayList<>();
                            }
                        }else{
                            //System.out.println("unbelievable! "+str+"no ( before | ");
                        }
                    }else{
                        //System.out.println("unbelievable!"+str+"  no ( for this )");
                    }
                }
                else if(temp=='|'&&targetIndex==0){
                    targetIndex=leftBrackets.size();
                    targetStackIndex=bracketsStack.size();
                }
            }
        }
        String preStr="";
        String middileStr="";
        String postStr="";
        if(startIndex>0&&endIndex<str.length()-1){
            preStr=str.substring(0,startIndex);
            postStr=str.substring(endIndex+1);
        }else{
            if(startIndex==0){
                preStr="";
            }else{
                preStr=str.substring(0,startIndex);
            }
            if(endIndex==str.length()-1){
                postStr="";
            }else{
                postStr=str.substring(endIndex+1);
            }
        }
        middileStr=str.substring(startIndex,endIndex+1);
        result.add(preStr);
        result.add(middileStr);
        result.add(postStr);
        return result;
    }

    /**
     * 判断一个Set当中，有没有可以裂变的value,含有类似（ XX | XX ）
     * @param valueSet
     * @return
     */
    public Set<String> getContainsOR(Set<String> valueSet) {
        Set<String>  result=null;
        if(valueSet==null||valueSet.size()==0){
            return result;
        }
        StringTools st=new StringTools();
        for(String value:valueSet){
            String tempVal=st.deleteInvalidPart(value);//移除,改成替换其中无效的部分为空格
            if(tempVal.contains("(")&&tempVal.contains(")")&&tempVal.contains("|")){
                int indexShu=tempVal.indexOf("|");
                int indexLeftBracket=getLatestLeftBracketIndex(tempVal,indexShu,"(");
                int indexRightBracket=getLatestRightBracketIndex(tempVal,indexShu,")");
                if(indexLeftBracket!=-1&&indexLeftBracket<indexShu&&indexRightBracket!=1000&&indexRightBracket>indexShu){
                    if(result==null){
                        result=new HashSet<>();
                    }
                    result.add(value);
                }
            }
        }
        return result;
    }
    /**
     *在str中从index位置的左侧在errorstr之前寻找target，如果可以找到，返回其位置，如果找不到，返回-1
     * @param str
     * @param index
     * @param target
     * @return
     */
    public int getLatestLeftBracketIndex(String str, int index, String target) {
        int result=-1;
        int len=target.length();
        index=index-len;
        while(index>=0){
            String window=str.substring(index,index+len);
            if(window.equals(target)){
                return index;
            }
//            if(window.equals(errorStr)){
//                return -1;
//            }
            index--;
        }
        return result;
    }

    /**
     *在str中从index位置的右侧在errorstr之前寻找target，如果可以找到，返回其位置，如果找不到，返回1000
     * @param str
     * @param index
     * @param target
     * @return
     */
    public int getLatestRightBracketIndex(String str, int index, String target) {
        int result=1000;
        int len=target.length();
        index=index+1;
        while(index<=str.length()-len){
            String window=str.substring(index,index+len);
            if(window.equals(target)){
                return index;
            }
//            if(window.equals(errorStr)){
//                return 1000;
//            }
            index++;
        }
        return result;
    }
}
