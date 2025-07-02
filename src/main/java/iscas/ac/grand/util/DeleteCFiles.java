package iscas.ac.grand.util;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class DeleteCFiles {
    public static void main(String[] args) {
        // 指定要扫描的目录路径
        Path directory = Paths.get("F:\\data-compiler-testing\\2024-12-16cpp-gcc-llvm-testsuit\\test-suit-all");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(
                directory, "*.c")) { // 过滤出所有后缀为 .c 的文件

            for (Path entry : stream) {
                System.out.println("Deleting file: " + entry.getFileName());
                Files.delete(entry); // 删除文件
            }

            System.out.println("All .c files in the directory have been deleted.");

        } catch (IOException | DirectoryIteratorException ex) {
            System.err.println(ex);
        }
    }
}