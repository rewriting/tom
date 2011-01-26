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

import tom.engine.Tom;
import java.util.*;
import java.io.*;

/**
  * This example show how to call Tom from a class, and compile many files
  * to use it, populate a directory "bench/src" with tom files, and run
  * java -Dtom.home=${TOM_HOME} gom.TomRunner
  * It will compile all tom files in that directory, and loop
  */
class TomRunner {

  public static void main(String[] args) {
    int maxiter = 1000;
    if (args.length >= 1) {
      maxiter = Integer.parseInt(args[0]);
    }
    int offset = 4;
    File path = new File("bench/src");
    File[] files = path.listFiles();
    String[] params = new String[files.length + offset];
    params[0] = "-X";
    params[1] = System.getProperty("tom.home")+"/Tom.xml";
    params[2] = "-d";
    params[3] = "bench/gen";
    try {
      for (int i = 0; i < files.length; i++) {
        params[i+offset] = files[i].getCanonicalPath();
      }
    } catch (IOException e) {
      System.out.println("File not found ?");
    }

    int cnt = 0;
    while(cnt++ < maxiter) {
      int res = Tom.exec(params);
      System.out.println("Generation: " + res);
      System.out.println("Iteration: "+cnt);
    }
  }
}
