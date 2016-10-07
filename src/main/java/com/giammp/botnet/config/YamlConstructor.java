package com.giammp.botnet.config;


import com.giammp.botnet.model.Target;
import com.giammp.botnet.model.Proxy;
import com.giammp.botnet.model.SleepCondition;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Constructor;

public class YamlConstructor extends Constructor {

    private static YamlConstructor instance;

    public static YamlConstructor getInstance() {
        if (instance == null) {
            instance = new YamlConstructor();
        }
        return instance;
    }

    private YamlConstructor() {
        super(BotConfiguration.class);
        TypeDescription description = new TypeDescription(BotConfiguration.class);
        description.putListPropertyType("targets", Target.class);
        description.putListPropertyType("proxies", Proxy.class);
        description.putListPropertyType("sleep", SleepCondition.class);
        super.addTypeDescription(description);
    }
}
