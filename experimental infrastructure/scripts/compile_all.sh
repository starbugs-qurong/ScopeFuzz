#!/bin/bash

# 编译器列表
compilers=("g++-10" "clang++-10" "clang++-11" "clang++-12" "clang++-13" "clang++-14" "clang++-15")

# 优化选项数组
OPTIMIZATION_LEVELS=("-O0" "-O1" "-O2" "-O3" "-Os")

# 编译所有文件并测试所有优化级别
for compiler in "${compilers[@]}"; do
  for opt_level in "${OPTIMIZATION_LEVELS[@]}"; do 
    # 编译并测试优化版本
    echo "Compiling with $compiler and optimization level $opt_level..."
    start_time=$(date +%s)
    make CXX=$compiler clean
    make CXX=$compiler OPT_CFLAGS="$opt_level" -j16
    end_time=$(date +%s)
    elapsed_time=$(($end_time - $start_time))
    elapsed_minutes=$(awk "BEGIN {printf \"%.2f\",${elapsed_time}/60}")
    echo "Elapsed time for $compiler with $opt_level: $elapsed_minutes minutes"

    # 处理编译失败的文件
    COMPILER_DIR="build/compiler/${compiler}-${opt_level#*-}"
    EXECUTABLES_DIR="${COMPILER_DIR}/executables"
    FAILURES_DIR="${COMPILER_DIR}/compiler_failures"
    mkdir -p "$FAILURES_DIR"

    # 生成编译状态文件
    COMPILE_STATUS_FILE="${COMPILER_DIR}/compile_status.csv"
    echo "program,compile_status" > "$COMPILE_STATUS_FILE"
    SOURCES=$(find build/cprogram -name "*.cpp" -exec basename {} .cpp \;)
    for source in $SOURCES; do
      executable="${EXECUTABLES_DIR}/${source}"
      source_cpp="${source}.cpp"
      if [ -f "$executable" ]; then
        echo "$source,2" >> "$COMPILE_STATUS_FILE"
      else
        if grep -q "^${source_cpp}$" "${FAILURES_DIR}/compile_timeouts.log"; then
          echo "$source,0" >> "$COMPILE_STATUS_FILE"
        elif grep -q "^${source_cpp}$" "${FAILURES_DIR}/compile_failures.log"; then
          echo "$source,1" >> "$COMPILE_STATUS_FILE"
        else
          echo "$source,1" >> "$COMPILE_STATUS_FILE"
        fi
      fi
    done
  done
done