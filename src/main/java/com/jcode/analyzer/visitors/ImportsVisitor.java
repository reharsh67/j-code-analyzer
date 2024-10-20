package com.jcode.analyzer.visitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class ImportsVisitor extends VoidVisitorAdapter<Void> {

    // Initialize SLF4J logger
    private static final Logger logger = LoggerFactory.getLogger(ImportsVisitor.class);

    // Set to store used imports
    private Set<String> usedImports = new HashSet<>();

    // Set to store all imports in the CompilationUnit
    private Set<ImportDeclaration> allImports = new HashSet<>();

    @Override
    public void visit(NameExpr n, Void arg) {
        // Track the usage of specific class names in the code
        usedImports.add(n.getNameAsString());
        logger.info("Tracking usage of class: {}", n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(ObjectCreationExpr n, Void arg) {
        // Track the usage of object creation types
        usedImports.add(n.getType().getNameAsString());
        logger.info("Tracking usage of object creation: {}", n.getType().getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(MethodCallExpr n, Void arg) {
        // Track the usage of method calls
        usedImports.add(n.getNameAsString());
        logger.info("Tracking usage of method: {}", n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(ClassOrInterfaceType n, Void arg) {
        // Track the usage of class/interface types
        usedImports.add(n.getNameAsString());
        logger.info("Tracking usage of class/interface type: {}", n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(ImportDeclaration n, Void args) {
        // Track all import declarations
        allImports.add(n);
        logger.info("Tracking import: {}", n.getName().getIdentifier());
        super.visit(n, args);
    }

    // Method to remove unused imports from the CompilationUnit
    public void removeUnusedImports() {
        if (!allImports.isEmpty() && !usedImports.isEmpty()) {
            logger.info("Checking for unused imports...");
            for (ImportDeclaration id : allImports) {
                // If the import is not found in the used imports set, it is removed
                if (!usedImports.contains(id.getName().getIdentifier())) {
                    logger.info("Removing unused import: {}", id.getName().getIdentifier());
                    id.remove();
                }
            }
        } else {
            logger.info("No unused imports found.");
        }
    }

    // Analyze the CompilationUnit to find and remove unused imports
    public void analyze(CompilationUnit cu) {
        logger.info("Starting import analysis...");
        try {
            // Traverse the CompilationUnit
            cu.accept(this, null);
            // Remove any unused imports
            removeUnusedImports();
            logger.info("Import analysis completed.");
        } catch (Exception e) {
            logger.error("Error during import analysis: {}", e.getMessage());
        }
    }
}
