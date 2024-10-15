package com.jcode.analyzer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.metamodel.NameExprMetaModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Beautify {

    public static void beautifyFile(CompilationUnit cu) {
        List<ImportDeclaration> imports = cu.getImports();
        if (imports != null) {
            Set<String> usedImports = findUsedImports(cu);
            removeUnUsedImports(cu, usedImports, imports);
            removeWhiteSpace(cu);
            removeUnUsedVariables(cu);
            checkConditionalStmt(cu);
        }
    }

    private static void checkConditionalStmt(CompilationUnit cu) {

        cu.accept(new VoidVisitorAdapter<Void>() {

            @Override
            public void visit(IfStmt n, Void args){
                super.visit(n,args);
                Expression condition = n.getCondition();
                if(isComparisonWithBoolenLit(condition,BinaryExpr.Operator.EQUALS,true)){
                    BinaryExpr binaryExpr = (BinaryExpr) condition;
                    n.setCondition(binaryExpr.getLeft());
                }
                else if(isComparisonWithBoolenLit(condition, BinaryExpr.Operator.EQUALS,false)){
                    BinaryExpr binaryExpr = (BinaryExpr) condition;
                    n.setCondition(new UnaryExpr(binaryExpr.getLeft(),UnaryExpr.Operator.LOGICAL_COMPLEMENT));
                }
                else if(isComparisonWithBoolenLit(condition, BinaryExpr.Operator.NOT_EQUALS,true)){
                    BinaryExpr binaryExpr = (BinaryExpr) condition;
                    n.setCondition(new UnaryExpr(binaryExpr.getLeft(),UnaryExpr.Operator.LOGICAL_COMPLEMENT));
                }
                else if(isComparisonWithBoolenLit(condition, BinaryExpr.Operator.NOT_EQUALS,false)){
                    BinaryExpr binaryExpr = (BinaryExpr) condition;
                    n.setCondition(binaryExpr.getLeft());
                }

            }

            @Override
            public void visit(WhileStmt n, Void args){
                super.visit(n,args);
                Expression condition = n.getCondition();
                if(isComparisonWithBoolenLit(condition, BinaryExpr.Operator.EQUALS,true)){
                    BinaryExpr binaryExpr = (BinaryExpr) condition;
                    n.setCondition(binaryExpr.getLeft());
                }
            }

            @Override
            public void visit(BlockStmt blockStmt, Void arg) {
                super.visit(blockStmt, arg);

                List<Statement> statements = blockStmt.getStatements();

                for (int i = 0; i < statements.size() - 1; i++) {
                    if (statements.get(i).isIfStmt()) {
                        IfStmt outerIf = statements.get(i).asIfStmt();

                        if (statements.get(i + 1).isIfStmt()) {
                            IfStmt innerIf = statements.get(i + 1).asIfStmt();

                            if (outerIf.getThenStmt().isBlockStmt() && outerIf.getThenStmt().asBlockStmt().getStatements().size() == 1 &&
                                    outerIf.getThenStmt().asBlockStmt().getStatement(0).isIfStmt() &&
                                    outerIf.getThenStmt().asBlockStmt().getStatement(0).asIfStmt().equals(innerIf)) {

                                Expression combinedCondition = new BinaryExpr(
                                        outerIf.getCondition(),
                                        innerIf.getCondition(),
                                        BinaryExpr.Operator.AND
                                );
                                IfStmt newIfStmt = new IfStmt(combinedCondition, innerIf.getThenStmt(), null);

                                statements.set(i, newIfStmt);

                                outerIf.getThenStmt().asBlockStmt().remove(innerIf);
                            }
                        }
                    }
                }
            }
            public boolean isComparisonWithBoolenLit(Expression condition, BinaryExpr.Operator operator, boolean b) {
                if(condition.isBinaryExpr()){
                    BinaryExpr binaryExpr = condition.asBinaryExpr();
                    if(binaryExpr.getOperator() == operator){
                        Expression left = binaryExpr.getLeft();
                        Expression right = binaryExpr.getRight();
                        if(right.isBooleanLiteralExpr()){
                            BooleanLiteralExpr booleanLiteralExpr = right.asBooleanLiteralExpr();
                            return booleanLiteralExpr.getValue() == b;
                        }
                        if(left.isBooleanLiteralExpr()){
                            BooleanLiteralExpr booleanLiteralExpr = left.asBooleanLiteralExpr();
                            return booleanLiteralExpr.getValue() == b;
                        }
                    }
                }
                return false;
            }
        },null);

    }

    private static void removeUnUsedVariables(CompilationUnit cu) {
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
            @Override
            public void visit(WhileStmt whileStmt, Void arg) {
                super.visit(whileStmt, arg);

                // Check for space after 'for' keyword
                if (!whileStmt.toString().matches("for\\s*\\(")) {
                    System.out.println("Improper space after 'while' keyword at line: " + whileStmt.getBegin().get().line);
                }

                // Check the indentation of the 'for' block
                String code = whileStmt.toString();
                checkIndentation(whileStmt, code);
            }

            @Override
            public void visit(SwitchStmt switchStmt, Void arg) {
                super.visit(switchStmt, arg);

                // Check for space after 'for' keyword
                if (!switchStmt.toString().matches("for\\s*\\(")) {
                    System.out.println("Improper space after 'switch' keyword at line: " + switchStmt.getBegin().get().line);
                }

                // Check the indentation of the 'for' block
                String code = switchStmt.toString();
                checkIndentation(switchStmt, code);
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
        }, null);

    }

    private static void removeUnUsedImports(CompilationUnit cu, Set<String> usedImports, List<ImportDeclaration> imports) {
        Iterator itr = imports.iterator();
        while (itr.hasNext()) {
            ImportDeclaration id = (ImportDeclaration) itr.next();
            if (!usedImports.contains(id.getName().getIdentifier().toString())) {
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
}
