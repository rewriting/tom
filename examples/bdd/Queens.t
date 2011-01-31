/*
 * Copyright (c) 2008-2011, INPL, INRIA
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

package bdd;

import bdd.bdd.types.Node;
import bdd.bdd.types.SolutionList;
import tom.library.sl.*;
import java.util.*;

/**************************************************************************
  BDD demonstration of the N-Queen chess problem.
  -----------------------------------------------
  The BDD variables correspond to a NxN chess board like:

     0    N      2N     ..  N*N-N
     1    N+1    2N+1   ..  N*N-N+1
     2    N+2    2N+2   ..  N*N-N+2
     ..   ..     ..     ..  ..
     N-1  2N-1   3N-1   ..  N*N-1

   So for example a 4x4 is:

     0 4  8 12
     1 5  9 13
     2 6 10 14
     3 7 11 15

   One solution is then that 2,4,11,13 should be true, meaning a queen
   should be placed there:

     . X . .
     . . . X
     X . . .
     . . X .
     
**************************************************************************/
public class Queens {

  %include{ bdd/Bdd.tom }
  %include{ sl.tom }

   private static bdd.Bdd tree = new Bdd();

  public final static void main(String[] args) {
    Node queen = mkQueens(8);

    //System.out.println(queen);
    SolutionList sol =  tree.allSat(queen);
    //System.out.println("allSat = " + sol);
    System.out.println("nb sol = " + sol.length());
    //System.out.println("countSat = " + tree.countSat(queen));

  }

  public static Node mkQueens(int N) {
    Node queen; /* N-queens problem express as a BDD */

    queen = mkTrue();
    Node X[][] = new Node[N][N]; /* BDD variable array */

    /* Build variable array */
    for(int i=0 ; i<N ; i++) {
      for(int j=0 ; j<N ; j++) {
        X[i][j] = var(i*N+j);
        //System.out.println(%[X[@i@,@j@] = @X[i][j]@]%);
      }
    }

    /* Place a queen in each row */
    for(int i=0 ; i<N ; i++) {
      Node e = mkFalse();
      for(int j=0 ; j<N ; j++) {
        e = or(e,X[i][j]);
        //System.out.println(%[e[@i@,@j@] = @e@]%);
      }
      queen = and(queen,e);
      //System.out.println("queen " + i + ": " + queen);
    }
   
    /* Build requirements for each variable(field) */
    for(int i=0 ; i<N ; i++) {
      for(int j=0 ; j<N ; j++) {
        queen = and(queen,build(X,i,j));
        //System.out.println(%[queen[@i@,@j@] = @queen@]%);
       }
    }
    return queen;
  }

  /* 
   * Build the requirements for all other fields than (i,j) assuming
   * that (i,j) has a queen 
   */
   private static Node build(Node[][] X, int i, int j) {
     Node a = mkTrue();
     Node b = mkTrue();
     Node c = mkTrue();
     Node d = mkTrue();

     /* No one in the same column */
     for(int l=0 ; l<X[i].length ; l++) {
       if(l != j) {
         a = and(a,imp(X[i][j],not(X[i][l])));
       }
     }
     /* No one in the same row */
     for(int k=0 ; k<X.length ; k++) {
       if(k != i) {
         b = and(b,imp(X[i][j],not(X[k][j])));
       }
     }
     /* No one in the same down-right diagonal */
     for(int k=0 ; k<X.length ; k++) {
       int ll = j+k-i;
       if(ll>=0 && ll<X.length) {
         if(k != i) {
           c = and(c,imp(X[i][j],not(X[k][ll])));
         }
       }
     }
     /* No one in the same up-right diagonal */
     for(int k=0 ; k<X.length ; k++) {
       int ll = j+i-k;
       if(ll>=0 && ll<X.length) {
         if(k != i) {
           d = and(d,imp(X[i][j],not(X[k][ll])));
         }
       }
     }
     //System.out.println(%[a[@i@,@j@] = @a@]%);
     //System.out.println(%[b[@i@,@j@] = @b@]%);
     //System.out.println(%[c[@i@,@j@] = @c@]%);
     //System.out.println(%[d[@i@,@j@] = @d@]%);
     Node res = and(a,and(b,and(c,d)));
     //System.out.println(%[constraint[@i@,@j@] = @res@]%);
     return  res;
   }

   private static Node mkTrue() { return Bdd.ONE; }
   private static Node mkFalse() { return Bdd.ZERO; }
   private static Node var(int index) { return `Var(index,mkFalse(),mkTrue()); }
   private static Node and(Node a, Node b) { return tree.apply(`OpAnd(),a,b); }
   private static Node or(Node a, Node b) { return tree.apply(`OpOr(),a,b); }
   private static Node imp(Node a, Node b) { return tree.apply(`OpImp(),a,b); }
   private static Node not(Node a) { return tree.apply(`OpNot(),a,a); }

}
