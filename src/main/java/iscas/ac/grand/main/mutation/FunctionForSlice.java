package iscas.ac.grand.main.mutation;

import iscas.ac.grand.main.common.FunctionSymbolRecord;
import iscas.ac.grand.main.common.ScopeTree;
import iscas.ac.grand.main.common.SymbolRecord;
import iscas.ac.grand.main.common.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * qurong
 * 2024-12-17
 * 用于切片的函数类型
 */
public class FunctionForSlice extends FunctionSymbolRecord {
    private String functionBody="";//函数体（包括函数头） 切片函数片段用
    private int startLineNum=0;//作用域开始行
    private int endLineNum=0;//作用域结束行
    private List<Integer> insertPositions=new ArrayList<>();//此作用域内可以插入其它作用域代码的位置（行号），可以是多个，先把作用域花括号{之后和}所在的行作为可插入行
    private String funCall="";//函数调用语句及关联语句
    private CppProgram cppProgram=null;//函数所属的程序

    public String getFunctionBody() {
        return functionBody;
    }

    public void setFunctionBody(String functionBody) {
        this.functionBody = functionBody;
    }

    public FunctionForSlice(){

    }

    public String getFunCall() {
        return funCall;
    }

    public void setFunCall(String funCall) {
        this.funCall = funCall;
    }

    public int getStartLineNum() {
        return startLineNum;
    }

    public void setStartLineNum(int startLineNum) {
        this.startLineNum = startLineNum;
    }

    public int getEndLineNum() {
        return endLineNum;
    }

    public void setEndLineNum(int endLineNum) {
        this.endLineNum = endLineNum;
    }

    public List<Integer> getInsertPositions() {
        return insertPositions;
    }

    public void setInsertPositions(List<Integer> insertPositions) {
        this.insertPositions = insertPositions;
    }

    public FunctionForSlice(String functionBlock, ScopeTree rootScope) {
//        this.returnType=new Type(0,"void");
        this.ID=generateUniqueId();
        int id=generateUniqueID();
        this.scope=new ScopeTree(id,rootScope);
//        this.name=getNameByBlock(functionBlock);
//        this.parameterList = getParameterListByBlock(functionBlock);
//        getInfoByBlock(functionBlock);
        sliceFun(functionBlock);
        this.functionBody=functionBlock;
    }

    public void sliceFun(String functionBlock){
        String input = functionBlock;

        // 查找第一个左括号的位置
        int leftBracketIndex = input.indexOf('(');
        if (leftBracketIndex == -1) {
            //System.out.println("未找到左括号");
            return;
        }

        // 提取括号前的字符串并保存到B
        String typeAndNameStr = input.substring(0, leftBracketIndex);
        if(typeAndNameStr.contains(" ")){
            typeAndNameStr=typeAndNameStr.trim();
            String typeAndName[]=typeAndNameStr.split(" ");
            if(typeAndName.length==2){
                this.returnType=new Type(typeAndName[0].trim());
                this.name=typeAndName[1].trim();
            }else{
                return;
            }
        }

        // 查找第一个右括号的位置
        int rightBracketIndex = input.indexOf(')');
        if (rightBracketIndex == -1) {
            //System.out.println("未找到匹配的右括号");
            return;
        }
        // 确保右括号在左括号之后
        if (rightBracketIndex <= leftBracketIndex) {
            //System.out.println("右括号位置不正确");
            return;
        }

        // 提取括号内的字符串并保存到A
        String parameterList = input.substring(leftBracketIndex + 1, rightBracketIndex);
        // 拆分参数列表并打印每个参数的类型和名称
        if (!parameterList.isEmpty()) {
            String[] parameters = parameterList.split(",");
            Pattern parameterPattern = Pattern.compile("(\\w+(?:\\s*\\*)?(?:\\s*&)?\\s+)(\\w+)");

            for (String parameter : parameters) {
                parameter = parameter.trim();
                Matcher parameterMatcher = parameterPattern.matcher(parameter);

                if (parameterMatcher.find()) {
                    // 提取参数类型（考虑指针和引用）
                    String paramType = parameterMatcher.group(1).trim();
                    Type paraType=new Type(paramType);
                    this.parameterList.add(paraType);
                    // 提取参数名
                    String paramName = parameterMatcher.group(2).trim();
                    SymbolRecord sr=new SymbolRecord(paramName, paraType, generateUniqueId(), "var", this.scope, "");
                    this.parameterIdentifiers.add(sr);
                    // 打印参数类型和名称
                    //System.out.println("Parameter Type: " + paramType + ", Parameter Name: " + paramName);
                }
            }
        }

        // 输出结果
        //System.out.println("字符串A（括号内的内容）: " + A);
        //System.out.println("字符串B（括号前的字符串）: " + B);
    }

    private List<Type> getInfoByBlock(String functionBlock) {
        List<Type> result = new ArrayList<Type>();
        if(functionBlock.contains("(")&&functionBlock.contains(")")){
            // 提取函数名和参数列表
            Pattern functionPattern = Pattern.compile("(\\w+\\s+)(\\w+)\\((.*)\\)\\s*;");
            Matcher functionMatcher = functionPattern.matcher(functionBlock);

            if (functionMatcher.find()) {
                // 提取返回值类型
                String returnType = functionMatcher.group(1).trim();
                this.returnType=new Type(returnType);
                // 提取函数名
                String functionName = functionMatcher.group(2).trim();
                this.name=functionName;
                // 提取参数列表
                String parameterList = functionMatcher.group(3).trim();

                // 打印返回值类型和函数名
                //System.out.println("Return Type: " + returnType);
                //System.out.println("Function Name: " + functionName);

                // 拆分参数列表并打印每个参数的类型和名称
                if (!parameterList.isEmpty()) {
                    String[] parameters = parameterList.split(",");
                    Pattern parameterPattern = Pattern.compile("(\\w+(?:\\s*\\*)?(?:\\s*&)?\\s+)(\\w+)");

                    for (String parameter : parameters) {
                        parameter = parameter.trim();
                        Matcher parameterMatcher = parameterPattern.matcher(parameter);

                        if (parameterMatcher.find()) {
                            // 提取参数类型（考虑指针和引用）
                            String paramType = parameterMatcher.group(1).trim();
                            Type paraType=new Type(paramType);
                            this.parameterList.add(paraType);
                            // 提取参数名
                            String paramName = parameterMatcher.group(2).trim();
                            SymbolRecord sr=new SymbolRecord(paramName, paraType, generateUniqueId(), "var", this.scope, "");
                            this.parameterIdentifiers.add(sr);
                            // 打印参数类型和名称
                            //System.out.println("Parameter Type: " + paramType + ", Parameter Name: " + paramName);
                        }
                    }
                }
            }
        }
        return result;
    }
    public static int generateUniqueID() {
        // 获取当前时间戳（毫秒）
        long timestamp = System.currentTimeMillis();
        Random random = new Random();

        // 截取时间戳的一部分，并将其转换为int类型
        // 这里我们取时间戳的最低32位（实际上int就是32位的）
        int idPartFromTimestamp = (int) (timestamp & 0xFFFFFFFFL);

        // 生成一个随机数，用于增加唯一性
        int randomPart = random.nextInt(Integer.MAX_VALUE);

        // 结合时间戳部分和随机数部分生成最终ID
        // 这里我们使用简单的异或运算，但你也可以使用其他更复杂的哈希函数
        int uniqueID = idPartFromTimestamp^randomPart;

        // 返回生成的唯一ID
        return uniqueID;
    }

    public static String generateUniqueId() {
        // 时间戳加上一个随机数（可选）
        long currentTime = System.currentTimeMillis();
        long randomNumber = (long) (Math.random() * 1000000);
        return String.valueOf(currentTime) + String.valueOf(randomNumber);
    }

    public CppProgram getCppProgram() {
        return cppProgram;
    }

    public void setCppProgram(CppProgram cppProgram) {
        this.cppProgram = cppProgram;
    }
}
