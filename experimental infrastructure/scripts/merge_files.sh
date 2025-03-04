#!/bin/bash

# 定义排除列表
exclude_list=(
    "attr-assume3.C"
    "attr-assume11.C"
    "attr-carries_dependency1.C"
    "attr-likely3.C"
    "auto-fn59.C"
    "bad_array_new1.C"
    "bad_array_new2.C"
    "constexpr-if9.C"
    "constexpr-label.C"
    "constexpr-lifetime7.C"
    "constexpr-nonlit1.C"
    "constexpr-nonlit2.C"
    "constexpr-static4.C"
    "decltype-auto1.C"
    "decltype-auto5.C"
    "decomp52.C"
    "decomp48.C"
    "enum40.C"
    "gen-attrs-4.C"
    "gen-attrs-39-1.C"
    "gen-attrs-53.C"
    "gen-attrs-72.C"
    "gen-attrs-79.C"
    "gen-attrs-80.C"
    "gen-attrs-81.C"
    "initlist128.C"
    "lambda-108829.C"
    "lambda-70383.C"
    "lambda-generic-variadic.C"
    "lambda-init2.C"
    "lambda-vla2.C"
    "lambda-vla3.C"
    "name-independent-decl1.C"
    "noexcept04.C"
    "pr83993.C"
    "ucn1.C"
    "Wnarrowing15.C"
    "Wnarrowing4.C" 
)

# 合并目录 type_diff 下的文件到 type_diff.txt
{
    echo "Content of type_diff directory:"
    echo
} > type_diff.txt

for file in type_diff/*; do
    if [[ -f "$file" ]]; then
        # 检查文件是否包含排除列表中的内容
        excluded=false
        for pattern in "${exclude_list[@]}"; do
            if grep -q "$pattern" "$file"; then
                excluded=true
                # 修改文件名
                mv "$file" "${file%.*}_${pattern}.${file##*.}"
                break
            fi
        done

        if [ "$excluded" = false ]; then
            # 将文件内容添加到 type_diff.txt
            echo "File: $(basename "$file")"
            echo
            cat "$file"
            echo
            echo "----------------------------------------"
            echo
        fi
    fi
done >> type_diff.txt

# 合并目录 diff_out_number 下的文件到 diff_out_number.txt
{
    echo "Content of diff_out_number directory:"
    echo
} > diff_out_number.txt

for file in diff_out_number/*; do
    if [[ -f "$file" ]]; then
        # 检查文件是否包含排除列表中的内容
        excluded=false
        for pattern in "${exclude_list[@]}"; do
            if grep -q "$pattern" "$file"; then
                excluded=true
                # 修改文件名
                mv "$file" "${file%.*}_${pattern}.${file##*.}"
                break
            fi
        done

        if [ "$excluded" = false ]; then
            # 将文件内容添加到 diff_out_number.txt
            echo "File: $(basename "$file")"
            echo
            cat "$file"
            echo
            echo "----------------------------------------"
            echo
        fi
    fi
done >> diff_out_number.txt