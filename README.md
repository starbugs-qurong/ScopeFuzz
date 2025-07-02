# ScopeFuzz
Testing Compilers with C++ Programs based on Scope-level Mutation

## Parameters
```java -jar ScopeFuzz.jar --help```

## Source code
See the code files in ```source/main``` directory.

## Grammar files
See the grammar files in ```grammar``` directory, ```CPlusLexer.g4``` and ```CPlusParser.g4```. You can alter the content of this grammar, but to ensure that the generated programs are legal, you may need to pay particular attention to potential issues arising from the incorporation of grammar related to scopes and identifiers.

## Source files
See the C/C++ files in ```source``` directory, ```zero-init1.C```, ```Wzero-as-null-pointer-constant-3.C``` and so on. You can change the number of source programs in this directory, and ScopeFuzz will parse and slice the source programs in this directory for insertion into the generated programs.

## Run
Run ScopeFuzz.jar "```java -jar ScopeFuzz.jar --generateNum 10```". 10 of c++ test programs will be generated in this directory: ```generate-output/XXXX-XX-XX/build/cprogram```. 
