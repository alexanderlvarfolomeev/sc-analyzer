package ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.printer.YamlPrinter;
import util.Utils;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Storage {

    private final Path filePath;

    private final CompilationUnit ast;

    private final CallGraph callGraph;

    private final Map<String, ClassOrInterfaceDeclaration> classes;
    private final Set<ClassOrInterfaceDeclaration> unnamedClasses;
    private final Map<String, RecordDeclaration> records;
    private final Set<RecordDeclaration> unnamedRecords;

    private final Set<CallableDeclaration<?>> callables;

    public Storage(CompilationUnit ast, Path filePath) {
        this.ast = ast;
        this.filePath = filePath;

        var classList = ast.findAll(ClassOrInterfaceDeclaration.class);
        unnamedClasses = classList.stream()
                .filter(ciDecl -> ciDecl.getFullyQualifiedName().isEmpty())
                .collect(Collectors.toSet());
        classes = classList.stream()
                .filter(ciDecl -> ciDecl.getFullyQualifiedName().isPresent())
                .collect(Collectors.toMap(ciDecl -> ciDecl.getFullyQualifiedName().orElseThrow(), Function.identity()));

        var recordList = ast.findAll(RecordDeclaration.class);
        unnamedRecords = recordList.stream()
                .filter(rDecl -> rDecl.getFullyQualifiedName().isEmpty())
                .collect(Collectors.toSet());
        records = recordList.stream()
                .filter(rDecl -> rDecl.getFullyQualifiedName().isPresent())
                .collect(Collectors.toMap(rDecl -> rDecl.getFullyQualifiedName().orElseThrow(), Function.identity()));

        callables = ast.findAll(CallableDeclaration.class).stream()
                .map(cDecl -> (CallableDeclaration<?>) cDecl)
                .collect(Collectors.toSet());

        Map<CallableDeclaration<?>, Set<String>> subCalls = callables.stream().collect(
                Collectors.toMap(
                        Function.identity(),
                        c -> {
                            var calls = new HashSet<String>();
                            c.findAll(ObjectCreationExpr.class).stream()
                                    .map(oc -> oc.getType().getNameWithScope())
                                    .forEach(calls::add);
                            c.findAll(MethodCallExpr.class).stream()
                                    .map(MethodCallExpr::getNameAsString)
                                    .forEach(calls::add);
                            c.findAll(MethodReferenceExpr.class).stream()
                                    .map(MethodReferenceExpr::getIdentifier)
                                    .forEach(calls::add);
                            return calls;
                        }
                )
        );
        callGraph = new CallGraph(subCalls);
    }

    public void printAst() {
        YamlPrinter printer = new YamlPrinter(true);
        System.out.println(Utils.createFilepathHeader(filePath));
        System.out.println(printer.output(ast));
        System.out.println();
    }

    public CompilationUnit getAst() {
        return ast;
    }

    public Path getFilePath() {
        return filePath;
    }

    public CallGraph getCallGraph() {
        return callGraph;
    }

    public Map<String, ClassOrInterfaceDeclaration> getClasses() {
        return classes;
    }

    public Set<ClassOrInterfaceDeclaration> getUnnamedClasses() {
        return unnamedClasses;
    }

    public Map<String, RecordDeclaration> getRecords() {
        return records;
    }

    public Set<RecordDeclaration> getUnnamedRecords() {
        return unnamedRecords;
    }

    public Set<CallableDeclaration<?>> getCallables() {
        return callables;
    }

    public record CallGraph(Map<CallableDeclaration<?>, Set<String>> subCalls) {
    }
}
