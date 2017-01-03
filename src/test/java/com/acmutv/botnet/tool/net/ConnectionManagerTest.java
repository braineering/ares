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

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * JUnit tests for {@link ConnectionManager}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
public class ConnectionManagerTest {

  @Before
  public void setup() {
    Assume.assumeTrue(ConnectionManager.checkConnection());
  }

  /**
   * Tests IP address retrieval.
   */
  @Test
  public void test_getIP() throws UnknownHostException {
    String actual = ConnectionManager.getIP();
  }

  /**
   * Tests MAC address retrieval.
   */
  @Test
  public void test_getMAC() throws SocketException, UnknownHostException {
    String actual = ConnectionManager.getMAC();
    Assert.assertNotNull(actual);
  }

  /**
   * Tests the connection availability check, by sending an HTTP request.
   * @throws IOException when HTTP GET error.
   */
  @Test
  public void test_checkConnection() throws IOException {
    boolean check = ConnectionManager.checkConnection();
    Assert.assertTrue(check);
  }

}
