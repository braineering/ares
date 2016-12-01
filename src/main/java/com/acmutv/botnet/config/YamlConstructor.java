/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Giacomo Marciani, Michele Porretta
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.acmutv.botnet.config;

import com.acmutv.botnet.model.TargetProxy;
import com.acmutv.botnet.model.Target;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * This class realizes the constructor for the SnakeYaml Parser, intended to the parsing of the
 * YAML configuration file.
 *
 * This class is implemented as a singleton.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see BotConfigurator
 * @see BotConfiguration
 * @see org.yaml.snakeyaml.Yaml
 * @see Constructor
 * @see TypeDescription
 */
public class YamlConstructor extends Constructor {

  private static YamlConstructor instance;

  /**
   * Initializes the singleton instance of the class.
   * @return the singleton instance of the class.
   */
  public static YamlConstructor getInstance() {
    if (instance == null) {
      instance = new YamlConstructor();
    }
    return instance;
  }

  /**
   * Creates the singleton of the class.
   */
  private YamlConstructor() {
    super(BotConfiguration.class);
    TypeDescription description = new TypeDescription(BotConfiguration.class);
    description.putListPropertyType("targets", Target.class);
    description.putListPropertyType("proxy", TargetProxy.class);
    description.putListPropertyType("sleep", String.class);
    super.addTypeDescription(description);
  }
}
