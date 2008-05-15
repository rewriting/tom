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
    JavaType2 c = `C();
    JavaType a = `A(c);
    JavaType x = `B();
    %match(a) {
      A(x) -> {
         JavaType tmp1 = x;
         JavaType2 tmp2 = `x;
      }
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
