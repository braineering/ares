/*
  The MIT License (MIT)

  Copyright (c) 2016 Giacomo Marciani and Michele Porretta

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:


  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.


  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
 */

package com.acmutv.botnet.config;

import com.acmutv.botnet.target.HttpTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.quartz.CronExpression;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.InputStream;

/**
 * This class realizes the configuration model for the whole application.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see Configurator
 * @see Yaml
 * @see HttpTarget
 * @see CronExpression
 */
@Data
@AllArgsConstructor
public class Configuration {

  // Default values
  public static boolean SYS_INFO = true;
  public static boolean NET_INFO = true;
  public static boolean SYS_STAT = true;
  public static boolean NET_STAT = true;
  public static long SYS_STAT_FREQ = 60;
  public static long NET_STAT_FREQ = 60;
  public static String INIT_RESOURCE = "com/acmutv/botnet/botinit.txt";
  public static String CMD_RESOURCE = "com/acmutv/botnet/botcmd.txt";
  public static String LOG_RESOURCE = "com/acmutv/botnet/botlog.txt";
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
  private long maxTime;
  private boolean debug;

  private static Configuration instance;

  /**
   * Retrieves the class singleton.
   * @return the singleton
   */
  public static synchronized Configuration getInstance() {
    if (instance == null) {
      synchronized (Configuration.class) {
        if (instance == null) {
          instance = new Configuration();
        }
      }
    }
    return instance;
  }

  /**
   * Creates the default configuration.
   */
  public Configuration() {
    this.sysInfo = SYS_INFO;
    this.netInfo = NET_INFO;
    this.sysStat = SYS_STAT;
    this.netStat = NET_STAT;
    this.sysStatFreq = SYS_STAT_FREQ;
    this.netStatFreq = NET_STAT_FREQ;
    this.initResource = INIT_RESOURCE;
    this.cmdResource = CMD_RESOURCE;
    this.logResource = LOG_RESOURCE;
    this.maxTime = MAX_TIME;
    this.debug = DEBUG;
  }

  public Configuration fromDefault() {
    this.sysInfo = SYS_INFO;
    this.netInfo = NET_INFO;
    this.sysStat = SYS_STAT;
    this.netStat = NET_STAT;
    this.sysStatFreq = SYS_STAT_FREQ;
    this.netStatFreq = NET_STAT_FREQ;
    this.initResource = INIT_RESOURCE;
    this.cmdResource = CMD_RESOURCE;
    this.logResource = LOG_RESOURCE;
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
  public Configuration fromYaml(final String path) {
    Yaml yaml = new Yaml(YamlConstructor.getInstance());

    FileReader file;
    try {
      file = new FileReader(path);
    } catch (Exception exc) {
      System.out.println(exc.getMessage());
      return new Configuration();
    }

    Configuration config = yaml.loadAs(file, Configuration.class);

    return this.fromConfig(config);
  }

  /**
   * Overwrites the current configuration with the one in the specified YAML file.
   * If something goes wrong, the default configuration is loaded.
   * @param configStream the configuration YAML file.
   * @return The current configuration.
   */
  public Configuration fromYaml(InputStream configStream) {
    Yaml yaml = new Yaml(YamlConstructor.getInstance());

    Configuration config = yaml.loadAs(configStream, Configuration.class);

    return this.fromConfig(config);
  }

  /**
   * Overwrites the current configuration with the one specified.
   * @param config the configuration to overwrite with.
   * @return The current configuration.
   */
  private Configuration fromConfig(final Configuration config) {
    if (config == null) {
      return this;
    }

    this.sysInfo = config.isSysInfo();
    this.netInfo = config.isNetInfo();
    this.sysStat = config.isSysStat();
    this.netStat = config.isNetStat();
    this.sysStatFreq = config.getSysStatFreq();
    this.netStatFreq = config.getNetStatFreq();
    this.logResource = config.getLogResource();
    this.cmdResource = config.getCmdResource();
    this.maxTime = config.getMaxTime();
    this.debug = config.isDebug();

    return this;
  }

}
