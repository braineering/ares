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

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * This class realizes JUnit tests for {@link HttpManager}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see HttpManager
 */
public class HttpManagerTest {

  @Before
  public void setup() {
    org.junit.Assume.assumeTrue(ConnectionManager.checkConnection());
  }

  /**
   * Tests the HTTP GET request (method:GET properties:none proxy:none)
   * @throws ParseException when invalid weburl.
   * @throws IOException when HTTP error.
   */
  @Test
  public void test_get() throws ParseException, IOException {
    final URL url = new URL("http://www.google.com");
    final int expected = 200;
    final int actual = HttpManager.makeRequest(HttpMethod.GET, url);
    assertEquals(expected, actual);
  }

  /**
   * Tests the HTTP GET request (method:GET properties:(User-Agent) proxy:none)
   * @throws ParseException when invalid weburl.
   * @throws IOException when HTTP error.
   */
  @Test
  public void test_get_props() throws ParseException, IOException {
    final URL url = new URL("http://www.google.com");
    final Map<String,String> props = new HashMap<>();
    props.put("User-Agent", "BOTNETv1.0.0");
    final int expected = 200;
    final int actual = HttpManager.makeRequest(HttpMethod.GET, url, props);
    assertEquals(expected, actual);
  }

  /**
   * Tests the HTTP GET request (method:GET properties:(User-Agent) proxy:provided)
   * See {@literal https://www.us-proxy.org/} for a list of active public proxies.
   * @throws ParseException when invalid weburl.
   * @throws IOException when HTTP error.
   */
  @Test
  public void test_get_propsAndProxy() throws ParseException, IOException {
    final URL url = new URL("http://www.google.com");
    final Map<String,String> props = new HashMap<>();
    props.put("User-Agent", "BOTNETv1.0.0");
    final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("31.220.56.101", 80));
    final int expected = 200;
    final int actual = HttpManager.makeRequest(HttpMethod.GET, url, props, proxy);
    assertEquals(expected, actual);
  }

}