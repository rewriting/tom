/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package quine;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

public class TestQuine {

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestQuine.class.getName());
  }

  @Test
  public void testQuine() {
    // get the original
    BufferedReader original = new BufferedReader(
        new InputStreamReader(getClass().getResourceAsStream("S.t")));

    ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
    System.setOut(new PrintStream(outStream));
    S.main(new String[0]);
    BufferedReader generated = new BufferedReader(
        new StringReader(outStream.toString()));

    try {
      int lines = 0;
      String oriLine = original.readLine();
      String genLine = generated.readLine();
      while(oriLine != null && genLine != null) {
        assertEquals(oriLine,genLine);
        lines++;
        oriLine = original.readLine();
        genLine = generated.readLine();
      }
      assertNull(oriLine);
      assertNull(genLine);
      assertEquals(23, lines);
    } catch (java.io.IOException e) {
      fail("IO Error " + e);
    }
  }

}

