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

import com.acmutv.botnet.core.control.Controller;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.string.TemplateEngine;
import com.acmutv.botnet.tool.time.Duration;
import com.acmutv.botnet.tool.time.Interval;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.List;
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
  public static final boolean SYS_INFO = true;

  /**
   * Default value for property netInfo.
   */
  public static final boolean NET_INFO = true;

  /**
   * Default value for property sysStat.
   */
  public static final boolean SYS_STAT = true;

  /**
   * Default value for property netStat.
   */
  public static final boolean NET_STAT = true;

  /**
   * Default value for property sampling.
   */
  public static final Duration SAMPLING = new Duration(1, TimeUnit.MINUTES);

  /**
   * Default value for property controllers
   */
  public static final List<Controller> CONTROLLERS = new ArrayList<>();

  /**
   * The fallback default controller.
   * Only used when no controllers are specified.
   */
  public static final Controller FALLBACK_CONTROLLER = new Controller(
      TemplateEngine.getInstance().replace("${PWD}/cc/botinit.json"),
      TemplateEngine.getInstance().replace("${PWD}/cc/botcmd.json"),
      TemplateEngine.getInstance().replace("${PWD}/cc/botlog.json")
  );

  /**
   * Default value for property polling.
   */
  public static final Interval POLLING = new Interval(10, 10, TimeUnit.SECONDS);

  /**
   * Default value for property reconnections.
   */
  public static final Long RECONNECTIONS = 3L;

  /**
   * Default value for property reconnectionWait.
   */
  public static final Interval RECONNECTION_WAIT = new Interval(10,10, TimeUnit.SECONDS);

  /**
   * Default value for property proxy.
   */
  public static final HttpProxy PROXY = HttpProxy.NONE;

  /**
   * Property sysInfo.
   * If true, the bot sends system information to the CONTROLLERS.
   * Default is: true.
   */
  private boolean sysInfo = SYS_INFO;

  /**
   * Property netInfo.
   * If true, the bot sends network information to the CONTROLLERS.
   * Default is: true.
   */
  private boolean netInfo = NET_INFO;

  /**
   * Property sysStat.
   * If true, the bot sends system statistics to the CONTROLLERS.
   * Default is: true.
   */
  private boolean sysStat = SYS_STAT;

  /**
   * Property netStat.
   * If true, the bot sends network statistics to the CONTROLLERS.
   * Default is: true.
   */
  private boolean netStat = NET_STAT;

  /**
   * Property sampling.
   * The bot collects system/network statistics with the specified sampling rate.
   * Default is: 60 seconds.
   */
  private Duration sampling = SAMPLING;

  /**
   * Property controllers.
   * The list of controllers to contact.
   */
  private List<Controller> controllers = CONTROLLERS;

  /**
   * Property polling.
   * The bot polls controllers for commands with a random period within this interval.
   */
  private Interval polling = POLLING;

  /**
   * Property reconnections.
   * The maximum number of connection errors tolerated when connecting to a CC.
   */
  private Long reconnections = RECONNECTIONS;

  /**
   * Property reconnectionWait.
   * The bot tries to reconnect to a controller waiting a random period within this interval.
   */
  private Interval reconnectionWait = RECONNECTION_WAIT;

  /**
   * Property proxy.
   * The proxy server to contact the controllers through.
   */
  private HttpProxy proxy = PROXY;

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
    this.controllers = other.controllers;
    this.polling = other.polling;
    this.reconnections = other.reconnections;
    this.reconnectionWait = other.reconnectionWait;
    this.proxy = other.proxy;
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
    this.controllers = CONTROLLERS;
    this.polling = POLLING;
    this.reconnections = RECONNECTIONS;
    this.reconnectionWait = RECONNECTION_WAIT;
    this.proxy = PROXY;
  }

}
