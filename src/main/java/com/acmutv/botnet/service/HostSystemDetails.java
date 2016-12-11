/*
  The MIT License (MIT)
  <p>
  Copyright (c) 2016 Giacomo Marciani
  <p>
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  <p>
  <p>
  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.
  <p>
  <p>
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
 */

package com.acmutv.botnet.service;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

/**
 * This class realizes utilities for connection management.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0
 * @see HttpURLConnection
 */
public class HostSystemDetails {

  /**
   * Retrieves the local host MAC address.
   * @return the string representation of MAC address.
   */
  public static String getMAC() {
    try {
      InetAddress ip = InetAddress.getLocalHost();
      Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
      while (networks.hasMoreElements()) {
        NetworkInterface network = networks.nextElement();
        byte[] mac = network.getHardwareAddress();
        if(mac != null) {
          StringBuilder sb = new StringBuilder();
          for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
          }
          return sb.toString();
        }
      }
    } catch (UnknownHostException|SocketException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Retrieves the local IP address.
   * @return the string representation of the local IP address; null, if error.
   */
  public static String getIP() {
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Checks if there is an available connection.
   * @return true, if the connection is available; false, otherwise.
   */
  public static boolean checkConnection() {
    boolean check;
    Socket socket = new Socket();
    try {
      socket.connect(new InetSocketAddress("www.google.com", 80), 10000);
      check = true;
    } catch (IOException exc) {
      check = false;
    } finally {
      try {
        socket.close();
      } catch (IOException exc) {
        exc.printStackTrace();
      }
    }
    return check;
  }

  /**
   * Retrieves the local number of cores.
   * @return the number of cores.
   */
  public static int getCores() {
    return Runtime.getRuntime().availableProcessors();
  }
}
