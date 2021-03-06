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

package com.adaptris.core.services.routing;


public class AlwaysMatchSyntaxIdentifierTest extends SyntaxIdentifierCase {

  public static final String LINE = "The quick brown fox jumps over the lazy dog";

  public AlwaysMatchSyntaxIdentifierTest(java.lang.String testName) {
    super(testName);
  }

  @Override
  public AlwaysMatchSyntaxIdentifier createIdentifier() {
    return new AlwaysMatchSyntaxIdentifier();
  }

  public void testMatch() throws Exception {
    AlwaysMatchSyntaxIdentifier ident = createIdentifier();
    assertTrue("Matches regexp", ident.isThisSyntax(LINE));
  }

}
