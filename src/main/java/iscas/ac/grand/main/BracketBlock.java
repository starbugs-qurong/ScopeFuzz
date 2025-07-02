package iscas.ac.grand.main;

public class BracketBlock {
    private int startIndex;
    private int endIndex;
    private String BlockContent;
    private VerticalLine verticalLine;

    public BracketBlock(int startIndex, int endIndex, String blockContent, VerticalLine verticalLine) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        BlockContent = blockContent;
        this.verticalLine = verticalLine;
    }

    public BracketBlock() {

    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public String getBlockContent() {
        return BlockContent;
    }

    public void setBlockContent(String blockContent) {
        BlockContent = blockContent;
    }

    public VerticalLine getVerticalLine() {
        return verticalLine;
    }

    public void setVerticalLine(VerticalLine verticalLine) {
        this.verticalLine = verticalLine;
    }
}
