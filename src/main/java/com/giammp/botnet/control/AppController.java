package com.giammp.botnet.control;


import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class AppController {

    public static final String APP_NAME = "BOT";
    public static final String TEAM_NAME = "Giammp Team";
    public static final String APP_VERSION = "1.0.0";
    public static final String APP_DESCRIPTION = "A simple bot that collects system/network info/stats.\n";

    public static final void printUsage() {
        System.out.format("%s version %s (by %s)\n", APP_NAME, APP_VERSION, TEAM_NAME);
        System.out.format("%s\n", APP_DESCRIPTION);
        System.out.format("Usage: %s [options,...]\n", APP_NAME);
    }

    public static final void printHelp(final Options opts) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(APP_NAME, opts, true);
    }

    public static final void printVersion() {
        System.out.format("%s version %s (by %s)\n", APP_NAME, APP_VERSION, TEAM_NAME);
    }

    public static final void quit() {
        System.exit(0);
    }

}
