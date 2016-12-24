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

package com.acmutv.botnet.core.analysis;

import com.acmutv.botnet.core.exception.BotAnalysisException;
import com.acmutv.botnet.tool.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * This class realizes the model of system statistics.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see SystemFeatures
 * @see NetworkFeatures
 * @see NetworkStatistics
 */
@Data
@AllArgsConstructor
public class SystemStatistics {

  /**
   * The Operating System's uptime.
   */
  private Duration uptime;

  /**
   * Returns local system statistics.
   * @return local system statistics.
   * @throws BotAnalysisException when some system statistics cannot be determined.
   */
  public static SystemStatistics getLocal() throws BotAnalysisException {
    Duration uptime;

    //TODO implement for real (this is only a placeholder implementation)
    uptime = new Duration(1, TimeUnit.HOURS);

    return new SystemStatistics(uptime);
  }
}
