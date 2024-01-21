package util;

import java.nio.file.Path;

public class Utils {

    public static String createFilepathHeader(Path filepath) {
        return createFilepathHeader(filepath, 80);
    }

    public static String createFilepathHeader(Path filepath, int size) {
        String pathAsString = filepath.toString();
        if (pathAsString.length() > size - 6) {
            return "# ..%s #".formatted(pathAsString.substring(pathAsString.length() - (size - 6)));
        } else {
            int leftCount = (size - 2 - pathAsString.length()) / 2;
            int rightCount = size - pathAsString.length() - 2 - leftCount;
            return "%s %s %s".formatted(
                    "#".repeat(Math.max(0, leftCount)),
                    pathAsString,
                    "#".repeat(Math.max(0, rightCount))
            );
        }
    }
}
