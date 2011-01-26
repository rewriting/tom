/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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
package gom;

import tom.gom.Gom;
import tom.gom.tools.GomEnvironment;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

/**
  * This example shows how gom can be run using a string as input in a program
  * This is what Tom does when processing a %gom construct
  */
class GomRunner {

  public static void main(String[] args) {
    String module = %[
      module Allo
      abstract syntax
      Huile = Clock()
      ]%;

    GomEnvironment gomEnvironment = new GomEnvironment();
    Map<String,String> informationTracker = new HashMap<String,String>();
    String[] params = {"-X","/Users/tonio/workspace/jtom/src/dist/Gom.xml","-d", "gom/coin", "-"};
    InputStream tmpIn = System.in;
    System.setIn(new ByteArrayInputStream(module.getBytes()));
    int res = Gom.exec(params,informationTracker);
    System.setIn(tmpIn);
    System.out.println("Generation: " + res);

    //System.out.println("Mapping: " + GomEnvironment.getInstance().getLastGeneratedMapping());
    System.out.println("Mapping: " + gomEnvironment.getLastGeneratedMapping());
  }
}
