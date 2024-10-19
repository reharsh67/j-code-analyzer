package com.jcode.analyzer.visitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.HashSet;
import java.util.Set;

public class EmptyStmtVisitor extends VoidVisitorAdapter<Void> {

    // Set to store all empty statements found in the CompilationUnit
    private Set<EmptyStmt> stmt = new HashSet<>();

    @Override
    public void visit(EmptyStmt n, Void args) {
        // Add each empty statement to the set for later removal
        stmt.add(n);
        System.out.println("Empty statement found and added to removal list.");
        super.visit(n, args);
    }

    // Method to analyze the CompilationUnit and remove empty statements
    public void analyze(CompilationUnit cu) {
        // Visit all nodes in the CompilationUnit
        System.out.println("Starting analysis to find and remove empty statements.");
        cu.accept(this, null);
        // Remove empty statements after analysis
        removeEmptyStmt();
    }

    // Method to remove all empty statements collected
    private void removeEmptyStmt() {
        System.out.println("Removing " + stmt.size() + " empty statements.");
        for (EmptyStmt s : stmt) {
            s.remove();
            System.out.println("Empty statement removed.");
        }
    }
}
