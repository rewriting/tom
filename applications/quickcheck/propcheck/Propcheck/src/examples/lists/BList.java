package examples.lists;

//import examples.lists.blist.types.*;

public class BList {
	private static boolean tom_equal_term_boolean(boolean t1, boolean t2) {return  t1==t2 ;}private static boolean tom_is_sort_boolean(boolean t) {return  true ;} private static boolean tom_equal_term_char(char t1, char t2) {return  t1==t2 ;}private static boolean tom_is_sort_char(char t) {return  true ;} private static boolean tom_equal_term_String(String t1, String t2) {return  t1.equals(t2) ;}private static boolean tom_is_sort_String(String t) {return  t instanceof String ;} private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_BList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_BList(Object t) {return  (t instanceof examples.lists.blist.blist.types.BList) ;}private static boolean tom_equal_term_Elem(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Elem(Object t) {return  (t instanceof examples.lists.blist.blist.types.Elem) ;}  









}