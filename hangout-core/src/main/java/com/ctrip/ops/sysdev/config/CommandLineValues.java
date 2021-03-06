package com.ctrip.ops.sysdev.config;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Level;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * Created by gnuhpc on 2016/12/11.
 */

@Data
public class CommandLineValues {
    private final String[] arguments;
    private CmdLineParser parser;

    @Option(name = "-h", aliases = {"--help"}, usage = "Print Help Information", required = false, help = true)
    private boolean isHelp = false;

    @Option(name = "-ll", aliases = {"--loglevel"}, usage = "Set log level: INFO (default), DEBUG, TRACE", required = false, handler = LogLevelOptionhandler.class)
    private Level logLevel = Level.WARN;

    @Option(name = "-f", aliases = {"--configfile"}, usage = " Specify a config file", required = true)
    private String configFile;

    @Option(name = "-l", aliases = {"--logfile"}, usage = "Specify a log file", required = false)
    private String logFile;

    @Option(name = "--version", usage = "Show Hangout Version", required = false)
    private boolean isShowVersion;

    @Option(name = "-v", usage = "set log level to info", required = false)
    private boolean infoLevel = false;

    @Option(name = "-vv", usage = "set log level to debug", required = false)
    private boolean debugLevel = false;

    @Option(name = "-vvvv", usage = "set log level to trace", required = false)
    private boolean traceLevel = false;

    public CommandLineValues(String... args) {
        parser = new CmdLineParser(this);
        arguments = args;
    }

    public void parseCmd() {
        printVersion();

        try {
            parser.parseArgument(arguments);

            // If help is needed
            if (isHelp) {
                parser.printUsage(System.err);
                System.exit(0);
            }
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            // print the list of available options
            System.exit(-1);
        }
    }

    private void printVersion() {
        System.out.println("Hangout  Version:" + getVersion() + "  Copyright @Ctrip   Author : childe@github, guuhpc@github");
    }

    //TODO not implementaion yet, reading from pom.xml is the goal
    private String getVersion() {
        return "0.19";
    }

    private void printUsage() {
        System.out.println("hangout [options...] arguments...");
        parser.printUsage(System.out);
        System.err.println();
    }

    public Level customGetLogLevel() {
        if (this.traceLevel) {
            return Level.TRACE;
        }
        if (this.debugLevel) {
            return Level.DEBUG;
        }
        if (this.infoLevel) {
            return Level.INFO;
        }

        return this.logLevel;
    }
}
