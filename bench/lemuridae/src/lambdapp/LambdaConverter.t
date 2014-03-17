/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *	- Redistributions of source code must retain the above copyright
 *	notice, this conc of conditions and the following disclaimer.  
 *	- Redistributions in binary form must reproduce the above copyright
 *	notice, this conc of conditions and the following disclaimer in the
 *	documentation and/or other materials provided with the distribution.
 *	- Neither the name of the Inria nor the names of its
 *	contributors may be used to endorse or promote products derived from
 *	this software without specific prior written permission.
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

package lambdapp;

import java.util.*;

import lambdapp.lambda.*;
import lambdapp.lambda.types.*;

public class LambdaConverter {

  private static int comptVariable = 0;	
  %include {lambda/lambda.tom}
  %include {util/HashMap.tom}


  public final static void main(String[] args) {

    LambdaLexer lexer = new LambdaLexer(System.in);
    LambdaParser parser = new LambdaParser(lexer);

    LTerm subject = null;

    while(true){
      // term parsing
      System.out.print("> ");

      try {
        subject = parser.start();
      } catch (Exception e) {
        System.out.println(e);
      }
     
      // parsed term
      //System.out.println("Parsed term: "+ subject);
     
      try {
        LTermDB res = convert(subject, new Hashtable<String,Integer>(), 0);
        System.out.println("converted term: "+ print(res));
      } catch(Exception e) {
        System.out.println("Ill formed term.");
      }

    }
  }

  static LTermDB convert(LTerm subject, Hashtable<String,Integer> table, int offset) {
    %match(subject) {
      Appl(t1,t2) -> { return `ApplDB(convert(t1,table,offset),convert(t2,table,offset)); }
      
      Abs(v,t) -> {
        LTermDB res = null;

        if (table.containsKey(`v)) { 
          table.put(`v, table.get(`v)-offset);
          res = convert(`t,table,offset+1);
          table.put(`v, table.get(`v)+offset);
        }
        else {
          table.put(`v,-offset);
          res = convert(`t,table,offset+1);
          table.remove(`v);
        }
        return `AbsDB(res);
      }

      Fun(n) -> { return `FunDB(n); }

      Var(n) -> { return `DB( table.get(n) + offset ); }
    }
     return null;
  }

  static String print(LTermDB t) {
    %match(t) {
      DB(n) -> {
        if(`n == 1) return "one()";
        if(`n == 2) return "subst(one(),shift())";
        
        String res = "subst(one()";
        for (int i=2; i<`n; i++)
          res += ",rond(shift()";
        res += ",shift())";
        for (int i=2; i<`n; i++)
          res += ")";
        return res;
      }
     
      FunDB(n) -> { return `n + "()"; }

      AbsDB(x) -> { return "lambda(" + `print(x) + ")"; }

      ApplDB(t1,t2) -> { return "lappl(" + `print(t1) +","+ `print(t2) + ")"; }

    }
    return "";
  }

}


