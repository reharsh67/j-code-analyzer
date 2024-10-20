package com.jcode.analyzer.helper;

import com.jcode.analyzer.constants.JConstants;
import com.jcode.analyzer.context.OperationContext;
import com.jcode.analyzer.context.OperationContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ApplicationHelper {

    // SLF4J Logger
    private static final Logger logger = LoggerFactory.getLogger(ApplicationHelper.class);

    // Initialize argument map with default values
    public static Map<String, Object> getArgMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(JConstants.PATH, null);
        map.put(JConstants.VERBOSE, false);
        map.put(JConstants.ALL, false);
        map.put(JConstants.CONDITIONAL, false);
        map.put(JConstants.VAR, false);
        map.put(JConstants.EMPTYSTMT, false);
        map.put(JConstants.INDENTANDWHITE, false);
        return map;
    }

    // Populate argument map with command-line arguments
    public static void populateArgMap(String[] args, Map<String, Object> argMap) {
        List<File> files = new ArrayList<>();
        OperationContext ctx = OperationContext.getContext();

        for (String arg : args) {
            if (arg.startsWith("--")) {
                processArg(arg, argMap, ctx);
            } else {
                processFilePath(arg, argMap, files, ctx);
            }
        }
        ctx.add2ApplicationContext("FileList", files);
        ctx.add2ApplicationContext("JARGS", argMap);
    }

    // Handle individual argument processing
    private static void processArg(String arg, Map<String, Object> argMap, OperationContext ctx) {
        switch (arg) {
            case JConstants.ALL:
                updateArgMapAndContext(argMap, ctx, JConstants.ALL, true);
                break;
            case JConstants.CONDITIONAL:
                updateArgMapAndContext(argMap, ctx, JConstants.CONDITIONAL, true);
                break;
            case JConstants.VAR:
                updateArgMapAndContext(argMap, ctx, JConstants.VAR, true);
                break;
            case JConstants.INDENTANDWHITE:
                updateArgMapAndContext(argMap, ctx, JConstants.INDENTANDWHITE, true);
                break;
            case JConstants.IMPORTS:
                updateArgMapAndContext(argMap, ctx, JConstants.IMPORTS, true);
                break;
            case JConstants.EMPTYSTMT:
                updateArgMapAndContext(argMap, ctx, JConstants.EMPTYSTMT, true);
                break;
            default:
                logger.warn("Unknown option: {}", arg);
                break;
        }
    }

    // Helper method to update the argument map and context
    private static void updateArgMapAndContext(Map<String, Object> argMap, OperationContext ctx, String key, boolean value) {
        argMap.put(key, value);
        ctx.add2ApplicationContext(key, value);
    }

    // Handle manual input for missing arguments
    public static void getManualInput(Map<String, Object> argMap) {
        List<File> files = new ArrayList<>();
        OperationContext ctx = OperationContext.getContext();

        try (Scanner sc = new Scanner(System.in)) {
            logger.info("Please provide path/to/your/java/file.java:");
            String path = sc.nextLine();
            processFilePath(path, argMap, files, ctx);

            logger.info("Would you like verbose to be enabled (Y/N)?");
            String verVal = sc.nextLine();
            boolean verbose = verVal.equalsIgnoreCase("y");
            argMap.put(JConstants.VERBOSE, verbose);
            ctx.add2ApplicationContext(JConstants.VERBOSE, verbose);

            logger.info("Since this is manual input, all options will be considered.");
            argMap.put(JConstants.ALL, true);
            ctx.add2ApplicationContext(JConstants.ALL, true);
            ctx.add2ApplicationContext("FileList", files);
        }
    }

    // Process the file path and add it to the context
    private static void processFilePath(String path, Map<String, Object> argMap, List<File> files, OperationContext ctx) {
        File file = new File(path);
        if (file.exists()) {
            argMap.put(JConstants.PATH, path);
            ctx.add2ApplicationContext(JConstants.PATH, file.getAbsolutePath());
            files.add(file);
            logger.info("Added file to context: {}", file.getAbsolutePath());
        } else {
            logger.error("File or directory not found: {}", path);
        }
    }

    // Initialize the context
    public static void init() {
        if (!isAlreadySet()) {
            OperationContextImpl.setContext(new OperationContextImpl());
            logger.info("Operation context initialized.");
        }
    }

    // Check if the context is already set
    private static boolean isAlreadySet() {
        return OperationContextImpl.getContext() != null;
    }
}
