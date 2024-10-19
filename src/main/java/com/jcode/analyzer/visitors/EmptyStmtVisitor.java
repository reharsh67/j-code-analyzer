package com.jcode.analyzer.visitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.HashSet;
import java.util.Set;

public class EmptyStmtVisitor extends VoidVisitorAdapter<Void> {

    private Set<EmptyStmt> stmt = new HashSet<>();

    @Override
    public void visit(EmptyStmt n, Void args) {
        stmt.add(n);
        super.visit(n, args);
    }

    public void analyze(CompilationUnit cu) {
        cu.accept(this, null);
        removeEmptyStmt();
    }

    private void removeEmptyStmt() {
        for (EmptyStmt s : stmt) {
            s.remove();
        }
    }
}
