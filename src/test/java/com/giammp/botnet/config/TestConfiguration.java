package com.giammp.botnet.config;


import com.giammp.botnet.model.Target;
import com.giammp.botnet.model.Proxy;
import com.giammp.botnet.model.SleepCondition;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestConfiguration {

    @Test
    public void testLoadYamlDefault() {
        String path = "src/test/resources/config.default.yml";
        BotConfiguration config = BotConfiguration.fromYaml(path);
        BotConfiguration expected = new BotConfiguration();
        assertEquals(expected, config);
    }

    @Test
    public void testLoadYamlComplete() {
        String path = "src/test/resources/config.complete.yml";
        BotConfiguration config = BotConfiguration.fromYaml(path);
        BotConfiguration expected = new BotConfiguration();
        expected.setSysInfo(true);
        expected.setNetInfo(false);
        expected.setSysStat(true);
        expected.setNetStat(false);
        expected.setLogfile("/home/giammp/botnet/log.txt");
        expected.getTargets().add(new Target("http://www.target1.com", 10000, 20000, 10));
        expected.getTargets().add(new Target("http://www.target2.com", 15000, 20000, 15));
        expected.getProxies().add(new Proxy("http://www.proxy1.com"));
        expected.getProxies().add(new Proxy("http://www.proxy2.com"));
        expected.getSleep().add(new SleepCondition("expression1"));
        expected.getSleep().add(new SleepCondition("expression2"));
        expected.setDebug(true);
        assertEquals(expected, config);
    }

    @Test
    public void loadYamlIncomplete() {
        String path = "src/test/resources/config.incomplete.yml";
        BotConfiguration config = BotConfiguration.fromYaml(path);
        BotConfiguration expected = new BotConfiguration();
        expected.setSysStat(false);
        expected.setNetStat(false);
        assertEquals(expected, config);
    }

    @Test
    public void loadYamlEmpty() {
        String path = "src/test/resources/config.empty.yml";
        BotConfiguration config = BotConfiguration.fromYaml(path);
        BotConfiguration expected = new BotConfiguration();
        assertEquals(expected, config);
    }
}
