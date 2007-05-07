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
package ted;

import java.io.*;
import aterm.*;
import aterm.pure.PureFactory;
import java.util.*;
import java.lang.reflect.*;


// pour le main
//import tom.library.strategy.mutraveler.*;



public class Ted {

  %include { atermmapping.tom }
  %include { sl.tom }

  // for parsing
  private static ATermFactory atermFactory = new PureFactory();
  
  public static Map match(ATerm a1, ATerm a2) {
    HashMap tds = new HashMap();
    return match(a1,a2,tds);
  }

  private static Map match(ATerm a1, ATerm a2, Map tds) {

    if (a1 == a2) return tds;

    %match (ATerm a1, ATerm a2) {

      ATermAppl(AFun(name,arity,_),args1), ATermAppl(AFun(name,arity,_), args2) -> {
        return match(`args1,`args2,tds);
      }

      ATermList(), ATermList() -> { System.out.println("ici"); return tds; }
      
      // different lenghts
      ATermList(_,_*), ATermList() -> { return null; }
      ATermList(), ATermList(_,_*) -> { return null; }

      ATermList(x1,t1*), ATermList(x2,t2*) -> { 
        tds = match(`x1,`x2, tds);
        if (tds == null)
          return null;
        else
          return match(`t1,`t2, tds);
      }

      t@ATermPlaceholder( ATermAppl(AFun(name,arity,_),_) ), _ -> {

        if( `arity != 0 ) {
          System.err.println("Bad placeholder format");
          System.exit(1);
        }   

        if(`name.equals("any")) {
          return tds;
        } else {
          if(tds.containsKey(`t)) {
            return match(((ATerm) tds.get(`t)), a2, tds);
          } else {
            tds.put(`t, a2);
            return tds;
          }
        }
      
      }

      /* Placeholder with wrong format */
      ATermPlaceholder(_), _ -> {
        System.err.println("Bad placeholder format");
        System.exit(1);
      }
    }

    return null;
  }

  public void run(String[] argv) throws java.io.IOException {
    if (argv.length != 1) {
      System.out.println("Usage java Ted file");
      System.exit(1);
    }

    BufferedReader in = new BufferedReader(new FileReader(argv[0]));

    // aterm to modify
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    ATerm res = atermFactory.parse(stdin.readLine());

    // loops over the lines
    while(in.ready()) {
      //strategy
      String strategy = in.readLine().trim();
      //action
      ATerm action = atermFactory.parse(in.readLine());
      res = run(res, strategy,action);
      // reads an empty line
      in.readLine();    
    }
    System.out.println(res);
  }

  public static ATerm run(ATerm res, String strategy, ATerm action) throws java.io.IOException {
    Constructor ctor = null;
    try {
      Class strategy_class = Class.forName("tom.library.sl." + strategy);
      ctor = strategy_class.getConstructor(new Class[] {Strategy.class});
    }
    catch( Exception e ) { System.err.println("This strategy doesn't exist or has a bad signature : " + e.getMessage()); }

    Strategy vtor = null;

    %match(ATerm action) {
      ATermAppl(AFun[name="replace"], concATerm(tomatch, replacement)) -> {
        try {
          vtor = (Strategy) ctor.newInstance (new Object[] {new ReplaceVisitor(`tomatch, `replacement)} );
          return (ATerm) vtor.visitLight(res);
        }
        catch ( Exception e ) { e.printStackTrace(); }
      }

      ATermAppl(AFun[name="remove"], concATerm(tomatch)) -> {
        try {
          vtor = (Strategy) ctor.newInstance (new Object[] {new MatchAndRemoveVisitor(`tomatch)});
          return  (ATerm) vtor.visitLight(res);
        }
        catch ( Exception e ) { e.printStackTrace(); }
      }

      ATermAppl(AFun[name="grep"], concATerm(tomatch)) -> {
        try {
          ATermList l = `concATerm();
          GrepVisitor v =  new GrepVisitor(`tomatch);
          Strategy s = `mu(MuVar("x"),Try(Sequence(v,All(MuVar("x")))));
          vtor = (Strategy) ctor.newInstance (new Object[] {s});
          s.visitLight(res);
          return v.getList();
        }
        catch ( Exception e ) { e.printStackTrace(); }
      }
    }

    return res;
  }

  //same functionality as above, except for the input and output
  public static String run(String aTerm, String strategy, String actionStr) throws java.io.IOException {
    ATerm at = run(atermFactory.parse(aTerm),strategy,atermFactory.parse(actionStr));
    return at.toString();
  }

  public static void main (String[] argv) throws IOException {
    Ted ted = new Ted();
    ted.run(argv);
  }
}
