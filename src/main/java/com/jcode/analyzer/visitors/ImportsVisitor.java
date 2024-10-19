package com.jcode.analyzer.visitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.HashSet;
import java.util.Set;

public class ImportsVisitor extends VoidVisitorAdapter<Void> {

    // Set to store used imports
    Set<String> usedImports = new HashSet<>();

    // Set to store all imports in the CompilationUnit
    Set<ImportDeclaration> allImports = new HashSet<>();

    @Override
    public void visit(NameExpr n, Void arg) {
        // Track the usage of specific class names in the code
        usedImports.add(n.getNameAsString());
        System.out.println("Tracking usage of class: " + n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(ObjectCreationExpr n, Void arg) {
        // Track the usage of object creation types
        usedImports.add(n.getType().getNameAsString());
        System.out.println("Tracking usage of object creation: " + n.getType().getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(MethodCallExpr n, Void arg) {
        // Track the usage of method calls
        usedImports.add(n.getNameAsString());
        System.out.println("Tracking usage of method: " + n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(ClassOrInterfaceType n, Void arg) {
        // Track the usage of class/interface types
        usedImports.add(n.getNameAsString());
        System.out.println("Tracking usage of class/interface type: " + n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(ImportDeclaration n, Void args) {
        // Track all import declarations
        allImports.add(n);
        System.out.println("Tracking import: " + n.getName().getIdentifier());
        super.visit(n, args);
    }

    // Method to remove unused imports from the CompilationUnit
    public void removeUnusedImports() {
        if (!allImports.isEmpty() && !usedImports.isEmpty()) {
            System.out.println("Checking for unused imports...");
            for (ImportDeclaration id : allImports) {
                // If the import is not found in the used imports set, it is removed
                if (!usedImports.contains(id.getName().getIdentifier())) {
                    System.out.println("Removing unused import: " + id.getName().getIdentifier());
                    id.remove();
                }
            }
        } else {
            System.out.println("No imports to remove.");
        }
    }

    // Analyze the CompilationUnit to find and remove unused imports
    public void analyze(CompilationUnit cu) {
        System.out.println("Starting import analysis...");
        // Traverse the CompilationUnit
        cu.accept(this, null);
        // Remove any unused imports
        removeUnusedImports();
        System.out.println("Import analysis completed.");
    }
}
