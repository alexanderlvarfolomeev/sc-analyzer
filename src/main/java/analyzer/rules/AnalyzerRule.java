package analyzer.rules;

import ast.Storage;
import com.github.javaparser.ast.nodeTypes.NodeWithRange;

import java.nio.file.Path;
import java.util.List;

/**
 * Interface for all analyzer rules.
 */
public interface AnalyzerRule {

    /**
     * Method to run analyze on one compilation unit
     *
     * @param astStorage storage for one compilation unit
     * @return found defects related to this rule
     */
    AnalysisDefects analyze(Storage astStorage);

    default String getPosition(NodeWithRange<?> node, Path filepath) {
        return node.getBegin().map(p -> filepath + ":" + p.line + ":" + p.column).orElse(filepath.toString());
    }

    record AnalysisDefects(List<Defect> defects) {
    }

    record Defect(String message, String position) {
    }
}
