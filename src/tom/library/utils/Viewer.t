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
package tom.library.utils;

import java.io.*;
import tom.library.sl.*;
import java.util.Stack;
import java.util.List;
import aterm.pure.PureFactory;
import att.grappa.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Provide tools to view terms, with a graphical browser or with GraphViz.
 */
public class Viewer {

  %include { ../mapping/java/sl.tom }
  %include { ../mapping/java/sl/visitable.tom }
  %include { ../mapping/java/aterm.tom }

  /* -------- dot part --------- */
  /**
   * Give a dot representation of a visitable term on the writer stream
   *
   * @param v the visitable term to visualize
   * @param w the writer stream
   * @throws RuntimeException in case of visit failure
   */
  public static void toDot(tom.library.sl.Visitable v, Writer w)
    throws java.io.IOException {
      if ( v instanceof tom.library.sl.Strategy ) {
        Strategy subj = (tom.library.sl.Strategy) v;
        Mu.expand(subj);
        try{
          subj = (Strategy) `TopDownCollect(RemoveMu()).visit(subj);
          w.write("digraph strategy {\nordering=out;");
          Strategy print = new PrintStrategy(w);
          `TopDownCollect(print).visit(subj);
        } catch (VisitFailure e) {throw new RuntimeException("unexcepted visit failure");}
        w.write("\n}");
        w.flush();
      } else {
        w.write("digraph visitable {\nordering=out;");
        try{
          Strategy print = new Print(w);
          `TopDown(print).visit(v);
        } catch (VisitFailure e) {throw new RuntimeException("unexcepted visit failure");}
        w.write("\n}");
        w.flush();
      }
    }

 /**
  * Give a dot representation of a visitable term on the standard output stream
  *
  * @param v the visitable term to visualize
  * @throws RuntimeException in case of visit failure
  */
 public static void toDot(tom.library.sl.Visitable v) {
    try {
      Writer w = new BufferedWriter(new OutputStreamWriter(System.out));
      toDot(v,w);
      w.write('\n');
      w.flush();
    } catch(java.io.IOException e) {}
  }

  %typeterm Writer{
    implement {Writer}
  }

  /**
   * Return a term node from a specified position
   *
   * @param p position of the node to return
   * @return a string representation of the node at the specified position
   */
  private static String getNodeFromPos(Position p) {
    int[] omega = p.toIntArray();
    StringBuilder r = new StringBuilder("p");
    for(int i=0 ; i<p.length() ; i++) {
      r.append(omega[i]);
      if(i<p.length()-1) {
        r.append("_");
      }
    }
    return r.toString();
  }

  //TODO: adapt to traverse any data-structure using newsl
  static private class Print extends AbstractStrategyBasic {

    protected Writer w;

    public Print(Writer w) {
      super(( new tom.library.sl.Identity() ));
      this.w=w;
    }

    /**
     * Visits the subject any in a light way by providing the introspector
     *
     * @param any the subject to visit
     * @param i the introspector
     * @throws VisitFailure if visitLight fails
     */
    public Object visitLight(Object any, Introspector i) throws VisitFailure {
      throw new VisitFailure();
    }

    /**
     * Visit the subject by providing an introspector
     *
     * @param introspector the introspector
     */
    public int visit(Introspector introspector) {
      Visitable v = (Visitable) getEnvironment().getSubject();
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
          String term = `v.toString();
          // in case of wrapped builtin return term in complete
          // else return the name of the constructor
          int end = term.indexOf("(");
          String name = term.substring(0,(end==-1)?term.length():end);
          w.write(%[
              @getNodeFromPos(current)@ [label="@name@"]; ]%);
          if(!current.equals(Position.make())) {
            Position father = current.up();
            w.write(%[
                @getNodeFromPos(father)@ -> @getNodeFromPos(current)@; ]%);
          }
        }
      } catch(IOException e) {}
      return Environment.SUCCESS;
    }
  }

  /**
   * Give a GUI display to visualize a visitable term
   *
   * @param vv the visitable term to visualize
   */
  public static void display(Visitable vv) {
    final Visitable v = vv;
    JFrame.setDefaultLookAndFeelDecorated(true);
    JFrame frame = new JFrame("Viewer");
    frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
        synchronized(v){  v.notify(); }}});
    try {
      Runtime rt = Runtime.getRuntime();
      Process pr = rt.exec("dot");
      Writer out = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
      Viewer.toDot(v, out);
      out.close();
      Parser parser = new Parser(pr.getInputStream());
      parser.parse();
      Graph graph = parser.getGraph();
      GrappaPanel panel = new GrappaPanel(graph);
      panel.setScaleToFit(true);
      frame.getContentPane().add(panel, java.awt.BorderLayout.CENTER);
      frame.pack();
      frame.setVisible(true);
      synchronized(v){
        v.wait();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /* -------- pstree-like part --------- */
  /**
   * Give a pstree-like representation of a visitable term on the standard output stream
   *
   * @param v the visitable term to visualize
   * @throws RuntimeException in case of visit failure
   */
  public static void toTree(tom.library.sl.Visitable v) {
    try {
      Writer w = new BufferedWriter(new OutputStreamWriter(System.out));
      toTree(v,w);
      w.write('\n');
      w.flush();
    } catch(java.io.IOException e) {}
  }

  /**
   * Give a pstree-like representation of a visitable term on the writer stream
   *
   * @param v the visitable term to visualize
   * @param w the writer stream
   * @throws RuntimeException in case of visit failure
   */
  public static void toTree(tom.library.sl.Visitable v, Writer w)
    throws java.io.IOException {
      aterm.ATermFactory atermFactory = new PureFactory();
      aterm.ATerm at = atermFactory.parse(v.toString());
      ATermToTree(at, w, new Stack<Integer>(), 0);
    }

  private static void writeContext(Writer w, Stack<Integer> context, int deep)
    throws java.io.IOException {
      for(int i=0; i<deep; i++) {
        if (context.contains(i)) {
          w.write("│");
        } else {
          w.write(' ');
        }
      }
    }

  /**
   * Return a tree representation of an ATerm, by providing a writer, a context and depth
   *
   * @param term the ATerm to represent as a tree
   * @param w the writer
   * @param context the context
   * @param deep the depth
   */
  private static void ATermToTree(aterm.ATerm term, Writer w, Stack<Integer> context, int deep)
    throws java.io.IOException {
      %match(term) {
        ATermInt(value) -> {
          w.write("─"+`value);
          return;
        }
        ATermAppl(AFun[name=afunname,quoted=quoted],list) -> {
          String name = (`quoted)?"\""+`afunname+"\"":`afunname;
          aterm.ATermAppl a = (aterm.ATermAppl) term;
          if (`a.getArity() == 0) {  // no child
            w.write("─"+name);
            return;
          } else if (`a.getArity() == 1) {  // only one child
            w.write("─" + name + "──");
            deep = deep + name.length() + 3;
            ATermToTree(`list.getFirst(),w,context,deep);
            return;
          } else {
            int ndeep = deep + name.length() + 3;
            %match (ATermList list) {
              concATerm(first,l*,last) -> {
                // first child
                w.write("─" + name + "─┬");
                context.push(ndeep-1);
                ATermToTree(`first,w,context,ndeep);
                context.pop();
                w.write('\n');

                // 2 ... n-1
                %match (ATermList l) {
                  concATerm(_*,c,_*) -> {
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


  /* -------- strategy part --------- */
  private static int counter = 0;

  /**
   * Replaces characters '.' '$' and '@' by '_' in a string
   *
   * @param s the string to process
   * @return a string where '.' '$' and '@' by '_' have been replaced
   */
  static private String clean(String s) {
    s = s.replace('.','_');
    s = s.replace('$','_');
    s = s.replace('@','_');
    return s;
  }

  %strategy RemoveMu() extends Identity() {
    visit Strategy {
      Mu[s2=strat] -> {
        return `strat;
      }
      _ -> {
        if (getEnvironment().getCurrentStack().contains(getEnvironment().getSubject())) {
          //corresponds to a pointer due to MuVar
          throw new VisitFailure();
        }
      }
    }
  }

  static private class PrintStrategy extends AbstractStrategyBasic {

    protected Writer w;

    //TODO: adapt with newsl to visit anu data-structures
    public PrintStrategy(Writer w) {
      super(( new tom.library.sl.Identity() ));
      this.w=w;
    }

    /**
     * Visits the subject any in a light way by providing the introspector
     *
     * @param any the subject to visit
     * @param i the introspector
     * @throws VisitFailure if visitLight fails
     */
    public Object visitLight(Object any, Introspector i) throws VisitFailure {
      throw new VisitFailure();
    }

    /**
     * Visit the subject by providing an introspector
     *
     * @param introspector the introspector
     */
    public int visit(Introspector introspector) {
      Visitable v = (Visitable) getEnvironment().getSubject();
      Position current = getEnvironment().getPosition();
      List<Object> stack = getEnvironment().getCurrentStack();
      try {
        //test if it is a pointer due to an expanded MuVar
        if (stack.contains(v)) {
          int index = stack.indexOf(v);
          Position dest = (Position) current.clone();
          for(int i=current.length();i>index;i--) {
            dest = dest.up();
          }
          Position father = current.up();
          w.write(%[
              @getNodeFromPos(father)@ -> @getNodeFromPos(dest)@; ]%);
          //fails to prevent loops
          return Environment.FAILURE;
        }
        else {
          String[] tab = `v.getClass().getName().split("\\.");
          String name = tab[tab.length-1];
          tab = name.split("\\$");
          name = tab[tab.length-1];
          w.write(%[
              @getNodeFromPos(current)@ [label="@name@"]; ]%);
          if(stack.size()!=0) {
            Position father = current.up();
            w.write(%[
                @getNodeFromPos(father)@ -> @getNodeFromPos(current)@; ]%);
          }
        }
      } catch(java.io.IOException e) {
        return Environment.FAILURE;
      }
      return Environment.SUCCESS;
    }

  }

}
