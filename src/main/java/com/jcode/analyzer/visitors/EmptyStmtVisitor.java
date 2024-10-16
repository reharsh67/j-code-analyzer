package com.jcode.analyzer.visitors;

import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class EmptyStmtVisitor extends VoidVisitorAdapter<Void> {
    public void vist(EmptyStmt n , Void args){
        n.remove();
        super.visit(n,args);
    }
}
