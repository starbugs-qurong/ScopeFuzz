#!/bin/bash

# 创建目标目录
mkdir -p diff_out_number

# 读取差异文件
clang_diff_file="clang_missing_files.txt"
gxx_special_file="gxx_special_files.txt"

# 临时文件用于存储cpp文件名
cpp_files_to_copy=$(mktemp)

# 合并差异文件，并提取文件名（去除.out后缀）
{
    cat "$clang_diff_file"
    cat "$gxx_special_file"
} | while read -r out_file; do
    # 提取program编号
    program_num=$(echo "$out_file" | sed 's/\.out$//')
    # 构造cpp文件名
    cpp_file="build/cprogram/${program_num}.cpp"
    # 检查cpp文件是否存在
    if [ -f "$cpp_file" ]; then
        echo "$cpp_file" >> "$cpp_files_to_copy"
    fi
done

# 复制cpp文件到目标目录，并记录到txt文件中
output_txt="diff_programs.txt"
echo "请帮我分析以下的程序为什么在GCC和Clang编译器上的行为不一致，在有些编译器或优化选项上生成了可执行文件，而在有些编译器或优化选项上则没有生成可执行文件" > "$output_txt"
echo "" >> "$output_txt" # 空行分隔

while IFS= read -r cpp_file; do
    # 复制文件到目标目录
    cp "$cpp_file" "diff_out_number/"
    # 提取文件名（带路径）用于记录
    file_to_record="diff_out_number/$(basename "$cpp_file")"
    # 记录文件名和文件内容到txt文件
    {
        echo "$file_to_record"
        cat "$file_to_record"
        echo "" # 文件内容后加空行分隔
    } >> "$output_txt"
done < "$cpp_files_to_copy"

# 清理临时文件
rm "$cpp_files_to_copy"

echo "分析结果和程序文件已保存到：$output_txt 和 diff_out_number/ 目录"