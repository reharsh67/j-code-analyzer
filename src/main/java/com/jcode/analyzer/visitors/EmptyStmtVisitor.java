package com.jcode.analyzer.visitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class EmptyStmtVisitor extends VoidVisitorAdapter<Void> {

    // Initialize SLF4J logger
    private static final Logger logger = LoggerFactory.getLogger(EmptyStmtVisitor.class);

    // Set to store all empty statements found in the CompilationUnit
    private Set<EmptyStmt> stmt = new HashSet<>();

    @Override
    public void visit(EmptyStmt n, Void args) {
        // Add each empty statement to the set for later removal
        stmt.add(n);
        logger.info("Empty statement found and added to removal list.");
        super.visit(n, args);
    }

    // Method to analyze the CompilationUnit and remove empty statements
    public void analyze(CompilationUnit cu) {
        try {
            logger.info("Starting analysis to find and remove empty statements.");
            cu.accept(this, null);
            // Remove empty statements after analysis
            removeEmptyStmt();
        } catch (Exception e) {
            logger.error("Error during analysis of CompilationUnit: {}", e.getMessage());
        }
    }

    // Method to remove all empty statements collected
    private void removeEmptyStmt() {
        logger.info("Removing {} empty statements.", stmt.size());
        for (EmptyStmt s : stmt) {
            try {
                s.remove();
                logger.info("Empty statement removed.");
            } catch (Exception e) {
                logger.error("Error removing empty statement: {}", e.getMessage());
            }
        }
    }
}
