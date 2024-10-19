package com.jcode.analyzer.driver;

import com.github.javaparser.ast.CompilationUnit;
import com.jcode.analyzer.constants.JConstants;
import com.jcode.analyzer.context.OperationContext;
import com.jcode.analyzer.visitors.ConditionalVisitor;
import com.jcode.analyzer.visitors.EmptyStmtVisitor;
import com.jcode.analyzer.visitors.ImportsVisitor;
import com.jcode.analyzer.visitors.UnUsedVariablesVisitor;
import com.jcode.analyzer.visitors.WhiteSpaceAndIndentVisitor;
import java.io.IOException;

public class Beautify {

    // Method to beautify the Java file represented by the CompilationUnit
    public static void beautifyFile(CompilationUnit cu, OperationContext ctx) throws IOException {
        // Check if all beautification options are enabled
        if ((boolean) ctx.get(JConstants.ALL)) {
            System.out.println("Applying all beautification options...");
            cu.accept(new ConditionalVisitor(), null);
            new ImportsVisitor().analyze(cu);
            new UnUsedVariablesVisitor().analyze(cu);
            cu.accept(new WhiteSpaceAndIndentVisitor(), null);
            new EmptyStmtVisitor().analyze(cu);
        } else {
            // Apply individual beautification options based on the context
            if ((boolean) ctx.get(JConstants.CONDITIONAL)) {
                System.out.println("Applying conditional checks...");
                cu.accept(new ConditionalVisitor(), null);
            }
            if ((boolean) ctx.get(JConstants.INDENTANDWHITE)) {
                System.out.println("Checking whitespace and indentation...");
                cu.accept(new WhiteSpaceAndIndentVisitor(), null);
            }
            if ((boolean) ctx.get(JConstants.VAR)) {
                System.out.println("Analyzing unused variables...");
                cu.accept(new UnUsedVariablesVisitor(), null);
            }
            if ((boolean) ctx.get(JConstants.IMPORTS)) {
                System.out.println("Optimizing imports...");
                cu.accept(new ImportsVisitor(), null);
            }
            if ((boolean) ctx.get(JConstants.EMPTYSTMT)) {
                System.out.println("Removing empty statements...");
                cu.accept(new EmptyStmtVisitor(), null);
            }
        }
        // Print completion message
        System.out.println("Beautification process completed.");
    }
}
