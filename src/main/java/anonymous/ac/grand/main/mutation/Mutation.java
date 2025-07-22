package anonymous.ac.grand.main.mutation;

import anonymous.ac.grand.main.antlr4.CPP14Parser;
import anonymous.ac.grand.main.antlr4.CPPParserASTListener;
import anonymous.ac.grand.main.antlr4.CustomRuleContextDecorator;
import anonymous.ac.grand.main.common.*;
import anonymous.ac.grand.main.common.*;
import anonymous.ac.grand.mutate.cplus14.CPlusConfigureInfo;
import anonymous.ac.grand.mutate.cplus14.GrammarGenerateForCPlus;

import java.util.*;


/**
 * anonymousAuthor
 * 2024-12-25
 * 包含各种变异算子
 */
public class Mutation {
    private String mutationStrategy="no mutation";
    public Mutation(String selectedMutationStrategy) {
        this.mutationStrategy=selectedMutationStrategy;
    }


    /**
     * anonymousAuthor
     * 2024-12-25
     * main2main 基于匹配的代码片段插入变异，选出一个用于插入目标程序的源程序
     * @return
     */
    public CppProgram mutationMain2Main(){
        CppProgram selectedProgram=null;
        if(ConfigureInfo.mutationStrategies.contains(ConfigureInfo.selectedMutationStrategy)&&ConfigureInfo.selectedMutationStrategy.equals(ConfigureInfo.mutationStrategyMain2Main)) {//main-main变异
            List<CppProgram> cppList = CppForInsert.cppList;
            if(cppList==null||cppList.size()==0){
                return selectedProgram;
            }
            Random random = new Random();
            int index=random.nextInt(cppList.size());
            selectedProgram = cppList.get(index);
            selectedProgram.getMainIn();
        }
        return selectedProgram;
    }

    /**
     * anonymousAuthor
     * 2024-12-25
     * 变异方法，根据不同的变异选项来调用不同的变异方法
     * @param st
     * @return
     */
    public String mutate( SymbolTable st) {
        String result="";
        if(ConfigureInfo.selectedMutationStrategy==ConfigureInfo.mutationStrategyMain2Main) {
            if (ConfigureInfo.mutationStrategies.contains(ConfigureInfo.selectedMutationStrategy)) {//main-main变异，将一个源程序main之外的部分插入到目标程序main函数之外，将源程序main之内的部分插入到目标程序main函数之内
                Mutation mut = new Mutation(ConfigureInfo.selectedMutationStrategy);
                CppProgram selectedProgram = mut.mutationMain2Main();
                if (selectedProgram != null && selectedProgram.getMainIn() != null) {
                    String mainIn="";
                    for(String str:selectedProgram.getMainIn()){
                        mainIn+=str+"\n";
                    }
//                    result = mainIn+ "\n" +"//"+selectedProgram.getCppFilePath()+"\n"+"//"+selectedProgram.getCppFileName() + "\n" ;//加入main函数内部的语句，以及该程序的名称和路径信息
                    result = mainIn+ "\n";
                    if (selectedProgram.getMainOut() != null) {
                        Map<String, List<String>> addedScopeDeclarationMap = st.getAddedScopeDeclaration();
                        List<String> addedScopeDeclarationInClass = addedScopeDeclarationMap.get(st.getProgramName());//补充的作用域对应的声明列表
                        if (addedScopeDeclarationInClass == null) {
                            addedScopeDeclarationInClass = new ArrayList<>();
                        }
                        addedScopeDeclarationInClass.addAll(selectedProgram.getMainOut());
//                        addedScopeDeclarationInClass.add("//"+selectedProgram.getCppFilePath()+"\n"+"//"+selectedProgram.getCppFileName() + "\n");
                        addedScopeDeclarationMap.put(st.getProgramName(), addedScopeDeclarationInClass);
                        st.setAddedScopeDeclaration(addedScopeDeclarationMap);
                    }
                    if (selectedProgram.getIncludeList() != null) {
                        Map<String, List<String>> addedIncludeDeclarationMap = st.getAddedIncludeDeclaration();
                        List<String> addedIncludeDeclarationInClass = addedIncludeDeclarationMap.get(st.getProgramName());//补充的头文件列表
                        if (addedIncludeDeclarationInClass == null) {
                            addedIncludeDeclarationInClass = new ArrayList<>();
                        }
                        addedIncludeDeclarationInClass.addAll(selectedProgram.getIncludeList());
                        addedIncludeDeclarationInClass.add("//"+selectedProgram.getCppFilePath()+"\n"+"//"+selectedProgram.getCppFileName() + "\n");
                        addedIncludeDeclarationMap.put(st.getProgramName(), addedIncludeDeclarationInClass);
                        st.setAddedIncludeDeclaration(addedIncludeDeclarationMap);
                    }
                }

            }
        }
//        else if(ConfigureInfo.selectedMutationStrategy==ConfigureInfo.mutationStrategyScope2Scope) {
//
//        }
//        else if(ConfigureInfo.selectedMutationStrategy==ConfigureInfo.mutationStrategyAstFun2Fun) {
//            mutate( st,  st.getRootScope(),   false);//从其他渠道调用到这里
//            System.out.println("mutate by ( st,  st.getRootScope(), false)");
////            if (ConfigureInfo.mutationStrategies.contains(ConfigureInfo.selectedMutationStrategy)) {//AstFun变异
////                Mutation mut = new Mutation(ConfigureInfo.selectedMutationStrategy);
////                CPP14Parser.FunctionDefinitionContext selectedFunContext = mut.mutationAstFun2Fun();
////                result+=selectedFunContext.getText();//函数的文本内容
////                FunctionSymbolRecord fsr=new FunctionSymbolRecord();
////                addFunctionSymbolRecord(selectedFunContext);//把函数的上下文关联到该函数的符号表记录上去
////            }
//        }
        return result;
    }

    /**
     * anonymousAuthor
     * 2024-12-25
     * main2main 基于AST的函数代码片段插入变异，选出一个用于插入目标程序的函数上下文
     * @return
     */
    public CPP14Parser.FunctionDefinitionContext mutationAstFun2Fun(){
        CPP14Parser.FunctionDefinitionContext selectedAstFun=null;
        if(ConfigureInfo.mutationStrategies.contains(ConfigureInfo.selectedMutationStrategy)&&ConfigureInfo.selectedMutationStrategy.equals(ConfigureInfo.mutationStrategyAstFun2Fun)) {//Astfun-fun变异
            while (selectedAstFun==null||selectedAstFun.functionBody()==null) {
//                List<CPP14Parser.FunctionDefinitionContext> funList = CppForInsert.astFunList; 反序列化方式
                List<CPP14Parser.FunctionDefinitionContext> funList = CPPParserASTListener.getFunList();//在生成之前解析程序，不需要序列化等操作
//                System.out.println("length of function context"+funList.size());
                if (funList == null || funList.size() == 0) {
                    return selectedAstFun;//可能返回空
                }
                Random random = new Random();
                int index = random.nextInt(funList.size());
                selectedAstFun = funList.get(index);
            }
        }
        return selectedAstFun;
    }



    /**
     * anonymousAuthor
     * 2025-1-9
     * Ast函数级别的变异，只在选项为mutationStrategyAstFun2Fun="AST Fun mutation"时，且在生成函数
     * @param st
     * @param scopeNode
     * @param isTemplate
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @return
     */
    public List<String> mutate(SymbolTable st, ScopeTree scopeNode, boolean isTemplate, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle, GrammarGenerateForCPlus ggfc) {
        List<String> result=new ArrayList<>();
        String resultElement="";
        if (ConfigureInfo.selectedMutationStrategy == ConfigureInfo.mutationStrategyAstFun2Fun && !ConfigureInfo.identifierEnumeration) {
            if (ConfigureInfo.mutationStrategies.contains(ConfigureInfo.selectedMutationStrategy)) {//AstFun变异
                Mutation mut = new Mutation(ConfigureInfo.selectedMutationStrategy);
                boolean funAvaiable = false;
                while (!funAvaiable) {
                    CPP14Parser.FunctionDefinitionContext selectedFunContext = mut.mutationAstFun2Fun();
                    if (selectedFunContext == null) {//如果源程序集合中没有函数，那么就不插入任何内容
                        result.add(resultElement);
                        return result;
                    }
                    CustomRuleContextDecorator decorator = new CustomRuleContextDecorator(selectedFunContext);
                    String customText = decorator.getTextWithSpaces();
                    FunctionSymbolRecord fsr = new FunctionSymbolRecord();
                    String tempFun = updateFunContext(customText, selectedFunContext, fsr, scopeNode, isTemplate, st);
                    if (tempFun != "0") {//如果返回值为0，表示解析该函数的部分出现异常，需要重新选择一个函数
                        resultElement += tempFun;//函数的文本内容
//                        resultElement += "\r\n" + "//" + selectedFunContext.getFilePath() + ";";//加入插入算子的源程序路径注释 加入一个没用的分号是为了换行能成功
                        resultElement += "\r\n" ;
                        funAvaiable = true;
                    }
                }
            }
        } else if (ConfigureInfo.selectedMutationStrategy == ConfigureInfo.mutationStrategyAstFun2Fun && ConfigureInfo.identifierEnumeration) {
            if (ConfigureInfo.mutationStrategies.contains(ConfigureInfo.selectedMutationStrategy)) {//AstFun变异
                Mutation mut = new Mutation(ConfigureInfo.selectedMutationStrategy);
                boolean funAvaiable = false;
                while (!funAvaiable) {
                    CPP14Parser.FunctionDefinitionContext selectedFunContext = mut.mutationAstFun2Fun();
                    if (selectedFunContext == null) {//如果源程序集合中没有函数，那么就不插入任何内容
                        result.add(resultElement);
                        return result;
                    }

                    CustomRuleContextDecorator decorator = new CustomRuleContextDecorator(selectedFunContext);
                    String customText = decorator.getTextWithSpaces();
                    FunctionSymbolRecord fsr = new FunctionSymbolRecord();
                    String tempFun = updateFunContext(customText, selectedFunContext, fsr, scopeNode, isTemplate, st);
                    if (tempFun != "0") {//如果返回值为0，表示解析该函数的部分出现异常，需要重新选择一个函数
                        resultElement += tempFun;//函数的文本内容
//                        resultElement += "\r\n" + "//" + selectedFunContext.getFilePath() + ";";//加入插入算子的源程序路径注释 加入一个没用的分号是为了换行能成功
                        resultElement += "\r\n" ;
                        result.add(resultElement);

                        List<String> enumerateResult=getEnumeratorByFragment(tempFun,fsr, scopeNode, isTemplate, st,index, tokenAppearTimes, ifNeedOutCycle,ggfc,selectedFunContext);//枚举后的程序片段列表
                        for(String ele:enumerateResult) {//枚举多次
                            resultElement = ele;//函数的文本内容重命名标识符后
//                            resultElement += "\r\n" + "//" + selectedFunContext.getFilePath() + ";";//加入插入算子的源程序路径注释 加入一个没用的分号是为了换行能成功
                            resultElement += "\r\n" ;
                            result.add(resultElement);
                        }

                        funAvaiable = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * anonymousAuthor
     * 2025-2-1
     *对于一个程序片段，对其骨架中包含的标识符进行枚举，得到一个枚举后的程序片段列表
     * @param fragment
     * @param fsr
     * @param scopeNode
     * @param isTemplate
     * @param st
     * @param index
     * @param tokenAppearTimes
     * @param ifNeedOutCycle
     * @param selectedFunContext
     * @return
     */
    private List<String> getEnumeratorByFragment(String fragment, FunctionSymbolRecord fsr, ScopeTree scopeNode, boolean isTemplate,
                                                 SymbolTable st, int index, Map<String, Integer> tokenAppearTimes, boolean ifNeedOutCycle,
                                                 GrammarGenerateForCPlus ggfc, CPP14Parser.FunctionDefinitionContext selectedFunContext) {
        List<String> result=new ArrayList<>();
        IdentifierRenaming ir=new IdentifierRenaming();
        ScopeBlockForSlice sbfs=new ScopeBlockForSlice(fragment);
        List<VariableIdentifierInfo> vi=ir.getVariableIdentifierInfo(sbfs);
        ggfc=ggfc==null?new GrammarGenerateForCPlus():ggfc;

        //获取当前作用域内可用的标识符列表
        List<SymbolRecord>  typeSymbolRecords=st.getSymbolRecords();//符号总表
        List<SymbolRecord>  validsymbolScopeIdentifier=ggfc.getScopeIdentifier(typeSymbolRecords,scopeNode,"int","var");//取出在当前scope可以使用的标识符名称
        List<String> identifierName=new ArrayList<>();
        if(validsymbolScopeIdentifier.size()<=0){
            identifierName.add(ggfc.getNewIdentifier("int",st, scopeNode,index,tokenAppearTimes, ifNeedOutCycle,"var", "self",0));
        }else{
            for(SymbolRecord sr:validsymbolScopeIdentifier){
                identifierName.add(sr.getName());
            }
        }

        while(identifierName.size()<ConfigureInfo.minValidIdntifer){//设置变异作用域内可用标识符数量
            identifierName.add(ggfc.getNewIdentifier("int",st, scopeNode,index,tokenAppearTimes, ifNeedOutCycle,"var", "self",0));
        }

        List<CPP14Parser.StatementContext> statements=selectedFunContext.getStatementList();
        if(statements==null&&statements.size()==0){
            //该函数中不包含语句，没有枚举的必要
            return result;
        }


        int[][] identifierRelation=new int[vi.size()][vi.size()];//程序片段中各个位置的标识符之间的关系，是一个2维的矩阵，每两个位置之间如果有赋值或决定关系，将其赋值为1，如果没有，赋值为0，而在具体计算时，如果两个标识符相同则再将其赋值为0，标识符不同则不变
        identifierRelation=getIdentifierRelation(fragment,statements,sbfs,selectedFunContext,identifierRelation);//根据函数中的赋值语句等来计算标识符之间的关系

//        List<List<String>> identifierOrders=getIdentifiersOrderByEnumeration(identifierName,identifierRelation.length);//按照程序片段的骨架，即标识符的位置个数n，以及可用的标识符个数m，一共有m的n次方种标识符重命名序列
//        List<List<String>> maxRelationIdentifierOrders=getMaxRelationIdentifierOrders(identifierOrders,identifierRelation);//获取使得标识符关系最大的那些序列
        List<List<String>> maxRelationIdentifierOrders=getOrderByMaxEnumeration(identifierName,sbfs);//获取使得标识符关系最大的那些序列 sbfs中已经包含了标识符关系等内容
        //ggfc.generateIdentifierByTypeC("int",index, tokenAppearTimes,  ifNeedOutCycle,  st,  scopeNode, CPlusConfigureInfo.varnameCategoryMap.get("int"),"self",0);
        int i=1;
        for(List<String> order:maxRelationIdentifierOrders){
            String tempFrag=replaceFragmentByOrder(sbfs.getRenameStrByAST(),order);//把原始的程序片段中的标识符位置按照标识符序列order中的顺序依次进行替换，生成新的程序片段
            System.out.println("rename "+i+": "+tempFrag);
            i++;
            result.add(tempFrag);
        }

        return result;
    }

    /**
     * anonymousAuthor
     * 2025-2-11
     * 优化后的获取使得标识符关系最大的哪些标识符枚举序列
     * 标识符的位置个数n正好是sbfs.identifierRelation数组的长度，以及可用的标识符个数m，即identifierNames的长度，一共有m的n次方种标识符重命名序列
     * @param identifierNames
     * @param sbfs
     * @return
     */
    private List<List<String>> getOrderByMaxEnumeration(List<String> identifierNames, ScopeBlockForSlice sbfs) {
        List<List<String>> result=new ArrayList<>();
        Map<Integer,String> indexGroup=new HashMap<>();//该序号的标识符位置所属的等价集合E1或E2
        for(int[] arr:sbfs.getRelationList()){//对于有标识符关系的标识符，将其划分枚举的等价集合
            int index1=arr[0];
            int index2=arr[1];
            int relation=arr[2];
            if(relation==1){//按理说 都是1
                //需要用到组合测试的一些方法
                indexGroup.put(index1,"E1");
                indexGroup.put(index2,"E2");
            }
        }
        //对于没有标识符关系的标识符，例如int a=0中的a，也需要记录这标识符的序号进行随机取值 也可以将其划分在E1或E2里
        for(int i=0;i<sbfs.getIdentifierRelation().length;i++){
            if(indexGroup.get(i)==null){
                indexGroup.put(i,"E0");
            }
        }
        for(String identifier1:identifierNames){
            for(String identifier2:identifierNames){
                List<String> tempOrder=new ArrayList<>();//每一个枚举的序列
                for(int j=0;j<sbfs.getIdentifierRelation().length;j++){
                    if(indexGroup.get(j)=="E0"){
                        Random random = new Random();
                        int randomInteger = random.nextInt(identifierNames.size());//随机取标识符
                        tempOrder.add(identifierNames.get(randomInteger));
                    }else if(indexGroup.get(j)=="E1"){
                        tempOrder.add(identifier1);//E1中的标识符使用相同的枚举结果
                    }else{
                        tempOrder.add(identifier2);//E2中的标识符使用相同的枚举结果
                    }
                }
                result.add(tempOrder);
            }
        }
        return result;
    }

    /**
     * anonymousAuthor
     * 2025-2-4
     * 根据程序片段的内容，来计算该程序片段中标识符之间的关系
     * @param fragment
     * @param statements
     * @param sbfs
     * @param selectedFunContext
     * @param identifierRelation
     * @return
     */
    private int[][] getIdentifierRelation(String fragment, List<CPP14Parser.StatementContext> statements, ScopeBlockForSlice sbfs, CPP14Parser.FunctionDefinitionContext selectedFunContext, int[][] identifierRelation) {
        if(statements==null){
            return identifierRelation;
        }
        sbfs.setFragmentWithSpace(fragment);
        Map<String,String>  renameMap=new HashMap<>();//标识符重命名map，<newName，origName>
        List<CPP14Parser.StatementContext> funStatement=new ArrayList<>();//函数中包含的语句
        for(CPP14Parser.StatementContext statement:statements){
            CustomRuleContextDecorator decorator2 = new CustomRuleContextDecorator(statement);
            String customStateText = decorator2.getTextWithSpaces();
            if(fragment.contains(customStateText)){
                String statementText=statement.getText();
                funStatement.add(statement);
            }
        }
        int idIndex=0;
        List<CPP14Parser.StatementContext> funAssignStatement=new ArrayList<>();//函数中包含的赋值语句

        String programStr=getProgramByLine(fragment);//对程序片段进行必要的换行操作，后面将会从前往后按行有序定位每一个标识符
        String[] programLines=programStr.split("\r\n");
        String renameFragmant=fragment;//对fragment重命名后的程序片段
        List<int[]> relationList=new ArrayList<>();//标识符关系列表
        List<String> classNames=getClassSpecifierNames(selectedFunContext);
        List<String> funNames=getFunSpecifierNames(selectedFunContext);

        for(String programLine:programLines){
            String renameLine=programLine;//对programLine重命名后的行
            for(CPP14Parser.StatementContext statement:statements){
                CustomRuleContextDecorator decorator2 = new CustomRuleContextDecorator(statement);
                String customStateText = decorator2.getTextWithSpaces();//原始的语句
                String[] strs=customStateText.split(" ");//对程序内容进行拆分


                if(programLine.contains(customStateText)){
//                if(programLine.contains(customStateText)&&customStateText.contains("=")&&!customStateText.contains("==")&&!customStateText.contains("!=")&&!customStateText.contains(">=")&&!customStateText.contains("<=")){
                    String renameStateText = "";//对语句customStateText重命名后的语句
                    List<CPP14Parser.UnqualifiedIdContext> unqualifiedIdsContext=new ArrayList<>();
                    List<CPP14Parser.QualifiedIdContext> qualifiedIdsContext=new ArrayList<>();
                    unqualifiedIdsContext=statement.getUnqualifiedIds();
                    qualifiedIdsContext=statement.getQualifiedIds();
                    List<String> unqualifiedIds=decorator2.getTextByContext(unqualifiedIdsContext);
                    List<String> qualifiedIds=decorator2.getTextByContext(qualifiedIdsContext);

                    boolean isLeft=true;//判断是等式左值还是右值部分
                    List<Integer> leftIdentifiers=new ArrayList<>();//等式左值包含的标识符序号（一般只有一个） 比如identifier2 只记录2，方便对标识符关系数据进行下标赋值
                    List<Integer> rightIdentifiers=new ArrayList<>();//等式右值包含的标识符序号（可能有多个）
                    if(unqualifiedIds!=null&&unqualifiedIds.size()>0){
                        System.out.println("statement: "+statement.getText());
                        for(String str:strs){
                            if(str.contains("=")){
                                isLeft=false;
                            }
                            if(unqualifiedIds.contains(str)&&isValidVariableName(str)&&!isFunctionName(str,funNames,customStateText)&&!isClassName(str,classNames,customStateText,unqualifiedIds,strs)){//是标识符,不是关键字 重命名并且记录序号等信息
//                            if(unqualifiedIds.contains(str)&&isValidVariableName(str)){//是标识符,不是关键字 重命名并且记录序号等信息
                                //按序重命名
                                String origName=str;
                                String newName="identifier"+idIndex+" ";
                                renameStateText+=newName;

                                renameMap.put(newName,origName);//更新重命名映射map
                                if(isLeft){//左值
                                    leftIdentifiers.add(idIndex);
                                }else{//右值
                                    rightIdentifiers.add(idIndex);
                                }
                                idIndex++;//标识符重命名序号从0开始

                            }else{//不是标识符，直接添加到重命名后的语句当中
                                renameStateText+=str+" ";
                            }
                        }
                        for(int left:leftIdentifiers){
                            for(int right:rightIdentifiers){
                                int[] r1={left,right,1};
                                relationList.add(r1);
                                //identifierRelation[left][right]=1;
                                //identifierRelation[right][left]=1;
                            }
                        }
                    }
                    String statementText=statement.getText();
                    funAssignStatement.add(statement);
                    renameLine=renameLine.replace(customStateText,renameStateText);//用重命名后的语句替换原始的语句
                    break;//如果一行匹配到了一个语句，就不再继续匹配了
                }
            }
            renameFragmant=renameFragmant.replace(programLine,renameLine);//用重命名后的行替换原始的行
        }

        int[][] result=new int[idIndex][idIndex];

        for(int[] arr:relationList){
            result[arr[0]][arr[1]]=arr[2];//我们不计result[arr[1]][arr[0]]=arr[2]; 因为这两个关系是对称的 降低枚举的数量1/2
        }

        if(idIndex>2){
            System.out.println("idindex: "+idIndex);
        }
        sbfs.setRenameStrByAST(renameFragmant);
        sbfs.setIdentifierRelation(result);
        sbfs.setRenameMap(renameMap);
        sbfs.setRelationList(relationList);

        System.out.println("fragment:\n"+fragment);
        System.out.println("renameFragment:\n"+renameFragmant);
//        System.out.println("renameFragmentByMatcher:\n"+sbfs.getRenameScopeStr());
        return result;
    }

    /**
     * anonymousAuthor
     * 2025-2-11
     * 判断标识符是否为类名，如果是那么判断该语句是否包含这个类变量的声明 一般后面跟着空格和另一个标识符
     * @param id 标识符名称
     * @param names 标识符名称列表
     * @param stat 语句内容
     * @param unqualifiedIds
     * @param strs
     * @return
     */
    private boolean isClassName(String id, List<String> names, String stat, List<String> unqualifiedIds, String[] strs) {
        boolean result=false;
        if(names!=null&&names.contains(id)){
            if(stat.contains(id)&&stat.contains(id+" ")){
                String pre="";
                String cur="";
                for(String str:strs){
                    pre=cur;
                    cur=str;
                    if(pre==id){
                        if(unqualifiedIds.contains(cur)){//如果前一个字符串是我们期望判断的标识符，而后一个字符串也是一个标识符，那么认为这是一个类名
                            result=true;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * anonymousAuthor
     * 2025-2-11
     * 判断标识符是否为函数名称,如果是那么判断该语句是否包含这个函数的调用 一般后面跟着括号
     * @param id
     * @param names
     * @param stat
     * @return
     */
    private boolean isFunctionName(String id, List<String> names, String stat) {
        boolean result=false;
        if(names!=null&&names.contains(id)){
//            if(stat.contains(id)&&(stat.contains(id+"(")||stat.contains(id+" ("))){
//                result=true;
//            }
            if(stat.contains(id)&&(stat.contains("("))){
                result=true;
            }
        }
        return result;
    }

    /**
     * anonymousAuthor
     * 2025-2-12
     * 将程序其中所有的类名等等记录在一个列表里返回
     * @param ctx
     * @return
     */
    private List<String> getClassSpecifierNames(CPP14Parser.FunctionDefinitionContext ctx) {
        if(ctx==null){
            return null;
        }
        CppProgram cp=ctx.getCpp();
        if(cp==null){
            return null;
        }
        List<CPP14Parser.ClassSpecifierContext> classes=cp.getClassContextList();
        List<CPP14Parser.TypeSpecifierContext> types=cp.getTypeContextList();
        List<CPP14Parser.TrailingTypeSpecifierContext> traiTypes=cp.getTrailTypeContextList();
        List<CPP14Parser.EnumSpecifierContext> enums=cp.getEnumContextList();

        List<String> result=new ArrayList<>();

        for(CPP14Parser.ClassSpecifierContext clazz:classes){
            if(clazz!=null) {
                CustomRuleContextDecorator decorator1 = new CustomRuleContextDecorator(clazz.classHead());
                String customText1 = decorator1.getTextWithSpaces();

                String[] splitResult = customText1.split(" ");
                if(splitResult!=null&&splitResult.length>1){
                    String str1 = splitResult[1];
                    result.add(str1);
                }else{
                    System.out.println("class name or parameter miss: "+clazz.getText());
                }
            }
        }

//        for(CPP14Parser.TypeSpecifierContext type:types){
//            if(type!=null) {
//                CustomRuleContextDecorator decorator1 = new CustomRuleContextDecorator(type.classSpecifier());
//                String customText1 = decorator1.getTextWithSpaces();
//
//                CustomRuleContextDecorator decorator2 = new CustomRuleContextDecorator(type.enumSpecifier());
//                String customText2 = decorator2.getTextWithSpaces();
//
//                String[] splitResult = splitString(customText2);
//                if(splitResult!=null&&splitResult.length>1){
//                    String str1 = splitResult[0];
//                    result.add(str1);
//                }else{
//                    System.out.println("type name or parameter miss: "+type.getText());
//                }
//            }
//        }


//        for(CPP14Parser.TrailingTypeSpecifierContext ttype:traiTypes){
//            if(ttype!=null) {
//                CustomRuleContextDecorator decorator1 = new CustomRuleContextDecorator(ttype.typeNameSpecifier());
//                String customText1 = decorator1.getTextWithSpaces();
//
//                CustomRuleContextDecorator decorator2 = new CustomRuleContextDecorator(ttype.elaboratedTypeSpecifier());
//                String customText2 = decorator2.getTextWithSpaces();
//
//                CustomRuleContextDecorator decorator3= new CustomRuleContextDecorator(ttype.elaboratedTypeSpecifier());
//                String customText3 = decorator3.getTextWithSpaces();
//
//                String[] splitResult = splitString(customText2);
//                if(splitResult!=null&&splitResult.length>1){
//                    String str1 = splitResult[0];
//                    result.add(str1);
//                }else{
//                    System.out.println("type name or parameter miss: "+ttype.getText());
//                }
//            }
//        }

        for(CPP14Parser.EnumSpecifierContext enu:enums){
            if(enu!=null) {
                CustomRuleContextDecorator decorator = new CustomRuleContextDecorator(enu.enumHead());
                String customText1 = decorator.getTextWithSpaces();
                String[] splitResult = customText1.split(" ");
                if(splitResult!=null&&splitResult.length>1){
                    String str1 = splitResult[1];
                    result.add(str1);
                }else{
                    System.out.println("enum name or parameter miss: "+enu.getText());
                }
            }
        }

        return result;
    }



    /**
     * anonymousAuthor
     * 2025-2-12
     * 将程序其中所有的函数名记录在一个列表里返回
     * @param ctx
     * @return
     */
    private List<String> getFunSpecifierNames(CPP14Parser.FunctionDefinitionContext ctx) {
        if(ctx==null){
            return null;
        }
        CppProgram cp=ctx.getCpp();
        if(cp==null){
            return null;
        }
        List<CPP14Parser.FunctionDefinitionContext> funs=cp.getFunctionContextList();

        List<String> result=new ArrayList<>();
        for(CPP14Parser.FunctionDefinitionContext fun:funs){
            if(fun!=null) {
                CustomRuleContextDecorator decorator = new CustomRuleContextDecorator(fun.declarator());
                String customText1 = decorator.getTextWithSpaces();
                String[] splitResult = splitString(customText1);
                if(splitResult!=null&&splitResult.length>1){
                    String str1 = splitResult[0];
                    result.add(str1);
                }else{
                    System.out.println("function name or parameter miss: "+fun.getText());
                }
            }
        }


        return result;
    }


    /**
     * anonymousAuthor
     * 2025-2-12
     * 判断是否为变量的标识符而非关键字
     * @param str
     * @return
     */
    private boolean isValidVariableName(String str) {
        // Simple check to avoid replacing parts of keywords or function names
        // This is not exhaustive and might need improvement for real-world use cases
//            String[] keywords = {"int", "float", "double", "char", "return", "if", "else", "for", "while", "main", "void"};
        String keywords[] = {
                "int", "float", "double", "char", "bool", "return", "if", "else",
                "for", "while", "main", "void",
                "auto", "break", "case", "catch", "class", "const", "continue",
                "default", "delete", "do", "dynamic_cast", "enum",
                "explicit", "extern", "friend", "goto", "inline", "mutable",
                "namespace", "new", "operator", "private", "protected", "public",
                "register", "reinterpret_cast", "restrict", "sizeof", "static",
                "static_assert", "static_cast", "switch", "template", "this",
                "throw", "try", "typedef", "typeid", "typename", "union",
                "using", "virtual", "volatile", "wchar_t", "constexpr",
                "decltype", "noexcept", "nullptr", "thread_local",
                // C++11及以后新增的关键字
                "alignas", "alignof", "decltype","auto",
                //标准库中的对象
                "cout","endl",
                //头文件中定义的类型
                "int", "double", "float", "char", "bool", "void",
                "wchar_t", "char16_t", "char32_t", "size_t", "ptrdiff_t", "nullptr_t",
                "int8_t", "int16_t", "int32_t", "int64_t", "uint8_t", "uint16_t",
                "uint32_t", "uint64_t", "int_least8_t", "int_least16_t", "int_least32_t", "int_least64_t",
                "uint_least8_t", "uint_least16_t", "uint_least32_t", "uint_least64_t",
                "int_fast8_t", "int_fast16_t", "int_fast32_t", "int_fast64_t",
                "uint_fast8_t", "uint_fast16_t", "uint_fast32_t", "uint_fast64_t",
                "intptr_t", "uintptr_t", "max_align_t"

        };
        for (String keyword : keywords) {
            if (keyword.equals(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * anonymousAuthor
     * 2025-2-8
     吧     * 在一个字符串中添加一些换行语句
     * @param fragment
     * @return
     */
    private String getProgramByLine(String fragment) {
        String result="";
        result=fragment.replaceAll("}","}\r\n");
        result=result.replaceAll(";",";\r\n");
        result=result.replaceAll("\\{","\\{\r\n");
        result=result.replaceAll("= \\{\r\n","= \\{");
        result=result.replaceAll("}\r\n;","};");
        result=result.replaceAll("}\r\n ;","};");
        return result;
    }

    /**
     * anonymousAuthor
     * 2025-2-2
     * 用标识符序列中的标识符依次替换程序片段中的标识符
     * @param fragment
     * @param order
     * @return
     */
    private String replaceFragmentByOrder(String fragment, List<String> order) {
        String reuslt=fragment;
        int i=0;
        for(String id:order){
            reuslt=reuslt.replace("identifier"+i,id);//如果fragment中已经都被替换成了identifer1，identifer2...
            i++;
        }
        return reuslt;
    }

    /**
     * anonymousAuthor
     * 2025-2-2
     * 计算每个序列对应的程序片段中标识符的关系值
     * @param identifierOrders
     * @param identifierRelation
     * @return
     */
    private List<List<String>> getMaxRelationIdentifierOrders(List<List<String>> identifierOrders, int[][] identifierRelation) {
        //有待完善

        return identifierOrders;
    }

    /**
     * anonymousAuthor
     * 2025-2-1
     * 按照程序片段的骨架，即标识符的位置个数n，以及可用的标识符个数m，一共有m的n次方种标识符重命名序列
     * @param identifierNames
     * @param positionNum
     * @return
     */
    private List<List<String>> getIdentifiersOrderByEnumeration(List<String> identifierNames, int positionNum) {
        List<List<String>> sequences = generateSequences(identifierNames, positionNum);
        // 输出所有序列
        for (List<String> sequence : sequences) {
            System.out.println(sequence);
        }
        return sequences;
    }

    /**
     * anonymousAuthor
     * 2025-2-2
     * 用递归的方法获取序列的m个位置上依次取n个字符串中一个的笛卡尔积结果
     * @param strings
     * @param m
     * @return
     */
    public static List<List<String>> generateSequences(List<String> strings, int m) {
        List<List<String>> sequences = new ArrayList<>();
        generate(strings, m, 0, new ArrayList<>(), sequences);
        return sequences;
    }

    /**
     * anonymousAuthor
     *2025-2-2
     * 用递归的方法获取序列的m个位置上依次取n个字符串中一个的笛卡尔积结果
     * @param strings
     * @param m
     * @param position
     * @param currentSequence
     * @param sequences
     */
    private static void generate(List<String> strings, int m, int position, List<String> currentSequence, List<List<String>> sequences) {
        if (position == m) {
            // 创建当前序列的一个副本，并将其添加到结果集中
            sequences.add(new ArrayList<>(currentSequence));
            return;
        }

        for (String str : strings) {
            currentSequence.add(str); // 将字符串添加到当前序列
            generate(strings, m, position + 1, currentSequence, sequences); // 递归调用
            currentSequence.remove(currentSequence.size() - 1); // 回溯，移除刚刚添加的字符串
        }
    }

    /**
     * anonymousAuthor
     * 2025-1-9
     * 把函数的名称（标识符），函数返回值，函数参数列表更新到函数符号表中
     * @param selectedFunContext
     * @param fsr
     * @param scopeNode
     */
    private String updateFunContext(String str,CPP14Parser.FunctionDefinitionContext selectedFunContext, FunctionSymbolRecord fsr, ScopeTree scopeNode,boolean isTemplate,SymbolTable st) {
        if(selectedFunContext==null||fsr==null){
            return str;
        }
        List<Type> parameterList = new ArrayList<>();
        List<SymbolRecord> parameterIdentifiers = new ArrayList<>();
        List<SymbolRecord> returnIdentifiers = new ArrayList<>();
        Type locateType = scopeNode.getLocateType();
        String parameters="";
        try {
            fsr.setFunContext(selectedFunContext);//把函数的上下文关联到该函数的符号表记录上去
            if(selectedFunContext.declSpecifierSeq()!=null) {//函数返回值类型
                CustomRuleContextDecorator decorator1 = new CustomRuleContextDecorator(selectedFunContext.declSpecifierSeq());
                String customText1 = decorator1.getTextWithSpaces().trim();
                Type type1=st.getTypeMap().get(customText1);
                if(type1==null){//防止源程序中的返回值类型不在基本类型当中
                    type1=newType(type1,customText1,st);
                }
                fsr.setReturnType(type1);//类型名称可能是constexpr int
            }
            if(selectedFunContext.declarator()!=null) {//函数名及参数列表
                CustomRuleContextDecorator decorator2 = new CustomRuleContextDecorator(selectedFunContext.declarator());
                String customText2 = decorator2.getTextWithSpaces();
                String[] splitResult = splitString(customText2);
                String str1 = splitResult[0];
                String str2 = splitResult[1];
                fsr.setName(str1);
                fsr.setID(fsr.getName());
                parameters =str2;
            }
            fsr.setLocateType(locateType);
            fsr.setCategory("function");
        }catch(Exception e){
            //            System.out.println("Function context have some problems: "+e.toString());
            System.out.println("Function: "+selectedFunContext.getText());
            return "0";

        }
        if(parameters.trim().equals("")){
            //参数列表为空
        }else{
             parameters= StringTools.deleteBrackets("(",")",parameters);
            String[] parameterArr = parameters.split(",");//用逗号拆分参数列表
            for(int j=0;j<parameterArr.length;j++){
                String param=parameterArr[j].trim();
                String[] paraInfo=param.split(" ");//用空格来分割类型和标识符
                if(paraInfo.length<2){//一个参数 但是类型和标识符有问题
//                            //System.out.println("paraInfo.length less than 2: "+paraInfo.length);
                }else if(paraInfo.length==2){//一个普通参数 类型和标识符匹配
                    String type1name=paraInfo[0].trim();
                    String ident1=paraInfo[1].trim();
                    Type type1=st.getTypeMap().get(type1name);
                    if(type1==null){//防止源程序中的参数的类型不在基本类型当中
                        type1=newType(type1,type1name,st);
                    }
                    parameterList.add(type1);
                    SymbolRecord sr1=new SymbolRecord(ident1,type1,ident1,"var",scopeNode,"");
                    parameterIdentifiers.add(sr1);
                    GrammarGenerateForCPlus.grammarGenerateService.updateSymTableByVar(st,sr1);//参数列表中的形参也是函数体的作用域内可以使用的变量，更新到符号表
                }else if(paraInfo.length==3){//一个指针参数 类型和标识符匹配
                    String type1name=paraInfo[0].trim();
                    String type1star=paraInfo[1].trim();
                    String ident1=paraInfo[2].trim();
                    if(type1star.equals("*")) {
                        Type type1=st.getTypeMap().get(type1name+type1star);
                        if(type1==null){//防止源程序中的参数的类型不在基本类型当中
                            type1=newType(type1,type1name+type1star,st);
                        }
                        parameterList.add(type1);
                        SymbolRecord sr1=new SymbolRecord(ident1,type1,ident1,"pointer",scopeNode,"");
                        parameterIdentifiers.add(sr1);
                        GrammarGenerateForCPlus.grammarGenerateService.updateSymTableByVar(st,sr1);//参数列表中的形参也是函数体的作用域内可以使用的变量，更新到符号表
                    }
                }else if(paraInfo.length==4){//一个数组参数 类型和标识符匹配
                    String type1name=paraInfo[0].trim();
                    String ident1=paraInfo[1].trim();
                    String leftBracket=paraInfo[2].trim();
                    String rightBracket=paraInfo[3].trim();
                    if(leftBracket.equals("[")&&rightBracket.equals("]")) {
                        Type type1=st.getTypeMap().get(type1name+leftBracket+rightBracket);
                        if(type1==null){//防止源程序中的参数的类型不在基本类型当中
                            type1=newType(type1,type1name+leftBracket+rightBracket,st);
                        }
                        parameterList.add(type1);
                        SymbolRecord sr1=new SymbolRecord(ident1,type1,ident1,"array",scopeNode,"");
                        sr1.setArrayLength(CPlusConfigureInfo.arrayLength);
                        parameterIdentifiers.add(sr1);
                        GrammarGenerateForCPlus.grammarGenerateService.updateSymTableByVar(st,sr1);//参数列表中的形参也是函数体的作用域内可以使用的变量，更新到符号表
                    }
                }else{//一个参数 但是类型和标识符有问题
//                            //System.out.println("paraInfo.length more than 2: "+paraInfo.length);
                }
            }
        }
        fsr.setParameterList(parameterList);
        fsr.setActrualParameterIdentifiers(parameterIdentifiers);

        if(isTemplate) {
            fsr.setTemplateFun(isTemplate);
        }
        if(selectedFunContext.functionBody()!=null) {
            if (selectedFunContext.functionBody().getText() != null) {
                //暂时没想到是否需要特殊处理
                //return 方法返回值的添加
//            String funBody=selectedFunContext.functionBody().getText();
                String tempFun = str;
                int returnExp = tempFun.lastIndexOf("return");
                if (returnExp >= 0) {
                    returnIdentifiers = st.getIdentifiersFromStr(tempFun.substring(tempFun.lastIndexOf("return")), st);
                }
                GrammarGenerateForCPlus ggfc = new GrammarGenerateForCPlus();
//                System.out.println("generateOutPut before");
                String outPutStr = ggfc.generateOutPut(st, scopeNode);
//                System.out.println("generateOutPut after");
                tempFun = ggfc.insertStr2ScopeEnd(tempFun, outPutStr);
                str = tempFun;
            }
        }

        fsr.setReturnIdentifiers(returnIdentifiers);

        //更新函数所在类的函数列表
        if(locateType!=null) {//如果类型不是空，方法是属于类的方法
            List<FunctionSymbolRecord> functions=locateType.getFunctions();//类中的函数列表
            functions.add(fsr);
            locateType.setFunctions(functions);
            scopeNode.setLocateType(locateType);
        }


        GrammarGenerateForCPlus.grammarGenerateService.updateSymTableByFun(st,fsr);//使用新声明的函数更新符号表模块
        scopeNode=scopeNode.getFather();//把作用域回溯到方法的声明之外
        return str;
    }

    /**
     * anonymousAuthor
     * 2025-1-16
     * 当函数的返回值类型为空时，创建一个该类型并加入到类型集合中
     * @param type
     * @param typename
     * @return
     */
    private Type newType(Type type, String typename, SymbolTable st) {
        if(type==null){//防止源程序中的参数的类型不在基本类型当中
            Map<String, Type> typeMap =st.getTypeMap();
            type = new Type(typeMap.size()+1, typename);//类型
            typeMap.put(typename,type);
            st.setTypeMap(typeMap);
        }
        return type;
    }

    /**
     * anonymousAuthor
     * 2025-1-15
     * 取函数名及参数列表
     * @param input
     * @return
     */
    public static String[] splitString(String input) {
        // 查找第一个左括号和最后一个右括号的索引
        int firstLeftParen = input.indexOf('(');
        int lastRightParen = input.lastIndexOf(')');

        // 检查括号是否存在
        if (firstLeftParen == -1 || lastRightParen == -1 || firstLeftParen >= lastRightParen) {
            throw new IllegalArgumentException("Invalid input string: missing or misplaced parentheses.");
        }

        // 提取子字符串
        String str1 = input.substring(0, firstLeftParen);
        String str2 = input.substring(firstLeftParen + 1, lastRightParen);

        // 返回结果数组
        return new String[]{str1, str2};
    }
}
