package sa;

import sa.rule.types.*;
import tom.library.sl.*;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class RewriteSystem {
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
  public static Trs trsRule(Trs trs, Signature eSig) {
    assert Property.isLhsLinear(trs) : "check lhs-linear";
    long startChrono; 

    RuleList res = `ConcRule();
    %match(trs) {
      (Trs|Otrs)(ConcRule(C1*,Rule(lhs,rhs),_*)) -> {
        // build the collection of all prev = l1+l2+...+ln   before  rule=Rule(lhs,rhs)
        // the lhs of rule in the unordered TRS = lhs - prev
        AddList prev = `ConcAdd();
        if(trs.isOtrs()) {
          %match(C1) {
            ConcRule(_*,Rule(l,r),_*) -> {
              prev = `ConcAdd(l,prev*);
            }
          }
        }

        Term t = `Sub(lhs,Add(prev*));
        if(Main.options.verbose) {
          System.out.println("\nPATTERN : " + Pretty.toString(t));
        }

        // transform lhs - prev into a collection of patterns matching exactly the same terms
        // pre-treatment: remove AP
        t = expandAP(t);
        // remove all Sub
        t = simplifySub(t,eSig);
        if(Main.options.verbose) {
          System.out.println("NO SUB = " + Pretty.toString(t));
        }

        startChrono = System.currentTimeMillis();
        t = simplifySubsumtion(t);
        timeSubsumtion += (System.currentTimeMillis()-startChrono);
        if(Main.options.verbose) {
          System.out.println("REMOVE SUBSUMTION = " + Pretty.toString(t));
        }

        if(Main.options.minimize) {
          // test new idea
          // quite slow
          t = simplifyAbstraction(t,eSig);
          t = simplifySubsumtion(t);
        }
        //assert Property.onlyTopLevelAdd(t) : "check only top-level Add";

        if(Main.options.verbose) {
          System.out.println("REDUCED : " + Pretty.toString(t));
        }

        RuleList rules = `ConcRule();
        // put back the rhs
        boolean flag = false;
        %match(t) {
          Add(ConcAdd(_*,e,_*)) -> {
            rules = `ConcRule(rules*,Rule(e,rhs));
            flag = true;
          }

          e@(At|Appl|Var)[] -> {
            rules = `ConcRule(rules*,Rule(e,rhs));
            flag = true;
          }

          _ -> {
            if(!flag) {
              System.out.println("WARNING NOT PUT BACK : " + Pretty.toString(t));
            }
          }
        }

        // TODO: normalize name of variables
        // remove x@t
        RuleCompiler ruleCompiler = new RuleCompiler(`eSig, `eSig); 
        rules = ruleCompiler.expandAt(rules);
        assert !Tools.containsAt(rules) : "check contain no AT";

        // minimize (local) the set of rules
        startChrono = System.currentTimeMillis();
        rules = removeRedundantRule(rules,eSig);
        timeMinimize += (System.currentTimeMillis()-startChrono);
        res = `ConcRule(res*,rules*);

      }
    }

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

    assert !Property.containsAP(res) : "check contain no AP";
    assert Property.isLhsLinear(res) : "check lhs-linear";
    if(trs.isOtrs()) {
      return `Otrs(res);
    } else {
      return `Trs(res);
    }
  }

  /*
   * Transform a term which contains Sub/Add into a term without Sub
   */
  private static LRUCache<Term,Term> cache = new LRUCache<Term,Term>(1000);
  private static Term simplifySub(Term t, Signature eSig) {
    Term res = cache.get(t);
    if(res!=null) {
      //System.out.println("cache hit");
      return res;
    } else {
      //System.out.println("nohit");
    }

    long startChrono = System.currentTimeMillis();
    try {
      Strategy S1 = `ChoiceId(PropagateEmpty(),SimplifySub(eSig),DistributeAdd());
      //Strategy S1 = `ChoiceId(PropagateEmpty(),SimplifySub(eSig),DistributeAdd());
      //Strategy S1 = `ChoiceId(PropagateEmpty(),SimplifySub(eSig));
      //Strategy S1 = `ChoiceId(CleanAdd(),PropagateEmpty(),SimplifySub(eSig),DistributeAdd());
      //Strategy S1 = `ChoiceId(CleanAdd(),PropagateEmpty(),SimplifySub(eSig));
      res = `InnermostId(S1).visitLight(t);
      cache.put(t,res);
      timeSubElim += (System.currentTimeMillis()-startChrono);
    } catch(VisitFailure e) {
      System.out.println("failure on: " + t);
    }
    return res;
  }


  /*
   * --------------------------------------------------
   * Rules for Add/Sub
   * --------------------------------------------------
   */

  %strategy PropagateEmpty() extends Identity() {
    visit Term {
      // f(t1,...,empty,...,tn) -> empty
      s@Appl(f,TermList(_*,Empty(),_*)) -> {
        assert !Property.containsSub(`s) : `s;
        Term res = `Empty();
        debug("propagate empty",`s,res);
        return res;
      }

      // HC: check where is this used; what happens if x in the RHS?
      // At(_,empty) -> empty
      s@At(_,Empty()) -> {
        assert !Property.containsSub(`s) : `s;
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
        assert !Property.containsSub(`s) : `s;
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

  /*
   * Transforms !p into Z \ p in t
   */
  private static Term expandAP(Term t) {
    try {
      t = `InnermostId(ExpandAP()).visitLight(t);
    } catch(VisitFailure e) {
      System.out.println("failure on: " + t);
    }
    return t;
  }

  %strategy ExpandAP() extends Identity() {
    visit Term {
      // !t -> X - t
      s@Anti(t) -> {
        Term Z = `Var(Tools.getName("Z"));
        Term res = `Sub(Z,t);
        debug("AP",`s,res);
        return res;
      }
    }
  }

  %strategy SimplifySub(eSig:Signature) extends Identity() {
    visit Term {

      // t - x -> empty
      s@Sub(t, Var[]) -> {
        assert !Property.containsSub(`t) : `t;
        Term res = `Empty();
        debug("t - x -> empty",`s,res);
        return res;
      }

      // t - empty -> t
      s@Sub(t, Empty()) -> {
        assert !Property.containsSub(`t) : `t;
        Term res = `t;
        debug("t - empty -> t",`s,res);
        return res;
      }
      
      // t - (a1 + ... + an) -> (t - a) - (a2 + ... + an))
      s@Sub(t, Add(ConcAdd(head,tail*))) -> {
        assert !Property.containsSub(`t) : `t;
        assert !Property.containsSub(`head) : `head;
        Term res = `Sub(Sub(t,head), Add(tail));
        debug("sub distrib1",`s,res);
        return res;
      }
    
      // X - t -> X@((a+b+g(_)+f(_,_)) \ t),  t should be in TFX (only symbols from declared signature)
      s@Sub(X@Var[], t@Appl(f,args_f)) -> {
        assert !Property.containsSub(`t) : `t;
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

      // empty - f(t1,...,tn) -> empty
      s@Sub(Empty(),u@Appl(f,tl)) -> {
        assert !Property.containsSub(`u) : `u;
        Term res = `Empty();
        debug("empty - f(...) -> empty",`s,res);
        return res;
      }

      // (a1 + ... + an) - t@f(t1,...,tn) -> (a1 - t) + ( (a2 + ... + an) - t )
      s@Sub(Add(ConcAdd(head,tail*)), t@Appl(f,tl)) -> {
        assert !Property.containsSub(`head) : `head;
        assert !Property.containsSub(`t) : `t;
        Term res = `Add(ConcAdd(Sub(head,t), Sub(Add(tail),t)));
        debug("sub distrib2",`s,res);
        return res;
      }

      // t@f(t1,...,tn) - g(t1',...,tm') -> t
      s@Sub(t@Appl(f,tl1), u@Appl(g, tl2)) && f!=g -> {
        assert !Property.containsSub(`t) : `t;
        assert !Property.containsSub(`u) : `u;
        Term res = `t;
        debug("sub elim1",`s,res);
        return res;
      }

      // f(t1,...,tn) - f(t1',...,tn') -> f(t1-t1',t2,...,tn) + f(t1, t2-t2',...,tn) + ... + f(t1,...,tn-tn')
      s@Sub(t1@Appl(f,tl1), t2@Appl(f, tl2)) -> {
        assert !Property.containsSub(`t1):`t1;
        assert !Property.containsSub(`t2):`t2;
        Term res = `subopt(t1,t2,eSig);
        debug("sub1",`s,res);
        return res;
      }

      // x@t1 - t2 -> x@(t1 - t2)
      s@Sub(At(x,t1), t2) -> {
        assert !Property.containsSub(`t1):`t1;
        assert !Property.containsSub(`t2):`t2;
        Term res = `At(x,Sub(t1,t2));
        debug("at",`s,res);
        return res;
      }

      // t1 - x@t2 -> t1 - t2
      s@Sub(t1, At(x,t2)) -> {
        assert !Property.containsSub(`t1):`t1;
        assert !Property.containsSub(`t2):`t2;
        Term res = `Sub(t1,t2);
        debug("at2",`s,res);
        return res;
      }

    }
  }

  // f(a1,...,an) - f(b1,...,bn) -> f(a1-b1,..., an) + ... + f(a1,...,an-bn)
  //                             -> f(a1,...,an) if ai\bi == ai
  private static Term subopt(Term t1,Term t2, Signature eSig) {
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
          /*
           * this is the optimization: returns f(a1,...,an) if ai\bi == ai
           */
          Term sub = simplifySub(`Sub(h1,h2),eSig); // do not use reduce because variables are normalized
          if(sub.equals(h1)) {
            return t1;
          }

          for(int i=0 ; i<len ; i++) {
            TermList tl = args[i]; // cannot use [] in `
            if(i==cpt) {
              tl = `TermList(tl*, sub);
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
    HashSet<Term> todo = new HashSet<Term>(); // terms to expand
    HashSet<Term> todo2 = new HashSet<Term>();
    HashSet<Term> tmpC = new HashSet<Term>(); // working memory when expanding a single term
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
          // t is a plain term
          t = Tools.normalizeVariable(t);
          c.add(t);
        } else {
          for(Term e:tmpC) {
            if(Property.isPlainTerm(e)) {
              e = Tools.normalizeVariable(e);
              c.add(e);
            } else {
              // todo list for the next round
              todo2.add(e);
            }
          }
        }
      }
      // all terms of toto have been expanded
      // swap todo, todo2
      HashSet<Term> tmp = todo;
      todo = todo2; // new terms which have to be expanded
      todo2 = tmp;  // reuse toto set, will be cleared next round
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
   * --------------------------------------------------
   * Syntactic matching
   * --------------------------------------------------
   */

  /*
   * Return TrueMatch() if t1 matches t2
   *        Empty()     if t1 does not match t2
   *        a constraint if no decision can be taken
   */
  private static Term matchConstraint(Term t1, Term t2) {
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
        if(res.isEmpty()) {
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
   * return true is t can be match by a term of al
   */
  private static boolean matchFromList(Term t, AddList al) {
    %match(al) {
      ConcAdd(_*,p,_*) -> {
        /*
         * to use matchConstraint algorithm we need to activate hooks for
         * PropagateTrueMatch and PropagateEmpty
         */
        if(`matchConstraint(p,t).isTrueMatch()) { // use this more general algorithm to factorize code
          return true;
        }
      }
    }
    return false;
  }

  %strategy PropagateTrueMatch() extends Identity() {
    visit Term {
      // f(TrueMatch,...,TrueMatch) -> TrueMatch with arity(f)>0
      s@Appl(f,argf@TermList(_,_*)) -> { // at least one argument
        boolean ok = true;
        TermList tl = `argf;
        while(ok && !tl.isEmptyTermList()) {
          ok &= (tl.getHeadTermList().isTrueMatch());
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
  
  /*
   * returns True if rule lhs->rhs is subsumed by ruleList
   * i.e. if lhs \ (l1 + ... + ln) = Empty
   */
  public static boolean canBeRemoved3(Rule rule, RuleList ruleList, Signature eSig) {
    boolean res = false;

    rule = (Rule) Tools.removeVar(rule);
    ruleList = (RuleList) Tools.removeVar(ruleList);

    %match(rule) {
      Rule(lhs,rhs) -> {
        AddList sum = `ConcAdd();
        %match(ruleList) {
          ConcRule(_*,Rule(l,r),_*) -> {
            sum = `ConcAdd(l,sum*);
          }
        }
        Term problem = `Sub(lhs,Add(sum));
        Term t = `simplifySub(problem,eSig);
        if(t.isEmpty()) {
          res = true;
          return res;
        }
      }
    }

    return res;
  }

  /*
   * --------------------------------------------------
   * Minimum (local) of a set of rules
   * --------------------------------------------------
   */

  /*
   * remove redundant rules 
   */
  // TODO: remove variables only once (and no longer in canBeRemoved2) to speedup the process
  private static RuleList removeRedundantRule(RuleList candidates, Signature eSig) {
    RuleList kernel  = `ConcRule(); // rules which are not subsumed by the others
    RuleList start  = `ConcRule();
    %match(candidates) {
      ConcRule(C1*,r@Rule(lhs,rhs),C2*) -> {
        boolean b = canBeRemoved3(`r, `ConcRule(C1*,C2*), eSig);
        if(!b) {
          kernel = `ConcRule(r,kernel*);
        } else {
          start = `ConcRule(r,start*);
        }
      }
    }

    RuleList result = removeRedundantRuleAux(start,kernel,eSig);
    return result;
  }

  private static RuleList removeRedundantRuleAux(RuleList candidates, RuleList kernel, Signature eSig) {
    assert Property.isLhsLinear(candidates) : "check lhs-linear" ;
    RuleList res = kernel;

    %match( candidates ) {
      ConcRule(head, tail*) -> {
        // try with the head kept in kernel
        RuleList branch1 = removeRedundantRuleAux(`tail, `ConcRule(kernel*,head), eSig);
        res = branch1;

        boolean b = canBeRemoved3(`head, `ConcRule(kernel*,tail*), eSig);
        if(b) {
          if(Main.options.verbose) {
            System.out.println("REMOVE: " + Pretty.toString(`head));
          }
          // try with the head removed 
          RuleList branch2 = removeRedundantRuleAux(`tail, kernel, eSig);
          if(branch2.length() < branch1.length()) {
            res = branch2;
          }
        }
      }
    }
    return res;
  }

  /*
   * --------------------------------------------------
   * Search abstractions to compute a minimum global
   * --------------------------------------------------
   */

  /*
   * given t1+...+tn, add all abstractions t' such that [t'] \subseteq [t1+...+tn]
   */
  private static Term simplifyAbstraction(Term sum, Signature eSig) {
    System.out.println("simplifyAbstraction");
    HashSet<Term> bag = new HashSet<Term>();
    %match(sum) {
      Add(tl) -> {
        AddList res = `tl; 
        %match(tl) {
          ConcAdd(_*,t,_*) -> {
            searchAbstractionTerm(bag,`t,sum,eSig);
          }
        }
        for(Term t:bag) {
          System.out.println("new candidate: " + Pretty.toString(t));
          res = `ConcAdd(t,res*);
        }
        return `Add(res);
      }
    }
    return sum;
  }

  /*
   * given subject and sum
   * search abstractions of subject such that [subject] \subseteq [sum]
   */
  public static void searchAbstractionTerm(HashSet<Term> c, Term subject, Term sum, Signature eSig) {
    HashSet<Term> saturate = new HashSet<Term>();
    generateAbstraction(saturate,subject);
    //System.out.println("#saturate = " + saturate.size());
    // TODO: order saturate to start with least general terms
    /*
    List<Term> list = new ArrayList<Term>(saturate);
    java.util.Comparator<Term> cmp = new java.util.Comparator<Term>() {
      public int compare(Term t1, Term t2) {
        if(matchConstraint(t1,t2)==`TrueMatch()) {
          return 1;
        } else if(matchConstraint(t2,t1)==`TrueMatch()) {
          return -1;
        } else {
          return 0;
        }
      }
    };

    java.util.Collections.sort(list, cmp);
     */
    //subject = (Term)Tools.removeAt(subject); // don't remove at to keep variable name used in rhs
    //subject = Tools.normalizeVariable(subject);
    //System.out.println("for: = " + Pretty.toString(subject));

    for(Term t:saturate) { // list / saturate
      //t = (Term)Tools.removeAt(t);
      //t = Tools.normalizeVariable(t);
      Term problem = `Sub(t,sum);
      //System.out.println("try: " + Pretty.toString(t));
      Term res = simplifySub(problem,eSig);
      res = (Term)Tools.removeAt(res);
      res = Tools.normalizeVariable(res);
      //System.out.println("res = " + Pretty.toString(res));
      if(res.isEmpty()) {
        //System.out.println("new candidate: " + Pretty.toString(t));
        c.add(t);
        // continue the search for a more general term
      } else {
        // stop the search
        //return;
      }
    }

  }

  /*
   * given subject
   * generate all possible abstractions (no order)
   */
  private static void generateAbstraction(HashSet<Term> c, Term subject) {
    HashSet<Term> todo = new HashSet<Term>(); // terms to expand
    HashSet<Term> todo2 = new HashSet<Term>();
    HashSet<Term> tmpC = new HashSet<Term>(); // working memory when expanding a single term
    todo.add(subject);
    while(todo.size() > 0) {
      todo2.clear();
      for(Term t:todo) {
        tmpC.clear();
        try {
          // TopDownCollect: apply s1 in a top-down way, s should extends the identity
          // a failure stops the top-down process under this current node
          `TopDownCollect(GenerateAbstraction(tmpC,t)).visit(t);
        } catch(VisitFailure e) {
        }
        if(tmpC.isEmpty()) {
          // t is a plain term
          c.add(t);
        } else {
          for(Term e:tmpC) {
            if(Property.isPlainTerm(e)) {
              c.add(e);
            } else {
              // todo list for the next round
              todo2.add(e);
            }
          }
        }
      }
      // all terms of toto have been expanded
      // swap todo, todo2
      HashSet<Term> tmp = todo;
      todo = todo2; // new terms which have to be expanded
      todo2 = tmp;  // reuse toto set, will be cleared next round
      //System.out.println("size(c) = " + c.size());
    }

  }

  %strategy GenerateAbstraction(c:HashSet, subject:Term) extends Identity() {
    visit Term {
      s@At(var,Appl(_,_)) -> {
        Term newt = (Term) getEnvironment().getPosition().getReplace(`var).visit(subject);
        c.add(newt);
        //`Fail().visitLight(`s); // stop collect under this node
      }

      s@Appl(_,_) -> {
        Term newt = (Term) getEnvironment().getPosition().getReplace(`Var(Tools.getName("Z"))).visit(subject);
        c.add(newt);
        //`Fail().visitLight(`s); // stop collect under this node
      }
    }
  }




  /*
   * Transform an ordered TRS with non-linear lhs rules into an ordered TRS with left-LINEAR rules
   */
  public static Trs transformNLOTRSintoLOTRS(Trs trs, Signature gSig) {

    %match(trs) {
      s@Otrs(ConcRule()) -> {
        return `s;
      }

      Otrs(ConcRule(Rule(lhs,rhs),tail*)) -> {
        TermList result = Tools.linearize(`lhs, gSig);
        %match(result) {
          TermList(_, Appl("True",TermList())) -> {
            // lhs is linear
            RuleList newTail = transformNLOTRSintoLOTRS(`Otrs(tail),gSig).getlist();
            RuleList res = `ConcRule(Rule(lhs,rhs), newTail*);
            assert Property.isLhsLinear(res);
            return `Otrs(res);
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
            RuleList newTail = transformNLOTRSintoLOTRS(`Otrs(transformHeadSymbol(tail,f,f_1,arity)),gSig).getlist();
            Rule trueCase = `Rule(Appl(f_1,TermList(f_args*,Appl("True",TermList()))),rhs);
            RuleList res = `ConcRule(Rule(newLhs,newRhs), trueCase, newTail*);
            assert Property.isLhsLinear(res);
            return `Otrs(res);
          }
        }

      }
    }

    assert Property.isLhsLinear(trs);
    return trs;
  }

  private static RuleList transformHeadSymbol(RuleList ruleList, String oldSymbol, String newSymbol, int old_arity) {
    %match(ruleList) {
      ConcRule() -> {
        return ruleList;
      }

      ConcRule(rule@Rule(Appl(f,f_args),rhs),tail*) -> {
        RuleList newTail = transformHeadSymbol(`tail,oldSymbol, newSymbol, old_arity);
        if(`f.equals(oldSymbol)) {
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

}
