#!/bin/bash
###
 # @Descripttion: 
 # @Author: 2022E8015082045
 # @Date: 2025-01-20 22:25:38
 # @LastEditors: 2025
 # @LastEditTime: 2025-01-20 01:20:05
 # @FilePath: /home/qurong/compiler-testing/automatic-testing-test/generate-output/1/compile_all.sh
### 

# 编译器列表
compilers=("g++-10" "clang++-10" "clang++-11" "clang++-12" "clang++-13" "clang++-14" "clang++-15")

# 优化选项数组
OPTIMIZATION_LEVELS=("-O0" "-O1" "-O2" "-O3" "-Os")
 
# 编译所有文件并测试所有优化级别
for compiler in "${compilers[@]}"; do
  for opt_level in "${OPTIMIZATION_LEVELS[@]}"; do 
    # 编译输出的可执行文件目录
    FAILURES_DIR="build/compiler/${compiler}-${opt_level#*-}/output"
    FILE_COUNT=$(find "$FAILURES_DIR" -type f | wc -l)
    # 输出文件数量
    echo "The number of files in the directory '$FAILURES_DIR' is: $FILE_COUNT"
    done
done