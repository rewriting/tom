

import testgen.m.types.*;
import java.io.*;
public class TestGen {
%gom {
  module m
    imports String
    abstract syntax
    T = Bottom(kid:T)
      | choice6(kid0:T)
      | mu1(kid0:T,kid1:T)
      | choice11(kid0:T,kid1:T)
      | mu0(kid0:T)
      | seq8(kid0:T)
      | mu2(kid0:T)
      | choice7(kid0:T,kid1:T)
      | mu3(kid0:T,kid1:T)
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
       choice10(x) -> choice11(seq8(x),x)

       choice11(Bottom(Z),x) -> id9(x)
       choice11(Appl(Z0,Z1),x) -> seq8(x)

       choice6(x) -> choice7(one4(x),x)
       choice7(Bottom(Z),x) -> rule5(x)
       choice7(Appl(Z0,Z1),x) -> one4(x)

       id9(x) -> x
       mu0(x) -> mu1(x,x)
       mu1(Bottom(Z),x) -> Bottom(Z)
       mu1(Appl(Z0,Z1),x) -> choice10(x)

       mu2(x) -> mu3(x,x)
       mu3(Bottom(Z),x) -> Bottom(Z)
       mu3(Appl(Z0,Z1),x) -> choice6(x)


       one4(Appl(Z0,Z1)) -> one4_1(Appl(Z0,one4_2(Z1)))

       one4_1(Appl(Z0,BottomList(Z))) -> Bottom(Appl(Z0,Z))
       one4_1(Appl(Z0,Cons(x1,x2))) -> Appl(Z0,Cons(x1,x2))

       one4_2(Nil()) -> BottomList(Nil())
       one4_2(Cons(x1,x2)) -> one4_3(mu2(x1),x2)

       one4_3(Appl(Z0,Z1),x2) -> Cons(Appl(Z0,Z1),x2)
       one4_3(Bottom(Z),Nil()) -> BottomList(Cons(Z,Nil()))
       one4_3(Bottom(Z),Cons(Z0,Z1)) -> Clean(Z, one4_2(Cons(Z0,Z1)))

       Clean(Z,BottomList(Nil())) -> BottomList(Cons(Z,Nil()))
       Clean(Z,BottomList(Cons(Z1,Nil()))) -> BottomList(Cons(Z,Cons(Z1,Nil())))
       Clean(Z,Cons(Z0,Z1)) -> Cons(Z,Cons(Z0,Z1))

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
