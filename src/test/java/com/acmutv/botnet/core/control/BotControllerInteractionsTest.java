/*
  The MIT License (MIT)

  Copyright (c) 2017 Giacomo Marciani and Michele Porretta

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

package com.acmutv.botnet.core.control;

import com.acmutv.botnet.core.BotControllerInteractions;
import com.acmutv.botnet.core.control.command.BotCommand;
import com.acmutv.botnet.core.control.command.CommandScope;
import com.acmutv.botnet.core.report.Report;
import com.acmutv.botnet.core.report.SimpleReport;
import com.acmutv.botnet.core.report.serial.SimpleReportJsonMapper;
import com.acmutv.botnet.tool.io.IOManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * JUnit tests for {@link BotControllerInteractions}. *
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotControllerInteractions
 */
public class BotControllerInteractionsTest {

  private static final Logger LOGGER = LogManager.getLogger(BotControllerInteractionsTest.class);

  @Test
  public void test_getInitialization_local() throws IOException {
    String initResource = "src/test/resources/control/controllers/1/init.json";
    String cmdResource = "src/test/resources/control/controllers/1/command.json";
    String reportResource = "src/test/resources/control/controllers/1/report.json";
    Controller controller = new Controller(initResource, cmdResource, reportResource);
    ControllerProperties actual = BotControllerInteractions.getInitialization(controller);
    ControllerProperties expected = new ControllerProperties();
    expected.put("auth.user-agent", "BotAgent1");
    expected.put("life.sleep", null);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void test_getInitialization_remote() {
    Controller controller = new Controller(
        "http://localhost:3000/init",
        "http://localhost:3000/command",
        "http://localhost:3000/report");
    ControllerProperties actual = null;
    try {
      actual = BotControllerInteractions.getInitialization(controller);
    } catch (IOException exc) {
      LOGGER.error(exc.getMessage());
    }
    LOGGER.info(actual);
  }

  @Test
  public void test_getCommand_local() throws IOException {
    String initResource = "src/test/resources/control/controllers/1/init.json";
    String cmdResource = "src/test/resources/control/controllers/1/command.json";
    String reportResource = "src/test/resources/control/controllers/1/report.json";
    Controller controller = new Controller(initResource, cmdResource, reportResource);
    BotCommand actual = BotControllerInteractions.getCommand(controller);
    BotCommand expected = new BotCommand(0, CommandScope.NONE);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void test_getCommand_remote() {
    Controller controller = new Controller(
        "http://localhost:3000/init",
        "http://localhost:3000/command",
        "http://localhost:3000/report");
    CloseableHttpClient client = HttpClients.createDefault();
    BotCommand actual = null;
    try {
      actual = BotControllerInteractions.getCommand(controller, client);
      client.close();
    } catch (IOException exc) {
      LOGGER.error(exc.getMessage());
    }
    LOGGER.info(actual);

  }

  @Test
  public void test_submitReport_local() throws IOException {
    String initResource = "src/test/resources/control/controllers/1/init.json";
    String cmdResource = "src/test/resources/control/controllers/1/command.json";
    String reportResource = "src/test/resources/control/controllers/1/report.json";
    Controller controller = new Controller(initResource, cmdResource, reportResource);
    Report expected = new SimpleReport();
    expected.put("foo", "bar");
    BotControllerInteractions.submitReport(controller, expected);
    Report actual;
    try (InputStream in = IOManager.getInputStream(controller.getLogResource())) {
      actual = new SimpleReportJsonMapper().readValue(in, SimpleReport.class);
    }
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void test_submitReport_remote() {
    Controller controller = new Controller(
        "http://localhost:3000/init",
        "http://localhost:3000/command",
        "http://localhost:3000/report");
    CloseableHttpClient client = HttpClients.createDefault();
    Report expected = new SimpleReport();
    expected.put("foo", "bar");
    try {
      BotControllerInteractions.submitReport(controller, expected, client);
      client.close();
    } catch (IOException exc) {
      LOGGER.error(exc.getMessage());
    }
  }
}
