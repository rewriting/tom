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

%include {caml/list.tom}

%typeterm any {
  implement          { dontcare (* not used *)     }
  is_sort(t)     { true }
}

%oplist list conc( any* ){
  is_fsym(t) { true }
  make_empty() { [] }
  make_insert(e,l) { e::l }
  get_head(l)   { List.hd l }
  get_tail(l)   { List.tl l }
  is_empty(l)   { (l=[]) }
}

let rec genere = function
    2 -> [2]
  | n -> n :: genere (n-1) ;;


exception Result of int list
let rec elim l=
try(
%match (list l) {
  conc(x*,e2,y*,e1,z*) -> {
    if `(e1) mod `(e2) = 0
    then raise (Result (elim(`conc(x*,e2,y*,z*))))
  } 
}; l
) with Result r -> r;;
  

let erat n = elim (List.rev(genere n)) ;;

let rec string_of_l l =
  List.fold_right (fun e -> fun r -> string_of_int e ^ " " ^ r) l "" ;;

let r = erat 1000 in
  print_string (string_of_l r ^ "\n") ;;




