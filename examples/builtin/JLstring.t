public class JLstring {

%typelist String {
  implement           { String }
  get_fun_sym(t)      { t }
  cmp_fun_sym(s1,s2)  { (s1.equals(s2)) }
  equals(t1,t2)       { (t1.equals(t2)) }
  get_head(s)         { new Character(s.charAt(0)) }
  get_tail(s)         { s.substring(1) }
  is_empty(s)         { (s.length()==0) }
}

%typeterm Char {
  implement           { Character }
  get_fun_sym(t)      { t }
  cmp_fun_sym(s1,s2)  { (s1.equals(s2)) }
  get_subterm(t, n)   { null }
  equals(t1,t2)       { (t1.equals(t2)) }
}


%oplist String concChar(Char*) {
  fsym             { null }
  is_fsym(t)       { (t!= null) && (t instanceof String) }
  make_empty()     { "" }
  make_insert(c,s) { (c+s) }
}

  public final static void main(String[] args) {
    JLstring test = new JLstring();
    String res = test.f("hello");

    System.out.println("res = " + res);
  }

  public String f(String s) {
    Character l = new Character('l');
    Character o = new Character('o');
    %match(String s, Char l, Char o) {
      (_*,x,y,_*), x,y -> {
        System.out.println("char = " + x);
        System.out.println("char = " + y);
      }
      _,_,_        -> { return "unknown"; }
    }
  }

}

/*

%typelist boolean {
  implement { boolean }
}

%typelist String {
  implement                             { String }
  Char get_fun_sym(String t)            { t }
  boolean cmp_fun_sym(Char s1, Char s2) { (s1.equals(s2)) }
  boolean equals(String t1, String t2)  { (t1.equals(t2)) }
  Char get_head(String s)               { s.charAt(0) }
  String Get_tail(String s)             { s.substring(1) }
  boolean is_empty(String s)            { (s.length()==0) }
}

%typeterm Char {
  implement                              { char }
  Char get_fun_sym(Char t)               { t }
  boolean cmp_fun_sym(Char s1, Char s2)  { (s1.equals(s2)) }
  boolean equals(Char t1, Char t2)       { (t1.equals(t2)) }
}

%oplist String concChar(Char*) {
  boolean is_fsym(Char t)              { (t!= null) && (t instanceof String) }
  String make_empty()                  { "" }
  String make_insert(Char c, String s) { (c+s) }
}


*/
