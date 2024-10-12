package com.jcode.analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;

public class JFileReaderAndParser {
    private String path;
    private boolean verboseEnable;

    public JFileReaderAndParser(String path, boolean verboseEnable) {
        this.path = path;
        this.verboseEnable = verboseEnable;
    }

    public void readAndParseFile() throws IOException, FileNotFoundException {
        File file = new File(this.getPath());
        ParseResult<CompilationUnit> result = FileReaderAndParserHelper.parse(file);
        if (result.isSuccessful() && result.getResult().isPresent()){
            CompilationUnit cu = result.getResult().get();
            FileReaderAndParserHelper.beautifyFile(cu);
            FileReaderAndParserHelper.writeFile(this.getPath(),cu);
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
