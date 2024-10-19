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
import java.util.List;

public class ConditionalVisitor extends VoidVisitorAdapter<Void> {

    @Override
    public void visit(IfStmt n, Void args) {
        super.visit(n, args);
        Expression condition = n.getCondition();
        if (isComparisonWithBoolenLit(condition, BinaryExpr.Operator.EQUALS, true)) {
            BinaryExpr binaryExpr = (BinaryExpr) condition;
            n.setCondition(binaryExpr.getLeft());
        } else if (isComparisonWithBoolenLit(condition, BinaryExpr.Operator.EQUALS, false)) {
            BinaryExpr binaryExpr = (BinaryExpr) condition;
            n.setCondition(new UnaryExpr(binaryExpr.getLeft(), UnaryExpr.Operator.LOGICAL_COMPLEMENT));
        } else if (isComparisonWithBoolenLit(condition, BinaryExpr.Operator.NOT_EQUALS, true)) {
            BinaryExpr binaryExpr = (BinaryExpr) condition;
            n.setCondition(new UnaryExpr(binaryExpr.getLeft(), UnaryExpr.Operator.LOGICAL_COMPLEMENT));
        } else if (isComparisonWithBoolenLit(condition, BinaryExpr.Operator.NOT_EQUALS, false)) {
            BinaryExpr binaryExpr = (BinaryExpr) condition;
            n.setCondition(binaryExpr.getLeft());
        }
    }

    @Override
    public void visit(WhileStmt n, Void args) {
        super.visit(n, args);
        Expression condition = n.getCondition();
        if (isComparisonWithBoolenLit(condition, BinaryExpr.Operator.EQUALS, true)) {
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
                    if (outerIf.getThenStmt().isBlockStmt() && outerIf.getThenStmt().asBlockStmt().getStatements().size() == 1 && outerIf.getThenStmt().asBlockStmt().getStatement(0).isIfStmt() && outerIf.getThenStmt().asBlockStmt().getStatement(0).asIfStmt().equals(innerIf)) {
                        Expression combinedCondition = new BinaryExpr(outerIf.getCondition(), innerIf.getCondition(), BinaryExpr.Operator.AND);
                        IfStmt newIfStmt = new IfStmt(combinedCondition, innerIf.getThenStmt(), null);
                        statements.set(i, newIfStmt);
                        outerIf.getThenStmt().asBlockStmt().remove(innerIf);
                    }
                }
            }
        }
    }

    public boolean isComparisonWithBoolenLit(Expression condition, BinaryExpr.Operator operator, boolean b) {
        if (condition.isBinaryExpr()) {
            BinaryExpr binaryExpr = condition.asBinaryExpr();
            if (binaryExpr.getOperator() == operator) {
                Expression left = binaryExpr.getLeft();
                Expression right = binaryExpr.getRight();
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
