open Peanotype ;;

%typeterm term {
  implement { mlpeano }
  get_fun_sym (t) { get_sym (t)} 
  cmp_fun_sym (t1,t2) {t1=t2} 
  get_subterm (t,n) {get_sub n t} 
}

  
%op term zero {
  fsym { fzero }
}

%op term suc(term) {
  fsym { fsuc }
}



let rec plus (t1,t2)= 
  let res = None in (
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
      
