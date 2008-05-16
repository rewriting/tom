package test;

public class Example {

  static abstract class JavaType {}
  
  static abstract class JavaType2 {}

  static class A extends JavaType {
    
    JavaType2 name;

    A(JavaType2 name) {
    this.name = name;
    }

    JavaType2 getName() {
      return name;
    }
  
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

  %typeterm TomType { 
    implement { JavaType }
    is_sort(t) { (t instanceof JavaType) }
  }

  %typeterm TomType2 { 
    implement { JavaType2 } 
    is_sort(t) { (t instanceof JavaType) }
  }

  %op TomType A(name : TomType2) {
    make(x) { new A(x) }
    is_fsym(t) { t instanceof A }
    get_slot(name,t) { t.getName() }
  }

  %op TomType B() {
    make() { new B() }
    is_fsym(t) { t instanceof B }
  }

  %op TomType2 C() {
    make() { new C() }
    is_fsym(t) { t instanceof C }
  }

}
