package com.jcode.analyzer.visitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UnUsedVariablesVisitor extends VoidVisitorAdapter<Void> {

    // Initialize SLF4J logger
    private static final Logger logger = LoggerFactory.getLogger(UnUsedVariablesVisitor.class);

    // Set to store declared variables
    private Set<VariableDeclarator> declaredVariables = new HashSet<>();

    // Set to store used variables
    private Set<String> usedVariables = new HashSet<>();

    // Set to store declared variable expressions
    private Set<VariableDeclarationExpr> declaredVariablesExpr = new HashSet<>();

    @Override
    public void visit(VariableDeclarator varDecl, Void arg) {
        // Collect all declared variables that are not constants (i.e., not static final)
        boolean isConstant = varDecl.getParentNode().flatMap(parent -> parent instanceof FieldDeclaration ? Optional.of((FieldDeclaration) parent) : Optional.empty())
                .map(fieldDecl -> fieldDecl.isStatic() && fieldDecl.isFinal()).orElse(false);
        if (!isConstant) {
            declaredVariables.add(varDecl);
            logger.info("Declared variable: {}", varDecl.getNameAsString());
        }
        super.visit(varDecl, arg);
    }

    @Override
    public void visit(NameExpr nameExpr, Void arg) {
        // Collect all variable usages (NameExpr)
        usedVariables.add(nameExpr.getNameAsString());
        logger.info("Used variable: {}", nameExpr.getNameAsString());
        super.visit(nameExpr, arg);
    }

    @Override
    public void visit(VariableDeclarationExpr varDeclExpr, Void arg) {
        // Collect all variable declaration expressions
        declaredVariablesExpr.add(varDeclExpr);
        logger.info("Variable declaration expression found.");
        super.visit(varDeclExpr, arg);
    }

    // Method to remove unused variables from the CompilationUnit
    public void removeUnusedVariables(CompilationUnit cu) {
        logger.info("Starting to remove unused variables...");
        // Remove unused variables by checking declared variables against used variables
        for (VariableDeclarator varDecl : declaredVariables) {
            if (!usedVariables.contains(varDecl.getNameAsString())) {
                logger.info("Removing unused variable: {}", varDecl.getNameAsString());
                varDecl.remove();
            }
        }
        // Remove empty variable declaration expressions
        for (VariableDeclarationExpr expr : declaredVariablesExpr) {
            if (expr.getVariables().isEmpty()) {
                logger.info("Removing empty variable declaration expression.");
                expr.remove();
            }
        }
        // Remove any empty expression statements
        cu.findAll(ExpressionStmt.class).forEach(stmt -> {
            if (stmt.getExpression().isVariableDeclarationExpr() && stmt.getExpression().asVariableDeclarationExpr().getVariables().isEmpty()) {
                logger.info("Removing empty expression statement.");
                stmt.remove();
            }
        });
    }

    // Analyze the CompilationUnit and remove unused variables
    public void analyze(CompilationUnit cu) {
        logger.info("Analyzing for unused variables...");
        try {
            // Traverse the CompilationUnit and collect variables
            cu.accept(this, null);
            // Remove unused variables after traversal
            removeUnusedVariables(cu);
            logger.info("Unused variables removal completed.");
        } catch (Exception e) {
            logger.error("Error during unused variables analysis: {}", e.getMessage());
        }
    }
}
