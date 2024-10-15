package com.jcode.analyzer.visitors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class WhiteSpaceAndIndentVisitor extends VoidVisitorAdapter<Void> {
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
}
