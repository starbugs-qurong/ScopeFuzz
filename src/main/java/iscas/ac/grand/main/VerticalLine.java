package iscas.ac.grand.main;
public class VerticalLine {
    String c;//字符
    String str;
    int depth;//字符在字符串中的深度

    public VerticalLine(String c, String str, int depth) {
        this.c = c;
        this.str = str;
        this.depth = depth;
    }

    public VerticalLine() {
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
