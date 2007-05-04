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
import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.MuStrategy;
import tom.library.strategy.mutraveler.MuVar;
import tom.library.strategy.mutraveler.Mu;



public class StrategyViewer {

  %include{mustrategy.tom}
  %typeterm Writer { 
    implement {java.io.Writer} 
    is_sort(t)     { t instanceof java.util.HashMap }
  }

  private static int counter = 0;  
  static private String clean(String s) {
    s = s.replace('.','_');
    s = s.replace('$','_');
    s = s.replace('@','_');
    return s;
  }

  private static void 
    toDot(MuStrategy subj, Writer out, MuStrategy hilight, String color) {
      %match (Strategy subj) {
        y@MuVar[] -> { return; }

        x@Mu[] ->{  ((Mu)`x).expand(); } // to mu-expand

        x -> { 
          if((`x) instanceof ted.DebugStrategy) 
            toDot( ((DebugStrategy)`x).getStrat(), out, hilight, color);

          // get display string for the node
          String[] tab = `x.getClass().getName().split("\\.");
          String name = tab[tab.length-1];
          tab = name.split("\\$");
          name = tab[tab.length-1];
          String idNode = `clean(x.toString());

          try { // because of IO exception

            if(`x == hilight) 
              out.write(%[@idNode@ [label="@name@",style=filled,fillcolor="@color@"];]%);
            else 
              out.write(%[@idNode@ [label="@name@"];]%);
            out.write("\n");  

            int n = `x.getChildCount();
            for(int i=0; i<n; i++) {
              Visitable s = `x.getChildAt(i);
              if(s instanceof ted.DebugStrategy)  
                s = s.getChildAt(0);
              %match(Strategy s) {
                y@MuVar[var=varName] -> {
                  MuStrategy pointer = (MuStrategy) `((MuVar)y).getInstance();
                  if (pointer == null) return;
                  String idMu = clean(pointer.toString());
                  if (pointer.getChildCount() > 0 && pointer.getChildAt(0) != `y) {
                    String idMuVar = idMu + "_" + (counter++);
                    out.write(%[
                        @idMuVar@ [label="@`varName@"]; 
                        @idNode@ -> @idMuVar@;
                        @idMuVar@ -> @idMu@;
                        ]%);
                  }
                  continue;
                }
                y -> {
                  out.write(%[@idNode@ -> @clean(`y.toString())@;]%);
                  out.write("\n");
                  toDot(`y,out,hilight,color);
                }
              } // match
            } // loop over the sons
            out.flush();
          } catch(Exception e) {
            e.printStackTrace();
          }
        }
      }
    }

  public static void 
    stratToDot(MuStrategy s, Writer w) 
    throws java.io.IOException {
      stratToDot(s,w,null,"");
    }

  public static void 
    stratToDot(MuStrategy s, Writer w, MuStrategy hilight, String color) 
    throws java.io.IOException {
      w.write("digraph G { graph [ordering=out];");
      toDot(s,w,hilight,color);
      w.write("}\n");
    }

  public static void stratToDotStdout(MuStrategy s) {
    try {
      Writer w = new BufferedWriter(new OutputStreamWriter(System.out));
      stratToDot(s,w);
    } catch (java.io.IOException e) { e.printStackTrace(); }
  }



  public static void main(String[] args) {
    MuStrategy strat = `Repeat(R());

    //MuStrategy strat = `Sequence(InnermostId(ChoiceId(RepeatId(R()),R())), InnermostId( ChoiceId( Sequence(RepeatId(R()), RepeatId(SequenceId(ChoiceId(R(),R()),OnceTopDownId(R())))), SequenceId(R(),OnceTopDownId(RepeatId(R()))))));


    stratToDotStdout(strat);
  }

  %strategy R() extends Identity() {
    visit Strategy {
      x -> { return `x; }
    }
  }
  %strategy S(s:Strategy) extends Identity() {
    visit Strategy {
      x -> { return `x; }
    }
  }
}
