package iscas.ac.grand.util;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
 

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CppCodeSlice {

    // 正则表达式模式
    private static final Pattern FUNCTION_PATTERN = Pattern.compile(
        "\\b[a-zA-Z_][a-zA-Z0-9_]*\\s+\\w+\\s*\\([)]*\\)\\s*\\{[}]*\\}");
    private static final Pattern STRUCT_PATTERN = Pattern.compile(
        "struct\\s+\\w+\\s*\\{[}]*\\}");
    private static final Pattern TEMPLATE_PATTERN = Pattern.compile(
        "template\\s*<[>]*>\\s*(class|struct)\\s+\\w+\\s*\\{[}]*\\}");

    public static void main(String[] args) {
        String filePath = "F:\\data-compiler-testing\\slice.cpp";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder code = new StringBuilder();
            while ((line = br.readLine()) != null) {
                code.append(line).append("\n");
            }

            splitCode(code.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void splitCode(String code) {
        System.out.println("Functions found:");
        Matcher functionMatcher = FUNCTION_PATTERN.matcher(code);
        while (functionMatcher.find()) {
            System.out.println(functionMatcher.group());
        }

        System.out.println("\nStructs found:");
        Matcher structMatcher = STRUCT_PATTERN.matcher(code);
        while (structMatcher.find()) {
            System.out.println(structMatcher.group());
        }

        System.out.println("\nTemplates found:");
        Matcher templateMatcher = TEMPLATE_PATTERN.matcher(code);
        while (templateMatcher.find()) {
            System.out.println(templateMatcher.group());
        }
    }
}