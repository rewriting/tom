package sa;

import sa.rule.types.*;
import tom.library.sl.*;
import java.util.HashSet;
import java.util.Set;

public class Trs {
  %include { rule/Rule.tom }
  %include { sl.tom }
  %include { java/util/types/HashSet.tom }
  %typeterm Signature { implement { Signature } }

  private static long timeSubElim;
  private static long timeAddElim;
  private static long timeExpand;
  private static long timeSubsumtion;
  private static long timeMinimize;

  private static void debug(String ruleName, Term input, Term res) {
    if(Main.options.debug && Main.options.verbose) {
    debugVerbose(ruleName,input,res);
    } else if(Main.options.debug) {
      System.out.println(ruleName);
    }

  }

  private static void debugVerbose(String ruleName, Term input, Term res) {
    System.out.println(ruleName + ": " + Pretty.toString(input) + " --> " + Pretty.toString(res));
  }

  /*
   * Transform a list of ordered rules into a TRS; rule by rule
   * input can contains: linear lhs rules with nested AP
   * result is LINEAR, without AP
   */
  public static RuleList trsRule(RuleList ruleList, Signature eSig) {
    assert Tools.isLhsLinear(ruleList) : "check lhs-linear";

    RuleList res = `ConcRule();
    %match(ruleList) {
      ConcRule(C1*,Rule(lhs,rhs),_*) -> {
        // build the collection of all  prevlhs = l1+l2+...+ln   before  rule=Rule(lhs,rhs)
        // the lhs of rule in the unordered TRS = lhs - prevlhs
        AddList prev = `ConcAdd();
        %match(C1) {
          ConcRule(_*,Rule(l,r),_*) -> {
            prev = `ConcAdd(l,prev*);
          }
        }
        Term pattern = `Sub(lhs,Add(prev*));
        if(Main.options.verbose) {
        System.out.println("\nPATTERN : " + Pretty.toString(pattern));
        }
        // transform   lhs - prevlhs   into a collection of patterns matching exactly the same terms
        Term t = `reduce(pattern,eSig);
        if(Main.options.verbose) {
          System.out.println("REDUCED : " + Pretty.toString(t));
        }

        // put back the rhs
        %match(t) {
          Add(ConcAdd(_*,e,_*)) -> {
            res = `ConcRule(res*,Rule(e,rhs));
          }

          e@(At|Appl|Var)[] -> {
            res = `ConcRule(res*,Rule(e,rhs));
          }
        }
      }
    }

    // remove x@t
    RuleCompiler ruleCompiler = new RuleCompiler(`eSig, `eSig); 
    res = ruleCompiler.expandAt(res);
    assert !Tools.containsAt(res) : "check contain no AT";

    // minimize the set of rules
    long startChrono = System.currentTimeMillis();
    res = removeRedundantRule(res,`ConcRule(),eSig);
    timeMinimize += (System.currentTimeMillis()-startChrono);

    if(Main.options.verbose) {
      for(Rule rule:`res.getCollectionConcRule()) {
        System.out.println(Pretty.toString(rule));
      }
      System.out.println("size = " + `res.length());
      System.out.println("subElim:       " + timeSubElim + " ms");
      System.out.println("addElim:       " + timeAddElim + " ms");
      System.out.println("expand:        " + timeExpand + " ms");
      System.out.println("subSubsumtion: " + timeSubsumtion + " ms");
      System.out.println("subMinimize:   " + timeMinimize + " ms");
    }

    assert !Tools.containsAP(res) : "check contain no AP";
    assert Tools.isLhsLinear(res) : "check lhs-linear";
    return res;
  }

  /*
   * Transform a term which contains Sub/Add into a list (top-level Add) of (linear) terms without Sub/Add
   * input may be non linear: X \ X for instance
   * result may also be non linear: f(X) + g(X)
   */
  private static Term reduce(Term t, Signature eSig) {
   long startChrono; 
    try {
      // DistributeAdd needed if we can start with terms like X \ f(a+b) or X \ (f(X)\f(f(_)))
      startChrono = System.currentTimeMillis();
      // pre-treatment: remove AP
      t = `InnermostId(ExpandAP()).visitLight(t);

      //Strategy S1 = `ChoiceId(CleanAdd(),PropagateEmpty(),SimplifySub(eSig),DistributeAdd());
      Strategy S1 = `ChoiceId(CleanAdd(),PropagateEmpty(),SimplifySub(eSig));
      t = `InnermostId(S1).visitLight(t);
      if(Main.options.verbose) {
        System.out.println("NO SUB = " + Pretty.toString(t));
      }
      timeSubElim += (System.currentTimeMillis()-startChrono);

      startChrono = System.currentTimeMillis();
      Strategy S2 = `ChoiceId(CleanAdd(),PropagateEmpty(), VarAdd(), FactorizeAdd());
      t = `InnermostId(S2).visitLight(t); // can be replaced by DistributeAdd() in S1
      if(Main.options.verbose) {
        System.out.println("NO ADD = " + Pretty.toString(t));
      }
      timeAddElim += (System.currentTimeMillis()-startChrono);
    } catch(VisitFailure e) {
      System.out.println("failure on: " + t);
    }

    startChrono = System.currentTimeMillis();
    t = expandAdd(t); // can be replaced by DistributeAdd() in S1
    if(Main.options.verbose) {
      System.out.println("EXPAND = " + Pretty.toString(t));
    }
    timeExpand += (System.currentTimeMillis()-startChrono);

    startChrono = System.currentTimeMillis();
    t = simplifySubsumtion(t);
    if(Main.options.verbose) {
      System.out.println("REMOVE SUBSUMTION = " + Pretty.toString(t));
    }
    timeSubsumtion += (System.currentTimeMillis()-startChrono);

    assert onlyTopLevelAdd(t) : "check only top-level Add";
    return t;
  }

  /*
   * remove redundant rules using a very smart matching algorithm :-)
   */
  // TODO: remove variables only once (and no longer in canBeRemove2) to speedup the process
  private static RuleList removeRedundantRule(RuleList candidates, RuleList kernel, Signature eSig) {
    assert Tools.isLhsLinear(candidates) : "check lhs-linear" ;
    RuleList res = kernel;

    %match( candidates ) {
      ConcRule(head, tail*) -> {
        // try with the head kept in kernel
        RuleList branch1 = removeRedundantRule(`tail, `ConcRule(kernel*,head), eSig);
        res = branch1;

        boolean b = canBeRemoved2(`head, `ConcRule(kernel*,tail*), eSig);
        if(b) {
          if(Main.options.verbose) {
            System.out.println("REMOVE: " + Pretty.toString(`head));
          }
          // try with the head removed 
          RuleList branch2 = removeRedundantRule(`tail, kernel, eSig);
          if(branch2.length() < branch1.length()) {
            res = branch2;
          }
        }
      }
    }
    return res;
  }

  %strategy PropagateEmpty() extends Identity() {
    visit Term {
      // f(t1,...,empty,...,tn) -> empty
      s@Appl(f,TermList(_*,Empty(),_*)) -> {
        Term res = `Empty();
        debug("propagate empty",`s,res);
        return res;
      }

      // HC: check where is this used; what happens if x in the RHS?
      // At(_,empty) -> empty
      s@At(_,Empty()) -> {
        Term res = `Empty();
        debug("_@empty",`s,res);
        return res;
      }

    }
  }

  /*
   * no longer needed
   * done in hooks
   */
  %strategy CleanAdd() extends Identity() {
    visit Term {
      // Add() -> empty
      s@Add(ConcAdd()) -> {
        Term res = `Empty();
        debug("elim ()",`s,res);
        return res;
      }

      // Add(t) -> t
      s@Add(ConcAdd(t)) -> {
        Term res = `t;
        debug("flatten2",`s,res);
        return res;
      }

      // t + empty -> t
      s@Add(ConcAdd(C1*,Empty(),C2*)) -> {
        Term res = `Add(ConcAdd(C1*,C2*));
        debug("elim empty",`s,res);
        return res;
      }

      // flatten: (a + (b + c) + d) -> (a + b + c + d)
      s@Add(ConcAdd(C1*, Add(ConcAdd(tl*)), C2*)) -> {
        Term res = `Add(ConcAdd(C1*,tl*,C2*));
        debug("flatten1",`s,res);
        return res;
      }

    }
  }

  %strategy FactorizeAdd() extends Identity() {
    visit Term {
      // f(t1,...,ti,...,tn) + f(t1,...,ti',...,tn) -> f(t1,..., ti + ti',...,tn)
      // all but one ti, ti' should be identical
      s@Add(ConcAdd(C1*, Appl(f,tl1), C2*, Appl(f, tl2), C3*)) -> {
        TermList tl = `addUniqueTi(tl1,tl2);
        if(tl != null) {
          Term res = `Add(ConcAdd(Appl(f,tl), C1*, C2*, C3*));
          debug("add merge",`s,res);
          return res;
        } else {
          //System.out.println("add merge failed");
        }
      }

    }
  }

  %strategy VarAdd() extends Identity() {
    visit Term {
      // a + x + b -> x
      s@Add(ConcAdd(_*, x@Var[], _*)) -> {
        Term res = `x;
        debug("a + x + b -> x",`s,res);
        return res;
      }
    }
  }

  %strategy ExpandAP() extends Identity() {
    visit Term {
      // !t -> X - t
      s@Anti(t) -> {
        Term Z = `Var(Tools.getName("Z"));
        Term res = `Sub(Z,t);
        debugVerbose("AP",`s,res);
        return res;
      }
    }
  }

  %strategy SimplifySub(eSig:Signature) extends Identity() {
    visit Term {

      // t - x -> empty
      s@Sub(t, Var[]) -> {
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
    
      // X - t -> expand AP   ==>    t should be in TFX (only symbols from declared signature)
      s@Sub(X@Var[], t@Appl(f,args_f)) -> {
        if(false && isPlainTerm(`t)) { // desactivate old version of X \ t
          RuleCompiler ruleCompiler = new RuleCompiler(`eSig, `eSig); // gSig
          RuleList rl = ruleCompiler.expandAntiPatterns(`ConcRule(Rule(Anti(t),Var("_"))));
          //System.out.println("rl = " + Pretty.toString(rl));
          AddList tl = `ConcAdd();
          GomType codomain = eSig.getCodomain(`f);
          %match(rl) {
            ConcRule(_*,Rule(lhs@Appl(g,args_g),rhs),_*) -> {
              tl = `ConcAdd(lhs,tl*); // order not preserved
            }
          }
          Term res = `Add(tl);
          res = eliminateIllTyped(res, codomain, `eSig);
          debug("expand AP",`s,res);
          // generate X@Add(tl)
          res = `At(X,res);
          return res;
        } else {
          // replace X \ t by X@((a+b+g(_)+f(_,_)) \ t)
          // this version is as efficient as the previous one, but simpler
          AddList al = `ConcAdd();
          GomType codomain = eSig.getCodomain(`f); // use codomain to generate well typed terms
          for(String name: eSig.getConstructors(codomain)) {
            int arity = eSig.getArity(name);
            Term expand = Tools.genAbstractTerm(name,arity, Tools.getName("Z"));
            al = `ConcAdd(expand,al*);
          }
          Term res = `Sub(Add(al),t);
          debug("expand AP2", `s,res);
          // generate X@Add(tl)
          res = `At(X,res);
          return res;
        }
      }

      // empty - f(t1,...,tn) -> empty
      s@Sub(Empty(),Appl(f,tl)) -> {
        Term res = `Empty();
        debug("empty - f(...) -> empty",`s,res);
        return res;
      }

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

      // f(t1,...,tn) - f(t1',...,tn') -> f(t1-t1',t2,...,tn) + f(t1, t2-t2',...,tn) + ... + f(t1,...,tn-tn')
      s@Sub(t1@Appl(f,tl1), t2@Appl(f, tl2)) -> {
        Term res = `sub(t1,t2);
        debug("sub1",`s,res);
        return res;
      }

      // x@t1 - t2 -> x@(t1 - t2)
      s@Sub(At(x,t1), t2) -> {
        Term res = `At(x,Sub(t1,t2));
        debug("at",`s,res);
        return res;
      }

      // t1 - x@t2 -> t1 - t2
      s@Sub(t1, At(x,t2)) -> {
        Term res = `Sub(t1,t2);
        debug("at2",`s,res);
        return res;
      }

    }
  }

  // f(a1,...,an) - f(b1,...,bn) -> f(a1-b1,..., an) + ... + f(a1,...,an-bn)
  private static Term sub(Term t1,Term t2) {
    %match(t1,t2) {
      Appl(f,args1), Appl(f,args2) -> {
        TermList tl1 = `args1;
        TermList tl2 = `args2;
        int len = tl1.length();
        TermList args[] = new TermList[len];
        for(int i=0 ; i<len ; i++) {
          args[i] = `TermList();
        }
        int cpt = 0;
        assert tl1.length() == tl2.length();
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
    if(l1==l2) {
      return l1;
    }
    TermList tl = `TermList();
    int cpt = 0;
    while(!tl1.isEmptyTermList() && cpt <= 1) {
      Term h1 = tl1.getHeadTermList();
      Term h2 = tl2.getHeadTermList();
      // we have to compare modulo renaming and AT
      if(h1 == h2) {
        tl = `TermList(tl*, h1);
      } else if(matchConstraint(h1,h2)==`TrueMatch() && matchConstraint(h2,h1)==`TrueMatch()) {
        // match(h1,h2) && match(h2,h1) is more efficient than removeVar(h1) == removeVar(h2)
        // PEM: check that we can keep h1 only
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
   * given a term (which contains Add and Sub) and a type
   * returns a term where badly typed terms are replaced by Empty()
   */
  private static Term eliminateIllTyped(Term t, GomType type, Signature eSig) {
    //System.out.println(Pretty.toString(t) + ":" + type.getName());
    %match(t) {
      v@Var[] -> {
        return `v;
      }

      Appl(f,args) -> {
        if(eSig.getCodomain(`f) == type) {
          GomTypeList domain = eSig.getDomain(`f);
          TermList tail = `args;
          TermList new_args = `TermList();
          while(!tail.isEmptyTermList()) {
            Term head = tail.getHeadTermList();
            GomType arg_type = domain.getHeadConcGomType();

            Term new_arg = eliminateIllTyped(head,arg_type,eSig);
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
        return `Sub(eliminateIllTyped(t1,type,eSig),eliminateIllTyped(t2,type,eSig));
      }

      Add(tl) -> {
        AddList tail = `tl;
        AddList res = `ConcAdd();
        while(!tail.isEmptyConcAdd()) {
          Term head = tail.getHeadConcAdd();
          res = `ConcAdd(eliminateIllTyped(head,type,eSig),res*);
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
*/
  %strategy DistributeAdd() extends Identity() {
    visit Term {

      // Add(t) -> t
      s@Add(ConcAdd(t)) -> {
        Term res = `t;
        //debug("flatten2",`s,res);
        return res;
      }

      // Z@(t1+t2) -> Z@t1 + Z@t2
      s@At(Z,Add(ConcAdd(head,tail*))) -> {
        Term res = `Add(ConcAdd(At(Z,head),At(Z,Add(tail))));
        return res;
      }

      // Z@(t1+t2) -> Z@t1 + Z@t2
      //s@At(Z,Add(tl)) -> {
      //  AddList tail = `tl;
      //  AddList res = `ConcAdd();
      //  while(!tail.isEmptyConcAdd()) {
      //    Term head = tail.getHeadConcAdd();
      //    res = `ConcAdd(At(Z,head),res*);
      //    tail = tail.getTailConcAdd();
      //  }
      //  return `Add(res);
      // }

      // f(t1,..., ti + ti',...,tn)  ->  f(t1,...,tn) + f(t1',...,tn')
      s@Appl(f, TermList(C1*, Add(ConcAdd(u,v,A2*)), C2*)) -> {
        Term res = `Add(ConcAdd(Appl(f, TermList(C1*,Add(ConcAdd(u,A2*)),C2*)), 
                                 Appl(f, TermList(C1*,Add(ConcAdd(v,A2*)),C2*))));
        //debug("distribute add",`s,res);
        return res;
      }

    }
  }


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
    HashSet<Term> todo2 = new HashSet<Term>();
    HashSet<Term> tmpC = new HashSet<Term>();
    todo.add(subject);
    while(todo.size() > 0) {
      todo2.clear();
      for(Term t:todo) {
        tmpC.clear();
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
      // swap todo, todo2
      HashSet<Term> tmp = todo;
      todo = todo2;
      todo2 = tmp;
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
        `Fail().visitLight(`subject);
      }
    }
  }

  /*
   * Given a list of terms
   * remove those which are subsumed by another ones
   */
  private static Term simplifySubsumtion(Term t) {
    %match(t) {
      Add(tl) -> {
        return `Add(simplifySubsumtionAux(tl, ConcAdd()));
      }
    }
    return t;
  }

  private static AddList simplifySubsumtionAux(AddList candidates, AddList kernel) {
    %match( candidates ) {
      ConcAdd(head, tail*) -> {
        boolean b = matchFromList(`head, `ConcAdd(tail*,kernel*));
        if(b) {
          return simplifySubsumtionAux(`tail, kernel);
        } else {
          return simplifySubsumtionAux(`tail, `ConcAdd(head,kernel*));
        }
      }
    }
    return kernel;
  }

  /*
   * simple but inefficient version
   *
  private static AddList simplifySubsumtion(AddList tl) {
    %match(tl) {
      ConcAdd(C1*,t1,C2*,t2,C3*) -> {
        if(`match(t1,t2)) {
          return simplifySubsumtion(`ConcAdd(t1,C1*,C2*,C3*));
        } else if(`match(t2,t1)) {
          return simplifySubsumtion(`ConcAdd(t2,C1*,C2*,C3*));
        }
      }
    }
    return tl;
  }
  */

  /*
   * return true is t can be match by a term of al
   */
  private static boolean matchFromList(Term t, AddList al) {
    %match(al) {
      ConcAdd(_*,p,_*) -> {
        /*
         * to use matchConstraint algorithm we need to activate hooks for
         * PropagateTrueMatch and PropagateEmpty
         */
        if(`matchConstraint(p,t) == `TrueMatch()) { // use this more general algorithm to factorize code
          return true;
        }
      }
    }
    return false;
  }

  /*
   * 1st idea to remove redundant patterns
   *
   * expand once all occurence of _ in a term
   */

/*
  private static Term expandVar(Term t, Signature eSig) {
    HashSet<Position> bag = new HashSet<Position>();
    HashSet<Term> res = new HashSet<Term>();

    GomType codomain = null;
    %match(t) {
      Appl(f,_) -> {
          codomain = eSig.getCodomain(`f);
      }
    }

    res.add(t);
    try {
      `TopDown(CollectVarPosition(bag)).visit(t);

      for(Position omega:bag) {
        HashSet<Term> todo = new HashSet<Term>();
        System.out.println("res.size  = " + res.size());
        for(Term subject:res) {
          // add g(Z1,...) ... h(Z1,...)
          for(String name: eSig.getConstructors()) {
            int arity = eSig.getArity(name);
            Term expand = Tools.genAbstractTerm(name,arity, "_");
            expand =  (Term) Tools.removeVar(expand);

            Term newt = (Term) omega.getReplace(expand).visit(subject);

            System.out.println("newt1 = " + Pretty.toString(newt));
            newt = eliminateIllTyped(newt, codomain, eSig);
            System.out.println("newt2 = " + Pretty.toString(newt));
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

    return `Add(al);
  }

  %strategy CollectVarPosition(c:HashSet) extends Identity() {
    visit Term {
      Var[] -> {
        c.add(getEnvironment().getPosition());
      }
    }
  }

  public static boolean canBeRemoved1(Rule rule, RuleList ruleList, Signature eSig) {
    %match(rule) {
      Rule(lhs,rhs) -> {
        System.out.println("CAN BE REMOVED 1 = " + Pretty.toString(`lhs));
        Term t = expandVar(`lhs,eSig);
        System.out.println("Expanded REMOVED 1 = " + Pretty.toString(t));
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

    System.out.println("BINGO 1 = ");
    return true;
  }
*/

  /*
   * 2nd idea to remove redundant patterns
   *
   * introduce matching constraints inside patterns
   */

  %strategy SimplifyMatch() extends Identity() {
    visit Term {
      // optim delete: t << t -> TrueMatch()
      s@Match(t,t) -> {
        Term res = `TrueMatch();
        debug("optim match delete",`s,res);
        return res;
      }

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
          newarg = `TermList(Match(h1,h2), newarg*); // build in reverse order
          tl1 = tl1.getTailTermList();
          tl2 = tl2.getTailTermList();
        }
        newarg = newarg.reverse();
        Term res = `Appl(f,newarg);
        debug("match decompose",`s,res);
        return res;
      }

      // match: _ << f(...) -> TrueMatch()
      s@Match(Var[],(Appl|Var)[]) -> {
        Term res = `TrueMatch();
        debug("match",`s,res);
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
      // f(TrueMatch,...,TrueMatch) -> TrueMatch with arity(f)>0
      s@Appl(f,argf@TermList(_,_*)) -> { // at least one argument
        boolean ok = true;
        TermList tl = `argf;
        while(ok && !tl.isEmptyTermList()) {
          ok &= (tl.getHeadTermList() == `TrueMatch());
          tl = tl.getTailTermList();
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

      s -> {
        assert !Tools.containsNamedVar(`s) : `s;
      }

      // t + TrueMatch -> TrueMatch
      s@Add(ConcAdd(_*, TrueMatch(), _*)) -> {
        Term res = `TrueMatch();
        debug("elim TrueMatch",`s,res);
        return res;
      }

      // t1 << _ + t2 << _ -> (t1+t2) << _ if t1,t2 != _
      s@Add(ConcAdd(C1*, Match(t1@!Var[], X@Var("_")), C2*, Match(t2@!Var[], Var("_")), C3*)) -> {
        Term match = `matchConstraint(Add(ConcAdd(t1,t2)),X);
        Term res = `Add(ConcAdd(match, C1*,C2*,C3));
        debug("simplify add match",`s,res);
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
          Set<String> foundConstructors = new HashSet<String>();
          for(Term t: `al.getCollectionConcAdd()) {
            // check that t is composed of variables only
            %match(t) {
              Appl(opname,!TermList(_*,!Var[],_*)) -> {
                // add f to set of names
                foundConstructors.add(`opname);
              }
            }
          }

          if(foundConstructors.equals(eSig.getConstructors(codomain))) {
            //System.out.println("OPS = " + ops + " al = " + `al);
            Term res = `Var("_");
            debug("abstraction",`s,res);
            return res;
          }
        } else {
          // do nothing
          //System.out.println("f -> null " + `f);
        }
      }
    }
  }

  public static boolean canBeRemoved2(Rule rule, RuleList ruleList, Signature eSig) {
    boolean res = false;

    rule = (Rule) Tools.removeVar(rule);
    ruleList = (RuleList) Tools.removeVar(ruleList);

    %match(rule) {
      Rule(lhs,rhs) -> {
        AddList constraint = `ConcAdd();
        %match(ruleList) {
          ConcRule(_*,Rule(l,r),_*) -> {
            Term mc = matchConstraint(`l, `lhs);
            if(mc == `TrueMatch()) {
              res = true;
              return res;
            } else {
              constraint = `ConcAdd(mc,constraint*);
            }
          }
        }
        Term matchingProblem = `Add(constraint);
        if(matchingProblem == `Empty()) {
          res = false;
          return res;
        }
        try {
          Strategy S2 = `ChoiceId(CleanAdd(),PropagateEmpty(),PropagateTrueMatch(),SimplifyAddMatch(),VarAdd(),FactorizeAdd(),SimplifyMatch(), TryAbstraction(eSig));
          Term sol = `InnermostId(S2).visitLight(matchingProblem);
          //sol = `RepeatId(OnceTopDownId(S2)).visitLight(matchingProblem);
          //System.out.println("case = " + Pretty.toString(`lhs));
          //System.out.println("matchingProblem = " + Pretty.toString(matchingProblem));
          if(sol == `TrueMatch()) {
            res = true;
          }
        } catch(VisitFailure e) {
          System.out.println("can be removed2 failure");
        }

      }
    }

    return res;
  }

/*
 * more efficient version of SimplifyMatch (factor 2!)
 */

  /*
   * Return TrueMatch() if t1 matches t2
   *        Empty()     if t1 does not match t2
   *        a constraint if no decision can be taken
   */
  // TODO: can be  used to replace both the function match(Term,Term) and the strategy SimplifyMatch()
  private static boolean newVersion = true;
  private static Term matchConstraint(Term t1, Term t2) {
    if(!newVersion) {
      return `Match(t1,t2);
    }

    %match(t1,t2) {
      // ElimAt (more efficient than removing AT before matching)
      At(_,p1), p2 -> { return `matchConstraint(p1,p2); }
      p1, At(_,p2) -> { return `matchConstraint(p1,p2); }

      // Delete
      Appl(name,TermList()),Appl(name,TermList()) -> { return `TrueMatch(); }
      // Decompose
      Appl(name,a1),Appl(name,a2) -> { return `propagateMatchConstraint(Appl(name,decomposeListConstraint(a1,a2,false))); }
      // SymbolClash
      Appl(name1,_),Appl(name2,_) && name1!=name2 -> { return `Empty(); }
      // Match
      Var[],(Var|Appl)[] -> { return `TrueMatch(); }
    }
    return `Match(t1,t2);
  }

  private static Term propagateMatchConstraint(Term t) {
    try {
      Strategy S = `ChoiceId(PropagateEmpty(),PropagateTrueMatch());
      return `InnermostId(S).visitLight(t);
    } catch(VisitFailure e) {
    }
    return t;
  }

  /*
   * return a list of matching constraints
   * TermList(Empty(),...,Empty()) when one of the constaint is Empty()
   */
  private static TermList decomposeListConstraint(TermList tl1, TermList tl2, boolean propagateEmpty) {
    %match(tl1,tl2) {
      TermList(), TermList() -> { return `TermList(); }
      TermList(head1,tail1*), TermList(head2,tail2*) -> { 
        if(propagateEmpty) {
          //return `TermList(Empty()); // optim: can be removed
          TermList tail = decomposeListConstraint(`tail1,`tail2,true);
          return `TermList(Empty(),tail*);
        }

        Term res = matchConstraint(`head1,`head2); 
        if(res == `Empty()) {
          //return `TermList(Empty()); // optim: can be removed
          TermList tail = decomposeListConstraint(`tail1,`tail2,true);
          return `TermList(Empty(),tail*);
        }

        TermList tail = decomposeListConstraint(`tail1,`tail2,propagateEmpty);
        return `TermList(res,tail*);
      }
    }
    assert false : "should not be there";
    return null;
  }

  /*
   * Transform an ordered TRS with non-linear lhs rules into an ordered TRS with left-LINEAR rules
   */
  public static RuleList transformNLOTRSintoLOTRS(RuleList ruleList, Signature gSig) {

    %match(ruleList) {
      ConcRule() -> {
        return ruleList;
      }

      ConcRule(Rule(lhs,rhs),tail*) -> {
        TermList result = Tools.linearize(`lhs, gSig);
        %match(result) {
          TermList(_, Appl("True",TermList())) -> {
            // lhs is linear
            RuleList newTail = Trs.transformNLOTRSintoLOTRS(`tail,gSig);
            RuleList res = `ConcRule(Rule(lhs,rhs), newTail*);
            assert Tools.isLhsLinear(res);
            return res;
          }

          TermList(newLhs@Appl(f,f_args), cond@!Appl("True",TermList())) -> {
            /*
             * lhs is non linear
             * f(t1_1,...,t1_n) -> rhs_1
             * ...
             * f(tm_1,...,tm_n) -> rhs_m
             * becomes (with t1'_1,...,t1'_n linear):
             *   f(t1'_1,...,t1'_n) -> f_1(t1'_1,...,t1'_n, true ^ constraint on non linear variables)
             *   f_1(t1'_1,...,t1'_n, true) -> rhs_1
             *   f_1(t2_1,...,t1_n, false) -> rhs_2  \
             *   ...                                  | apply the algorithm recursively on these rules
             *   f_1(tm_1,...,tm_n, false) -> rhs_m  /
             */
            String f_1 = Tools.getName(Tools.addAuxExtension(`f));
            GomTypeList f_domain = gSig.getDomain(`f);
            GomType f_codomain = gSig.getCodomain(`f);
            gSig.addFunctionSymbol(f_1,`ConcGomType(f_domain*,Signature.TYPE_BOOLEAN),f_codomain);
            Term newRhs = `Appl(f_1, TermList(f_args*,cond));
            int arity = gSig.getArity(`f);
            RuleList newTail = Trs.transformNLOTRSintoLOTRS(transformHeadSymbol(`tail,`f,f_1,arity),gSig);
            Rule trueCase = `Rule(Appl(f_1,TermList(f_args*,Appl("True",TermList()))),rhs);
            RuleList res = `ConcRule(Rule(newLhs,newRhs), trueCase, newTail*);
            assert Tools.isLhsLinear(res);
            return res;
          }
        }

      }
    }

    assert Tools.isLhsLinear(ruleList);
    return ruleList;
  }

  private static RuleList transformHeadSymbol(RuleList ruleList, String oldSymbol, String newSymbol, int old_arity) {
    %match(ruleList) {
      ConcRule() -> {
        return ruleList;
      }

      ConcRule(rule@Rule(Appl(f,f_args),rhs),tail*) -> {
        RuleList newTail = transformHeadSymbol(`tail,oldSymbol, newSymbol, old_arity);
        if(`f == oldSymbol) {
          return `ConcRule(Rule(Appl(newSymbol,TermList(f_args*,Appl("False",TermList()))),rhs),newTail*);
        } else {
          return `ConcRule(rule,newTail*);

        }
      }

      // special case for X -> rhs
      // generate f'(Z1,...,Zn,False) -> rhs
      //          X -> rhs
      ConcRule(rule@Rule(Var[],rhs),tail*) -> {
        RuleList newTail = transformHeadSymbol(`tail,oldSymbol, newSymbol, old_arity);
        TermList args = `TermList(Appl("False",TermList()));
        for(int i=0 ; i<old_arity ; i++) {
          args = `TermList(Var(Tools.getName("Z")),args*);
        }
        return `ConcRule(Rule(Appl(newSymbol,args),rhs),rule,newTail*);
      }

    }
    return ruleList;
  }

  /*
   * predicates for assert
   */

  /*
   * returns true if the term does not contain any Add or Sub
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

  /*
   * returns true if contains no Add/Sub (except at top level)
   */
  private static boolean onlyTopLevelAdd(Term subject) {
    %match(subject) {
      Add(tl) -> {
        boolean res = true;
        for(Term t:`tl.getCollectionConcAdd()) {
          res &= isPlainTerm(t);
        }
        return res;
      }
      Sub[] -> {
        return false;
      }
    }
    return true;
  }

}


