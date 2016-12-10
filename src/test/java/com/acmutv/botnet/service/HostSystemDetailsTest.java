/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 Giacomo Marciani
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

import com.acmutv.botnet.service.HostSystemDetails;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * This class realizes JUnit tests for {@link HostSystemDetails}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
public class HostSystemDetailsTest {

  @Before
  public void setup() {
    org.junit.Assume.assumeTrue(HostSystemDetails.checkConnection());
  }

  /**
   * Tests IP address retrieval.
   */
  @Test
  public void test_getIP() {
    String actual = HostSystemDetails.getIP();
    System.out.println("IP: " + actual);
  }

  /**
   * Tests MAC address retrieval.
   */
  @Test
  public void test_getMAC() {
    String actual = HostSystemDetails.getMAC();
    System.out.println("MAC: " + actual);
  }

  /**
   * Tests the connection availability check, by sending an HTTP request.
   * @throws IOException when HTTP GET error.
   */
  @Test
  public void test_checkConnection() throws IOException {
    boolean check = HostSystemDetails.checkConnection();
    System.out.println("Connection: " + check);
  }

}
