package com.jcode.analyzer;

import com.jcode.analyzer.constants.JConstants;
import com.jcode.analyzer.helper.ApplicationHelper;
import com.jcode.analyzer.model.JFileReaderAndParser;
import java.io.IOException;
import java.util.Map;

public class AnalyzerApp {

    public static void main(String[] args) throws IOException {
        // Initialize application settings and resources
        ApplicationHelper.init();
        System.out.println("Application initialized.");
        // Get an empty argument map
        Map argMap = ApplicationHelper.getArgMap();
        // Populate the argument map with command-line arguments
        ApplicationHelper.populateArgMap(args, argMap);
        System.out.println("Arguments populated: " + argMap);
        // Check if the PATH argument is missing, prompt the user for manual input
        if (argMap.get(JConstants.PATH) == null) {
            System.out.println("Path argument is missing. Asking for manual input...");
            ApplicationHelper.getManualInput(argMap);
        }
        System.out.println("Path to analyze: " + argMap.get(JConstants.PATH));
        // Create a file reader and parser instance with the provided path and verbosity flag
        JFileReaderAndParser read = new JFileReaderAndParser(argMap.get(JConstants.PATH).toString(), (boolean) argMap.get(JConstants.VERBOSE));
        System.out.println("Starting to read and parse the file: " + argMap.get(JConstants.PATH));
        // Read and parse the file
        read.readAndParseFile();
        System.out.println("File parsing completed.");
    }
}
