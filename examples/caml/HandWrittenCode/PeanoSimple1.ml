open Peanotype ;;

exception Erreur of string ;;

let tom_get_fun_sym_term(t) = get_sym (t)
let tom_cmp_fun_sym_term(t1,t2) = t1=t2
let tom_get_subterm_term(t, n) = get_sub n t




let rec plus (t1,t2)=  
  let res = ref None in (


      let tom_match1_1 = t1 in (	
        let tom_match1_2 = t2 in (
          (
      	    (* match1_pattern1 *) 
	    (* let x = (tom_match1_1 : mlpeano) in *)
	    let x = tom_match1_1 in (  
	      if (tom_cmp_fun_sym_term(tom_get_fun_sym_term(tom_match1_2) ,  fzero ))
	      then (
                res := Some x
              )
	    )
        );           
	
	( (* match1_pattern2 *)
	  let x = tom_match1_1 in (
	    if(tom_cmp_fun_sym_term(tom_get_fun_sym_term(tom_match1_2) ,  fsuc ))
	    then (
	      let tom_match1_2_1=tom_get_subterm_term(tom_match1_2, 0) 
	      in 
	      let y= tom_match1_2_1
	      in res := Some (tom_make_suc(plus(x,y)))
            )
            (* else () *)
	  )
        );
        )
      )

      ;
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


(* REMARQUES SUR LA TRADUCTION
 * J'ai essayé de coller à la syntaxe CAML pour ne pas
 * dérouter l'utilisateur. Je ne sais pas si c'est le bon
 * choix. Voir le %match.
 *
 * En revanche pour %typeterm et %op je n'ai rien trouvé
 *  d'autre pour la notation par accolades. *)
