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
package tom.library.utils;

import java.io.*;
import tom.library.sl.*;
import java.util.Stack;
import aterm.pure.PureFactory;
import att.grappa.*;
import javax.swing.*;

public class Viewer {

  %include { sl.tom }
  %include { ../mapping/java/sl/visitable.tom }
  %include { ../mapping/java/aterm.tom }

  /* -------- dot part --------- */
  public static void toDot(tom.library.sl.Visitable v, Writer w) 
    throws java.io.IOException {
      w.write("digraph visitable {\nordering=out;");
      try{
        Strategy print = new Print(w);
        `TopDown(print).visit(v);
      } catch (VisitFailure e) {throw new RuntimeException("unexcepted visit failure");}
      w.write("\n}");
    }

  public static void toDot(tom.library.sl.Visitable v) throws IOException{
      Writer w = new BufferedWriter(new OutputStreamWriter(System.out)); 
      toDot(v,w);
      w.write('\n');
      w.flush();
  }

  %typeterm Writer{
    implement {Writer}
  }

  private static String getNodeFromPos(Position p) {
    String tmp = p.toString().replaceAll(", ",""); 
    return "p"+tmp.substring(1,tmp.length()-1); 
  }

  static class Print extends AbstractStrategy {

    protected Writer w;

    public Print(Writer w) {
      initSubterm();
      this.w=w;
    }

    public Visitable visitLight(Visitable any) throws VisitFailure {
      throw new VisitFailure();
    } 

    public int visit() {
      Visitable v = getEnvironment().getSubject();
      try {
      if (v instanceof Path) {
        Position current = getEnvironment().getPosition();
        Position father = current.up();
        w.write(%[
            @getNodeFromPos(current)@ [label=""];
            @getNodeFromPos(father)@ -> @getNodeFromPos(current)@; ]%);
        Position dest = (Position) current.add((Path)v).getCanonicalPath();
        w.write(%[
            @getNodeFromPos(current)@ -> @getNodeFromPos(dest)@; ]%);
      } else {
        Position current = getEnvironment().getPosition();
        String[] tab = `v.getClass().getName().split("\\.");
        String name = tab[tab.length-1];
        tab = name.split("\\$");
        name = tab[tab.length-1];
        w.write(%[
            @getNodeFromPos(current)@ [label="@name@"]; ]%);
        if(!current.equals(new Position(new int[]{}))) {
          Position father = current.up();
          w.write(%[
              @getNodeFromPos(father)@ -> @getNodeFromPos(current)@; ]%);
        }
      }
      } catch(IOException e) {}
      return Environment.SUCCESS;
    }
  }

  public static void display(Visitable vv) {
    final Visitable v = vv;
    javax.swing.SwingUtilities.invokeLater(
        new Runnable() { public void run() { 
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Viewer");
        try {
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("dot");
        Writer out = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
        Viewer.toDot(v);
        Viewer.toDot(v, out);
        out.close();
        Parser parser = new Parser(pr.getInputStream());
        parser.parse();
        Graph graph = parser.getGraph();
        GrappaPanel panel = new GrappaPanel(graph);
        frame.getContentPane().add(panel, java.awt.BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        } catch (Exception e) {
        e.printStackTrace();
        }
        }});
  }

  /* -------- pstree-like part --------- */
  public static void toTree(tom.library.sl.Visitable v) throws IOException {
    Writer w = new BufferedWriter(new OutputStreamWriter(System.out)); 
    toTree(v,w);
    w.write('\n');
    w.flush();
  }


  public static void toTree(tom.library.sl.Visitable v, Writer w)
    throws java.io.IOException {
      aterm.ATermFactory atermFactory = new PureFactory();
      aterm.ATerm at = atermFactory.parse(v.toString());
      ATermToTree(at, w, new Stack<Integer>(), 0);
    }

  private static void writeContext(Writer w, Stack<Integer> context, int deep) 
    throws java.io.IOException {
      for(int i=0; i<deep; i++) {
        if (context.contains(i))
          w.write('\u2502');
        else
          w.write(' ');
      }
    }

  private static void ATermToTree(aterm.ATerm term, Writer w, Stack<Integer> context, int deep) 
    throws java.io.IOException {
      %match(term) {
        ATermAppl(AFun[name=name],list) -> {
          aterm.ATermAppl a = (aterm.ATermAppl) term;
          if (`a.getArity() == 0) {  // no child
            w.write("\u2500"+`name);
            return;
          } else if (`a.getArity() == 1) {  // only one child
            w.write('\u2500' + `name + "\u2500\u2500");
            deep = deep + `name.length() + 3;
            ATermToTree(`list.getFirst(),w,context,deep);
            return;
          } else {
            int ndeep = deep + `name.length() + 3;
            %match (ATermList `list) {
              (first,l*,last) -> {
                // first child
                w.write('\u2500' + `name + "\u2500\u252C");
                context.push(ndeep-1); 
                ATermToTree(`first,w,context,ndeep);
                context.pop();
                w.write('\n');

                // 2 ... n-1
                %match (ATermList l) {
                  (_*,c,_*) -> {
                    writeContext(w,context,ndeep-1);
                    w.write("\u251C");
                    context.push(ndeep-1);
                    ATermToTree(`c,w,context,ndeep);
                    context.pop();
                    w.write('\n');
                  }
                }
                // last child
                writeContext(w,context,ndeep-1);
                w.write("\u2514");
                ATermToTree(`last,w,context,ndeep);
              }
            }
          }
        }
      }
    }


  /* -------- strategy part --------- */
  private static int counter = 0;  

  static private String clean(String s) {
    s = s.replace('.','_');
    s = s.replace('$','_');
    s = s.replace('@','_');
    return s;
  }

  private static void 
    toDot(Strategy subj, Writer w) throws IOException {
      w.write("digraph G { graph [ordering=out];");
      %match (Strategy subj) {
        y@MuVar[] -> { return; }

        x@Mu[] ->{  ((Mu)`x).muExpand(); } // to mu-expand

        x -> { 

          // get display string for the node
          String[] tab = `x.getClass().getName().split("\\.");
          String name = tab[tab.length-1];
          tab = name.split("\\$");
          name = tab[tab.length-1];
          String idNode = `clean(x.toString());

          w.write(%[@idNode@ [label="@name@"];]%);
          w.write("\n");  

          int n = `x.getChildCount();
          for(int i=0; i<n; i++) {
            Visitable s = `x.getChildAt(i);
            %match(Strategy s) {
              y@MuVar[var=varName] -> {
                Strategy pointer = (Strategy) `((MuVar)y).getInstance();
                if (pointer == null) return;
                String idMu = clean(pointer.toString());
                if (pointer.getChildCount() > 0 && pointer.getChildAt(0) != `y) {
                  String idMuVar = idMu + "_" + (counter++);
                  w.write(%[
                      @idMuVar@ [label="@`varName@"]; 
                      @idNode@ -> @idMuVar@;
                      @idMuVar@ -> @idMu@;
                      ]%);
                }
                continue;
              }
              y -> {
                w.write(%[@idNode@ -> @clean(`y.toString())@;]%);
                w.write("\n");
                toDot(`y,w);
              }
            } // match
          } // loop over the sons
          w.flush();
        }
      }
      w.write("}\n");
    }

}
