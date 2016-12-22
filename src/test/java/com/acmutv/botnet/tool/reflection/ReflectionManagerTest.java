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

import com.acmutv.botnet.tool.runtime.RuntimeManager;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * This class realizes JUnit tests for {@link ReflectionManager}.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see ReflectionManager
 */
public class ReflectionManagerTest {

  /**
   * Utility class (for testing use only).
   */
  @Data
  private static class CustomObject {
    private boolean propBoolean = false;
    private int propInt = 10;
    private long propLong = 10;
    private double propDouble = 10.10;
    private String propString = "default";
  }

  /**
   * Tests reflected getter for properties (error).
   */
  @Test
  public void test_get_error() {
    CustomObject object = new CustomObject();
    try {
      ReflectionManager.get(CustomObject.class, object, "prop");
    } catch (IntrospectionException | IllegalAccessException | InvocationTargetException exc) {
      return;
    }
    Assert.fail();
  }

  /**
   * Tests reflected setter for properties (error).
   */
  @Test
  public void test_set_error() {
    CustomObject object = new CustomObject();
    try {
      ReflectionManager.set(CustomObject.class, object, "prop", "custom");
    }  catch (IntrospectionException | IllegalAccessException | InvocationTargetException exc) {
      return;
    }
    Assert.fail();
  }

  /**
   * Tests reflected getter for boolean properties.
   */
  @Test
  public void test_get_boolean() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
    CustomObject object = new CustomObject();
    Object actualObj = ReflectionManager.get(CustomObject.class, object, "propBoolean");
    Assert.assertNotNull(actualObj);
    boolean actual = (boolean) actualObj;
    Assert.assertFalse(actual);
  }

  /**
   * Tests reflected getter for int properties.
   */
  @Test
  public void test_get_int() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
    CustomObject object = new CustomObject();
    Object actualObj = ReflectionManager.get(CustomObject.class, object, "propInt");
    Assert.assertNotNull(actualObj);
    int actual = (int) actualObj;
    int expected = 10;
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests reflected getter for long properties.
   */
  @Test
  public void test_get_long() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
    CustomObject object = new CustomObject();
    Object actualObj = ReflectionManager.get(CustomObject.class, object, "propLong");
    Assert.assertNotNull(actualObj);
    long actual = (long) actualObj;
    long expected = 10;
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests reflected getter for double properties.
   */
  @Test
  public void test_get_double() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
    CustomObject object = new CustomObject();
    Object actualObj = ReflectionManager.get(CustomObject.class, object, "propDouble");
    Assert.assertNotNull(actualObj);
    double actual = (double) actualObj;
    double expected = 10.10;
    Assert.assertEquals(expected, actual, 0);
  }

  /**
   * Tests reflected getter for string properties.
   */
  @Test
  public void test_get_string() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
    CustomObject object = new CustomObject();
    Object actualObj = ReflectionManager.get(CustomObject.class, object, "propString");
    Assert.assertNotNull(actualObj);
    String actual = (String) actualObj;
    String expected = "default";
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests reflected setter for boolean properties.
   */
  @Test
  public void test_set_boolean() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
    CustomObject object = new CustomObject();
    ReflectionManager.set(CustomObject.class, object, "propBoolean", true);
    Assert.assertTrue(object.isPropBoolean());
  }

  /**
   * Tests reflected setter for int properties.
   */
  @Test
  public void test_set_int() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
    CustomObject object = new CustomObject();
    ReflectionManager.set(CustomObject.class, object, "propInt", 100);
    int expected = 100;
    int actual = object.getPropInt();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests reflected setter for long properties.
   */
  @Test
  public void test_set_long() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
    CustomObject object = new CustomObject();
    ReflectionManager.set(CustomObject.class, object, "propLong", 100);
    long expected = 100;
    long actual = object.getPropLong();
    Assert.assertEquals(expected, actual);
  }

  /**
   * Tests reflected setter for double properties.
   */
  @Test
  public void test_set_double() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
    CustomObject object = new CustomObject();
    ReflectionManager.set(CustomObject.class, object, "propDouble", 100.10);
    double expected = 100.10;
    double actual = object.getPropDouble();
    Assert.assertEquals(expected, actual, 0);
  }

  /**
   * Tests reflected setter for string properties.
   */
  @Test
  public void test_set_string() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
    CustomObject object = new CustomObject();
    ReflectionManager.set(CustomObject.class, object, "propString", "custom");
    String expected = "custom";
    String actual = object.getPropString();
    Assert.assertEquals(expected, actual);
  }
}