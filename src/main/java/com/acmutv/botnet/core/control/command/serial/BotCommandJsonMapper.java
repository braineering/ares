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

package com.acmutv.botnet.core.control.command.serial;

import com.acmutv.botnet.core.attack.flooding.HttpFloodAttack;
import com.acmutv.botnet.core.attack.flooding.serial.HttpFloodAttackDeserializer;
import com.acmutv.botnet.core.attack.flooding.serial.HttpFloodAttackSerializer;
import com.acmutv.botnet.core.control.command.BotCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.EqualsAndHashCode;

/**
 * The JSON constructor for {@link BotCommand}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotCommand
 * @see BotCommandSerializer
 * @see BotCommandDeserializer
 */
@EqualsAndHashCode(callSuper = true)
public class BotCommandJsonMapper extends ObjectMapper {

  /**
   * Initializes the JSON constructor.
   */
  public BotCommandJsonMapper() {
    super();
    SimpleModule module = new SimpleModule();
    module.addSerializer(BotCommand.class, BotCommandSerializer.getInstance());
    module.addSerializer(HttpFloodAttack.class, HttpFloodAttackSerializer.getInstance());
    module.addDeserializer(BotCommand.class, BotCommandDeserializer.getInstance());
    module.addDeserializer(HttpFloodAttack.class, HttpFloodAttackDeserializer.getInstance());
    super.registerModule(module);
    super.enable(SerializationFeature.INDENT_OUTPUT);
  }
}
