
open Peanotype ;;
exception Erreur of string ;;

%typeterm term {
  implement { mlpeano }
  get_fun_sym (t) { get_sym (t)} 
  cmp_fun_sym (t1,t2) {t1=t2} 
  get_subterm (t,n) {get_sub n t} 
  equals(t1,t2) {t1=t2}
}

  
%op term zero {
  fsym { fzero }
}

%op term suc(term) {
  fsym { fsuc }
}



let rec plus (t1,t2)= 
  let res = ref None in (
  %match (term t1 , term t2 ) {
    x,zero    -> {res := Some x}
    x, suc(y) -> {res := Some (`suc(plus(x,y)))}
  };	
    
  match !res with 
	      None -> raise (Erreur "rien n'a filtre")
      | Some r -> r
  ;
  )
;;
      
let rec make_peano = function
    0 -> Zero
  | n -> Suc (make_peano (n-1))

let run (n) =
  let n'  = make_peano (n) in
  let res = plus (n',n') in
    print_string ( "plus(" ^ (string_of_int n) ^ "," ^ (string_of_int n) ^
		   ") = " ^ (string_of_peano res)  ^ "\n" );;

let main () =

print_string "Bonjour :-) \n";
  run (10)
;;

main();;
