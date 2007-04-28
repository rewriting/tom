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

package structure;

import java.io.*;
import java.util.*;

import structure.structures.*;
import structure.structures.types.*;

import jjtraveler.VisitFailure;
import tom.library.sl.Strategy;
import tom.library.sl.FireException;

public class StructureGom {

  private final static boolean optim = true;
  private final static boolean optim2 = true;
  private final static boolean optim3 = true;

  %include { structures/Structures.tom }
  %include { sl.tom }
  %include { util/types/Collection.tom }
  %include { util/types/HashSet.tom }

  StructureGom() { }
  
  public void run(Struc initStruc) {
		//initStruc = `par(concPar(cop(concCop(seq(concSeq(d1(),d2())),a())), seq(concSeq(par(concPar(neg(d1()),neg(b()))),neg(d2()))),seq(concSeq(b(),neg(a())))));
    System.out.println("Starting with: " + prettyPrint(initStruc));
    //System.out.println("length = " + length(initStruc));
    //System.out.println("pairs  = " + numberOfPair(initStruc));

    long startChrono = System.currentTimeMillis();
    boolean res      = localSearch(initStruc, `o());
    //boolean res      = proofSearch(initStruc, `o());
    long stopChrono  = System.currentTimeMillis();

    System.out.println("proof = "+res+" in "+(stopChrono-startChrono)+" ms");
  }

  static final int MAXITER = 25;
  public boolean proofSearch(Struc start, Struc end) {
    HashSet result = new HashSet();
    Collection c1 = new HashSet();
    c1.add(start);

    for(int i=1 ; i<MAXITER ; i++) {
      Collection c2 = new HashSet();
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
    Comparator comparator = new Comparator() {
        public int compare(Object o1, Object o2) {
          if(o1==o2) {
            return 0;
          }

          Struc s1 = (Struc)o1;
          Struc s2 = (Struc)o2;
          int v1 = weight(s1);
          int v2 = weight(s2);
          if(v1<v2) {
            return -1;
          } else if(v1>v2) {
            return 1;
          } else {
            int res = s1.compareToLPO(s2);
            //int res = s1.compareTo(s2);
            if(res == 0) {
              System.out.println("wrong order");
              System.out.println("s1 = " + s1);
              System.out.println("s2 = " + s2);
              System.exit(0);
            } 
/*
              System.out.println("s1 = " + s1);
              System.out.println("s2 = " + s2);
              System.out.println("res = " + res);
*/
            return res;
          }
        }
      };
  public boolean localSearch(Struc start, Struc end) {
    TreeSet c1 = new TreeSet(comparator);
    c1.add(start);

    HashSet result = new HashSet();
    long i=0;
    int nc2 = 0;
    int nc1 = 0;
    int nremoved = 0;
    int weight = 0;
    while(true) {
      i++;

      Struc subject = (Struc) c1.first();
      c1.remove(subject);
      result.add(subject);
     
      weight += weight(subject);
      weightMap.remove(subject); // memory optimization

      HashSet c2 = new HashSet();
      collectOneStep(c2,subject);
      nc2 = nc2 + c2.size();

      for(Iterator it=c2.iterator() ; it.hasNext() ; ) {
        Struc elt = (Struc) it.next();
        if(true || provable(elt)) {
          if(result.contains(elt)) {
            nremoved++;
          } else {
            if(!c1.contains(elt)) {
              c1.add(elt);
              nc1++;
            }
          }
        }
      }

      if(i%step == 0) {
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

      // System.out.println("------------------------------------------------------------");

      if(c1.contains(end)) {
        return true;
      } else if(c1.isEmpty()) {
        System.out.println("the input is false");
        return false;
      }
    }
    //System.out.println("proof not found");
    //return false; 
  }

  public List testOneStep(String input) {
    Struc initStruc = strucFromPretty(input);
    Collection col = new TreeSet(comparator);
    collectOneStep(col,initStruc);

    List result = new ArrayList(col.size());
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
      while(true) {
        System.out.println("Enter a structure:");
        StructuresLexer lexer = new StructuresLexer(new DataInputStream(System.in));
        StructuresParser parser = new StructuresParser(lexer);
        query = parser.struc();
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
      StructuresLexer lexer = new StructuresLexer(new StringReader(s));
      StructuresParser parser = new StructuresParser(lexer);
      query = parser.struc();
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
  public void collectOneStep(final Collection collection, Struc subject) {
    try {
      `BottomUp(OneStep(subject,collection)).fire(subject);
      //System.out.println(collection);
    } catch (FireException e) {
      System.out.println("Failed to get successors " + subject);
    }
  }

  %strategy OneStep(subject:Struc,c:Collection) extends `Identity() {
    visit Struc {
      /* [(R,T),U] -> ([R,U],T)
         [(R,T),U] -> ([T,U],R) */
      par(concPar(X1*,cop(concCop(R*,T*)),X2*,U,X3*)) -> {

        if(`T*.isEmptyconcCop() || `R*.isEmptyconcCop() ) { 
        } else {
          StrucPar context = `concPar(X1*,X2*,X3*);

          if(!optim3 || !canReact(`T,`U)) {
            if(!optim2 || canReact(`R,`U)) {
              StrucPar parR = cop2par(`R*);
              Struc elt1 = `par(concPar(cop(concCop(par(concPar(parR*,U)),T*)),context*));
              c.add(getEnvironment().getPosition().getReplace(elt1).visit(subject));
            }
          }
          if(!optim3 || !canReact(`R,`U)) {
            if(!optim2 || canReact(`T,`U)) {
              StrucPar parT = cop2par(`T*);
              Struc elt2 = `par(concPar(cop(concCop(par(concPar(parT*,U)),R*)),context*));
              c.add(getEnvironment().getPosition().getReplace(elt2).visit(subject));
            }
          }
        }
      }

      /* [U,(R,T)] -> ([R,U],T)
         [U,(R,T)] -> ([T,U],R) */
      par(concPar(X1*,U,X2*,cop(concCop(R*,T*)),X3*)) -> {
        if(`T*.isEmptyconcCop() || `R*.isEmptyconcCop()) { 

        } else {
          StrucPar context = `concPar(X1*,X2*,X3*);

          if(!optim3 || !canReact(`T,`U)) {
            if(!optim2 || canReact(`R,`U)) {
              StrucPar parR = cop2par(`R*);
              Struc elt3 = `par(concPar(cop(concCop(par(concPar(parR*,U)),T*)),context*));
              c.add(getEnvironment().getPosition().getReplace(elt3).visit(subject));
            }
          }

          if(!optim3 || !canReact(`R,`U)) {
            if(!optim2 || canReact(`T,`U)) {
              StrucPar parT = cop2par(`T*);
              Struc elt4 = `par(concPar(cop(concCop(par(concPar(parT*,U)),R*)),context*));
              c.add(getEnvironment().getPosition().getReplace(elt4).visit(subject));
            }
          }
        }
      }

        /* [R,T] -> <R;T>
           [R,T] -> <T;R> */
        par(concPar(X1*,R,X2*,T,X3*)) -> {
          StrucPar context = `concPar(X1*,X2*,X3*);
          c.add(getEnvironment().getPosition().getReplace(
                `par(concPar(seq(concSeq(R,T)),context*))).visit(subject));
          c.add(getEnvironment().getPosition().getReplace(
                `par(concPar(seq(concSeq(T,R)),context*))).visit(subject));
        }

        /* [<R;U>,<T;V>] -> <[R,T];[U,V]>
           [<R;U>,<T;V>] -> <[U,V];[R,T]> */
        par(concPar(X1*,seq(concSeq(R*,U*)),X2*,seq(concSeq(T*,V*)),X3*)) -> {
          if(`R*.isEmptyconcSeq() || `U*.isEmptyconcSeq()
              || `T*.isEmptyconcSeq() || `V*.isEmptyconcSeq()) {
          } else {
            if(!optim || (!canReact(`U,`T) && !canReact(`R,`V))) {
              if(!optim2 || (canReact(`R,`T) && canReact(`U,`V))) {
                //System.out.println("U = " + `U + "\tT = " + `T);
                //System.out.println("R = " + `R + "\tV = " + `V);
                StrucPar context = `concPar(X1*,X2*,X3*);
                StrucPar parR = seq2par(`R*);
                StrucPar parU = seq2par(`U*);
                StrucPar parT = seq2par(`T*);
                StrucPar parV = seq2par(`V*);
                c.add(getEnvironment().getPosition().getReplace(
                      `par(concPar(seq(concSeq(par(concPar(parR*,parT*)),par(concPar(parU*,parV*)))),context*))
                      ).visit(subject));
                c.add(getEnvironment().getPosition().getReplace(
                      `par(concPar(seq(concSeq(par(concPar(parU*,parV*)),par(concPar(parR*,parT*)))),context*))
                      ).visit(subject));
              }
            }
          }
        }

        /* [R,<T;U>] -> <[R,T];U>
           [R,<T;U>] -> <T;[R,U]> */
        par(concPar(X1*,R,X2*,seq(concSeq(T*,U*)),X3*)) -> {
          if(`T*.isEmptyconcSeq() || `U*.isEmptyconcSeq()) {
          } else {
            StrucPar context = `concPar(X1*,X2*,X3*);
            if(!optim2 || canReact(`R,`T)) {
              StrucPar parT = seq2par(`T*);
              c.add(getEnvironment().getPosition().getReplace(
                    `par(concPar(seq(concSeq(par(concPar(R,parT*)),U*)),context*))
                    ).visit(subject));
            }

            if(!optim2 || canReact(`R,`U)) {
              StrucPar parU = seq2par(`U*);
              c.add(getEnvironment().getPosition().getReplace(
                    `par(concPar(seq(concSeq(T*,par(concPar(R,parU*)))),context*))
                    ).visit(subject));
            }
          }
        }

        /* [<T;U>,R] -> <[R,T];U>
           [<T;U>,R] -> <T;[R,U]> */
        par(concPar(X1*,seq(concSeq(T*,U*)),X2*,R,X3*)) -> {
          if(`T*.isEmptyconcSeq() || `U*.isEmptyconcSeq()) {
          } else {
            StrucPar context = `concPar(X1*,X2*,X3*);

            if(!optim2 || canReact(`R,`T)) {
              StrucPar parT = seq2par(`T*);
              c.add(getEnvironment().getPosition().getReplace(
                    `par(concPar(seq(concSeq(par(concPar(R,parT*)),U*)),context*))
                    ).visit(subject));
            }

            if(!optim2 || canReact(`R,`U)) {
              StrucPar parU = seq2par(`U*);
              c.add(getEnvironment().getPosition().getReplace(
                    `par(concPar(seq(concSeq(T*,par(concPar(R,parU*)))),context*))
                    ).visit(subject));
            }
          }
        }

        /* [X,-X] -> o */
        par(concPar(X1*,x,X2*,neg(x),X3*)) -> {
          Struc elt5 = `par(concPar(X1*,X2*,X3*));
          c.add(getEnvironment().getPosition().getReplace(elt5).visit(subject));
        }
        /* [-X,X] -> o */
        par(concPar(X1*,neg(x),X2*,x,X3*)) -> {
          Struc elt6 = `par(concPar(X1*,X2*,X3*));
          c.add(getEnvironment().getPosition().getReplace(elt6).visit(subject));
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
        cop(l) -> { res =  length(`l); break block; }
        par(l) -> { res =  length(`l); break block; }
        seq(l) -> { res =  2+length(`l); break block; }
        _      -> { res =  1; break block; }
      }
      %match(StrucPar t) {
        concPar()           -> { res =  0; break block; }
        concPar(head,tail*) -> { res =  length(`head) + length(`tail*); break block; }
      }
      %match(StrucCop t) {
        concCop()           -> { res =  0; break block; }
        concCop(head,tail*) -> { res =  length(`head) + length(`tail*); break block; }
      }
      %match(StrucSeq t) {
        concSeq()           -> { res =  0; break block; }
        concSeq(head,tail*) -> { res =  length(`head) + length(`tail*); break block; }
      }
    }
    //lengthCache.put(t,new Integer(res));

    return res;
  }

  private static WeakHashMap weightMap = new WeakHashMap();
	private static int weightCall = 0;
  public static int weight(StructuresAbstractType subject) {
		weightCall++;
    if(weightMap.containsKey(subject)) {
      return ((Integer)weightMap.get(subject)).intValue();
    }
    //double l = (double)length(subject);
    //double n = (double)numberOfPair(subject);
    int l = length(subject);
    int n = numberOfPair(subject);
    double w = (l*l)/(1.0+n);
    // System.out.println(prettyPrint(subject));
    // System.out.println("l = " + l + "\t#pair = " + n + "\tw = " + w);
    weightMap.put(subject,new Integer((int)w));
    return (int)w;
  }

  public static int numberOfPair(StructuresAbstractType subject) {
    final Collection collection = new ArrayList();
    try {
      `BottomUp(CountPairs(collection)).visit(subject);
    } catch (VisitFailure e) {
      System.out.println("Failed to count pairs" + subject);
    }
    return collection.size();
  }

  %strategy CountPairs(bag:Collection) extends `Identity() {
    visit Struc {
      par(concPar(_*,x,_*,neg(x),_*)) -> {
        bag.add(`x);
      }
      cop(concCop(_*,x,_*,neg(x),_*)) -> {
        bag.add(`x);
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

    if(subject == `o()) {
      return true;
    } else if(positive.size() != negative.size()) {
      //System.out.print("#pos = " + positive.size());
      //System.out.println("\t#neg = " + negative.size());
      return false;
    } else {
      for(Iterator it = negative.iterator(); it.hasNext() ; ) {
        Struc s = (Struc)it.next();
        if(!positive.contains(s)) {
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

    for(Iterator it = atomT1.iterator() ; it.hasNext() ; ) {
      if(atomT2.contains(it.next())) {
        return true;
      }
    }
    return false;
  }


  private static void collectAtom(StructuresAbstractType subject, final HashSet positive, final HashSet negative) {
    try {
      `BottomUp(FindAtoms(positive,negative)).visit(subject);
    } catch (VisitFailure e) {
      System.out.println("Failed to get atoms" + subject);
    }
  }
  %strategy FindAtoms(positive:HashSet,negative:HashSet) extends `Identity() {
    visit Struc {
      s@cop(_) -> { return `s; }
      s@par(_) -> { return `s; }
      s@seq(_) -> { return `s; }
      neg(s) -> { negative.add(`s); return `s; } 
      s -> { positive.add(`s); return `s; }
    }
  }

  /* conversion functions */
  static StrucPar cop2par(StrucCop l) {
    %match(StrucCop l) {
      concCop() -> { return `concPar(); }
      concCop(head,tail*) -> { 
        StrucPar ptail = cop2par(`tail*);
        return `concPar(head,ptail*); 
      }
    }
    return null;
  }

  static StrucPar seq2par(StrucSeq l) {
    %match(StrucSeq l) {
      concSeq() -> { return `concPar(); }
      concSeq(head,tail*) -> {
        StrucPar ptail = seq2par(`tail*);
        return `concPar(head,ptail*); 
      }
    }
    return null;
  }

  public String prettyPrint(StructuresAbstractType t) {
    %match(Struc t) {
      neg(s) -> { return "-" + prettyPrint(`s);}
      par(l) -> { return "[" + prettyPrint(`l) + "]";}
      cop(l) -> { return "(" + prettyPrint(`l) + ")";}
      seq(l) -> { return "<" + prettyPrint(`l) + ">";}
      x      -> {
        String res = `x.toString();
        /* constant, so remove the trailing () */
        return res.substring(0,res.length()-2);
      }
    }
    %match(StrucCop t) {
      concCop(head) -> {
        return prettyPrint(`head);
      }
      concCop(head,tail*) -> {
        return prettyPrint(`head) + "," + prettyPrint(`tail*); 
      }
    }
    %match(StrucPar t) {
      concPar(head) -> {
        return prettyPrint(`head);
      }
      concPar(head,tail*) -> { 
        return prettyPrint(`head) + "," + prettyPrint(`tail*); 
      }
    }
    %match(StrucSeq t) {
      concSeq(head) -> {
        return prettyPrint(`head);
      }
      concSeq(head,tail*) -> { 
        return prettyPrint(`head) + ";" + prettyPrint(`tail*); 
      }
    }
    return t.toString();
  }
}
