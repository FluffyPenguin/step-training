// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import static org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class GreeterTest {

  @Test
  public void testGreeting() throws Exception {
    Greeter greeter = new Greeter();

    String greeting = greeter.greet("Ada");

    assertEquals("Hello Ada", greeting);
  }

  @Test
  public void testGreetingTrimsWhitespace() throws Exception {
    Greeter greeter = new Greeter();

    String greeting = greeter.greet("   Ada   ");

    // Whitespace should be trimmed
    assertEquals("Hello Ada", greeting);
  }

  @Test
  public void testGreetingRemovesSymbols() throws Exception {
    Greeter greeter = new Greeter();
    String greeting = greeter.greet(" !@#_()%($#)%#($%)#$ Ada $%#)$ (%)   (@#$ ");

    //Symbols should be removed
    assertEquals("Hello Ada", greeting);
  }
}
