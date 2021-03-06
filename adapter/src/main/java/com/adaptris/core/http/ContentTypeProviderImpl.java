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

package com.adaptris.core.http;

import static org.apache.commons.lang.StringUtils.isBlank;

import javax.mail.internet.ContentType;
import javax.mail.internet.ParseException;

public abstract class ContentTypeProviderImpl implements ContentTypeProvider {


  protected String build(String mimeType, String charset) {
    StringBuilder buf = new StringBuilder();
    buf.append(mimeType);
    if (!isBlank(charset) && !hasCharset(mimeType)) {
      buf.append("; charset=");
      buf.append(charset);
    }
    return buf.toString();
  }

  private boolean hasCharset(String mimeType) {
    boolean result = false;
    try {
      ContentType ct = new ContentType(mimeType);
      String charset = ct.getParameter("charset");
      result = !isBlank(charset);
    }
    catch (ParseException e) {
      // couldn't parse, hasn't got a charset.
      result = false;
    }
    return result;
  }
}
