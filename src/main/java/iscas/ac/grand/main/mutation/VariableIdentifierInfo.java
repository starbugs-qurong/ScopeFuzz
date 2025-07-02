package iscas.ac.grand.main.mutation;

public class VariableIdentifierInfo {
   private String originalVar="";
   private String newName="";
   private int lineNumber=0;


    public String getOriginalVar() {
        return originalVar;
    }

    public void setOriginalVar(String originalVar) {
        this.originalVar = originalVar;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
