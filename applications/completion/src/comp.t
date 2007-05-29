open Datatypes

open Globals

open Printf

%include { datatypes_tom.t }

exception Res of tableau list

exception Res_eq_list of eq_list
exception Not_match

let refresh eq =
  let rec in_subst x = function
      [] -> false
    | Term(Var y, _)::q when y = x -> true
    | e::q -> in_subst x q
  in
  let rec aux subst_accu = function
      (Var x)::q -> 
	  if in_subst x subst_accu
	  then  aux subst_accu q
	  else
	    let y = fresh_var "u_" in
	      aux ((Term(Var x, Var y))::subst_accu) q
    | Fun(n, a)::q ->
	aux subst_accu (List.rev_append a q)
    | [] -> subst_accu
  in
    match eq with
	Prop(Atomic(_,a) as p, q) -> let sub = aux [] a in
	  Prop(subst sub p, subst sub q)
      | Term(Fun(_,a) as s, t) -> let sub = aux [] a in
	  Term(subst_term sub s, subst_term sub t)
      | Term(Var x, t) -> let y = fresh_var "u_" in
	  Term(Var y, subst_term [Term(Var x, Var y)] t)
      | _ -> raise (Invalid_argument "Refreshing non-atomic rule")

let pmatch eqs = 
  let rec aux ts = %match (eq_list ts) {
    conc_eq(_*,p@prop(!atomic(_,_),_),_*) ->
      { 
	raise (Invalid_argument "Matching non-atomic proposition (left)")
      }

    conc_eq(_*,p@prop(_,!atomic[]),_*) ->
      { 
	raise (Invalid_argument "Matching non-atomic proposition (right)") 
      }

    conc_eq(_*,prop(atomic(s,_),atomic(!s,_)),_*) ->
      { raise Not_match }
 
    conc_eq(_*,term(fun(s,_),fun(!s,_)),_*) ->
      { raise Not_match }

    conc_eq(t1*,prop(p,p),t2*) -> 
      { raise (Res_eq_list (aux (`conc_eq(t1*,t2*)))) }

    conc_eq(t1*,term(p,p),t2*) -> 
      { raise (Res_eq_list (aux (`conc_eq(t1*,t2*)))) }
 
    conc_eq(_*,term(var(s),t),term(var(s),!t),_*) -> 
      { raise Not_match }
 
    conc_eq(_*,term(fun[],var[]),_*) -> 
      { raise Not_match }

    conc_eq(t1*,prop(atomic(s,a1),atomic(s,a2)),t2*) -> 
      { raise (Res_eq_list (aux (try 
				List.fold_left2 
				  (fun l t1 t2 -> Term(t1,t2)::l)
				  (`conc_eq(t1*,t2*)) (`a1) (`a2)
			      with Invalid_argument _ -> raise Not_match)))
      }

    conc_eq(t1*,term(fun(s,a1),fun(s,a2)),t2*) -> 
      { raise (Res_eq_list (aux (try 
				List.fold_left2 
				  (fun l t1 t2 -> Term(t1,t2)::l)
				  (`conc_eq(t1*,t2*)) (`a1) (`a2)
			      with Invalid_argument _ -> raise Not_match)))
      }

  }; ts
  in 
    try aux eqs
    with Res_eq_list l -> l

let rec check x = function
    Var(y) when y = x -> raise Not_match
  | Fun(_,a) -> List.iter (check x) a
  | _ -> ()

let rec is_in_term x = function
    Var(y) when y = x -> true
  | Fun(_,a) -> List.exists (is_in_term x) a
  | _ -> false

let rec is_in_prop x = function
    Atomic(_,a) -> List.exists (is_in_term x) a
  | _ -> failwith "Unifying non-atomic proposition (is_in)"

let rec is_in x = function
    [] -> false
  | Prop(p,q)::eqs -> is_in_prop x p || is_in_prop x q || is_in x eqs
  | Term(p,q)::eqs -> is_in_term x p || is_in_term x q || is_in x eqs

let unify ts = 
  let rec aux_accu accu ts = 
    let aux = aux_accu accu in
%match (eq_list ts) {
    conc_eq(_*,p@prop(!atomic(_,_),_),_*) ->
      { 
	raise (Invalid_argument "Unifying non-atomic proposition (left)")
      }

    conc_eq(_*,p@prop(_,!atomic[]),_*) ->
      { 
	raise (Invalid_argument "Unifying non-atomic proposition (right)") 
      }
   
    conc_eq(_*,prop(atomic(s,_),atomic(!s,_)),_*) ->
      { raise Not_match }

    conc_eq(t1*,prop(p,p),t2*) -> 
	  { raise (Res_eq_list (aux (`conc_eq(t1*,t2*)))) }

    conc_eq(t1*,prop(atomic(s,a1),atomic(s,a2)),t2*) -> 
      { raise (Res_eq_list (aux (try 
				List.fold_left2 
				  (fun l t1 t2 -> Term(t1,t2)::l)
				  (`conc_eq(t1*,t2*)) (`a1) (`a2)
			      with Invalid_argument _ -> raise Not_match)))
      }

    conc_eq(_*,term(fun(s,_),fun(!s,_)),_*) ->
      { raise Not_match }
 
    conc_eq(t1*,term(s,s),t2*) ->
      { raise (Res_eq_list (aux (`conc_eq(t1*,t2*)))) }
    
    conc_eq(t1*,term(s@fun[],t@var[]),t2*) ->
      { raise (Res_eq_list (aux (`conc_eq(t1*,term(t,s),t2*)))) }
    
    conc_eq(t1*,term(fun(s,a1),fun(s,a2)),t2*) -> 
      { raise (Res_eq_list (aux (try 
				   List.fold_left2 
				     (fun l t1 t2 -> Term(t1,t2)::l)
				     (`conc_eq(t1*,t2*)) (`a1) (`a2)
				 with Invalid_argument _ -> raise Not_match)))
      }
  };
    match ts with
	(Term(Var(x),(Fun(_,_) as s)) as e)::q ->
	  check x s;
	  if is_in x q then let sub = [e] in
	    aux_accu (e::accu) 
	      (List.rev_map
		 (function 
		      Term(s,t) -> 
			Term(subst_term sub s,subst_term sub t)
		    | Prop(p,q) ->
			Prop(subst sub p, subst sub q))
		       q)
	  else 
	    aux_accu (e::accu) q
      | (Term(Var(x),Var(y)) as e)::q ->
	  if is_in y q then
	    if is_in x q then
	      let sub = [e] in
		aux_accu (e::accu) 
		  (List.rev_map
		   (function 
			Term(s,t) -> 
			  Term(subst_term sub s,subst_term sub t)
		      | Prop(p,q) ->
			  Prop(subst sub p,subst sub q))
		   q)
	    else
	      let e = Term(Var y,Var x) in 
	      let sub = [e] in
		aux_accu (e::accu) 
		  (List.rev_map
		     (function 
			  Term(s,t) -> 
			    Term(subst_term sub s,subst_term sub t)
			| Prop(p,q) ->
			    Prop(subst sub p,subst sub q))
		     q)
	  else
	    aux_accu (e::accu) q
      | [] -> List.rev accu
  in
    try aux_accu [] ts
    with Res_eq_list x -> x

let rewrite_gen unify eq substi = function
    Atomic(n,a) as p ->
      begin match eq with 
	| Prop(l,r) ->
	    let sub = unify (Prop(l,p)::substi) in
	      subst sub r, sub
	| Term(l,r) ->
	    let rec aux = function
		[] -> raise Not_match
	      | (Fun(n,a) as t)::q -> 
		  begin
		    try
		      try 
			let sub = unify (Term(l,t)::substi) in
			  (subst_term sub r)::q, substi
		      with 
			  Not_match -> 
			    let ra, substi = aux a in
			      Fun(n, ra)::q, substi
		    with
			Not_match -> let r, s = aux q in
			  t::r, s
		  end
	      | t::q -> 
		  let r, s = aux q in
		    t::r, s
	    in let r,s = aux a in Atomic(n, r), s
      end
  | x ->
      Printf.fprintf stderr "%s" (prop_to_string x); flush stderr;
      raise (Invalid_argument "Rewriting non-atomic proposition")

let rewrite a b c= 
  try
    rewrite_gen pmatch a b c
  with Not_match -> rewrite_gen unify a b c

exception Solved

let closure tab =
  let rec aux accu = function
      (b,_)::q ->
	let accuref = ref [] in
%match (prop_list b) {
	  conc(_*,a(atomic(s,a1),c(_,c1,_)),_*,a(not(atomic(s,a2)),c(_,c2,_)),_*) |
	  conc(_*,a(not(atomic(s,a1)),c(_,c1,_)),_*,a(atomic(s,a2),c(_,c2,_)),_*) 
	-> {
	  (try 
             let 
		 to_add = List.fold_left2 (fun l t1 t2 -> Term(t1,t2)::l) 
	       (List.rev_append (`c1) (`c2)) (`a1) (`a2) in
	       ignore (unify to_add);
	       List.iter 
		 (fun q -> 
		   accuref :=
		     (List.rev_append to_add q)::!accuref)
		 accu
	   with Invalid_argument _ | Not_match -> ()
	  )
	}
};
	  if !accuref = [] 
	  then raise Not_match
	  else aux (!accuref) q
    | [] -> List.iter (fun cs -> try ignore (unify cs); raise Solved with Not_match -> ()) accu
  in
    try
      aux [[]] tab 
    with Not_match -> ()

exception Hop

let tableau rules tab =
  let h = Hashtbl.create 100000 in
  let hash t = t in
  let rec tableau_aux finished tabs =
    match tabs with
	[] -> finished
      | _ ->
	  let future_tabs = ref [] 
	  and future_finished = ref finished in
	  let add tab = future_tabs := tab::!future_tabs in
	  let add_f tab = future_finished := tab::!future_finished in
	    fprintf !proof_chan "\n [ ";
	    List.iter
	      (fun tab ->
		 try 
		   if tab = [] 
		   then raise Solved
		   else
		     add_f (Hashtbl.find h (hash tab))
		 with 
		     Not_found ->
		       Hashtbl.add h (hash tab) tab; 
		       fprintf (!proof_chan) "\n%s" (tableau_to_string tab);
		       try
  (* cleaning and axioms *)
%match (tableau tab) {
    tab(b1*,l,b2*,l,b3*) ->
	{ begin
	    fprintf (!proof_chan) "{redundance}";
	    add (`tab(b1*,l,b2*,b3*));
	    raise Hop
	  end }
    
    tab(b1*,b(conc(_*,a(True(),_),_*),_),b2*) ->
	{ begin 
	    fprintf (!proof_chan) "{true}";
	    add (`tab(b1*,b2*));
	    raise Hop
	  end } 

    tab(b1*,b(conc(_*,a(not(False()),_),_*),_),b2*) ->
	{ begin 
	    fprintf (!proof_chan) "{true}";
	    add (`tab(b1*,b2*));
	    raise Hop
	  end } 

    tab(b1*,b(conc(_*,a(p,_),_*,a(not(p),_),_*),_),b2*) ->
	{ begin 
	    fprintf (!proof_chan) "{closure}";
	    add (`tab(b1*,b2*));
	    raise Hop
	  end } 

    tab(b1*,b(conc(_*,a(not(p),_),_*,a(p,_),_*),_),b2*) ->
	{ begin 
	    fprintf (!proof_chan) "{closure}";
	    add (`tab(b1*,b2*));
	    raise Hop
	  end } 

    tab(b1*,b(conc(gamma*,a(False(),_),delta*),s),b2*) ->
	{ begin
	    fprintf (!proof_chan) "{false}";
	    add (`tab(b1*,b(conc(gamma*,delta*),s),b2*));
	    raise Hop
	  end }

    tab(b1*,b(conc(gamma*,a(not(True()),_),delta*),s),b2*) ->
	{ begin
	    fprintf (!proof_chan) "{false}";
	    add (`tab(b1*,b(conc(gamma*,delta*),s),b2*));
	    raise Hop
	  end }
    
    tab(b1*,b(conc(gamma*,ap,eta*,ap,delta*),s),b2*) ->
	{ begin
	    fprintf (!proof_chan) "{weak}";
	    add (`tab(b1*,b(conc(gamma*,ap,eta*,delta*),s),b2*));
	    raise Hop
	  end }

};
  (* closure *)
  closure tab;
%match (tableau tab) {
    tab(b1*,b(conc(gamma*,a(not(not(p)),l),delta*),s),b2*) -> 
	{ begin
	    fprintf (!proof_chan) "{classical}";
	    add (`tab(b1*,b(conc(gamma*,a(p,l),delta*),s),b2*));
	    raise Hop
	  end
	}
    
    tab(b1*,b(conc(gamma*,a(or(p,q),l),delta*),s),b2*) ->
      { (* alpha *)
	begin
	  fprintf (!proof_chan) "{alpha}";
	  add (`tab(b1*,b2*,b(conc(gamma*,a(p,l),a(q,l),delta*),s)));
	  raise Hop
	end }
	      
    tab(b1*,b(conc(gamma*,a(not(and(p,q)),l),delta*),s),b2*) ->
      { (* alpha *)
	begin
	  fprintf (!proof_chan) "{alpha}";
	  add (`tab(b1*,b2*,b(conc(gamma*,a(not(p),l),a(not(q),l),delta*),s)));
	  raise Hop
	end }
		    
    tab(b1*,b(conc(gamma*,a(all(x,p),cs@c(l,_,_)),delta*),s),b2*) ->
      { (* delta *)
	let e = Term(Var (`x), 
		     Fun(fresh_var "sk_", 
			 List.rev_map (fun x -> Var x) (`l))) in
	let q = subst [e] (`p) 
	in
	  begin
	    fprintf (!proof_chan) "{delta}";
	    add (`tab(b1*,b2*,b(conc(gamma*,a(q,cs),delta*),s)));
	    raise Hop
	  end }

    tab(b1*,b(conc(gamma*,a(not(ex(x,p)),cs@c(l,_,_)),delta*),s),b2*) ->
      { (* delta *)
	let e = Term(Var (`x), 
		     Fun(fresh_var "sk_", 
			 List.rev_map (fun x -> Var x) (`l))) in
	let q = subst [e] (`p) 
	in
	  begin
	    fprintf (!proof_chan) "{delta}";
	    add (`tab(b1*,b2*,b(conc(gamma*,a(not(q),cs),delta*),s)));
	    raise Hop
	  end }

    tab(b1*,b(conc(gamma*,a(and(p,q),l),delta*),s),b2*) -> 
       { (* beta *)
	 begin 
	   fprintf (!proof_chan) "{beta}";
	   add (`tab(b1*,b2*,b(conc(gamma*,a(p,l),delta*),s),
				b(conc(gamma*,a(q,l),delta*),s)));
	   raise Hop
	 end }

    tab(b1*,b(conc(gamma*,a(not(or(p,q)),l),delta*),s),b2*) -> 
      { (* beta *)
	begin
	  fprintf (!proof_chan) "{beta}";
	  add (`tab(b1*,b2*,b(conc(gamma*,a(not(p),l),delta*),s),
				b(conc(gamma*,a(not(q),l),delta*),s)));
	  raise Hop
	end }
  
    tab(b1*,b(bg@conc(_*,a(ex(_,_),_),_*),an(true(),t)),b2*) |
    tab(b1*,b(bg@conc(_*,a(not(all(_,_)),_),_*),an(true(),t)),b2*)
       ->
     { (* gamma *)
       let rec aux accu = function
	 | ((Ex(x,p),(l,c,rr)) as o)::ps ->
	     let y = fresh_var "v_" in
	     let e = Term(Var x, Var y) in	
	     let q = subst [e] p 
	     and r = y::l, c, rr in
	       aux (o::(q,r)::accu) ps
	 | ((Not(All(x,p)),(l,c,rr)) as o)::ps ->
	     let y = fresh_var "v_" in
	     let e = Term(Var x, Var y) in	
	     let q = Not(subst [e] p)
	     and r = y::l, c, rr in
	       aux (o::(q,r)::accu) ps
	 | ap::ps -> aux (ap::accu) ps
	 | [] -> List.rev accu
       in let nbg = aux [] (`bg) in
	 begin
	   fprintf (!proof_chan) "{gamma}";
	   add (`tab(b1*,b2*,b(nbg,an(false(),t))));
	   raise Hop;
	 end
     }
};
%match (tableau tab) {
  tab(b1*,b(bg@conc(_*,a(atomic(_,_),_),_*),an(_,_)),b2*) 
| tab(b1*,b(bg@conc(_*,a(not(atomic(_,_)),_),_*),an(_,_)),b2*) -> { 
    (* rw *)
    let rec aux bunify accu c ap = 
      let unify = if bunify then unify else pmatch in
	match ap with
	  | (Atomic(_,_) as p,(l,cs,rr))::r ->
	      let rec aux2 = function
		| eq::eqs -> begin
		    try
		  if List.mem eq rr then raise Not_match
		  else
		    let rp, rc = rewrite_gen unify (refresh eq) cs p in
		      aux bunify ((rp,(l,rc,[]))::(p,(l,cs,eq::rr))::accu) true r
    		with
		    Not_match -> aux2 eqs
	      end
	    | [] -> aux bunify ((p,(l,cs,rr))::accu) c r
	  in aux2 rules
      | (Not(Atomic(_,_) as p),(l,cs,rr))::r ->
	  let rec aux2 = function
	    | eq::eqs -> begin
		try
		  if List.mem eq rr then raise Not_match 
		  else
		    let rp, rc = rewrite_gen unify (refresh eq) cs p in
		      aux bunify ((Not rp,(l,rc,[]))::((Not p,(l,rc,eq::rr)))::accu)
			true r
		with
		    Not_match -> aux2 eqs
	      end
	    | [] -> aux bunify ((Not(p),(l,cs,rr))::accu) c r
	  in aux2 rules 
      | ap::r -> aux bunify (ap::accu) c r
      | [] -> if bunify || c then [accu], c else aux true [] c accu
    in
    let rec aux_s accu c = function
      | (Atomic(_,_) as p,(l,cs,rr))::r ->
	  let rec aux2 accu2 b = function
	    | eq::eqs -> begin
		try
		  if List.mem eq rr then raise Not_match
		  else
		    let rp, rc = rewrite (refresh eq) cs p in
		      aux2 (List.fold_left
			      (fun h q -> ((rp,(l,rc,[]))::(p,(l,cs,eq::rr))::q)::h)
			      accu2 accu) true
			eqs
		with
		    Not_match -> aux2 accu2 b eqs
	      end
	    | [] -> if b then accu2,b
	      else List.rev_map (fun q -> (p,(l,cs,rr))::q) accu, b
	  in let accu, b =  aux2 [] false rules in
	    aux_s accu (c || b) r
      | (Not(Atomic(_,_) as p),(l,cs,rr))::r ->
	  let rec aux2 accu2 b = function
	    | eq::eqs -> begin
		try
		  if List.mem eq rr then raise Not_match
		  else
		    let rp, rc = rewrite (refresh eq) cs p in
		      aux2 (List.fold_left
			      (fun h q -> ((Not rp,(l,rc,[]))::((Not p,(l,rc,eq::rr)))::q)::h) accu2 accu) true
		      eqs
		with
		    Not_match -> aux2 accu2 b eqs
	      end
	    | [] -> if b then accu2, b
	      else List.rev_map (fun q -> (Not(p),(l,cs,rr))::q) accu, b
	  in let accu, b = aux2 [] false rules in
	    aux_s accu (c||b) r
      | ap::r -> aux_s (List.rev_map (fun q -> ap::q) accu) c r
      | [] -> accu, c
    in
    let nbgs, c = if !Globals.split_rew then
      aux_s [[]] false (`bg)
    else aux false [] false (`bg) in
      if c then 
	begin
	  fprintf (!proof_chan) "{rw}";
	  List.iter
	    (fun nbg -> 
	       add 
		 (`tab(b1*,b2*,b(nbg,an(true(),true()))))) nbgs;
	  raise Hop
	end
      else ()
  }
};
%match (tableau tab) {
  tab(b1*,b(bg@conc(_*,a(ex(_,_),_),_*),an(_,t)),b2*) |
  tab(b1*,b(bg@conc(_*,a(not(all(_,_)),_),_*),an(_,t)),b2*)
    ->
      { (* gamma *)
	let rec aux accu = function
	  | ((Ex(x,p),(l,c,rr)) as o)::ps ->
	      let y = fresh_var "v_" in
	      let e = Term(Var x, Var y) in	
	      let q = subst [e] p 
	      and r = y::l, c, rr in
		aux (o::(q,r)::accu) ps
	  | ((Not(All(x,p)),(l,c,rr)) as o)::ps ->
	      let y = fresh_var "v_" in
	      let e = Term(Var x, Var y) in	
	      let q = Not(subst [e] p)
	      and r = y::l, c, rr in
		aux (o::(q,r)::accu) ps
	  | ap::ps -> aux (ap::accu) ps
	  | [] -> List.rev accu
	in let nbg = aux [] (`bg) in 
	  begin
	    fprintf (!proof_chan) "{gamma}";
	    add (`tab(b1*,b2*,b(nbg,an(false(),t))));
	    raise Hop
	  end }
};
add_f tab
			 with
			     Hop -> ()
	      ) tabs;
	    try
	      fprintf !proof_chan "\n ] ";
	      tableau_aux !future_finished !future_tabs		       
	    with
		Sys.Break -> List.rev_append !future_finished !future_tabs
  in Sys.catch_break true; tableau_aux [] [tab]
	 
let insert p q = 
  let rec aux accu = function
      a::qs when a = p -> q
    | a::qs -> aux (a::accu) qs 
    | [] -> p::accu
  in aux [] q

(*let rec unify_max tab = 
  let rec maxi tab =
    let rec aux res accu = function
	x::q -> let l, n = maxi (List.rev_append accu q) in
	  aux ((x::l, n+1)::res) (x::accu) q
      | [] -> List.fold_left (fun (_,nl as l) (_,ni as i) -> if nl > ni then i else l) ([],max_int) res
    in
      try 
	match tab with 
	    [] -> [], 0 
	  | [_] -> closure tab; tab, 1
	  | x::q -> closure tab; aux [] [] tab
      with
	  Solved -> [],0      
  in [fst (maxi tab)]
*)

module B = Set.Make (struct
  type t = branch
  let compare s t = compare s t
end)

module T = Set.Make (struct
  type t = B.t
  let compare = B.compare
end)

let unify_max tab =
  let rec direct_subsets e =
    let r = T.empty in
      B.fold (fun x r -> T.add (B.remove x e) r) e r
  in
  let rec aux tabs =
    let e = T.empty in
    let e =  T.fold (fun x e ->
		T.fold T.add e (direct_subsets x)) tabs e in
    let e = T.fold (fun x e ->
		      try closure (B.elements x);
			e
		      with Solved -> T.remove x e) e e in
      if T.is_empty e
      then
	T.fold (fun t r -> (B.elements t)::r) tabs []
      else
	aux e
  in aux (T.add (List.fold_left (fun x b -> B.add b x) B.empty tab) (T.empty))
      

let sort_result cut_prop tab =
  let rec aux accu = function
      (b,_)::bs -> 
	aux
	  (List.fold_left 
	     (fun res (p,_) -> 
		if (cut_prop = p) or (Not(cut_prop) = p)	  
		then res
		else List.fold_left (fun res (q, _) -> insert (insert (Not(p),([],[],[])) q,(true,true)) res) res accu)
	     [] b)
	  bs
    | [] -> accu
  in aux [[],(true,true)] tab

exception Res_tab of tableau

let rec clean tab = 
  try %match (tableau tab) {
    tab(b1*,b(ps,_),b2*,b(ps,_),b3*) ->
    { raise (Res_tab(clean (`tab(b1*,b(ps,an(true(),true())),b2*,b3*))))}

    tab(b1*,b(conc(_*,a(p,_),_*,a(not(p),_),_*),_),b2*) ->
	  { raise (Res_tab(clean (`tab(b1*,b2*))))}
    
    tab(b1*,b(conc(_*,a(not(p),_),_*,a(p,_),_*),_),b2*) ->
	  { raise (Res_tab(clean (`tab(b1*,b2*))))}

    tab(b1*,b(conc(gamma*,a(not(not(p)),l),delta*),_),b2*) ->
    { raise (Res_tab(clean (`tab(b1*,b(conc(gamma*,a(p,l),delta*),an(true(),true())),b2*))))}

    tab(b1*,b(conc(gamma*,p,eta*,p,delta*),_),b2*) ->
    { raise (Res_tab(clean (`tab(b1*,b(conc(gamma*,p,eta*,delta*),an(true(),true())),b2*))))}

  }; tab
    with Res_tab x -> x
 
let rec critic eq1 = function
    Prop(Atomic(n,a) as p,_) as eq2 ->
      let rp, sub1 = rewrite (refresh eq1) [] p in
      let rq, sub2 = rewrite eq2 sub1 (subst sub1 p) in
      let bp = subst sub2 p in
	[
	  [bp,([],[],[]); rp,([],[],[])],(true,true);
	  [Not(bp),([],[],[]); Not(rq),([],[],[])],(true,true)
	],
	[
	  [bp,([],[],[]); rq,([],[],[])],(true,true);
	  [Not(bp),([],[],[]); Not(rp),([],[],[])],(true,true)
	], bp
  | Term(_,_) as eq2 -> begin
      match eq1 with
	  Prop(_,_) -> critic eq2 eq1
	| _ -> raise Not_match end
  | Prop(x,_) ->
      Printf.fprintf stderr "%s" (prop_to_string x); flush stderr;
      raise (Invalid_argument "Rule with non non-atomic proposition")

let rec orify = function
    [] -> False
  | (False,_)::r -> orify r
  | (q,_)::r ->
      let rec aux accu = function 
	| (False,_)::t -> aux accu t
	| (p,_)::t -> aux (Or(p,accu)) t
	| [] -> accu
      in 
	aux q r

let rec get_rules = function 
    b::r -> begin try
%match (branch b) {
  b(conc(b1*,a(p@atomic(_,_),_),b2*),_) -> {
    raise (Res_eq_list 
	   (Prop((`p), Or((`p), Not(orify((orify (`b1),([],[],[]))::(`b2)))))::(get_rules r))
	  ) }
    
    b(conc(b1*,a(not(p@atomic(_,_)),_),b2*),_) -> {
   raise (Res_eq_list 
	  (Prop((`p), And((`p), orify ((orify (`b1),([],[],[]))::(`b2))))::(get_rules r))
	  ) }

    b(conc(gamma*,a(not(not(p)),l),delta*),s) -> 
	{ 
	    raise (Res_eq_list 
		   (get_rules (`tab(b(conc(gamma*,a(p,l),delta*),s),r*))))
	}
    
    b(conc(gamma*,a(or(p,q),l),delta*),s) ->
      { (* alpha *)
	raise (Res_eq_list 
	       (get_rules (`tab(b(conc(gamma*,a(p,l),a(q,l),delta*),s),r*))))
      }
	      
    b(conc(gamma*,a(not(and(p,q)),l),delta*),s) ->
      { (* alpha *)
	raise (Res_eq_list 
	       (get_rules (`tab(b(conc(gamma*,a(not(p),l),a(not(q),l),delta*),s),r*))))
      }
		    
    b(conc(gamma*,a(all(x,p),cs@c(l,_,_)),delta*),s) ->
      { (* delta *)
	let e = Term(Var (`x), 
		     Var(fresh_var "var_")) in
	let q = subst [e] (`p) 
	in
	raise (Res_eq_list 
	       (get_rules (`tab(b(conc(gamma*,a(q,cs),delta*),s),r*))))
      }

    b(conc(gamma*,a(not(ex(x,p)),cs@c(l,_,_)),delta*),s) ->
      { (* delta *)
	let e = Term(Var (`x), 
		     Var (fresh_var "var_")) in
	let q = subst [e] (`p) 
	in
	raise (Res_eq_list 
	       (get_rules (`tab(b(conc(gamma*,a(not(q),cs),delta*),s),r*))))
      }

    b(conc(gamma*,a(and(p,q),l),delta*),s) -> 
       { (* beta *)
	 raise (Res_eq_list 
		(get_rules (`tab(b(conc(gamma*,a(p,l),delta*),s),
				 b(conc(gamma*,a(q,l),delta*),s),r*))))
       }

    b(conc(gamma*,a(not(or(p,q)),l),delta*),s) -> 
      { (* beta *)
       	  raise (Res_eq_list 
		 (get_rules (`tab(b(conc(gamma*,a(not(p),l),delta*),s),
				  b(conc(gamma*,a(not(q),l),delta*),s),r*))))
      }

   b(conc(gamma*,ap@a(ex(x,p),cs@c(l,_,_)),delta*),s) ->
      { (* gamma *)
	let e = Term(Var (`x), 
		     Fun(fresh_var "const_",[])) in
	let q = subst [e] (`p) 
	in
	raise (Res_eq_list 
	       (get_rules (`tab(b(conc(gamma*,a(q,cs),delta*,ap),s),r*))))
      }

    b(conc(gamma*,ap@a(not(all(x,p)),cs@c(l,_,_)),delta*),s) ->
      { (* gamma *)
	let e = Term(Var (`x), 
		     Fun(fresh_var "const_",[])) in
	let q = subst [e] (`p) 
	in
	raise (Res_eq_list 
	       (get_rules (`tab(b(conc(gamma*,a(not(q),cs),delta*,ap),s),r*))))
      }

}; failwith "Problem with the creation of the rules"
    with Res_eq_list l -> l end
  | [] -> []
      
