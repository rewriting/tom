import regress.undeclaredexplicitytype.simple.types.*;
public class UndeclaredExplicityType {
  %gom {
    module Simple 
      abstract syntax
      B = b()
  }

  public static void main(String[] args) {
    B tt = `b();
    %match{
      // "A" was not declared (i.e. "A" is a bad type)
      b() << A tt -> { System.out.println(`tt); }
    }
  }
}
