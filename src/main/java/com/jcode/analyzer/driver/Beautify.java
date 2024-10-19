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

    public static void beautifyFile(CompilationUnit cu, OperationContext ctx) throws IOException {
        if ((boolean) ctx.get(JConstants.ALL)) {
            cu.accept(new ConditionalVisitor(), null);
            new ImportsVisitor().analyze(cu);
            new UnUsedVariablesVisitor().analyze(cu);
            cu.accept(new WhiteSpaceAndIndentVisitor(), null);
            new EmptyStmtVisitor().analyze(cu);
        } else {
            if ((boolean) ctx.get(JConstants.CONDITIONAL)) {
                cu.accept(new ConditionalVisitor(), null);
            }
            if ((boolean) ctx.get(JConstants.INDENTANDWHITE)) {
                cu.accept(new WhiteSpaceAndIndentVisitor(), null);
            }
            if ((boolean) ctx.get(JConstants.VAR)) {
                cu.accept(new UnUsedVariablesVisitor(), null);
            }
            if ((boolean) ctx.get(JConstants.IMPORTS)) {
                cu.accept(new ImportsVisitor(), null);
            }
            if ((boolean) ctx.get(JConstants.EMPTYSTMT)) {
                cu.accept(new EmptyStmtVisitor(), null);
            }
        }
    }
}
