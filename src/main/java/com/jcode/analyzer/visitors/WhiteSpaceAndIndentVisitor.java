package com.jcode.analyzer.visitors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhiteSpaceAndIndentVisitor extends VoidVisitorAdapter<Void> {

    // Initialize SLF4J logger
    private static final Logger logger = LoggerFactory.getLogger(WhiteSpaceAndIndentVisitor.class);

    // Define consistent indentation (4 spaces here)
    private static final String INDENTATION = "    ";

    @Override
    public void visit(BinaryExpr binaryExpr, Void arg) {
        super.visit(binaryExpr, arg);
        // Check for proper spacing around the operator (spaces before and after the operator)
        if (!binaryExpr.toString().matches(".*\\s+[=+\\-*/]\\s+.*")) {
            logger.warn("Improper spacing around operator at line: {}", binaryExpr.getBegin().get().line);
        }
    }

    @Override
    public void visit(IfStmt ifStmt, Void arg) {
        super.visit(ifStmt, arg);
        // Check for space after 'if' keyword
        if (!ifStmt.toString().matches("if\\s*\\(.*")) {
            logger.warn("Improper space after 'if' keyword at line: {}", ifStmt.getBegin().get().line);
        }
        // Check the indentation of the 'if' block
        checkIndentation(ifStmt, ifStmt.toString());
    }

    @Override
    public void visit(ForStmt forStmt, Void arg) {
        super.visit(forStmt, arg);
        // Check for space after 'for' keyword
        if (!forStmt.toString().matches("for\\s*\\(.*")) {
            logger.warn("Improper space after 'for' keyword at line: {}", forStmt.getBegin().get().line);
        }
        // Check the indentation of the 'for' block
        checkIndentation(forStmt, forStmt.toString());
    }

    @Override
    public void visit(WhileStmt whileStmt, Void arg) {
        super.visit(whileStmt, arg);
        // Check for space after 'while' keyword
        if (!whileStmt.toString().matches("while\\s*\\(.*")) {
            logger.warn("Improper space after 'while' keyword at line: {}", whileStmt.getBegin().get().line);
        }
        // Check the indentation of the 'while' block
        checkIndentation(whileStmt, whileStmt.toString());
    }

    @Override
    public void visit(SwitchStmt switchStmt, Void arg) {
        super.visit(switchStmt, arg);
        // Check for space after 'switch' keyword
        if (!switchStmt.toString().matches("switch\\s*\\(.*")) {
            logger.warn("Improper space after 'switch' keyword at line: {}", switchStmt.getBegin().get().line);
        }
        // Check the indentation of the 'switch' block
        checkIndentation(switchStmt, switchStmt.toString());
    }

    // Function to check indentation of a block of code
    private void checkIndentation(Node node, String code) {
        String[] lines = code.split("\n");
        for (String line : lines) {
            if (!line.trim().isEmpty() && !line.startsWith(INDENTATION)) {
                logger.warn("Improper indentation at line: {}", node.getBegin().get().line);
                break;
            }
        }
    }
}
