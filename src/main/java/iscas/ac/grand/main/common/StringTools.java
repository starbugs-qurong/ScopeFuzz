package iscas.ac.grand.main.common;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTools {
    /**
     * 判断当前字符串中是否还有未匹配的左括号
     * @return
     */
    public static boolean judgeBrackets(String str){
        boolean result=false;
        if(str==null||(!str.contains("(")&&!str.contains("[")&&!str.contains("{")&&!str.contains("'"))){
            return false;
        }else{
            //定义左右括号的对应关系
            Map<Character,Character> bracket  = new HashMap<>();
            bracket.put(')','(');
            bracket.put(']','[');
            bracket.put('}','{');
            bracket.put('\'','\'');//单引号也要匹配，尤其是单引号中的括号不应该参与匹配

            Stack stack = new Stack();

            for(int i = 0; i < str.length(); i++){

                Character temp = str.charAt(i);//先转换成字符
                //是否为左括号
                if(temp=='\\'){//如果是转义字符，向后再读一位
                    i++;
                    continue;
                }
                if(bracket.containsValue(temp)){
                    if(stack.isEmpty()){
                        stack.push(temp);
                    }else if(!stack.peek().equals('\'')&&!stack.peek().equals('[')){
                        stack.push(temp);
                    }else if(stack.peek().equals('\'')){
                        if(temp.equals('\'')){
                            stack.pop();
                        }else{
                            //单引号中间的各种括号不用加入待匹配栈中
//                            stack.push(temp);
                        }
                    }
                    //是否为右括号
                }else if(bracket.containsKey(temp)){
                    if(stack.isEmpty()){
                        return true;
                    }
                    //若左右括号匹配
                    if(stack.peek() == bracket.get(temp)){
                        stack.pop();
                    }else if(stack.peek().equals('\'')){

                    }else {
                        //return true;
                    }
                }

            }
            return !stack.isEmpty();
        }
    }
    /**
     * qurong
     * 2025-2-19 将两个List<String>中的字符串，两两拼接形成新的字符型列表
     * @param pre
     * @param post
     * @return
     */
    public List<String> combineStrList(List<String> pre, List<String> post) {
        List<String> result=new ArrayList<>();
        if(pre==null||pre.size()==0){
            result =post;
        }else{
            for(String resPre:pre){
                if(post==null||post.size()==0)
                {
                    result.add(resPre);
                }else {
                    for (String resPost : post) {
                        result.add(resPre + " " + resPost);
                    }
                }
            }
        }
        return result;
    }
    /**
     * 判断当前字符串中是否还有未匹配的左括号,用于拆分value中的竖线
     * @return
     */
    public static boolean judgePartBrackets(String str){
        boolean result=false;
        if(str==null||(!str.contains("(")&&!str.contains("[")&&!str.contains("{"))){
            return false;
        }else{
            //定义左右括号的对应关系
            Map<Character,Character> bracket  = new HashMap<>();
            bracket.put(']','[');
            bracket.put('\'','\'');//单引号也要匹配，尤其是单引号中的括号不应该参与匹配

            Stack stack = new Stack();

            for(int i = 0; i < str.length(); i++){

                Character temp = str.charAt(i);//先转换成字符
                //是否为左括号
                if(temp=='\\'){//如果是转义字符，向后再读一位
                    i++;
                    continue;
                }
                if(bracket.containsValue(temp)){
                    if(stack.isEmpty()){
                        stack.push(temp);
                    }else if(!stack.peek().equals('\'')&&!stack.peek().equals('[')){
                        stack.push(temp);
                    }else if(stack.peek().equals('\'')){
                        if(temp.equals('\'')){
                            stack.pop();
                        }else{
                            //单引号中间的各种括号不用加入待匹配栈中
//                            stack.push(temp);
                        }
                    }
                    //是否为右括号
                }else if(bracket.containsKey(temp)){
                    if(stack.isEmpty()){
                        return true;
                    }
                    //若左右括号匹配
                    if(stack.peek() == bracket.get(temp)){
                        stack.pop();
                    }else if(stack.peek().equals('\'')){

                    }else {
                        //return true;
                    }
                }

            }
            return !stack.isEmpty();
        }
    }

    /**
     * 判断当前字符串中是否包括一个[]
     * @return
     */
    public static boolean judgeSquareBrackets(String str){
        boolean result=false;
        if(str==null||!str.contains("[")||!str.contains("]")){
            return false;
        }
        int j=str.length()-1;
        int i=0;
        while(i<j){
            if(str.charAt(i)!='['){
                i++;
            }
            if(str.charAt(j)!=']'){
                j--;
            }
            if(i<j&&str.charAt(i)=='['&&str.charAt(j)==']'){
                return true;
            }
        }
        return false;
    }
    /**
     * 判断当前字符串中是否包括一个''
     * @return
     */
    public static boolean judgeYingBrackets(String str){
        boolean result=false;
        if(str==null||!str.contains("'")){
            return false;
        }
        int j=str.length()-1;
        int i=0;
        while(i<j){
            if(str.charAt(i)!='\''){
                i++;
            }
            if(str.charAt(j)!='\''){
                j--;
            }
            if(i<j&&str.charAt(i)=='\''&&str.charAt(j)=='\''){
                return true;
            }
        }
        return false;
    }

    /**
     * 删除内容外部的多层括号
     * @param start
     * @param end
     * @param str
     * @return
     */
    public static String deleteBrackets(String start,String end,String str){
        if(str==null||str==""){
            return "";
        }
        String result=str.trim();
        int count=0;
        while(result.startsWith(start)&&result.endsWith(end)&&result.length()>=3){//去除最外层的括号，不排除多层的可能性
            String tempStr=result.substring(1,result.length()-1);
//            if(matchBeginAndEnd(result,start,end)){
//            if(!judgePartBrackets(result) && !judgePartBrackets(tempStr)){
            if(!judgeBrackets(result) && !judgeBrackets(tempStr)){
                count++;
                result=result.substring(1,result.length()-1);
                result=result.trim();
            }else{
                break;
            }
        }
        if(start=="{"&&end=="}"){
            if(count>0){
                for(int i=0;i<count;i++){
                    result="'{' "+result+"'}'";
                }
            }
        }
        if(result.startsWith(start)&&result.endsWith(end)&&result.length()==2){
            return "'"+start+"'"+" '"+end+"'";
        }
        return result;
    }

    /**
     * 删除内容外部的多层括号
     * @param start
     * @param end
     * @param str
     * @return
     */
    public static String deleteParBrackets(String start,String end,String str){
        if(str==null||str==""){
            return "";
        }
        String result=str.trim();
        int count=0;
        while(result.startsWith(start)&&result.endsWith(end)&&result.length()>=3){//去除最外层的括号，不排除多层的可能性
            String tempStr=result.substring(1,result.length()-1);
//            if(matchBeginAndEnd(result,start,end)){
            if(!judgePartBrackets(result) && !judgePartBrackets(tempStr)){
//            if(!judgeBrackets(result) && !judgeBrackets(tempStr)){
                count++;
                result=result.substring(1,result.length()-1);
                result=result.trim();
            }else{
                break;
            }
        }
        if(start=="{"&&end=="}"){
            if(count>0){
                for(int i=0;i<count;i++){
                    result="'{' "+result+"'}'";
                }
            }
        }
        if(result.startsWith(start)&&result.endsWith(end)&&result.length()==2){
            return "'"+start+"'"+" '"+end+"'";
        }
        return result;
    }

    /**
     * 首尾是否是匹配的两个字符
     * @param str
     * @return
     */
    private static boolean matchBeginAndEnd(String str,String start,String end) {
        boolean result=false;
        Stack stack1 = new Stack();
        if(str.startsWith(start)){
            stack1.push(start);
        }else{
            return result;
        }

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
            if(stack.isEmpty()) {//此时读入的才是非‘’ []中的字符
                if (temp == start.charAt(0)) {
                    if (stack1.isEmpty()) {
                        return result;
                    } else {
                        stack1.push(start);
                    }
                } else if (temp == end.charAt(0)) {
                    if (stack1.isEmpty()) {
                        //System.out.println(" unmatched ) in :" + str);
                        return result;
                    } else {
                        if (stack1.size() == 1) {
                            if (i == str.length() - 1) {
                                return true;
                            } else {
                                return result;
                            }
                        } else {
                            stack1.pop();
                        }
                    }
                }
            }
        }
        return result;
    }

    public static List<String> getMsg(String msg) {

        List<String> list = new ArrayList<String>();
        Pattern p = Pattern.compile("(\\()([\\s\\S]*)(\\))");
        Matcher m = p.matcher(msg);
        int count=0;
        while (m.find()) {
//            list.add(m.group(0).substring(1, m.group().length() - 1));
            count++;
            //System.out.println("Match no:"+count);
            //System.out.println("Found at: "+ m.start()+ " - " + m.end());
        }

        return list;
    }


    /**
     * 判断字符串是token表达式1 还是正则表达式2 还是''表达式3 0表示其它
     * @param str
     * @return
     */
    public int judgeTokenOrRegex(String str) {
        int result=0;
        if(str==null||str==""){
            return result;
        }
        //含有token
        Set<String> tokens=getWordsFromStr(str);
        if(tokens!=null&&tokens.size()>0) {
            return 1;
        }
        if(str.startsWith("(")){
            return 1;
        }
        //从前往后，可以匹配到一个[] 1
        if(((str.startsWith("[")||str.startsWith("~["))&&str.endsWith("]"))||(str.startsWith("[")&&(str.endsWith("]*")||str.endsWith("]+")||str.endsWith("]?")))){
            return 2;
        }
//        boolean type2=common.StringTools.judgeSquareBrackets(str);
//        if(type2){
//            return 2;
//        }
        //从前往后，可以匹配到一个'' 3
        if(str.startsWith("\'")&&str.endsWith("\'")&&str.length()>=3){
            return 3;
        }
//        boolean type3=common.StringTools.judgeYingBrackets(str);
//        if(type3){
//            return 3;
//        }
        return result;
    }
    /**
     * 从字符串中获取单词
     * @param str
     * @return
     */
    public Set<String> getWordsFromStr(String str) {
        Set<String> result=new HashSet();
        if(str==null||str==""){
            return null;
        }
//        if(str.contains("prefix")){
//            //System.out.println("get token here :"+str);
//        }
        String str1=deleteInvalidPart(str);
        String regex = "[a-zA-Z_0-9\']+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str1);
        while (m.find()) {
            String word=m.group();
            if(word!=null&&!(word.startsWith("\'")||word.endsWith("\'"))){
                result.add(m.group());
            }
        }
//        if(str.contains("prefix")){
//            //System.out.println(" have get these tokens :");
//            for(String s:result){
//                //System.out.println(s);
//            }
//        }
        return result;
    }

    /**
     * 删除字符串中的无效部分例如单引号，方括号中的内容
     * @param str
     * @return
     */
    public String deleteInvalidPart(String str) {
        String result="";
        if(str==null||str.length()==0){
            return "";
        }
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
                    result+=temp;
                }else if(!stack.isEmpty()){
                    if(stack.peek().equals('\'')){
                        //栈顶是单引号，但是没读到单引号与他匹配
                    }else if(stack.peek().equals('[')){
                        //栈顶是[，可以匹配
                        stack.pop();
                    }
                }
            }
            if(stack.isEmpty()){
                result+=temp;
            }
        }
        return result;
    }

    /**
     * 从字符串数组中获取单词
     * @param strs
     * @return
     */
    public Set<String> getWordsFromStrs(List<String> strs) {
        Set<String> result=new HashSet();
        if(strs==null||strs.size()==0){
            return result;
        }
        for(String str:strs){
            Set<String> temp=getWordsFromStr(str);
            if(temp!=null&&temp.size()>0){
                result.addAll(temp);
            }
        }
        return result;
    }



    public static void main(String[] args){
        String result=deleteBrackets("(",")","(((  ( (hdsahdjkhsjkah hdahskjdh hsadkjashk)) )  ggggg (  ( (hdsahdjkhsjkah hdahskjdh hsadkjashk)) )))");
//        //System.out.println(result);

        boolean flag=judgeBrackets("'\\\\' [btnfr\"\'\\\\]");
//        //System.out.println(flag);

        String msg = "mSurface=(ccc)  ggggg (ddd)   (sdnajndjl)";
        List<String> list = getMsg(msg);
//        //System.out.println(list);
    }
}
