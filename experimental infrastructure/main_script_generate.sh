
#!/bin/bash
current_date=$(date +"%Y-%m-%d")_1

java -jar mavenScopeFuzz-1.0-SNAPSHOT-jar-with-dependencies.jar --generateNum 10000 --index $current_date &
