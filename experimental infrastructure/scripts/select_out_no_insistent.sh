#!/bin/bash

# 创建临时工作目录
mkdir -p tmp

# 定义公共参数
OPT="-O0"   # 选择任意一个优化级别进行分析（因为每个编译器内部各优化级别文件数相同）

########################################################################
# 第一部分：找出Clang高版本缺失的文件（2621-2620=1）
########################################################################

# 生成存在文件列表（clang++-10/11/15）
clang_keep=("clang++-10" "clang++-11" "clang++-15")
for compiler in "${clang_keep[@]}"; do
    dir="build/compiler/${compiler}-${OPT#*-}/output"
    find "$dir" -type f -printf "%f\n"
done | sort | uniq > tmp/keep_clang.list

# 生成缺失文件列表（clang++-12/13/14）
clang_lost=("clang++-12" "clang++-13" "clang++-14")
for compiler in "${clang_lost[@]}"; do
    dir="build/compiler/${compiler}-${OPT#*-}/output"
    find "$dir" -type f -printf "%f\n"
done | sort | uniq > tmp/lost_clang.list

# 找出差异文件
comm -23 tmp/keep_clang.list tmp/lost_clang.list > clang_missing_files.txt
echo "[CLANG差异分析] 找到差异文件数量: $(wc -l < clang_missing_files.txt)"

########################################################################
# 第二部分：找出G++特有文件（2638-2620=18）
########################################################################

# 生成G++文件列表
gxx_compiler="g++-10"
dir="build/compiler/${gxx_compiler}-${OPT#*-}/output"
find "$dir" -type f -printf "%f\n" | sort > tmp/gxx_files.list

# 生成所有Clang基准文件列表（取clang++-12作为基准）
clang_base=("clang++-12" "clang++-13" "clang++-14")  # 任选一个即可，因为它们文件数相同
for compiler in "${clang_base[@]}"; do
    dir="build/compiler/${compiler}-${OPT#*-}/output"
    find "$dir" -type f -printf "%f\n"
done | sort | uniq > tmp/base_clang.list

# 找出G++特有文件
comm -23 tmp/gxx_files.list tmp/base_clang.list > gxx_special_files.txt
echo "[G++差异分析] 找到特有文件数量: $(wc -l < gxx_special_files.txt)"

# 清理临时文件（如需保留可注释掉）
rm -rf tmp

echo "分析结果已保存到："
echo "• Clang版本差异文件: clang_missing_files.txt"
echo "• G++特有文件: gxx_special_files.txt"