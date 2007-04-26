type term = 
    Var of string
  | Fun of string * term list

type term_list = term list

type prop = 
    Atomic of string * term_list
  | Not of prop
  | Or of prop * prop
  | And of prop * prop
  | All of string * prop
  | Ex of string * prop

type eq = 
    Term of term * term
  | Prop of prop * prop

type eq_list = eq list

type string_list = string list

type constr = string_list * eq_list * eq_list

type a_prop = prop * constr

type prop_list = a_prop list

type annotation = bool * bool

type branch = prop_list * annotation

type tableau = branch list

let rec term_to_string = function
    Var s -> s
  | Fun(s, a) -> s ^ 
      (match a with
	   [] -> "."
	 | x::q ->
	     (List.fold_left (fun a t -> a ^ ", " ^ (term_to_string t))
		("(" ^ (term_to_string x)) q) ^ ")"
      )
	    
let rec prop_to_string = function  
    Atomic (s,a) ->
      s ^ (match a with
	       [] -> "";
	     |  x::q ->  
		  (List.fold_left (fun a t -> a ^  ", " ^ (term_to_string t)) 
		    ("(" ^ (term_to_string x)) q) ^ ")")
  | Not(Atomic _ as p) -> "-" ^ (prop_to_string p)
  | Not p ->  Printf.sprintf "-(%s)" (prop_to_string p)
  | And(Atomic _ as p, (Atomic _ as q)) 
  | And(Not _ as p, (Atomic _ as q))
  | And(Not _ as p, (Not _ as q))
  | And(Atomic _ as p, (Not _ as q))
    ->
      Printf.sprintf "%s /\\ %s" (prop_to_string p) (prop_to_string q)
  | Or(Atomic _ as p, (Atomic _ as q)) 
  | Or(Not _ as p, (Atomic _ as q))
  | Or(Not _ as p, (Not _ as q))
  | Or(Atomic _ as p, (Not _ as q))
    ->
      Printf.sprintf "%s \\/ %s" (prop_to_string p) (prop_to_string q)
  | And(Atomic _ as p, q) 
    ->
     Printf.sprintf "%s /\\ (%s)" (prop_to_string p) (prop_to_string q)
  | Or(Atomic _ as p, q) 
    ->
     Printf.sprintf "%s \\/ (%s)" (prop_to_string p) (prop_to_string q)
  | And(p, (Atomic _ as q)) 
    ->
      Printf.sprintf "(%s) /\\ %s" (prop_to_string p) (prop_to_string q)
  | Or(p, (Atomic _ as q)) 
    ->
     Printf.sprintf "(%s) \\/ %s" (prop_to_string p) (prop_to_string q)
  | And(Not _ as p, q) 
    ->
     Printf.sprintf "%s /\\ (%s)" (prop_to_string p) (prop_to_string q)
  | Or(Not _ as p, q) 
    ->
     Printf.sprintf "%s \\/ (%s)" (prop_to_string p) (prop_to_string q)
  | And(p, (Not _ as q)) 
    ->
      Printf.sprintf "(%s) /\\ %s" (prop_to_string p) (prop_to_string q)
  | Or(p, (Not _ as q)) 
    ->
     Printf.sprintf "(%s) \\/ %s" (prop_to_string p) (prop_to_string q)
  | And(p, q) 
    ->
      Printf.sprintf "(%s) /\\ (%s)" (prop_to_string p) (prop_to_string q)
  | Or(p, q) 
    ->
      Printf.sprintf "(%s) \\/ (%s)" (prop_to_string p) (prop_to_string q)
  | All(x,p) ->
      Printf.sprintf "ALL %s. %s" x (prop_to_string p)
  | Ex(x,p) ->
      Printf.sprintf "EX %s. %s" x (prop_to_string p)

let branch_to_string (b, _: branch) = 
  let rec aux = function
      [] -> ""
  | [(p,_)] -> prop_to_string p
  | (p,_)::q -> Printf.sprintf "%s, %s" 
      (prop_to_string p) (aux q)
  in aux b

let rec tableau_to_string = function
    [] -> ""
  | [b] -> branch_to_string b
  | b::q -> Printf.sprintf "%s | %s" 
      (branch_to_string b) (tableau_to_string q)
	
let tableau_list_to_string = 
  List.fold_left (fun a t -> 
		    Printf.sprintf "%s%s\n" a (tableau_to_string t)) ""

let rec rules_to_string = function
    [] -> ""
  | [Term(s,t)] ->
      (term_to_string s) ^ " -> " ^ (term_to_string t)
  | [Prop(p,q)] ->
     (prop_to_string p) ^ " --> " ^ (prop_to_string q)
  | (Term(s,t))::q -> 
      Printf.sprintf "%s -> %s, %s"
	(term_to_string s) (term_to_string s) (rules_to_string q)
  | (Prop(p,q))::r ->
      Printf.sprintf "%s --> %s, %s"
	(prop_to_string p) (prop_to_string q) (rules_to_string r)

let rec subst_var sub name = match sub with
    Term(Var(s),t)::q when s = name -> t
  | _::q -> subst_var q name
  | [] -> Var(name)

let rec subst_term sub = function
  | Var s -> subst_var sub s
  | Fun(s,a) -> Fun(s, List.map (subst_term sub) a) 

let rec subst sub = function
  | Atomic(s,a) -> Atomic(s, List.map (subst_term sub) a)
  | Not(p) -> Not(subst sub p)
  | And(p,q) -> And(subst sub p, subst sub q)
  | Or(p,q) -> Or(subst sub p, subst sub q)
  | All(x,q) -> All(x, subst (Term(Var x, Var x)::sub) q)
  | Ex(x,q) -> Ex(x, subst (Term(Var x, Var x)::sub) q)

let fresh_counter = ref 0

let fresh_var s = 
  fresh_counter := !fresh_counter + 1;
  s ^ (string_of_int !fresh_counter)

let rec prop_eq p q = match p,q with
  | All(x,p), All(y,q) ->
      (x = y && p = q) || (let z = Var(fresh_var "v_") in 
			     prop_eq 
			       (subst [Term(Var x, z)] p)
			       (subst [Term(Var y, z)] q))
  | Ex(x,p), Ex(y,q) ->
      (x = y && p = q) || (let z = Var(fresh_var "v_") in 
			     prop_eq 
			       (subst [Term(Var x, z)] p)
			       (subst [Term(Var y, z)] q))
  | Not(p), Not(q) -> prop_eq p q
  | And(p,q), And(r,s) -> prop_eq p r && prop_eq q s
  | Or(p,q), Or(r,s) -> prop_eq p r && prop_eq q s
  | _,_ -> p = q

