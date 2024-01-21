package ast;

import java.nio.file.Path;
import java.util.List;

public class Consts {
    public static final Path RESOURCE_FOLDER = Path.of("src/test/resources");
    public static final Path SIMPLE_CLASS_WITH_METHODS_FILE = RESOURCE_FOLDER.resolve("SimpleClass.java");

    public static final String SIMPLE_CLASS_NAME = "SimpleClass";
    public static final List<String> SIMPLE_CLASS_FUNCTIONS = List.of("foo", "bar");
}
