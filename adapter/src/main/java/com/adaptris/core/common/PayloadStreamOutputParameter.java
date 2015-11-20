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

package com.adaptris.core.common;

import static org.apache.commons.lang.StringUtils.defaultIfEmpty;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.apache.commons.io.IOUtils;

import com.adaptris.core.util.ExceptionHelper;
import com.adaptris.interlok.InterlokException;
import com.adaptris.interlok.config.DataOutputParameter;
import com.adaptris.interlok.types.InterlokMessage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This {@code DataOutputParameter} is used when you want to write data to the {@link com.adaptris.core.AdaptrisMessage} payload.
 * 
 * 
 * @config stream-payload-output-parameter
 * 
 */
@XStreamAlias("stream-payload-output-parameter")
public class PayloadStreamOutputParameter implements DataOutputParameter<InputStream> {

  private String contentEncoding;
  public PayloadStreamOutputParameter() {
    
  }

  @Override
  public void insert(InputStream data, InterlokMessage msg) throws InterlokException {
    try {
      String encoding = defaultIfEmpty(getContentEncoding(), msg.getContentEncoding());
      copyAndClose(data, msg.getWriter(encoding));
    } catch (IOException e) {
      ExceptionHelper.rethrowCoreException(e);
    }
  }

  public String getContentEncoding() {
    return contentEncoding;
  }

  public void setContentEncoding(String contentEncoding) {
    this.contentEncoding = contentEncoding;
  }

  private void copyAndClose(InputStream input, Writer out) throws IOException {
    try (InputStream autoCloseIn = new BufferedInputStream(input); BufferedWriter autoCloseOut = new BufferedWriter(out)) {
      IOUtils.copy(autoCloseIn, autoCloseOut);
    }
  }

}
