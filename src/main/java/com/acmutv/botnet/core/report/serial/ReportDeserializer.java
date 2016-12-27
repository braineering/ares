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

package com.acmutv.botnet.core.report.serial;

import com.acmutv.botnet.core.report.Report;
import com.acmutv.botnet.core.report.SimpleReport;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * This class realizes the JSON deserializer for {@link Report}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see Report
 */
public class ReportDeserializer extends StdDeserializer<Report> {

  /**
   * The singleton of {@link ReportDeserializer}.
   */
  private static ReportDeserializer instance;

  /**
   * Returns the singleton of {@link ReportDeserializer}.
   * @return the singleton.
   */
  public static ReportDeserializer getInstance() {
    if (instance == null) {
      instance = new ReportDeserializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link ReportDeserializer}.
   */
  private ReportDeserializer() {
    super((Class<Report>) null);
  }

  @Override
  public Report deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);
    Report report = new SimpleReport();
    node.fields().forEachRemaining(e -> report.put(e.getKey(), e.getValue()));
    return report;
  }
}