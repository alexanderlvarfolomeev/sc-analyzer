package analyzer.util;

import analyzer.Consts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.Utils;

public class UtilsTest {

    @Test
    public void checkHeaderShortenLongFilename() {
        String header = Utils.createFilepathHeader(Consts.NONEXISTENT_WITH_LONG_NAME);
        Assertions.assertEquals(80, header.length());
    }

    @Test
    public void checkHeaderExpandShortFilename() {
        String header = Utils.createFilepathHeader(Consts.NONEXISTENT_WITH_SHORT_NAME);
        Assertions.assertEquals(80, header.length());
    }
}
