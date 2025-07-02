package iscas.ac.grand.main.common;

import java.util.Set;

public class Value {
    private String value;//取值文法
    private String valueType;//取值方式 1仅包含另一个token 2含有一个token以及其它字符 3至少包含两个token 4正则表达式 5字符串''
    private Set<String> childrenToken;//此取值方式中包含的子token
    private int terminateLenth;//从此取值到终止值的长度，如果本身是终止值，这个长度为0，否则，取其子token终止长度的最大值。


    public Value(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public Set<String> getChildrenToken() {
        return childrenToken;
    }

    public void setChildrenToken(Set<String> childrenToken) {
        this.childrenToken = childrenToken;
    }

    public int getTerminateLenth() {
        return terminateLenth;
    }

    public void setTerminateLenth(int terminateLenth) {
        this.terminateLenth = terminateLenth;
    }
}
