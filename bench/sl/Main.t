/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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
 *  - Neither the name of the Inria nor the names of its
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
package sl;

import sl.term.types.*;
import static sl.BenchJJTraveler.*;

public class Main{

  %include {term/term.tom}

  public static void main(String[] args) {

    int intbase = 0;
    int count = 0;
    try {
      intbase = Integer.parseInt(args[0]);
      count = Integer.parseInt(args[1]);
    } catch (Exception e) {
      System.out.println("Usage: java sl.Main <base> <count>");
      return;
    }
    Nat base = buildInt(intbase);
    sl.jjterm.types.Nat jjbase = buildJJInt(intbase);
    Nat[] query = new Nat[10];
    int cpt = 0;
    query[cpt++] = `F(M(),N(),P(),Zero(),Suc(Zero()));
    query[cpt++] = `F(M(),N(),F(N(),P(),Zero(),N(),Zero()),Zero(),Suc(Zero()));
    query[cpt++] = `C(F(N(), Zero(), C(M(), N()), P(), Suc(N())), C(N(), P()));
    query[cpt++] = `F(C(M(), C(N(), Suc(Suc(N())))), N(), C(P(), Suc(Zero())), Suc(Suc(Zero())), Zero());
    query[cpt++] = `F(N(), M(), Suc(C(Suc(P()), N())), M(), C(Suc(N()), N()));
    query[cpt++] = `F(P(), Suc(P()), Zero(), N(), P());

    sl.jjterm.types.Nat[] jjquery = new sl.jjterm.types.Nat[10];
    cpt = 0;
    jjquery[cpt++] = make_F(make_M(),make_N(),make_P(),make_Zero(),make_Suc(make_Zero()));
    jjquery[cpt++] = make_F(make_M(),make_N(),make_F(make_N(),make_P(),make_Zero(),make_N(),make_Zero()),make_Zero(),make_Suc(make_Zero()));
    jjquery[cpt++] = make_C(make_F(make_N(),make_Zero(),make_C(make_M(),make_N()),make_P(),make_Suc(make_N())),make_C(make_N(),make_P()));
    jjquery[cpt++] = make_F(make_C(make_M(),make_C(make_N(),make_Suc(make_Suc(make_N())))),make_N(),make_C(make_P(),make_Suc(make_Zero())),make_Suc(make_Suc(make_Zero())),make_Zero());
    jjquery[cpt++] = make_F(make_N(),make_M(),make_Suc(make_C(make_Suc(make_P()),make_N())),make_M(),make_C(make_Suc(make_N()),make_N()));
    jjquery[cpt++] = make_F(make_P(),make_Suc(make_P()),make_Zero(),make_N(),make_P());

    for(int i=0 ; i<cpt ; i++) {
      BenchSlTraveler.run(query[i], 1, count,base);
      BenchSl.run(query[i], 1, count,base);
      BenchJJTraveler.run(jjquery[i], 1, count,jjbase);
      BenchSlTraveler.run(query[i], 2, count,base);
      BenchSl.run(query[i], 2, count,base);
      BenchJJTraveler.run(jjquery[i], 2, count,jjbase);
    }
  }

  public static Nat buildInt(int i) {
    Nat res = `Zero();
    for(int j=0; j<i; j++) {
      res = `Suc(res);
    }
    return res;
  }

  public static sl.jjterm.types.Nat buildJJInt(int i) {
    sl.jjterm.types.Nat res = make_Zero();
    for(int j=0; j<i; j++) {
      res = make_Suc(res);
    }
    return res;
  }


}
