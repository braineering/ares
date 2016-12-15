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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.Yaml;

/**
 * This class realizes the app configuration model.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see Yaml
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppConfiguration {

  public static boolean SYS_INFO = true;
  public static boolean NET_INFO = true;
  public static boolean SYS_STAT = true;
  public static boolean NET_STAT = true;
  public static long SYS_STAT_FREQ = 60;
  public static long NET_STAT_FREQ = 60;
  public static String INIT_RESOURCE = "~/com/acmutv/botnet/botinit.txt";
  public static String CMD_RESOURCE = "~/com/acmutv/botnet/botcmd.txt";
  public static String LOG_RESOURCE = "~/com/acmutv/botnet/botlog.txt";

  private boolean sysInfo = SYS_INFO;
  private boolean netInfo = NET_INFO;
  private boolean sysStat = SYS_STAT;
  private boolean netStat = NET_STAT;
  private long sysStatFreq = SYS_STAT_FREQ;
  private long netStatFreq = NET_STAT_FREQ;
  private String initResource = INIT_RESOURCE;
  private String cmdResource = CMD_RESOURCE;
  private String logResource = LOG_RESOURCE;

  /**
   * Constructs a configuration as a copy of the one specified.
   * @param other the configuration to copy.
   */
  public AppConfiguration(AppConfiguration other) {
    this.copy(other);
  }

  /**
   * Copies the settings of the configuration specified.
   * @param other the configuration to copy.
   */
  public void copy(AppConfiguration other) {
    this.sysInfo = other.sysInfo;
    this.netInfo = other.netInfo;
    this.sysStat = other.sysStat;
    this.netStat = other.netStat;
    this.sysStatFreq = other.sysStatFreq;
    this.netStatFreq = other.netStatFreq;
    this.initResource = other.initResource;
    this.cmdResource = other.cmdResource;
    this.logResource = other.logResource;
  }

  /**
   * Restores the default configuration settings.
   */
  public void toDefault() {
    this.sysInfo = SYS_INFO;
    this.netInfo = NET_INFO;
    this.sysStat = SYS_STAT;
    this.netStat = NET_STAT;
    this.sysStatFreq = SYS_STAT_FREQ;
    this.netStatFreq = NET_STAT_FREQ;
    this.initResource = INIT_RESOURCE;
    this.cmdResource = CMD_RESOURCE;
    this.logResource = LOG_RESOURCE;
  }

}
