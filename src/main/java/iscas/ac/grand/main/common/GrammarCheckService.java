package iscas.ac.grand.main.common;
import iscas.ac.grand.main.common.Grammar;

import java.util.*;

public class GrammarCheckService {

    /**
     * 从grammar的keyTokens中删除那些不是token的项，这些项是从字符串中的字母识别出来的
     * @param grammar
     * @return
     */
    public Grammar checkKeyTokens(Grammar grammar) {
        Grammar result=new Grammar();
        if(grammar==null)return result;
        result.setPath(grammar.getPath());
        result.setVars(grammar.getVars());
        result.setOrderedList(grammar.getOrderedList());
        result.setConstraint(grammar.getConstraint());
        result.setGrammarInfo(grammar.getGrammarInfo());

        //更新一个key包含的下一级toekn(只考虑下位一层)
        List<String> keys=grammar.getOrderedList();
        Map<String, Set<String>> keyTokens=grammar.getKeyTokens();
        for(Map.Entry<String,Set<String>> keytokens:keyTokens.entrySet()){
            Set<String> values=keytokens.getValue();
            if(values!=null&&values.size()>0){
                values.removeIf(val -> !keys.contains(val));
            }
        }
        result.setKeyTokens(keyTokens);

        StringTools st=new StringTools();

        //更新一个key包含的所有toekn(考虑逐层遍历)
        Map<String, Token> tokens=new HashMap<String, Token>();
        for(String key:keys){
            if(key.equalsIgnoreCase("variableInitializer")){
                //System.out.println("");
            }
            Token token=new Token(key);
            Set<String> allTokens=grammar.getKeyAllTokensByKey(key);
            if(allTokens==null||allTokens.size()==0){
                allTokens=grammar.updateAllTokensByKey(key);
            }
            if(allTokens==null||allTokens.size()==0){
                //此变量无子token
                token.setTokenType("4");
                token.setCycleStep(-1);
            }
            else{
                Set<String> nearTokens=grammar.getKeyTokensByKey(key);
                if(nearTokens==null||nearTokens.size()==0){
                    //此变量无子token
                    token.setTokenType("4");
                    token.setCycleStep(-1);
                }else{
                    if(nearTokens.contains(key)){
                        //下位token中含有当前token 生成过程中有循环
                        token.setTokenType("2");
                        token.setCycleStep(1);
                    }else if(allTokens.contains(key)){
                        //夸层级循环
                        token.setTokenType("3");
                        if(key=="annotation"){
                            //System.out.println("");
                        }
                        token.setCycleStep(grammar.getStepLenByKey(key));
                    }else{
                        //无循环
                        token.setTokenType("1");
                        token.setCycleStep(0);
                    }
                }
            }

            //更新一个key包含的所有toekn和value，以对象的形式，比如token的类型（递归，非递归）等，比如value的类型，token,正则表达式，字符串等
            List<String> valueListStr=grammar.getValueByKey(key);//key的所有取值方式，竖线分割
            List<Value> valueList=new ArrayList<>();//key的所有取值方式
            if(valueListStr!=null) {
                for (String val : valueListStr) {
                    Value value = new Value(val);
                    Set<String> nearTokens = grammar.getKeyTokensByKey(key);
                    Set<String> valtoken = st.getWordsFromStr(val);
                    if (valtoken == null || valtoken.size() == 0) {
                        //无子token 正则表达式或者字符串''
                        if (val.startsWith("'") && val.endsWith("'")) {
                            //字符串
                            value.setValueType("5");
                        } else {
                            //正则表达式
                            value.setValueType("4");
                        }
                    } else {
                        valtoken.removeIf(str -> !nearTokens.contains(str));
                        if (valtoken.size() == 1) {
                            //可能只包含一个token
                            if (valtoken.contains(val)) {
                                //只包含一个token,没有其它符号
                                value.setValueType("1");
                            } else {
                                //除token之外,还有其它符号
                                value.setValueType("2");
                            }
                        } else {
                            //至少包含两个token
                            value.setValueType("3");
                        }
                    }
                    value.setChildrenToken(valtoken);
                    valueList.add(value);
                }
            }

            token.setValueList(valueList);
            tokens.put(key,token);
        }
        result.setTokens(tokens);
        result.setKeyAllTokens(grammar.getKeyAllTokens());

        return result;
    }

    /**
     * 更新跨层级循环中循环链上的取值
     * @param grammar
     * @return
     */
    public Grammar updateCycleMap(Grammar grammar) {
        Grammar result=new Grammar();
        if(grammar==null)return result;
        List<String> keys=grammar.getOrderedList();
        //更新跨层循环的路径
        for(String key:keys){
            Token token=grammar.getTokenByKey(key);
            if(token.getTokenType()==("3")||token.getTokenType()==("2")){
                Node n=grammar.updateCycleNodeByKey(key);
                token.setCycleNode(n);
                Node node=grammar.updateCycleNodeByKey(key);
                //System.out.println(key+":    "+token.getCycleStep()+"  ");
                int i=0;
                while(node!=null){
                    Value val=node.getValue();
                    if(val!=null){
                        i++;
//                        System.out.print(i+": "+val.getValue()+"    ");
                    }
                    node=node.getParent();
                }
                token.setCycleStep(i);
                //System.out.println("");
                //System.out.println(key+":    "+token.getCycleStep()+"  ");

                Map<String, Token> tokens=grammar.getTokens();
                tokens.put(token.getTokenName(),token);
                grammar.setTokens(tokens);
            }
        }
        return grammar;
    }

    /**
     * 更新跨层循环中跳出循环的取值策略，一个循环变量可能有多种跳出循环的方式
     * @param grammar
     * @return
     */
    public Grammar updateOutCycleStrategyMap(Grammar grammar) {
        Grammar result=new Grammar();
        if(grammar==null)return result;
        List<String> keys=grammar.getOrderedList();
        //更新跨层循环的路径
        for(String key:keys){
            Token token=grammar.getTokenByKey(key);
            if(key.equals("elementValue")){
//                //System.out.println("");
            }
            if(key.equals("annotation")){
//                //System.out.println("");
            }
            if(key.equals("typeArguments")){
//                //System.out.println("");
            }
            if(key.equals("classOrInterfaceType")){
//                //System.out.println("");
            }
            if(key.equals("classBodyDeclaration")){
//                //System.out.println("");
            }

            boolean flag=false;
            if(token.getTokenType()==("3")){
                Node node=token.getCycleNode();
                System.out.println(key+":    "+token.getCycleStep()+"  ");
                List<Map<String, Value>> outCycleStrategies=new ArrayList<Map<String, Value>>();
                int i=0;
                while(node!=null){
                    if(flag){
                        break;
                    }

                    String nodeName=node.getNodeName();
                    //如果已经计算过，直接取值
                    List<Map<String, Value>> existsValueMapList=grammar.getOutCycleStrategiesByKey(nodeName);
                    if(existsValueMapList!=null&&existsValueMapList.size()>0){
                        Node tempNode=node.parent;
                        Node tempChildNode=node;
                        while(tempNode!=null){
                            existsValueMapList.get(0).put(tempNode.getNodeName(),tempChildNode.getValue());
                            tempChildNode=tempNode;
                            tempNode=tempNode.parent;
                        }
                        outCycleStrategies.add(existsValueMapList.get(0));
                        break;
                    }


                    Token innerToken=grammar.getTokenByKey(nodeName);

                    List<Value> innerTokenValues=innerToken.getValueList();
                    for(Value v:innerTokenValues) {//对于循环路径上节点的每一个Value v,判断是否为终止值，是的话，把路径添加后，跳出循环。
                        if(v.getValueType()=="5"||v.getValueType()=="4") {//对于终止取值，一定可以跳出循环
                            flag=true;
                            Map<String, Value> outCycleStrategy=new HashMap<>();
                            outCycleStrategy.put(nodeName,v);

                            Node tempNode=node.parent;
                            Node tempChildNode=node;
                            while(tempNode!=null){
                                outCycleStrategy.put(tempNode.getNodeName(),tempChildNode.getValue());
                                tempChildNode=tempNode;
                                tempNode=tempNode.parent;
                            }
                            outCycleStrategies.add(outCycleStrategy);
                            break;
                        }
                    }
                    if(!flag){
                        for(Value v:innerTokenValues){//对于循环路径上节点的每一个Value v
                            //一个取值方式至少要找到一个终止的出口
                            Map<String, Value> tempmap=getStrategies(grammar,nodeName,v,key);
                            if(tempmap!=null&&tempmap.size()>0){
                                Node tempNode=node.parent;
                                Node tempChildNode=node;
                                while(tempNode!=null){
                                    tempmap.put(tempNode.getNodeName(),tempChildNode.getValue());
                                    tempChildNode=tempNode;
                                    tempNode=tempNode.parent;
                                }
                                outCycleStrategies.add(tempmap);
                                flag=true;
                                break;
                            }else{
                                System.out.println("cant find a out cycle path for node :"+nodeName+" value " +v.getValue());
                            }
//                        Set<String> tvt=v.getChildrenToken();//node所对应token的某一种取值当中出现的token列表
//                        int index=0;
//                        for(String t:tvt){//对于某一个节点的某个取值中出现的每一个token
//                            common.Token tToken=grammar.getTokenByKey(t);
//                            if(tToken.getTokenType()=="1"||tToken.getTokenType()=="2"||tToken.getTokenType()=="4"){
//                                index++;
//                                continue;
//                            }
//                            Set<String> allChildrenToken=grammar.getKeyAllTokensByKey(t);//取出所有的后代token
//                            if(allChildrenToken.contains(key)){//判断后代token中有没有出现循环变量key
//                                //包含择说明这不是一个可以跳出循环的取值方式
//                                break;
//                            }
//                            index++;
//                        }
//                        if(tvt!=null&&tvt.size()==index){//说明对于某一个节点node的某个取值v中出现的每一个token t,其后代中都不存在循环变量key,说明节点node的取值Value v是一个可以跳出循环的取值方式
//                            Map<String,common.Value> outCycleStrategy=new HashMap<>();
//                            outCycleStrategy.put(nodeName,v);
//                            outCycleStrategies.add(outCycleStrategy);
//                        }
                        }
                    }else{
                        break;
                    }
                    node=node.getParent();
                }
                if(outCycleStrategies==null||outCycleStrategies.size()==0){
                    System.out.println("cant find a path for ++++++++: " +key);
                }
                System.out.println(key+":    "+token.getCycleStep()+"  ");
                System.out.println("out path maps:");
                i=1;
                for(Map<String, Value> m:outCycleStrategies){
                    System.out.print("out path"+i+" : ");
                    for(Map.Entry<String, Value> entry:m.entrySet()){
                        System.out.println(entry.getKey()+" : "+entry.getValue().getValue()+" ");
                    }
                    i++;
                }
                Map<String,List<Map<String, Value>>> tokenOutCycleStrategies=grammar.getOutCycleStrategies();
                tokenOutCycleStrategies.put(token.getTokenName(),outCycleStrategies);
                grammar.setOutCycleStrategies(tokenOutCycleStrategies);
            }
        }
        return grammar;
    }
    /**
     * 从当前的取值value开始，寻找一条取值路径，最终到达终止值
     * @param startNodeName
     * @param value
     * @param key
     * @return
     */
    public Map<String, Value> getStrategies(Grammar grammar, String startNodeName, Value value, String key) {
        Map<String, Value> result=new HashMap<>();
        if(startNodeName==null||startNodeName==""||value==null||key==null||key==""){
            return null;
        }
        result.put(startNodeName,value);
        boolean flag=true;
        int index=0;
        Value tempVal=value;
        Queue<String> tokenQueue=new LinkedList<>();
        Set<String> childrenTokens=tempVal.getChildrenToken();//某一种取值tempVal当中出现的token列表
        for(String t:childrenTokens) {
            tokenQueue.offer(t);
        }
        Map<String, Value> mediaResult=new HashMap<>();
        while(flag&&index< ConfigureInfo.getTerminateMaxFindLen()&&!tokenQueue.isEmpty()){
            String tokenName=tokenQueue.poll();
            if(result.get(tokenName)!=null){
                continue;
            }
            Token token=grammar.getTokenByKey(tokenName);
            tempVal=getTerminateValue(tokenName,grammar,token);
            if(tempVal.getValueType().equals("4")||tempVal.getValueType().equals("5")){
                result.put(tokenName,tempVal);//对于tokenName已经找到了终止子值
                continue;
            }
            mediaResult.put(tokenName,tempVal);//对于tokenName未找到终止子值

            childrenTokens=tempVal.getChildrenToken();//某一种取值tempVal当中出现的token列表
            for(String childToken:childrenTokens){//对于某个取值中出现的每一个token
                if(!tokenQueue.contains(childToken)) {
                    tokenQueue.offer(childToken);
                }
//                common.Token tToken=grammar.getTokenByKey(childToken);
//                if(tToken.getTokenType()=="4"){
//                    result.put(childToken,tToken.getValueList().get(0));
//                }
            }
            index++;
        }
        for(Map.Entry<String, Value> entry:mediaResult.entrySet()){
            if(result.get(entry.getKey())==null){
                result.put(entry.getKey(),entry.getValue());
            }
        }
        if(index>= ConfigureInfo.getTerminateMaxFindLen()){
            return null;
        }
        return result;
    }

    /**\
     * 更新各个变量的跳出循环取值，当有多种方式时，保留长度最短的一个
     * @param grammar
     * @return
     */
    public Grammar updateOutCycleValue(Grammar grammar) {
        Grammar result=new Grammar();
        if(grammar==null)return result;
        List<String> keys=grammar.getOrderedList();
        int len=grammar.getOrderedList().size();
        List<Token> cycleTokens=new ArrayList<>();//文法中的会产生循环的变量
        List<Token> cycleAndOrTokens=new ArrayList<>();//文法中的会产生循环的变量，且其取值中包含|竖线
        int i=0;
        Grammar g=new Grammar();
        if(ConfigureInfo.ifLog) {System.out.println("=============================终结符============================start");}
        for(String key:keys) {//先更新不含任何子token的token的跳出Value值,有多个时随机选一个，跳出长度设置为1
            Value tempValue;
            Token token = grammar.getTokenByKey(key);
            
            if (token.getTokenType().equals("4") ) {
            	
                tempValue=g.getValueByRandom(token.getValueList());
                if(ConfigureInfo.ifLog) {System.out.println("token "+key+" : "+tempValue.getValue());}
                tempValue.setTerminateLenth(0);
                token.setOutCycleValue(tempValue);
                token.setOutCycleValueLen(1);
                Map<String, Token> tokens=grammar.getTokens();
                tokens.put(token.getTokenName(),token);
                grammar.setTokens(tokens);
                i++;
            }
        }
        if(ConfigureInfo.ifLog) {
            System.out.println("=============================终结符============================end");
        }
        
        int maxCanUpdate=0;
        while(i<len){
            for(String key:keys) {
                Value tempValue;
                List<Value> tempVals = new ArrayList<>();
                Token token = grammar.getTokenByKey(key);
                if(token.getOutCycleValue()==null){
                    tempVals=getTerminateValues(grammar,token);//取出取值中所有token的跳出Value值已经计算出来的那些取值
                    if(tempVals!=null&&tempVals.size()>0){//至少有一个Value是已知的
//                    tempValue=g.getValueByRandom(tempVals);//这里是随机取了一个已经更新过子token的子值
                        tempValue=getValueByShortestRandom(tempVals);//这里是取了一个已经更新过子token的,跳出长度最短的子值
                        token.setOutCycleValue(tempValue);
                        token.setOutCycleValueLen(tempValue.getTerminateLenth()+1);
                        Map<String, Token> tokens=grammar.getTokens();
                        tokens.put(token.getTokenName(),token);
                        grammar.setTokens(tokens);
                        i++;
                    }
                }
            }
            if(maxCanUpdate==i){
                int j=0;
                for(String key:keys) {
                    Token token = grammar.getTokenByKey(key);
                    if (token.getOutCycleValue() == null) {
                        cycleTokens.add(token);//记录因包含竖线|取值产生循环的非终结符
                        System.out.println("token "+(j++)+" : "+key);
                        List<Value> tempVals=token.getValueList();
                        int k=0;
                        for(Value v:tempVals) {
                            if(v.getValue().contains("|")){
                                cycleAndOrTokens.add(token);//记录因包含竖线|取值产生循环，需要展开的值
                            }
                            System.out.println("value "+(k++)+" : "+v.getValue());
                        }
                    }
                }
                ConfigureInfo.setNeedsplitNum(ConfigureInfo.getNeedsplitNum()+1);
                break;
            }
            maxCanUpdate=i;
        }
        //2024.12.11 对于只包含一项value的token 指定outcylevalue为该值



        if(ConfigureInfo.ifLog) {
            System.out.println("=============================循环跳出异常============================end");
            printNoOutPathTokens(keys, grammar);
            System.out.println("=============================循环跳出异常============================end");
        }
        grammar.setCycleTokens(cycleTokens);
        grammar.setCycleAndOrTokens(cycleAndOrTokens);
        return grammar;
    }

    /**
     * qurong
     * 2024.4.26
     * 打印所有没有跳出出口的token
     * @param keys
     * @param grammar
     */
    private void printNoOutPathTokens(List<String> keys, Grammar grammar) {
		// TODO Auto-generated method stub
    	int i=1;
    	for(String key:keys) {
            Value tempValue;
            List<Value> tempVals = new ArrayList<>();
            Token token = grammar.getTokenByKey(key);
            if(token.getOutCycleValue()==null){
            	System.out.println(i+": token :"+key);
            	i++;
            }
        }
	}
    
    /**
     * qurong
     * 2024.4.26
     * 打印所有有跳出出口的token
     * @param grammar
     */
    public void printOutPathTokens( Grammar grammar) {
		// TODO Auto-generated method stub
    	System.out.println("=============================各个token跳出循环的value============================start");
    	int i=1;
    	List<String> keys=grammar.getOrderedList();
    	for(String key:keys) {
            Value tempValue;
            List<Value> tempVals = new ArrayList<>();
            Token token = grammar.getTokenByKey(key);
            if(token.getOutCycleValue()!=null){
            	System.out.println(i+": token :"+key);
            	Value value=token.getOutCycleValue();
            	System.out.println("out value:"+value.getValue());
            	i++;
            }
        }
    	System.out.println("=============================各个token跳出循环的value============================end");
	}

	/**
     * 从token的各个子Value中筛选出那些终止子值Value，或者token都已经更新过跳出值的Value
     * @param token
     * @return
     */
    public List<Value> getTerminateValues(Grammar grammar, Token token) {
        List<Value> result=new ArrayList<>();
        List<Value> vals=token.getValueList();
        Map<String, Token> tokens=grammar.getTokens();
        for(Value val:vals){
            if(val.getValueType().equals("4")||val.getValueType().equals("5")){
                val.setTerminateLenth(0);
                result.add(val);
//            }else if(val.getValueType().equals("1")||val.getValueType().equals("2")){//只含有一个子token
            }else if(val.getValueType().equals("3")||val.getValueType().equals("1")||val.getValueType().equals("2")){//含有子token
                boolean isUpdated=true;
                Set<String> childTokens=val.getChildrenToken();
                int maxLen=0;
                for(String childToken:childTokens){
                    if(tokens.get(childToken).getOutCycleValue()!=null&&tokens.get(childToken).getOutCycleValueLen()>0){
                        if(maxLen<tokens.get(childToken).getOutCycleValueLen()){
                            maxLen=tokens.get(childToken).getOutCycleValueLen();
                        }
                    }else{
                        isUpdated=false;
                        break;
                    }
                }
                if(isUpdated&&childTokens!=null&&childTokens.size()>0){
                    val.setTerminateLenth(maxLen);
                    result.add(val);
                }else{
                    //System.out.println("this postion467");
                }
            }else{
                //System.out.println("cant process this value: "+val.getValue());
            }

        }
        return result;
    }

    /**
     * 选出list中的一个Value值,其跳出长度是最短的
     * @param list
     * @return
     */
    public Value getValueByShortestRandom(List<Value> list) {
        Value result=new Value("");
        if(list==null||list.size()==0){
            //System.out.println(" no root to generate program ");
            return result;
        }
        int length=list.size();
        if(length==1){
            result=list.get(0);
        }else{
            List<Value> equalLenValues = new ArrayList<>();
            int min=Integer.MAX_VALUE;
            for(Value val:list){
                if(val.getTerminateLenth()<min){
                    equalLenValues = new ArrayList<>();
                    equalLenValues.add(val);
                    min=val.getTerminateLenth();
                }else if(val.getTerminateLenth()==min){
                    equalLenValues.add(val);
                }
            }
            length=equalLenValues.size();
            int random=(int)(Math.random()*length);
            result=list.get(random);
        }
        return result;
    }
    /**
     * 从token的终结子值中随机选出一个作为待生成的文法
     * @param str
     * @param grammar
     * @param token
     * @return
     */
    public Value getTerminateValue(String str, Grammar grammar, Token token) {
        Value result=new Value("");
        if(str==null||str==""){
            return result;
        }
        str=str.trim();
        if(str==""){
            return result;
        }
        if(str.equals("typeArguments")){
//            //System.out.println("");
        }if(str.equals("elementValue")){
//            //System.out.println("");
        }
        Value v=grammar.getRelativeTerminateValue().get(str);
        if(v!=null){
            return v;
        }
        List<Value> values=token.getValueList();
        if(values==null||values.size()==0){
            return null;
        }
        if(values.size()==1){
            result= values.get(0);
        }else{
            List<Value> terminateValues=new ArrayList<>();
            if(token.getTokenType()=="2"){//下位层循环
                for(Value val:values){
                    if(!val.getChildrenToken().contains(str)){
                        terminateValues.add(val);
                    }
                }
            }else if(token.getTokenType()=="3"){//跨层循环
                for(Value val:values){
                    Set<String> childrenToken=val.getChildrenToken();//子一级Child
                    if(!childrenToken.contains(str)){
                        boolean valid=true;
                        for(String child:childrenToken){
                            Set<String> allChildrenToken=grammar.getKeyAllTokensByKey(child);
                            if(allChildrenToken.contains(str)){
                                valid=false;
                                break;
                            }
                        }
                        if(valid){
                            terminateValues.add(val);
                        }
                    }
                }
                if(terminateValues.size()==0){//

                }
            }
            if(terminateValues.size()>0){
                if(terminateValues.size()==1){
                    result=terminateValues.get(0);
                }else{
                    int n=(int)(Math.random()*terminateValues.size());
                    result=terminateValues.get(n);
                }
            }else{
            //System.out.println(" no terminate child value for :"+str);
            for(Value val:values){
                //System.out.println(val.getValue());
            }
                //按照优先级选
                for(Value value:values){//优先选终结子值
                    if(value.getValueType().equals("4")||value.getValueType().equals("5")){
                        result= value;
                        break;
                    }
                }

                int count=0;
                for(Value value:values){//优先选子变量取值皆为终止子值的取值方式
                    count=0;
                    Set<String> tokens=value.getChildrenToken();
                    for(String tokenName:tokens){
                        Token t=grammar.getTokenByKey(tokenName);
                        if(t.getTokenType().equals("4")){
                            count++;
                        }
                    }
                    if(count>=tokens.size()){
                        result= value;
                        break;
                    }
                }

                for(Value value:values){//优先选子变量取值皆为终止子值或者非循环子变量的取值方式
                    count=0;
                    Set<String> tokens=value.getChildrenToken();
                    for(String tokenName:tokens){
                        Token t=grammar.getTokenByKey(tokenName);
                        if(t.getTokenType().equals("4")||t.getTokenType().equals("1")){
                            count++;
                        }
                    }
                    if(count>=tokens.size()){
                        result= value;
                        break;
                    }
                }

                for(Value value:values){//优先选子变量取值皆为终止子值或者非循环子变量或单变量循环变量的取值方式
                    count=0;
                    Set<String> tokens=value.getChildrenToken();
                    for(String tokenName:tokens){
                        Token t=grammar.getTokenByKey(tokenName);
                        if(t.getTokenType().equals("4")||t.getTokenType().equals("1")||t.getTokenType().equals("2")){
                            count++;
                        }
                    }
                    if(count>=tokens.size()){
                        result= value;
                        break;
                    }
                }
            }
        }
        Grammar g=new Grammar();
        if(result.getValue().equals("")){
            result=g.getValueByRandom(values);
        }
        Map<String, Value> relativeTerminateValue=grammar.getRelativeTerminateValue();
        relativeTerminateValue.put(str,result);
        grammar.setRelativeTerminateValue(relativeTerminateValue);
        return result;
    }

    /**
     * 更新会产生无法跳出的循环的变量的取值，将其值中的竖线进行分割
     * @param grammar
     * @return
     */

    public Grammar updateSplitCycleOr(Grammar grammar) {
        Grammar result=new Grammar();
        if(grammar==null)return result;
        List<Token> cycleIncludeOrTokens=grammar.getCycleAndOrTokens();//取出取值包含竖线且取不到终止值的非终结符
        List<String> cycleKey=new ArrayList<>();//循环非终结符的名称列表
        for(Token token:cycleIncludeOrTokens) {
            String key = token.getTokenName();//非终结符的名称
            cycleKey.add(key);
        }
        Map<String, List<String>> vars=grammar.getVars();//文法中的词法、语法变量，及对应的取值方式（竖线分隔）
        SplitValue sv=new SplitValue();
        for(Token token:cycleIncludeOrTokens){//在无法终止的循环非终结符列表中，需要先拆分再更新取值列表vars
            String key=token.getTokenName();//非终结符的名称
            List<Value> vals=token.getValueList();//非终结符的各个取值
            List<String> tempVals=new ArrayList<>();//新的非终结符的取值列表
            for(Value val:vals){
                String tempval=val.getValue();
                if(tempval.contains("|")){
                    List<String> tempList=new ArrayList<>();
                    tempList.add(tempval);
                    tempList=sv.splitValueByOr(tempList);
                    tempVals.addAll(tempList);
                }else{
                    tempVals.add(tempval);
                }
            }
            vars.put(key,tempVals);//更新非终结符key对应的取值列表
            
        }
        grammar.setVars(vars);

        return grammar;
    }
    /**
     * qurong
     * 2024.4.26
     * 输出所有的循环token
     * @param grammar
     */

	public void printCycleTokens(Grammar grammar) {
		// TODO Auto-generated method stub
		System.out.println("=============================各个循环token============================start");
    	int i=1;
    	List<String> keys=grammar.getOrderedList();
    	for(String key:keys) {
            Value tempValue;
            List<Value> tempVals = new ArrayList<>();
            Token token = grammar.getTokenByKey(key);
            if(token.getTokenType()=="2"||token.getTokenType()=="3"){//是单变量循环或者多变量循环
            	System.out.println(i+": token :"+key);
            	Value value=token.getOutCycleValue();
            	System.out.println("out value:"+value.getValue());
            	i++;
            }
        }
    	System.out.println("=============================各个循环token============================end");
	}
}
