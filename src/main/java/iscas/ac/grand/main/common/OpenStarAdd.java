package iscas.ac.grand.main.common;

import iscas.ac.grand.main.javapkg.JavaConfigureInfo;
import java.util.ArrayList;
import java.util.List;

public class OpenStarAdd {

    /**
     * 通过展开一个valulist中的所有*，重新确定valulist包含的元素，例如valulist包含a*和b*两个元素，在*最大取值为3的情况下，展开*后valulist包含的元素为 空 a aa aaa b bb bbb这七个
     * @param valueList
     * @return
     */
    public List<String> deleteStars(List<String> valueList) {
        List<String> result=new ArrayList<>();
        if(valueList==null||valueList.size()==0){
            return null;
        }
        for(String s:valueList){
            if(s.contains("*")){
                result.addAll(openStars(s));//展开单个取值s中的所有*
            }else{
                result.add(s);
            }
        }
        return result;
    }

    /**
     * 展开单个取值value中的所有*
     * @param value
     * @return
     */
    public List<String> openStars(String value) {
        List<String> result=new ArrayList<>();
        if(value==null||value==""){
            return null;
        }
        StringTools st=new StringTools();
        //展开value中的所有*
        String[] items=value.split(" ");//认为括号后面要么跟+？* 要么跟空格
        String tempItem="";
        boolean tempFlag=false;
        for(String item:items){
            if(tempItem==""){
                tempItem=item;
                tempFlag=true;
            }else{
                tempFlag=false;
            }
            if(StringTools.judgeBrackets(tempItem)){
                if(!tempFlag){
                    tempItem+=" "+item;
                }
            }else{
                tempItem=tempItem.trim();
                int type=st.judgeTokenOrRegex(tempItem);//判断tempItem是正则表达式 还是token表达式 还是''表达式
                result=openStarsByItem(tempItem,type,result);//将tempItem按照情况（结尾是否为*，是否为token或正则表达式等）来处理，更新result中包含的元素;
                if(!tempFlag){
                    tempItem=item;
                }else {
                    tempItem ="";
                }
            }
        }
        if(tempItem!=""){//最后一个结果也要为其生成语言
            if(StringTools.judgeBrackets(tempItem)){
                //System.out.println(" can't match brackets: "+tempItem);
                if(result.size()==0){//第一个成分
                    result.add(tempItem);//直接添加到valueList中
                }else{//中间成分
                    for(String valPre:result){
                        String newPre=valPre.trim()+" "+tempItem;//每一个前缀和tempItem拼接起来
                        result.add(newPre);
                    }
                }
            }else{
                tempItem=tempItem.trim();
                int type=st.judgeTokenOrRegex(tempItem);//判断tempItem是正则表达式 还是token表达式 还是''表达式
                result=openStarsByItem(tempItem,type,result);//将tempItem按照情况（结尾是否为*，是否为token或正则表达式等）来处理，更新result中包含的元素;
            }
        }

        return result;
    }

    /**
     * 将tempItem按照情况（结尾是否为*，是否为token或正则表达式等）来处理，更新result中包含的元素
     * @param tempItem
     * @param valueList
     * @return
     */
    public List<String> openStarsByItem(String tempItem, int type,List<String> valueList) {
        List<String> result=new ArrayList();
        if(type==1){//token类型
            if(tempItem.endsWith("*")){
                tempItem=tempItem.substring(0,tempItem.length()-1);
                if(!tempItem.contains("|")||!isOrItem(tempItem)){//不含竖线，或者包含竖线但是竖线不是一级运算符号（a|b）这种就是不应该删除括号的
                    tempItem=StringTools.deleteBrackets("(",")",tempItem);
                }
                if(valueList.size()==0){//value中第一个成分就包含需要展开的*
                    result.add("");//*等于0相当于空字符串
                    for(int i = 1; i<= JavaConfigureInfo.starnum; i++){//*等于0相当于空字符串,上面已经添加过，不再添加
                        String iTempItem="";
                        for(int j=1;j<=i;j++){//i个tempItem元素空格拼接
                            iTempItem+=" "+tempItem;
                        }
                        result.add(iTempItem);
                    }
                }else{//不是起始位置
                    result.addAll(valueList);//把i取值为零时各种前缀都添加到结果集合中
                    for(String valPre:valueList){
                        for(int i = 1; i<= JavaConfigureInfo.starnum; i++){//*等于0相当于包含仅valPre
                            String iTempItem="";
                            for(int j=1;j<=i;j++){//i个tempItem元素空格拼接
                                iTempItem+=" "+tempItem;
                            }
                            String newPre=valPre.trim()+" "+iTempItem;//前缀和i个tempItem拼接起来
                            result.add(newPre);
                        }
                    }
                }

            }else {//非*结尾，只将其添加到valueList中的每一个value的后面
                if(valueList.size()==0){//第一个成分
                    result.add(tempItem);//直接添加到result中
                }else{//中间成分
                    for(String valPre:valueList){
                        String newPre=valPre.trim()+" "+tempItem;//每一个前缀和tempItem拼接起来
                        result.add(newPre);
                    }
                }
            }
        }else {//正则表达式或者字符串类型
            if(valueList.size()==0){//第一个成分
                result.add(tempItem);//直接添加到result中
            }else{//中间成分
                for(String valPre:valueList){
                    String newPre=valPre.trim()+" "+tempItem;//每一个前缀和tempItem拼接起来
                    result.add(newPre);
                }
            }
        }
        return result;
    }

    /**
     * 判断是否为（主体|主体|主体）的形式，即括号中要先对竖线部分进行拆分
     * @param str
     * @return
     */
    public boolean isOrItem(String str) {
        boolean result=false;
        str=StringTools.deleteBrackets("(",")",str);//先去掉首尾的括号
        str=str.replace("|"," | ");//保证|不连接在前后的主体上
        String[] items=str.split(" ");//认为括号后面要么跟+？* 要么跟空格
        items=deleteNullEle(items);
        String tempItem="";
        boolean tempFlag=false;
        for(int i=0;i<items.length; i++){
            String item=items[i];
            if(tempItem==""){
                tempItem=item;
                tempFlag=true;
            }else{
                tempFlag=false;
            }
            if(StringTools.judgeBrackets(tempItem)){
                if(!tempFlag){
                    tempItem+=" "+item;
                }
            }else{//括号匹配了
                tempItem=tempItem.trim();
                i=i+1;
                if(i<items.length) {
                    item = items[i].trim();
                    if(!item.equals("|")){
                        result=false;
                        break;
                    }
                }else{
                    result=true;
                    break;
                }
                if(!tempFlag){
                    tempItem=item;
                }else {
                    tempItem ="";
                }
            }
        }
        return result;
    }

    /**
     * 删除数组中元素内容为空元素
     * @param arr
     * @return
     */
    public String[] deleteNullEle(String[] arr) {
        List<String> list=new ArrayList<String>();
        for(int i=0;i<arr.length&&arr.length>0;i++){
            if(arr[i]==null||"".equals(arr[i].trim().toString())){
                continue;
            }else{
                list.add(arr[i]);
            } }
        String []reuslt=new String[list.size()];
        for(int i=0;i<reuslt.length;i++){
            reuslt[i]=list.get(i);
        }
        return reuslt;
    }


    /**
     * 通过展开一个valulist中的所有+，重新确定valulist包含的元素，例如valulist包含a+和b+两个元素，在+最大取值为3的情况下，展开+后valulist包含的元素为 a aa aaa b bb bbb这六个
     * @param valueList
     * @return
     */
    public List<String> deleteAdds(List<String> valueList) {
        List<String> result=new ArrayList<>();
        if(valueList==null||valueList.size()==0){
            return null;
        }
        for(String s:valueList){
            if(s.contains("*")){
                result.addAll(openStars(s));//展开单个取值s中的所有*
            }else{
                result.add(s);
            }
        }
        return result;
    }

    /**
     * 展开单个取值value中的所有+
     * @param value
     * @return
     */
    public List<String> openAdds(String value) {
        List<String> result=new ArrayList<>();
        if(value==null||value==""){
            return null;
        }
        StringTools st=new StringTools();
        //展开value中的所有*
        String[] items=value.split(" ");//认为括号后面要么跟+？* 要么跟空格
        String tempItem="";
        boolean tempFlag=false;
        for(String item:items){
            if(tempItem==""){
                tempItem=item;
                tempFlag=true;
            }else{
                tempFlag=false;
            }
            if(StringTools.judgeBrackets(tempItem)){
                if(!tempFlag){
                    tempItem+=" "+item;
                }
            }else{
                tempItem=tempItem.trim();
                int type=st.judgeTokenOrRegex(tempItem);//判断tempItem是正则表达式 还是token表达式 还是''表达式
                result=openAddsByItem(tempItem,type,result);//将tempItem按照情况（结尾是否为*，是否为token或正则表达式等）来处理，更新result中包含的元素;
                if(!tempFlag){
                    tempItem=item;
                }else {
                    tempItem ="";
                }
            }
        }
        if(tempItem!=""){//最后一个结果也要为其生成语言
            if(StringTools.judgeBrackets(tempItem)){
                //System.out.println(" can't match brackets: "+tempItem);
                if(result.size()==0){//第一个成分
                    result.add(tempItem);//直接添加到valueList中
                }else{//中间成分
                    for(String valPre:result){
                        String newPre=valPre.trim()+" "+tempItem;//每一个前缀和tempItem拼接起来
                        result.add(newPre);
                    }
                }
            }else{
                tempItem=tempItem.trim();
                int type=st.judgeTokenOrRegex(tempItem);//判断tempItem是正则表达式 还是token表达式 还是''表达式
                result=openStarsByItem(tempItem,type,result);//将tempItem按照情况（结尾是否为*，是否为token或正则表达式等）来处理，更新result中包含的元素;
            }
        }

        return result;
    }

    /**
     * 将tempItem按照情况（结尾是否为+，是否为token或正则表达式等）来处理，更新result中包含的元素
     * @param tempItem
     * @param valueList
     * @return
     */
    public List<String> openAddsByItem(String tempItem, int type,List<String> valueList) {
        List<String> result=new ArrayList();
        if(type==1){//token类型
            if(tempItem.endsWith("+")){
                tempItem=tempItem.substring(0,tempItem.length()-1);
                if(!tempItem.contains("|")||!isOrItem(tempItem)){//不含竖线，或者包含竖线但是竖线不是一级运算符号（a|b）这种就是不应该删除括号的
                    tempItem= StringTools.deleteBrackets("(",")",tempItem);
                }
                if(valueList.size()==0){//value中第一个成分就包含需要展开的+
                    result.add("");//*等于0相当于空字符串
                    for(int i = 1; i<= JavaConfigureInfo.addnum; i++){//*等于0相当于空字符串,上面已经添加过，不再添加
                        String iTempItem="";
                        for(int j=1;j<=i;j++){//i个tempItem元素空格拼接
                            iTempItem+=" "+tempItem;
                        }
                        result.add(iTempItem);
                    }
                }else{//不是起始位置
//                    result.addAll(valueList);//把i取值为零时各种前缀都添加到结果集合中 这里区别于*可以取值为0，+至少取值1
                    for(String valPre:valueList){
                        for(int i = 1; i<= JavaConfigureInfo.addnum; i++){//*等于0相当于包含仅valPre
                            String iTempItem="";
                            for(int j=1;j<=i;j++){//i个tempItem元素空格拼接
                                iTempItem+=" "+tempItem;
                            }
                            String newPre=valPre.trim()+" "+iTempItem;//前缀和i个tempItem拼接起来
                            result.add(newPre);
                        }
                    }
                }

            }else {//非*结尾，只将其添加到valueList中的每一个value的后面
                if(result.size()==0){//第一个成分
                    result.add(tempItem);//直接添加到result中
                }else{//中间成分
                    for(String valPre:valueList){
                        String newPre=valPre.trim()+" "+tempItem;//每一个前缀和tempItem拼接起来
                        result.add(newPre);
                    }
                }
            }
        }else {//正则表达式或者字符串类型
            if(valueList.size()==0){//第一个成分
                result.add(tempItem);//直接添加到result中
            }else{//中间成分
                for(String valPre:valueList){
                    String newPre=valPre.trim()+" "+tempItem;//每一个前缀和tempItem拼接起来
                    result.add(newPre);
                }
            }
        }
        return result;
    }
    public static void main(String[] args){
        OpenStarAdd openStarAdd=new OpenStarAdd();
        List<String> valueList=new ArrayList<>();
        valueList.add("modifier* memberDeclaration");
        openStarAdd.deleteStars(valueList);
    }
}
