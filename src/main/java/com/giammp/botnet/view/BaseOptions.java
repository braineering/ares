package com.giammp.botnet.view;


import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class BaseOptions extends Options {

    private static final long serialVersionUID = 1L;

    public static final String DESCRIPTION_CONFIGURATION = "YAML configuration file.";
    public static final String DESCRIPTION_DEBUG = "Debug mode.";
    public static final String DESCRIPTION_HELP = "Project helper.";
    public static final String DESCRIPTION_VERSION = "Project version.";

    private static BaseOptions instance;

    public static BaseOptions getInstance() {
        if (instance == null)
            instance = new BaseOptions();
        return instance;
    }

    private BaseOptions() {
        Option debug = this.optDebug();
        Option configuration = this.optConfiguration();
        Option help = this.optHelp();
        Option version = this.optVersion();

        super.addOption(debug);
        super.addOption(configuration);
        super.addOption(help);
        super.addOption(version);
    }

    private Option optDebug() {
        return Option.builder("D")
                .longOpt("debug")
                .desc(DESCRIPTION_DEBUG)
                .required(false)
                .hasArg(false)
                .build();
    }

    private Option optConfiguration() {
        return Option.builder("c")
                .longOpt("configuration")
                .desc(DESCRIPTION_CONFIGURATION)
                .required(false)
                .hasArg(true)
                .numberOfArgs(1)
                .argName("YAML-FILE")
                .build();
    }

    private Option optHelp() {
        return Option.builder("h")
                .longOpt("help")
                .desc(DESCRIPTION_HELP)
                .required(false)
                .hasArg(false)
                .build();
    }

    private Option optVersion() {
        return Option.builder("v")
                .longOpt("version")
                .desc(DESCRIPTION_VERSION)
                .required(false)
                .hasArg(false)
                .build();
    }

}
