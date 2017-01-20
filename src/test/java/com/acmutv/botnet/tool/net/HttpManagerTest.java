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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * JUnit tests for {@link HttpManager}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpManager
 */
public class HttpManagerTest {

  private static final Logger LOGGER = LogManager.getLogger(HttpManagerTest.class);

  @Before
  public void setup() {
    org.junit.Assume.assumeTrue(ConnectionManager.checkConnection());
  }

  /**
   * Tests the HTTP url check.
   */
  @Test
  public void test_isHttpUrl() {
    Assert.assertTrue(HttpManager.isHttpUrl("http://www.google.com"));
    Assert.assertTrue(HttpManager.isHttpUrl("http://google.com"));
    Assert.assertTrue(HttpManager.isHttpUrl("https://www.google.com"));
    Assert.assertTrue(HttpManager.isHttpUrl("https://google.com"));

    Assert.assertFalse(HttpManager.isHttpUrl("www.google.com"));
    Assert.assertFalse(HttpManager.isHttpUrl("www/google/com"));
  }

  /**
   * Tests the HTTP GET request.
   * Proxy: none | header: none | params: none
   * @throws ParseException when invalid weburl.
   * @throws IOException when HTTP error.
   */
  @Test
  public void test_makeRequest_get_simple() throws ParseException, IOException {
    URL url = new URL("http://www.google.com");
    int actual1 = HttpManager.makeRequest(HttpMethod.GET, url, null, null, null);
    Assert.assertTrue(actual1 == 200 || actual1 == 403);
  }

  /**
   * Tests the HTTP GET request.
   * Proxy: provided | header: provided | params: provided
   * @throws ParseException when invalid weburl.
   * @throws IOException when HTTP error.
   */
  @Test
  public void test_makeRequest_get_complex() throws ParseException, IOException {
    URL url = new URL("http://www.google.com");
    HttpProxy proxy = new HttpProxy("31.220.56.101", 80);
    Map<String,String> header = new HashMap<String,String>(){{
      put("content-type", "multipart/form-data");
    }};
    Map<String,String> params = new HashMap<String,String>(){{
      put("Content-Type", "text%2Fhtml");
      put("test", "response_headers");
    }};
    int actual = HttpManager.makeRequest(HttpMethod.GET, url, proxy, header, params);
    Assert.assertTrue(actual == 200 || actual == 403);
  }

  /**
   * Tests the HTTP GET request.
   * Proxy: none | header: none | params: none
   * @throws ParseException when invalid weburl.
   * @throws IOException when HTTP error.
   */
  @Test
  public void test_getResponseBody_get_simple() throws ParseException, IOException {
    URL url = new URL("http://www.google.com");
    InputStream in = HttpManager.getResponseBody(HttpMethod.GET, url, null, null, null);
    String actual = IOUtils.toString(in, Charset.defaultCharset());
    LOGGER.info(actual);
  }

  /**
   * Tests the HTTP GET request.
   * Proxy: provided | header: provided | params: provided
   * @throws ParseException when invalid weburl.
   * @throws IOException when HTTP error.
   */
  @Test
  public void test_getResponseBody_get_complex() throws ParseException, IOException {
    URL url = new URL("http://www.google.com");
    HttpProxy proxy = new HttpProxy("31.220.56.101", 80);
    Map<String,String> header = new HashMap<String,String>(){{
      put("content-type", "multipart/form-data");
    }};
    Map<String,String> params = new HashMap<String,String>(){{
      put("Content-Type", "text%2Fhtml");
      put("test", "response_headers");
    }};
    String actual = null;
    try (InputStream in = HttpManager.getResponseBody(HttpMethod.GET, url, proxy, header, params);) {
      actual = IOUtils.toString(in, Charset.defaultCharset());
    } catch (IOException exc) {LOGGER.warn(exc.getMessage());}

    LOGGER.info(actual);
  }

}
