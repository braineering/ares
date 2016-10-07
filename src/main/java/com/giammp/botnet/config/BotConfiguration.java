package com.giammp.botnet.config;

import com.giammp.botnet.model.Target;
import com.giammp.botnet.model.Proxy;
import com.giammp.botnet.model.SleepCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class BotConfiguration {
    private boolean sysInfo;
    private boolean netInfo;
    private boolean sysStat;
    private boolean netStat;
    private String logfile;
    private List<Target> targets;
    private List<Proxy> proxies;
    private List<SleepCondition> sleep;
    private boolean debug;

    public BotConfiguration() {
        this.sysInfo = true;
        this.netInfo = true;
        this.sysStat = true;
        this.netStat = true;
        this.logfile = "./botlog.txt";
        this.targets = new ArrayList<Target>();
        this.proxies = new ArrayList<Proxy>();
        this.sleep = new ArrayList<SleepCondition>();
        this.debug = false;
    }

    public static BotConfiguration fromYaml(final String path) {
        BotConfiguration config;

        Yaml yaml = new Yaml(YamlConstructor.getInstance());

        FileReader file;
        try {
            file = new FileReader(path);
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            return new BotConfiguration();
        }

        config = yaml.loadAs(file, BotConfiguration.class);

        if (config == null) config = new BotConfiguration();

        if (config.getTargets() == null) config.setTargets(new ArrayList<Target>());
        if (config.getProxies() == null) config.setProxies(new ArrayList<Proxy>());
        if (config.getSleep() == null) config.setSleep(new ArrayList<SleepCondition>());

        return config;
    }
}
