package com.jcode.analyzer.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.jcode.analyzer.context.OperationContext;
import com.jcode.analyzer.driver.Beautify;
import com.jcode.analyzer.helper.FileReaderAndParserHelper;

public class JFileReaderAndParser {
    private String path;
    private boolean verboseEnable;

    public JFileReaderAndParser(String path, boolean verboseEnable) {
        this.path = path;
        this.verboseEnable = verboseEnable;
    }

    public void readAndParseFile() throws IOException, FileNotFoundException {
        List lst = FileReaderAndParserHelper.getAllJavaFiles();
        for(Object obj : lst) {
            File file = (File) obj;
            ParseResult<CompilationUnit> result = FileReaderAndParserHelper.parse(file);
            if (result.isSuccessful() && result.getResult().isPresent()) {
                CompilationUnit cu = result.getResult().get();
                Beautify.beautifyFile(cu);
                FileReaderAndParserHelper.writeFile(file.getPath(), cu);
            }
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isVerboseEnable() {
        return verboseEnable;
    }

    public void setVerboseEnable(boolean verboseEnable) {
        this.verboseEnable = verboseEnable;
    }
}
