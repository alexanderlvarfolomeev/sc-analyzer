package analyzer;

import analyzer.rules.AnalyzerRule;
import util.Utils;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public record DefectStorage(Map<Path, List<AnalyzerRule.Defect>> defects) {
    public void printDefects() {
        defects.forEach(this::printDefectList);
    }

    public void printDefectList(Path filepath, List<AnalyzerRule.Defect> defects) {
        System.out.println(Utils.createFilepathHeader(filepath));
        defects.forEach(defect -> System.out.println(defect.position() + ": " + defect.message()));
    }
}
