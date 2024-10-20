package com.jcode.analyzer.driver;

import com.github.javaparser.ast.CompilationUnit;
import com.jcode.analyzer.constants.JConstants;
import com.jcode.analyzer.context.OperationContext;
import com.jcode.analyzer.visitors.ConditionalVisitor;
import com.jcode.analyzer.visitors.EmptyStmtVisitor;
import com.jcode.analyzer.visitors.ImportsVisitor;
import com.jcode.analyzer.visitors.UnUsedVariablesVisitor;
import com.jcode.analyzer.visitors.WhiteSpaceAndIndentVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Beautify {

    // SLF4J Logger
    private static final Logger logger = LoggerFactory.getLogger(Beautify.class);

    // Method to beautify the Java file represented by the CompilationUnit
    public static void beautifyFile(CompilationUnit cu, OperationContext ctx) throws IOException {
        try {
            // Check if all beautification options are enabled
            if (Boolean.TRUE.equals(ctx.get(JConstants.ALL))) {
                logger.info("Applying all beautification options...");
                applyAllVisitors(cu);
            } else {
                // Apply individual beautification options based on the context
                applySelectedVisitors(cu, ctx);
            }
            // Log completion message
            logger.info("Beautification process completed for: {}", cu.getPrimaryTypeName().orElse("Unnamed class"));
        } catch (Exception e) {
            logger.error("Beautification process failed for: {}", cu.getPrimaryTypeName().orElse("Unnamed class"), e);
            throw new IOException("Error during beautification", e);
        }
    }

    // Method to apply all visitors
    private static void applyAllVisitors(CompilationUnit cu) {
        cu.accept(new ConditionalVisitor(), null);
        new ImportsVisitor().analyze(cu);
        new UnUsedVariablesVisitor().analyze(cu);
        cu.accept(new WhiteSpaceAndIndentVisitor(), null);
        new EmptyStmtVisitor().analyze(cu);
    }

    // Method to apply selected visitors based on the context
    private static void applySelectedVisitors(CompilationUnit cu, OperationContext ctx) {
        if (Boolean.TRUE.equals(ctx.get(JConstants.CONDITIONAL))) {
            logger.info("Applying conditional checks...");
            cu.accept(new ConditionalVisitor(), null);
        }
        if (Boolean.TRUE.equals(ctx.get(JConstants.INDENTANDWHITE))) {
            logger.info("Checking whitespace and indentation...");
            cu.accept(new WhiteSpaceAndIndentVisitor(), null);
        }
        if (Boolean.TRUE.equals(ctx.get(JConstants.VAR))) {
            logger.info("Analyzing unused variables...");
            cu.accept(new UnUsedVariablesVisitor(), null);
        }
        if (Boolean.TRUE.equals(ctx.get(JConstants.IMPORTS))) {
            logger.info("Optimizing imports...");
            cu.accept(new ImportsVisitor(), null);
        }
        if (Boolean.TRUE.equals(ctx.get(JConstants.EMPTYSTMT))) {
            logger.info("Removing empty statements...");
            cu.accept(new EmptyStmtVisitor(), null);
        }
    }
}
