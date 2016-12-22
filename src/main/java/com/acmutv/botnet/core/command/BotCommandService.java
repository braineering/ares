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

package com.acmutv.botnet.core.command;

import com.acmutv.botnet.core.command.json.BotCommandMapper;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URI;

/**
 * This class realizes bot command services.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotCommand
 */
public class BotCommandService {

  private static final Logger LOGGER = LogManager.getLogger(BotCommandService.class);

  /**
   * Read a {@link BotCommand} from a JSON file.
   * The JSON file is ovewritten with an empty JSON.
   * This function is used to simulate bot->CC communication.
   * @param resource the resource providing a JSON file.
   * @return the parsed command.
   * @throws IOException when command cannot be parsed.
   */
  public static BotCommand popCommand(URI resource) throws IOException {
    LOGGER.traceEntry("resource={}", resource);
    BotCommand cmd;
    try (InputStream in = resource.toURL().openStream()) {
      cmd = readFromJson(in);
    }

    /*
    try (OutputStream out = new FileOutputStream(path)) {
      out.write("{}".getBytes());
    }
    return LOGGER.traceExit("cmd={}", cmd);
    */
    return LOGGER.traceExit("cmd={}", cmd);
  }

  /**
   * Read a {@link BotCommand} from a JSON file.
   * The JSON file is ovewritten with an empty JSON.
   * This function is used to simulate bot->CC communication.
   * @param path the path to JSON file.
   * @return the parsed command.
   * @throws IOException when command cannot be parsed.
   */
  public static BotCommand popCommand(String path) throws IOException {
    LOGGER.traceEntry("path={}", path);
    BotCommand cmd = readFromJson(path);
    try (OutputStream out = new FileOutputStream(path)) {
      out.write("{}".getBytes());
    }
    return LOGGER.traceExit("cmd={}", cmd);
  }

  /**
   * Parses a {@link BotCommand} from a JSON file.
   * @param path the path to JSON file.
   * @return the parsed BotCommand; BotCommand with scope NONE, in case of errors.
   * @throws IOException when command cannot be parsed.
   */
  public static BotCommand readFromJson(String path) throws IOException {
    LOGGER.traceEntry("path={}", path);
    BotCommand cmd;
    try (InputStream in = new FileInputStream(path)) {
      cmd = readFromJson(in);
    }
    return LOGGER.traceExit("cmd={}", cmd);
  }

  /**
   * Parses a {@link BotCommand} from a JSON file.
   * @param in the JSON file.
   * @return the parsed command.
   * @throws IOException when command cannot be parsed.
   */
  public static BotCommand readFromJson(InputStream in) throws IOException {
    LOGGER.traceEntry("in={}", in);
    BotCommandMapper mapper = new BotCommandMapper();
    BotCommand cmd = mapper.readValue(in, BotCommand.class);
    return LOGGER.traceExit(cmd);
  }

  /**
   * Read a {@link BotCommand} from a JSON file.
   * The JSON file is ovewritten with an empty JSON.
   * This function is used to simulate bot->CC communication.
   * @param in the JSON file.
   * @return the parsed BotCommand; BotCommand with scope NONE, in case of errors.
   * @throws IOException when command cannot be parsed.
   */
  public static BotCommand takeCommand(InputStream in) throws IOException {
    LOGGER.traceEntry("in={}", in);
    BotCommand cmd = readFromJson(in);
    OutputStream out = null;
    IOUtils.copy(in, out);
    out.close();
    return LOGGER.traceExit(cmd);
  }


  public static BotCommand readFromJsonResource(String cmdResource) throws IOException {
    LOGGER.traceEntry();
    BotCommand cmd = null;
    return LOGGER.traceExit(cmd);
  }
}
