package analyzer;

import java.nio.file.Path;

public class Consts {
    public static final Path RESOURCE_FOLDER = Path.of("src/test/resources");
    public static final Path CORRECT_FILE = RESOURCE_FOLDER.resolve("CorrectMain.java");
    public static final Path INCORRECT_FILE = RESOURCE_FOLDER.resolve("MainWithError.java");
    public static final Path NONEXISTED_WITH_LONG_NAME =
            RESOURCE_FOLDER.resolve("file_with_looooooooooooooooooooooooooooooooooooooooooooooooooooooooooong_name.java");
    public static final Path NONEXISTED_WITH_SHORT_NAME = Path.of("short_name.java");
}
