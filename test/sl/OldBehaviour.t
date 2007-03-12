package sl;
import sl.testsl.types.*;
import tom.library.strategy.mutraveler.MuStrategy;

public class OldBehaviour {
  %include { mustrategy.tom }
  %include { testsl/testsl.tom }
  %include { testsl/_testsl.tom }

  %strategy R1() extends `Identity() {
    visit Term {
      f(a()) -> { return `f(b()); }
      b() -> { return `c(); }
    }
  }

  %strategy R2() extends `Fail() {
    visit Term {
      f(a()) -> { return `f(b()); }
      b() -> { return `c(); }
    }
  }
  %strategy Builda() extends `Identity() {
    visit Term {
      _ -> { return `a(); }
    }
  }
  %strategy Buildb() extends `Identity() {
    visit Term {
      _ -> { return `b(); }
    }
  }

  public Term test1() {
    Term subject = `f(a());
    MuStrategy s = `Identity();
    return (Term) s.apply(subject);
  }

  public Term test2() {
    Term subject = `f(a());
    MuStrategy s = `(R1());
    return (Term) s.apply(subject);
  }

  public Term test3() {
    Term subject = `f(a());
    MuStrategy s = `One(R1());
    return (Term) s.apply(subject);
  }

  public Term test4() {
    Term subject = `g(f(a()),b());
    MuStrategy s = `OnceBottomUp((R2()));
    subject = (Term) s.apply(subject);
    subject = (Term) s.apply(subject);
    return (Term) s.apply(subject);
  }

  public Term test5() {
    Term subject = `g(f(a()),b());
    MuStrategy s = `All((R2()));
    return (Term) s.apply(subject);
  }

  public Term test6() {
    Term subject = `g(f(a()),b());
    MuStrategy s = `Sequence(All(R2()),All(All(R1())));
    return (Term) s.apply(subject);
  }

  public Term test7() {
    Term subject = `g(f(a()),b());
    MuStrategy s = `Omega(1,R2());
    return (Term) s.apply(subject);
  }

  public Term test8() {
    Term subject = `g(f(a()),b());
    MuStrategy s = `Omega(2,R2());
    return (Term) s.apply(subject);
  }

  public Term test9() {
    Term subject = `f(b());
    MuStrategy s = `IfThenElse(R2(),Builda(),Buildb());
    return (Term) s.apply(subject);
  }

  public Term test10() {
    Term subject = `f(a());
    MuStrategy s = `IfThenElse(R2(),Builda(),Buildb());
    return (Term) s.apply(subject);
  }

  public Term test11() {
    Term subject = `g(f(a()),b());
    MuStrategy s = `OnceTopDownId((R1()));
    subject = (Term) s.apply(subject);
    subject = (Term) s.apply(subject);
    return (Term) s.apply(subject);
  }

  public Term test12(){
    Term subject = `g(f(a()),b());
    MuStrategy s = `Innermost((R2()));
    return (Term) s.apply(subject);
  }

  public Term test13(){
    Term subject = `g(f(a()),b());
    MuStrategy s = `Outermost((R2()));
    return (Term) s.apply(subject);
  }


  public Term testWhen1() {
    Term subject = `f(a());
    MuStrategy s = `When_f(R2());
    Term res;
    try {
      return (Term) s.visit(subject);
    } catch (jjtraveler.VisitFailure f) {
      throw new RuntimeException("Fail");
    }
  }

  public Term testCongruence1() {
    Term subject = `g(f(a()),b());
    MuStrategy s = `_g(R1(),R2());
    Term res;
    try {
      return (Term) s.visit(subject);
    } catch (jjtraveler.VisitFailure f) {
      throw new RuntimeException("Fail");
    }
  }

  public Term testCongruence2() {
    Term subject = `f(a());
    MuStrategy s = `_g(R1(),R2());
    try {
      return (Term) s.visit(subject);
    } catch (jjtraveler.VisitFailure f) {
      throw new RuntimeException("Fail");
    }
  }

  public Term testCongruenceList() {
    Term subject = `l(a(),b());
    MuStrategy s = `_l(R1());
    try {
      return (Term) s.visit(subject);
    } catch (jjtraveler.VisitFailure f) {
      throw new RuntimeException("Fail");
    }
  }


}
