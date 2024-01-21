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
        DefectStorage storage = new Analyzer(new Config(false, Consts.INCORRECT_FILE)).start();

        Assertions.assertEquals(1, storage.defects().size());
        Assertions.assertTrue(storage.defects().keySet().stream().allMatch(Consts.INCORRECT_FILE::equals));
        Assertions.assertTrue(storage.defects().values().stream().allMatch(l -> l.size() == 1));
    }
    @Test
    public void checkMultiVariableDeclarationRuleSavesCorrectPosition() {
        DefectStorage storage = new Analyzer(new Config(false, Consts.INCORRECT_FILE)).start();

        var defect = storage.defects().get(Consts.INCORRECT_FILE).get(0);

        Assertions.assertTrue(defect.position().startsWith(Consts.INCORRECT_FILE + ":10:"));
    }
}
