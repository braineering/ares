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

package com.acmutv.botnet.tool.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;

/**
 * This class realizes HTTP contact utilities.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpURLConnection
 *
 */
public class HttpManager {

  private static final Logger LOGGER = LogManager.getLogger(HttpManager.class);

  /**
   * Executes a HTTP request.
   * @param method the HTTP method.
   * @param url the web url to contact.
   * @return the response code.
   * @throws IOException when HTTP error.
   */
  public static int makeRequest(final HttpMethod method, final URL url) throws IOException {
    LOGGER.traceEntry("url={}", url);
    HttpURLConnection http = (HttpURLConnection) url.openConnection();
    http.setRequestMethod(method.name());
    int response = http.getResponseCode();
    return LOGGER.traceExit(response);
  }

  /**
   * Executes a HTTP request.
   * @param method the HTTP method.
   * @param props the request properties.
   * @return the response code.
   * @throws IOException when HTTP error.
   */
  public static int makeRequest(final HttpMethod method, final URL url, Map<String,String> props) throws IOException {
    LOGGER.traceEntry("url={}", url);
    HttpURLConnection http = (HttpURLConnection) url.openConnection();
    http.setRequestMethod(method.name());
    props.forEach((k,v) -> http.setRequestProperty(k, v));
    int response = http.getResponseCode();
    return LOGGER.traceExit(response);
  }

  /**
   * Executes a HTTP request.
   * @param method the HTTP method.
   * @param props the request properties.
   * @param proxy The proxy server.
   * @return the response code.
   * @throws IOException when HTTP error.
   */
  public static int makeRequest(final HttpMethod method, final URL url, Map<String,String> props, final Proxy proxy) throws IOException {
    LOGGER.traceEntry("url={} proxy={}", url, proxy);
    HttpURLConnection http = (HttpURLConnection) url.openConnection(proxy);
    http.setRequestMethod(method.name());
    props.forEach((k,v) -> http.setRequestProperty(k, v));
    int response = http.getResponseCode();
    return LOGGER.traceExit(response);
  }
}
