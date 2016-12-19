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

import com.acmutv.botnet.tool.time.Frequency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.Yaml;

import java.util.concurrent.TimeUnit;

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

  /**
   * Default value for property sysInfo.
   */
  public static boolean SYS_INFO = true;

  /**
   * Default value for property netInfo.
   */
  public static boolean NET_INFO = true;

  /**
   * Default value for property sysStat.
   */
  public static boolean SYS_STAT = true;

  /**
   * Default value for property netStat.
   */
  public static boolean NET_STAT = true;

  /**
   * Default value for property sampling.
   */
  public static Frequency SAMPLING = new Frequency(60, TimeUnit.SECONDS);

  /**
   * Default value for property initResource.
   */
  public static String INIT_RESOURCE = AppConfiguration.class.getResource("/cc/botinit.json").getPath();

  /**
   * Default value for property cmdResource.
   */
  public static String CMD_RESOURCE = AppConfiguration.class.getResource("/cc/botcmd.json").getPath();

  /**
   * Default value for property logResource.
   */
  public static String LOG_RESOURCE = AppConfiguration.class.getResource("/cc/botlog.json").getPath();

  /**
   * Property sysInfo.
   * If true, the bot sends system information to the CC.
   * Default is: true.
   */
  private boolean sysInfo = SYS_INFO;

  /**
   * Property netInfo.
   * If true, the bot sends network information to the CC.
   * Default is: true.
   */
  private boolean netInfo = NET_INFO;

  /**
   * Property sysStat.
   * If true, the bot sends system statistics to the CC.
   * Default is: true.
   */
  private boolean sysStat = SYS_STAT;

  /**
   * Property netStat.
   * If true, the bot sends network statistics to the CC.
   * Default is: true.
   */
  private boolean netStat = NET_STAT;

  /**
   * Property sampling.
   * The bot collects system/network statistics with the specified sampling rate.
   * Default is: 60 seconds.
   */
  private Frequency sampling = SAMPLING;

  /**
   * Property initResource.
   * The bot uses this resource to join the botnet.
   * Default is: ${PROJECT_RESOURCES}cc/botinit.json.
   */
  private String initResource = INIT_RESOURCE;

  /**
   * Property cmdResource.
   * The bot uses this resource to get commands to execute.
   * Default is: ${PROJECT_RESOURCES}cc/botcmd.json.
   */
  private String cmdResource = CMD_RESOURCE;

  /**
   * Property logResource.
   * The bot uses this resource to submit reports.
   * Default is: ${PROJECT_RESOURCES}cc/botlog.json.
   */
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
    this.sampling = other.sampling;
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
    this.sampling = SAMPLING;
    this.initResource = INIT_RESOURCE;
    this.cmdResource = CMD_RESOURCE;
    this.logResource = LOG_RESOURCE;
  }

}
