package analyzer.config;

import java.nio.file.Path;

public record Config(boolean printAST, Path root) {
}
