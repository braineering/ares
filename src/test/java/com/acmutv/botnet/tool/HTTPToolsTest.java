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

package com.acmutv.botnet.tool;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

/**
 * This class realizes JUnit tests on HTTP utilities.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0
 * @see HTTPTools
 */
public class HTTPToolsTest {

  @Before
  public void setup() {
    org.junit.Assume.assumeTrue(SystemTools.checkConnection());
  }

  /**
   * Tests the HTTP GET without Proxy.
   * @throws ParseException when invalid weburl.
   * @throws IOException when HTTP GET error.
   */
  @Test
  public void testGET() throws ParseException, IOException {
    URL url = new URL("http://www.google.com");
    String result = HTTPTools.makeGET(url);
    String expected = "GET http://www.google.com :: 200";
    assertEquals(expected, result);
  }

  /**
   * Tests the HTTP GET with Proxy.
   * @throws ParseException when invalid weburl or Proxy.
   * @throws IOException when HTTP GET error.
   */
  @Test
  public void testGETWithProxy() throws ParseException, IOException {
    URL url = new URL("http://www.google.com");
    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("104.28.5.228", 80));
    String result = HTTPTools.makeGETWithProxy(url, proxy);
    String expected = "GET http://www.google.com :: 400";
    assertEquals(expected, result);
  }
}
