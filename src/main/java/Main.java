import analyzer.Analyzer;
import analyzer.DefectStorage;
import analyzer.config.Config;
import org.apache.commons.cli.*;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("a", "ast", false, "Print AST.");
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
                String path = cmd.getOptionValue('i', ".");
                Config config = new Config(printAST, Path.of(path));

                new Analyzer(config).start().printDefects();
            }
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp("analyzer", options);

            System.exit(1);
        }
    }
}
