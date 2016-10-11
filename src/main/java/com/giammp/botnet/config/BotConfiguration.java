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

package com.giammp.botnet.config;

import com.giammp.botnet.model.TargetProxy;
import com.giammp.botnet.model.Target;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.quartz.CronExpression;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class realizes the configuration model for the whole application.
 *
 * @author Giacomo Marciani <gmarciani@ieee.org>
 * @author Michele Porretta <mporretta@acm.org>
 * @since 1.0.0
 * @see Target
 * @see CronExpression
 * @see Yaml
 */
@Data
@AllArgsConstructor
public class BotConfiguration {
  private boolean sysInfo;
  private boolean netInfo;
  private boolean sysStat;
  private boolean netStat;
  private long sysStatTime;
  private long netStatTime;
  private String logfile;
  private String cmdfile;
  private List<Target> targets;
  private TargetProxy proxy;
  private List<String> sleep;
  private boolean debug;

  /**
   * Creates the  default configuration.
   */
  public BotConfiguration() {
    this.sysInfo = true;
    this.netInfo = true;
    this.sysStat = true;
    this.netStat = true;
    this.sysStatTime = 60;
    this.netStatTime = 60;
    this.logfile = "./botlog.txt";
    this.cmdfile = "./botcmd.txt";
    this.targets = new ArrayList<Target>();
    this.proxy = null;
    this.sleep = new ArrayList<String>();
    this.debug = false;
  }

  /**
   * Loads the application configuration from a YAML file.
   * The default configuration is merged with the settings given by the configuration file.
   * If something goes wrong, the default configuration is loaded.
   *
   * @param path The absolute path to the configuration file.
   * @return The configuration.
   */
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
