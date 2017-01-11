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

import com.acmutv.botnet.config.AppConfiguration;
import com.acmutv.botnet.core.attack.HttpAttack;
import com.acmutv.botnet.core.report.SimpleReport;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The JSON deserializer for {@link SimpleReport}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see SimpleReport
 */
public class SimpleReportDeserializer extends StdDeserializer<SimpleReport> {

  private static final Logger LOGGER = LogManager.getLogger(SimpleReportDeserializer.class);

  /**
   * The singleton of {@link SimpleReportDeserializer}.
   */
  private static SimpleReportDeserializer instance;

  /**
   * Returns the singleton of {@link SimpleReportDeserializer}.
   * @return the singleton.
   */
  public static SimpleReportDeserializer getInstance() {
    if (instance == null) {
      instance = new SimpleReportDeserializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link SimpleReportDeserializer}.
   */
  private SimpleReportDeserializer() {
    super((Class<SimpleReport>) null);
  }

  @Override
  public SimpleReport deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);
    SimpleReport report = new SimpleReport();

    if (node.hasNonNull(SimpleReport.KEY_CONFIGURATION)) {
      final AppConfiguration config =
          ctx.readValue(node.get(SimpleReport.KEY_CONFIGURATION).traverse(parser.getCodec()), AppConfiguration.class);
      report.put(SimpleReport.KEY_CONFIGURATION, config);
    }

    if (node.hasNonNull(SimpleReport.KEY_ATTACKS_HTTP)) {
      List<HttpAttack> httpAttacks = new ArrayList<>();
      Iterator<JsonNode> iter = node.get(SimpleReport.KEY_ATTACKS_HTTP).elements();
      while (iter.hasNext()) {
        JsonNode n = iter.next();
        HttpAttack attack = ctx.readValue(n.traverse(parser.getCodec()), HttpAttack.class);
        httpAttacks.add(attack);
      }
      report.put(SimpleReport.KEY_ATTACKS_HTTP, httpAttacks);
    }

    node.fields().forEachRemaining(f -> {
      if (!f.getKey().equals(SimpleReport.KEY_CONFIGURATION) &&
          !f.getKey().equals(SimpleReport.KEY_ATTACKS_HTTP)) {
        report.put(f.getKey(), f.getValue().asText());
      }
    });

    return report;
  }
}