/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.accumulo.core.conf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import org.apache.accumulo.core.conf.AccumuloConfiguration.ScanExecutorConfig;
import org.apache.accumulo.core.spi.scan.SimpleScanDispatcher;
import org.junit.jupiter.api.Test;

public class AccumuloConfigurationTest {

  @SuppressWarnings("removal")
  private static final Property VFS_CONTEXT_CLASSPATH_PROPERTY =
      Property.VFS_CONTEXT_CLASSPATH_PROPERTY;

  @Test
  public void testGetPropertyByString() {
    AccumuloConfiguration c = DefaultConfiguration.getInstance();
    boolean found = false;
    for (Property p : Property.values()) {
      if (p.getType() != PropertyType.PREFIX) {
        found = true;
        // ensure checking by property and by key works the same
        assertEquals(c.get(p), c.get(p.getKey()));
        // ensure that getting by key returns the expected value
        assertEquals(p.getDefaultValue(), c.get(p.getKey()));
      }
    }
    assertTrue(found, "test was a dud, and did nothing");
  }

  @Test
  public void testGetSinglePort() {
    AccumuloConfiguration c = DefaultConfiguration.getInstance();
    ConfigurationCopy cc = new ConfigurationCopy(c);
    cc.set(Property.TSERV_CLIENTPORT, "9997");
    int[] ports = cc.getPort(Property.TSERV_CLIENTPORT);
    assertEquals(1, ports.length);
    assertEquals(9997, ports[0]);
  }

  @Test
  public void testGetAnyPort() {
    AccumuloConfiguration c = DefaultConfiguration.getInstance();
    ConfigurationCopy cc = new ConfigurationCopy(c);
    cc.set(Property.TSERV_CLIENTPORT, "0");
    int[] ports = cc.getPort(Property.TSERV_CLIENTPORT);
    assertEquals(1, ports.length);
    assertEquals(0, ports[0]);
  }

  @Test
  public void testGetInvalidPort() {
    AccumuloConfiguration c = DefaultConfiguration.getInstance();
    ConfigurationCopy cc = new ConfigurationCopy(c);
    cc.set(Property.TSERV_CLIENTPORT, "1020");
    int[] ports = cc.getPort(Property.TSERV_CLIENTPORT);
    assertEquals(1, ports.length);
    assertEquals(Integer.parseInt(Property.TSERV_CLIENTPORT.getDefaultValue()), ports[0]);
  }

  @Test
  public void testGetPortRange() {
    AccumuloConfiguration c = DefaultConfiguration.getInstance();
    ConfigurationCopy cc = new ConfigurationCopy(c);
    cc.set(Property.TSERV_CLIENTPORT, "9997-9999");
    int[] ports = cc.getPort(Property.TSERV_CLIENTPORT);
    assertEquals(3, ports.length);
    assertEquals(9997, ports[0]);
    assertEquals(9998, ports[1]);
    assertEquals(9999, ports[2]);
  }

  @Test
  public void testGetPortRangeInvalidLow() {
    AccumuloConfiguration c = DefaultConfiguration.getInstance();
    ConfigurationCopy cc = new ConfigurationCopy(c);
    cc.set(Property.TSERV_CLIENTPORT, "1020-1026");
    assertThrows(IllegalArgumentException.class, () -> {
      int[] ports = cc.getPort(Property.TSERV_CLIENTPORT);
      assertEquals(3, ports.length);
      assertEquals(1024, ports[0]);
      assertEquals(1025, ports[1]);
      assertEquals(1026, ports[2]);
    });
  }

  @Test
  public void testGetPortRangeInvalidHigh() {
    AccumuloConfiguration c = DefaultConfiguration.getInstance();
    ConfigurationCopy cc = new ConfigurationCopy(c);
    cc.set(Property.TSERV_CLIENTPORT, "65533-65538");
    assertThrows(IllegalArgumentException.class, () -> {
      int[] ports = cc.getPort(Property.TSERV_CLIENTPORT);
      assertEquals(3, ports.length);
      assertEquals(65533, ports[0]);
      assertEquals(65534, ports[1]);
      assertEquals(65535, ports[2]);
    });
  }

  @Test
  public void testGetPortInvalidSyntax() {
    AccumuloConfiguration c = DefaultConfiguration.getInstance();
    ConfigurationCopy cc = new ConfigurationCopy(c);
    cc.set(Property.TSERV_CLIENTPORT, "[65533,65538]");
    assertThrows(IllegalArgumentException.class, () -> cc.getPort(Property.TSERV_CLIENTPORT));
  }

  private static class TestConfiguration extends AccumuloConfiguration {

    private HashMap<String,String> props = new HashMap<>();
    private int upCount = 0;
    private AccumuloConfiguration parent;

    TestConfiguration() {
      parent = null;
    }

    TestConfiguration(AccumuloConfiguration parent) {
      this.parent = parent;
    }

    public void set(String p, String v) {
      props.put(p, v);
      upCount++;
    }

    @Override
    public boolean isPropertySet(Property prop) {
      return props.containsKey(prop.getKey());
    }

    @Override
    public long getUpdateCount() {
      return upCount;
    }

    @Override
    public String get(Property property) {
      String v = props.get(property.getKey());
      if (v == null & parent != null) {
        v = parent.get(property);
      }
      return v;
    }

    @Override
    public void getProperties(Map<String,String> output, Predicate<String> filter) {
      if (parent != null) {
        parent.getProperties(output, filter);
      }
      for (Entry<String,String> entry : props.entrySet()) {
        if (filter.test(entry.getKey())) {
          output.put(entry.getKey(), entry.getValue());
        }
      }
    }

  }

  @Test
  public void testMutatePrefixMap() {
    TestConfiguration tc = new TestConfiguration();
    tc.set(Property.TABLE_ARBITRARY_PROP_PREFIX.getKey() + "a1", "325");
    tc.set(Property.TABLE_ARBITRARY_PROP_PREFIX.getKey() + "a2", "asg34");

    Map<String,String> pm1 = tc.getAllPropertiesWithPrefix(Property.TABLE_ARBITRARY_PROP_PREFIX);

    Map<String,String> expected1 = new HashMap<>();
    expected1.put(Property.TABLE_ARBITRARY_PROP_PREFIX.getKey() + "a1", "325");
    expected1.put(Property.TABLE_ARBITRARY_PROP_PREFIX.getKey() + "a2", "asg34");
    assertEquals(expected1, pm1);

    assertThrows(UnsupportedOperationException.class, () -> pm1.put("k9", "v3"));
  }

  public TestConfiguration setUpTestConfigForGetByPrefix() {
    // Universal Arrangement for GetByPrefix test cases
    TestConfiguration tc = new TestConfiguration();

    tc.set(Property.TABLE_ARBITRARY_PROP_PREFIX.getKey() + "a1", "325");
    tc.set(Property.TABLE_ARBITRARY_PROP_PREFIX.getKey() + "a2", "asg34");

    tc.set(Property.TABLE_ITERATOR_SCAN_PREFIX.getKey() + "i1", "class34");
    tc.set(Property.TABLE_ITERATOR_SCAN_PREFIX.getKey() + "i1.opt", "o99");

    return tc;
  }

  public TestConfiguration reSetTestConfigForGetByPrefix(TestConfiguration tc) {
    tc.set(Property.TABLE_ITERATOR_SCAN_PREFIX.getKey() + "i2", "class42");
    tc.set(Property.TABLE_ITERATOR_SCAN_PREFIX.getKey() + "i2.opt", "o78234");
    return tc;
  }

  @Test
  public void testGetByPrefixWithTableArbitraryPropPrefix() {
    // Arrangement
    TestConfiguration tc = setUpTestConfigForGetByPrefix();
    // Expected Value
    Map<String,String> expected1 = new HashMap<>();
    expected1.put(Property.TABLE_ARBITRARY_PROP_PREFIX.getKey() + "a1", "325");
    expected1.put(Property.TABLE_ARBITRARY_PROP_PREFIX.getKey() + "a2", "asg34");

    // Action
    Map<String,String> pm1 = tc.getAllPropertiesWithPrefix(Property.TABLE_ARBITRARY_PROP_PREFIX);

    // Assert
    assertSame(pm1, tc.getAllPropertiesWithPrefix(Property.TABLE_ARBITRARY_PROP_PREFIX));
    assertEquals(expected1, pm1);
  }

  @Test
  public void testGetByPrefixWithTableIteratorScanPrefix() {
    // Arrangement
    TestConfiguration tc = setUpTestConfigForGetByPrefix();
    // Expected Value
    Map<String,String> expected2 = new HashMap<>();
    expected2.put(Property.TABLE_ITERATOR_SCAN_PREFIX.getKey() + "i1", "class34");
    expected2.put(Property.TABLE_ITERATOR_SCAN_PREFIX.getKey() + "i1.opt", "o99");

    // Action
    Map<String,String> pm2 = tc.getAllPropertiesWithPrefix(Property.TABLE_ITERATOR_SCAN_PREFIX);

    // Assert
    assertSame(pm2, tc.getAllPropertiesWithPrefix(Property.TABLE_ITERATOR_SCAN_PREFIX));
    assertEquals(expected2, pm2);
  }

  @Test
  public void testGetByPrefixVFSContextClasspathProp() {
    // Arrangement
    TestConfiguration tc = setUpTestConfigForGetByPrefix();

    // Action
    Map<String,String> pm3 = tc.getAllPropertiesWithPrefix(Property.VFS_CONTEXT_CLASSPATH_PROPERTY);

    // Assert
    assertSame(pm3, tc.getAllPropertiesWithPrefix(Property.VFS_CONTEXT_CLASSPATH_PROPERTY));
    assertEquals(0, pm3.size());
  }

  @Test
  public void testGetByPrefixGetOnePrefixNotRegenerateOther() {
    // Arrangement
    TestConfiguration tc = setUpTestConfigForGetByPrefix();
    // Expected Value
    Map<String,String> pm1 = tc.getAllPropertiesWithPrefix(Property.TABLE_ARBITRARY_PROP_PREFIX);
    Map<String,String> pm2 = tc.getAllPropertiesWithPrefix(Property.TABLE_ITERATOR_SCAN_PREFIX);
    Map<String,String> pm3 = tc.getAllPropertiesWithPrefix(Property.VFS_CONTEXT_CLASSPATH_PROPERTY);

    // Action
    // ensure getting one prefix does not cause others to unnecessarily regenerate
    Map<String,String> pm4 = tc.getAllPropertiesWithPrefix(Property.TABLE_ARBITRARY_PROP_PREFIX);
    Map<String,String> pm5 = tc.getAllPropertiesWithPrefix(Property.TABLE_ITERATOR_SCAN_PREFIX);
    Map<String,String> pm6 = tc.getAllPropertiesWithPrefix(Property.VFS_CONTEXT_CLASSPATH_PROPERTY);

    // Assert
    assertSame(pm1, pm4);
    assertSame(pm2, pm5);
    assertSame(pm3, pm6);
  }

  @Test
  public void testGetByPrefixWithOneTimeRegeneratedTableIteratorScanPrefix() {
    // Arrangement
    TestConfiguration tc = setUpTestConfigForGetByPrefix();
    Map<String,String> pm2 = tc.getAllPropertiesWithPrefix(Property.TABLE_ITERATOR_SCAN_PREFIX);
    // Regenerate(check if the regeneration change other prefix)
    tc = reSetTestConfigForGetByPrefix(tc);
    // Expected Value
    Map<String,String> expected2 = new HashMap<>();
    expected2.put(Property.TABLE_ITERATOR_SCAN_PREFIX.getKey() + "i1", "class34");
    expected2.put(Property.TABLE_ITERATOR_SCAN_PREFIX.getKey() + "i1.opt", "o99");
    expected2.put(Property.TABLE_ITERATOR_SCAN_PREFIX.getKey() + "i2", "class42");
    expected2.put(Property.TABLE_ITERATOR_SCAN_PREFIX.getKey() + "i2.opt", "o78234");

    // Action
    Map<String,String> pmA = tc.getAllPropertiesWithPrefix(Property.TABLE_ITERATOR_SCAN_PREFIX);

    // Assert
    assertNotSame(pm2, pmA);
    assertSame(pmA, tc.getAllPropertiesWithPrefix(Property.TABLE_ITERATOR_SCAN_PREFIX));
    assertEquals(expected2, pmA);
  }

  @Test
  public void testGetByPrefixWithOneTimeRegeneratedTableArbitraryPropPrefix() {
    // Arrangement
    TestConfiguration tc = setUpTestConfigForGetByPrefix();
    Map<String,String> pm1 = tc.getAllPropertiesWithPrefix(Property.TABLE_ARBITRARY_PROP_PREFIX);
    // Regenerate
    tc = reSetTestConfigForGetByPrefix(tc);
    // Expected Value
    Map<String,String> expected1 = new HashMap<>();
    expected1.put(Property.TABLE_ARBITRARY_PROP_PREFIX.getKey() + "a1", "325");
    expected1.put(Property.TABLE_ARBITRARY_PROP_PREFIX.getKey() + "a2", "asg34");

    // Action
    Map<String,String> pmC = tc.getAllPropertiesWithPrefix(Property.TABLE_ARBITRARY_PROP_PREFIX);

    // Assert
    assertNotSame(pm1, pmC);// not same instance
    assertSame(pmC, tc.getAllPropertiesWithPrefix(Property.TABLE_ARBITRARY_PROP_PREFIX));
    assertEquals(expected1, pmC);// same value
  }

  @Test
  public void testGetByPrefixWithTwiceRegeneratedVFSContextClasspathProp() {
    // Arrangement
    TestConfiguration tc = setUpTestConfigForGetByPrefix();
    Map<String,String> pm3 = tc.getAllPropertiesWithPrefix(Property.VFS_CONTEXT_CLASSPATH_PROPERTY);
    // Regenerate First Time
    tc = reSetTestConfigForGetByPrefix(tc);
    // Regenerate Second Time
    tc.set(Property.VFS_CONTEXT_CLASSPATH_PROPERTY.getKey() + "ctx123", "hdfs://ib/p1");

    // Action
    Map<String,String> pmE = tc.getAllPropertiesWithPrefix(Property.VFS_CONTEXT_CLASSPATH_PROPERTY);

    // Assert
    assertSame(pmE, tc.getAllPropertiesWithPrefix(Property.VFS_CONTEXT_CLASSPATH_PROPERTY));
    assertNotSame(pm3, pmE);
    assertEquals(
        Map.of(Property.VFS_CONTEXT_CLASSPATH_PROPERTY.getKey() + "ctx123", "hdfs://ib/p1"), pmE);
  }

  @Test
  public void testGetByPrefixWithTwiceRegeneratedTableIteratorScanPrefix() {
    // Arrangement
    TestConfiguration tc = setUpTestConfigForGetByPrefix();
    // Regenerate First Time
    tc = reSetTestConfigForGetByPrefix(tc);
    Map<String,String> pmA = tc.getAllPropertiesWithPrefix(Property.TABLE_ITERATOR_SCAN_PREFIX);
    // Regenerate Second Time
    tc.set(Property.VFS_CONTEXT_CLASSPATH_PROPERTY.getKey() + "ctx123", "hdfs://ib/p1");
    // Expected Value
    Map<String,String> expected2 = new HashMap<>();
    expected2.put(Property.TABLE_ITERATOR_SCAN_PREFIX.getKey() + "i1", "class34");
    expected2.put(Property.TABLE_ITERATOR_SCAN_PREFIX.getKey() + "i1.opt", "o99");
    expected2.put(Property.TABLE_ITERATOR_SCAN_PREFIX.getKey() + "i2", "class42");
    expected2.put(Property.TABLE_ITERATOR_SCAN_PREFIX.getKey() + "i2.opt", "o78234");

    // Action
    Map<String,String> pmG = tc.getAllPropertiesWithPrefix(Property.TABLE_ITERATOR_SCAN_PREFIX);

    // Assert
    assertNotSame(pmA, pmG);
    assertSame(pmG, tc.getAllPropertiesWithPrefix(Property.TABLE_ITERATOR_SCAN_PREFIX));
    assertEquals(expected2, pmG);
  }

  @Test
  public void testGetByPrefixWithTwiceRegeneratedTableArbitraryPropPrefix() {
    // Arrangement
    TestConfiguration tc = setUpTestConfigForGetByPrefix();
    // Regenerate First Time
    tc = reSetTestConfigForGetByPrefix(tc);
    Map<String,String> pmC = tc.getAllPropertiesWithPrefix(Property.TABLE_ARBITRARY_PROP_PREFIX);
    // Regenerate Second Time
    tc.set(Property.VFS_CONTEXT_CLASSPATH_PROPERTY.getKey() + "ctx123", "hdfs://ib/p1");
    // Expected Value
    Map<String,String> expected1 = new HashMap<>();
    expected1.put(Property.TABLE_ARBITRARY_PROP_PREFIX.getKey() + "a1", "325");
    expected1.put(Property.TABLE_ARBITRARY_PROP_PREFIX.getKey() + "a2", "asg34");

    // Action
    Map<String,String> pmI = tc.getAllPropertiesWithPrefix(Property.TABLE_ARBITRARY_PROP_PREFIX);

    // Assert
    assertNotSame(pmC, pmI);
    assertSame(pmI, tc.getAllPropertiesWithPrefix(Property.TABLE_ARBITRARY_PROP_PREFIX));
    assertEquals(expected1, pmI);
  }

  @Test
  public void testGetByPrefixGetOnePrefixNotRegenerateOtherAfterRegenerate() {
    // Arrangement
    TestConfiguration tc = setUpTestConfigForGetByPrefix();
    // Regenerate First Time
    tc = reSetTestConfigForGetByPrefix(tc);
    // Regenerate Second Time
    tc.set(Property.VFS_CONTEXT_CLASSPATH_PROPERTY.getKey() + "ctx123", "hdfs://ib/p1");
    Map<String,String> pmE = tc.getAllPropertiesWithPrefix(Property.VFS_CONTEXT_CLASSPATH_PROPERTY);
    Map<String,String> pmG = tc.getAllPropertiesWithPrefix(Property.TABLE_ITERATOR_SCAN_PREFIX);

    // Action
    // ensure getting one prefix does not cause others to unnecessarily regenerate
    Map<String,String> pmK = tc.getAllPropertiesWithPrefix(Property.VFS_CONTEXT_CLASSPATH_PROPERTY);
    Map<String,String> pmL = tc.getAllPropertiesWithPrefix(Property.TABLE_ITERATOR_SCAN_PREFIX);

    // Assert
    assertSame(pmE, pmK);
    assertSame(pmG, pmL);
  }

  @Test
  public void testScanExecutors() {
    String defName = SimpleScanDispatcher.DEFAULT_SCAN_EXECUTOR_NAME;

    TestConfiguration tc = new TestConfiguration(DefaultConfiguration.getInstance());

    Collection<ScanExecutorConfig> executors = tc.getScanExecutors(false);

    assertEquals(2, executors.size());

    ScanExecutorConfig sec =
        executors.stream().filter(c -> c.name.equals(defName)).findFirst().get();
    assertEquals(Integer.parseInt(Property.TSERV_SCAN_EXECUTORS_DEFAULT_THREADS.getDefaultValue()),
        sec.maxThreads);
    assertFalse(sec.priority.isPresent());
    assertTrue(sec.prioritizerClass.get().isEmpty());
    assertTrue(sec.prioritizerOpts.isEmpty());

    // ensure deprecated props is read if nothing else is set
    tc.set("tserver.readahead.concurrent.max", "6");
    assertEquals(6, sec.getCurrentMaxThreads());
    assertEquals(Integer.parseInt(Property.TSERV_SCAN_EXECUTORS_DEFAULT_THREADS.getDefaultValue()),
        sec.maxThreads);
    ScanExecutorConfig sec2 =
        tc.getScanExecutors(false).stream().filter(c -> c.name.equals(defName)).findFirst().get();
    assertEquals(6, sec2.maxThreads);

    // ensure new prop overrides deprecated prop
    tc.set(Property.TSERV_SCAN_EXECUTORS_DEFAULT_THREADS.getKey(), "9");
    assertEquals(9, sec.getCurrentMaxThreads());
    assertEquals(Integer.parseInt(Property.TSERV_SCAN_EXECUTORS_DEFAULT_THREADS.getDefaultValue()),
        sec.maxThreads);
    ScanExecutorConfig sec3 =
        tc.getScanExecutors(false).stream().filter(c -> c.name.equals(defName)).findFirst().get();
    assertEquals(9, sec3.maxThreads);

    ScanExecutorConfig sec4 =
        executors.stream().filter(c -> c.name.equals("meta")).findFirst().get();
    assertEquals(Integer.parseInt(Property.TSERV_SCAN_EXECUTORS_META_THREADS.getDefaultValue()),
        sec4.maxThreads);
    assertFalse(sec4.priority.isPresent());
    assertFalse(sec4.prioritizerClass.isPresent());
    assertTrue(sec4.prioritizerOpts.isEmpty());

    tc.set("tserver.metadata.readahead.concurrent.max", "2");
    assertEquals(2, sec4.getCurrentMaxThreads());
    ScanExecutorConfig sec5 =
        tc.getScanExecutors(false).stream().filter(c -> c.name.equals("meta")).findFirst().get();
    assertEquals(2, sec5.maxThreads);

    tc.set(Property.TSERV_SCAN_EXECUTORS_META_THREADS.getKey(), "3");
    assertEquals(3, sec4.getCurrentMaxThreads());
    ScanExecutorConfig sec6 =
        tc.getScanExecutors(false).stream().filter(c -> c.name.equals("meta")).findFirst().get();
    assertEquals(3, sec6.maxThreads);

    String prefix = Property.TSERV_SCAN_EXECUTORS_PREFIX.getKey();
    tc.set(prefix + "hulksmash.threads", "66");
    tc.set(prefix + "hulksmash.priority", "3");
    tc.set(prefix + "hulksmash.prioritizer", "com.foo.ScanPrioritizer");
    tc.set(prefix + "hulksmash.prioritizer.opts.k1", "v1");
    tc.set(prefix + "hulksmash.prioritizer.opts.k2", "v3");

    executors = tc.getScanExecutors(false);
    assertEquals(3, executors.size());
    ScanExecutorConfig sec7 =
        executors.stream().filter(c -> c.name.equals("hulksmash")).findFirst().get();
    assertEquals(66, sec7.maxThreads);
    assertEquals(3, sec7.priority.getAsInt());
    assertEquals("com.foo.ScanPrioritizer", sec7.prioritizerClass.get());
    assertEquals(Map.of("k1", "v1", "k2", "v3"), sec7.prioritizerOpts);

    tc.set(prefix + "hulksmash.threads", "44");
    assertEquals(66, sec7.maxThreads);
    assertEquals(44, sec7.getCurrentMaxThreads());

    ScanExecutorConfig sec8 = tc.getScanExecutors(false).stream()
        .filter(c -> c.name.equals("hulksmash")).findFirst().get();
    assertEquals(44, sec8.maxThreads);

    // test scan server props
    Collection<ScanExecutorConfig> scanServExecutors = tc.getScanExecutors(true);
    assertEquals(2, scanServExecutors.size());
    ScanExecutorConfig sec9 =
        scanServExecutors.stream().filter(c -> c.name.equals(defName)).findFirst().get();
    // earlier in the test tserver.readahead.concurrent.max was set to 6
    assertEquals(6, sec9.maxThreads);
    assertFalse(sec9.priority.isPresent());
    assertTrue(sec9.prioritizerClass.get().isEmpty());
    assertTrue(sec9.prioritizerOpts.isEmpty());

    tc.set(Property.SSERV_SCAN_EXECUTORS_DEFAULT_THREADS.getKey(), "17");
    ScanExecutorConfig sec10 =
        tc.getScanExecutors(true).stream().filter(c -> c.name.equals(defName)).findFirst().get();
    assertEquals(17, sec10.maxThreads);
  }

  // note: this is hard to test if there aren't any deprecated properties
  // if that's the case, just comment this test out or create a dummy deprecated property
  @SuppressWarnings("deprecation")
  @Test
  public void testResolveDeprecated() {
    var conf = new ConfigurationCopy();

    // deprecated first argument
    var e1 = assertThrows(IllegalArgumentException.class, () -> conf
        .resolve(Property.INSTANCE_DFS_DIR, Property.INSTANCE_DFS_URI, Property.INSTANCE_DFS_URI));
    assertEquals("Unexpected deprecated INSTANCE_DFS_DIR", e1.getMessage());

    // non-deprecated second argument
    var e2 = assertThrows(IllegalArgumentException.class,
        () -> conf.resolve(Property.INSTANCE_VOLUMES, Property.INSTANCE_DFS_DIR,
            Property.INSTANCE_SECRET, Property.INSTANCE_DFS_DIR, Property.INSTANCE_VOLUMES));
    assertEquals("Unexpected non-deprecated [INSTANCE_SECRET, INSTANCE_VOLUMES]", e2.getMessage());

    // empty second argument always resolves to non-deprecated first argument
    assertSame(Property.INSTANCE_VOLUMES, conf.resolve(Property.INSTANCE_VOLUMES));

    // none are set, resolve to non-deprecated
    assertSame(Property.INSTANCE_VOLUMES, conf.resolve(Property.INSTANCE_VOLUMES,
        Property.INSTANCE_DFS_DIR, Property.INSTANCE_DFS_URI));

    // resolve to first deprecated argument that's set; here, it's the final one
    conf.set(Property.INSTANCE_DFS_URI, "");
    assertSame(Property.INSTANCE_DFS_URI, conf.resolve(Property.INSTANCE_VOLUMES,
        Property.INSTANCE_DFS_DIR, Property.INSTANCE_DFS_URI));

    // resolve to first deprecated argument that's set; now, it's the first one because both are set
    conf.set(Property.INSTANCE_DFS_DIR, "");
    assertSame(Property.INSTANCE_DFS_DIR, conf.resolve(Property.INSTANCE_VOLUMES,
        Property.INSTANCE_DFS_DIR, Property.INSTANCE_DFS_URI));

    // every property is set, so resolve to the non-deprecated one
    conf.set(Property.INSTANCE_VOLUMES, "");
    assertSame(Property.INSTANCE_VOLUMES, conf.resolve(Property.INSTANCE_VOLUMES,
        Property.INSTANCE_DFS_DIR, Property.INSTANCE_DFS_URI));
  }
}
