package com.jcode.analyzer.visitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class VisitorHelper {
    public static void removeUnUsedVariables(CompilationUnit cu) {
        Set<String> usedVariables = new HashSet<>();
        cu.findAll(NameExpr.class).forEach(var -> usedVariables.add(var.getNameAsString()));

        cu.findAll(VariableDeclarator.class).forEach(varDecl -> {
            if (!usedVariables.contains(varDecl.getNameAsString())) {
                varDecl.remove();
            }
        });

        cu.findAll(FieldDeclaration.class).forEach(varDec -> {
            if (varDec.getVariables().isEmpty()) {
                varDec.remove();
            }
        });

        cu.findAll(VariableDeclarationExpr.class).forEach(dVal -> {
            if(dVal.getVariables().isEmpty()){
                dVal.remove();
            }
        });
    }
    public static void removeUnUsedImports(CompilationUnit cu){
        List<ImportDeclaration> imports = cu.getImports();
        Set<String> usedImports = findUsedImports(cu);
        if(!imports.isEmpty() && !usedImports.isEmpty()){
        Iterator itr = imports.iterator();
        while (itr.hasNext()) {
            ImportDeclaration id = (ImportDeclaration) itr.next();
            if (!usedImports.contains(id.getName().getIdentifier().toString())) {
                itr.remove();
            }
        }
        }
    }

    private static Set<String> findUsedImports(CompilationUnit cu) {
        Set<String> usedImports = new HashSet<>();
        cu.accept(new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(NameExpr n, Void arg) {
                usedImports.add(n.getNameAsString());
                super.visit(n, arg);
            }

            @Override
            public void visit(ObjectCreationExpr n, Void arg) {
                usedImports.add(n.getType().getNameAsString());
                super.visit(n, arg);
            }

            @Override
            public void visit(MethodCallExpr n, Void arg) {
                usedImports.add(n.getNameAsString());
                super.visit(n, arg);
            }

            @Override
            public void visit(ClassOrInterfaceType n, Void arg) {
                usedImports.add(n.getNameAsString());
                super.visit(n, arg);
            }
        }, null);
        return usedImports;
    }

    public static void removeEmptyStmt(CompilationUnit cu){
        cu.findAll(EmptyStmt.class).forEach(stmt -> stmt.remove());
    }
}
