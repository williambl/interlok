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

package com.adaptris.core.stubs;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.DefaultMessageFactory;

/**
 * <p>
 * The stub factory which returns implementations of
 * <code>AdaptrisMessage</code>.
 * </p>
 * <p>
 * Functionally the same as DefaultMessageFactory but we want to check that the
 * message implementations that are created can be different.
 * </p>
 */
public final class DefectiveMessageFactory extends DefaultMessageFactory {

  public static enum WhenToBreak {
    INPUT,
    OUTPUT,
    BOTH,
    NEVER
  };

  private transient WhenToBreak whenToBreak;

  public DefectiveMessageFactory() {
    whenToBreak = WhenToBreak.BOTH;
  }

  public DefectiveMessageFactory(WhenToBreak wtb) {
    whenToBreak = wtb;
  }

  @Override
  public AdaptrisMessage newMessage() {
    AdaptrisMessage result = new DefectiveAdaptrisMessage(uniqueIdGenerator(), this);
    return result;
  }

  boolean brokenInput() {
    return (whenToBreak == WhenToBreak.INPUT) || (whenToBreak == WhenToBreak.BOTH);
  }

  boolean brokenOutput() {
    return (whenToBreak == WhenToBreak.OUTPUT) || (whenToBreak == WhenToBreak.BOTH);
  }
}
