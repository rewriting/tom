%include {caml/list.tom}

%typeterm any {
  implement          { dontcare (* not used *)     }
  get_fun_sym(t)     { ()       (* not used ? *)   }
  cmp_fun_sym(t1,t2) { true     (* is it used ? *) }
  get_subterm (t,n)  { ()       (* not used *)     }
}

%oplist list conc( any* ){
  fsym{ "conc" }
  make_empty() { [] }
  make_insert(e,l) { e::l }
}


let rec genere = function
    2 -> [2]
  | n -> n :: genere (n-1) ;;


exception Result of int list
let rec elim l=
try(
%match (list l) {
  conc(x*,e1,y*,e2,z*) -> { if e1 mod e2 = 0 
			    then raise (Result (elim(`conc (x*,y*,e2,z*)))) } 
}; l
)with Result r -> r;;
  

let erat n = elim (genere n) ;;

let rec string_of_l l =
  List.fold_right (fun e -> fun r -> string_of_int e ^ " " ^ r) l "" ;;

let r = erat 200 in
  print_string (string_of_l r ^ "\n") ;;




