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

  public Term test1() throws jjtraveler.VisitFailure{
    Term subject = `f(a());
    MuStrategy s = `Identity();
    return (Term) s.visit(subject);
  }

  public Term test2() throws jjtraveler.VisitFailure{
    Term subject = `f(a());
    MuStrategy s = `(R1());
    return (Term) s.visit(subject);
  }

  public Term test3() throws jjtraveler.VisitFailure{
    Term subject = `f(a());
    MuStrategy s = `One(R1());
    return (Term) s.visit(subject);
  }

  public Term test4() throws jjtraveler.VisitFailure{
    Term subject = `g(f(a()),b());
    MuStrategy s = `OnceBottomUp((R2()));
    subject = (Term) s.visit(subject);
    subject = (Term) s.visit(subject);
    return (Term) s.visit(subject);
  }

  public Term test5() throws jjtraveler.VisitFailure{
    Term subject = `g(f(a()),b());
    MuStrategy s = `All((R2()));
    return (Term) s.visit(subject);
  }

  public Term testChoice() throws jjtraveler.VisitFailure{
    Term subject = `f(b());
    MuStrategy s = `Choice(R2(),R1());
    return (Term) s.visit(subject);
  }

  public Term testChoiceSideEffect() throws jjtraveler.VisitFailure{
    Term subject = `f(a());
    MuStrategy s = `Choice(Sequence(R1(),R2()),R1());
    return (Term) s.visit(subject);
  }

  public Term testNot() throws jjtraveler.VisitFailure{
    Term subject = `f(b());
    MuStrategy s = `Not(R2());
    return (Term) s.visit(subject);
  }

  public Term testNotSideEffect() throws jjtraveler.VisitFailure{
    Term subject = `f(a());
    MuStrategy s = `Not(Sequence(R1(),R2()));
    return (Term) s.visit(subject);
  }

  public Term test6() throws jjtraveler.VisitFailure{
    Term subject = `g(f(a()),b());
    MuStrategy s = `Sequence(All(R2()),All(All(R1())));
    return (Term) s.visit(subject);
  }

  public Term test7() throws jjtraveler.VisitFailure{
    Term subject = `g(f(a()),b());
    MuStrategy s = `Omega(1,R2());
    return (Term) s.visit(subject);
  }

  public Term test8() throws jjtraveler.VisitFailure{
    Term subject = `g(f(a()),b());
    MuStrategy s = `Omega(2,R2());
    return (Term) s.visit(subject);
  }

  public Term test9() throws jjtraveler.VisitFailure{
    Term subject = `f(b());
    MuStrategy s = `IfThenElse(R2(),Builda(),Buildb());
    return (Term) s.visit(subject);
  }

  public Term testITESideEffect() throws jjtraveler.VisitFailure{
    Term subject = `f(a());
    MuStrategy s = `IfThenElse(R2(),Identity(),Buildb());
    return (Term) s.visit(subject);
  }

  public Term test10() throws jjtraveler.VisitFailure{
    Term subject = `f(a());
    MuStrategy s = `IfThenElse(R2(),Builda(),Buildb());
    return (Term) s.visit(subject);
  }

  public Term test11() throws jjtraveler.VisitFailure{
    Term subject = `g(f(a()),b());
    MuStrategy s = `OnceTopDownId((R1()));
    subject = (Term) s.visit(subject);
    subject = (Term) s.visit(subject);
    return (Term) s.visit(subject);
  }

  public Term test12()throws jjtraveler.VisitFailure{
    Term subject = `g(f(a()),b());
    MuStrategy s = `Innermost((R2()));
    return (Term) s.visit(subject);
  }

  public Term test13()throws jjtraveler.VisitFailure{
    Term subject = `g(f(a()),b());
    MuStrategy s = `Outermost((R2()));
    return (Term) s.visit(subject);
  }


  public Term testWhen1() throws jjtraveler.VisitFailure{
    Term subject = `f(a());
    MuStrategy s = `When_f(R2());
    Term res;
    try {
      return (Term) s.visit(subject);
    } catch (jjtraveler.VisitFailure f) {
      throw new RuntimeException("Fail");
    }
  }

  public Term testCongruence1() throws jjtraveler.VisitFailure{
    Term subject = `g(f(a()),b());
    MuStrategy s = `_g(R1(),R2());
    Term res;
    try {
      return (Term) s.visit(subject);
    } catch (jjtraveler.VisitFailure f) {
      throw new RuntimeException("Fail");
    }
  }

  public Term testCongruence2() throws jjtraveler.VisitFailure{
    Term subject = `f(a());
    MuStrategy s = `_g(R1(),R2());
    try {
      return (Term) s.visit(subject);
    } catch (jjtraveler.VisitFailure f) {
      throw new RuntimeException("Fail");
    }
  }

  public Term testCongruenceList() throws jjtraveler.VisitFailure{
    Term subject = `l(a(),b());
    MuStrategy s = `_l(R1());
    try {
      return (Term) s.visit(subject);
    } catch (jjtraveler.VisitFailure f) {
      throw new RuntimeException("Fail");
    }
  }


}
