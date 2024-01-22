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

    /**
     * Run {@link Analyzer} with given options
     *
     * @return defects or null if error occurs
     */
    public DefectStorage start() {
        Analyzer analyzer = new Analyzer(config);

        analyzer.printProblems();

        if (failOnErrors && analyzer.hasProblems()) {
            return null;
        }

        DefectStorage defectStorage = analyzer.start();
        defectStorage.printDefects();

        if (failOnErrors && defectStorage.defects().values().stream().mapToLong(List::size).sum() != 0) {
            return null;
        }

         return defectStorage;
    }

    /**
     * Parse command line arguments and runs analyzer
     *
     * @param args command line arguments
     */
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

                DefectStorage defectStorage = new Main(failOnErrors, analyzerConfig).start();

                if (defectStorage == null) {
                    System.exit(1);
                }
            }
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp("analyzer", options);

            System.exit(1);
        }
    }
}
