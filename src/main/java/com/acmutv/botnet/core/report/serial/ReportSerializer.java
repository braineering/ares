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
import com.acmutv.botnet.core.report.Report;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class realizes the JSON serializer for {@link Report}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see Report
 */
public class ReportSerializer extends StdSerializer<Report> {

  /**
   * The singleton of {@link ReportSerializer}.
   */
  private static ReportSerializer instance;

  /**
   * Returns the singleton of {@link ReportSerializer}.
   * @return the singleton.
   */
  public static ReportSerializer getInstance() {
    if (instance == null) {
      instance = new ReportSerializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link ReportSerializer}.
   */
  private ReportSerializer() {
    super((Class<Report>) null);
  }

  @Override
  public void serialize(Report value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    if (value.containsKey("config")) {
      final AppConfiguration config = (AppConfiguration) value.get("config");
      gen.writeFieldName("config");
      provider.findValueSerializer(AppConfiguration.class).serialize(config, gen, provider);
      //new AppConfigurationJsonMapper().writeValue(gen, config);
    }

    List<String> sortedKeys = new ArrayList<>();
    value.keySet().stream()
        .sorted()
        .filter((String k) ->!k.equals("config"))
        .forEachOrdered(sortedKeys::add);
    for (String k : sortedKeys) {
      gen.writeStringField(k, value.get(k).toString());
    }

    gen.writeEndObject();
  }
}