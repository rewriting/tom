// StratMu("_x1",
//         StratSequence(
//                       StratAll(StratName("_x1")),
//                       StratChoice(
//                                   Rule(g("x"),g(i,g(x))),
//                                   StratIdentity()
//                                   )
//                       )
//         )

import main.m.types.*;
import java.io.*;
public class Main {
  %gom {
    module m
      abstract syntax
      Bool = 
        | True()
        | False()
        | eq(kid0_Dummy:Dummy,kid1_Dummy:Dummy)
        | and(kid0_Bool:Bool,kid1_Bool:Bool)
      T = 
        | f(kid0_T:T)
        | a()
        | h(kid0_T:T)
        | i(kid0_U:U)
        | rule59_T(kid0:T)
        | choice72_T(kid0:T)
        | choice71_T(kid0:T)
        | id70_T(kid0:T)
        | Bottom_T(kid0:T)
        | seq73_T(kid0:T)
        | seq274_T(kid0:T,kid1:T)
      Dummy = 
        | mu12(kid0_Dummy:Dummy)
        | all40(kid0_Dummy:Dummy)
        | all40_h(kid0_Dummy:Dummy,kid1_Dummy:Dummy)
        | all40_i(kid0_Dummy:Dummy,kid1_Dummy:Dummy)
        | all40_f(kid0_Dummy:Dummy,kid1_Dummy:Dummy)
        | all40_g(kid0_Dummy:Dummy,kid1_Dummy:Dummy)
        | mainStrat(kid0_Dummy:Dummy)
      U = 
        | g(kid0_T:T)
        | rule59_U(kid0:U)
        | choice72_U(kid0:U)
        | choice71_U(kid0:U)
        | id70_U(kid0:U)
        | Bottom_U(kid0:U)
        | seq73_U(kid0:U)
        | seq274_U(kid0:U,kid1:U)

      module m:rules() {
        all40(f(X0)) -> all40_f(mu12(X0),f(X0))
        all40(Bottom(X22)) -> Bottom(X22)
        all40_f(f(Z78_1),Z26) -> f(f(Z78_1))
        all40_f(g(Z79_1),Z26) -> f(g(Z79_1))
        all40_f(a(),Z26) -> f(a())
        all40_f(h(Z81_1),Z26) -> f(h(Z81_1))
        all40_f(i(Z82_1),Z26) -> f(i(Z82_1))
        all40_f(Bottom(X0),Z26) -> Bottom(Z26)
        all40(a()) -> a()
        all40(h(X0)) -> all40_h(mu12(X0),h(X0))
        all40_h(f(Z84_1),Z26) -> h(f(Z84_1))
        all40_h(g(Z85_1),Z26) -> h(g(Z85_1))
        all40_h(a(),Z26) -> h(a())
        all40_h(h(Z87_1),Z26) -> h(h(Z87_1))
        all40_h(i(Z88_1),Z26) -> h(i(Z88_1))
        all40_h(Bottom(X0),Z26) -> Bottom(Z26)
        all40(i(X0)) -> all40_i(mu12(X0),i(X0))
        all40_i(f(Z90_1),Z26) -> i(f(Z90_1))
        all40_i(g(Z91_1),Z26) -> i(g(Z91_1))
        all40_i(a(),Z26) -> i(a())
        all40_i(h(Z93_1),Z26) -> i(h(Z93_1))
        all40_i(i(Z94_1),Z26) -> i(i(Z94_1))
        all40_i(Bottom(X0),Z26) -> Bottom(Z26)
        all40(g(X0)) -> all40_g(mu12(X0),g(X0))
        all40_g(f(Z96_1),Z26) -> g(f(Z96_1))
        all40_g(g(Z97_1),Z26) -> g(g(Z97_1))
        all40_g(a(),Z26) -> g(a())
        all40_g(h(Z99_1),Z26) -> g(h(Z99_1))
        all40_g(i(Z100_1),Z26) -> g(i(Z100_1))
        all40_g(Bottom(X0),Z26) -> Bottom(Z26)

        rule59_U(g(x)) -> g(i(g(x)))
        rule59_T(f(Z102_1)) -> Bottom_T(f(Z102_1))
        rule59_T(a()) -> Bottom_T(a())
        rule59_T(h(Z104_1)) -> Bottom_T(h(Z104_1))
        rule59_T(i(Z105_1)) -> Bottom_T(i(Z105_1))
        rule59_U(Bottom_U(X50)) -> Bottom_U(X50)
        rule59_T(Bottom_T(X50)) -> Bottom_T(X50)

        id70_T(f(Z107_1)) -> f(Z107_1)
        id70_U(g(Z108_1)) -> g(Z108_1)
        id70_T(a()) -> a()
        id70_T(h(Z110_1)) -> h(Z110_1)
        id70_T(i(Z111_1)) -> i(Z111_1)
        id70_U(Bottom_U(X61)) -> Bottom_U(X61)
        id70_T(Bottom_T(X61)) -> Bottom_T(X61)

        choice71_T(f(Z113_1)) -> choice72_T(rule59_T(f(Z113_1)))
        choice71_U(g(Z114_1)) -> choice72_U(rule59_U(g(Z114_1)))
        choice71_T(a()) -> choice72_T(rule59_T(a()))
        choice71_T(h(Z116_1)) -> choice72_T(rule59_T(h(Z116_1)))
        choice71_T(i(Z117_1)) -> choice72_T(rule59_T(i(Z117_1)))
        choice71_U(Bottom_U(X41)) -> Bottom_U(X41)
        choice71_T(Bottom_T(X41)) -> Bottom_T(X41)

        choice72_T(f(Z119_1)) -> f(Z119_1)
        choice72_U(g(Z120_1)) -> g(Z120_1)
        choice72_T(a()) -> a()
        choice72_T(h(Z122_1)) -> h(Z122_1)
        choice72_T(i(Z123_1)) -> i(Z123_1)
        choice72(Bottom_U(X41)) -> id70_U(X41)
        choice72(Bottom_T(X41)) -> id70_T(X41)

        seq73_T(f(Z125_1)) -> seq274_T(choice71_T(all40_T(f(Z125_1))),f(Z125_1))
        seq73_U(g(Z126_1)) -> seq274_U(choice71_U(all40_U(g(Z126_1))),g(Z126_1))
        seq73_T(a()) -> seq274_T(choice71_T(all40_T(a())),a())
        seq73_T(h(Z128_1)) -> seq274_T(choice71_T(all40_T(h(Z128_1))),h(Z128_1))
        seq73_T(i(Z129_1)) -> seq274_T(choice71_T(all40_T(i(Z129_1))),i(Z129_1))
        seq73_U(Bottom_U(X13)) -> Bottom_U(X13)
        seq73_T(Bottom_T(X13)) -> Bottom_T(X13)

        seq274_T(f(Z131_1),Z17) -> f(Z131_1)
        seq274_U(g(Z132_1),Z17) -> g(Z132_1)
        seq274_T(a(),Z17) -> a()
        seq274_T(h(Z134_1),Z17) -> h(Z134_1)
        seq274_T(i(Z135_1),Z17) -> i(Z135_1)
        seq274_U(Bottom_T(Y14),X13) -> Bottom_U(X13)
        seq274_T(Bottom_T(Y14),X13) -> Bottom_T(X13)

        mu12(f(Z137_1)) -> seq73(f(Z137_1))
        mu12(g(Z138_1)) -> seq73(g(Z138_1))
        mu12(a()) -> seq73(a())
        mu12(h(Z140_1)) -> seq73(h(Z140_1))
        mu12(i(Z141_1)) -> seq73(i(Z141_1))
        mu12(Bottom(X3)) -> Bottom(X3)

        and(True(),True()) -> True()
        and(True(),False()) -> False()
        and(False(),True()) -> False()
        and(False(),False()) -> False()
        eq(f(Z75_1),f(Z76_1)) -> and(eq(Z75_1,Z76_1),True())
        eq(f(Z75_1),a()) -> False()
        eq(f(Z75_1),h(Z76_1)) -> False()
        eq(f(Z75_1),i(Z76_1)) -> False()
        eq(f(Z75_1),g(Z76_1)) -> False()
        eq(a(),f(Z76_1)) -> False()
        eq(a(),a()) -> True()
        eq(a(),h(Z76_1)) -> False()
        eq(a(),i(Z76_1)) -> False()
        eq(a(),g(Z76_1)) -> False()
        eq(h(Z75_1),f(Z76_1)) -> False()
        eq(h(Z75_1),a()) -> False()
        eq(h(Z75_1),h(Z76_1)) -> and(eq(Z75_1,Z76_1),True())
        eq(h(Z75_1),i(Z76_1)) -> False()
        eq(h(Z75_1),g(Z76_1)) -> False()
        eq(i(Z75_1),f(Z76_1)) -> False()
        eq(i(Z75_1),a()) -> False()
        eq(i(Z75_1),h(Z76_1)) -> False()
        eq(i(Z75_1),i(Z76_1)) -> and(eq(Z75_1,Z76_1),True())
        eq(i(Z75_1),g(Z76_1)) -> False()
        eq(g(Z75_1),f(Z76_1)) -> False()
        eq(g(Z75_1),a()) -> False()
        eq(g(Z75_1),h(Z76_1)) -> False()
        eq(g(Z75_1),i(Z76_1)) -> False()
        eq(g(Z75_1),g(Z76_1)) -> and(eq(Z75_1,Z76_1),True())
        mainStrat(X77) -> seq73(X77)

    }
  }
  
  public static void main(String[] args) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      T input = T.fromString(reader.readLine());
      long start = System.currentTimeMillis();
      
      T t = `mainStrat(input);
      
      long stop = System.currentTimeMillis();
      System.out.println(t);
      System.out.println("time1 (ms): " + ((stop-start)));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

