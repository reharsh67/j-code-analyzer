package com.jcode.analyzer.visitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmptyStmtVisitor extends VoidVisitorAdapter<Void> {
    private Set<EmptyStmt> stmt = new HashSet<>();
    @Override
    public void visit(EmptyStmt n , Void args){
        stmt.add(n);
        super.visit(n,args);
    }

    public void analyze(CompilationUnit cu){
        cu.accept(this,null);

        cu.findAll(ExpressionStmt.class).forEach(stmt -> {
            if(stmt.getExpression().isVariableDeclarationExpr() && stmt.getExpression().asVariableDeclarationExpr().getVariables().isEmpty()){
                stmt.remove();
            }
        });

        removeEmptyStmt();
    }

    private void removeEmptyStmt() {
        for (EmptyStmt s: stmt) {
            s.remove();
        }
    }
}
