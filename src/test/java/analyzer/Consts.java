package analyzer;

import java.nio.file.Path;

public class Consts {
    public static final Path RESOURCE_FOLDER = Path.of("src/test/resources");
    public static final Path CORRECT_FILE = RESOURCE_FOLDER.resolve("CorrectMain.java");
    public static final Path FILE_WITH_BROKEN_RULE = RESOURCE_FOLDER.resolve("MainWithError.java");
    public static final Path INVALID_JAVA_FILE = RESOURCE_FOLDER.resolve("BrokenMain.java");

    public static final Path NONEXISTENT_WITH_LONG_NAME =
            RESOURCE_FOLDER.resolve("file_with_looooooooooooooooooooooooooooooooooooooooooooooooooooooooooong_name.java");
    public static final Path NONEXISTENT_WITH_SHORT_NAME = Path.of("short_name.java");
}
