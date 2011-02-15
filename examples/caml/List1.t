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
(*open TomExtension;;*)
(*open TomList ;; *)

exception Res of int list
  
%include { caml/int.tom }
%include { caml/list.tom }

%oplist list conc( int* ) {
  is_fsym(t) { true }
  make_empty()  { [] }
  make_insert(e,l) { e::l }
  get_head(l)   { List.hd l }
  get_tail(l)   { List.tl l }
  is_empty(l)   { (l=[]) }
}

let rec swapsort l = 
try (
   %match(list l) {
    conc(x1*,x,y,x2*) -> {
      if (`x>`y) then raise (Res(swapsort(`conc(x1*,y,x,x2*))))
    }

    conc(x1*,x,x,x2*) -> {
      raise (Res(swapsort(`conc(x1*,x,x2*))))
    }

  }
  ; l
)
with Res r -> r
 
 
let rec string_of_int_list = function [] -> "\n"
  | i :: r -> (string_of_int i) ^ " " ^ (string_of_int_list r);;


let l1 = [10;8;7;6;5;5;6;7;8;9];;
let l2 = swapsort(l1);;
print_string(string_of_int_list l1);;
print_string(string_of_int_list l2);;
