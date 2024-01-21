package analyzer.rules;

import analyzer.Analyzer;
import analyzer.Consts;
import analyzer.DefectStorage;
import analyzer.config.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MultiVariableDeclarationRuleTest {

    @Test
    public void checkIncorrectFileWithAnalyzer() {
        DefectStorage storage = new Analyzer(new Config(false, Consts.FILE_WITH_BROKEN_RULE)).start();

        Assertions.assertEquals(1, storage.defects().size());
        Assertions.assertTrue(storage.defects().keySet().stream().allMatch(Consts.FILE_WITH_BROKEN_RULE::equals));
        Assertions.assertTrue(storage.defects().values().stream().allMatch(l -> l.size() == 1));
    }

    @Test
    public void checkMultiVariableDeclarationRuleSavesCorrectPosition() {
        DefectStorage storage = new Analyzer(new Config(false, Consts.FILE_WITH_BROKEN_RULE)).start();

        var defect = storage.defects().get(Consts.FILE_WITH_BROKEN_RULE).get(0);

        Assertions.assertTrue(defect.position().startsWith(Consts.FILE_WITH_BROKEN_RULE + ":10:"));
    }
}
