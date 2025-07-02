package iscas.ac.grand.main.common;
import java.util.List;
import java.util.Map;

public class Token {
    private String tokenName;//变量名
    private List<Value> valueList;//变量取值方式
    private String tokenType;//变量类型 非递归有子token1 递归2 跨层递归3 无子token4
    private int cycleStep;//记录循环步长 0对应非递归 如果子value中含有当前token,那么这个长度为1，每多一层加1 无子token对应-1
    private Node cycleNode;//跨层循环变量循环的终点node，其上取值链 例如 A：null  B：B+C  D：D 'e'*   A:A*(F)  变量链为A B D A
    private Value outCycleValue;//跳出循环的固定取值，如果取值方式valueList中有终止子值，那么这个Value设置为终止子值（如果有多个随机选一个），否则设置为各个子value跳出长度最小的子Value。
    private int outCycleValueLen;//跳出循环的固定取值及其到达终止值的长度，如果取值方式valueList中有终止子值，那么这个长度设置为1，否则设置为各个子value跳出长度的最小值。

    public Token(String tokenName){
        this.tokenName=tokenName;
    }

    public String getTokenName() {
        return tokenName;
    }

    public List<Value> getValueList() {
        return valueList;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public void setValueList(List<Value> valueList) {
        this.valueList = valueList;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getCycleStep() {
        return cycleStep;
    }

    public void setCycleStep(int cycleStep) {
        this.cycleStep = cycleStep;
    }

    public Node getCycleNode() {
        return cycleNode;
    }

    public void setCycleNode(Node cycleNode) {
        this.cycleNode = cycleNode;
    }

    public Value getOutCycleValue() {
        return outCycleValue;
    }

    public void setOutCycleValue(Value outCycleValue) {
        this.outCycleValue = outCycleValue;
    }

    public int getOutCycleValueLen() {
        return outCycleValueLen;
    }

    public void setOutCycleValueLen(int outCycleValueLen) {
        this.outCycleValueLen = outCycleValueLen;
    }
}
