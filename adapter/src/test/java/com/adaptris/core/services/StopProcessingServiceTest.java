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

package com.adaptris.core.services;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreConstants;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.GeneralServiceExample;


public class StopProcessingServiceTest extends GeneralServiceExample {

  public StopProcessingServiceTest(java.lang.String testName) {
    super(testName);
  }

  @Override
  protected void setUp() throws Exception {
  }

  public void testService() throws Exception {
    AdaptrisMessage msg = new DefaultMessageFactory().newMessage();
    execute(new StopProcessingService(), msg);
    assertEquals("Stop processing", CoreConstants.STOP_PROCESSING_VALUE, msg.getMetadataValue(CoreConstants.STOP_PROCESSING_KEY));
    assertEquals("Skip producer", CoreConstants.STOP_PROCESSING_VALUE, msg.getMetadataValue(CoreConstants.KEY_WORKFLOW_SKIP_PRODUCER));
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    return new StopProcessingService();
  }

}
