open TomExtension;;
open TomList ;;

exception Res of int list

%typelist list {
    implement { List }
    get_fun_sym(t) { "conc" }
    cmp_fun_sym(t1,t2) { t1 = t2 }
    equals(l1,l2) { l1=l2 }
    get_head(l)   { List.hd l }
    get_tail(l)   { List.tl l }
    is_empty(l)   { l = [] }
  }

%typeterm entier{
    implement { int }
    get_fun_sym(t) {}
    cmp_fun_sym (t1,t2) {t1 = t2}
    get_subterm(t,n) {}
}

%oplist list conc( entier* ) {
  fsym { "conc" }
  make_empty()  { [] }
  make_insert(e,l) { e::l }
}
  


let rec swapsort(l) = 
try (
  (

   %match(list l) {
    conc(X1*,x,y,X2*) -> {
      if (x>y) then raise (Res ( swapSort(`conc(X1*,y,x,X2*)) ))
    }
  }
  ; l
)
with Res r -> r


let rec string_of_int_list = function [] -> "\n"
  | i :: r -> (string_of_int i) ^ " " ^ (string_of_int_list r);;


print_string "Bonjour ;-)\n";;
let l1 = [10;9;8;7;6;5;];;
print_string(string_of_int_list l1);;
let l2 = swapsort(l1);;
print_string(string_of_int_list l1);;
print_string(string_of_int_list l2);;
