package com.jcode.analyzer.visitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.jcode.analyzer.constants.JConstants;
import com.jcode.analyzer.context.OperationContext;
import com.jcode.analyzer.helper.FileReaderAndParserHelper;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class UnUsedVariablesVisitor extends VoidVisitorAdapter<Void> {
    private Set<VariableDeclarator> declaredVariables = new HashSet<>();
    private Set<String> usedVariables = new HashSet<>();
    private Set<VariableDeclarationExpr> declaredVariablesExpr = new HashSet<>();
    @Override
    public void visit(VariableDeclarator varDecl, Void arg) {
        // Collect all declared variables
        declaredVariables.add(varDecl);
        super.visit(varDecl, arg);
    }

    @Override
    public void visit(NameExpr nameExpr, Void arg) {
        // Collect all variable references (NameExpr)
        usedVariables.add(nameExpr.getNameAsString());
        super.visit(nameExpr, arg);
    }

    @Override
    public void visit(VariableDeclarationExpr nameExpr, Void arg) {
        // Collect all variable references (NameExpr)
        declaredVariablesExpr.add(nameExpr);
        super.visit(nameExpr, arg);
    }
    // Remove unused variables from the CompilationUnit
    public void removeUnusedVariables() {
        // Iterate through declared variables and remove the unused ones
        for (VariableDeclarator varDecl : declaredVariables) {
            if (!usedVariables.contains(varDecl.getNameAsString())) {
                varDecl.remove(); // This removes the unused variable declaration
            }
        }
        for(VariableDeclarationExpr expr : declaredVariablesExpr){
            if(expr.getVariables().isEmpty()){
                expr.remove();
            }
        }
    }
    public void analyze(CompilationUnit cu) {
        // Traverse the compilation unit with this analyzer
        cu.accept(this, null);
        // After analyzing, remove unused variables
        removeUnusedVariables();
    }
}
