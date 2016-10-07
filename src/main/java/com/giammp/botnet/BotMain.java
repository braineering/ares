package com.giammp.botnet;

import com.giammp.botnet.config.BotConfigurator;
import com.giammp.botnet.config.BotConfiguration;

public class BotMain {

    public static void main(String[] args) {

        final BotConfiguration config = BotConfigurator.loadConfiguration(args);

        if (config.isDebug()) {
            System.out.println(config);
        }

    }
}
