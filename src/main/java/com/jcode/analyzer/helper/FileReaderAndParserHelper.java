package com.jcode.analyzer.helper;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.jcode.analyzer.context.OperationContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileReaderAndParserHelper {
    public static ParseResult<CompilationUnit> parse(File file) throws FileNotFoundException {
              JavaParser jp =  new JavaParser();
              return jp.parse(file);
    }

    public static void writeFile(String path,CompilationUnit cu) throws IOException {
        Files.write(Paths.get(path),cu.toString().getBytes());
    }

    public static List<File> getAllJavaFiles() {
        OperationContext ctx = OperationContext.getContext();
        List files = (List) ctx.get("FileList");
        List<File> javaFiles = new ArrayList<>();
        for (Object obj  : files) {
            File file = (File) obj;
            if (file.isDirectory()) {
                // Recursively add Java files from directory
                findJavaFilesInDirectory(file, javaFiles);
            } else if (file.getName().endsWith(".java")) {
                javaFiles.add(file);
            }
        }
        return javaFiles;
    }

    private static void findJavaFilesInDirectory(File directory, List<File> javaFiles) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findJavaFilesInDirectory(file, javaFiles);
                } else if (file.getName().endsWith(".java")) {
                    javaFiles.add(file);
                }
            }
        }
    }
}
