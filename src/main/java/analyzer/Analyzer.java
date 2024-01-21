package analyzer;

import analyzer.config.Config;
import analyzer.rules.AnalyzerRule;
import ast.Storage;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import org.reflections.Reflections;
import util.Utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Analyzer {
    private final Config config;

    private final List<Storage> storages;

    private final Map<Path, List<Problem>> problems;

    private final List<AnalyzerRule> rules;

    public Analyzer(Config config) {
        this.config = config;
        rules = findRules();

        var parsedStorages = parseStorages(config.root());
        storages = parsedStorages.storages;
        problems = parsedStorages.problems;
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

    public void printProblems() {
        if (!problems.isEmpty()) {
            problems.forEach((localPath, problemList) -> {
                        System.err.println(Utils.createFilepathHeader(localPath));
                        problemList.forEach(p -> System.err.println(p.getMessage()));
                        System.err.println();
                    }
            );
        }
    }

    public boolean hasProblems() {
        return problems.values().stream().mapToInt(List::size).sum() != 0;
    }

    public List<Storage> getStorages() {
        return storages;
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

    private static ParsedStorages parseStorages(Path root) {
        var storages = new ArrayList<Storage>();
        var problems = new HashMap<Path, List<Problem>>();

        var parserConfig = new ParserConfiguration();
        parserConfig.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17);

        if (Files.isDirectory(root)) {
            SourceRoot sourceRoot = new SourceRoot(root, parserConfig);

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
            SourceRoot sourceRoot = new SourceRoot(root.getParent(), parserConfig);

            try {
                CompilationUnit cu = sourceRoot.parse("", root.getFileName().toString());
                storages.add(new Storage(cu, root));
            } catch (ParseProblemException e) {
                problems.put(root, e.getProblems());
            }
        }

        return new ParsedStorages(storages, problems);
    }

    private record ParsedStorages(List<Storage> storages, Map<Path, List<Problem>> problems) {
    }
}
