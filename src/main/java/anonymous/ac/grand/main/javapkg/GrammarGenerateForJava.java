package anonymous.ac.grand.main.javapkg;

import anonymous.ac.grand.main.c.CConfigureInfo;
import anonymous.ac.grand.main.common.*;
import anonymous.ac.grand.main.python.PythonConfigureInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GrammarGenerateForJava {

    GrammarGenerateService grammarGenerateService=new GrammarGenerateService();
    List<Grammar> grammars=new ArrayList<>();

    public GrammarGenerateForJava(List<Grammar> grammars) {
        this.grammars = grammars;
        grammarGenerateService=new GrammarGenerateService(grammars);
    }

    public GrammarGenerateForJava() {
    }
    /**
     * 除数不能为零
     * @param str
     * @return
     */
    public String zeroExclideJava(String str) {
        str="(("+str+"==0)?"+"1:("+str+"))";
        return str;
    }

    /**
     * anonymousAuthor
     * 2022-12-7
     * @param scopeNode
     * @return
     */
    public String getTabProfixByDepth(ScopeTree scopeNode) {
        String result="";
        if(scopeNode==null){
            return result;
        }
        int depth=scopeNode.getDepth();
        int i=0;
        while(i<depth){
            i++;
            result+="\t";
        }
        return result;
    }

    /**
     *anonymousAuthor
     * 2022-11-14
     * 生成java中的特殊部分如声明变量、方法、类型等
     * @param str
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    public String generateSpecialPartForJava(String str, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode, int lineOrder) {
        String result="";
        //方法返回值相关
        if(ConfigureInfo.ifJavaCode&&str!=""&&(str.trim().equalsIgnoreCase(JavaConfigureInfo.methodDeclaration))){
            result=generateMethodDeclaration(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }

        //类声明相关
        if(ConfigureInfo.ifJavaCode&&str!=""&&(str.trim().equalsIgnoreCase(JavaConfigureInfo.classDeclaration))){
            result=generateClassDeclaration(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }
        //public类声明相关
        if(ConfigureInfo.ifJavaCode&&str!=""&&(str.trim().equalsIgnoreCase(JavaConfigureInfo.publicClassDeclaration))){
            result=generatePublicClassDeclaration(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }

        //main方法相关
        if(ConfigureInfo.ifJavaCode&&str!=""&&(str.trim().equalsIgnoreCase(JavaConfigureInfo.mainContent))){
            result=generateMain(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }

        //引用标识符相关变量
        //是否是Java声明token,如果是，要更新直接包含的Identifier到符号表
        if(ConfigureInfo.ifJavaCode&&str!=""&&(str.trim().equalsIgnoreCase(JavaConfigureInfo.fieldDeclaration)||str.trim().equalsIgnoreCase(JavaConfigureInfo.localDeclaration))){//不区分大小写，如果是声明变量的文法串
            //声明文法特殊处理，记录类型，标识符，作用域等信息
            result=generateDeclaration(str,st,scopeNode,index,ifNeedOutCycle,tokenAppearTimes,lineOrder);
            return result;
        }else if(ConfigureInfo.ifJavaCode&&str!=""&&(JavaConfigureInfo.getVarnames().contains(str.trim()))){
            result=generateIdentifierByType(str, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }
        return result;
    }

    /**
     * 2022.10.24
     * anonymousAuthor
     * @param str
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    private String generateMethodDeclaration(String str, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        String result="";
        scopeNode=grammarGenerateService.updateScope(st,scopeNode);//方法定义也开启一个作用域，这是为了把方法的参数也能包含进作用域
        String[] items=str.split(" ");//认为括号后面要么跟+？* 要么跟空格  typeTypeOrVoid identifier formalParameters methodBody
        String temResult="";
        Type type=new Type();
        String identifier="";
        List<Type> parameterList=new ArrayList<>();
        List<SymbolRecord> parameterIdentifiers=new ArrayList<>();
        Type locateType=new Type();
        Constraint constraint=new Constraint();
        for(String item:items) {
            temResult = grammarGenerateService.generateProgramForMatchedValue(item, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            temResult = constraint.checkUniqueConstraint(temResult, item, index, grammars);//唯一性约束（*不可重复相同值）
            temResult = constraint.checkContinueConstraint(temResult, item, index, grammars);//连续性约束，，如果在约束列表中，temResult中不能有空格
            if (item.equals("typeTypeOrVoid")) {
                type =st.getTypeMap().get(temResult);
                if(type==null){
//                    //System.out.println("what is this" + temResult);
                }
                scopeNode.setType(type);//当前作用域内返回return语句需要返回的表达式的类型
            } else if (item.equals("identifier")) {
                identifier=grammarGenerateService.getIdentifier(index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
                temResult=identifier;
            }else if(item.equals("formalParameters")){
                //可以用逗号拆分开来，把各个参数类型提取出来，保存在parameterList当中
                if(temResult.trim().equals("")){
                    //参数列表为空
                }else{
                    String parameters= StringTools.deleteBrackets("(",")",temResult);
                    String[] parameterArr = parameters.split(",");//用逗号拆分参数列表
                    for(int j=0;j<parameterArr.length;j++){
                        String param=parameterArr[j].trim();
                        String[] paraInfo=param.split(" ");//用空格来分割类型和标识符
                        if(paraInfo.length<2){//一个参数 但是类型和标识符有问题
//                            //System.out.println("paraInfo.length less than 2: "+paraInfo.length);
                        }else if(paraInfo.length==2){//一个参数 类型和标识符匹配
                            String type1name=paraInfo[0].trim();
                            String ident1=paraInfo[1].trim();
                            Type type1=st.getTypeMap().get(type1name);
                            parameterList.add(type1);
                            SymbolRecord sr1=new SymbolRecord(ident1,type1,ident1,"var",scopeNode,"");
                            parameterIdentifiers.add(sr1);
                            grammarGenerateService.updateSymTableByVar(st,sr1);//参数列表中的形参也是函数体的作用域内可以使用的变量，更新到符号表
                        }else{//一个参数 但是类型和标识符有问题
//                            //System.out.println("paraInfo.length more than 2: "+paraInfo.length);
                        }
                    }
                }

            }else if(item.equals("methodBody")){
                //暂时没想到是否需要特殊处理
                //return 方法返回值的添加
                Type returnType=scopeNode.getType();
                temResult=addReturn(temResult,returnType,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,lineOrder);
            }
            result += " " + temResult;
        }
        //locateType方法所在类的类型
        locateType=scopeNode.getLocateType();
        FunctionSymbolRecord funSymbolRecord=new FunctionSymbolRecord(identifier,type,identifier,"function",scopeNode,locateType,parameterList,parameterIdentifiers);

        //更新函数所在类的函数列表
        List<FunctionSymbolRecord> functions=locateType.getFunctions();//类中的函数列表
        functions.add(funSymbolRecord);
        locateType.setFunctions(functions);
        scopeNode.setLocateType(locateType);

        grammarGenerateService.updateSymTableByFun(st,funSymbolRecord);//使用新声明的函数更新符号表模块
        scopeNode=scopeNode.getFather();//把作用域回溯到方法的声明之外

        return result;
    }

    /**
     * anonymousAuthor
     * 2022-11-14
     * 生成java中的特殊项如声明变量、方法、类型等
     * @param item
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    public String generateSpecialItemForJava(String item, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        String result="";
        Constraint constraint=new Constraint();
        if(ConfigureInfo.ifJavaCode&&item!=""&&(JavaConfigureInfo.getVarnames().contains(item))){//引用变量标识符
            String temResult=generateIdentifierByType(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            result =temResult;

        }
        //main方法相关
        else if(ConfigureInfo.ifJavaCode&&item!=""&&(item.trim().equalsIgnoreCase(JavaConfigureInfo.mainContent))){
            result=generateMain(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }
        //类声明相关
        else if(ConfigureInfo.ifJavaCode&&item!=""&&(item.trim().equalsIgnoreCase(JavaConfigureInfo.classDeclaration))){
            result=generateClassDeclaration(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }
        //public类声明相关
        else if(ConfigureInfo.ifJavaCode&&item!=""&&(item.trim().equalsIgnoreCase(JavaConfigureInfo.publicClassDeclaration))){
            result=generatePublicClassDeclaration(item, index,tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            return result;
        }
        else if(ConfigureInfo.ifJavaCode&&item!=""&&(item.trim().equalsIgnoreCase(JavaConfigureInfo.fieldDeclaration)||item.trim().equalsIgnoreCase(JavaConfigureInfo.localDeclaration))){//声明文法特殊处理，记录类型，标识符，作用域等信息
            String temResult=generateDeclaration(item,st,scopeNode,index,ifNeedOutCycle,tokenAppearTimes,lineOrder);
            result += " " + temResult;
        }else if(ConfigureInfo.ifJavaCode&&item!=""&&(item.trim().equalsIgnoreCase(JavaConfigureInfo.methodDeclaration))){//声明方法
            String temResult=generateMethodDeclaration(item,index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode,lineOrder);
            result += " " + temResult;
        }else if(ConfigureInfo.ifJavaCode&&item!=""&&(JavaConfigureInfo.divisorGrammars.contains(item))){
            String temResult=grammarGenerateService.generateProgramForMatchedValue(item, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            temResult = constraint.checkUniqueConstraint(temResult, item, index, grammars);//唯一性约束（*不可重复相同值）
            temResult = constraint.checkContinueConstraint(temResult, item, index, grammars);//连续性约束，如果在约束列表中，temResult中不能有空格
            temResult=zeroExclideJava(temResult);
            result += " " + temResult;
        }
        return result;
    }

    /**
     * anonymousAuthor
     * 2022.10.27
     * @param str
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param sn
     * @return
     */
    public String generateClassDeclaration(String str, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree sn,int lineOrder) {
        String result="";
        //新的类开启一个作用域
        int scopeId=st.getScopeNum()+1;
        ScopeTree scopeNode=new ScopeTree(scopeId,null,"1",null,null);//作用域根节点
        scopeNode.setRoot(sn.getRoot());
        scopeNode.setFather(sn.getRoot());//生成全局变量多少的开关，不加这一行，每个类中会生成较多全局变量，只用一次，加这一行，每个新生成的全局变量会在备选列表中，因此生成的全局变量很少，每个变量参与的运算很多
        st.setScopeNum(scopeId);
        List<ScopeTree> children=sn.getRoot().getChildren();
        children.add(scopeNode);
        sn.getRoot().setChildren(children);

        String[] items=str.split(" ");//CLASS identifier classBody
        String temResult="";
        Type type=new Type();
        String identifier="";
        Constraint constraint=new Constraint();
        if(st!=null&&st.getProgramName()!=null&&!st.getProgramName().equals("")){
            type.setFileName(st.getProgramName());
        }else{
            return str;
        }
        //作用域即所在类更新
        int typeId=st.getTypeMap().size();
        type.setTypeId(typeId);
        Type outerType=scopeNode.getLocateType();
        if(outerType!=null){
            List<Type> innerTypes=outerType.getInnerTypes();
            innerTypes.add(type);
            outerType.setInnerTypes(innerTypes);
            type.setOuterType(outerType);
        }
        //生成类型标识符
        identifier=grammarGenerateService.getIdentifier(index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        type.setTypeName(identifier);

        type.setScopeNode(scopeNode);//生成全局变量多少的开关3
        scopeNode.setLocateType(type);
        type.setScopeNode(scopeNode);//生成全局变量多少的开关3


        //class体生成

        for(String item:items) {
            temResult = grammarGenerateService.generateProgramForMatchedValue(item, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            temResult = constraint.checkUniqueConstraint(temResult, item, index, grammars);//唯一性约束（*不可重复相同值）
            temResult = constraint.checkContinueConstraint(temResult, item, index, grammars);//连续性约束，，如果在约束列表中，temResult中不能有空格
            if (item.equals("identifier")) {
                temResult = identifier;
            }
            result += " " + temResult;
        }

        //作用域所在类回溯
        scopeNode.setLocateType(outerType);
        grammarGenerateService.updateSymTableByClass(st,type);//使用新声明的函数更新符号表模块
        //添加当前类中所有的未声明标识符到类的最顶端
        result=insertAddDeclarationToType(st,result,type);//把补充声明语句插入到生成的程序中
        return result;
    }

    /**
     * anonymousAuthor
     * 2022.10.27
     * @param str
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param sn
     * @return
     */
    private String generatePublicClassDeclaration(String str, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree sn,int lineOrder) {
        String result="";
        //新的类开启一个作用域
        int scopeId=st.getScopeNum()+1;
        ScopeTree scopeNode=new ScopeTree(scopeId,null,scopeId+"",null,null);//作用域根节点
        scopeNode.setRoot(sn.getRoot());
        scopeNode.setFather(sn.getRoot());//生成全局变量多少的开关，不加这一行，每个类中会生成较多全局变量，只用一次，加这一行，每个新生成的全局变量会在备选列表中，因此生成的全局变量很少，每个变量参与的运算很多
        st.setScopeNum(scopeId);
        List<ScopeTree> children=sn.getRoot().getChildren();
        children.add(scopeNode);
        sn.getRoot().setChildren(children);


        String[] items=str.split(" ");//CLASS identifier includeMainClassBody
        String temResult="";
        Type type=new Type();
        String identifier="";
        if(st!=null&&st.getProgramName()!=null&&!st.getProgramName().equals("")){
            type.setTypeName(st.getProgramName());
            identifier=st.getProgramName();
            type.setFileName(st.getProgramName());
        }else{
            return str;
        }
        //作用域即所在类更新
        int typeId=st.getTypeMap().size();
        type.setTypeId(typeId);
        Type outerType=scopeNode.getLocateType();
        if(outerType!=null){
            List<Type> innerTypes=outerType.getInnerTypes();
            innerTypes.add(type);
            outerType.setInnerTypes(innerTypes);
            type.setOuterType(outerType);
        }

        type.setScopeNode(scopeNode);//生成全局变量多少的开关3
        scopeNode.setLocateType(type);
        type.setScopeNode(scopeNode);//生成全局变量多少的开关3
        grammarGenerateService.updateSymTableByClass(st,type);//使用新声明的函数更新符号表模块


        //class体生成
        Constraint constraint=new Constraint();
        for(String item:items) {
            temResult = grammarGenerateService.generateProgramForMatchedValue(item, index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            temResult = constraint.checkUniqueConstraint(temResult, item, index, grammars);//唯一性约束（*不可重复相同值）
            temResult = constraint.checkContinueConstraint(temResult, item, index, grammars);//连续性约束，，如果在约束列表中，temResult中不能有空格
            if (item.equals("identifier")) {
                result += " " + identifier;
                continue;
            }
            result += " " + temResult;
        }

        //作用域所在类回溯
        scopeNode.setLocateType(outerType);
        result=insertAddDeclarationToType(st,result,type);//把补充声明语句插入到生成的程序中
        return result;
    }

    /**
     * anonymousAuthor
     * 2022.10.30
     * @param str
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    public String generateMain(String str, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        String result="";
        List<String> addedDeclaration=new ArrayList<>();//补充的声明列表,main生成结束后将其添加在main函数最开始的位置

        //查找类型列表
        Map<String,Type> cusTomTypeMap=st.getCustomizedTypeMap();

        //为每个类创建一个对象
        result+=generateObjectByType(cusTomTypeMap,index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);

        //调用每个类中的每一个方法，从符号表中读取，在当前作用域中，且category是object，或者直接读取
        result+=generateObjectCallMethod(index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,addedDeclaration,lineOrder);

        //输出main中声明的变量以及对象的成员变量的值
        result+=generateOutPut(cusTomTypeMap, st, scopeNode);

        result=insertAddDeclarationToMain(addedDeclaration,result);//在result最开始的位置加入为声明标识符的声明语句
        return result;
    }

    /**
     * 生成声明
     * @param str
     * @param st
     * @param scopeNode
     * @param index
     * @param ifNeedOutCycle
     * @return
     */
    public String generateDeclaration(String str, SymbolTable st, ScopeTree scopeNode, int index, boolean ifNeedOutCycle, Map<String, Integer> tempTokenAppearTimes,int lineOrder) {
        String item="typeType";
        String typeType="";
        String result="";
        String temResult = grammarGenerateService.generateProgramForMatchedValue(item, index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        //确定声明的变量的类型
        typeType = temResult.trim();
        //确定声明的变量的标识
        SymbolRecord symbolRecord=generateDeclarationByTypeInCurScope(typeType,st,scopeNode,index,tempTokenAppearTimes,ifNeedOutCycle, JavaConfigureInfo.javaBaseTypeExp,lineOrder);
        if(str.equals(JavaConfigureInfo.fieldDeclaration)){//和成员变量声明文法相关
            result =typeType+" "+ symbolRecord.getName()+ " = "+symbolRecord.getValue()+";";
            //成员变量加入到所在类型的成员变量列表里
            Type locateType=scopeNode.getLocateType();
            List<SymbolRecord> vars=locateType.getVars();
            vars.add(symbolRecord);
            locateType.setVars(vars);
            scopeNode.setLocateType(locateType);
        }else{
            result =typeType+" "+ symbolRecord.getName()+ " = "+symbolRecord.getValue();
        }
        grammarGenerateService.updateSymTableByVar(st,symbolRecord);//更新符号表模块-定义标识符
        return result;
    }



    /**
     * anonymousAuthor
     * 2022.10.25
     * @param str
     * @param type
     * @return
     */
    public String addReturn(String str, Type type, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        String result="";
        String addStr="";
        if(type==null||type.getTypeName()==null){
            return str;
        }
        if(type.getTypeName().equals("void")){//方法的返回值是void类型
            //可以添加一个空的return语句到方法体结束之前
            addStr="return;";
        }else{
            //添加一条return expression; 到方法体的末尾
            addStr="return "+grammarGenerateService.constructProgramByValue(type.getGrammarExpression(),index,tokenAppearTimes,ifNeedOutCycle,st,scopeNode, lineOrder)+";";
        }
        if(str.lastIndexOf("}")==str.length()-1){//最后一位是}
            str=str.substring(0,str.length()-2);
            str+=" "+addStr;
            str+=" }";
        }
        return str;
    }

    /**
     * 2022-10-12生成一个标识符，确保有定义
     * @param strCopy
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    public String generateIdentifierByType(String strCopy, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        String typeType="";
        strCopy=strCopy.trim();
        String identifier="";
        int addCount=0;
        Map<String, Integer> tempTokenAppearTimes = tokenAppearTimes;
        //这里做类型的区分
        String tokenType=strCopy.trim();
        if(ConfigureInfo.ifJavaCode){
            typeType=JavaConfigureInfo.varnamesMap.get(tokenType);
        }else if(ConfigureInfo.ifPythonCode){
            typeType= PythonConfigureInfo.varnamesMap.get(tokenType);
        }else if(ConfigureInfo.ifCCode){
            typeType= CConfigureInfo.varnamesMap.get(tokenType);
        }
        if(typeType==null||typeType==""){
            typeType="int";
//            //System.out.println("unknown type "+"strCopy");
        }
        List<SymbolRecord>  typeSymbolRecords=st.getSymbolRecords();//符号总表
        List<String>  validsymbolScopeIdentifier=getScopeIdentifier(typeSymbolRecords,scopeNode,typeType);//取出在当前scope可以使用的标识符名称
        String defineStatement="";
        if(validsymbolScopeIdentifier.size()>0){
            int indexI=(int)(Math.random()*(validsymbolScopeIdentifier.size()-1));
            identifier=validsymbolScopeIdentifier.get(indexI);
            return identifier;
        }else{
            //声明一个Int变量，将其添加在当前block以及所有父级block中的某些位置，例如顶部，返回声明的变量名称
            SymbolRecord symbolRecord=generateDeclarationByType(typeType,st, scopeNode,index,tempTokenAppearTimes, ifNeedOutCycle,lineOrder);//在class的顶端，第一个声明的后面添加，使用的方法是把这个声明暂存到一个补充声明列表里，在程序生成结束后，一次性的添加到程序中包含的第一个分号后面
            Type locateType=scopeNode.getLocateType();
            if(locateType!=null){
                Map<String,List<String>> addedDeclarationMap=st.getAddedDeclaration();
                List<String> addedDeclaration=addedDeclarationMap.get(locateType.getTypeName());//补充的声明列表
                if(addedDeclaration==null){
                    addedDeclaration=new ArrayList<>();
                }
                defineStatement=typeType+" "+ symbolRecord.getName()+ " = "+symbolRecord.getValue()+";";
                addedDeclaration.add(defineStatement);
                addCount=addedDeclaration.size();
                addedDeclarationMap.put(locateType.getTypeName(),addedDeclaration);
                st.setAddedDeclaration(addedDeclarationMap);

                //成员变量加入到所在类型的成员变量列表里
                List<SymbolRecord> vars=locateType.getVars();
                vars.add(symbolRecord);
                locateType.setVars(vars);
                scopeNode.setLocateType(locateType);
            }else{
                Map<String,List<String>> addedDeclarationMap=st.getAddedDeclaration();
                List<String> addedDeclaration=addedDeclarationMap.get("null");//补充的声明列表
                if(addedDeclaration==null){
                    addedDeclaration=new ArrayList<>();
                }
                defineStatement=typeType+" "+ symbolRecord.getName()+ " = "+symbolRecord.getValue()+";";
                addedDeclaration.add(defineStatement);
                addCount=addedDeclaration.size();
                addedDeclarationMap.put("null",addedDeclaration);
                st.setAddedDeclaration(addedDeclarationMap);
            }
            grammarGenerateService.updateSymTableByVar(st,symbolRecord);
            grammarGenerateService.updateIdentifierRelations("", defineStatement, scopeNode,st,(0-addCount));
            
            return symbolRecord.getName();
        }
    }



    /**
     * 2022-10-12
     * 筛选出作用域内可用的，同类型的标识符名称
     * @param symbolRecords
     * @param scopeNode
     * @param typeType
     * @return
     */
    public List<String> getScopeIdentifier(List<SymbolRecord> symbolRecords, ScopeTree scopeNode, String typeType) {
        List<String> result=new ArrayList<>();
        List<Integer> scopeIDs=grammarGenerateService.getValidScopeIDs(scopeNode);//获取当前作用域以及父级作用域的ID
        if(scopeIDs.size()==0||symbolRecords==null||symbolRecords.size()==0){
            return result;
        }
        for(SymbolRecord sr : symbolRecords){
            if(sr.getType()==null){
                continue;
            }
            if(scopeIDs.contains(sr.getScope().getScopeID())&&typeType.equals(sr.getType().getTypeName())){
                result.add(sr.getName());
            }
        }
        return result;
    }

    /**
     * 2022-10-27
     * 在生成类中，一些标识符未声明，记录在临时列表里，在类生成结束后，一次性的添加到类中
     * @param st
     * @param program
     * @return
     */
    public String insertAddDeclarationToType(SymbolTable st, String program, Type type) {
        List<String> addedDeclaration=st.getAddedDeclaration().get(type.getTypeName());
        if(addedDeclaration==null||addedDeclaration.size()==0){
            return program;
        }
        String result;
        String declarationString="";
        for(String ad:addedDeclaration){
            declarationString+=ad;
        }
        String head=program.substring(0,program.indexOf("{")+1);
        String tail=program.substring(program.indexOf("{")+1);

        result=head+declarationString+tail;
        return result;

    }

    /**
     * 2022-10-12
     * 在合适的位置生成一个typeType类型的变量声明，方便当前作用域内使用
     * @param typeType
     * @param st
     * @param scopeNode
     * @return
     */
    private SymbolRecord generateDeclarationByType(String typeType, SymbolTable st, ScopeTree scopeNode,int index,Map<String,Integer> tempTokenAppearTimes,boolean ifNeedOutCycle,int lineOrder) {
        SymbolRecord sr=generateDeclarationByTypeInCurScope(typeType, st, scopeNode, index,tempTokenAppearTimes, ifNeedOutCycle, JavaConfigureInfo.javaBaseTypeLiteral,lineOrder);
        sr.setScope(scopeNode.getLocateType().getScopeNode());//用当前作用域所在类的作用域
//        sr.setScope(scopeNode.getRoot());//用根作用域 生成大量全局变量开关2
        return sr;
    }

    /**
     * anonymousAuthor
     * 2022.10.31
     * 为每个类创建一个对象
     * @param cusTomTypeMap
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    private String generateObjectByType(Map<String,Type> cusTomTypeMap, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,int lineOrder) {
        String result="";
        String createObjectStr="";
        String objectIdentifier="";
        String objectValue="";
        for (Map.Entry<String,Type> entry : cusTomTypeMap.entrySet()) {
            createObjectStr="";
            String typename=entry.getKey();
            Type type=entry.getValue();
            objectIdentifier=grammarGenerateService.getIdentifier(index, tokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
            objectValue="new "+typename+"();";
            createObjectStr+=typename+" "+objectIdentifier+"= "+objectValue;
            //更新符号表
            SymbolRecord sr=new SymbolRecord(objectIdentifier,type,objectIdentifier,"object",scopeNode,objectValue);
            grammarGenerateService.updateSymTableByObj(st,sr);
            result+=createObjectStr;
        }
        return result;
    }

    /**
     * anonymousAuthor
     * 2022.10.31
     *  调用每个类中的每一个方法，从符号表中读取，在当前作用域中，且category是object，或者直接读取
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param st
     * @param scopeNode
     * @return
     */
    private String generateObjectCallMethod( int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, SymbolTable st, ScopeTree scopeNode,List<String> addedDeclaration,int lineOrder) {
        String result="";
        List<SymbolRecord>  objSymbolRecords=st.getObjSymbolRecords();//对象名
        for(SymbolRecord obj:objSymbolRecords){
            Type type=obj.getType();//对象的类型
            List<FunctionSymbolRecord> functions=type.getFunctions();//类中的函数列表
            String functionCall;
            for(FunctionSymbolRecord fun:functions){
                functionCall="";//初始化方法调用生成结内容
                //方法返回值部分
                Type returnType=fun.getReturnType();//方法的返回值类型
                if(returnType.getTypeName().equals("void")){//方法没有返回值

                }else{//方法有返回值
                    functionCall+=returnType.getTypeName();
                    functionCall+=" ";
                    SymbolRecord symbolRecord = generateDeclarationByTypeInCur(returnType.getTypeName(), st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle,lineOrder);//在class的顶端，第一个声明的后面添加，使用的方法是把这个声明暂存到一个补充声明列表里，在程序生成结束后，一次性的添加到程序中包含的第一个分号后面
                    grammarGenerateService.updateSymTableByVar(st, symbolRecord);
                    functionCall+=symbolRecord.getName();
                    functionCall+="=";
                }
                //对象及方法名
                functionCall+=obj.getName();
                functionCall+=".";
                functionCall+=fun.getName();

                //参数列表部分
                List<Type> parameterList=fun.getParameterList();//函数的参数列表，key是标识符，value是标识符的类型
                if(parameterList==null||parameterList.size()==0){//方法没有参数
                    functionCall+="()";
                }else{
                    functionCall+="(";
                    int count=0;
                    for (Type type1 : parameterList) {
                        String param= "";
                        //在当前作用域内生成某个类型的标识符作为函数的实参
                        SymbolRecord symbolRecord = generateDeclarationByTypeInCur(type1.getTypeName(), st, scopeNode, index, tokenAppearTimes, ifNeedOutCycle,lineOrder);//在class的顶端，第一个声明的后面添加，使用的方法是把这个声明暂存到一个补充声明列表里，在程序生成结束后，一次性的添加到程序中包含的第一个分号后面
                        addedDeclaration.add(type1.getTypeName() + " " + symbolRecord.getName() + " = " + symbolRecord.getValue() + ";");
                        grammarGenerateService.updateSymTableByVar(st, symbolRecord);
                        param=symbolRecord.getName();
                        if(count==0) {//第一个参数
                            functionCall += param;
                            count++;
                        }else{//第二个及之后的参数
                            functionCall +=","+ param;
                            count++;
                        }
                    }
                    functionCall+=")";
                }
                //语句结束符号;
                functionCall+=";";
                result+=functionCall;
            }
        }
        return result;
    }

    /**
     * 2022-10-12
     * 在当前作用域内生成一个typeType类型的变量声明。
     * @param typeType
     * @param st
     * @param scopeNode
     * @return
     */
    public SymbolRecord generateDeclarationByTypeInCurScope(String typeType, SymbolTable st, ScopeTree scopeNode, int index, Map<String, Integer> tempTokenAppearTimes, boolean ifNeedOutCycle, Map<String, String> typeExpression,int lineOrder) {
        String declaration=typeType;
        String identifier="";
        String identifierValue="";
        Constraint constraint=new Constraint();
        identifier=grammarGenerateService.getIdentifier(index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        identifierValue=grammarGenerateService.generateProgramForMatchedValue(typeExpression.get(typeType), index, tempTokenAppearTimes, ifNeedOutCycle, st, scopeNode,lineOrder);
        identifierValue = constraint.checkUniqueConstraint(identifierValue, typeExpression.get(typeType), index, grammars);//唯一性约束（*不可重复相同值）
        identifierValue = constraint.checkContinueConstraint(identifierValue, typeExpression.get(typeType), index, grammars);//连续性约束，，如果在约束列表中，temResult中不能有空格
        Type type=st.getTypeMap().get(typeType);
//        if(type==null)//System.out.println("what is this"+typeType);
        declaration += " " +identifier+ " = "+identifierValue;
        return new SymbolRecord(identifier,type,identifier,"var",scopeNode,identifierValue);
    }

    /**
     * 2022-10-12
     * 在当前作用于内生成一个typeType类型的变量声明，方便当前作用域内使用
     * @param typeType
     * @param st
     * @param scopeNode
     * @return
     */
    private SymbolRecord generateDeclarationByTypeInCur(String typeType, SymbolTable st, ScopeTree scopeNode,int index,Map<String,Integer> tempTokenAppearTimes,boolean ifNeedOutCycle,int lineOrder) {
        SymbolRecord sr=generateDeclarationByTypeInCurScope(typeType, st, scopeNode, index,tempTokenAppearTimes, ifNeedOutCycle, JavaConfigureInfo.javaBaseTypeLiteral,lineOrder);
        sr.setScope(scopeNode);//用当前作用域所在类的作用域
        return sr;
    }

    /**
     * anonymousAuthor
     * 2022.10.31
     *输出main中声明的变量以及对象的成员变量的值
     * @param cusTomTypeMap
     * @param st
     * @param scopeNode
     * @return
     */
    public String generateOutPut(Map<String,Type> cusTomTypeMap, SymbolTable st, ScopeTree scopeNode) {
        String result="";
        //main中的基本变量的值输出到控制台
        Map<String,Type> typeMap=st.getTypeMap();//基本类型映射表
        Set<String> baseTypes=typeMap.keySet();//基本类型名称列表
        List<SymbolRecord> baseSymbolRecords=grammarGenerateService.getSymbolRecordByType(st,scopeNode,baseTypes);//main当中可用的基本类型的标识符
        for(SymbolRecord baseIden:baseSymbolRecords){
            result+="//System.out.println("+baseIden.getName()+");";
        }

        //main中自定义的变量输出其成员变量的值到控制台
        Set<String> cusTypes=cusTomTypeMap.keySet();//自定义类型集合
        List<SymbolRecord> cusSymbolRecords=grammarGenerateService.getSymbolRecordByType(st,scopeNode,cusTypes);//main当中可用的自定义类型的标识符
        for(SymbolRecord cusIden:cusSymbolRecords){
            Type type=cusIden.getType();//获取自定义的类型
            String iden=cusIden.getName();//获取自定义类型的变量标识符
            List<SymbolRecord> vars=type.getVars();//获取自定义类型中的成员变量列表
            for(SymbolRecord var:vars){
                result+="//System.out.println("+iden+"."+var.getName()+");";
            }
        }
        return result;
    }


    /**
     * 2022-10-30
     * 在生成程序中，一些标识符未声明，记录在临时列表里，在程序生成结束后，一次性的添加到程序的最开始
     * @param addedDeclaration
     * @param program
     * @return
     */
    private String insertAddDeclarationToMain(List<String> addedDeclaration, String program) {
        if(addedDeclaration==null||addedDeclaration.size()==0){
            return program;
        }
        String result;
        String declarationString="";
        for(String ad:addedDeclaration){
            declarationString+=ad;
        }
        result=declarationString+program;
        return result;
    }

    /**
     * 向循环中插入break语句，防止死循环
     * anonymousAuthor
     * 2022.11.4
     * @param loopSegment
     * @param identifierCount
     * @return
     */
    public String insertBreakToLoop(String loopSegment, String identifierCount) {
        String result;
        String breakString="";
        breakString+=identifierCount+"="+identifierCount+"+1;";
        breakString+="if("+identifierCount+">"+ ConfigureInfo.getLoopCountMax()+"){";
        breakString+= JavaConfigureInfo.loopBreak+";}";
        String head=loopSegment.substring(0,loopSegment.indexOf("{")+1);
        String tail=loopSegment.substring(loopSegment.indexOf("{")+1);
        result=head+breakString+tail;
        return result;
    }

}
