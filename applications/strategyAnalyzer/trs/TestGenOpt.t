

import testgenopt.m.types.*;
import java.io.*;
public class TestGenOpt {
%gom {
  module m
    imports String
    abstract syntax
    T = Bottom(kid:T)
      | choice6(kid0:T)
      | mu1(kid0:T)
      | choice11(kid0:T)
      | mu0(kid0:T)
      | seq8(kid0:T)
      | mu2(kid0:T)
      | choice7(kid0:T)
      | mu3(kid0:T)
      | rule5(kid0:T)
      | choice10(kid0:T)
      | id9(kid0:T)
      | Appl(s:T,arg:L)
      | one4(k:T)
      | one4_1(k:T)
      | a()
      | b()
      | f()
      | g()

      L =  Nil()
      | Cons(head:T,tail:L)
      | Clean(head:T,tail:L)
      | one4_2(arg:L)
      | one4_3(k1:T,k3:L)
      | BottomList(arg:L)

    module m:rules() {
       choice10(x) -> choice11(seq8(x))

       choice11(Bottom(Z)) -> id9(Z)
       choice11(Appl(Z0,Z1)) -> Appl(Z0,Z1)

       choice6(x) -> choice7(one4(x))
       choice7(Bottom(Z)) -> rule5(Z)
       choice7(Appl(Z0,Z1)) -> Appl(Z0,Z1)

       id9(x) -> x
       mu0(x) -> mu1(x)
       mu1(Bottom(Z)) -> Bottom(Z)
       mu1(Appl(Z0,Z1)) -> choice10(Appl(Z0,Z1))

       mu2(x) -> mu3(x)
       mu3(Bottom(Z)) -> Bottom(Z)
       mu3(Appl(Z0,Z1)) -> choice6(Appl(Z0,Z1))


       // all_1 SAME AS one_1
       all(Appl(Z0,Z1)) -> all_1(Appl(Z0,all_2(Z1)))

       all_1(Appl(Z0,BottomList(Z))) -> Bottom(Appl(Z0,Z))
       all_1(Appl(Z0,Cons(x1,x2))) -> Appl(Z0,Cons(x1,x2))
       all_1(Appl(Z0,Nill)) -> Appl(Z0,Nill)

       all_2(Nil()) -> Nil()
       all_2(Cons(x1,x2)) -> all_3(mu2(x1),x2,Cons(x1,Nil))

       all_3(Appl(Z0,Z1),Nil,Y) -> Cons(Appl(Z0,Z1),Nil)
       all_3(Appl(Z0,Z1),Cons(x1,x2),Y) -> all_3(mu2(x1),x2,Cons(Appl(Z0,Z1),Y))
       all_3(Bottom(Z),x2,Y) -> BottomList(Concat(Y,Cons(Z,x2)))

       Concat(Nil,Z) -> Z
       Concat(Cons(X,Y),Z) -> Cons(X,Concat(Y,Z))

       rule5(Bottom(Z)) -> Bottom(Z)
       rule5(Appl(a(),Nil())) -> Appl(b(),Nil())
       rule5(Appl(b(),Z1)) -> Bottom(Appl(b(),Z1))
       rule5(Appl(f(),Z1)) -> Bottom(Appl(f(),Z1))
       rule5(Appl(g(),Z1)) -> Bottom(Appl(g(),Z1))
       seq8(x) -> mu0(choice6(x))

  }


}

public static void main(String[] args) {
  try {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    T input = T.fromString(reader.readLine());
//     L inputL = L.fromString(reader.readLine());
      // Appl(f(),Cons(Appl(f(),Cons(Appl(f(),Cons(Appl(a(),Nil()),Nil())),Nil())),Nil()))
      // Appl(a(),Nil())
      // Appl(f(),Cons(Appl(a(),Nil()),Nil()))
      // Appl(g(),Cons(Appl(f(),Cons(Appl(a(),Nil()),Nil())),Cons(Appl(f(),Cons(Appl(a(),Nil()),Nil())),Nil())))
//     L t = `one4_2(inputL);
        T t = `choice10(input);
    System.out.println(t);
  } catch (IOException e) {
    e.printStackTrace();
  }
}
}
