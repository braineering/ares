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

package com.acmutv.botnet.tool.reflection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * This class realizes Java reflection services.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
public class ReflectionManager {

  private static final Logger LOGGER = LogManager.getLogger(ReflectionManager.class);

  /**
   * Gets an object property by reflection.
   *
   * @param type     the object class to reflect.
   * @param object   the object instance to address.
   * @param property the name of the object property to get.
   * @return the property value.
   * @throws IntrospectionException when property cannot be found.
   * @throws InvocationTargetException when getter method cannot be invoked.
   * @throws IllegalAccessException when getter method cannot be accessed.
   */
  public static Object get(Class<?> type, Object object, String property)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    LOGGER.traceEntry("type={} object={} property={}", type, object, property);
    final BeanInfo beanInfo = Introspector.getBeanInfo(type);
    final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
    Method getter = Arrays.stream(propertyDescriptors)
        .filter(d -> d.getName().equals(property))
        .findFirst().map(PropertyDescriptor::getReadMethod)
        .orElse(null);
    if (getter == null) {
      throw new IntrospectionException("Cannot find setter nethod for property " + property);
    }
    Object result = getter.invoke(object);

    return LOGGER.traceExit(result);
  }

  /**
   * Sets an object property by reflection.
   *
   * @param type     the object class to reflect.
   * @param object   the object instance to address.
   * @param property the name of the object property to set.
   * @param value    the value to set the property to.
   * @throws IntrospectionException when property cannot be found.
   * @throws InvocationTargetException when setter method cannot be invoked.
   * @throws IllegalAccessException when setter method cannot be accessed.
   */
  public static void set(Class<?> type, Object object, String property, Object value)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    LOGGER.traceEntry("type={} object={} property={} value={}", type, object, property, value);
    final BeanInfo beanInfo = Introspector.getBeanInfo(type);
    final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
    Method setter = Arrays.stream(propertyDescriptors)
        .filter(d -> d.getName().equals(property))
        .findFirst().map(PropertyDescriptor::getWriteMethod)
        .orElse(null);
    if (setter == null) {
      throw new IntrospectionException("Cannot find setter nethod for property " + property);
    }

    setter.invoke(object, value);
  }
}
