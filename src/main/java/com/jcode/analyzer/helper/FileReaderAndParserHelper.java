package com.jcode.analyzer.helper;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.jcode.analyzer.context.OperationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileReaderAndParserHelper {

    // SLF4J Logger
    private static final Logger logger = LoggerFactory.getLogger(FileReaderAndParserHelper.class);

    // Method to parse a Java file into a CompilationUnit
    public static ParseResult<CompilationUnit> parse(File file) throws FileNotFoundException {
        JavaParser jp = new JavaParser();
        // Attempt to parse the file and return the result
        ParseResult<CompilationUnit> result = jp.parse(file);

        // Log success or failure message
        if (result.isSuccessful()) {
            logger.info("Successfully parsed file: {}", file.getPath());
        } else {
            logger.error("Error parsing file: {}", file.getPath());
        }
        return result;
    }

    // Method to write the CompilationUnit back to a file
    public static void writeFile(String path, CompilationUnit cu) throws IOException {
        Files.write(Paths.get(path), cu.toString().getBytes());
        // Log confirmation message after writing to the file
        logger.info("Successfully wrote to file: {}", path);
    }

    // Method to get all Java files from the provided context
    public static List<File> getAllJavaFiles() {
        OperationContext ctx = OperationContext.getContext();
        // Retrieve the list of files from the context
        List<File> files = (List<File>) ctx.get("FileList");
        List<File> javaFiles = new ArrayList<>();

        // Iterate through the files and collect Java files
        for (File file : files) {
            if (file.isDirectory()) {
                // Recursively find Java files in the directory
                findJavaFilesInDirectory(file, javaFiles);
            } else if (file.getName().endsWith(".java")) {
                javaFiles.add(file);
            }
        }

        // Log the number of Java files found
        logger.info("Total Java files found: {}", javaFiles.size());
        return javaFiles;
    }

    // Recursive method to find Java files in a directory
    private static void findJavaFilesInDirectory(File directory, List<File> javaFiles) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursively search in subdirectories
                    findJavaFilesInDirectory(file, javaFiles);
                } else if (file.getName().endsWith(".java")) {
                    javaFiles.add(file);
                    // Log message when a Java file is found
                    logger.info("Found Java file: {}", file.getPath());
                }
            }
        }
    }
}
