(*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
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

(* vim: set filetype=ocaml : *)

exception Erreur of string ;;

%include { caml/metal.tom }

type peano =
    Zero 
  | Suc of peano ;;

let fzero = get_fun_sym(Zero)
and fsuc  = get_fun_sym(Suc(Zero));;

%typeterm peano {
  implement { peano }
  is_sort(t) { true }
  equals(t1,t2) { t1=t2 }
}
  
%op peano zero() {
  is_fsym(t) { get_fun_sym(t) = fzero }
  make { Zero }
}

%op peano suc(pred:peano) {
  is_fsym(t) { get_fun_sym(t) = fsuc }
  get_slot(pred,t) { get_subterm t 0 }
  make(t) { Suc t }
}

exception Result of peano
let rec plus (t1,t2)= 
  try(
  %match (peano t1 , peano t2 ) {
    x, zero() -> { raise (Result(`x)) }
    x, suc(y) -> { raise (Result(`suc(plus(x,y)))) }
  };
  assert false
  ) with Result r -> r;;    
      
let rec fib (t1)= 
  try(
  %match (peano t1 ) {
    zero() -> { raise (Result(`suc(zero()))) }
    suc(zero()) -> { raise (Result(`suc(zero()))) }
    suc(suc(x)) -> { raise (Result(`plus(fib(x),fib(suc(x))))) }
  };
  assert false
  ) with Result r -> r;;    

let rec make_peano = function
    0 -> Zero
  | n -> Suc (make_peano (n-1))

let rec string_of_peano = function
    Zero -> "Zero"
  | Suc n -> "Suc(" ^ (string_of_peano n) ^")"

let run (n) =
  let n'  = make_peano (n) in
  let res = plus (n',n') in
    print_string ( "plus(" ^ (string_of_int n) ^ "," ^ (string_of_int n) ^
		   ") = " ^ (string_of_peano res)  ^ "\n" );;

let main () =
  print_string "Bonjour :-) \n";
  run (10);
  for i=1 to 5 do (fib ( make_peano (18)) ; ()) done
;;

main();;
