package com.jcode.analyzer;

import com.jcode.analyzer.constants.JConstants;
import com.jcode.analyzer.helper.ApplicationHelper;
import com.jcode.analyzer.model.JFileReaderAndParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class AnalyzerApp {

    // Initialize SLF4J logger
    private static final Logger logger = LoggerFactory.getLogger(AnalyzerApp.class);

    public static void main(String[] args) throws IOException {
        // Initialize application settings and resources
        ApplicationHelper.init();
        logger.info("Application initialized.");

        // Get an empty argument map
        Map<String, Object> argMap = ApplicationHelper.getArgMap();

        // Populate the argument map with command-line arguments
        ApplicationHelper.populateArgMap(args, argMap);
        logger.info("Arguments populated: {}", argMap);

        // Check if the PATH argument is missing, prompt the user for manual input
        if (argMap.get(JConstants.PATH) == null) {
            logger.warn("Path argument is missing. Asking for manual input...");
            ApplicationHelper.getManualInput(argMap);
        }

        logger.info("Path to analyze: {}", argMap.get(JConstants.PATH));

        // Create a file reader and parser instance with the provided path and verbosity flag
        JFileReaderAndParser reader = new JFileReaderAndParser(
                argMap.get(JConstants.PATH).toString(),
                (boolean) argMap.get(JConstants.VERBOSE)
        );
        logger.info("Starting to read and parse the file: {}", argMap.get(JConstants.PATH));

        // Read and parse the file
        reader.readAndParseFile();
        logger.info("File parsing completed.");
    }
}

