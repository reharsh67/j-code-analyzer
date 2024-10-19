package com.jcode.analyzer.driver;

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
import com.jcode.analyzer.constants.JConstants;
import com.jcode.analyzer.context.OperationContext;
import com.jcode.analyzer.visitors.ConditionalVisitor;
import com.jcode.analyzer.visitors.EmptyStmtVisitor;
import com.jcode.analyzer.visitors.ImportsVisitor;
import com.jcode.analyzer.visitors.UnUsedVariablesVisitor;
import com.jcode.analyzer.visitors.WhiteSpaceAndIndentVisitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class Beautify {

    public static void beautifyFile(CompilationUnit cu,OperationContext ctx) throws IOException {
        if((boolean)ctx.get(JConstants.ALL)) {
            cu.accept(new ConditionalVisitor(), null);
            new ImportsVisitor().analyze(cu);
            new UnUsedVariablesVisitor().analyze(cu);
            cu.accept(new WhiteSpaceAndIndentVisitor(), null);
            new EmptyStmtVisitor().analyze(cu);

        }
        else{
            if((boolean)ctx.get(JConstants.CONDITIONAL)){
                cu.accept(new ConditionalVisitor(), null);
            }
            if((boolean)ctx.get(JConstants.INDENTANDWHITE)){
                cu.accept(new WhiteSpaceAndIndentVisitor(), null);
            }
            if((boolean)ctx.get(JConstants.VAR)){
                cu.accept(new UnUsedVariablesVisitor(),null);
            }
            if((boolean)ctx.get(JConstants.IMPORTS)){
                cu.accept(new ImportsVisitor(),null);
            }
            if((boolean)ctx.get(JConstants.EMPTYSTMT)){
                cu.accept(new EmptyStmtVisitor(),null);
            }
        }
    }

}
