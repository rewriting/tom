(* vim: set filetype=ocaml : *)
(*open TomExtension;;*)
(*open TomList ;; *)

exception Res of int list
  
%include { caml/int.tom }
%include { caml/list.tom }

%oplist list conc( int* ) {
  fsym { "conc" }
  make_empty()  { [] }
  make_insert(e,l) { e::l }
}

let rec swapsort l = 
try (
   %match(list l) {
    conc(x1*,x,y,x2*) -> {
      if (x>y) then raise (Res(swapsort(`conc(x1*,y,x,x2*))))
    }

    conc(x1*,x,x,x2*) -> {
      raise (Res(swapsort(`conc(x1*,x,x2*))))
    }

  }
  ; l
)
with Res r -> r
 
 
let rec string_of_int_list = function [] -> "\n"
  | i :: r -> (string_of_int i) ^ " " ^ (string_of_int_list r);;


let l1 = [10;8;7;6;5;5;6;7;8;9];;
let l2 = swapsort(l1);;
print_string(string_of_int_list l1);;
print_string(string_of_int_list l2);;
