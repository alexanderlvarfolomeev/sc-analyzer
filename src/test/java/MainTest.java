import analyzer.Consts;
import analyzer.DefectStorage;
import analyzer.config.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MainTest {
    @Test
    public void testMainReturnsNullOnInvalidFile() {
        Main main = new Main(true, new Config(false, Consts.INVALID_JAVA_FILE));

        Assertions.assertNull(main.start());
    }
    @Test
    public void testMainReturnsNullOnFileWIthBrokenRule() {
        Main main = new Main(true, new Config(false, Consts.FILE_WITH_BROKEN_RULE));

        Assertions.assertNull(main.start());
    }
    @Test
    public void testMainReturnsStorageOnCorrectFile() {
        Main main = new Main(true, new Config(false, Consts.CORRECT_FILE));

        DefectStorage defectStorage = main.start();

        Assertions.assertNotNull(defectStorage);
        Assertions.assertEquals(0, defectStorage.defects().size());
    }
}
