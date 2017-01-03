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

import lombok.Getter;

/**
 * Enumerates bot's command types.
 * These command are sent by the Controller and executed by the bot.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotCommand
 */
@Getter
public enum CommandScope {
  ATTACK_HTTP ("ATTACK_HTTP", true, true),
  CALMDOWN    ("CALMDOWN", true, true),
  KILL        ("KILL", true, true),
  NONE        ("NONE", false, false),
  REPORT      ("REPORT", true, false),
  RESTART     ("RESTART", true, false),
  SAVE_CONFIG ("SAVE_CONFIG", true, true),
  SLEEP       ("SLEEP", true, false),
  UPDATE      ("UPDATE", true, true),
  WAKEUP      ("WAKEUP", true, true);

  private final String name;
  private final boolean withParams;
  private final boolean sendReportAfter;

  CommandScope(final String name, final boolean withParams, final boolean sendReportAfter) {
    this.name = name;
    this.withParams = withParams;
    this.sendReportAfter = sendReportAfter;
  }
}