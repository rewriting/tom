package test;

public class Example {
  static abstract class JavaType {}
  static abstract class JavaType2 {}

  static class A extends JavaType {
    A(JavaType2 e) {}
  }

  static class B extends JavaType {
    B() {}
  }

  static class C extends JavaType2 {
    C() {}
  }

  public static void main(String[] args) {
    System.out.println("Hello");
    JavaType2 c = new C();
    JavaType a = `A(c);
    JavaType b = `B();
    %match(a) {
      A(C()) -> { }
    }
  }

  %typeterm TomType { implements { JavaType } }
  %typeterm TomType2 { implements { JavaType2 } }

  %op TomType A(name : TomType2) {
    make(x) { new A(x) }
  }

  %op TomType B() {
    make() { new B() }
  }

  %op TomType2 C() {
    make() { new C() }
  }

  

}
