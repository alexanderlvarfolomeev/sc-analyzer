# SC-Analyzer

## About

SC-Analyzer is static code analyzer for Java, which uses JavaParser to gain compilation units' AST.

### Run arguments. Existing flags

| Flags                | Description                                           |
|----------------------|-------------------------------------------------------|
| -a, --ast            | Print AST for each processed file                     | 
| -e, --errors         | Fail on parse exceptions and unmatched rules          |
| -i, --input \<path\> | Specify path to analyze, default is working directory |
| -h, --help           | Show help message                                     |

### Existing rules

1. **MultiVariableDeclarationRule**

    Checks code doesn't contain multivariable declaration (`int x = 0, y = 0`)



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
Create JAR
```
./gradlew shadowJar
```
JAR location: <repo root>/build/libs

Run JAR
```
java -jar analyzer-1.0-SNAPSHOT-all.jar <analyzer args>
```
