package sl;
import sl.testsl.types.*;
import tom.library.sl.*;

public class OldBehaviour {
  %include { sl.tom }
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

  public Term test1() throws tom.library.sl.VisitFailure{
    Term subject = `f(a());
    Strategy s = `Identity();
    return (Term) s.visitLight(subject);
  }

  public Term test2() throws tom.library.sl.VisitFailure{
    Term subject = `f(a());
    Strategy s = `(R1());
    return (Term) s.visitLight(subject);
  }

  public Term test3() throws tom.library.sl.VisitFailure{
    Term subject = `f(a());
    Strategy s = `One(R1());
    return (Term) s.visitLight(subject);
  }

  public Term test4() throws tom.library.sl.VisitFailure{
    Term subject = `g(f(a()),b());
    Strategy s = `OnceBottomUp((R2()));
    subject = (Term) s.visitLight(subject);
    subject = (Term) s.visitLight(subject);
    return (Term) s.visitLight(subject);
  }

  public Term test5() throws tom.library.sl.VisitFailure{
    Term subject = `g(f(a()),b());
    Strategy s = `All((R2()));
    return (Term) s.visitLight(subject);
  }

  public Term testChoice() throws tom.library.sl.VisitFailure{
    Term subject = `f(b());
    Strategy s = `Choice(R2(),R1());
    return (Term) s.visitLight(subject);
  }

  public Term testChoiceSideEffect() throws tom.library.sl.VisitFailure{
    Term subject = `f(a());
    Strategy s = `Choice(Sequence(R1(),R2()),R1());
    return (Term) s.visitLight(subject);
  }

  public Term testNot() throws tom.library.sl.VisitFailure{
    Term subject = `f(b());
    Strategy s = `Not(R2());
    return (Term) s.visitLight(subject);
  }

  public Term testNotSideEffect() throws tom.library.sl.VisitFailure{
    Term subject = `f(a());
    Strategy s = `Not(Sequence(R1(),R2()));
    return (Term) s.visitLight(subject);
  }

  public Term test6() throws tom.library.sl.VisitFailure{
    Term subject = `g(f(a()),b());
    Strategy s = `Sequence(All(R2()),All(All(R1())));
    return (Term) s.visitLight(subject);
  }

  public Term test7() throws tom.library.sl.VisitFailure{
    Term subject = `g(f(a()),b());
    Strategy s = `Omega(1,R2());
    return (Term) s.visitLight(subject);
  }

  public Term test8() throws tom.library.sl.VisitFailure{
    Term subject = `g(f(a()),b());
    Strategy s = `Omega(2,R2());
    return (Term) s.visitLight(subject);
  }

  public Term test9() throws tom.library.sl.VisitFailure{
    Term subject = `f(b());
    Strategy s = `IfThenElse(R2(),Builda(),Buildb());
    return (Term) s.visitLight(subject);
  }

  public Term testITESideEffect() throws tom.library.sl.VisitFailure{
    Term subject = `f(a());
    Strategy s = `IfThenElse(R2(),Identity(),Buildb());
    return (Term) s.visitLight(subject);
  }

  public Term test10() throws tom.library.sl.VisitFailure{
    Term subject = `f(a());
    Strategy s = `IfThenElse(R2(),Builda(),Buildb());
    return (Term) s.visitLight(subject);
  }

  public Term test11() throws tom.library.sl.VisitFailure{
    Term subject = `g(f(a()),b());
    Strategy s = `OnceTopDownId((R1()));
    subject = (Term) s.visitLight(subject);
    subject = (Term) s.visitLight(subject);
    return (Term) s.visitLight(subject);
  }

  public Term test12()throws tom.library.sl.VisitFailure{
    Term subject = `g(f(a()),b());
    Strategy s = `Innermost((R2()));
    return (Term) s.visitLight(subject);
  }

  public Term test13()throws tom.library.sl.VisitFailure{
    Term subject = `g(f(a()),b());
    Strategy s = `Outermost((R2()));
    return (Term) s.visitLight(subject);
  }


  public Term testWhen1() throws tom.library.sl.VisitFailure{
    Term subject = `f(a());
    Strategy s = `When_f(R2());
    Term res;
    try {
      return (Term) s.visitLight(subject);
    } catch (tom.library.sl.VisitFailure f) {
      throw new RuntimeException("Fail");
    }
  }

  public Term testCongruence1() throws tom.library.sl.VisitFailure{
    Term subject = `g(f(a()),b());
    Strategy s = `_g(R1(),R2());
    Term res;
    try {
      return (Term) s.visitLight(subject);
    } catch (tom.library.sl.VisitFailure f) {
      throw new RuntimeException("Fail");
    }
  }

  public Term testCongruence2() throws tom.library.sl.VisitFailure{
    Term subject = `f(a());
    Strategy s = `_g(R1(),R2());
    try {
      return (Term) s.visitLight(subject);
    } catch (tom.library.sl.VisitFailure f) {
      throw new RuntimeException("Fail");
    }
  }

  public Term testCongruenceList() throws tom.library.sl.VisitFailure{
    Term subject = `l(a(),b());
    Strategy s = `_l(R1());
    try {
      return (Term) s.visitLight(subject);
    } catch (tom.library.sl.VisitFailure f) {
      throw new RuntimeException("Fail");
    }
  }


}
