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

import com.acmutv.botnet.model.TargetProxy;
import com.acmutv.botnet.model.Target;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.quartz.CronExpression;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class realizes the configuration model for the whole application.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see Target
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
  public static String  LOG_FILE = "botlog.txt";
  public static String  CMD_FILE = "botcmd.txt";
  public static TargetProxy  PROXY = null;
  public static boolean DEBUG = false;
  public static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

  private boolean sysInfo;
  private boolean netInfo;
  private boolean sysStat;
  private boolean netStat;
  private long sysStatFreq;
  private long netStatFreq;
  private String logfile;
  private String cmdfile;
  private List<Target> targets;
  private TargetProxy proxy;
  private List<String> sleep;
  private boolean debug;
  private DateTimeFormatter dtf;

  private static BotConfiguration instance;

  /**
   * Retrieves the class singleton.
   * @return the singleton
   */
  public static BotConfiguration getInstance() {
    if (instance == null) {
      instance = new BotConfiguration();
    }
    return instance;
  }

  /**
   * Creates the default configuration.
   */
  public static BotConfiguration getDefault() {
    return new BotConfiguration();
  }

  /**
   * Creates the default configuration.
   */
  private BotConfiguration() {
    this.sysInfo = SYS_INFO;
    this.netInfo = NET_INFO;
    this.sysStat = SYS_STAT;
    this.netStat = NET_STAT;
    this.sysStatFreq = SYS_STAT_FREQ;
    this.netStatFreq = NET_STAT_FREQ;
    this.logfile = LOG_FILE;
    this.cmdfile = CMD_FILE;
    this.proxy = PROXY;
    this.debug = DEBUG;
    this.targets = new ArrayList<Target>();
    this.sleep = new ArrayList<String>();
    this.dtf = DTF;
  }

  /**
   * Loads the application configuration from a YAML file.
   * The default configuration is merged with the settings given by the configuration file.
   * If something goes wrong, the default configuration is loaded.
   * @param path The absolute path to the configuration file.
   * @return The configuration.
   */
  public BotConfiguration fromYaml(final String path) {
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

    if (config == null) {
      config = new BotConfiguration();
    }

    if (config.getTargets() == null) {
      config.setTargets(new ArrayList<Target>());
    }

    if (config.getSleep() == null) {
      config.setSleep(new ArrayList<String>());
    }

    return config;
  }

}
