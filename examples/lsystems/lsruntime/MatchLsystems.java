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
package lsruntime;

import lsruntime.adt.lsystems.types.Node;
import lsruntime.adt.lsystems.types.NodeList;

public interface MatchLsystems {
  
  /**
   * For the initialization of the algorithm
   *
   * @return a NodeList which contains the axiom
   */
  NodeList init();
  
  /**
   * Try to match the Node token between prev_rev and next
   * 
   * /!\ the NodeList prev_rev is the reversed list of which is before token
   * 
   * the %match should apear like:
   *   
   **   %match(NodeList prev_rev,Node token, NodeList next) {
   **     // F < A -> F
   **     concNode(I*,F,head*),A,_ -> {
   **       if (runtime.UCD(I,extign)) {
   **         return `concNode(F);
   **       }
   **     }
   **     // F > B -> B F
   **     _,F,concNode(I*,B,tail*) -> {
   **       if (runtime.UCD(I,extign)) {
   **         return `concNode(B,F);
   **       }
   **     }
   **     // F > [ B ] B -> F F
   **     _,F,concNode(I1*,SubList(concNode(I2*,B,tail1*)),I3*,B,tail2*) -> {
   **       if (runtime.UCD(I1,ign) && runtime.UCD(I2,ign) && runtime.UCD(I3,ign)) {
   **         return `concNode(F,F);
   **       }
   **     }
   **     // A < B > A -> A
   **     concNode(I1*,A,head*),B,concNode(I2*,A,tail*) -> {
   **       if (runtime.UCD(I1,ign) && runtime.UCD(I2,ign)) {
   **         return `concNode(A);
   **       }
   **     }
   **     // F -> A    Attention : à mettre à la fin
   **     _,F,_ -> {
   **       return `concNode(A);
   **     }
   **   }
   *
   * @return a NodeList which contains the axiom
   */
  NodeList apply(NodeList prev_rev, Node token, NodeList next);
  
}
