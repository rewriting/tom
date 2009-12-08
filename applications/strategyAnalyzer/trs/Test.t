
import test.m.types.*;
import java.io.*;
public class Test {
  %gom {
    module m
      abstract syntax
      T = Bottom(kid:T)
        | choice6(kid0:T)
        | one4_1(kid0:T)
        | mu1(kid0:T,kid1:T)
        | choice11(kid0:T,kid1:T)
        | g(kid0:T,kid1:T)
        | mu0(kid0:T)
        | seq8(kid0:T)
        | b()
        | mu2(kid0:T)
        | one4_2(kid0:T,kid1:T,kid2:T,kid3:T)
        | one4(kid0:T)
        | one4_0()
        | a()
        | f(kid0:T)
        | choice7(kid0:T,kid1:T)
        | mu3(kid0:T,kid1:T)
        | rule5(kid0:T)
        | choice10(kid0:T)
        | id9(kid0:T)
      module m:rules() {

        choice10(x) -> choice11(seq8(x),x)
        choice11(Bottom(Z),x) -> id9(Z) // ou id9(x)
        choice11(a(),x) -> seq8(x)
        choice11(b(),x) -> seq8(x)
        choice11(f(Z1),x) -> seq8(x)
//         choice11(g(Z1,Z2),x) -> seq8(x)

        choice6(x) -> choice7(one4(x),x)
        choice7(Bottom(Z),x) -> rule5(Z)  // ou rule5(x)
        choice7(a(),x) -> one4(x)
        choice7(b(),x) -> one4(x)
        choice7(f(Z1),x) -> one4(x)
//         choice7(g(Z1,Z2),x) -> one4(x)

        id9(x) -> x

        mu0(x) -> mu1(x,x)
        mu1(Bottom(Z),x) -> Bottom(Z)
        mu1(a(),x) -> choice10(x)
        mu1(b(),x) -> choice10(x)
        mu1(f(Z1),x) -> choice10(x)
//         mu1(g(Z1,Z2),x) -> choice10(x)

        mu2(x) -> mu3(x,x)
        mu3(Bottom(Z),x) -> Bottom(Z)
        mu3(a(),x) -> choice6(x)
        mu3(b(),x) -> choice6(x)
        mu3(f(Z1),x) -> choice6(x)
//         mu3(g(Z1,Z2),x) -> choice6(x)

        one4(a()) -> Bottom(a())
        one4(b()) -> Bottom(b())
        one4(f(x1)) -> one4_1(mu2(x1))
//         one4(g(x1,x2)) -> one4_2(mu2(x1),mu2(x2),g(mu2(x1),x2),g(x1,mu2(x2)))

        one4_1(Bottom(Z)) -> Bottom(f(Z))
        one4_1(a()) -> f(a())
        one4_1(b()) -> f(b())
        one4_1(f(Z1)) -> f(f(Z1))
//         one4_1(g(Z1,Z2)) -> f(g(Z1,Z2))

//         one4_2(Bottom(Z),Bottom(Z),x1,x2) -> Bottom(Z)
//         one4_2(a(),y2,x1,x2) -> x1
//         one4_2(b(),y2,x1,x2) -> x1
//         one4_2(f(Z1),y2,x1,x2) -> x1
//         one4_2(g(Z1,Z2),y2,x1,x2) -> x1
//         one4_2(y1,a(),x1,x2) -> x2
//         one4_2(y1,b(),x1,x2) -> x2
//         one4_2(y1,f(Z1),x1,x2) -> x2
//         one4_2(y1,g(Z1,Z2),x1,x2) -> x2

        rule5(Bottom(Z)) -> Bottom(Z)
        rule5(a()) -> f(b())
        rule5(b()) -> Bottom(b())
        rule5(f(Z1)) -> Bottom(f(Z1))
//         rule5(g(Z1,Z2)) -> Bottom(g(Z1,Z2))

        seq8(x) -> mu0(choice6(x))

    }
  }
  

  public static void main(String[] args) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      T input = T.fromString(reader.readLine());
      T t = `choice10(input);
      System.out.println(t);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
    
    
