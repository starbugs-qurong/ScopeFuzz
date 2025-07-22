# SCOPE++
Efficient Scope-aware Mutation for Generating Valid C++ Programs

##SCOPE++.jar Usage Instructions

### Help Command
```java -jar SCOPE++.jar --help```

### Example Command
```java -jar SCOPE++.jar  --generateNum 10  --mutationStrategy main-mutation```
  
### Source code
See the code files in ```src/main``` directory.

### Grammar files
See the grammar files in ```grammar``` directory, ```CPlusLexer.g4``` and ```CPlusParser.g4```. You can alter the content of this grammar, but to ensure that the generated programs are legal, you may need to pay particular attention to potential issues arising from the incorporation of grammar related to scopes and identifiers.

### Source files
See the C/C++ files in ```source``` directory, ```zero-init1.C```, ```Wzero-as-null-pointer-constant-3.C``` and so on. You can change the number of source programs in this directory, and ScopeFuzz will parse and slice the source programs in this directory for insertion into the generated programs.

### Run
Run ScopeFuzz.jar "```java -jar ScopeFuzz.jar --generateNum 10```". 10 of c++ test programs will be generated in this directory: ```generate-output/XXXX-XX-XX/build/cprogram```. 



##Source Code Usage Instructions
The main function is located in this file: ```src/main/java/anonymous/ac/grand/gui/MainClassForGeneration.java```. If you run it will generate 10 C++ mutated programs in ```generate-output``` directory. You can change the number of generated programs by this variable ```generateNum```.


##Source C++ Programs Updating
If you want update the source C++ programs for mutation, you can insert C++ programs into directory ```source```, and then rerun this java file  ```src/main/java/anonymous/ac/grand/main/mutation/BraceMatcher.java``` .
