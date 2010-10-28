public class Problem2{

  static class A {
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
  
// ------------------------------------------------------------
  %typeterm TomA {
    implement { A }
    is_sort(t) { $t instanceof A }
  }

  %typeterm TomC {
    implement { A }
    is_sort(t) { $t instanceof A }
  }

  %typeterm TomB {
    implement { B }
    is_sort(t) { $t instanceof B }
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

// ------------------------------------------------------------

  public final static void main(String[] args) {
    Problem2 test = new Problem2();
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
    String op = term.getOp();
    System.out.print("Term = " + `op);
    A y = `b();
    B x = `b();
    %match {
      //x << y && (y == a()) && (g(x) == g(b())) -> { System.out.println("Test!"); }
      g[num2=arg] << TomB term -> { System.out.println("(" + `arg.getOp() + ")"); }
      f[num1=arg] << TomA term -> { System.out.println("(" + `arg.getOp() + ")"); }
    }
  }
}

