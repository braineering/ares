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

package com.acmutv.botnet;

import com.acmutv.botnet.tool.net.HttpManager;
import com.acmutv.botnet.tool.net.HttpMethod;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class realizes local tests (for personal use only).
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
public class Misc {

  @Test
  public void test1() throws ParseException, IOException {
    URL url = new URL("http://localhost:3600/init");
    InputStream in = HttpManager.getResponseBody(HttpMethod.GET, url, null, null, null);
    String actual = IOUtils.toString(in, Charset.defaultCharset());
    System.out.println(actual);
  }

  @Test
  public void test2() throws ParseException, IOException {
    URL url = new URL("http://localhost:3600/command");
    InputStream in = HttpManager.getResponseBody(HttpMethod.GET, url, null, null, null);
    String actual = IOUtils.toString(in, Charset.defaultCharset());
    System.out.println(actual);
  }

  @Test
  public void test3() throws ParseException, IOException {
    //TODO
    URL url = new URL("http://localhost:3600/report");
    Map<String,String> header = new HashMap<>();
    header.put("content-type", "application/json");
    Map<String,String> params = new HashMap<>();
    header.put("report", "{\"foo\":\"bar\"}");
    InputStream in = HttpManager.getResponseBody(HttpMethod.POST, url, null, null, null);
    String actual = IOUtils.toString(in, Charset.defaultCharset());
    System.out.println(actual);
  }
}
