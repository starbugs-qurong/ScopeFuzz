#!/bin/bash

current_date=$(date +"%Y-%m-%d")_1

target_dir="generate-output/$current_date"
mkdir -p "$target_dir"
 
cp generate-output/scripts/compile_all.sh "$target_dir/"
cp generate-output/scripts/run_and_time.sh "$target_dir/"
cp generate-output/scripts/Makefile "$target_dir/"
cp generate-output/scripts/filter_script.sh "$target_dir/" 
cp generate-output/scripts/count_valid_rate.sh "$target_dir/" 
cp generate-output/scripts/select_out_no_insistent.sh "$target_dir/"  
cp generate-output/scripts/generate_diff_out_number_txt.sh "$target_dir/" 
cp generate-output/scripts/merge_files.sh "$target_dir/" 


cd generate-output/$current_date/ || exit
./compile_all.sh
./run_and_time.sh
./filter_script.sh
./count_valid_rate.sh
./select_out_no_insistent.sh
./generate_diff_out_number_txt.sh
./merge_files.sh

