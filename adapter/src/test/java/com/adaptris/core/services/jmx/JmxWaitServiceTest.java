/*
 * Copyright 2015 Adaptris Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adaptris.core.services.jmx;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.ServiceCase;
import com.adaptris.core.ServiceException;
import com.adaptris.core.jmx.JmxConnection;
import com.adaptris.util.TimeInterval;

public class JmxWaitServiceTest extends ServiceCase {

  private static final String BASE_DIR_KEY = "JmxServiceExamples.baseDir";

  @Mock
  private JmxOperationInvoker<Boolean> mockInvoker;

  public JmxWaitServiceTest(String name) {
    super(name);
    if (PROPERTIES.getProperty(BASE_DIR_KEY) != null) {
      setBaseDir(PROPERTIES.getProperty(BASE_DIR_KEY));
    }
  }

  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  public void tearDown() throws Exception {
  }

  public void testWaitError() throws Exception {
    JmxWaitService service = new JmxWaitService();
    service.setOperationName(getName());
    service.setObjectName("com.adaptris:type=JMX,id=" + getName());
    when(mockInvoker.invoke((MBeanServerConnection) any(), (ObjectName) any(), anyString(), any(Object[].class),
        any(String[].class)))
        .thenThrow(new Exception(getName()));
    service.setInvoker(mockInvoker);
    service.setRetryInterval(new TimeInterval(1L, TimeUnit.SECONDS));
    try {
      start(service);
      service.doService(AdaptrisMessageFactory.getDefaultInstance().newMessage());
      fail();
    } catch (ServiceException expected) {
      assertNotNull(expected.getCause());
      assertEquals(getName(), expected.getCause().getMessage());
    } finally {
      stop(service);
    }
  }

  public void testNoWait() throws Exception {
    JmxWaitService service = new JmxWaitService();
    service.setOperationName(getName());
    service.setObjectName("com.adaptris:type=JMX,id=" + getName());
    when(mockInvoker.invoke((MBeanServerConnection) any(), (ObjectName) any(), anyString(), any(Object[].class),
        any(String[].class))).thenReturn(true);
    service.setInvoker(mockInvoker);
    try {
      start(service);
      service.doService(AdaptrisMessageFactory.getDefaultInstance().newMessage());
    }
    finally {
      stop(service);
    }
  }

  public void testWait() throws Exception {
    JmxWaitService service = new JmxWaitService();
    service.setOperationName(getName());
    service.setObjectName("com.adaptris:type=JMX,id=" + getName());
    when(mockInvoker.invoke((MBeanServerConnection) any(), (ObjectName) any(), anyString(), any(Object[].class),
        any(String[].class))).thenReturn(false, true);
    service.setInvoker(mockInvoker);
    service.setRetryInterval(new TimeInterval(1L, TimeUnit.SECONDS));
    try {
      start(service);
      service.doService(AdaptrisMessageFactory.getDefaultInstance().newMessage());
    }
    finally {
      stop(service);
    }
  }

  public void testWaitNegation() throws Exception {
    JmxWaitService service = new JmxWaitService();
    service.setOperationName(getName());
    service.setObjectName("com.adaptris:type=JMX,id=" + getName());
    when(mockInvoker.invoke((MBeanServerConnection) any(), (ObjectName) any(), anyString(), any(Object[].class),
        any(String[].class)))
        .thenReturn(true, false);
    service.setInvoker(mockInvoker);
    service.setNegate(true);
    service.setRetryInterval(new TimeInterval(1L, TimeUnit.SECONDS));
    try {
      start(service);
      service.doService(AdaptrisMessageFactory.getDefaultInstance().newMessage());
    } finally {
      stop(service);
    }
  }



  @Override
  protected Object retrieveObjectForSampleConfig() {
    JmxWaitService callService = new JmxWaitService();
    JmxConnection conn = new JmxConnection();
    conn.setJmxServiceUrl("service:jmx:jmxmp://localhost:5555");
    conn.setUsername("jmxUsername");
    conn.setPassword("jmxPassword");
    callService.setConnection(conn);
    callService.setObjectName("com.adaptris:type=InFlight,adapter=MyAdapter,channel=C1,workflow=WF1,id=name");
    callService.setOperationName("hasInFlightMessages");
    return callService;
  }

  @Override
  protected Object retrieveObjectForCastorRoundTrip() {
    JmxWaitService callService = new JmxWaitService();
    callService.setConnection(new JmxConnection());
    callService.setObjectName("com.adaptris:type=InFlight,adapter=MyAdapter,channel=C1,workflow=WF1,id=name");
    callService.setOperationName("hasInFlightMessages");
    return callService;
  }



}
