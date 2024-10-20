package com.jcode.analyzer.visitors;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ConditionalVisitor extends VoidVisitorAdapter<Void> {

    // Initialize SLF4J logger
    private static final Logger logger = LoggerFactory.getLogger(ConditionalVisitor.class);

    @Override
    public void visit(IfStmt n, Void args) {
        try {
            super.visit(n, args);
            // Get the condition of the if statement
            Expression condition = n.getCondition();
            // Simplify conditions where the comparison is with boolean literals
            if (isComparisonWithBoolenLit(condition, BinaryExpr.Operator.EQUALS, true)) {
                BinaryExpr binaryExpr = (BinaryExpr) condition;
                logger.info("Simplifying '== true' condition in IfStmt.");
                n.setCondition(binaryExpr.getLeft());
            } else if (isComparisonWithBoolenLit(condition, BinaryExpr.Operator.EQUALS, false)) {
                BinaryExpr binaryExpr = (BinaryExpr) condition;
                logger.info("Simplifying '== false' condition in IfStmt.");
                n.setCondition(new UnaryExpr(binaryExpr.getLeft(), UnaryExpr.Operator.LOGICAL_COMPLEMENT));
            } else if (isComparisonWithBoolenLit(condition, BinaryExpr.Operator.NOT_EQUALS, true)) {
                BinaryExpr binaryExpr = (BinaryExpr) condition;
                logger.info("Simplifying '!= true' condition in IfStmt.");
                n.setCondition(new UnaryExpr(binaryExpr.getLeft(), UnaryExpr.Operator.LOGICAL_COMPLEMENT));
            } else if (isComparisonWithBoolenLit(condition, BinaryExpr.Operator.NOT_EQUALS, false)) {
                BinaryExpr binaryExpr = (BinaryExpr) condition;
                logger.info("Simplifying '!= false' condition in IfStmt.");
                n.setCondition(binaryExpr.getLeft());
            } else {
                logger.debug("No simplification applied to IfStmt condition: {}", condition);
            }
        } catch (Exception e) {
            logger.error("Error while visiting IfStmt: {}", e.getMessage());
        }
    }

    @Override
    public void visit(WhileStmt n, Void args) {
        try {
            super.visit(n, args);
            // Get the condition of the while statement
            Expression condition = n.getCondition();
            // Simplify conditions where the comparison is with 'true'
            if (isComparisonWithBoolenLit(condition, BinaryExpr.Operator.EQUALS, true)) {
                BinaryExpr binaryExpr = (BinaryExpr) condition;
                logger.info("Simplifying '== true' condition in WhileStmt.");
                n.setCondition(binaryExpr.getLeft());
            } else {
                logger.debug("No simplification applied to WhileStmt condition: {}", condition);
            }
        } catch (Exception e) {
            logger.error("Error while visiting WhileStmt: {}", e.getMessage());
        }
    }

    @Override
    public void visit(BlockStmt blockStmt, Void arg) {
        try {
            super.visit(blockStmt, arg);
            // Get all statements in the block
            List<Statement> statements = blockStmt.getStatements();
            // Combine nested if-statements into a single if-statement with an AND condition
            for (int i = 0; i < statements.size() - 1; i++) {
                if (statements.get(i).isIfStmt()) {
                    IfStmt outerIf = statements.get(i).asIfStmt();
                    if (statements.get(i + 1).isIfStmt()) {
                        IfStmt innerIf = statements.get(i + 1).asIfStmt();
                        // Check if the inner if is directly nested in the outer if's then block
                        if (outerIf.getThenStmt().isBlockStmt() && outerIf.getThenStmt().asBlockStmt().getStatements().size() == 1 &&
                                outerIf.getThenStmt().asBlockStmt().getStatement(0).isIfStmt() &&
                                outerIf.getThenStmt().asBlockStmt().getStatement(0).asIfStmt().equals(innerIf)) {
                            // Combine conditions using AND and simplify
                            Expression combinedCondition = new BinaryExpr(outerIf.getCondition(), innerIf.getCondition(), BinaryExpr.Operator.AND);
                            logger.info("Combining nested IfStmts into a single IfStmt with combined conditions.");
                            // Create new IfStmt with combined condition
                            IfStmt newIfStmt = new IfStmt(combinedCondition, innerIf.getThenStmt(), null);
                            statements.set(i, newIfStmt);
                            // Remove the inner IfStmt
                            outerIf.getThenStmt().asBlockStmt().remove(innerIf);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error while visiting BlockStmt: {}", e.getMessage());
        }
    }

    // Helper method to check if a condition is a comparison with a boolean literal (true/false)
    public boolean isComparisonWithBoolenLit(Expression condition, BinaryExpr.Operator operator, boolean b) {
        if (condition.isBinaryExpr()) {
            BinaryExpr binaryExpr = condition.asBinaryExpr();
            if (binaryExpr.getOperator() == operator) {
                Expression left = binaryExpr.getLeft();
                Expression right = binaryExpr.getRight();
                // Check if either side is a boolean literal matching the provided value (true/false)
                if (right.isBooleanLiteralExpr()) {
                    BooleanLiteralExpr booleanLiteralExpr = right.asBooleanLiteralExpr();
                    return booleanLiteralExpr.getValue() == b;
                }
                if (left.isBooleanLiteralExpr()) {
                    BooleanLiteralExpr booleanLiteralExpr = left.asBooleanLiteralExpr();
                    return booleanLiteralExpr.getValue() == b;
                }
            }
        }
        return false;
    }
}
