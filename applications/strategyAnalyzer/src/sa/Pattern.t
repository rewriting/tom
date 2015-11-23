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
//      example4(); // interp
//     example5(); // balance
//     example6(); // and-or
    example7(); // simplest reduce
//     example7bis(); // simplest reduce with one type
//    example8(); // reduce deeper
  }

  /*
   * Transform a list of ordered rules into a TRS; rule by rule
   */
  public static RuleList trsRule(RuleList ruleList, Signature eSig) {

    //ruleList = (RuleList) removeVar(ruleList);

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
        System.out.println("\nPATTERN : " + Pretty.toString(pattern));
        // transform   lhs - prevlhs   into a collection of patterns matching exactly the same terms
        Term t = `reduce(pattern,eSig);
        System.out.println("REDUCED : " + Pretty.toString(t));

        // put back the rhs
        %match(t) {
          Add(ConcAdd(_*,e,_*)) -> {
            res = `ConcRule(res*,Rule(e,rhs));
          }

          e@(Appl|Var)[] -> {
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
    res = removeRedundantRule(res,eSig);

    for(Rule rule:`res.getCollectionConcRule()) {
      System.out.println(Pretty.toString(rule));
    }
    System.out.println("size = " + `res.length());


    return res;
  }

  private static Term reduce(Term t, Signature eSig) {
    try {
      // DistributeAdd needed if we can start with terms like X \ f(a+b) or X \ (f(X)\f(f(_)))
      Strategy S1 = `ChoiceId(CleanAdd(),PropagateEmpty(),SimplifySub(eSig));
      t = `InnermostId(S1).visitLight(t);
      System.out.println("NO SUB = " + Pretty.toString(t));

      Strategy S2 = `ChoiceId(CleanAdd(),PropagateEmpty(), VarAdd(), FactorizeAdd());
      t = `InnermostId(S2).visitLight(t);
      System.out.println("NO ADD = " + Pretty.toString(t));
    } catch(VisitFailure e) {
      System.out.println("failure on: " + t);
    }

    t = expandAdd(t);
    System.out.println("EXPAND = " + Pretty.toString(t));

    t = simplifySubsumtion(t);
    System.out.println("REMOVE SUBSUMTION = " + Pretty.toString(t));

    return t;
  }

  /*
   * remove redundant rules using a very smart matching algorithm :-)
   */
  private static RuleList removeRedundantRule(RuleList rules, Signature eSig) {
    return removeRedundantRuleAux(rules,`ConcRule(), eSig);
  }

  // TODO: remove variables only once (and no longer in canBeRemove2) to speedup the process
  private static RuleList removeRedundantRuleAux(RuleList candidates, RuleList kernel, Signature eSig) {
    HashSet<RuleList> bag = new HashSet<RuleList>();

    %match( candidates ) {
      ConcRule(head, tail*) -> {
        boolean b = canBeRemoved2(`head, `ConcRule(tail*,kernel*), eSig);
        RuleList res = removeRedundantRuleAux(`tail, `ConcRule(head,kernel*), eSig);
        if(b) {
          System.out.println("REMOVE: " + Pretty.toString(`head));
          // try with the head removed 
         RuleList tmp = removeRedundantRuleAux(`tail, kernel, eSig);
         if(tmp.length() < res.length()) {
             return tmp;
         }
         // bag.add(removeRedundantRuleAux(`tail, kernel, eSig));

          // uncomment the following return for a greedy algorithm:
          //return removeRedundantRule(`ConcRule(C1*,C2*), eSig);

        }
        return res;

        // try with the head kept in kernel
        //bag.add(removeRedundantRuleAux(`tail, `ConcRule(head,kernel*), eSig));
      }
    }
/*
    RuleList minrules = `ConcRule(candidates*,kernel*);
    int minlength = minrules.length();
    for(RuleList e:bag) {
      if(e.length() < minlength) {
        minrules = e;
        minlength = minrules.length();
      }
    }

    return minrules;
    */
    return `ConcRule(candidates*,kernel*);
  }

  %strategy PropagateEmpty() extends Identity() {
    visit Term {
      // f(t1,...,empty,...,tn) -> empty
      s@Appl(f,TermList(_*,Empty(),_*)) -> {
        Term res = `Empty();
        debug("propagate empty",`s,res);
        return res;
      }

      // HC: chechk where is this used; what happens if x in the RHS?
      // At(x,empty) -> empty
      s@At(x,Empty()) -> {
        Term res = `Empty();
        debug("x@empty",`s,res);
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
        if(isPlainTerm(`t)) {
          RuleCompiler ruleCompiler = new RuleCompiler(`eSig, `eSig); // gSig
          RuleList rl = ruleCompiler.expandAntiPatterns(`ConcRule(Rule(Anti(t),Var("_"))));
          //System.out.println("rl = " + Pretty.toString(rl));
          AddList tl = `ConcAdd();
          GomType codomain = eSig.getCodomain(`f);
          %match(rl) {
            ConcRule(_*,Rule(lhs@Appl(g,args_g),rhs),_*) -> {
              //if(eSig.getCodomain(`g) == codomain) { // optim: remove ill typed terms 
                //tl = `ConcAdd((Term)removeVar(lhs),tl*); // order not preserved
                tl = `ConcAdd(lhs,tl*); // order not preserved
              //}
            }
          }
          Term res = `Add(tl);
          debug("expand AP",`s,res);

          res = eliminateIllTyped(res, codomain, `eSig);

          // PEM: we should generate X@Add(tl)
          res = `At(X,res);

          return res;
        }
      }

      // empty - f(t1,...,tn) -> empty
      s@Sub(Empty(),Appl(f,tl)) -> {
        Term res = `Empty();
        debug("empty - f(...) -> empty",`s,res);
        return `Empty();
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
      } else if(match(h1,h2) && match(h2,h1)) {
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
   * Replace a named variable by an underscore
   */
  %strategy RemoveVar() extends Identity() {
    visit Term {
      Var[] -> {
        return `Var("_");
      }

      At(_,t) -> {
        return `t;
      }
    }
  }

  private static tom.library.sl.Visitable removeVar(tom.library.sl.Visitable t) {
    try {
      return `TopDown(RemoveVar()).visitLight(t);
    } catch(VisitFailure e) {
      throw new RuntimeException("should not be there");
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
        boolean b = match(`head, `ConcAdd(tail*,kernel*));
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
   * Return true if t1 matches t2
   */
  private static boolean match(Term t1, Term t2) {
    //assert !Tools.containsAt(t1) : "check contain no AT";
    //assert !Tools.containsAt(t2) : "check contain no AT";

    %match(t1,t2) {
      // ElimAt (more efficient than removing AT before matching)
      At(_,p1), p2 -> { return `match(p1,p2); }
      p1, At(_,p2) -> { return `match(p1,p2); }

      // Delete
      Appl(name,TermList()),Appl(name,TermList()) -> { return true; }
      // Decompose
      Appl(name,a1),Appl(name,a2) -> { return `decomposeList(a1,a2); }
      // SymbolClash
      Appl(name1,args1),Appl(name2,args2) && name1!=name2 -> { return false; }

      // Match
      Var[],Appl(name2,args2) -> { return true; }
      Var[],Var[] -> { return true; }

    }
    return false;
  }

  /*
   * return true is t can be match by a term of al
   */
  private static boolean match(Term t, AddList al) {
    //t = (Term) removeVar(t);
    //al = (AddList) removeVar(al);

    %match(al) {
      ConcAdd(_*,p,_*) -> {
        if(`match(p,t)) {
          return true;
        }
      }
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
  private  static Term expandVar(Term t, Signature eSig) {
    HashSet<Position> bag = new HashSet<Position>();
    HashSet<Term> res = new HashSet<Term>();

    GomType codomain = null;
    %match(t) {
      Appl(f,_) -> {
          codomain = eSig.getCodomain(`f);
      }
    }

    //System.out.println("t = " + t);
    //System.out.println("codomain = " + codomain);

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
            expand =  (Term) removeVar(expand);

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
/*
        %match(argf) {
          TermList(_*,!TrueMatch(),_*) -> {
            ok = false;
          }
        }
  */      
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
        Term match = `Match(Add(ConcAdd(t1,t2)),X);
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

    rule = (Rule) removeVar(rule);
    ruleList = (RuleList) removeVar(ruleList);

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
          Strategy S2 = `ChoiceId(CleanAdd(),PropagateEmpty(),PropagateTrueMatch(),SimplifyAddMatch(),VarAdd(),FactorizeAdd(),SimplifyMatch(), TryAbstraction(eSig));
          matchingProblem = `InnermostId(S2).visitLight(matchingProblem);
          System.out.println("case = " + Pretty.toString(`lhs));
          System.out.println("matchingProblem = " + Pretty.toString(matchingProblem));
          if(matchingProblem == `TrueMatch()) {
            res = true;
            //System.out.println("BINGO");
          }
        } catch(VisitFailure e) {
          System.out.println("can be removed2 failure");
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

    Term V = `Var("_");
    Term X = `Var("x");
    Term Y = `Var("y");

    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("b", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("g", `ConcGomType(GomType("T")), `GomType("T") );
    eSig.addSymbol("f", `ConcGomType(GomType("T")), `GomType("T") );
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

    RuleList res = `trsRule(ConcRule(Rule(fhba,r0), Rule(fhab,r1), Rule(V,r2)), eSig);
  }
  
  private static void example2() {
    Signature eSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("Nil", `ConcGomType(), `GomType("List") );
    eSig.addSymbol("Cons", `ConcGomType(GomType("T"),GomType("List")), `GomType("List") );
    eSig.addSymbol("sep", `ConcGomType(GomType("T"),GomType("List")), `GomType("List") );
    eSig.setFunction("sep");

    Term y_ys = `Appl("Cons", TermList(V,V));
    Term x_y_ys = `Appl("Cons", TermList(V,y_ys));
    Term p0 = `Appl("sep", TermList(V,x_y_ys));
    Term p1 = `Appl("sep", TermList(V,V));

    Term r0 = `Appl("rhs0",TermList());
    Term r1 = `Appl("rhs1",TermList());

    RuleList res = `trsRule(ConcRule(Rule(p0,r0), Rule(p1,r1)), eSig);

  }

  private static void example3() {
    Signature eSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    eSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );
    eSig.addSymbol("C", `ConcGomType(GomType("Nat")), `GomType("TT") );
    eSig.addSymbol("Bound", `ConcGomType(GomType("Nat")), `GomType("TT") );
    eSig.addSymbol("Neg", `ConcGomType(GomType("TT")), `GomType("TT") );
    eSig.addSymbol("Add", `ConcGomType(GomType("TT"),GomType("TT")), `GomType("TT") );
    eSig.addSymbol("Sub", `ConcGomType(GomType("TT"),GomType("TT")), `GomType("TT") );
    eSig.addSymbol("Mul", `ConcGomType(GomType("Nat"),GomType("TT")), `GomType("TT") );
    eSig.addSymbol("numadd", `ConcGomType(GomType("TT"),GomType("TT")), `GomType("TT") );
    eSig.setFunction("numadd");

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

    RuleList res = `trsRule(ConcRule(Rule(p1,r1), Rule(p2,r2), Rule(p3,r3), Rule(p4,r4), Rule(p5,r5)), eSig);

  }


  private static void example4() {
    Signature eSig = new Signature();

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

    eSig.addSymbol("interp", `ConcGomType(GomType("Nat"),GomType("List")), `GomType("Val") );
    eSig.setFunction("interp");

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
                                      Rule(p6,r6),Rule(p7,r7)),eSig);
    //RuleList res4 = `trsRule(ConcRule(Rule(p0,r0),Rule(p1,r1), Rule(p7,r7)),eSig);

//     //interp(S(Z()),Cons(Undef(),Cons(_,_)))
//     //interp(S(Z()),Cons(Undef(),Nil()))
//     //interp(S(Z()),Cons(Undef(),_))
//     Term t1 = `Appl("interp",TermList(Appl("S",TermList(Appl("Z",TermList()))),Appl("Cons",TermList(Appl("Undef",TermList()),Appl("Cons",TermList(Var("_"),Var("_")))))));
//       Term t2 = `Appl("interp",TermList(Appl("S",TermList(Appl("Z",TermList()))),Appl("Cons",TermList(Appl("Undef",TermList()),Appl("Nil",TermList())))));
//       Term t3 = `Appl("interp",TermList(Appl("S",TermList(Appl("Z",TermList()))),Appl("Cons",TermList(Appl("Undef",TermList()),Var("_")))));

//     //`reduce(Add(ConcAdd(t1,t2,t3)),eSig);

  }

  private static Term T(Term t1, Term t2, Term t3, Term t4) {
    return `Appl("T",TermList(t1,t2,t3,t4));
  }

  
  
  private static void example5() {
    Signature eSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    eSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );
    eSig.addSymbol("R", `ConcGomType(), `GomType("Color") );
    eSig.addSymbol("B", `ConcGomType(), `GomType("Color") );
    eSig.addSymbol("E", `ConcGomType(), `GomType("Tree") );
    eSig.addSymbol("T", `ConcGomType(GomType("Color"), GomType("Tree"), GomType("Nat"), GomType("Tree")), `GomType("Tree"));
    eSig.addSymbol("balance", `ConcGomType(GomType("Tree")), `GomType("Tree") );
    eSig.setFunction("balance");

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
                                      Rule(p3,r3), Rule(p4,r4)), eSig);
  }

  private static void example6() {
    Signature eSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("True", `ConcGomType(), `GomType("Bool") );
    eSig.addSymbol("False", `ConcGomType(), `GomType("Bool") );
    eSig.addSymbol("and", `ConcGomType(GomType("Bool"),GomType("Bool")), `GomType("Bool") );
    eSig.addSymbol("or",  `ConcGomType(GomType("Bool"),GomType("Bool")), `GomType("Bool") );
    eSig.setFunction("and");
    eSig.setFunction("or");

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

    RuleList res = trsRule(`ConcRule(r0,r1,r2,r3), eSig);
  }


  private static void example7() {
    Signature eSig = new Signature();

    Term V = `Var("_");

    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("b", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("f", `ConcGomType(GomType("T"),GomType("T")), `GomType("U") );
    eSig.setFunction("f");

    Term a =`Appl("a", TermList());
    Term b =`Appl("b", TermList());

    Term fav =`Appl("f", TermList(a,V));
    Term fbv =`Appl("f", TermList(b,V));
    Term fva =`Appl("f", TermList(V,a));

    Term r0 = `Appl("rhs0",TermList());

    RuleList res = `trsRule(ConcRule(Rule(fav,r0), Rule(fbv,r0), Rule(fva,r0)), eSig);
  }

  private static void example7bis() {
    Signature eSig = new Signature();

    Term V = `Var("_");

    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("b", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("f", `ConcGomType(GomType("T"),GomType("T")), `GomType("T") );

    Term a =`Appl("a", TermList());
    Term b =`Appl("b", TermList());

    Term fvv =`Appl("f", TermList(V,V));
    Term fav =`Appl("f", TermList(a,V));
    Term fbv =`Appl("f", TermList(b,V));
    Term ffv =`Appl("f", TermList(fvv,V));
    Term fva =`Appl("f", TermList(V,a));

    Term r0 = `Appl("rhs0",TermList());

    RuleList res = `trsRule(ConcRule(Rule(fav,r0), Rule(ffv,r0), Rule(fbv,r0), Rule(fva,r0)), eSig);
  }


  private static void example8() {
    Signature eSig = new Signature();

    Term V = `Var("_");

    eSig.addSymbol("a", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("b", `ConcGomType(), `GomType("T") );
    eSig.addSymbol("g", `ConcGomType(GomType("T")), `GomType("T") );
    eSig.addSymbol("f", `ConcGomType(GomType("T"),GomType("T")), `GomType("U") );

    Term a =`Appl("a", TermList());
    Term b =`Appl("b", TermList());

    Term gv =`Appl("g", TermList(V));
    Term ga =`Appl("g", TermList(a));
    Term gb =`Appl("g", TermList(b));
    Term gg =`Appl("g", TermList(gv));

    Term fgaa =`Appl("f", TermList(ga,a));
    Term fgba =`Appl("f", TermList(gb,a));
    Term fgga =`Appl("f", TermList(gg,a));
    Term faa =`Appl("f", TermList(a,a));
    Term fba =`Appl("f", TermList(b,a));
    Term fva =`Appl("f", TermList(V,a));

    Term r0 = `Appl("rhs0",TermList());

    RuleList res = `trsRule(ConcRule(Rule(fgba,r0), Rule(fgaa,r0), Rule(fgga,r0), Rule(faa,r0), Rule(fba,r0), Rule(fva,r0)), eSig);

  }


  /*
   * Reduce a list of rules
   */
  public static RuleList reduceRules(RuleList ruleList, Signature eSig) {
    
    // test subsumtion idea
    %match(ruleList) {
      ConcRule(C1*,rule,C2*) -> {
        canBeRemoved1(`rule, `ConcRule(C1*,C2*), eSig);
//         canBeRemoved2(`rule, `ConcRule(C1*,C2*), eSig);
      }
    }

    return ruleList;
  }


}


