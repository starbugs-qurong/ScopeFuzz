package iscas.ac.grand.main.mutation;
import java.util.*;
import java.util.regex.*;

public class IdentifierRenaming {

        private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\b([a-zA-Z_][a-zA-Z0-9_]*)\\b");

//        public static void main(String[] args) {
//            String cppCode = "int a = 5;\n" +
//                    "a = a + 1;\n" +
//                    "int b = a - c;\n" +
//                    "double c = 3.14;\n";
//            IdentifierRenaming ir=new IdentifierRenaming();
//            ir.getVariableIdentifierInfo(new ScopeBlockForSlice(cppCode));
//        }

    /**
     * qurong
     * 2024-12-31
     * 从作用域中拆分出变量名称和位置
     * @param sbfs
     * @return
     */
    public List<VariableIdentifierInfo> getVariableIdentifierInfo(ScopeBlockForSlice sbfs) {
        List<VariableIdentifierInfo> result = new ArrayList<>();
//        String cppCode = "int a = 5;\n" +
//                "a = a + 1;\n" +
//                "int b = a - c;\n" +
//                "double c = 3.14;\n";

        String cppCode= sbfs.getScopeBodyFragment();
        sbfs.setOriginalScopeStr(cppCode);
//        System.out.println("Original Code:");
//        System.out.println(cppCode);
        Map<String, List<String>> variableReplacements = new HashMap<>();
        Map<String, Integer> variableLineNumbers = new HashMap<>();
        StringBuilder transformedCode = new StringBuilder();
        int variableCounter = 1;

        String[] lines = cppCode.split("\n");
        for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
            String line = lines[lineNumber];
            Matcher matcher = VARIABLE_PATTERN.matcher(line);

            Set<String> variablesInLine = new HashSet<>();
            StringBuilder newLine = new StringBuilder();
            int lastMatchEnd = 0;

            while (matcher.find()) {
                String variable = matcher.group(1);
                if (isValidVariableName(variable)) {

                    // Check if the variable has already been replaced in this line
                    if (!variablesInLine.contains(variable)) {
                        variablesInLine.add(variable);

                        // Generate a new variable name
                        String newName = "V" + variableCounter;

                        // Store the replacement and line number
                        variableReplacements.computeIfAbsent(variable, k -> new ArrayList<>()).add(newName);
                        if (!variableLineNumbers.containsKey(newName)) {
                            variableLineNumbers.put(newName, lineNumber + 1); // 1-based line number
                        }

                        // Replace the variable in the line
                        int matchStart = matcher.start();
                        int matchEnd = matcher.end();
                        newLine.append(line, lastMatchEnd, matchStart)
                                .append(newName);
                        lastMatchEnd = matchEnd;

                        // Increment the variable counter for the next unique variable
                        variableCounter++;
                    }
                }
            }

            // Append the remaining part of the line (after the last match)
            newLine.append(line.substring(lastMatchEnd));
            transformedCode.append(newLine).append("\n");
        }

        // Output the transformed code
//        System.out.println("Transformed Code:");
        List<String> li=sbfs.getRenameScopeStr();
        li.add(transformedCode.toString());
        sbfs.setRenameScopeStr(li);
//        System.out.println(transformedCode.toString());

        // Output the variable replacements and line numbers
//        System.out.println("\nVariable Replacements and Line Numbers:");
        for (Map.Entry<String, List<String>> entry : variableReplacements.entrySet()) {
            String originalVar = entry.getKey();
            List<String> replacements = entry.getValue();
            for (int i = 0; i < replacements.size(); i++) {
                String newName = replacements.get(i);
                int lineNumber = variableLineNumbers.get(newName);
                VariableIdentifierInfo vii=new VariableIdentifierInfo();
                vii.setOriginalVar(originalVar);
                vii.setNewName(newName);
                vii.setLineNumber(lineNumber);
                result.add(vii);
                System.out.println(originalVar + " (as " + newName + ") at line " + lineNumber);
            }
        }
        return result;
    }
        static boolean isValidVariableName(String name) {
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
                if (keyword.equals(name)) {
                    return false;
                }
            }
            return true;
        }
    }