package sl;
import sl.testsl.types.*;
import tom.library.sl.Strategy;

public class NewBehaviour {

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

  public Term test1() {
    Term subject = `f(a());
    Strategy s = `Identity();
    return (Term) s.fire(subject);
  }

  public Term test2() {
    Term subject = `f(a());
    Strategy s = `(R1());
    return (Term) s.fire(subject);
  }

  public Term test3() {
    Term subject = `f(a());
    Strategy s = `One(R1());
    return (Term) s.fire(subject);
  }

  public Term test4() {
    Term subject = `g(f(a()),b());
    Strategy s = `OnceBottomUp((R2()));
    subject = (Term) s.fire(subject);
    subject = (Term) s.fire(subject);
    return (Term) s.fire(subject);
  }

  public Term test5() {
    Term subject = `g(f(a()),b());
    Strategy s = `All((R2()));
    return (Term) s.fire(subject);
  }

  public Term test6() {
    Term subject = `g(f(a()),b());
    Strategy s = `Sequence(All(R2()),All(All(R1())));
    return (Term) s.fire(subject);
  }

  public Term test7() {
    Term subject = `g(f(a()),b());
    Strategy s = `Omega(1,R2());
    return (Term) s.fire(subject);
  }

  public Term test8() {
    Term subject = `g(f(a()),b());
    Strategy s = `Omega(2,R2());
    return (Term) s.fire(subject);
  }

  public Term test9() {
    Term subject = `f(b());
    Strategy s = `IfThenElse(R2(),Builda(),Buildb());
    return (Term) s.fire(subject);
  }

  public Term test10() {
    Term subject = `f(a());
    Strategy s = `IfThenElse(R2(),Builda(),Buildb());
    return (Term) s.fire(subject);
  }

  public Term testWhen1() {
    Term subject = `f(a());
    Strategy s = `When_f(R2());
    return (Term) s.fire(subject);
  }

  public Term testCongruence1() {
    Term subject = `g(f(a()),b());
    Strategy s = `_g(R1(),R2());
    return (Term) s.fire(subject);
  }

  public Term testCongruence2() {
    Term subject = `f(a());
    Strategy s = `_g(R1(),R2());
    return (Term) s.fire(subject);
  }

  public Term testCongruenceList() {
    Term subject = `l(a(),b(),a(),b());
    Strategy s = `_l(R1());
    return (Term) s.fire(subject);
  }


}
