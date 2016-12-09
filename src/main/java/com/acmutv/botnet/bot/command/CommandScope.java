/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 Giacomo Marciani and Michele Porretta
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

package com.acmutv.botnet.bot.command;

import com.acmutv.botnet.bot.Bot;
import com.acmutv.botnet.bot.BotState;
import lombok.Getter;

import java.util.Map;

/**
 * This enum enumerates bot's command types.
 * These command are sent by the C&C and executed by the bot.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotCommand
 */
@Getter
public enum CommandScope {
  NONE ("NONE", false),
  INIT ("INIT", true),
  SLEEP ("SLEEP", true),
  KILL ("KILL", false),
  SHUTDOWN ("SHUTDOWN", false),
  ATTACK_HTTP_GET ("ATTACK HTTP-GET", true),
  ATTACK_HTTP_POST ("ATTACK HTTP-POST", true),
  ATTACK_SMTP_SEND ("ATTACK SMTP-SEND", true);

  private final String name;
  private final boolean withParams;

  CommandScope(final String name, final boolean withParams) {
    this.name = name;
    this.withParams = withParams;
  }

  public static CommandScope from(Object obj) {
    try {
      return CommandScope.valueOf(obj.toString());
    } catch (IllegalArgumentException e) {
      return CommandScope.NONE;
    }
  }
}