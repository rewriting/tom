import test.m.types.*;
public class Test {
  %gom {
    module m
      abstract syntax
      T = 
      | Bottom()
      | a()
      | b()
      | f(s1:T)
      | g(s1:T,s2:T)
      | phi0(s1:T)
      | phi1(s1:T,s2:T)
      | phi2(s1:T)
      | phi3(s1:T,s2:T)
      | phi4(s1:T)
      | phi4_1(s1:T,s2:T)
      | phi4_2(s1:T,s2:T,s3:T,s4:T)
      | phi5(s1:T)
      | phi6(s1:T)
      | phi7(s1:T,s2:T)
      | phi8(s1:T)
      | phi9(s1:T)
      | phi10(s1:T)
      | phi11(s1:T,s2:T)

    module m:rules() {
phi0(x) -> phi1(x,x)
phi1(!Bottom(),x) -> phi10(x)
phi1(Bottom(),x) -> Bottom()
phi2(x) -> phi3(x,x)
phi3(!Bottom(),x) -> phi6(x)
phi3(Bottom(),x) -> Bottom()
phi4(a()) -> Bottom()
phi4(f(x1)) -> phi4_1(phi0(x1),f(phi0(x1)))
phi4(g(x1,x2)) -> phi4_2(phi0(x1),phi0(x2),g(phi0(x1),x2),g(x1,phi0(x2)))
phi4_1(Bottom(),x1) -> Bottom()
phi4_1(!Bottom(),x1) -> x1
phi4_2(Bottom(),Bottom(),x1,x2) -> Bottom()
phi4_2(!Bottom(),y2,x1,x2) -> x1
phi4_2(y1,!Bottom(),x1,x2) -> x2
phi5(a()) -> b()
phi5(!a()) -> Bottom()
phi6(x) -> phi7(phi4(x),x)
phi7(Bottom(),x) -> phi5(x)
phi7(!Bottom(),x) -> phi4(x)
phi8(x) -> phi0(phi6(x))
phi9(x) -> x
phi10(x) -> phi11(phi8(x),x)
phi11(Bottom(),x) -> phi9(x)
phi11(!Bottom(),x) -> phi8(x)
    }
  }


  public static void main(String[] args) {
    T t = `phi10(f(g(a(),b())));
    System.out.println(t);
  }
}

