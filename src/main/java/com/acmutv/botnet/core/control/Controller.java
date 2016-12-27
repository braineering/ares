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

import com.acmutv.botnet.config.AppConfiguration;
import com.acmutv.botnet.core.control.command.BotCommand;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Interval;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * This class realizes a simple Command and Control.
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
   * The resource providing commands.
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
   * If null, the polling period specified in {@link AppConfiguration} is used.
   */
  private Interval polling = null;

  /**
   * The maximum number of connection errors tolerated.
   * If null, the number specified in {@link AppConfiguration} is used.
   */
  private Long reconnections = null;

  /**
   * The period between reconnections, as random number within this interval.
   * If null, the period specified in {@link AppConfiguration} is used.
   */
  private Interval reconnectionWait = null;

  /**
   * The proxy server to connect through.
   * If null, the proxy server specified in {@link AppConfiguration} is used.
   */
  private HttpProxy proxy = null;
}
