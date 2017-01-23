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

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.stream.Collectors;

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

  public static final String HTTP_URL_REGEXP = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

  /**
   * Checks if {@code str} is a valid HTTP URL.
   * @param str the string to check.
   * @return true if {@code str} is a valid HTTP URL; false, otherwise.
   */
  public static boolean isHttpUrl(String str) {
    return str.matches(HTTP_URL_REGEXP);
  }

  /**
   * Executes a HTTP request and return the response body.
   * @param method the HTTP method.
   * @param resource the web url to contact.
   * @return the response body.
   * @throws IOException when HTTP error.
   */
  public static InputStream getResponseBody(final HttpMethod method, final URL resource) throws IOException {
    return getResponseBody(method, resource, null, null, null);
  }

  /**
   * Executes a HTTP request and return the response body.
   * @param method the HTTP method.
   * @param resource the web url to contact.
   * @param proxy the proxy server.
   * @return the response body.
   * @throws IOException when HTTP error.
   */
  public static InputStream getResponseBody(final HttpMethod method, final URL resource, HttpProxy proxy) throws IOException {
    return getResponseBody(method, resource, proxy, null, null);
  }

  /**
   * Executes a HTTP request.
   * @param method the HTTP method.
   * @param resource the web url to contact.
   * @param proxy the proxy server.
   * @param header the request header.
   * @param params the request parameters.
   * @return the response input stream.
   * @throws IOException when HTTP error.
   */
  public static InputStream getResponseBody(final HttpMethod method, final URL resource,
                                            HttpProxy proxy,
                                            Map<String,String> header, Map<String,String> params) throws IOException {
    HttpURLConnection http = request(method, resource, proxy, header, params);
    return http.getInputStream();
  }

  /**
   * Executes a HTTP request.
   * @param method the HTTP method.
   * @param resource the web url to contact.
   * @return the response code.
   * @throws IOException when HTTP error.
   */
  public static int makeRequest(final HttpMethod method, final URL resource) throws IOException {
    return makeRequest(method, resource, null, null, null);
  }

  /**
   * Executes a HTTP request.
   * @param method the HTTP method.
   * @param resource the web url to contact.
   * @param proxy the proxy server.
   * @return the response code.
   * @throws IOException when HTTP error.
   */
  public static int makeRequest(HttpMethod method, URL resource, HttpProxy proxy) throws IOException {
    return makeRequest(method, resource, proxy, null, null);
  }

  /**
   * Executes a HTTP request.
   * @param method the HTTP method.
   * @param resource the web url to contact.
   * @param proxy the proxy server.
   * @param header the request header.
   * @param params the request parameters.
   * @return the response code.
   * @throws IOException when HTTP error.
   */
  public static int makeRequest(HttpMethod method, URL resource, HttpProxy proxy,
                                Map<String,String> header, Map<String,String> params) throws IOException {
    HttpURLConnection http = request(method, resource, proxy, header, params);
    return http.getResponseCode();
  }

  /**
   * Executes a HTTP request.
   * @param method the HTTP method.
   * @param resource the web url to contact.
   * @param proxy the proxy server.
   * @param header the request header.
   * @param params the request parameters.
   * @return the response code.
   * @throws IOException when HTTP error.
   */
  private static HttpURLConnection request(final HttpMethod method, final URL resource,
                                           HttpProxy proxy,
                                           Map<String,String> header, Map<String,String> params) throws IOException {
    LOGGER.traceEntry("resource={} proxy={} header={} params={} data={}", resource, proxy, header, params);

    String formattedParams = (params != null) ?
        params.entrySet().stream()
            .map(e -> e.getKey() + "=" + e.getValue())
            .collect(Collectors.joining("&"))
        :
        "";

    URL url = (formattedParams.length() > 0) ?
        new URL(resource.toString() + "?" + formattedParams)
        :
        resource;

    Proxy jproxy = (proxy == null || proxy.equals(HttpProxy.NONE)) ?
        Proxy.NO_PROXY
        :
        proxy;

    HttpURLConnection http = (HttpURLConnection) url.openConnection(jproxy);

    http.setRequestMethod(method.name());

    if (header != null) {
      header.forEach(http::setRequestProperty);
    }

    if (method.equals(HttpMethod.POST) && formattedParams.length() > 0) {
      byte data[] = formattedParams.getBytes(Charset.defaultCharset());
      int datalen = data.length;
      http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      http.setRequestProperty("charset", "utf-8");
      http.setRequestProperty("Content-Length", Integer.toString(datalen));
      http.setDoOutput(true);
      http.setUseCaches(false);
      try (DataOutputStream out = new DataOutputStream(http.getOutputStream())) {
        out.write(data);
      }
    }

    return http;
  }
}
