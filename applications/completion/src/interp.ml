open Datatypes

exception Eof

let rules = ref []
  
let solve_and_print tab =  
  begin try
    let t1 = Comp.tableau !rules tab in
      print_string "\n------------------\n";
      print_string "Remaining branches\n";
      print_string (tableau_list_to_string t1);
      flush stdout
  with Comp.Solved ->
    print_string "\n------------------\n";
    print_string "  Proved !\n";
    flush stdout
  end

let unif rules = 
  try
    print_string (rules_to_string (Comp.unify rules)); flush stdout
   with 
      Comp.Not_match -> print_string "Can't unify.\n"; flush stdout

let bunif t = 
  try 
    Comp.closure t; print_string "Can't unify.\n"; flush stdout 
  with Comp.Solved -> print_string "Unifiable.\n"; flush stdout

let comp tab = 
  begin try
    let t1 = Comp.tableau !rules tab in
    let s = 
      List.fold_left (fun r t -> List.rev_append (Comp.unify_max t) r)
      [] t1 in 
      print_string "\n------------------\n";
      print_string "Tableaux to be completed:\n";
      print_string (tableau_list_to_string s);
      flush stdout
  with Comp.Solved ->
    print_string "\n------------------\n";
    print_string "Nothing to be completed.\n";
    flush stdout
  end

let create_rules p t =  
  try
    let t1 = Comp.tableau !rules t in
      List.iter (fun t -> 
		   let bs = List.fold_left 
		     (fun res t -> 
			List.rev_append res (Comp.sort_result p t))
		     [] (Comp.unify_max t) in
		   let bs = Comp.clean bs in
		     print_string ("Conclusions of critical proofs: "
				   ^ (tableau_to_string bs) ^
				   "\n");
		   let r = Comp.get_rules bs in
		     print_string ("Adding: " ^ (rules_to_string r) ^ "\n");
		     flush stdout;
		     rules := List.rev_append r !rules) t1
  with Comp.Solved -> 
    print_string "Nothing to be completed.\n";
    flush stdout

let rec critical_proofs = function  
    eq2::q as l ->
      List.iter
	(fun eq1 ->
	   try 
	     let t1, t2, p = Comp.critic eq1 eq2 in
	       Printf.printf "\nCritical pair:\n %s \nvs. %s\n" 
		   (rules_to_string [eq1])
		 (rules_to_string [eq2]);
	       flush stdout;
	       create_rules p t1;
	       if not(t1 = t2) then
		 create_rules p t2 else ()
	   with Comp.Not_match -> ()
	) l;
      critical_proofs q
  | [] -> ()

    
open Lexing

let error_handling str =
  try
    let p = Parsing.symbol_start_pos () and q = Parsing.symbol_end_pos () in
    let r = p.pos_cnum - p.pos_bol and s = q.pos_cnum - q.pos_bol in
      Printf.fprintf stderr 
	"Parse error at line %d, characters %d to %d:\n  %s\n" 
	p.pos_lnum r s str; flush stderr
  with exn -> Printf.fprintf stderr
    "Error while processing an error:\n  %s\n" str; flush stderr

