package com.jcode.analyzer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Beautify {

    public static void beautifyFile(CompilationUnit cu) {
        List<ImportDeclaration> imports = cu.getImports();
        if(imports != null) {
            Set<String> usedImports = findUsedImports(cu);
            removeUnUsedImports(cu, usedImports, imports);
            removeWhiteSpace(cu);
        }
    }

    private static void removeWhiteSpace(CompilationUnit cu) {

        cu.accept(new VoidVisitorAdapter<Void>() {
            private static final String INDENTATION = "    "; // Define consistent indentation (4 spaces here)
            @Override
            public void visit(BinaryExpr binaryExpr, Void arg) {
                super.visit(binaryExpr, arg);

                // Check for proper space around the operator
                if (!binaryExpr.toString().matches("\\s+[=+-/*]\\s+")) {
                    System.out.println("Improper spacing around operator at line: " + binaryExpr.getBegin().get().line);
                }
            }

            @Override
            public void visit(IfStmt ifStmt, Void arg) {
                super.visit(ifStmt, arg);

                // Check for space after 'if' keyword
                if (!ifStmt.toString().matches("if\\s*\\(")) {
                    System.out.println("Improper space after 'if' keyword at line: " + ifStmt.getBegin().get().line);
                }

                // Check the indentation of the 'if' block
                String code = ifStmt.toString();
                checkIndentation(ifStmt, code);
            }

            @Override
            public void visit(ForStmt forStmt, Void arg) {
                super.visit(forStmt, arg);

                // Check for space after 'for' keyword
                if (!forStmt.toString().matches("for\\s*\\(")) {
                    System.out.println("Improper space after 'for' keyword at line: " + forStmt.getBegin().get().line);
                }

                // Check the indentation of the 'for' block
                String code = forStmt.toString();
                checkIndentation(forStmt, code);
            }
            // Function to check indentation of a block of code
            private void checkIndentation(Node node, String code) {
                String[] lines = code.split("\n");
                for (String line : lines) {
                    if (!line.startsWith(INDENTATION)) {
                        System.out.println("Improper indentation at line: " + node.getBegin().get().line);
                        break;
                    }
                }
            }
        },null);

    }

    private static void removeUnUsedImports(CompilationUnit cu,Set<String> usedImports, List<ImportDeclaration> imports) {
        Iterator itr = imports.iterator();
        while(itr.hasNext()){
            ImportDeclaration id = (ImportDeclaration) itr.next();
            if(!usedImports.contains(id.getName().getIdentifier().toString())){
                itr.remove();
            }
        }
    }

    private static Set<String> findUsedImports(CompilationUnit cu) {
        Set<String> usedImports = new HashSet<>();
        cu.accept(new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(NameExpr n, Void arg) {
                usedImports.add(n.getNameAsString());
                super.visit(n,arg);
            }
            @Override
            public void visit(ObjectCreationExpr n, Void arg){
                usedImports.add(n.getType().getNameAsString());
                super.visit(n,arg);
            }
            @Override
            public void visit(MethodCallExpr n, Void arg){
                usedImports.add(n.getNameAsString());
                super.visit(n,arg);
            }
            @Override
            public void visit(ClassOrInterfaceType n, Void arg){
                usedImports.add(n.getNameAsString());
                super.visit(n,arg);
            }
        },null);
        return usedImports;
    }
}
