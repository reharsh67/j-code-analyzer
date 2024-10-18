package com.jcode.analyzer.visitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ImportsVisitor extends VoidVisitorAdapter<Void> {
    Set<String> usedImports = new HashSet<>();
    Set<ImportDeclaration> allImports = new HashSet<>();

    public void visit(NameExpr n, Void arg) {
        usedImports.add(n.getNameAsString());
        super.visit(n, arg);
    }


    public void visit(ObjectCreationExpr n, Void arg) {
        usedImports.add(n.getType().getNameAsString());
        super.visit(n, arg);
    }


    public void visit(MethodCallExpr n, Void arg) {
        usedImports.add(n.getNameAsString());
        super.visit(n, arg);
    }


    public void visit(ClassOrInterfaceType n, Void arg) {
        usedImports.add(n.getNameAsString());
        super.visit(n, arg);
    }

    public void visit(ImportDeclaration n , Void args){
        allImports.add(n);
        super.visit(n,args);
    }


    public void removeUnusedImports(){
        if(!allImports.isEmpty() && !usedImports.isEmpty()){
            for(ImportDeclaration id : allImports){
                if (!usedImports.contains(id.getName().getIdentifier().toString())) {
                    id.remove();
                }
            }
        }

    }

    public void analyze(CompilationUnit cu) {
        cu.accept(this,null);
        removeUnusedImports();
    }
}
