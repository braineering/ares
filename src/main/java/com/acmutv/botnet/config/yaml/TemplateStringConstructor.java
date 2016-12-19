/*
  The MIT License (MIT)

  Copyright (c) 2016 Giacomo Marciani

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

package com.acmutv.botnet.config.yaml;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;

import java.util.HashMap;
import java.util.Map;

/**
 * This class realizes the YAML constructor for template string.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateStringConstructor extends AbstractConstruct {

  private static final Logger LOGGER = LogManager.getLogger(TemplateStringConstructor.class);

  /**
   * The templating map, used by the {@link StrSubstitutor}.
   */
  @NonNull
  private Map<String,String> map = buildMap();

  @Override
  public Object construct(Node node) {
    LOGGER.traceEntry("node={}", node);
    ScalarNode snode = (ScalarNode) node;
    String value = snode.getValue();
    StrSubstitutor ss = new StrSubstitutor(this.map);
    String result = ss.replace(value);
    return LOGGER.traceExit(result);
  }

  /**
   * Builds the templating map, used by the {@link StrSubstitutor}.
   * @return the templating map.
   */
  private Map<String,String> buildMap() {
    Map<String,String> map = new HashMap<>();
    map.put("PROJECT_RESOURCES",
        AppConfigurationConstructor.class.getResource("/").getPath().replaceAll("/$", ""));
    return map;
  }
}