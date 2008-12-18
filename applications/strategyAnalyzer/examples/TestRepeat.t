import testrepeat.m.types.*;
public class TestRepeat {
  %gom {
    module m
      abstract syntax
      T = 
      | Bottom()
      | a()
      | b()
      | c()
      | f(s1:T)
      | g(s1:T,s2:T)
      | phi0(s1:T)
      | phi1(s1:T,s2:T)
      | phi2(s1:T)
      | phi3(s1:T)
      | phi4(s1:T)
      | phi5(s1:T,s2:T)
      | phi6(s1:T)
      | phi7(s1:T)
      | phi8(s1:T)
      | phi9(s1:T,s2:T)

    module m:rules() {
phi2(a()) -> b()
phi2(!a()) -> Bottom()
phi3(b()) -> c()
phi3(!b()) -> Bottom()
phi4(x) -> phi5(phi2(x),x)
phi5(Bottom(),x) -> phi3(x)
phi5(!Bottom(),x) -> phi2(x)
phi6(x) -> phi0(phi4(x))
phi7(x) -> x
phi8(x) -> phi9(phi6(x),x)
phi9(Bottom(),x) -> phi7(x)
phi9(!Bottom(),x) -> phi6(x)
phi0(x) -> phi1(x,x)
phi1(!Bottom(),x) -> phi8(x)
phi1(Bottom(),x) -> Bottom()

    }
  }


  public static void main(String[] args) {
    T t = `phi8(c());
    System.out.println(t);
  }
}

