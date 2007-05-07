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

import aterm.*;
import aterm.pure.PureFactory;
import java.io.*;



import tom.library.strategy.mutraveler.MuStrategy;
import tom.library.strategy.mutraveler.Position;

import java.util.Stack;

public class VisitableViewer {

  %include { atermmapping.tom }
  %include { sl.tom }

  // for parsing
  private static ATermFactory atermFactory = new PureFactory();


  public static void visitableToDotStdout(Visitable v) {
    visitableToDotStdout(v,null,"");
  }

  public static void visitableToDot(Visitable v, Writer w) 
    throws java.io.IOException {
      visitableToDot(v,w,null,"");
    }

  public static void 
    visitableToDotStdout(Visitable v, Position hilight, String color)
    {
      try {
        Writer w = new BufferedWriter(new OutputStreamWriter(System.out)); 
        visitableToDot(v,w,hilight,color);
        w.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  public static void 
    visitableToDot(Visitable v, Writer w, Position hilight, String color) 
    throws java.io.IOException {
      ATerm at = atermFactory.parse(v.toString());
      ATermToDot(at, w, hilight, color);
    }

  private static 
    void ATermToDot(ATerm term, Writer w, Position hilight, String color) 
    throws java.io.IOException 
    {
      w.write("digraph g { graph [ordering=out];\n");
      ATermToDotRec(term, w, new Position(), hilight, color);
      w.write("}\n");
    }

  private static String posToId(Position p) {
    int[] a = p.toArray();
    StringBuffer buf = new StringBuffer("pos_");
    for(int i=0; i<p.depth(); i++) {
      buf.append(a[i]);
    }
    return buf.toString();
  }

  private static void ATermToDotRec
    (ATerm term, Writer w, Position pos, Position hilight, String color)
    throws java.io.IOException {
      %match(ATerm term) {
        a@ATermAppl(AFun[name=name],list) -> {
          // node id declaration
          if (pos.equals(hilight)) {
            w.write(%[@posToId(pos)@ [label="@`name@",fillcolor="@color@",style=filled];]%);
          } else {
            w.write(%[@posToId(pos)@ [label="@`name@"];]%);
          }

          // arrows 
          int i=0;
          for(; !`list.isEmpty(); `list = `list.getNext()) {
            ATerm t = `list.getFirst(); i++;
            w.write(%[@posToId(pos)@ -> ]%);
            pos.down(i);
            w.write(%[@posToId(pos)@;]%);
            ATermToDotRec(t,w,pos,hilight,color);
            pos.up();
          }
        }
      }
    }

  /* -------- pstree-like part --------- */
  public static void toTreeStdout(Visitable v) {
    try {
      Writer w = new BufferedWriter(new OutputStreamWriter(System.out)); 
      toTree(v,w);
      w.write('\n');
      w.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void toTree(Visitable v, Writer w)
    throws java.io.IOException {
        ATerm at = atermFactory.parse(v.toString());
        ATermToTree(at, w, new Stack<Integer>(), 0);
      }

  private static void writeContext(Writer w, Stack<Integer> context, int deep) 
    throws java.io.IOException {
    for(int i=0; i<deep; i++) {
      if (context.contains(i))
        w.write('│');
      else
        w.write(' ');
    }
  }



  private static void ATermToTree(ATerm term, Writer w, Stack<Integer> context, int deep) 
    throws java.io.IOException {
      %match(term) {
        ATermAppl(AFun[name=name],list) -> {
          ATermAppl a = (ATermAppl) term;
          if (`a.getArity() == 0) {  // no child
            w.write("─"+`name);
            return;
          } else if (`a.getArity() == 1) {  // only one child
            w.write("─" + `name + "──");
            deep = deep + `name.length() + 3;
            ATermToTree(`list.getFirst(),w,context,deep);
            return;
          } else {
            int ndeep = deep + `name.length() + 3;
            %match (ATermList `list) {
              (first,l*,last) -> {
                // first child
                w.write("─" + `name + "─┬");
                context.push(ndeep-1); 
                ATermToTree(`first,w,context,ndeep);
                context.pop();
                w.write('\n');

                // 2 ... n-1
                %match (ATermList l) {
                  (_*,c,_*) -> {
                    writeContext(w,context,ndeep-1);
                    w.write("├");
                    context.push(ndeep-1);
                    ATermToTree(`c,w,context,ndeep);
                    context.pop();
                    w.write('\n');
                  }
                }
                // last child
                writeContext(w,context,ndeep-1);
                w.write("└");
                ATermToTree(`last,w,context,ndeep);
              }
            }
          }
        }
      }
    }
}
