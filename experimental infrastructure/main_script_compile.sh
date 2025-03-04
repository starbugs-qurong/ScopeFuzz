#!/bin/bash

current_date=$(date +"%Y-%m-%d")_1

target_dir="generate-output/$current_date"
mkdir -p "$target_dir"
 
cp generate-output/5/compile_all.sh "$target_dir/"
cp generate-output/5/run_and_time.sh "$target_dir/"
cp generate-output/5/Makefile "$target_dir/"
cp generate-output/5/filter_script.sh "$target_dir/" 
cp generate-output/5/count_valid_rate.sh "$target_dir/" 
cp generate-output/5/select_out_no_insistent.sh "$target_dir/"  
cp generate-output/5/generate_diff_out_number_txt.sh "$target_dir/" 
cp generate-output/5/merge_files.sh "$target_dir/" 


cd generate-output/$current_date/ || exit
./compile_all.sh
./run_and_time.sh
./filter_script.sh
./count_valid_rate.sh
./select_out_no_insistent.sh
./generate_diff_out_number_txt.sh
./merge_files.sh

