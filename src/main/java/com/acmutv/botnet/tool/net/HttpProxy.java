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

import org.apache.http.HttpHost;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * This class realizes a HTTP proxy.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see Proxy
 */
public class HttpProxy extends Proxy {

  public static final HttpProxy NONE = new HttpProxy("", 0);

  public HttpProxy(String address, int port) {
    super(Type.HTTP, new InetSocketAddress(address, port));
  }

  private HttpProxy(Type type, InetSocketAddress sock) {
    super(type, sock);
  }

  /**
   * Returns proxy's IP address.
   * @return the IP address.
   */
  public String getIP() {
    InetSocketAddress sock = (InetSocketAddress) super.address();
    return sock.getHostString();
  }

  /**
   * Returns proxy's port number.
   * @return the port number.
   */
  public int getPort() {
    InetSocketAddress sock = (InetSocketAddress) super.address();
    return sock.getPort();
  }

  /**
   * Returns the proxy as a host.
   * @return the proxy as a host.
   */
  public HttpHost toHttpHost() {
    if (this.equals(HttpProxy.NONE)) {
      return null;
    } else {
      return new HttpHost(this.getIP(), this.getPort(), "http");
    }
  }

  /**
   * Parses {@link HttpProxy} from string.
   * @param string the string to parse.
   * @return the parsed {@link HttpProxy}; null if cannot be parsed.
   */
  public static HttpProxy valueOf(String string) {
    if (string == null) return null;
    if (string.equalsIgnoreCase("none")) return HttpProxy.NONE;
    String parts[] = string.split(":",2);
    if (parts.length != 2) return null;
    String ipaddr = parts[0];
    int port = Integer.valueOf(parts[1]);
    return new HttpProxy(ipaddr, port);
  }

  /**
   * Returns proxy's compact string representation [IP]:[PORT]
   * @return the compact string representation.
   */
  public String toCompactString() {
    //noinspection ConstantConditions
    if (this.equals(HttpProxy.NONE)) {
      return "none";
    } else {
      return String.format("%s:%d", this.getIP(), this.getPort());
    }
  }
}
