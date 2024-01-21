package analyzer.rules;

import ast.Storage;
import com.github.javaparser.Position;
import com.github.javaparser.ast.nodeTypes.NodeWithRange;

import java.nio.file.Path;
import java.util.List;

public interface AnalyzerRule {
    AnalysisDefects analyze(Storage astStorage);

    default String getPosition(NodeWithRange<?> node, Path filepath) {
        return node.getBegin().map(p -> filepath + ":" + p.line + ":" + p.column).orElse(filepath.toString());
    }

    record AnalysisDefects(List<Defect> defects) {
    }

    record Defect(String message, String position) {
    }
}
