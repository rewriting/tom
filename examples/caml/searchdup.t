%typeterm int {
  implement           { int }
  is_sort(t) { true }
  equals(t1,t2)       { (t1=t2) }
}

%typeterm int_list {
  implement           { int list }
  is_sort(t) { true }
  equals(t1,t2)       { (t1 = t2) }
}

%oplist int_list conc(int*) {
    is_fsym(t)       { true }
    make_empty()     { [] }
    make_insert(c,s) { (c::s) }
    get_head(s)         { (List.hd s) }
    get_tail(s)         { (List.tl s) }
    is_empty(s)         { (s = []) }
}

let _ = let ps = [ 1;3;4;2;1;5] in
%match (int_list ps) {
    conc(_*,p,_*,!p,_*) -> {
      print_endline "not same."
    }
    _ -> {
      print_endline "Same."
    }
}
