public class SubtypeExample{

  class A {
    public A num1;
    public A() {}
    public A(A num1) { this.num1 = num1; }
    public String getOp() { return ""; }
  }

  class Javaa extends A {
    public Javaa() { super(); }
    public String getOp() { return "a"; }
  }

  class Javaf extends A {
    public Javaf(A num1) { super(num1); }
    public String getOp() { return "f"; }
  }

  class B extends A {
    public B num2;
    public B() {}
    public B(B num2) { this.num2 = num2; }
    public String getOp() { return ""; }
  }
  
  class Javab extends B {
    public Javab() { super(); }
    public String getOp() { return "b"; }
  }

  class Javag extends B {
    public Javag(B num2) { super(num2); }
    public String getOp() { return "g"; }
  }
  
// ------------------------------------------------------------
  %typeterm TomA {
    implement { A }
    is_sort(t) { $t instanceof A }
  }

  %typeterm TomB {
    implement { B }
    is_sort(t) { $t instanceof B }
  }

  %subtype TomB <: TomA

// ------------------------------------------------------------
  %op TomA a() {
    is_fsym(t) { $t instanceof Javaa }
  }

  %op TomA f(num1:TomA) {
    is_fsym(t) { $t instanceof Javaf }
    get_slot(num1,t) { ((Javaf)$t).num1 }
  }

  %op TomB b() {
    is_fsym(t) { $t instanceof Javab }
  }

  %op TomB g(num2:TomB) {
    is_fsym(t) { $t instanceof Javag }
    get_slot(num2,t) { ((Javag)$t).num2 }
  }

// ------------------------------------------------------------

  public final static void main(String[] args) {
    SubtypeExample test = new SubtypeExample();
    test.buildExpA();
    test.buildExpB();
  }

  public void buildExpA() {
    print(new Javaf(new Javaa()));
  } 

  public void buildExpB() {
    print(new Javag(new Javab()));
  } 

  public void print(A term) {
    String op = term.getOp();
    System.out.print("Term = " + `op);
    %match {
      g[num2=arg] << TomB term -> { System.out.println("(" + `arg.getOp() + ")"); }
      f[num1=arg] << TomA term -> { System.out.println("(" + `arg.getOp() + ")"); }
    }
  }
}

