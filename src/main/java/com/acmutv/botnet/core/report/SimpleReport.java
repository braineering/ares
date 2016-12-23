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

package com.acmutv.botnet.core.report;

import com.acmutv.botnet.core.report.serial.ReportJsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * This class realizes a simple report.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
public class SimpleReport extends HashMap<String,Object> implements Report {

  public SimpleReport(Map<? extends String,? extends Object> map) {
    super(map);
  }

  public SimpleReport() {
    super();
  }

  /**
   * Merges the report with another report.
   * @param other the report to merge with.
   */
  public void merge(Report other) {
    super.putAll(other);
  }

  /**
   * Converts the instance to a JSON.
   * @return the JSON.
   */
  @Override
  public String toJson() {
    ObjectMapper mapper = new ReportJsonMapper();
    //TODO map the report to JSON
    return null;
  }
}
