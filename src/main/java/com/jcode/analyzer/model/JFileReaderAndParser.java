package com.jcode.analyzer.model;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.jcode.analyzer.context.OperationContext;
import com.jcode.analyzer.driver.Beautify;
import com.jcode.analyzer.helper.FileReaderAndParserHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JFileReaderAndParser {

    private static final Logger logger = LoggerFactory.getLogger(JFileReaderAndParser.class);

    private String path;
    private boolean verboseEnable;

    public JFileReaderAndParser(String path, boolean verboseEnable) {
        this.path = path;
        this.verboseEnable = verboseEnable;
    }

    public void readAndParseFile() throws IOException {
        List<File> javaFiles = FileReaderAndParserHelper.getAllJavaFiles();
        int numOfThreads = Runtime.getRuntime().availableProcessors();
        logger.info("Number of processors: {}", numOfThreads);

        ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
        for (File file : javaFiles) {
            OperationContext ctx = OperationContext.getContext().clone();
            executor.submit(() -> processFile(file,ctx));
        }

        shutdownExecutor(executor);
    }

    private void processFile(File file, OperationContext ctx) {
        try {
            ParseResult<CompilationUnit> result = FileReaderAndParserHelper.parse(file);
            if (result.isSuccessful() && result.getResult().isPresent()) {
                CompilationUnit cu = result.getResult().get();
                Beautify.beautifyFile(cu, ctx);
                writeFile(file, cu);
                if (verboseEnable) {
                    logger.info("Successfully processed file: {}", file.getPath());
                }
            } else {
                logger.warn("Failed to parse file: {}", file.getPath());
            }
        } catch (IOException e) {
            logger.error("IO error processing file: {}", file.getPath(), e);
        } catch (Exception e) {
            logger.error("Error processing file: {}", file.getPath(), e);
        }
    }

    private void writeFile(File file, CompilationUnit cu) throws IOException {
        synchronized (JFileReaderAndParser.class) {
            FileReaderAndParserHelper.writeFile(file.getPath(), cu);
        }
    }

    private void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                logger.warn("Executor did not terminate in the specified time.");
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            logger.error("Executor interrupted during shutdown", e);
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
