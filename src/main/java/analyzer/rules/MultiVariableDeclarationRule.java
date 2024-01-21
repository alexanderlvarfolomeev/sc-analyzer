package analyzer.rules;

import ast.Storage;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

import java.util.Objects;
import java.util.stream.Collectors;

public class MultiVariableDeclarationRule implements AnalyzerRule {
    @Override
    public AnalysisDefects analyze(Storage astStorage) {
        var defects = astStorage.getAst().findAll(VariableDeclarationExpr.class)
                .stream()
                .map(vde -> processDeclarationExpr(vde, astStorage))
                .filter(Objects::nonNull)
                .toList();
        return new AnalysisDefects(defects);
    }

    private Defect processDeclarationExpr(VariableDeclarationExpr expr, Storage astStorage) {
        if (expr.getVariables().size() > 1) {
            return new Defect(
                    expr.getVariables().stream()
                            .map(VariableDeclarator::getNameAsString)
                            .collect(
                                    Collectors.joining(
                                            ", ",
                                            "Multiple variable declaration expression: ",
                                            "."
                                    )
                            ),
                    getPosition(expr, astStorage.getFilePath())
            );
        } else {
            return null;
        }
    }
}
