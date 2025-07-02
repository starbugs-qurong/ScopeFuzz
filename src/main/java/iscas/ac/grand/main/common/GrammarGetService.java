package iscas.ac.grand.main.common;
import iscas.ac.grand.main.c.CConfigureInfo;
import iscas.ac.grand.main.javapkg.JavaConfigureInfo;
import iscas.ac.grand.main.python.PythonConfigureInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class GrammarGetService {
    /**
     * 从path文件中读取出各个变量的定义，separator表示多个取值之间的分隔符号，文法中一般为|竖线，约束中一般为&
     * @param path
     * @param separator
     * @return
     * @throws IOException
     */

    public Grammar getVarsFormG4(String path, String separator) throws IOException{
        BufferedReader fileReader = new BufferedReader(new FileReader(path));//语法文件
        Map<String, List<String>> keyValues=new HashMap<String,List<String>>();//变量键值对map
        Map<String, Set<String>> keyTokens=new HashMap<String,Set<String>>();//变量键token map
        List<String> orderedList=new ArrayList<>();
        String line;
        boolean isLexer=false;//当前文法是否为词法文件
        boolean isParser=false;//当前文法是否为语法文件
        boolean isGrammar=false;//当前文件是否为一个完整的文法文件
        String tokenVocab="";//文法需要引用的的词法文件 如果有的话
        String grammarName="";//文法的名称
        StringTools st=new StringTools();
        SplitValue sv=new SplitValue();
        while ((line = fileReader.readLine()) != null) {
            //不保留行尾注释
            line=line.trim();
            if(line=="") {
                continue;//忽略这一个空行
            }
            //忽略注释行
            if(line.startsWith("//")||line.startsWith("/*")){
//                fileReader=deleteCommentsLine(line,fileReader);
                String validCOn=deleteCommentsLine(line,fileReader);
                if(validCOn!=null&&validCOn!=""){
                    line=validCOn;
                }
                else{
                    continue;
                }
            }
            line=deleteComments(line);
            //确定是否为词法标记
            if(line.startsWith("lexer grammar")) {
                isLexer=true;
                //拆分出文法名称
                grammarName=getGrammarName(line);
                continue;//忽略这一行
            }

            //确定是否为语法标记
            if(line.startsWith("parser grammar")) {
                isParser=true;
                //拆分出文法名称
                grammarName=getGrammarName(line);
                continue;//忽略这一行
            }
            //确定是否为文法标记
            if(line.startsWith("grammar ")) {
                isGrammar=true;
                //拆分出文法名称
                grammarName=getGrammarName(line);
                continue;//忽略这一行
            }
            //确定是否为语法标记
            if(line.startsWith("options {")) {
                //匹配{}，拆分出token词汇表的名称 { tokenVocab = CqlLexer; }，可能会跨行，要读到}再作为整体处理，还要注意多个项的情况options {
                //	tokenVocab = GoLexer;
                //	superClass = GoParserBase;
                //}
                tokenVocab=getTokenVocab(line);
                continue;//忽略这一行
            }
            //有效变量
            if(startWithChar(line)) {//以字母开头
                if (line.endsWith(";")) {//单行定义的变量
                    //删去末尾的分号
                    line=deleteSemicolon(line);
                    //使用冒号：拆分后保存在变量的map里
                    if(line.contains(":")){
                        KeyValue keyValue=getKVByString(line);
                        List value=new ArrayList<String>();//变量值value
                        value.add(keyValue.getValue());
                        if(value.size()==0){
                            //System.out.println(keyValue.getKey());
                        }
                        List copyValue=value;//变量值value拷贝
                        if(ifSplit()) {
                            value = sv.splitValueByOr(value);//拆分value
                            if (value == null || value.size() == 0) {
                                value = copyValue;//分割失败，回退
                                //System.out.println(keyValue.getKey());
                            }
                        }
                        value=deleteUnicodeValues(value);//如果value有多个取值，从中删除正则表达式中含有的取值方式
                        keyValues.put(keyValue.getKey(),value);
                        Set<String> tokens=st.getWordsFromStr(keyValue.getValue());//变量值value中的token
                        keyTokens.put(keyValue.getKey(),tokens);
                        orderedList.add(keyValue.getKey());
                    }else{//缺少冒号，无法拆分
                        //System.out.println(line+": no : to match, no : in this line");
                    }
                } else {//多行定义的变量
                    String key="";//变量名key
                    List value=new ArrayList<String>();//变量值value
                    boolean semicolonFlag=false;
                    String tempVal=new String("");//value值的缓存，连接跨行值
                    if(line.contains(":")){//第一行中包括key和部分value值，g4文件中一般没有这种情况
                        KeyValue keyValue=getKVByString(line);
                        key=keyValue.getKey();
                        tempVal=keyValue.getValue();
                    }else{//第一行中只包括key，g4文件中一般都是这种情况
                        key=line;//变量名key
                        //向后继续读入，直到遇到单行中的分号；
                        line = fileReader.readLine();
                        //忽略注释行
                        while(line!=null&&(line.equals("")||line.startsWith("//")||line.startsWith("/*"))){
//                            fileReader=deleteCommentsLine(line,fileReader);
                            String validCOn=deleteCommentsLine(line,fileReader);
                            if(validCOn!=null&&validCOn!=""){
                                line=validCOn;
                            }
                            else{
                                line = fileReader.readLine();
                            }
                            if(line!=null){
                                line=line.trim();
                                line=deleteComments(line);
                            }
                        }
                        while(line!=null){
                            line=line.trim();
                            line=deleteComments(line);
                            if(line.startsWith(":")){//去掉行首的冒号
                                if(line.endsWith(";")){
                                    semicolonFlag=true;
                                    line=line.substring(1,line.length()-2);
                                }else{
                                    line=line.substring(1);
                                }
                                line=line.trim();
                                tempVal=line;
                                break;
//                                value.add(line);
                            }else{
                                //key行不包括冒号且value第一行首位不是冒号：，key可能有多行跨行的情况
//                                //System.out.println(line+": no : in the key "+key+" and first line of "+line+" to match");//C.g4中的fragment 描述的变量名是这种情况，key是有两个字符串一起描述的，可以把fragment描述符省略
                                line=line.trim();
                                if(line.contains(":")){//在key跨行的非首行中包括冒号：那么冒号后面的部分应该算作部分value值
                                    if(line.endsWith(";")){
                                        semicolonFlag=true;
                                        line=line.substring(0,line.length()-1);
                                    }
                                    KeyValue keyValue=getKVByString(line);
                                    if(key.trim().equalsIgnoreCase("fragment")){
                                        key=keyValue.getKey();
                                    }else{
                                        key+=" "+keyValue.getKey();
                                    }
                                    tempVal=keyValue.getValue();
                                    break;
                                }else{
                                    if(key.trim().equalsIgnoreCase("fragment")) {
                                        key=line;
                                    } else{
                                        key+=" "+line;
                                    }

                                }
                                line = fileReader.readLine();
                            }
                        }
                    }
                    if(!semicolonFlag){//还未读取到分号
                        line = fileReader.readLine();
                        while (line!= null){
                            line=line.trim();
                            line=deleteComments(line);
                            //忽略注释行
                            while(line!=null&&(line.equals("")||line.startsWith("//")||line.startsWith("/*"))){
//                            fileReader=deleteCommentsLine(line,fileReader);
                                String validCOn=deleteCommentsLine(line,fileReader);
                                if(validCOn!=null&&validCOn!=""){
                                    line=validCOn;
                                }
                                else{
                                    line = fileReader.readLine();
                                }
                                if(line!=null){
                                    line=line.trim();
                                    line=deleteComments(line);
                                }
                            }
                            if(line!=null&&!line.equals(";")&&!line.endsWith(";")){
                                if(line.startsWith(separator)){//去掉行首的竖线|,（在约束中为去掉&）
                                    //如果当前的缓存值中包含未匹配的左括号([{，就继续缓存，否则，向取值列表value中add一个完整的值tempVal
                                    if(StringTools.judgeBrackets(tempVal)){
                                        tempVal+=" "+line;
                                    }else{
                                        value.add(tempVal);
                                        tempVal=new String("");
                                        line=line.substring(1);
                                        line=line.trim();
                                        tempVal=line;
                                    }
                                }else{
                                    tempVal+=" "+line;
                                    //key行不包括竖线|且value第一行首位不是竖线|：
                                    //System.out.println(line+": no | in the key "+key+" and first line of "+line+" to match");//按理说不会运行到这一行,但不少G4文件中的value值存在跨行现象
                                }
                            }else{
                                if(line==null){
                                    //System.out.println("can't match ; in value "+tempVal+" of "+key);
                                }else{
                                    if(line.length()>=2&&line.endsWith(";")){//;分号前面的内容应该加到tempVal中
                                        tempVal+=" "+line.substring(0,line.length()-1);
                                    }else{
                                        //单行中仅有一个;分号
                                    }
                                }
                                if(tempVal!=null){
                                    value.add(tempVal);
                                    tempVal=new String("");
                                    break;
                                }
                            }
                            line = fileReader.readLine();
                        }
                    }else{//在冒号的行尾出现分号，提前结束
                        //对tempVal中的竖线进行拆分
                        value.add(tempVal);
                    }
                    key=deleteFragment(key);//变量名key
                    List copyValue=value;//变量值value拷贝
                    if(value.contains("modifier* memberDeclaration")){
                        //System.out.println("(value.contains(modifier* memberDeclaration"+value);
                    }
                    if(ifSplit()) {//由分割开关来控制是否进行这一步的操作
                        value = sv.splitValueByOr(value);////拆分value，根据取值中的|，把每个取值裂变成多个取值
                        if (value == null || value.size() == 0) {
                            value = copyValue;//分割失败，回退
                            //System.out.println("value == null || value.size() == 0"+key);
                        }
                    }
                    value=deleteUnicodeValues(value);//如果value有多个取值，从中删除正则表达式中含有的取值方式
                    keyValues.put(key,value);
                    Set<String> tokens=st.getWordsFromStrs(value);//变量值value中的token
                    keyTokens.put(key,tokens);
                    orderedList.add(key);
                    if(line==null){
                        //读取结束没有匹配到变量value结束符号；
                        if(value!=null) {
                            //System.out.println(value.toArray().toString() + ": no ; to match in " + key);
                        }
                        break;
                    }
                }
            }
        }
        GrammarInfo grammarInfo=new GrammarInfo(grammarName,isGrammar,isParser,isLexer,tokenVocab);
        Grammar result=new Grammar(path,keyValues,orderedList,keyTokens,new HashMap<>(),grammarInfo);
        return result;
    }

    /**
     * qurong
     * 2022-11-16
     * 判断是否需要进行分割
     * @return
     */
    private boolean ifSplit() {
        if(ConfigureInfo.ifJavaCode&&JavaConfigureInfo.ifSplitOr){
            return true;
        }else if(ConfigureInfo.ifPythonCode&& PythonConfigureInfo.ifSplitOr){
            return true;
        }else if(ConfigureInfo.ifCCode&& CConfigureInfo.ifSplitOr){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * 删除词法文件中的内部变量名的前缀fragment
     * @param str
     * @return
     */
    public String deleteFragment(String str) {
        if(str!=null&&str.length()>=9&&str.toLowerCase().startsWith("fragment ")){
            return str.substring(9);
        }
        return str;
    }

    /**
     * 在一个字符串中，冒号分割两个子字符串，分别表示一个key和一个value
     * @param line
     * @return
     */
    public KeyValue getKVByString(String line) {
        KeyValue result=new KeyValue("","");
        String[] cons = line.split(":");
        if(cons!=null) {
            if (cons.length >= 1) {
                cons[0]=deleteFragment(cons[0].trim());//对fragment修饰的变量的特殊处理
            }
            if (cons.length >= 2) {
                result.setKey(cons[0].trim());//变量名key
                if(cons.length == 2) {
                    result.setValue(cons[1].trim());
                }else{
                    result.setValue(line.substring(line.indexOf(':')).trim());
                }
//                                result.put(key,value);
            }else{
                if(cons.length ==1){
                    //第一行中包括键和冒号key:
                    result.setKey(cons[0].trim());//变量名key
                }else{
                    //System.out.println(line+": no : to match, split array length<1");//按理说不会运行到这一行
                }
            }
        }else{
            //System.out.println(line+": no : to match, split array is null");//按理说不会运行到这一行
        }
        return result;
    }
    /**
     * 如果value有多个取值，从中删除正则表达式中含有unicode的取值方式,返回其余的取值方式
     * @param value
     * @return
     */
    public List<String> deleteUnicodeValues(List<String> value) {
        List<String> result=new ArrayList<>();
        for(String chars:value) {
            boolean ifSave=true;
            for (int i = 0; i < chars.length(); i++) {
                Character cur = chars.charAt(i);
                if (cur == '-') {
                    if (i <= 0) {
                    } else if (i >= chars.length() - 1) {
                    } else {//0-9全部加入结果集合
                        if (chars.charAt(i + 1) == '\\') {//下一位是转义字符的首尾，可能是-\uDBFF，这种先不处理
                            ifSave=false;
                            break;
                        }
                    }
                } else {//不是转义开始符号\，也不是转义\后的第一个字符，也不是-，那就是普通字符
                }
            }
            if(ifSave){
                result.add(chars);
            }
        }
        if(result.size()==0){
            //System.out.println("this value list all contain unicode: "+value);
        }
        return result;
    }
    /**
     * 删除从当前行开始的空行，单行注释，以及多行注释，返回文件读取的位置
     * @param line
     * @param fileReader
     * @return
     * @throws IOException
     */
    public static String deleteCommentsLine(String line, BufferedReader fileReader) throws IOException{
        //确定是否为单注释行
        if(line.equals("")||line.startsWith("//")) {
            return null;//忽略这一行注释
        }
        //确定是否为多行注释
        if(line.startsWith("/*")){
            if(line.endsWith("*/")){
                //多行注释在一行中结束了
                return null;
            }
            if(line.contains("*/")){
                String[] con=line.split("\\*/");
                if(con.length==2){
                    return con[1].trim();
                }else{
                    //一行中有两个*/注释结束符号
                    //System.out.println(line+": 2 ‘*/’ in this line ");
                }
            }
            String comments=line;
            //向后继续读入，直到多行注释结束
            line = fileReader.readLine();
            while (line!= null){
                line=line.trim();
                if(!line.endsWith("*/")&&!line.contains("*/")){
                    comments+="/n"+line;
                }else{
                    if(line.contains("*/")&&!line.endsWith("*/")){
                        //System.out.println(line+": the content after this comment can't be processed ");
                        String[] con=line.split("\\*/");
                        if(con.length==2){
                            //System.out.println(line+": there are some contents after ‘*/’ in this line ");
                            return con[1].trim();
                        }else{
                            //一行中有两个*/注释结束符号
                            //System.out.println(line+": 2 ‘*/’ in this line ");
                        }
                        return "";
                    }
                    return "";
                }
                line = fileReader.readLine();
            }
            if(line==null){
                //读取结束没有匹配到多行注释的结束符号*/
                //System.out.println(comments+": no */ to match");
                return null;
            }
            return "";
        }
        return "";
    }

    /**
     * 删除行末注释//
     * @param str
     * @return
     */
    public static String deleteComments(String str){
        String result=str;
        if(str.contains("//")&&!str.contains("'//'")){
            int index=str.lastIndexOf("//");
            result=str.substring(0,str.lastIndexOf("//")>1?str.lastIndexOf("//"):str.length());
            result=result.trim();
        }
        else if(str.contains("#")&&!str.contains("'#'")){
            int index=str.lastIndexOf("#");
            result=str.substring(0,str.lastIndexOf("#")>1?str.lastIndexOf("#"):str.length());
            result=result.trim();
        }
        return result;
    }

    /**
     * 删除末尾的分号
     * @param str
     * @return
     */
    public String deleteSemicolon(String str) {
        String result=str.substring(0,str.length()-1);
        return result;
    }

    /**
     * 判断一个字符串是否以字母开头
     * @param s
     * @return
     */
    public static boolean startWithChar(String s) {
        if (s != null && s.length() > 0) {
            String start = s.trim().substring(0, 1);
            Pattern pattern = Pattern.compile("^[A-Za-z]+$");
            return pattern.matcher(start).matches();
        } else {
            return false;
        }
    }
    public String getTokenVocab(String line) {
        String result="";
        if(line.contains("tokenVocab")){
            String[] parts=line.split("=");
            if(parts.length>1){
                String item=parts[1];
                if(item.contains(";")&&item.contains("}")){
                    parts=item.split(";");
                    if(parts.length>1){
                        result=parts[0];
                    }
                }
            }
        }
        result=result.trim();
        return result;
    }

    public String getGrammarName(String line) {
        String result="";
        String[] parts=line.split("grammar");
        if(parts.length>1){
            String item=parts[1].trim();
            if(item.contains(";")){
                item=item.substring(0,item.indexOf(";"));
            }
            result=item.trim();
        }
        return result;
    }
}
