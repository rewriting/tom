/* Generated by TOM (version 20070920 (src)): Do not edit this file */package policy;
import java.util.*;

import accesscontrol.*;
import accesscontrol.types.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class FlowPolicy implements Policy{
	/* Generated by TOM (version 20070920 (src)): Do not edit this file */private static boolean tom_equal_term_Strategy(Object t1, Object t2) { return t1.equals(t2);}private static boolean tom_is_sort_Strategy(Object t) { return  t instanceof tom.library.sl.Strategy ;}/* Generated by TOM (version 20070920 (src)): Do not edit this file *//* Generated by TOM (version 20070920 (src)): Do not edit this file */private static boolean tom_equal_term_int(int t1, int t2) { return  (t1==t2) ;}private static boolean tom_is_sort_int(int t) { return  true ;} /* Generated by TOM (version 20070920 (src)): Do not edit this file *//* Generated by TOM (version 20070920 (src)): Do not edit this file */ private static boolean tom_equal_term_String(String t1, String t2) { return  (t1.equals(t2)) ;}private static boolean tom_is_sort_String(String t) { return  t instanceof String ;}  private static  tom.library.sl.Strategy  tom_make_mu( tom.library.sl.Strategy  var,  tom.library.sl.Strategy  v) { return  new tom.library.sl.Mu(var,v) ; }private static  tom.library.sl.Strategy  tom_make_MuVar( String  name) { return  new tom.library.sl.MuVar(name) ; }private static  tom.library.sl.Strategy  tom_make_Identity() { return  new tom.library.sl.Identity() ; }private static boolean tom_is_fun_sym_SequenceId( tom.library.sl.Strategy  t) { return  (t instanceof tom.library.sl.SequenceId) ;}private static  tom.library.sl.Strategy  tom_empty_list_SequenceId() { return  null ; }private static  tom.library.sl.Strategy  tom_cons_list_SequenceId( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  (tail==null)?head:new tom.library.sl.SequenceId(head,tail) ; }private static  tom.library.sl.Strategy  tom_get_head_SequenceId_Strategy( tom.library.sl.Strategy  t) { return  (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.SequenceId.FIRST) ;}private static  tom.library.sl.Strategy  tom_get_tail_SequenceId_Strategy( tom.library.sl.Strategy  t) { return  (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.SequenceId.THEN) ;}private static boolean tom_is_empty_SequenceId_Strategy( tom.library.sl.Strategy  t) { return  t == null ;}   private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy l1,  tom.library.sl.Strategy  l2) {     if(tom_is_empty_SequenceId_Strategy(l1)) {       return l2;     } else if(tom_is_empty_SequenceId_Strategy(l2)) {       return l1;     } else if(tom_is_fun_sym_SequenceId(l1)) {       if(tom_is_empty_SequenceId_Strategy(((tom_is_fun_sym_SequenceId(l1))?tom_get_tail_SequenceId_Strategy(l1):tom_empty_list_SequenceId()))) {         return ( tom.library.sl.Strategy )tom_cons_list_SequenceId(((tom_is_fun_sym_SequenceId(l1))?tom_get_head_SequenceId_Strategy(l1):l1),l2);       } else {         return ( tom.library.sl.Strategy )tom_cons_list_SequenceId(((tom_is_fun_sym_SequenceId(l1))?tom_get_head_SequenceId_Strategy(l1):l1),tom_append_list_SequenceId(((tom_is_fun_sym_SequenceId(l1))?tom_get_tail_SequenceId_Strategy(l1):tom_empty_list_SequenceId()),l2));       }     } else {       return ( tom.library.sl.Strategy )tom_cons_list_SequenceId(l1, l2);     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if(tom_equal_term_Strategy(begin,end)) {       return tail;     } else {       return ( tom.library.sl.Strategy )tom_cons_list_SequenceId(((tom_is_fun_sym_SequenceId(begin))?tom_get_head_SequenceId_Strategy(begin):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((tom_is_fun_sym_SequenceId(begin))?tom_get_tail_SequenceId_Strategy(begin):tom_empty_list_SequenceId()),end,tail));     }   }    /* Generated by TOM (version 20070920 (src)): Do not edit this file */ /* Generated by TOM (version 20070920 (src)): Do not edit this file */private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_SequenceId(v,tom_cons_list_SequenceId(tom_make_MuVar("_x"),tom_empty_list_SequenceId()))) ; }   /* Generated by TOM (version 20070920 (src)): Do not edit this file *//* Generated by TOM (version 20070920 (src)): Do not edit this file */ private static boolean tom_equal_term_ListOfAccesses(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ListOfAccesses(Object t) { return  t instanceof accesscontrol.types.ListOfAccesses ;}private static boolean tom_equal_term_Request(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Request(Object t) { return  t instanceof accesscontrol.types.Request ;}private static boolean tom_equal_term_Subject(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Subject(Object t) { return  t instanceof accesscontrol.types.Subject ;}private static boolean tom_equal_term_RequestType(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_RequestType(Object t) { return  t instanceof accesscontrol.types.RequestType ;}private static boolean tom_equal_term_AccessMode(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_AccessMode(Object t) { return  t instanceof accesscontrol.types.AccessMode ;}private static boolean tom_equal_term_State(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_State(Object t) { return  t instanceof accesscontrol.types.State ;}private static boolean tom_equal_term_RequestUponState(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_RequestUponState(Object t) { return  t instanceof accesscontrol.types.RequestUponState ;}private static boolean tom_equal_term_SecurityObject(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_SecurityObject(Object t) { return  t instanceof accesscontrol.types.SecurityObject ;}private static boolean tom_equal_term_AccessType(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_AccessType(Object t) { return  t instanceof accesscontrol.types.AccessType ;}private static boolean tom_equal_term_Access(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Access(Object t) { return  t instanceof accesscontrol.types.Access ;}private static  accesscontrol.types.Request  tom_make_request( accesscontrol.types.RequestType  t0,  accesscontrol.types.Access  t1) { return  accesscontrol.types.request.request.make(t0, t1) ; }private static  accesscontrol.types.RequestType  tom_make_add() { return  accesscontrol.types.requesttype.add.make() ; }private static boolean tom_is_fun_sym_am( accesscontrol.types.AccessMode  t) { return  t instanceof accesscontrol.types.accessmode.am ;}private static  accesscontrol.types.AccessMode  tom_make_am( int  t0) { return  accesscontrol.types.accessmode.am.make(t0) ; }private static  int  tom_get_slot_am_m( accesscontrol.types.AccessMode  t) { return  t.getm() ;}private static boolean tom_is_fun_sym_state( accesscontrol.types.State  t) { return  t instanceof accesscontrol.types.state.state ;}private static  accesscontrol.types.State  tom_make_state( accesscontrol.types.ListOfAccesses  t0,  accesscontrol.types.ListOfAccesses  t1) { return  accesscontrol.types.state.state.make(t0, t1) ; }private static  accesscontrol.types.ListOfAccesses  tom_get_slot_state_reads( accesscontrol.types.State  t) { return  t.getreads() ;}private static  accesscontrol.types.ListOfAccesses  tom_get_slot_state_writes( accesscontrol.types.State  t) { return  t.getwrites() ;}private static  accesscontrol.types.RequestUponState  tom_make_rus( accesscontrol.types.Request  t0,  accesscontrol.types.State  t1) { return  accesscontrol.types.requestuponstate.rus.make(t0, t1) ; }private static boolean tom_is_fun_sym_implicit( accesscontrol.types.AccessType  t) { return  t instanceof accesscontrol.types.accesstype.implicit ;}private static  accesscontrol.types.AccessType  tom_make_implicit() { return  accesscontrol.types.accesstype.implicit.make() ; }private static boolean tom_is_fun_sym_access( accesscontrol.types.Access  t) { return  t instanceof accesscontrol.types.access.access ;}private static  accesscontrol.types.Access  tom_make_access( accesscontrol.types.Subject  t0,  accesscontrol.types.SecurityObject  t1,  accesscontrol.types.AccessMode  t2,  accesscontrol.types.AccessType  t3) { return  accesscontrol.types.access.access.make(t0, t1, t2, t3) ; }private static  accesscontrol.types.Subject  tom_get_slot_access_subject( accesscontrol.types.Access  t) { return  t.getsubject() ;}private static  accesscontrol.types.SecurityObject  tom_get_slot_access_securityObject( accesscontrol.types.Access  t) { return  t.getsecurityObject() ;}private static  accesscontrol.types.AccessMode  tom_get_slot_access_a( accesscontrol.types.Access  t) { return  t.geta() ;}private static  accesscontrol.types.AccessType  tom_get_slot_access_e( accesscontrol.types.Access  t) { return  t.gete() ;}private static boolean tom_is_fun_sym_accesses( accesscontrol.types.ListOfAccesses  t) { return  t instanceof accesscontrol.types.listofaccesses.Consaccesses || t instanceof accesscontrol.types.listofaccesses.Emptyaccesses ;}private static  accesscontrol.types.ListOfAccesses  tom_empty_list_accesses() { return  accesscontrol.types.listofaccesses.Emptyaccesses.make() ; }private static  accesscontrol.types.ListOfAccesses  tom_cons_list_accesses( accesscontrol.types.Access  e,  accesscontrol.types.ListOfAccesses  l) { return  accesscontrol.types.listofaccesses.Consaccesses.make(e,l) ; }private static  accesscontrol.types.Access  tom_get_head_accesses_ListOfAccesses( accesscontrol.types.ListOfAccesses  l) { return  l.getHeadaccesses() ;}private static  accesscontrol.types.ListOfAccesses  tom_get_tail_accesses_ListOfAccesses( accesscontrol.types.ListOfAccesses  l) { return  l.getTailaccesses() ;}private static boolean tom_is_empty_accesses_ListOfAccesses( accesscontrol.types.ListOfAccesses  l) { return  l.isEmptyaccesses() ;}   private static   accesscontrol.types.ListOfAccesses  tom_append_list_accesses( accesscontrol.types.ListOfAccesses l1,  accesscontrol.types.ListOfAccesses  l2) {     if(tom_is_empty_accesses_ListOfAccesses(l1)) {       return l2;     } else if(tom_is_empty_accesses_ListOfAccesses(l2)) {       return l1;     } else if(tom_is_empty_accesses_ListOfAccesses(tom_get_tail_accesses_ListOfAccesses(l1))) {       return ( accesscontrol.types.ListOfAccesses )tom_cons_list_accesses(tom_get_head_accesses_ListOfAccesses(l1),l2);     } else {       return ( accesscontrol.types.ListOfAccesses )tom_cons_list_accesses(tom_get_head_accesses_ListOfAccesses(l1),tom_append_list_accesses(tom_get_tail_accesses_ListOfAccesses(l1),l2));     }   }   private static   accesscontrol.types.ListOfAccesses  tom_get_slice_accesses( accesscontrol.types.ListOfAccesses  begin,  accesscontrol.types.ListOfAccesses  end, accesscontrol.types.ListOfAccesses  tail) {     if(tom_equal_term_ListOfAccesses(begin,end)) {       return tail;     } else {       return ( accesscontrol.types.ListOfAccesses )tom_cons_list_accesses(tom_get_head_accesses_ListOfAccesses(begin),( accesscontrol.types.ListOfAccesses )tom_get_slice_accesses(tom_get_tail_accesses_ListOfAccesses(begin),end,tail));     }   }    



public PartiallyOrderdedSetOfSecurityLevels securityLevelsOrderImproved;




private static class makeExplicit extends  accesscontrol.accesscontrolBasicStrategy  {public makeExplicit() { super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() { return 1; }public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}public  accesscontrol.types.State  visit_State( accesscontrol.types.State  tom__arg) throws tom.library.sl.VisitFailure {if (tom_is_sort_State(tom__arg)) {{  accesscontrol.types.State  tomMatch45NameNumberfreshSubject_1=(( accesscontrol.types.State )tom__arg);if (tom_is_fun_sym_state(tomMatch45NameNumberfreshSubject_1)) {{  accesscontrol.types.ListOfAccesses  tomMatch45NameNumber_freshVar_0=tom_get_slot_state_reads(tomMatch45NameNumberfreshSubject_1);{  accesscontrol.types.ListOfAccesses  tomMatch45NameNumber_freshVar_1=tom_get_slot_state_writes(tomMatch45NameNumberfreshSubject_1);if (tom_is_fun_sym_accesses(tomMatch45NameNumber_freshVar_0)) {{  accesscontrol.types.ListOfAccesses  tomMatch45NameNumber_freshVar_2=tomMatch45NameNumber_freshVar_0;{  accesscontrol.types.ListOfAccesses  tom_reads=tomMatch45NameNumber_freshVar_0;{  accesscontrol.types.ListOfAccesses  tomMatch45NameNumber_begin_4=tomMatch45NameNumber_freshVar_2;{  accesscontrol.types.ListOfAccesses  tomMatch45NameNumber_end_5=tomMatch45NameNumber_freshVar_2;do {{{  accesscontrol.types.ListOfAccesses  tom_x1=tom_get_slice_accesses(tomMatch45NameNumber_begin_4,tomMatch45NameNumber_end_5,tom_empty_list_accesses());{  accesscontrol.types.ListOfAccesses  tomMatch45NameNumber_freshVar_3=tomMatch45NameNumber_end_5;if (!(tom_is_empty_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_3))) {if (tom_is_fun_sym_access(tom_get_head_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_3))) {{  accesscontrol.types.Subject  tomMatch45NameNumber_freshVar_122=tom_get_slot_access_subject(tom_get_head_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_3));{  accesscontrol.types.SecurityObject  tomMatch45NameNumber_freshVar_123=tom_get_slot_access_securityObject(tom_get_head_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_3));{  accesscontrol.types.AccessMode  tomMatch45NameNumber_freshVar_124=tom_get_slot_access_a(tom_get_head_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_3));{  accesscontrol.types.AccessType  tomMatch45NameNumber_freshVar_125=tom_get_slot_access_e(tom_get_head_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_3));{  accesscontrol.types.Subject  tom_s1=tomMatch45NameNumber_freshVar_122;{  accesscontrol.types.SecurityObject  tom_o1=tomMatch45NameNumber_freshVar_123;if (tom_is_fun_sym_am(tomMatch45NameNumber_freshVar_124)) {{  int  tomMatch45NameNumber_freshVar_126=tom_get_slot_am_m(tomMatch45NameNumber_freshVar_124);if (tom_equal_term_int(0, tomMatch45NameNumber_freshVar_126)) {{  accesscontrol.types.ListOfAccesses  tomMatch45NameNumber_freshVar_6=tom_get_tail_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_3);{  accesscontrol.types.ListOfAccesses  tomMatch45NameNumber_begin_8=tomMatch45NameNumber_freshVar_6;{  accesscontrol.types.ListOfAccesses  tomMatch45NameNumber_end_9=tomMatch45NameNumber_freshVar_6;do {{{  accesscontrol.types.ListOfAccesses  tom_x2=tom_get_slice_accesses(tomMatch45NameNumber_begin_8,tomMatch45NameNumber_end_9,tom_empty_list_accesses());{  accesscontrol.types.ListOfAccesses  tomMatch45NameNumber_freshVar_7=tomMatch45NameNumber_end_9;if (!(tom_is_empty_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_7))) {if (tom_is_fun_sym_access(tom_get_head_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_7))) {{  accesscontrol.types.Subject  tomMatch45NameNumber_freshVar_127=tom_get_slot_access_subject(tom_get_head_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_7));{  accesscontrol.types.SecurityObject  tomMatch45NameNumber_freshVar_128=tom_get_slot_access_securityObject(tom_get_head_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_7));{  accesscontrol.types.AccessMode  tomMatch45NameNumber_freshVar_129=tom_get_slot_access_a(tom_get_head_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_7));{  accesscontrol.types.AccessType  tomMatch45NameNumber_freshVar_130=tom_get_slot_access_e(tom_get_head_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_7));{  accesscontrol.types.Subject  tom_s2=tomMatch45NameNumber_freshVar_127;{  accesscontrol.types.SecurityObject  tom_o2=tomMatch45NameNumber_freshVar_128;if (tom_is_fun_sym_am(tomMatch45NameNumber_freshVar_129)) {{  int  tomMatch45NameNumber_freshVar_131=tom_get_slot_am_m(tomMatch45NameNumber_freshVar_129);if (tom_equal_term_int(0, tomMatch45NameNumber_freshVar_131)) {{  accesscontrol.types.ListOfAccesses  tomMatch45NameNumber_freshVar_10=tom_get_tail_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_7);{  accesscontrol.types.ListOfAccesses  tom_x3=tomMatch45NameNumber_freshVar_10;if (tom_is_fun_sym_accesses(tomMatch45NameNumber_freshVar_1)) {{  accesscontrol.types.ListOfAccesses  tomMatch45NameNumber_freshVar_12=tomMatch45NameNumber_freshVar_1;{  accesscontrol.types.ListOfAccesses  tom_writes=tomMatch45NameNumber_freshVar_1;{  accesscontrol.types.ListOfAccesses  tomMatch45NameNumber_begin_14=tomMatch45NameNumber_freshVar_12;{  accesscontrol.types.ListOfAccesses  tomMatch45NameNumber_end_15=tomMatch45NameNumber_freshVar_12;do {{{  accesscontrol.types.ListOfAccesses  tom_x4=tom_get_slice_accesses(tomMatch45NameNumber_begin_14,tomMatch45NameNumber_end_15,tom_empty_list_accesses());{  accesscontrol.types.ListOfAccesses  tomMatch45NameNumber_freshVar_13=tomMatch45NameNumber_end_15;if (!(tom_is_empty_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_13))) {if (tom_is_fun_sym_access(tom_get_head_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_13))) {{  accesscontrol.types.Subject  tomMatch45NameNumber_freshVar_132=tom_get_slot_access_subject(tom_get_head_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_13));{  accesscontrol.types.SecurityObject  tomMatch45NameNumber_freshVar_133=tom_get_slot_access_securityObject(tom_get_head_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_13));{  accesscontrol.types.AccessMode  tomMatch45NameNumber_freshVar_134=tom_get_slot_access_a(tom_get_head_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_13));{  accesscontrol.types.AccessType  tomMatch45NameNumber_freshVar_135=tom_get_slot_access_e(tom_get_head_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_13));{  accesscontrol.types.Subject  tom_s=tomMatch45NameNumber_freshVar_132;{  accesscontrol.types.SecurityObject  tom_o=tomMatch45NameNumber_freshVar_133;if (tom_is_fun_sym_am(tomMatch45NameNumber_freshVar_134)) {{  int  tomMatch45NameNumber_freshVar_136=tom_get_slot_am_m(tomMatch45NameNumber_freshVar_134);if (tom_equal_term_int(1, tomMatch45NameNumber_freshVar_136)) {{  accesscontrol.types.ListOfAccesses  tomMatch45NameNumber_freshVar_16=tom_get_tail_accesses_ListOfAccesses(tomMatch45NameNumber_freshVar_13);{  accesscontrol.types.ListOfAccesses  tom_x5=tomMatch45NameNumber_freshVar_16;if ( true ) {



        if (((tom_s==tom_s1&& tom_o==tom_o2))){
          ListOfAccesses l=tom_append_list_accesses(tom_x1,tom_append_list_accesses(tom_x2,tom_append_list_accesses(tom_x3,tom_empty_list_accesses())));
          boolean contains=false;
          if (tom_is_sort_ListOfAccesses(l)) {{  accesscontrol.types.ListOfAccesses  tomMatch46NameNumberfreshSubject_1=(( accesscontrol.types.ListOfAccesses )l);if (tom_is_fun_sym_accesses(tomMatch46NameNumberfreshSubject_1)) {{  accesscontrol.types.ListOfAccesses  tomMatch46NameNumber_freshVar_0=tomMatch46NameNumberfreshSubject_1;{  accesscontrol.types.ListOfAccesses  tomMatch46NameNumber_begin_2=tomMatch46NameNumber_freshVar_0;{  accesscontrol.types.ListOfAccesses  tomMatch46NameNumber_end_3=tomMatch46NameNumber_freshVar_0;do {{{  accesscontrol.types.ListOfAccesses  tom_x=tom_get_slice_accesses(tomMatch46NameNumber_begin_2,tomMatch46NameNumber_end_3,tom_empty_list_accesses());{  accesscontrol.types.ListOfAccesses  tomMatch46NameNumber_freshVar_1=tomMatch46NameNumber_end_3;if (!(tom_is_empty_accesses_ListOfAccesses(tomMatch46NameNumber_freshVar_1))) {if (tom_is_fun_sym_access(tom_get_head_accesses_ListOfAccesses(tomMatch46NameNumber_freshVar_1))) {{  accesscontrol.types.Subject  tomMatch46NameNumber_freshVar_20=tom_get_slot_access_subject(tom_get_head_accesses_ListOfAccesses(tomMatch46NameNumber_freshVar_1));{  accesscontrol.types.SecurityObject  tomMatch46NameNumber_freshVar_21=tom_get_slot_access_securityObject(tom_get_head_accesses_ListOfAccesses(tomMatch46NameNumber_freshVar_1));{  accesscontrol.types.AccessMode  tomMatch46NameNumber_freshVar_22=tom_get_slot_access_a(tom_get_head_accesses_ListOfAccesses(tomMatch46NameNumber_freshVar_1));{  accesscontrol.types.AccessType  tomMatch46NameNumber_freshVar_23=tom_get_slot_access_e(tom_get_head_accesses_ListOfAccesses(tomMatch46NameNumber_freshVar_1));{  accesscontrol.types.Subject  tom_s3=tomMatch46NameNumber_freshVar_20;{  accesscontrol.types.SecurityObject  tom_o3=tomMatch46NameNumber_freshVar_21;if (tom_is_fun_sym_am(tomMatch46NameNumber_freshVar_22)) {{  int  tomMatch46NameNumber_freshVar_24=tom_get_slot_am_m(tomMatch46NameNumber_freshVar_22);if (tom_equal_term_int(0, tomMatch46NameNumber_freshVar_24)) {{  accesscontrol.types.ListOfAccesses  tomMatch46NameNumber_freshVar_4=tom_get_tail_accesses_ListOfAccesses(tomMatch46NameNumber_freshVar_1);{  accesscontrol.types.ListOfAccesses  tom_y=tomMatch46NameNumber_freshVar_4;if ( true ) {

              if (tom_s2==tom_s3&& tom_o1==tom_o3){
                contains=true;
              }
            }}}}}}}}}}}}}}}}if (tom_is_empty_accesses_ListOfAccesses(tomMatch46NameNumber_end_3)) {tomMatch46NameNumber_end_3=tomMatch46NameNumber_begin_2;} else {tomMatch46NameNumber_end_3=tom_get_tail_accesses_ListOfAccesses(tomMatch46NameNumber_end_3);}}} while(!(tom_equal_term_ListOfAccesses(tomMatch46NameNumber_end_3, tomMatch46NameNumber_begin_2)));}}}}}}
 
          if (contains) return tom_make_state(tom_reads,tom_writes);
          else return tom_make_state(tom_cons_list_accesses(tom_make_access(tom_s2,tom_o1,tom_make_am(0),tom_make_implicit()),tom_append_list_accesses(tom_reads,tom_empty_list_accesses())),tom_writes);
        }else  if (((tom_s==tom_s2&& tom_o==tom_o1))){
          ListOfAccesses l=tom_append_list_accesses(tom_x1,tom_append_list_accesses(tom_x2,tom_append_list_accesses(tom_x3,tom_empty_list_accesses())));
          boolean contains=false;
          if (tom_is_sort_ListOfAccesses(l)) {{  accesscontrol.types.ListOfAccesses  tomMatch47NameNumberfreshSubject_1=(( accesscontrol.types.ListOfAccesses )l);if (tom_is_fun_sym_accesses(tomMatch47NameNumberfreshSubject_1)) {{  accesscontrol.types.ListOfAccesses  tomMatch47NameNumber_freshVar_0=tomMatch47NameNumberfreshSubject_1;{  accesscontrol.types.ListOfAccesses  tomMatch47NameNumber_begin_2=tomMatch47NameNumber_freshVar_0;{  accesscontrol.types.ListOfAccesses  tomMatch47NameNumber_end_3=tomMatch47NameNumber_freshVar_0;do {{{  accesscontrol.types.ListOfAccesses  tom_x=tom_get_slice_accesses(tomMatch47NameNumber_begin_2,tomMatch47NameNumber_end_3,tom_empty_list_accesses());{  accesscontrol.types.ListOfAccesses  tomMatch47NameNumber_freshVar_1=tomMatch47NameNumber_end_3;if (!(tom_is_empty_accesses_ListOfAccesses(tomMatch47NameNumber_freshVar_1))) {if (tom_is_fun_sym_access(tom_get_head_accesses_ListOfAccesses(tomMatch47NameNumber_freshVar_1))) {{  accesscontrol.types.Subject  tomMatch47NameNumber_freshVar_20=tom_get_slot_access_subject(tom_get_head_accesses_ListOfAccesses(tomMatch47NameNumber_freshVar_1));{  accesscontrol.types.SecurityObject  tomMatch47NameNumber_freshVar_21=tom_get_slot_access_securityObject(tom_get_head_accesses_ListOfAccesses(tomMatch47NameNumber_freshVar_1));{  accesscontrol.types.AccessMode  tomMatch47NameNumber_freshVar_22=tom_get_slot_access_a(tom_get_head_accesses_ListOfAccesses(tomMatch47NameNumber_freshVar_1));{  accesscontrol.types.AccessType  tomMatch47NameNumber_freshVar_23=tom_get_slot_access_e(tom_get_head_accesses_ListOfAccesses(tomMatch47NameNumber_freshVar_1));{  accesscontrol.types.Subject  tom_s3=tomMatch47NameNumber_freshVar_20;{  accesscontrol.types.SecurityObject  tom_o3=tomMatch47NameNumber_freshVar_21;if (tom_is_fun_sym_am(tomMatch47NameNumber_freshVar_22)) {{  int  tomMatch47NameNumber_freshVar_24=tom_get_slot_am_m(tomMatch47NameNumber_freshVar_22);if (tom_equal_term_int(0, tomMatch47NameNumber_freshVar_24)) {{  accesscontrol.types.ListOfAccesses  tomMatch47NameNumber_freshVar_4=tom_get_tail_accesses_ListOfAccesses(tomMatch47NameNumber_freshVar_1);{  accesscontrol.types.ListOfAccesses  tom_y=tomMatch47NameNumber_freshVar_4;if ( true ) {

              if (tom_s1==tom_s3&& tom_o2==tom_o3){
                contains=true;
              }
            }}}}}}}}}}}}}}}}if (tom_is_empty_accesses_ListOfAccesses(tomMatch47NameNumber_end_3)) {tomMatch47NameNumber_end_3=tomMatch47NameNumber_begin_2;} else {tomMatch47NameNumber_end_3=tom_get_tail_accesses_ListOfAccesses(tomMatch47NameNumber_end_3);}}} while(!(tom_equal_term_ListOfAccesses(tomMatch47NameNumber_end_3, tomMatch47NameNumber_begin_2)));}}}}}}
 
          if (contains) return tom_make_state(tom_reads,tom_writes);
          else return tom_make_state(tom_cons_list_accesses(tom_make_access(tom_s1,tom_o2,tom_make_am(0),tom_make_implicit()),tom_append_list_accesses(tom_reads,tom_empty_list_accesses())),tom_writes);
        }
      }}}}}}}}}}}}}}}}if (tom_is_empty_accesses_ListOfAccesses(tomMatch45NameNumber_end_15)) {tomMatch45NameNumber_end_15=tomMatch45NameNumber_begin_14;} else {tomMatch45NameNumber_end_15=tom_get_tail_accesses_ListOfAccesses(tomMatch45NameNumber_end_15);}}} while(!(tom_equal_term_ListOfAccesses(tomMatch45NameNumber_end_15, tomMatch45NameNumber_begin_14)));}}}}}}}}}}}}}}}}}}}}if (tom_is_empty_accesses_ListOfAccesses(tomMatch45NameNumber_end_9)) {tomMatch45NameNumber_end_9=tomMatch45NameNumber_begin_8;} else {tomMatch45NameNumber_end_9=tom_get_tail_accesses_ListOfAccesses(tomMatch45NameNumber_end_9);}}} while(!(tom_equal_term_ListOfAccesses(tomMatch45NameNumber_end_9, tomMatch45NameNumber_begin_8)));}}}}}}}}}}}}}}}}if (tom_is_empty_accesses_ListOfAccesses(tomMatch45NameNumber_end_5)) {tomMatch45NameNumber_end_5=tomMatch45NameNumber_begin_4;} else {tomMatch45NameNumber_end_5=tom_get_tail_accesses_ListOfAccesses(tomMatch45NameNumber_end_5);}}} while(!(tom_equal_term_ListOfAccesses(tomMatch45NameNumber_end_5, tomMatch45NameNumber_begin_4)));}}}}}}}}}}return super.visit_State(tom__arg); }}private static  tom.library.sl.Strategy  tom_make_makeExplicit() { return new makeExplicit(); }



  //Verification of state
  public boolean valid(State setOfAccesses){
	 RequestUponState currentRequestOfScenario;
	 ArrayList<RequestUponState> implicitRequestsUponOriginalState=new ArrayList<RequestUponState>();
    try {
    	//make explicit implicit accesses
      State res=(State)tom_make_RepeatId(tom_make_makeExplicit()).visit(setOfAccesses);
      implicitRequestsUponOriginalState=new ArrayList<RequestUponState>();
      	//add implicit accesses to "implicitRequestsUponOriginalState"
      if (tom_is_sort_State(res)) {{  accesscontrol.types.State  tomMatch48NameNumberfreshSubject_1=(( accesscontrol.types.State )res);if (tom_is_fun_sym_state(tomMatch48NameNumberfreshSubject_1)) {{  accesscontrol.types.ListOfAccesses  tomMatch48NameNumber_freshVar_0=tom_get_slot_state_writes(tomMatch48NameNumberfreshSubject_1);if (tom_is_fun_sym_accesses(tomMatch48NameNumber_freshVar_0)) {{  accesscontrol.types.ListOfAccesses  tomMatch48NameNumber_freshVar_1=tomMatch48NameNumber_freshVar_0;{  accesscontrol.types.ListOfAccesses  tomMatch48NameNumber_begin_3=tomMatch48NameNumber_freshVar_1;{  accesscontrol.types.ListOfAccesses  tomMatch48NameNumber_end_4=tomMatch48NameNumber_freshVar_1;do {{{  accesscontrol.types.ListOfAccesses  tomMatch48NameNumber_freshVar_2=tomMatch48NameNumber_end_4;if (!(tom_is_empty_accesses_ListOfAccesses(tomMatch48NameNumber_freshVar_2))) {if (tom_is_fun_sym_access(tom_get_head_accesses_ListOfAccesses(tomMatch48NameNumber_freshVar_2))) {{  accesscontrol.types.AccessType  tomMatch48NameNumber_freshVar_7=tom_get_slot_access_e(tom_get_head_accesses_ListOfAccesses(tomMatch48NameNumber_freshVar_2));if (tom_is_fun_sym_implicit(tomMatch48NameNumber_freshVar_7)) {{  accesscontrol.types.Access  tom_a=tom_get_head_accesses_ListOfAccesses(tomMatch48NameNumber_freshVar_2);{  accesscontrol.types.ListOfAccesses  tomMatch48NameNumber_freshVar_5=tom_get_tail_accesses_ListOfAccesses(tomMatch48NameNumber_freshVar_2);if ( true ) {
implicitRequestsUponOriginalState.add(tom_make_rus(tom_make_request(tom_make_add(),tom_a),setOfAccesses));}}}}}}}}if (tom_is_empty_accesses_ListOfAccesses(tomMatch48NameNumber_end_4)) {tomMatch48NameNumber_end_4=tomMatch48NameNumber_begin_3;} else {tomMatch48NameNumber_end_4=tom_get_tail_accesses_ListOfAccesses(tomMatch48NameNumber_end_4);}}} while(!(tom_equal_term_ListOfAccesses(tomMatch48NameNumber_end_4, tomMatch48NameNumber_begin_3)));}}}}}}if (tom_is_fun_sym_state(tomMatch48NameNumberfreshSubject_1)) {{  accesscontrol.types.ListOfAccesses  tomMatch48NameNumber_freshVar_8=tom_get_slot_state_reads(tomMatch48NameNumberfreshSubject_1);if (tom_is_fun_sym_accesses(tomMatch48NameNumber_freshVar_8)) {{  accesscontrol.types.ListOfAccesses  tomMatch48NameNumber_freshVar_9=tomMatch48NameNumber_freshVar_8;{  accesscontrol.types.ListOfAccesses  tomMatch48NameNumber_begin_11=tomMatch48NameNumber_freshVar_9;{  accesscontrol.types.ListOfAccesses  tomMatch48NameNumber_end_12=tomMatch48NameNumber_freshVar_9;do {{{  accesscontrol.types.ListOfAccesses  tomMatch48NameNumber_freshVar_10=tomMatch48NameNumber_end_12;if (!(tom_is_empty_accesses_ListOfAccesses(tomMatch48NameNumber_freshVar_10))) {if (tom_is_fun_sym_access(tom_get_head_accesses_ListOfAccesses(tomMatch48NameNumber_freshVar_10))) {{  accesscontrol.types.AccessType  tomMatch48NameNumber_freshVar_15=tom_get_slot_access_e(tom_get_head_accesses_ListOfAccesses(tomMatch48NameNumber_freshVar_10));if (tom_is_fun_sym_implicit(tomMatch48NameNumber_freshVar_15)) {{  accesscontrol.types.Access  tom_a=tom_get_head_accesses_ListOfAccesses(tomMatch48NameNumber_freshVar_10);{  accesscontrol.types.ListOfAccesses  tomMatch48NameNumber_freshVar_13=tom_get_tail_accesses_ListOfAccesses(tomMatch48NameNumber_freshVar_10);if ( true ) {
implicitRequestsUponOriginalState.add(tom_make_rus(tom_make_request(tom_make_add(),tom_a),setOfAccesses));}}}}}}}}if (tom_is_empty_accesses_ListOfAccesses(tomMatch48NameNumber_end_12)) {tomMatch48NameNumber_end_12=tomMatch48NameNumber_begin_11;} else {tomMatch48NameNumber_end_12=tom_get_tail_accesses_ListOfAccesses(tomMatch48NameNumber_end_12);}}} while(!(tom_equal_term_ListOfAccesses(tomMatch48NameNumber_end_12, tomMatch48NameNumber_begin_11)));}}}}}}}}


      //for each implicit access 
      for(Iterator<RequestUponState> iterator=implicitRequestsUponOriginalState.iterator(); iterator.hasNext();){
        RequestUponState iruos=(RequestUponState)iterator.next();
        //test if the implicit access is accepted
        if (!(transition(iruos).getgranted())){
        	//behavior if the access is not granted which means that there is a leakage
          currentRequestOfScenario=iruos;
          System.out.print("Scenario detected :"+currentRequestOfScenario);
          return false;
        }
      }


    } catch (Exception e) {
      System.out.println("A problem occured while applying strategy");
    }
    // behavior if the access is granted
    return true;

  }

  // For a fixed configuration of subjects and objects and a given permutation for requests  
  public Object[] check(RequestUponState rus){
	  Object[] result=new Object[2];
	  result[1]=true;
      	  // try to add the access to the state given the implementation of the policy
      Response response=transition(rus);
      	  // get the new state whether it has changed or not
      result[0]=response.getstate();
      	  // if the request is granted
      if(response.getgranted()) {
    	  	// Verify the new state, if the verification fails generate an error message and return false
        if (valid((State)result[0])==false){
        	//System.out.println("Information leakage detected");
        	result[1]=false;
        }
      }else{
    	  result[1]=true;
      }
      
	  	  //else generate a message and return the result 
    //System.out.println("No information leakage detected");
    return result;
  }

}
