/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.accumulo.server.util.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BaseRelativeTimeTest {

  private BogusTime futureAdvice;
  private BogusTime pastAdvice;
  private BogusTime local;
  private BaseRelativeTime brt;

  static class BogusTime implements ProvidesTime {
    public long value = 0;

    @Override
    public long currentTime() {
      return value;
    }
  }

  @Before
  public void setup(){
    //Arrangement
    futureAdvice = new BogusTime();
    pastAdvice = new BogusTime();
    local = new BogusTime();
    local.value = futureAdvice.value = pastAdvice.value = System.currentTimeMillis();
    // Ten seconds into the future
    futureAdvice.value += 10000;
    // Ten seconds into the past
    pastAdvice.value -= 10000;

    brt = new BaseRelativeTime(local);
  }

  @Test
  public void testMatchesTime() {
    BogusTime bt = new BogusTime();
    BogusTime now = new BogusTime();
    now.value = bt.value = System.currentTimeMillis();

    BaseRelativeTime brt = new BaseRelativeTime(now);
    assertEquals(brt.currentTime(), now.value);
    brt.updateTime(now.value);
    assertEquals(brt.currentTime(), now.value);
  }

  @Test
  public void testFutureTimeOnce() {
    //Action
    brt.updateTime(futureAdvice.value);
    long once = brt.currentTime();
    //Assert
    assertTrue(once < futureAdvice.value);
    assertTrue(once > local.value);
  }

  @Test
  public void testFutureTimeManyTimes() {
    //Action
    brt.updateTime(futureAdvice.value);
    long once = brt.currentTime();

    for (int i = 0; i < 100; i++) {
      brt.updateTime(futureAdvice.value);
    }
    long many = brt.currentTime();

    //Assert
    assertTrue(many > once);
    assertTrue("after much advice, relative time is still closer to local time",
            (futureAdvice.value - many) < (once - local.value));
  }

  @Test
  public void testPastTimeOnce() {
    //Action
    brt.updateTime(pastAdvice.value);
    long once = brt.currentTime();
    //Assert
    assertTrue(once < local.value);
  }

  @Test
  public void testPastTimeTwice() {
    //Action
    brt.updateTime(pastAdvice.value);
    long once = brt.currentTime();
    brt.updateTime(pastAdvice.value);
    long twice = brt.currentTime();
    //Assert
    assertTrue("Time cannot go backwards", once <= twice);
  }
}
