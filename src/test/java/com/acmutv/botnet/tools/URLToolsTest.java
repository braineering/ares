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

package com.acmutv.botnet.tools;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class realizes unit tests on URL encoding tools.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0
 * @see URLTools
 */
public class URLToolsTest {

  /**
   * Tests the URL string validity check.
   */
  @Test
  public void testIsURL() {
    assertTrue(URLTools.isURL("http://www.sub.google.com"));
    assertTrue(URLTools.isURL("http://sub.google.com"));
    assertTrue(URLTools.isURL("http://www.google.com"));
    assertTrue(URLTools.isURL("http://google.com"));

    assertTrue(URLTools.isURL("https://www.sub.google.com"));
    assertTrue(URLTools.isURL("https://sub.google.com"));
    assertTrue(URLTools.isURL("https://www.google.com"));
    assertTrue(URLTools.isURL("https://google.com"));

    assertFalse(URLTools.isURL(""));
    assertFalse(URLTools.isURL("http://"));
    assertFalse(URLTools.isURL("https://"));
    assertFalse(URLTools.isURL("http://www"));
    assertFalse(URLTools.isURL("http://google"));
    assertFalse(URLTools.isURL("http://.com"));
    assertFalse(URLTools.isURL("www.google"));
    assertFalse(URLTools.isURL("google"));
    assertFalse(URLTools.isURL(".com"));
  }

  /**
   * Tests the IPv4 address string validity check.
   */
  @Test
  public void testIsIP4() {
    assertTrue(URLTools.isIP4("192.168.0.1"));

    assertFalse(URLTools.isIP4(""));
    assertFalse(URLTools.isIP4("255"));
    assertFalse(URLTools.isIP4("255.255"));
    assertFalse(URLTools.isIP4("255.255.255"));
    assertFalse(URLTools.isIP4("255.255.255.255.255"));
  }

  /**
   * Tests the web URL string check.
   * @throws  MalformedURLException when error in test.
   */
  @Test
  public void testGetURLFromString_url() throws MalformedURLException {
    URL url = URLTools.getURLFromString("http://www.google.com");
    URL expected = new URL("http://www.google.com");
    assertEquals(expected, url);
  }

  /**
   * Tests the IPv4 address string check.
   */
  @Test
  public void testGetURLFromString_ip4() {
    URL url = URLTools.getURLFromString("104.171.115.66");
    String expected = "http://104.171.115.66/";
    assertEquals(expected, url.toString());
  }
}
