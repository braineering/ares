/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Giacomo Marciani, Michele Porretta
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.acmutv.botnet.config;

import com.acmutv.botnet.target.HttpTarget;
import com.acmutv.botnet.target.HttpTargetProxy;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.quartz.CronExpression;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class realizes the configuration model for the whole application.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpTarget
 * @see CronExpression
 * @see Yaml
 */
@Data
@AllArgsConstructor
public class BotConfiguration {

  // Default values
  public static boolean SYS_INFO = true;
  public static boolean NET_INFO = true;
  public static boolean SYS_STAT = true;
  public static boolean NET_STAT = true;
  public static long SYS_STAT_FREQ = 60;
  public static long NET_STAT_FREQ = 60;
  public static String INIT_RESOURCE = "botinit.txt";
  public static String CMD_RESOURCE = "botcmd.txt";
  public static String LOG_RESOURCE = "botlog.txt";
  public static HttpTargetProxy PROXY = null;
  public static long MAX_TIME = 0;
  public static boolean DEBUG = false;

  private boolean sysInfo;
  private boolean netInfo;
  private boolean sysStat;
  private boolean netStat;
  private long sysStatFreq;
  private long netStatFreq;
  private String initResource;
  private String cmdResource;
  private String logResource;
  private List<HttpTarget> httpTargets;
  private HttpTargetProxy proxy;
  private List<String> sleep;
  private long maxTime;
  private boolean debug;

  private static BotConfiguration instance;

  /**
   * Retrieves the class singleton.
   * @return the singleton
   */
  public static synchronized BotConfiguration getInstance() {
    if (instance == null) {
      synchronized (BotConfiguration.class) {
        if (instance == null) {
          instance = new BotConfiguration();
        }
      }
    }
    return instance;
  }

  /**
   * Creates the default configuration.
   */
  public BotConfiguration() {
    this.sysInfo = SYS_INFO;
    this.netInfo = NET_INFO;
    this.sysStat = SYS_STAT;
    this.netStat = NET_STAT;
    this.sysStatFreq = SYS_STAT_FREQ;
    this.netStatFreq = NET_STAT_FREQ;
    this.logResource = LOG_RESOURCE;
    this.cmdResource = CMD_RESOURCE;
    this.proxy = PROXY;
    this.httpTargets = new ArrayList<HttpTarget>();
    this.sleep = new ArrayList<String>();
    this.maxTime = MAX_TIME;
    this.debug = DEBUG;
  }

  public BotConfiguration fromDefault() {
    this.sysInfo = SYS_INFO;
    this.netInfo = NET_INFO;
    this.sysStat = SYS_STAT;
    this.netStat = NET_STAT;
    this.sysStatFreq = SYS_STAT_FREQ;
    this.netStatFreq = NET_STAT_FREQ;
    this.logResource = LOG_RESOURCE;
    this.cmdResource = CMD_RESOURCE;
    this.proxy = PROXY;
    this.httpTargets = new ArrayList<HttpTarget>();
    this.sleep = new ArrayList<String>();
    this.maxTime = MAX_TIME;
    this.debug = DEBUG;

    return this;
  }

  /**
   * Overwrites the current configuration with the one in the specified YAML file.
   * If something goes wrong, the default configuration is loaded.
   * @param path The absolute path to the configuration file.
   * @return The configuration.
   */
  public BotConfiguration fromYaml(final String path) {
    Yaml yaml = new Yaml(YamlConstructor.getInstance());

    FileReader file;
    try {
      file = new FileReader(path);
    } catch (Exception exc) {
      System.out.println(exc.getMessage());
      return new BotConfiguration();
    }

    BotConfiguration config = yaml.loadAs(file, BotConfiguration.class);

    return this.fromConfig(config);
  }

  /**
   * Overwrites the current configuration with the one in the specified YAML file.
   * If something goes wrong, the default configuration is loaded.
   * @param configStream the configuration YAML file.
   * @return The current configuration.
   */
  public BotConfiguration fromYaml(InputStream configStream) {
    Yaml yaml = new Yaml(YamlConstructor.getInstance());

    BotConfiguration config = yaml.loadAs(configStream, BotConfiguration.class);

    return this.fromConfig(config);
  }

  /**
   * Overwrites the current configuration with the one specified.
   * @param config the configuration to overwrite with.
   * @return The current configuration.
   */
  public BotConfiguration fromConfig(final BotConfiguration config) {
    if (config == null) {
      return this;
    }

    if (config.getHttpTargets() == null) {
      config.setHttpTargets(new ArrayList<HttpTarget>());
    }

    if (config.getSleep() == null) {
      config.setSleep(new ArrayList<String>());
    }

    this.sysInfo = config.isSysInfo();
    this.netInfo = config.isNetInfo();
    this.sysStat = config.isSysStat();
    this.netStat = config.isNetStat();
    this.sysStatFreq = config.getSysStatFreq();
    this.netStatFreq = config.getNetStatFreq();
    this.logResource = config.getLogResource();
    this.cmdResource = config.getCmdResource();
    this.proxy = config.getProxy();
    this.httpTargets = config.getHttpTargets();
    this.sleep = config.getSleep();
    this.maxTime = config.getMaxTime();
    this.debug = config.isDebug();

    return this;
  }

}
