package iscas.ac.grand.main.common;
import java.io.File;
import java.util.*;

public class Grammar {
    private String path="";//文法存放的路径
    private String grammarName="";//文法名称
    private Map<String, List<String>> vars=new HashMap<String, List<String>>();//文法中的词法、语法变量，及对应的取值方式（竖线分隔）
    private List<String> orderedList=new ArrayList<>();//文法中所有的变量名，即vars中的key列
    private Map<String, Set<String>> keyTokens=new HashMap<String, Set<String>>();//文法中的变量和变量的取值中出现的tokens的对应关系，即变量和子变量的对应关系
    private Map<String, Set<String>> keyAllTokens=new HashMap<String, Set<String>>();//变量和所有子变量的对应关系（深度递归）
    public static Constraint constraint=new Constraint();//文法的约束
    private Map<String, Token> Tokens=new HashMap<String, Token>();//文法中的变量
    private Map<String,List<Map<String, Value>>> outCycleStrategies=new HashMap<>();//循环变量跳出循环的若干种策略，其中Map<common.Token,common.Value>表示每个变量的一种特定取值方式
    private Map<String, Value> relativeTerminateValue=new HashMap<String, Value>();//只有一种取值方式W的相对终止取值是W，如果有两个，选择相对不容易循环的那一个
    private GrammarInfo grammarInfo;//文法的信息，例如文法的名称，词汇表的名称等
    private List<Token> cycleTokens=new ArrayList<>();//文法中的会产生循环的变量
    private List<Token> cycleAndOrTokens=new ArrayList<>();//文法中的会产生循环的变量，且其取值中包含|竖线


    public Grammar(){

    }

    public Grammar(String path, Map<String, List<String>> vars, List<String> orderedList, Map<String, Set<String>> keyTokens, HashMap<String, Set<String>> keyAllTokens, GrammarInfo grammarInfo) {
        this.path=path;
        this.vars=vars;
        this.orderedList=orderedList;
        this.keyTokens=keyTokens;
        this.keyAllTokens=keyAllTokens;
        this.grammarInfo=grammarInfo;

    }

    public Map<String, Value> getRelativeTerminateValue() {
        return relativeTerminateValue;
    }

    public void setRelativeTerminateValue(Map<String, Value> relativeTerminateValue) {
        this.relativeTerminateValue = relativeTerminateValue;
    }

    public Map<String, List<Map<String, Value>>> getOutCycleStrategies() {
        return outCycleStrategies;
    }
    public List<Map<String, Value>> getOutCycleStrategiesByKey(String key) {
        return outCycleStrategies.get(key);
    }

    public void setOutCycleStrategies(Map<String, List<Map<String, Value>>> outCycleStrategies) {
        this.outCycleStrategies = outCycleStrategies;
    }

    public Constraint getConstraint() {
        return constraint;
    }

    public void setConstraint(Constraint constraint) {
        Grammar.constraint = constraint;
    }

    public void setVars(Map<String, List<String>> vars) {
        this.vars = vars;
    }

    public void setOrderedList(List<String> orderedList) {
        this.orderedList = orderedList;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, List<String>> getVars() {
        return vars;
    }

    public List<String> getOrderedList() {
        return orderedList;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Set<String>> getKeyTokens() {
        return keyTokens;
    }

    public void setKeyTokens(Map<String, Set<String>> keyTokens) {
        this.keyTokens = keyTokens;
    }


    public Map<String, Set<String>> getKeyAllTokens() {
        return keyAllTokens;
    }

    public Set<String> getKeyAllTokensByKey(String node) {
        return keyAllTokens.get(node);
    }

    public void setKeyAllTokens(Map<String, Set<String>> keyAllTokens) {
        this.keyAllTokens = keyAllTokens;
    }

    public Map<String, Token> getTokens() {
        return Tokens;
    }

    public GrammarInfo getGrammarInfo() {
        return grammarInfo;
    }

    public void setGrammarInfo(GrammarInfo grammarInfo) {
        this.grammarInfo = grammarInfo;
    }

    public String getGrammarName() {
        return grammarName;
    }

    public void setGrammarName(String grammarName) {
        this.grammarName = grammarName;
    }

    /**
     * 根据tokenName返回token
     * @return
     */
    public Token getTokenByKey(String key) {
        return Tokens.get(key);
    }

    public void setTokens(Map<String, Token> tokens) {
        Tokens = tokens;
    }

    public List<Token> getCycleTokens() {
        return cycleTokens;
    }

    public void setCycleTokens(List<Token> cycleTokens) {
        this.cycleTokens = cycleTokens;
    }

    public List<Token> getCycleAndOrTokens() {
        return cycleAndOrTokens;
    }

    public void setCycleAndOrTokens(List<Token> cycleAndOrTokens) {
        this.cycleAndOrTokens = cycleAndOrTokens;
    }

    /**
     * 取出key所有子孙中的token(不包括自身)
     * @param node
     * @return
     */
    public Set<String> updateAllTokensByKey(String node) {
        Set<String> result=new HashSet<String>();
        Queue<String> queue=new LinkedList<String>();
        if(orderedList.contains(node)){
            queue.offer(node);
        }
        while(!queue.isEmpty()){
            String ele= queue.poll();
            Set<String> eleTokens=getKeyTokensByKey(ele);
            if(eleTokens!=null&&eleTokens.size()>0){
                for(String token:eleTokens){
                    if(!queue.contains(token)&&!result.contains(token)){
                        queue.offer(token);
                    }
                }
                result.addAll(eleTokens);
            }
        }
        this.keyAllTokens.put(node,result);
        return result;
    }

    /**
     * 根据key取出其下位中的取值方式
     * @param node
     * @return
     */
    public List<String> getValueByKey(String node) {
        if(orderedList.contains(node)){
            return vars.get(node);
        }
        return null;
    }

    /**
     * 取出key的下位中的token
     * @param node
     * @return
     */
    public Set<String> getKeyTokensByKey(String node) {
        if(orderedList.contains(node)){
            return keyTokens.get(node);
        }
        return null;
    }


    /**
     * 取循环步长-1不包含任何子token 0不包含自身 1下位中含有自身 2 孙位中含有自身 n距离n层的位置中含有自身
     * @param node
     * @return
     */
    public int getStepLenByKey(String node) {
        int result=1;
        Queue<String> queue=new LinkedList<String>();
        if(orderedList.contains(node)){
            queue.offer(node);
        }
        List<Integer> countList=new ArrayList();
        List<Integer> countList2=new ArrayList();
        int temp=-1;
        int count=0;
        while(!queue.isEmpty()){
            String ele= queue.poll();
            Set<String> eleTokens=getKeyTokensByKey(ele);
            if(eleTokens!=null&&eleTokens.size()>0){
                if(eleTokens.contains(node)){
//                   result=getStepLen(countList);
                    result++;
                   break;//找到最近的循环位置了
                }else{
                    int j=0;
                    for(String token:eleTokens){
                        if(!queue.contains(token)){
                            queue.offer(token);
                            count++;
                            j++;
                        }
                    }
                    countList.add(j);
                    countList2.add(j);
                    if(temp==-1){
                        temp=countList.get(0);
                        countList.remove(0);
                    }else{
                        temp--;
                        if(temp==0){
                            result++;
                            temp--;
                        }
                    }
                }
            }
        }
        if(result<=0){
            if(count>0){
                result=0;
            }else{
                result=-1;
            }
        }
        return result;
    }
    /**
     * 取出节点的循环取值链（倒序可以根据节点的parentNode找上一级）
     * @param nodeName
     * @return
     */
    public Node updateCycleNodeByKey(String nodeName){
        Node result=new Node(nodeName);
        Queue<String> queue=new LinkedList<>();
        Queue<Node> nodesQueue=new LinkedList<>();
        if(orderedList.contains(nodeName)){
            queue.offer(nodeName);
            nodesQueue.offer(new Node(nodeName));
        }
        boolean flag=false;
        while(!queue.isEmpty()){
            if(flag){
                break;
            }
            String ele= queue.poll();
            Node node=nodesQueue.poll();
            Token t=getTokenByKey(ele);
            List<Value> valueList=t.getValueList();
            for(Value val:valueList){
                Set<String> eleTokens=val.getChildrenToken();
                if(eleTokens!=null&&eleTokens.size()>0){
                    if(eleTokens.contains(nodeName)){
                        result=new Node(nodeName,node,val);
                        //找到最近的循环位置了
                        flag=true;
                        break;
                    }else{
                        for(String token:eleTokens){
                            if(!queue.contains(token)){
                                queue.offer(token);
                                nodesQueue.offer(new Node(token,node,val));
                            }
                        }

                    }
                }
            }
        }
        //lastNode向上查找得到完整的List，然后翻转list
        return result;
    }

    /**
     * 筛选文法中合法的文法，例如把lexer合并到对应的parser文法中，如果一个lexer没有被合并到parser中，暂时先统计，不加入到有效文法集合中
     * @param grammars
     * @return
     */
    public List<Grammar> getValidGrammars(List<Grammar> grammars) {
        List<Grammar> result=new ArrayList<>();

        for(Grammar g:grammars){
            GrammarInfo grammarInfo=g.getGrammarInfo();
            if(grammarInfo!=null){
                String tokenVocab=grammarInfo.getTokenVocab().trim();
//                if(tokenVocab!=""){
                if(tokenVocab!=""&&(grammarInfo.isParser()||grammarInfo.isGrammar())&&!grammarInfo.isLexer()){
                    Grammar lexerGammar=getLexerGrammarByName(grammars,tokenVocab,g.getPath());//词法文件按照名称获取，如果有多个同名的lexer，按照路径筛选正确的那一个
                    if(lexerGammar!=null){
                        Grammar tempGrammar=mergeGrammar(g,lexerGammar);//合并lexer到parser里
                        if(tempGrammar!=null){
                            result.add(tempGrammar);
                        }else{
                            //System.out.println(g.getPath()+": "+" grammar is null after merge ");
                        }
                    }
                }else if(tokenVocab==""&&!grammarInfo.isLexer()){
                    result.add(g);
                }
                else{
                    //System.out.println(g.getPath()+" lexer");
                }
            }else{
                //System.out.println(g.getPath()+": "+" grammar information is null ");
            }
        }
        return result;

    }

    /**
     * 合并两个grammar
     * @param parserGrammar
     * @param lexerGrammar
     * @return
     */
    private Grammar mergeGrammar(Grammar parserGrammar, Grammar lexerGrammar) {
        Grammar result=parserGrammar;
        if(lexerGrammar==null){
            return parserGrammar;
        }
        if(parserGrammar==null){
            return lexerGrammar;
        }
        //合并变量及其取值
        Map<String,List<String>> parserVars=parserGrammar.getVars();
        Map<String,List<String>> lexerVars=lexerGrammar.getVars();
        for(Map.Entry<String,List<String>> entry:lexerVars.entrySet()){
            String key=entry.getKey();
            if(parserVars.get(key)==null){
                parserVars.put(key,entry.getValue());
            }else{
                //System.out.println(" 1 double token in parser and lexer named "+key+" in "+parserGrammar.getPath());
            }
        }
        result.setVars(parserVars);

        //合并变量名称列表
        List<String> parserOrderedList=parserGrammar.getOrderedList();
        List<String> lexererOrderedList=lexerGrammar.getOrderedList();
        if(parserOrderedList!=null&&lexererOrderedList!=null){
            parserOrderedList.addAll(lexererOrderedList);
        }else{
            //System.out.println(" null orderedList in "+parserGrammar.getPath()+" or "+lexerGrammar.getPath());
        }
        result.setOrderedList(parserOrderedList);

        //合并token列表
        Map<String, Set<String>> parserKeyTokens=parserGrammar.getKeyTokens();
        Map<String, Set<String>> lexerKeyTokens=lexerGrammar.getKeyTokens();
        for(Map.Entry<String,Set<String>> entry:lexerKeyTokens.entrySet()){
            String key=entry.getKey();
            if(parserKeyTokens.get(key)==null){
                parserKeyTokens.put(key,entry.getValue());
            }else{
                //System.out.println(" 2 double token in parser and lexer named "+key+" in "+parserGrammar.getPath());
            }
        }
        result.setKeyTokens(parserKeyTokens);

        return result;
    }

    /**
     * 从grammar list当中返回名称为
     * @param grammars
     * @param grammarName
     * @return
     */
    private Grammar getLexerGrammarByName(List<Grammar> grammars, String grammarName,String path) {
        Grammar result=null;
        List<Grammar> tempList=new ArrayList<>();
        for(Grammar gramm : grammars) {
            GrammarInfo grammarInfo=gramm.getGrammarInfo();
            if(grammarInfo!=null){
                if(grammarInfo.isLexer()){
                    if(grammarInfo.getGrammarName().equals(grammarName)){
                        if(isSameDirectory(path,gramm.getPath())){//lexer和parser是否在同一目录下
                            tempList.add(gramm);
                        }
                    }
                }
            }
        }
        if(tempList.size()>0){
            if(tempList.size()==1){
                result=tempList.get(0);
            }else{
                //System.out.println("more than one lexer "+grammarName+" in this path: "+path);
            }
        }
        return result;
    }

    /**
     * 判断两个文件是不是在同一个路径下
     * @param path2
     * @param path1
     * @return
     */
    private boolean isSameDirectory(String path1, String path2) {
        boolean result=false;
        path1=path1.substring(0,path1.lastIndexOf(File.separator)) ;
        path2=path2.substring(0,path2.lastIndexOf(File.separator)) ;
        if(path1.equals(path2)){
            result=true;
        }
        return result;
    }

    /**
     * 采用随机的方式选出list中的一个Value值返回
     * @param list
     * @return
     */
    public Value getValueByRandom(List<Value> list) {
        Value result=new Value("");
        if(list==null||list.size()==0){
            //System.out.println(" no root to generate program ");
            return result;
        }
        int length=list.size();
        if(length==1){
            result=list.get(0);
        }else{
            int random=(int)(Math.random()*length);
            result=list.get(random);
        }
        return result;
    }
}
