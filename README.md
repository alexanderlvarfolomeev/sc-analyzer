# SC-Analizer

## About

SC-Analizer is static code analizer for Java, which uses JavaParser to gain compilation units' AST.

### Run arguments. Existing flags

| Flags                 | Description                                            |
| --------------------- | ------------------------------------------------------ |
| -a, --ast             | Print AST for each processed file                      | 
| -e, --errors          | Fail on parse exceptions and unmatched rules           |
| -i, --input \<path\>  | Specify path to analyze, default is working directory  |
| -h, --help            | Show help message                                      |

### Add new rule

Add [AnalyzerRule](src/main/java/analyzer/rules/AnalyzerRule.java) subtype to [rules](src/main/java/analyzer/rules/) directory.
Rule implementation should contain default constructor.

## Build

### Windows
```
./gradlew.bat build
```
### Linux / Mac OS
```
./gradlew build
```

## Run

### Windows
```
./gradlew.bat run --args="<analyzer args>"

./gradlew.bat run --args="--help"
```
### Linux / Mac OS
```
./gradlew run --args="<analyzer args>"

./gradlew run --args="--help"
```

### Run with built JAR
```
java -jar built-shadow-jar.jar <analyzer args>
```
