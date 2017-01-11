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

package com.acmutv.botnet.core.control.command;

import com.acmutv.botnet.core.control.command.serial.BotCommandJsonMapper;
import com.acmutv.botnet.tool.io.IOManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * The bot command services.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotCommand
 */
public class BotCommandService {

  private static final Logger LOGGER = LogManager.getLogger(BotCommandService.class);

  /**
   * Deserializes {@link BotCommand} from a resource providing a JSON.
   * If the resource is writable, it is then cleared with a JSON representing the `NONE` command.
   * @param resource the resource providing a JSON.
   * @return the consumed command.
   * @throws IOException if {@link BotCommand} cannot be consumed.
   */
  public static BotCommand consumeJsonResource(String resource) throws IOException {
    LOGGER.traceEntry("resource={}", resource);
    BotCommand cmd = fromJsonResource(resource);
    if (IOManager.isWritableResource(resource)) {
      BotCommandJsonMapper mapper = new BotCommandJsonMapper();
      String noneCommandJson = mapper.writeValueAsString(BotCommand.NONE);
      IOManager.writeResource(resource, noneCommandJson);
    }
    return LOGGER.traceExit(cmd);
  }

  /**
   * Deserializes {@link BotCommand} from JSON.
   * @param in the stream providing a JSON.
   * @return the parsed command.
   * @throws IOException if {@link BotCommand} cannot be deserialized.
   */
  public static BotCommand fromJson(InputStream in) throws IOException {
    BotCommandJsonMapper mapper = new BotCommandJsonMapper();
    return mapper.readValue(in, BotCommand.class);
  }

  /**
   * Deserializes {@link BotCommand} from a resource providing a JSON.
   * @param resource the resource providing a JSON.
   * @return the deserialized command.
   * @throws IOException if {@link BotCommand} cannot be deserialized.
   */
  public static BotCommand fromJsonResource(String resource) throws IOException {
    BotCommand cmd;
    try (final InputStream in = IOManager.getInputStream(resource)) {
      cmd = fromJson(in);
    }
    return cmd;
  }

}
