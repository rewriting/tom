(* vim: set filetype=ocaml : *)
(*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.  
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *)

open Peanotype ;;
exception Erreur of string ;;

%typeterm term {
  implement { mlpeano }
  is_sort(t) { true }
  equals(t1,t2) {t1=t2}
}
  
%op term zero() {
  is_fsym(t) { get_sym(t) = fzero }
}

%op term suc(s1:term) {
  is_fsym(t) { get_sym(t) = fsuc }
  get_slot(s1,t) { get_sub 0 t }
  make(t) { (Suc t) }
}

let rec plus (t1,t2)= 
  let res = ref None in (
  %match (term t1 , term t2 ) {
    x,zero()  -> { res := Some `(x) }
    x, suc(y) -> { res := Some (`suc(plus(x,y))) }
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
