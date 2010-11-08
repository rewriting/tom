public class Problem4{

  static class A extends C {
    public A num1;
    public A() {}
    public A(A num1) { this.num1 = num1; }
    public String getOp() { return ""; }
  }

  static class Javaa extends A {
    public Javaa() { super(); }
    public String getOp() { return "a"; }
  }

  static class Javaf extends A {
    public Javaf(A num1) { super(num1); }
    public String getOp() { return "f"; }
  }

  static class B extends A {
    public B num2;
    public B() {}
    public B(B num2) { this.num2 = num2; }
    public String getOp() { return ""; }
  }
  
  static class Javab extends B {
    public Javab() { super(); }
    public String getOp() { return "b"; }
  }

  static class Javag extends B {
    public Javag(B num2) { super(num2); }
    public String getOp() { return "g"; }
  }

  static class C {
    public C num3;
    public C() {}
  }

  static class Javac extends C {
    public Javac() { super(); }
  }
  
// ------------------------------------------------------------
  %typeterm TomA {
    implement { A }
    is_sort(t) { $t instanceof A }
    equals(t1,t2) { $t1 == $t2 }
  }

  %typeterm TomB {
    implement { B }
    is_sort(t) { $t instanceof B }
    equals(t1,t2) { $t1 == $t2 }
  }

  %typeterm TomC {
    implement { C }
    is_sort(t) { $t instanceof C }
    equals(t1,t2) { $t1 == $t2 }
  }

  %subtype TomB <: TomA
  %subtype TomA <: TomC

// ------------------------------------------------------------
  %op TomA a() {
    is_fsym(t) { $t instanceof Javaa }
    make() { new Javaa() }
  }

  %op TomA f(num1:TomA) {
    is_fsym(t) { $t instanceof Javaf }
    make(t) { new Javaf($t) }
    get_slot(num1,t) { ((Javaf)$t).num1 }
  }

  %op TomB b() {
    is_fsym(t) { $t instanceof Javab }
    make() { new Javab() }
  }

  %op TomB g(num2:TomB) {
    is_fsym(t) { $t instanceof Javag }
    make(t) { new Javag($t) }
    get_slot(num2,t) { ((Javag)$t).num2 }
  }

  %op TomC c() {
    is_fsym(t) { $t instanceof Javac }
    make() { new Javac() }
  }
// ------------------------------------------------------------

  public final static void main(String[] args) {
    Problem4 test = new Problem4();
    test.buildExpA();
    test.buildExpB();
  }

  public void buildExpA() {
    print(`f(a()));
  } 

  public void buildExpB() {
    print(`g(b()));
  } 

  public void print(A term) {
    A y = `g(b());
    %match {
      g(x) << TomB y && f(b()) << up(x) && f(y) << f(g(b())) -> { System.out.println("x first ligne = " + `x); }
    }
  }

  public static A up(B element) {
    return `f(element);
  }
}

