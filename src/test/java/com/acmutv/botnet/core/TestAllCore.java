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

package com.acmutv.botnet.core;

import com.acmutv.botnet.core.analysis.TestAllAnalysis;
import com.acmutv.botnet.core.control.TestAllControl;
import com.acmutv.botnet.core.exec.TestAllPool;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import com.acmutv.botnet.core.attack.TestAllAttack;
import com.acmutv.botnet.core.report.TestAllReport;

/**
 * JUnit test suite for core services.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see TestAllAnalysis
 * @see TestAllAttack
 * @see TestAllControl
 * @see TestAllPool
 * @see TestAllReport
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestAllAnalysis.class,
    TestAllAttack.class,
    TestAllControl.class,
    TestAllPool.class,
    TestAllReport.class
})
public class TestAllCore {

}
