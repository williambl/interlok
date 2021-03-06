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

package com.adaptris.util.text.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Merge implementation that replaces a node derived from an Xpath.
 *
 * @author lchan
 *
 */
abstract class MergeImpl implements DocumentMerge {
  protected transient Logger log = LoggerFactory.getLogger(this.getClass().getName());

  public MergeImpl() {
  }

}
