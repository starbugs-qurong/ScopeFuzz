#!/bin/bash

# 删除重复的表头
head -n 1 results_summary.csv > results_diff_flag.csv

awk -F ',' '
NR == 1 {
    # 记录所有运行状态列
    for (i=1; i<=NF; i++) {
        if ($i ~ /_run$/) run_cols[i] = 1
    }
    next
}
{
    # 检查未定义行为
    has_ub = 0
    for (col in run_cols) {
        if ($col == 3) {
            has_ub = 1
            break
        }
    }
    if (has_ub) next
    
    # 检查差异
    delete vals
    for (i=2; i<=NF; i++) vals[$i]++
    if (length(vals) > 1) {
        # 检查奇偶列是否完全一致
        odd_val = even_val = ""
        for (i=2; i<=NF; i++) {
            if (i % 2 == 0) {
                if (even_val == "") even_val = $i
                if ($i != even_val) break
            } else {
                if (odd_val == "") odd_val = $i
                if ($i != odd_val) break
            }
        }
        if (i > NF) next
        print
    }
}' results_summary.csv >> results_diff_flag.csv

# 创建目标目录
mkdir -p type_diff

# 复制符合条件的程序文件
while IFS=, read -r program; do
    if [[ -f build/cprogram/${program}.cpp ]]; then
        cp build/cprogram/${program}.cpp type_diff/
    fi
done < <(tail -n +2 results_diff_flag.csv | cut -d, -f1)