package com.jcode.analyzer;

import java.io.IOException;
import java.util.Scanner;

public class AnalyzerApp
{

    private static String VERBOSE = "--verbose";
    public static void main( String[] args ) throws IOException {

        String path;
        boolean verboseEnabled = false;
        if(args.length > 0){
            path = args[0];
            if(args[1].equals(VERBOSE)) verboseEnabled = true;
        }
        else{
            Scanner  sc = new Scanner(System.in);
            System.out.println("Please provide path/to/your/java/file.java");
            path = sc.nextLine();
            System.out.println("Would you like verbose to be enabled Y/N ?");
            String verVal = sc.nextLine();
            if(verVal.equals("y")){
                verboseEnabled = true;
            }

            JFileReaderAndParser read = new JFileReaderAndParser(path,verboseEnabled);
            read.readAndParseFile();

        }




    }
}
