package com.giammp.botnet.config;


import com.giammp.botnet.control.AppController;
import com.giammp.botnet.view.BaseOptions;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

public class BotConfigurator {

    public static BaseOptions OPTS = BaseOptions.getInstance();

    public static BotConfiguration loadConfiguration(String[] argv) {
        CommandLine cmd = getCommandLine(argv);
        BotConfiguration config = null;

        if (cmd.hasOption("help")) {
            AppController.printHelp(OPTS);
            System.exit(0);
        } else if (cmd.hasOption("version")) {
            AppController.printVersion();
            System.exit(0);
        }

        if (cmd.hasOption("configuration")) {
            String configPath = cmd.getOptionValue("configuration");
            config = BotConfiguration.fromYaml(configPath);
        } else {
            config = new BotConfiguration();
        }

        if (cmd.hasOption("debug")) {
            config.setDebug(true);
        }

        String args[] = cmd.getArgs();

        if (config.isDebug()) {

        }

        return config;
    }

    private static CommandLine getCommandLine(String argv[]) {
        CommandLineParser cmdParser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = cmdParser.parse(OPTS, argv);
        } catch (ParseException exc) {
            System.err.println("[BOT]> ERROR: " + exc.getMessage());
            AppController.printUsage();
        }

        return cmd;
    }
}
