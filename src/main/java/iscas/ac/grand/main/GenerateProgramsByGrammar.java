package iscas.ac.grand.main;
import iscas.ac.grand.main.common.*;
import iscas.ac.grand.main.mutation.CppForInsert;

import java.io.*;
import java.util.*;


public class GenerateProgramsByGrammar {

    List<Grammar> grammars=new ArrayList<>();

    public List<Grammar> getGrammars() {
        return grammars;
    }

    public void setGrammars(List<Grammar> grammars) {
        this.grammars = grammars;
    }

//    public static void main(String[] args)  throws IOException{
    public void generateTestProgram() throws Exception{
//        List<String> values=new ArrayList<>();
//        values.add("'[' (']' ('[' ']')* arrayInitializer | expression ']' ('[' expression ']')* ('[' ']')*)");
//        splitValueByOr(values);
        GenerateProgramsByGrammar generateProgramsByGrammar = new GenerateProgramsByGrammar();
        GenerateProgramsByGrammar gpg= generateProgramsByGrammar;
        String directory=ConfigureInfo.directory;
        int generateNum=ConfigureInfo.generateNum;
//        printGrammarName(directory);//输出文件夹下的文法名称
        //dot  edif300 erlang focal golang
//        System.out.println("before parser g4");
        gpg.setGrammars(getKeyValueFromG4(directory,gpg.getGrammars()));//读取文法
        List<Grammar> grammar=gpg.getGrammars();
//        System.out.println("grammar.size-out: "+grammar.size());
        PrintData pd=new PrintData(gpg.getGrammars());
        pd.writeVarInfo(directory);//输出变量的占比信息，终结 非终结
        pd.writeCycleInfo(directory);//输出循环的占比信息
        pd.writeSplitValueInfo(directory);//输出需要拆分取值的占比信息
//        //System.out.println("c:"+ ConfigureInfo.getNeedsplitNum());
//        System.out.println("after parser g4");
//        System.out.println("before generate");
        generateProgramsByGrammmars(generateNum,gpg.getGrammars());//生成程序
//        System.out.println("after generate");
//        //System.out.println("more than one root:"+ ConfigureInfo.getMoreThanOneRoot());
        String str1="";
        String str2="";
//        String result=constructProgramByValue("<assoc=right> expression bop='%=' expression",0,new HashMap<>(),false);

//        String result2=blockMatch("[GN] '\\'' (~['\\n\\r]   |   '\\'\\''   |   '\"')* '\\''",0);
//        //System.out.println(result2);

//        common.Grammar grammar1=getVarsFormG4("F:\\produce-program\\grammars-v4-master\\dot\\DOT.g4");//根据语法文件读入语法变量键值对
//        printMap(grammar1);
//        generateProgramsByGrammmar(grammar1,100);//生成程序
//        common.Grammar grammar2=getVarsFormG4("F:\\produce-program\\grammars-v4-master\\java\\java\\JavaLexer.g4");//根据语法文件读入词法变量键值对
//        printMap(grammar2);

    }


    /**
     * 为list中的每一个文法生成满足文法的num个程序，输出到文件里
     * @throws Exception
     */
    private static void generateProgramsByGrammmars(int num, List<Grammar> grammars) throws Exception {
        if(grammars==null||grammars.size()==0||num<=0){
            return;
        }
        int j;
        GrammarGenerateService ggs=new GrammarGenerateService(grammars);
        for(j=0;j<grammars.size();j++){
            if(grammars.get(j).getPath().endsWith("C.g4")){
//                //System.out.println(grammars.get(j).getPath());
            }
            List<String> programs=ggs.generateProgramsByGrammmar(j,num);
        }
    }

    /**
     * 读取目录下所有g4文法文件，输出键值对到相应目录下同名的txt中，递归
     * @param directory
     */
    private static List<Grammar> getKeyValueFromG4(String directory,List<Grammar> grammars) throws IOException {
        G4FileSearch tfs = new G4FileSearch();
        tfs.find(new File(directory));
        List<String> g4List=tfs.getRes();
        Grammar grammar=new Grammar();
        List<Grammar> allGrammars=new ArrayList<>();
        List<Grammar> result=new ArrayList<>();
        String loopStatistics="filename(文件名)\ttokenNum(文法变量总数)\tacyclicTokenNum(非循环变量数)\tacyclicNonterminateTokenNum(非循环变量中有子token的变量数)" +
                "\tterminateTokenNum(非循环变量中终止变量数)\tcyclicTokenNum(循环变量数)\tcyclicSelfTokenNum(自身循环变量数)\tcyclicMultiTokenNum(循环中涉及到多个变量的变量数)\n";
        GrammarGetService ggs=new GrammarGetService();
        for(String g4 : g4List){
//            //System.out.println(g4);
            grammar=ggs.getVarsFormG4(g4,"|");//语法变量键值对，变量顺序等
            if(ConfigureInfo.constraint==null) {
            	Constraint constraint =new Constraint();
                constraint=constraint.getConstraint(grammar,g4.replace(".g4","_constraint.txt"));//取出约束
                if(constraint!=null) grammar.setConstraint(constraint);
            }else {
            	grammar.setConstraint(ConfigureInfo.constraint);
            }
            allGrammars.add(grammar);
        }
        grammar=new Grammar();
        List<Grammar> validGrammars=grammar.getValidGrammars(allGrammars);
//        System.out.println("grammar.size-in getKeyValueFromG4: "+validGrammars.size());
        GrammarCheckService gcs=new GrammarCheckService();
        PrintData pd=new PrintData(grammars);
        Statistic statistic=new Statistic();
        for(Grammar g:validGrammars){////更新文法中token及value的信息,合并一些parser和lexer
            int i=2;
            do{
                //合并一些parser和lexer
                //更新文法中token及value的信息

                g = gcs.checkKeyTokens(g);//更新grammar中的tokens 以及value
                g = gcs.updateCycleMap(g);//更新跨层循环链上的取值
                //g=gcs.updateOutCycleStrategyMap(g);//更新跨层循环中跳出循环的取值策略，一个循环变量可能有多种跳出循环的方式
                g = gcs.updateOutCycleValue(g);//更新各个变量的跳出循环取值，当有多种方式时，保留长度最短的一个
                g= gcs.updateSplitCycleOr(g);//更新会产生无法跳出的循环的变量的取值，将其值中的竖线进行分割
                
                
                g = gcs.checkKeyTokens(g);//更新grammar中的tokens 以及value
                g = gcs.updateCycleMap(g);//更新跨层循环链上的取值
                //g=gcs.updateOutCycleStrategyMap(g);//更新跨层循环中跳出循环的取值策略，一个循环变量可能有多种跳出循环的方式
                g = gcs.updateOutCycleValue(g);//更新各个变量的跳出循环取值，当有多种方式时，保留长度最短的一个
                g= gcs.updateSplitCycleOr(g);//更新会产生无法跳出的循环的变量的取值，将其值中的竖线进行分割

                if(ConfigureInfo.ifLog) {
                    gcs.printOutPathTokens(g);
                    gcs.printCycleTokens(g);
                }
                
                
                
                i--;
            }while(i>0);
            pd.writeMap(g.getPath().replace(".g4",".txt"),g);//中间结果键值对输出到txt文件
            loopStatistics+=statistic.getLoopStatistics(g);
            result.add(g);
        }
        pd.writeProgram(directory+""+File.separator+"loopStatistics.txt",loopStatistics);
        return result;
    }



    /**
     * 获取一个node在文法树中所有的子节点，即node所对应key的value中出现的key，递归
     * @param node
     * @param vars
     * @param orderedList
     * @return
     */
    public Set<String> getChilds(String node, Map<String,List<String>> vars, List<String> orderedList) {
        Set<String> result=new HashSet();
        List<String> valueList=vars.get(node);//node对应的所有的value值
        GrammarGenerateService ggs=new GrammarGenerateService(grammars);
        Set<String> keyInValSet=ggs.getKeyFromValues(valueList,orderedList);//所有value中出现的所有key
        if(keyInValSet!=null&&keyInValSet.size()>0) {
            if(keyInValSet.contains(node)){//防止自身循环，将其先移除
                keyInValSet.remove(node);
            }
            result.addAll(keyInValSet);
            for(String key:keyInValSet){
                Set<String> childsKey=getChilds(key,vars,orderedList);
                if(childsKey!=null&&childsKey.size()>0) {
                    result.addAll(keyInValSet);
                }
            }
        }
        return result;
    }

    /**
     * 随机从多个可选的跳出循环的路径中选出一个
     * @param strategies
     * @return
     */
    public Map<String, Value> getOutCycleStrategyByRandom(List<Map<String,Value>> strategies) {
        Map<String,Value> result=new HashMap<String,Value>();
        if(strategies==null||strategies.size()==0){
            return null;
        }
        if(strategies.size()==1){
            return strategies.get(0);
        }
        int len=strategies.size();
        Random random = new Random();
        int num = random.nextInt(len);
        result=strategies.get(num);
        return result;
    }

    /**
     * 从key的valueList中选出一个终结子值
     * @param str
     * @param index
     * @param token
     * @return
     */
    public Value getValueByRandomTerminate(String str, int index, Token token) {
        if(token==null||str==null||str=="")
            return new Value("");
        GrammarCheckService gcs=new GrammarCheckService();
        Value result= gcs.getTerminateValue(str, grammars.get(index), token);//取当前token的终结子值
        return result;
    }
}
