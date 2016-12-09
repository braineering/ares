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

import java.net.*;
import java.util.regex.Pattern;

/**
 * This class realizes URL/InetAddress ecnoding utilities.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see java.net.URL
 * @see java.net.InetAddress
 */
public class URLTools {

  public static final String REG_URL =
      "^(http:\\/\\/|https:\\/\\/)?(www.)?" +
          "([a-zA-Z0-9]+).[a-zA-Z0-9]*.[\u200C\u200Ba-z]{3}\\.([a-z]+)?$";

  private static final String REG_IP4 =
      "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
          "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
          "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
          "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

  private static final String REG_IP6 =
      "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
          "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
          "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
          "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

  /**
   * Returns the URL represented by the given string. The string could be a website address or an
   * IPv4 address (protocol http is considered).
   * @param str the string to parse.
   * @return the parsed URL.
   */
  public static URL getURLFromString(final String str) {
    if (isURL(str)) {
      try {
        return new URL(str);
      } catch (MalformedURLException e) {
        e.printStackTrace();
        return null;
      }
    } else if (isIP4(str)) {
      try {
        return new URL("http", str, -1, "/");
      } catch (MalformedURLException e) {
        e.printStackTrace();
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * Checks if the given string is a valid web URL.
   * @param str the string to check.
   * @return true, if the string is a valid web URL; false, otherwise.
   */
  public static boolean isURL(final String str) {
    return Pattern.matches(REG_URL, str);
  }

  /**
   * Checks if the given string is a valid IPv4 address.
   * @param str the string to check.
   * @return true, if the string is a valid IPv4 address; false, otherwise.
   */
  public static boolean isIP4(String str) {
    return Pattern.matches(REG_IP4, str);
  }

}
