package iscas.ac.grand.main.common;

public class Node {
    String nodeName;//当前节点对应的变量名称 例如B
    Node parent;//当前节点的直接父节点,例如A，A有两种取值方式，一种是C* B? ‘;’  另一种是[0-9]+
    Value value;//当前节点位于哪个取值当中，这个取值对应一个Value类型的变量，例如其value为C* B? ‘;’


    public Node(String nodeName) {
        this.nodeName = nodeName;
    }

    public Node(String nodeName, Node parent,Value value ) {
        this.nodeName = nodeName;
        this.parent = parent;
        this.value = value;
    }

    public String getNodeName() {
        return nodeName;
    }

    public Node getParent() {
        return parent;
    }

    public Value getValue() {
        return value;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
