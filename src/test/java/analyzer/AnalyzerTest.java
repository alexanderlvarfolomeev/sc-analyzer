package analyzer;


import analyzer.config.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

public class AnalyzerTest {

    @Test
    public void checkCorrectFileWithAnalyzer() {
        Path correctFile = Path.of("src/test/resources/CorrectMain.java");
        DefectStorage storage = new Analyzer(new Config(false, Consts.CORRECT_FILE)).start();

        Assertions.assertEquals(0, storage.defects().size());
    }

    @Test
    public void testDirectoryRootPath() {
        DefectStorage storage = new Analyzer(new Config(false, Consts.RESOURCE_FOLDER)).start();

        Assertions.assertEquals(1, storage.defects().size());
        Assertions.assertTrue(storage.defects().keySet().stream().allMatch(Consts.INCORRECT_FILE.getFileName()::equals));
    }
}
