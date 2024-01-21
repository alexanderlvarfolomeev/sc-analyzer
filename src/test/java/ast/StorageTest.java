package ast;

import analyzer.Analyzer;
import analyzer.config.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class StorageTest {
    @Test
    public void testStorageAstCollections() {
        Analyzer analyzer = new Analyzer(new Config(false, Consts.SIMPLE_CLASS_WITH_METHODS_FILE));

        Assertions.assertEquals(1, analyzer.getStorages().size());
        Storage storage = analyzer.getStorages().get(0);

        Assertions.assertEquals(Consts.SIMPLE_CLASS_WITH_METHODS_FILE, storage.getFilePath());

        Assertions.assertEquals(1, storage.getClasses().size());
        Assertions.assertEquals(
                Consts.SIMPLE_CLASS_NAME,
                storage.getClasses().get(Consts.SIMPLE_CLASS_NAME).getNameAsString()
        );

        Assertions.assertEquals(0, storage.getUnnamedClasses().size());
        Assertions.assertEquals(0, storage.getRecords().size());
        Assertions.assertEquals(0, storage.getUnnamedRecords().size());

        Assertions.assertEquals(
                Set.copyOf(Consts.SIMPLE_CLASS_FUNCTIONS),
                storage.getCallables().stream().map(cd -> cd.getNameAsString()).collect(Collectors.toSet())
        );

        Assertions.assertEquals(
                Set.copyOf(Consts.SIMPLE_CLASS_FUNCTIONS),
                storage.getCallGraph().subCalls().keySet().stream().map(cd -> cd.getNameAsString()).collect(Collectors.toSet())
        );
        Assertions.assertTrue(
                storage.getCallGraph().subCalls().entrySet()
                        .stream()
                        .filter(e -> Objects.equals(e.getKey().getNameAsString(), "foo"))
                        .anyMatch(e -> e.getValue().equals(Set.of("bar")))
        );
        Assertions.assertTrue(
                storage.getCallGraph().subCalls().entrySet()
                        .stream()
                        .filter(e -> Objects.equals(e.getKey().getNameAsString(), "bar"))
                        .anyMatch(e -> e.getValue().equals(Set.of("outer")))
        );
    }
}
