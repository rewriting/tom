/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the Inria nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package sl;

import sl.jjterm.types.*;
import jjtraveler.*;

class BenchJJTraveler {

  public static boolean equal_term_Nat(Object t1, Object t2) {
    return  (t1==t2) ;
  }
  public static boolean is_sort_Nat(Object t) {
    return  (t instanceof sl.jjterm.types.Nat) ;
  }
  public static boolean is_fun_sym_Zero( sl.jjterm.types.Nat  t) {
    return  (t instanceof sl.jjterm.types.nat.Zero) ;
  }
  public static  sl.jjterm.types.Nat  make_Zero() { 
    return  sl.jjterm.types.nat.Zero.make() ;
  }
  public static boolean is_fun_sym_Suc( sl.jjterm.types.Nat  t) {
    return  (t instanceof sl.jjterm.types.nat.Suc) ;
  }
  public static  sl.jjterm.types.Nat  make_Suc( sl.jjterm.types.Nat  t0) { 
    return  sl.jjterm.types.nat.Suc.make(t0) ;
  }
  public static  sl.jjterm.types.Nat  get_slot_Suc_n( sl.jjterm.types.Nat  t) {
    return  t.getn() ;
  }
  public static boolean is_fun_sym_C( sl.jjterm.types.Nat  t) {
    return  (t instanceof sl.jjterm.types.nat.C) ;
  }
  public static  sl.jjterm.types.Nat  make_C( sl.jjterm.types.Nat  t0,  sl.jjterm.types.Nat  t1) { 
    return  sl.jjterm.types.nat.C.make(t0, t1) ;
  }
  public static  sl.jjterm.types.Nat  get_slot_C_n1( sl.jjterm.types.Nat  t) {
    return  t.getn1() ;
  }
  public static  sl.jjterm.types.Nat  get_slot_C_n2( sl.jjterm.types.Nat  t) {
    return  t.getn2() ;
  }
  public static boolean is_fun_sym_F( sl.jjterm.types.Nat  t) {
    return  (t instanceof sl.jjterm.types.nat.F) ;
  }
  public static  sl.jjterm.types.Nat  make_F( sl.jjterm.types.Nat  t0,  sl.jjterm.types.Nat  t1,  sl.jjterm.types.Nat  t2,  sl.jjterm.types.Nat  t3,  sl.jjterm.types.Nat  t4) { 
    return  sl.jjterm.types.nat.F.make(t0, t1, t2, t3, t4) ;
  }
  public static  sl.jjterm.types.Nat  get_slot_F_n1( sl.jjterm.types.Nat  t) {
    return  t.getn1() ;
  }
  public static  sl.jjterm.types.Nat  get_slot_F_n2( sl.jjterm.types.Nat  t) {
    return  t.getn2() ;
  }
  public static  sl.jjterm.types.Nat  get_slot_F_n3( sl.jjterm.types.Nat  t) {
    return  t.getn3() ;
  }
  public static  sl.jjterm.types.Nat  get_slot_F_n4( sl.jjterm.types.Nat  t) {
    return  t.getn4() ;
  }
  public static  sl.jjterm.types.Nat  get_slot_F_n5( sl.jjterm.types.Nat  t) {
    return  t.getn5() ;
  }
  public static boolean is_fun_sym_M( sl.jjterm.types.Nat  t) {
    return  (t instanceof sl.jjterm.types.nat.M) ;
  }
  public static boolean is_fun_sym_N( sl.jjterm.types.Nat  t) {
    return  (t instanceof sl.jjterm.types.nat.N) ;
  }

  public static  sl.jjterm.types.Nat  make_M() { 
    return  sl.jjterm.types.nat.M.make() ;
  }
 
  public static  sl.jjterm.types.Nat  make_N() { 
    return  sl.jjterm.types.nat.N.make() ;
  }
  public static boolean is_fun_sym_P( sl.jjterm.types.Nat  t) {
    return  (t instanceof sl.jjterm.types.nat.P) ;
  }
  public static  sl.jjterm.types.Nat  make_P() { 
    return  sl.jjterm.types.nat.P.make() ;
  }

  public static class Rewrite implements Visitor {

    private Visitor any;
    private  sl.jjterm.types.Nat  base;

    public Rewrite( sl.jjterm.types.Nat  base) {
      any = new Identity();
      this.base=base;
    }

    public  sl.jjterm.types.Nat getbase() {
      return base;
    }

    public  sl.jjterm.types.Nat  visit_Nat( sl.jjterm.types.Nat  _arg) throws jjtraveler.VisitFailure {
      {
        {
          if (is_sort_Nat(_arg)) {
            if (is_fun_sym_M((( sl.jjterm.types.Nat )_arg))) {
              return make_Suc(make_Zero());
            }
          }
        }
        {
          if (is_sort_Nat(_arg)) {
            if (is_fun_sym_N((( sl.jjterm.types.Nat )_arg))) {

              return make_Suc(make_Suc(make_Suc(make_Zero())));
            }
          }
        }
        {
          if (is_sort_Nat(_arg)) {
            if (is_fun_sym_P((( sl.jjterm.types.Nat )_arg))) {

              return base;
            }
          }
        }
        {
          if (is_sort_Nat(_arg)) {
            if (is_fun_sym_C((( sl.jjterm.types.Nat )_arg))) {
              if (is_fun_sym_Zero(get_slot_C_n1((( sl.jjterm.types.Nat )_arg)))) {

                return get_slot_C_n2((( sl.jjterm.types.Nat )_arg));
              }
            }
          }
        }{
          if (is_sort_Nat(_arg)) {
            if (is_fun_sym_C((( sl.jjterm.types.Nat )_arg))) {
              sl.jjterm.types.Nat  tomMatch2NameNumber_freshVar_12=get_slot_C_n1((( sl.jjterm.types.Nat )_arg));
              if (is_fun_sym_Suc(tomMatch2NameNumber_freshVar_12)) {

                return make_Suc(make_C(get_slot_Suc_n(tomMatch2NameNumber_freshVar_12),get_slot_C_n2((( sl.jjterm.types.Nat )_arg))));
              }
            }
          }
        }
        {
          if (is_sort_Nat(_arg)) {
            if (is_fun_sym_F((( sl.jjterm.types.Nat )_arg))) {
              sl.jjterm.types.Nat  tomMatch2NameNumber_freshVar_20=get_slot_F_n3((( sl.jjterm.types.Nat )_arg));
              sl.jjterm.types.Nat  tomMatch2NameNumber_freshVar_21=get_slot_F_n4((( sl.jjterm.types.Nat )_arg));
              if (is_fun_sym_Suc(tomMatch2NameNumber_freshVar_20)) {

                return make_F(get_slot_F_n1((( sl.jjterm.types.Nat )_arg)),get_slot_F_n2((( sl.jjterm.types.Nat )_arg)),get_slot_Suc_n(tomMatch2NameNumber_freshVar_20),make_C(tomMatch2NameNumber_freshVar_21,tomMatch2NameNumber_freshVar_21),get_slot_F_n5((( sl.jjterm.types.Nat )_arg)));
              }
            }
          }
        }
        {
          if (is_sort_Nat(_arg)) {
            if (is_fun_sym_F((( sl.jjterm.types.Nat )_arg))) {
              sl.jjterm.types.Nat  tomMatch2NameNumber_freshVar_28=get_slot_F_n2((( sl.jjterm.types.Nat )_arg));
              sl.jjterm.types.Nat  tomMatch2NameNumber_freshVar_30=get_slot_F_n4((( sl.jjterm.types.Nat )_arg));
              if (is_fun_sym_Suc(tomMatch2NameNumber_freshVar_28)) {
                if (is_fun_sym_Zero(get_slot_F_n3((( sl.jjterm.types.Nat )_arg)))) {

                  return make_F(get_slot_F_n1((( sl.jjterm.types.Nat )_arg)),get_slot_Suc_n(tomMatch2NameNumber_freshVar_28),make_P(),tomMatch2NameNumber_freshVar_30,tomMatch2NameNumber_freshVar_30);
                }
              }
            }
          }
        }
        {
          if (is_sort_Nat(_arg)) {
            if (is_fun_sym_F((( sl.jjterm.types.Nat )_arg))) {
              sl.jjterm.types.Nat  tomMatch2NameNumber_freshVar_37=get_slot_F_n1((( sl.jjterm.types.Nat )_arg));
              if (is_fun_sym_Suc(tomMatch2NameNumber_freshVar_37)) {
                if (is_fun_sym_Zero(get_slot_F_n2((( sl.jjterm.types.Nat )_arg)))) {
                  if (is_fun_sym_Zero(get_slot_F_n3((( sl.jjterm.types.Nat )_arg)))) {


                    return make_F(get_slot_Suc_n(tomMatch2NameNumber_freshVar_37),make_N(),make_P(),make_Suc(make_Zero()),make_Zero());

                  }
                }
              }
            }
          }
        }
        {
          if (is_sort_Nat(_arg)) {
            if (is_fun_sym_F((( sl.jjterm.types.Nat )_arg))) {
              if (is_fun_sym_Zero(get_slot_F_n1((( sl.jjterm.types.Nat )_arg)))) {

                if (is_fun_sym_Zero(get_slot_F_n2((( sl.jjterm.types.Nat )_arg)))) {
                  if (is_fun_sym_Zero(get_slot_F_n3((( sl.jjterm.types.Nat )_arg)))) {

                    return get_slot_F_n4((( sl.jjterm.types.Nat )_arg));
                  }
                }
              }
            }
          }
        }
      }
      return _visit_Nat(_arg);
    }


    public  sl.jjterm.types.Nat  _visit_Nat( sl.jjterm.types.Nat arg) throws jjtraveler.VisitFailure {
      return (sl.jjterm.types.Nat) any.visit(arg);
    }

    public Visitable visit(Visitable v) throws jjtraveler.VisitFailure {
      if (is_sort_Nat(v)) {
        return visit_Nat((( sl.jjterm.types.Nat )v));
      }
      return any.visit(v);
    }

  }

public static void run(Nat subject, int version,  int count, Nat base) {
  System.out.print("jjtraveler : ");
  try {
    Visitor s1 = new RepeatIdJJ(new BottomUp(new Rewrite(base)));
    Visitor s2 = new InnermostIdJJ(new Rewrite(base));
    long startChrono = System.currentTimeMillis();
    for(int i = 0; i < count; ++i) {
      if(version==1) {
        s1.visit(subject);
        //System.out.println(s1.visit(subject));
      } else {
        s2.visit(subject);
        //System.out.println(s2.visit(subject));
      }
    }
    long stopChrono = System.currentTimeMillis();
    System.out.println((stopChrono-startChrono)/1000.);
  } catch(VisitFailure e) {
    System.out.println("failure");
  }
}

}
