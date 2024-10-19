package com.jcode.analyzer;

import com.jcode.analyzer.constants.JConstants;
import com.jcode.analyzer.helper.ApplicationHelper;
import com.jcode.analyzer.model.JFileReaderAndParser;
import java.io.IOException;
import java.util.Map;

public class AnalyzerApp {

    public static void main(String[] args) throws IOException {
        ApplicationHelper.init();
        Map argMap = ApplicationHelper.getArgMap();
        ApplicationHelper.populateArgMap(args, argMap);
        if (argMap.get(JConstants.PATH) == null) {
            ApplicationHelper.getManualInput(argMap);
        }
        JFileReaderAndParser read = new JFileReaderAndParser(argMap.get(JConstants.PATH).toString(), (boolean) argMap.get(JConstants.VERBOSE));
        read.readAndParseFile();
    }
}
