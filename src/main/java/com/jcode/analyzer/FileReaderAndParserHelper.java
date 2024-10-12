package com.jcode.analyzer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FileReaderAndParserHelper {
    public static ParseResult<CompilationUnit> parse(File file) throws FileNotFoundException {
              JavaParser jp =  new JavaParser();
              return jp.parse(file);
    }

    public static void beautifyFile(CompilationUnit cu) {
        List<ImportDeclaration> imports = cu.getImports();
        if(imports != null) {
            Set<String> usedImports = findUsedImports(cu);
            removeUnUsedImports(cu, usedImports, imports);
        }
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
    public static void writeFile(String path,CompilationUnit cu) throws IOException {
        Files.write(Paths.get(path),cu.toString().getBytes());
    }
}
