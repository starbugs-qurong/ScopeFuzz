#!/bin/bash

MAX_RUNTIME=10
UB_DIR="undefined_behavior"
mkdir -p "$UB_DIR"  # 提前创建目标目录

for compiler_dir in build/compiler/*; do
  echo "Running executables in $compiler_dir..."
  total_elapsed_time=0

  output_dir="$compiler_dir/output"
  mkdir -p "$output_dir"

  run_failures_dir="$compiler_dir/run_failures"
  mkdir -p "$run_failures_dir"
  RUN_STATUS_FILE="$run_failures_dir/run_status.csv"
  echo "program,run_status" > "$RUN_STATUS_FILE"

  for executable in "$compiler_dir/executables"/*; do
    exec_name="$(basename $executable)"
    start_time=$(date +%s.%N)
    timeout "$MAX_RUNTIME" ./"$executable" > "$output_dir/$exec_name.out" 2> "$output_dir/$exec_name.err"
    exit_status=$?
    end_time=$(date +%s.%N)
    elapsed_time=$(echo "$end_time - $start_time" | bc)

    # 检测未定义行为
    if grep -q "runtime error: .* undefined behavior" "$output_dir/$exec_name.err"; then
      run_status=3
      # 复制源程序到UB目录（保留目录结构）
      src_program="build/cprogram/${exec_name}.cpp"
      if [ -f "$src_program" ]; then
        rsync -a --relative "$src_program" "$UB_DIR/"  # 使用相对路径保持结构
      fi
    elif [ $exit_status -eq 124 ]; then
      run_status=0
    elif [ $exit_status -ne 0 ]; then
      run_status=1
    else
      run_status=2
    fi
    
    echo "$exec_name,$run_status" >> "$RUN_STATUS_FILE"
  done
done

# 生成汇总表格（保持原逻辑）
SUMMARY_FILE="results_summary.csv"
echo -n "program" > "$SUMMARY_FILE"

compiler_configs=()
for config_dir in build/compiler/*; do
  if [ -d "$config_dir" ]; then
    config=$(basename "$config_dir")
    compiler_configs+=("$config")
  fi
done

for config in "${compiler_configs[@]}"; do
  echo -n ",${config}_compile,${config}_run" >> "$SUMMARY_FILE"
done
echo >> "$SUMMARY_FILE"

PROGRAMS=$(find build/cprogram -name "*.cpp" -exec basename {} .cpp \; | sort -u)

for program in $PROGRAMS; do
  echo -n "$program" >> "$SUMMARY_FILE"
  for config in "${compiler_configs[@]}"; do
    config_dir="build/compiler/$config"
    compile_status_file="$config_dir/compile_status.csv"
    run_status_file="$config_dir/run_failures/run_status.csv"

    compile_status=1
    if [ -f "$compile_status_file" ]; then
      line=$(grep "^${program}," "$compile_status_file")
      [ -n "$line" ] && compile_status=$(echo "$line" | cut -d',' -f2)
    fi

    run_status=1
    if [ -f "$run_status_file" ]; then
      line=$(grep "^${program}," "$run_status_file")
      [ -n "$line" ] && run_status=$(echo "$line" | cut -d',' -f2)
    fi

    echo -n ",$compile_status,$run_status" >> "$SUMMARY_FILE"
  done
  echo >> "$SUMMARY_FILE"
done

echo "结果已汇总至 $SUMMARY_FILE"
echo "包含未定义行为的程序已复制到：$UB_DIR"