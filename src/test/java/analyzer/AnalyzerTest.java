package analyzer;


import analyzer.config.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class AnalyzerTest {

    @Test
    public void testCorrectFileWithAnalyzer() {
        Analyzer analyzer = new Analyzer(new Config(false, Consts.CORRECT_FILE));
        DefectStorage storage = analyzer.start();

        Assertions.assertFalse(analyzer.hasProblems());
        Assertions.assertEquals(0, storage.defects().size());
    }

    @Test
    public void testProblemsOnInvalidJavaFile() {
        Analyzer analyzer = new Analyzer(new Config(false, Consts.INVALID_JAVA_FILE));
        DefectStorage storage = analyzer.start();

        Assertions.assertTrue(analyzer.hasProblems());
        Assertions.assertEquals(0, storage.defects().size());
    }

    @Test
    public void testDirectoryRootPath() {
        DefectStorage storage = new Analyzer(new Config(false, Consts.RESOURCE_FOLDER)).start();

        Assertions.assertEquals(1, storage.defects().size());
        Assertions.assertTrue(storage.defects().keySet().stream().allMatch(Consts.FILE_WITH_BROKEN_RULE.getFileName()::equals));
    }
}
