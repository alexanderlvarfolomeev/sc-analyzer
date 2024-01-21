package analyzer;

import analyzer.config.Config;
import analyzer.rules.AnalyzerRule;
import ast.Storage;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import org.reflections.Reflections;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Analyzer {
    private final Config config;
    private final List<Storage> storages;

    private final List<AnalyzerRule> rules;

    public Analyzer(Config config) {
        this.config = config;
        rules = findRules();
        storages = parseStorages(config.root());
    }

    public DefectStorage start() {
        if (config.printAST()) {
            storages.forEach(Storage::printAst);
            System.out.println();
        }

        var defects = storages.stream().collect(
                TreeMap<Path, List<AnalyzerRule.Defect>>::new,
                (t, s) -> {
                    var ds = rules.stream().flatMap(r -> r.analyze(s).defects().stream()).toList();
                    if (!ds.isEmpty()) {
                        t.put(s.getFilePath(), ds);
                    }
                },
                TreeMap::putAll
        );
        return new DefectStorage(defects);
    }

    private List<AnalyzerRule> findRules() {
        Reflections reflections = new Reflections("analyzer.rules");
        return reflections.getSubTypesOf(AnalyzerRule.class).stream().map(this::createNewRule).toList();
    }

    private AnalyzerRule createNewRule(Class<? extends AnalyzerRule> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    private static List<Storage> parseStorages(Path root) {

        var storages = new ArrayList<Storage>();
        var problems = new HashMap<Path, List<Problem>>();
        if (Files.isDirectory(root)) {
            SourceRoot sourceRoot = new SourceRoot(root);

            try {
                sourceRoot.parse("", (l, a, parseResult) -> {
                    parseResult.ifSuccessful(cu -> storages.add(new Storage(cu, l)));
                    if (!parseResult.getProblems().isEmpty()) {
                        problems.put(l, parseResult.getProblems());
                    }
                    return SourceRoot.Callback.Result.DONT_SAVE;
                });
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            SourceRoot sourceRoot = new SourceRoot(root.getParent());

            try {
                CompilationUnit cu = sourceRoot.parse("", root.getFileName().toString());
                storages.add(new Storage(cu, root));
            } catch (ParseProblemException e) {
                problems.put(root, e.getProblems());
            }
        }

        if (!problems.isEmpty()) {
            problems.forEach((localPath, problemList) -> {
                        System.err.println("Parse failed: " + localPath);
                        problemList.forEach(p -> System.err.println(p.getMessage()));
                        System.err.println();
                    }
            );
        }

        return storages;
    }
}
