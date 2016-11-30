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

package com.acmutv.botnet;

import com.acmutv.botnet.control.TargetAttackerTest;
import com.acmutv.botnet.model.PeriodTest;
import com.acmutv.botnet.tools.RandomToolsTest;
import com.acmutv.botnet.config.ConfigurationTest;
import com.acmutv.botnet.control.BashExecutorTest;
import com.acmutv.botnet.tools.HTTPToolsTest;
import com.acmutv.botnet.tools.URLToolsTest;
import com.acmutv.botnet.tools.WatchToolsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * This class realizes the unit test suite that encapsulates all the unit tests provided for the
 * application.
 * @author Giacomo Marciani {@literal <gmarciani@ieee.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0.0
 * @see RandomToolsTest
 * @see URLToolsTest
 * @see HTTPToolsTest
 * @see WatchToolsTest
 * @see PeriodTest
 * @see ConfigurationTest
 * @see BashExecutorTest
 * @see TargetAttackerTest
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    RandomToolsTest.class,
    URLToolsTest.class,
    HTTPToolsTest.class,
    WatchToolsTest.class,
    PeriodTest.class,
    ConfigurationTest.class,
    BashExecutorTest.class,
    TargetAttackerTest.class
})
public class TestAll {

}
