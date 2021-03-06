/*
 * Copyright 2015 Adaptris Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package com.adaptris.security;

import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.adaptris.security.keystore.ConfiguredUrl;
import com.adaptris.security.util.Constants;

/**
 * Test Inline Keystore Functionality wrapping a single KEYSTORE_X509
 * certificate
 *
 * @author $Author: lchan $
 */
public class TestConfiguredUrl extends TestCase {

  private Properties cfg;
  private ConfiguredUrl url, copy;
  private static Log logR = null;

  /** @see TestCase */
  public TestConfiguredUrl(String testName) {
    super(testName);
    if (logR == null) {
      logR = LogFactory.getLog(TestConfiguredUrl.class);
    }
  }

  /** main. */
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(TestConfiguredUrl.class);
    return suite;
  }

  /**
   * @see TestCase#setUp()
   */
  @Override
  public void setUp() throws Exception {
    super.setUp();
    Config config = Config.getInstance();
    cfg = config.getProperties();

    if (cfg == null) {
      fail("No Configuration(!) available");
    }
    url = new ConfiguredUrl();
    url.setUrl(cfg.getProperty(Config.KEYSTORE_TEST_URL) + "&"
        + Constants.KEYSTORE_PASSWORD + "="
        + cfg.getProperty(Config.KEYSTORE_COMMON_KEYSTORE_PW));
    copy = new ConfiguredUrl(cfg.getProperty(Config.KEYSTORE_TEST_URL) + "&" + Constants.KEYSTORE_PASSWORD + "="
        + cfg.getProperty(Config.KEYSTORE_COMMON_KEYSTORE_PW));

    config.buildKeystore(cfg.getProperty(Config.KEYSTORE_TEST_URL), null, true);
  }

  /**
   * @see TestCase#tearDown()
   */
  @Override
  public void tearDown() throws Exception {
    super.tearDown();
  }

  public void testAsKeystoreLocation() throws Exception {
    assertNotNull(url.asKeystoreLocation());
    assertEquals(url.asKeystoreLocation(), copy.asKeystoreLocation());
    assertEquals(url.asKeystoreLocation().hashCode(), copy.asKeystoreLocation().hashCode());
  }

  public void testAsKeystoreProxy() throws Exception {
    assertNotNull(url.asKeystoreProxy());
  }

  public void testEquality() throws Exception {
    assertNotSame(url, new Object());
    assertEquals(url, copy);
    assertEquals(url.hashCode(), copy.hashCode());
    assertEquals(0, new ConfiguredUrl().hashCode());
    assertNotNull(url.toString());
  }

}
