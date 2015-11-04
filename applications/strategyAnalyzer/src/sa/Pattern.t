package sa;

import sa.rule.types.*;
import tom.library.sl.*;
import java.util.HashSet;
import java.util.Set;

public class Pattern {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/HashSet.tom }
  %typeterm Signature { implement { Signature } }


  private static void debug(String ruleName, Term input, Term res) {
    System.out.println(ruleName);
    //debugVerbose(ruleName,input,res);
  }

  private static void debugVerbose(String ruleName, Term input, Term res) {
    System.out.println(ruleName + ": " + Pretty.toString(input) + " --> " + Pretty.toString(res));
  }

  public static void main(String args[]) {
//     example1();
    // example2();
//     example3(); // numadd
    example4(); // interp
//     example5(); // balance
//     example6(); // and-or
  }

  /*
   * Transform a list of ordered rules into a TRS; rule by rule
   */
  public static RuleList trsRule(RuleList ruleList, Signature eSig, Signature gSig) {

    try {
      ruleList = `TopDown(RemoveVar()).visitLight(`ruleList);
    } catch(VisitFailure e) {
    }
    RuleList res = `ConcRule();
    %match(ruleList) {
      ConcRule(C1*,Rule(lhs,rhs),_*) -> {
        AddList prev = `ConcAdd();
        %match(C1) {
          ConcRule(_*,Rule(l,r),_*) -> {
            prev = `ConcAdd(l,prev*);
          }
        }
        Term pattern = `Sub(lhs,Add(prev*));
        System.out.println("PATTERN : " + Pretty.toString(pattern));
        Term t = `reduce(pattern,eSig,gSig);
        System.out.println("REDUCED : " + Pretty.toString(t));
        %match(t) {
          Add(ConcAdd(_*,e,_*)) -> {
            res = `ConcRule(Rule(e,rhs),res*);
          }

          e@(Appl|Var)[] -> {
            res = `ConcRule(Rule(e,rhs),res*);
          }
        }
      }
    }



    // test subsumtion idea
    //res = removeRedundantRule(res,eSig,gSig);
    res = removeRedundantRuleAux(res,`ConcRule(), eSig,gSig);

    for(Rule rule:`res.getCollectionConcRule()) {
      System.out.println(Pretty.toString(rule));
    }
    System.out.println("size = " + `res.length());


    return res;
  }


  private static RuleList removeRedundantRule(RuleList rules, Signature eSig, Signature gSig) {
    HashSet<RuleList> bag = new HashSet<RuleList>();

    %match(rules) {
      ConcRule(C1*,rule,C2*) -> {
        boolean b = canBeRemoved2(`rule, `ConcRule(C1*,C2*), eSig, gSig);
        if(b) {
          System.out.println("REMOVE: " + Pretty.toString(`rule));
          bag.add(removeRedundantRule(`ConcRule(C1*,C2*), eSig, gSig));

          // uncomment the following return for a greedy algorithm:
          //return removeRedundantRule(`ConcRule(C1*,C2*), eSig, gSig);

        }
      }
    }

    RuleList minrules = rules;
    int minlength = minrules.length();
    for(RuleList e:bag) {
      if(e.length() < minlength) {
        minrules = e;
        minlength = minrules.length();
      }
    }

    return minrules;

  }

  private static RuleList removeRedundantRuleAux(RuleList candidates, RuleList kernel, Signature eSig, Signature gSig) {
    HashSet<RuleList> bag = new HashSet<RuleList>();

    %match( candidates ) {
      ConcRule(head, tail*) -> {
        boolean b = canBeRemoved2(`head, `ConcRule(tail*,kernel*), eSig, gSig);
        if(b) {
          System.out.println("REMOVE: " + Pretty.toString(`head));
          // try with the head removed 
          bag.add(removeRedundantRuleAux(`tail, kernel, eSig, gSig));

          // uncomment the following return for a greedy algorithm:
          //return removeRedundantRule(`ConcRule(C1*,C2*), eSig, gSig);

        }

        // try with the head kept in kernel
        bag.add(removeRedundantRuleAux(`tail, `ConcRule(head,kernel*), eSig, gSig));
      }
    }

    RuleList minrules = `ConcRule(candidates*,kernel*);
    int minlength = minrules.length();
    for(RuleList e:bag) {
      if(e.length() < minlength) {
        minrules = e;
        minlength = minrules.length();
      }
    }

    return minrules;

  }



  private static Term reduce(Term t, Signature eSig, Signature gSig) {
    try {
      // DistributeAdd needed if we can start with terms like X \ f(a+b) or X \ (f(X)\f(f(_)))
      Strategy S1 = `ChoiceId(EmptyAdd2Empty(),PropagateEmpty(),SimplifySub(eSig,gSig));
      Strategy S2 = `ChoiceId(EmptyAdd2Empty(),PropagateEmpty(), SimplifyAdd());
      //Strategy S2 = `ChoiceId(EmptyAdd2Empty(),PropagateEmpty(),TrySubsumption(), SimplifyAdd());

      t =  `InnermostId(S1).visitLight(t);
      System.out.println("NO SUBs = " + Pretty.toString(t));
      //       System.out.println("NO SUBs = " + t);

      t = `InnermostId(S2).visitLight(t);
      System.out.println("NO ADD = " + Pretty.toString(t));

      /*
      Term old = null;
      while(old != t) {
        old = t;
        System.out.println("S2: " + t);
        t = `InnermostId(S2).visitLight(t);
        System.out.println("EXPAND: " + t);
        t = expandAdd(t);
      }
      System.out.println("FIXPOINT = " + Pretty.toString(t));
      System.out.println("FIXPOINT = " + t);
      */

    } catch(VisitFailure e) {
      System.out.println("failure on: " + t);
    }

    t = expandAdd(t);
    System.out.println("EXPAND = " + Pretty.toString(t));

    %match(t) {
      Add(tl) -> {
        t = `Add(simplifySubsumption(tl));
        System.out.println("REMOVE SUBSUMTION = " + Pretty.toString(t));
      }
    }
    
    return t;
  }

  %strategy PropagateEmpty() extends Identity() {
    visit Term {
      //   f(t1,...,empty,...,tn) -> empty
      s@Appl(f,TermList(_*,Empty(),_*)) -> {
        Term res = `Empty();
        debug("propagate empty",`s,res);
        return res;
      }
    }
  }

  %strategy EmptyAdd2Empty() extends Identity() {
    visit Term {
      s@Add(ConcAdd()) -> {
        Term res = `Empty();
        debug("elim ()",`s,res);
        return res;
      }
    }
  }

  %strategy ElimEmpty() extends Identity() {
    visit Term {
      // t + empty -> t
      s@Add(ConcAdd(C1*,Empty(),C2*)) -> {
        Term res = `Add(ConcAdd(C1*,C2*));
        debug("elim empty",`s,res);
        return res;
      }
    }
  }

  %strategy FlattenAdd() extends Identity() {
    visit Term {

      // flatten: (a + (b + c) + d) -> (a + b + c + d)
      s@Add(ConcAdd(C1*, Add(ConcAdd(tl*)), C2*)) -> {
        Term res = `Add(ConcAdd(C1*,tl*,C2*));
        debug("flatten1",`s,res);
        return res;
      }

      // t + t -> t
      s@Add(ConcAdd(C1*, t, C2*, t, C3*)) -> {
       Term res = `Add(ConcAdd(C1*,t,C2*,C3*));
       debug("t + t -> t",`s,res);
       return res;
     }

    }
  }

  // check abstraction
  // a + b + g(_) + f(_) == signature ==> X
  %strategy TryAbstraction(eSig:Signature) extends Identity() {
    visit Term {
      s@Add(al@ConcAdd(Appl(f,_),_*)) -> {
          GomType codomain = eSig.getCodomain(`f);
          if(codomain != null) {
            Set<String> ops = eSig.getSymbols(codomain);
            AddList l = `ConcAdd();
            for(String name:ops) {
              TermList args = `TermList();
              for(int i=0 ; i<eSig.getArity(name) ; i++) {
                args = `TermList(Var("_"),args*);
              }
              Term p = `Appl(name,args);
              l = `ConcAdd(p,l*);
            }
            if(l == `al) {
              System.out.println("OPS = " + ops + " al = " + `al);
              return `Var("_");
            }
          } else {
            // do nothing
            //System.out.println("f -> null " + `f);
          }
      }
    }
  }

  %strategy TrySubsumption() extends Identity() {
    visit Term {
      s@Add(ConcAdd(C1*,t1@(Appl|Var)[],C2*,t2@(Appl|Var)[],C3*)) -> {
        //System.out.println("try: " + `t1 + " << " + `t2); 
        if(match(`t1,`t2)) {
          Term res = `Add(ConcAdd(t1,C1*,C2*,C3*));
          debug("subsumtion",`s,res);
          return res;
        } else if(match(`t2,`t1)) {
          Term res = `Add(ConcAdd(t2,C1*,C2*,C3*));
          debug("subsumtion",`s,res);
          return res;
        }
      }
    }
  }
      
  %strategy SimplifyAdd() extends Identity() {
    visit Term {

      // flatten: (a + (b + c) + d) -> (a + b + c + d)
      s@Add(ConcAdd(C1*, Add(ConcAdd(tl*)), C2*)) -> {
        Term res = `Add(ConcAdd(C1*,tl*,C2*));
        debug("flatten1",`s,res);
        return res;
      }

      // Add(t) -> t
      s@Add(ConcAdd(t)) -> {
        Term res = `t;
        debug("flatten2",`s,res);
        return res;
      }

      // a + x + b -> x
      s@Add(ConcAdd(_*, x@Var("_"), _*)) -> {
        Term res = `x;
        debug("a + x + b -> x",`s,res);
        return res;
      }

      // t + empty -> t
      s@Add(ConcAdd(C1*,Empty(),C2*)) -> {
        Term res = `Add(ConcAdd(C1*,C2*));
        debug("elim empty",`s,res);
        return res;
      }
     
      // f(t1,...,ti,...,tn) + f(t1,...,ti',...,tn) -> f(t1,..., ti + ti',...,tn)
      // all but one ti, ti' should be identical
      s@Add(ConcAdd(C1*, Appl(f,tl1), C2*, Appl(f, tl2), C3*)) -> {
        /*
        TermList res = mergeAdd(`s);
        int n = res.length();
        if(n>0) {
          int r = (int)(java.lang.Math.random()*n);
          System.out.println("n = " + n + " random = " + r);
          for(int i=0 ; i<r ; i++) {
            res = res.getTailTermList();
          }
          return res.getHeadTermList();
        } else {
          return `s; // perform identity 
        }
        */
        TermList tl = `addUniqueTi(tl1,tl2);
        if(tl != null) {
          Term res = `Add(ConcAdd(C1*, Appl(f,tl), C2*, C3*));
          debug("add merge",`s,res);
          return res;
        } else {
          //System.out.println("add merge failed");
        }
      }


    }
  }
/*
  private static TermList mergeAdd(Term t) {
    TermList res = `TermList();
    %match(t) {
      s@Add(ConcAdd(C1*, Appl(f,tl1), C2*, Appl(f, tl2), C3*)) -> {
        TermList tl = `addUniqueTi(tl1,tl2);
        if(tl != null) {
          res = `TermList(Add(ConcAdd(C1*, Appl(f,tl), C2*, C3*)),res*);
          return res; // to remove non determinism
        } else {
          //System.out.println("add merge failed");
        }

      }
    }
    return res;
  }
*/

  /*
   * returns true is the term does not contain any Add or Sub
   */
  private static boolean isPlainTerm(Term t) {
    try {
      `TopDown(PlainTerm()).visitLight(t);
    } catch(VisitFailure e) {
      return false;
    }
    return true;
  }

  %strategy PlainTerm() extends Identity() {
    visit Term {
      t@(Add|Sub)[] -> {
        `Fail().visitLight(`t);
      }
    }

  }


  %strategy SimplifySub(eSig:Signature,gSig:Signature) extends Identity() {//Fail() {
    visit Term {

      // t - x -> empty
      s@Sub(t, Var("_")) -> {
        Term res = `Empty();
        debug("t - x -> empty",`s,res);
        return res;
      }

      // t - empty -> t
      s@Sub(t, Empty()) -> {
        Term res = `t;
        debug("t - empty -> t",`s,res);
        return res;
      }
      
      // t - (a1 + ... + an) -> (t - a) - (a2 + ... + an))
      s@Sub(t, Add(ConcAdd(head,tail*))) -> {
        Term res = `Sub(Sub(t,head), Add(ConcAdd(tail*)));
        debug("sub distrib1",`s,res);
        return res;
      }
     
      // X - t -> expand AP
      s@Sub(Var("_"), t@Appl(f,args_f)) -> {
        if(isPlainTerm(`t)) {
          RuleCompiler ruleCompiler = new RuleCompiler(`eSig, `gSig);
          RuleList rl = ruleCompiler.expandAntiPatterns(`ConcRule(Rule(Anti(t),Var("_"))));
          //System.out.println("rl = " + Pretty.toString(rl));
          AddList tl = `ConcAdd();
          GomType codomain = gSig.getCodomain(`f);
          %match(rl) {
            ConcRule(_*,Rule(lhs@Appl(g,args_g),rhs),_*) -> {
              //if(gSig.getCodomain(`g) == codomain) { // optim: remove ill typed terms 
                Term newLhs = `TopDown(RemoveVar()).visitLight(`lhs);
                tl = `ConcAdd(newLhs,tl*); // order not preserved
              //}
            }
          }
          Term res = `Add(tl);
          debug("expand AP",`s,res);
          res = eliminateIllTyped(res, codomain, `gSig);
          return res;
        }
      }

      // empty - t -> empty
      //Sub(Empty(),t) -> {
      //  return `Empty();
      //}

      // empty - f(t1,...,tn) -> empty
      s@Sub(Empty(),Appl(f,tl)) -> {
        Term res = `Empty();
        debug("empty - f(...) -> empty",`s,res);
        return `Empty();
      }


      // t - t -> empty
      //Sub(t, t) -> {
      //  return `Empty();
      //}

      // (a + t + b) - t -> (a + b)
      //Sub(Add(ConcAdd(C1*,t,C2*)),t) -> {
      //  return `Add(ConcAdd(C1*,C2*));
      //}


      // (a1 + ... + an) - b -> (a1 - b) + ( (a2 + ... + an) - b )
      //s@Sub(Add(ConcAdd(head,tail*)), t) -> {
      //  Term res = `Add(ConcAdd(Sub(head,t), Sub(Add(ConcAdd(tail*)),t)));
      //  System.out.println("sub distrib2 : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
      //  return res;
      //}

      // (a1 + ... + an) - t@f(t1,...,tn) -> (a1 - t) + ( (a2 + ... + an) - t )
      s@Sub(Add(ConcAdd(head,tail*)), t@Appl(f,tl)) -> {
        Term res = `Add(ConcAdd(Sub(head,t), Sub(Add(ConcAdd(tail*)),t)));
        debug("sub distrib2",`s,res);
        return res;
      }

      // t@f(t1,...,tn) - g(t1',...,tm') -> t
      s@Sub(t@Appl(f,tl1), Appl(g, tl2)) && f!=g -> {
        Term res = `t;
        debug("sub elim1",`s,res);
        return res;
      }

      // f(t1,...,tn) - (a + g(t1',...,tm') + b) -> f(t1,...,tn) - (a + b)
      //s@Sub(t@Appl(f,tl1),Add(ConcAdd(C1*, Appl(g, tl2), C2*))) && f!=g -> {
      //  Term res = `Sub(t, Add(ConcAdd(C1*,C2*)));
      //  System.out.println("sub elim2 : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
      //  return res;
      // }


      // f(t1,t2) - f(t1',t2') -> f(t1, t2-t2') + f(t1-t1', t2)
      // f(t1,...,tn) - f(t1',...,tn') -> f(t1-t1',t2,...,tn) + f(t1, t2-t2',...,tn) + ... + f(t1,...,tn-tn')
      s@Sub(t1@Appl(f,tl1), t2@Appl(f, tl2)) -> {
        Term res = `sub(t1,t2);
        debug("sub1",`s,res);
        return res;
      }

      //s@Sub(t1@Appl(f,tl1),Add(ConcAdd(C1*, t2@Appl(f, tl2), C2*))) -> {
      //  Term res = `Sub(sub(t1,t2), Add(ConcAdd(C1*,C2*)));
      //  System.out.println("sub2 : " + Pretty.toString(`s) + " --> " + Pretty.toString(res));
      //  return res;
      // }

    }
  }

  // f(a1,...,an) - f(b1,...,bn) -> f(a1-b1,..., an) + ... + f(a1,...,an-bn)
  private static Term sub(Term t1,Term t2) {
    %match(t1,t2) {
      Appl(f,args1), Appl(f,args2) -> {
        TermList tl1 = `args1;
        TermList tl2 = `args2;
        int len = `tl1.length();
        TermList args[] = new TermList[len];
        for(int i=0 ; i<len ; i++) {
          args[i] = `TermList();
        }
        int cpt = 0;
        while(!tl1.isEmptyTermList()) {
          Term h1 = tl1.getHeadTermList();
          Term h2 = tl2.getHeadTermList();

          for(int i=0 ; i<len ; i++) {
            TermList tl = args[i]; // cannot use [] in `
            if(i==cpt) {
              tl = `TermList(tl*, Sub(h1,h2));
            } else {
              tl = `TermList(tl*, h1);
            }
            args[i] = tl;
          }

          tl1 = tl1.getTailTermList();
          tl2 = tl2.getTailTermList();
          cpt++;
        }

        AddList sum = `ConcAdd();
        for(int i=0 ; i<len ; i++) {
          TermList tl = args[i]; // cannot use [] in `
          sum = `ConcAdd(Appl(f,tl),sum*);;
        }

        return `Add(sum);
      }
    }
    System.out.println("cannot sub " + t1 + " and " + t2);
    return null;
  }

  // (a1,...,an) + (b1,...,bn) -> (a1,..., ai+bi,..., an)
  // for the unique i such that ai != bi
  private static TermList addUniqueTi(TermList l1,TermList l2) {
    TermList tl1=l1;
    TermList tl2=l2;
    TermList tl = `TermList();
    int cpt = 0;
    while(!tl1.isEmptyTermList()) {
      Term h1 = tl1.getHeadTermList();
      Term h2 = tl2.getHeadTermList();
      if(h1==h2) {
        tl = `TermList(tl*, h1);
      } else {
        tl = `TermList(tl*, Add(ConcAdd(h1,h2)));
        cpt++;
      }

      tl1 = tl1.getTailTermList();
      tl2 = tl2.getTailTermList();
    }
    if(cpt <= 1) {
      return tl;
    } else {
      //System.out.println("cannot add " + l1 + " and " + l2);
      return null;
    }
  }

  /*
   * Replace a named variable by an underscore
   */
  %strategy RemoveVar() extends Identity() {
    visit Term {
      Var(_) -> {
        return `Var("_");
      }
    }
  }

  /*
   * given a term (which contains Add and Sub) and a type
   * returns a term where badly typed terms are replaced by Empty()
   */
  private static Term eliminateIllTyped(Term t, GomType type, Signature gSig) {
    //System.out.println(Pretty.toString(t) + ":" + type.getName());
    %match(t) {
      Var("_") -> {
        return t;
      }

      Appl(f,args) -> {
        if(gSig.getCodomain(`f) == type) {
          GomTypeList domain = gSig.getDomain(`f);
          TermList tail = `args;
          TermList new_args = `TermList();
          while(!tail.isEmptyTermList()) {
            Term head = tail.getHeadTermList();
            GomType arg_type = domain.getHeadConcGomType();

            Term new_arg = eliminateIllTyped(head,arg_type,gSig);
            if(new_arg == `Empty()) {
              // propagate Empty for any term which contains Empty
              return `Empty();
            }

            new_args = `TermList(new_args*, new_arg);
            tail = tail.getTailTermList();
            domain = domain.getTailConcGomType();
          }
          return `Appl(f,new_args);
        } else {
          return `Empty();
        }
      }

      Sub(t1,t2) -> {
        return `Sub(eliminateIllTyped(t1,type,gSig),eliminateIllTyped(t2,type,gSig));
      }

      Add(tl) -> {
        AddList tail = `tl;
        AddList res = `ConcAdd();
        while(!tail.isEmptyConcAdd()) {
          Term head = tail.getHeadConcAdd();
          res = `ConcAdd(eliminateIllTyped(head,type,gSig),res*);
          tail = tail.getTailConcAdd();
        }
        return `Add(res);
      }

    }

    System.out.println("eliminateIllTyped Should not be there: " + `t);
    return t;
  }
/*
   //
   // implementation using rules
   // stack overflow on big terms
   //
  private static Term expandAdd(Term t) {
    try {
      return `RepeatId(TopDown(DistributeAdd())).visitLight(t);
    } catch(VisitFailure e) {
      System.out.println("expandAdd: failure");
    }
    return t;
  }

  %strategy DistributeAdd() extends Identity() {//Fail() {
    visit Term {

      // Add(t) -> t
      s@Add(ConcAdd(t)) -> {
        Term res = `t;
        //debug("flatten2",`s,res);
        return res;
      }

      // f(t1,..., ti + ti',...,tn)  ->  f(t1,...,tn) + f(t1',...,tn')
      s@Appl(f, TermList(C1*, Add(ConcAdd(u,v,A2*)), C2*)) -> {
        Term res = `Add(ConcAdd(Appl(f, TermList(C1*,Add(ConcAdd(u,A2*)),C2*)), 
                                 Appl(f, TermList(C1*,Add(ConcAdd(v,A2*)),C2*))));
        //debug("distribute add",`s,res);
        return res;
      }

    }
  }
*/

  /*
   * Transform a term which contains Add into
   * a sum of terms which do no longer contain any Add
   * (more efficient implementation)
   */
  private static Term expandAdd(Term t) {
    HashSet<Term> bag = new HashSet<Term>();
    expandAddAux(bag,t);
    AddList tl = `ConcAdd();
    for(Term e:bag) {
      tl = `ConcAdd(e,tl*);
    }
    return `Add(tl);
  }

  // Transform a term which contains Add into a set of plain terms
  private static void expandAddAux(HashSet<Term> c, Term subject) {
    HashSet<Term> todo = new HashSet<Term>();
    todo.add(subject);

    while(todo.size() > 0) {
      HashSet<Term> todo2 = new HashSet<Term>();
      for(Term t:todo) {
        HashSet<Term> tmpC = new HashSet<Term>();
        try {
           // TopDownCollect: apply s1 in a top-down way, s should extends the identity
           // a failure stops the top-down process under this current node
          `TopDownCollect(ExpandAdd(tmpC,t)).visit(t);
        } catch(VisitFailure e) {
        }
        if(tmpC.isEmpty()) {
          c.add(t);
        } else {
          for(Term e:tmpC) {
            if(isPlainTerm(e)) {
              c.add(e);
            } else {
              todo2.add(e);
            }
          }
        }
      }
      todo = todo2;
      //System.out.println("size(c) = " + c.size());
    }
  }

  %strategy ExpandAdd(c:HashSet, subject:Term) extends Identity() {
    visit Term {
      Add(ConcAdd(_*,t,_*)) -> {
        Term newt = (Term) getEnvironment().getPosition().getReplace(`t).visit(subject);
        c.add(newt);
      }
      
      Add(ConcAdd(head,tail*)) -> {
        // remove the term which contains Add(ConcAdd(...))
        c.remove(`subject);
        // fails to stop the TopDownCollect, and thus do not expand deeper terms
        `Fail().visit(`subject);
      }
    }
  }

  /*
   * Given a list of terms
   * remove those which are subsumed by another one
   */
  private static AddList simplifySubsumption(AddList tl) {
    %match(tl) {
      ConcAdd(C1*,t1,C2*,t2,C3*) -> {
        if(`match(t1,t2)) {
          return simplifySubsumption(`ConcAdd(t1,C1*,C2*,C3*));
        } else if(`match(t2,t1)) {
          return simplifySubsumption(`ConcAdd(t2,C1*,C2*,C3*));
        }
      }
    }
    return tl;
  }

  /*
   * Return true if t1 matches t2
   */
  private static boolean match(Term t1, Term t2) {
    %match(t1,t2) {
      // Delete
      Appl(name,TermList()),Appl(name,TermList()) -> { return true; }
      // Decompose
      Appl(name,a1),Appl(name,a2) -> { return `decomposeList(a1,a2); }
      // SymbolClash
      Appl(name1,args1),Appl(name2,args2) && name1!=name2 -> { return false; }

      // Match
      Var(_),Appl(name2,args2) -> { return true; }
      Var(_),Var(_) -> { return true; }

    }
    return false;
  }

  private static boolean decomposeList(TermList tl1, TermList tl2) {
    %match(tl1,tl2) {
      TermList(), TermList() -> { return true; }
      TermList(head1,tail1*), TermList(head2,tail2*) -> { 
        return `match(head1,head2) && `decomposeList(tail1,tail2); 
      }
    }
    return false;
  }

  /*
   * 1st idea to remove redundant patterns
   *
   * expand once all occurence of _ in a term
   */
  private  static Term expandVar(Term t, Signature eSig, Signature gSig) {
    HashSet<Position> bag = new HashSet<Position>();
    HashSet<Term> res = new HashSet<Term>();

    GomType codomain = null;
    %match(t) {
      Appl(f,_) -> {
          codomain = gSig.getCodomain(`f);
      }
    }

    //System.out.println("t = " + t);
    //System.out.println("codomain = " + codomain);

    res.add(t);
    try {
      `TopDown(CollectVarPosition(bag)).visit(t);

      for(Position omega:bag) {
        //System.out.println("omega = " + omega);
        /*
        try {
          System.out.println("subterm: " + omega.getSubterm().visit(t));
        } catch(VisitFailure e) {
          System.out.println("getSubterm failed: " + t);
          System.out.println("pos = " + omega);

        }
*/



        HashSet<Term> todo = new HashSet<Term>();
        System.out.println("res.size  = " + res.size());
        for(Term subject:res) {
          // add g(Z1,...) ... h(Z1,...)
          for(String name: eSig.getSymbols()) {
            int arity = eSig.getArity(name);
            Term expand = Tools.genAbstractTerm(name,arity, "_");
            expand = `TopDown(RemoveVar()).visitLight(expand);

            Term newt = (Term) omega.getReplace(expand).visit(subject);

            //System.out.println("newt1 = " + Pretty.toString(newt));
            newt = eliminateIllTyped(newt, codomain, gSig);
            //System.out.println("newt2 = " + Pretty.toString(newt));
            if(newt != `Empty()) {
              todo.add(newt);
            }
          }
          System.out.println("todo.size = " + todo.size());

        }
        res=todo;

        
      }

    } catch(VisitFailure e) {
      System.out.println("expandVar failed");
    }

    AddList al = `ConcAdd();
    for(Term newt:res) {
      al = `ConcAdd(newt,al*);
    }

    //for(Term e:res) {
    //  System.out.println("expandVar = " + Pretty.toString(e));
    //}

    return `Add(al);
  }

  %strategy CollectVarPosition(c:HashSet) extends Identity() {
    visit Term {
      Var("_") -> {
        c.add(getEnvironment().getPosition());
      }
    }
  }

  public static boolean canBeRemoved1(Rule rule, RuleList ruleList, Signature eSig, Signature gSig) {
    %match(rule) {
      Rule(lhs,rhs) -> {

        Term t = expandVar(`lhs,eSig, gSig);
        %match(t) {
          Add(ConcAdd(_*,et,_*)) -> {
            boolean foundMatch = false;
            %match(ruleList) {
              ConcRule(_*,Rule(lhs,_),_*) -> {
                if(match(`lhs,`et)) {
                  foundMatch = true;
                }
              }
            }
            if(!foundMatch) {
              return false;
            }
          }
        }
      }
    }

    return true;
  }


  /*
   * 2nd idea to remove redundant patterns
   *
   * introduce matching constraints inside patterns
   */

  %strategy SimplifyMatch() extends Identity() {
    visit Term {
      // delete: a() << a() -> TrueMatch()
      s@Match(Appl(name,TermList()),Appl(name,TermList())) -> {
        Term res = `TrueMatch();
        debug("match delete",`s,res);
        return res;
      }

      // decompose: f(a1,...,an) << f(b1,...,bn) -> f(a1<<b1,...,an<<bn) 
      s@Match(Appl(f,arg1),Appl(f,arg2)) -> {
        TermList tl1 = `arg1;
        TermList tl2 = `arg2;
        TermList newarg = `TermList();
        while(!tl1.isEmptyTermList()) {
          Term h1 = tl1.getHeadTermList();
          Term h2 = tl2.getHeadTermList();
          newarg = `TermList(newarg*, Match(h1,h2));
          tl1 = tl1.getTailTermList();
          tl2 = tl2.getTailTermList();
        }
        Term res = `Appl(f,newarg);
        debug("match decompose",`s,res);
        return res;
      }

      // match: _ << f(...) -> TrueMatch()
      s@Match(Var(_),Appl(_,_)) -> {
        Term res = `TrueMatch();
        debug("match1",`s,res);
        return res;
      }

      // match: _ << _ -> TrueMatch()
      s@Match(Var(_),Var(_)) -> {
        Term res = `TrueMatch();
        debug("match2",`s,res);
        return res;
      }

      // symbol clash: f(t1,...,tn) -> g(u1,...,um) -> Empty
      s@Match(Appl(f,_),Appl(g,_)) && f!=g -> {
        Term res = `Empty();
        debug("match symbol clash",`s,res);
        return res;
      }

    }
  }

  %strategy PropagateTrueMatch() extends Identity() {
    visit Term {
      // f(TrueMatch,...,TrueMatch) -> TrueMatch
      s@Appl(f,argf@TermList(_,_*)) -> { // at least one argument
        boolean ok = true;
        %match(argf) {
          TermList(_*,!TrueMatch(),_*) -> {
            ok = false;
          }
        }
        
        if(ok) {
          Term res = `TrueMatch();
          debug("propagate match true",`s,res);
          return res;
        }
      }
    }
  }
  
  %strategy SimplifyAddMatch() extends Identity() {
    visit Term {
      // t + TrueMatch -> TrueMatch
      s@Add(ConcAdd(_*, TrueMatch(), _*)) -> {
        Term res = `TrueMatch();
        debug("elim TrueMatch",`s,res);
        return res;
      }

      // t1 << _ + t2 << _ -> (t1+t2) << _ if t1,t2 != _
      s@Add(ConcAdd(C1*, Match(t1@!Var[], Var("_")), C2*, Match(t2@!Var[], Var("_")), C3*)) -> {
        Term match = `Match(Add(ConcAdd(t1,t2)),Var("_"));
        Term res = `Add(ConcAdd(match, C1*,C2*,C3));
        debug("simplify add match",`s,res);
        return res;
      }
    }
  }
  
  public static boolean canBeRemoved2(Rule rule, RuleList ruleList, Signature eSig, Signature gSig) {
    boolean res = false;
    %match(rule) {
      Rule(lhs,rhs) -> {
        AddList constraint = `ConcAdd();
        %match(ruleList) {
          ConcRule(_*,Rule(l,r),_*) -> {
            constraint = `ConcAdd(Match(l, lhs),constraint*);
          }
        }
        Term matchingProblem = `Add(constraint);
        try {
          // PropagateTrueMatch()
          Strategy S2 = `ChoiceId(EmptyAdd2Empty(),PropagateEmpty(),PropagateTrueMatch(),SimplifyAddMatch(),SimplifyAdd(),SimplifyMatch(), TryAbstraction(eSig));
          matchingProblem = `InnermostId(S2).visitLight(matchingProblem);
          System.out.println("case = " + Pretty.toString(`lhs));
          System.out.println("matchingProblem = " + Pretty.toString(matchingProblem));
          if(matchingProblem == `TrueMatch()) {
            res = true;
          }
        } catch(VisitFailure e) {
          System.out.println("canBeremoves failure");
        }
      }
    }

    return res;
  }

  /*
   * examples
   */
  private static void example1() {
    Signature eSig = new Signature();
    Signature gSig = new Signature();

    Term V = `Var("_");
    Term X = `Var("x");
    Term Y = `Var("y");

    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("b", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("g", `ConcGomType(GomType("T")), `GomType("T") );
    eSig.addSymbol("f", `ConcGomType(GomType("T")), `GomType("T") );
    eSig.addSymbol("h", `ConcGomType(GomType("T"),GomType("T")), `GomType("T") );

    gSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    gSig.addSymbol("b", `ConcGomType(), `GomType("T") );
    gSig.addSymbol("g", `ConcGomType(GomType("T")), `GomType("T") );
    gSig.addSymbol("f", `ConcGomType(GomType("T")), `GomType("T") );
    eSig.addSymbol("h", `ConcGomType(GomType("T"),GomType("T")), `GomType("T") );

    Term a =`Appl("a", TermList());
    Term b =`Appl("b", TermList());
    Term ga =`Appl("g", TermList(a));
    Term gv =`Appl("g", TermList(V));

    Term fa =`Appl("f", TermList(a));
    Term fga =`Appl("f", TermList(ga));
    Term fgv =`Appl("f", TermList(gv));
    Term fv =`Appl("f", TermList(V));

    Term hxy =`Appl("h", TermList(X,Y));
    Term hab =`Appl("h", TermList(a,b));
    Term hba =`Appl("h", TermList(b,a));
    Term fhxy =`Appl("f", TermList(hxy));
    Term fhab =`Appl("f", TermList(hab));
    Term fhba =`Appl("f", TermList(hba));

    // f(g(x)) \ ( f(a) + f(g(a)) )
    //t = `Sub(fgv, Add(ConcAdd(fa,fga)));
    //t = `Sub(fv, Add(ConcAdd(fa,fga,fgv)));

    // X \ f(a + b)
    //     t = `Sub(V, Appl("f",TermList(Add(ConcAdd(a,b)))));
    //t = `Sub(V, Add(ConcAdd(fhba,fhab)));

//  t = (_ \ (f(h(b(),a())) + f(h(a(),b()))))
// t1 = (_ \ f((h(b(),a()) + h(a(),b()))))

    //type = `GomType("T");
    Term r0 = `Appl("rhs0",TermList());
    Term r1 = `Appl("rhr1",TermList());
    Term r2 = `Appl("rhs2",TermList());

    RuleList res = `trsRule(ConcRule(Rule(fhba,r0), Rule(fhab,r1), Rule(V,r2)), eSig,gSig);
  }
  
  private static void example2() {
    Signature eSig = new Signature();
    Signature gSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("Nil", `ConcGomType(), `GomType("List") );
    eSig.addSymbol("Cons", `ConcGomType(GomType("T"),GomType("List")), `GomType("List") );

    gSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    gSig.addSymbol("Nil", `ConcGomType(), `GomType("List") );
    gSig.addSymbol("Cons", `ConcGomType(GomType("T"),GomType("List")), `GomType("List") );
    gSig.addSymbol("sep", `ConcGomType(GomType("T"),GomType("List")), `GomType("List") );

    Term y_ys = `Appl("Cons", TermList(V,V));
    Term x_y_ys = `Appl("Cons", TermList(V,y_ys));
    Term p0 = `Appl("sep", TermList(V,x_y_ys));
    Term p1 = `Appl("sep", TermList(V,V));

    Term r0 = `Appl("rhs0",TermList());
    Term r1 = `Appl("rhs1",TermList());

    RuleList res = `trsRule(ConcRule(Rule(p0,r0), Rule(p1,r1)), eSig,gSig);

  }

  private static void example3() {
    Signature eSig = new Signature();
    Signature gSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    eSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );
    eSig.addSymbol("C", `ConcGomType(GomType("Nat")), `GomType("TT") );
    eSig.addSymbol("Bound", `ConcGomType(GomType("Nat")), `GomType("TT") );
    eSig.addSymbol("Neg", `ConcGomType(GomType("TT")), `GomType("TT") );
    eSig.addSymbol("Add", `ConcGomType(GomType("TT"),GomType("TT")), `GomType("TT") );
    eSig.addSymbol("Sub", `ConcGomType(GomType("TT"),GomType("TT")), `GomType("TT") );
    eSig.addSymbol("Mul", `ConcGomType(GomType("Nat"),GomType("TT")), `GomType("TT") );


    gSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    gSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );
    gSig.addSymbol("C", `ConcGomType(GomType("Nat")), `GomType("TT") );
    gSig.addSymbol("Bound", `ConcGomType(GomType("Nat")), `GomType("TT") );
    gSig.addSymbol("Neg", `ConcGomType(GomType("TT")), `GomType("TT") );
    gSig.addSymbol("Add", `ConcGomType(GomType("TT"),GomType("TT")), `GomType("TT") );
    gSig.addSymbol("Sub", `ConcGomType(GomType("TT"),GomType("TT")), `GomType("TT") );
    gSig.addSymbol("Mul", `ConcGomType(GomType("Nat"),GomType("TT")), `GomType("TT") );
    gSig.addSymbol("numadd", `ConcGomType(GomType("TT"),GomType("TT")), `GomType("TT") );

    Term pat = `Appl("Add",TermList(Appl("Mul",TermList(V,Appl("Bound",TermList(V)))),V));
    Term p1 = `Appl("numadd",TermList(pat,pat));
    Term p2 = `Appl("numadd",TermList(pat,V));
    Term p3 = `Appl("numadd",TermList(V,pat));
    Term p4 = `Appl("numadd",TermList(Appl("C",TermList(V)),Appl("C",TermList(V))));
    Term p5 = `Appl("numadd",TermList(V,V));

    Term r1 = `Appl("rhs1",TermList());
    Term r2 = `Appl("rhs2",TermList());
    Term r3 = `Appl("rhs3",TermList());
    Term r4 = `Appl("rhs4",TermList());
    Term r5 = `Appl("rhs5",TermList());

    RuleList res = `trsRule(ConcRule(Rule(p1,r1), Rule(p2,r2), Rule(p3,r3), Rule(p4,r4), Rule(p5,r5)), eSig,gSig);

  }


  private static void example4() {
    Signature eSig = new Signature();
    Signature gSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("True", `ConcGomType(), `GomType("Bool") );
    eSig.addSymbol("False", `ConcGomType(), `GomType("Bool") );
    eSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    eSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );
    eSig.addSymbol("Nil", `ConcGomType(), `GomType("List") );
    eSig.addSymbol("Cons", `ConcGomType(GomType("Val"),GomType("List")), `GomType("List") );
    eSig.addSymbol("Nv", `ConcGomType(GomType("Nat")), `GomType("Val") );
    eSig.addSymbol("Bv", `ConcGomType(GomType("Bool")), `GomType("Val") );
    eSig.addSymbol("Undef", `ConcGomType(), `GomType("Val") );


    gSig.addSymbol("True", `ConcGomType(), `GomType("Bool") );
    gSig.addSymbol("False", `ConcGomType(), `GomType("Bool") );
    gSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    gSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );
    gSig.addSymbol("Nil", `ConcGomType(), `GomType("List") );
    gSig.addSymbol("Cons", `ConcGomType(GomType("Val"),GomType("List")), `GomType("List") );
    gSig.addSymbol("Nv", `ConcGomType(GomType("Nat")), `GomType("Val") );
    gSig.addSymbol("Bv", `ConcGomType(GomType("Bool")), `GomType("Val") );
    gSig.addSymbol("Undef", `ConcGomType(), `GomType("Val") );

    gSig.addSymbol("interp", `ConcGomType(GomType("Nat"),GomType("List")), `GomType("Val") );

    Term nat0 = `Appl("Z",TermList());
    Term nat1 = `Appl("S",TermList(nat0));
    Term nat2 = `Appl("S",TermList(nat1));
    Term nat3 = `Appl("S",TermList(nat2));
    Term nat4 = `Appl("S",TermList(nat3));
    Term nat5 = `Appl("S",TermList(nat4));
    Term nat6 = `Appl("S",TermList(nat5));
    Term nv = `Appl("Nv",TermList(V));
    Term bv = `Appl("Bv",TermList(V));
    Term nil = `Appl("Nil",TermList());

    Term p0 = `Appl("interp",TermList(nat0,nil));
    Term p1 = `Appl("interp",TermList(nat1,Appl("Cons",TermList(nv,nil))));
    Term p2 = `Appl("interp",TermList(nat2,Appl("Cons",TermList(nv,Appl("Cons",TermList(nv,nil))))));
    Term p3 = `Appl("interp",TermList(nat3,nil));
    Term p4 = `Appl("interp",TermList(nat4,Appl("Cons",TermList(bv,nil))));
    Term p5 = `Appl("interp",TermList(nat5,Appl("Cons",TermList(bv,Appl("Cons",TermList(bv,nil))))));
    Term p6 = `Appl("interp",TermList(nat6,Appl("Cons",TermList(nv,Appl("Cons",TermList(nv,nil))))));
    Term p7 = `Appl("interp",TermList(V,V));

    Term r0 = `Appl("rhs0",TermList());
    Term r1 = `Appl("rhs1",TermList());
    Term r2 = `Appl("rhs2",TermList());
    Term r3 = `Appl("rhs3",TermList());
    Term r4 = `Appl("rhs4",TermList());
    Term r5 = `Appl("rhs5",TermList());
    Term r6 = `Appl("rhs6",TermList());
    Term r7 = `Appl("rhs7",TermList());
    RuleList res4 = `trsRule(ConcRule(Rule(p0,r0), Rule(p1,r1), Rule(p2,r2),
                                      Rule(p3,r3), Rule(p4,r4), Rule(p5,r5),
                                      Rule(p6,r6),Rule(p7,r7)),eSig,gSig);
    //RuleList res4 = `trsRule(ConcRule(Rule(p0,r0),Rule(p1,r1), Rule(p7,r7)),eSig,gSig);

//     //interp(S(Z()),Cons(Undef(),Cons(_,_)))
//     //interp(S(Z()),Cons(Undef(),Nil()))
//     //interp(S(Z()),Cons(Undef(),_))
//     Term t1 = `Appl("interp",TermList(Appl("S",TermList(Appl("Z",TermList()))),Appl("Cons",TermList(Appl("Undef",TermList()),Appl("Cons",TermList(Var("_"),Var("_")))))));
//       Term t2 = `Appl("interp",TermList(Appl("S",TermList(Appl("Z",TermList()))),Appl("Cons",TermList(Appl("Undef",TermList()),Appl("Nil",TermList())))));
//       Term t3 = `Appl("interp",TermList(Appl("S",TermList(Appl("Z",TermList()))),Appl("Cons",TermList(Appl("Undef",TermList()),Var("_")))));

//     //`reduce(Add(ConcAdd(t1,t2,t3)),eSig,gSig);

  }

  private static Term T(Term t1, Term t2, Term t3, Term t4) {
    return `Appl("T",TermList(t1,t2,t3,t4));
  }

  
  
  private static void example5() {
    Signature eSig = new Signature();
    Signature gSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    eSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );
    eSig.addSymbol("R", `ConcGomType(), `GomType("Color") );
    eSig.addSymbol("B", `ConcGomType(), `GomType("Color") );
    eSig.addSymbol("E", `ConcGomType(), `GomType("Tree") );
    eSig.addSymbol("T", `ConcGomType(GomType("Color"), GomType("Tree"), GomType("Nat"), GomType("Tree")), `GomType("Tree"));

    gSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    gSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );
    gSig.addSymbol("R", `ConcGomType(), `GomType("Color") );
    gSig.addSymbol("B", `ConcGomType(), `GomType("Color") );
    gSig.addSymbol("E", `ConcGomType(), `GomType("Tree") );
    gSig.addSymbol("T", `ConcGomType(GomType("Color"), GomType("Tree"), GomType("Nat"), GomType("Tree")), `GomType("Tree"));

    gSig.addSymbol("balance", `ConcGomType(GomType("Tree")), `GomType("Tree") );

    Term B = `Appl("B",TermList());
    Term R = `Appl("R",TermList());

    Term p0 = `Appl("balance", TermList(T(B,T(R,T(R,V,V,V),V,V),V,V)));
    Term p1 = `Appl("balance", TermList(T(B,T(R,V,V,T(R,V,V,V)),V,V)));
    Term p2 = `Appl("balance", TermList(T(B,V,V,T(R,T(R,V,V,V),V,V))));
    Term p3 = `Appl("balance", TermList(T(B,V,V,T(R,V,V,T(R,V,V,V)))));
    Term p4 = `Appl("balance", TermList(V));

    Term r0 = `Appl("rhs0",TermList());
    Term r1 = `Appl("rhs1",TermList());
    Term r2 = `Appl("rhs2",TermList());
    Term r3 = `Appl("rhs3",TermList());
    Term r4 = `Appl("rhs4",TermList());

    RuleList res = `trsRule(ConcRule(Rule(p0,r0), Rule(p1,r1), Rule(p2,r2),
                                      Rule(p3,r3), Rule(p4,r4)), eSig,gSig);
  }

  private static void example6() {
    Signature eSig = new Signature();
    Signature gSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("True", `ConcGomType(), `GomType("Bool") );
    eSig.addSymbol("False", `ConcGomType(), `GomType("Bool") );

    gSig.addSymbol("True", `ConcGomType(), `GomType("Bool") );
    gSig.addSymbol("False", `ConcGomType(), `GomType("Bool") );
    gSig.addSymbol("and", `ConcGomType(GomType("Bool"),GomType("Bool")), `GomType("Bool") );
    gSig.addSymbol("or",  `ConcGomType(GomType("Bool"),GomType("Bool")), `GomType("Bool") );

    Term True = `Appl("True",TermList());
    Term False = `Appl("False",TermList());

    Rule r0 = `Rule(Appl("and", TermList(True,True)), True);
    Rule r1 = `Rule(Appl("and", TermList(V,V)), False);

    Rule r2 = `Rule(Appl("or", TermList(False,False)), False);
    //Rule r3 = `Rule(Appl("or", TermList(V,V)), True);
    Rule r3 = `Rule(Add(ConcAdd( 
            Appl("or", TermList(False,True)), 
            Appl("or", TermList(True,False)), 
            Appl("or", TermList(True,True)))), True);

    RuleList res = trsRule(`ConcRule(r0,r1,r2,r3), eSig,gSig);
  }



}


