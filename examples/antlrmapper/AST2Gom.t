/*
 * Copyright (c) 2004-2007, INRIA
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
package antlrmapper;

import tom.antlrmapper.ATermAST;

import aterm.*;
import aterm.pure.*;

import antlrmapper.seq.*;
import antlrmapper.seq.types.*;

public class AST2Gom {

  %include { seq/Seq.tom }
  %include { Mapping.tom }

  public static Sequent genFinalTree(ATermAST t){
    return getFinalGomTree(t.genATermFromAST(TokenTable.getTokenMap()));
  }

  public static Sequent getFinalGomTree(ATerm n) {
    %match(ATerm n){
      SEQ(_,(N1,N2)) ->{
        return `seq(getListGom(N1),getListGom(N2));
      }
    }
    throw new RuntimeException("Unable to translate SEQ " + n);
  }

  private static ListPred getListGom(ATerm n){
    %match(ATerm n){
      LIST(_,(N1,N2)) -> {
        ListPred l = getListGom(`N2);
        return `concPred(getNodeGom(N1),l*);
      }
      _ -> {
        return `concPred(getNodeGom(n));
      }
    }
    throw new RuntimeException("Unable to translate LIST " + n);
  }

  private static Pred getNodeGom(ATerm n){
    %match(ATerm n){
      IMPL(_,(N1,N2)) ->{
        return `impl(getNodeGom(N1),getNodeGom(N2));
      }
      AND(_,(N1,N2)) ->{
        return `wedge(getNodeGom(N1),getNodeGom(N2));
      }
      OR(_,(N1,N2)) ->{
        return `vee(getNodeGom(N1),getNodeGom(N2));
      }
      NOT(_,(N1,_*)) ->{
        return `neg(getNodeGom(N1));
      }
      ID(NodeInfo[text=text],_) ->{
      System.out.println("aterm: " + aterm.pure.SingletonFactory.getInstance().parse(`text));
        return Pred.fromTerm(
            aterm.pure.SingletonFactory.getInstance().parse(`text));
      }
    }
    throw new RuntimeException("Unable to translate " + n);
  }
}
