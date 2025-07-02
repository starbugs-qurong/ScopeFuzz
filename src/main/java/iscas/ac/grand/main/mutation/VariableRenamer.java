package iscas.ac.grand.main.mutation;

import java.util.*;
import java.util.regex.*;

/**
 * qurong
 * 2024-12-31
 * 已弃用
 */
public class VariableRenamer {

    public static void main(String[] args) {
        String cppCode = "int main() {\n" +
                "    int a = 5;\n" +
                "    float b = a+3.14;\n" +
                "    double c = b+2.71;\n" +
                "    char d = 'x';\n" +
                "    a = a + b;\n" +
                "    return 0;\n" +
                "}\n";

        List<String> lines = Arrays.asList(cppCode.split("\n"));
        Map<String, Integer> variableToIndex = new HashMap<>();
        Map<Integer, String> indexToVariable = new LinkedHashMap<>();
        Map<Integer, List<Integer>> lineNumberToVariables = new LinkedHashMap<>();

        Pattern variablePattern = Pattern.compile("\\b([a-zA-Z_][a-zA-Z0-9_]*)\\b");

        for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            Matcher matcher = variablePattern.matcher(line);

            while (matcher.find()) {
                String variable = matcher.group(1);

                // Check if the match is a valid variable name and not part of a keyword or function
                if (isValidVariableName(variable)) {
//                    if (!variableToIndex.containsKey(variable)) {//考虑记录重命的标识符为同一个 但是我们不需要记录这项内容
                        int newIndex = variableToIndex.size() + 1;
                        String newName = "V" + newIndex;
                        variableToIndex.put(variable, newIndex);
                        indexToVariable.put(newIndex, newName);
                        if (!lineNumberToVariables.containsKey(lineNumber)) {
                            lineNumberToVariables.put(lineNumber, new ArrayList<>());
                        }
                        lineNumberToVariables.get(lineNumber).add(newIndex);
//                    }

                    // Replace the variable in the current line with the new name
                    // Note: This will replace all occurrences in the line.
                    // For more precise replacement, you'd need a more sophisticated parser.
                    line = line.replaceAll("\\b" + Pattern.quote(variable) + "\\b", indexToVariable.get(variableToIndex.get(variable)));
                }
            }

            lines.set(lineNumber, line);
        }

        // Output the transformed code
        System.out.println("Transformed Code:");
        for (String line : lines) {
            System.out.println(line);
        }

        // Output variable indices and line numbers
        System.out.println("\nVariable Indices and Line Numbers:");
        for (Map.Entry<Integer, String> entry : indexToVariable.entrySet()) {
            int index = entry.getKey();
            String newName = entry.getValue();
            List<Integer> lineNumbers = new ArrayList<>();
            for (Map.Entry<Integer, List<Integer>> lnEntry : lineNumberToVariables.entrySet()) {
                if (lnEntry.getValue().contains(index)) {
                    lineNumbers.add(lnEntry.getKey() + 1); // +1 to convert to 1-based line number
                }
            }
            System.out.println("Variable " + newName + " (Index " + index + "): Lines " + lineNumbers.get(0));
            if(lineNumbers!=null&&lineNumbers.size()>1){
                for(int line:lineNumbers){
                    System.out.println( "Lines " + line);
                }
            }
        }
    }

    private static boolean isValidVariableName(String name) {
        // Simple check to avoid replacing parts of keywords or function names
        // This is not exhaustive and might need improvement for real-world use cases
        String[] keywords = {"int", "float", "double", "char", "return", "if", "else", "for", "while", "main", "void"};
        for (String keyword : keywords) {
            if (keyword.equals(name)) {
                return false;
            }
        }
        return true;
    }
}