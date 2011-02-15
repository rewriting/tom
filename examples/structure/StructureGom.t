/*
 * Copyright (c) 2004-2011, INPL, INRIA
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

package structure;

import java.io.*;
import java.util.*;

import structure.structures.*;
import structure.structures.types.*;

import tom.library.sl.Strategy;
import tom.library.sl.VisitFailure;

import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.ANTLRStringStream;

public class StructureGom {

  private final static boolean optim = true;
  private final static boolean optim2 = true;
  private final static boolean optim3 = true;

  %include { structures/Structures.tom }
  %include { sl.tom }
  %include { util/types/HashSet.tom }

  %typeterm StrucCollection {
    implement     { java.util.Collection<Struc> }
    is_sort(t)    { $t instanceof java.util.Collection }
    equals(l1,l2) { $l1.equals($l2) }
  }

  StructureGom() { }

  public void run(Struc initStruc) {
		//initStruc = `Par(ConcPar(Cop(ConcCop(Seq(ConcSeq(D1(),D2())),A())), Seq(ConcSeq(Par(ConcPar(Neg(D1()),Neg(B()))),Neg(D2()))),Seq(ConcSeq(B(),Neg(A())))));
    System.out.println("Starting with: " + prettyPrint(initStruc));
    //System.out.println("length = " + length(initStruc));
    //System.out.println("pairs  = " + numberOfPair(initStruc));

    long startChrono = System.currentTimeMillis();
    boolean res      = localSearch(initStruc, `O());
    //boolean res      = proofSearch(initStruc, `O());
    long stopChrono  = System.currentTimeMillis();

    if (!res) {
      System.out.println("the input is false");
    }

    System.out.println("proof = "+res+" in "+(stopChrono-startChrono)+" ms");
  }

  static final int MAXITER = 25;
  public boolean proofSearch(Struc start, Struc end) {
    HashSet<Struc> result = new HashSet<Struc>();
    Collection<Struc> c1 = new HashSet<Struc>();
    c1.add(start);

    for(int i=1 ; i<MAXITER ; i++) {
      Collection<Struc> c2 = new HashSet<Struc>();
      Iterator it = c1.iterator();
      while(it.hasNext()) {
        Struc item = (Struc) it.next();
        collectOneStep(c2,item);
        result.add(item);
      }

      System.out.print("iteration " + i + ":");
      System.out.print("\tc2.size = " + c2.size());
      c2.removeAll(result);
      System.out.print("\tc2'.size = " + c2.size());
      System.out.print("\tresult.size = " + result.size());
      System.out.println();
      c1 = c2;

      if(c1.isEmpty()) {
        return false;
      } else if(c1.contains(end)) {
        return true;
      }
    }
    return false;
  }

  private final static int MAXLOCALITER = 1000000;
  private final static int step = 1000;
    Comparator<Struc> comparator = new Comparator<Struc>() {
        public int compare(Struc s1, Struc s2) {
          if (s1==s2) {
            return 0;
          }

          int v1 = weight(s1);
          int v2 = weight(s2);
          if (v1<v2) {
            return -1;
          } else if (v1>v2) {
            return 1;
          } else {
            int res = s1.compareToLPO(s2);
            if(res == 0) {
              System.out.println("wrong order");
              System.out.println("s1 = " + s1);
              System.out.println("s2 = " + s2);
              System.exit(0);
            }
            return res;
          }
        }
      };
  public boolean localSolve(Struc start) {
    return localSearch(start, `O());
  }

  public boolean localSearch(Struc start, Struc end) {
    TreeSet<Struc> c1 = new TreeSet<Struc>(comparator);
    c1.add(start);

    HashSet<Struc> result = new HashSet<Struc>();
    long i=0;
    int nc2 = 0;
    int nc1 = 0;
    int nremoved = 0;
    int weight = 0;
    while (true) {
      i++;

      Struc subject = (Struc) c1.first();
      c1.remove(subject);
      result.add(subject);

      weight += weight(subject);
      weightMap.remove(subject); // memory optimization

      HashSet<Struc> c2 = new HashSet<Struc>();
      collectOneStep(c2,subject);
      nc2 = nc2 + c2.size();

      for (Iterator it=c2.iterator() ; it.hasNext() ; ) {
        Struc elt = (Struc) it.next();
        if (true || provable(elt)) {
          if (result.contains(elt)) {
            nremoved++;
          } else {
            if (!c1.contains(elt)) {
              c1.add(elt);
              nc1++;
            }
          }
        }
      }

      if (i%step == 0) {
        System.out.print("iteration " + i + ":");
        System.out.print("\t#c1 = " + c1.size());
        System.out.print("\t#weight call = " + weightCall);
        System.out.print("\t#weight = " + weightMap.size());
        System.out.print("\t~weight = " + (int)(weight/step));
        System.out.println();
        System.out.print("\t#deduced = " + nc2);
        System.out.print("\t#removed by history = " + nremoved);
        System.out.print("\t#added to c1 = " + nc1);
        System.out.println();
        nc2 = 0;
        nc1 = 0;
        nremoved = 0;
        weight = 0;
        if(weightMap.size() > 200000) {
          weightMap.clear();
        }
      }

      if (c1.contains(end)) {
        return true;
      } else if (c1.isEmpty()) {
        return false;
      }
    }
  }

  public List<String> testOneStep(String input) {
    Struc initStruc = strucFromPretty(input);
    Collection<Struc> col = new TreeSet<Struc>(comparator);
    collectOneStep(col,initStruc);

    List<String> result = new ArrayList<String>(col.size());
    Iterator it = col.iterator();
    while(it.hasNext()) {
      Struc item = (Struc) it.next();
      result.add(prettyPrint(item));
    }
    return result;
  }

  private static boolean canonical = true;

  public static void main(String[] args) {
    StructureGom test = new StructureGom();

    Struc query= null;
    try {
      while (true) {
        System.out.println("Enter a structure:");
        StructuresLexer lexer =
          new StructuresLexer(
            new ANTLRInputStream(System.in));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        StructuresParser parser = new StructuresParser(tokens);
        Tree t = (Tree) parser.new_struc().getTree();
        query = (Struc) StructuresAdaptor.getTerm(t);
        test.run(query);
      }
    } catch (Exception e) {
      System.out.println("Exiting because " + e);
      e.printStackTrace();
    }
  }

  public Struc strucFromPretty(String s) {
    Struc query= null;
    try {
      StructuresLexer lexer = new StructuresLexer(new ANTLRStringStream(s));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      StructuresParser parser = new StructuresParser(tokens);
      Tree t = (Tree) parser.new_struc().getTree();
      query = (Struc) StructuresAdaptor.getTerm(t);
      return query;
    } catch (Exception e) {
      System.out.println("Exiting because " + e);
      e.printStackTrace();
    }
    return query;
  }

  /*
   * Apply a function to each subterm of a term
   * and collect all possible results in a collection
   */
  public void collectOneStep(final Collection<Struc> collection, Struc subject) {
    try {
      `BottomUp(OneStep(subject,collection)).visit(subject);
    } catch (VisitFailure e) {
      System.out.println("Failed to get successors " + subject);
    }
  }

  %strategy OneStep(subject:Struc,c:StrucCollection) extends `Identity() {
    visit Struc {
      /* [(R,T),U] -> ([R,U],T)
         [(R,T),U] -> ([T,U],R) */
      Par(ConcPar(X1*,Cop(ConcCop(R*,T*)),X2*,U,X3*)) -> {

        if (`T*.isEmptyConcCop() || `R*.isEmptyConcCop() ) {
        } else {
          StrucPar context = `ConcPar(X1*,X2*,X3*);

          if (!optim3 || !canReact(`T,`U)) {
            if (!optim2 || canReact(`R,`U)) {
              StrucPar parR = cop2par(`R*);
              Struc elt1 = `Par(ConcPar(Cop(ConcCop(Par(ConcPar(parR*,U)),T*)),context*));
              c.add(getEnvironment().getPosition().getReplace(elt1).visitLight(subject));
            }
          }
          if (!optim3 || !canReact(`R,`U)) {
            if (!optim2 || canReact(`T,`U)) {
              StrucPar parT = cop2par(`T*);
              Struc elt2 = `Par(ConcPar(Cop(ConcCop(Par(ConcPar(parT*,U)),R*)),context*));
              c.add(getEnvironment().getPosition().getReplace(elt2).visitLight(subject));
            }
          }
        }
      }

      /* [U,(R,T)] -> ([R,U],T)
         [U,(R,T)] -> ([T,U],R) */
      Par(ConcPar(X1*,U,X2*,Cop(ConcCop(R*,T*)),X3*)) -> {
        if (`T*.isEmptyConcCop() || `R*.isEmptyConcCop()) {

        } else {
          StrucPar context = `ConcPar(X1*,X2*,X3*);

          if (!optim3 || !canReact(`T,`U)) {
            if (!optim2 || canReact(`R,`U)) {
              StrucPar parR = cop2par(`R*);
              Struc elt3 = `Par(ConcPar(Cop(ConcCop(Par(ConcPar(parR*,U)),T*)),context*));
              c.add(getEnvironment().getPosition().getReplace(elt3).visitLight(subject));
            }
          }

          if (!optim3 || !canReact(`R,`U)) {
            if (!optim2 || canReact(`T,`U)) {
              StrucPar parT = cop2par(`T*);
              Struc elt4 = `Par(ConcPar(Cop(ConcCop(Par(ConcPar(parT*,U)),R*)),context*));
              c.add(getEnvironment().getPosition().getReplace(elt4).visitLight(subject));
            }
          }
        }
      }

        /* [R,T] -> <R;T>
           [R,T] -> <T;R> */
        Par(ConcPar(X1*,R,X2*,T,X3*)) -> {
          StrucPar context = `ConcPar(X1*,X2*,X3*);
          c.add(getEnvironment().getPosition().getReplace(
                `Par(ConcPar(Seq(ConcSeq(R,T)),context*))).visitLight(subject));
          c.add(getEnvironment().getPosition().getReplace(
                `Par(ConcPar(Seq(ConcSeq(T,R)),context*))).visitLight(subject));
        }

        /* [<R;U>,<T;V>] -> <[R,T];[U,V]>
           [<R;U>,<T;V>] -> <[U,V];[R,T]> */
        Par(ConcPar(X1*,Seq(ConcSeq(R*,U*)),X2*,Seq(ConcSeq(T*,V*)),X3*)) -> {
          if (`R*.isEmptyConcSeq() || `U*.isEmptyConcSeq()
              || `T*.isEmptyConcSeq() || `V*.isEmptyConcSeq()) {
          } else {
            if (!optim || (!canReact(`U,`T) && !canReact(`R,`V))) {
              if (!optim2 || (canReact(`R,`T) && canReact(`U,`V))) {
                //System.out.println("U = " + `U + "\tT = " + `T);
                //System.out.println("R = " + `R + "\tV = " + `V);
                StrucPar context = `ConcPar(X1*,X2*,X3*);
                StrucPar parR = seq2par(`R*);
                StrucPar parU = seq2par(`U*);
                StrucPar parT = seq2par(`T*);
                StrucPar parV = seq2par(`V*);
                c.add(getEnvironment().getPosition().getReplace(
                      `Par(ConcPar(Seq(ConcSeq(Par(ConcPar(parR*,parT*)),Par(ConcPar(parU*,parV*)))),context*))
                      ).visitLight(subject));
                c.add(getEnvironment().getPosition().getReplace(
                      `Par(ConcPar(Seq(ConcSeq(Par(ConcPar(parU*,parV*)),Par(ConcPar(parR*,parT*)))),context*))
                      ).visitLight(subject));
              }
            }
          }
        }

        /* [R,<T;U>] -> <[R,T];U>
           [R,<T;U>] -> <T;[R,U]> */
        Par(ConcPar(X1*,R,X2*,Seq(ConcSeq(T*,U*)),X3*)) -> {
          if (`T*.isEmptyConcSeq() || `U*.isEmptyConcSeq()) {
          } else {
            StrucPar context = `ConcPar(X1*,X2*,X3*);
            if (!optim2 || canReact(`R,`T)) {
              StrucPar parT = seq2par(`T*);
              c.add(getEnvironment().getPosition().getReplace(
                    `Par(ConcPar(Seq(ConcSeq(Par(ConcPar(R,parT*)),U*)),context*))
                    ).visitLight(subject));
            }

            if (!optim2 || canReact(`R,`U)) {
              StrucPar parU = seq2par(`U*);
              c.add(getEnvironment().getPosition().getReplace(
                    `Par(ConcPar(Seq(ConcSeq(T*,Par(ConcPar(R,parU*)))),context*))
                    ).visitLight(subject));
            }
          }
        }

        /* [<T;U>,R] -> <[R,T];U>
           [<T;U>,R] -> <T;[R,U]> */
        Par(ConcPar(X1*,Seq(ConcSeq(T*,U*)),X2*,R,X3*)) -> {
          if(`T*.isEmptyConcSeq() || `U*.isEmptyConcSeq()) {
          } else {
            StrucPar context = `ConcPar(X1*,X2*,X3*);

            if (!optim2 || canReact(`R,`T)) {
              StrucPar parT = seq2par(`T*);
              c.add(getEnvironment().getPosition().getReplace(
                    `Par(ConcPar(Seq(ConcSeq(Par(ConcPar(R,parT*)),U*)),context*))
                    ).visitLight(subject));
            }

            if (!optim2 || canReact(`R,`U)) {
              StrucPar parU = seq2par(`U*);
              c.add(getEnvironment().getPosition().getReplace(
                    `Par(ConcPar(Seq(ConcSeq(T*,Par(ConcPar(R,parU*)))),context*))
                    ).visitLight(subject));
            }
          }
        }

        /* [X,-X] -> o */
        Par(ConcPar(X1*,x,X2*,Neg(x),X3*)) -> {
          Struc elt5 = `Par(ConcPar(X1*,X2*,X3*));
          c.add(getEnvironment().getPosition().getReplace(elt5).visitLight(subject));
        }
        /* [-X,X] -> o */
        Par(ConcPar(X1*,Neg(x),X2*,x,X3*)) -> {
          Struc elt6 = `Par(ConcPar(X1*,X2*,X3*));
          c.add(getEnvironment().getPosition().getReplace(elt6).visitLight(subject));
        }
      }
    }

  //private WeakHashMap lengthCache = new WeakHashMap();
  public static int length(StructuresAbstractType t) {

    //if(lengthCache.containsKey(t)) {
    //return ((Integer)lengthCache.get(t)).intValue();
    //}

    int res = 0;
    block: {
      %match(Struc t) {
        Cop(l) -> { res =  length(`l); break block; }
        Par(l) -> { res =  length(`l); break block; }
        Seq(l) -> { res =  2+length(`l); break block; }
        _      -> { res =  1; break block; }
      }
      %match(StrucPar t) {
        ConcPar()           -> { res =  0; break block; }
        ConcPar(head,tail*) -> { res =  length(`head) + length(`tail*); break block; }
      }
      %match(StrucCop t) {
        ConcCop()           -> { res =  0; break block; }
        ConcCop(head,tail*) -> { res =  length(`head) + length(`tail*); break block; }
      }
      %match(StrucSeq t) {
        ConcSeq()           -> { res =  0; break block; }
        ConcSeq(head,tail*) -> { res =  length(`head) + length(`tail*); break block; }
      }
    }
    //lengthCache.put(t,new Integer(res));

    return res;
  }

  private static WeakHashMap<StructuresAbstractType,Integer> weightMap =
    new WeakHashMap<StructuresAbstractType,Integer>();
	private static int weightCall = 0;
  public static int weight(StructuresAbstractType subject) {
		weightCall++;
    if (weightMap.containsKey(subject)) {
      return weightMap.get(subject);
    }
    //double l = (double)length(subject);
    //double n = (double)numberOfPair(subject);
    int l = length(subject);
    int n = numberOfPair(subject);
    double w = (l*l)/(1.0+n);
    // System.out.println(prettyPrint(subject));
    // System.out.println("l = " + l + "\t#pair = " + n + "\tw = " + w);
    weightMap.put(subject, (int)w);
    return (int)w;
  }

  %typeterm MutableInt {
    implement      { MutableInt }
    is_sort(t)     { $t instanceof MutableInt }
    equals(l1,l2)  { $l1.equals($l2) }
  }
  private static class MutableInt {
    private int value = 0;
    MutableInt(int val) {
      value = val;
    }
    int getValue() {
      return value;
    }
    void increment() {
      value++;
    }
  }
  public static int numberOfPair(StructuresAbstractType subject) {
    final MutableInt count = new MutableInt(0);
    try {
      `BottomUp(CountPairs(count)).visitLight(subject);
    } catch (VisitFailure e) {
      System.out.println("Failed to count pairs" + subject);
    }
    return count.getValue();
  }

  %strategy CountPairs(count:MutableInt) extends `Identity() {
    visit Struc {
      Par(ConcPar(_*,x,_*,Neg(x),_*)) -> {
        count.increment();
      }
      Cop(ConcCop(_*,x,_*,Neg(x),_*)) -> {
        count.increment();
      }
    }
  }

  /*
   * by default a formula is provable
   * but we can detect some un-provable formula
   * here, we check that any positive atom also appears as a negative atom
   */
  private static boolean provable(Struc subject) {
    final HashSet positive = new HashSet();
    final HashSet negative = new HashSet();
    collectAtom(subject, positive,negative);

    if (subject == `O()) {
      return true;
    } else if (positive.size() != negative.size()) {
      //System.out.print("#pos = " + positive.size());
      //System.out.println("\t#neg = " + negative.size());
      return false;
    } else {
      for (Iterator it = negative.iterator(); it.hasNext() ; ) {
        Struc s = (Struc)it.next();
        if (!positive.contains(s)) {
          //System.out.println("no " + `s + " in " + positive);
          return false;
        }
      }
    }

    return true;
  }

  /*
   * we say that 2 structures can react if t1 \cap -t2 \neq empty
   */
  private static boolean canReact(StructuresAbstractType t1, StructuresAbstractType t2) {
    final HashSet atomT1 = new HashSet();
    final HashSet atomT2 = new HashSet();
    collectAtom(t1, atomT1, atomT1);
    collectAtom(t2, atomT2, atomT2);

    for (Iterator it = atomT1.iterator() ; it.hasNext() ; ) {
      if (atomT2.contains(it.next())) {
        return true;
      }
    }
    return false;
  }


  private static void collectAtom(StructuresAbstractType subject, final HashSet positive, final HashSet negative) {
    try {
      `BottomUp(FindAtoms(positive,negative)).visitLight(subject);
    } catch (VisitFailure e) {
      System.out.println("Failed to get atoms" + subject);
    }
  }
  %strategy FindAtoms(positive:HashSet,negative:HashSet) extends `Identity() {
    visit Struc {
      s@Cop(_) -> { return `s; }
      s@Par(_) -> { return `s; }
      s@Seq(_) -> { return `s; }
      Neg(s) -> { negative.add(`s); return `s; }
      s -> { positive.add(`s); return `s; }
    }
  }

  /* conversion functions */
  static StrucPar cop2par(StrucCop l) {
    %match(StrucCop l) {
      ConcCop() -> { return `ConcPar(); }
      ConcCop(head,tail*) -> {
        StrucPar ptail = cop2par(`tail*);
        return `ConcPar(head,ptail*);
      }
    }
    return null;
  }

  static StrucPar seq2par(StrucSeq l) {
    %match(StrucSeq l) {
      ConcSeq() -> { return `ConcPar(); }
      ConcSeq(head,tail*) -> {
        StrucPar ptail = seq2par(`tail*);
        return `ConcPar(head,ptail*);
      }
    }
    return null;
  }

  public String prettyPrint(StructuresAbstractType t) {
    %match(Struc t) {
      Neg(s)  -> { return "-" + prettyPrint(`s);}
      Par(l)  -> { return "[" + prettyPrint(`l) + "]";}
      Cop(l)  -> { return "(" + prettyPrint(`l) + ")";}
      Seq(l)  -> { return "<" + prettyPrint(`l) + ">";}
      Atom(n) -> { return `n;}
      O()     -> { return "o";}
    }
    %match(StrucCop t) {
      ConcCop(head) -> {
        return prettyPrint(`head);
      }
      ConcCop(head,tail*) -> {
        return prettyPrint(`head) + "," + prettyPrint(`tail*);
      }
    }
    %match(StrucPar t) {
      ConcPar(head) -> {
        return prettyPrint(`head);
      }
      ConcPar(head,tail*) -> {
        return prettyPrint(`head) + "," + prettyPrint(`tail*);
      }
    }
    %match(StrucSeq t) {
      ConcSeq(head) -> {
        return prettyPrint(`head);
      }
      ConcSeq(head,tail*) -> {
        return prettyPrint(`head) + ";" + prettyPrint(`tail*);
      }
    }
    return t.toString();
  }
}
