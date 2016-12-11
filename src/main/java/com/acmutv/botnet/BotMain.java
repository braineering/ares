/*
  The MIT License (MIT)

  Copyright (c) 2016 Giacomo Marciani, Michele Porretta

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

import com.acmutv.botnet.bot.Bot;
import com.acmutv.botnet.service.BotWrapperShutdown;
import com.acmutv.botnet.config.BotConfigurator;
import com.acmutv.botnet.config.BotConfiguration;
import com.acmutv.botnet.service.AppService;

/**
 * This class realizes the bot wrapper entry-point.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
public class BotMain {

  /**
   * The bot wrapper main method, executed when the program is launched.
   * @param args The command line arguments.
   * @see BotConfigurator
   * @see BotConfiguration
   * @see Bot
   */
  public static void main(String[] args) {

    BotConfiguration config = BotConfigurator.loadConfiguration(args);

    AppService.registerShutdownHooks(new BotWrapperShutdown());

    Bot bot = new Bot(config);

    bot.run();

    System.exit(0);
  }

}
