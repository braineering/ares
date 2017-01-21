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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * The command model.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see CommandScope
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotCommand {

  public static final BotCommand NONE = new BotCommand();

  /**
   * The command timestamp.
   */
  private long timestamp = 0;

  /**
   * The command scope.
   */
  @NonNull
  private CommandScope scope = CommandScope.NONE;

  /**
   * The command parameters.
   */
  @NonNull
  private Map<String,Object> params = new HashMap<>();

  /**
   * Creates a new command with the specified {@code scope}, timestamp zero and empty parameters.
   * @param scope the command scope.
   */
  public BotCommand(CommandScope scope) {
    this.scope = scope;
  }

  /**
   * Creates a new command with the specified {@code scope}, and empty parameters.
   * @param scope the command scope.
   */
  public BotCommand(long timestamp, CommandScope scope) {
    this.timestamp = timestamp;
    this.scope = scope;
  }

}
