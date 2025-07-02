package iscas.ac.grand.main.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;




public class Block {
    String base="";

    public Block(String base) {
        this.base = base;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    /**
     * [a-zA-Z0-9_]*
     * @param regex
     * @return
     */
    public String getMoreThanZeroByRegex(String regex,int num,boolean flag){
        String result="";
        if(regex==null||!(regex.startsWith("[")&&regex.endsWith("]"))||num<=0||!flag){
            return result;
        }
        String[] regs=regex.split("\\*");
        for(String reg:regs) {
            reg = reg.trim();
            if(num==0){
                result+=reg;
                continue;
            }
            if(num==1){
                int n=(int)(Math.random()*100);
                if(n>=50){
                    result+=getOneFromRegex(regex);
                }else{
                    result+="";
                }
                continue;
            }
            int n=(int)(Math.random()*(num+1));
            for(int i=0;i<n;i++){
//            result+=getOne(regex);//加号前面的token，重复多个的话要不要空格隔开？暂定隔开
                result+=getOneFromRegex(regex);
            }
        }
        return result;
    }

    /**
     * [a-zA-Z0-9_]+
     * @param regex
     * @return
     */
    public String getMoreThanOneByRegex(String regex,int num,boolean flag){
        String result="";
        if(regex!=null&&regex.endsWith("+")){
            regex=regex.substring(0,regex.length()-1);
        }
        if(regex==null||!(regex.startsWith("[")&&regex.endsWith("]"))||num<=0||!flag){
            return result;
        }
        String[] regs=regex.split("\\+");
        for(String reg:regs) {
            reg = reg.trim();
            if(num<=1){
                result+=getOneFromRegex(reg);
                continue;
            }
            int n=(int)(Math.random()*num+1);
            for(int i=0;i<n;i++){
//            result+=getOne(regex);//加号前面的token，重复多个的话要不要空格隔开？暂定隔开
                result+=getOneFromRegex(regex);
            }
        }

        return result;
    }

    /**
     * 例如从正则[a-zA-Z0-9_]里取出一个满足的字符b
     * @param regex
     * @return
     */
    public String getOneFromRegex(String regex) {
        String chars="";
        String result="";
        if(regex.startsWith("[")&&regex.endsWith("]")){
            chars=regex.substring(1, regex.length()-1);
        }else{
            return "";
        }
        if(regex.startsWith("\\u")) {//unicode相关
        	return getUnicodeStrByRegex(regex);
        }
        if(regex.contains("-")) {//unicode相关
        	return getUnicodeStrByRegex(regex);
        }
        
        String[] regs=chars.split("]\\[");
        for(String reg:regs) {
            reg = reg.trim();
            if(reg.contains("-")){//有范围字符例如a-zA-Z0-9 \uD800-\uDBFF，特殊处理
                Set<String> expendedChars=getExpendedChars(reg);
                int lenght=expendedChars.size();
                int n=(int)(Math.random()*lenght);
                result+=expendedChars.toArray()[n];
            }else{
                int lenght=reg.length();
                int n=(int)(Math.random()*lenght);
                result+=reg.charAt(n);
            }
        }
        return result;
    }
    
    /**
     * qurong
     * 2024.4.25
     * 按照unicode的取值规则，从其中取出一个字符
     *例如[\u0000-\u0009\u000B\u000C\u000E-\u0026\u0028-\u005B\u005D-\u007F]
     */
    public String getUnicodeStrByRegex(String str) {
    	String result="";
    	if(str.startsWith("[")&&str.endsWith("]")){
    		str=str.substring(1, str.length()-1);
        }
    	List<String> unicodeStr=new ArrayList<>();
    	str=str.replace("\\u", "");
    	for (int i = 0; i < str.length(); i += 4) {
    		String temp=new String();
    	    String codePoint = str.substring(i, i + 4);
    	    String next=str.substring(i+4, i + 5);
    	   int len=str.length();
    	    if(i+9<=str.length()&&(next.equals("-")||next.contains("-"))) {
    	    	String endPoint = str.substring(i+5, i + 9);
    	    	unicodeStr.addAll(getAllUnicodeInScopeByStr(codePoint,endPoint));
    	    	i=i+5;
    	    }else {
    	    	temp+=convertUnicodeByStr(codePoint);
    			unicodeStr.add(temp);
    	    }    
        }
    	
//    	String[] unicodes=str.split("\\u");
//    	for(int j=0;j<unicodes.length;j++) {
//    		String unicode=unicodes[j];//例如\\u000A 或者\\u000A-
//    		unicode=unicode.replace("\\u", "");
//    		String temp=new String();
//    		if(unicode.length()==5) {//
//    			if(unicode.charAt(4)=='-'&&j<unicodes.length-1) {//把\u0000-\u0009区间内的字符都添加到可选字符集合
//    				String start=unicode.substring(0, 4);
//    				String end=unicodes[j+1].substring(0, 4);
//    				unicodeStr.addAll(getAllUnicodeInScopeByStr(start,end));
//    			}else {
//    				temp+=convertUnicodeByStr(unicode.substring(0, 4));
//        			unicodeStr.add(temp);
//        			unicodeStr.add(unicode.substring(4, 5));
//    			}
//    		}else if(unicode.length()==4) {
//    			temp+=convertUnicodeByStr(unicode);
//    			unicodeStr.add(temp);
//    		}else if (unicode.length()>5){//除了unicode之外，还跟了其它内容
//    			temp+=convertUnicodeByStr(unicode.substring(0, 4));
//    			unicodeStr.add(temp);
//    			for(int i=4;i<unicode.length();i++) {
//    				unicodeStr.add(unicode.substring(i, i+1));
//    			}
//    		}
//    		else {
//    			temp+=convertUnicodeByStr(unicode);
//    			unicodeStr.add(temp);
//    			//System.out.println("unicode length less than 4: "+unicode+" "+temp);
//    		}
//    	}
    	
    	int n=(int)(Math.random()*(unicodeStr.size())+1);
    	if(n>=0&&n<unicodeStr.size()) {
    		result=unicodeStr.get(n);
    	}else if(unicodeStr!=null&&unicodeStr.size()>0){
    		result=unicodeStr.get(0);
    	}
    	
    	return result;
    }
    
    /**
     * qurong
     * 2024.4.24
     * unicod2string
     * 根据XXXX转换单个unicode字符
     */
	private char[] convertUnicodeByInt(int number) {
		return Character.toChars(number);

//    	String unicode = "\\u48\\u65\\u6C\\u6C\\u6F\\u20\\u57\\u6F\\u72\\u6C\\u64\\u21";
//    	unicode = unicode.replace("\\u", "");
//    	StringBuilder str = new StringBuilder();
//    	for (int i = 0; i < unicode.length(); i += 2) {
//    	    String codePoint = unicode.substring(i, i + 2);
//    	    str.append((char) Integer.parseInt(codePoint, 16));
//    	    //System.out.println(Character.toChars(Integer.parseInt(codePoint, 16)));
//    	    
//    	}
//    	//System.out.println(str);

    }
	
	/**
     * qurong
     * 2024.4.24
     * unicod2string
     * 根据\\uXXXX或者XXXX转换单个unicode字符
     */
	private String convertUnicodeByStr(String number) {
		number= number.replace("\\u", "");
		String reuslt="";
		char[] chars=Character.toChars(Integer.parseInt(number, 16));
		reuslt=String.valueOf(chars);
		return reuslt;
	}
	
	/**
     * qurong
     * 2024.4.24
     * unicod2string
     * 根据XXXX转换为一个字符串
     */
	private String convertUnicodeInScope(int startCodePoint,int endCodePoint) {
    	//String str2 = StringEscapeUtils.unescapeJava("\\u0000\\u0001\\u0002\\u0003\\u0004\\u0005\\u0006\\u0007\\u0008\\u0009");
		String result="";
    	for (int codePoint = startCodePoint; codePoint <= endCodePoint; codePoint++) {
    		result+=Character.toChars(codePoint);
    	}
    	return result;

    	
    }
	/**
     * qurong
     * 2024.4.24
     * unicod2string
     * 根据XXXX取出区间内的一个字符
     */
	private String selectUnicodeInScopeByInt(int startCodePoint,int endCodePoint) {
		int n=(int)(Math.random()*(endCodePoint-startCodePoint+1)+1);
		String result="";
		result+=Character.toChars(startCodePoint+n);
    	return result;

    }
	
	/**
     * qurong
     * 2024.4.24
     * unicod2string
     * 根据XXXX取出区间内所有字符
     */
	private List<String> getAllUnicodeInScopeByInt(int startCodePoint,int endCodePoint) {
		
		List<String> result=new ArrayList<>();
		for(int i=startCodePoint;i<=endCodePoint;i++) {
			String temp=new String();
			char[] chars=Character.toChars(i);
			temp=String.valueOf(chars);
			result.add(temp);
		}
		
    	return result;

    }
	
	/**
     * qurong
     * 2024.4.24
     * unicod2string
     * 根据\\uXXXX取出区间内所有的字符
     */
	private List<String> getAllUnicodeInScopeByStr(String startCodePoint,String endCodePoint) {
		List<String> result=new ArrayList<>();
		String unicode1 = startCodePoint.replace("\\u", "");
		String unicode2 = endCodePoint.replace("\\u", "");
		int startInt=Integer.parseInt(unicode1, 16) ;  
    	int endInt= Integer.parseInt(unicode2, 16)  ;   
    	
    	result.addAll(getAllUnicodeInScopeByInt(startInt,endInt));
    	return result;

    }
	
	/**
     * qurong
     * 2024.4.24
     * unicod2string
     * 根据\\uXXXX取出区间内的一个字符
     */
	private String selectUnicodeInScopeByStr(String startCodePoint,String endCodePoint) {
		String result="";
		String unicode1 = startCodePoint.replace("\\u", "");
		String unicode2 = endCodePoint.replace("\\u", "");
		int startInt=Integer.parseInt(unicode1, 16) ;  
    	int endInt= Integer.parseInt(unicode2, 16)  ;    
    	result=selectUnicodeInScopeByInt(startInt,endInt);
    	return result;

    }

    /**
     * 将正则中的区域范围字符展开
     * @param chars
     * @return
     */
    private Set<String> getExpendedChars(String chars) {
        Set<String> result=new HashSet<>();
        for(int i=0;i<chars.length();i++){
            Character cur=chars.charAt(i);
            if(cur=='-'){
                if(i<=0){
                    //第一个位置就是-，暂时不理解是什么含义
                    //System.out.println(" '-' in the first position of a regular expression :"+chars);
                    result.add(String.valueOf(cur));
                }else if(i>=chars.length()-1){
                    //最后一个位置就是-，暂时不理解是什么含义
                    //System.out.println(" '-' in the last position of a regular expression :"+chars);
                    result.add(String.valueOf(cur));
                }else{//0-9全部加入结果集合
                	
                    if(chars.charAt(i+1)=='\\'){//下一位是转义字符的首尾，可能是-\uDBFF，这种先不处理
                        //System.out.println(" '-\\' in the regular expression :"+chars);
                        
                    }
                    for(char c=chars.charAt(i-1);c<=chars.charAt(i+1);c++){
                        result.add(String.valueOf(c));
                    }
                }
            }else{//不是转义开始符号\，也不是转义\后的第一个字符，也不是-，那就是普通字符
                result.add(String.valueOf(cur));
            }
        }
        return result;
    }


    /**
     * token+ 一个或多个,在1-num之间随机取值
     * @param str
     * @return
     */
    public List<String> getMoreThanOne(String str,int num){
        List<String> result=new ArrayList<>();
        if(str==null||str==""){
            return result;
        }
        String[] regs=new String[1];
        if(str.endsWith("+")){
            str=str.substring(0,str.length()-1);
        }
        regs[0]=str;
//        String[] regs=str.split("\\+");    [0-9] ([0-9_]+ [0-9])+ 不能直接spilt
        for(String reg:regs){
            reg=reg.trim();
            if(num<=1){
                result.add(reg);
                continue;
            }
            int n=(int)(Math.random()*num+1);
            for(int i=0;i<n;i++){
                result.add(reg);//加号前面的token，重复多个的话要不要空格隔开？暂定隔开
            }
        }
        return result;
    }

    /**
     * 从多个或|连接的内容中选择一个(t1 | t2 | (t3 | t4) | t5)，可能有嵌套
     * @param str
     */
    public String getOneFromOr(String str){
        String result="";
        if(str==null||str==""){
            return result;
        }
        str= StringTools.deleteBrackets("(",")",str);
        if(str==null||str==""){//(((())))
            return result;
        }
        if(!str.contains("|")){
            return str;
        }
        if(StringTools.judgeBrackets(str)){//括号逐层匹配，是否有无法匹配的括号存在
            //System.out.println(" unmatched ( in this value: "+str);
            return str;
        }
        //策略一，取t1 t2  t3  t4  t5的概率各为1/5

        //策略二，取t1 t2  t5的概率各为1/4, t3  t4 的概率各为1/8
        List<String> items = getItemsFromOr(str);//取出输入中的第一级子项t1 | t2 | (t3 | t4) | t5-->t1， t2 ， (t3 | t4) ， t5共四项
        if(items==null||items.size()==0){
            return result;
        }
        int length=items.size();
        if(length==1){
            return items.get(0);
        }
        int n=(int)(Math.random()*length);
        if(items.get(n).contains("|")){
            return getOneFromOr(items.get(n));
        }else{
            return items.get(n);
        }
    }

    /**
     * 取出输入中的第一级子项t1， t2 ， (t3 | t4) ， t5共四项
     * @param str
     * @return
     */
    public List<String> getItemsFromOr(String str) {
        List<String> result=new ArrayList<>();
        String tempItem="";
        String[] items=str.split("\\|");
        for(String item:items){
            if(tempItem==""){
                tempItem=item;
            }else{
                if(StringTools.judgeBrackets(tempItem)){
                    tempItem+=" | "+item;
                }else{
                    result.add(tempItem.trim());
                    tempItem=new String("");
                    tempItem=item;
                }
            }
        }
        if(!StringTools.judgeBrackets(tempItem)){
            result.add(tempItem.trim());
        }
        return result;
    }

    /**
     * 取一个或零个str，适用token后面跟?
     * @param str
     * @return
     */
    public List<String> getZeroOrOne(String str){
        List<String> result=new ArrayList<>();
        if(str==null){
            return result;
        }
        String[] regs=new String[1];
        if(str.endsWith("?")){
            str=str.substring(0,str.length()-1);
        }
        regs[0]=str;
//        String[] regs=str.split("\\?");
        for(String reg:regs) {
            reg = reg.trim();
            int n=(int)(Math.random()*100);
            if(n>=50){
                result.add(reg);
            }
        }
        return result;
    }

    /**
     * 取一个或零个，适用[a-z]，后面跟?
     * @param str
     * @return
     */
    public String getZeroOrOneByRegex(String str){
        String result="";
        if(str==null){
            return "";
        }
        String[] regs=str.split("\\?");
        for(String reg:regs) {
            reg = reg.trim();
            int n=(int)(Math.random()*100);
            if(n>=50){
                result=getOneFromRegex(reg);
            }else{
                result+="";
            }
        }

        return result;
    }

    /**
     * token+ 零个或多个,在0-num之间随机取值*
     * @param str
     * @param num
     * @return
     */
    public List<String> getMoreThanZero(String str,int num){
        List<String> result=new ArrayList<>();
        if(str==null||str==""){
            return result;
        }
        if(num==0){
            return result;
        }
        String[] regs=new String[1];
        if(str.endsWith("*")){
            str=str.substring(0,str.length()-1);
        }
        regs[0]=str;
//        String[] regs=str.split("\\*");
        for(String reg:regs){
            reg=reg.trim();
            if(num==1){
                int n=(int)(Math.random()*100);
                if(n>=50){
                    result.add(reg);
                }
                continue;
            }
            int n=(int)(Math.random()*(num+1));
            for(int i=0;i<n;i++){
                result.add(reg);//加号前面的token，重复多个的话要不要空格隔开？暂定隔开
            }
        }
        return result;
    }

    /**
     * 取出一个不在正则表达式[]中的字符，~[a-z] 可以取值2、A、+等,num表示char随机的范围上限
     * 如果是~[a-z]~[A-z] 生成两个字符 Aa分别满足两个条件
     * @param str
     * @return
     */
    public String getRevertByRegex(String str,int num){
        String chars="";
        String result="";
//        List<String> regs = new ArrayList<>();
        String[] regs=str.split("~");
        for(String reg:regs){
            if(reg.startsWith("[")&&reg.endsWith("]")){
                chars=reg.substring(1, reg.length()-1);
            }else{
                continue;
            }
            Set<String> expendedChars=getExpendedChars(chars);
            result+=getOneBesidesRegion(expendedChars,num);
        }
//        if(str.startsWith("[")&&str.endsWith("]")){
//            chars=str.substring(1, str.length()-1);
//        }else{
//            return "";
//        }
//        Set<String> expendedChars=getExpendedChars(chars);
//        result+=getOneBesidesRegion(expendedChars,num);
        return result;
    }

    /**
     * 从0-255随机取一个字符，不在chars当中
     * @param chars
     * @param num
     * @return
     */
    private char getOneBesidesRegion(Set<String> chars,int num) {
        char result=0;
        if(chars==null||chars.size()==0){
            int n=(int)(Math.random()*(num));
            result=(char)n;
            return result;
        }
        else if(num<chars.size()){
            num=5*chars.size();
        }
        boolean flag=false;
        int j=0;
        int k=0;
        while(!flag) {
            j++;
            int n = (int) (Math.random() * (num));
            result = (char) n;
            int i=0;
            for (String c : chars) {
                i++;
                if (c.charAt(0)==(result)) {
                    flag=false;
                    break;
                }
            }
            if(i>=chars.size()){
                return result;
            }
            if(j>2*num){//找了控制数量
                k++;
                num=2*num;
                j=0;
            }
            if(k>=10){//找不到满足的字符
                //System.out.println(" timeout can't find a char to match ~"+chars);
                for (String c : chars) {
                    System.out.print(c);
                }
            }
        }
        return result;
    }
    public static void main(String[] args) throws UnsupportedEncodingException {
        Block block1 = new Block("");
        String result=block1.getUnicodeStrByRegex("\\u0000-\\u0009\\u000B\\u000C\\u000E-\\u0026\\u0028-\\u005B\\u005D-\\u007F");
        
    }
    

    /*
    public static void main(String[] args) throws UnsupportedEncodingException {
        Block block1 = new Block("");
        String result1=block1.getMoreThanOneByRegex("[a-zA-Z0-9_\'\\\t\n\r\"\uD800-\uD801]+[a-zA-Z0-9]+",100,true);//[\uD800-\uD801]结果会比较奇怪
//        //System.out.println(result1);

        List<String> result=new ArrayList<>();

        Block block3 = new Block("");
        result.addAll(block1.getMoreThanOne("sfa+ 123+",100));//[\uD800-\uD801]结果会比较奇怪


        result.addAll(block1.getMoreThanZero("sfa* sdas*",100));//[\uD800-\uD801]结果会比较奇怪

        Block block2 = new Block("");
        String result2=block2.getOneFromOr("(t1 | t2 | (t3 | t4) | t5)");

        Block block4 = new Block("");
        result.addAll(block4.getZeroOrOne("sadfahjsk?hshsh?"));


        String result5=block2.getZeroOrOneByRegex("[a-zA-Z0-9_\\\t\r\n\uD800-\uD801]");
//        //System.out.println(result5);

        result.addAll(block2.getZeroOrOne("(t1 | t2 | (t3 | t4) | t5)"));

        String result7=block2.getMoreThanZeroByRegex("[a-zA-Z0-9_\\\t\r\n\uD800-\uD801]",2,true);
//        //System.out.println("getMoreThanZeroByRegex-----"+result7);

        result.addAll(block2.getMoreThanZero("(t1 | t2 | (t3 | t4) | t5)",2));

        String result9=block2.getRevertByRegex("~[a-z]~[A-Z]~[0-9]",255);
//        //System.out.println("getRevertByRegex-----"+result9);

        String result10=block2.getOneFromRegex("[AHDGFDDH][saasdfas][4533453]");
//        //System.out.println(result10);

        Set<String> l=block2.getExpendedChars("[\\u0000-\\u007F]");
//        //System.out.println(l);

    }
    */
}
