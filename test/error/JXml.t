%typeterm TNode{
  implement { TNode}
  get_fun_sym(t) {null}
  cmp_fun_sym(s1,s2) { false}
  get_subterm(t,n) {null}
  equals(t1,t2) {t1.equals(t2)}
}

%match(TNode list) {
      <IntegerList>[<(Int|Integer)>(#TEXT(s1))</(Int|Integer)>,
                    <(Integer|Int)>(#TEXT(s2))</(Integer|In)>]</IntegerList> -> {
        if(s1.compareTo(s2) > 0) { return false; }
      }
    }
