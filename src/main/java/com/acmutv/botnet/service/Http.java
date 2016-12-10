/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 Giacomo Marciani, Michele Porretta
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.acmutv.botnet.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

/**
 * This class realizes HTTP contact utilities.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpURLConnection
 */
public class Http {

  /**
   * Returns the result of a HTTP GET without proxy.
   * @param url The web url to contact.
   * @return The string representation of the GET result.
   * @throws IOException when HTTP GET error.
   */
  public static String makeGET(final URL url) throws IOException {
    HttpURLConnection http = (HttpURLConnection) url.openConnection();
    http.setRequestMethod("GET");
    http.setRequestProperty("User-Agent", "BOTNETv1.0.0");
    int response = http.getResponseCode();
    String output = String.format("GET %s :: %d", url, response);
    return output;
  }

  /**
   * Returns the result of a HTTP GET with proxy.
   * @param url The web url to contact.
   * @param proxy The proxy server.
   * @return The string representation of the GET result.
   * @throws IOException when HTTP GET error.
   */
  public static String makeGETWithProxy(final URL url, final Proxy proxy) throws IOException {
    HttpURLConnection http = (HttpURLConnection) url.openConnection(proxy);
    http.setRequestMethod("GET");
    http.setRequestProperty("User-Agent", "BOTNETv1.0.0");
    int response = http.getResponseCode();
    String output = String.format("GET %s :: %d", url, response);
    return output;
  }
}
