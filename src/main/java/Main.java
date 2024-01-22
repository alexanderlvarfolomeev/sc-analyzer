import analyzer.Analyzer;
import analyzer.DefectStorage;
import analyzer.config.Config;
import org.apache.commons.cli.*;

import java.nio.file.Path;
import java.util.List;

public class Main {

    private final boolean failOnErrors;
    private final Config config;

    public Main(boolean failOnErrors, Config config) {
        this.failOnErrors = failOnErrors;
        this.config = config;
    }

    public void start() {
        Analyzer analyzer = new Analyzer(config);

        analyzer.printProblems();

        if (failOnErrors && analyzer.hasProblems()) {
            System.exit(1);
        }

        DefectStorage defectStorage = analyzer.start();
        defectStorage.printDefects();

        if (failOnErrors && defectStorage.defects().values().stream().mapToLong(List::size).sum() != 0) {
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("a", "ast", false, "Print AST.");
        options.addOption("e", "error", false, "Fail on errors.");
        options.addOption("h", "help", false, "Shows this help message.");
        options.addOption("i", "input", true, "Input file/folder. Default: working folder.");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption('h')) {
                formatter.printHelp("analyzer [--help] [--ast] [--input path]", options);
            } else {
                boolean printAST = cmd.hasOption('a');
                boolean failOnErrors = cmd.hasOption('e');
                String path = cmd.getOptionValue('i', ".");
                Config analyzerConfig = new Config(printAST, Path.of(path));

                new Main(failOnErrors, analyzerConfig).start();
            }
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp("analyzer", options);

            System.exit(1);
        }
    }
}
