package mi2;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;


import base.data.types.*;
import base.data.types.t1.*;
import base.data.types.t2.*;
import tom.library.sl.*;

public class MainGom extends TestCase {

    private static boolean tom_equal_term_int(int t1, int t2) {
        return t1 == t2;
    }

    private static boolean tom_is_sort_int(int t) {
        return true;
    }

    private static boolean tom_equal_term_boolean(boolean t1, boolean t2) {
        return t1 == t2;
    }

    private static boolean tom_is_sort_boolean(boolean t) {
        return true;
    }

    private static boolean tom_equal_term_char(char t1, char t2) {
        return t1 == t2;
    }

    private static boolean tom_is_sort_char(char t) {
        return true;
    }

    private static boolean tom_equal_term_String(String t1, String t2) {
        return t1.equals(t2);
    }

    private static boolean tom_is_sort_String(String t) {
        return t instanceof String;
    }

    private static boolean tom_equal_term_T1(Object t1, Object t2) {
        return t1.equals(t2);
    }

    private static boolean tom_is_sort_T1(Object t) {
        return t instanceof base.data.types.T1;
    }

    private static boolean tom_equal_term_T2(Object t1, Object t2) {
        return t1.equals(t2);
    }

    private static boolean tom_is_sort_T2(Object t) {
        return t instanceof base.data.types.T2;
    }

    private static base.data.types.T1 tom_make_a() {
        return mi2.gom.Module.a_Mapping.instance.make();
    }

    private static boolean tom_is_fun_sym_a(base.data.types.T1 t) {
        return mi2.gom.Module.a_Mapping.instance.isSym(t);
    }

    private static base.data.types.T2 tom_make_b() {
        return mi2.gom.Module.b_Mapping.instance.make();
    }

    private static boolean tom_is_fun_sym_b(base.data.types.T2 t) {
        return mi2.gom.Module.b_Mapping.instance.isSym(t);
    }

    private static base.data.types.T1 tom_make_f(base.data.types.T1 s1, base.data.types.T2 s2) {
        return mi2.gom.Module.f_Mapping.instance.make(s1, s2);
    }

    private static boolean tom_is_fun_sym_f(base.data.types.T1 t) {
        return mi2.gom.Module.f_Mapping.instance.isSym(t);
    }

    private static base.data.types.T1 tom_get_slot_f_s1(base.data.types.T1 t) {
        return mi2.gom.Module.f_Mapping.instance.gets1(t);
    }

    private static base.data.types.T2 tom_get_slot_f_s2(base.data.types.T1 t) {
        return mi2.gom.Module.f_Mapping.instance.gets2(t);
    }

    private static base.data.types.T2 tom_make_g(base.data.types.T2 s2) {
        return mi2.gom.Module.g_Mapping.instance.make(s2);
    }

    private static boolean tom_is_fun_sym_g(base.data.types.T2 t) {
        return mi2.gom.Module.g_Mapping.instance.isSym(t);
    }

    private static base.data.types.T2 tom_get_slot_g_s2(base.data.types.T2 t) {
        return mi2.gom.Module.g_Mapping.instance.gets2(t);
    }


    private static boolean tom_equal_term_Strategy(Object t1, Object t2) {
        return (t1.equals(t2));
    }

    private static boolean tom_is_sort_Strategy(Object t) {
        return (t instanceof tom.library.sl.Strategy);
    }

    private static boolean tom_equal_term_Position(Object t1, Object t2) {
        return (t1.equals(t2));
    }

    private static boolean tom_is_sort_Position(Object t) {
        return (t instanceof tom.library.sl.Position);
    }

    private static boolean tom_is_fun_sym_mu(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_mu(tom.library.sl.Strategy var, tom.library.sl.Strategy v) {
        return (new tom.library.sl.Mu(var, v));
    }

    private static boolean tom_is_fun_sym_Mu(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.Mu));
    }

    private static tom.library.sl.Strategy tom_make_Mu(tom.library.sl.Strategy var, tom.library.sl.Strategy v) {
        return (new tom.library.sl.Mu(var, v));
    }

    private static tom.library.sl.Strategy tom_get_slot_Mu_s1(tom.library.sl.Strategy t) {
        return ((tom.library.sl.MuVar) t.getChildAt(tom.library.sl.Mu.VAR));
    }

    private static tom.library.sl.Strategy tom_get_slot_Mu_s2(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.Mu.V));
    }

    private static boolean tom_is_fun_sym_MuVar(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.MuVar));
    }

    private static tom.library.sl.Strategy tom_make_MuVar(String name) {
        return (new tom.library.sl.MuVar(name));
    }

    private static String tom_get_slot_MuVar_var(tom.library.sl.Strategy t) {
        return (((tom.library.sl.MuVar) t).getName());
    }

    private static boolean tom_is_fun_sym_Omega(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.Omega));
    }

    private static tom.library.sl.Strategy tom_make_Omega(int pos, tom.library.sl.Strategy v) {
        return (new tom.library.sl.Omega(pos, v));
    }

    private static int tom_get_slot_Omega_position(tom.library.sl.Strategy t) {
        return (((tom.library.sl.Omega) t).getPos());
    }

    private static tom.library.sl.Strategy tom_get_slot_Omega_s1(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.Omega.ARG));
    }

    private static boolean tom_is_fun_sym_Identity(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.Identity));
    }

    private static tom.library.sl.Strategy tom_make_Identity() {
        return (new tom.library.sl.Identity());
    }

    private static boolean tom_is_fun_sym_One(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.One));
    }

    private static tom.library.sl.Strategy tom_make_One(tom.library.sl.Strategy v) {
        return (new tom.library.sl.One(v));
    }

    private static tom.library.sl.Strategy tom_get_slot_One_s1(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.One.ARG));
    }

    private static boolean tom_is_fun_sym_All(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.All));
    }

    private static tom.library.sl.Strategy tom_make_All(tom.library.sl.Strategy v) {
        return (new tom.library.sl.All(v));
    }

    private static tom.library.sl.Strategy tom_get_slot_All_s1(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.All.ARG));
    }

    private static boolean tom_is_fun_sym_IfThenElse(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.IfThenElse));
    }

    private static tom.library.sl.Strategy tom_make_IfThenElse(tom.library.sl.Strategy condition, tom.library.sl.Strategy trueCase, tom.library.sl.Strategy falseCase) {
        return (new tom.library.sl.IfThenElse(condition, trueCase, falseCase));
    }

    private static tom.library.sl.Strategy tom_get_slot_IfThenElse_s1(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.IfThenElse.CONDITION));
    }

    private static tom.library.sl.Strategy tom_get_slot_IfThenElse_s2(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.IfThenElse.TRUE_CASE));
    }

    private static tom.library.sl.Strategy tom_get_slot_IfThenElse_s3(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.IfThenElse.FALSE_CASE));
    }

    private static boolean tom_is_fun_sym_Fail(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.Fail));
    }

    private static tom.library.sl.Strategy tom_make_Fail() {
        return (new tom.library.sl.Fail());
    }

    private static boolean tom_is_fun_sym_Not(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.Not));
    }

    private static tom.library.sl.Strategy tom_make_Not(tom.library.sl.Strategy v) {
        return (new tom.library.sl.Not(v));
    }

    private static tom.library.sl.Strategy tom_get_slot_Not_s1(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.Not.ARG));
    }

    private static boolean tom_is_fun_sym_Sequence(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.Sequence));
    }

    private static tom.library.sl.Strategy tom_empty_list_Sequence() {
        return (null);
    }

    private static tom.library.sl.Strategy tom_cons_list_Sequence(tom.library.sl.Strategy head, tom.library.sl.Strategy tail) {
        return ((tail == null) ? head : new tom.library.sl.Sequence(head, tail));
    }

    private static tom.library.sl.Strategy tom_get_head_Sequence_Strategy(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.Sequence.FIRST));
    }

    private static tom.library.sl.Strategy tom_get_tail_Sequence_Strategy(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.Sequence.THEN));
    }

    private static boolean tom_is_empty_Sequence_Strategy(tom.library.sl.Strategy t) {
        return (t == null);
    }

    private static tom.library.sl.Strategy tom_append_list_Sequence(tom.library.sl.Strategy l1, tom.library.sl.Strategy l2) {
        if ((l1 == null)) {
            return l2;
        } else if ((l2 == null)) {
            return l1;
        } else if (((l1 instanceof tom.library.sl.Sequence))) {
            if ((((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.Sequence.THEN)) == null)) {
                return ((l2 == null) ? ((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.Sequence.FIRST)) : new tom.library.sl.Sequence(((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.Sequence.FIRST)), l2));
            } else {
                return ((tom_append_list_Sequence(((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.Sequence.THEN)), l2) == null) ? ((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.Sequence.FIRST)) : new tom.library.sl.Sequence(((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.Sequence.FIRST)), tom_append_list_Sequence(((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.Sequence.THEN)), l2)));
            }
        } else {
            return ((l2 == null) ? l1 : new tom.library.sl.Sequence(l1, l2));
        }
    }

    private static tom.library.sl.Strategy tom_get_slice_Sequence(tom.library.sl.Strategy begin, tom.library.sl.Strategy end, tom.library.sl.Strategy tail) {
        if ((begin.equals(end))) {
            return tail;
        } else if ((end.equals(tail)) && ((end == null) || (end.equals(tom_empty_list_Sequence())))) {
            /* code to avoid a call to make, and thus to avoid looping during list-matching */
            return begin;
        }
        return (((tom.library.sl.Strategy) tom_get_slice_Sequence(((((begin instanceof tom.library.sl.Sequence))) ? ((tom.library.sl.Strategy) begin.getChildAt(tom.library.sl.Sequence.THEN)) : tom_empty_list_Sequence()), end, tail) == null) ? ((((begin instanceof tom.library.sl.Sequence))) ? ((tom.library.sl.Strategy) begin.getChildAt(tom.library.sl.Sequence.FIRST)) : begin) : new tom.library.sl.Sequence(((((begin instanceof tom.library.sl.Sequence))) ? ((tom.library.sl.Strategy) begin.getChildAt(tom.library.sl.Sequence.FIRST)) : begin), (tom.library.sl.Strategy) tom_get_slice_Sequence(((((begin instanceof tom.library.sl.Sequence))) ? ((tom.library.sl.Strategy) begin.getChildAt(tom.library.sl.Sequence.THEN)) : tom_empty_list_Sequence()), end, tail)));
    }

    private static boolean tom_is_fun_sym_Choice(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.Choice));
    }

    private static tom.library.sl.Strategy tom_empty_list_Choice() {
        return (null);
    }

    private static tom.library.sl.Strategy tom_cons_list_Choice(tom.library.sl.Strategy head, tom.library.sl.Strategy tail) {
        return ((tail == null) ? head : new tom.library.sl.Choice(head, tail));
    }

    private static tom.library.sl.Strategy tom_get_head_Choice_Strategy(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.Choice.FIRST));
    }

    private static tom.library.sl.Strategy tom_get_tail_Choice_Strategy(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.Choice.THEN));
    }

    private static boolean tom_is_empty_Choice_Strategy(tom.library.sl.Strategy t) {
        return (t == null);
    }

    private static tom.library.sl.Strategy tom_append_list_Choice(tom.library.sl.Strategy l1, tom.library.sl.Strategy l2) {
        if ((l1 == null)) {
            return l2;
        } else if ((l2 == null)) {
            return l1;
        } else if (((l1 instanceof tom.library.sl.Choice))) {
            if ((((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.Choice.THEN)) == null)) {
                return ((l2 == null) ? ((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.Choice.FIRST)) : new tom.library.sl.Choice(((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.Choice.FIRST)), l2));
            } else {
                return ((tom_append_list_Choice(((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.Choice.THEN)), l2) == null) ? ((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.Choice.FIRST)) : new tom.library.sl.Choice(((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.Choice.FIRST)), tom_append_list_Choice(((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.Choice.THEN)), l2)));
            }
        } else {
            return ((l2 == null) ? l1 : new tom.library.sl.Choice(l1, l2));
        }
    }

    private static tom.library.sl.Strategy tom_get_slice_Choice(tom.library.sl.Strategy begin, tom.library.sl.Strategy end, tom.library.sl.Strategy tail) {
        if ((begin.equals(end))) {
            return tail;
        } else if ((end.equals(tail)) && ((end == null) || (end.equals(tom_empty_list_Choice())))) {
            /* code to avoid a call to make, and thus to avoid looping during list-matching */
            return begin;
        }
        return (((tom.library.sl.Strategy) tom_get_slice_Choice(((((begin instanceof tom.library.sl.Choice))) ? ((tom.library.sl.Strategy) begin.getChildAt(tom.library.sl.Choice.THEN)) : tom_empty_list_Choice()), end, tail) == null) ? ((((begin instanceof tom.library.sl.Choice))) ? ((tom.library.sl.Strategy) begin.getChildAt(tom.library.sl.Choice.FIRST)) : begin) : new tom.library.sl.Choice(((((begin instanceof tom.library.sl.Choice))) ? ((tom.library.sl.Strategy) begin.getChildAt(tom.library.sl.Choice.FIRST)) : begin), (tom.library.sl.Strategy) tom_get_slice_Choice(((((begin instanceof tom.library.sl.Choice))) ? ((tom.library.sl.Strategy) begin.getChildAt(tom.library.sl.Choice.THEN)) : tom_empty_list_Choice()), end, tail)));
    }

    private static boolean tom_is_fun_sym_ChoiceUndet(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.ChoiceUndet));
    }

    private static tom.library.sl.Strategy tom_empty_list_ChoiceUndet() {
        return (null);
    }

    private static tom.library.sl.Strategy tom_cons_list_ChoiceUndet(tom.library.sl.Strategy head, tom.library.sl.Strategy tail) {
        return ((tail == null) ? head : new tom.library.sl.ChoiceUndet(head, tail));
    }

    private static tom.library.sl.Strategy tom_get_head_ChoiceUndet_Strategy(tom.library.sl.Strategy t) {
        return (((tom.library.sl.ChoiceUndet) t).getHead());
    }

    private static tom.library.sl.Strategy tom_get_tail_ChoiceUndet_Strategy(tom.library.sl.Strategy t) {
        return (((tom.library.sl.ChoiceUndet) t).getTail());
    }

    private static boolean tom_is_empty_ChoiceUndet_Strategy(tom.library.sl.Strategy t) {
        return (t == null);
    }

    private static tom.library.sl.Strategy tom_append_list_ChoiceUndet(tom.library.sl.Strategy l1, tom.library.sl.Strategy l2) {
        if ((l1 == null)) {
            return l2;
        } else if ((l2 == null)) {
            return l1;
        } else if (((l1 instanceof tom.library.sl.ChoiceUndet))) {
            if (((((tom.library.sl.ChoiceUndet) l1).getTail()) == null)) {
                return ((l2 == null) ? (((tom.library.sl.ChoiceUndet) l1).getHead()) : new tom.library.sl.ChoiceUndet((((tom.library.sl.ChoiceUndet) l1).getHead()), l2));
            } else {
                return ((tom_append_list_ChoiceUndet((((tom.library.sl.ChoiceUndet) l1).getTail()), l2) == null) ? (((tom.library.sl.ChoiceUndet) l1).getHead()) : new tom.library.sl.ChoiceUndet((((tom.library.sl.ChoiceUndet) l1).getHead()), tom_append_list_ChoiceUndet((((tom.library.sl.ChoiceUndet) l1).getTail()), l2)));
            }
        } else {
            return ((l2 == null) ? l1 : new tom.library.sl.ChoiceUndet(l1, l2));
        }
    }

    private static tom.library.sl.Strategy tom_get_slice_ChoiceUndet(tom.library.sl.Strategy begin, tom.library.sl.Strategy end, tom.library.sl.Strategy tail) {
        if ((begin.equals(end))) {
            return tail;
        } else if ((end.equals(tail)) && ((end == null) || (end.equals(tom_empty_list_ChoiceUndet())))) {
            /* code to avoid a call to make, and thus to avoid looping during list-matching */
            return begin;
        }
        return (((tom.library.sl.Strategy) tom_get_slice_ChoiceUndet(((((begin instanceof tom.library.sl.ChoiceUndet))) ? (((tom.library.sl.ChoiceUndet) begin).getTail()) : tom_empty_list_ChoiceUndet()), end, tail) == null) ? ((((begin instanceof tom.library.sl.ChoiceUndet))) ? (((tom.library.sl.ChoiceUndet) begin).getHead()) : begin) : new tom.library.sl.ChoiceUndet(((((begin instanceof tom.library.sl.ChoiceUndet))) ? (((tom.library.sl.ChoiceUndet) begin).getHead()) : begin), (tom.library.sl.Strategy) tom_get_slice_ChoiceUndet(((((begin instanceof tom.library.sl.ChoiceUndet))) ? (((tom.library.sl.ChoiceUndet) begin).getTail()) : tom_empty_list_ChoiceUndet()), end, tail)));
    }

    private static boolean tom_is_fun_sym_SequenceId(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.SequenceId));
    }

    private static tom.library.sl.Strategy tom_empty_list_SequenceId() {
        return (null);
    }

    private static tom.library.sl.Strategy tom_cons_list_SequenceId(tom.library.sl.Strategy head, tom.library.sl.Strategy tail) {
        return ((tail == null) ? head : new tom.library.sl.SequenceId(head, tail));
    }

    private static tom.library.sl.Strategy tom_get_head_SequenceId_Strategy(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.SequenceId.FIRST));
    }

    private static tom.library.sl.Strategy tom_get_tail_SequenceId_Strategy(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.SequenceId.THEN));
    }

    private static boolean tom_is_empty_SequenceId_Strategy(tom.library.sl.Strategy t) {
        return (t == null);
    }

    private static tom.library.sl.Strategy tom_append_list_SequenceId(tom.library.sl.Strategy l1, tom.library.sl.Strategy l2) {
        if ((l1 == null)) {
            return l2;
        } else if ((l2 == null)) {
            return l1;
        } else if (((l1 instanceof tom.library.sl.SequenceId))) {
            if ((((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.SequenceId.THEN)) == null)) {
                return ((l2 == null) ? ((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.SequenceId.FIRST)) : new tom.library.sl.SequenceId(((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.SequenceId.FIRST)), l2));
            } else {
                return ((tom_append_list_SequenceId(((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.SequenceId.THEN)), l2) == null) ? ((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.SequenceId.FIRST)) : new tom.library.sl.SequenceId(((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.SequenceId.FIRST)), tom_append_list_SequenceId(((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.SequenceId.THEN)), l2)));
            }
        } else {
            return ((l2 == null) ? l1 : new tom.library.sl.SequenceId(l1, l2));
        }
    }

    private static tom.library.sl.Strategy tom_get_slice_SequenceId(tom.library.sl.Strategy begin, tom.library.sl.Strategy end, tom.library.sl.Strategy tail) {
        if ((begin.equals(end))) {
            return tail;
        } else if ((end.equals(tail)) && ((end == null) || (end.equals(tom_empty_list_SequenceId())))) {
            /* code to avoid a call to make, and thus to avoid looping during list-matching */
            return begin;
        }
        return (((tom.library.sl.Strategy) tom_get_slice_SequenceId(((((begin instanceof tom.library.sl.SequenceId))) ? ((tom.library.sl.Strategy) begin.getChildAt(tom.library.sl.SequenceId.THEN)) : tom_empty_list_SequenceId()), end, tail) == null) ? ((((begin instanceof tom.library.sl.SequenceId))) ? ((tom.library.sl.Strategy) begin.getChildAt(tom.library.sl.SequenceId.FIRST)) : begin) : new tom.library.sl.SequenceId(((((begin instanceof tom.library.sl.SequenceId))) ? ((tom.library.sl.Strategy) begin.getChildAt(tom.library.sl.SequenceId.FIRST)) : begin), (tom.library.sl.Strategy) tom_get_slice_SequenceId(((((begin instanceof tom.library.sl.SequenceId))) ? ((tom.library.sl.Strategy) begin.getChildAt(tom.library.sl.SequenceId.THEN)) : tom_empty_list_SequenceId()), end, tail)));
    }

    private static boolean tom_is_fun_sym_ChoiceId(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.ChoiceId));
    }

    private static tom.library.sl.Strategy tom_empty_list_ChoiceId() {
        return (null);
    }

    private static tom.library.sl.Strategy tom_cons_list_ChoiceId(tom.library.sl.Strategy head, tom.library.sl.Strategy tail) {
        return ((tail == null) ? head : new tom.library.sl.ChoiceId(head, tail));
    }

    private static tom.library.sl.Strategy tom_get_head_ChoiceId_Strategy(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.ChoiceId.FIRST));
    }

    private static tom.library.sl.Strategy tom_get_tail_ChoiceId_Strategy(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.ChoiceId.THEN));
    }

    private static boolean tom_is_empty_ChoiceId_Strategy(tom.library.sl.Strategy t) {
        return (t == null);
    }

    private static tom.library.sl.Strategy tom_append_list_ChoiceId(tom.library.sl.Strategy l1, tom.library.sl.Strategy l2) {
        if ((l1 == null)) {
            return l2;
        } else if ((l2 == null)) {
            return l1;
        } else if (((l1 instanceof tom.library.sl.ChoiceId))) {
            if ((((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.ChoiceId.THEN)) == null)) {
                return ((l2 == null) ? ((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.ChoiceId.FIRST)) : new tom.library.sl.ChoiceId(((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.ChoiceId.FIRST)), l2));
            } else {
                return ((tom_append_list_ChoiceId(((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.ChoiceId.THEN)), l2) == null) ? ((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.ChoiceId.FIRST)) : new tom.library.sl.ChoiceId(((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.ChoiceId.FIRST)), tom_append_list_ChoiceId(((tom.library.sl.Strategy) l1.getChildAt(tom.library.sl.ChoiceId.THEN)), l2)));
            }
        } else {
            return ((l2 == null) ? l1 : new tom.library.sl.ChoiceId(l1, l2));
        }
    }

    private static tom.library.sl.Strategy tom_get_slice_ChoiceId(tom.library.sl.Strategy begin, tom.library.sl.Strategy end, tom.library.sl.Strategy tail) {
        if ((begin.equals(end))) {
            return tail;
        } else if ((end.equals(tail)) && ((end == null) || (end.equals(tom_empty_list_ChoiceId())))) {
            /* code to avoid a call to make, and thus to avoid looping during list-matching */
            return begin;
        }
        return (((tom.library.sl.Strategy) tom_get_slice_ChoiceId(((((begin instanceof tom.library.sl.ChoiceId))) ? ((tom.library.sl.Strategy) begin.getChildAt(tom.library.sl.ChoiceId.THEN)) : tom_empty_list_ChoiceId()), end, tail) == null) ? ((((begin instanceof tom.library.sl.ChoiceId))) ? ((tom.library.sl.Strategy) begin.getChildAt(tom.library.sl.ChoiceId.FIRST)) : begin) : new tom.library.sl.ChoiceId(((((begin instanceof tom.library.sl.ChoiceId))) ? ((tom.library.sl.Strategy) begin.getChildAt(tom.library.sl.ChoiceId.FIRST)) : begin), (tom.library.sl.Strategy) tom_get_slice_ChoiceId(((((begin instanceof tom.library.sl.ChoiceId))) ? ((tom.library.sl.Strategy) begin.getChildAt(tom.library.sl.ChoiceId.THEN)) : tom_empty_list_ChoiceId()), end, tail)));
    }

    private static boolean tom_is_fun_sym_OneId(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.OneId));
    }

    private static tom.library.sl.Strategy tom_make_OneId(tom.library.sl.Strategy v) {
        return (new tom.library.sl.OneId(v));
    }

    private static tom.library.sl.Strategy tom_get_slot_OneId_s1(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.OneId.ARG));
    }

    private static boolean tom_is_fun_sym_Pselect(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.Pselect));
    }

    private static tom.library.sl.Strategy tom_make_Pselect(int p, int q, tom.library.sl.Strategy first, tom.library.sl.Strategy then) {
        return (new tom.library.sl.Pselect(p, q, first, then));
    }

    private static int tom_get_slot_Pselect_p1(tom.library.sl.Strategy t) {
        return (((tom.library.sl.Pselect) t).getP());
    }

    private static int tom_get_slot_Pselect_p2(tom.library.sl.Strategy t) {
        return (((tom.library.sl.Pselect) t).getQ());
    }

    private static tom.library.sl.Strategy tom_get_slot_Pselect_s1(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.Pselect.FIRST));
    }

    private static tom.library.sl.Strategy tom_get_slot_Pselect_s2(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.Pselect.THEN));
    }

    private static boolean tom_is_fun_sym_OmegaU(tom.library.sl.Strategy t) {
        return (t instanceof tom.library.sl.OmegaU);
    }

    private static tom.library.sl.Strategy tom_make_OmegaU(tom.library.sl.Strategy v) {
        return (new tom.library.sl.OmegaU(v));
    }

    private static tom.library.sl.Strategy tom_get_slot_OmegaU_s1(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.OmegaU.ARG));
    }

    private static boolean tom_is_fun_sym_Up(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.Up));
    }

    private static tom.library.sl.Strategy tom_make_Up(tom.library.sl.Strategy v) {
        return (new tom.library.sl.Up(v));
    }

    private static tom.library.sl.Strategy tom_get_slot_Up_s1(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.Up.ARG));
    }

    private static boolean tom_is_fun_sym_AllSeq(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.AllSeq));
    }

    private static tom.library.sl.Strategy tom_make_AllSeq(tom.library.sl.Strategy s) {
        return (new tom.library.sl.AllSeq(s));
    }

    private static tom.library.sl.Strategy tom_get_slot_AllSeq_s(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.DeRef.ARG));
    }

    private static boolean tom_is_fun_sym_DeRef(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.DeRef) && ((tom.library.sl.DeRef) t).isRelative() == true && ((tom.library.sl.DeRef) t).isStrict() == false);
    }

    private static tom.library.sl.Strategy tom_make_DeRef(tom.library.sl.Strategy s) {
        return (tom.library.sl.DeRef.makeRelative(s));
    }

    private static tom.library.sl.Strategy tom_get_slot_DeRef_s(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.DeRef.ARG));
    }

    private static boolean tom_is_fun_sym_StrictDeRef(tom.library.sl.Strategy t) {
        return ((t instanceof tom.library.sl.DeRef) && ((tom.library.sl.DeRef) t).isRelative() == true && ((tom.library.sl.DeRef) t).isStrict() == true);
    }

    private static tom.library.sl.Strategy tom_make_StrictDeRef(tom.library.sl.Strategy s) {
        return (tom.library.sl.DeRef.makeRelativeStrict(s));
    }

    private static tom.library.sl.Strategy tom_get_slot_StrictDeRef_s(tom.library.sl.Strategy t) {
        return ((tom.library.sl.Strategy) t.getChildAt(tom.library.sl.DeRef.ARG));
    }

    private static boolean tom_is_fun_sym_AU(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_AU(tom.library.sl.Strategy s1, tom.library.sl.Strategy s2) {
        return (
                tom_make_mu(tom_make_MuVar("x"), tom_cons_list_Choice(s2, tom_cons_list_Choice(tom_cons_list_Sequence(tom_cons_list_Sequence(s1, tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("x")), tom_empty_list_Sequence())), tom_cons_list_Sequence(tom_make_One(tom_make_Identity()), tom_empty_list_Sequence())), tom_empty_list_Choice()))))

                ;
    }

    private static boolean tom_is_fun_sym_EU(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_EU(tom.library.sl.Strategy s1, tom.library.sl.Strategy s2) {
        return (
                tom_make_mu(tom_make_MuVar("x"), tom_cons_list_Choice(s2, tom_cons_list_Choice(tom_cons_list_Sequence(s1, tom_cons_list_Sequence(tom_make_One(tom_make_MuVar("x")), tom_empty_list_Sequence())), tom_empty_list_Choice()))))

                ;
    }

    private static boolean tom_is_fun_sym_TrueCtl(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_TrueCtl() {
        return tom_make_Identity()

                ;
    }

    private static boolean tom_is_fun_sym_FalseCtl(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_FalseCtl() {
        return tom_make_Fail()

                ;
    }

    private static boolean tom_is_fun_sym_AndCtl(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_AndCtl(tom.library.sl.Strategy s1, tom.library.sl.Strategy s2) {
        return tom_cons_list_Sequence(s1, tom_cons_list_Sequence(s2, tom_empty_list_Sequence()))

                ;
    }

    private static boolean tom_is_fun_sym_OrCtl(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_OrCtl(tom.library.sl.Strategy s1, tom.library.sl.Strategy s2) {
        return tom_cons_list_Choice(s1, tom_cons_list_Choice(s2, tom_empty_list_Choice()))

                ;
    }

    private static boolean tom_is_fun_sym_AF(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_AF(tom.library.sl.Strategy s) {
        return tom_make_AU(tom_make_Identity(), s)

                ;
    }

    private static boolean tom_is_fun_sym_AG(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_AG(tom.library.sl.Strategy s) {
        return tom_make_TopDown(s)

                ;
    }

    private static boolean tom_is_fun_sym_EF(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_EF(tom.library.sl.Strategy s) {
        return tom_make_EU(tom_make_Identity(), s)

                ;
    }

    private static boolean tom_is_fun_sym_AX(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_AX(tom.library.sl.Strategy s) {
        return (
                tom_make_All(s))

                ;
    }

    private static boolean tom_is_fun_sym_EX(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_EX(tom.library.sl.Strategy s) {
        return (
                tom_make_One(s))

                ;
    }

    private static boolean tom_is_fun_sym_Try(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_Try(tom.library.sl.Strategy v) {
        return (
                tom_cons_list_Choice(v, tom_cons_list_Choice(tom_make_Identity(), tom_empty_list_Choice())))

                ;
    }

    private static boolean tom_is_fun_sym_TopDown(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_TopDown(tom.library.sl.Strategy v) {
        return (
                tom_make_mu(tom_make_MuVar("_x"), tom_cons_list_Sequence(v, tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")), tom_empty_list_Sequence()))))

                ;
    }

    private static boolean tom_is_fun_sym_TopDownCollect(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_TopDownCollect(tom.library.sl.Strategy v) {
        return (
                tom_make_mu(tom_make_MuVar("_x"), tom_make_Try(tom_cons_list_Sequence(v, tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")), tom_empty_list_Sequence())))))

                ;
    }

    private static boolean tom_is_fun_sym_TopDownStopOnSuccess(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_TopDownStopOnSuccess(tom.library.sl.Strategy v) {
        return (
                tom_make_mu(tom_make_MuVar("x"), tom_cons_list_Choice(v, tom_cons_list_Choice(tom_make_All(tom_make_MuVar("x")), tom_empty_list_Choice()))))

                ;
    }

    private static boolean tom_is_fun_sym_TopDownIdStopOnSuccess(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_TopDownIdStopOnSuccess(tom.library.sl.Strategy v) {
        return (
                tom_make_mu(tom_make_MuVar("x"), tom_cons_list_ChoiceId(v, tom_cons_list_ChoiceId(tom_make_All(tom_make_MuVar("x")), tom_empty_list_ChoiceId()))))

                ;
    }

    private static boolean tom_is_fun_sym_BottomUp(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_BottomUp(tom.library.sl.Strategy v) {
        return (
                tom_make_mu(tom_make_MuVar("_x"), tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")), tom_cons_list_Sequence(v, tom_empty_list_Sequence()))))

                ;
    }

    private static boolean tom_is_fun_sym_OnceBottomUp(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_OnceBottomUp(tom.library.sl.Strategy v) {
        return (
                tom_make_mu(tom_make_MuVar("_x"), tom_cons_list_Choice(tom_make_One(tom_make_MuVar("_x")), tom_cons_list_Choice(v, tom_empty_list_Choice()))))

                ;
    }

    private static boolean tom_is_fun_sym_OnceTopDown(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_OnceTopDown(tom.library.sl.Strategy v) {
        return (
                tom_make_mu(tom_make_MuVar("_x"), tom_cons_list_Choice(v, tom_cons_list_Choice(tom_make_One(tom_make_MuVar("_x")), tom_empty_list_Choice()))))

                ;
    }

    private static boolean tom_is_fun_sym_Innermost(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_Innermost(tom.library.sl.Strategy v) {
        return (
                tom_make_mu(tom_make_MuVar("_x"), tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")), tom_cons_list_Sequence(tom_make_Try(tom_cons_list_Sequence(v, tom_cons_list_Sequence(tom_make_MuVar("_x"), tom_empty_list_Sequence()))), tom_empty_list_Sequence()))))

                ;
    }

    private static boolean tom_is_fun_sym_Outermost(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_Outermost(tom.library.sl.Strategy v) {
        return (
                tom_make_Repeat(tom_make_OnceTopDown(v)))

                ;
    }

    private static boolean tom_is_fun_sym_Repeat(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_Repeat(tom.library.sl.Strategy v) {
        return (
                tom_make_mu(tom_make_MuVar("_x"), tom_cons_list_Choice(tom_cons_list_Sequence(v, tom_cons_list_Sequence(tom_make_MuVar("_x"), tom_empty_list_Sequence())), tom_cons_list_Choice(tom_make_Identity(), tom_empty_list_Choice()))))

                ;
    }

    private static boolean tom_is_fun_sym_RepeatId(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_RepeatId(tom.library.sl.Strategy v) {
        return (
                tom_make_mu(tom_make_MuVar("_x"), tom_cons_list_SequenceId(v, tom_cons_list_SequenceId(tom_make_MuVar("_x"), tom_empty_list_SequenceId()))))

                ;
    }

    private static boolean tom_is_fun_sym_OnceBottomUpId(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_OnceBottomUpId(tom.library.sl.Strategy v) {
        return (
                tom_make_mu(tom_make_MuVar("_x"), tom_cons_list_ChoiceId(tom_make_OneId(tom_make_MuVar("_x")), tom_cons_list_ChoiceId(v, tom_empty_list_ChoiceId()))))

                ;
    }

    private static boolean tom_is_fun_sym_OnceTopDownId(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_OnceTopDownId(tom.library.sl.Strategy v) {
        return (
                tom_make_mu(tom_make_MuVar("_x"), tom_cons_list_ChoiceId(v, tom_cons_list_ChoiceId(tom_make_OneId(tom_make_MuVar("_x")), tom_empty_list_ChoiceId()))))

                ;
    }

    private static boolean tom_is_fun_sym_InnermostId(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_InnermostId(tom.library.sl.Strategy v) {
        return (
                tom_make_mu(tom_make_MuVar("_x"), tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")), tom_cons_list_Sequence(tom_cons_list_SequenceId(v, tom_cons_list_SequenceId(tom_make_MuVar("_x"), tom_empty_list_SequenceId())), tom_empty_list_Sequence()))))

                ;
    }

    private static boolean tom_is_fun_sym_InnermostIdSeq(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_InnermostIdSeq(tom.library.sl.Strategy v) {
        return (
                tom_make_mu(tom_make_MuVar("_x"), tom_cons_list_Sequence(tom_make_AllSeq(tom_make_MuVar("_x")), tom_cons_list_Sequence(tom_cons_list_SequenceId(v, tom_cons_list_SequenceId(tom_make_MuVar("_x"), tom_empty_list_SequenceId())), tom_empty_list_Sequence()))))

                ;
    }

    private static boolean tom_is_fun_sym_OutermostId(tom.library.sl.Strategy t) {
        return false;
    }

    private static tom.library.sl.Strategy tom_make_OutermostId(tom.library.sl.Strategy v) {
        return (
                tom_make_RepeatId(tom_make_OnceTopDownId(v)))

                ;
    }


    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(MainGom.class));
    }

    public void testMatch() {
        T1 subject =
                tom_make_f(tom_make_f(tom_make_a(), tom_make_b()), tom_make_g(tom_make_b()));

        {
            {
                if (tom_is_sort_T1(subject)) {
                    if (tom_is_fun_sym_f(((base.data.types.T1) subject))) {
                        base.data.types.T2 tomMatch7NameNumber_freshVar_2 = tom_get_slot_f_s2(((base.data.types.T1) subject));
                        if (tom_is_fun_sym_g(tomMatch7NameNumber_freshVar_2)) {

                            assertEquals(
                                    tom_get_slot_f_s1(((base.data.types.T1) subject)),
                                    tom_make_f(tom_make_a(), tom_make_b()));
                            assertEquals(
                                    tom_get_slot_g_s2(tomMatch7NameNumber_freshVar_2),
                                    tom_make_b());
                            return;


                        }
                    }
                }

            }

        }

        fail();
    }


    public void testVisit() {
        T1 subject =
                tom_make_f(tom_make_f(tom_make_a(), tom_make_b()), tom_make_g(tom_make_b()));
        try {
            T1 res = (T1)
                    tom_make_Repeat(tom_make_OnceBottomUp(tom_make_Rule())).visitLight(subject, new LocalIntrospector());
            assertEquals(res,
                    tom_make_a());
        } catch (VisitFailure e) {
            fail();
        }

    }


    public static class LocalIntrospector implements tom.library.sl.Introspector {
        @SuppressWarnings("unchecked")
        public int getChildCount(Object o) {
            if (o == null) {
                return 0;
            }
            if (tom_is_sort_Strategy(o)) {
                if (tom_is_fun_sym_OnceBottomUp(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_OmegaU(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_Identity(((tom.library.sl.Strategy) o))) {
                    return 0;
                }
                if (tom_is_fun_sym_OneId(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_Not(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_ChoiceUndet(((tom.library.sl.Strategy) o))) {
                    if (tom_is_empty_ChoiceUndet_Strategy(((tom.library.sl.Strategy) o))) {
                        return 0;
                    } else {
                        return 2;
                    }
                }
                if (tom_is_fun_sym_RepeatId(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_OnceTopDown(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_Rule(((tom.library.sl.Strategy) o))) {
                    return 0;
                }
                if (tom_is_fun_sym_mu(((tom.library.sl.Strategy) o))) {
                    return 2;
                }
                if (tom_is_fun_sym_OnceTopDownId(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_One(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_Choice(((tom.library.sl.Strategy) o))) {
                    if (tom_is_empty_Choice_Strategy(((tom.library.sl.Strategy) o))) {
                        return 0;
                    } else {
                        return 2;
                    }
                }
                if (tom_is_fun_sym_InnermostId(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_TrueCtl(((tom.library.sl.Strategy) o))) {
                    return 0;
                }
                if (tom_is_fun_sym_TopDownStopOnSuccess(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_StrictDeRef(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_ChoiceId(((tom.library.sl.Strategy) o))) {
                    if (tom_is_empty_ChoiceId_Strategy(((tom.library.sl.Strategy) o))) {
                        return 0;
                    } else {
                        return 2;
                    }
                }
                if (tom_is_fun_sym_Mu(((tom.library.sl.Strategy) o))) {
                    return 2;
                }
                if (tom_is_fun_sym_OutermostId(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_Innermost(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_OrCtl(((tom.library.sl.Strategy) o))) {
                    return 2;
                }
                if (tom_is_fun_sym_TopDown(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_TopDownCollect(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_EF(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_Pselect(((tom.library.sl.Strategy) o))) {
                    return 4;
                }
                if (tom_is_fun_sym_AF(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_AG(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_IfThenElse(((tom.library.sl.Strategy) o))) {
                    return 3;
                }
                if (tom_is_fun_sym_TopDownIdStopOnSuccess(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_Omega(((tom.library.sl.Strategy) o))) {
                    return 2;
                }
                if (tom_is_fun_sym_EU(((tom.library.sl.Strategy) o))) {
                    return 2;
                }
                if (tom_is_fun_sym_EX(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_Fail(((tom.library.sl.Strategy) o))) {
                    return 0;
                }
                if (tom_is_fun_sym_AU(((tom.library.sl.Strategy) o))) {
                    return 2;
                }
                if (tom_is_fun_sym_AX(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_BottomUp(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_Repeat(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_FalseCtl(((tom.library.sl.Strategy) o))) {
                    return 0;
                }
                if (tom_is_fun_sym_Sequence(((tom.library.sl.Strategy) o))) {
                    if (tom_is_empty_Sequence_Strategy(((tom.library.sl.Strategy) o))) {
                        return 0;
                    } else {
                        return 2;
                    }
                }
                if (tom_is_fun_sym_MuVar(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_Outermost(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_DeRef(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_SequenceId(((tom.library.sl.Strategy) o))) {
                    if (tom_is_empty_SequenceId_Strategy(((tom.library.sl.Strategy) o))) {
                        return 0;
                    } else {
                        return 2;
                    }
                }
                if (tom_is_fun_sym_AllSeq(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_OnceBottomUpId(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_All(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_AndCtl(((tom.library.sl.Strategy) o))) {
                    return 2;
                }
                if (tom_is_fun_sym_Up(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_Try(((tom.library.sl.Strategy) o))) {
                    return 1;
                }
                if (tom_is_fun_sym_InnermostIdSeq(((tom.library.sl.Strategy) o))) {
                    return 1;
                }

            }
            if (tom_is_sort_Position(o)) {
            }
            if (tom_is_sort_T1(o)) {
                if (tom_is_fun_sym_a(((base.data.types.T1) o))) {
                    return 0;
                }
                if (tom_is_fun_sym_f(((base.data.types.T1) o))) {
                    return 2;
                }

            }
            if (tom_is_sort_T2(o)) {
                if (tom_is_fun_sym_b(((base.data.types.T2) o))) {
                    return 0;
                }
                if (tom_is_fun_sym_g(((base.data.types.T2) o))) {
                    return 1;
                }

            }
            return 0;

        }

        @SuppressWarnings("unchecked")
        public Object[] getChildren(Object o) {
            if (tom_is_sort_Strategy(o)) {
                if (tom_is_fun_sym_OnceBottomUp(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_OmegaU(((tom.library.sl.Strategy) o))) {
                    return new Object[]{tom_get_slot_OmegaU_s1(((tom.library.sl.Strategy) o))};
                }
                if (tom_is_fun_sym_Identity(((tom.library.sl.Strategy) o))) {
                    return new Object[]{};
                }
                if (tom_is_fun_sym_OneId(((tom.library.sl.Strategy) o))) {
                    return new Object[]{tom_get_slot_OneId_s1(((tom.library.sl.Strategy) o))};
                }
                if (tom_is_fun_sym_Not(((tom.library.sl.Strategy) o))) {
                    return new Object[]{tom_get_slot_Not_s1(((tom.library.sl.Strategy) o))};
                }
                if (tom_is_fun_sym_ChoiceUndet(((tom.library.sl.Strategy) o))) {
                    if (tom_is_empty_ChoiceUndet_Strategy(((tom.library.sl.Strategy) o))) {
                        return new Object[]{};
                    } else {
                        return new Object[]{tom_get_head_ChoiceUndet_Strategy(((tom.library.sl.Strategy) o)), tom_get_tail_ChoiceUndet_Strategy(((tom.library.sl.Strategy) o))};
                    }
                }
                if (tom_is_fun_sym_RepeatId(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_OnceTopDown(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_Rule(((tom.library.sl.Strategy) o))) {
                    return new Object[]{};
                }
                if (tom_is_fun_sym_mu(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null, null};
                }
                if (tom_is_fun_sym_OnceTopDownId(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_One(((tom.library.sl.Strategy) o))) {
                    return new Object[]{tom_get_slot_One_s1(((tom.library.sl.Strategy) o))};
                }
                if (tom_is_fun_sym_Choice(((tom.library.sl.Strategy) o))) {
                    if (tom_is_empty_Choice_Strategy(((tom.library.sl.Strategy) o))) {
                        return new Object[]{};
                    } else {
                        return new Object[]{tom_get_head_Choice_Strategy(((tom.library.sl.Strategy) o)), tom_get_tail_Choice_Strategy(((tom.library.sl.Strategy) o))};
                    }
                }
                if (tom_is_fun_sym_InnermostId(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_TrueCtl(((tom.library.sl.Strategy) o))) {
                    return new Object[]{};
                }
                if (tom_is_fun_sym_TopDownStopOnSuccess(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_StrictDeRef(((tom.library.sl.Strategy) o))) {
                    return new Object[]{tom_get_slot_StrictDeRef_s(((tom.library.sl.Strategy) o))};
                }
                if (tom_is_fun_sym_ChoiceId(((tom.library.sl.Strategy) o))) {
                    if (tom_is_empty_ChoiceId_Strategy(((tom.library.sl.Strategy) o))) {
                        return new Object[]{};
                    } else {
                        return new Object[]{tom_get_head_ChoiceId_Strategy(((tom.library.sl.Strategy) o)), tom_get_tail_ChoiceId_Strategy(((tom.library.sl.Strategy) o))};
                    }
                }
                if (tom_is_fun_sym_Mu(((tom.library.sl.Strategy) o))) {
                    return new Object[]{tom_get_slot_Mu_s1(((tom.library.sl.Strategy) o)), tom_get_slot_Mu_s2(((tom.library.sl.Strategy) o))};
                }
                if (tom_is_fun_sym_OutermostId(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_Innermost(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_OrCtl(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null, null};
                }
                if (tom_is_fun_sym_TopDown(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_TopDownCollect(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_EF(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_Pselect(((tom.library.sl.Strategy) o))) {
                    return new Object[]{tom_get_slot_Pselect_p1(((tom.library.sl.Strategy) o)), tom_get_slot_Pselect_p2(((tom.library.sl.Strategy) o)), tom_get_slot_Pselect_s1(((tom.library.sl.Strategy) o)), tom_get_slot_Pselect_s2(((tom.library.sl.Strategy) o))};
                }
                if (tom_is_fun_sym_AF(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_AG(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_IfThenElse(((tom.library.sl.Strategy) o))) {
                    return new Object[]{tom_get_slot_IfThenElse_s1(((tom.library.sl.Strategy) o)), tom_get_slot_IfThenElse_s2(((tom.library.sl.Strategy) o)), tom_get_slot_IfThenElse_s3(((tom.library.sl.Strategy) o))};
                }
                if (tom_is_fun_sym_TopDownIdStopOnSuccess(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_Omega(((tom.library.sl.Strategy) o))) {
                    return new Object[]{tom_get_slot_Omega_position(((tom.library.sl.Strategy) o)), tom_get_slot_Omega_s1(((tom.library.sl.Strategy) o))};
                }
                if (tom_is_fun_sym_EU(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null, null};
                }
                if (tom_is_fun_sym_EX(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_Fail(((tom.library.sl.Strategy) o))) {
                    return new Object[]{};
                }
                if (tom_is_fun_sym_AU(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null, null};
                }
                if (tom_is_fun_sym_AX(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_BottomUp(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_Repeat(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_FalseCtl(((tom.library.sl.Strategy) o))) {
                    return new Object[]{};
                }
                if (tom_is_fun_sym_Sequence(((tom.library.sl.Strategy) o))) {
                    if (tom_is_empty_Sequence_Strategy(((tom.library.sl.Strategy) o))) {
                        return new Object[]{};
                    } else {
                        return new Object[]{tom_get_head_Sequence_Strategy(((tom.library.sl.Strategy) o)), tom_get_tail_Sequence_Strategy(((tom.library.sl.Strategy) o))};
                    }
                }
                if (tom_is_fun_sym_MuVar(((tom.library.sl.Strategy) o))) {
                    return new Object[]{tom_get_slot_MuVar_var(((tom.library.sl.Strategy) o))};
                }
                if (tom_is_fun_sym_Outermost(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_DeRef(((tom.library.sl.Strategy) o))) {
                    return new Object[]{tom_get_slot_DeRef_s(((tom.library.sl.Strategy) o))};
                }
                if (tom_is_fun_sym_SequenceId(((tom.library.sl.Strategy) o))) {
                    if (tom_is_empty_SequenceId_Strategy(((tom.library.sl.Strategy) o))) {
                        return new Object[]{};
                    } else {
                        return new Object[]{tom_get_head_SequenceId_Strategy(((tom.library.sl.Strategy) o)), tom_get_tail_SequenceId_Strategy(((tom.library.sl.Strategy) o))};
                    }
                }
                if (tom_is_fun_sym_AllSeq(((tom.library.sl.Strategy) o))) {
                    return new Object[]{tom_get_slot_AllSeq_s(((tom.library.sl.Strategy) o))};
                }
                if (tom_is_fun_sym_OnceBottomUpId(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_All(((tom.library.sl.Strategy) o))) {
                    return new Object[]{tom_get_slot_All_s1(((tom.library.sl.Strategy) o))};
                }
                if (tom_is_fun_sym_AndCtl(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null, null};
                }
                if (tom_is_fun_sym_Up(((tom.library.sl.Strategy) o))) {
                    return new Object[]{tom_get_slot_Up_s1(((tom.library.sl.Strategy) o))};
                }
                if (tom_is_fun_sym_Try(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }
                if (tom_is_fun_sym_InnermostIdSeq(((tom.library.sl.Strategy) o))) {
                    return new Object[]{null};
                }

            }
            if (tom_is_sort_Position(o)) {
            }
            if (tom_is_sort_T1(o)) {
                if (tom_is_fun_sym_a(((base.data.types.T1) o))) {
                    return new Object[]{};
                }
                if (tom_is_fun_sym_f(((base.data.types.T1) o))) {
                    return new Object[]{tom_get_slot_f_s1(((base.data.types.T1) o)), tom_get_slot_f_s2(((base.data.types.T1) o))};
                }

            }
            if (tom_is_sort_T2(o)) {
                if (tom_is_fun_sym_b(((base.data.types.T2) o))) {
                    return new Object[]{};
                }
                if (tom_is_fun_sym_g(((base.data.types.T2) o))) {
                    return new Object[]{tom_get_slot_g_s2(((base.data.types.T2) o))};
                }

            }
            return null;

        }

        @SuppressWarnings("unchecked")
        public Object setChildren(Object o, Object[] children) {
            if (tom_is_sort_Strategy(o)) {
                if (tom_is_fun_sym_OnceBottomUp(((tom.library.sl.Strategy) o))) {
                    return tom_make_OnceBottomUp(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_OmegaU(((tom.library.sl.Strategy) o))) {
                    return tom_make_OmegaU(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_Identity(((tom.library.sl.Strategy) o))) {
                    return tom_make_Identity();
                }
                if (tom_is_fun_sym_OneId(((tom.library.sl.Strategy) o))) {
                    return tom_make_OneId(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_Not(((tom.library.sl.Strategy) o))) {
                    return tom_make_Not(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_ChoiceUndet(((tom.library.sl.Strategy) o))) {
                    if (children.length == 0) {
                        return tom_empty_list_ChoiceUndet();
                    } else {
                        return tom_cons_list_ChoiceUndet(((tom.library.sl.Strategy) children[0]), ((tom.library.sl.Strategy) children[1]));
                    }
                }
                if (tom_is_fun_sym_RepeatId(((tom.library.sl.Strategy) o))) {
                    return tom_make_RepeatId(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_OnceTopDown(((tom.library.sl.Strategy) o))) {
                    return tom_make_OnceTopDown(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_Rule(((tom.library.sl.Strategy) o))) {
                    return tom_make_Rule();
                }
                if (tom_is_fun_sym_mu(((tom.library.sl.Strategy) o))) {
                    return tom_make_mu(((tom.library.sl.Strategy) children[0]), ((tom.library.sl.Strategy) children[1]));
                }
                if (tom_is_fun_sym_OnceTopDownId(((tom.library.sl.Strategy) o))) {
                    return tom_make_OnceTopDownId(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_One(((tom.library.sl.Strategy) o))) {
                    return tom_make_One(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_Choice(((tom.library.sl.Strategy) o))) {
                    if (children.length == 0) {
                        return tom_empty_list_Choice();
                    } else {
                        return tom_cons_list_Choice(((tom.library.sl.Strategy) children[0]), ((tom.library.sl.Strategy) children[1]));
                    }
                }
                if (tom_is_fun_sym_InnermostId(((tom.library.sl.Strategy) o))) {
                    return tom_make_InnermostId(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_TrueCtl(((tom.library.sl.Strategy) o))) {
                    return tom_make_TrueCtl();
                }
                if (tom_is_fun_sym_TopDownStopOnSuccess(((tom.library.sl.Strategy) o))) {
                    return tom_make_TopDownStopOnSuccess(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_StrictDeRef(((tom.library.sl.Strategy) o))) {
                    return tom_make_StrictDeRef(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_ChoiceId(((tom.library.sl.Strategy) o))) {
                    if (children.length == 0) {
                        return tom_empty_list_ChoiceId();
                    } else {
                        return tom_cons_list_ChoiceId(((tom.library.sl.Strategy) children[0]), ((tom.library.sl.Strategy) children[1]));
                    }
                }
                if (tom_is_fun_sym_Mu(((tom.library.sl.Strategy) o))) {
                    return tom_make_Mu(((tom.library.sl.Strategy) children[0]), ((tom.library.sl.Strategy) children[1]));
                }
                if (tom_is_fun_sym_OutermostId(((tom.library.sl.Strategy) o))) {
                    return tom_make_OutermostId(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_Innermost(((tom.library.sl.Strategy) o))) {
                    return tom_make_Innermost(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_OrCtl(((tom.library.sl.Strategy) o))) {
                    return tom_make_OrCtl(((tom.library.sl.Strategy) children[0]), ((tom.library.sl.Strategy) children[1]));
                }
                if (tom_is_fun_sym_TopDown(((tom.library.sl.Strategy) o))) {
                    return tom_make_TopDown(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_TopDownCollect(((tom.library.sl.Strategy) o))) {
                    return tom_make_TopDownCollect(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_EF(((tom.library.sl.Strategy) o))) {
                    return tom_make_EF(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_Pselect(((tom.library.sl.Strategy) o))) {
                    return tom_make_Pselect((java.lang.Integer) children[0], (java.lang.Integer) children[1], ((tom.library.sl.Strategy) children[2]), ((tom.library.sl.Strategy) children[3]));
                }
                if (tom_is_fun_sym_AF(((tom.library.sl.Strategy) o))) {
                    return tom_make_AF(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_AG(((tom.library.sl.Strategy) o))) {
                    return tom_make_AG(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_IfThenElse(((tom.library.sl.Strategy) o))) {
                    return tom_make_IfThenElse(((tom.library.sl.Strategy) children[0]), ((tom.library.sl.Strategy) children[1]), ((tom.library.sl.Strategy) children[2]));
                }
                if (tom_is_fun_sym_TopDownIdStopOnSuccess(((tom.library.sl.Strategy) o))) {
                    return tom_make_TopDownIdStopOnSuccess(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_Omega(((tom.library.sl.Strategy) o))) {
                    return tom_make_Omega((java.lang.Integer) children[0], ((tom.library.sl.Strategy) children[1]));
                }
                if (tom_is_fun_sym_EU(((tom.library.sl.Strategy) o))) {
                    return tom_make_EU(((tom.library.sl.Strategy) children[0]), ((tom.library.sl.Strategy) children[1]));
                }
                if (tom_is_fun_sym_EX(((tom.library.sl.Strategy) o))) {
                    return tom_make_EX(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_Fail(((tom.library.sl.Strategy) o))) {
                    return tom_make_Fail();
                }
                if (tom_is_fun_sym_AU(((tom.library.sl.Strategy) o))) {
                    return tom_make_AU(((tom.library.sl.Strategy) children[0]), ((tom.library.sl.Strategy) children[1]));
                }
                if (tom_is_fun_sym_AX(((tom.library.sl.Strategy) o))) {
                    return tom_make_AX(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_BottomUp(((tom.library.sl.Strategy) o))) {
                    return tom_make_BottomUp(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_Repeat(((tom.library.sl.Strategy) o))) {
                    return tom_make_Repeat(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_FalseCtl(((tom.library.sl.Strategy) o))) {
                    return tom_make_FalseCtl();
                }
                if (tom_is_fun_sym_Sequence(((tom.library.sl.Strategy) o))) {
                    if (children.length == 0) {
                        return tom_empty_list_Sequence();
                    } else {
                        return tom_cons_list_Sequence(((tom.library.sl.Strategy) children[0]), ((tom.library.sl.Strategy) children[1]));
                    }
                }
                if (tom_is_fun_sym_MuVar(((tom.library.sl.Strategy) o))) {
                    return tom_make_MuVar((String) children[0]);
                }
                if (tom_is_fun_sym_Outermost(((tom.library.sl.Strategy) o))) {
                    return tom_make_Outermost(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_DeRef(((tom.library.sl.Strategy) o))) {
                    return tom_make_DeRef(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_SequenceId(((tom.library.sl.Strategy) o))) {
                    if (children.length == 0) {
                        return tom_empty_list_SequenceId();
                    } else {
                        return tom_cons_list_SequenceId(((tom.library.sl.Strategy) children[0]), ((tom.library.sl.Strategy) children[1]));
                    }
                }
                if (tom_is_fun_sym_AllSeq(((tom.library.sl.Strategy) o))) {
                    return tom_make_AllSeq(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_OnceBottomUpId(((tom.library.sl.Strategy) o))) {
                    return tom_make_OnceBottomUpId(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_All(((tom.library.sl.Strategy) o))) {
                    return tom_make_All(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_AndCtl(((tom.library.sl.Strategy) o))) {
                    return tom_make_AndCtl(((tom.library.sl.Strategy) children[0]), ((tom.library.sl.Strategy) children[1]));
                }
                if (tom_is_fun_sym_Up(((tom.library.sl.Strategy) o))) {
                    return tom_make_Up(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_Try(((tom.library.sl.Strategy) o))) {
                    return tom_make_Try(((tom.library.sl.Strategy) children[0]));
                }
                if (tom_is_fun_sym_InnermostIdSeq(((tom.library.sl.Strategy) o))) {
                    return tom_make_InnermostIdSeq(((tom.library.sl.Strategy) children[0]));
                }

            }
            if (tom_is_sort_Position(o)) {
            }
            if (tom_is_sort_T1(o)) {
                if (tom_is_fun_sym_a(((base.data.types.T1) o))) {
                    return tom_make_a();
                }
                if (tom_is_fun_sym_f(((base.data.types.T1) o))) {
                    return tom_make_f(((base.data.types.T1) children[0]), ((base.data.types.T2) children[1]));
                }

            }
            if (tom_is_sort_T2(o)) {
                if (tom_is_fun_sym_b(((base.data.types.T2) o))) {
                    return tom_make_b();
                }
                if (tom_is_fun_sym_g(((base.data.types.T2) o))) {
                    return tom_make_g(((base.data.types.T2) children[0]));
                }

            }
            return o;

        }

        @SuppressWarnings("unchecked")
        public Object getChildAt(Object o, int i) {
            return getChildren(o)[i];
        }

        @SuppressWarnings("unchecked")
        public Object setChildAt(Object o, int i, Object child) {

            Object[] newChildren = getChildren(o);
            newChildren[i] = child;
            return setChildren(o, newChildren);
        }
    }

    public static class Rule extends tom.library.sl.AbstractStrategyBasic {
        public Rule() {
            super(tom_make_Fail());
        }

        public tom.library.sl.Visitable[] getChildren() {
            tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
            stratChilds[0] = super.getChildAt(0);
            return stratChilds;
        }

        public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
            super.setChildAt(0, children[0]);
            return this;
        }

        public int getChildCount() {
            return 1;
        }

        public tom.library.sl.Visitable getChildAt(int index) {
            switch (index) {
                case 0:
                    return super.getChildAt(0);
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
            switch (index) {
                case 0:
                    return super.setChildAt(0, child);
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        @SuppressWarnings("unchecked")
        public base.data.types.T1 visit_T1(base.data.types.T1 tom__arg, tom.library.sl.Introspector introspector)
                throws tom.library.sl.VisitFailure {
            {
                {
                    if (tom_is_sort_T1(tom__arg)) {
                        if (tom_is_fun_sym_f(((base.data.types.T1) tom__arg))) {

                            return
                                    tom_get_slot_f_s1(((base.data.types.T1) tom__arg));


                        }
                    }

                }

            }
            return _visit_T1(tom__arg, introspector);

        }

        @SuppressWarnings("unchecked")
        public base.data.types.T1 _visit_T1(base.data.types.T1 arg, tom.library.sl.Introspector introspector)
                throws tom.library.sl.VisitFailure {
            if (!((environment == null))) {
                return ((base.data.types.T1) any.visit(environment, introspector));
            } else {
                return any.visitLight(arg, introspector);
            }
        }

        @SuppressWarnings("unchecked")
        public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
                throws tom.library.sl.VisitFailure {
            if (tom_is_sort_T1(v)) {
                return ((T) visit_T1(((base.data.types.T1) v), introspector));
            }
            if (!((environment == null))) {
                return ((T) any.visit(environment, introspector));
            } else {
                return any.visitLight(v, introspector);
            }

        }
    }

    private static tom.library.sl.Strategy tom_make_Rule() {
        return new Rule();
    }

    private static boolean tom_is_fun_sym_Rule(tom.library.sl.Strategy t) {
        return (t instanceof Rule);
    }


}
